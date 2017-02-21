package com.donson.realparam.utils;

import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * @author mz
 * 
 */
public class NetUtil {

	private static ConnectivityManager manager = null;
	private static TelephonyManager telephonyManager = null;

	private static ConnectivityManager getConnectivityManager(Context context) {
		if (manager == null) {
			manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		return manager;
	}
	private static TelephonyManager getTelephonyManager(Context context){
		if(telephonyManager == null){
			telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		}
		return telephonyManager;
		
	}

	/**
	 * @param context
	 * @return
	 */
	public static NetworkInfo getAvtiveNetWorkInfo(Context context) {
		ConnectivityManager manager = getConnectivityManager(context);
		return manager.getActiveNetworkInfo();
	}

	/**
	 * @param context
	 * @return �ֻ������Ƿ�����
	 */
	public static boolean isMobileConnect(Context context) {
		ConnectivityManager manager = getConnectivityManager(context);
		NetworkInfo mobileInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return mobileInfo.isConnected();
	}

	/**
	 * @param context
	 */
	public static boolean isHasNet(Context context) {
		NetworkInfo networkInfo = getAvtiveNetWorkInfo(context);
		return networkInfo != null && networkInfo.isConnectedOrConnecting();
	}

	public static boolean isWifiConnect(Context context) {
		ConnectivityManager manager = getConnectivityManager(context);
		NetworkInfo wifiInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifiInfo.isConnected();
	}

	/**
	 * Return a human-readable name describe the type of the network,for example
	 * "WIFI" or "MOBILE".
	 * 
	 * @return the name of the network type
	 * @param context
	 */
	public static String getActiveNetTypeName(Context context) {
		NetworkInfo wifiInfo = getAvtiveNetWorkInfo(context);
		if (wifiInfo != null) {
			return wifiInfo.getTypeName();
		}
		return null;
	}

	/**
	 * Return a human-readable name describing the subtype of the network.
	 * 
	 * @return the name of the network subtype
	 * @param context
	 */
	public static String getActiveNetSubtypeName(Context context) {
		NetworkInfo wifiInfo = getAvtiveNetWorkInfo(context);
		if (wifiInfo != null) {
			return wifiInfo.getSubtypeName();
		}
		return null;
	}

	/**
	 * Reports the type of network to which the info in this {@code NetworkInfo}
	 * pertains.
	 * 
	 * @return one of {@link ConnectivityManager#TYPE_MOBILE},
	 *         {@link ConnectivityManager#TYPE_WIFI},
	 *         {@link ConnectivityManager#TYPE_WIMAX},
	 *         {@link ConnectivityManager#TYPE_ETHERNET},
	 *         {@link ConnectivityManager#TYPE_BLUETOOTH}, or other types
	 *         defined by {@link ConnectivityManager}
	 */
	public static int getActiveNetType(Context context) {
		NetworkInfo wifiInfo = getAvtiveNetWorkInfo(context);
		if (wifiInfo != null) {
			return wifiInfo.getType();
		}
		return -2;
	}

	/**
	 * Return a network-type-specific integer describing the subtype of the network.
	 * @return the network subtype
	 * @param context
	 * @return
	 */
	public static int getActiveSubNetType(Context context) {
		NetworkInfo wifiInfo = getAvtiveNetWorkInfo(context);
		if (wifiInfo != null) {
			return wifiInfo.getSubtype();
		}
		return -2;
	}
	/**
	 *  CONNECTING, CONNECTED, SUSPENDED, DISCONNECTING, DISCONNECTED, UNKNOWN
	 * @param context
	 * @return
	 */
	public static String getNetState(Context context) {
		NetworkInfo wifiInfo = getAvtiveNetWorkInfo(context);
		if (wifiInfo != null) {
			return wifiInfo.getState().toString();
		}
		return null;
	}
	/**
	 * @param context
	 * @return  wifi �� Mobile ״̬
	 */
	public static String getWifiMobileConnectState(Context context){
		ConnectivityManager manager = getConnectivityManager(context);
		if (manager == null) {
			return "";
		}
		String str ="";
		NetworkInfo localNetworkInfo1 = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		str += "wifi:"+	localNetworkInfo1.getState();
		str +="\n";
		localNetworkInfo1 = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		str += "mobile:"+	localNetworkInfo1.getState();
		return str;
	}
	
	/**
	 * ��ǰʹ�õ��������ͣ�<br/>
	 * NETWORK_TYPE_UNKNOWN ��������δ֪ 0<br/>
	 * NETWORK_TYPE_GPRS GPRS���� 1<br/>
	 * NETWORK_TYPE_EDGE EDGE���� 2<br/>
	 * NETWORK_TYPE_UMTS UMTS���� 3<br/>
	 * NETWORK_TYPE_HSDPA HSDPA���� 8<br/>
	 * NETWORK_TYPE_HSUPA HSUPA���� 9<br/>
	 * NETWORK_TYPE_HSPA HSPA���� 10<br/>
	 * NETWORK_TYPE_CDMA CDMA����,IS95A �� IS95B. 4<br/>
	 * NETWORK_TYPE_EVDO_0 EVDO����, revision 0. 5<br/>
	 * NETWORK_TYPE_EVDO_A EVDO����, revision A. 6<br/>
	 * NETWORK_TYPE_1xRTT 1xRTT���� 7<br/>
	 * ���й���ͨ��3GΪUMTS��HSDPA���ƶ�����ͨ��2GΪGPRS��EGDE�����ŵ�2GΪCDMA�����ŵ�3GΪEVDO<br/>
	 * @return
	 */
	public static int getNetWorkType1(Context context) {
		TelephonyManager telMgr = getTelephonyManager(context);
		return telMgr.getNetworkType();
	}
	/**
	 * 2G or 3G or 4G int 
	 * @param context
	 * @return
	 */
	public static int getNetTypeClass(Context context){
		TelephonyManager tm = getTelephonyManager(context);
		try {
			Method declaredMethod = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getNetworkClass", int.class);
			int networkClass = (int) declaredMethod.invoke(null, tm.getNetworkType());
			return networkClass;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2;
	}
	
	public static int getNetWorkType2(Context context){
		TelephonyManager tm = getTelephonyManager(context);
		Method declaredMethod;
		try {
			declaredMethod = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			declaredMethod.setAccessible(true);
			int getDefaultSubscription = (int) declaredMethod.invoke(tm);

			Method declaredMethod2 = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDataNetworkType", int.class);
			int networkType = (int) declaredMethod2.invoke(tm, getDefaultSubscription);
			return networkType;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return -2;
	}
	public static int getNetWorkType3(Context context){
		TelephonyManager tm = getTelephonyManager(context);
		try {
			try {
				Method declaredMethod = Class.forName("android.telephony.TelephonyManager")
						.getDeclaredMethod("getVoiceNetworkType");
				int getVoiceNetworkType = (int) declaredMethod.invoke(tm);
				return getVoiceNetworkType;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -2;
	}
	public static int getNetWorkType4(Context context){
		TelephonyManager tm = getTelephonyManager(context);
		try {
			Method declaredMethod = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			declaredMethod.setAccessible(true);
			int getDefaultSubscription = (int) declaredMethod.invoke(null);

			Method declaredMethod2 = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getVoiceNetworkType", int.class);
			int getVoiceNetworkType = (int) declaredMethod2.invoke(tm, getDefaultSubscription);
			return getVoiceNetworkType;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2;
	}
	public static int getNetWorkType5(Context context){
		TelephonyManager tm = getTelephonyManager(context);
		Method declaredMethod;
		try {
			declaredMethod = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			declaredMethod.setAccessible(true);
			long getDefaultSubscription = (long) declaredMethod.invoke(tm);

			Method declaredMethod2 = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDataNetworkType", long.class);
			int networkType = (int) declaredMethod2.invoke(tm, getDefaultSubscription);
			return networkType;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -2;
	}
	public static int getNetWorkType6(Context context){
		TelephonyManager tm = getTelephonyManager(context);
		try {
			Method declaredMethod = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			declaredMethod.setAccessible(true);
			long getDefaultSubscription = (long) declaredMethod.invoke(null);

			Method declaredMethod2 = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getVoiceNetworkType", long.class);
			int getVoiceNetworkType = (int) declaredMethod2.invoke(tm, getDefaultSubscription);
			return getVoiceNetworkType;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2;
	}
	

}
