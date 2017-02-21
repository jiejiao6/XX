package Xposed;


public class MethodInt {
	public static final int GET_LAST_KNOWN_LOCATION = 1;
	public static final int GET_LATITUDE = 2;
	public static final int GET_LONGITUDE = 3;
	public static final int GET_LATITUDE_CDMA = 4;
	public static final int GET_LONGITUDE_CDMA = 5;
	public static final int GET_CELL_LOCATION = 6;
	public static final int GET_CELL_ID = 7;
	public static final int GET_CELL_LAC = 8;
	public static final int GET_SYSTEM_ID_CDMA = 11;
	
	public static final int GET_CELL_MNC = 9;
	public static final int GET_CELL_MCC = 10;
	
	
	
	
	
	public static final int GET_NETWORK_OPERATOR = 12;
	public static final int GET_SYSTEM_PROPERTIES_1 = 13;
	public static final int GET_SYSTEM_PROPERTIES_2 = 14;
	
	
	public static final int GET_WIFI_STATE = 15;
	public static final int GET_IS_WIFI_ENABLE = 16;
	
	
	public static final int GET_TYPE = 22;
	public static final int GET_NET_TYPE_NAME = 23;
	public static final int GET_SUB_NET_TYPE_NAME = 24;
	public static final int GET_SUB_TYPE = 25;
	public static final int GET_BLUETOOTH_ADDRESS = 27;
	public static final int GET_BLUETOOTH_ADDRESSNAME = 28;
	public static final int GET_SIM_SERIAL_NUMBER = 29;
	public static final int GET_SIM_SERIAL_NUMBER_2 = 30;
	public static final int GET_SIM_SERIAL_NUMBER_3 = 31;
	
	public static final int INSTALLED_PACKAGES = 33;
	public static final int INSTALLED_PACKAGES_APPLICATIONS = 34;
	public static final int INSTALLED_PACKAGES_RUNNING_PROCESSES = 35;
	
	public static final int GET_LINE_NUMBER1_1 = 36;
	public static final int GET_LINE_NUMBER1_2 = 37;
	public static final int GET_LINE_NUMBER1_3 = 38;
	
	public static final int GET_PHONE_TYPE_1 = 39;
	public static final int GET_PHONE_TYPE_2 = 40;
	public static final int GET_PHONE_TYPE_3 = 41;
	public static final int GET_PHONE_TYPE_4 = 42;
	
	public static final int GET_SIM_STATE = 43;
	public static final int GET_SIM_STATE_2 = 44;
	
	public static final int GET_HAS_ICC_CARD = 45;
	public static final int GET_HAS_ICC_CARD_2 = 46;
	
	public static final int GET_SIM_OPERATOR = 47;
	public static final int GET_SIM_OPERATOR_2 = 48;
	public static final int GET_SUBSCRIBER_ID = 49;
	public static final int GET_SUBSCRIBER_ID_2 = 50;
	public static final int GET_SUBSCRIBER_ID_3 = 51;
	
	public static final int GET_NETWORK_OPERATOR_NAME = 52;
	public static final int GET_NETWORK_OPERATOR_NAME_2 = 53;
	public static final int GET_NETWORK_OPERATOR_NAME_3 = 54;
	
	public static final int GET_BSSID = 55;
	public static final int GET_SSID = 56;
	public static final int GET_LINK_SPEED = 57;
	public static final int GET_RSSI = 58;
	public static final int GET_MAC_ADDRESS = 59;
	public static final int GET_NET_WORK_INTERFACES_P2P0_MAC = 60;
	public static final int GET_HARDWARE_ADDRESS = 61;
	public static final int GET_SCAN_RESULTS = 62;
	
	public static final int GET_MOBILE_WIFI_CONNECTED = 63;
//	public static final int GET_WIFI_CONNECTED = 64;
	public static final int GET_NETWORK_CLASS = 65;
	
	public static final int GET_NET_TYPE_1 = 66;
	public static final int GET_NET_TYPE_2 = 67;
	public static final int GET_NET_TYPE_3 = 68;
	
	public static final int GET_BUILD_FINGERPRINT = 69;
	public static final int GET_BUILD_RADIO_VERSION = 70;
	
	public static final int GET_HEIGHT = 71;
	public static final int GET_WIDTH = 72;
	public static final int GET_DISPLAY_SIZE = 73;
	public static final int GET_METRICS  = 75;

