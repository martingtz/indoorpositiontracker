package com.inte.communication;

public interface Frame {
	/**
	 * This method returns the ID of the type of frame.
	 * 
	 * @return String the frame ID.
	 */
	public String getID();
	/**
	 * This method returns the CSV formated string of the type of frame.
	 * 
	 * @return String CSV formated string.
	 */
	public String getCSV();
}
