package com.inte.indoorpositiontracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import com.inte.SQLiteHandlerUpdated.AccessPoint;
import com.inte.communication.FingerprintFrame;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

public class MapEditActivity extends MapActivity implements SensorEventListener {
	private static final int MENU_ITEM_SCAN = 31;
	private static final int MENU_ITEM_FINGERPRINTS = 32;
	private static final int MENU_ITEM_SHOW_FINGERPRINTS = 33;
	private static final int MENU_ITEM_DELETE_FINGERPRINTS = 34;
	private static final int MENU_ITEM_EXPORT_FINGERPRINTS = 35;
	private static final int MENU_ITEM_IMPORT_FINGERPRINTS = 36;
	private static final int MENU_ITEM_EXIT = 37;
	private static final int MAX_SORTED_SCANRESULTS = 11;
	private static final int MIN_SCAN_COUNT = 3;
	private static final int SCAN_COUNT = 3;
	private static final int SCAN_INTERVAL = 500;
	private static final int BASE_CHANNEL_FREQ = 2412;
	private int mScansLeft = 0;
	private WifiPointView mPointer;
	private long mTouchStarted;
	private ProgressDialog mLoadingDialog;
	private HashMap<String, Integer> mMeasurements;
	private ArrayList<AccessPoint> apListA = null;
	private boolean mShowFingerprints = true;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	Float[] mOrientation = new Float[3];

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String floor = intent.getStringExtra(MapViewActivity.EXTRA_MESSAGE_FLOOR);
		setMap(Integer.valueOf(floor));
		setTitle(getTitle() + " (edit mode)");
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	}

	@Override
	public void onReceiveWifiScanResults(List<ScanResult> scanResults) {
		if (mScansLeft != 0 && mPointer != null) {
			/**************************************************************************************************************************************/
			Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
				@Override
				public int compare(ScanResult lhs, ScanResult rhs) {
					return (lhs.level > rhs.level ? -1 : (lhs.level == rhs.level ? 0 : 1));
				}
			};
			Collections.sort(scanResults, comparator);
			List<ScanResult> results = (scanResults.size() < MAX_SORTED_SCANRESULTS) ? scanResults
					: scanResults.subList(0, MAX_SORTED_SCANRESULTS);
			/**************************************************************************************************************************************/
			if (results.size() >= MIN_SCAN_COUNT) {
				mScansLeft--;
				// add scan results to hashmap
				ArrayList<AccessPoint> apListB = new ArrayList<AccessPoint>();
				HashMap<String, Integer> measurements = new HashMap<String, Integer>();
				HashMap<String, Integer> measurementsLocal = new HashMap<String, Integer>();
				for (ScanResult result : results) {
					AccessPoint resultAP = new AccessPoint(result.SSID, result.BSSID, result.level,
							(result.frequency - BASE_CHANNEL_FREQ) / 5 + 1);
					apListB.add(resultAP);
					measurementsLocal.put(result.BSSID, result.level);
					// mApplication.getmDatabase().addAccessPoint(resultAP); //

				}
				/******************************************** LOCAL ***********************************************************************/

				TreeSet<String> keys = new TreeSet<String>();
				keys.addAll(measurementsLocal.keySet());
				keys.addAll(mMeasurements.keySet());
				for (String key : keys) {
					Integer value = measurementsLocal.get(key);
					Integer oldValue = mMeasurements.get(key);
					if (oldValue == null) {
						mMeasurements.put(key, value + (-119 * (SCAN_COUNT - 1 - mScansLeft)));
					} else if (value == null) {
						mMeasurements.put(key, -119 + oldValue);
					} else {
						mMeasurements.put(key, value + oldValue);
					}
				}
			
				/********************************************************************************************************************/
				if (apListA == null) {
					for (int i = 0; i < apListB.size(); i++)
						apListB.get(i).setAp_rssi(apListB.get(i).getAp_rssi() + (-119 * (SCAN_COUNT - 1 - mScansLeft)));
					apListA = apListB;
				} else {
					boolean flag;
					for (int i = 0; i < apListB.size(); i++) {
						flag = false;
						for (int j = 0; j < apListA.size(); j++) {
							if (apListB.get(i).equals(apListA.get(j))) {
								flag = true;
								apListA.get(j).setAp_rssi(apListA.get(j).getAp_rssi() + apListB.get(i).getAp_rssi());
								break;
							}
						}
						if (!flag) {

							apListB.get(i)
									.setAp_rssi(apListB.get(i).getAp_rssi() + (-119 * (SCAN_COUNT - 1 - mScansLeft)));
							apListA.add(apListB.get(i));
						}
					}

					for (int j = 0; j < apListA.size(); j++) {
						flag = false;
						for (int i = 0; i < apListB.size(); i++) {
							if ((apListA.get(j).equals(apListB.get(i))))
								flag = true;
						}
						if (!flag)
							apListA.get(j).setAp_rssi(apListA.get(j).getAp_rssi() - 119);
					}

				}
				/********************************************************************************************************************/

				if (mScansLeft > 0) { // keep on scanning
					scanNext();
				} else {
					
					for (int j = 0; j < apListA.size(); j++) {
						apListA.get(j).setAp_rssi(apListA.get(j).getAp_rssi() / SCAN_COUNT);
						measurements.put(apListA.get(j).getAp_bssid(), apListA.get(j).getAp_rssi());
						
					}
					
					
					
					 for (String key : mMeasurements.keySet()) {
	                        int value = (int) mMeasurements.get(key) / SCAN_COUNT;
	                        mMeasurements.put(key, value);}
					
					
					
					
					Log.i("comparacion", "----------------Mediciones 1--------------------------------");
			
						Log.i("comparacion", mMeasurements.entrySet().toString());
					
					
					Log.i("comparacion", "----------------Mediciones 2--------------------------------");
				
						Log.i("comparacion", measurements.entrySet().toString());
					
					
					
					
					
					


					Fingerprint f = new Fingerprint(measurements, mSelectedMap);
					FingerprintFrame fingerPrintFrame = new FingerprintFrame(getDeviceBSSID(), 0, 0, 0,
							mPointer.getLocation().x, mPointer.getLocation().y, apListA.size());

					fingerPrintFrame.setApList(apListA);

					if (fingerPrintFrame.getCSV().lastIndexOf(',') != -1) {
						try {
							Log.i("fingerprintframe", "***************sending***************");
							Log.i("fingerprintframe", fingerPrintFrame.getCSV());
							ConnectionHandler.performRequest(fingerPrintFrame.getCSV());
						} catch (Exception e) {
							Log.i("fingerprintframe", e.getMessage());
						}
					}

					f.setLocation(mPointer.getLocation());
					f.setOrientation(mOrientation);
					mMap.createNewWifiPointOnMap(f, mShowFingerprints);
					mApplication.addFingerprint(f);
					mLoadingDialog.dismiss();
				}
			} else {
				mLoadingDialog.dismiss();
				Toast.makeText(getApplicationContext(),
						"Failed to create fingerprint. Could not find enough access points (found " + results.size()
								+ ", need at least " + MIN_SCAN_COUNT + ").",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public boolean onTouch(View v, MotionEvent event) {
		v.onTouchEvent(event); // handle map etc touch events

		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mTouchStarted = event.getEventTime(); // calculate tap start
			break;
		case MotionEvent.ACTION_UP:
			if (event.getEventTime() - mTouchStarted < 150) { // user tapped the
																// screen
				PointF location = new PointF(event.getX(), event.getY()); // get
																			// touch
																			// location

				// add pointer on screen where the user tapped and start wifi
				// scan
				if (mPointer == null) {
					mPointer = mMap.createNewWifiPointOnMap(location);
					mPointer.activate();
				} else {
					mMap.setWifiPointViewPosition(mPointer, location);
				}
				refreshMap(); // redraw map
			}
			break;
		}

		return true; // indicate event was handled
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// add menu items
		menu.add(Menu.NONE, MENU_ITEM_SCAN, Menu.NONE, "Scan");

		SubMenu sub = menu.addSubMenu(Menu.NONE, MENU_ITEM_FINGERPRINTS, Menu.NONE, "Fingerprints");
		sub.add(Menu.NONE, MENU_ITEM_SHOW_FINGERPRINTS, Menu.NONE,
				(mShowFingerprints ? "Hide fingerprints" : "Show fingerprints"));
		sub.add(Menu.NONE, MENU_ITEM_DELETE_FINGERPRINTS, Menu.NONE, "Delete all fingerprints");
		sub.add(Menu.NONE, MENU_ITEM_EXPORT_FINGERPRINTS, Menu.NONE, "Export DB");
		sub.add(Menu.NONE, MENU_ITEM_IMPORT_FINGERPRINTS, Menu.NONE, "Import DB");
		menu.add(Menu.NONE, MENU_ITEM_EXIT, Menu.NONE, "Exit edit mode");
		super.onCreateOptionsMenu(menu); // items for changing map
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case MENU_ITEM_SCAN: // start scan
			if (mPointer == null) {
				// notify user that UI pointer must be positioned first before
				// the scan can be started
				Toast.makeText(getApplicationContext(),
						"Failed to start scan. Tap on the map first" + " to place the position marker.",
						Toast.LENGTH_LONG).show();
			} else {
				startScan(); // show loading dialog and start wifi scan
			}
			return true;
		case MENU_ITEM_SHOW_FINGERPRINTS: // show/hide fingerprints
			setFingerprintVisibility(!mShowFingerprints);
			item.setTitle(mShowFingerprints ? "Hide fingerprints" : "Show fingerprints");
			return true;
		case MENU_ITEM_DELETE_FINGERPRINTS: // delete all fingerprints (from
											// screen and database)
			deleteAllFingerprints();
			return true;
		case MENU_ITEM_EXPORT_FINGERPRINTS: // delete all fingerprints (from
											// screen and database)
			exportDB();
			return true;
		case MENU_ITEM_IMPORT_FINGERPRINTS: // delete all fingerprints (from
											// screen and database)
			importDB();
			return true;
		case MENU_ITEM_EXIT: // exit edit mode
			finish();
			return true;
		default: // change map
			return super.onOptionsItemSelected(item);
		}
	}

	public String getDeviceBSSID() {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		// Log.i("logs", wifiInfo.getSSID());
		return wifiInfo.getMacAddress();
	}

	public void startScan() {
		mScansLeft = SCAN_COUNT;
		mMeasurements = new HashMap<String, Integer>();
		apListA = null;
		mLoadingDialog = ProgressDialog.show(this, "", "Scanning..", true); // show
																			// loading
																			// bar
		mWifi.startScan();
	}

	public void scanNext() {
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				mWifi.startScan();
			}

		}, SCAN_INTERVAL);
	}

	public void setFingerprintVisibility(boolean visible) {
		mShowFingerprints = visible;
		mMap.setWifiPointsVisibility(visible);

		if (mPointer != null) {
			mPointer.setVisible(true); // pointer is always visible
		}

		refreshMap(); // redraw map
	}

	public void deleteAllFingerprints() {
		// create alert dialog and delete all fingerprints only after user has
		// confirmed he wants to delete them
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Are you sure?");
		alertDialogBuilder.setMessage("Are sure you want to delete all fingerprints?");

		// add yes button to dialog
		alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mMap.deleteFingerprints(); // delete fingerprints from the
											// screen
				mApplication.deleteAllFingerprints(mSelectedMap); // delete
																	// fingerprints
																	// from the
																	// database
			}
		});

		// add no button to dialog
		alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel(); // close the dialog and do nothing
			}
		});

		// create the dialog and show it
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void exportDB() {
		Toast.makeText(this, "DB Exported!", Toast.LENGTH_SHORT).show();
		mApplication.exportDB(this.getApplicationContext());
	}

	public void importDB() {
		Toast.makeText(this, "DB Imported!", Toast.LENGTH_SHORT).show();
		mApplication.importDB(this.getApplicationContext());
	}

	@Override
	public void setMap(int resId) {
		super.setMap(resId);
		mMap.deleteFingerprints(); // clear screen from fingerprints

		ArrayList<Fingerprint> fingerprints = mApplication.getFingerprintData(mSelectedMap); // load
																								// fingerprints
																								// from
																								// the
																								// database

		// add WifiPointViews on map with fingerprint data loaded from the
		// database
		for (Fingerprint fingerprint : fingerprints) {
			mMap.createNewWifiPointOnMap(fingerprint, mShowFingerprints);
		}
		System.out.println("\nNumber of fingerprints:" + fingerprints.size() + "\n");
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public void onSensorChanged(SensorEvent event) {
		mOrientation[0] = event.values[0];
		mOrientation[1] = event.values[1];
		mOrientation[2] = event.values[2];
	}

}
