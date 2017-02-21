package com.donson.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import Http.HttpUtil2;
import Http.ProgressEvent;
import Http.ShowDownDialogEvent;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.HttpConstants;
import com.donson.xxxiugaiqi.R;
import com.param.netInterface.HttpUtil.ResponseListener;
import com.param.utils.CommonOperationUtil;

public class CheckUpdateUtil {
	Context context;
//	ProgressDialog ddialog = null;
	public CheckUpdateUtil(Context context ) {
		this.context = context;
	}
	public static final String MyApkpath = ConstantsHookConfig.APK_LOCAL_MY_PATH+"/"+CommonOperationUtil.convertPackageName2Apk(ConstantsHookConfig.MY_PACKAGE_NAME);

	public void checkUpdate() {
		HttpUtil2 httpUtil2 = new HttpUtil2(context);
		httpUtil2.setListener(new ResponseListener() {
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
								showToast(context.getString(R.string.left_version_no_change));
							}else {
								checkLocalVersion(version);
							}
							break;
						default:
							String message = jsonObject.optString("MESSAGE");
							showToast(message);
							break;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					showToast(context.getString(R.string.left_get_version_err));
				}
			}
		});
		httpUtil2.checkUpdate();
	}
	protected void checkLocalVersion(String newVer) {
		File file = new File(MyApkpath);
		if(file.exists()){
			String localVer = AppInfosUtil.getApkVersionByPath(context, MyApkpath);
			if(newVer.equals(localVer)){
				install();
			}else {
				downLoadNewApk(newVer);
			}
		}else {
			downLoadNewApk(newVer);
		}
	}
	HttpUtil2 httpUtil2 =null;
	public void downLoadNewApk(String newVer) {
		httpUtil2 = new HttpUtil2(context);
		Map<String, String> map = new HashMap<String, String>();
		if(ConstantsHookConfig.IS_MOBILE){
			map.put(HttpConstants.REQUEST_KEY_URL, "version/"+newVer+".apk");
		}else {
			map.put(HttpConstants.REQUEST_KEY_URL, "version/"+newVer+"_pc.apk");
		}
		map.put(HttpConstants.REQUEST_KEY_PATH, ConstantsHookConfig.DOWNLOAD_MY_AKP_PATH+CommonOperationUtil.convertPackageName2Apk(ConstantsHookConfig.MY_PACKAGE_NAME));
		EventBus.getDefault().post(new ShowDownDialogEvent(String.format(context.getString(R.string.left_version_updating),newVer)));
		httpUtil2.setListener(new ResponseListener() {
			
			@Override
			public void result(Object obj) {
				if(obj!=null&&obj instanceof String){
					String res = (String) obj;
					if(res.equals(HttpConstants.RESPONSE_OK)){
						
						showToast(context.getString(R.string.left_version_down_ok));
						install();
					}else {
						showToast(context.getString(R.string.left_version_down_err));
					}
				}else {
					showToast(context.getString(R.string.left_version_down_err));
				}
			}
		});
		httpUtil2.downBigFileFromServer(context, map, false);
	}
	
	protected void install() {
	    SendBroadCastUtil.upDateXX(context,MyApkpath);
	}
	
	Toast toast;
	public void showToast(final String tips){
		toast = toast==null?Toast.makeText(context, "", Toast.LENGTH_SHORT):toast;
		toast.setText(tips);
		toast.show();
	}

}
