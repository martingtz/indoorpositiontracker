package com.inte.SQLiteHandlerUpdated;

public class Map {
	
	private int idMap;
	private String name;
	private String url;
	
	public Map(){
		super();
	}
	
	public Map(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public int getIdMap() {
		return idMap;
	}

	public void setIdMap(int idMap) {
		this.idMap = idMap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return "Map [idMap=" + idMap + ", name=" + name + ", url=" + url + "]";
	}
}
