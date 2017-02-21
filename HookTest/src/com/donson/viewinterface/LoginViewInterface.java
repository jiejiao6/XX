package com.donson.viewinterface;

public interface LoginViewInterface {
	public void toast(String tips);
	public void toastBig(String tips);
	public String getAuthorization();
	public void setAuthorizationError(String err);
}
