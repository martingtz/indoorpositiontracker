package navigation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeSet;

public class Fingerprint {
	int mId;
	String mMap;
	Point2D mLocation;
	Float[] mOrientation = new Float[3];
	HashMap<String, Integer> mMeasurements;

	/** CONSTRUCTORS */

	public Fingerprint() {
		mId = 0;
		mMap = "abcdefg";
	}

	public Fingerprint(HashMap<String, Integer> measurements) {
		this();
		mMeasurements = measurements;
	}

	public Fingerprint(HashMap<String, Integer> measurements, String map) {
		this(measurements);
		mMap = map;
	}

	public Fingerprint(int id, String map, Point2D location) {
		// this();
		mId = id;
		mLocation = location;
		mMap = map;
	}

	public Fingerprint(int id, String map, Point2D location, HashMap<String, Integer> measurements) {
		this(id, map, location);
		mMeasurements = measurements;
	}

	public Fingerprint(int id, String map, Point2D location, HashMap<String, Integer> measurements,
			Float[] orientation) {
		this(id, map, location, measurements);
		mOrientation = orientation;
	}

	public void setId(int id) {
		mId = id;
	}

	public int getId() {
		return mId;
	}

	public void setMap(String map) {
		mMap = map;
	}

	public String getMap() {
		return mMap;
	}

	public void setLocation(Point2D location) {
		mLocation = location;
	}

	public void setLocation(float x, float y) {
		mLocation = new Point2D.Float(x, y);
	}

	public Point2D getLocation() {
		return mLocation;
	}

	public void setMeasurements(HashMap<String, Integer> measurements) {
		mMeasurements = measurements;
	}

	public HashMap<String, Integer> getMeasurements() {
		return mMeasurements;
	}

	/** calculates the (squared) euclidean distance to the given fingerprint */
	public float compare(Fingerprint fingerprint) {
		float result = 0f;

		HashMap<String, Integer> fingerprintMeasurements = fingerprint.getMeasurements();
		TreeSet<String> keys = new TreeSet<String>();
		keys.addAll(mMeasurements.keySet());
		keys.addAll(fingerprintMeasurements.keySet());

		for (String key : keys) {
			int value = 0;
			Integer fValue = fingerprintMeasurements.get(key);
			Integer mValue = mMeasurements.get(key);
			value = (fValue == null) ? -119 : (int) fValue;
			value -= (mValue == null) ? -119 : (int) mValue;
			result += value * value;
		}

		// result = FloatMath.sqrt(result); // squared euclidean distance is
		// enough, this is not needed

		return result;
	}

	/**
	 * compares the fingerprint to a set of fingerprints and returns the
	 * fingerprint with the smallest euclidean distance to it
	 */
	public Fingerprint getClosestMatch(ArrayList<Fingerprint> fingerprints) {
		// long time = System.currentTimeMillis();
		Fingerprint closest = null;
		float bestScore = -1;

		if (fingerprints != null) {
			for (Fingerprint fingerprint : fingerprints) {
				float score = compare(fingerprint);
				if (bestScore == -1 || bestScore > score) {
					bestScore = score;
					closest = fingerprint;
				}
			}
		}
		return closest;
	}

	public Float[] getOrientation() {
		return mOrientation;
	}

	public void setOrientation(Float[] orientation) {
		this.mOrientation = orientation;
	}

	@Override
	public String toString() {
		return "Fingerprint [mId=" + mId + ", mMap=" + mMap + ", mLocation=" + mLocation + ", mOrientation="
				+ Arrays.toString(mOrientation) + ", mMeasurements=" + mMeasurements + "]\n";
	}

	public String getCSV() {
		String csv = mId + "," + mMap + "," + mLocation.getX() + "," + mLocation.getY() + "," + mOrientation[0] + ","
				+ mOrientation[1] + "," + mOrientation[2];

		Iterator<Entry<String, Integer>> it = mMeasurements.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Integer> e = it.next();
			// System.out.println(e.getKey() + " " + e.getValue());
			csv += "," + e.getKey() + "," + e.getValue();
		}

		return csv;
	}

}
