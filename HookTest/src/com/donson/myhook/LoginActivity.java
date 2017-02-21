package com.donson.myhook;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.donson.controller.LoginViewController;
import com.donson.utils.AppInfosUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.viewinterface.LoginViewInterface;
import com.donson.xxxiugaiqi.R;

public class LoginActivity extends BaseActivity implements LoginViewInterface{
	EditText etAuthor ;
	Button btnVertify;
	TextView tvVersion;
	LoginViewController controller;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		controller = new LoginViewController(this, this);
		initRect();
		etAuthor = (EditText) findViewById(R.id.et_login_author);
		btnVertify = (Button) findViewById(R.id.btn_login_vertify);
		tvVersion = (TextView) findViewById(R.id.tv_login_version);
		String deviceCode = SPrefHookUtil.getLoginStr(this,SPrefHookUtil.KEY_LOGIN_DEVICE_CODE);
		if(!TextUtils.isEmpty(deviceCode)){
			etAuthor.setText(deviceCode);
		}
		setEvent();
//		controller.autoLogin();
	}
	private void initRect() {
		initTopBar();
		setTopTitle(getString(R.string.login_title));
	}
	private void setEvent() {
		tvVersion.setText(getString(R.string.login_tv_version)+AppInfosUtil.getVersion(getApplicationContext()));
		btnVertify.setOnClickListener(controller);
	}
	@Override
	public void toast(String tips) {
		showToast(tips);
	}
	@Override
	public String getAuthorization() {
		return etAuthor.getText().toString();
	}
	@Override
	public void setAuthorizationError(String err) {
		etAuthor.setError(err);
	}
	@Override
	public void toastBig(String tips) {
		showBigToast(this,tips);
	}
}
