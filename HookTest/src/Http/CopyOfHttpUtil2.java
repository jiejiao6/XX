package Http;

import imei.util.IMEIGET;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import Http.DownloadAsyncTask.DownCallback;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.HttpConstants;
import com.donson.config.Logger;
import com.donson.myhook.bean.JsonTask;
import com.donson.myhook.bean.NetTask;
import com.donson.myhook.services.ScriptService;
import com.donson.utils.ActivityUtil;
import com.donson.utils.AppInfosUtil;
import com.donson.utils.CharacterParser;
import com.donson.utils.CheckUpdateUtil;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.MyfileUtil;
import com.donson.utils.OpenActivityUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.SendBroadCastUtil;
import com.google.android.gms.internal.el;
import com.google.gson.JsonObject;
import com.param.bean.LiuCunMode;
import com.param.config.ConstantsConfig;
import com.param.config.SPrefUtil;
import com.param.dao.DbDao;
import com.param.netInterface.HttpUtil.ResponseListener;
import com.param.utils.CommonOperationUtil;
import com.param.utils.FileUtil;

public class CopyOfHttpUtil2 {
	public static final String BASE_URL = "http://123.59.41.163";
	
	ResponseListener listener = null;
	Context context;
	DbDao dao;
	TelephonyManager tm;
	IMEIGET imeiget;
	WifiManager wifiManager ;
	public CopyOfHttpUtil2(Context context) {
		this.context = context;
		dao = DbDao.getInstance(context);
		imeiget = new IMEIGET(context);
		 tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		 wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}
	public void setListener(ResponseListener listener){
		this.listener = listener;
	}
	public String getDownUrl (){
		String ip = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_SETTING_DOWN_URL,SPrefHookUtil.D_SETTING_DOWN_URL);
	return "http://".concat(ip);
	}
	public  String getUrl(){
//		String url = BASE_DNS_URL;
//		String url = TEST_BASE_DNS_URL;
		String url = BASE_URL;
		return url;
	}
	
	/***************************************************************************************************/
