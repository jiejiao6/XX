package Xposed;

import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.donson.config.Logger;
import com.donson.utils.EasyClickUtil;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;

public class XposedInstallerHook extends XHook {
	private static XposedInstallerHook instance;

	private XposedInstallerHook() {
		// TODO Auto-generated constructor stub
	}

	public static XposedInstallerHook getInstance() {
		if (instance == null) {
			synchronized (XposedInstallerHook.class) {
				if (instance == null) {
					instance = new XposedInstallerHook();
				}
			}
		}
		return instance;
	}
	boolean clicked = false;
	int count  = 0;
	boolean spinnerClicked = false;
	boolean frameClicked = false;
	Spinner spinner = null;
	boolean rebootClicked =false;
	@Override
	protected void handleMethod(String packageName, ClassLoader classLoader)
			throws Exception {
		clicked= false;
		spinnerClicked = false;
		frameClicked = false;
		count = 0;
		spinner = null;
		rebootClicked =false;
		HookMethod(View.class, "dispatchTouchEvent", packageName,
				MethodInt.DIS, MotionEvent.class);
		HookMethod(View.class, "refreshDrawableState", packageName,
				MethodInt.XPOSED_MODE);// pptp
	}

	@Override
	protected void after(String packageName, MethodHookParam param, int type)
			throws Exception {
		Object obj;
		switch (type) {
		case MethodInt.XPOSED_MODE:
			obj = param.thisObject;
			if (obj instanceof TextView) {
				TextView tv = (TextView) obj;
				if(!TextUtils.isEmpty(tv.getText())&&("XX修改器".equals(tv.getText().toString()))||"XX助手".equals(tv.getText().toString())){
					ViewGroup group = (ViewGroup) tv.getParent().getParent().getParent();
//					Logger.h("child:"+group.getChildCount());
					if(group!=null&&group.getChildCount()>=3){
						View view = group.getChildAt(2);
						if(view instanceof CheckBox){
							CheckBox box = (CheckBox) view;
							if(!clicked){
								count++;
								if(count>=2){
									clicked = true;
									Toast.makeText(tv.getContext(), "click:"+count, Toast.LENGTH_SHORT).show();
								}
								boolean res = setSimulateClick(box, 0, 0);//box.performClick();
								Logger.h("==========clicked first"+box.isChecked()+"  "+res);
								if(clicked&&!spinnerClicked&&spinner!=null){
									boolean res1 = setSimulateClick(spinner,0,0);//spinner.performClick();
									Logger.h("=======box===Spinner clicked:"+clicked+"  "+res1);
								}
							}
							if(!box.isChecked()){
								boolean res = setSimulateClick(box, 0, 0);//box.performClick();
								Logger.h("==========clicked !box.isChecked()"+box.isChecked()+"  "+res);
							}
						}
					}
					tv.refreshDrawableState();
				}
				else if (!TextUtils.isEmpty(tv.getText())&&"重启".equals(tv.getText().toString())&&EasyClickUtil.getXposedHook(tv.getContext())&&EasyClickUtil.getXposedHook(tv.getContext())&&!rebootClicked) {
						boolean res = tv.performClick();
						if(res){
							rebootClicked = true;
						}
						Logger.h("========重启"+"  "+res+"  "+EasyClickUtil.getXposedHook(tv.getContext()));
				}
				else if (!TextUtils.isEmpty(tv.getText())&&tv.getId()==16908313&&"确定".equals(tv.getText().toString())) {
					boolean res = tv.performClick();
					Logger.h("========确定"+"  "+res);
				}
			}
			if(obj instanceof Spinner){
				if(clicked&&!spinnerClicked){
					spinnerClicked = true;
					Spinner spinner = (Spinner) obj;
					boolean res = setSimulateClick(spinner,0,0);//spinner.performClick();
					Logger.h("==========Spinner clicked:"+clicked+"  "+res);
				}else {
					spinner = (Spinner) obj;
				}
			}
			if(obj instanceof ListView){
				ListView listView = (ListView)obj;
				if(listView.getChildCount()==6){
					View view = listView.getChildAt(0);
					if(view instanceof TextView){
						TextView tv = (TextView) view;
						if(!frameClicked){
							frameClicked = true;
							boolean res = listView.performItemClick(listView.getChildAt(0), 0, listView.getItemIdAtPosition(0));
							Logger.h("lis::"+listView.getChildCount()+" "+tv.getText()+"  "+res);
						}
					}
				}
			}
			break;
		default:
			break;
		}

	}

}
