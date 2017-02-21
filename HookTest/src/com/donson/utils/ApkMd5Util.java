package com.donson.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.param.utils.MD5Util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

public class ApkMd5Util {
	//安装后MD5值不同 和版本相关的
	static String TAG = "TEST_HOOK";
	public static void get(Context context,String packageName){
		StringBuffer sb = new StringBuffer();
		PackageInfo pi;
		try {
			pi = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
		Signature signatures = pi.signatures[0];
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(signatures.toByteArray());
		byte[] digest = md.digest();
		String res = MD5Util.toHexString(digest);
		Log.i(TAG, "apk md5 = "+res);

		sb.append("apk md5 = "+res);}
		catch (Exception e) {
			e.printStackTrace();
		}
		}

		 
}
