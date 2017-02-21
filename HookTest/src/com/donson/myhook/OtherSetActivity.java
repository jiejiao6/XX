package com.donson.myhook;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.donson.config.ConstantsHookConfig;
import com.donson.utils.OpenActivityUtil;
import com.donson.xxxiugaiqi.R;
import com.mz.annotation.ContentViewInject;
import com.mz.annotation.InjectUtils;
import com.mz.annotation.OnClick;
import com.mz.annotation.ViewInject;
@ContentViewInject(R.layout.activity_other_set)
public class OtherSetActivity extends BaseActivity /*implements OnClickListener*/ {
	@ViewInject(R.id.btn_liucun)
	Button btnLiucun;
	@ViewInject(R.id.btn_vpn_set)
	Button btnVpnSet;
	
	@ViewInject(R.id.btn_main_script_set)
	Button btnScriptSet;
	
	@ViewInject(R.id.btn_chang_white_list)
	Button btnChangWhiteList;
	
	@ViewInject(R.id.btn_listen_file_opt)
	Button btnListenFileOpt;
	
	@ViewInject(R.id.btn_listen_sys_opt)
	Button btnListenSysOpt;
	
	@ViewInject(R.id.btn_main_wifi_sim)
	Button btnWifiSim;
	
	@ViewInject(R.id.btn_uninstall_apk)
	Button btnUninstallApk;
	
	@ViewInject(R.id.btn_show_debug_log)
	Button btnShowDebugLog;
	
	@ViewInject(R.id.btn_show_upload_log)
	Button btnUpladLog;
	
	@ViewInject(R.id.btn_show_install_log)
	Button btnShowInstallLog;
	
	@ViewInject(R.id.btn_chose_market)
	Button btnChoseMarket;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InjectUtils.injectAll(this);
		initRect();
		setEvent();
	}

	private void initRect() {
		initTopBar();
		setTopTitle(getString(R.string.title_other_set));
		setLeftBtnBack();
	}

	private void setEvent() {
//		btnLiucun.setOnClickListener(this);
//		btnVpnSet.setOnClickListener(this);
//		btnScriptSet.setOnClickListener(this);
//		btnChangWhiteList.setOnClickListener(this);
//		btnListenFileOpt.setOnClickListener(this);
//		btnListenSysOpt.setOnClickListener(this);
//		btnWifiSim.setOnClickListener(this);
//		btnUninstallApk.setOnClickListener(this);
//		btnShowDebugLog.setOnClickListener(this);
	}

	@OnClick({R.id.btn_liucun,R.id.btn_vpn_set,R.id.btn_main_script_set,R.id.btn_chang_white_list,R.id.btn_listen_file_opt,R.id.btn_listen_sys_opt,
		R.id.btn_main_wifi_sim,R.id.btn_uninstall_apk,R.id.btn_show_debug_log,R.id.btn_show_upload_log,R.id.btn_chose_market})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_liucun:
			openLiuCun();
			break;
		case R.id.btn_vpn_set:
			openVPNSet();
			break;
		case R.id.btn_main_script_set:
			openScriptSet();
			break;
		case R.id.btn_chang_white_list:
			openChangeWhiteList();
			break;
		case R.id.btn_listen_file_opt:
			openFileListen();
			break;
		case R.id.btn_listen_sys_opt:
			openSysListen();
			break;
		case R.id.btn_main_wifi_sim:
			openWifiSimSet();
			break;
		case R.id.btn_uninstall_apk:
			openUnInstall();
			break;
		case R.id.btn_show_debug_log:
//			openDebugLog(LOGFLAG_ERR);
			OpenActivityUtil.openTextActivity(OtherSetActivity.this,ConstantsHookConfig.PATH_RECORD_ERR_LOG);
			break;
		case R.id.btn_show_upload_log:
			openDebugLog(LOGFLAG_UPLOAD);
			break;
		case R.id.btn_chose_market:
			openMarketChose();
			break;
		default:
			break;
		}
	}
	private void openMarketChose() {
		OpenActivityUtil.startMarketActivity(this);
	}
	public static int LOGFLAG_ERR = 0;
	public static int LOGFLAG_UPLOAD = 1;
	private void openDebugLog(int flag) {
		OpenActivityUtil.startDebugLogActivity(this,flag);
	}

	public void openLiuCun() {
		OpenActivityUtil.startLiuCunActivity(this);
	}
	public void openVPNSet() {
		OpenActivityUtil.startVpnSetActivity(this);
	}
	public void openScriptSet() {
		OpenActivityUtil.startScriptSetActivity(this);
	}
	public void openChangeWhiteList() {
		OpenActivityUtil.startChangeWhiteListActivity(this);
	}
	
	public void openFileListen() {
		OpenActivityUtil.startFileOptListenActivity(this);
	}
	public void openSysListen() {
		OpenActivityUtil.startSysOptListenActivity(this);
	}
	
	public void openWifiSimSet() {
		OpenActivityUtil.startWifiSimActivity(this);
	}
	protected void openUnInstall() {
		OpenActivityUtil.startUninstallActivity(this);
	}

}
