package com.donson.myhook.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.myhook.MainActivity;
import com.donson.realparam.utils.NetUtil;
import com.donson.utils.ActivityUtil;
import com.donson.utils.CmdUtil;
import com.donson.utils.CommonTimeUtil;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.VpnUtil;
import com.donson.xxxiugaiqi.R;

public class ListenService extends Service{
	WifiManager Wifimanager ;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		Wifimanager = (WifiManager) getSystemService(Activity.WIFI_SERVICE);
		Logger.i("onCreate=ListenService=="+android.os.Process.myPid());
		showNotif();
		super.onCreate();
	}
	private static final int NOTIFY_FAKEPLAYER_ID = 1335;
	NotificationManager manager;
	Notification myNotify;
	private RemoteViews views;  
	void showNotif() {
		System.out.println("===ListenService==showNotif====");
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
		
		Intent i = new Intent(this,MainActivity.class);
        //注意Intent的flag设置：FLAG_ACTIVITY_CLEAR_TOP: 如果activity已在当前任务中运行，在它前端的activity都会被关闭，它就成了最前端的activity。FLAG_ACTIVITY_SINGLE_TOP: 如果activity已经在最前端运行，则不需要再加载。设置这两个flag，就是让一个且唯一的一个activity（服务界面）运行在最前端。
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);           
        Notification myNotify = new Notification.Builder(this) 
                                .setSmallIcon(R.drawable.ic_launcher) 
                                .setTicker(getString(R.string.background_service_listen)) 
                                .setContentTitle(getString(R.string.background_service_listen)) 
                                .setContentText(getString(R.string.background_service_content))
                                .setContentIntent(pi) 
                                .build(); 
		// 设置notification的flag，表明在点击通知后，通知并不会消失，也在最右图上仍在通知栏显示图标。这是确保在activity中退出后，状态栏仍有图标可提下拉、点击，再次进入activity。
		myNotify.flags |= Notification.FLAG_NO_CLEAR;
		// 步骤 2：startForeground( int,
		// Notification)将服务设置为foreground状态，使系统知道该服务是用户关注，低内存情况下不会killed，并提供通知向用户表明处于foreground状态。
		startForeground(NOTIFY_FAKEPLAYER_ID, myNotify);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		listenOtherService();
		return super.onStartCommand(intent, flags, startId);
	}
	int netDisConnectCount = 0;
	private void listenOtherService() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if(!ActivityUtil.isServiceRunning(getApplicationContext(), ConstantsHookConfig.SERVICE_NAME_MAIN)){
						Logger.l("=====listenOtherService========");
						startService(new Intent(getApplicationContext(), MyInternetService.class));
					}
					boolean rebooted = //SPrefHookUtil.getSettingBoolean(getApplicationContext(), SPrefHookUtil.KEY_REBOOT, SPrefHookUtil.D_REBOOT);
					EasyClickUtil.getRebootFlag(getApplicationContext());
					Logger.l("rebooted:"+rebooted+"  "+(System.currentTimeMillis()>=CommonTimeUtil.getDay3Clock()&&!rebooted));
					if(System.currentTimeMillis()>=CommonTimeUtil.getDay3Clock()&&!rebooted){
//						SPrefHookUtil.putSettingBoolean(getApplicationContext(), SPrefHookUtil.KEY_REBOOT, true);
						EasyClickUtil.setRebootFlag(getApplicationContext(), true);
						reBoot(getApplicationContext());
					}
					if(System.currentTimeMillis()<CommonTimeUtil.getDay3Clock()){
						EasyClickUtil.setRebootFlag(getApplicationContext(), false);
//						SPrefHookUtil.putSettingBoolean(getApplicationContext(), SPrefHookUtil.KEY_REBOOT, false);
					}
					if(!ActivityUtil.isServiceRunning(getApplicationContext(), ConstantsHookConfig.SERVICE_NAME_SCRIPT)){
						EasyClickUtil.setScriptIsRunning(getApplicationContext(),EasyClickUtil.SCRIPT_NOT_RUNNING);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!NetUtil.isWifiConnect(getApplicationContext())){
						netDisConnectCount++;
						boolean res = Wifimanager.reconnect();
					}else {
						netDisConnectCount = 0;
					}
					Logger.i("net is connected::netConnectCount:"+netDisConnectCount);
					if(netDisConnectCount>60*60){
						netDisConnectCount=0;
						reBoot(getApplicationContext());
					}
//					int res = 	VpnUtil.isVpnConnected2();
//					Logger.i("SCREEN","res------"+res);
				}
			}
		}).start();
	}
	public  void reBoot(Context context2) {
		if(ConstantsHookConfig.IS_MOBILE){
			try{
				Process p = CmdUtil.run("reboot");
				String s = CmdUtil.readResult(p);
				Logger.i("CMD----------" + s + "==" + CmdUtil.isRoot());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
