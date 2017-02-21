package com.donson.viewinterface;

import java.util.List;

import android.graphics.drawable.Drawable;

import com.donson.myhook.bean.DataMode;

public interface MainViewInterface {
	public void toast(String tips);
	public void toastBig(String tips);
	public void toastBig(String tips,int color);
	public void setTips(String tips);
	public void notifyAdapter(List<DataMode> list);
	public void setMyTitleMainThread(String title);
	public void showNoUseTips(boolean isvisible,String text);
	public void easyClickable(boolean clickable);
	public void setListenPart(String packageName,Drawable icon,String appName);
	public void setListenBtnVisible(boolean isvisible);

}
