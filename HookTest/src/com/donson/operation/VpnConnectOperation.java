package com.donson.operation;

import com.donson.utils.VpnUtil;

import android.app.Activity;

public class VpnConnectOperation {
	Activity mActivity;
	public VpnConnectOperation(Activity mActivity) {
		this.mActivity = mActivity;
	}
	public void connectVpn(){
		if(VpnUtil.isVpnConnected()){
			VpnUtil.operaVpn(mActivity, 2);
		}else {
			VpnUtil.operaVpn(mActivity, 1);
		}
	}

}
