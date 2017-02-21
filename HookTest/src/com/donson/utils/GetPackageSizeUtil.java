package com.donson.utils;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Process;

import com.donson.config.Logger;

public class GetPackageSizeUtil {
	public interface SizeResponseListener{
		public void response(Object size);
	}
	SizeResponseListener listener;
	public void setListener(SizeResponseListener listener){
		this.listener = listener;
	}
	Context context;
	public GetPackageSizeUtil(Context context) {
		this.context = context;
	}
	public void getpkginfo(String pkg){
		PackageManager pm = context.getPackageManager();
		try {
			Method getPackageSizeInfo = pm.getClass()
			.getMethod("getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			getPackageSizeInfo.invoke(pm, pkg,
			new PkgSizeObserver());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class PkgSizeObserver extends IPackageStatsObserver.Stub {
        public PkgSizeObserver() {
		}
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
        	if(listener!=null){
        		listener.response(pStats.dataSize+pStats.cacheSize);//kB
			}
         }
     }
	/**
	 * 获取文件大小
	 * 
	 * @param length
	 * @return
	 */
	public static String formatFileSize(long length) {
		String result = null;
		int sub_string = 0;
		if (length >= 1073741824) {
			sub_string = String.valueOf((float) length / 1073741824).indexOf(
					".");
			result = ((float) length / 1073741824 + "000").substring(0,
					sub_string + 3)
					+ "GB";
		} else if (length >= 1048576) {
			sub_string = String.valueOf((float) length / 1048576).indexOf(".");
			result = ((float) length / 1048576 + "000").substring(0,
					sub_string + 3)
					+ "MB";
		} else if (length >= 1024) {
			sub_string = String.valueOf((float) length / 1024).indexOf(".");
			result = ((float) length / 1024 + "000").substring(0,
					sub_string + 3)
					+ "KB";
		} else if (length < 1024)
			result = Long.toString(length) + "B";
		return result;
	}

}
