package com.donson.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.inputmethod.InputMethodManager;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.myhook.AutoActivity;
import com.donson.myhook.ChangeWhiteListActivity;
import com.donson.myhook.DebugLogActivity;
import com.donson.myhook.FileOtpListenActivity;
import com.donson.myhook.LiucunActivity;
import com.donson.myhook.LoginActivity;
import com.donson.myhook.MainActivity;
import com.donson.myhook.MarketActivity;
import com.donson.myhook.OtherSetActivity;
import com.donson.myhook.ScriptSetActivity;
import com.donson.myhook.SelectAppActivity;
import com.donson.myhook.ShowTextActivity;
import com.donson.myhook.SysOtpListenActivity;
import com.donson.myhook.UninstallActivity;
import com.donson.myhook.VpnSetActivity;
import com.donson.myhook.WifiSimSetActivity;
import com.donson.xxxiugaiqi.R;

public class OpenActivityUtil {

	public static void openApkByPackageName(Context context,String appPackageName) {
		try {
			Intent intent = context.getPackageManager()
					.getLaunchIntentForPackage(appPackageName);
			context.startActivity(intent);
			Logger.m("intent================"+intent);
		} catch (Exception e) {
			Logger.m("openApkByPackageName==Exc===" + e);
			e.printStackTrace();
		}
	}
	public static void openApkByDetailInfo(Context context,String appPackageName,String className,String log) {
		try {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.setClassName(appPackageName, className);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			Logger.i("openApkByDetailInfo=="+intent+"  "+log);
			Logger.m("intent================"+intent);
		} catch (Exception e) {
			Logger.i("openApkByDetailInfo==Ex"+e);
			e.printStackTrace();
		}
	}

