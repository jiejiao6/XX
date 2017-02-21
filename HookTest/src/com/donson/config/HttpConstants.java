package com.donson.config;

public class HttpConstants {
	public static final String APK_DOWN_ACTION = "";
//	public static final String SCRIPT_DOWN_ACTION = "images/script/";
	
	public static final String REQUEST_KEY_PACKAGENAME = "PACKAGENAME";
	public static final String REQUEST_KEY_URL = "URL";
	public static final String REQUEST_KEY_PATH = "PATH";
	public static final String RESPONSE_OK = "OK";
	public static final String RESPONSE_ERR = "ERR";
	
	public static String DOWNLOAD_AKP_KEY = "apk_key";
	public static String DOWNLOAD_SCRIPT_KEY = "script_key";
	public static final String SCRIPT_SUFFIX = ".lua";
	
	
	//网络请求连接超时时间 15s
		public  static final int NET_CONNECT_TIME_OUT_TIME=20;
		//网络请求写入超时时间  15s
		public  static final int NET_WRITE_TIME_OUT_TIME=20;
		//网络请求读超时时间  15s
		public  static final int NET_READ_TIME_OUT_TIME=20;
	
		public static final String CODE = "CODE";
		public static final String DATA = "DATA";
		public static final String RESULT = "RESULT";
		public static final String MESSAGE = "MESSAGE";
}
