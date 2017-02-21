package com.donson.realparam.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.donson.config.Logger;
import com.donson.myhook.bean.WifiMode2;
import com.google.gson.Gson;
import com.param.bean.WifiMode;

public class WifiUtil {
	public static WifiMode2 getWifiMode(Context context) {
		WifiMode2 wifiMode2 = new WifiMode2();
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		wifiMode2.setSsid(wifiInfo.getSSID());
		wifiMode2.setBssid(wifiInfo.getBSSID());
		wifiMode2.setMacAddress(wifiInfo.getMacAddress());
		wifiMode2.setLinkSpeed(wifiInfo.getLinkSpeed());
		wifiMode2.setRssi(wifiInfo.getRssi());
		List<ScanResult> scanResults = wifiManager.getScanResults();
		ArrayList<WifiMode> wifiLists = new ArrayList<WifiMode>();
		for(ScanResult scanResult:scanResults){
			WifiMode wifiMode = new WifiMode();
			wifiMode.setBssid(scanResult.BSSID);
			wifiMode.setSsid(scanResult.SSID);
			wifiMode.setCapabilities(scanResult.capabilities);
			wifiMode.setFrequency(scanResult.frequency);
			wifiMode.setLevel(scanResult.level);
			wifiLists.add(wifiMode);
		}
		if(wifiLists!=null&&wifiLists.size()>0){
			Gson gson = new Gson();
			String wifilist = gson.toJson(wifiLists);
			wifiMode2.setWifilist(wifilist);
		}
		return wifiMode2;
	}
	
	/**
	 * 1. wifiManager.WIFI_STATE_DISABLED (1)  
		2. wifiManager..WIFI_STATE_ENABLED (3)   
		3. wifiManager..WIFI_STATE_DISABLING (0)
	4 wifiManager..WIFI_STATE_ENABLING  (2)
	 * 
	 * @param context
	 * @return
	 */
	public static String isWifiEnabled(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		int wifiState = wifiManager.getWifiState();
		Logger.i("wifiState:::::"+wifiState);
		return wifiManager.isWifiEnabled()+" wifi state "+wifiState;

	}


}
