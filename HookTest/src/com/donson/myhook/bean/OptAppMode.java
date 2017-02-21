package com.donson.myhook.bean;

import com.donson.config.Logger;

import android.content.pm.PackageInfo;

public class OptAppMode {
	// private String className;
	public static final int FLAG_UNINSTALL = 0;
	public static final int FLAG_INSTALL =1;
	public static final int FLAG_CLOSE = 2;
	public static final int FLAG_CLEAR = 3;
	public static final int FLAG_DELETE_FILE = 4;
	public static final int FLAG_SYS_OPT = 5;
	public static final int FLAG_VPN = 6;
	public static final int FLAG_PARAM = 7;
	private PackageInfo packageInfo;
	private int flag;
	private int fileErrSize;
	private int folderErrSize;
	
	private int sysCount;
	private int sysFailCount;
	
	private long dataSize;
	

	public long getDataSize() {
		return dataSize;
	}

	public void setDataSize(long dataSize) {
		this.dataSize = dataSize;
	}

	public int getFileErrSize() {
		return fileErrSize;
	}

	public void setFileErrSize(int fileErrSize) {
		this.fileErrSize = fileErrSize;
	}

	public int getFolderErrSize() {
		return folderErrSize;
	}

	public void setFolderErrSize(int folderErrSize) {
		this.folderErrSize = folderErrSize;
	}

	public int getSysCount() {
		return sysCount;
	}

	public void setSysCount(int sysCount) {
		this.sysCount = sysCount;
	}

	public int getSysFailCount() {
		return sysFailCount;
	}

	public void setSysFailCount(int sysFailCount) {
		this.sysFailCount = sysFailCount;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public PackageInfo getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}
	
	@Override
	public boolean equals(Object o) {
		return this.packageInfo.packageName.equals(((OptAppMode)o).packageInfo.packageName);
	}
	@Override
	public int hashCode() {
		return this.packageInfo.packageName.hashCode();
	}

}
