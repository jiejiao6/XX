package imei.util;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class IMEIGET {
	TelephonyManager manager;
	Context context;
	public IMEIGET(Context context){
		manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		this.context = context;
	}
	public String getImei(){
		String imei = manager.getDeviceId();
		if(imei.length()==15){
			return imei;
		}else {
			Object object = isDoubleSim(context);
			if(object instanceof GaotongDoubleInfo){
				GaotongDoubleInfo gaotongDoubleInfo = (GaotongDoubleInfo)object;
				String imei_1 = gaotongDoubleInfo.getImei_1();
				String imei_2 = gaotongDoubleInfo.getImei_2();
				if(imei_1.length()==15){
					imei = imei_1;
				}else if (imei_2.length()==15) {
					imei = imei_2;
				}
			}else if (object instanceof MtkDoubleInfo) {
				MtkDoubleInfo mtkDoubleInfo = (MtkDoubleInfo)object;
				String imei_1 = mtkDoubleInfo.getImei_1();
				String imei_2 = mtkDoubleInfo.getImei_2();
				if(imei_1.length()==15){
					imei = imei_1;
				}else if (imei_2.length()==15) {
					imei = imei_2;
				}
			}
		return imei;
		}
	}
	/**
	 * @param c
	 * @return 返回平台数据
	 */
	public Object isDoubleSim(Context c) {
		GaotongDoubleInfo gaotongDoubleInfo = initQualcommDoubleSim(c);
		MtkDoubleInfo mtkDoubleInfo = initMtkDoubleSim(c);
		boolean isGaoTongCpu = gaotongDoubleInfo.isGaotongDoubleSim();
		boolean isMtkCpu = mtkDoubleInfo.isMtkDoubleSim();
		if (isGaoTongCpu) {
			// 高通芯片双卡
			return gaotongDoubleInfo;
		} else if (isMtkCpu) {
			// MTK芯片双卡
			return mtkDoubleInfo;
		} else {
			//普通单卡手机
			return null;
		}
	}
	public static MtkDoubleInfo initMtkDoubleSim(Context mContext) {
		MtkDoubleInfo mtkDoubleInfo = new MtkDoubleInfo();
		try {
			TelephonyManager tm = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);
			Class<?> c = Class.forName("com.android.internal.telephony.Phone");
			Field fields1 = c.getField("GEMINI_SIM_1");
			fields1.setAccessible(true);
			mtkDoubleInfo.setSimId_1((Integer) fields1.get(null));
			Field fields2 = c.getField("GEMINI_SIM_2");
			fields2.setAccessible(true);
			mtkDoubleInfo.setSimId_2((Integer) fields2.get(null));
			Method m = TelephonyManager.class.getDeclaredMethod(
					"getSubscriberIdGemini", int.class);
			mtkDoubleInfo.setImsi_1((String) m.invoke(tm,
					mtkDoubleInfo.getSimId_1()));
			mtkDoubleInfo.setImsi_2((String) m.invoke(tm,
					mtkDoubleInfo.getSimId_2()));

			Method m1 = TelephonyManager.class.getDeclaredMethod(
					"getDeviceIdGemini", int.class);
			mtkDoubleInfo.setImei_1((String) m1.invoke(tm,
					mtkDoubleInfo.getSimId_1()));
			mtkDoubleInfo.setImei_2((String) m1.invoke(tm,
					mtkDoubleInfo.getSimId_2()));

			Method mx = TelephonyManager.class.getDeclaredMethod(
					"getPhoneTypeGemini", int.class);
			mtkDoubleInfo.setPhoneType_1((Integer) mx.invoke(tm,
					mtkDoubleInfo.getSimId_1()));
			mtkDoubleInfo.setPhoneType_2((Integer) mx.invoke(tm,
					mtkDoubleInfo.getSimId_2()));

			if (TextUtils.isEmpty(mtkDoubleInfo.getImsi_1())
					&& (!TextUtils.isEmpty(mtkDoubleInfo.getImsi_2()))) {
				mtkDoubleInfo.setDefaultImsi(mtkDoubleInfo.getImsi_2());
			}
			if (TextUtils.isEmpty(mtkDoubleInfo.getImsi_2())
					&& (!TextUtils.isEmpty(mtkDoubleInfo.getImsi_1()))) {
				mtkDoubleInfo.setDefaultImsi(mtkDoubleInfo.getImsi_1());
			}
		} catch (Exception e) {
			mtkDoubleInfo.setMtkDoubleSim(false);
			return mtkDoubleInfo;
		}
		mtkDoubleInfo.setMtkDoubleSim(true);
		return mtkDoubleInfo;
	}
	public static GaotongDoubleInfo initQualcommDoubleSim(Context mContext) {
		GaotongDoubleInfo gaotongDoubleInfo = new GaotongDoubleInfo();
		try {
			Class<?> cx = Class
					.forName("android.telephony.MSimTelephonyManager");
			Object obj = mContext.getSystemService("phone_msim");

			Method md = cx.getMethod("getDeviceId", int.class);
			Method ms = cx.getMethod("getSubscriberId", int.class);

			gaotongDoubleInfo.setImei_1((String) md.invoke(obj,
					0));
			gaotongDoubleInfo.setImei_2((String) md.invoke(obj,
					1));
		} catch (Exception e) {
			e.printStackTrace();
			gaotongDoubleInfo.setGaotongDoubleSim(false);
			return gaotongDoubleInfo;
		}
		gaotongDoubleInfo.setGaotongDoubleSim(true);
		return gaotongDoubleInfo;
	}


}
