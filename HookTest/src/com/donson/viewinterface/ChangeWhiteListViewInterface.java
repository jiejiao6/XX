package com.donson.viewinterface;

import java.util.List;

import com.donson.myhook.bean.AppInfosMode;

public interface ChangeWhiteListViewInterface {
	public void updateMAppList(int flag,AppInfosMode appInfosMode,int location);
	public List<AppInfosMode> getAppInfoList(int flag);
	public void notifyAllAppList();
	public void notifyWhiteList();
}
