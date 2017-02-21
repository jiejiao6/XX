package Xposed;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.utils.SPrefHookUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.param.bean.ParamEntity;
import com.param.utils.FileUtil;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookMain implements IXposedHookLoadPackage {
	static ParamEntity paramEntity = null;
	XSharedPreferences mXSPrefHook;//参数
	static XSharedPreferences mXSPrefSetting;//设置
	
	public static SystmeValueHook systmeValueHook;//文件及系统值操作
	public static ParamHook paramHook;
	public static VpnConnectHook vpnConnectHook;
	public static SettingVpnHook settingVpnHook;
	public static XXAssistantHook assistantHook;
	public static String listenApk;
	public static String downApk;
	public static MUSecurityHook securityHook;
	public static MUSecurityCenterHook securityCenterHook;
	public static XposedInstallerHook xposedInstallerHook;
	public static YYBHOOK yybhook;
	public static PackageInstallerHook packageInstallerHook;
	
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		
		String packageName = lpparam.packageName;
		ClassLoader classLoader = lpparam.classLoader;
		ApplicationInfo appInfo = lpparam.appInfo;
		
		if (appInfo == null || TextUtils.isEmpty(packageName))
			return;
		if(packageName.equals(ConstantsHookConfig.VPN_DIALOG_PN)){
			Logger.h("--VPN_DIALOG_PN----------------"+(appInfo.flags & ApplicationInfo.FLAG_SYSTEM));
			vpnConnectHook = VpnConnectHook.getInstance();
			vpnConnectHook.handleMethod(packageName, classLoader);
		}
		if(packageName.equals(ConstantsHookConfig.SETTINGS)){
			Logger.h("--SETTINGS----------------"+(appInfo.flags & ApplicationInfo.FLAG_SYSTEM) );
			settingVpnHook = SettingVpnHook.getInstance();
			settingVpnHook.handleMethod(packageName, classLoader);
		}
		if(packageName.equals(ConstantsHookConfig.PACKAGE_SECURITY)){
			securityHook = MUSecurityHook.getIntance();
			securityHook.handleMethod(packageName, classLoader);
		}
		if(packageName.equals(ConstantsHookConfig.MY_PACKAGE_NAME)){
			assistantHook = XXAssistantHook.getInstance();
			assistantHook.handleMethod(packageName, classLoader);
		}
		if(packageName.equals(ConstantsHookConfig.PACKAGE_SECURITY_CENTER)){
			securityCenterHook = MUSecurityCenterHook.getInstance();
			securityCenterHook.handleMethod(packageName, classLoader);
		}
		if(packageName.equals(ConstantsHookConfig.XPOSED_01_PACKAGE_NAME)){
			xposedInstallerHook = XposedInstallerHook.getInstance();
			xposedInstallerHook.handleMethod(packageName, classLoader);
		}
		if(packageName.equals(ConstantsHookConfig.PAC_YYB)){
			yybhook = YYBHOOK.getInstance();
			yybhook.handleMethod(packageName, classLoader);
			XposedHelpers.findAndHookMethod(File.class, "renameTo", new Object[] { File.class,
				new AppInfos_XC_MethodHook(MethodInt.GET_CREATE_APP2, packageName) });
		}
		if(packageName.equals(ConstantsHookConfig.PAC_INSTALL)){
			packageInstallerHook = PackageInstallerHook.getInstance();
			packageInstallerHook.handleMethod(packageName, classLoader);
		}
		HookMethod(View.class, "dispatchTouchEvent", packageName,
				MethodInt.DIS, MotionEvent.class);// 后面参数是方法的参数类型  
		if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0  //系统应用
				|| packageName.equals(ConstantsHookConfig.XPOSED_01_PACKAGE_NAME)
				||packageName.equals(ConstantsHookConfig.XPOSED_02_PACKAGE_NAME)//XPOSED
				|| packageName.equals(ConstantsHookConfig.MY_PACKAGE_NAME)//屏蔽自己
				||packageName.equals(ConstantsHookConfig.Root_PACKAGE_NAME)
				||packageName.equals(ConstantsHookConfig.CONTROL_PACKAGE_NAME)) {	//360root
			return;
		}
		/***********************************************************************************/
		if (mXSPrefHook == null)
		mXSPrefHook = new XSharedPreferences(ConstantsHookConfig.MY_PACKAGE_NAME,SPrefHookUtil.SP_HOOK);
		if (mXSPrefSetting == null)
			mXSPrefSetting = new XSharedPreferences(ConstantsHookConfig.MY_PACKAGE_NAME,SPrefHookUtil.SP_SETTING);
		String whiteListStr = mXSPrefSetting.getString(SPrefHookUtil.KEY_SETTING_CHANG_WHITE_LIST, "");//修改白名单
		
		boolean isGlobal= mXSPrefSetting.getBoolean(SPrefHookUtil.KEY_SETTING_GLOBAL_CHANGEDE, SPrefHookUtil.D_SETTING_GLOBAL_CHANGEDE);
		
		listenApk = mXSPrefSetting.getString(SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME, "");
		downApk = mXSPrefSetting.getString(SPrefHookUtil.KEY_SETTING_DOWN_PACKAGE_NAME, "");
		List<String> whiteList = new ArrayList<String>();//
		if (!TextUtils.isEmpty(whiteListStr)) {
			Gson gson = new Gson();
			whiteList = gson.fromJson(whiteListStr, new TypeToken<List<String>>() {}.getType());
			if(whiteList!=null&&whiteList.size()>0){
				for(String name:whiteList){
					if (packageName.equals(name))
						return;
				}
			}
		}
		Logger.h(packageName+"===listenApk====="+listenApk);
		if(!isGlobal&&((!TextUtils.isEmpty(listenApk)&&!listenApk.equals(packageName))||TextUtils.isEmpty(listenApk))){
			return;
		}
		Logger.h("listenApk====="+listenApk);
		try {
			Gson gson = new Gson();
			String json = mXSPrefHook.getString(SPrefHookUtil.KEY_HOOK, "");
			paramEntity = gson.fromJson(json, ParamEntity.class);
			
			//隐藏自己和xposed
			if (!(ConstantsHookConfig.MY_PACKAGE_NAME.equals(packageName)||ConstantsHookConfig.CONTROL_PACKAGE_NAME.equals(packageName))) {
				XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", classLoader,
						"getInstalledPackages", int.class,new AppInfos_XC_MethodHook(MethodInt.INSTALLED_PACKAGES, packageName));
				XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager", classLoader,
						"getInstalledApplications", int.class,
						new AppInfos_XC_MethodHook(MethodInt.INSTALLED_PACKAGES_APPLICATIONS, packageName));
				HookMethod(ActivityManager.class, "getRunningAppProcesses",packageName, MethodInt.INSTALLED_PACKAGES_RUNNING_PROCESSES);
			}
			systmeValueHook = SystmeValueHook.getInstance();
			if(packageName.equals(listenApk)||packageName.equals(downApk)){
				// 监听文件操作
				systmeValueHook.handleMethod(packageName, classLoader);
//				HookMethod(AccountManager.class, "getAccountsByType", packageName, MethodInt.ACCOUNT,String.class);
			}
			paramHook = ParamHook.getInstance(paramEntity,mXSPrefSetting);
			paramHook.setParamEntity(paramEntity);
			paramHook.handleMethod(packageName, classLoader);
