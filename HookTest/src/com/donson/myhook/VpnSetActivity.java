package com.donson.myhook;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.donson.controller.VpnViewController;
import com.donson.viewinterface.VpnViewInterface;
import com.donson.xxxiugaiqi.R;
import com.mz.annotation.ContentViewInject;
import com.mz.annotation.InjectUtils;
import com.mz.annotation.ViewInject;

@ContentViewInject(R.layout.activity_vpn_set)
public class VpnSetActivity extends BaseActivity implements VpnViewInterface{
	@ViewInject(R.id.rl_vpn_auto_connect)
	RelativeLayout rlVpnAutoCon;
	@ViewInject(R.id.cb_vpn_connect)
	CheckBox cbVpnConn;
	@ViewInject(R.id.et_vpn_reconnect_time)
	EditText etVpnReconnTime;
	@ViewInject(R.id.et_vpn_connect_time)
	EditText etVpnConTime;
	@ViewInject(R.id.et_vpn_server_add)
	EditText etVpnServerAdd;
	@ViewInject(R.id.et_vpn_username)
	EditText etVpnUserName;
	@ViewInject(R.id.et_vpn_password)
	EditText etVpnPassword;
	@ViewInject(R.id.btn_vpn_save)
	Button btnVpnSave;
	@ViewInject(R.id.btn_vpn_delete_vpn)
	Button btnDeleteVpn;
	@ViewInject(R.id.btn_vpn_add_vpn)
	Button btnAddVpn;
	@ViewInject(R.id.btn_vpn_change_vpn)
	Button btnChangeVpn;
	@ViewInject(R.id.btn_vpn_connect_vpn)
	Button btnConnectVpn;
	@ViewInject(R.id.btn_vpn_disconnect_vpn)
	Button btnDisconnectVpn;
	
	VpnViewController controller;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InjectUtils.injectAll(this);
		controller = new VpnViewController(this, this);
		initRect();
		setEvent();
	}
	private void initRect() {
		initTopBar();
		setTopTitle(getString(R.string.vpn_title));
		setLeftBtnBack();
	}
	private void setEvent() {
		rlVpnAutoCon.setOnClickListener(controller);
		btnVpnSave.setOnClickListener(controller);
		btnAddVpn.setOnClickListener(controller);
		btnDeleteVpn.setOnClickListener(controller);
		btnChangeVpn.setOnClickListener(controller);
		btnConnectVpn.setOnClickListener(controller);
		btnDisconnectVpn.setOnClickListener(controller);
		controller.addTextWatchConn(etVpnConTime);
		controller.addTextWatchReConn(etVpnReconnTime);
	}

	@Override
	public void toast(String tips) {
		showToast(tips);
	}

	@Override
	public boolean getCheckBox() {
		return cbVpnConn.isChecked();
	}

	@Override
	public void setCheckBox(boolean value) {
		cbVpnConn.setChecked(value);
	}

	@Override
	public String getReConnText() {
		return etVpnReconnTime.getText().toString();
	}

	@Override
	public void setReConnText(boolean err,String time) {
		if(err){
			etVpnReconnTime.setError(time);
		}else{
			etVpnReconnTime.setText(time);
		}
		
	}

	@Override
	public String getConnText() {
		return etVpnConTime.getText().toString();
	}

	@Override
	public void setConnText(boolean err,String time) {
		if(err){
			etVpnConTime.setError(time);
		}else {
			etVpnConTime.setText(time);
		}
	}

	@Override
	public void toastBig(String tips) {
		showBigToast(this,tips);
	}
	@Override
	public String getServer() {
		return etVpnServerAdd.getText().toString();
	}
	@Override
	public void setServer(boolean err, String server) {
		etVpnServerAdd.setText(server);
		
	}
	@Override
	public String getUserName() {
		return etVpnUserName.getText().toString();
	}
	@Override
	public void setUserName(boolean err, String userName) {
		etVpnUserName.setText(userName);
	}
	@Override
	public String getPassword() {
		return etVpnPassword.getText().toString();
	}
	@Override
	public void setPassword(boolean err, String password) {
		etVpnPassword.setText(password);
	}
}
