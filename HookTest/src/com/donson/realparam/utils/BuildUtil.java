package com.donson.realparam.utils;

import java.lang.reflect.Method;

import android.os.Build;



public class BuildUtil {
	/**
	 * 
	 * @return 
	 */
	public static String getRelease() {
		return android.os.Build.VERSION.RELEASE;
	}

	public static String getRelease2() {
		return YSystemPropertiesUtils.get("ro.build.version.release");
	}

	public static String getRelease3() {
		return YSystemPropertiesUtils.get("ro.build.version.release", null);
	}
	/**
	 * 
	 * @return
	 */
	public static String getBrand() {
		return android.os.Build.BRAND;
	}

	public static String getBrand2() {
		return YSystemPropertiesUtils.get("ro.product.brand");
	}
	/**
	 * 
	 * @return 
	 */
	public static String getModel() {
		return android.os.Build.MODEL;
	}

	public static String getModel2() {
		return YSystemPropertiesUtils.get("ro.product.model");
	}
	/**
	 * 
	 * @return 
	 */
	public static String getBoard() {
		return android.os.Build.BOARD;
	}
	public static String getBoard2() {
		return YSystemPropertiesUtils.get("ro.product.board");
	}
	

	/**
	 * 
	 * @return 
	 */
	public static String getProduct() {
		return android.os.Build.PRODUCT;
	}

	public static String getProduct2() {
		return YSystemPropertiesUtils.get("ro.product.name");
	}

	public static String getCpuAbi() {
		return android.os.Build.CPU_ABI;
	}

	public static String getCpuAbi_2() {
		return YSystemPropertiesUtils.get("ro.product.cpu.abi");
	}

	public static String getCpuAbi2() {
		return android.os.Build.CPU_ABI2;
	}

	public static String getCpuAbi2_2() {
		return YSystemPropertiesUtils.get("ro.product.cpu.abi2");
	}
	public static String[] getSupported_abis() {
		return Build.VERSION.SDK_INT > 20 ? Build.SUPPORTED_ABIS : null;
	}

	public static String[] getSupported_abis2() {
		return YSystemPropertiesUtils.getStringList("ro.product.cpu.abilist", ",");
	}

	public static String[] getSupported_32_bit_abis() {
		return Build.VERSION.SDK_INT > 20 ? Build.SUPPORTED_32_BIT_ABIS :null;
	}

	public static String[] getSupported_32_bit_abis2() {
		return YSystemPropertiesUtils.getStringList("ro.product.cpu.abilist32", ",");
	}

	
	public static String[] getSupported_64_bit_abis() {
		return Build.VERSION.SDK_INT > 20 ? Build.SUPPORTED_64_BIT_ABIS : null;
	}

	/**
	 * ID/
	 * 
	 * @return
	 */
	public static String getID() {
		return android.os.Build.ID;
	}

	public static String getID2() {
		return YSystemPropertiesUtils.get("ro.build.id");
	}
	/**
	 * 
	 * @return 
	 */
	public static String getDisplay() {
		return android.os.Build.DISPLAY;
	}

	public static String getDisplay2() {
		return YSystemPropertiesUtils.get("ro.build.display.id");
	}

	/**
	 * 
	 * @return 
	 */
	public static String getFingerprint() {
		return android.os.Build.FINGERPRINT;
	}

	public static String getFingerprint3() {
		return YSystemPropertiesUtils.get("ro.build.fingerprint");
	}

	public static String getFingerprint2() {
		try {
			if (Build.VERSION.SDK_INT > 20) {
				Method deriveFingerprint = Class.forName("android.os.Build").getDeclaredMethod("deriveFingerprint");
				deriveFingerprint.setAccessible(true);
				return (String) deriveFingerprint.invoke(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public static String getCodename() {
		return android.os.Build.VERSION.CODENAME;
	}

	/**
	 * 
	 * @return 
	 */
	public static String getManufacturer() {
		return android.os.Build.MANUFACTURER;
	}

	public static String getManufacturer2() {
		return YSystemPropertiesUtils.get("ro.product.manufacturer");
	}
	/**
	 * 
	 * @return HOST/
	 */
	public static String getHost() {
		return android.os.Build.HOST;
	}

	public static String getHost2() {
		return YSystemPropertiesUtils.get("ro.build.host");
	}

	/**
	 * 
	 * @return 
	 */
	public static String getType() {
		return android.os.Build.TYPE;
	}

	public static String getType2() {
		return YSystemPropertiesUtils.get("ro.build.type");
	}
	/**
	 * 
	 * @return SDK
	 */
	public static int getSDK_INT() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 
	 * @return SDK
	 */
	public static String getSDK() {
		return android.os.Build.VERSION.SDK;
	}

	public static String getSDK2() {
		return YSystemPropertiesUtils.get("ro.build.version.sdk");
	}

	public static String getSDK3() {
		return YSystemPropertiesUtils.get("ro.build.version.sdk", null);
	}
	
	/**
	 * 
	 * @return 
	 */
	public static String getTags() {
		return android.os.Build.TAGS;
	}

	public static String getTags2() {
		return YSystemPropertiesUtils.get("ro.build.tags");
	}

	/**
	 * 
	 * @return 
	 */
	public static String getDevice() {
		return android.os.Build.DEVICE;
	}

	public static String getDevice2() {
		return YSystemPropertiesUtils.get("ro.product.device");
	}

	/**
	 * 
	 * @return
	 */
	public static String getSerial() {
		return android.os.Build.SERIAL;
	}

	public static String getSerial2() {
		return YSystemPropertiesUtils.get("ro.serialno");
	}

	public static String getBootloader() {
		return android.os.Build.BOOTLOADER;
	}
	/**
	 * 
	 * @return
	 */
	public static String getIncremental() {
		return android.os.Build.VERSION.INCREMENTAL;
	}

	public static String getRadioVersion() {
		return android.os.Build.getRadioVersion();
	}

	public static String getRadioVersion2() {
		return android.os.Build.RADIO;
	}

	public static String getRadioVersion3() {
		return YSystemPropertiesUtils.get("gsm.version.baseband");
	}
	
	public static String getHardWare() {
		return android.os.Build.HARDWARE;
	}
	public static String getHardWare2() {
		return YSystemPropertiesUtils.get("ro.hardware");
	}
	
	public static String getUser() {
		return android.os.Build.USER;
	}
	/**
	 * @return
	 */
	public static String getUser2() {
		return YSystemPropertiesUtils.get("ro.build.user");
	}
	

}
