package com.donson.myhook.services;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.utils.ActivityUtil;
import com.donson.utils.OpenActivityUtil;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 后边的XXX.class就是要启动的服务
//		Intent service = new Intent(context, MyInternetService.class);
//		context.startService(service);
		Logger.i( "开机自动服务自动启动.....");
		// 启动应用，参数为需要自动启动的应用的包名
//		Intent intent2 = context.getPackageManager().getLaunchIntentForPackage(
//				ConstantsHookConfig.CONTROL_PACKAGE_NAME);
//		context.startActivity(intent);
//		wakeAndUnlock(context, true);
//		OpenActivityUtil.openApkByDetailInfo(context, ConstantsHookConfig.CONTROL_PACKAGE_NAME, ConstantsHookConfig.CONTROL_ACTIVITY_NAME);
		if(!ActivityUtil.isServiceRunning(context, ConstantsHookConfig.CONTROL_SERVICE_NAME)){
			Logger.i("script  isservice running");
		        Intent testActivityIntent = new Intent();   
		        testActivityIntent.setAction("android.intent.action.START_PCM_PLAY_SERVICE") ; 
		        testActivityIntent.setClassName(ConstantsHookConfig.CONTROL_PACKAGE_NAME, ConstantsHookConfig.CONTROL_SERVICE_NAME);
		        testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);					       
		        context.startService(testActivityIntent); 
		}
	}
}
