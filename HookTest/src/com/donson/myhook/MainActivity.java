package com.donson.myhook;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androlua.CrashHandler;
import com.donson.config.Logger;
import com.donson.controller.MainViewController;
import com.donson.myhook.adapters.ParamAdapter;
import com.donson.myhook.bean.DataMode;
import com.donson.myhook.services.ListenService;
import com.donson.myhook.services.MyInternetService;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.OpenActivityUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.StringUtil;
import com.donson.viewinterface.MainViewInterface;
import com.donson.widget.MyListView;
import com.donson.xxxiugaiqi.R;
import com.donson.zhushoubase.BaseApplication;
import com.param.config.ConstantsConfig;
import com.param.utils.FileUtil;

public class MainActivity extends BaseActivity implements MainViewInterface,
		OnLongClickListener {
	private Button btnEasyClick = null, 
			btnListenApk = null;
	private ViewGroup listenGroup;
	private ImageView ivListenIcon;
	private TextView tvListenAppName, tvListenPackageName, tvIsXpUsed;

	private TextView tvTips = null;
	private MyListView paramList = null;
	private ParamAdapter adapter = null;
	private List<DataMode> list = null;
	private MainViewController controller;
	BaseApplication app;
	private void CheckPermission() {
//		if (!Settings.System.canWrite(this)) {
//			showToast("请在该设置页面勾选，才可以使用路况提醒功能");
//			Uri selfPackageUri = Uri.parse("package:"
//					+ getPackageName());
//			Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
//					selfPackageUri);
//			startActivity(intent);
//		}
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
//                //有了权限，具体的动作
//                Settings.System.putInt(getContentResolver(),
//                        Settings.System.SCREEN_BRIGHTNESS, progress);
//                data2 = intToString(progress, 255);
//                tvSunlightValue.setText(data2 + "%");
            }
        }
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		CheckPermission();
		SPrefHookUtil.putSettingBoolean(MainActivity.this, SPrefHookUtil.KEY_SETTING_NET_DEBUG, true);
