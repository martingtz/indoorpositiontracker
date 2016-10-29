package com.inte.SQLiteHandlerUpdated;

import java.util.ArrayList;

public class ScanSession {
	
	private int idScanSession;
	private Orientation orientation;
	private Position position;
	private ArrayList<FingerprintModel> fingerprintsList;

	public ScanSession(){
		fingerprintsList = new ArrayList<FingerprintModel>();
	}
	
	public ScanSession(Orientation orientation, Position position) {
		this();
		this.orientation = orientation;
		this.position = position;
	}

	public int getIdScanSession() {
		return idScanSession;
	}

	public void setIdScanSession(int idScanSession) {
		this.idScanSession = idScanSession;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	public ArrayList<FingerprintModel> getScanSession_Fingerprints(){
		return fingerprintsList;
	}
	
	public void addFingerprintInfo(FingerprintModel fingerprint) {
		fingerprintsList.add(fingerprint);
	}

	@Override
	public String toString() {
		return "ScanSession [idScanSession=" + idScanSession + ", fingerprintsList=" + fingerprintsList
				+ ", orientation=" + orientation + ", position=" + position + "]";
	}
}
