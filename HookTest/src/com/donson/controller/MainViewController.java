package com.donson.controller;

import imei.util.IMEIGET;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Http.HttpUtil2;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.VpnService;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.HttpConstants;
import com.donson.config.Logger;
import com.donson.myhook.bean.AppInfosMode;
import com.donson.myhook.bean.DataMode;
import com.donson.myhook.bean.DisplayMode;
import com.donson.myhook.bean.SimInfosMode;
import com.donson.myhook.bean.WifiMode2;
import com.donson.myhook.services.MyInternetService;
import com.donson.myhook.services.ScriptService;
import com.donson.operation.ScriptRunOperation;
import com.donson.realparam.utils.BuildUtil;
import com.donson.realparam.utils.ConvertUtil;
import com.donson.realparam.utils.DisplayUtil;
import com.donson.realparam.utils.GetLocationUtil;
import com.donson.realparam.utils.InnerIPUtil;
import com.donson.realparam.utils.NetUtil;
import com.donson.realparam.utils.OtherUtil;
import com.donson.realparam.utils.P2P0MACUtil;
import com.donson.realparam.utils.ProviderUtil;
import com.donson.realparam.utils.TelephonyUtil;
import com.donson.realparam.utils.WifiUtil;
import com.donson.utils.AppInfosUtil;
import com.donson.utils.CmdUtil;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.MyfileUtil;
import com.donson.utils.OpenActivityUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.SendBroadCastUtil;
import com.donson.utils.VpnUtil;
import com.donson.viewinterface.MainViewInterface;
import com.donson.xxxiugaiqi.R;
import com.donson.zhushoubase.BaseApplication;
import com.donson.zhushoubase.BroadcastType;
import com.google.gson.Gson;
import com.param.bean.ParamEntity;
import com.param.config.ConstantsConfig;
import com.param.config.SPrefUtil;
import com.param.netInterface.HttpUtil.ResponseListener;

public class MainViewController {
	private ParamAdbShellApkCBroadcastReceiver adbReceiver = null;
	private MainViewInterface viewInterface;
	private Activity mContext;
	private PackageManager pm; 
	LeftListController controller;
	BaseApplication app;
	
