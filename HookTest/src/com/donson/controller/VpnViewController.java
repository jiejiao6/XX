package com.donson.controller;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.donson.utils.EasyClickUtil;
import com.donson.utils.OpenActivityUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.VpnUtil;
import com.donson.viewinterface.VpnViewInterface;
import com.donson.xxxiugaiqi.R;

public class VpnViewController implements OnClickListener{
	VpnViewInterface viewInterface;
	Activity mActivity;
	public VpnViewController(Activity mActivity,VpnViewInterface viewInterface) {
		this.viewInterface = viewInterface;
		this.mActivity = mActivity;
		init();
	}
	private void init() {
		boolean isChecked = SPrefHookUtil.getTaskBoolean(mActivity, SPrefHookUtil.KEY_TASK_VPN_AUTO_CONN,SPrefHookUtil.D_TASK_VPN_AUTO_CONN);
		viewInterface.setCheckBox(isChecked);
		int conn = SPrefHookUtil.getSettingInt(mActivity, SPrefHookUtil.KEY_SETTING_VPN_CONN_TIME, SPrefHookUtil.D_SETTING_VPN_CONN_TIME);
		viewInterface.setConnText(false, String.valueOf(conn));
		int reConn = SPrefHookUtil.getSettingInt(mActivity, SPrefHookUtil.KEY_SETTING_VPN_RECONN_TIME, SPrefHookUtil.D_SETTING_VPN_RECONN_TIME);
		viewInterface.setReConnText(false, String.valueOf(reConn));
		String server = EasyClickUtil.getVpnServer(mActivity);
		String userName = EasyClickUtil.getVpnUserName(mActivity);
		String password = EasyClickUtil.getVpnPassword(mActivity);
		viewInterface.setServer(true, server);
		viewInterface.setUserName(true, userName);
		viewInterface.setPassword(true, password);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_vpn_auto_connect:
			if(viewInterface.getCheckBox()){
				viewInterface.setCheckBox(false);
				SPrefHookUtil.putTaskBoolean(mActivity, SPrefHookUtil.KEY_TASK_VPN_AUTO_CONN, false);
			}else {
				viewInterface.setCheckBox(true);
				SPrefHookUtil.putTaskBoolean(mActivity, SPrefHookUtil.KEY_TASK_VPN_AUTO_CONN, true);
			}
			break;
		case R.id.btn_vpn_save:
			String reConn = viewInterface.getReConnText();
			String conn = viewInterface.getConnText();
			if(TextUtils.isEmpty(reConn)||(Integer.parseInt(reConn.toString())<2||Integer.parseInt(reConn.toString())>10)){
				viewInterface.setReConnText(true, mActivity.getString(R.string.vpn_et_re_conn_err));
				viewInterface.toast(mActivity.getString(R.string.vpn_et_re_conn_err));
				return;
			}if(TextUtils.isEmpty(conn)||(Integer.parseInt(conn.toString())<5||Integer.parseInt(conn.toString())>25)){
				viewInterface.setConnText(true, mActivity.getString(R.string.vpn_et_conn_err));
				viewInterface.toast(mActivity.getString(R.string.vpn_et_conn_err));
				return;
			}
			else {
				SPrefHookUtil.putSettingInt(mActivity, SPrefHookUtil.KEY_SETTING_VPN_CONN_TIME, Integer.parseInt(conn.toString()));
				SPrefHookUtil.putSettingInt(mActivity, SPrefHookUtil.KEY_SETTING_VPN_RECONN_TIME, Integer.parseInt(reConn.toString()));
			}
			EasyClickUtil.setVpnServer(mActivity, viewInterface.getServer());
			EasyClickUtil.setVpnUserName(mActivity, viewInterface.getUserName());
			EasyClickUtil.setVpnPassword(mActivity, viewInterface.getPassword());
			break;
		case R.id.btn_vpn_delete_vpn:
			openDeleteVpn();
			break;
		case R.id.btn_vpn_add_vpn:
			openVpnAdd();
			break;
		case R.id.btn_vpn_change_vpn:
			openChangeVpn();
			break;
		case R.id.btn_vpn_connect_vpn:
			openConnectVpn();
			break;
		case R.id.btn_vpn_disconnect_vpn:
			opendisConnectVpn();
			break;
		default:
			break;
		}
	}
	
	public void addTextWatchConn(EditText etVpnConTime) {
		etVpnConTime.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(TextUtils.isEmpty(s)||(Integer.parseInt(s.toString())<5||Integer.parseInt(s.toString())>25)){
					viewInterface.setConnText(true, mActivity.getString(R.string.vpn_et_conn_err));
				}
			}
		});
	}
	public void addTextWatchReConn(EditText etVpnReconnTime) {
		etVpnReconnTime.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(TextUtils.isEmpty(s)||(Integer.parseInt(s.toString())<2||Integer.parseInt(s.toString())>10)){
					viewInterface.setReConnText(true, mActivity.getString(R.string.vpn_et_re_conn_err));
				}
			}
		});
	}
	
	public void openVpnAdd() {
		
		OpenActivityUtil.openVpnAdd(mActivity);
	}
	public void openChangeVpn() {
		
		OpenActivityUtil.openVpnChange(mActivity);
	}
	
	public void openDeleteVpn() {
		OpenActivityUtil.openVpnDelete(mActivity);
		SPrefHookUtil.putTaskInt(mActivity, SPrefHookUtil.KEY_TASK_VPN_ID, 0);
	}
	private void opendisConnectVpn() {
		if(VpnUtil.isVpnConnected1()){
			EasyClickUtil.setvpnOptWhere(mActivity, EasyClickUtil.VPN_SET_OPT);
			OpenActivityUtil.openVpnDisConnect(mActivity);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			EasyClickUtil.setvpnConnectFlag(mActivity, EasyClickUtil.NOT_CONNECT_VPN);
		}else {
			viewInterface.toast("VPN is not connected!");
		}
		
	}
	private void openConnectVpn() {//com.android.vpndialogs/com.android.vpndialogs.ManageDialog
//		OpenActivityUtil.openApkByDetailInfo(mActivity, "com.android.vpndialogs", "com.android.vpndialogs.ManageDialog");
		EasyClickUtil.setvpnOptWhere(mActivity, EasyClickUtil.VPN_SET_OPT);
		OpenActivityUtil.openVpnConnect(mActivity);

	}
}
