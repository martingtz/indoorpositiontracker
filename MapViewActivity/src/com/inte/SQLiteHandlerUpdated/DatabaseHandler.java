package com.inte.SQLiteHandlerUpdated;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{
	
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fingerprintsDB2.db";
    
    public static final String TABLE_MAP = "map";
    public static final String TABLE_ACCESSPOINT = "accesspoint";
    public static final String TABLE_ORIENTATION = "orientation";
    //public static final String TABLE_USERDEVICE = "userdevice";
    public static final String TABLE_POSITION = "position";
    public static final String TABLE_SCANSESSION = "scansession";
    public static final String TABLE_FINGERPRINT = "fingerprint";
    
    // map table columns names
    public static final String PK_MAP_ID = "idMap";
    public static final String COL_MAP_NAME = "name";
    public static final String COL_MAP_URL = "url";
    //private static final String COL_MAP_MAPCOL = " Mapcol ";
    
    //Access Point table column names
    public static final String PK_ACCESSPOINT_ID = "idAccessPoint";
    public static final String COL_ACCESSPOINT_SSID = "SSID";
    public static final String COL_ACCESSPOINT_BSSID = "BSSID";
    public static final String COL_ACCESSPOINT_CHANNEL = "Channel";
    
    //Orientation table column names
    public static final String PK_ORIENTATION_ID = "idOrientation";
    public static final String COL_ORIENTATION_X = "x";
    public static final String COL_ORIENTATION_Y = "y";
    public static final String COL_ORIENTATION_Z = "z";
    
    //User Device table column names
//    public static final String PK_USERDEVICE_ID = "idUserDevice";
//    public static final String COL_USERDEVICE_BSSID = "BSSID";
//    public static final String COL_USERDEVICE_NAME = "name";
    
    //Position table column names
    public static final String PK_POSITION_ID = "idPosition";
    public static final String FKEY_POSITION_MAP_ID = "Map_idMap";
    public static final String COL_POSITION_X = "posX";
    public static final String COL_POSITION_Y = "posY";
    
    //Scan Session table column names
    public static final String PK_SCANSESSION_ID = "idScanSession";
    public static final String FKEY_SCANSESSION_ORIENTATION_ID = "Orientation_idOrientation";
    public static final String FKEY_SCANSESSION_POSITION_ID = "Position_idPosition";
    public static final String FKEY_SCANSESSION_USERDEVICE_ID = "UserDevice_idUserDevice";
    public static final String COL_SCANSESSION_TIMESTAMP = "timestamp";
    
    //Finger Print table column names
    public static final String PK_FINGERPRINT_ID = "idFingerprint";
    public static final String FKEY_FINGERPRINT_SCANSESSION_ID = "ScanSession_idScanSession";
    public static final String FKEY_FINGERPRINT_ACCESSPOINT_ID = "AccessPoint_idAccessPoint";
    public static final String COL_FINGERPRINT_RSSI = "RSSI";
    
    private static final String CREATE_ACCESSPOINT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ACCESSPOINT + "("
            + PK_ACCESSPOINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_ACCESSPOINT_SSID + " TEXT DEFAULT NULL,"
            + COL_ACCESSPOINT_BSSID + " TEXT UNIQUE ON CONFLICT IGNORE," 
            + COL_ACCESSPOINT_CHANNEL + " INTEGER DEFAULT NULL" +");";
    
    private static final String CREATE_MAP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MAP + "("
            + PK_MAP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_MAP_NAME + " TEXT DEFAULT NULL,"
            + COL_MAP_URL + " TEXT DEFAULT NULL" + ");";
            // + COL_MAP_MAPCOL + " INTEGER" + ")";
    
    private static final String CREATE_ORIENTATION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ORIENTATION + "("
            + PK_ORIENTATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_ORIENTATION_X + " INTEGER DEFAULT NULL,"
            + COL_ORIENTATION_Y + " INTEGER DEFAULT NULL," 
            + COL_ORIENTATION_Z + " INTEGER DEFAULT NULL" + ");";
    
//    private static final String CREATE_USERDEVICE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERDEVICE + "("
//            + PK_USERDEVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//            + COL_USERDEVICE_BSSID + " TEXT DEFAULT NULL,"
//            + COL_USERDEVICE_NAME + " TEXT DEFAULT NULL" + ");";
    
    private static final String CREATE_POSITION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_POSITION + "("
            + PK_POSITION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_POSITION_X + " INTEGER DEFAULT NULL," 
            + COL_POSITION_Y + " INTEGER DEFAULT NULL,"
            + FKEY_POSITION_MAP_ID + " INTEGER REFERENCES "+TABLE_MAP+");";
    
    private static final String CREATE_SCANSESSION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SCANSESSION + "("
            + PK_SCANSESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_SCANSESSION_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," 
            + FKEY_SCANSESSION_ORIENTATION_ID + " INTEGER REFERENCES " +TABLE_ORIENTATION +","
            + FKEY_SCANSESSION_POSITION_ID + " INTEGER REFERENCES " +TABLE_POSITION +");";
            //+ FKEY_SCANSESSION_USERDEVICE_ID + " INTEGER REFERENCES " +TABLE_USERDEVICE+");";
    
    private static final String CREATE_FINGERPRINT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FINGERPRINT + "("
            + PK_FINGERPRINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_FINGERPRINT_RSSI + " INTEGER DEFAULT NULL,"
            + FKEY_FINGERPRINT_SCANSESSION_ID + " INTEGER REFERENCES " +TABLE_SCANSESSION +","
            + FKEY_FINGERPRINT_ACCESSPOINT_ID + " INTEGER REFERENCES " +TABLE_ACCESSPOINT +");";
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_ORIENTATION_TABLE);
		//db.execSQL(CREATE_USERDEVICE_TABLE); 
		db.execSQL(CREATE_ACCESSPOINT_TABLE);
        db.execSQL(CREATE_MAP_TABLE);                                              
        db.execSQL(CREATE_POSITION_TABLE);           
        db.execSQL(CREATE_SCANSESSION_TABLE);
        db.execSQL(CREATE_FINGERPRINT_TABLE);
        
        /*Default map insertion*/
		ContentValues mapValues = new ContentValues();
		mapValues.put(COL_MAP_NAME, "plamex");
		mapValues.put(COL_MAP_URL, "plamex.com");
		db.insert(TABLE_MAP, null, mapValues);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINGERPRINT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCANSESSION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSITION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCESSPOINT);
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDEVICE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORIENTATION);
	
		onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	    if (!db.isReadOnly()) {
	        // Enable foreign key constraints
	        db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	}
	
	public long addOrientation(Orientation orientation){
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues orientationValues = new ContentValues();
		orientationValues.put(COL_ORIENTATION_X, orientation.x);
		orientationValues.put(COL_ORIENTATION_Y, orientation.y);
		orientationValues.put(COL_ORIENTATION_Z, orientation.z);
		
		return db.insert(TABLE_ORIENTATION, null, orientationValues);
	}
	
	public long addAccessPoint(AccessPoint accessPoint){
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues AP_Values = new ContentValues();
		AP_Values.put(COL_ACCESSPOINT_SSID, accessPoint.getAp_ssid());
		AP_Values.put(COL_ACCESSPOINT_BSSID, accessPoint.getAp_bssid());
		AP_Values.put(COL_ACCESSPOINT_CHANNEL, accessPoint.getAp_chan());
		
		return db.insert(TABLE_ACCESSPOINT, null, AP_Values);
	}
	
	public long addMap(Map map){
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues mapValues = new ContentValues();
		mapValues.put(COL_MAP_NAME, map.getName());
		mapValues.put(COL_MAP_URL, map.getUrl());
		
		return db.insert(TABLE_MAP, null, mapValues);
	}
	
	public long addPosition(Position position){
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues positionValues = new ContentValues();
		positionValues.put(FKEY_POSITION_MAP_ID, position.getMap().getIdMap());
		positionValues.put(COL_POSITION_X, position.getPosX());
		positionValues.put(COL_POSITION_Y, position.getPosY());
		
		return db.insert(TABLE_POSITION, null, positionValues);
	}
	
	public long addScanSession(ScanSession scanSession){
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues scanSessionValues = new ContentValues();
		scanSessionValues.put(FKEY_SCANSESSION_ORIENTATION_ID, scanSession.getOrientation().getIdOrientation());
		scanSessionValues.put(FKEY_SCANSESSION_POSITION_ID, scanSession.getPosition().getIdPosition());
		
		long ssId = db.insert(TABLE_SCANSESSION, null, scanSessionValues);
		if(ssId != -1)
			for(FingerprintModel fModel : scanSession.getScanSession_Fingerprints()){
				addFingerprint(fModel,(int)ssId);
			}
		return ssId;
	}
	
	private long addFingerprint(FingerprintModel fingerprint, int idScanSession){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues fingerprintValues = new ContentValues();
		fingerprintValues.put(FKEY_FINGERPRINT_SCANSESSION_ID, idScanSession);
		fingerprintValues.put(FKEY_FINGERPRINT_ACCESSPOINT_ID, fingerprint.getAccessPoint().getIdAccessPoint());
		fingerprintValues.put(COL_FINGERPRINT_RSSI, fingerprint.getRSSI());
		
		return db.insert(TABLE_FINGERPRINT, null, fingerprintValues);
	}
	
	public AccessPoint getAccessPointFromDB(String BSSID){
		AccessPoint ap = new AccessPoint();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(DatabaseHandler.TABLE_ACCESSPOINT,
				new String[] { DatabaseHandler.PK_ACCESSPOINT_ID,
							   DatabaseHandler.COL_ACCESSPOINT_SSID,
							   DatabaseHandler.COL_ACCESSPOINT_BSSID,
							   DatabaseHandler.COL_ACCESSPOINT_CHANNEL},
				DatabaseHandler.COL_ACCESSPOINT_BSSID + " = ?",
			    new String[] { BSSID}, null, null, null, Integer.toString(1));
		if (cursor.getCount() > 0){
			cursor.moveToFirst();
			ap.setIdAccessPoint(Integer.parseInt(cursor.getString(0)));
			ap.setAp_ssid(cursor.getString(1));
			ap.setAp_bssid(cursor.getString(2));
			ap.setAp_chan(Integer.parseInt(cursor.getString(3)));
		}
		cursor.close();
		return ap;
	}
}