	public MainViewController(Activity context,MainViewInterface viewInterface) {
		this.viewInterface = viewInterface;
		this.mContext = context;
		pm = context.getPackageManager();
		app = (BaseApplication) mContext.getApplication();
		controller = new LeftListController(mContext,viewInterface);
	}
	/**
	 * 一键操作点击
	 */
	public void setEasyClick() {// 是否受限制
		Logger.i("################:");
		updateCurrentTask();
		mContext.stopService(new Intent(mContext, ScriptService.class));
		EasyClickUtil.setScriptIsRunning(mContext,EasyClickUtil.SCRIPT_NOT_RUNNING);
		if (!checkRootXposed()) {
			viewInterface.toastBig(mContext.getString(R.string.main_no_use));
			EasyClickUtil.setIsTaskRunning(mContext,EasyClickUtil.TASK_NOT_RUNNING);
			app.setIsRunning(false);
			Logger.i("--isRunning-auto 102"+ EasyClickUtil.getIsTaskRunning(mContext));
			return;
		}
		if (TextUtils.isEmpty(getListenPackageName())) {
			if (!VpnUtil.isVpnConnected()) {
				app.setIsRunning(false);
				viewInterface.toastBig(mContext.getString(R.string.main_no_listen_apk));
				return;
			}
		}
		OpenActivityUtil.startAutoOptActivity(mContext);
	}
	/**
	 * 限制
	 * @return
	 */
	protected boolean handleLimit() {
		if(!SPrefHookUtil.getTaskBoolean(mContext, SPrefHookUtil.KEY_TASK_CONTINUOUS, SPrefHookUtil.D_TASK_CONTINUOUS)){
			EasyClickUtil.setIsTaskRunning(mContext, EasyClickUtil.TASK_NOT_RUNNING);
			app.setIsRunning(false);
			Logger.i("------isRunning===main 118"+EasyClickUtil.getIsTaskRunning(mContext));
			viewInterface.toastBig(mContext.getString(R.string.main_no_continue));
			return true;
		}
		if(TextUtils.isEmpty(getListenPackageName())){
			EasyClickUtil.setIsTaskRunning(mContext, EasyClickUtil.TASK_NOT_RUNNING);
			app.setIsRunning(false);
			Logger.i("------isRunning===main 125"+EasyClickUtil.getIsTaskRunning(mContext));
			viewInterface.toastBig(mContext.getString(R.string.main_no_listen_apk));
			return true;
		}
		boolean isTotalLimit = SPrefHookUtil.getSettingBoolean(mContext, SPrefHookUtil.KEY_SETTING_TOTAL_TIMES_LIMIT, SPrefHookUtil.D_SETTING_TOTAL_TIMES_LIMIT);
		if(!isTotalLimit){
			return false;
		}
		int runCurCount = SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME);
		int totalCount = SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_RUN_TIMES, SPrefHookUtil.D_TASK_RUN_TIMES);
		if(runCurCount<totalCount){
			return false;
		}else {
			EasyClickUtil.setIsTaskRunning(mContext, EasyClickUtil.TASK_NOT_RUNNING);
			app.setIsRunning(false);
			Logger.i("------isRunning===auto 171"+EasyClickUtil.getIsTaskRunning(mContext));
			viewInterface.toast(mContext.getString(R.string.main_auto_running_finish));
			 return true;
		}
	}
	public void openMain(){
		OpenActivityUtil.startMainActivity(mContext);
		mContext.stopService(new Intent(mContext, ScriptService.class));
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				disConnectVpn();
				if(uploadParam()){
					limitContinue();;
				}
			}
		}).start();
	}
	/**
	 * 返回是否继续执行
	 * @return
	 */
	protected boolean uploadParam() {
		boolean isnew = SPrefHookUtil.getSettingBoolean(mContext, SPrefHookUtil.KEY_SETTING_CURRENT_IS_NEW, false);
		if(!isnew){
			return true;
		}
		boolean market = EasyClickUtil.getMarketHookFlag(mContext);
		if(market){
			return true;
		}
		String info = SPrefHookUtil.getHookStr(mContext,SPrefHookUtil.KEY_HOOK);
		Logger.i("+++++++TextUtils.isEmpty(info):"+(TextUtils.isEmpty(info)));
		if(TextUtils.isEmpty(info)){
			return true ;
		}
		recordCount2(true,SPrefHookUtil.getCurTaskInt(mContext, SPrefHookUtil.KEY_CUR_TASK_TASK_ID,SPrefHookUtil.D_TASK_TASK_ID),System.currentTimeMillis(),info);
		return false;
	}
	private void recordCount2(final boolean save, final int taskId, Long generationTime, final String info) {
			try{
			viewInterface.setMyTitleMainThread(mContext.getString(R.string.title_main_uploading));
			Date date = new Date(generationTime);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			final String logTime = dateFormat.format(date);
			HttpUtil2 httpUtil2 = new HttpUtil2(mContext);
			int runCount1 = SPrefHookUtil.getCurTaskInt(mContext, SPrefHookUtil.KEY_CUR_TASK_CUR_RUN_TIME, SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME));
			httpUtil2.uploadParamStatus(save,taskId,logTime,++runCount1,info);
			httpUtil2.setListener(new ResponseListener() {	
				@Override
				public void result(Object obj) {
					if(obj instanceof String){
						String result = (String) obj;
						if(HttpConstants.RESPONSE_OK.equals(result)){
							viewInterface.toastBig(mContext.getString(R.string.auto_date_update_ok));
							String curPlanId = SPrefHookUtil.getCurTaskStr(mContext, SPrefHookUtil.KEY_CUR_TASK_PLAN_ID);
							String planId = SPrefHookUtil.getTaskStr(mContext, SPrefHookUtil.KEY_TASK_PLAN_ID);
							Logger.i("curPlanId  "+curPlanId+"  planId:"+planId+"  "+planId.equals(curPlanId));
							if(curPlanId.equals(planId)){
								int runCount = SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME);
								boolean res = SPrefHookUtil.putTaskInt(mContext, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, runCount+1);
								int runCount3 = SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME);
								Logger.i("***:"+res+"runCount:"+runCount+" cou:"+runCount3);
							}
							limitContinue();
						}else {
							viewInterface.toastBig(mContext.getString(R.string.auto_date_update_err),R.color.red);
							HttpUtil2 httpUtil = new HttpUtil2(mContext);
							int runCount2 = SPrefHookUtil.getCurTaskInt(mContext, SPrefHookUtil.KEY_CUR_TASK_CUR_RUN_TIME, SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME));
							httpUtil.uploadParamStatus(save,taskId,logTime,++runCount2,info);
							httpUtil.setListener(new ResponseListener() {
								
								@Override
								public void result(Object obj) {
									String result2 = (String) obj;
									if(HttpConstants.RESPONSE_OK.equals(result2)){
										viewInterface.toastBig(mContext.getString(R.string.auto_date_update_ok)+2);
										String curPlanId = SPrefHookUtil.getCurTaskStr(mContext, SPrefHookUtil.KEY_CUR_TASK_PLAN_ID);
										String planId = SPrefHookUtil.getTaskStr(mContext, SPrefHookUtil.KEY_TASK_PLAN_ID);
										if(curPlanId.equals(planId)){
											int runCount = SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME);
											boolean res = SPrefHookUtil.putTaskInt(mContext, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, ++runCount);
											int runCount3 = SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME);
											Logger.i("***   :"+res+"runCount:"+runCount+" cou:"+runCount3);
										}
										limitContinue();
									}else {
										viewInterface.toastBig(mContext.getString(R.string.auto_date_update_err)+2,R.color.red);
										MyfileUtil myfileUtil = new MyfileUtil();
										myfileUtil.recordLog(myfileUtil.paramuploadErr,getCurListenPackageName());
										limitContinue();
									}
								}
							});
						}
					}
				}
			});
			}
			catch(Exception e){
				Logger.i("Ex:"+e);
				e.printStackTrace();
			}
	}
	public void limitContinue() {
		viewInterface.setMyTitleMainThread(mContext.getString(R.string.title_main));
		if(!handleLimit())
			setEasyClick();
	}
	/**
	 * 更新当前任务
	 */
	private void updateCurrentTask() {
		SPrefHookUtil.putCurTaskStr(mContext, SPrefHookUtil.KEY_CUR_PACKAGE_NAME, getListenPackageName());//
		SPrefHookUtil.putCurTaskStr(mContext, SPrefHookUtil.KEY_CUR_TASK_SCRIPT_NAME, SPrefHookUtil.getTaskStr(mContext, SPrefHookUtil.KEY_TASK_SCRIPT_NAME));
		SPrefHookUtil.putCurTaskInt(mContext,SPrefHookUtil.KEY_CUR_TASK_SCRIPT_TIME,SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_SCRIPT_TIME, 0));
		SPrefHookUtil.putCurTaskInt(mContext, SPrefHookUtil.KEY_CUR_TASK_TASK_ID, SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_TASK_ID,SPrefHookUtil.D_TASK_TASK_ID));
		SPrefHookUtil.putCurTaskStr(mContext, SPrefHookUtil.KEY_CUR_TASK_ORDER_ID, SPrefHookUtil.getTaskStr(mContext, SPrefHookUtil.KEY_TASK_ORDER_ID));
		SPrefHookUtil.putCurTaskStr(mContext, SPrefHookUtil.KEY_CUR_TASK_PLAN_ID, SPrefHookUtil.getTaskStr(mContext, SPrefHookUtil.KEY_TASK_PLAN_ID));
		SPrefHookUtil.putCurTaskInt(mContext, SPrefHookUtil.KEY_CUR_TASK_PLAN_TYPE, SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_PLAN_TYPE,SPrefHookUtil.D_TASK_PLAN_TYPE));
		SPrefHookUtil.putCurTaskInt(mContext, SPrefHookUtil.KEY_CUR_TASK_CUR_RUN_TIME, SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, 0));
	Logger.i("++++++++++++++++++++++++++++++++++  updateCurrentTask"+SPrefHookUtil.getCurTaskInt(mContext, SPrefHookUtil.KEY_CUR_TASK_CUR_RUN_TIME, SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME)));
	}

	protected void disConnectVpn() {
		Logger.i("=========vpnConnected=11===="+VpnUtil.isVpnConnected());
		if(VpnUtil.isVpnConnected1()){
			viewInterface.setMyTitleMainThread(mContext.getString(R.string.title_main_diconnect_vpn));
			Logger.i("=========vpnConnected=====");
			EasyClickUtil.setvpnOptWhere(mContext, EasyClickUtil.MAIN_DISCONNECT);
			OpenActivityUtil.openVpnDisConnect(mContext);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			EasyClickUtil.setvpnConnectFlag(mContext, EasyClickUtil.NOT_CONNECT_VPN);
			if(!VpnUtil.isVpnConnected()){
				viewInterface.toastBig(mContext.getString(R.string.auto_disconnect_vpn));
			}
		}else {
			return;
		}
	}
	public void init() {
		adbReceiver = new ParamAdbShellApkCBroadcastReceiver();
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(ConstantsHookConfig.ACTION_PARAM_OK2);
		myIntentFilter.addAction(ConstantsHookConfig.ACTION_LISTNER_APP);
		myIntentFilter.addAction(BroadcastType.ShellExecStart.getValue());
		myIntentFilter.addAction(BroadcastType.ShellExecFinish.getValue());
		myIntentFilter.addAction(ConstantsHookConfig.ACTION_START_RUN);
		myIntentFilter.addAction(ConstantsHookConfig.ACTION_SHELL_EXE_ERR);
		myIntentFilter.addAction(ConstantsHookConfig.ACTION_LOGOUT);
		mContext.registerReceiver(adbReceiver, myIntentFilter);
//		dbDao = LocalDbDao.getInstance(mContext);
		try{
			notifyRealParam(mContext);
		}catch(Exception e){
			e.printStackTrace();
		}
		checkListen();
		checkRootXposed();
		controller.initSlidMenue();
	}
	private boolean checkRootXposed() {
		boolean isRoot=CmdUtil.isRoot();
		Logger.h("EasyClickUtil.getXposedUsedFlag(mContext)==="+EasyClickUtil.getXposedUsedFlag(mContext));
		if(!CmdUtil.isRoot()){
//			ShowDialog(mContext.getString(R.string.tips_need_root));
			viewInterface.showNoUseTips(true, mContext.getString(R.string.main_no_root));
			viewInterface.easyClickable(false);
			return false;
		}
		//如果Xposed 不起作用
		else if(TextUtils.isEmpty(EasyClickUtil.getXposedUsedFlag(mContext))||!EasyClickUtil.getXposedUsedFlag(mContext).equals(EasyClickUtil.XPOSED_USED)){
			viewInterface.showNoUseTips(true, mContext.getString(R.string.main_xposed_nouse));
			viewInterface.easyClickable(false);
			return false;
		}else {
			viewInterface.showNoUseTips(false,"");
			viewInterface.easyClickable(true);
			return true;
		}
	}
	private void ShowDialog(String string) {
		AlertDialog dialog = new AlertDialog.Builder(mContext)
				.setTitle(string)
				.setPositiveButton(mContext.getString(R.string.btn_ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create();
		dialog.show();
	}
	public String getListenPackageName(){
		return SPrefHookUtil.getSettingStr(mContext,SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
	}
	public String getCurListenPackageName(){
		return SPrefHookUtil.getCurTaskStr(mContext,SPrefHookUtil.KEY_CUR_PACKAGE_NAME);
	}
	public void checkListen(){
		String listen = SPrefHookUtil.getSettingStr(mContext, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
		if(!TextUtils.isEmpty(listen)){
			String packageName = listen;
			AppInfosMode appInfosMode = AppInfosUtil.getAppInfosModebyPackageName(mContext, packageName);
			String appName = AppInfosUtil.getAppNameByMode(appInfosMode, pm);
			Drawable icon =AppInfosUtil.getAppIconByMode(appInfosMode, pm);
			viewInterface.setListenBtnVisible(false);
			if(TextUtils.isEmpty(appName)&&icon == null){
				appName = mContext.getString(R.string.tips_no_use);
				icon = mContext.getResources().getDrawable(R.drawable.no_use);
			}
			viewInterface.setListenPart(packageName, icon, appName);
		}else {
			viewInterface.setListenBtnVisible(true);
		}
	}
	/**
	 * 保存参数
	 * @param info
	 * @param onlyParam 
	 */
	public void saveParam(String info,boolean save, boolean onlyParam) {
		Gson gson =new Gson();
		ParamEntity paramEntity = gson.fromJson(info, ParamEntity.class);
		Logger.i(paramEntity.toString());
		if(save){
			paramEntity.setGeneration_time(System.currentTimeMillis());
			paramEntity.setTask_id(SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_TASK_ID,SPrefHookUtil.D_TASK_TASK_ID));
		}
		if(!onlyParam){
		//不会上传时间
//			recordCount(save,paramEntity.getTask_id(),paramEntity.getGeneration_time(),info);
			SPrefHookUtil.putHookStr(mContext,info, SPrefHookUtil.KEY_HOOK);
			openApk();
		}else {
			SPrefHookUtil.putHookStr(mContext,info, SPrefHookUtil.KEY_HOOK);
		}
	}
	public void openApk() {
		if(EasyClickUtil.getMarketHookFlag(mContext)){
			OpenActivityUtil.openMarket(mContext);
//			SendBroadCastUtil.sendMarketBroadcast(mContext);
			return;
		}
		if(SPrefHookUtil.getSettingBoolean(mContext, SPrefHookUtil.KEY_SETTING_OPEN_APK, SPrefHookUtil.D_SETTING_OPEN_APK))
			OpenActivityUtil.openApkByPackageName(mContext, getCurListenPackageName());
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
//					Logger.i("------");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				new ScriptRunOperation(mContext);
			}
		}).start();
	}
	/**
	 * 记录运行条数
	 * @param save
	 * @param generationTime 
	 * @param taskId 
	 * @param info 
	 */
