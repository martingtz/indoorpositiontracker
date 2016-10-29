package com.inte.indoorpositiontracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import com.inte.SQLiteHandlerUpdated.AccessPoint;
import com.inte.communication.HandshakeFrame;
import com.inte.communication.ScanFrame;

import android.content.Intent;
import android.graphics.PointF;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MapViewActivity extends MapActivity {
	public static String ipAddress = "";
	public final static String EXTRA_MESSAGE_FLOOR = "com.inte.indoorpositiontracker.FLOOR";
	private static final int MENU_ITEM_EDIT_MAP = 21;
	public static final int SCAN_DELAY = 1000;
	public static final int SCAN_INTERVAL = 5000;
	public static final int MAX_SCAN_THREADS = 2;
	private static final int MAX_SORTED_SCANRESULTS = 11;
	private int mScanThreadCount = 0;
	private WifiPointView mLocationPointer;
	private static Handler sUpdateHandler = new Handler();
	private Runnable mRefreshMap = new Runnable() {
		public void run() {
			refreshMap();
		}
	};

	private boolean mPaused = false;
	private HashMap<String, Integer> mMeasurements;
	private ArrayList<AccessPoint> apListA = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread t0 = new Thread() {
			public void run() {
				/* Handshake al servidor */
				final HandshakeFrame handshakeFrame = new HandshakeFrame(getDeviceBSSID());
				try {
					Log.i("handshake", "sending Handshake: " + handshakeFrame.getCSV());
					String response = ConnectionHandler.performRequest(handshakeFrame.getCSV());
					Log.i("handshake", "Server say: " + response);
				} catch (IOException e) {
					Log.i("handshake", e.toString());
				} catch (InterruptedException e) {
					Log.i("handshake", e.toString());
				} catch (ExecutionException e) {
					Log.i("handshake", e.toString());
				}
			}
		};
		t0.start();

		apListA = new ArrayList<AccessPoint>();

		mMeasurements = new HashMap<String, Integer>();
		
		
		
		mLocationPointer = mMap.createNewWifiPointOnMap(new PointF(-1000, -1000));
		mLocationPointer.activate();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (mPaused == false) { // start scan only when this activity is
										// active
					// Esperamos 1 segundos antes de mandar el primer scanframe
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					Log.i("scanning", "Scanning ...");
					mWifi.startScan();
				}
			}

		}, SCAN_DELAY, SCAN_INTERVAL);
	}

	public void onResume() {
		super.onResume();

		mPaused = false;
	}

	public void onPause() {
		super.onPause();

		mPaused = true;
	}

	@Override
	public void onReceiveWifiScanResults(final List<ScanResult> scanResults) {
		IndoorPositionTracker application = (IndoorPositionTracker) getApplication();
		final ArrayList<Fingerprint> fingerprints = application.getFingerprintData(mSelectedMap);

		/*************************************************************************************************************/
		/*
		 * Sorting scanResuts by RSSI
		 ****************************/
		Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
			@Override
			public int compare(ScanResult lhs, ScanResult rhs) {
				return (lhs.level > rhs.level ? -1 : (lhs.level == rhs.level ? 0 : 1));
			}
		};
		Collections.sort(scanResults, comparator);
		final List<ScanResult> results = (scanResults.size() < MAX_SORTED_SCANRESULTS) ? scanResults
				: scanResults.subList(0, MAX_SORTED_SCANRESULTS);
		/*************************************************************************************************************/
		if (results.size() > 0 && fingerprints.size() > 0 && mScanThreadCount <= MAX_SCAN_THREADS) {
			Thread t = new Thread() {
				public void run() {
					mScanThreadCount++;
					ArrayList<AccessPoint> apListB = new ArrayList<AccessPoint>();
					HashMap<String, Integer> measurements = new HashMap<String, Integer>();
					HashMap<String, Integer> localMeasurements = new HashMap<String, Integer>();
					for (ScanResult result : results) {
						AccessPoint resultAP = new AccessPoint(result.SSID, result.BSSID, result.level,
								result.frequency);
						apListB.add(resultAP);
						localMeasurements.put(result.BSSID, result.level);
					}

					/**********************************************LOCAL***********************************************************************/
					TreeSet<String> keys = new TreeSet<String>();
					keys.addAll(mMeasurements.keySet());
					keys.addAll(localMeasurements.keySet());
					for (String key : keys) {
						Integer value = localMeasurements.get(key);
						Integer oldValue = mMeasurements.get(key);
						if (oldValue == null) {
							mMeasurements.put(key, value);
						} else if (value == null) {
							mMeasurements.remove(key);
						} else {
							value = (int) (oldValue * 0.4f + value * 0.6f);
							mMeasurements.put(key, value);
						}
					}

					/********************************************************************************************************************/
					if (apListA.isEmpty()) {
						apListA = apListB;
					} else {
						boolean flag;
						for (int i = 0; i < apListB.size(); i++) {
							flag = false;
							for (int j = 0; j < apListA.size(); j++) {
								if (apListB.get(i).equals(apListA.get(j))) {
									flag = true;
									apListA.get(j).setAp_rssi((int) (apListA.get(j).getAp_rssi() * 0.4f
											+ apListB.get(i).getAp_rssi() * 0.6f));
									break;
								}
							}
							if (!flag)
								apListA.add(apListB.get(i));
						}

						for (int j = 0; j < apListA.size(); j++) {
							flag = false;
							for (int i = 0; i < apListB.size(); i++) {
								if ((apListA.get(j).equals(apListB.get(i))))
									flag = true;
							}
							if (!flag)
								apListA.remove(j);
						}

					}

					for (int j = 0; j < apListA.size(); j++)
						measurements.put(apListA.get(j).getAp_bssid(), apListA.get(j).getAp_rssi());
					
					
					
					
					//Comparacion
					
					Log.i("comparacion", "----------------Mediciones 1--------------------------------");
					
					Log.i("comparacion", mMeasurements.entrySet().toString());
				
				
					Log.i("comparacion", "----------------Mediciones 2--------------------------------");
			
					Log.i("comparacion", measurements.entrySet().toString());
					
					
					
					 Fingerprint flocal = new Fingerprint(mMeasurements);
					 Fingerprint close =flocal.getClosestMatch(fingerprints);
					
					
					 Log.i("calculado", "----------------Fingerprint 1--------------------------------");
					 Log.i("calculado", close.toString());
					

					/******************************************************ENVIAR*************************************************************/
					String fingerprintResponse = null;
					/* enviarlo al server */
					final ScanFrame scanFrame = new ScanFrame(getDeviceBSSID(), 0, 0, 0, apListA.size());
					scanFrame.setApList(apListA);
					int idx = scanFrame.getCSV().lastIndexOf(',');
					if (idx != -1) {
						Log.i("scanframe", "***************sending***************");
						Log.i("scanframe", scanFrame.getCSV());
						try {

							MapViewActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(MapViewActivity.this, "Sending Scan Frame...", Toast.LENGTH_SHORT)
											.show();
								}
							});

							fingerprintResponse = ConnectionHandler.performRequest(scanFrame.getCSV());
						} catch (Exception e) {
							Log.i("scanframe", e.toString());
							e.printStackTrace();
						}
					}

					if (fingerprintResponse != null) {

						/* recibir resultado y parsearlo */
						Log.i("response", "String received: " + fingerprintResponse);

						Fingerprint closestMatch = getFingerprintFromResponse(fingerprintResponse);

						// Fingerprint f = new Fingerprint(measurements);
						// Fingerprint closestMatch =
						// f.getClosestMatch(fingerprints);
						Log.i("calculado", "----------------Fingerprint 2--------------------------------");
						Log.i("calculado", "closestmatch:" + closestMatch.toString());
						
						Log.i("closestmatch", "closestmatch:" + closestMatch.toString());
						mLocationPointer.setFingerprint(closestMatch);
						sUpdateHandler.post(mRefreshMap);

					}
					mScanThreadCount--;
				}
			};
			t.start(); // start new scan thread
		}
	}

	public Fingerprint getFingerprintFromResponse(String fingerprintResponse) {
		Fingerprint f = new Fingerprint();
		String[] array = fingerprintResponse.split(",");
		// Log.i("parser", array.toString());
		f.setId(Integer.parseInt(array[0]));
		f.setMap(array[1]);
		f.setLocation(Float.parseFloat(array[2]), Float.parseFloat(array[3]));
		Float[] orientation = new Float[3];
		orientation[0] = Float.parseFloat(array[4]);
		orientation[1] = Float.parseFloat(array[5]);
		orientation[2] = Float.parseFloat(array[6]);
		f.setOrientation(orientation);
		HashMap<String, Integer> measurements = new HashMap<String, Integer>();
		for (int i = 7; i < array.length; i += 2) {
			measurements.put(array[i], Integer.parseInt(array[i + 1]));
		}
		f.setMeasurements(measurements);
		Log.i("parsedFingerprint", "Fingerprint parseado: " + f.toString());
		return f;

	}

	public String getDeviceBSSID() {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		// Log.i("logs", wifiInfo.getSSID());
		return wifiInfo.getMacAddress();
	}

	public void startMapEditActivity() {
		Intent intent = new Intent(MapViewActivity.this, MapEditActivity.class);
		intent.putExtra(EXTRA_MESSAGE_FLOOR, mSelectedMap);
		startActivity(intent); // start map edit mode
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// add menu items
		super.onCreateOptionsMenu(menu); // items for changing map
		menu.add(Menu.NONE, MENU_ITEM_EDIT_MAP, Menu.NONE, "Edit map");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case MENU_ITEM_EDIT_MAP: // start map edit mode
			startMapEditActivity();
			return true;
		default: // change map
			return super.onOptionsItemSelected(item);
		}
	}

}