	public static void startChangeWhiteListActivity(Activity context) {
		Intent intent = new Intent(context, ChangeWhiteListActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}

	public static void startAllAppActivity(Activity context) {
		Intent intent = new Intent(context, SelectAppActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}

	public static void startListenApkActivity(Activity context) {
		Intent intent = new Intent(context, SelectAppActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}

	public static void startLiuCunActivity(Activity context) {
		Intent intent = new Intent(context, LiucunActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}

	public static void startFileOptListenActivity(Activity context) {
		Intent intent = new Intent(context, FileOtpListenActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}

	public static void startAutoOptActivity(Activity context) {
		Intent intent = new Intent(context, AutoActivity.class);
//		intent.putExtra(ConstantsHookConfig.EXTRA_CONTINUE_CONTROL, isControl);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}

	public static void startSysOptListenActivity(Activity context) {
		Intent intent = new Intent(context, SysOtpListenActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}
	public static void startVpnSetActivity(Activity context) {
		Intent intent = new Intent(context, VpnSetActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}
	public static void startMainActivity(Activity context) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	public static void startLoginActivity(Activity context) {
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	public static void startScriptSetActivity(Activity context) {
		Intent intent = new Intent(context, ScriptSetActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}
	public static void openSdChose(Activity context) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");// 设置类型，我这里是任意类型，任意后缀的可以这样写。
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		((ScriptSetActivity)context).startActivityForResult(intent, ConstantsHookConfig.requestSD);
	}
	public static void startWifiSimActivity(Activity context) {
		Intent intent = new Intent(context, WifiSimSetActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
		
	}
	public static void startInput(Activity context) {
		
		((InputMethodManager)context.getSystemService("input_method")).showInputMethodPicker(); 
//		Intent intent = new Intent();
//		intent.setAction("android.settings.INPUT_METHOD_SETTINGS");
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(intent);
	}
	public static void openVpnSet(Context context) {
		Intent intent = new Intent();
		intent.setAction("android.net.vpn.SETTINGS");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	public static void openVpnAdd(Context context) {
		EasyClickUtil.setvpnConnectFlag(context, EasyClickUtil.NOT_CONNECT_VPN);
		EasyClickUtil.setIsChangingVpn(context, EasyClickUtil.NOT_CHANGE_VPN);
		EasyClickUtil.setIsDeletingVpn(context, EasyClickUtil.NOT_DELETE_VPN);
		EasyClickUtil.setIsAddingVpn(context, EasyClickUtil.ADD_VPN);
		openVpnSet(context);
	}
	public static void openVpnDelete(Context context) {
		EasyClickUtil.setvpnConnectFlag(context, EasyClickUtil.NOT_CONNECT_VPN);
		EasyClickUtil.setIsAddingVpn(context, EasyClickUtil.NOT_ADD_VPN);
		EasyClickUtil.setIsChangingVpn(context, EasyClickUtil.NOT_CHANGE_VPN);
		EasyClickUtil.setIsDeletingVpn(context, EasyClickUtil.DELETE_VPN);
		openVpnSet(context);
	}
	public static void openVpnChange(Context context) {
		EasyClickUtil.setvpnConnectFlag(context, EasyClickUtil.NOT_CONNECT_VPN);
		EasyClickUtil.setIsAddingVpn(context, EasyClickUtil.NOT_ADD_VPN);
		EasyClickUtil.setIsChangingVpn(context, EasyClickUtil.CHANGE_VPN);
		EasyClickUtil.setIsDeletingVpn(context, EasyClickUtil.NOT_DELETE_VPN);
		openVpnSet(context);
	}
	public static void openVpnConnect(Context context) {
		EasyClickUtil.setvpnConnectFlag(context, EasyClickUtil.CONNECT_VPN);
		EasyClickUtil.setIsAddingVpn(context, EasyClickUtil.NOT_ADD_VPN);
		EasyClickUtil.setIsChangingVpn(context, EasyClickUtil.NOT_CHANGE_VPN);
		EasyClickUtil.setIsDeletingVpn(context, EasyClickUtil.NOT_DELETE_VPN);
		openVpnSet(context);
	}
	public static void openVpnDisConnect(Context context) {
			EasyClickUtil.setvpnConnectFlag(context, EasyClickUtil.CONNECT_VPN);
			EasyClickUtil.setIsAddingVpn(context, EasyClickUtil.NOT_ADD_VPN);
			EasyClickUtil.setIsChangingVpn(context, EasyClickUtil.NOT_CHANGE_VPN);
			EasyClickUtil.setIsDeletingVpn(context, EasyClickUtil.NOT_DELETE_VPN);
			openVpnSet(context);
	}
	public static void startAutoStart(Activity context) {
		//com.miui.securitycenter/com.miui.permcenter.autostart.AutoStartManagementActivity
		openApkByDetailInfo(context, ConstantsHookConfig.PACKAGE_SECURITY_CENTER, ConstantsHookConfig.ACTIVITY_AUTO_START,"open act 179");
	}
	public static void startXposed(Activity context) {
		Intent intent = new Intent();
		intent.setPackage(ConstantsHookConfig.XPOSED_01_PACKAGE_NAME);
		intent.setClassName(ConstantsHookConfig.XPOSED_01_PACKAGE_NAME, ConstantsHookConfig.XPOSED_01_MODE_ACTIVITY_NAME);
		intent.putExtra("section", 1);
		intent.putExtra("finish_on_up_navigation", true);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
//		openApkByDetailInfo(context, ConstantsHookConfig.XPOSED_01_PACKAGE_NAME, ConstantsHookConfig.XPOSED_01_MODE_ACTIVITY_NAME);

	}
	public static void startUninstallActivity(Activity context) {
		Intent intent = new Intent(context, UninstallActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}
	public static void startOhterSetActivity(Activity context) {
		Intent intent = new Intent(context, OtherSetActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}
	public static void startDebugLogActivity(Activity context,int flag) {
		Intent intent = new Intent(context, DebugLogActivity.class);
		intent.putExtra("LOGFLAG", flag);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}
	public static void openTextActivity(Activity context,
			String path) {
		Intent intent = new Intent(context, ShowTextActivity.class);
		intent.putExtra("path", path);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}
	public static void startMarketActivity(Activity context) {
		Intent intent = new Intent(context, MarketActivity.class);
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
	}
	public static void openMarket(Context context) {
		String downPn = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_SETTING_DOWN_PACKAGE_NAME);
		String marketPackageName = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
		Uri localUri = Uri.parse("market://details?id=" + downPn);
		Intent localIntent = new Intent("android.intent.action.VIEW", localUri);
		localIntent.setPackage(marketPackageName);
		// localIntent.addCategory("android.intent.category.BROWSABLE");//可不用加
		// localIntent.addCategory("android.intent.category.DEFAULT");//可不用加
		localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(localIntent);
		} catch (Exception e) {
			Logger.i(e.toString());
		}
	}
	
	
	
	
	
	
}
