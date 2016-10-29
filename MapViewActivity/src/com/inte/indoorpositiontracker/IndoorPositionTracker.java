package com.inte.indoorpositiontracker;

import java.util.ArrayList;

import com.inte.SQLiteHandlerUpdated.DatabaseHandler;
import com.inte.SQLiteHandlerUpdated.Map;

import android.app.Application;
import android.content.Context;

public class IndoorPositionTracker extends Application {
	private ArrayList<Fingerprint> mFingerprints;
	private FingerprintDatabaseHandler mFingerprintDatabaseHandler;
	private DatabaseHandler mDatabase;
	public static Map defaultMap; /* default map id = 1 */

	/** INSTANCE METHODS */

	@Override
	public void onCreate() {
		super.onCreate();
		mFingerprints = new ArrayList<Fingerprint>();
		mFingerprintDatabaseHandler = new FingerprintDatabaseHandler(this);
		loadFingerprintsFromDatabase();
		// mDatabase = new com.inte.SQLiteHandlerUpdated.DatabaseHandler(this);
		defaultMap = new Map("plamex", "plamex.com");
		defaultMap.setIdMap(1);
	}

	public void loadFingerprintsFromDatabase() {
		mFingerprints = mFingerprintDatabaseHandler.getAllFingerprints(); // fetch
																			// fingerprint
																			// data
																			// from
																			// the
																			// database
	}

	public ArrayList<Fingerprint> getFingerprintData() {
		return mFingerprints;
	}

	public ArrayList<Fingerprint> getFingerprintData(String map) {
		ArrayList<Fingerprint> fingerprints = new ArrayList<Fingerprint>();
		for (Fingerprint fingerprint : mFingerprints) {
			if (fingerprint.getMap().compareTo(map) == 0) {
				fingerprints.add(fingerprint);
			}
		}

		return fingerprints;
	}

	public void addFingerprint(Fingerprint fingerprint) {
		mFingerprints.add(fingerprint); // add to fingerprint arraylist
		mFingerprintDatabaseHandler.addFingerprint(fingerprint); // add to
																	// database
	}

	public void exportDB(Context myContext) {
		mFingerprintDatabaseHandler.exportDB(myContext); // delete all
															// fingerprints from
															// database
	}

	public void importDB(Context myContext) {
		mFingerprintDatabaseHandler.importDB(myContext);// delete all
														// fingerprints from
														// database
	}

	public void deleteAllFingerprints() {
		mFingerprints.clear(); // delete all fingerprints from arraylist
		mFingerprintDatabaseHandler.deleteAllFingerprints(); // delete all
																// fingerprints
																// from database
	}

	public void deleteAllFingerprints(String map) {
		ArrayList<Fingerprint> itemsToRemove = new ArrayList<Fingerprint>();

		// collect fingerprints that need to be deleted
		for (Fingerprint fingerprint : mFingerprints) {
			if (fingerprint.getMap().compareTo(map) == 0) {
				itemsToRemove.add(fingerprint);
			}
		}

		// delete collected fingerprints
		for (Fingerprint fingerprint : itemsToRemove) {
			mFingerprintDatabaseHandler.deleteFingerprint(fingerprint); // delete
																		// from
																		// database
			mFingerprints.remove(fingerprint); // delete from arraylist
		}
	}

	/*
	 * Access to database search and insert methods
	 */

	public DatabaseHandler getmDatabase() {
		return mDatabase;
	}
}