//	private void recordCount(final boolean save, final int taskId, Long generationTime, final String info) {
////		int runCount = SPrefHookUtil.getSettingInt(context, SPrefHookUtil.KEY_SETTING_CUR_RUN_TIME, SPrefHookUtil.D_SETTING_CUR_RUN_TIME);
////		boolean re = SPrefHookUtil.putSettingInt(context, SPrefHookUtil.KEY_SETTING_CUR_RUN_TIME, runCount+1);
//		if(!save){
//			int runLiucunCount = SPrefUtil.getInt(context, SPrefUtil.C_RUN_LIU_CUN_COUNT, SPrefUtil.D_RUN_LIU_CUN_COUNT);
//			SPrefUtil.putInt(mContext, SPrefUtil.C_RUN_LIU_CUN_COUNT, runLiucunCount+1);
//		}
//		if(save){
//			try{
//			Date date = new Date(generationTime);
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			final String logTime = dateFormat.format(date);
//			HttpUtil2 httpUtil2 = new HttpUtil2(mContext);
//			httpUtil2.uploadParamStatus(save,taskId,logTime,info);
//			httpUtil2.setListener(new ResponseListener() {
//				
//				@Override
//				public void result(Object obj) {
//					if(obj instanceof String){
//						String result = (String) obj;
//						if(HttpConstants.RESPONSE_OK.equals(result)){
//							int runCount = SPrefHookUtil.getTaskInt(mContext, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME);
//							boolean re = SPrefHookUtil.putTaskInt(mContext, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, runCount+1);
//						}else {
//							HttpUtil2 httpUtil = new HttpUtil2(mContext);
//							httpUtil.uploadParamStatus(save,taskId,logTime,info);
//						}
//					}
//				}
//			});
//			}
//			catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//	}
	/**
	 * 解除注册
	 */
	public void unregistReceiver() {
		if(adbReceiver!=null){
			mContext.unregisterReceiver(adbReceiver);
		}
		controller.onDestory();
	}
	@SuppressLint("NewApi")
	public void notifyRealParam(Activity mActivity){
		final List<DataMode> list =new ArrayList<DataMode>();
		Location location = GetLocationUtil.getLocation(mActivity);
		
		list.add(new DataMode(mActivity.getString(R.string.longitude), location!=null?location.getLongitude():null, "", true));
		list.add(new DataMode(mActivity.getString(R.string.latitude), location!=null?location.getLatitude():null, "", true));
		list.add(new DataMode(mActivity.getString(R.string.location_accuracy),location!=null?location.getAccuracy():null,"",false));
		CellLocation cellLocation = GetLocationUtil.getcellLocation(mActivity);
		if(cellLocation instanceof GsmCellLocation){
			GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
			list.add(new DataMode(mActivity.getString(R.string.cellid_gms), gsmCellLocation!=null?gsmCellLocation.getCid():null, "", true));
			list.add(new DataMode(mActivity.getString(R.string.lac_gms), gsmCellLocation!=null?gsmCellLocation.getLac():null, "", true));
		}
		if(cellLocation instanceof CdmaCellLocation){
			CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
			list.add(new DataMode(mActivity.getString(R.string.cellid_cdma), cdmaCellLocation!=null?cdmaCellLocation.getBaseStationId():null, "", true));
			list.add(new DataMode(mActivity.getString(R.string.lac_cdma), cdmaCellLocation!=null?cdmaCellLocation.getNetworkId():null, "", true));
			list.add(new DataMode(mActivity.getString(R.string.longitude_cdma), cdmaCellLocation!=null?cdmaCellLocation.getBaseStationLongitude() / 14400:null, "", false));
			list.add(new DataMode(mActivity.getString(R.string.latitude_cdma), cdmaCellLocation!=null?cdmaCellLocation.getBaseStationLatitude() / 14400:null, "", false));
		}
		List<CellInfo> allCellInfo = GetLocationUtil.getAllCellInfo(mActivity);
		HashMap<String, Integer> hashMap = new HashMap<>();
		if (allCellInfo != null)
			for (CellInfo cellInfo : allCellInfo) {
				if (cellInfo instanceof CellInfoGsm) {
					CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
					CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
					int mcc = cellIdentityGsm.getMcc();
					int mnc = cellIdentityGsm.getMnc();
					int cid = cellIdentityGsm.getCid();
					int lac = cellIdentityGsm.getLac();
					String a = "Gsm" + mcc + "a" + mnc + "a" + cid + "a" + lac;
					if (!hashMap.containsKey(a)) {
						hashMap.put(a, 1);
						list.add(new DataMode(mActivity.getString(R.string.mcc_gms), mcc, "", false));
						list.add(new DataMode(mActivity.getString(R.string.mnc_gms), mnc, "", false));
						list.add(new DataMode(mActivity.getString(R.string.cellid_gms), cid, "", false));
						list.add(new DataMode(mActivity.getString(R.string.lac_gms), lac, "", false));
					}
				} else if (cellInfo instanceof CellInfoCdma) {
					CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;
					CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
					int latitude = cellIdentity.getLatitude() / 14400;
					int longitude = cellIdentity.getLongitude() / 14400;
					int networkId = cellIdentity.getNetworkId();
					int basestationId = cellIdentity.getBasestationId();
					int systemId = cellIdentity.getSystemId();
					String a = "Cdma" + latitude + "a" + longitude + "a" + networkId + "a" + basestationId + "a"
							+ systemId;
					if (!hashMap.containsKey(a)) {
						hashMap.put(a, 1);
						list.add(new DataMode(mActivity.getString(R.string.latitude_cdma), latitude, "", false));
						list.add(new DataMode(mActivity.getString(R.string.longitude_cdma), longitude, "", false));
						list.add(new DataMode(mActivity.getString(R.string.cellid_cdma), networkId, "", false));
						list.add(new DataMode(mActivity.getString(R.string.lac_cdma), basestationId, "", false));
						list.add(new DataMode(mActivity.getString(R.string.system_id_cdma), systemId, "", false));
					}
				} else if (cellInfo instanceof CellInfoLte) {
					CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
					CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
					int mcc = cellIdentity.getMcc();
					int mnc = cellIdentity.getMnc();
					int ci = cellIdentity.getCi();
					int tac = cellIdentity.getTac();
					String a = "Lte" + mcc + "a" + mnc + "a" + ci + "a" + tac;
					if (!hashMap.containsKey(a)) {
						hashMap.put(a, 1);
						list.add(new DataMode(mActivity.getString(R.string.cellid_lte), ci, "", false));
						list.add(new DataMode(mActivity.getString(R.string.lac_lte), tac, "", false));
						list.add(new DataMode(mActivity.getString(R.string.mcc_lte), mcc, "", false));
						list.add(new DataMode(mActivity.getString(R.string.mnc_lte), mnc, "", false));
					}
				} else if (cellInfo instanceof CellInfoWcdma) {
					CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
					CellIdentityWcdma cellIdentity = cellInfoWcdma.getCellIdentity();
					int mcc = cellIdentity.getMcc();
					int mnc = cellIdentity.getMnc();
					int cid = cellIdentity.getCid();
					int lac = cellIdentity.getLac();
					String a = "Wcdma" + mcc + "a" + mnc + "a" + cid + "a" + lac;
					if (!hashMap.containsKey(a)) {
						hashMap.put(a, 1);
						list.add(new DataMode(mActivity.getString(R.string.cellid_wcdma), cid, "", false));
						list.add(new DataMode(mActivity.getString(R.string.lac_wcdma), lac, "", false));
						list.add(new DataMode(mActivity.getString(R.string.mcc_wcdma), mcc, "", false));
						list.add(new DataMode(mActivity.getString(R.string.mnc_wcdma), mnc, "", false));
					}
				}
			}
			list.add(new DataMode(mActivity.getString(R.string.inner_ip), InnerIPUtil.getInnerIP(mActivity), "", false));
			list.add(new DataMode(mActivity.getString(R.string.android_id_1), ProviderUtil.getAndroidID(mActivity),"",false));
			list.add(new DataMode(mActivity.getString(R.string.android_id_2), ProviderUtil.getAndroidID2(mActivity),"",false));
			
			list.add(new DataMode(mActivity.getString(R.string.user_agent_1), new WebView(mActivity).getSettings().getUserAgentString(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.user_agent_2), WebSettings.getDefaultUserAgent(mActivity),"",false));
			SimInfosMode simInfosMode = TelephonyUtil.getSimInfosMode(mActivity);

			IMEIGET imeiget = new IMEIGET(mActivity);
			list.add(new DataMode(mActivity.getString(R.string.imei_1), simInfosMode.getImei(),imeiget.getImei(),false));
			list.add(new DataMode(mActivity.getString(R.string.gsf_id), OtherUtil.getGsfAndroidId(mActivity),"",false));
			list.add(new DataMode(mActivity.getString(R.string.sim_serial_1), simInfosMode.getSim_serial(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.phone_number_1), simInfosMode.getPhone_num(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.phone_type_1), simInfosMode.getPhoneType_1() == -1 ? null : ConvertUtil.phoneTypeint2Str(simInfosMode.getPhoneType_1()),"",true));
			list.add(new DataMode(mActivity.getString(R.string.simoperator_name), simInfosMode.getSim_operator_name(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.sim_state_1), simInfosMode.getSim_state() == -1 ? null : simInfosMode.getSim_state(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.icccard_1), TelephonyUtil.getHasIccCard(mActivity),"",true));
			list.add(new DataMode(mActivity.getString(R.string.sim_operator_name_1), simInfosMode.getOperator()/*MccMncConver.n_str2Str(simInfosMode.getOperator())*/,"",true));
			list.add(new DataMode(mActivity.getString(R.string.sim_operator_id_1), simInfosMode.getSim_operator(),ConvertUtil.mccMnc_str2Str(simInfosMode.getSim_operator()),true));
			list.add(new DataMode(mActivity.getString(R.string.imsi_1), simInfosMode.getSubscriberId(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.sim_operator_country_id), simInfosMode.getSim_country_iso(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.spn_operator_name_1), simInfosMode.getNetwork_operator_name(),"",true));
			WifiMode2 wifiMode2 = WifiUtil.getWifiMode(mActivity);
			list.add(new DataMode(mActivity.getString(R.string.ssid), wifiMode2.getSsid(), "",true));
			list.add(new DataMode(mActivity.getString(R.string.bssid), wifiMode2.getBssid(), "",true));
			list.add(new DataMode(mActivity.getString(R.string.wifi_link_speed), wifiMode2.getLinkSpeed(), "",true));
			list.add(new DataMode(mActivity.getString(R.string.rssi), wifiMode2.getRssi(), "",true));
			list.add(new DataMode(mActivity.getString(R.string.mac), wifiMode2.getMacAddress(), "",true));
			list.add(new DataMode(mActivity.getString(R.string.p2p0_mac), P2P0MACUtil.getP2P0Mac(), "",true));
			list.add(new DataMode(mActivity.getString(R.string.wifi_list), wifiMode2.getWifilist(), "",true));
			list.add(new DataMode(mActivity.getString(R.string.is_wifi_open), WifiUtil.isWifiEnabled(mActivity),"",true));
			list.add(new DataMode(mActivity.getString(R.string.has_available_net),NetUtil.isHasNet(mActivity),"",false));
			list.add(new DataMode(mActivity.getString(R.string.is_mobile_connect),NetUtil.isMobileConnect(mActivity),"",false));
			list.add(new DataMode(mActivity.getString(R.string.is_wifi_connected), NetUtil.isWifiConnect(mActivity),"",false));
			list.add(new DataMode(mActivity.getString(R.string.net_type_class_2g),NetUtil.getNetTypeClass(mActivity),ConvertUtil.netWorkClassint2String(NetUtil.getNetTypeClass(mActivity)),true));
			list.add(new DataMode(mActivity.getString(R.string.net_work_type_1),NetUtil.getNetWorkType1(mActivity),"",false));

			list.add(new DataMode(mActivity.getString(R.string.active_net_info),NetUtil.getActiveNetTypeName(mActivity)+"_"
					+NetUtil.getActiveNetType(mActivity)+"_"+NetUtil.getActiveNetSubtypeName(mActivity)+"_"+NetUtil.getActiveSubNetType(mActivity),
					"NetworkInfo:getTypeName_getType_getSubtypeName_getSubtype",true));
			
			list.add(new DataMode(mActivity.getString(R.string.active_net_state),NetUtil.getNetState(mActivity),"",false));
			list.add(new DataMode(mActivity.getString(R.string.wifi_mobile_net_state),NetUtil.getWifiMobileConnectState(mActivity),"",false));
			
			list.add(new DataMode(mActivity.getString(R.string.build_release_1), BuildUtil.getRelease(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_brand_1), BuildUtil.getBrand(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_model_1), BuildUtil.getModel(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_board), BuildUtil.getBoard(),"",false));

			
			list.add(new DataMode(mActivity.getString(R.string.build_product_1), BuildUtil.getProduct(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_cpu_abi_1), BuildUtil.getCpuAbi(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_cpu_abi2_1), BuildUtil.getCpuAbi2(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_id_1), BuildUtil.getID(),"",true));
			
			list.add(new DataMode(mActivity.getString(R.string.build_display_1), BuildUtil.getDisplay(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_fingerprint_1), BuildUtil.getFingerprint(),"",true));
			
			list.add(new DataMode(mActivity.getString(R.string.build_code_name), BuildUtil.getCodename(),"",false));
			
			list.add(new DataMode(mActivity.getString(R.string.build_manufacture_1), BuildUtil.getManufacturer(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_host_1), BuildUtil.getHost(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_type_1), BuildUtil.getType(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_sdk_1), BuildUtil.getSDK(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_tags_1), BuildUtil.getTags(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_devices_1), BuildUtil.getDevice(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_serial_1), BuildUtil.getSerial(),"",true));
			
			list.add(new DataMode(mActivity.getString(R.string.build_bootloader), BuildUtil.getBootloader(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.build_incremental), BuildUtil.getIncremental(),"",false));
			
			list.add(new DataMode(mActivity.getString(R.string.build_radioVersion_1), BuildUtil.getRadioVersion(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_hardware), BuildUtil.getHardWare(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.build_user_1), BuildUtil.getUser(),"",false));
			
			DisplayMode displayMode = DisplayUtil.getDisplayMode(mActivity);
			DisplayMode displayMode2 = DisplayUtil.getDisplayMode2(mActivity);
			list.add(new DataMode(mActivity.getString(R.string.width), displayMode.getWidth(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.height), displayMode.getHeight(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.display_size), displayMode.getDisplaySize(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.scaled_density_1), displayMode.getScaledDensity(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.scaled_density_2), displayMode2.getScaledDensity(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.width_pixels_1), displayMode.getWidthPixels(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.width_pixels_2), displayMode2.getWidthPixels(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.height_pixels_1), displayMode.getHeightPixels(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.height_pixels_2), displayMode2.getHeightPixels(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.density_dpi_1), displayMode.getDensityDpi(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.density_dpi_2), displayMode2.getDensityDpi(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.density_1), displayMode.getDensity(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.density_2), displayMode2.getDensity(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.xdpi_1), displayMode.getXdpi(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.xdpi_2), displayMode2.getXdpi(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.ydpi_1), displayMode.getYdpi(),"",false));
			list.add(new DataMode(mActivity.getString(R.string.ydpi_2), displayMode2.getYdpi(),"",true));
			
			list.add(new DataMode(mActivity.getString(R.string.bluetooth_mac), OtherUtil.getBluetoothMac(),"",true));
			list.add(new DataMode(mActivity.getString(R.string.bluetooth_name), OtherUtil.getBluetoothName(),"",true));
			
			list.add(new DataMode(mActivity.getString(R.string.cpu_frequency), OtherUtil.getCpuFrequency(),"Root ",false));
			list.add(new DataMode(mActivity.getString(R.string.kernel_version), OtherUtil.getkernelVersion(),"Root",false));
		
//			final DataMode dataModead_1 = new DataMode(mActivity.getString(R.string.ad_id_1), "", "", false);
//			final DataMode dataModead_2 = new DataMode(mActivity.getString(R.string.ad_id_2), "", "", false);
//			AdvertisingIDUtil.getADID1(mActivity,new AdCallback() {
//				@Override
//				public void callback(Message message) {
//					dataModead_1.setValue(message.obj.toString());
//					list.add(dataModead_1);
//					viewInterface.notifyAdapter(list);
//				}
//			});
//			AdvertisingIDUtil.getADID2(mActivity,new AdCallback() {
//				@Override
//				public void callback(Message message) {
//					dataModead_2.setValue(message.obj.toString());
//					list.add(dataModead_1);
//					viewInterface.notifyAdapter(list);
//				}
//			});
			
		viewInterface.notifyAdapter(list);
	}
	
	/**
	 * 打开应用
	 */
	public void openListenChoose() {
		OpenActivityUtil.startListenApkActivity(mContext);
	}
	/**
	 * 
	 * @author Administrator
	 *
	 */
	public class ParamAdbShellApkCBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Logger.m("==================action name is:==================" + action);
			Logger.i("==================action name is:==================" + action);
			if(action.equals(/*ConstantsConfig.ACTION_PARAM_BROADCAST*/ConstantsHookConfig.ACTION_PARAM_OK2)){
				viewInterface.toastBig(context.getString(R.string.param_ok));
				 String info = intent.getExtras().getString(ConstantsConfig.INTENT_PARAM);  
				 boolean save = intent.getBooleanExtra(ConstantsConfig.INTENT_SAVE, true);
				 boolean onlyParam = intent.getBooleanExtra(ConstantsConfig.INTENT_ONLY_PARAM, false);
				 Logger.i(info);
				 saveParam(info,save,onlyParam);
			}
			else if(action.equals(BroadcastType.ShellExecFinish.getValue())){
				openMain();
				
//				if(!VpnUtil.isVpnConnected()&&!SPrefHookUtil.getSettingBoolean(context, SPrefHookUtil.KEY_SETTING_CONTINUOUS, SPrefHookUtil.D_SETTING_CONTINUOUS)){
//					app.setIsRunning(false);
//				}else {
				
//					setEasyClick(true,true);
//				}
				viewInterface.toastBig(context.getString(R.string.main_script_finish));
			}else if(action.equals(BroadcastType.ShellExecStart.getValue())){
				/*boolean netDebug = SPrefHookUtil.getSettingBoolean(context,SPrefHookUtil.KEY_SETTING_NET_DEBUG,SPrefHookUtil.D_SETTING_NET_DEBUG);
				
				Logger.i("netDebug::"+netDebug+" app.getIsRunning()::"+app.getIsRunning()+"  "+SPrefHookUtil.getSettingBoolean(context, SPrefHookUtil.KEY_SETTING_RUN_AUTO,SPrefHookUtil.D_SETTING_RUN_AUTO));
				if(netDebug&&!app.getIsRunning()){
					context.stopService(new Intent(context, ScriptService.class));
					setEasyClick(true);
					if(VpnUtil.isVpnConnected()){
						setEasyClick();
					}
				}
				if(SPrefHookUtil.getSettingBoolean(context, SPrefHookUtil.KEY_SETTING_RUN_AUTO,SPrefHookUtil.D_SETTING_RUN_AUTO)){
					context.stopService(new Intent(context, ScriptService.class));
				}*/
			}else if (action.equals(ConstantsHookConfig.ACTION_SHELL_EXE_ERR)) {
				viewInterface.toastBig(context.getString(R.string.main_script_run_err));
//				if(!VpnUtil.isVpnConnected()&&!SPrefHookUtil.getSettingBoolean(context, SPrefHookUtil.KEY_SETTING_CONTINUOUS, SPrefHookUtil.D_SETTING_CONTINUOUS)){
//					EasyClickUtil.setIsTaskRunning(context, EasyClickUtil.TASK_NOT_RUNNING);
//					app.setIsRunning(false);
//					Logger.i("------isRunning===mian 552"+EasyClickUtil.getIsTaskRunning(context));
//				}else {
				openMain();
//					setEasyClick(true,true);
//				}
			}
			else if (action.equals(ConstantsHookConfig.ACTION_LISTNER_APP)) {
				checkListen();
//				if(intent.getBooleanExtra(ConstantsHookConfig.FLAG_INSTALL, false)){
//					dbDao.deleteLiucunTable();
//				}
//				SPrefUtil.putInt(context, SPrefUtil.C_RUN_COUNT, 0);
				SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, 0);
				SPrefUtil.putInt(context, SPrefUtil.C_RUN_LIU_CUN_COUNT, 0);
			}else if (action.equals(ConstantsHookConfig.ACTION_START_RUN)) {
				if(SPrefHookUtil.getTaskBoolean(context, SPrefHookUtil.KEY_TASK_CONTINUOUS, SPrefHookUtil.D_TASK_CONTINUOUS)){
					EasyClickUtil.setIsTaskRunning(context, EasyClickUtil.TASK_RUNNING);
					app.setIsRunning(true);
					Logger.i("--isRunning- main 544-"+EasyClickUtil.getIsTaskRunning(context));
					limitContinue();
				}else {
					EasyClickUtil.setIsTaskRunning(context, EasyClickUtil.TASK_NOT_RUNNING);
					app.setIsRunning(false);
					Logger.i("isrunning===main 721="+EasyClickUtil.getIsTaskRunning(context));
				}
			}else if (action.equals(ConstantsHookConfig.ACTION_LOGOUT)) {
				viewInterface.toastBig(context.getString(R.string.main_logout));
				SPrefHookUtil.putLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_ID, "");
				context.stopService(new Intent(context, ScriptService.class));
				context.stopService(new Intent(context, MyInternetService.class));
				EasyClickUtil.setIsTaskRunning(context, EasyClickUtil.TASK_NOT_RUNNING);
				app.setIsRunning(false);
				OpenActivityUtil.startLoginActivity(mContext);
				mContext.finish();
			}
			
		}
	}

}
