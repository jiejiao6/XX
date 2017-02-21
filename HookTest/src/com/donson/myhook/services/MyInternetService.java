package com.donson.myhook.services;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import Http.CheckTaskEvent;
import Http.HttpUtil2;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.text.TextUtils;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.myhook.MainActivity;
import com.donson.utils.ActivityUtil;
import com.donson.utils.CmdUtil;
import com.donson.utils.CommonTimeUtil;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.OpenActivityUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.SendBroadCastUtil;
import com.donson.utils.VpnUtil;
import com.donson.xxxiugaiqi.R;
import com.donson.zhushoubase.BaseApplication;

public class MyInternetService extends Service {

	WifiManager wifiManager;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		wifiManager = (WifiManager) getSystemService(Activity.WIFI_SERVICE);
		app = (BaseApplication) getApplication();
		showNotif();
		checkRun();
		checkNet1();
		EventBus.getDefault().register(this);
		super.onCreate();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Logger.i("=*****************===================onStartCommand=========********************=====");
		return START_STICKY;
	}
	private static final int NOTIFY_FAKEPLAYER_ID=14000;  
	void showNotif() {
		Intent i = new Intent(this,MainActivity.class);
        //注意Intent的flag设置：FLAG_ACTIVITY_CLEAR_TOP: 如果activity已在当前任务中运行，在它前端的activity都会被关闭，它就成了最前端的activity。FLAG_ACTIVITY_SINGLE_TOP: 如果activity已经在最前端运行，则不需要再加载。设置这两个flag，就是让一个且唯一的一个activity（服务界面）运行在最前端。
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);           
        Notification myNotify = new Notification.Builder(this) 
                                .setSmallIcon(R.drawable.ic_launcher) 
                                .setTicker(getString(R.string.background_service)) 
                                .setContentTitle(getString(R.string.background_service)) 
                                .setContentText(getString(R.string.background_service_content))
                                .setContentIntent(pi) 
                                .build();             
        //设置notification的flag，表明在点击通知后，通知并不会消失，也在最右图上仍在通知栏显示图标。这是确保在activity中退出后，状态栏仍有图标可提下拉、点击，再次进入activity。
        myNotify.flags |= Notification.FLAG_NO_CLEAR;
       // 步骤 2：startForeground( int, Notification)将服务设置为foreground状态，使系统知道该服务是用户关注，低内存情况下不会killed，并提供通知向用户表明处于foreground状态。
        startForeground(NOTIFY_FAKEPLAYER_ID,myNotify);
	}
	BaseApplication app;
	public void checkRun(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					/*if(!ActivityUtil.isAppRunning(getApplicationContext(), ConstantsHookConfig.CONTROL_PACKAGE_NAME)){
						Logger.i("is service running");
					try{
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_MAIN);
						intent.setClassName(ConstantsHookConfig.CONTROL_PACKAGE_NAME, ConstantsHookConfig.CONTROL_ACTIVITY_NAME);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						startService(new Intent(getApplicationContext(), ListenService.class));
					}catch(Exception e){
						e.printStackTrace();
						}
					}*/
					if(!ActivityUtil.isServiceRunning(getApplicationContext(), ConstantsHookConfig.CONTROL_SERVICE_NAME)){
						Logger.i("CONTROL_SERVICE_NAME  isservice running");
					        Intent testActivityIntent = new Intent();   
					        testActivityIntent.setAction("android.intent.action.START_CONTROL") ; 
					        testActivityIntent.setClassName(ConstantsHookConfig.CONTROL_PACKAGE_NAME, ConstantsHookConfig.CONTROL_SERVICE_NAME);
					        testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);					       
					        startService(testActivityIntent); 
					}
					if(!ActivityUtil.isServiceRunning(getApplicationContext(), ConstantsHookConfig.SERVICE_NAME_LISTEN)){
						Logger.i("=====listenOtherService========");
						startService(new Intent(getApplicationContext(), ListenService.class));
					}
					
					boolean netDebug = SPrefHookUtil.getSettingBoolean(getApplicationContext(),SPrefHookUtil.KEY_SETTING_NET_DEBUG,SPrefHookUtil.D_SETTING_NET_DEBUG);
					if(netDebug){
						boolean isContinue = SPrefHookUtil.getTaskBoolean(getApplicationContext(), SPrefHookUtil.KEY_TASK_CONTINUOUS,SPrefHookUtil.D_TASK_CONTINUOUS);
						boolean isCountLimit = SPrefHookUtil.getSettingBoolean(getApplicationContext(), SPrefHookUtil.KEY_SETTING_TOTAL_TIMES_LIMIT, SPrefHookUtil.D_SETTING_TOTAL_TIMES_LIMIT);
						boolean isScriptRunning = EasyClickUtil.getScriptIsRunning(getApplicationContext());
						
						if(!isCountLimit){
							Logger.i(" app.getIsRunning():"+EasyClickUtil.getIsTaskRunning(getApplicationContext())+"::"+"  iscontinue:"+isContinue+
									"  script:"+EasyClickUtil.getScriptIsRunning(getApplicationContext()));
							if(!EasyClickUtil.getIsTaskRunning(getApplicationContext())&&CmdUtil.isRoot()&&checkCanRun()&& isContinue){
//								app.setIsRunning(true);
								
								SendBroadCastUtil.startRun(getApplicationContext());
							}else if ((isCountLimit||!checkCanRun()||!isContinue)&&!isScriptRunning) {
								EasyClickUtil.setIsTaskRunning(getApplicationContext(), EasyClickUtil.TASK_NOT_RUNNING);
								app.setIsRunning(false);
								Logger.i("--isRunning-ser 112-"+EasyClickUtil.getIsTaskRunning(getApplicationContext()));
							}
						}else {
							boolean isautoRunning = SPrefHookUtil.getSettingBoolean(getApplicationContext(), SPrefHookUtil.KEY_SETTING_RUN_AUTO,false);
							int runCurCount = SPrefHookUtil.getTaskInt(getApplicationContext(), SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME);
							int totalCount = SPrefHookUtil.getTaskInt(getApplicationContext(), SPrefHookUtil.KEY_TASK_RUN_TIMES, SPrefHookUtil.D_TASK_RUN_TIMES);
							Logger.i(" app.getIsRunning():"+EasyClickUtil.getIsTaskRunning(getApplicationContext())+"::"+(!EasyClickUtil.getIsTaskRunning(getApplicationContext())&&runCurCount<totalCount&&CmdUtil.isRoot()&&checkCanRun())+"  iscontinue:"+isContinue+"  isautoRunning:"+isautoRunning+
									"  script:"+EasyClickUtil.getScriptIsRunning(getApplicationContext())
									/*+"isvpnconn::"+VpnUtil.isVpnConnected()+"  isvpnused::"+VpnUtil.isVpnUsed()*/);
							if(!EasyClickUtil.getIsTaskRunning(getApplicationContext())&&runCurCount<totalCount&&CmdUtil.isRoot()&&checkCanRun()&& isContinue){
//								app.setIsRunning(true);
								if(!ConstantsHookConfig.IS_MOBILE&&EasyClickUtil.getScriptIsRunning(getApplicationContext())){
									try {
										OpenActivityUtil.openApkByPackageName(getApplicationContext(), SPrefHookUtil.getCurTaskStr(getApplicationContext(), SPrefHookUtil.KEY_CUR_PACKAGE_NAME));
										Thread.sleep(100*1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								SendBroadCastUtil.startRun(getApplicationContext());
							}else if ((runCurCount>=totalCount||!checkCanRun()||!isContinue)&&!isScriptRunning) {
								EasyClickUtil.setIsTaskRunning(getApplicationContext(), EasyClickUtil.TASK_NOT_RUNNING);
								app.setIsRunning(false);
								Logger.i("--isRunning-ser 123-"+EasyClickUtil.getIsTaskRunning(getApplicationContext()));
							}
						}
					}else {
						EasyClickUtil.setIsTaskRunning(getApplicationContext(), EasyClickUtil.TASK_NOT_RUNNING);
						app.setIsRunning(false);
						Logger.i("--isRunning-ser 117-"+EasyClickUtil.getIsTaskRunning(getApplicationContext()));
					}
					try {
						Thread.sleep(5*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					long t = Math.abs(System.currentTimeMillis()-checkNetTime);
					Logger.i("------网络获取计时------"+t);
					if(Math.abs(System.currentTimeMillis()-checkNetTime)>=60*1000){
						if (netDebug) {
							checkNet();
						}
					}
				}
				
			}
		}).start();
	}
	
	long checkNetTime = 0;
	public void checkNet(){
		checkNetTime = System.currentTimeMillis();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (true) {
					Logger.i("=======================while (true)=======================");
					final boolean netDebug = SPrefHookUtil.getSettingBoolean(getApplicationContext(),SPrefHookUtil.KEY_SETTING_NET_DEBUG,SPrefHookUtil.D_SETTING_NET_DEBUG);
					if (netDebug) {
						HttpUtil2 httpUtil = new HttpUtil2(getApplicationContext());
						httpUtil.getTask();
					}
//				}
//			}
//		}).start();
	}
	public void checkNet1(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				checkNet();
			}
		}).start();
	}
	
	protected boolean checkCanRun() {
//		if(!app.getIsRunning()&&runCurCount<totalCount&&CmdUtil.isRoot() )
		if(TextUtils.isEmpty(EasyClickUtil.getXposedUsedFlag(getApplicationContext()))||!EasyClickUtil.getXposedUsedFlag(getApplicationContext()).equals(EasyClickUtil.XPOSED_USED)){
			return false;
		}
		return true;
	}
	int disconnectTime = 0;
	int disconnectTime2 = 0;
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventMainThread(final CheckTaskEvent event) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Logger.i("*********getTaskResult*******"+event.isSucceed()+"  disconnectTime::"+disconnectTime+"  disconnectTime2:"+disconnectTime2);
				if(!event.isSucceed()){
					disconnectTime++;
					disconnectTime2++;
				}else {
					disconnectTime=0;
					disconnectTime2=0;
				}
				if(disconnectTime>=40){
					wifiManager.setWifiEnabled(false);
					boolean result = wifiManager.disconnect();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					wifiManager.setWifiEnabled(true);
					boolean reconn = wifiManager.reconnect();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					disconnectTime = 0;
					Logger.i("disconnect:"+result+" reconn:"+reconn);
				}
				if(disconnectTime2>60){//30分钟收不到消息重启
					disconnectTime2 = 0;
					EasyClickUtil.setRebootFlag(getApplicationContext(),false);
//					SPrefHookUtil.putSettingBoolean(getApplicationContext(), SPrefHookUtil.KEY_REBOOT, false);
				}
				try {
					Thread.sleep(30*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				checkNetTime = System.currentTimeMillis();
				checkNet();
			}
		}).start();
	}
	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		Intent localIntent = new Intent();
		localIntent.setClass(this, MyInternetService.class); //销毁时重新启动Service
		this.startService(localIntent);
		super.onDestroy();
	}

}
