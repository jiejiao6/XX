package com.donson.viewinterface;

import java.util.List;

import android.content.res.ColorStateList;

public interface ScriptSetViewInterface {
	public void toast(String tips);
	public void toastBig(String tips);
	public void setTvScriptId(String scriptId);
	public String getScriptId();
	public void setIsScriptRun(boolean is);
	public boolean getIsScriptRun();
	public void setIsScriptExist(String exist);
	public void setIsScriptExistColor(ColorStateList colors);
	public void notifyAllScript(List<String> list);

}
