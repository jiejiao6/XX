package Xposed;

import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.donson.config.Logger;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;

public class MUSecurityCenterHook extends XHook {
	private static MUSecurityCenterHook instance;
	private MUSecurityCenterHook() {
	}
	public static MUSecurityCenterHook getInstance(){
		if(instance == null){
			synchronized (MUSecurityHook.class) {
				if(instance == null){
					instance = new MUSecurityCenterHook();
				}
			}
		}
		return instance;
	}
	@Override
	protected void handleMethod(String packageName, ClassLoader classLoader)
			throws Exception {
		HookMethod(View.class, "dispatchTouchEvent", packageName,
				MethodInt.DIS, MotionEvent.class);
		HookMethod(View.class, "refreshDrawableState", packageName,
				MethodInt.SECURITY_ROOT_NEXT);
	}

	@Override
	protected void after(String packageName, MethodHookParam param, int type)
			throws Exception {
		Object obj;
		switch (type) {
		case MethodInt.SECURITY_ROOT_NEXT:
			obj = param.thisObject;
			if (obj instanceof TextView) {
				TextView text = (TextView) obj;
				if (/*text.getId() == 2131427744 &&*/ !TextUtils.isEmpty(text.getText())&&(text.getText().toString().equals("下一步")||text.getText().toString().equals("允许"))) {
					boolean res = text.performClick();
					Logger.h("MIUI===security=====SYSTEM=============2131427744===="
							+ res + "    " + text.getText()+"-----result------ "+param.getResult()+"   "+param.method );				
					}
				else if (/*text.getId() == 2131427398 && */!TextUtils.isEmpty(text.getText())&&(text.getText().toString().equals("XXControl")||text.getText().toString().equals("XX助手")||text.getText().toString().equals("XX修改器")||text.getText().toString().equals("RE文件管理器"))) {
					
					ViewGroup group = (ViewGroup) text.getParent();
					Logger.h("MIUI===security================2131427398===group="+group.getClass() +group.getChildCount());	
					if(group.getChildCount()>=3){
						View view = group.getChildAt(2);
						if(view instanceof CheckBox/*&&view.getId()==2131427564*/){
							CheckBox box = (CheckBox) view;
							if(!box.isChecked()){
								boolean res = setSimulateClick(box, 0, 0);
								Logger.h("MIUI===security================2131427564===="
										+ res + "    "  );	
							}
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
