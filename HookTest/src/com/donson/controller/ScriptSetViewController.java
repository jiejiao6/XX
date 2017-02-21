package com.donson.controller;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.HttpConstants;
import com.donson.operation.ScriptRunOperation;
import com.donson.utils.MyfileUtil;
import com.donson.utils.OpenActivityUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.viewinterface.ScriptSetViewInterface;
import com.donson.xxxiugaiqi.R;

public class ScriptSetViewController implements OnClickListener{
	Activity mActivity;
	ScriptSetViewInterface viewInterface;
	public ScriptSetViewController(Activity mActivity,ScriptSetViewInterface viewInterface) {
		this.mActivity = mActivity;
		this.viewInterface = viewInterface;
		
	}
	public void init() {
		getIsRunScript();
		String scriptName = getScriptName();
		getIsScriptExist(scriptName);
	}
	private void getIsRunScript() {
		boolean isRunscript = SPrefHookUtil.getSettingBoolean(mActivity, SPrefHookUtil.KEY_SETTING_IS_RUN_SCRIPT, SPrefHookUtil.D_SETTING_IS_RUN_SCRIPT);
		viewInterface.setIsScriptRun(isRunscript);
	}
	private String getScriptName() {
		String script_name = SPrefHookUtil.getTaskStr(mActivity, SPrefHookUtil.KEY_TASK_SCRIPT_NAME, SPrefHookUtil.D_TASK_SCRIPT_NAME);
		viewInterface.setTvScriptId(String.format(mActivity.getString(R.string.script_cur_script_name), script_name));
		return script_name;
	}
	private void getIsScriptExist(String scriptName) {
		final String name = scriptName+HttpConstants.SCRIPT_SUFFIX;
		String path = ConstantsHookConfig.SCRIPT_FILE+name;
		File file = new File(path);
		if(!file.exists()){
			viewInterface.setIsScriptExist(mActivity.getString(R.string.script_cur_script_not_exixt));
			viewInterface.setIsScriptExistColor(mActivity.getResources().getColorStateList(R.color.red));
		}else {
			viewInterface.setIsScriptExist(mActivity.getString(R.string.script_cur_script_a_exixt));
			viewInterface.setIsScriptExistColor(mActivity.getResources().getColorStateList(R.color.black));
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_script_is_run:
			boolean isRunscript = viewInterface.getIsScriptRun();
			viewInterface.setIsScriptRun(!isRunscript);
			SPrefHookUtil.putSettingBoolean(mActivity, SPrefHookUtil.KEY_SETTING_IS_RUN_SCRIPT, !isRunscript);
			break;
		case R.id.btn_script_change_script_name:
			doChangeScriptName();
			break;
		case R.id.btn_script_choose_sd:
			chooseFromSd();
			break;
		case R.id.btn_script_clear_all_script:
			deleteAllScript();
			getIsScriptExist(getScriptName());
			viewInterface.notifyAllScript(getAllScriptFileName());
			break;
		case R.id.btn_script_run:
			ScriptRunOperation script = new ScriptRunOperation(mActivity);
			break;
		case R.id.btn_script_time:
			doChangeScriptTime();
			break;
		default:
			break;
		}
	}
	private void doChangeScriptTime() {
		LayoutInflater  inflater = LayoutInflater.from(mActivity);
		View view = inflater.inflate(R.layout.dialog_chang_script_id, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle(mActivity.getString(R.string.script_btn_change_script_time));
		builder.setView(view);
		final TextView tvTips = (TextView) view.findViewById(R.id.script_tc_tips);
		tvTips.setText(mActivity.getString(R.string.script_tv_tip_script_time));
		final TextView suffix = (TextView) view.findViewById(R.id.et_script_suffix);
		suffix.setText("s");
		final EditText et = (EditText) view.findViewById(R.id.et_script_or_id);
		int scriptTime = SPrefHookUtil.getTaskInt(mActivity, SPrefHookUtil.KEY_TASK_SCRIPT_TIME, SPrefHookUtil.D_TASK_SCRIPT_TIME);
		et.setText(""+scriptTime);
		et.setSelection(String.valueOf(scriptTime).length());
		builder.setNegativeButton(mActivity.getString(R.string.script_btn_cancel), null);
		builder.setPositiveButton(mActivity.getString(R.string.script_btn_yes), null);
		builder.create();
		final AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(et.getText())){
					viewInterface.toast(mActivity.getString(R.string.script_time_can_not_null));
					et.setError(mActivity.getString(R.string.script_time_can_not_null));
				}else {
					 boolean is = SPrefHookUtil.putTaskInt(mActivity, SPrefHookUtil.KEY_TASK_SCRIPT_TIME, Integer.valueOf(et.getText().toString()));
					 dialog.dismiss();
					 if(is){
						 viewInterface.toast(mActivity.getString(R.string.script_time_set_ok));
					 }
				}
			}
		});
	}
	private void deleteAllScript() {
		List<String> script = getAllScriptFile();
		if(script!=null&&script.size()>=1){
			deleteScript(script);
		}
	}
	private void deleteScript(List<String> script) {
		for (String path:script) {
			File file = new File(path);
			if(file.exists()){
				file.delete();
			}
		}
	}
	public List<String> getAllScriptFile() {
		Set<String> set =null;
		
		try {
			File file = new File(ConstantsHookConfig.SCRIPT_LOCAL_PATH);
			if (!file.exists()) {
				file.mkdirs();
				return null;
			}else {
				File[] files = file.listFiles();
				if(files!=null&&files.length>0){
					set = new HashSet<String>();
					for(int i = 0;i<files.length;i++){
						String path = files[i].getAbsolutePath();
//						Logger.i("allScript======name="+path.substring(path.lastIndexOf("/"),path.length()));
						set.add(path);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> script = new ArrayList<String>();
		if(set!=null){
			for (Iterator iterator = set.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				if(string.endsWith(HttpConstants.SCRIPT_SUFFIX)){
					script.add(string);
					
				}
			}
		}
		return script;
	}
	public List<String> getAllScriptFileName() {
		int dirLen = ConstantsHookConfig.SCRIPT_LOCAL_PATH.length();
		List<String> listName = new ArrayList<String>();
		List<String> list = getAllScriptFile();
		if(list!=null&&list.size()>0){
			for(String path : list){
				if(path.length()>dirLen){
					listName.add(path.substring(path.lastIndexOf("/")+1));
					
				}
			}
		}
		return listName;
	}
	private void doChangeScriptName() {
		LayoutInflater  inflater = LayoutInflater.from(mActivity);
		View view = inflater.inflate(R.layout.dialog_chang_script_id, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle(mActivity.getString(R.string.script_btn_change_orider_id_titel));
		builder.setView(view);
		final EditText et = (EditText) view.findViewById(R.id.et_script_or_id);
		String scriptName = getScriptName();
		et.setText(""+scriptName);
		et.setSelection(scriptName.length());
		builder.setNegativeButton(mActivity.getString(R.string.script_btn_cancel), null);
		builder.setPositiveButton(mActivity.getString(R.string.script_btn_yes), null);
		builder.create();
		final AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(et.getText())){
					viewInterface.toast(mActivity.getString(R.string.script_orider_id_can_not_null));
					et.setError(mActivity.getString(R.string.script_orider_id_can_not_null));
				}else {
					 SPrefHookUtil.putTaskStr(mActivity, SPrefHookUtil.KEY_TASK_SCRIPT_NAME, et.getText().toString());
					 getIsScriptExist(getScriptName());
					 dialog.dismiss();
				}
			}
		});
	}
	static final int FLAG_REPLACE = 0;
	static final int FLAG_ADD = 1;
	int flag ;
	private void chooseFromSd() {
		AlertDialog alertDialog = new AlertDialog.Builder(mActivity).setPositiveButton(mActivity.getString(R.string.script_change_cur_script), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				flag = FLAG_REPLACE;OpenActivityUtil.openSdChose(mActivity);
			}
		}).setNegativeButton(mActivity.getString(R.string.script_add_script), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				flag = FLAG_ADD;OpenActivityUtil.openSdChose(mActivity);
			}
		}).create();
		alertDialog.show();
		
	}
	boolean result = false;
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {// 是否选择，没选择就不会继续
			switch (requestCode) {
			case ConstantsHookConfig.requestSD:
				Uri uri = data.getData();// 得到uri，后面就是将uri转化成file的过程。
				try {
					File file = new File(new URI(uri.toString()));
					String path = file.getAbsolutePath();
					final String fileName = path.substring(path.lastIndexOf("/") + 1,path.length()).trim().replace(" ", "");
					final String sourcePath = path;
					if(fileName.endsWith(HttpConstants.SCRIPT_SUFFIX)){
						if (flag == FLAG_REPLACE) {
							result = MyfileUtil.copyLua2XXScriptFile(sourcePath, getScriptName()+HttpConstants.SCRIPT_SUFFIX,true);
							if(result){
								viewInterface.toast(mActivity.getString(R.string.script_opt_ok));
							}else {
								viewInterface.toast(mActivity.getString(R.string.script_opt_err));
							}
						}else {
							List<String> names = getAllScriptFileName();
							if(names.contains(fileName)){
								AlertDialog alertDialog = new AlertDialog.Builder(mActivity)
										.setMessage(mActivity.getString(R.string.script_exist))
										.setPositiveButton(mActivity.getString(R.string.script_btn_yes),
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface dialog,int which) {
														result = MyfileUtil.copyLua2XXScriptFile(sourcePath, fileName,true);
														viewInterface.notifyAllScript(getAllScriptFileName());
														if(result){
															viewInterface.toast(mActivity.getString(R.string.script_opt_ok));
														}else {
															viewInterface.toast(mActivity.getString(R.string.script_opt_err));
														}
													}
												})
										.setNegativeButton(mActivity.getString(R.string.script_btn_cancel),new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												return;
											}
										}).create();
								alertDialog.show();
							}else {
								result = MyfileUtil.copyLua2XXScriptFile(sourcePath, fileName,false);
								if(result){
									viewInterface.toast(mActivity.getString(R.string.script_opt_ok));
								}else {
									viewInterface.toast(mActivity.getString(R.string.script_opt_err));
								}
							}
						}
						viewInterface.notifyAllScript(getAllScriptFileName());
						
					}else {
						viewInterface.toast(String.format(mActivity.getString(R.string.script_sd_suffix_err), HttpConstants.SCRIPT_SUFFIX));
					}
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

}
