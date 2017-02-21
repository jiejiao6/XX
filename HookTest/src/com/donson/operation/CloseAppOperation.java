package com.donson.operation;

import java.util.Set;

import android.app.Activity;

import com.donson.config.ConstantsHookConfig;
import com.donson.myhook.bean.OptAppMode;
import com.donson.utils.CmdUtil;
import com.donson.utils.SPrefHookUtil;

public class CloseAppOperation {
	Activity context;
	String listenApk;
	Set<OptAppMode> closeApks;
	public CloseAppOperation(Activity context) {
		this.context = context;
		listenApk = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
	}
	
	public void setCloseApks(Set<OptAppMode> closeApks){
		this.closeApks = closeApks;
	}
	public void closeApk(){
		if(closeApks==null){
			return;
		}
		for (OptAppMode closeApk:closeApks) {
			if(!closeApk.getPackageInfo().packageName.equals("com.hdj.downapp_market"))
				CmdUtil.killProcess(closeApk.getPackageInfo().packageName);
		}
		CmdUtil.killProcess(ConstantsHookConfig.SETTINGS);
	}
	 

}
