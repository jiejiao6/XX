package Xposed;

import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.donson.config.Logger;
import com.donson.utils.EasyClickUtil;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;

public class SettingVpnHook extends XHook {
	private static SettingVpnHook instance;
	private SettingVpnHook() {
		// TODO Auto-generated constructor stub
	}
	public static SettingVpnHook getInstance(){
		if(instance==null){
			synchronized (SettingVpnHook.class) {
				if(instance == null){
					instance = new SettingVpnHook();
				}
			}
		}
		return instance;
	}
	@Override
	protected void handleMethod(String packageName, ClassLoader classLoader)
			throws Exception {
		name = false;
		server = false;
		clickAdd = false;
		username = false;
		password = false;
		saveAccount = false;
		vpnlistClicked = false;
		HookMethod(View.class, "dispatchTouchEvent", packageName,
				MethodInt.DIS, MotionEvent.class);
		HookMethod(AbsListView.class, "isTextFilterEnabled",packageName,
				MethodInt.ABSLISTVIEW_ISTEXTFILTERENABLED);//点击listview、
//		HookMethod(View.class, "refreshDrawableState", packageName,MethodInt.VIEW_TOSTRING_VPN_SETTING_ISSHOW_VPN);//pptp
		HookMethod(View.class, "refreshDrawableState", packageName,MethodInt.VIEW_TOSTRING_VPN_SETTING_CONNECT);
		HookMethod(View.class, "refreshDrawableState", packageName,MethodInt.VIEW_VPN_ADD);
	}
	@Override
	protected void after(String packageName, MethodHookParam param, int type)
			throws Exception {
		Object obj;
		switch (type) {
		case MethodInt.ABSLISTVIEW_ISTEXTFILTERENABLED://点击listview
			obj = param.thisObject;
			if (obj instanceof ListView) {
				ListView lv_a = (ListView) obj;
				Logger.h("vpn-------------1----------list"+((EasyClickUtil.getIsAddingVpn(lv_a.getContext())||EasyClickUtil.getIsChangingVpn(lv_a.getContext()))&&server));
				if((EasyClickUtil.getIsAddingVpn(lv_a.getContext())||EasyClickUtil.getIsChangingVpn(lv_a.getContext()))&&server){
					int count = lv_a.getCount();
					int pos = 0;
					if (count >2) {
						pos = 2;
					}
					Logger.h("count==="+count);
					lv_a.performItemClick(lv_a.getChildAt(pos), pos, lv_a.getItemIdAtPosition(pos));
				}else {
					Logger.h("vpn------------2----------list"+((EasyClickUtil.getvpnConnectFlag(lv_a.getContext())==EasyClickUtil.NOT_CONNECT_VPN)));
					if((EasyClickUtil.getvpnConnectFlag(lv_a.getContext())==EasyClickUtil.NOT_CONNECT_VPN)){
						return;
					}
					int count = lv_a.getCount();
					Logger.h("vpn-------------count----------list" + count);
					if (count > 2) {
						if(!vpnlistClicked){
							vpnlistClicked = true;
							lv_a.performItemClick(lv_a.getChildAt(2), 2,lv_a.getItemIdAtPosition(2));
							Message msg = handler.obtainMessage();
							msg.obj = lv_a;
							msg.what = MethodInt.VPN_DISCONNECT_OK;
							Logger.h("what-----"+msg.what);
							handler.sendMessageDelayed(msg, 1000);
						}
					}

				}
			}
			break;
		case MethodInt.VIEW_TOSTRING_VPN_SETTING_CONNECT:// 点击连接按钮
			obj = param.thisObject;
			if (obj instanceof TextView) {
				TextView btn=(TextView)obj;
				
				if(!TextUtils.isEmpty(btn.getText())&&"VPN".equals(btn.getText().toString())){
					ViewGroup group = (ViewGroup) btn.getParent().getParent();
					if(group.getChildCount()==2){
						if(group.getChildAt(0) instanceof ImageView){
//							if(vpnBackBtn==null)
								vpnBackBtn = (LinearLayout) group;
						}
					}
				}
				if(EasyClickUtil.getIsAddingVpn(btn.getContext())||EasyClickUtil.getIsChangingVpn(btn.getContext())){
//					Logger.h("username====="+username+"  pass:"+password+"   saveAccount:;:"+saveAccount+"   "+vpnConnect);
					if(username&&password&&saveAccount&&(btn.getText().equals("Connect")||btn.getText().equals("连接"))){
						Logger.h("username============================================================================"+username+"  pass:"+password+"   "+vpnConnect);
						if(vpnConnect) return;
						vpnConnect=true;
						EasyClickUtil.setIsAddingVpn(btn.getContext(),EasyClickUtil.NOT_ADD_VPN );
						EasyClickUtil.setIsChangingVpn(btn.getContext(),EasyClickUtil.NOT_CHANGE_VPN ); 
						Message msg = handler.obtainMessage();
						msg.obj = btn;
						msg.what = MethodInt.VIEW_VPN_ADD_ACCOUNT_OK;
						Logger.h("what-----"+msg.what);
						handler.sendMessageDelayed(msg, 1000);
//						handler.sendEmptyMessageDelayed(MethodInt.VPN_CONNECT, OPERA_TIME_1S);
					}
				}else {
					if((EasyClickUtil.getvpnConnectFlag(btn.getContext())==EasyClickUtil.NOT_CONNECT_VPN)){
						return;
					}
					if(btn.getText().equals("Connect")||btn.getText().equals("连接")){
						Logger.h("连接  vpnConnect::"+vpnConnect);
						if(vpnConnect) return;
						vpnConnect=true;
//						btn.performClick();
						try{
							EasyClickUtil.setvpnConnectFlag(btn.getContext(),EasyClickUtil.NOT_CONNECT_VPN);
							Message msg = handler.obtainMessage();
							msg.obj = btn;
							msg.what = MethodInt.VPN_CONNECT;
							handler.sendMessageDelayed(msg, 500);
						}catch(Exception e){
							Logger.h("EXE:conn:"+e);
						}
					}
				}
			}
			break;
		case MethodInt.VIEW_VPN_ADD:
			obj=param.thisObject;
			if(obj instanceof TextView){
				TextView tv=(TextView)obj;
				if(EasyClickUtil.getIsAddingVpn(tv.getContext())||EasyClickUtil.getIsChangingVpn(tv.getContext())){
					if(EasyClickUtil.getIsAddingVpn(tv.getContext())&&!clickAdd&&!TextUtils.isEmpty(tv.getText())&&("添加VPN".equals(tv.getText().toString())/*||"确定".equals(tv.getText().toString())*/)){
						Logger.h("ADD_VPN  "+tv.getText());
						clickAdd = true;
						tv.performClick();
					}
					if(!TextUtils.isEmpty(tv.getText())&&("确定".equals(tv.getText().toString()))&&tv.getId()==16908314){
						if(name&& server){
							Message msg = handler.obtainMessage();
							msg.obj = tv;
							msg.what = MethodInt.VIEW_VPN_ADD_OK;
							Logger.h("what-----"+msg.what);
							handler.sendMessageDelayed(msg, OPERA_TIME_1S);
//							EasyClickUtil.setIsAddingVpn(tv.getContext(),EasyClickUtil.NOT_ADD_VPN);
//							handler.sendEmptyMessageDelayed(MethodInt.VIEW_VPN_ADD_OK, OPERA_TIME_1S);
						}
					}
					if(!TextUtils.isEmpty(tv.getText())&&("名称".equals(tv.getText().toString()))){
						ViewGroup group = (ViewGroup) tv.getParent();
						if(group.getChildCount()==2){
							View view = group.getChildAt(1);
							if(view instanceof EditText){
								EditText editText = (EditText) view;
								editText.setText(EasyClickUtil.getVpnUserName(tv.getContext()));
								name = true;
							}
						}
					}
					if(!TextUtils.isEmpty(tv.getText())&&("服务器地址".equals(tv.getText().toString()))){
						ViewGroup group = (ViewGroup) tv.getParent();
						if(group.getChildCount()==2){
							View view = group.getChildAt(1);
							if(view instanceof EditText){
								EditText editText = (EditText) view;
								editText.setText(EasyClickUtil.getVpnServer(tv.getContext()));
								server = true;
							}
						}
					}
					if(!TextUtils.isEmpty(tv.getText())&&("用户名".equals(tv.getText().toString()))){
						ViewGroup group = (ViewGroup) tv.getParent();
						Logger.t("group.getChildCount()==="+group.getChildCount());
						if(group.getChildCount()==5){
							View view1 = group.getChildAt(1);
							if(view1 instanceof EditText){
								EditText editText = (EditText) view1;
								editText.setText(EasyClickUtil.getVpnUserName(view1.getContext()));
								username = true;
							}
							View view2 = group.getChildAt(3);
							if(view2 instanceof EditText){
								EditText editText = (EditText) view2;
								editText.setText(EasyClickUtil.getVpnPassword(view2.getContext()));
								password = true;
							}
						}
					}
				}
				if(EasyClickUtil.getIsDeletingVpn(tv.getContext())){
					if(("删除VPN".equals(tv.getText().toString()))&&tv.isClickable()&&!deleteVpn){
						deleteVpn = true;
						tv.performClick();
					}
					if((tv.getId()==269156396||tv.getId()==269156391)&&"删除VPN".equals(tv.getText().toString())&&!tv.isClickable()){
						deleteVpndialog = true;
					}
					if(tv.getId()==16908313&&"确定".equals(tv.getText().toString())&&deleteVpndialog){
						tv.performClick();
						EasyClickUtil.setIsAddingVpn(tv.getContext(),EasyClickUtil.NOT_ADD_VPN );
						EasyClickUtil.setIsChangingVpn(tv.getContext(),EasyClickUtil.NOT_CHANGE_VPN ); 
						EasyClickUtil.setIsDeletingVpn(tv.getContext(),EasyClickUtil.NOT_DELETE_VPN ); 
						Message msg = handler.obtainMessage();
						msg.obj = tv.getContext();
						msg.what = MethodInt.VPN_DELETE_OK;
						Logger.h("what-----"+msg.what);
						handler.sendMessageDelayed(msg, 500);
					}
				}
			}
			if(obj instanceof ImageView){
				ImageView imageView = (ImageView)obj;
				if((EasyClickUtil.getIsChangingVpn(imageView.getContext())||EasyClickUtil.getIsDeletingVpn(imageView.getContext()))){
					if((imageView.getWidth()>0&&imageView.getWidth()==imageView.getHeight()&&imageView.isClickable())&&!changeServerClick){
						changeServerClick = true;
						imageView.performClick();
					}
//					if((imageView.getId()==2131624349||imageView.getId()==2131624343||imageView.getId()==2131624344||imageView.getId()==2131624355||imageView.getId()==2131493254||imageView.getId()==2131624406)&&!changeServerClick){
//						changeServerClick = true;
//						imageView.performClick();
//					}
				}
			}
			if(obj instanceof CheckBox){
				CheckBox check = (CheckBox) obj;
				if("保存帐户信息".equals(check.getText().toString())){
					if(!check.isChecked())
						check.setChecked(true);
					saveAccount = true;
				}
			}
			break;
		default:
			break;
		}
	}
}
