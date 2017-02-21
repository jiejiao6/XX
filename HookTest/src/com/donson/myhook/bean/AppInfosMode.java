package com.donson.myhook.bean;

import android.content.pm.PackageInfo;
import android.os.Parcel;
import android.os.Parcelable;

public class AppInfosMode implements Parcelable  {
	
	private String className;
	private PackageInfo packageInfo;
	private String pinYin;
	private String alpha;
	private boolean ischeck;
	
	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	public String getPinYin() {
		return pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}

	public PackageInfo getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}
	
	public boolean isIscheck() {
		return ischeck;
	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}



	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(pinYin);
		dest.writeString(alpha);
		dest.writeParcelable(packageInfo, flags);
	}
	
	  public static final Parcelable.Creator<AppInfosMode> CREATOR = new Creator<AppInfosMode>() {  
	        public AppInfosMode createFromParcel(Parcel source) {  
	        	AppInfosMode mBook = new AppInfosMode();  
	            mBook.pinYin = source.readString();  
	            mBook.alpha = source.readString();  
	            mBook.packageInfo = source.readParcelable(PackageInfo.class.getClassLoader());  
	            return mBook;  
	        }  
	        public AppInfosMode[] newArray(int size) {  
	            return new AppInfosMode[size];  
	        }  
	    };

	@Override
	public String toString() {
		return "AppInfosMode [className=" + className + ", packageInfo="
				+ packageInfo + ", pinYin=" + pinYin + ", alpha=" + alpha
				+ ", ischeck=" + ischeck + "]";
	}  
	
	    

}
