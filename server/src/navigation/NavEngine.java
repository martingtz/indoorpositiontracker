package navigation;

import java.util.ArrayList;
import storage.DatabaseManager;
import storage.QueryHandler;

public class NavEngine {
    private ArrayList<Fingerprint> mFingerprints;
    private DatabaseManager mFingerprintDatabaseHandler;
    private QueryHandler mQueryHandler;
    
    public NavEngine(DatabaseManager databaseManager){
        mFingerprints = new ArrayList<Fingerprint>();
        mFingerprintDatabaseHandler = databaseManager;
        mQueryHandler = new QueryHandler(databaseManager);
        loadFingerprintsFromDatabase();
    }
    
	public void loadFingerprintsFromDatabase() {
        mFingerprints = mQueryHandler.getAllFingerprints(); // fetch fingerprint data from the database
    }
    
    public ArrayList<Fingerprint> getFingerprintData() {
        return mFingerprints;
    }
    
    public ArrayList<Fingerprint> getFingerprintData(String map) {
        ArrayList<Fingerprint> fingerprints = new ArrayList<Fingerprint>();
        for(Fingerprint fingerprint : mFingerprints) {
            if(fingerprint.getMap().compareTo(map) == 0) {
                fingerprints.add(fingerprint);
            }
        }
        
        return fingerprints;
    }
}
