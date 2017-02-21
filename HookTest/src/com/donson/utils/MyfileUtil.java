package com.donson.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import okhttp3.ResponseBody;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.utils.GetPackageSizeUtil.SizeResponseListener;
import com.donson.viewinterface.CallBackInterface;
import com.param.config.ConstantsConfig;
import com.param.utils.FileUtil;

public class MyfileUtil extends FileUtil {
	public static boolean isExternalStorageFile(String path){
		String externalPath = ConstantsConfig.ROOTPATH;
		int length = externalPath.length();
		if(!TextUtils.isEmpty(path)&&path.length()>=length&&externalPath.equals(path.substring(0, length))){
			return true;
		}else {
			return false;
		}
	}
	/**
	 * @param path
	 * @param listenPacName
	 * @return
	 */
	public static boolean isDataStorageFile(String path,String listenPacName){
		if(TextUtils.isEmpty(listenPacName)||TextUtils.isEmpty(path)){
			return false;
		}
		if(path.contains("data/data/"+listenPacName)){
			return false;
		}
		if(path.contains(listenPacName)){
			return true;
		}
		
		String dataPath = Environment.getDataDirectory().getAbsolutePath()+File.separator+"data/"+listenPacName;
		int length = dataPath.length();
		if(!TextUtils.isEmpty(path)&&path.length()>=length&&dataPath.equals(path.substring(0, length))){
			return true;
		}else {
			return false;
		}
	}
	public static boolean deleteFile(String path,String packageName) {
		try {
			File file = new File(path);
			if (file.exists()) {
				if (MyfileUtil.isExternalStorageFile(path)) {
					boolean result = file.delete();
					if (result) return true;
				} else {
					if(!path.contains("data/data/"+packageName)&&
							!path.contains("data/app/"+packageName)&&
							!path.contains("data/user/0/"+packageName)&&
							!path.contains("data/app-lib")){
						boolean dataDelete = CmdUtil.clearDataFile(path);
						if(dataDelete) return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean deleteFileDir(String path,String packageName) {
		try {
			File file = new File(path);
			if (file.exists()) {
				if (MyfileUtil.isExternalStorageFile(path)) {
					return deleteFileDirectory(file,packageName);
//					boolean result = file.delete();
//					if (result) return true;
				} else {
					if(!path.contains("data/data/"+packageName)&&
							!path.contains("data/app/"+packageName)&&
							!path.contains("data/user/0/"+packageName)&&
							!path.contains("data/app-lib")){
						return  CmdUtil.clearDataFile(path);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	private static boolean deleteFileDirectory(File file,String packageName) {
		String path = file.getAbsolutePath();
		if(path.equals("/storage/scard1")||path.equals("storage/scard1")||path.equals("/storage/scard1/")||path.equals("storage/scard1/")){
			return false;
		}
		if(path.equals("/storage/emulated/0")||path.equals("storage/emulated/0/")||path.equals("storage/emulated/0")||path.equals("/storage/emulated/0/")){
			return false;
		}
		if(path.equals("/storage/emulated/0/Android")||path.equals("storage/emulated/0/Android/")||path.equals("storage/emulated/0/Android")||path.equals("/storage/emulated/0/Android/")){
			return false;
		}
		if(path.equals("/storage/scard1/Android")||path.equals("storage/scard1/Android/")||path.equals("storage/scard1/Android")||path.equals("/storage/scard1/0/Android/")){
			return false;
		}
		if(path.contains("MIUI")||(path.contains("Android/data")&&!path.contains(packageName))||path.contains("Xiaomi")){
			file.delete();
			return false;
		}
		if(path.contains(ConstantsConfig.EXTRA_PATH)||path.contains("xx/crash")||path.contains("/storage/emulated/0/xx")){
			return false;
		}
		if(file.isDirectory()){
			Logger.i("file::"+file.getAbsolutePath());
			File[] files = file.listFiles();
			if(files.length>0){
				for (int i = 0; i < files.length; i++) {
					deleteFileDirectory(files[i],packageName);
				}
			}else {
				file.delete();
			}
		}else {
			return file.delete();
		}
		return false;
	}
	/**
	 * b
	 */
	static long mSize = -1;
	public static long getDataSize2(Context context,String packageName){
		mSize=-1;
		GetPackageSizeUtil packageSizeUtil = new GetPackageSizeUtil(context);
		packageSizeUtil.setListener(new SizeResponseListener() {
			
			@Override
			public void response(Object size) {
				long double1 = (long) size;;
				mSize=double1;
			}
		});
		packageSizeUtil.getpkginfo(packageName);
		while(mSize==-1){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return mSize;
	}
	/**
	 * @param packageName
	 * @return
	 */
	public static double getDataSize(String packageName){
		String path = "data/data/"+packageName;
		return getFileLoaderSize(path)/1024;
	}
	public static long getFileLoaderSize(String path){
		File file = new File(path);
		if (file.exists()) {
			if(file.isDirectory()){
				long size = 0;
				String[] paths = CmdUtil.lsDataDir(path);
				if(paths!=null){
					for (int i = 0; i < paths.length; i++) {
						size+=getFileLoaderSize(paths[i]);
					}
					return size;
				}
			}else {
				long size = file.length();
				return size;  //KB 
			}
		}
		return 0;
	}
	
	public static void deleteFile(String path){
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}
	
	public static boolean copyFile2Other(String sourceP, String destinationP) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			File sourceFile = new File(sourceP);
			if (!sourceFile.exists()) {
				return false;
			}
			File desFile = new File(destinationP);
			if (!desFile.exists()) {
				desFile.getParentFile().mkdirs();
				desFile.createNewFile();
			}
			fis = new FileInputStream(sourceFile);
			fos = new FileOutputStream(desFile);
			int tmp = -1;
			while((tmp = fis.read(buffer))!=-1){
				fos.write(buffer, 0, tmp);
			}
			fos.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(fis!=null){
					fis.close();fis = null;
				}if(fos!=null){
					fos.close();fos = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;

	}
	public static byte[] buffer = new byte[1024];
	
	public static boolean copyDownApk2Backups(String source,String desPath){
		return copyFile2Other(source, desPath);
	}
	public static String getBackUpPath (String packageName,String channel){
		return ConstantsHookConfig.APK_LOCAL_BACKUP_PATH+"/"+packageName+"_"+channel+".apk";
	}
	public static boolean copyLua2XXScriptFile(String source,
			String desFileName,boolean replace) {
		File file = new File(source);
		if (!file.exists()) {
			return false;
		}
		String desPath = ConstantsHookConfig.SCRIPT_FILE+desFileName;
		File des = new File(desPath);
		checkDir(des.getParentFile());
		if(des.exists()&&!replace){
			return false;
		}
		return copyFile2Other(source, desPath);
	}
	public static void recordAddApk(String packageName) {
		File file = new File(ConstantsHookConfig.PATH_RECORD_APK_INSTALL);
		appendStringToFile(packageName, file);
	}
	
	public static void appendStringToFile(String string,File file) {
		RandomAccessFile accessFile = null;
		try {
			checkDir(file.getParentFile());
			if (!file.exists()) {
				file.createNewFile();
			}
			accessFile = new RandomAccessFile(file.getAbsolutePath(), "rw");
			accessFile.seek(accessFile.length());
			accessFile.writeBytes(string + "\n");
		} catch (Exception e) {
			e.printStackTrace();
			Logger.h("record===ex=======" + e);
		} finally {
			try {
				if (accessFile != null) {
					accessFile.close();
					accessFile = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static boolean writeResponseBodyToDisk(String channel ,String packageName ,File downFile, ResponseBody body) {
		Logger.i("=========="+downFile+"  "+downFile.exists());
		FileOutputStream fileOutputStream = null;
		BufferedInputStream inputStream = null;
		try {
			if (!downFile.exists())
				downFile.createNewFile();
			fileOutputStream = new FileOutputStream(downFile);
			inputStream = new BufferedInputStream(body.byteStream());
			byte[] tmp = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(tmp)) != -1) {
				fileOutputStream.write(tmp, 0, len);
			}
			fileOutputStream.flush();
			String soucrePath = downFile.getAbsolutePath(); 
			if(soucrePath.contains(ConstantsHookConfig.APK_LOCAL_PATH)){
				MyfileUtil.copyDownApk2Backups(soucrePath, MyfileUtil.getBackUpPath(packageName, channel));
			}
			
			Logger.i("=========="+downFile+"  "+fileOutputStream);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
					fileOutputStream = null;
				}
				if (inputStream != null) {
//					inputStream.close();
//					inputStream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 从 文件 逐行读取
	 * @param path
	 * @return
	 */
	public static  String  readFileFromSDCard(String path){ 
		File file=new File(path);
		checkDir(file.getParentFile());
		StringBuilder sb=new StringBuilder();
		BufferedReader br = null;
		try{
			if(!file.exists()){
				file.createNewFile();
			}
			br=new BufferedReader(new FileReader(file));
			String line=null;
            while((line=br.readLine())!=null)
            {
         	   sb.append(line+"\n");
           }
            return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				if(br!=null)
					br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}  
	public final int  vpnConnectErr = 0;
	public final int paramuploadErr = 1;
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
	public void recordLog(int reason, String packageName){
		File file = new File(ConstantsHookConfig.PATH_RECORD_ERR_LOG);
		checkDir();
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			String time = dateFormat.format(new Date(System.currentTimeMillis()));
			String reasons = reason==vpnConnectErr?"___VPN   ":"___PARAM ";
			String record = time+":"+reasons+"   :"+packageName;
			appendStringToFile(record, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void recordUploadLog(String err, String packageName){
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String data = dateFormat1.format(System.currentTimeMillis());
		File file = new File(ConstantsHookConfig.PATH_RECORD_UPLOAD_ERR_LOG+""+data+".txt");
		checkDir(file.getParentFile());
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			String time = dateFormat.format(new Date(System.currentTimeMillis()));
			String record = time+":"+packageName+"   :"+err;
			appendStringToFile(record, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