//	"vpnAccount":"186712","vpnPassword":"1111","vpnUrl":"z.vpsnb.com"
	public static final String VPNACCOUNT = "vpnAccount";
	public static final String VPNPASSWORD = "vpnPassword";
	public static final String VPNURL = "vpnUrl";
	
	public void getVpnAccount(String deviceId,String deviceCode,String imei,final int vpnId){
		final RetrofitService2 service= RetrofitServiceGeter2.getRetrofitService(getUrl(), true);
		service.getVpnAccount(deviceId, deviceCode, imei, vpnId).enqueue(new Callback<ResponseBody>() {
			
			@Override
			public void onResponse(Call<ResponseBody> arg0, Response<ResponseBody> arg1) {
				try {
					String json = arg1.body().string();
					Logger.i("=VPN==="+json);
					JSONObject jsonObject = new JSONObject(json);
					if(jsonObject.optInt(HttpConstants.CODE)==0){
						JSONObject data = jsonObject.optJSONObject(HttpConstants.DATA);
						JSONObject result = data.optJSONObject(HttpConstants.RESULT);
						EasyClickUtil.setVpnServer(context, result.optString(VPNURL));
						EasyClickUtil.setVpnUserName(context, result.optString(VPNACCOUNT));
						EasyClickUtil.setVpnPassword(context, result.optString(VPNPASSWORD));
						if(SPrefHookUtil.getTaskInt(context, SPrefHookUtil.KEY_TASK_VPN_ID, SPrefHookUtil.D_TASK_VPN_ID)==0){
							OpenActivityUtil.openVpnAdd(context);
						}else {
							OpenActivityUtil.openVpnChange(context);
						}
						SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_VPN_ID, vpnId);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(Call<ResponseBody> arg0, Throwable arg1) {
			}
		});
	}
	String ssid = null;
	public String getSSid(){
		if(TextUtils.isEmpty(ssid)){
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			ssid =  wifiInfo.getSSID();
		}
		return ssid;
	}
	/**
	 * 获取任务信息
	 */
	public void getTask() {
		final RetrofitService2 service= RetrofitServiceGeter2.getRetrofitService(getUrl(), true);
		final String deviceId = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_ID);
		final String deviceCode = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_CODE);
		String planId = SPrefHookUtil.getCurTaskStr(context, SPrefHookUtil.KEY_CUR_TASK_PLAN_ID,SPrefHookUtil.getTaskStr(context, SPrefHookUtil.KEY_TASK_PLAN_ID));
		int execNum = SPrefHookUtil.getCurTaskInt(context, SPrefHookUtil.KEY_CUR_TASK_CUR_RUN_TIME, SPrefHookUtil.getTaskInt(context, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME));
//		final String imei = tm.getDeviceId();
		final String imei = imeiget.getImei();
		String version = AppInfosUtil.getVersion(context);
		String ssid = getSSid();
		Logger.i("getTask=="+getUrl()+"?mod=api&action=getDeviceInfo&deviceId="+deviceId+"&deviceCode="+deviceCode+"&imei="+imei+"&planId="+String.valueOf(planId)+"&execNum="+execNum+"&ssid="+ssid);
		service.getTask(deviceId, deviceCode, imei, String.valueOf(planId), String.valueOf(execNum),version,ssid).enqueue(new Callback<JsonTask>() {
			
			@Override
			public void onResponse(Call<JsonTask> arg0, Response<JsonTask> arg1) {
				EventBus.getDefault().post(new CheckTaskEvent(true));
				JsonTask jsonTask = arg1.body();
				if(arg1.code()==200&&jsonTask!=null&&jsonTask.getCODE()==0){
					Logger.i("result:"+arg1.body().toString()+"======:"+jsonTask.getDATA());
					if(jsonTask.getDATA()!=null){
						List<NetTask> netTasks = jsonTask.getDATA().getRESULT();
						if(netTasks!=null&&netTasks.size()>=1){
							NetTask netTask = netTasks.get(0);
							if(!TextUtils.isEmpty(netTask.getPackageName())){
								handleTask(netTask);
								if(isNotRunning()&&netTask.getUpdateFlag()==1){
									context.stopService(new Intent(context, ScriptService.class));
									CheckUpdateUtil checkUpdateUtil = new CheckUpdateUtil(context);
									checkUpdateUtil.checkUpdate();
								}else if (isNotRunning()&&netTask.getVpnId()!=SPrefHookUtil.getTaskInt(context, SPrefHookUtil.KEY_TASK_VPN_ID, SPrefHookUtil.D_TASK_VPN_ID)) {
									if(netTask.getVpnId()!=0){
										Logger.i("getVpnAccount=="+getUrl()+"?mod=api&action=getVpnAccount&deviceId="+deviceId+"&deviceCode="+deviceCode+"&imei="+imei+"&vpnId="+netTask.getVpnId());
										getVpnAccount(deviceId, deviceCode, imei, netTask.getVpnId());
									}else if(netTask.getVpnId()==0){
										OpenActivityUtil.openVpnDelete(context);
										SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_VPN_ID, 0);
									}
								}
							}else {
								removeTask(netTask);
								Logger.i("EasyClickUtil.getScriptIsRunning(context)::"+EasyClickUtil.getScriptIsRunning(context));
								if(isNotRunning()&&netTask.getVpnId()!=SPrefHookUtil.getTaskInt(context, SPrefHookUtil.KEY_TASK_VPN_ID, SPrefHookUtil.D_TASK_VPN_ID)){
									if(netTask.getVpnId()!=0){
										Logger.i("getVpnAccount=="+getUrl()+"?mod=api&action=getVpnAccount&deviceId="+deviceId+"&deviceCode="+deviceCode+"&imei="+imei+"&vpnId="+netTask.getVpnId());
										getVpnAccount(deviceId, deviceCode, imei, netTask.getVpnId());
									}else if(netTask.getVpnId()==0){
										OpenActivityUtil.openVpnDelete(context);
										SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_VPN_ID, 0);
									}
								}
								else if(isNotRunning()&&netTask.getUpdateFlag()==1){
									CheckUpdateUtil checkUpdateUtil = new CheckUpdateUtil(context);
									checkUpdateUtil.checkUpdate();
								}
							}
						}else if (jsonTask.getCODE()==0&&netTasks.size()==0) {
							removeTask(null);
						}
					}
				}
				else {
					//失败
				}
			}
			@Override
			public void onFailure(Call<JsonTask> arg0, Throwable arg1) {
				EventBus.getDefault().post(new CheckTaskEvent(false));
				//账户异常
				
				Logger.i("result: Throwable:"+arg1.toString());
			}
		});
	}
	protected boolean isNotRunning(){
		return (!SPrefHookUtil.getSettingBoolean(context,SPrefHookUtil.KEY_SETTING_RUN_AUTO, false)&&!EasyClickUtil.getScriptIsRunning(context)&&!EasyClickUtil.getIsTaskRunning(context));
	}
	protected void removeTask(NetTask netTask) {
		EasyClickUtil.setIsHasTask(context, EasyClickUtil.NOT_HAS_TASK);
		String packageName = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
		if(!TextUtils.isEmpty(packageName)){
			boolean pac = SPrefHookUtil.putSettingStr(context, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME, "");
			if(pac){
				SendBroadCastUtil.listenApp(/*ConstantsHookConfig.FLAG_NEW_LISTNER_APP,*/ context,false);
			}	
		}
		if(!SPrefHookUtil.D_TASK_PLAN_ID.equals(SPrefHookUtil.getTaskStr(context, SPrefHookUtil.KEY_TASK_PLAN_ID))){
			SPrefHookUtil.putTaskStr(context, SPrefHookUtil.KEY_TASK_PLAN_ID, SPrefHookUtil.D_TASK_PLAN_ID);
		}
		if(isRemoveInt(SPrefHookUtil.getTaskInt(context, SPrefHookUtil.KEY_TASK_SCRIPT_TIME, SPrefHookUtil.D_TASK_SCRIPT_TIME), SPrefHookUtil.D_TASK_SCRIPT_TIME)){
			SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_SCRIPT_TIME, SPrefHookUtil.D_TASK_SCRIPT_TIME);
		}
		if(isRemoveInt(SPrefHookUtil.getTaskInt(context, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME), SPrefHookUtil.D_TASK_CUR_RUN_TIME)){
			SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME);
		}
		SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_RUN_TIMES,SPrefHookUtil.D_TASK_RUN_TIMES);
		SPrefHookUtil.putTaskStr(context, SPrefHookUtil.KEY_TASK_ORDER_ID, SPrefHookUtil.D_TASK_ORDER_ID);
