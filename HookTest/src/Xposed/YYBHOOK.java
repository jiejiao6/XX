package Xposed;

import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.donson.config.Logger;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;

public class YYBHOOK extends XHook {
	private static YYBHOOK instance;
	private YYBHOOK() {
		// TODO Auto-generated constructor stub
	}
	public static YYBHOOK getInstance() {
		if(instance == null){
			synchronized(YYBHOOK.class){
				if(instance == null){
					instance = new YYBHOOK();
				}
			}
		}
		
		return instance;
	}

	boolean downClick;
	Button downbutton = null;
	@Override
	protected void handleMethod(String packageName, ClassLoader classLoader)
			throws Exception {
		downClick = false;
		HookMethod(View.class, "dispatchTouchEvent", packageName,
				MethodInt.DIS, MotionEvent.class);// 后面参数是方法的参数类型  
		HookMethod(View.class, "refreshDrawableState", packageName,
				MethodInt.VIEW_TOSTRING_YYB_DOWN);
		HookMethod(TextView.class, "isSuggestionsEnabled", packageName,
				MethodInt.VIEW_TOSTRING_YYB_TV_PROGRESS);
		HookMethod(View.class, "setVisibility", packageName,
				MethodInt.VIEW_TOSTRING_YYB_TV_LOAD,int.class );
		HookMethod(View.class, "getMeasuredWidth", packageName,
				MethodInt.VIEW_TOSTRING_YYB_TV_LOAD);
	}

	@Override
	protected void after(String packageName, MethodHookParam param, int type)
			throws Exception {
		Object object = param.thisObject;
		switch (type) {
		case MethodInt.VIEW_TOSTRING_YYB_TV_LOAD:
			if(object instanceof View){
				View view = (View) object;
				if(view.getClass().toString().equals("class com.tencent.assistant.component.txscrollview.TXImageView")){
//					Logger.m("***************"+view.getId()+view.getClass()+"  "+(downbutton!=null)+"  "+downClick);
					if(view.getId()==5&&downbutton!=null&&!downClick){
						downClick =true;
						boolean res2 = downbutton.performClick();
						Logger.m("--------------------"+view.getId()+"-------------- "+res2);
					}
				}
			}
			break;
		case MethodInt.VIEW_TOSTRING_YYB_DOWN:
			if(object instanceof TextView){
				TextView downBtn = (TextView) object;
				if (!TextUtils.isEmpty(downBtn.getText()) && downBtn.getText().toString().contains("下载 (")) {
					Logger.m(downBtn.getText().toString()+"   "+downBtn.getVisibility()+"  "+downBtn.getWidth());
				}
				if(downBtn.getId()==2131690100&&downBtn.getWidth()>0){
					Logger.m("====2131690100====="+"   "+downBtn.getWidth()+"  "+downBtn.getVisibility());
//					if(!downClick){
//						downClick = true;
//						boolean res1 =downBtn.performClick();
					downbutton = (Button)downBtn;
//						Logger.m("====2131690100====="+"   "+downBtn.getWidth()+"  "+downBtn.getVisibility());
//					}
				}else if (!TextUtils.isEmpty(downBtn.getText())&&("土豪继续下载".equals(downBtn.getText().toString())||"暂不开启".equals(downBtn.getText().toString())||"下载 (5.3MB)".equals(downBtn.getText().toString()))) {
					boolean res = downBtn.performClick();
					Logger.m(downBtn.getText().toString()+"   "+res);
				}
				
			}
			break;
		
		default:
			break;
		}

	}

	
}
