package com.donson.utils;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.param.config.ConstantsConfig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SendBroadCastUtil {
	/**
	 * 设置 监听应用
	 * @param flag
	 * @param context
	 */
	public static void listenApp( Context context,boolean isNew) {
		Intent intent = new Intent();
		intent.setAction(ConstantsHookConfig.ACTION_LISTNER_APP);
//		intent.putExtra(ConstantsHookConfig.FLAG, flag);
		intent.putExtra(ConstantsHookConfig.FLAG_INSTALL, isNew);
		context.sendBroadcast(intent);
	}
	public static void checkVpn(Context context) {
		Intent intent = new Intent();
		intent.setAction(ConstantsHookConfig.ACTION_VPN_RECEVIER);
		intent.putExtra(ConstantsHookConfig.FLAG, ConstantsHookConfig.FLAG_CHECK_CONNECT);
		context.sendBroadcast(intent);
	}
	/**
	 * 应用安装成功后继续进行操作
	 * @param context
	 */
	public static void exeAutoRun(Context context) {
		Intent intent = new Intent();
		intent.setAction(ConstantsHookConfig.ACTION_EXE_AUTO_RUN);
		context.sendBroadcast(intent);
	}
	public static void startRun(Context context) {
		Intent intent = new Intent();
		intent.setAction(ConstantsHookConfig.ACTION_START_RUN);
		context.sendBroadcast(intent);
	}
	
	public static void paramOk2(Context context,String string,boolean save, boolean onlyParam) {
		Intent intent = new Intent();
		intent.setAction(ConstantsHookConfig.ACTION_PARAM_OK2);
        intent.putExtra(ConstantsConfig.INTENT_PARAM, string);  
        intent.putExtra(ConstantsConfig.INTENT_SAVE, save);  
        intent.putExtra(ConstantsConfig.INTENT_ONLY_PARAM, onlyParam);  
		context.sendBroadcast(intent);
	}
	
	public static void runScriptTimeOut(Context context) {
		Intent intent = new Intent();
		intent.setAction(ConstantsHookConfig.ACTION_SHELL_EXE_ERR);
		context.sendBroadcast(intent);
	}
	public static void getLiucunPramErr(Context context) {
		Intent intent = new Intent();
		intent.setAction(ConstantsHookConfig.ACTION_GET_LIUCUN_PARAM_ERR);
		context.sendBroadcast(intent);
	}
	
	public static void upDateXX(Context context, String myapkpath) {
		Intent intent = new Intent();
		intent.setAction(ConstantsHookConfig.ACTION_UPDATE_XX);
		intent.putExtra(ConstantsHookConfig.INTENT_MY_APK_PATH, myapkpath);
		context.sendBroadcast(intent);
	}
	public static void sendMarketBroadcast(Activity context) {
		Intent intent = new Intent();
		intent.setAction(ConstantsHookConfig.HDJ_GIT_BROADCAST);
		context.sendBroadcast(intent);
	}
	public static void sendLogout(Context context) {
		Intent intent = new Intent();
		intent.setAction(ConstantsHookConfig.ACTION_LOGOUT);
		context.sendBroadcast(intent);
	}
}
