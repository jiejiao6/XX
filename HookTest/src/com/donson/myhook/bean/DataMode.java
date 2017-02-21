package com.donson.myhook.bean;

public class DataMode {

	String key;
	String value;
	String explain;
	boolean isChanged;

	public DataMode(String key, Object value, String explain, boolean isChanged) {
		this.key = key;
		if(value!=null){
			this.value = value.toString();
		}
		this.explain = explain;
		this.isChanged = isChanged;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	@Override
	public String toString() {
		return "DataMode [key=" + key + ", value=" + value + ", explain="
				+ explain + ", isChanged=" + isChanged + "]";
	}
}
