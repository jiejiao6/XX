package Xposed;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.realparam.utils.ConvertUtil;
import com.donson.utils.SPrefHookUtil;
import com.param.bean.ParamEntity;
import com.param.controller.CommonParamHelper;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;

public class ParamHook extends XHook {
	private static ParamHook instance;
	ParamEntity paramEntity;
	XSharedPreferences mXSPrefSetting;
	private ParamHook(ParamEntity paramEntity,XSharedPreferences mXSPrefSetting) {
		this.paramEntity = paramEntity;
		this.mXSPrefSetting = mXSPrefSetting;
	}
	public static ParamHook getInstance(ParamEntity paramEntity, XSharedPreferences mXSPrefSetting){
		if(instance==null){
			synchronized (ParamHook.class) {
				if(instance==null){
					instance = new ParamHook(paramEntity, mXSPrefSetting);
				}
			}
		}
		return instance;
	}
	public void setParamEntity(ParamEntity paramEntity){
		this.paramEntity = paramEntity;
	}

	@Override
	protected void handleMethod(String packageName,ClassLoader classLoader)throws Exception {
		int realsdk = Integer.valueOf(Build.VERSION.SDK);
		HookMethod(TelephonyManager.class, "getDeviceId", packageName,MethodInt.GET_IMEI_1);
//		Logger.m("realsdk==="+realsdk);
//		if(realsdk>20){
//			HookMethod(TelephonyManager.class, "getImei", packageName,MethodInt.GET_IMEI_2);
//		}
		Logger.h("==================handleMethod==============");
		try{
			 SystemPropertiesHook systemPropertiesHook = new SystemPropertiesHook(paramEntity);
			 XposedHelpers.findAndHookMethod("android.os.SystemProperties", classLoader, "get", String.class, String.class, systemPropertiesHook);
			HookMethod(LocationManager.class, "getLastKnownLocation",packageName, MethodInt.GET_LAST_KNOWN_LOCATION,String.class);
			HookMethod(Location.class, "getLongitude", packageName,MethodInt.GET_LONGITUDE);
			HookMethod(Location.class, "getLatitude", packageName,MethodInt.GET_LATITUDE);
			HookMethod(CdmaCellLocation.class, "getBaseStationLongitude", packageName, MethodInt.GET_LONGITUDE);
			HookMethod(CdmaCellLocation.class, "getBaseStationLatitude", packageName, MethodInt.GET_LATITUDE);
			HookMethod(CdmaCellLocation.class, "getBaseStationId", packageName,MethodInt.GET_CELL_ID);
			HookMethod(CdmaCellLocation.class, "getNetworkId", packageName,MethodInt.GET_CELL_LAC);
	        
			HookMethod(GsmCellLocation.class, "getCid", packageName,MethodInt.GET_CELL_ID);
			HookMethod(GsmCellLocation.class, "getLac", packageName,MethodInt.GET_CELL_LAC);
			
			HookMethod(CellIdentityGsm.class, "getMcc", packageName, MethodInt.GET_CELL_MCC);//---
	        HookMethod(CellIdentityGsm.class, "getMnc", packageName, MethodInt.GET_CELL_MNC);//---
	        HookMethod(CellIdentityGsm.class, "getCid", packageName, MethodInt.GET_CELL_ID);
	        HookMethod(CellIdentityGsm.class, "getLac", packageName, MethodInt.GET_CELL_LAC);
	        
	        HookMethod(CellIdentityCdma.class, "getLatitude", packageName, MethodInt.GET_LATITUDE_CDMA);//---
	        HookMethod(CellIdentityCdma.class, "getLongitude", packageName, MethodInt.GET_LONGITUDE_CDMA);//---
	        HookMethod(CellIdentityCdma.class, "getNetworkId", packageName, MethodInt.GET_CELL_ID);//---
	        HookMethod(CellIdentityCdma.class, "getBasestationId", packageName, MethodInt.GET_CELL_LAC);//---
	        HookMethod(CellIdentityCdma.class, "getSystemId", packageName, MethodInt.GET_SYSTEM_ID_CDMA);//---
	        Logger.h("==================getSystemId==============");
	        HookMethod(CellIdentityLte.class, "getMcc", packageName, MethodInt.GET_CELL_MCC);//--
	        HookMethod(CellIdentityLte.class, "getMnc", packageName, MethodInt.GET_CELL_MNC);//--
	        
	        HookMethod(CellIdentityLte.class, "getCi", packageName, MethodInt.GET_CELL_ID);//--
	        HookMethod(CellIdentityLte.class, "getTac", packageName, MethodInt.GET_CELL_LAC);//--
	        
	        HookMethod(CellIdentityWcdma.class, "getMcc", packageName, MethodInt.GET_CELL_MCC);
	        HookMethod(CellIdentityWcdma.class, "getMnc", packageName, MethodInt.GET_CELL_MNC);
	        HookMethod(CellIdentityWcdma.class, "getCid", packageName, MethodInt.GET_CELL_ID);
	        HookMethod(CellIdentityWcdma.class, "getLac", packageName, MethodInt.GET_CELL_LAC);
	        HookMethod(TelephonyManager.class, "getCellLocation", packageName,MethodInt.GET_CELL_LOCATION);
		}catch(Exception e){
			Logger.h("EX==="+e);
		}
		Logger.h("==================getIpAddress==============");
		////////////////////////////////////////////////////////
        
		HookMethod(WifiInfo.class, "getIpAddress", packageName,MethodInt.GET_INNER_IP_INT);
//		HookMethod(InetAddress.class, "getHostAddress", packageName,MethodInt.GET_INNER_IP_STR);
		Logger.h("==================hasIccCard==============");

		HookMethod(TelephonyManager.class, "hasIccCard", packageName, MethodInt.GET_HAS_ICC_CARD);
		HookMethod(TelephonyManager.class, "getSimState", packageName, MethodInt.GET_SIM_STATE);
        HookMethod(TelephonyManager.class, "getNetworkOperator", packageName, MethodInt.GET_NETWORK_OPERATOR);
        HookMethod(WifiManager.class, "getWifiState", packageName, MethodInt.GET_WIFI_STATE);
        HookMethod(WifiManager.class, "isWifiEnabled", packageName, MethodInt.GET_IS_WIFI_ENABLE);
        
        HookMethod(WifiInfo.class, "getBSSID", packageName, MethodInt.GET_BSSID);  //wifi 
        HookMethod(WifiManager.class, "getScanResults", packageName, MethodInt.GET_SCAN_RESULTS);
        HookMethod(ConnectivityManager.class, "getNetworkInfo", packageName, MethodInt.GET_MOBILE_WIFI_CONNECTED,int.class);
        HookMethod(NetworkInfo.class, "getType", packageName, MethodInt.GET_TYPE);
        
        
		HookMethod(TelephonyManager.class, "getSimSerialNumber", packageName, MethodInt.GET_SIM_SERIAL_NUMBER);
		HookMethod(TelephonyManager.class, "getLine1Number", packageName, MethodInt.GET_LINE_NUMBER1_1);
		HookMethod(TelephonyManager.class, "getPhoneType", packageName, MethodInt.GET_PHONE_TYPE_1);

        HookMethod(TelephonyManager.class, "getSimOperator", packageName, MethodInt.GET_SIM_OPERATOR);
        HookMethod(TelephonyManager.class, "getSimCountryIso", packageName, MethodInt.GET_SIM_OPERATOR_COUNTRY);
        HookMethod(TelephonyManager.class, "getSubscriberId", packageName, MethodInt.GET_SUBSCRIBER_ID);
        HookMethod(TelephonyManager.class, "getNetworkOperatorName", packageName, MethodInt.GET_NETWORK_OPERATOR_NAME);
        
        HookMethod(TelephonyManager.class, "getSimOperatorName", packageName, MethodInt.GET_NETWORK_OPERATOR_NAME);
        
        HookMethod(WifiInfo.class, "getSSID", packageName, MethodInt.GET_SSID);
        HookMethod(WifiInfo.class, "getLinkSpeed", packageName, MethodInt.GET_LINK_SPEED);
        HookMethod(WifiInfo.class, "getRssi", packageName, MethodInt.GET_RSSI);
        HookMethod(WifiInfo.class, "getMacAddress", packageName, MethodInt.GET_MAC_ADDRESS); //mac ��ַ
        HookMethod(NetworkInterface.class, "getHardwareAddress", packageName, MethodInt.GET_HARDWARE_ADDRESS); //mac ��ַ

      /////////  
        HookMethod(TelephonyManager.class, "getNetworkClass", packageName, MethodInt.GET_NETWORK_CLASS,int.class);
        HookMethod(TelephonyManager.class, "getNetworkType", packageName, MethodInt.GET_NET_TYPE_1);

   
        HookMethod(NetworkInfo.class, "getTypeName", packageName, MethodInt.GET_NET_TYPE_NAME);
        HookMethod(NetworkInfo.class, "getSubtype", packageName, MethodInt.GET_SUB_TYPE);
        HookMethod(NetworkInfo.class, "getSubtypeName", packageName, MethodInt.GET_SUB_NET_TYPE_NAME);
	////////////////////////////////////	
		
   	 	HookMethod("android.os.SystemProperties", "get", classLoader, packageName, MethodInt.GET_SYSTEM_PROPERTIES_1, String.class);
   	 	HookMethod("android.os.SystemProperties", "get", classLoader, packageName, MethodInt.GET_SYSTEM_PROPERTIES_2, String.class,String.class);
       
   	 	
   	 	String build_release = paramEntity.getBuild_release();
        Logger.ht("build_release---==="+build_release);
		if (!TextUtils.isEmpty(build_release)) {
			XposedHelpers.findField(android.os.Build.VERSION.class, "RELEASE").set(null, build_release);
		}
		String build_brand = paramEntity.getBuild_brand();
//		 Logger.ht("build_brand---==="+build_brand);
		if (!TextUtils.isEmpty(build_brand)) {
			XposedHelpers.findField(android.os.Build.class, "BRAND").set(null, build_brand);
		}
		String build_mold = paramEntity.getBuild_model();
		if (!TextUtils.isEmpty(build_mold)) {
			XposedHelpers.findField(android.os.Build.class, "MODEL").set(null, build_mold);
		}
		String build_board = paramEntity.getBuild_board();
		if (!TextUtils.isEmpty(build_mold)) {
			XposedHelpers.findField(android.os.Build.class, "BOARD").set(null, build_board);
		}
		String build_product = paramEntity.getBuild_product();
		if (!TextUtils.isEmpty(build_product)) {
			XposedHelpers.findField(android.os.Build.class, "PRODUCT").set(null, build_product);
		}
		String build_cpu_abi1 = paramEntity.getBuild_cpuabi_1();
		if (!TextUtils.isEmpty(build_cpu_abi1)) {
			XposedHelpers.findField(android.os.Build.class, "CPU_ABI").set(null, build_cpu_abi1);
		}
		String build_cpu_abi2 = paramEntity.getBuild_cpuabi_2();
		if (!TextUtils.isEmpty(build_cpu_abi2)) {
			XposedHelpers.findField(android.os.Build.class, "CPU_ABI2").set(null, build_cpu_abi2);
		}
		String build_id = paramEntity.getBuild_id();
		if (!TextUtils.isEmpty(build_id)) {
			XposedHelpers.findField(android.os.Build.class, "ID").set(null, build_id);
		}
		String build_display = paramEntity.getBuild_display();
		if (!TextUtils.isEmpty(build_display)) {
			XposedHelpers.findField(android.os.Build.class, "DISPLAY").set(null, build_display);
		}
		String build_fingerprint = paramEntity.getBuild_fingerprint();
		if (!TextUtils.isEmpty(build_display)) {
			XposedHelpers.findField(android.os.Build.class, "FINGERPRINT").set(null, build_fingerprint);
		}
		
		String build_manufacturer = paramEntity.getBuild_manufacture();
//		Logger.ht("build_manufacturer========"+build_manufacturer);
		if (!TextUtils.isEmpty(build_manufacturer)) {
			XposedHelpers.findField(android.os.Build.class, "MANUFACTURER").set(null, build_manufacturer);
		}
		String build_host = paramEntity.getBuild_host();
		if (!TextUtils.isEmpty(build_host)) {
			XposedHelpers.findField(android.os.Build.class, "HOST").set(null, build_host);
		}
		String type = paramEntity.getBuild_type();
        if (!TextUtils.isEmpty(type)) {
            XposedHelpers.findField(Build.class, "TYPE").set(null, type);
        }
        int sdk = paramEntity.getBuild_sdk();
        if(sdk!=0){
        	String sdk_s = String.valueOf(sdk);
//        	Logger.ht("sdk======"+sdk+"  "+Build.VERSION.SDK);
	        XposedHelpers.findField(Build.VERSION.class, "SDK").set(null, sdk_s);
//	        XposedHelpers.findField(Build.VERSION.class, "SDK_INT").set(null, sdk);
        }
        
        
        String tags =  paramEntity.getBuild_tags();
        if (!TextUtils.isEmpty(tags)) {
            XposedHelpers.findField(Build.class, "TAGS").set(null, tags);
        }
        String build_device = paramEntity.getBuild_device();
//        Logger.ht("build_device======"+build_device);
		if (!TextUtils.isEmpty(build_device)) {
			XposedHelpers.findField(android.os.Build.class, "DEVICE").set(null, build_device);
		}
		String build_serial = paramEntity.getBuild_serial();
//		 Logger.ht("build_serial======"+build_serial);
		if (!TextUtils.isEmpty(build_serial)) {
			XposedHelpers.findField(android.os.Build.class, "SERIAL").set(null, build_serial);
		}
//		
		String build_radio_version = paramEntity.getBuild_radioversion();
		if (!TextUtils.isEmpty(build_serial)) {
			XposedHelpers.findField(android.os.Build.class, "RADIO").set(null, build_radio_version);//unknow
		}
		String build_hardware = paramEntity.getBuild_hardware();
		if (!TextUtils.isEmpty(build_hardware)) {
			XposedHelpers.findField(android.os.Build.class, "HARDWARE").set(null, build_hardware);
		}
		String build_user = paramEntity.getBuild_user();
		if (!TextUtils.isEmpty(build_hardware)) {
			XposedHelpers.findField(android.os.Build.class, "USER").set(null, build_user);
		}
		HookMethod(BluetoothAdapter.class, "getAddress", packageName, MethodInt.GET_BLUETOOTH_ADDRESS);
		HookMethod(BluetoothAdapter.class, "getName", packageName, MethodInt.GET_BLUETOOTH_ADDRESSNAME);
		
		HookMethod(Display.class, "getHeight", packageName, MethodInt.GET_HEIGHT);
		HookMethod(Display.class, "getWidth", packageName, MethodInt.GET_WIDTH);
		HookMethod(Display.class, "getSize", packageName, MethodInt.GET_DISPLAY_SIZE,Point.class);
		HookMethod(Display.class, "getMetrics", packageName, MethodInt.GET_METRICS,DisplayMetrics.class);
		
		HookMethod(Secure.class, "getString", packageName,MethodInt.GET_ANDROID_ID, ContentResolver.class,String.class);
		HookMethod(System.class, "getString", packageName,MethodInt.GET_ANDROID_ID, ContentResolver.class,String.class);
		HookMethod(Resources.class, "getDisplayMetrics", packageName, MethodInt.GET_DISPLAY_METRICS);
		
//		HookMethod(Build.class, "deriveFingerprint", packageName,MethodInt.GET_BUILD_FINGERPRINT);
		HookMethod(WebView.class, "getSettings", packageName, MethodInt.GET_USERAGENT_6);
		HookMethod(Build.class, "getRadioVersion", packageName, MethodInt.GET_BUILD_RADIO_VERSION);

		Logger.ht("-----------------deriveFingerprint--------------------------------------------------------");

        before_HookMethod( XposedHelpers.findClass("com.android.webview.chromium.ContentSettingsAdapter", classLoader), "getUserAgentString", paramEntity.getUserAgent());

//		HookMethod(WebView.class, "getSettings", packageName, MethodInt.GET_USERAGENT_1);
		HookMethod(WebSettings.class, "getUserAgentString", packageName, MethodInt.GET_USERAGENT_0);

        HookMethod(WebSettings.class, "getDefaultUserAgent", packageName, MethodInt.GET_USERAGENT_2, Context.class);
        HookMethod(WebView.class, "setWebViewClient", packageName, MethodInt.GET_USERAGENT_3, new Object[] { WebViewClient.class });
        HookMethod(WebView.class, "loadUrl", packageName, MethodInt.GET_USERAGENT_3, new Object[] { String.class });
        HookMethod(WebView.class, "getFavicon", packageName, MethodInt.GET_USERAGENT_3);
        HookMethod(WebView.class, "computeScroll", packageName, MethodInt.GET_USERAGENT_3);
        HookMethod(WebSettings.class, "setUserAgentString", packageName, MethodInt.GET_USERAGENT_4,String.class);
//        HookMethod(HttpProtocolParams.class, "setUserAgent", packageName, MethodInt.GET_USERAGENT_4,String.class);
		
//		before_HookMethodSys(classLoader, "");

//		HookMethod(HttpURLConnection.class, "setRequestProperty", packageName, MethodInt.GET_USERAGENT_5,String.class,String.class);
//		
		
		
	}
	 private void before_HookMethod(final Class cl, final String method, final String result) {
         try {
             XposedHelpers.findAndHookMethod(cl, method, new Object[]{new XC_MethodHook() {
                 protected void beforeHookedMethod(MethodHookParam param)
                         throws Throwable {
                	 Logger.m("+++++  "+param.getResult()+"  ==");
                     param.setResult(result);
                 }

             }});
         } catch (Throwable e) {
             Logger.m("  "+ e);
         }
	 }
//	 HookMethod("android.os.SystemProperties", "get", classLoader, packageName, MethodInt.GET_SYSTEM_PROPERTIES_1, String.class);
//	 	HookMethod("android.os.SystemProperties", "get", classLoader, packageName, MethodInt.GET_SYSTEM_PROPERTIES_2, String.class,String.class);
    
