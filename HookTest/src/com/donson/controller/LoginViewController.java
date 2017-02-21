package com.donson.controller;

import imei.util.IMEIGET;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import Http.HttpUtil2;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.donson.utils.OpenActivityUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.viewinterface.LoginViewInterface;
import com.donson.xxxiugaiqi.R;
import com.donson.zhushoubase.BaseApplication;
import com.param.netInterface.HttpUtil.ResponseListener;

public class LoginViewController implements OnClickListener {
	Activity context;
	LoginViewInterface viewInterface;
	TelephonyManager tm;
	BaseApplication app;
	public LoginViewController(Activity context, LoginViewInterface viewInterface) {
		this.context = context;
		this.viewInterface = viewInterface;
		 tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		 app = (BaseApplication) context.getApplication();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login_vertify:
			String author = viewInterface.getAuthorization();
			btnVertifyClick(author);
			break;

		default:
			break;
		}
	}
	private ProgressDialog showDialog(){
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage("loading");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
		return dialog;
	}
	private void btnVertifyClick(String author) {
		
		if(TextUtils.isEmpty(author)){
			viewInterface.setAuthorizationError(context.getString(R.string.login_et_author_not_null));
			viewInterface.toast(context.getString(R.string.login_et_author_not_null));
		}else {
			final ProgressDialog dialog = showDialog();
			HttpUtil2 httpUtil2 = new HttpUtil2(context);
			Map<String, String> map = new HashMap<String,String>();
			IMEIGET imeiget = new IMEIGET(context);
			String imei = imeiget.getImei();//tm.getDeviceId();
			map.put(HttpUtil2.KEY_IMEI, imei);
			map.put(HttpUtil2.KEY_AUTHOR, author);
			httpUtil2.setListener(new ResponseListener() {
				@Override
				public void result(Object obj) {
					if(obj == null){
						viewInterface.toast(context.getString(R.string.login_err_net));
						if(dialog!=null&&dialog.isShowing())
							dialog.dismiss();
					}else {
						if(obj instanceof String){
							String str = (String) obj;
							handleLoginReturnOk(str,dialog);
						}else if (obj instanceof Integer) {
							if(dialog!=null&&dialog.isShowing()){
								dialog.dismiss();
							}
							viewInterface.toast(context.getString(R.string.login_err_net_err_code)+ (Integer)obj);
						}
					}
				}
			});
			httpUtil2.getLogin(map);
		}
	}
	public static final String JSON_KEY_CODE = "CODE";
	public static final String JSON_KEY_MESSAGE = "MESSAGE";
	public static final String JSON_KEY_DATA = "DATA";
	public static final String JSON_KEY_RESULT = "RESULT";
	public static final String JSON_KEY_DEVICE_CODE = "deviceCode";
	public static final String JSON_KEY_DEVICE_ID = "deviceId";
	protected void handleLoginReturnOk(String str, ProgressDialog dialog) {
		if(TextUtils.isEmpty(str)){
			viewInterface.toast(context.getString(R.string.login_err_net));
			return;
		}
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(str);
			int code = jsonObject.optInt(JSON_KEY_CODE);
			switch (code) {
			case 0:
				JSONObject data = jsonObject.optJSONObject(JSON_KEY_DATA);
				JSONObject result = data.optJSONObject(JSON_KEY_RESULT);
//				String imei = result.optString("bindImei");
				String deviceCode = result.optString(JSON_KEY_DEVICE_CODE);
				String deviceId = result.optString(JSON_KEY_DEVICE_ID);
//				String oldImei = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_IMEI);
				String oldDeviceCode = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_CODE);
				String oldDeviceId = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_ID);
				if(isReSetString(deviceCode,oldDeviceCode)){
					SPrefHookUtil.putLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_CODE, deviceCode);
				}
				if(isReSetString(deviceId, oldDeviceId)){
					SPrefHookUtil.putLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_ID, deviceId);
				}
//				SPrefHookUtil.putLoginStr(context, SPrefHookUtil.KEY_LOGIN_IMEI, imei);
				if(dialog!=null&&dialog.isShowing()){
					dialog.dismiss();
				}
				app.setLogined(true);
				OpenActivityUtil.startMainActivity(context);
				context.finish();
				context.overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);
				break;
			case -10001:
				if(dialog!=null&&dialog.isShowing()){
					dialog.dismiss();
				}
				viewInterface.toast(context.getString(R.string.login_err_param_err));
				break;
			case -30001:
				if(dialog!=null&&dialog.isShowing()){
					dialog.dismiss();
				}
				viewInterface.toast(context.getString(R.string.login_err_no_author));
				break;
			case -30002:
				String message1 = jsonObject.optString(JSON_KEY_MESSAGE);
				if(dialog!=null&&dialog.isShowing()){
					dialog.dismiss();
				}
				viewInterface.toast(context.getString(R.string.login_err_author_used));
				break;
			default:
				String message = jsonObject.optString(JSON_KEY_MESSAGE);
				if(dialog!=null&&dialog.isShowing()){
					dialog.dismiss();
				}
				viewInterface.toast(context.getString(R.string.login_err_net_err_code)+message );
				break;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(dialog!=null&&dialog.isShowing()){
				dialog.dismiss();
			}
			viewInterface.toast(context.getString(R.string.login_err_json_err));
		}
	}
	private boolean isReSetString(String newStr,String oldStr) {
		if((TextUtils.isEmpty(oldStr)&&!TextUtils.isEmpty(newStr))||(!TextUtils.isEmpty(oldStr)&&!TextUtils.isEmpty(newStr)&&!oldStr.equals(newStr))){
			return true;
		}
		return false;
	}
	public void autoLogin() {
		
		if(app.getIsLogined()){
			OpenActivityUtil.startMainActivity(context);
			context.finish();
			context.overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);
		}
		String oldDeviceCode = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_CODE);
		if(TextUtils.isEmpty(oldDeviceCode)){
			return;
		}
		btnVertifyClick(oldDeviceCode);
	}
}
