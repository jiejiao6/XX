package com.donson.realparam.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.donson.config.Logger;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class OtherUtil {
	/*
	 */
	public static String getCpuFrequency() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if(!TextUtils.isEmpty(result)){
			int a = Integer.parseInt(result.trim());
			int b = a / (1000 * 1000);
			int c = (int) ((((a + (0.5 * 1000 * 100)) / (1000 * 100))) % 10);
			return b + "." + c + "GHz";
		}else {
			return "";
		}
	}
	/**
	 * @return
	 */
	public static String getBluetoothMac() {
		String btMac ="";
		BluetoothAdapter bAdapt = BluetoothAdapter.getDefaultAdapter();
		if (bAdapt != null) {
			btMac = bAdapt.getAddress();
		}
		return btMac;
	}
	/**
	 * ��ȡ�������
	 * @return
	 */
	public static String getBluetoothName() {
		String btMac ="";
		BluetoothAdapter bAdapt = BluetoothAdapter.getDefaultAdapter();
		if (bAdapt != null) {
			btMac = bAdapt.getName();
		}
		return btMac;
	}
	/**
	 * INNER-VER �ں˰汾 return String
	 * 
	 * @return
	 */
	public static String getkernelVersion() {

		Process process = null;
		String kernelVersion = "";
		try {
			process = Runtime.getRuntime().exec("cat /proc/version");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// get the output line
		InputStream outs = process.getInputStream();
		InputStreamReader isrout = new InputStreamReader(outs);
		BufferedReader brout = new BufferedReader(isrout, 8 * 1024);

		String result = "";
		String line;
		// get the whole standard output string
		try {
			while ((line = brout.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (result != "") {
				String Keyword = "version ";
				int index = result.indexOf(Keyword);
				line = result.substring(index + Keyword.length());
				index = line.indexOf(" ");
				kernelVersion = line.substring(0, index);
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return kernelVersion;
	}
	public static String getGsfAndroidId(Context context) 
    {
        Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
        String ID_KEY = "android_id";
        String params[] = {ID_KEY};
        
        Cursor c = context.getContentResolver().query(URI, null, null, params, null);
        if (c==null||(!c.moveToFirst() || c.getColumnCount() < 2))
            return null;
        try 
        {
            return Long.toHexString(Long.parseLong(c.getString(1)));
        } 
        catch (NumberFormatException e) 
        {
            return null;
        }
    }
}
