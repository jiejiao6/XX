package com.donson.realparam.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.donson.config.Logger;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
/**
 * 
 * @author mz
 *
 */
public class InnerIPUtil {
	public static String getInnerIP(Context context) {
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		if(wifiManager.isWifiEnabled()){
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			String ip =ipAddress+"::"+ intToIp(ipAddress);
			return ip;
		} else {
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
						.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							String ip = inetAddress.getHostAddress().toString();
							return ip;
						}
					}
				}
			} catch (SocketException ex) {

			}
		}
		return null;
	}
	public static String intToIp(long i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}

	public static long ip2int(String ip) {
		String[] items = ip.split("\\.");
		return Long.valueOf(items[3]) << 24 | Long.valueOf(items[2]) << 16 | Long.valueOf(items[1]) << 8
				| Long.valueOf(items[0]);
	}

}
