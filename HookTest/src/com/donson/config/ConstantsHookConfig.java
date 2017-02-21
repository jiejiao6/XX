package com.donson.config;

import java.io.File;

import com.param.config.ConstantsConfig;

public class ConstantsHookConfig {
	
	public final static String SCRIPT_FILE = ConstantsConfig.EXTRA_PATH+File.separator+ ConstantsConfig.DOWNLOAD_SCRIPT_PATH_NAME+File.separator;
	public static String APK_LOCAL_PATH = ConstantsConfig.EXTRA_PATH+"/download/apk";
	public static String APK_LOCAL_MY_PATH = ConstantsConfig.EXTRA_PATH+"/download/myApk";
	public static String APK_LOCAL_BACKUP_PATH = ConstantsConfig.EXTRA_PATH+"/download/backups";
	public static String SCRIPT_LOCAL_PATH = ConstantsConfig.EXTRA_PATH+"/download/script";
	//PATH==========
//	public static String EXTRA_PATH=ConstantsConfig.ROOTPATH+"/xx/file";
	public static String PATH_RECORD_APK_INSTALL = ConstantsConfig.EXTRA_PATH+"/installApk.txt";
	public static String DOWNLOAD_PATH_NAME="download";
	
	public static String PATH_RECORD_ERR_LOG = ConstantsConfig.EXTRA_PATH+"/LOG.txt";
	
	public static String PATH_RECORD_UPLOAD_ERR_LOG = ConstantsConfig.EXTRA_PATH+"/UPLOADLOG/";

	public static String DOWNLOAD_MY_AKP_PATH = ConstantsConfig.EXTRA_PATH+File.separator+"download/myApk/";
	//Intent Action
	public static String ACTION_LISTNER_APP = "com.listner.hook.app";
	public static final String ACTION_VPN_RECEVIER = "com.pptp.vpn";
	public static final String ACTION_EXE_AUTO_RUN = "com.auto.run";
//	public static final String ACTION_SHELL_EXEC_START = "NotifyShellExecStart";
//	public static final String ACTION_SHELL_EXEC_FINISH = "NotifyShellExecFinish";
	public static final String ACTION_START_RUN = "com.auto.run.start";
	public static final String ACTION_PARAM_OK2 = "com.param.getparam2";
	public static final String ACTION_SHELL_EXE_ERR = "com.script.err";
	public static final String ACTION_GET_LIUCUN_PARAM_ERR = "com.liucun.param.err";
	public static final String ACTION_UPDATE_XX = "com.update.xx";
	
	public final static String HDJ_GIT_BROADCAST="com.hdjad.openMarket";
	public final static String ACTION_LOGOUT="com.xxassistant.logout";
	
	public static final String EXTRA_CONTINUE_CONTROL = "extra_continue_control";
//	public static final String EXTRA_RECORD = "extra_record";

	
	//intent Extra
	public static String FLAG = "flag";
	public static String FLAG_INSTALL = "flag_install";
	public static String FLAG_NEW_LISTNER_APP = "flag_new_listner_app";
	public static String KEY_SELECT_APP = "key_select_app";
	public static String FLAG_CLEAR_LISTNER_APP = "flag_clear_listner_app";
	public static final String FLAG_CHECK_CONNECT= "flag_check_connect";
	
	
	//PackageName
	public static String XPOSED_01_PACKAGE_NAME = "de.robv.android.xposed.installer";
	public static String XPOSED_02_PACKAGE_NAME = "pro.burgerz.wsm.manager";
	public static String HDJ_HOOK_PACKAGE_NAME = "com.hdjad.github";
//	public static String _360ROOT = "com.qihoo.permmgr";
//	public static String MIUI_HOME_PACKAGE_NAME = "com.miui.home";
//	public static String MIUI_SYSTEM_PACKAGE_NAME = "com.android.systemui";
	public static String MY_PACKAGE_NAME = "com.donson.xxxiugaiqi";
	public static String MY_MAIN_ACTIVITY_NAME = "com.donson.myhook.MainActivity";
	public static String MY_VPN_SET_ACTIVITY_NAME = "com.donson.myhook.VpnSetActivity";

	public static String MY_AUTO_ACTIVITY_NAME = "com.donson.myhook.AutoActivity";

	public static String Root_PACKAGE_NAME = "com.qihoo.permmgr";
	public static String CONTROL_PACKAGE_NAME = "com.donson.xxcontrol";
	public static String CONTROL_ACTIVITY_NAME = "com.donson.xxcontrol.MainActivity";
	public static String CONTROL_SERVICE_NAME = "com.donson.xxcontrol.MyInternetService";
	public static final String  PAC_KEYBOARD = "com.android.adbkeyboard";
	public static final String PACKAGE_SECURITY = "com.lbe.security.miui";
	public static final String PACKAGE_SECURITY_CENTER = "com.miui.securitycenter";
	public static final String ACTIVITY_AUTO_START = "com.miui.permcenter.autostart.AutoStartManagementActivity";
	
	
	public final static String PAC_BAIDU="com.baidu.appsearch";
	public final static String PAC_360="com.qihoo.appstore";
	public final static String PAC_YYB = "com.tencent.android.qqdownloader";
	public final static String PAC_WDJ = "com.wandoujia.phoenix2";
	public final static String PAC_PP = "com.pp.assistant";
	public final static String PAC_AZ = "cn.goapk.market";
	public final static String PAC_MU = "com.xiaomi.market";
	public final static String PAC_MU_DOWN = "com.android.providers.downloads";
	public final static String PAC_GP = "com.android.vending";
	public final static String PAC_GP_PN_Login="com.google.android.gsf.login";
//	com.google.android.gms/com.google.android.gms.plus.oob.UpgradeAccountActivity
	public final static String VPN_GMS_PN="com.google.android.gms";

	public final static String PAC_INSTALL="com.android.packageinstaller";

	
	
	public static String SERVICE_NAME_MAIN= "com.donson.myhook.services.MyInternetService";
	public static String SERVICE_NAME_LISTEN= "com.donson.myhook.services.ListenService";
	public static String SERVICE_NAME_SCRIPT= "com.donson.myhook.services.ScriptService";

	public static String PACKAGE_VIP_SHOP = "com.achievo.vipshop";
	
//	public static String XPOSED_01_MODE_ACTIVITY_NAME = "de.robv.android.xposed.installer.WelcomeActivity";
	public static String XPOSED_01_MODE_ACTIVITY_NAME = "de.robv.android.xposed.installer.XposedInstallerActivity";

	public static String DOWNLOADER_PACKAGE_NAME = "com.android.providers.downloads";
	public static String BROWSER_PACKAGE_NAME = "com.android.browser";
	
	public final static String VPN_DIALOG_PN="com.android.vpndialogs";
	public final static String SETTINGS="com.android.settings";
	public final static String PACKAGE_NINEGAME = "cn.ninegame.gamemanager";
	
	public static String GSF_ID_CONTENT = "content://com.google.android.gsf.gservices";
	
	public static String APK_URL = "http://imtt.dd.qq.com/16891/E809C55086EBA2EA5452EA3E3A8D4B6C.apk?fsname=com.tencent.mm_6.3.25_861.apk&csr=4d5s";
	public static String INTENT_MY_APK_PATH = "my_akp_path";
	public static String MARKET_MU = "com.xiaomi.market";
	public static boolean IS_MOBILE = true;
	
	
	
	
	//RequestCode
	public final static int requestSD = 0;
	
	
}
