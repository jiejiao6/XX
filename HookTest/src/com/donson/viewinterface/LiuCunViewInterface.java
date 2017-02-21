package com.donson.viewinterface;

import java.util.List;

public interface LiuCunViewInterface {
	public void toast(String tips);
	public void toastBig(String tips);
	public String getPercentSet();
	public void setEt(int flag,boolean isError,String str);
	public void notifySpinner(List<String> packageList);
	public void notifySpinnerDelete(String packageName);
	public void setLiuCunDataText(int all,int liucun);
	public void setTotalCountText(int total);
}
