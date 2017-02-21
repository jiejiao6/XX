package com.donson.operation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Xposed.FileSysOptRecordToFile;
import Xposed.MethodInt;
import Xposed.XposedParamHelpUtil;
import android.content.Context;
import android.provider.Settings.System;
import android.text.TextUtils;

import com.donson.config.Logger;
import com.donson.myhook.bean.OptAppMode;
import com.donson.utils.SPrefHookUtil;
import com.donson.viewinterface.AutoOptViewInterface;

public class SystemValueOperation {
	AutoOptViewInterface viewInterface;
	String listenApk;
	Context context;
	Set<OptAppMode> listenSet;
	OptAppMode listenApkMode;
	Set<String> changeSet;
	public SystemValueOperation(Context context, OptAppMode listenApkMode,
			AutoOptViewInterface viewInterface) {
		this.context = context;
		this.viewInterface = viewInterface;
		this.listenApkMode = listenApkMode;
		listenSet = new HashSet<OptAppMode>();
		changeSet = new HashSet<String>();
		listenApk = SPrefHookUtil.getSettingStr(context,SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
		getSysVaule();
	}

	public void getSysVaule() {
		if (listenSet != null && listenApkMode != null) {
			listenApkMode.setFlag(OptAppMode.FLAG_SYS_OPT);
			listenApkMode.setSysCount(-1);
			listenApkMode.setSysFailCount(-1);
			int count = 0;
			Set<String> systemSet = FileSysOptRecordToFile.getSysStringset(listenApk);
			Iterator<String> iterator = systemSet.iterator();
			while (iterator.hasNext()) {
				String string = (String) iterator.next();
				try {
					JSONObject jsonObject = new JSONObject(string);
					JSONArray args = jsonObject
							.optJSONArray(XposedParamHelpUtil.KEY_ARGS);
					String arg1 = (String) args.opt(0);
					String method = jsonObject.optString(XposedParamHelpUtil.KEY_METHOD);
					if (!TextUtils.isEmpty(arg1)&&method.equals(MethodInt.GET_STRING)
							|| method.equals(MethodInt.PUT_STRING)
							|| method.equals(MethodInt.GET_INT)
							|| method.equals(MethodInt.PUT_INT)) {
						if (arg1.equals("android_id")) {
							continue;
						}
						changeSet.add(arg1);
						count++;
					} else {
						continue;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			listenApkMode.setSysCount(count);
			listenSet.clear();
			listenSet.add(listenApkMode);
			viewInterface.notifyAutoListMainThread(listenSet);
		}
	}

	public int recoverSysValue() {
		int failedCount = 0;
		// changeSet
		if (changeSet.size() > 0) {
			Iterator<String> iterator = changeSet.iterator();
			while (iterator.hasNext()) {
				String string = (String) iterator.next();
				boolean result = System.putString(context.getContentResolver(),string, null);
				if (!result) {
					failedCount++;
				}
			}
		}
		if (listenSet != null && listenApkMode != null) {
			listenApkMode.setSysFailCount(failedCount);
			listenSet.clear();
			listenSet.add(listenApkMode);
			viewInterface.notifyAutoListMainThread(listenSet);
		}
		return failedCount;
	}
}
