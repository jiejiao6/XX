package com.donson.operation;

import android.content.Context;

import com.donson.utils.CmdUtil;
import com.donson.utils.SPrefHookUtil;


public class UnInstallApkOperation {
	
	String listenApk;
	public UnInstallApkOperation(Context context) {
		listenApk = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
	}
	
	public boolean unInstallApk(){
		CmdUtil.unInstallApk(listenApk);
		return false;
	}

}
