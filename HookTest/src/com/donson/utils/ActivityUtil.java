package com.donson.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.donson.config.Logger;

public class ActivityUtil {
	public static String getLauncherTopApp(Context context) {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningTaskInfo> appTasks = activityManager
					.getRunningTasks(1);
			if (null != appTasks && !appTasks.isEmpty()) {
				return appTasks.get(0).topActivity.toString();
			}
		} else {
			UsageStatsManager sUsageStatsManager = (UsageStatsManager) context
					.getSystemService("usagestats");
			long endTime = System.currentTimeMillis();
			long beginTime = endTime - 10000;
			if (sUsageStatsManager == null) {
				sUsageStatsManager = (UsageStatsManager) context
						.getSystemService(Context.USAGE_STATS_SERVICE);
			}
			String result = "";
			UsageEvents.Event event = new UsageEvents.Event();
			UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime,
					endTime);
			while (usageEvents.hasNextEvent()) {
				usageEvents.getNextEvent(event);
				if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
					ComponentName com = new ComponentName(
							event.getPackageName(), event.getClassName());
					result = com.toString();
				}
			}
			if (!android.text.TextUtils.isEmpty(result)) {
				return result;
			}
		}
		return "";
	}
	
	public static boolean isAppRunning(Context context,String packageName){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		for(RunningAppProcessInfo rapi : infos){
			if(rapi.processName.equals(packageName)){
				return true;
			}
		}
		return false;
		}
	
	public static boolean isServiceRunning(Context context,String serviceClassName){ 
        final ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE); 
        final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE); 
        for (RunningServiceInfo runningServiceInfo : services) { 
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)){ 
                return true; 
            } 
        } 
        return false; 
     }
	public static String getPackageNameByFile(Context context,String path){
		PackageManager manager = context.getPackageManager();
		PackageInfo info = manager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
		if(info!=null){
			return info.applicationInfo.packageName;
		}
		return null;
	}

}
