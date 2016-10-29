package com.inte.SQLiteHandlerUpdated;

public class FingerprintModel {
	
	private int idFingerprint;
	private ScanSession scanSession;
	private AccessPoint accessPoint;
	private int RSSI;
	
	public FingerprintModel(AccessPoint accessPoint, int rSSI) {
		this.setAccessPoint(accessPoint);
		RSSI = rSSI;
	}
	
	public FingerprintModel(){
		super();
	}
	
	public int getIdFingerprint() {
		return idFingerprint;
	}
	
	public void setIdFingerprint(int idFingerprint) {
		this.idFingerprint = idFingerprint;
	}
	
	public int getRSSI() {
		return RSSI;
	}
	
	public void setRSSI(int rSSI) {
		RSSI = rSSI;
	}
	
	public ScanSession getScanSession() {
		return scanSession;
	}
	
	public void setScanSession(ScanSession scanSession) {
		this.scanSession = scanSession;
	}
	
	public AccessPoint getAccessPoint() {
		return accessPoint;
	}
	
	public void setAccessPoint(AccessPoint accessPoint) {
		this.accessPoint = accessPoint;
	}
	
	@Override
	public String toString() {
		return "FingerprintModel [idFingerprint=" + idFingerprint + ", scanSession=" + scanSession + ", accessPoint="
				+ accessPoint + ", RSSI=" + RSSI + "]";
	}
}
