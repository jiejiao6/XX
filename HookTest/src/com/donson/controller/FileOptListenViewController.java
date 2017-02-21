package com.donson.controller;

import java.util.Set;

import com.donson.utils.MyfileUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.viewinterface.FileOptListenViewInterface;

import Xposed.FileSysOptRecordToFile;
import android.app.Activity;
import android.text.TextUtils;

public class FileOptListenViewController {
	Activity mActivity;
	FileOptListenViewInterface viewInterface;
	public FileOptListenViewController(Activity mActivity,FileOptListenViewInterface viewInterface) {
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
	/**
	 * ɾ���¼
	 */
	public void deleteAllRecord() {
		
//		deleteListenFiles();
//		clearAllDataFile(getListenPackageName());
		deleteRecordFile();
		viewInterface.notifyList();
	}

	public void deleteRecordFile() {
		MyfileUtil.deleteFile(FileSysOptRecordToFile.RECORD_FILE_OPT_PATH);
	}
	/**
	 *//*
	public void deleteListenFiles() {
		MyfileUtil.deleteAllRecordFile(getAllRecoredData(),
				getListenPackageName(), new CallBackInterface() {
					@Override
					public void onCallback(Object obj) {
						int[] result = (int[]) obj; 
						viewInterface.toast(String.format(mActivity.getString(R.string.tips_delete_ok_err), result[0],result[1]));
						deleteRecordFile();
					}
				});
	}
	*//**
	 * ��� �������
	 * @param packageName
	 *//*
	public void clearAllDataFile(String packageName){
		CmdUtil.ClearApk(packageName);//�������
	}*/
	
	public Set<String> getAllRecoredData(){
		Set<String> set = FileSysOptRecordToFile.getAllrecordedFile(getListenPackageName());
		/*for (String path :set) {
			Logger.i(path);
		}*/
		return set;
	}
	
}
