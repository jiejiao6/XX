package Xposed;

import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.donson.config.Logger;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;

public class PackageInstallerHook extends XHook {
	public static PackageInstallerHook instance;
	private PackageInstallerHook() {
	}
	public static PackageInstallerHook getInstance(){
		if(instance == null){
			synchronized (PackageInstallerHook.class) {
				if(instance == null){
					instance = new PackageInstallerHook();
				}
			}
		}
		return instance;
	}

	
	@Override
	protected void handleMethod(String packageName, ClassLoader classLoader)
			throws Exception {
		installallow = false;
		installclick = false;
		HookMethod(View.class, "refreshDrawableState", packageName,
				MethodInt.INSTALL_TEXT_VIEW);
	}

	@Override
	protected void after(String packageName, MethodHookParam param, int type)
			throws Exception {
		Object object = param.thisObject;
		switch (type) {
		case MethodInt.INSTALL_TEXT_VIEW:
			if(object instanceof TextView){
				TextView textView = (TextView) object;
				if(!TextUtils.isEmpty(textView.getText())){
					if("仅允许一次".equals(textView.getText().toString())&&!installallow){
						installallow = true;
						boolean res = textView.performClick();
						Logger.m("======="+textView.getText()+"  ********"+res);
					}else if ("安装".equals(textView.getText().toString())&&!installclick) {
						installclick = true;
						boolean res = textView.performClick();
						Logger.m("======="+textView.getText()+"  ********"+res);
						Message msg = handler.obtainMessage();
						msg.what = MethodInt.INSTALL_TEXT_VIEW;
						handler.sendMessageDelayed(msg, 1000);
					}
					
				}
			}
			break;

		default:
			break;
		}

	}

}
