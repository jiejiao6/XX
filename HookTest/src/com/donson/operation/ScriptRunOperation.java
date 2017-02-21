package com.donson.operation;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.donson.config.HttpConstants;
import com.donson.config.Logger;
import com.donson.myhook.services.ScriptService;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.xxxiugaiqi.R;
import com.donson.zhushoubase.BaseApplication;
import com.param.config.ConstantsConfig;

public class ScriptRunOperation {
	Activity context;
	BaseApplication app;
	public ScriptRunOperation(Activity context) {
		this.context = context;
		app = (BaseApplication) context.getApplication();
		if(SPrefHookUtil.getSettingBoolean(context, SPrefHookUtil.KEY_SETTING_IS_RUN_SCRIPT, SPrefHookUtil.D_SETTING_IS_RUN_SCRIPT)){
			testExecLuaScriptFile();
		}else {
			EasyClickUtil.setIsTaskRunning(this.context, EasyClickUtil.TASK_NOT_RUNNING);
			app.setIsRunning(false);
			Logger.i("--isRunning-scriptrunopt 26-"+EasyClickUtil.getIsTaskRunning(context));
		}
	}
	public void testExecLuaScriptFile() {
		context.stopService(new Intent(context, ScriptService.class));
		String scriptName = SPrefHookUtil.getCurTaskStr(context, SPrefHookUtil.KEY_CUR_TASK_SCRIPT_NAME);//SPrefHookUtil.getTaskStr(context, SPrefHookUtil.KEY_TASK_SCRIPT_NAME,SPrefHookUtil.D_TASK_SCRIPT_NAME);
		String name = scriptName+HttpConstants.SCRIPT_SUFFIX;
		String sourcePath = ConstantsConfig.EXTRA_PATH+File.separator+ ConstantsConfig.DOWNLOAD_SCRIPT_PATH_NAME+File.separator+name;
		if(!(new File(sourcePath)).exists()){
			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, context.getString(R.string.script_cur_script_not_exixt), Toast.LENGTH_SHORT).show();
				}
			});
		}
		context.startService(new Intent(context, ScriptService.class));
	}
	
	

}
