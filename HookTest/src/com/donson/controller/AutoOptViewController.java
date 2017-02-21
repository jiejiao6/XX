package com.donson.controller;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import Http.HttpUtil2;
import Http.ProgressEvent;
import Xposed.FileSysOptRecordToFile;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.HttpConstants;
import com.donson.config.Logger;
import com.donson.myhook.bean.OptAppMode;
import com.donson.operation.ClearAppOperation;
import com.donson.operation.CloseAppOperation;
import com.donson.operation.DeleteFileOperation;
import com.donson.operation.SystemValueOperation;
import com.donson.operation.UnInstallApkOperation;
import com.donson.operation.UnInstallRecordApks;
import com.donson.operation.VpnConnectOperation;
import com.donson.utils.ActivityUtil;
import com.donson.utils.AppInfosUtil;
import com.donson.utils.CmdUtil;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.MyfileUtil;
import com.donson.utils.OpenActivityUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.SendBroadCastUtil;
import com.donson.utils.VpnUtil;
import com.donson.viewinterface.AutoOptViewInterface;
import com.donson.xxxiugaiqi.R;
import com.donson.zhushoubase.BaseApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.param.bean.ContactMode;
import com.param.bean.ParamEntity;
import com.param.config.ConstantsConfig;
import com.param.config.SPrefUtil;
import com.param.controller.MyParamInterface;
import com.param.netInterface.HttpUtil.ResponseListener;
import com.param.utils.CommonOperationUtil;
import com.param.utils.FileUtil;
import com.param.utils.MD5Util;
import com.param.utils.RandomContactsUtil;
import com.param.utils.RandomContactsUtil.InsertCallback;
import com.param.utils.RandomMessageUtil;
import com.param.utils.RandomRecentUtil;

public class AutoOptViewController {
	private Activity mActivity;
	WeakReference<Activity> weakReferenceActivity;
	private AutoOptViewInterface viewInterface;
	BaseApplication app;
	PackageManager pm;
	boolean  isRunning = false;
	boolean isVpnfinish = false;
	int Vpncount = 0;
	OptAppMode listenApkMode ;
	Set<OptAppMode> listenSet ;
	MyParamInterface interface1;
	MyReceiver myReceiver;
	ProgressDialog dialog2 = null;//安装dialog
//	boolean mIsControl;
	boolean isDownLoad = false;
	public AutoOptViewController(Activity mActivity,AutoOptViewInterface viewInterface) {
		this.mActivity = mActivity;
//		mIsControl = isControl;
		weakReferenceActivity = new WeakReference<Activity>(mActivity);
		this.viewInterface =viewInterface;
		pm = mActivity.getPackageManager();
		listenApkMode = getApkMode(getListenPackageName());
		listenSet = new HashSet<OptAppMode>();
		interface1 =MyParamInterface.getInstance(mActivity);
		app = (BaseApplication) mActivity.getApplication();
		EventBus.getDefault().register(this);
	}

	public void init() {
		isDownLoad = false;
		isVpnfinish = false;
		Vpncount = 0;
		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstantsHookConfig.ACTION_EXE_AUTO_RUN);
		filter.addAction(ConstantsConfig.ACTION_PARAM_BROADCAST);
		filter.addAction(ConstantsHookConfig.ACTION_VPN_RECEVIER);
		filter.addAction(ConstantsHookConfig.ACTION_GET_LIUCUN_PARAM_ERR);
		mActivity.registerReceiver(myReceiver, filter);

//		List<OptAppMode> list = getClosedListApkMode();
//		viewInterface.notifyAutoListMainThread(list);
		handleAutoRun();
		
	}
//	public String listenApk = null;
	public String getListenPackageName(){
//		if(listenApk==null){
//		String listenApk = SPrefHookUtil.getSettingStr(mActivity,SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
////		}
//		return listenApk;
		return SPrefHookUtil.getCurTaskStr(mActivity,SPrefHookUtil.KEY_CUR_PACKAGE_NAME);
	}
	public String getCurListenPackageName(){
		return SPrefHookUtil.getCurTaskStr(mActivity,SPrefHookUtil.KEY_CUR_PACKAGE_NAME);
	}
	public boolean getUninstallApkflag(){
		return SPrefHookUtil.getSettingBoolean(mActivity, SPrefHookUtil.KEY_SETTING_UNINSTALL_APK,SPrefHookUtil.D_SETTING_UNINSTALL_APK);
	}
	/**
	 * 计时
	 */
	int timeCount = 0;

	/**
	 * 自动运行 与计时开始
	 */
	 Thread runThread = null;
