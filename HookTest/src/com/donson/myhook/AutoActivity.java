package com.donson.myhook;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.Toast;

import com.androlua.CrashHandler;
import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.controller.AutoOptViewController;
import com.donson.myhook.adapters.AutoItemAdapter;
import com.donson.myhook.bean.OptAppMode;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.StringUtil;
import com.donson.viewinterface.AutoOptViewInterface;
import com.donson.xxxiugaiqi.R;
import com.donson.zhushoubase.BaseApplication;

public class AutoActivity extends BaseActivity implements AutoOptViewInterface{
	private ListView lvAutoOpttips;
	private AutoItemAdapter adapter;
	List<OptAppMode> autolist;
	AutoOptViewController controller;
	BaseApplication app;
//	boolean isControl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
//		isControl = intent.getBooleanExtra(ConstantsHookConfig.EXTRA_CONTINUE_CONTROL, false);
		CrashHandler crashHandler = CrashHandler.getInstance();  
        crashHandler.init(getApplicationContext()); 
		app = (BaseApplication) getApplication();
		EasyClickUtil.setIsTaskRunning(this, EasyClickUtil.TASK_RUNNING);
		app.setIsRunning(true);
		Logger.i("-------#########------------isRunning===autoac 33"+EasyClickUtil.getIsTaskRunning(this));
		SPrefHookUtil.putSettingBoolean(getApplicationContext(), SPrefHookUtil.KEY_SETTING_RUN_AUTO,true);
		setContentView(R.layout.activity_auto_opt);
		lvAutoOpttips = (ListView) findViewById(R.id.lv_auto_tips);
		controller = new AutoOptViewController(this,this);
		autolist = new ArrayList<OptAppMode>();
		autolist.addAll(controller.getClosedListApkMode());
		adapter = new AutoItemAdapter(this, autolist);
		lvAutoOpttips.setAdapter(adapter);
		controller.init();
		initRect();
		setEvent();
	}
	@Override
	protected void onResume() {
		EasyClickUtil.setIsTaskRunning(this, EasyClickUtil.TASK_RUNNING);
		app.setIsRunning(true);
		Logger.i("--isRunning-auto ac 49-"+EasyClickUtil.getIsTaskRunning(this));
		super.onResume();
	}
	private void setEvent() {
		
	}
	private void initRect() {
		initTopBar();
		setTopLeftButtonDrawable(getResources().getDrawable(R.drawable.ic_launcher));
		setTopLeftText(getString(R.string.title_wait));
	}
	
	@Override
	public void toast(String tips) {
		showToast(tips);
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String title = (String) msg.obj ;
			setTopTitle(title);
		};
	};
	@Override
	public void setMyTitleMainThread(final String title) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				setTopTitle(title);
			}
		});
		
	}
	@Override
	public void setMyLeftTitleMainThread(final String title) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(title!=null)
				setTopLeftText(title+"");
			}
		});
	}
	@Override
	public void notifyAutoListMainThread(final Set<OptAppMode> newList) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				try {
					autolist.clear();
					autolist.addAll(newList);
					adapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SPrefHookUtil.putSettingBoolean(getApplicationContext(), SPrefHookUtil.KEY_SETTING_RUN_AUTO,false);
		controller.onDestory();
		controller = null;
	}
	Toast toast1;
	@Override
	public void toastBig(final String tips) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				toast1 = toast1==null?Toast.makeText(AutoActivity.this, "", Toast.LENGTH_SHORT):toast1;
				toast1.setText(StringUtil.SpanColor(StringUtil.SpanSize(tips, 35), getResources().getColor(R.color.green02)));
				toast1.show();
			}
		});
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void toastBig(final String tips, final int color) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				toast1 = toast1 == null ? Toast.makeText(AutoActivity.this, "",Toast.LENGTH_SHORT) : toast1;
				toast1.setText(StringUtil.SpanColor(StringUtil.SpanSize(tips, 35),
						getResources().getColor(color)));
				toast1.show();
			}
		});
	}
}
