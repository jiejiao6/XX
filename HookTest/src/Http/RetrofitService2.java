package Http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

import com.donson.myhook.bean.JsonTask;
import com.donson.myhook.bean.NetTask;

public interface RetrofitService2 {
	@FormUrlEncoded
	@POST("?mod=api&action=getDeviceInfo")
	Call<JsonTask> getTask(@Field("deviceId") String deviceId,
			@Field("deviceCode") String deviceCode, @Field("imei") String imei,
			@Field("planId") String planId, @Field("execNum") String execNum,
			@Field("version") String version,
			@Field("ssid") String ssid);
	@FormUrlEncoded
	@POST("?mod=api&action=getDeviceInfo")
	Call<ResponseBody> getTask2(@Field("deviceId") String deviceId,
			@Field("deviceCode") String deviceCode, @Field("imei") String imei,
			@Field("planId") String planId, @Field("execNum") String execNum,
			@Field("version") String version,
			@Field("ssid") String ssid);

	@FormUrlEncoded
	@POST("?mod=api&action=loginIsValid")
	Call<ResponseBody> login(@Field("imei") String imei,
			@Field("deviceCode") String authorization);

	@FormUrlEncoded
	@POST("?mod=api&action=logDeviceExecNum")
	Call<ResponseBody> uploadStatus(@Field("orderId") String orderId,
			@Field("deviceId") String deviceId, @Field("taskId") String taskId,
			@Field("isNewStatus") String isNewStatus,
			@Field("deviceCode") String deviceCode, @Field("imei") String imei,
			@Field("logDate") String logDate, @Field("planId") String planId,
			@Field("execNum") int execNum, @Field("deviceParam") String param);

	@FormUrlEncoded
	@POST("?mod=api&action=getRetainData")
	Call<ResponseBody> getretainData(@Field("deviceId") String deviceId,
			@Field("deviceCode") String deviceCode, @Field("imei") String imei,
			@Field("orderId") String orderId, @Field("planId") String planId);

	@FormUrlEncoded
	@POST("?mod=api&action=getVersionName")
	Call<ResponseBody> checkUpdate(@Field("deviceId") String deviceId,
			@Field("deviceCode") String deviceCode, @Field("imei") String imei);

	@FormUrlEncoded
	@POST("?mod=api&action=getVpnAccount")
	Call<ResponseBody> getVpnAccount(@Field("deviceId") String deviceId,
			@Field("deviceCode") String deviceCode, @Field("imei") String imei,
			@Field("vpnId") int vpnId);
	
	@FormUrlEncoded
	@POST("?mod=api&action=sendVpnStatusToServer")
	Call<ResponseBody> uploadVpnStatus(@Field("deviceId") String deviceId,
			@Field("deviceCode") String deviceCode, @Field("imei") String imei,
			@Field("status") int status);


}
