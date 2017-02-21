package com.donson.realparam.utils;

import java.lang.reflect.Method;

import com.donson.config.Logger;
import com.donson.myhook.bean.SimInfosMode;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;


public class TelephonyUtil {
	public static SimInfosMode getSimInfosMode(Activity mainActivity) {
		SimInfosMode simInfosMode = new SimInfosMode();
		TelephonyManager tm = (TelephonyManager) mainActivity.getSystemService(Context.TELEPHONY_SERVICE);
		simInfosMode.setSim_state(tm.getSimState());
		try {
			Method declaredMethod = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSim");
			int defaultSim = (int) declaredMethod.invoke(tm);

			Method declaredMethod2 = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getSimState", int.class);
			int simState = (int) declaredMethod2.invoke(tm, defaultSim);
			simInfosMode.setSim_state2(simState);
		} catch (Exception e) {
			e.printStackTrace();
		}

		simInfosMode.setOperator(tm.getNetworkOperator());
		simInfosMode.setPhoneType_1(tm.getPhoneType());

		try {
			Method declaredMethod = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getCurrentPhoneType");
			int getCurrentPhoneType = (int) declaredMethod.invoke(tm);
			simInfosMode.setPhoneType_2(getCurrentPhoneType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Method declaredMethod = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			declaredMethod.setAccessible(true);
			int getDefaultSubscription = (int) declaredMethod.invoke(tm);

			Method declaredMethod2 = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getCurrentPhoneType", int.class);
			int getCurrentPhoneType = (int) declaredMethod2.invoke(tm, getDefaultSubscription);
			simInfosMode.setPhoneType_3(getCurrentPhoneType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Method declaredMethod = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			declaredMethod.setAccessible(true);
			long getDefaultSubscription = (long) declaredMethod.invoke(tm);

			Method declaredMethod2 = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getCurrentPhoneType", long.class);
			int getCurrentPhoneType = (int) declaredMethod2.invoke(tm, getDefaultSubscription);
			simInfosMode.setPhoneType_8(getCurrentPhoneType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Method declaredMethod = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getPhoneTypeFromProperty");
			declaredMethod.setAccessible(true);
			int getPhoneTypeFromProperty = (int) declaredMethod.invoke(tm);
			simInfosMode.setPhoneType_4(getPhoneTypeFromProperty);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Method declaredMethod = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultPhone");
			declaredMethod.setAccessible(true);
			int getDefaultPhone = (int) declaredMethod.invoke(null);

			Method declaredMethod2 = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getPhoneTypeFromProperty", int.class);
			declaredMethod2.setAccessible(true);
			int getPhoneTypeFromProperty = (int) declaredMethod2.invoke(tm, getDefaultPhone);
			simInfosMode.setPhoneType_5(getPhoneTypeFromProperty);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Method declaredMethod = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getPhoneTypeFromNetworkType");
			declaredMethod.setAccessible(true);
			int getPhoneTypeFromNetworkType = (int) declaredMethod.invoke(tm);
			simInfosMode.setPhoneType_6(getPhoneTypeFromNetworkType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Method declaredMethod = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultPhone");
			declaredMethod.setAccessible(true);
			int getDefaultPhone = (int) declaredMethod.invoke(null);

			Method declaredMethod2 = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getPhoneTypeFromNetworkType", int.class);
			declaredMethod2.setAccessible(true);
			int getPhoneTypeFromNetworkType = (int) declaredMethod2.invoke(tm, getDefaultPhone);
			simInfosMode.setPhoneType_7(getPhoneTypeFromNetworkType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		simInfosMode.setSim_serial(getSimSerialNumber(tm));
		try {
			Method getDefaultSubscription = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			getDefaultSubscription.setAccessible(true);
			int defaultSubscription = (int) getDefaultSubscription.invoke(null);

			Method getSimSerialNumber = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getSimSerialNumber", int.class);
			String simSerialNumber = (String) getSimSerialNumber.invoke(tm, defaultSubscription);
			simInfosMode.setSim_serial2(simSerialNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Method getDefaultSubscription = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			getDefaultSubscription.setAccessible(true);
			long defaultSubscription = (long) getDefaultSubscription.invoke(null);

			Method getSimSerialNumber = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getSimSerialNumber", long.class);
			String simSerialNumber = (String) getSimSerialNumber.invoke(tm, defaultSubscription);
			simInfosMode.setSim_serial3(simSerialNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}

		simInfosMode.setSim_operator_name(getSimOperatorName(tm));

		simInfosMode.setNetwork_operator_name(tm.getNetworkOperatorName());
		try {
			Method getDefaultSubscription = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			getDefaultSubscription.setAccessible(true);
			int defaultSubscription = (int) getDefaultSubscription.invoke(null);

			Method getNetworkOperatorName = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getNetworkOperatorName", int.class);
			String networkOperatorName = (String) getNetworkOperatorName.invoke(tm, defaultSubscription);
			simInfosMode.setNetwork_operator_name2(networkOperatorName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Method getDefaultSubscription = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			getDefaultSubscription.setAccessible(true);
			long defaultSubscription = (long) getDefaultSubscription.invoke(null);

			Method getNetworkOperatorName = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getNetworkOperatorName", long.class);
			String networkOperatorName = (String) getNetworkOperatorName.invoke(tm, defaultSubscription);
			simInfosMode.setNetwork_operator_name3(networkOperatorName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		simInfosMode.setPhone_num(tm.getLine1Number());

		try {
			Method getDefaultSubscription = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			getDefaultSubscription.setAccessible(true);
			int defaultSubscription = (int) getDefaultSubscription.invoke(null);

			Method getLine1NumberForSubscriber = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getLine1NumberForSubscriber", int.class);
			String line1NumberForSubscriber = (String) getLine1NumberForSubscriber.invoke(tm, defaultSubscription);
			simInfosMode.setPhone_num2(line1NumberForSubscriber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Method getDefaultSubscription = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			getDefaultSubscription.setAccessible(true);
			long defaultSubscription = (long) getDefaultSubscription.invoke(null);

			Method getLine1NumberForSubscriber = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getLine1NumberForSubscriber", long.class);
			String line1NumberForSubscriber = (String) getLine1NumberForSubscriber.invoke(tm, defaultSubscription);
			simInfosMode.setPhone_num3(line1NumberForSubscriber);
		} catch (Exception e) {
			e.printStackTrace();
		}

		simInfosMode.setImei(tm.getDeviceId());
		try {
			Method declaredMethod = Class.forName("android.telephony.TelephonyManager").getDeclaredMethod("getImei");
			String imei_2 = (String) declaredMethod.invoke(tm);
			simInfosMode.setImei_2(imei_2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Method getDefaultSim = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSim");
			int defaultSim = (int) getDefaultSim.invoke(tm);

			try {
				Method declaredMethod2 = Class.forName("android.telephony.TelephonyManager")
						.getDeclaredMethod("getDeviceId", int.class);
				String imei_3 = (String) declaredMethod2.invoke(tm, defaultSim);
				simInfosMode.setImei_3(imei_3);
			} catch (Exception e) {
			}

		} catch (Exception e) {
			e.printStackTrace();
			Logger.i("--TelephonyManager---Exception---"+e.toString());
		}

		simInfosMode.setSim_country_iso(tm.getSimCountryIso());// SIM���ṩ�̵Ĺ�Ҵ���
		simInfosMode.setSim_operator(getSimOperator(tm));// MCC+MNC

		try {
			Method getSimOperatorNumeric = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getSimOperatorNumeric");
			String simOperatorNumeric = (String) getSimOperatorNumeric.invoke(tm);
			simInfosMode.setSim_operator2(simOperatorNumeric);
		} catch (Exception e) {
		}

		try {

			Method getDefaultDataSubId = Class.forName("android.telephony.SubscriptionManager")
					.getDeclaredMethod("getDefaultDataSubId");
			int subId = (int) getDefaultDataSubId.invoke(null);

			Method isUsableSubIdValue = Class.forName("android.telephony.SubscriptionManager")
					.getDeclaredMethod("isUsableSubIdValue", int.class);
			boolean b_isUsableSubIdValue = (boolean) isUsableSubIdValue.invoke(null, subId);

			if (!b_isUsableSubIdValue) {
				Method getDefaultSmsSubId = Class.forName("android.telephony.SubscriptionManager")
						.getDeclaredMethod("getDefaultSmsSubId");
				subId = (int) getDefaultSmsSubId.invoke(null);

				Method isUsableSubIdValue2 = Class.forName("android.telephony.SubscriptionManager")
						.getDeclaredMethod("isUsableSubIdValue", int.class);
				b_isUsableSubIdValue = (boolean) isUsableSubIdValue2.invoke(null, subId);
				if (!b_isUsableSubIdValue) {
					Method getDefaultVoiceSubId = Class.forName("android.telephony.SubscriptionManager")
							.getDeclaredMethod("getDefaultVoiceSubId");
					subId = (int) getDefaultVoiceSubId.invoke(null);
					Method isUsableSubIdValue3 = Class.forName("android.telephony.SubscriptionManager")
							.getDeclaredMethod("isUsableSubIdValue", int.class);
					b_isUsableSubIdValue = (boolean) isUsableSubIdValue3.invoke(null, subId);
					if (!b_isUsableSubIdValue) {
						Method getDefaultSubId = Class.forName("android.telephony.SubscriptionManager")
								.getDeclaredMethod("getDefaultSubId");
						subId = (int) getDefaultSubId.invoke(null);
					}
				}
			}
			try {
				Method getPhoneId = Class.forName("android.telephony.SubscriptionManager")
						.getDeclaredMethod("getPhoneId", int.class);
				int phoneId = (int) getPhoneId.invoke(tm, subId);

				Method getSimOperatorNumericForPhone = Class.forName("android.telephony.TelephonyManager")
						.getDeclaredMethod("getSimOperatorNumericForPhone", int.class);
				String simOperatorNumericForPhone = (String) getSimOperatorNumericForPhone.invoke(tm, phoneId);
				simInfosMode.setSim_operator3(simOperatorNumericForPhone);
			} catch (Exception e) {
			}

		} catch (Exception e) {
		}

		simInfosMode.setSubscriberId(tm.getSubscriberId());
		try {
			Method getDefaultSubscription = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			getDefaultSubscription.setAccessible(true);
			int defaultSubscription = (int) getDefaultSubscription.invoke(null);

			Method getSubscriberId = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getSubscriberId", int.class);
			String subscriberId = (String) getSubscriberId.invoke(tm, defaultSubscription);
			simInfosMode.setSubscriberId2(subscriberId);
		} catch (Exception e) {
		}
		try {
			Method getDefaultSubscription = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getDefaultSubscription");
			getDefaultSubscription.setAccessible(true);
			long defaultSubscription = (long) getDefaultSubscription.invoke(null);

			Method getSubscriberId = Class.forName("android.telephony.TelephonyManager")
					.getDeclaredMethod("getSubscriberId", long.class);
			String subscriberId = (String) getSubscriberId.invoke(tm, defaultSubscription);
			simInfosMode.setSubscriberId3(subscriberId);
		} catch (Exception e) {
		}

		return simInfosMode;
	}
	// ICCID��Integrate circuit card identity ���ɵ�·��ʶ���루�̻����ֻ�SIM���У�
		// ICCIDΪIC����Ψһʶ����룬����20λ������ɣ�������ʽΪ��XXXXXX 0MFSS YYGXX XXXXX���ֱ�������£�
		// ǰ��λ��Ӫ�̴��룺�й��ƶ���Ϊ��898600��898602 ���й���ͨ��Ϊ��898601���й����898603
		// ICCID�ĺ��� SIM�������

		private static String getSimSerialNumber(TelephonyManager tm) {
			boolean hasIccCard = tm.hasIccCard();
			String simSerialNumber = "";
			if (hasIccCard) {
				simSerialNumber = tm.getSimSerialNumber();
			}
			return simSerialNumber;
		}

		public static boolean getHasIccCard(Context context) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			boolean hasIccCard = tm.hasIccCard();
			return hasIccCard;
		}

		public static int getHasIccCard2(Context context) {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			try {
				Method declaredMethod = Class.forName("android.telephony.TelephonyManager")
						.getDeclaredMethod("getDefaultSim");
				int defaultSim = (int) declaredMethod.invoke(tm);

				Method declaredMethod2 = Class.forName("android.telephony.TelephonyManager").getDeclaredMethod("hasIccCard",
						int.class);
				boolean hasIccCard = (boolean) declaredMethod2.invoke(tm, defaultSim);
				return hasIccCard ? 1 : 2;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1;
		}

		public static boolean hasIccCard_int2boolean(int i_hasIccCard) {
			return i_hasIccCard == 1 ? true : false;
		}
		/**
		 * 
		 * @param context
		 * @param tm
		 * @return SPN 
		 */
		private static String getSimOperatorName(TelephonyManager tm) {
			int state = tm.getSimState();
			String simOperatorName = "";
			if (TelephonyManager.SIM_STATE_READY == state) {
				simOperatorName = tm.getSimOperatorName();
			}
			return simOperatorName;
		}
		private static String getSimOperator(TelephonyManager tm) {
			String simOperator = "";
//			int state = tm.getSimState();
			simOperator = tm.getSimOperator();
			/*if (TelephonyManager.SIM_STATE_READY == state) {
				simOperator = tm.getSimOperator();
			}*/
			return simOperator;
		}

}
