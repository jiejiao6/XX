package com.donson.realparam.utils;

import android.content.Context;

public class ProviderUtil {
		public static String getAndroidID(Context context) {
			return android.provider.Settings.Secure.getString(context.getContentResolver(),
					android.provider.Settings.Secure.ANDROID_ID);
		}

		public static String getAndroidID2(Context context) {
			return android.provider.Settings.System.getString(context.getContentResolver(),
					android.provider.Settings.Secure.ANDROID_ID);
		}

		public static boolean isAdbEnabled(Context context) {
			int nRet = android.provider.Settings.Secure.getInt(context.getContentResolver(),
					android.provider.Settings.Secure.ADB_ENABLED, 0);
			return nRet > 0;
		}

		public static boolean isAllowedUnknownSource(Context context) {
			int nRet = android.provider.Settings.Secure.getInt(context.getContentResolver(),
					android.provider.Settings.Secure.INSTALL_NON_MARKET_APPS, 0);
			return nRet == 1;
		}

}
