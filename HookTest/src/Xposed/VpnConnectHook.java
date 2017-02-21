package Xposed;

import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.donson.config.Logger;
import com.donson.utils.EasyClickUtil;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;

public class VpnConnectHook extends XHook {
	private static VpnConnectHook instance;
	private VpnConnectHook() {
		// TODO Auto-generated constructor stub
	}
	public static VpnConnectHook getInstance(){
		if(instance==null){
			synchronized (VpnConnectHook.class) {
				if(instance == null){
					instance = new VpnConnectHook();
				}
			}
		}
		return instance;
	}

	@Override
	protected void handleMethod(String packageName, ClassLoader classLoader)
			throws Exception {
		HookMethod(View.class, "refreshDrawableState", packageName,MethodInt.VPN_DISCONNECT);
	}

	@Override
	protected void after(String packageName, MethodHookParam param, int type)
			throws Exception {
		Object obj;
		switch (type) {
		case MethodInt.VPN_DISCONNECT:
			obj=param.thisObject;
			if(obj instanceof Button){
				Button btn=(Button)obj;
				if((EasyClickUtil.getvpnConnectFlag(btn.getContext())==EasyClickUtil.NOT_CONNECT_VPN)){
					return;
				}
				if(btn.getText().equals("Disconnect")||btn.getText().equals("断开连接")){
					Logger.h("dis:vpnDisconnected_click"+vpnDisconnected_click);
					if(vpnDisconnected_click) return;
					vpnDisconnected_click=true;
					try{
						boolean res = btn.performClick();
						Message msg = handler.obtainMessage();
						msg.obj = btn;
						msg.what = MethodInt.VPN_DISCONNECT;
						handler.sendMessageDelayed(msg, 1000);
						Logger.i("res-------------"+res);
//						EasyClickUtil.setvpnDisConnectFlag(btn.getContext(),false);//  没有权限
					}catch(Exception e){
						Logger.h("EXdis:"+e);
						e.printStackTrace();
					}
					
				}
			}
			break;

		default:
			break;
		}
	}

}