//	 private void uploadcontinue(){
//		 new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				if(handleLimit()){
//					isRunning = false;
//					return;
//				}
////				final String orderId = SPrefHookUtil.getSettingStr(mActivity, SPrefHookUtil.KEY_SETTING_ORDER_ID,SPrefHookUtil.D_SETTING_ORDER_ID);
//				if(SPrefHookUtil.getSettingBoolean(mActivity, SPrefHookUtil.KEY_SETTING_IS_RUN_SCRIPT,SPrefHookUtil.D_SETTING_IS_RUN_SCRIPT)){
//					checkScript();
//				}else {
//					handleInstallUnInstall();
//				}
//			}
//		}).run();
//	 }
	private void handleAutoRun() {
		runThread = 
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				isRunning = true;
				disConnectVpn();
//				boolean iscontinue = uploadParam();
//				if(!iscontinue){
//					return;
//				}
//				if(handleLimit()){
//					isRunning = false;
//					return;
//				}
//				final String orderId = SPrefHookUtil.getSettingStr(mActivity, SPrefHookUtil.KEY_SETTING_ORDER_ID,SPrefHookUtil.D_SETTING_ORDER_ID);
				if(SPrefHookUtil.getSettingBoolean(mActivity, SPrefHookUtil.KEY_SETTING_IS_RUN_SCRIPT,SPrefHookUtil.D_SETTING_IS_RUN_SCRIPT)){
					checkScript();
				}else {
					handleInstallUnInstall();
				}
			}
		});
		runThread.start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(isRunning)
				try {
					Thread.sleep(1000);
					viewInterface.setMyTitleMainThread(++timeCount+"s");
					Logger.i("=====time====="+timeCount+mActivity+"   "+this);
					if((!isDownLoad&&(timeCount>300))||(isDownLoad&&(timeCount>2000))){
						if(runThread!=null){
							runThread.interrupt();
							runThread = null;
						}
						mActivity.finish();
						EasyClickUtil.setIsTaskRunning(mActivity, EasyClickUtil.TASK_NOT_RUNNING);
						app.setIsRunning(false);
						Logger.i(timeCount+"--isRunning-auto 137-"+EasyClickUtil.getIsTaskRunning(mActivity));
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	/**
	 * 返回是否继续执行
	 * @return
	 *//*
	protected boolean uploadParam() {
		boolean lastSave = SPrefHookUtil.getSettingBoolean(mActivity, SPrefHookUtil.KEY_SETTING_CURRENT_IS_NEW, false);
		Logger.i("+++++++!mIsRecord||!lastSave::"+(!mIsRecord||!lastSave)+"  mIsRecord:"+mIsRecord+" !lastSave: "+!lastSave);
		if(!mIsRecord||!lastSave){
			return true;
		}
		String info = SPrefHookUtil.getHookStr(mActivity,SPrefHookUtil.KEY_HOOK);
		Logger.i("+++++++TextUtils.isEmpty(info):"+(TextUtils.isEmpty(info)));
		if(TextUtils.isEmpty(info)){
			return true ;
		}
		viewInterface.setMyLeftTitleMainThread(mActivity.getString(R.string.auto_upload_data));
		recordCount(true,SPrefHookUtil.getTaskInt(mActivity, SPrefHookUtil.KEY_TASK_TASK_ID,SPrefHookUtil.D_TASK_TASK_ID),System.currentTimeMillis(),info);
		return false;
	}
	private void recordCount(final boolean save, final int taskId, Long generationTime, final String info) {
			try{
			Date date = new Date(generationTime);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
			final String logTime = dateFormat.format(date);
			HttpUtil2 httpUtil2 = new HttpUtil2(mActivity);
			httpUtil2.uploadParamStatus(save,taskId,logTime,info);
			httpUtil2.setListener(new ResponseListener() {	
				@Override
				public void result(Object obj) {
					if(obj instanceof String){
						String result = (String) obj;
						if(HttpConstants.RESPONSE_OK.equals(result)){
							viewInterface.toastBig(mActivity.getString(R.string.auto_date_update_ok));
							int runCount = SPrefHookUtil.getTaskInt(mActivity, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME);
							boolean re = SPrefHookUtil.putTaskInt(mActivity, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, runCount+1);
							uploadcontinue();
						}else {
							viewInterface.toastBig(mActivity.getString(R.string.auto_date_update_err),R.color.red);
							HttpUtil2 httpUtil = new HttpUtil2(mActivity);
							httpUtil.uploadParamStatus(save,taskId,logTime,info);
							httpUtil.setListener(new ResponseListener() {
								
								@Override
								public void result(Object obj) {
									String result2 = (String) obj;
									if(HttpConstants.RESPONSE_OK.equals(result2)){
										viewInterface.toastBig(mActivity.getString(R.string.auto_date_update_ok)+2);
										int runCount = SPrefHookUtil.getTaskInt(mActivity, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME);
										boolean re = SPrefHookUtil.putTaskInt(mActivity, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, runCount+1);
										uploadcontinue();
									}else {
										viewInterface.toastBig(mActivity.getString(R.string.auto_date_update_err)+2,R.color.red);
										MyfileUtil myfileUtil = new MyfileUtil();
										myfileUtil.recordLog(myfileUtil.paramuploadErr,getListenPackageName());
										uploadcontinue();
									}
								}
							});
						}
					}
				}
			});
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}*/

//	protected boolean handleLimit() {
//		if(mIsControl&&!SPrefHookUtil.getTaskBoolean(mActivity, SPrefHookUtil.KEY_TASK_CONTINUOUS, SPrefHookUtil.D_TASK_CONTINUOUS)){
//			EasyClickUtil.setIsTaskRunning(mActivity, EasyClickUtil.TASK_NOT_RUNNING);
//			app.setIsRunning(false);
//			Logger.i("------isRunning===auto 151"+EasyClickUtil.getIsTaskRunning(mActivity));
//			viewInterface.toastBig(mActivity.getString(R.string.main_no_continue));
//			OpenActivityUtil.startMainActivity(mActivity);
//			mActivity.finish();
//			mActivity.overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);
//			return true;
//		}
//		if(TextUtils.isEmpty(getListenPackageName())){
//			EasyClickUtil.setIsTaskRunning(mActivity, EasyClickUtil.TASK_NOT_RUNNING);
//			app.setIsRunning(false);
//			Logger.i("------isRunning===auto 148"+EasyClickUtil.getIsTaskRunning(mActivity));
//			viewInterface.toastBig(mActivity.getString(R.string.main_no_listen_apk));
//			OpenActivityUtil.startMainActivity(mActivity);
//			mActivity.finish();
//			mActivity.overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);
//			return true;
//		}
////		int planType = SPrefHookUtil.getSettingInt(mActivity, SPrefHookUtil.KEY_SETTING_PLAN_TYPE, SPrefHookUtil.D_SETTING_PLAN_TYPE);
////		if(planType!=SPrefHookUtil.D_SETTING_PLAN_TYPE){
////			return false;
////		}
//		boolean isTotalLimit = SPrefHookUtil.getSettingBoolean(mActivity, SPrefHookUtil.KEY_SETTING_TOTAL_TIMES_LIMIT, SPrefHookUtil.D_SETTING_TOTAL_TIMES_LIMIT);
//		if(!isTotalLimit){
//			return false;
//		}
//		int runCurCount = SPrefHookUtil.getTaskInt(mActivity, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME);
//		int totalCount = SPrefHookUtil.getTaskInt(mActivity, SPrefHookUtil.KEY_TASK_RUN_TIMES, SPrefHookUtil.D_TASK_RUN_TIMES);
//		if(runCurCount<totalCount){
//			return false;
//		}else {
//			EasyClickUtil.setIsTaskRunning(mActivity, EasyClickUtil.TASK_NOT_RUNNING);
//			app.setIsRunning(false);
//			Logger.i("------isRunning===auto 171"+EasyClickUtil.getIsTaskRunning(mActivity));
//			viewInterface.toast(mActivity.getString(R.string.main_auto_running_finish));
//			OpenActivityUtil.startMainActivity(mActivity);
//			 mActivity.finish();
//			 mActivity.overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);
//			 return true;
//		}
//	}
	protected void disConnectVpn() {
		Logger.i("=========vpnConnected=11===="+VpnUtil.isVpnConnected());
		if(VpnUtil.isVpnConnected1()){
			Logger.i("=========vpnConnected=====");
			viewInterface.setMyLeftTitleMainThread(mActivity.getString(R.string.auto_disconnect_vpn));
//			EasyClickUtil.setvpnConnectFlag(mActivity, EasyClickUtil.CONNECT_VPN);
//			EasyClickUtil.setIsAddingVpn(mActivity, EasyClickUtil.NOT_ADD_VPN);
//			EasyClickUtil.setIsChangingVpn(mActivity, EasyClickUtil.NOT_CHANGE_VPN);
//			EasyClickUtil.setIsDeletingVpn(mActivity, EasyClickUtil.NOT_DELETE_VPN);
//			VpnUtil.disConnectVpn(mActivity);
//			EasyClickUtil.setvpnConnectFlag(mActivity, EasyClickUtil.NOT_CONNECT_VPN);
			EasyClickUtil.setvpnOptWhere(mActivity, EasyClickUtil.AUTO_OPT);
			OpenActivityUtil.openVpnDisConnect(mActivity);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			EasyClickUtil.setvpnConnectFlag(mActivity, EasyClickUtil.NOT_CONNECT_VPN);
			if(!VpnUtil.isVpnConnected()){
				viewInterface.toastBig(mActivity.getString(R.string.auto_disconnect_vpn));
			}
		}else {
			return;
		}
	}
	/**
	 * 处理卸载、下载、安装
	 */
	HttpUtil2 httpUtil ;
	protected void handleInstallUnInstall() {
		final String packageName = getListenPackageName();
		boolean isInstalled = CmdUtil.isAppInstalled(mActivity, packageName);
		if(getUninstallApkflag()&&isInstalled){
			try {//卸载应用
				viewInterface.notifyAutoListMainThread(getUninstallListApkMode());
				String title = mActivity.getResources().getString(R.string.auto_uninstall_apk);
				viewInterface.setMyLeftTitleMainThread(title);
				UnInstallApkOperation appOperation = new UnInstallApkOperation(mActivity);
				appOperation.unInstallApk();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {//安装应用
			boolean netDebug = SPrefHookUtil.getSettingBoolean(mActivity,SPrefHookUtil.KEY_SETTING_NET_DEBUG,SPrefHookUtil.D_SETTING_NET_DEBUG);
			boolean isInstalled2 = CmdUtil.isAppInstalled(mActivity, packageName);
			String apkVer = SPrefHookUtil.getTaskStr(mActivity, SPrefHookUtil.KEY_TASK_APK_VER);
			final String path = ConstantsHookConfig.APK_LOCAL_PATH+"/"+CommonOperationUtil.convertPackageName2Apk(packageName);
			String channel = SPrefUtil.getString(mActivity, SPrefUtil.C_CHANNEL, SPrefUtil.D_CHANNEL);
			final String backPath = MyfileUtil.getBackUpPath(packageName, channel);
			File backFile = new File(backPath);
			File file = new File(path);
			if(!netDebug){
				if(isInstalled2){
					handleAutoRunOpt();
				}else{
					if(file.exists()){
						SPrefHookUtil.putTaskBoolean(mActivity, SPrefHookUtil.KEY_TASK_APK_INSTALL, true);
						installTips(path,isInstalled2);
					}else {
						mActivity.finish();
						EasyClickUtil.setIsTaskRunning(mActivity, EasyClickUtil.TASK_NOT_RUNNING);
						app.setIsRunning(false);
						Logger.i("--------isRunning===auto 217"+EasyClickUtil.getIsTaskRunning(mActivity));;
						viewInterface.toastBig(mActivity.getString(R.string.auto_no_apk_can_use));
					}
				}
			}else{
				if(isInstalled2&&file.exists()&&MD5Util.md5sum(path).toLowerCase().equals(apkVer.toLowerCase())){//安装了 并且当前安装的是应用
					handleAutoRunOpt();
				}
				else{
					if(listenSet!=null&&listenApkMode!=null){
						listenApkMode.setFlag(OptAppMode.FLAG_INSTALL);
						listenSet.clear();
						listenSet.add(listenApkMode);
					}
					viewInterface.setMyLeftTitleMainThread(mActivity.getString(R.string.auto_install_apk));
					viewInterface.notifyAutoListMainThread(listenSet);
					if(file.exists()&&(MD5Util.md5sum(path).toLowerCase()).equals(apkVer)){
						SPrefHookUtil.putTaskBoolean(mActivity, SPrefHookUtil.KEY_TASK_APK_INSTALL, true);
						installTips(path,isInstalled2);
					}else{
						if (backFile.exists()&&(MD5Util.md5sum(backPath).toLowerCase()).equals(apkVer.toLowerCase())) {
							SPrefHookUtil.putTaskBoolean(mActivity, SPrefHookUtil.KEY_TASK_APK_INSTALL, true);
							MyfileUtil.copyDownApk2Backups(backPath, path);
							installTips(backPath,isInstalled2);
							}
						else{
							getDownLoad(backPath,isInstalled2);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 下载应用
	 * @param path
	 * @param backPath
	 * @param isInstalled
	 */
	private void getDownLoad(/*final String path,*/String backPath, final boolean isInstalled){
		isDownLoad = true;
		final String packageName = getListenPackageName();
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				httpUtil = new HttpUtil2(mActivity);
				Map<String, String> map = new HashMap<String, String>();
				String orderId = SPrefHookUtil.getTaskStr(mActivity, SPrefHookUtil.KEY_TASK_ORDER_ID,SPrefHookUtil.D_TASK_ORDER_ID);
				String actionUrl = SPrefHookUtil.getTaskStr(mActivity, SPrefHookUtil.KEY_TASK_APK_URL);
				map.put(HttpConstants.REQUEST_KEY_URL, actionUrl);
				Logger.i("isInstalled======actionUrl===="+actionUrl);
				map.put(HttpConstants.REQUEST_KEY_PACKAGENAME, packageName);
				map.put(HttpConstants.REQUEST_KEY_PATH, HttpConstants.DOWNLOAD_AKP_KEY);
				httpUtil.downBigFileFromServer(mActivity, map,true);
				showDowndialog(mActivity.getString(R.string.auto_downLoad_title_apk));
				httpUtil.setListener(new ResponseListener() {
					@Override
					public void result(Object obj) {
						if(obj instanceof String){
							if(obj.equals(HttpConstants.RESPONSE_OK)){
								SPrefHookUtil.putTaskBoolean(mActivity, SPrefHookUtil.KEY_TASK_APK_INSTALL, true);
								showDialog();
								final String path = ConstantsHookConfig.APK_LOCAL_PATH+"/"+CommonOperationUtil.convertPackageName2Apk(packageName);
								new Thread(new Runnable() {
									@Override
									public void run() {
										if(isInstalled){
											String pathVersion = AppInfosUtil.getApkVersionByPath(mActivity, path);
											String packageVersion = AppInfosUtil.getApkVersionByPackageName(mActivity, getListenPackageName());
											if(AppInfosUtil.isAppNewVersion(pathVersion, packageVersion)<0){
												CmdUtil.unInstallApk(getListenPackageName());
											}
										}
										final String path = ConstantsHookConfig.APK_LOCAL_PATH+"/"+CommonOperationUtil.convertPackageName2Apk(packageName);

										String packageName = ActivityUtil.getPackageNameByFile(mActivity, path);
										if(TextUtils.isEmpty(packageName)||!packageName.equals(getListenPackageName())){
											Logger.i("finish===安装包错误===="+packageName);
											viewInterface.toast(mActivity.getString(R.string.auto_install_err_package));
											finishThis();
											return;
										}
										boolean result = CmdUtil.installApk(mActivity, path);
										if(!result){
											if(isInstalled){
												CmdUtil.unInstallApk(getListenPackageName());
												boolean install2 = CmdUtil.installApk(mActivity, path);
												if(!install2){
													installErr(path);
												}
											}else {
												installErr(path);
											}
										}
									}
								}).start();
							}
							else {
								dismissDownDialog();
								viewInterface.toastBig(mActivity.getString(R.string.auto_tip_downLoad_err));
								SendBroadCastUtil.startRun(mActivity);
								EasyClickUtil.setIsTaskRunning(mActivity, EasyClickUtil.TASK_NOT_RUNNING);
								app.setIsRunning(false);
								Logger.i("--isRunning-auto 271-"+EasyClickUtil.getIsTaskRunning(mActivity));
								mActivity.finish();
								mActivity.overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);
							}
						}
					}
				});
			}
		});
	}
	private void finishThis(){
		SendBroadCastUtil.startRun(mActivity);
		EasyClickUtil.setIsTaskRunning(mActivity, EasyClickUtil.TASK_NOT_RUNNING);
		app.setIsRunning(false);
		Logger.i("--isRunning-auto 503-"+EasyClickUtil.getIsTaskRunning(mActivity));
		mActivity.finish();
		mActivity.overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);
	}
	private void installTips(String path,boolean isInstalled) {
		String packageName = ActivityUtil.getPackageNameByFile(mActivity, path);
		if(TextUtils.isEmpty(packageName)||!packageName.equals(getListenPackageName())){
			Logger.i("finish===安装包错误===="+packageName);
			viewInterface.toast(mActivity.getString(R.string.auto_install_err_package));
			finishThis();
			return;
		}
		mActivity.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				showDialog();
			}
		});
		if(isInstalled){
			String pathVersion = AppInfosUtil.getApkVersionByPath(mActivity, path);
			String packageVersion = AppInfosUtil.getApkVersionByPackageName(mActivity, getListenPackageName());
			if(AppInfosUtil.isAppNewVersion(pathVersion, packageVersion)<0){
				CmdUtil.unInstallApk(getListenPackageName());
			}
		}
		boolean result = CmdUtil.installApk(mActivity, path);
		if(!result){
			if(isInstalled){
				CmdUtil.unInstallApk(getListenPackageName());
				boolean install2 = CmdUtil.installApk(mActivity, path);
				if(!install2){
					installErr(path);
				}
			}else {
				installErr(path);
			}
		}
	}
	
	private void installErr(String path){
		viewInterface.toastBig(mActivity.getString(R.string.tips_install_err));
		EasyClickUtil.setIsTaskRunning(mActivity, EasyClickUtil.TASK_NOT_RUNNING);
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
		app.setIsRunning(false);
		Logger.i("------isRunning-auto 401-"+EasyClickUtil.getIsTaskRunning(mActivity));
		mActivity.finish();
		mActivity.overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);
	}
	/**
	 * 关闭应用等操作	
	 */
	private void handleAutoRunOpt() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				startRunOpt();
			}
		}).start();
	}
	protected void startRunOpt() {
		try {//关闭
			Set<OptAppMode> set = getClosedListApkMode();
			viewInterface.notifyAutoListMainThread(set);
			viewInterface.setMyLeftTitleMainThread(mActivity.getString(R.string.auto_close_apk));
			CloseAppOperation closeAppOperation = new CloseAppOperation(mActivity);
			closeAppOperation.setCloseApks(getClosedListApkMode());
			closeAppOperation.closeApk();
		} catch (Exception e) {
			Logger.i("关闭 Exception"+e);
			e.printStackTrace();
		}
		try {//清除数据
			viewInterface.notifyAutoListMainThread(getClearListApkMode());
			viewInterface.setMyLeftTitleMainThread(mActivity.getString(R.string.auto_clear_apk_data));
			ClearAppOperation appOperation =new ClearAppOperation(mActivity,viewInterface);
			appOperation.clearApp();
		} catch (Exception e) {
			Logger.i("清除数据  Exception=="+e);
			e.printStackTrace();
		}
		try {//恢复系统值
			viewInterface.setMyLeftTitleMainThread(mActivity.getString(R.string.auto_recover_sys_value));
			SystemValueOperation appOperation =new SystemValueOperation(mActivity,listenApkMode,viewInterface);
			appOperation.recoverSysValue();
			MyfileUtil.deleteFile(FileSysOptRecordToFile.RECORD_SYSTEM_OPT_PATH);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		try{//删除系统账号
			viewInterface.setMyLeftTitleMainThread(mActivity.getString(R.string.auto_delete_accounts));
			AccountManager accountManager = AccountManager.get(mActivity);
			Account[] accounts = accountManager.getAccounts();
			for (int i = 0; i < accounts.length; i++) {
				final Account account = accounts[i];
				Logger.i("account======"+account.toString()+"   ");
				if(!account.type.equals("miui.yellowpage")){
					accountManager.removeAccount(account, null, null);
				}
			}
		}catch(Exception e){
			Logger.i("---------"+e);
			e.printStackTrace();
//			"cn.ninegame.modules.account.authsync.sync.auth"
		}
		try {//删除已安装的应用
			viewInterface.setMyLeftTitleMainThread(mActivity.getString(R.string.auto_uninstall_all_apks));
//			SystemValueOperation appOperation =new SystemValueOperation(mActivity,listenApkMode,viewInterface);
//			appOperation.recoverSysValue();
			UnInstallRecordApks operation = new UnInstallRecordApks(mActivity);
			operation.unInstallAllApks();
			MyfileUtil.deleteFile(ConstantsHookConfig.PATH_RECORD_APK_INSTALL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {//删除文件
			if(listenSet!=null&&listenApkMode!=null){
				listenApkMode.setFlag(OptAppMode.FLAG_DELETE_FILE);
				listenApkMode.setFileErrSize(-1);
				listenApkMode.setFolderErrSize(-1);
				listenSet.clear();
				listenSet.add(listenApkMode);
			}
			viewInterface.notifyAutoListMainThread(listenSet);
			viewInterface.setMyLeftTitleMainThread(mActivity.getString(R.string.auto_delete_file));
			DeleteFileOperation appOperation = new DeleteFileOperation(mActivity,listenSet,viewInterface);
			appOperation.deleteFile();
			MyfileUtil.deleteFile(FileSysOptRecordToFile.RECORD_FILE_OPT_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean isVpn = SPrefHookUtil.getTaskBoolean(mActivity, SPrefHookUtil.KEY_TASK_VPN_AUTO_CONN,SPrefHookUtil.D_TASK_VPN_AUTO_CONN);
		final boolean isNew = SPrefHookUtil.getTaskInt(mActivity, SPrefHookUtil.KEY_TASK_PLAN_TYPE, SPrefHookUtil.D_TASK_PLAN_TYPE)==SPrefHookUtil.D_TASK_PLAN_TYPE?true:false;
		
		if(isVpn&&isNew){
			if(connectVpn(true)){
				generateParam();
			}
		}else {
			disConnectVpn();
			generateParam();
		}
	}
	public void generateParam(){
		try {//生成数据
			if(listenSet!=null&&listenApkMode!=null){
				listenApkMode.setFlag(OptAppMode.FLAG_PARAM);
				listenSet.clear();
				listenSet.add(listenApkMode);
			}
			viewInterface.notifyAutoListMainThread(listenSet);
			viewInterface.setMyLeftTitleMainThread(mActivity.getString(R.string.auto_param_generation));
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(SPrefHookUtil.getCurTaskInt(mActivity, SPrefHookUtil.KEY_CUR_TASK_PLAN_TYPE, SPrefHookUtil.D_TASK_PLAN_TYPE)==SPrefHookUtil.D_TASK_PLAN_TYPE){
						SPrefHookUtil.putSettingBoolean(mActivity, SPrefHookUtil.KEY_SETTING_CURRENT_IS_NEW, true);
						interface1.getParam(getListenPackageName(),false);
					}else {
						SPrefHookUtil.putSettingBoolean(mActivity, SPrefHookUtil.KEY_SETTING_CURRENT_IS_NEW, false);
						HttpUtil2 util2 =new HttpUtil2(mActivity);
						util2.getLiuCunParam();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean connectVpn(boolean isvpnlimit){
		try {
			if(listenSet!=null&&listenApkMode!=null){
				listenApkMode.setFlag(OptAppMode.FLAG_VPN);
				listenSet.clear();
				listenSet.add(listenApkMode);
			}
			viewInterface.notifyAutoListMainThread(listenSet);
			viewInterface.setMyLeftTitleMainThread(mActivity.getString(R.string.auto_connect_vpn));
			EasyClickUtil.setvpnOptWhere(mActivity, EasyClickUtil.AUTO_OPT);
			VpnConnectOperation connectOperation = new VpnConnectOperation(mActivity);
			connectOperation.connectVpn();
			while(!isVpnfinish){
				Thread.sleep(1000);
			}
			boolean isconnected = VpnUtil.isVpnConnected();
			if(!isconnected){
				isVpnfinish = false;
				viewInterface.toastBig(mActivity.getString(R.string.auto_vpn_connect_err));
				if(!VpnUtil.isVpnConnected()){
					EasyClickUtil.setvpnOptWhere(mActivity, EasyClickUtil.AUTO_OPT);
					VpnUtil.operaVpn(mActivity, 1);
					while(!isVpnfinish){
						Thread.sleep(1000);
						if(VpnUtil.isVpnConnected()){
							isVpnfinish = true;;
						}
					}
					boolean isconnected2 = VpnUtil.isVpnConnected();
					if(isconnected2){
						vpnstatus(VPNOK);
						viewInterface.toastBig(mActivity.getString(R.string.auto_vpn_connect_ok)+2);
						return true;
					}else {
						isVpnfinish = false;
						viewInterface.toastBig(mActivity.getString(R.string.auto_vpn_connect_err)+2);
						EasyClickUtil.setvpnOptWhere(mActivity, EasyClickUtil.AUTO_OPT);
						VpnUtil.operaVpn(mActivity, 1);
						while(!isVpnfinish){
							Thread.sleep(1000);
							if(VpnUtil.isVpnConnected()){
								isVpnfinish = true;;
							}
						}
						boolean isconnected3 = VpnUtil.isVpnConnected();
						if(isconnected3){
							vpnstatus(VPNOK);
							viewInterface.toastBig(mActivity.getString(R.string.auto_vpn_connect_ok)+3);
							return true;
						} else {
							viewInterface.toastBig(mActivity.getString(R.string.auto_vpn_connect_err) + 3);
							if (isvpnlimit) {
								isVpnfinish = false;
								viewInterface.setMyLeftTitleMainThread(mActivity.getString(R.string.auto_connect_vpn)+ "4");
								EasyClickUtil.setvpnOptWhere(mActivity,EasyClickUtil.AUTO_OPT);
								VpnUtil.operaVpn(mActivity, 1);
								int vpncount = 0;
								while (!VpnUtil.isVpnConnected()&& vpncount <= 60) {
									Thread.sleep(1000);
									vpncount++;
								}
								if (!VpnUtil.isVpnConnected()) {
									MyfileUtil myfileUtil = new MyfileUtil();
									myfileUtil.recordLog(myfileUtil.vpnConnectErr,getListenPackageName());
									if (isvpnlimit) {
										SendBroadCastUtil.startRun(mActivity);
										EasyClickUtil.setIsTaskRunning(mActivity,EasyClickUtil.TASK_NOT_RUNNING);
										app.setIsRunning(false);
										Logger.i("--isRunning-auto 723-"+ EasyClickUtil.getIsTaskRunning(mActivity));
										mActivity.finish();
										mActivity.overridePendingTransition(
												R.anim.slide_left_in,
												R.anim.slide_right_out);
									}
									CmdUtil.killProcess(ConstantsHookConfig.SETTINGS);
									vpnstatus(VPNERR);
									return false;
								} else {
									vpnstatus(VPNOK);
									CmdUtil.killProcess(ConstantsHookConfig.SETTINGS);
									return true;
								}
							} else {
								MyfileUtil myfileUtil = new MyfileUtil();
								myfileUtil.recordLog(myfileUtil.vpnConnectErr,getListenPackageName());
								CmdUtil.killProcess(ConstantsHookConfig.SETTINGS);
								vpnstatus(VPNERR);
							}
						}
					}
				}else {
					vpnstatus(VPNOK);
					viewInterface.toastBig(mActivity.getString(R.string.auto_vpn_connect_ok));
				}
			}else {
				vpnstatus(VPNOK);
				viewInterface.toastBig(mActivity.getString(R.string.auto_vpn_connect_ok));
				
			}
			CmdUtil.killProcess(ConstantsHookConfig.SETTINGS);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public final static int VPNERR = 0;
	public final static int VPNOK = 1;
	public void vpnstatus(final int status){
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
					HttpUtil2 util2 =new HttpUtil2(mActivity);
					util2.uploadVPNStatus(status);
//					util2.setListener(new L)
			}
		});
	}
	/**
	 * @return
	 */
	public Set<OptAppMode> getClosedListApkMode(){
		Set<OptAppMode> optList=new HashSet<OptAppMode>();//clear();
		putApkMode(optList, getListenPackageName(),OptAppMode.FLAG_CLOSE,false);
		Set<PackageInfo> running = AppInfosUtil.getRunningPackageInfo(mActivity);
		for(PackageInfo run:running){
			OptAppMode mode2 = new OptAppMode();
			mode2.setFlag(OptAppMode.FLAG_CLOSE);
			mode2.setPackageInfo(run);
			optList.add(mode2);
		}
		return optList;
	}
	/**
	 * @return
	 */
	public Set<OptAppMode> getClearListApkMode(){
		Set<OptAppMode> optList=new HashSet<OptAppMode>();
		putApkMode(optList, getListenPackageName(),OptAppMode.FLAG_CLEAR,true);
		putApkMode(optList, ConstantsHookConfig.DOWNLOADER_PACKAGE_NAME,OptAppMode.FLAG_CLEAR,true);
		putApkMode(optList, ConstantsHookConfig.BROWSER_PACKAGE_NAME,OptAppMode.FLAG_CLEAR,true);
		return optList;
	}
	public Set<OptAppMode> getUninstallListApkMode(){
		Set<OptAppMode> optList=new HashSet<OptAppMode>();
		putApkMode(optList, getListenPackageName(),OptAppMode.FLAG_UNINSTALL,false);
		return optList;
	}
	/**
	 * @param optList
	 * @param mode
	 */
	public void putApkMode(Set<OptAppMode> optList,String packageName,int flag,boolean dataSize){
		OptAppMode mode = new OptAppMode();
		mode.setFlag(flag);
		if(dataSize){
			mode.setDataSize(MyfileUtil.getDataSize2(mActivity,packageName));
		}
		PackageInfo packageInfo = null;
		try {
			packageInfo =pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(packageInfo!=null){
			mode.setPackageInfo(packageInfo);
			optList.add(mode);
		}
	}
	public OptAppMode getApkMode(String packageName){
		OptAppMode mode = null;
		PackageInfo packageInfo = null;
		try {
			packageInfo =pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(packageInfo!=null){
			mode = new OptAppMode();
			mode.setPackageInfo(packageInfo);
		}
		return mode;
	}
	class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(final Context context, final Intent intent) {
			Logger.i("*************************"+intent.getAction());
			if(intent.getAction().equals(ConstantsConfig.ACTION_PARAM_BROADCAST)){
				String info = intent.getExtras().getString(ConstantsConfig.INTENT_PARAM);  
				 boolean save = intent.getBooleanExtra(ConstantsConfig.INTENT_SAVE, true);
				Gson gson =new Gson();
				ParamEntity paramEntity = gson.fromJson(info, ParamEntity.class);
				if(!save){
					String contact = paramEntity.getNote();
					if(contact.contains("{")){
						List<ContactMode> contacts = gson.fromJson(contact, new TypeToken<List<ContactMode>>(){}.getType()); 
						getContacts(contacts,"",false,intent);
					}else {
						getContacts(null,paramEntity.getNote(),true,intent);
					}
				 }
				boolean isVpn = SPrefHookUtil.getTaskBoolean(mActivity, SPrefHookUtil.KEY_TASK_VPN_AUTO_CONN,SPrefHookUtil.D_TASK_VPN_AUTO_CONN);
				final boolean isNew = SPrefHookUtil.getTaskInt(mActivity, SPrefHookUtil.KEY_TASK_PLAN_TYPE, SPrefHookUtil.D_TASK_PLAN_TYPE)==SPrefHookUtil.D_TASK_PLAN_TYPE?true:false;
				if (isVpn && !isNew) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							vpnOK = false;
							if(!VpnUtil.isVpnConnected()){
								connectVpn(false);
							}
							getParamOk(context, intent,true,VPN);
						}
					}).start();
				}else {
					getParamOk(context, intent,false,NEW);
				}
			}else if(intent.getAction().equals(ConstantsHookConfig.ACTION_EXE_AUTO_RUN)){
				if(dialog2!=null&&dialog2.isShowing())
					dialog2.dismiss();
				SendBroadCastUtil.listenApp(/*ConstantsHookConfig.FLAG_NEW_LISTNER_APP,*/ context,true);
				handleAutoRunOpt();
			}else if(intent.getAction().equals(ConstantsHookConfig.ACTION_VPN_RECEVIER)){
				Vpncount++;
				isVpnfinish = true;
			}else if (intent.getAction().equals(ConstantsHookConfig.ACTION_GET_LIUCUN_PARAM_ERR)) {
				 isRunning = false;
				 app.setIsRunning(false);
				 EasyClickUtil.setIsTaskRunning(mActivity, EasyClickUtil.TASK_NOT_RUNNING);
				 Logger.i("---------app.setIsRunning===-"+app.getIsRunning());
				 mActivity.finish();
				 mActivity.overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);
			}
		}
	}
	public List<ContactMode> contactsC = new ArrayList<ContactMode>();
	private void getContacts(final List<ContactMode> contacts,final String contactStr1,final boolean isFromDb, final Intent intent){
		Random random = new Random();
		final int messageCount = 5+random.nextInt(45);
		final int recentCount = 5+random.nextInt(45);
		final RandomContactsUtil  randomContactsUtil = new RandomContactsUtil(mActivity);
		if(isFromDb){
			String[] arr = contactStr1.split(",");
		    List<Integer> ids = new ArrayList<Integer>();
		    for (int i = 0; i < arr.length; i++) {
		    	ids.add(Integer.valueOf(arr[i]));
			}
			final List<ContactMode> contactModes =randomContactsUtil.RandomConstacts(ids);
			contactsC = contactModes;
		}else {
			contactsC = contacts;
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				contactOk = false;
				randomContactsUtil.deleteAllContacts();
				randomContactsUtil.insertAllContact(contactsC, new InsertCallback() {
					@Override
					public void onCallback(boolean result) {
						getParamOk(mActivity, intent, true, CONTACTS);
					}
				});
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				messageOK = false;
				RandomMessageUtil randomMessageUtil = new RandomMessageUtil(mActivity);
				randomMessageUtil.deleteAllMessage();
				randomMessageUtil.insertMessage(randomMessageUtil.randomMessages(messageCount, contactsC),new InsertCallback() {
					@Override
					public void onCallback(boolean result) {
						getParamOk(mActivity, intent, true, MESSAGE);
					}
				});
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				recentOk = false;
				RandomRecentUtil randomRecentUtil = new RandomRecentUtil(mActivity);
				randomRecentUtil.deleteAllrecentCalls();
				randomRecentUtil.insertRecentCalls(randomRecentUtil.randomRecentCalls(recentCount, contactsC),new InsertCallback() {
					@Override
					public void onCallback(boolean result) {
						getParamOk(mActivity, intent, true, RECENT);
					}
				});
			}
		}).start();
	}
	public static final int NEW = 0;
	public static final int VPN = 1;
	public static final int CONTACTS = 2;
	public static final int MESSAGE = 3;
	public static final int RECENT = 4;
	public boolean vpnOK=false,contactOk=false,messageOK=false,recentOk=false;
	public void getParamOk(Context context,Intent intent,boolean liucun,int result){
		 if(!liucun){
			 getParamOk2(context, intent);
		 }else {
			switch (result) {
			case VPN:
				Logger.i("######################################==VPN");
				vpnOK = true;
				break;
			case CONTACTS:
				Logger.i("######################################==CONTACTS");
				contactOk = true;
				break;
			case MESSAGE:
				Logger.i("######################################==MESSAGE");
				messageOK = true;
				break;
			case RECENT:
				Logger.i("######################################==RECENT");
				recentOk = true;
				break;
			}
			if(vpnOK&&contactOk&&messageOK&&recentOk){
				getParamOk2(context, intent);
			}
		}
	}
	public void getParamOk2(Context context,Intent intent){
		 isRunning = false;
		 String info = intent.getExtras().getString(ConstantsConfig.INTENT_PARAM);  
		 boolean save = intent.getBooleanExtra(ConstantsConfig.INTENT_SAVE, true);
		 boolean onlyParam = intent.getBooleanExtra(ConstantsConfig.INTENT_ONLY_PARAM, false);
		 SendBroadCastUtil.paramOk2(context, info, save,onlyParam);
		 mActivity.finish();
		 mActivity.overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);
		 OpenActivityUtil.startMainActivity(mActivity);
	}
	public void onDestory(){
		timeCount = 0;
		isRunning = false;
		if(dialog2!=null&&dialog2.isShowing())
			dialog2.dismiss();
		if(httpUtil!=null){
			httpUtil.setListener(null);
			httpUtil.cancleDown();
		}
		httpUtil = null;
		mActivity.unregisterReceiver(myReceiver);
		EventBus.getDefault().unregister(this);
	}
	private void checkScript() {
		String scriptName = SPrefHookUtil.getCurTaskStr(mActivity, SPrefHookUtil.KEY_CUR_TASK_SCRIPT_NAME);//SPrefHookUtil.getTaskStr(mActivity, SPrefHookUtil.KEY_TASK_SCRIPT_NAME,SPrefHookUtil.D_TASK_SCRIPT_NAME);
		boolean netDebug = SPrefHookUtil.getSettingBoolean(mActivity,SPrefHookUtil.KEY_SETTING_NET_DEBUG,SPrefHookUtil.D_SETTING_NET_DEBUG);
		String scriptVer = SPrefHookUtil.getTaskStr(mActivity, SPrefHookUtil.KEY_TASK_SCR_VER);
		final String packageName = SPrefHookUtil.getSettingStr(mActivity, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
		final String name = scriptName+HttpConstants.SCRIPT_SUFFIX;
		String scriptFilePath = ConstantsHookConfig.SCRIPT_FILE+name;
		File dir = FileUtil.checkExtraDir(ConstantsConfig.DOWNLOAD_SCRIPT_PATH_NAME);
		final File file = new File(dir,name);
		if(!netDebug){
			if(!file.exists()){
				viewInterface.toastBig(mActivity.getString(R.string.auto_no_script_can_use));
			}
			handleInstallUnInstall();
		}else {
			if(file.exists()&&scriptVer.equals(MD5Util.md5sum(scriptFilePath).toLowerCase())){
				handleInstallUnInstall();
				return;
			}
			else{
				mActivity.runOnUiThread(new Runnable() {
					public void run() {
						HttpUtil2 httpUtil = new HttpUtil2(mActivity);
						Map<String, String> map = new HashMap<String, String>();
						String actionUrl = SPrefHookUtil.getTaskStr(mActivity, SPrefHookUtil.KEY_TASK_SCRIPT_URL);
						map.put(HttpConstants.REQUEST_KEY_PACKAGENAME, packageName);
						Logger.i("actionUrl==="+actionUrl);
						map.put(HttpConstants.REQUEST_KEY_URL, actionUrl);
						map.put(HttpConstants.REQUEST_KEY_PATH, file.getAbsolutePath());
						httpUtil.downBigFileFromServer(mActivity, map, false);
						showDowndialog(mActivity.getString(R.string.auto_downLoad_title_script));
						httpUtil.setListener(new ResponseListener() {
							@Override
							public void result(Object obj) {
								dismissDownDialog();
								if (obj instanceof String&&!obj.equals(HttpConstants.RESPONSE_OK)) {
									//下载不成功 提示 进一步操作
									dismissDownDialog();
									viewInterface.toastBig(mActivity.getString(R.string.auto_tip_script_downLoad_err));
									SendBroadCastUtil.startRun(mActivity);
									EasyClickUtil.setIsTaskRunning(mActivity, EasyClickUtil.TASK_NOT_RUNNING);
									app.setIsRunning(false);
									Logger.i("--isRunning-auto 271-"+EasyClickUtil.getIsTaskRunning(mActivity));
									mActivity.finish();
									mActivity.overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);
								
								}else {
									new Thread(new Runnable() {
										@Override
										public void run() {
											handleInstallUnInstall();
										}
									}).start();
								}
								
							}
						});
					}
				});
			}
		}
		
	}
	/**
	 * 下载dialog
	 */
	ProgressDialog ddialog = null;//下载dialog
	public void showDowndialog(String title){
		ddialog = new ProgressDialog(mActivity);
		ddialog.setProgressNumberFormat("%1d KB/%2d KB");
		ddialog.setTitle(title);
		ddialog.setMessage(mActivity.getString(R.string.auto_downLoad_message));
		ddialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		ddialog.setCancelable(false);
		ddialog.show();
	}
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventMainThread(ProgressEvent event) {
		if(ddialog!=null&&ddialog.isShowing()){
			ddialog.setMax(event.getTotal());
			ddialog.setProgress(event.getProgress());
			if(event.getTotal()<0&&event.getProgress()<0){
				if(ddialog!=null&&ddialog.isShowing()){
					ddialog.dismiss();
				}
//				viewInterface.toast(mActivity.getString(R.string.left_version_down_err));
			}
			if(event.getProgress()>=event.getTotal()){
				if(ddialog!=null&&ddialog.isShowing()){
					ddialog.dismiss();
				}
			}
		}
	}
	public void dismissDownDialog(){
		if(ddialog!=null&&ddialog.isShowing()){
			ddialog.dismiss();
		}
	}
	/**
	 * 安装 dialog
	 */
	private void showDialog(){
		if(weakReferenceActivity.get()!=null&&!weakReferenceActivity.get().isFinishing()){
			dialog2 = new ProgressDialog(weakReferenceActivity.get());
			dialog2.setTitle(mActivity.getString(R.string.title_installing));
			dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog2.setCancelable(false);
			dialog2.show();
		}
	}
}
