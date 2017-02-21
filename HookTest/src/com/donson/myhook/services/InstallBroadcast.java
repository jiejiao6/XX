package com.donson.myhook.services;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.MyfileUtil;
import com.donson.utils.OpenActivityUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.SendBroadCastUtil;
import com.param.utils.FileUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class InstallBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.m("PackageAddBroadcast"+intent.getAction()+"   ");
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			String name = intent.getDataString();
			String packageName = name.substring(name.indexOf(":") + 1);
			String listenApk = SPrefHookUtil.getCurTaskStr(context,
					SPrefHookUtil.KEY_CUR_PACKAGE_NAME);// SPrefHookUtil.getSettingStr(context,
														// SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
			if (!TextUtils.isEmpty(packageName)) {
				Logger.m("packageName================"+packageName+"   ");
				if (packageName.equals(listenApk)) {
					if (SPrefHookUtil.getTaskBoolean(context,
							SPrefHookUtil.KEY_TASK_APK_INSTALL,
							SPrefHookUtil.D_TASK_APK_INSTALL)) {
						SPrefHookUtil.putTaskBoolean(context,
								SPrefHookUtil.KEY_TASK_APK_INSTALL, false);
						SendBroadCastUtil.exeAutoRun(context);
					}
				} else if (!packageName.equals(listenApk)
						&& !packageName
								.equals(ConstantsHookConfig.MY_PACKAGE_NAME)
						&& !packageName
								.equals(ConstantsHookConfig.XPOSED_01_PACKAGE_NAME)
						&& !packageName
								.equals(ConstantsHookConfig.XPOSED_02_PACKAGE_NAME)
						&& !packageName
								.equals(ConstantsHookConfig.Root_PACKAGE_NAME)
						&& !packageName
								.equals(ConstantsHookConfig.CONTROL_PACKAGE_NAME)) {
					MyfileUtil.recordAddApk(packageName);
				} 
				Logger.m("packageName================"+packageName+"   "+SPrefHookUtil.getSettingStr(context,
						SPrefHookUtil.KEY_SETTING_DOWN_PACKAGE_NAME)+"  "+packageName.equals(SPrefHookUtil.getSettingStr(context,
								SPrefHookUtil.KEY_SETTING_DOWN_PACKAGE_NAME)));

				if (packageName.equals(SPrefHookUtil.getSettingStr(context,
						SPrefHookUtil.KEY_SETTING_DOWN_PACKAGE_NAME))) {
					Logger.m("EasyClickUtil.getMarketHookFlag(context)::"+EasyClickUtil.getMarketHookFlag(context));
					if(EasyClickUtil.getMarketHookFlag(context)){
						performOpenApk(context,packageName);
					}
				}
			}
		}
	}
	private void performOpenApk(final Context context,final String appPackageName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				OpenActivityUtil.openApkByPackageName(context, appPackageName);
				try {
					Thread.sleep(20*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Logger.m("=====================");
//				OpenActivityUtil.openApkByDetailInfo(context, ConstantsHookConfig.MY_PACKAGE_NAME, ConstantsHookConfig.MY_MAIN_ACTIVITY_NAME, "");
				SendBroadCastUtil.runScriptTimeOut(context);
				Logger.m("=====================");

			}
		}).start();
	}

}
