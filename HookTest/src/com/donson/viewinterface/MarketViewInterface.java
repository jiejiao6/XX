package com.donson.viewinterface;

import com.donson.myhook.bean.MARKET;


public interface MarketViewInterface {
	public void toast(String tips);
	public void checkRadioButton(MARKET market);
	public void marketTips(String tips);
	public void isMarketChecked(boolean isChecked);
	public void setDownApp(String downApp);
}
