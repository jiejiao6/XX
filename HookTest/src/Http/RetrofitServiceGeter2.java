package Http;

import java.util.concurrent.TimeUnit;

import com.donson.config.HttpConstants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceGeter2 {
	public static RetrofitService2 getRetrofitService(String url, boolean convert) {
		// if(retrofitService==null){
		RetrofitService2 retrofitService = null;
		OkHttpClient client = new OkHttpClient();
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(HttpConstants.NET_CONNECT_TIME_OUT_TIME,
				TimeUnit.SECONDS);
		builder.writeTimeout(HttpConstants.NET_WRITE_TIME_OUT_TIME,
				TimeUnit.SECONDS);
		builder.readTimeout(HttpConstants.NET_READ_TIME_OUT_TIME,
				TimeUnit.SECONDS);
		Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
		retrofitBuilder.client(builder.build());
		retrofitBuilder.baseUrl(url);
		if (convert) {
			retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
		}
		Retrofit retrofit = retrofitBuilder.build();
		retrofitService = retrofit.create(RetrofitService2.class);
		// }
		return retrofitService;
	}
	/**
	 * 下载用
	 * 
	 * @param url
	 * @param convert
	 * @return
	 *//*
	public static RetrofitService2 getDownRetrofitService(String url,boolean convert) {
		RetrofitService2 retrofitService = null;
		OkHttpClient.Builder builder = ProgressHelper.addProgress(null);
		Retrofit.Builder retrofitBuilder1 = new Retrofit.Builder();
		retrofitBuilder1.client(builder.build());
		retrofitBuilder1.baseUrl(url);
		if (convert) {
			retrofitBuilder1.addConverterFactory(GsonConverterFactory.create());
		}
		Retrofit retrofit = retrofitBuilder1.build();
		retrofitService = retrofit.create(RetrofitService2.class);
		return retrofitService;
	}*/
	
}