//		SPrefHookUtil.putSettingStr(MainActivity.this, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME,"");// 不要

		app = (BaseApplication) getApplication();
		if (!app.getIsRunning()) {
			EasyClickUtil.setIsTaskRunning(getApplicationContext(),
					EasyClickUtil.TASK_NOT_RUNNING);
		}
		EasyClickUtil.setXposedHook(MainActivity.this, false);
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		setContentView(R.layout.activity_main);
		new Thread(new Runnable() {

			@Override
			public void run() {
				new Thread(new Runnable() {
					@Override
					public void run() {
//						CopyDBUtil.copyDB(context);
						FileUtil.copyDbToLocalRaw(MainActivity.this,ConstantsConfig.FILE_CELL_DB_NAME, R.raw.cell);
						FileUtil.copyDbToLocalRaw(MainActivity.this,ConstantsConfig.FILE_PHOND_DB_NAME, R.raw.phone);
						FileUtil.copyDbToLocalRaw(MainActivity.this,ConstantsConfig.FILE_CONTACTS_DB_NAME, R.raw.contacts);
						FileUtil.copyDbToLocalRaw(MainActivity.this,ConstantsConfig.FILE_IPDATA_DB_NAME, R.raw.ipdata);
					}
				}).start();
			}
		}).start();
		controller = new MainViewController(this, this);
		
		String deviceId = SPrefHookUtil.getLoginStr(this,SPrefHookUtil.KEY_LOGIN_DEVICE_ID);
		String deviceCode = SPrefHookUtil.getLoginStr(this,SPrefHookUtil.KEY_LOGIN_DEVICE_CODE);
		Logger.i("app.getIsLogined()===" + app.getIsLogined()+"  "+deviceId+"  "+deviceCode);
		
		if (TextUtils.isEmpty(deviceId) || TextUtils.isEmpty(deviceCode)) {
			
			OpenActivityUtil.startLoginActivity(this);
			finish();
			overridePendingTransition(android.R.anim.fade_out,
					android.R.anim.fade_in);
		} else {
			init();
		}
		
		/*
		 * if(!app.getIsLogined()){ //
		 * SPrefHookUtil.putSettingBoolean(getApplicationContext(),
		 * SPrefHookUtil.KEY_SETTING_RUN_AUTO,false);
		 * OpenActivityUtil.startLoginActivity(this); finish();
		 * overridePendingTransition(android.R.anim.fade_out,
		 * android.R.anim.fade_in); } else { init(); }
		 */
		// init();
	}

	private void init() {
		list = new ArrayList<DataMode>();
		listenGroup = (ViewGroup) findViewById(R.id.listen_layout);
		ivListenIcon = (ImageView) findViewById(R.id.iv_listen_icon);
		tvListenAppName = (TextView) findViewById(R.id.tv_listen_apk_name);
		tvListenPackageName = (TextView) findViewById(R.id.tv_listen_package);
		tvIsXpUsed = (TextView) findViewById(R.id.main_tv_xposed_is_used);
		btnListenApk = (Button) findViewById(R.id.btn_listen_apk);
		btnEasyClick = (Button) findViewById(R.id.btn_easyClick);
		tvTips = (TextView) findViewById(R.id.tv_tips);
		paramList = (MyListView) findViewById(R.id.param_list);
		adapter = new ParamAdapter(this, list);
		paramList.setAdapter(adapter);
		controller.init();
		startService(new Intent(this, MyInternetService.class));
		startService(new Intent(getApplicationContext(), ListenService.class));
		initRect();
		setEvent();
		paramList.setFocusable(false);
		btnEasyClick.requestFocus();
	}
	private void setEvent() {
		btnListenApk.setOnLongClickListener(this);
		listenGroup.setOnLongClickListener(this);
		btnEasyClick.setOnLongClickListener(this);
	}

	private void initRect() {
		initTopBar();
		setTopTitle(getString(R.string.title_main));

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		controller.unregistReceiver();
	}

	@Override
	public void toast(String tips) {
		showToast(tips);
	}

	@Override
	public void setTips(String tips) {
		tvTips.setText(tips);
	}

	@Override
	public void notifyAdapter(List<DataMode> list) {
		adapter.setList(list);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void setListenPart(String packageName, Drawable icon, String appName) {
		ivListenIcon.setImageDrawable(icon);
		tvListenAppName.setText(appName);
		tvListenPackageName.setText(packageName);
	}

	@Override
	public void setListenBtnVisible(boolean isvisible) {
		if (isvisible) {
			btnListenApk.setVisibility(View.VISIBLE);
			listenGroup.setVisibility(View.GONE);
		} else {
			btnListenApk.setVisibility(View.GONE);
			listenGroup.setVisibility(View.VISIBLE);
		}
	}

	
	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		return;
	}

	@Override
	public void toastBig(String tips) {
		showBigToast(this, tips);
	}
	Toast toast1;
	@Override
	public void toastBig(final String tips, final int color) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				toast1 = toast1 == null ? Toast.makeText(MainActivity.this, "",Toast.LENGTH_SHORT) : toast1;
				toast1.setText(StringUtil.SpanColor(StringUtil.SpanSize(tips, 35),
						getResources().getColor(color)));
				toast1.show();
			}
		});
	}
	@Override
	public void showNoUseTips(final boolean isvisible, final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvIsXpUsed.setText(text);
				tvIsXpUsed.setVisibility(isvisible ? View.VISIBLE : View.GONE);
			}
		});
	}

	@Override
	public void easyClickable(boolean clickable) {
		btnEasyClick.setClickable(clickable);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		case R.id.btn_easyClick:
			controller.setEasyClick();
//			controller.limitContinue();
			break;
		case R.id.btn_listen_apk:
		case R.id.listen_layout:
			controller.openListenChoose();
			break;
		default:
			break;
		}
		
		return true;
	}

	@Override
	public void setMyTitleMainThread(final String title) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				setTopTitle(title);
			}
		});
	}
}