//		SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_TASK_ID, SPrefHookUtil.D_TASK_TASK_ID);
		SPrefUtil.putString(context, SPrefUtil.C_CHANNEL, SPrefUtil.D_CHANNEL);
		SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_PLAN_TYPE, SPrefHookUtil.D_TASK_PLAN_TYPE);
		SPrefHookUtil.putTaskStr(context, SPrefHookUtil.KEY_TASK_SCRIPT_NAME, SPrefHookUtil.D_TASK_SCRIPT_NAME);
		SPrefHookUtil.putSettingBoolean(context, SPrefHookUtil.KEY_SETTING_TOTAL_TIMES_LIMIT, true);
		String downip = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_SETTING_DOWN_URL);
		if(netTask!=null&&(isReSetString(netTask.getDownIp(), downip))){
			SPrefHookUtil.putSettingStr(context, SPrefHookUtil.KEY_SETTING_DOWN_URL, netTask.getDownIp());
		}
	}
	public boolean isRemoveInt(int oldInt,int defInt){
		return oldInt==defInt?false:true;
	}
	protected void handleTask(NetTask netTask) {
		EasyClickUtil.setIsHasTask(context, EasyClickUtil.HAS_TASK);
		String packageName = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
		String planId = SPrefHookUtil.getTaskStr(context, SPrefHookUtil.KEY_TASK_PLAN_ID,SPrefHookUtil.D_TASK_PLAN_ID);//上传次数用
		int scriptTime = SPrefHookUtil.getTaskInt(context, SPrefHookUtil.KEY_TASK_SCRIPT_TIME,SPrefHookUtil.D_TASK_SCRIPT_TIME);//脚本执行时间
//		int execNum = SPrefHookUtil.getSettingInt(context, SPrefHookUtil.KEY_SETTING_CUR_RUN_TIME,SPrefHookUtil.D_SETTING_CUR_RUN_TIME);//当前执行次数
		int execTotal = SPrefHookUtil.getTaskInt(context, SPrefHookUtil.KEY_TASK_RUN_TIMES,SPrefHookUtil.D_TASK_RUN_TIMES);//任务总次数
		String orderId = SPrefHookUtil.getTaskStr(context, SPrefHookUtil.KEY_TASK_ORDER_ID,SPrefHookUtil.D_TASK_ORDER_ID);
		String apkVer = SPrefHookUtil.getTaskStr(context, SPrefHookUtil.KEY_TASK_APK_VER);
		String scriptVer = SPrefHookUtil.getTaskStr(context, SPrefHookUtil.KEY_TASK_SCR_VER);
		String apkUrl = SPrefHookUtil.getTaskStr(context, SPrefHookUtil.KEY_TASK_APK_URL);
		String scriptUrl = SPrefHookUtil.getTaskStr(context, SPrefHookUtil.KEY_TASK_SCRIPT_URL);
		String channel = SPrefUtil.getString(context, SPrefUtil.C_CHANNEL, SPrefUtil.D_CHANNEL);
		String downip = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_SETTING_DOWN_URL);
		int plantype = SPrefHookUtil.getTaskInt(context, SPrefHookUtil.KEY_TASK_PLAN_TYPE, SPrefHookUtil.D_TASK_PLAN_TYPE);
		int taskId =  SPrefHookUtil.getTaskInt(context, SPrefHookUtil.KEY_TASK_TASK_ID, SPrefHookUtil.D_TASK_TASK_ID);
		int wifiRate = SPrefUtil.getInt(context, SPrefUtil.C_WIFI_PERCENT, SPrefUtil.D_WIFI_PERCENT);
		int simRate = SPrefUtil.getInt(context, SPrefUtil.C_SIM_PERCENT, SPrefUtil.D_SIM_PERCENT);
		String scriptName = SPrefHookUtil.getTaskStr(context, SPrefHookUtil.KEY_TASK_SCRIPT_NAME,SPrefHookUtil.D_TASK_SCRIPT_NAME);
		if(isReSetString(netTask.getFileLuaName(), scriptName)){
			SPrefHookUtil.putTaskStr(context, SPrefHookUtil.KEY_TASK_SCRIPT_NAME, netTask.getFileLuaName());
		}
		if(isReSetString(netTask.getDownIp(), downip)){
			SPrefHookUtil.putSettingStr(context, SPrefHookUtil.KEY_SETTING_DOWN_URL, netTask.getDownIp());
		}
		if(isReSetInt(netTask.getPlanType(), plantype)){
			SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_PLAN_TYPE, netTask.getPlanType());
		}
		if(netTask.getPlanType()==SPrefHookUtil.D_TASK_PLAN_TYPE){//新增
			SPrefHookUtil.putSettingBoolean(context, SPrefHookUtil.KEY_SETTING_TOTAL_TIMES_LIMIT, true);
			reSetVpn(netTask.getIsUseVpn());
		}else {//留存
			SPrefHookUtil.putSettingBoolean(context, SPrefHookUtil.KEY_SETTING_TOTAL_TIMES_LIMIT, false);
			reSetVpn(netTask.getIsRetainVPN());
		}
		if(isReSetInt(netTask.getWifiRate(), wifiRate)){
			SPrefUtil.putInt(context, SPrefUtil.C_WIFI_PERCENT, netTask.getWifiRate());
		}
		if(isRemoveInt(netTask.getSimRate(), simRate)){
			SPrefUtil.putInt(context, SPrefUtil.C_SIM_PERCENT, netTask.getSimRate());
		}
		LiuCunMode liuCunMode = null;
		if(!TextUtils.isEmpty(netTask.getPackageName())){
			liuCunMode = dao.getLiuCunSetByPackageName(netTask.getPackageName());
		}else {
			liuCunMode = dao.getLiuCunSetByPackageName(packageName);
		}
		String oldRetained = null;
		if(liuCunMode!=null){
			oldRetained = liuCunMode.getPercentStr();
		}
		String newChannel = netTask.getChannel();
		String pinChannel = CharacterParser.getInstance().getSelling(newChannel);
		if(isReSetString(pinChannel, channel)){
			SPrefUtil.putString(context, SPrefUtil.C_CHANNEL, pinChannel);
		}
		if(isReSetString(netTask.getPackageName(),packageName)){
			boolean pac = SPrefHookUtil.putSettingStr(context, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME, netTask.getPackageName());
			if(pac){
//				context.stopService(new Intent(context, ScriptService.class));
				SendBroadCastUtil.listenApp(/*ConstantsHookConfig.FLAG_NEW_LISTNER_APP, */context,false);
			}
		}
		reSetContinus(netTask.getIsPause());
		
		if(isReSetString(netTask.getApkVer(), apkVer)){
			SPrefHookUtil.putTaskStr(context, SPrefHookUtil.KEY_TASK_APK_VER, netTask.getApkVer());
		}
		if(isReSetString(netTask.getScrVer(), scriptVer)){
			SPrefHookUtil.putTaskStr(context, SPrefHookUtil.KEY_TASK_SCR_VER, netTask.getScrVer());
		}
		if(isReSetString(netTask.getPlanId(),planId)){
			SPrefHookUtil.putTaskStr(context, SPrefHookUtil.KEY_TASK_PLAN_ID, netTask.getPlanId());
			SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, netTask.getExecNum());
		}
		if(isReSetInt(netTask.getScriptTime(), scriptTime)){
			SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_SCRIPT_TIME, netTask.getScriptTime());
		}
		int execNum = SPrefHookUtil.getTaskInt(context, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME,SPrefHookUtil.D_TASK_CUR_RUN_TIME);//当前执行次数
		if(isReSetCurRunTime(netTask.getExecNum(), execNum)){
			SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, netTask.getExecNum());
		}
		
		
		if(isReSetInt(netTask.getExecTotal(), execTotal)){
			SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_RUN_TIMES, netTask.getExecTotal());
		}
		if(isReSetString(netTask.getOrderId(), orderId)){
			SPrefHookUtil.putTaskStr(context, SPrefHookUtil.KEY_TASK_ORDER_ID, netTask.getOrderId());
		}
		String retain = netTask.getRetained().replace(",","_");
		if(isReSetString(retain, oldRetained)){
			dao.insertOrReplaceLiuCunSetting(netTask.getPackageName(), retain);
		}
		if(isReSetString(netTask.getApkPath(), apkUrl)){
			SPrefHookUtil.putTaskStr(context, SPrefHookUtil.KEY_TASK_APK_URL, netTask.getApkPath());
		}
		if(isReSetString(netTask.getLuaPath(), scriptUrl)){
			SPrefHookUtil.putTaskStr(context, SPrefHookUtil.KEY_TASK_SCRIPT_URL, netTask.getLuaPath());
		}
		if(isReSetInt(netTask.getTaskId(), taskId)){
			SPrefHookUtil.putTaskInt(context, SPrefHookUtil.KEY_TASK_TASK_ID, netTask.getTaskId());
		}
	}
	private void reSetContinus(int isPause) {
		boolean isContinue = SPrefHookUtil.getTaskBoolean(context, SPrefHookUtil.KEY_TASK_CONTINUOUS, SPrefHookUtil.D_TASK_CONTINUOUS);
		int con = isContinue? 0 :1;
		if(con != isPause){
			boolean newCon = isPause==0?true:false;
			SPrefHookUtil.putTaskBoolean(context, SPrefHookUtil.KEY_TASK_CONTINUOUS, newCon);
		}
	}
	private void reSetVpn(int isUseVpn) {
		boolean vpn = SPrefHookUtil.getTaskBoolean(context, SPrefHookUtil.KEY_TASK_VPN_AUTO_CONN, SPrefHookUtil.D_TASK_VPN_AUTO_CONN);
		boolean vpn2 = isUseVpn==0?false:true;
		if(vpn!=vpn2){
			SPrefHookUtil.putTaskBoolean(context, SPrefHookUtil.KEY_TASK_VPN_AUTO_CONN, vpn2);
		}
	}
	private boolean isReSetCurRunTime(int execNum, int execNum2) {
		if(execNum>execNum2/*||execNum==0*/)return true;
		return false;
	}
	private boolean isReSetString(String newStr,String oldStr) {
		if((TextUtils.isEmpty(oldStr)&&!TextUtils.isEmpty(newStr))||(!TextUtils.isEmpty(oldStr)&&!TextUtils.isEmpty(newStr)&&!oldStr.equals(newStr))){
			return true;
		}
		return false;
	}
	private boolean isReSetInt(int newInt,int oldInt) {
		if(newInt!=oldInt){
			return true;
		}
		return false;
	}
	/**
	 * Login-----------------------------------------------------------------------------------------------------------------
	 */
	public static final String KEY_IMEI = "IMEI";
	public static final String KEY_AUTHOR = "AUTHORIZATION";
	public void getLogin(Map<String, String> map){
		RetrofitService2 service= RetrofitServiceGeter2.getRetrofitService(getUrl(), false);
		Logger.i("getLogin=="+getUrl()+"?mod=api&action=loginIsValid&imei="+map.get(KEY_IMEI)+"&deviceCode="+map.get(KEY_AUTHOR));
		service.login(map.get(KEY_IMEI), map.get(KEY_AUTHOR)).enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> arg0, Response<ResponseBody> arg1) {
				try {
					if(arg1.code() == 200&&arg1.body()!=null){
						listener.result(arg1.body().string());
					}else {
						listener.result(arg1.code());
					}
				} catch (IOException e) {
					listener.result(null);
					Logger.i("IOException::"+e);
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(Call<ResponseBody> arg0, Throwable arg1) {
				listener.result(null);
				Logger.i("Throwable::"+arg1);
			}
		});
	}
	
	/**
	 * 下载更新--------------------------------------------------------------------------------------------------------------
	 */
	DownloadAsyncTask asyncTask = null;
	public void downBigFileFromServer(final Context  context,final Map<String, String> map,boolean isBackUp){
		String url =getDownUrl() /*getUrl()*/+"/"+map.get(HttpConstants.REQUEST_KEY_URL);
		Logger.i("下载：：="+url);
		File file = convertPath(map.get(HttpConstants.REQUEST_KEY_PACKAGENAME),map.get(HttpConstants.REQUEST_KEY_PATH));
		asyncTask = new DownloadAsyncTask(url, file.getAbsolutePath());
		String channel = SPrefUtil.getString(context, SPrefUtil.C_CHANNEL, SPrefUtil.D_CHANNEL);
		String backupPath = "";
		if(isBackUp){
			backupPath = MyfileUtil.getBackUpPath(map.get(HttpConstants.REQUEST_KEY_PACKAGENAME), channel);
		}
		asyncTask.execute(backupPath);
		asyncTask.setCallback(new DownCallback() {
			@Override
			public void onSucceed() {
				if(listener!=null){
					listener.result(HttpConstants.RESPONSE_OK);
				}
			}
			@Override
			public void onFailure(String err) {
				Logger.i("EXE:::"+err);
				if(listener!=null){
					listener.result(HttpConstants.RESPONSE_ERR);
				}
			}
		});
	}
	public void cancleDown(){
		if(asyncTask!=null){
			asyncTask.cancel(true);
		}
	}
	protected File convertPath(String packageName, String path) {
		File dir;
		File downFile = null; 
		if(path.contains(HttpConstants.DOWNLOAD_AKP_KEY)){
			dir = FileUtil.checkExtraDir(ConstantsConfig.DOWNLOAD_PATH_NAME);
			downFile = new File(dir,CommonOperationUtil.convertPackageName2Apk(packageName));
		}else if (path.contains(HttpConstants.DOWNLOAD_SCRIPT_KEY)) {
			dir = FileUtil.checkExtraDir(ConstantsConfig.DOWNLOAD_SCRIPT_PATH_NAME);
			downFile = new File(dir,CommonOperationUtil.convertPackageName2Script(packageName));
		}
		else {
			downFile = new File(path);
			FileUtil.checkDir(downFile.getParentFile());
		}
		return downFile;
	}
	public static final String NEWPARAM = "1";
	public static final String REMAINPARAM = "2";
	/**
	 * 
	 * 上传次数
	 * isNewStatus  1新增 2留存
	 * @param logTime 
	 * @param taskId 
	 */
	
	
	public void uploadParamStatus(final boolean save, final int taskId, final String logDate,int count ,String param){
		RetrofitService2 service= RetrofitServiceGeter2.getRetrofitService(getUrl(), false);
		final String packageName = SPrefHookUtil.getSettingStr(context, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
		String deviceId = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_ID);
		String isNewStatus = save?NEWPARAM:REMAINPARAM;
		String deviceCode = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_CODE);
		String imei = imeiget.getImei();
