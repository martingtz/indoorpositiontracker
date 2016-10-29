package com.inte.SQLiteHandlerUpdated;

public class Position {
	
	private int idPosition;
	private Map map;
	private int posX;
	private int posY;
	
	public Position(Map map, int posX, int posY) {
		this.map = map;
		this.posX = posX;
		this.posY = posY;
	}
	
	public Position(){
		super();
	}

	public int getIdPosition() {
		return idPosition;
	}

	public void setIdPosition(int idPosition) {
		this.idPosition = idPosition;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}
	
	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public Map getMap() {
		return map;
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
	
	@Override
	public String toString() {
		return "Position [idPosition=" + idPosition + ", map=" + map + ", posX=" + posX + ", posY=" + posY + "]";
	}
}
