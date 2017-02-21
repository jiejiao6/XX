package com.donson.operation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.donson.config.ConstantsHookConfig;
import com.donson.utils.CmdUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.viewinterface.AutoOptViewInterface;

import android.content.Context;

public class ClearAppOperation {
	
	Context context;
	String listenApk = "";
	AutoOptViewInterface viewInterface;
	public ClearAppOperation(Context context,AutoOptViewInterface viewInterface) {
		this.context = context;
		listenApk = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
		this.viewInterface = viewInterface;
	}
	public void clearApp(){
		List<String> needClearList = getNeedClearList();
		for(String apk:needClearList){
			CmdUtil.ClearApk(apk);
		}
//		CmdUtil.ClearApk(ConstantsHookConfig.SETTINGS);
	}
	public List<String> getNeedClearList(){
		List<String> needClearList  = new ArrayList<String>();
		needClearList.add(listenApk);
		needClearList.add(ConstantsHookConfig.DOWNLOADER_PACKAGE_NAME);
		needClearList.add(ConstantsHookConfig.BROWSER_PACKAGE_NAME);
		return needClearList;
	}
	public Map<String,Long> getMemerySize(){
		return null;
		
	}
	 public  long getFolderSize(File file) throws Exception {  
	        long size = 0;  
	        try {  
	            File[] fileList = file.listFiles();  
	            for (int i = 0; i < fileList.length; i++) {  
	                if (fileList[i].isDirectory()) {  
	                    size = size + getFolderSize(fileList[i]);  
	                } else {  
	                    size = size + fileList[i].length();  
	                }  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return size;  
	    }  

}
