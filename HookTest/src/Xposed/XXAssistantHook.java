package Xposed;

import android.content.ContentResolver;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;

public class XXAssistantHook extends XHook{
	private static XXAssistantHook instance;
	private XXAssistantHook() {
	}
	public static XXAssistantHook getInstance(){
		if(instance==null){
			synchronized (XXAssistantHook.class) {
				if(instance==null){
					instance = new XXAssistantHook();
				}
			}
		}
		return instance;
	}
	@Override
	protected void handleMethod(String packageName, ClassLoader classLoader)
			throws Exception {
		SystemVauleHook(MethodInt.GET_STRING, packageName, MethodInt.DONSON_XPOSED_FLAG, ContentResolver.class,String.class);
	}

	@Override
	protected void after(String packageName, MethodHookParam param, int type)
			throws Exception {
		switch (type) {
		case MethodInt.DONSON_XPOSED_FLAG:
			XposedParamHelpUtil.XpodedIsUsed(param);
			break;
		default:
			break;
		}
	}

}
