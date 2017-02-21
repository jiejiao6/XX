package com.donson.myhook;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.donson.config.Logger;
import com.donson.controller.ScriptSetViewController;
import com.donson.myhook.adapters.StringAdapter;
import com.donson.viewinterface.ScriptSetViewInterface;
import com.donson.xxxiugaiqi.R;


public class ScriptSetActivity extends BaseActivity implements ScriptSetViewInterface{
	RelativeLayout rlScriptRun;
	CheckBox cbRunScript;
	TextView tvCurScriptName,tvIsCurScriptexist;
	Button btnChangeScriptName,btnChooseFromSd,btnClearAllScript,btnRun,btnScriptTime;
	ListView lvAllScript;
	StringAdapter adapter;
	ScriptSetViewController controller;
	List<String> allScript;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_script_set);
		initRect();
		controller = new ScriptSetViewController(this,this);
		rlScriptRun = (RelativeLayout) findViewById(R.id.rl_script_is_run);
		cbRunScript = (CheckBox) findViewById(R.id.cb_script_is_run_script);
		tvCurScriptName = (TextView) findViewById(R.id.tv_script_name);
		tvIsCurScriptexist = (TextView) findViewById(R.id.tv_script_is_script_exist);
		btnChangeScriptName = (Button) findViewById(R.id.btn_script_change_script_name);
		btnChooseFromSd = (Button) findViewById(R.id.btn_script_choose_sd);
		btnClearAllScript = (Button) findViewById(R.id.btn_script_clear_all_script);
		btnRun = (Button) findViewById(R.id.btn_script_run);
		btnScriptTime = (Button) findViewById(R.id.btn_script_time);
		allScript = controller.getAllScriptFileName();
		if(allScript ==null){
			allScript = new ArrayList<String>();
		}
		lvAllScript = (ListView) findViewById(R.id.lv_script_allScript);
		adapter = new StringAdapter(getApplicationContext(), allScript);
		lvAllScript.setAdapter(adapter);
		setEvent();
		controller.init();
	}
	private void setEvent() {
		rlScriptRun.setOnClickListener(controller);
		btnChangeScriptName.setOnClickListener(controller);
		btnChooseFromSd.setOnClickListener(controller);
		btnClearAllScript.setOnClickListener(controller);
		btnRun.setOnClickListener(controller);
		btnScriptTime.setOnClickListener(controller);
		
	}
	private void initRect() {
		initTopBar();
		setTopTitle(getString(R.string.script_title));
		setLeftBtnBack();
	}
	@Override
	public void toast(String tips) {
		showToast(tips);
	}
	@Override
	public void toastBig(String tips) {
		showBigToast(getApplicationContext(), tips);
	}
	@Override
	public void setTvScriptId(String scriptId) {
		tvCurScriptName.setText(scriptId);
	}
	@Override
	public String getScriptId() {
		return tvCurScriptName.getText().toString();
	}
	@Override
	public void setIsScriptRun(boolean is) {
		cbRunScript.setChecked(is);
	}
	@Override
	public void setIsScriptExist(String exist) {
		tvIsCurScriptexist.setText(exist);
	}
	@Override
	public boolean getIsScriptRun() {
		return cbRunScript.isChecked();
	}
	@Override
	public void notifyAllScript(List<String> list) {
		this.allScript.clear();
		allScript.addAll(list);
		adapter.notifyDataSetChanged();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		controller.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void setIsScriptExistColor(ColorStateList colors) {
		tvIsCurScriptexist.setTextColor(colors);
	}
	
}