//			XposedHelpers.findAndHookMethod("android.os.BinderProxy", lpparam.classLoader, "transact", int.class, Parcel.class, Parcel.class, int.class, new XC_MethodHook() {
			/*XposedHelpers.findAndHookMethod("android.os.Binder", lpparam.classLoader, "transact", int.class, Parcel.class, Parcel.class, int.class, new XC_MethodHook() {

				protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
	                super.afterHookedMethod(param);
	                Logger.h("-----------------ADID----------*****-------------------------------------");
	                IBinder binder = (IBinder) param.thisObject;

	                String interfaceBinder = binder.getInterfaceDescriptor();

	                if (interfaceBinder.equals("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService")) {
	                     if(paramEntity!=null&&!TextUtils.isEmpty(paramEntity.getAdID0())){
	                    	 param.setResult(paramEntity.getAdID0());
	                     }
	                }
	            }
	        });
			Logger.h("-----------------ADID-------------------------------------------------------");
			final String CLASS_PROVIDER = "com.google.android.gsf.gservices.GservicesProvider";
			final Uri URI_GSERIVCES = Uri.parse("content://com.google.android.gsf.gservices");
			final Class<?> ProviderClass = XposedHelpers.findClass(CLASS_PROVIDER, lpparam.classLoader);
			XposedHelpers.findAndHookMethod(ProviderClass, "query", Uri.class, String[].class, String.class,
				String[].class, String.class, new XC_MethodHook() {
				@Override
				protected void afterHookedMethod(MethodHookParam param) throws Throwable {
					Cursor cursor = (Cursor) param.getResult();
					Logger.h("---------------cursorcursorcursor--gsf------------------------------------------------------");
					if (cursor == null)
						return;
					// XposedBridge.log(CursorHelper.getCursorColumn(cursor));
					// XposedBridge.log(CursorHelper.getCursorValue(cursor));
					Uri uri = (Uri) param.args[0];
					String[] selectionArgs = (String[]) param.args[3];
					String key = null;
					if (selectionArgs != null && selectionArgs.length > 0)
						key = ((String[]) param.args[3])[0];
					if (cursor==null||(!cursor.moveToFirst() || cursor.getColumnCount() < 2))
			            return ;
					cursor.getString(1);
					if(!TextUtils.isEmpty(uri.toString())&&uri.toString().equals(URI_GSERIVCES.toString())){
						MatrixCursor fake_cursor = new MatrixCursor(new String[] { "key", "value" });
						fake_cursor.addRow(new Object[]{"gsf.gservices",paramEntity.getGsfId()});
//						fake_cursor.addRow(new Object[] { true, "true" });
						param.setResult(fake_cursor);
					}
				}
			});*/
			Logger.h("-----------------ContentResolver-------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.h("ex:=" + e);
		}
	}
	public void HookMethod(final Class<?> class1, String methodName,
			final String packageName, final int type, Object... objects) {
		Object[] new_objects = new Object[objects.length + 1];
		for (int i = 0; i < objects.length; i++) {
			new_objects[i] = objects[i];
		}
		new_objects[objects.length] = new AppInfos_XC_MethodHook(type,packageName);
		XposedHelpers.findAndHookMethod(class1, methodName, new_objects);
	}
	public void HookMethod(final Class<?> class1, String methodName,
			final String packageName, final int type) {
		XposedHelpers.findAndHookMethod(class1, methodName,new Object[] { new AppInfos_XC_MethodHook(type, packageName) });
	}
	public static class AppInfos_XC_MethodHook extends XC_MethodHook {
		int type;
		String packageName;

		public AppInfos_XC_MethodHook(int type, String packageName) {
			this.packageName = packageName;
			this.type = type;
		}
		@Override
		protected void beforeHookedMethod(MethodHookParam param)
				throws Throwable {
			// TODO Auto-generated method stub
			super.beforeHookedMethod(param);
//			switch (type) {
//			case value:
//				
//				break;
//
//			default:
//				break;
//			}
		}

		@Override
		protected void afterHookedMethod(MethodHookParam param)
				throws Throwable {
			try{
				if(paramHook!=null){
					paramHook.after(packageName, param, type);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(packageName.equals(listenApk)&&systmeValueHook!=null){
					systmeValueHook.after(packageName, param, type);
				}
			}catch(Exception e){
				Logger.h("Sys===11=Ex:" + e);
				e.printStackTrace();
			}
			try{
				if(vpnConnectHook!=null&&packageName.equals(ConstantsHookConfig.VPN_DIALOG_PN)){
					vpnConnectHook.after(packageName, param, type);
				}
				if(settingVpnHook!=null&&packageName.equals(ConstantsHookConfig.SETTINGS)){
					settingVpnHook.after(packageName, param, type);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(assistantHook!=null&&packageName.equals(ConstantsHookConfig.MY_PACKAGE_NAME)){
					assistantHook.after(packageName, param, type);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
				if(securityHook!=null&&packageName.equals(ConstantsHookConfig.PACKAGE_SECURITY)){
					securityHook.after(packageName, param, type);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(securityCenterHook!=null&&packageName.equals(ConstantsHookConfig.PACKAGE_SECURITY_CENTER)){
					securityCenterHook.after(packageName, param, type);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try{
				if(xposedInstallerHook!=null&&packageName.equals(ConstantsHookConfig.XPOSED_01_PACKAGE_NAME)){
					xposedInstallerHook.after(packageName, param, type);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(yybhook!=null&&packageName.equals(ConstantsHookConfig.PAC_YYB)){
					yybhook.after(packageName, param, type);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				if(packageInstallerHook!=null&&packageName.equals(ConstantsHookConfig.PAC_INSTALL)){
					packageInstallerHook.after(packageName, param, type);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			Object obj;
			switch (type) {
			case MethodInt.GET_CREATE_APP2:
				Object thisOb = param.thisObject;
				Object object = param.args[0];
				if (object == null)
					return;
				File file1 = (File) param.args[0];
				String path = file1.getAbsolutePath();
				if(path.endsWith(".apk")){
					Logger.m("path::::"+path+"  "+packageName);
					File file2 = (File) thisOb;
//					String packageName = ActivityUtil.getPackageNameByFile(context, path);
				}
				break;
			case MethodInt.INSTALLED_PACKAGES:
				List<PackageInfo> result = (List<PackageInfo>) param.getResult();
				List<PackageInfo> arrayList = new ArrayList<PackageInfo>();
				for (PackageInfo packageInfo : result) {
					String str = packageInfo.packageName;
					if (isHidePackageName(str))
						continue;
					arrayList.add(packageInfo);
				}
				param.setResult(arrayList);
				break;
			case MethodInt.INSTALLED_PACKAGES_APPLICATIONS:
				List<ApplicationInfo> ApplicationInfo_result3 = (List<ApplicationInfo>) param.getResult();
				List<ApplicationInfo> ApplicationInfo_arrayList2 = new ArrayList<ApplicationInfo>();
				for (ApplicationInfo packageInfo : ApplicationInfo_result3) {
					String str = packageInfo.packageName;
					if (isHidePackageName(str))
						continue;
					ApplicationInfo_arrayList2.add(packageInfo);
				}
				param.setResult(ApplicationInfo_arrayList2);
				break;
			case MethodInt.INSTALLED_PACKAGES_RUNNING_PROCESSES:
				List<RunningAppProcessInfo> RunningAppProcessInfo_result3 = (List<RunningAppProcessInfo>) param.getResult();
				List<RunningAppProcessInfo> RunningAppProcessInfo_arrayList2 = new ArrayList<RunningAppProcessInfo>();
				for (RunningAppProcessInfo packageInfo : RunningAppProcessInfo_result3) {
					String str = packageInfo.processName;
					if (isHidePackageName(str))
						continue;
					RunningAppProcessInfo_arrayList2.add(packageInfo);
				}
				param.setResult(RunningAppProcessInfo_arrayList2);
				break;
			case MethodInt.DIS:
				obj = param.thisObject;
				if (obj instanceof View) {
					View view = (View) obj;
					if (view instanceof TextView) {
						TextView te = (TextView) view;
						Logger.h("---------disp---------------id:" + view.getId()
								+ "---text:" + te.getText() + "---width:" + view.getWidth()+ "--height:" + view.getHeight()
								+ "---class:" + obj.getClass().toString() + "---SuperClass:"
								+ obj.getClass().getSuperclass().toString()+"---SuperSuperClass:"
								+ obj.getClass().getSuperclass().getSuperclass()
								+ "---textSize:"+ te.getTextSize() + "---PackageName:" + packageName+"---clickAable"+te.isClickable());
					} else {
						Logger.h("---------disp-----------------id:" + view.getId()
								+ "---width:" + view.getWidth() + "---height:" + view.getHeight()
								+ "----class:"+ obj.getClass().toString() +  "---SuperClass:"
								+ obj.getClass().getSuperclass().toString() +"---SuperSuperClass:"
								+ obj.getClass().getSuperclass().getSuperclass().toString()
								 + "---PackageName:" + packageName+"---clickAable:"+view.isClickable());
					}
				}
				break;
			}
		}
	}
	
	public static class My_XC_MethodHook extends XC_MethodHook {

		String packageName;
		String str_markets;
		int mType;

		public My_XC_MethodHook(String packageName, String str_markets,int type) {
			super();
			this.packageName = packageName;
			this.str_markets = str_markets;
			mType = type;
		}

		@Override
		protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
			switch (mType) {
			case MethodInt.FILE_CONSTRUCTION:
				Object[] args = param.args;
				String path = null;
				if (args.length == 2) {
					if(args[0].toString().endsWith("/")||args[1].toString().toString().startsWith("/")){
						path = (args[0].toString() +  args[1].toString());
					}else {
						path = (args[0].toString() + "/" + args[1].toString());
					}
				} else if (args.length == 1) {
					if(args[0] instanceof URI){
						URI uri = (URI) args[0];
						File file = new File(uri);
						path = file.getAbsolutePath();
					}else {
						path = args[0].toString();
					}
				}
				String string = mXSPrefSetting.getString(SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME, "");
				if (TextUtils.isEmpty(string) || !string.equals(packageName)) {
					return;
				}
				FileSysOptRecordToFile.saveFilePath(packageName, path,args);
				break;
			case MethodInt.FILE_RANDOMFILE_CONSTRUCTION:
				Object[] argsR = param.args;
				String pathR = null;
				if(argsR.length==2){
					pathR = argsR[0].toString();
				}
//				Logger.t("file:==RANDOMFILE_CONSTRUCTION=:"+param.thisObject+"   "+param.method+"  "+pathR);
				String pacR = mXSPrefSetting.getString(SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME, "");
				if (TextUtils.isEmpty(pacR) || !pacR.equals(packageName)) {
					return;
				}
				FileSysOptRecordToFile.saveFilePath(packageName, pathR, argsR);
				break;
			case MethodInt.FILE_WRITER_CONSTRUCTION:
				Object[] argsW= param.args;
				String pathW = null;
				if(argsW.length>=1){
					pathW = argsW[0].toString();
				}
//				Logger.t("file:==WRITER_CONSTRUCTION=:"+param.thisObject+"   "+param.method+"  "+pathW);
				String pacW = mXSPrefSetting.getString(SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME, "");
				if (TextUtils.isEmpty(pacW) || !pacW.equals(packageName)) {
					return;
				}
				FileSysOptRecordToFile.saveFilePath(packageName, pathW, argsW);
			default:
				break;
			}
			
		}
		@Override
		protected void afterHookedMethod(MethodHookParam param) throws Throwable {
			super.afterHookedMethod(param);
		}
	}
	public static boolean isHidePackageName(String str) {
		if(!TextUtils.isEmpty(str)&&(str.equals(ConstantsHookConfig.XPOSED_01_PACKAGE_NAME)
				||str.equals(ConstantsHookConfig.MY_PACKAGE_NAME)
				||str.equals(ConstantsHookConfig.XPOSED_02_PACKAGE_NAME))){
			return true;
		}
		return false;
	}
}