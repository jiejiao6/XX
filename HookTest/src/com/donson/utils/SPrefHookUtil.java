package com.donson.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPrefHookUtil {
	private static SharedPreferences hook_pref;
	private static SharedPreferences setting_pref;
	private static SharedPreferences login_pref;
	private static SharedPreferences task_pref;
	private static SharedPreferences cur_task_pref;
	/******************************************************************************************************************/

	public static final String SP_HOOK = "sp_hook";
	public static final String KEY_HOOK = "key_hook";
	
	
	/******************************************************************************************************************/
	public static final String SP_SETTING = "sp_setting";
	public static String KEY_HHOOK_PACKAGE_NAME = "key_hhook_package_name"; //监听 安装包
	public static final String KEY_SETTING_CHANG_WHITE_LIST = "key_change_white_list"; //
	public static final String FLAG_SETTING_CLOSE_WHITE_LIST = "key_close_white_list";//
	
	public static final String KEY_SETTING_DOWN_PACKAGE_NAME = "key_donson_down_package_name";
	
	public static final String KEY_SETTING_MARKET = "key_setting_market";//

	public static final String KEY_SETTING_C_MARKET_DOWN_APK = "key_setting_market_down_apk";//

	
	public static final String KEY_SETTING_RUN_AUTO = "key_running_auto"; //是否自动执行中，及在AutoOptActivity中
	public static final boolean D_SETTING_RUN_AUTO = false; //

	public static final String KEY_SETTING_GLOBAL_CHANGEDE = "key_global_changed"; //全局修改是否打开
	public static final boolean D_SETTING_GLOBAL_CHANGEDE = true; //

	public static final String KEY_SETTING_DENSITY_CHEANGE = "key_density_change"; //修改密度
	public static final boolean D_SETTING_DENSITY_CHEANGE = false; //

	public static final String KEY_SETTING_OPEN_APK = "key_open_apk"; //一键操作后打开应用
	public static final boolean D_SETTING_OPEN_APK = false; //

	public static final String KEY_SETTING_UNINSTALL_APK = "key_uninstall_apk"; //卸载应用
	public static final boolean D_SETTING_UNINSTALL_APK = false; //
	
	public static final String KEY_SETTING_NET_DEBUG = "key_net_debug"; //联网调试
	public static final boolean D_SETTING_NET_DEBUG = true; //
	
	
	
	public static final String KEY_SETTING_TOTAL_TIMES_LIMIT = "key_total_times_limit"; //连续模式
	public static final boolean D_SETTING_TOTAL_TIMES_LIMIT = true;

	public static final String KEY_SETTING_VPN_CONN_TIME = "key_vpn_con_time"; 
	public static final int D_SETTING_VPN_CONN_TIME = 25; 
	
	public static final String KEY_SETTING_VPN_RECONN_TIME = "key_vpn_re_conn_time"; 
	public static final int D_SETTING_VPN_RECONN_TIME = 2;
	
	public static final String KEY_SETTING_IS_RUN_SCRIPT = "key_is_run_script"; 
	public static final boolean D_SETTING_IS_RUN_SCRIPT = true; 
	
	public static String KEY_SETTING_DOWN_URL = "key_down_url_ip";
	public static String D_SETTING_DOWN_URL = "123.59.41.163:80";
	
	public static String KEY_SETTING_CURRENT_IS_NEW = "key_is_cur_new";
	public static boolean D_SETTING_CURRENT_IS_NEW = true;
	
	
//	public static final String KEY_REBOOT = "key_reboot"; 
//	public static final boolean D_REBOOT = false; 
	/******************************************************************************************************************/
	public static final String SP_LOGIN = "sp_login";
	public static final String KEY_LOGIN_IMEI = "key_login_imei"; //
	public static final String KEY_LOGIN_DEVICE_ID = "key_login_device_id"; //
	public static final String KEY_LOGIN_DEVICE_CODE = "key_login_device_code"; //
	
	/******************************************************************************************************************/
	public static final String SP_TASK = "sp_task";
	public static final String KEY_TASK_APK_URL = "key_apk_url"; //下载应用的url
	
	public static final String KEY_TASK_SCRIPT_URL = "key_script_url"; //下载脚本的url

	public static final String KEY_TASK_RUN_TIMES = "key_run_times"; //任务总次数
	public static final int D_TASK_RUN_TIMES = 0; //
	
	public static final String KEY_TASK_CUR_RUN_TIME = "key_cur_run_time"; //任务当前执行次数
	public static final int D_TASK_CUR_RUN_TIME = 0; //

	public static final String KEY_TASK_APK_INSTALL = "key_apk_install"; //广播中判断 如果为true auto 自动执行，否则为正常安装的应用
	public static final boolean D_TASK_APK_INSTALL = false; //
	
	public static final String KEY_TASK_PLAN_ID= "key_plan_id";  //方案id  用于长传任务执行次数的区分
	public static final String D_TASK_PLAN_ID= "0";
	
	public static final String KEY_TASK_ORDER_ID= "key_order_id";  //订单ID 用于下载应用、脚本
	public static final String D_TASK_ORDER_ID= "0";
	
	public static final String KEY_TASK_TASK_ID= "key_task_id";  //订单ID 用于下载应用、脚本
	public static final int D_TASK_TASK_ID= 0;
	
	public static final String KEY_TASK_SCRIPT_TIME= "key_script_time";  //脚本时间
	public static final int D_TASK_SCRIPT_TIME= 0;  //默认 0s
	
	
	public static final String KEY_TASK_PLAN_TYPE= "key_plan_type";  
	/**
	 * 1新增、2留存  ；默认1（新增）
	 */
	public static final int D_TASK_PLAN_TYPE= 1; 
	
	public static final String KEY_TASK_VPN_AUTO_CONN = "key_vpn_auto_conn"; 
	public static final boolean D_TASK_VPN_AUTO_CONN = true; 
	
	public static final String KEY_TASK_APK_VER= "key_apk_md5";  
	
	public static final String KEY_TASK_SCR_VER= "key_script_md5";  //
	//脚本是否执行中
//	public static final String KEY_SETTING_SCRIPT_RUNNING = "key_script_running"; //脚本是否正在执行
//	public static final boolean D_SETTING_SCRIPT_RUNNING = false; //
	
	public static final String KEY_TASK_CONTINUOUS = "key_continuous"; //连续模式
	public static final boolean D_TASK_CONTINUOUS = true;
	
	public final static String KEY_TASK_VPN_ID = "key_vpn_id";
	public final static int D_TASK_VPN_ID = 0;
	
	public static final String KEY_TASK_SCRIPT_NAME = "key_task_script_name";
	public static final String D_TASK_SCRIPT_NAME = "0";
	/****************************************************************************************************************/
	public static final String SP_CUR_TASK = "sp_cur_task";
	public static String KEY_CUR_PACKAGE_NAME = "key_cur_package_name"; //监听 安装包
	
	
	public static final String KEY_CUR_TASK_PLAN_ID= "key_plan_id";  //方案id  用于长传任务执行次数的区分
	public static final String D_CUR_TASK_PLAN_ID= "0";
	
	public static final String KEY_CUR_TASK_ORDER_ID= "key_order_id";  //订单ID 用于下载应用、脚本
	public static final String D_CUR_TASK_ORDER_ID= "0";
	
	public static final String KEY_CUR_TASK_TASK_ID= "key_task_id";  //上传参数时用
	public static final int D_CUR_TASK_TASK_ID= 0;
	
	public static final String KEY_CUR_TASK_SCRIPT_TIME= "key_script_time";  //脚本时间
	public static final int D_CUR_TASK_SCRIPT_TIME= 0;  //默认 0s
	
	
	public static final String KEY_CUR_TASK_PLAN_TYPE= "key_plan_type";  
	/**
	 * 1新增、2留存  ；默认1（新增）
	 */
	public static final int D_CUR_TASK_PLAN_TYPE= 1; 
	public static final String KEY_CUR_TASK_SCRIPT_NAME = "key_task_script_name";
	public static final String D_CUR_TASK_SCRIPT_NAME = "0";
	
	public static final String KEY_CUR_TASK_CUR_RUN_TIME = "key_cur_run_time"; //任务当前执行次数
	public static final int D_CUR_TASK_CUR_RUN_TIME = 0; //

	/****************************************************************************************************************/

	
	private static SharedPreferences getCurTaskInstance(Context paramContext) {
		if (cur_task_pref == null) {
			cur_task_pref = paramContext.getSharedPreferences(SP_CUR_TASK, 1);
		}
		return cur_task_pref;
	}

	public static boolean putCurTaskStr(Context context, String key,String value) {
		return getCurTaskInstance(context).edit()
				.putString(key, value).commit();
	}
	
	public static String getCurTaskStr(Context context, String key) {
		return getCurTaskInstance(context).getString(key, "");
	}
	
	public static String getCurTaskStr(Context context, String key,String value) {
		return getCurTaskInstance(context).getString(key, value);
	}
	
	public static int getCurTaskInt(Context context, String key, int defaultB) {
		SharedPreferences pref = getCurTaskInstance(context);
		int result = pref.getInt(key, defaultB);
		return result;
	}

	public static boolean putCurTaskInt(Context context, String key, int value) {
		SharedPreferences pref = getCurTaskInstance(context);
		boolean commit = pref.edit().putInt(key, value).commit();
		return commit;
	}
	/************************************************************************/
	private static SharedPreferences getHookInstance(Context paramContext) {
		if (hook_pref == null) {
			hook_pref = paramContext.getSharedPreferences(SP_HOOK, 1);
		}
		return hook_pref;
	}

	public static boolean putHookStr(Context paramContext, String paramString1,
			String paramString2) {
		return getHookInstance(paramContext).edit()
				.putString(paramString2, paramString1).commit();
	}
	
	public static String getHookStr(Context paramContext, String key) {
		return getHookInstance(paramContext).getString(key, "");
	}

	private static SharedPreferences getSettingInstance(Context context) {
		if (setting_pref == null)
			setting_pref = context.getSharedPreferences(SP_SETTING,
					Context.MODE_WORLD_READABLE);
		return setting_pref;
	}

	public static String getSettingStr(Context context, String key) {
		SharedPreferences pref = getSettingInstance(context);
		String str = pref.getString(key, "");
		return str;
	}
	public static String getSettingStr(Context context, String key,String defaultValue) {
		SharedPreferences pref = getSettingInstance(context);
		String str = pref.getString(key, defaultValue);
		return str;
	}

	public static boolean putSettingStr(Context context, String key,
			String value) {
		SharedPreferences pref = getSettingInstance(context);
		boolean commit = pref.edit().putString(key, value).commit();
		return commit;
	}

	public static boolean getSettingBoolean(Context context, String key,
			boolean defaultB) {
		SharedPreferences pref = getSettingInstance(context);
		boolean bool = pref.getBoolean(key, defaultB);
		return bool;
	}

	public static boolean putSettingBoolean(Context context, String key,
			boolean value) {
		SharedPreferences pref = getSettingInstance(context);
		boolean commit = pref.edit().putBoolean(key, value).commit();
		return commit;
	}

	public static int getSettingInt(Context context, String key, int defaultB) {
		SharedPreferences pref = getSettingInstance(context);
		int result = pref.getInt(key, defaultB);
		return result;
	}

	public static boolean putSettingInt(Context context, String key, int value) {
		SharedPreferences pref = getSettingInstance(context);
		boolean commit = pref.edit().putInt(key, value).commit();
		return commit;
	}

	private static SharedPreferences getLoginInstance(Context paramContext) {
		if (login_pref == null) {
			login_pref = paramContext.getSharedPreferences(SP_LOGIN, 1);
		}
		return login_pref;
	}

	public static String getLoginStr(Context context, String key) {
		SharedPreferences pref = getLoginInstance(context);
		String str = pref.getString(key, "");
		return str;
	}

	public static boolean putLoginStr(Context context, String key, String value) {
		SharedPreferences pref = getLoginInstance(context);
		boolean commit = pref.edit().putString(key, value).commit();
		return commit;
	}
	
	
	private static SharedPreferences getTaskInstance(Context paramContext) {
		if (task_pref == null) {
			task_pref = paramContext.getSharedPreferences(SP_TASK, 1);
		}
		return task_pref;
	}
	public static boolean putTaskStr(Context context,String key,String value){
		SharedPreferences pref = getTaskInstance(context);
		boolean commit = pref.edit().putString(key, value).commit();
		return commit;
	}
	public static String getTaskStr(Context context,String key){
		SharedPreferences pref = getTaskInstance(context);
		String str = pref.getString(key, "");
		return str;
	}
	public static String getTaskStr(Context context,String key,String defaultValue){
		SharedPreferences pref = getTaskInstance(context);
		String str = pref.getString(key, defaultValue);
		return str;
	}
	public static boolean putTaskInt(Context context,String key,int value){
		SharedPreferences pref = getTaskInstance(context);
		boolean commit = pref.edit().putInt(key, value).commit();
		return commit;
	}
	public static int getTaskInt(Context context,String key,int defaultValue){
		SharedPreferences pref = getTaskInstance(context);
		int res = pref.getInt(key, defaultValue);
		return res;
	}

	public static boolean putTaskBoolean(Context context,
			String key, boolean value) {
		SharedPreferences pref = getTaskInstance(context);
		boolean commit = pref.edit().putBoolean(key, value).commit();
		return commit;
	}
	public static boolean getTaskBoolean(Context context,String key,boolean defaultValue){
		SharedPreferences pref = getTaskInstance(context);
		boolean res = pref.getBoolean(key, defaultValue);
		return res;
	}

}
