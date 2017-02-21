package com.donson.realparam.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.donson.config.Logger;
import com.donson.realparam.utils.AdvertisingIdClient2.AdInfo;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;

public class AdvertisingIDUtil {
	public interface AdCallback{
		void callback(Message message);
	}
	public static void getADID1(final Context mContext,final AdCallback adCallback) {
		final Handler mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				adCallback.callback(msg);
			}
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				Info adInfo = null;
				try {
					adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
					if (adInfo != null) {
						final String id = adInfo.getId();
						final boolean isLAT = adInfo.isLimitAdTrackingEnabled();
						Message message = mHandler.obtainMessage();
						message.obj = id+":"+isLAT;
						message.what = GET_AD_ID_OK;
						mHandler.sendMessage(message);
					}
				} catch (Exception e) {
					Logger.i("EX:===" + e);
					Message message = mHandler.obtainMessage();
					message.what = GET_AD_ID_ERROR;
					message.obj = e;
					mHandler.sendMessage(message);
				}
			}
			
		}).start();
		
	}
	public final static int GET_AD_ID_OK = 0;
	public final static int GET_AD_ID_ERROR = 1;
	public static void getADID2(final Activity mActivity,final AdCallback adCallback ) {
		final Handler mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				adCallback.callback(msg);
			}
		};
		new Thread(new Runnable() {
			public void run() {
				try {
					AdInfo adInfo = AdvertisingIdClient2
							.getAdvertisingIdInfo(mActivity);
					String advertisingId = adInfo.getId();
					boolean optOutEnabled = adInfo.isLimitAdTrackingEnabled();
					Logger.i("advertisingId" + advertisingId);
					Logger.i("optOutEnabled" + optOutEnabled);
					Message message = mHandler.obtainMessage();
					message.obj = advertisingId+":"+optOutEnabled;
					message.what = GET_AD_ID_OK;
					mHandler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
					Logger.i("e:" + e);
					Message message = mHandler.obtainMessage();
					message.what = GET_AD_ID_ERROR;
					message.obj = e;
					mHandler.sendMessage(message);
				}
				
			}
		}).start();
	}
}