	 private void before_HookMethodSys( final ClassLoader classLoader,final String result) {
         try {
             XposedHelpers.findAndHookMethod("android.os.SystemProperties", classLoader, "get", new Object[]{new XC_MethodHook() {
                 protected void beforeHookedMethod(MethodHookParam param)
                         throws Throwable {
                	 Logger.h("+++++  "+param.getResult()+"  =="+param.args);
                     param.setResult(result);
                 }

             }});
         } catch (Throwable e) {
             Logger.h(" Throwable "+ e);
         }
	 }
	@Override
	protected void after(String packageName,MethodHookParam param, int type) throws Exception {
		Object result2;//
		switch (type) {
		case MethodInt.GET_IMEI_1:
			if (!TextUtils.isEmpty(paramEntity.getImei())) {
				Logger.ht("param==imei:"+paramEntity.getImei());
				param.setResult(paramEntity.getImei());
				Logger.ht("GET_IMEI_1========gai22===="+param.getResult());
			}
			break;
		case MethodInt.GET_IMEI_2:
			Logger.m("IMEI2::" + paramEntity.getImei()+" "+param.getResult());
			try {
				int sdk = paramEntity.getBuild_sdk();
				if(sdk>20){
					if (!TextUtils.isEmpty(paramEntity.getImei())) {
						param.setResult(paramEntity.getImei());
					}
				}
				else {
					param.setResult("");	
				}
			} catch (Exception e) {
				Logger.m("IMEI2:err:"+e);
			}
			break;
		case MethodInt.GET_IMEI_3:
			try {
				int sdk = paramEntity.getBuild_sdk();
				if(sdk>20){
					if (!TextUtils.isEmpty(paramEntity.getImei())) {
						param.setResult(paramEntity.getImei());
					}
				}
				else {
					param.setResult("");	
				}
			} catch (Exception e) {
				Logger.ht("IMEI3:err:"+e);
			}
			break;
		case MethodInt.GET_GSF_ID:
			Logger.ht("-----------------GET_GSF_ID-------------------------------------------------------");
			Object[] args = param.args;
			String gsfID= paramEntity.getGsfId();
			///GSF_ID
			Uri uri = (Uri) args[0];
			 String params3[] = (String[]) args[3];
			 if(isAskGsfid(uri,params3)&&gsfID!=null){
				 param.setResult(gsfID);
			 }
			break;
		case MethodInt.GET_LAST_KNOWN_LOCATION:
			result2 = param.getResult();
			Logger.ht("---GET_LAST_KNOWN_LOCATION-::"+result2);
			/*if (result2 == null)
				return;*/
			if (paramEntity != null) {
				String lat = paramEntity.getLatitude();
				String lon = paramEntity.getLongitude();
				if (!(TextUtils.isEmpty(lat) && TextUtils.isEmpty(lon))) {
					Location location = new Location(
							LocationManager.NETWORK_PROVIDER);
					location.setLatitude(Double.valueOf(lat));
					location.setLongitude(Double.valueOf(lon));
					param.setResult(location);
				}
			}
			break;
		case MethodInt.GET_LATITUDE:
//			Logger.ht("Latitude::" + paramEntity.getLatitude());
			if (!TextUtils.isEmpty(paramEntity.getLatitude())) {
				double lat = Double.parseDouble(paramEntity.getLatitude());
				param.setResult(lat);
			}
			break;
		case MethodInt.GET_LONGITUDE:
//			Logger.ht("longitude::" + paramEntity.getLongitude());
			if (!TextUtils.isEmpty(paramEntity.getLongitude())) {
				double longitude = Double.parseDouble(paramEntity
						.getLongitude());
				param.setResult(longitude);
			}
			break;
		case MethodInt.GET_CELL_LOCATION:
			result2 = param.getResult();
			if (result2 != null)
				return;
			if (paramEntity != null) {
				String cellID = paramEntity.getCid();
				String cellLac = paramEntity.getLac();
				int cell_id = Integer.parseInt(cellID);
				int cell_lac = Integer.parseInt(cellLac);
				if (cell_id > 0 && cell_lac > 0) {
					GsmCellLocation gsmCellLocation = new GsmCellLocation();
					gsmCellLocation.setLacAndCid(cell_lac, cell_id);
					CellLocation cellLocation = gsmCellLocation;
					Logger.ht("cellLocation::" + cellLocation);
					param.setResult(cellLocation);
				}
			}
			break;
		case MethodInt.GET_CELL_ID:
			if (paramEntity != null) {
				String cellID = paramEntity.getCid();
				int cell_id = Integer.parseInt(cellID);
				if (cell_id > 0) {
					param.setResult(cell_id);
//					Logger.ht("cell_id::" + cell_id);
				}
			}
			break;
		case MethodInt.GET_CELL_LAC:
			if (paramEntity != null) {
				String cellLac = paramEntity.getLac();
				int cell_lac = Integer.parseInt(cellLac);
				if (cell_lac > 0) {
					param.setResult(cell_lac);
//					Logger.ht("cell_lac::" + cell_lac);
				}
			}
			break;
		case MethodInt.GET_CELL_MCC:
			if(paramEntity!=null){
				param.setResult(paramEntity.getMcc());
			}
			break;
		case MethodInt.GET_CELL_MNC:
			if(paramEntity!=null){
				param.setResult(paramEntity.getMnc());
			}
			break;
		case MethodInt.GET_INNER_IP_STR://不改  外网 转发路由器的ip
			Logger.ht("GGET_INNER_IP_STR============"+param.getResult()+"  "+paramEntity.getIp()+"  "+paramEntity.getInnerIP()+"    "+paramEntity.getWifiState());
			if(!packageName.equals(ConstantsHookConfig.PACKAGE_VIP_SHOP)){
				if (!TextUtils.isEmpty(paramEntity.getInnerIP())&&paramEntity.getWifiState()!=3) {
//					param.setResult("183.14.30.134");
				}
				Logger.ht("GGET_INNER_IP_STR2222============"+param.getResult());
			}
			break;
		case MethodInt.GET_INNER_IP_INT:
			Logger.ht("GET_INNER_IP_INT============");
//			if(!packageName.equals(ConstantsHookConfig.PACKAGE_VIP_SHOP)){
				if (!TextUtils.isEmpty(paramEntity.getInnerIP())) {
				int ip_int = CommonParamHelper.ip2int(paramEntity.getInnerIP());
				param.setResult(ip_int);
				Logger.ht("innerIPint::" + ip_int);
				}
//				}
			break;
		case MethodInt.GET_ANDROID_ID:
			if (param.args.length > 1 && param.args[1].equals(android.provider.Settings.Secure.ANDROID_ID)) {
				if (!TextUtils.isEmpty(paramEntity.getAndroid_id())) {
					param.setResult(paramEntity.getAndroid_id());
//					Logger.ht("androidID::" + paramEntity.getAndroid_id());
				}
			}
			break;
		case MethodInt.GET_PHONE_TYPE_1:
			Logger.ht("GET_PHONE_TYPE_1============");
			if(!TextUtils.isEmpty(paramEntity.getIccid())){
				param.setResult(paramEntity.getPhoneType());
			}
			break;
		case MethodInt.GET_PHONE_TYPE_2:
			int sdk_pt_2 = paramEntity.getBuild_sdk();
			if(sdk_pt_2>21){
				param.setResult(paramEntity.getPhoneType());
			}
			break;
		case MethodInt.GET_PHONE_TYPE_3:
			int sdk_pt_3 = paramEntity.getBuild_sdk();
			if(sdk_pt_3>20){
				param.setResult(paramEntity.getPhoneType());
			}
			break;
		case MethodInt.GET_PHONE_TYPE_4:
			int sdk_pt_4 = paramEntity.getBuild_sdk();
			if(sdk_pt_4==21){
				param.setResult(paramEntity.getPhoneType());
			}
				break;
		case MethodInt.GET_NETWORK_OPERATOR:
			Logger.ht("GET_NETWORK_OPERATOR==========!TextUtils.isEmpty(paramEntity.getIccid())=="+(!TextUtils.isEmpty(paramEntity.getIccid())));
			if(!TextUtils.isEmpty(paramEntity.getIccid())){
				param.setResult(paramEntity.getSimOperator_id());
			}else {
				param.setResult("");
			}
			
			break;
		case MethodInt.GET_NETWORK_OPERATOR_NAME:
			Logger.ht("GET_NETWORK_OPERATOR_NAME============");
			if(!TextUtils.isEmpty(paramEntity.getIccid())){
			param.setResult(paramEntity.getNetWorkoperatorName());
			}else {
				param.setResult("");
			}
			break;
		case MethodInt.GET_NETWORK_OPERATOR_NAME_2:
			int sdk_opn_2 = paramEntity.getBuild_sdk();
			if(sdk_opn_2==21&&!TextUtils.isEmpty(paramEntity.getIccid())){
				param.setResult(paramEntity.getNetWorkoperatorName());
			}else {
				param.setResult("");
			}
			break;
		case MethodInt.GET_NETWORK_OPERATOR_NAME_3:
			int sdk_opn_3 = paramEntity.getBuild_sdk();
			if(sdk_opn_3==21&&!TextUtils.isEmpty(paramEntity.getIccid())){
				param.setResult(paramEntity.getNetWorkoperatorName());
			}else {
				param.setResult("");
			}
			break;
		case MethodInt.GET_SUBSCRIBER_ID:
			Logger.ht("GET_SUBSCRIBER_ID============");
			if(!TextUtils.isEmpty(paramEntity.getIccid())){
			param.setResult(paramEntity.getImsi());
			}else {
				param.setResult("");
			}
			break;
		case MethodInt.GET_SUBSCRIBER_ID_2:
			int sdk_imsi_2 = paramEntity.getBuild_sdk();
			if(sdk_imsi_2>21&&!TextUtils.isEmpty(paramEntity.getIccid())){
				param.setResult(paramEntity.getImsi());
			}else {
				param.setResult("");
			}
			break;
		case MethodInt.GET_SUBSCRIBER_ID_3:
			int sdk_imsi_3 = paramEntity.getBuild_sdk();
			if(sdk_imsi_3==21&&!TextUtils.isEmpty(paramEntity.getIccid())){
				param.setResult(paramEntity.getImsi());
			}else {
				param.setResult("");
			}
			break;
		case MethodInt.GET_SIM_STATE:
			Logger.ht("GET_SIM_STATE============");
			if(!TextUtils.isEmpty(paramEntity.getIccid())){
				param.setResult(paramEntity.getSimState());
			}else {
				param.setResult(1);
			}
			break;
		case MethodInt.GET_SIM_STATE_2:
			int sdk_ss = paramEntity.getBuild_sdk();
			if(sdk_ss>20&&!TextUtils.isEmpty(paramEntity.getIccid())){
				param.setResult(paramEntity.getSimState());
			}else {
				param.setResult(1);
			}
			break;
		case MethodInt.GET_WIFI_STATE:
			Logger.ht("GET_WIFI_STATE============");
				param.setResult(paramEntity.getWifiState());
			break;
		case MethodInt.GET_IS_WIFI_ENABLE:
			Logger.ht("GET_IS_WIFI_ENABLE========paramEntity.getNetInfoType()===="+paramEntity.getNetInfoType()+"  "+paramEntity.getWifiState()+ paramEntity.getImei());
			if(paramEntity.getNetInfoType()==1){
				param.setResult(true);
			}else {
				if(paramEntity.getWifiState()!=3){
					param.setResult(false);//
				}
			}
			break;
		case MethodInt.GET_BSSID:
			Logger.ht("GET_BSSID============");
//			Logger.ht("bssid::"+paramEntity.getBssid()+"  getNetInfoType:"+paramEntity.getNetInfoType());
			if(paramEntity.getNetInfoType()==1){
				param.setResult(paramEntity.getBssid());
			}
			else {
				if(paramEntity.getWifiState()==3)
					param.setResult("00:00:00:00:00:00");
				else param.setResult("");
			}
			break;
		case MethodInt.GET_SSID:
			Logger.ht("GET_SSID============");
//			Logger.ht("ssid::"+paramEntity.getSsid()+"  getNetInfoType:"+paramEntity.getNetInfoType());
			if(paramEntity.getNetInfoType()==1){
				param.setResult(paramEntity.getSsid());
			}
			else {
				if(paramEntity.getWifiState()==3)
					param.setResult("0x");
				else param.setResult("<unknown ssid>");
			}
			break;
		case MethodInt.GET_LINK_SPEED:
			Logger.ht("GET_LINK_SPEED============");
			if(paramEntity.getNetInfoType()!=1){
				param.setResult(-1);
			}else {
				param.setResult(paramEntity.getLinkSpeed());
			}
			break;
		case MethodInt.GET_RSSI:
			Logger.ht("GET_RSSI============");
			if(paramEntity.getNetInfoType()!=1){
				param.setResult(-200);
			}
			else {
				param.setResult(paramEntity.getRssi());
			}
			break;
		case MethodInt.GET_MAC_ADDRESS:
			Logger.ht("GET_MAC_ADDRESS============");
			param.setResult(paramEntity.getMac());
			break;
		/*case MethodInt.GET_NET_WORK_INTERFACES_P2P0_MAC:
			String mac = paramEntity.getMac();
			String p2p0mac = paramEntity.getMac();
			Enumeration<NetworkInterface> interfaces= getNetworkInterFace(mac,p2p0mac,param.getResult());
			break;*/
		case MethodInt.GET_HARDWARE_ADDRESS:
			Logger.ht("GET_HARDWARE_ADDRESS============");
			Object obj = param.thisObject;
			if(obj!=null&& obj instanceof NetworkInterface){
				NetworkInterface interface1 = (NetworkInterface) obj;
				if(interface1.getName().equals("wlan0")){
					param.setResult(ConvertUtil.mac2byte(paramEntity.getMac()));
				}
				else if (interface1.getName().equals("p2p0")) {
					Logger.ht("=============p2pmac==============");
					param.setResult(ConvertUtil.mac2byte(paramEntity.getP2p0Mac()));
				}
			}
			break;
		case MethodInt.GET_SCAN_RESULTS:
			Logger.ht("GET_SCAN_RESULTS============");
			List<ScanResult> scanResults = (List<ScanResult>)param.getResult();
			List<ScanResult> nScanResults = XposedParamHelpUtil.handleListScanResult(param,paramEntity.getWifilist());
			Logger.ht("=======GET_SCAN_RESULTS  result======="+"  "+nScanResults.size()+"   "+nScanResults);
			param.setResult(nScanResults);
			break;
		case MethodInt.GET_MOBILE_WIFI_CONNECTED:
			Logger.ht("GET_MOBILE_WIFI_CONNECTED============");
			try{
				if (!TextUtils.isEmpty(paramEntity.getIccid())) {

					obj = param.thisObject;
					Object[] args1 = param.args;
					NetworkInfo localNetworkInfo = (NetworkInfo) param
							.getResult();
					if (args1.length > 0) {
						int arg1 = (int) args1[0];
						Class<?> clazz = Class
								.forName("android.net.NetworkInfo");// Class.forName("android.net.NetworkInfo");
						Field field = clazz.getDeclaredField("mState");
						field.setAccessible(true);
						if (arg1 == ConnectivityManager.TYPE_MOBILE) {
							if (paramEntity != null
									&& paramEntity.getNetInfoType() == 0) {
								field.set(localNetworkInfo, State.CONNECTED);
							} else {
								field.set(localNetworkInfo, State.DISCONNECTED);
							}
						} else if (arg1 == ConnectivityManager.TYPE_WIFI) {
							if (paramEntity != null
									&& paramEntity.getNetInfoType() == 1) {
								field.set(localNetworkInfo, State.CONNECTED);
							} else {
								field.set(localNetworkInfo, State.DISCONNECTED);
							}
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				Logger.ht("Param===Ex:"+e);
			}
			break;
		case MethodInt.GET_NETWORK_CLASS:
			Logger.ht("GET_NETWORK_CLASS============");
			if(!TextUtils.isEmpty(paramEntity.getIccid())){
				int net_class = XposedParamHelpUtil.handleNetWorkClass(param,paramEntity.getNetWorkType());
				param.setResult(net_class);
			}
			break;
		case MethodInt.GET_NET_TYPE_1:
			Logger.ht("GET_NET_TYPE_1============");
			if(!TextUtils.isEmpty(paramEntity.getIccid())){
				param.setResult(paramEntity.getNetWorkType());
			}
			break;
		case MethodInt.GET_NET_TYPE_2:
			if(paramEntity.getBuild_sdk()>21&&paramEntity!=null){
				param.setResult(paramEntity.getNetWorkType());
			}else {
				param.setResult("");
			}
			break;
		case MethodInt.GET_NET_TYPE_3:
			if(paramEntity.getBuild_sdk()==21&&paramEntity!=null){
				param.setResult(paramEntity.getNetWorkType());
			}else {
				param.setResult("");
			}
			break;
		case MethodInt.GET_TYPE:
			Logger.ht("GET_TYPE============");
			/*param.setResult(paramEntity.getNetInfoType());*/
			param.setResult(paramEntity.getNetInfoType());
			break;
		case MethodInt.GET_NET_TYPE_NAME:
			Logger.ht("GET_NET_TYPE_NAME============");
			param.setResult(paramEntity.getNetInfoTypeName());
			break;
		case MethodInt.GET_SUB_NET_TYPE_NAME:
			Logger.ht("GET_SUB_NET_TYPE_NAME============");
			param.setResult(paramEntity.getNetInfoSubTypeName());
			break;
		case MethodInt.GET_SUB_TYPE:
			Logger.ht("GET_SUB_TYPE============");
			param.setResult(paramEntity.getNetInfoSubType());
			break;
		case MethodInt.GET_BUILD_RADIO_VERSION:
			Logger.m("====radio========");
			param.setResult(paramEntity.getBuild_radioversion());
			break;
		case MethodInt.GET_BLUETOOTH_ADDRESS:
			param.setResult(paramEntity.getBluetoothMac());
			break;
		case MethodInt.GET_BLUETOOTH_ADDRESSNAME:
			param.setResult(paramEntity.getBluetoothName());
			break;
		case MethodInt.GET_LINE_NUMBER1_1:
			Logger.ht("GET_LINE_NUMBER1_1============");
			Logger.ht("phoneNum:======"+paramEntity.getPhoneNum());
			if(!TextUtils.isEmpty(paramEntity.getIccid())&&!TextUtils.isEmpty(paramEntity.getPhoneNum())){
				param.setResult(paramEntity.getPhoneNum());
			}else {
				param.setResult("");
			}
			break;
		case MethodInt.GET_LINE_NUMBER1_2:
			int sdk_n_1 = paramEntity.getBuild_sdk();
			if(sdk_n_1>21&&paramEntity.getPhoneNum()!=null){
				param.setResult(paramEntity.getPhoneNum());
			}
			break;
		case MethodInt.GET_LINE_NUMBER1_3:
			int sdk_n_2 = paramEntity.getBuild_sdk();
			if(sdk_n_2==21&&paramEntity.getPhoneNum()!=null){
				param.setResult(paramEntity.getPhoneNum());
			}
			break;
			
		case MethodInt.GET_SIM_SERIAL_NUMBER:
			Logger.ht("GET_SIM_SERIAL_NUMBER============");
			Logger.ht("getSimSerialNumber==iccid");
			if(!TextUtils.isEmpty(paramEntity.getIccid())){
				param.setResult(paramEntity.getIccid());
			}else {
				param.setResult("");
			}
			break;
		case MethodInt.GET_SIM_SERIAL_NUMBER_2:
			int sdk1 = paramEntity.getBuild_sdk();
			if(sdk1>21&&paramEntity.getIccid()!=null){
				param.setResult(paramEntity.getIccid());
			}else {
				param.setResult("");
			}
			break;
		case MethodInt.GET_SIM_SERIAL_NUMBER_3:
			int sdk = paramEntity.getBuild_sdk();
			if(sdk==21&&paramEntity.getIccid()!=null){
				param.setResult(paramEntity.getIccid());
			}
			break;
		case MethodInt.GET_SIM_OPERATOR:
			Logger.ht("GET_SIM_OPERATOR============");
			int sim_state = paramEntity.getSimState();
			if (!TextUtils.isEmpty(paramEntity.getIccid())) {
				if (sim_state == TelephonyManager.SIM_STATE_READY) {
					param.setResult(paramEntity.getSimOperator_id());
				} else {
					param.setResult("");
				}
			}else {
				param.setResult("");
			}
			break; 
		case MethodInt.GET_SIM_OPERATOR_2:
			int sdk_so = paramEntity.getBuild_sdk();
			int sim_state2 = paramEntity.getSimState();
			if (!TextUtils.isEmpty(paramEntity.getIccid())) {
				if (sdk_so > 21
						&& sim_state2 == TelephonyManager.SIM_STATE_READY) {
					param.setResult(paramEntity.getSimOperator_id());
				} else {
					param.setResult("");
				}
			}else {
				param.setResult("");
			}
			break; 
		case MethodInt.GET_SIM_OPERATOR_COUNTRY:
			Logger.ht("GET_SIM_OPERATOR_COUNTRY============");
			if(!TextUtils.isEmpty(paramEntity.getIccid())){
				param.setResult("cn");
			}else {
				param.setResult("");
			}
			break;
		
		case MethodInt.GET_HAS_ICC_CARD:
			Logger.ht("GET_HAS_ICC_CARD============");
			if(!TextUtils.isEmpty(paramEntity.getIccid())){
				param.setResult(true);
			}else {
				param.setResult(false);
			}
			break;
		case MethodInt.GET_HAS_ICC_CARD_2:
			int sdk_hi = paramEntity.getBuild_sdk();
			if(sdk_hi>20){
				param.setResult(true);
			}else {
				param.setResult("");
			}
			break;
		case MethodInt.GET_SYSTEM_PROPERTIES_1:
			Logger.h("==================GET_SYSTEM_PROPERTIES_1============================"+param.args[0]);
			String res1 = XposedParamHelpUtil.handleSystemOperties(param,paramEntity);
			if(res1 != null){
				param.setResult(res1);
			}
			break;
		case MethodInt.GET_SYSTEM_PROPERTIES_2:
			String res2 = XposedParamHelpUtil.handleSystemOperties2(param,paramEntity);
			if(res2!=null){
				param.setResult(res2);
			}
			break;
		case MethodInt.GET_BUILD_FINGERPRINT:
			Logger.ht("===GET_BUILD_FINGERPRINT===");
			if(paramEntity.getBuild_sdk()>20){
				param.setResult(paramEntity.getBuild_fingerprint());
			}else {
				param.setResult("");
			}
			break;
		case MethodInt.GET_HEIGHT:
			param.setResult(Integer.parseInt(paramEntity.getHeightPixels()));
			break;
		case MethodInt.GET_WIDTH:
			param.setResult(Integer.parseInt(paramEntity.getWidthPixels()));
			break;
		case MethodInt.GET_DISPLAY_SIZE:
			Object[] argsp = param.args;
			if (argsp[0] == null) {
				return;
			}
			Point point = (Point) argsp[0] ;
			point.set(Integer.parseInt(paramEntity.getWidthPixels()), Integer.parseInt(paramEntity.getHeightPixels()));
			param.setResult(point);
			break;
		case MethodInt.GET_METRICS:
			Object[] args2 = param.args;
			if (args2[0] == null) {
				return;
			}
			DisplayMetrics metric2 = (DisplayMetrics) args2[0];
			
			
//			Logger.ht(mXSPrefSetting.getBoolean(SPrefHookUtil.KEY_SETTING_DENSITY_CHEANGE, SPrefHookUtil.D_SETTING_DENSITY_CHEANGE)+":density");
			if(mXSPrefSetting.getBoolean(SPrefHookUtil.KEY_SETTING_DENSITY_CHEANGE, SPrefHookUtil.D_SETTING_DENSITY_CHEANGE))
				{
					metric2.density = paramEntity.getDensity();
					metric2.densityDpi = paramEntity.getDpi();
					metric2.xdpi = (float) (paramEntity.getDpi()-15.1562);;
					metric2.ydpi = (float) (paramEntity.getDpi()-15.1568);
					metric2.scaledDensity = paramEntity.getDensity();
				}
			metric2.heightPixels =  Integer.parseInt(paramEntity.getHeightPixels());
			metric2.widthPixels =  Integer.parseInt(paramEntity.getWidthPixels());
			
			param.setResult(metric2);
			break;
		case MethodInt.GET_DISPLAY_METRICS:
			Object metricsO = param.getResult();
			if(metricsO instanceof DisplayMetrics){
				DisplayMetrics metric = (DisplayMetrics) metricsO;
				if(mXSPrefSetting.getBoolean(SPrefHookUtil.KEY_SETTING_DENSITY_CHEANGE, SPrefHookUtil.D_SETTING_DENSITY_CHEANGE))
				{	metric.density = paramEntity.getDensity();
					metric.densityDpi = paramEntity.getDpi();
					metric.xdpi = (float) (paramEntity.getDpi()-15.1562);
					metric.ydpi = (float) (paramEntity.getDpi()-15.1568);
					metric.scaledDensity = paramEntity.getDensity();
				}
				metric.heightPixels = Integer.parseInt(paramEntity.getHeightPixels());
				metric.widthPixels = Integer.parseInt(paramEntity.getWidthPixels());
				param.setResult(metric);
			}
			
			break;
			case MethodInt.GET_USERAGENT_6:
				Logger.ht("************GET_USERAGENT_6======*******======"+param.thisObject.getClass());
				obj=param.thisObject;
				Object result12 = param.getResult();
				Logger.ht("GET_USERAGENT_1=="+(result12 instanceof WebSettings)+ " :"+param.method);
				WebSettings webSettings2 = (WebSettings) result12;
				if(obj instanceof WebSettings&&paramEntity!=null){
					webSettings2.setUserAgentString(paramEntity.getUserAgent());
				}
				break;
			case MethodInt.GET_USERAGENT_1:
				Logger.ht("************GET_USERAGENT_1============");
				obj=param.thisObject;
				Object result1 = param.getResult();
				Logger.ht("GET_USERAGENT_1=="+(result1 instanceof WebSettings)+ " :"+param.method);
				WebSettings webSettings = (WebSettings) result1;
				if(obj instanceof WebSettings&&paramEntity!=null){
					webSettings.setUserAgentString(paramEntity.getUserAgent());
				}
				break;
			case MethodInt.GET_USERAGENT_2:
				obj=param.thisObject;
				Logger.ht("***************GET_USERAGENT_2============="+param.getResult()+" :"+param.method);
				if(paramEntity!=null){
					param.setResult(paramEntity.getUserAgent());
				}
				break;
			case MethodInt.GET_USERAGENT_3:
				obj=param.thisObject;
				Logger.ht("**************GET_USERAGENT_3============="+param.method);
				if(obj instanceof WebView&&paramEntity!=null){
					((WebView) obj).getSettings().setUserAgentString(paramEntity.getUserAgent());
				}
				
				break;
			case MethodInt.GET_USERAGENT_4:
				Logger.ht("**************GET_USERAGENT_4============="+param.method);
				obj = param.thisObject;
				Object[] argObjects = param.args;
				if(argObjects.length>=1&&argObjects[0]!=null){
					argObjects[0] = paramEntity.getUserAgent();
				}
				break;
			case MethodInt.GET_USERAGENT_5:
				Logger.m("**************GET_USERAGENT_5============="+param.method);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) param.thisObject;
			Object[] args3 = param.args;
			if(args3.length>=2&&paramEntity!=null&&!TextUtils.isEmpty(paramEntity.getUserAgent())){
				String key = (String) args3[0];
				if(TextUtils.isEmpty(key)&&key.equals("User-Agent")){
					httpUrlConnection.setRequestProperty("User-Agent", "GYUserAgentAndroid");
				}
			}
			break;
		}

	}
	public static boolean isAskGsfid(Uri uri, String[] params3) {
		if(uri!=null&&ConstantsHookConfig.GSF_ID_CONTENT.equals(uri.toString())){
			if(params3!=null){
				for (String str:params3) {
					if(str.equals("android_id")){
						return true;
						}
					}
				}
		}
		return false;
	}
	
}
