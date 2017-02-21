package com.donson.myhook.bean;

public class WifiMode2 {
	/**
	 * SSID/网络的名称
	 */
	public String ssid;
	/**
	 * BSSID/接入点地址
	 */
	public String wifilist;
	public String bssid;
	public String macAddress;
	public int linkSpeed;
	/**
	 * Rssi/无线网络的接收信号强度
	 */
	public int rssi;
	
	public int getLinkSpeed() {
		return linkSpeed;
	}
	public void setLinkSpeed(int linkSpeed) {
		this.linkSpeed = linkSpeed;
	}
	public int getRssi() {
		return rssi;
	}
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getWifilist() {
		return wifilist;
	}
	public void setWifilist(String wifilist) {
		this.wifilist = wifilist;
	}
	public String getBssid() {
		return bssid;
	}
	public void setBssid(String bssid) {
		this.bssid = bssid;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	@Override
	public String toString() {
		return "WifiMode2 [ssid=" + ssid + ", wifilist=" + wifilist
				+ ", bssid=" + bssid + ", macAddress=" + macAddress
				+ ", linkSpeed=" + linkSpeed + ", rssi=" + rssi + "]";
	}
	
}
