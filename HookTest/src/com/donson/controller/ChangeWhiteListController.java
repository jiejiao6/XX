package com.donson.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.donson.config.Logger;
import com.donson.myhook.bean.AppInfosMode;
import com.donson.utils.OpenActivityUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.viewinterface.ChangeWhiteListViewInterface;
import com.donson.xxxiugaiqi.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ChangeWhiteListController {
	private final static int FLAG_ADD_WHITE = 0;
	private final static int FLAG_REMOVE_WHITE = 1;
	
	private final static int FLAG_WHITE = 0;
	private final static int FLAG_ALL = 1;
	Activity context = null;
	long lastClickTime;
	ChangeWhiteListViewInterface viewInterface = null;

	public ChangeWhiteListController(Activity context,
			ChangeWhiteListViewInterface viewInterface) {
		this.context = context;
		this.viewInterface = viewInterface;
	}

	public void chooseApp() {
		OpenActivityUtil.startAllAppActivity(context);
	}

	public void handleOnItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		
		TextView textView = (TextView) view.findViewById(R.id.tv_text);
		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_checkbox);
		if(checkBox.getVisibility()!=View.VISIBLE){
			return ;
		}
		if (Math.abs(lastClickTime - System.currentTimeMillis()) < 1000) {
			TranslateAnimation localTranslateAnimation = new TranslateAnimation(1, 0.0F, 1, -1.0F, 1, 0.0F, 1, 0.0F);
			localTranslateAnimation.setDuration(700L);
			view.startAnimation(localTranslateAnimation);
			checkBox.setChecked(!checkBox.isChecked());
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if(checkBox.isChecked()){
						AppInfosMode appInfosMode = viewInterface.getAppInfoList(FLAG_ALL).get(position);
						appInfosMode.setIscheck(true);
						viewInterface.updateMAppList(FLAG_ADD_WHITE, appInfosMode, position);
						viewInterface.notifyAllAppList();
						addWhiteJsonToSp(appInfosMode);
					}else {
						AppInfosMode appInfosMode = viewInterface.getAppInfoList(FLAG_WHITE).get(position);
						appInfosMode.setIscheck(false);
						viewInterface.updateMAppList(FLAG_REMOVE_WHITE, appInfosMode, position);
						viewInterface.notifyWhiteList();
						removeWhiteJsonToSp(appInfosMode);
					}
				}
			}, 700);
			
		}
		lastClickTime = System.currentTimeMillis();
	}
	protected void removeWhiteJsonToSp(AppInfosMode appInfosMode) {
		String str=SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_SETTING_CHANG_WHITE_LIST);
		List<String> listStr = null;
		Gson gson = new Gson();
		if(!TextUtils.isEmpty(str)){
			listStr = gson.fromJson(str, new TypeToken<List<String>>(){}.getType());
		}else {
			listStr =new ArrayList<String>();
		}
		if(listStr.size()>=1){
			listStr.remove(appInfosMode.getPackageInfo().packageName);
			String str2= gson.toJson(listStr);
			SPrefHookUtil.putSettingStr(context, SPrefHookUtil.KEY_SETTING_CHANG_WHITE_LIST, str2);
		}
	}

	private void addWhiteJsonToSp(AppInfosMode appInfosMode) {
		String str=SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_SETTING_CHANG_WHITE_LIST);
		List<String> listStr = null;
		Gson gson = new Gson();
		if(!TextUtils.isEmpty(str)){
			listStr = gson.fromJson(str, new TypeToken<List<String>>(){}.getType());
		}
		else {
			listStr =new ArrayList<String>();
		}
		listStr.add(appInfosMode.getPackageInfo().packageName);
		String str2= gson.toJson(listStr);
		SPrefHookUtil.putSettingStr(context, SPrefHookUtil.KEY_SETTING_CHANG_WHITE_LIST, str2);
	}
}