//		String imei = tm.getDeviceId();
		String planId = SPrefHookUtil.getCurTaskStr(context, SPrefHookUtil.KEY_CUR_TASK_PLAN_ID);
		int  execNum = count;
		String orderId = SPrefHookUtil.getCurTaskStr(context, SPrefHookUtil.KEY_CUR_TASK_ORDER_ID);//SPrefHookUtil.getTaskStr(context, SPrefHookUtil.KEY_TASK_ORDER_ID);
		Logger.i(getUrl()+"?mod=api&action=logDeviceExecNum&deviceId="+deviceId+"&orderId="+orderId+"&taskId="+taskId+"&isNewStatus="+isNewStatus+"&deviceCode="+deviceCode
				+"&imei="+imei+"&logDate="+logDate+"&planId="+planId+"&execNum="+execNum+"&deviceParam="+param);
		service.uploadStatus(orderId,deviceId, String.valueOf(taskId), isNewStatus,deviceCode,imei,logDate,planId,execNum,param).enqueue(new Callback<ResponseBody>() {
			
			@Override
			public void onResponse(Call<ResponseBody> arg0, Response<ResponseBody> arg1) {
				if(arg1.code() == 200&&arg1.body()!=null){
					try {
						String response = arg1.body().string();
						Logger.i("uploadParamStatus========response==================="+response);
						JSONObject jsonObject = new JSONObject(response);
						if(jsonObject.optInt(HttpConstants.CODE)==0){
							if(listener!=null){
								listener.result(HttpConstants.RESPONSE_OK);
								MyfileUtil.recordUploadLog("  listener==jsonObject  "+jsonObject.toString(),packageName);
							}else {
								MyfileUtil.recordUploadLog("  listener==null  ",packageName);
							}
						}else {
							MyfileUtil.recordUploadLog(" CODE:  "+jsonObject.optInt(HttpConstants.CODE)+"  ",packageName);
							if(listener!=null){
								listener.result(HttpConstants.RESPONSE_ERR);
							}
						}
					} catch (Exception e) {
						MyfileUtil.recordUploadLog(" e:  "+e+"  ",packageName);
						if(listener!=null){
							listener.result(HttpConstants.RESPONSE_ERR);
						}
						Logger.i("uploadParamStatus===============jsonObject.optInt(CODE)==e=="+e);
						e.printStackTrace();
					}
				}
			}
			@Override
			public void onFailure(Call<ResponseBody> arg0, Throwable arg1) {
				Logger.i("uploadParamStatus===================Throwable===="+arg1);
//				uploadParamStatus(save, taskId, logDate, param);
				MyfileUtil.recordUploadLog(" Throwable:  "+arg1+"  ",packageName);
				if(listener!=null){
					listener.result(HttpConstants.RESPONSE_ERR);
				}
			}
		});
	}
	public void checkUpdate(){
		String deviceId = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_ID);
		String deviceCode = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_CODE);
		String imei = imeiget.getImei();
