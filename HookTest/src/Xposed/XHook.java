package Xposed;

import Xposed.HookMain.AppInfos_XC_MethodHook;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.OpenActivityUtil;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedHelpers;

public abstract class XHook {
	public static  boolean vpnDisconnected_click=false;
	public static boolean showVpnConnClick=false;
//	public static boolean isShowVPN=false;
	public static boolean vpnConnect=false;
	public static final int OPERA_TIME_2S = 2000;
	public static final int OPERA_TIME_1S = 1000;
	
	public static boolean name = false;
	public static boolean server = false;
	public static boolean username = false;
	public static boolean password = false;
	public static boolean clickAdd = false;
	public static boolean saveAccount = false;
	public static boolean changeServer = false;
	public static boolean changeServerClick = false;
	public static boolean deleteVpn = false;
	public static boolean deleteVpndialog = false;
//	public static boolean vpnDisconnectFlag = false;
	public static LinearLayout vpnBackBtn = null;
	public static boolean vpnlistClicked = false;
	
	
	static boolean installallow = false;
	static boolean installclick = false;
	public static Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			Logger.h("what:"+msg.what);
			switch (msg.what) {
			case MethodInt.INSTALL_TEXT_VIEW:
				installallow = false;
				installclick = false;
				break;
				
			case MethodInt.VPN_DISCONNECT_OK://3012
				View view = (View) msg.obj;
					if(vpnBackBtn!=null) {
//						boolean res = setSimulateClick(view, 0, 0);
						boolean res = vpnBackBtn.performClick();
						Logger.h("返回==VPN_DISCONNECT_OK====="+res);
						vpnBackBtn = null;
					}
					if(EasyClickUtil.getvpnOptWhere(view.getContext())==EasyClickUtil.AUTO_OPT){
						OpenActivityUtil.openApkByDetailInfo(view.getContext(), ConstantsHookConfig.MY_PACKAGE_NAME, ConstantsHookConfig.MY_AUTO_ACTIVITY_NAME,"Xposed  60");
					}else if(EasyClickUtil.getvpnOptWhere(view.getContext())==EasyClickUtil.VPN_SET_OPT){
						OpenActivityUtil.openApkByDetailInfo(view.getContext(), ConstantsHookConfig.MY_PACKAGE_NAME, ConstantsHookConfig.MY_VPN_SET_ACTIVITY_NAME,"Xposed  62");
					}else if(EasyClickUtil.getvpnOptWhere(view.getContext())==EasyClickUtil.MAIN_DISCONNECT) {
						OpenActivityUtil.openApkByDetailInfo(view.getContext(), ConstantsHookConfig.MY_PACKAGE_NAME, ConstantsHookConfig.MY_MAIN_ACTIVITY_NAME,"Xposed  64");
					}
					vpnlistClicked = false;
//				}
				break;
			case MethodInt.VPN_DISCONNECT://3000
				final Button btn = (Button) msg.obj;
				Logger.h("VPN_DISCONNECT::"+EasyClickUtil.getvpnOptWhere(btn.getContext()));
				Logger.h("返回 vpnBackBtn   VPN_DISCONNECT_OK  dis;;;"+vpnBackBtn+"  vpnDisconnected_click:"+vpnDisconnected_click);
				vpnDisconnected_click=false;
				vpnlistClicked = false;
				break;
			case MethodInt.SHOW_VPN_CONNECT_OPERA://listview  界面3004
				Logger.h("showVpnConnClick||isShowVPN==="+showVpnConnClick+"-"/*+isShowVPN*/);
				if(showVpnConnClick/*||!isShowVPN*/) return;
				showVpnConnClick=true;
				ListView lv_a = (ListView) msg.obj;
				int count = msg.arg1;
				int pos = 0;
				if (count >2) {
					pos = 2;
				}
				Logger.h("count==="+count);
				lv_a.performItemClick(lv_a.getChildAt(pos), pos, lv_a.getItemIdAtPosition(pos));
				handler.sendEmptyMessageDelayed(MethodInt.SHOW_VPN_CONNECT, OPERA_TIME_2S);
				break;
			case MethodInt.SHOW_VPN_CONNECT://3005
				showVpnConnClick=false;
				break;
//			case MethodInt.IS_SHOW_VPN:
//				isShowVPN=false;
//				break;
			case MethodInt.VPN_CONNECT://3007
				final TextView ok = (TextView) msg.obj;
				boolean  res = ok.performClick();
				Logger.h("VPN_CONNECT  "+res);
				vpnConnect=false;
				name = false;
				username = false;
				server = false;
				password = false;
				saveAccount = false;
				clickAdd = false;
				saveAccount = false;
				changeServer = false;
				changeServerClick = false;
				deleteVpn =false;
				deleteVpndialog = false;
				vpnlistClicked = false;
				break;
			case MethodInt.VIEW_VPN_ADD_OK://3009
				try{
					TextView tv = (TextView) msg.obj;
					if(tv!=null)
						tv.performClick();
				}catch (Exception e) {
					Logger.h("EXE:::::::::::"+e);
				}
				break;
			case MethodInt.VIEW_VPN_ADD_ACCOUNT_OK://3010
				TextView ok2 = (TextView) msg.obj;
				ok2.performClick();
				clickAdd = false;
				vpnConnect=false;
				name = false;
				username = false;
				server = false;
				password = false;
				saveAccount = false;
				changeServerClick = false;
				changeServer = false;
				deleteVpn =false;
				deleteVpndialog = false;
				if(vpnBackBtn!=null) {
//					boolean res = setSimulateClick(view, 0, 0);
					boolean res3 = vpnBackBtn.performClick();
					Logger.h("返回==VIEW_VPN_ADD_ACCOUNT_OK====="+res3);
					vpnBackBtn = null;
				}
				OpenActivityUtil.openApkByDetailInfo(ok2.getContext(), ConstantsHookConfig.MY_PACKAGE_NAME, ConstantsHookConfig.MY_MAIN_ACTIVITY_NAME,"Xposed  139");
				break;
			case MethodInt.VPN_DELETE_OK://3011
				vpnConnect=false;
				name = false;
				username = false;
				server = false;
				password = false;
				saveAccount = false;
				clickAdd = false;
				saveAccount = false;
				changeServer = false;
				changeServerClick = false;
				deleteVpn =false;
				deleteVpndialog = false;
				if(msg.obj!=null){
					Context context = (Context) msg.obj;
					if(vpnBackBtn!=null) {
//						boolean res = setSimulateClick(view, 0, 0);
						boolean res2 = vpnBackBtn.performClick();
						Logger.h("返回==VPN_DELETE_OK====="+res2);
						vpnBackBtn = null;
					}
					OpenActivityUtil.openApkByDetailInfo(context, ConstantsHookConfig.MY_PACKAGE_NAME, ConstantsHookConfig.MY_MAIN_ACTIVITY_NAME,"162");
					context = null;
				}
				break;
			default:
				break;
			}
		};
	};
	abstract protected void handleMethod(String packageName,ClassLoader classLoader)
			throws Exception;
	
	
	abstract protected void after(String packageName,MethodHookParam param, int type)
			throws Exception;

	public void HookMethod(final String class1, String methodName,
			final ClassLoader classLoader, final String packageName,
			final int type, Object... objects) {
		Object[] new_objects = new Object[objects.length + 1];
		for (int i = 0; i < objects.length; i++) {
			new_objects[i] = objects[i];
		}
		new_objects[objects.length] = new AppInfos_XC_MethodHook(type,packageName);
		XposedHelpers.findAndHookMethod(class1, classLoader, methodName,new_objects);
	}

	public void HookMethod(final Class<?> class1, String methodName,
			final String packageName, final int type, Object... objects) {
		Object[] new_objects = new Object[objects.length + 1];
		for (int i = 0; i < objects.length; i++) {
			new_objects[i] = objects[i];
		}
		new_objects[objects.length] = new AppInfos_XC_MethodHook(type,packageName);
		XposedHelpers.findAndHookMethod(class1, methodName, new_objects);
	}

	public void HookMethod(final Class<?> class1, String methodName,
			final String packageName, final int type) {
		XposedHelpers.findAndHookMethod(class1, methodName,new Object[] { new AppInfos_XC_MethodHook(type, packageName) });
	}
	public void SystemVauleHook(String methodName, final String packageName, final int type, Object... objects) {
		Object[] new_objects = new Object[objects.length + 1];
		for (int i = 0; i < objects.length; i++) {
			new_objects[i] = objects[i];
		}
		new_objects[objects.length] = new AppInfos_XC_MethodHook(type, packageName);
		XposedHelpers.findAndHookMethod(System.class, methodName, new_objects);
		XposedHelpers.findAndHookMethod(Secure.class, methodName, new_objects);
	}
	public static  boolean setSimulateClick(View view, float x, float y) {
//		if (EasyOperateClickUtil.getAutoClickFlag(view.getContext()) == EasyOperateClickUtil.AUTOCLICK) {
			long downTime = SystemClock.uptimeMillis();
			final MotionEvent downEvent = MotionEvent.obtain(downTime,
					downTime, MotionEvent.ACTION_DOWN, x, y, 0);
			downTime += 200;
			final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
					MotionEvent.ACTION_UP, x, y, 0);

			boolean onTouchEvent = view.onTouchEvent(downEvent);
			boolean onTouchEvent2 = view.onTouchEvent(upEvent);
			downEvent.recycle();
			upEvent.recycle();
			return onTouchEvent && onTouchEvent2;
	}
}
