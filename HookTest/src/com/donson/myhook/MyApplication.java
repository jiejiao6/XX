package com.donson.myhook;


import android.app.Application;

import com.param.utils.CrashHandler;

public class MyApplication extends Application {
	boolean isLogined = false;
	boolean isRunning = false;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		isRunning = false;
		CrashHandler crashHandler = CrashHandler.getInstance();  
        crashHandler.init(getApplicationContext()); 
	}
	
	public boolean getIsLogined(){
		return isLogined;
	}
	public void setLogined(boolean isLogined) {
		this.isLogined = isLogined;
	}

}
