package com.donson.myhook.services;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.widget.RemoteViews;

import com.androlua.LuaEngine;
import com.donson.config.ConstantsHookConfig;
import com.donson.config.HttpConstants;
import com.donson.config.Logger;
import com.donson.myhook.MainActivity;
import com.donson.utils.ActivityUtil;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.OpenActivityUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.SendBroadCastUtil;
import com.donson.xxxiugaiqi.R;
import com.param.config.ConstantsConfig;

public class ScriptService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		EasyClickUtil.setScriptIsRunning(getApplicationContext(), EasyClickUtil.SCRIPT_RUNNING);
		Logger.i("onCreate==="+Process.myPid()+"  KEY_SETTING_SCRIPT_RUNNING:"+EasyClickUtil.getScriptIsRunning(getApplicationContext()));
		showNotif();
		super.onCreate();
	}
	public static String MY_PACKAGE_NAME = "com.donson.xxxiugaiqi";
	public static String MY_MAIN_ACTIVITY_NAME = "com.donson.myhook.MainActivity";
	public  void openApkByDetailInfo(Context context,String appPackageName,String className) {
		try {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.setClassName(appPackageName, className);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			Logger.i("openApkByDetailInfo==ExScript::"+e);
			e.printStackTrace();
		}
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("onStartCommand===="+Process.myPid());
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				int count = 0;
				final int scriptTime = //SPrefHookUtil.getTaskInt(getApplicationContext(), SPrefHookUtil.KEY_TASK_SCRIPT_TIME, SPrefHookUtil.D_TASK_SCRIPT_TIME);
				SPrefHookUtil.getCurTaskInt(getApplicationContext(),SPrefHookUtil.KEY_CUR_TASK_SCRIPT_TIME, 0);
				while (count<scriptTime) {
					try {
						Thread.sleep(1000);
						count++;
						views.setTextViewText(R.id.tv_time,String.format(getString(R.string.script_run_cur_time), count));  
						startForeground(NOTIFY_FAKEPLAYER_ID, myNotify);
//						manager.notify(0, myNotify);
						Logger.i("==========scriptRunning============"+count+"  "+scriptTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!ActivityUtil.isServiceRunning(getApplicationContext(), ConstantsHookConfig.CONTROL_SERVICE_NAME)){
						Logger.i("script  isservice running");
					        Intent testActivityIntent = new Intent();   
					        testActivityIntent.setAction("android.intent.action.START_PCM_PLAY_SERVICE") ; 
					        testActivityIntent.setClassName(ConstantsHookConfig.CONTROL_PACKAGE_NAME, ConstantsHookConfig.CONTROL_SERVICE_NAME);
					        testActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);					       
					        startService(testActivityIntent); 
					}
				}
				openApkByDetailInfo(getApplicationContext(), MY_PACKAGE_NAME, MY_MAIN_ACTIVITY_NAME);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				OpenActivityUtil.openApkByDetailInfo(getApplicationContext(), ConstantsHookConfig.MY_PACKAGE_NAME, ConstantsHookConfig.MY_MAIN_ACTIVITY_NAME, "script 94");
				SendBroadCastUtil.runScriptTimeOut(getApplicationContext());
				stopSelf();
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				String scriptName = SPrefHookUtil.getCurTaskStr(getApplicationContext(), SPrefHookUtil.KEY_CUR_TASK_SCRIPT_NAME);//SPrefHookUtil.getTaskStr(getApplicationContext(), SPrefHookUtil.KEY_TASK_SCRIPT_NAME,SPrefHookUtil.D_TASK_SCRIPT_NAME);
				String name = scriptName+HttpConstants.SCRIPT_SUFFIX;
				
//				String path = getApplicationContext().getFilesDir() + "/"+ name;
				
				String sourcePath = ConstantsConfig.EXTRA_PATH+File.separator+ ConstantsConfig.DOWNLOAD_SCRIPT_PATH_NAME+File.separator+name;
				boolean is = new File(sourcePath).exists();
				if(!is){
					return;
				}
				System.out.println("come here ......" + sourcePath);
				// 参数
//				HashMap<Object, Object> args = new HashMap<Object, Object>();
//				args.put(1, "joshua");
//				args.put(2, "123456");
//				args.put(3, "https://taobao.com");
				
				LuaEngine.getInstance().execLuaScriptFile(sourcePath, null);
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		EasyClickUtil.setScriptIsRunning(getApplicationContext(), EasyClickUtil.SCRIPT_NOT_RUNNING);
		Logger.i("onDestroy===="+Process.myPid()+"  KEY_SETTING_SCRIPT_RUNNING:"+EasyClickUtil.getScriptIsRunning(getApplicationContext()));
		Process.killProcess(Process.myPid());
		super.onDestroy();
	}

	private static final int NOTIFY_FAKEPLAYER_ID = 1333;
	NotificationManager manager;
	Notification myNotify;
	private RemoteViews views;  
	void showNotif() {
		System.out.println("showNotif====");
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
		Intent i = new Intent(this, MainActivity.class);
		views = new RemoteViews(getPackageName(), R.layout.notification_update); 
		
		// 注意Intent的flag设置：FLAG_ACTIVITY_CLEAR_TOP:
		// 如果activity已在当前任务中运行，在它前端的activity都会被关闭，它就成了最前端的activity。FLAG_ACTIVITY_SINGLE_TOP:
		// 如果activity已经在最前端运行，则不需要再加载。设置这两个flag，就是让一个且唯一的一个activity（服务界面）运行在最前端。
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
		myNotify = new Notification.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker(getString(R.string.background_service_script))
				.setContentTitle(getString(R.string.background_service_script))
//				.setContentText(getString(R.string.background_service_script_content))
				.setContent(views).setContentIntent(pi)
				.build();
		// 设置notification的flag，表明在点击通知后，通知并不会消失，也在最右图上仍在通知栏显示图标。这是确保在activity中退出后，状态栏仍有图标可提下拉、点击，再次进入activity。
		myNotify.flags |= Notification.FLAG_NO_CLEAR;
		// 步骤 2：startForeground( int,
		// Notification)将服务设置为foreground状态，使系统知道该服务是用户关注，低内存情况下不会killed，并提供通知向用户表明处于foreground状态。
		startForeground(NOTIFY_FAKEPLAYER_ID, myNotify);

	}

}