//		String imei = tm.getDeviceId();
		RetrofitService2 service= RetrofitServiceGeter2.getRetrofitService(getUrl(), false);
		Logger.i("CHECKVer::"+getUrl()+"?mod=api&action=getVersionName&deviceId="+deviceId+"&deviceCode="+deviceCode+"&imei="+imei);
		service.checkUpdate(deviceId,deviceCode,imei).enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> arg0, Response<ResponseBody> arg1) {
				try {
					if(arg1.code()==200&&arg1.body()!=null){
						if(listener!=null){
							listener.result(arg1.body().string());
						}else {
							listener.result(null);
						}
					}
					Logger.i("checkUpdate=="+arg1.body().string());
				} catch (IOException e) {
					listener.result(null);
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(Call<ResponseBody> arg0, Throwable arg1) {
				listener.result(arg1);
			}
		});
	}
	public void getLiuCunParam(){
		String deviceId = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_ID);
		String deviceCode = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_CODE);
		String imei = imeiget.getImei();
//		String imei = tm.getDeviceId();
		String orderId = SPrefHookUtil.getCurTaskStr(context, SPrefHookUtil.KEY_TASK_ORDER_ID);
		String planId = SPrefHookUtil.getCurTaskStr(context, SPrefHookUtil.KEY_TASK_PLAN_ID);
		RetrofitService2 service= RetrofitServiceGeter2.getRetrofitService(getUrl(), false);
		Logger.i("留存 getLiuCunParam="+getUrl()+"?mod=api&action=getRetainData&deviceId="+deviceId+"&deviceCode="+deviceCode+"&imei="+imei+"&orderId="+orderId+"&planId="+planId);
		service.getretainData(deviceId, deviceCode, imei, orderId,planId).enqueue(new Callback<ResponseBody>() {
			
			@Override
			public void onResponse(Call<ResponseBody> arg0, Response<ResponseBody> arg1) {
				if(arg1.code()==200&&arg1.body()!=null){
					try {
						String result = arg1.body().string();
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(result);
							if(jsonObject.optInt(HttpConstants.CODE)==0){
								JSONObject data = jsonObject.optJSONObject(HttpConstants.DATA);
								String param = data.optString(HttpConstants.RESULT);
								Intent intent = new Intent();  
					            intent.setAction(ConstantsConfig.ACTION_PARAM_BROADCAST);  
					            intent.putExtra(ConstantsConfig.INTENT_PARAM, param);  
					            intent.putExtra(ConstantsConfig.INTENT_SAVE, false);  
					            context.sendBroadcast(intent); 
					            Logger.i("留存   param::"+param);
							}
							else {
								SendBroadCastUtil.getLiucunPramErr(context);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							SendBroadCastUtil.getLiucunPramErr(context);
						}
						Logger.i("留存::"+result);
					} catch (IOException e) {
						e.printStackTrace();
						SendBroadCastUtil.getLiucunPramErr(context);
					}
				}
			}
			@Override
			public void onFailure(Call<ResponseBody> arg0, Throwable arg1) {
				Logger.i("留存:Throwable:"+arg1);
				SendBroadCastUtil.getLiucunPramErr(context);
			}
		});
		
	}
	public void uploadVPNStatus(int status){
		String deviceId = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_ID);
		String deviceCode = SPrefHookUtil.getLoginStr(context, SPrefHookUtil.KEY_LOGIN_DEVICE_CODE);
		String imei = imeiget.getImei();
		RetrofitService2 service= RetrofitServiceGeter2.getRetrofitService(getUrl(), false);
		Logger.i("vpn 状态： ="+getUrl()+"?mod=api&action=sendVpnStatusToServer&deviceId="+deviceId+"&deviceCode="+deviceCode+"&imei="+imei+"&status"+status);
		service.uploadVpnStatus(deviceId, deviceCode, imei, status).enqueue(new Callback<ResponseBody>() {
			
			@Override
			public void onResponse(Call<ResponseBody> arg0, Response<ResponseBody> arg1) {
				if(arg1.code()==200&&arg1.body()!=null){
					try {
						String json = arg1.body().string();
						JSONObject jsonObject = new JSONObject(json);
						int code = jsonObject.optInt("CODE");
						Logger.i("CODE"+code);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			@Override
			public void onFailure(Call<ResponseBody> arg0, Throwable arg1) {
				
			}
		});
	}
}
