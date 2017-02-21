package com.donson.realparam.utils;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;

public class GetLocationUtil {
	public static Location getLocation(Activity mActivity){
		LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	}
	public static CellLocation getcellLocation(Activity mActivity){
		TelephonyManager telephonyManager = (TelephonyManager)mActivity.getSystemService(Context.TELEPHONY_SERVICE);
		CellLocation cellLocation = telephonyManager.getCellLocation();
		return cellLocation;
	}
	@SuppressLint("NewApi")
	public static List<CellInfo> getAllCellInfo(Activity mainActivity) {
		TelephonyManager tm = (TelephonyManager) mainActivity.getSystemService(Context.TELEPHONY_SERVICE);
		List<CellInfo> allCellInfo = tm.getAllCellInfo();// Call requires API
		return allCellInfo;
	}
}
