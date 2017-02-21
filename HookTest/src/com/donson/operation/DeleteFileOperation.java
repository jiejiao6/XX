package com.donson.operation;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.donson.config.Logger;
import com.donson.myhook.bean.OptAppMode;
import com.donson.utils.MyfileUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.viewinterface.AutoOptViewInterface;

import Xposed.FileSysOptRecordToFile;
import android.content.Context;

public class DeleteFileOperation {
	String listenApk;
	Set<OptAppMode> listenSet;
	AutoOptViewInterface viewInterface;
	public DeleteFileOperation(Context context,Set<OptAppMode> listenSet, AutoOptViewInterface viewInterface) {
		listenApk = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
		this.viewInterface = viewInterface;
		this.listenSet = listenSet;
	}
	File file = null;
	Set<String> fileSet = new HashSet<String>();
	Set<String> folderSet = new HashSet<String>();
	public void deleteFile(){
		Set<String> pathes = FileSysOptRecordToFile.getAllrecordedFile(listenApk);
		for (String path:pathes) {
			file = new File(path);
			if(!file.exists()){
				continue;
			}if(file.isFile()){
				fileSet.add(path);
			}else if (file.isDirectory()) {
				folderSet.add(path);
			}
		}
		int file_ok_count = 0;
		int folder_ok_count = 0;
		
		
		Iterator<String> iterator1 = fileSet.iterator();
		while(iterator1.hasNext()){
			String path = iterator1.next();
			file = new File(path);
			if(!file.exists()){
				iterator1.remove();
			}
			boolean delete = MyfileUtil.deleteFile(path, listenApk);
		}
		
		Iterator<String> iterator2 = folderSet.iterator();
        while(iterator2.hasNext()){
            String path = iterator2.next();
            file = new File(path);
            if(!file.exists()){
            	iterator2.remove();
				continue;
			}
            boolean delete = MyfileUtil.deleteFileDir(path, listenApk);
        }
		if(listenSet!=null&&listenSet.size()>0){
			Iterator<OptAppMode> iterator = listenSet.iterator();
			OptAppMode appMode = iterator.next();
			appMode.setFlag(OptAppMode.FLAG_DELETE_FILE);
			appMode.setFileErrSize(fileSet.size()-file_ok_count);
			appMode.setFolderErrSize(folderSet.size()-folder_ok_count);
			listenSet.clear();
			listenSet.add(appMode);
			viewInterface.notifyAutoListMainThread(listenSet);
		}
	}
}