	public static final int GET_DISPLAY_METRICS = 74;
	
	
	public static final int SYSTEM_VALUE_PUT_STRING = 76;
	public static final int SYSTEM_VALUE_GET_STRING = 77;
	public static final int SYSTEM_VALUE_PUT_LONG = 78;
	public static final int SYSTEM_VALUE_GET_LONG_2 = 79;
	public static final int SYSTEM_VALUE_GET_LONG_3 = 80;
	
	public static final int SYSTEM_VALUE_PUT_FLOAT = 81;
	public static final int SYSTEM_VALUE_GET_FLOAT_2 = 82;
	public static final int SYSTEM_VALUE_GET_FLOAT_3 = 83;
	public static final int SYSTEM_VALUE_PUT_INT = 84;
	public static final int SYSTEM_VALUE_GET_INT_2 = 85;
	public static final int SYSTEM_VALUE_GET_INT_3 = 86;
	
	
	
	public static final int GET_IMEI_1 = 110;//device_id
	public static final int GET_IMEI_2 = 111;
	public static final int GET_IMEI_3 = 112;
	public static final int GET_GSF_ID = 113;
	
	public static final int GET_USERAGENT_1 = 114;
	public static final int GET_USERAGENT_2 = 115;
	public static final int GET_USERAGENT_0 = 1150;
	public static final int GET_USERAGENT_3 = 116;
	public static final int GET_USERAGENT_4 = 117;
	public static final int GET_USERAGENT_5 = 118;
	
	public static final int GET_USERAGENT_6 = 119;
	
	public static final int GET_INNER_IP_STR = 200;
	public static final int GET_INNER_IP_INT = 201;
	public static final int GET_ANDROID_ID = 202;
	
	
	public static final int GET_SIM_OPERATOR_COUNTRY = 203;
	
	public static final int SET_TEXT = 1000;
	public static final int DONSON_XPOSED_FLAG = 1100;
	public static final int DIS = 2000;
	public static final int HOOKHDJ = 2100;
	
	
	public static final int GET_LINE1_NUMBER = 18;
	
	public final static int VPN_DISCONNECT=3000;
	public final static int ABSLISTVIEW_ISTEXTFILTERENABLED=3001;
	public final static int VIEW_TOSTRING_VPN_SETTING_ISSHOW_VPN=3002;
	public final static int VIEW_TOSTRING_VPN_SETTING_CONNECT=3003;
	public final static int SHOW_VPN_CONNECT_OPERA = 3004;
	public final static int SHOW_VPN_CONNECT = 3005;
	public final static int IS_SHOW_VPN = 3006;
	public final static int VPN_CONNECT = 3007;
	
	public final static int VIEW_VPN_ADD=3008;
	public final static int VIEW_VPN_ADD_OK = 3009;
	public final static int VIEW_VPN_ADD_ACCOUNT_OK = 3010;
	
	public final static int VPN_DELETE_OK = 3011;
	public final static int VPN_DISCONNECT_OK = 3012;
	
	public final static int SECURITY_ACCEPT = 3015;
	public final static int SECURITY_ROOT_NEXT = 3016;
	
	public final static int XPOSED_MODE = 3017;
	public static final int FILE_RECORD = 3018;
	
	public static final int FILE_CONSTRUCTION = 3019;
	public static final int FILE_RANDOMFILE_CONSTRUCTION = 3020;
	public static final int FILE_WRITER_CONSTRUCTION = 3021;
	public static final int FILE_STREAM_CONSTRUCTION = 3022;
	public static final int FILE_REANME_TO = 3023;
	public static final int FILE_MKDIR = 3024;
	public static final int FILE_MKDIRS = 3025;
//	public static final int ACCOUNT = 3022;

	public static String PUT_STRING = "putString";
	public static String GET_STRING = "getString";
	public static String GET_LONG = "getLong";
	public static String PUT_LONG = "putLong";
	public static String GET_FLOAT = "getFloat";
	public static String PUT_FLOAT = "putFloat";
	public static String GET_INT = "getInt";
	public static String PUT_INT = "putInt";
	
	
	public  static final int YYB_DOWN=4000;
	public  static final int VIEW_TOSTRING_YYB_DOWN=4001;
	public  static final int VIEW_TOSTRING_YYB_TV_PROGRESS=4002;
	public  static final int VIEW_TOSTRING_YYB_TV_LOAD=4003;
	public  static final int VIEW_TOSTRING_YYB_DOWN_DIS=4004;
	public  static final int YYB_PAUSE=4005;
	
	public static final int GET_CREATE_APP2 = 4006;
	
	public static final int INSTALL_TEXT_VIEW = 4010;
	
}
