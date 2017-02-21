package com.donson.viewinterface;

import java.util.Set;

import com.donson.myhook.bean.OptAppMode;


public interface AutoOptViewInterface {
	public void toast(String tips);
	public void toastBig(String tips);
	public void toastBig(String tips,int color);
	public void setMyTitleMainThread(String title);
	public void setMyLeftTitleMainThread(String title);
	public void notifyAutoListMainThread(Set<OptAppMode> newList);
	
}
