package Xposed;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.wifi.ScanResult;
import android.text.TextUtils;

import com.donson.config.Logger;
import com.donson.utils.EasyClickUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.param.bean.ParamEntity;
import com.param.bean.WifiMode;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;

public class XposedParamHelpUtil {

	public static List<ScanResult> handleListScanResult(MethodHookParam param,
			String string) {
		List<ScanResult> result = (List<ScanResult>) param.getResult();
		List<ScanResult> list2 = new ArrayList<ScanResult>();
//		Logger.h("==========ScanResult=======result:"+result.size());
		if (result.size() > 0 && !TextUtils.isEmpty(string)) {
			Gson gson = new Gson();
			List<WifiMode> list = gson.fromJson(string,
					new TypeToken<ArrayList<WifiMode>>() {
					}.getType());
			int size = result.size();
			int index = 0;
			for (WifiMode wifiMode : list) {
				Logger.h("==========ScanResult=======index:"+index);
				ScanResult scanResult = result.get(index);
				scanResult.BSSID = wifiMode.getBssid();
				scanResult.SSID = wifiMode.getSsid();
				scanResult.frequency = wifiMode.getFrequency();
				scanResult.level = wifiMode.getLevel();
				scanResult.capabilities = wifiMode.getCapabilities();
				boolean res = list2.add(scanResult);
				index++;
				if(index>=size){
					ScanResult sr1 = null;
					Constructor<ScanResult> ctor;
					try {
						ctor = ScanResult.class.getDeclaredConstructor(ScanResult.class);
						ctor.setAccessible(true);
			            ScanResult scanResult2 = ctor.newInstance(sr1);
						scanResult2.BSSID = wifiMode.getBssid();
						scanResult2.SSID = wifiMode.getSsid();
						scanResult2.frequency = wifiMode.getFrequency();
						scanResult2.level = wifiMode.getLevel();
						scanResult2.capabilities = wifiMode.getCapabilities();
						list2.add(scanResult);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}else if(result.size()<=0){
			try{
				Gson gson = new Gson();
				List<WifiMode> list = gson.fromJson(string,
						new TypeToken<ArrayList<WifiMode>>() {
						}.getType());
				for (WifiMode wifiMode : list) {
					ScanResult sr1 = null;
					Constructor<ScanResult> ctor = ScanResult.class.getDeclaredConstructor(ScanResult.class);
		            ctor.setAccessible(true);
		            ScanResult scanResult = ctor.newInstance(sr1);
					scanResult.BSSID = wifiMode.getBssid();
					scanResult.SSID = wifiMode.getSsid();
					scanResult.frequency = wifiMode.getFrequency();
					scanResult.level = wifiMode.getLevel();
					scanResult.capabilities = wifiMode.getCapabilities();
					boolean res = list2.add(scanResult);
//					Logger.h("==========ScanResult=======list2:"+list2+"  res:"+res);
				}
			}catch(Exception e){
				e.printStackTrace();
				Logger.ht("Exce  scan  :"+e);
			}
			
		}
		Logger.h("==========ScanResult==***8=====list2:"+list2);
		return list2;
	}

	/** Unknown network class. {@hide} */
	public static final int NETWORK_CLASS_UNKNOWN = 0;
	/** Class of broadly defined "2G" networks. {@hide} */
	public static final int NETWORK_CLASS_2_G = 1;
	/** Class of broadly defined "3G" networks. {@hide} */
	public static final int NETWORK_CLASS_3_G = 2;
	/** Class of broadly defined "4G" networks. {@hide} */
	public static final int NETWORK_CLASS_4_G = 3;
	/** Network type is unknown */
	public static final int NETWORK_TYPE_UNKNOWN = 0;
	/** Current network is GPRS */
	public static final int NETWORK_TYPE_GPRS = 1;
	/** Current network is EDGE */
	public static final int NETWORK_TYPE_EDGE = 2;
	/** Current network is UMTS */
	public static final int NETWORK_TYPE_UMTS = 3;
	/** Current network is CDMA: Either IS95A or IS95B */
	public static final int NETWORK_TYPE_CDMA = 4;
	/** Current network is EVDO revision 0 */
	public static final int NETWORK_TYPE_EVDO_0 = 5;
	/** Current network is EVDO revision A */
	public static final int NETWORK_TYPE_EVDO_A = 6;
	/** Current network is 1xRTT */
	public static final int NETWORK_TYPE_1xRTT = 7;
	/** Current network is HSDPA */
	public static final int NETWORK_TYPE_HSDPA = 8;
	/** Current network is HSUPA */
	public static final int NETWORK_TYPE_HSUPA = 9;
	/** Current network is HSPA */
	public static final int NETWORK_TYPE_HSPA = 10;
	/** Current network is iDen */
	public static final int NETWORK_TYPE_IDEN = 11;
	/** Current network is EVDO revision B */
	public static final int NETWORK_TYPE_EVDO_B = 12;
	/** Current network is LTE */
	public static final int NETWORK_TYPE_LTE = 13;
	/** Current network is eHRPD */
	public static final int NETWORK_TYPE_EHRPD = 14;
	/** Current network is HSPA+ */
	public static final int NETWORK_TYPE_HSPAP = 15;
	/** Current network is GSM {@hide} */
	public static final int NETWORK_TYPE_GSM = 16;

	public static int handleNetWorkClass(MethodHookParam param, int networkType) {
		switch (networkType) {
		case NETWORK_TYPE_GPRS:
		case NETWORK_TYPE_GSM:
		case NETWORK_TYPE_EDGE:
		case NETWORK_TYPE_CDMA:
		case NETWORK_TYPE_1xRTT:
		case NETWORK_TYPE_IDEN:
			return NETWORK_CLASS_2_G;
		case NETWORK_TYPE_UMTS:
		case NETWORK_TYPE_EVDO_0:
		case NETWORK_TYPE_EVDO_A:
		case NETWORK_TYPE_HSDPA:
		case NETWORK_TYPE_HSUPA:
		case NETWORK_TYPE_HSPA:
		case NETWORK_TYPE_EVDO_B:
		case NETWORK_TYPE_EHRPD:
		case NETWORK_TYPE_HSPAP:
			return NETWORK_CLASS_3_G;
		case NETWORK_TYPE_LTE:
			return NETWORK_CLASS_4_G;
		default:
			return NETWORK_CLASS_UNKNOWN;
		}
	}
	//;
	public static String handleSystemOperties(MethodHookParam param,
			ParamEntity paramEntity) {
		Object[] args1 = param.args;
		String arg1 = (String) param.args[0];
		if(arg1!=null){
			if("gsm.operator.numeric".equals(arg1)){
				if(!TextUtils.isEmpty(paramEntity.getIccid())){
					return paramEntity.getSimOperator_id();
				}else {
					return "";
				}
			}
			else if ("ro.build.version.release".equals(arg1)) {
				return paramEntity.getBuild_release();
			}
			else if ("ro.product.brand".equals(arg1)) {
				return paramEntity.getBuild_brand();
			}
			else if ("ro.product.model".equals(arg1)) {
				return paramEntity.getBuild_model();
			}
			else if ("ro.product.board".equals(arg1)) {
				return paramEntity.getBuild_board();
			}
			else if ("ro.product.name".equals(arg1)) {
				return paramEntity.getBuild_product();
			}
			else if ("ro.product.cpu.abi".equals(arg1)) {
				return paramEntity.getBuild_cpuabi_1();
			}
			else if ("ro.product.cpu.abi2".equals(arg1)) {
				return paramEntity.getBuild_cpuabi_2();
			}else if ("ro.build.id".equals(arg1)) {
				return paramEntity.getBuild_id();
			}
			else if ("ro.build.display.id".equals(arg1)) {
				return paramEntity.getBuild_display();
			}else if ("ro.build.fingerprint".equals(arg1)) {
				return paramEntity.getBuild_fingerprint();
			}else if ("ro.product.manufacturer".equals(arg1)) {
				return paramEntity.getBuild_manufacture();
			}else if ("ro.build.host".equals(arg1)) {
				return paramEntity.getBuild_host();
			}else if ("ro.build.type".equals(arg1)) {
				return paramEntity.getBuild_type();
			}else if ("ro.build.version.sdk".equals(arg1)) {
				return String.valueOf(paramEntity.getBuild_sdk());
			}else if ("ro.build.tags".equals(arg1)) {
				return paramEntity.getBuild_tags();
			}else if ("ro.product.device".equals(arg1)) {
				return paramEntity.getBuild_device();
			}else if ("ro.serialno".equals(arg1)) {
				return paramEntity.getBuild_serial();
			}else if("gsm.version.baseband".equals(arg1)){
				return paramEntity.getBuild_radioversion();
			}else if ("ro.hardware".equals(arg1)) {
				return paramEntity.getBuild_hardware();
			}else if ("ro.build.user".equals(arg1)) {
				return paramEntity.getBuild_user();
			}
		}
		return null;
	}
	
	public static String handleSystemOperties2(MethodHookParam param,
			ParamEntity paramEntity) {
		String arg1 = (String) param.args[0];
		if(arg1!=null){
			if ("ro.build.version.release".equals(arg1)) {
				return paramEntity.getBuild_release();
			}else if ("ro.build.version.sdk".equals(arg1)) {
				return String.valueOf(paramEntity.getBuild_sdk());
			}
		}
		return null;
	}
	
	public static String KEY_ARGS = "DATA";
	public static String KEY_METHOD = "METHOD";
	public static String KEY_RESULT = "RESULT";
	public static void saveSystemValue(MethodHookParam param, String packageName,String method) {
		String resultS = "";
			Object[] args_get = param.args;
			Object result = param.getResult();
			if(result!=null){
				resultS = result.toString();
			}
			ArrayList<String> arrayList = new ArrayList<String>();
			for (int i = 1; i < args_get.length; i++) {
				arrayList.add(args_get[i].toString());
			}
			HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
			hashMap.put(KEY_ARGS, arrayList);
			try {
				JSONObject jsonObject = new JSONObject(hashMap);
//				jsonObject.put(KEY_ARGS, data);
				jsonObject.put(KEY_METHOD, method);
				jsonObject.put(KEY_RESULT, resultS);
				FileSysOptRecordToFile.saveSystemOpt(packageName,jsonObject.toString());
			} catch (JSONException e) {
				Logger.h("exception===================="+e);
				e.printStackTrace();
			}
	}
	public static void XpodedIsUsed(MethodHookParam param){
		String resultS = "";
		Object[] args_get = param.args;
		Object result = param.getResult();
		if(result!=null){
			resultS = result.toString();
		}
		Logger.h("====XpodedIsUsed====================");
		if(args_get.length>=2){
			String arg1 = (String) args_get[1];
			Logger.h("====XpodedIsUsed=================arg1==="+arg1+"  "+(arg1.equals(EasyClickUtil.KEY_IS_XPOSED_USED)));
			if(arg1.equals(EasyClickUtil.KEY_IS_XPOSED_USED)){
				param.setResult(EasyClickUtil.XPOSED_USED);
			}
		}
	}
}
