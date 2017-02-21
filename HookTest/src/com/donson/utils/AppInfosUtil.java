package com.donson.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.myhook.bean.AppInfosMode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppInfosUtil {
	/**
	 * @param activity
	 * @return
	 */
	public static ArrayList<AppInfosMode> getInstallApp(Activity activity) {
		PackageManager mPm = activity.getPackageManager();
		ArrayList<AppInfosMode> mAppList = new ArrayList<AppInfosMode>();
		List<PackageInfo> installedPackages = mPm.getInstalledPackages(PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		for (PackageInfo packageInfo : installedPackages) {
			int flags = packageInfo.applicationInfo.flags;
			if (((flags & ApplicationInfo.FLAG_SYSTEM) != 0)&&!packageInfo.packageName.equals(ConstantsHookConfig.MARKET_MU))
				continue;
			String packageName2 = packageInfo.packageName;
			if (packageName2.equals(activity.getPackageName()))
				continue;
			if (packageName2.equals(ConstantsHookConfig.XPOSED_01_PACKAGE_NAME))
				continue;
			/*if (packageName2.equals(ConstantsHookConfig.HDJ_HOOK_PACKAGE_NAME))
				continue;*/
			AppInfosMode packageInfoMode = new AppInfosMode();
			packageInfoMode.setPackageInfo(packageInfo);
			String pinYin = getLabelNamePinYin(packageInfo, activity);
			packageInfoMode.setPinYin(pinYin);

			String first_str = pinYin.trim().subSequence(0, 1).toString();
			if (first_str.matches("[A-Z]")) {
				packageInfoMode.setAlpha(first_str);
			} else {
				packageInfoMode.setAlpha("#");
			}

			mAppList.add(packageInfoMode);
		}
		Collections.sort(mAppList, new Comparator<AppInfosMode>() {

			@Override
			public int compare(AppInfosMode lhs, AppInfosMode rhs) {
				String lhs_trim = lhs.getPinYin().trim();
				String rhs_trim = rhs.getPinYin().trim();
				return lhs_trim.compareTo(rhs_trim);
			}
		});
		return mAppList;
	}
	
	public static String getLabelNamePinYin(PackageInfo packageInfo, Activity activity) {
		CharacterParser mCharacterParser = CharacterParser.getInstance();
		ApplicationInfo applicationInfo = packageInfo.applicationInfo;
		CharSequence loadLabel = applicationInfo.loadLabel(activity.getPackageManager());
		String pinYin = mCharacterParser.getSelling(loadLabel.toString()).toUpperCase();
		return pinYin;
	}

	public static String getPackageName(AppInfosMode packageInfoMode) {
		PackageInfo packageInfo = packageInfoMode.getPackageInfo();
		String packageName = packageInfo.packageName;
		return packageName;
	}

	public static ArrayList<AppInfosMode> getAvailableData(ArrayList<AppInfosMode> browerList) {
		ArrayList<AppInfosMode> arrayList = new ArrayList<AppInfosMode>();
		for (AppInfosMode browerMode : browerList) {
			boolean ischeck = browerMode.isIscheck();
			if (ischeck) {
				arrayList.add(browerMode);
			}
		}
		return arrayList;
	}

	public static ArrayList<String> getAvailablePackageNameList(ArrayList<AppInfosMode> browerList) {
		ArrayList<AppInfosMode> availableData2 = AppInfosUtil.getAvailableData(browerList);
		ArrayList<String> arrayList = new ArrayList<String>();
		for (AppInfosMode browerMode : availableData2) {
			String packageName = browerMode.getPackageInfo().packageName;
			arrayList.add(packageName);
		}
		return arrayList;
	}

	public static CharSequence getLabel(String packageName, Context context) {
		CharSequence loadLabel = "";
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			loadLabel = applicationInfo.loadLabel(pm);

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return loadLabel;
	}

	/**
	 * @param context
	 * @param string
	 * @return
	 */
	public static AppInfosMode getAppInfosModebyPackageName(Activity context,String string) {
		AppInfosMode appInfosMode = null;
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(string, PackageManager.GET_META_DATA);

			if(packageInfo!=null){
				appInfosMode = new AppInfosMode();
				appInfosMode.setPackageInfo(packageInfo);
				String pinYin = getLabelNamePinYin(packageInfo, context);
				appInfosMode.setPinYin(pinYin);
				String first_str = pinYin.trim().subSequence(0, 1).toString();
				if (first_str.matches("[A-Z]")) {
					appInfosMode.setAlpha(first_str);
				} else {
					appInfosMode.setAlpha("#");
				}
				appInfosMode.setIscheck(true);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appInfosMode;
	}
	
	public static PackageInfo getPackageInfoByPackageName(Activity context,String packageName){
		PackageManager pm = context.getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * ����
	 * @param context
	 * @param string
	 * @return
	 */
	public static List<AppInfosMode> getWhiteList(Activity context,String string) {
		List<AppInfosMode> list = new ArrayList<AppInfosMode>();
		AppInfosMode tmp = null;
		List<String> defaults = new ArrayList<String>();
		defaults.add(ConstantsHookConfig.XPOSED_01_PACKAGE_NAME);
		defaults.add(ConstantsHookConfig.XPOSED_02_PACKAGE_NAME);
		defaults.add(context.getPackageName());
		defaults.add(ConstantsHookConfig.HDJ_HOOK_PACKAGE_NAME);
		defaults.add(ConstantsHookConfig.Root_PACKAGE_NAME);
		defaults.add(ConstantsHookConfig.CONTROL_PACKAGE_NAME);
		if(!TextUtils.isEmpty(string)){
			Gson gson = new Gson();
			List<String> settingStr = gson.fromJson(string, new TypeToken<List<String>>(){}.getType()); 
			defaults.addAll(settingStr);
		}
		for (int i = 0; i < defaults.size(); i++) {
			tmp = getAppInfosModebyPackageName(context, defaults.get(i));
			if(tmp!=null){
				list.add(tmp);
			}
		}
		return list;
	}
	/**
	 * @param mode
	 * @param pm
	 * @return
	 */
	public static String getAppNameByMode(AppInfosMode mode,PackageManager pm){
		if(mode!=null&&mode.getPackageInfo()!=null){
//			&&mode.getPackageInfo().applicationInfo.loadLabel(pm)!=null){
			return getAppNameByPackageInfo(mode.getPackageInfo(), pm);//mode.getPackageInfo().applicationInfo.loadLabel(pm).toString();
		}else {
			return "";
		}
	}
	/**
	 * @param mode
	 * @param pm
	 * @return
	 */
	public static Drawable getAppIconByMode(AppInfosMode mode,PackageManager pm){
		if(mode!=null&&mode.getPackageInfo()!=null){
			return getAppIconByPackageInfo(mode.getPackageInfo(), pm);
		} else {
			return null;
		}
		/*if (mode != null && mode.getPackageInfo() != null&& mode.getPackageInfo().applicationInfo != null) {
			return mode.getPackageInfo().applicationInfo.loadIcon(pm);
		} else {
			return null;
		}*/
	}
	/**
	 * @param packageInfo
	 * @param pm
	 * @return
	 */
	public static Drawable getAppIconByPackageInfo(PackageInfo packageInfo,PackageManager pm){
		if (packageInfo!=null&&packageInfo.applicationInfo!=null){
			return packageInfo.applicationInfo.loadIcon(pm) ;
		} else {
			return null;
		}
	}
	/**
	 * @param packageInfo
	 * @param pm
	 * @return
	 */
	public static String getAppNameByPackageInfo(PackageInfo packageInfo,PackageManager pm){
		if (packageInfo!=null&&packageInfo.applicationInfo!=null){			
			return packageInfo.applicationInfo.loadLabel(pm).toString();
		}else {
			return "";
		}
	}
	public static Set<PackageInfo> getRunningPackageInfo(Context context){
		Set<PackageInfo> set =new HashSet<PackageInfo>();
		PackageManager pm =context.getPackageManager();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningTasks = am.getRunningAppProcesses();
		
		PackageInfo packageInfo = new PackageInfo();
		for(RunningAppProcessInfo run:runningTasks){
			try {
				ApplicationInfo applicationInfo = pm.getApplicationInfo(run.processName, PackageManager.GET_UNINSTALLED_PACKAGES);
				if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)>0){
					continue;
				}
				if(applicationInfo.packageName.equals(ConstantsHookConfig.MY_PACKAGE_NAME)
						||applicationInfo.packageName.equals(ConstantsHookConfig.XPOSED_01_PACKAGE_NAME)
						||applicationInfo.packageName.equals(ConstantsHookConfig.Root_PACKAGE_NAME)
						||applicationInfo.packageName.equals(ConstantsHookConfig.CONTROL_PACKAGE_NAME)
						||applicationInfo.packageName.equals(ConstantsHookConfig.PAC_KEYBOARD)){
					continue;
				}
				packageInfo = pm.getPackageInfo(run.processName, PackageManager.GET_META_DATA);
				set.add(packageInfo);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return set;
	}
	
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public static String getApkVersionByPackageName(Context context,String packageName){
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(packageName, 0);
			if(info!=null){
				return info.versionName;
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	public static String getApkVersionByPath(Context context,String path) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
			if(info!=null){
				String version = info.versionName;
				return version;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public static int isAppNewVersion( String onlineVersion,String localVersion)
	{
		if (localVersion.equals(onlineVersion))
		{
			return 0;
		}
		String[] localArray = localVersion.split("\\.");
		String[] onlineArray = onlineVersion.split("\\.");
		for (int i = 0; i < localArray.length; i++) {
			System.out.println(localVersion+"  "+localArray[i]);
		}
		int length = localArray.length < onlineArray.length ? localArray.length : onlineArray.length;
		try{
			for (int i = 0; i < length; i++)
			{
				if (Integer.parseInt(onlineArray[i]) > Integer.parseInt(localArray[i]))
				{
					return 1;
				}
				else if (Integer.parseInt(onlineArray[i]) < Integer.parseInt(localArray[i]))
				{
					return -1;
				}
				// 相等 比较下一组值
			}
		}catch(Exception e){
			
		}
		return -1;
	}
}
