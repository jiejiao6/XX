package com.donson.controller;
import java.util.Set;
import Xposed.FileSysOptRecordToFile;
import android.app.Activity;
import android.text.TextUtils;

import com.donson.utils.MyfileUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.viewinterface.SysOptListenViewInterface;

public class SysOptListenViewController {
	Activity mActivity;
	SysOptListenViewInterface viewInterface;
	public SysOptListenViewController(Activity mActivity,SysOptListenViewInterface viewInterface) {
		this.mActivity = mActivity;
		this.viewInterface =viewInterface;
		init();
	}
	public void init(){
		if(TextUtils.isEmpty(getListenPackageName())){
			deleteRecordFile();
		}
	}
	
	public String getListenPackageName(){
		String listenPackage = SPrefHookUtil.getSettingStr(mActivity, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
		return listenPackage;
	}
	public void deleteAllRecord() {
		
//		deleteListenFiles();
//		clearAllDataFile(getListenPackageName());
		deleteRecordFile();
		viewInterface.notifyList();
	}

	public void deleteRecordFile() {
		MyfileUtil.deleteFile(FileSysOptRecordToFile.RECORD_SYSTEM_OPT_PATH);
	}
	public Set<String> getAllRecoredData(){
		Set<String> set = FileSysOptRecordToFile.getSysStringset(getListenPackageName());
		return set;
	}
	
}
