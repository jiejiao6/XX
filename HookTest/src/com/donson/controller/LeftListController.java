package com.donson.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import Http.HttpUtil2;
import Http.ProgressEvent;
import Http.ShowDownDialogEvent;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.HttpConstants;
import com.donson.config.Logger;
import com.donson.myhook.adapters.LeftListAdapter;
import com.donson.myhook.adapters.LeftListAdapter.ViewHolder;
import com.donson.myhook.services.MyInternetService;
import com.donson.myhook.services.ScriptService;
import com.donson.utils.AppInfosUtil;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.OpenActivityUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.SendBroadCastUtil;
import com.donson.viewinterface.MainViewInterface;
import com.donson.xxxiugaiqi.R;
import com.donson.zhushoubase.BaseApplication;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.param.config.ConstantsConfig;
import com.param.controller.MyParamInterface;
import com.param.netInterface.HttpUtil.ResponseListener;
import com.param.utils.CommonOperationUtil;

public class LeftListController {

	Activity context;
	ListView lvLeft;
	LeftListAdapter adapter;
	List<String> list;
	MainViewInterface mViewInterface;
	BaseApplication app;
	MyReceiver myReceiver;
	LayoutInflater layoutInflater;
	public LeftListController(Activity context,MainViewInterface viewInterface) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		list = new ArrayList<String>();
		mViewInterface = viewInterface;
		app = (BaseApplication) context.getApplication();
		EventBus.getDefault().register(this);
		
		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConstantsConfig.ACTION_PARAM_BROADCAST);
		context.registerReceiver(myReceiver, filter);
	}
	
	public void initSlidMenue() {
		SlidingMenu menu = new SlidingMenu(context);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		int width = context.getWindowManager().getDefaultDisplay().getWidth();
		menu.setBehindWidth(3 * width / 5);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(context, SlidingMenu.SLIDING_CONTENT);
		View view = View.inflate(context, R.layout.leftmenu, null);
		findViewById(view);
		// menu.setMenu(R.layout.leftmenu);
		menu.setMenu(view);
		
	}

	public static final int GLOBAL_CHANGE = 0;
	public static final int CHANGE_DENSITY = 1;
	public static final int OPEN_APP = 2;
	public static final int UNINSTALL_APP = 3;
	public static final int NETDEBUG = 4;
	public static final int CONTINUOUS = 5;
	public static final int COUNT_LIMIT = 6;
	
	public static final int MORE_SET = 7;
	public static final int RANDOM_PARAM = 8;
	public static final int CHECK_VERSION = 9;
	public static final int CHANGE_DOWN_URL = 10;
	public static final int CHANGE_INPUT = 11;
	public static final int OPEN_XPOSED = 12;
	public static final int AUTO_BOOT_START = 13;
	public static final int LOGOUT = 14;
	public static final int RECONNECTNET = 15;
	public static final int MARKET = 16;
	
	private void findViewById(View view) {
		list.add(context.getString(R.string.left_global_change));		//0
		list.add(context.getString(R.string.left_change_density));		//1
		list.add(context.getString(R.string.left_open_app));			//2
		list.add(context.getString(R.string.left_uninstall_app));		//3
		list.add(context.getString(R.string.left_net_debug));			//4
		list.add(context.getString(R.string.left_continuous));			//5
		list.add(context.getString(R.string.left_total_count_limit));	//6
		list.add(context.getString(R.string.title_other_set));			//7
		list.add(context.getString(R.string.left_new_param));			//8
		list.add(context.getString(R.string.left_check_new_version));	//9
		list.add(context.getString(R.string.left_change_down_url));	  	//10
		list.add(context.getString(R.string.left_open_chagne_input));	//11
		list.add(context.getString(R.string.left_open_xposed_installer));	//12
		list.add(context.getString(R.string.left_open_auto_start));	//13
		list.add(context.getString(R.string.left_logout));				//14
		list.add(context.getString(R.string.left_reconnect_net));	//15
		list.add(context.getString(R.string.left_market));	//16
		lvLeft = (ListView) view.findViewById(R.id.lv_left_list);
		adapter = new LeftListAdapter(context, list);
		lvLeft.setAdapter(adapter);
		lvLeft.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LeftListAdapter.ViewHolder holder = (ViewHolder) view.getTag();
				boolean ischecked = holder.checkBox.isChecked();
				holder.checkBox.setChecked(!ischecked);
				switch (position) {
				case GLOBAL_CHANGE:
					SPrefHookUtil.putSettingBoolean(context, SPrefHookUtil.KEY_SETTING_GLOBAL_CHANGEDE, holder.checkBox.isChecked());
					break;
				case CHANGE_DENSITY:
					SPrefHookUtil.putSettingBoolean(context, SPrefHookUtil.KEY_SETTING_DENSITY_CHEANGE, holder.checkBox.isChecked());
					break;
				case OPEN_APP:
					SPrefHookUtil.putSettingBoolean(context, SPrefHookUtil.KEY_SETTING_OPEN_APK, holder.checkBox.isChecked());
					break;
				case UNINSTALL_APP:
					SPrefHookUtil.putSettingBoolean(context, SPrefHookUtil.KEY_SETTING_UNINSTALL_APK, holder.checkBox.isChecked());
					break;
				case NETDEBUG:
					SPrefHookUtil.putSettingBoolean(context, SPrefHookUtil.KEY_SETTING_NET_DEBUG, holder.checkBox.isChecked());
//					if(!holder.checkBox.isChecked()){
//						app.setIsRunning(false);
//					}
					break;
				case CONTINUOUS:
					SPrefHookUtil.putTaskBoolean(context, SPrefHookUtil.KEY_TASK_CONTINUOUS, holder.checkBox.isChecked());
					break;
				case COUNT_LIMIT:
					SPrefHookUtil.putSettingBoolean(context, SPrefHookUtil.KEY_SETTING_TOTAL_TIMES_LIMIT, holder.checkBox.isChecked());
					break;
				case MORE_SET:
					openMoreSet();
					break;
				case RANDOM_PARAM:
					randomParam();
					break;
				case CHECK_VERSION:
					checkUpdate();
					break;
//				case INSTALL_MANUAL:
//					installManual();
//					break;
				case CHANGE_DOWN_URL:
					changeDownUrl();
					break;
				case CHANGE_INPUT:
					openInput();
					break;
				case AUTO_BOOT_START:
					openAutoStart();
					break;
				case OPEN_XPOSED:
					openXposed();
					break;
//				case OPEN_UNINSTALLER:
//					openUnInstall();
//					break;
				case LOGOUT:
					SPrefHookUtil.putLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_ID, "");
					context.stopService(new Intent(context, ScriptService.class));
					context.stopService(new Intent(context, MyInternetService.class));
					EasyClickUtil.setIsTaskRunning(context, EasyClickUtil.TASK_NOT_RUNNING);
					app.setIsRunning(false);
					OpenActivityUtil.startLoginActivity(context);
					context.finish();
					break;
				case RECONNECTNET:
					WifiManager manager = (WifiManager) context.getSystemService(Activity.WIFI_SERVICE);
					boolean dis = manager.disconnect();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					boolean res = manager.reconnect();
					Logger.i(dis+" reconnect:::"+res);
//					manager.setWifiEnabled(true);
					break;
				case MARKET:
					EasyClickUtil.setMarketHookFlag(context, holder.checkBox.isChecked());
					break;
				default:
//					
					break;
				}
			}
		});
	}


	

	/**
	 * 修改下载的url
	 */
	protected void changeDownUrl() {
		String ip = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_SETTING_DOWN_URL,SPrefHookUtil.D_SETTING_DOWN_URL);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View dialogView = layoutInflater.inflate(R.layout.dialog_chang_script_id,null);
		final EditText et = (EditText) dialogView.findViewById(R.id.et_script_or_id);
		TextView suf = (TextView) dialogView.findViewById(R.id.et_script_suffix);
		TextView tip = (TextView) dialogView.findViewById(R.id.script_tc_tips);
		suf.setVisibility(View.GONE);tip.setVisibility(View.GONE);
		et.setText(ip);
		builder.setView(dialogView);
		builder.setPositiveButton(context.getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(!TextUtils.isEmpty(et.getText())){
					String newIp = et.getText().toString();
					boolean res = SPrefHookUtil.putSettingStr(context, SPrefHookUtil.KEY_SETTING_DOWN_URL, newIp);
				}
			}
		});
		builder.setNegativeButton(context.getString(R.string.dialog_cancle), null);
		builder.setTitle(context.getString(R.string.left_change_down_url));
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	protected void randomParam() {
		MyParamInterface interface1 = MyParamInterface.getInstance(context);
		String packageName = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
		interface1.getParam(packageName,true);
	}
	
	class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(final Context context, final Intent intent) {
			Logger.i("***************MyReceiver**********"+intent.getAction());
			if(intent.getAction().equals(ConstantsConfig.ACTION_PARAM_BROADCAST)){
				 boolean onlyParam = intent.getBooleanExtra(ConstantsConfig.INTENT_ONLY_PARAM, false);
				if(onlyParam){
					 String info = intent.getExtras().getString(ConstantsConfig.INTENT_PARAM);  
					 boolean save = intent.getBooleanExtra(ConstantsConfig.INTENT_SAVE, true);
					 SendBroadCastUtil.paramOk2(context, info, save,onlyParam);
				}
			}
		}
	}

	protected void checkUpdate() {
		HttpUtil2 httpUtil2 = new HttpUtil2(context);
		httpUtil2.setListener(new ResponseListener() {
//			={"CODE":0,"DATA":{"RESULT":"1.0.2"},"MESSAGE":"success"}
			@Override
			public void result(Object obj) {

				if(obj!=null&&obj instanceof String){
					String json = (String) obj;
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(json);
						int code = jsonObject.optInt("CODE");
						switch (code) {
						case 0:
							String version = jsonObject.optJSONObject("DATA").optString("RESULT");
							if(version.equals(AppInfosUtil.getVersion(context))){
								mViewInterface.toast(context.getString(R.string.left_version_no_change));
							}else {
								checkLocalVersion(version);
							}
							break;
						default:
							String message = jsonObject.optString("MESSAGE");
							mViewInterface.toast(message);
							break;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					mViewInterface.toast(context.getString(R.string.left_get_version_err));
				}
			}
		});
		httpUtil2.checkUpdate();
		final String path = ConstantsHookConfig.APK_LOCAL_MY_PATH+"/"+CommonOperationUtil.convertPackageName2Apk(ConstantsHookConfig.MY_PACKAGE_NAME);
	}
	public static final String MyApkpath = ConstantsHookConfig.APK_LOCAL_MY_PATH+"/"+CommonOperationUtil.convertPackageName2Apk(ConstantsHookConfig.MY_PACKAGE_NAME);
	protected void checkLocalVersion(String newVer) {
		File file = new File(MyApkpath);
		if(file.exists()){
			String localVer = AppInfosUtil.getApkVersionByPath(context, MyApkpath);
			if(newVer.equals(localVer)){
				showDialog(newVer);
			}else {
				downLoadNewApk(newVer);
			}
		}else {
			downLoadNewApk(newVer);
		}
	}
	private void showDialog(final String newVer) {
		AlertDialog alertDialog = new AlertDialog.Builder(context)
		.setMessage(context.getString(R.string.left_local_exist))
		.setPositiveButton(context.getString(R.string.left_user_local), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				install();
			}
		} ).setNegativeButton(context.getString(R.string.left_download_new), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				downLoadNewApk(newVer);
			}
		}).create();
		alertDialog.show();
		
	}
	HttpUtil2 httpUtil2 =null;
	protected void downLoadNewApk(String newVer) {
		httpUtil2 = new HttpUtil2(context);
		Map<String, String> map = new HashMap<String, String>();
		if(ConstantsHookConfig.IS_MOBILE){
			map.put(HttpConstants.REQUEST_KEY_URL, "version/"+newVer+".apk");
		}else {
			map.put(HttpConstants.REQUEST_KEY_URL, "version/"+newVer+"_pc.apk");

		}
		map.put(HttpConstants.REQUEST_KEY_PATH, ConstantsHookConfig.DOWNLOAD_MY_AKP_PATH+CommonOperationUtil.convertPackageName2Apk(ConstantsHookConfig.MY_PACKAGE_NAME));
		httpUtil2.setListener(new ResponseListener() {
			
			@Override
			public void result(Object obj) {
				if(obj!=null&&obj instanceof String){
					String res = (String) obj;
					if(res.equals(HttpConstants.RESPONSE_OK)){
						mViewInterface.toast(context.getString(R.string.left_version_down_ok));
						install();
					}else {
						mViewInterface.toast(context.getString(R.string.left_version_down_err));
					}
				}else {
					mViewInterface.toast(context.getString(R.string.left_version_down_err));
				}
			}
		});
		httpUtil2.downBigFileFromServer(context, map, false);
		showDowndialog(String.format(context.getString(R.string.left_version_updating),newVer));
	}
	
	protected void install() {
//		Intent i = new Intent(Intent.ACTION_VIEW);
//	    i.setDataAndType(Uri.parse("file://" + MyApkpath), "application/vnd.android.package-archive");
//	    context.startActivity(i);
	    SendBroadCastUtil.upDateXX(context,MyApkpath);
	}
	protected  void installManual() {
		File file = new File(MyApkpath);
		if (!file.exists()) {
			mViewInterface.toast(context.getString(R.string.left_file_not_exist));
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
	    i.setDataAndType(Uri.parse("file://" + MyApkpath), "application/vnd.android.package-archive");
	    context.startActivity(i);
	}
	
	ProgressDialog ddialog = null;
	public void showDowndialog(String title){
		ddialog = new ProgressDialog(context);
		ddialog.setProgressNumberFormat("%1d KB/%2d KB");
		ddialog.setTitle(title);
		ddialog.setMessage(context.getString(R.string.auto_downLoad_message));
		ddialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		ddialog.setCancelable(false);
		/*ddialog.setButton("ca", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				httpUtil2.cancleDown();
			}
		});*/
		ddialog.show();
	}
	
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onEventMainThread(ShowDownDialogEvent event) {
		Logger.i("onEventMainThread  "+event.getTitle()+"  "+(ddialog!=null)+"  ");
		if(ddialog==null||(ddialog!=null&&!ddialog.isShowing())){
			showDowndialog(event.getTitle()+"");
		}
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
				mViewInterface.toast(context.getString(R.string.left_version_down_err));
			}
			if(event.getProgress()>=event.getTotal()){
				if(ddialog!=null&&ddialog.isShowing()){
					ddialog.dismiss();
				}
			}
		}
	}
	public void onDestory(){
		EventBus.getDefault().unregister(this);
		context.unregisterReceiver(myReceiver);
	}
	/**
	 * 打开应用
	 */
	public void openChangeWhiteList() {
		OpenActivityUtil.startChangeWhiteListActivity(context);
	}
	public void openListenChoose() {
		OpenActivityUtil.startListenApkActivity(context);
	}
	public void openLiuCun() {
		OpenActivityUtil.startLiuCunActivity(context);
	}
	public void openFileListen() {
		OpenActivityUtil.startFileOptListenActivity(context);
	}
	public void openSysListen() {
		OpenActivityUtil.startSysOptListenActivity(context);
	}
	public void openVPNSet() {
		OpenActivityUtil.startVpnSetActivity(context);
	}
	public void openScriptSet() {
		OpenActivityUtil.startScriptSetActivity(context);
	}
	public void openWifiSimSet() {
		OpenActivityUtil.startWifiSimActivity(context);
	}
	public void openInput(){
		OpenActivityUtil.startInput(context);
	}
	protected void openAutoStart() {
		OpenActivityUtil.startAutoStart(context);
	}

	protected void openXposed() {
		EasyClickUtil.setXposedHook(context, true);
		OpenActivityUtil.startXposed(context);
	}
	protected void openUnInstall() {
		OpenActivityUtil.startUninstallActivity(context);
	}
	
	protected void openMoreSet() {
		OpenActivityUtil.startOhterSetActivity(context);
	}
}
