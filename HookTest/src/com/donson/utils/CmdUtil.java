package com.donson.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.donson.config.Logger;

public class CmdUtil {

	public static Process run(String cmd) throws Exception {
		ProcessBuilder builder = new ProcessBuilder("su");
		Process process = builder.start();
		DataOutputStream dos = new DataOutputStream(process.getOutputStream());
		dos.writeBytes(cmd + "\n");
		dos.flush();
		dos.writeBytes("exit \n");
		dos.flush();
		dos.close();
		dos = null;
		process.waitFor();
		int result = process.exitValue();
		return process;
	}
	
	public static String readResult(Process process) throws Exception {
		DataInputStream in = new DataInputStream(process.getInputStream());
		StringBuffer sb = new StringBuffer();
		byte[] buffer = new byte[1024];
		int tmp = -1;
		while ((tmp = in.read(buffer)) != -1) {
			sb.append(new String(buffer, 0, tmp));
		}
		in.close();
		in = null;
		return sb.toString();
	}
	/**
	 * @return
	 */
	public static boolean isRoot() {
		try {
			Process process = run("date");
			process.waitFor();
			String result = readResult(process);
//			Logger.i("cmd res==" + result);
			if (!TextUtils.isEmpty(result)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.i("cmd isRootEXE=  " + e);
		}
		return false;
	}
//	/**
//	 * @param packageName
//	 */
//	public static void clearAllDataFile(final String packageName) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					String path = "data/data/" + packageName;
//					File fileDir = new File(path);
//					if (fileDir.exists()) {
//						String killer = "rm -rf " + fileDir.getAbsolutePath()+"/*";
//						//rm -rf /data/data/com.example.getparamtest/*
//						Process process = CmdUtil.run(killer);
//						process.waitFor();
//						String res = CmdUtil.readResult(process);
//						Logger.i("clearAllData==" + res);
//					}
//				} catch (Exception e) {
//					Logger.i("clearAllData==Exception==" + e);
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}
	/**
	 * @param path
	 * @return
	 */
	public static boolean clearDataFile(String path) {
		try {
			String cmd = "rm -r " + path;
			Process process = CmdUtil.run(cmd);
			process.waitFor();
			int res = process.exitValue();
			String res2 = CmdUtil.readResult(process);
			Logger.i("CMD:clearDataFile=" + res + "  res2:" + res2 + "  path:" + path);
			if (res == 0)
				return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
//	/**
//	 * @param mContext
//	 * @param packageName
//	 */
//	public static void clearCache(final Context mContext,
//			final String packageName) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Context otherAppContext = mContext.createPackageContext(
//							packageName, Context.CONTEXT_IGNORE_SECURITY);
//					File path = otherAppContext.getCacheDir();
//					Logger.i("clearCache==" + path.getAbsolutePath());
//					if (path == null)
//						return;
//					String killer = " rm -r " + path.toString();
//					Process process = CmdUtil.run(killer);
//					process.waitFor();
//					String res = CmdUtil.readResult(process);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}
	/**
	 * @param packageName
	 */
	public static String[] lsDataDir(String path){
		String cmd = "ls " + path;
		Process process;
		try {
			process = CmdUtil.run(cmd);
			process.waitFor();
			String result = CmdUtil.readResult(process);
			if(result.equals("")){
				return null;
			}
			String lsResult[] = result.split("\n");
			String[] resultArr = new String[lsResult.length];
			for(int i = 0;i<lsResult.length;i++){
				if(!path.endsWith("/")){
					resultArr[i]=path+"/"+lsResult[i];
				}else resultArr[i]=path+lsResult[i];
			}
			return resultArr;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.i("Cmd lsDataDir Exception::"+e);
		}
		return null;
	}
	 /** 
     */  
	public static boolean killProcess(String packageName) {  
    	String cmd = "am force-stop " + packageName + " \n";  
    	Process process;
		try {
			process = CmdUtil.run(cmd);
			process.waitFor();
			String res = CmdUtil.readResult(process);
			Logger.i("CMD==:killProcess==res: "+res+"  packageName："+packageName);
			int result = process.exitValue();
			if(result==0) return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			process = null;
		}
		return false;
    } 
	
	 /** 
     * packageɾ
     */  
	public static void ClearApk(String packageName) {  
    	String cmd = "pm clear " + packageName + " \n";  
    	Process process;
		try {
			process = CmdUtil.run(cmd);
			process.waitFor();
			String res = CmdUtil.readResult(process);
			Logger.i("CMD==:ClearApk:res="+res+"  packageName："+packageName);
	    	process = null;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.i("CMD:ClearApk:res:ex:"+e);
		}
    } 
//	localStringBuffer.append("pm clear " + str).append("\n");

	public static boolean unInstallApk(String packageName) {
		String cmd = "pm uninstall " + packageName;
		Process process;
		try {
			process = CmdUtil.run(cmd);
			process.waitFor();
			String res = CmdUtil.readResult(process);
			process = null;
			if(res.contains("Success")){
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			process = null;
		}
		return false;
	}
	/**
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAppInstalled(Context context,String packageName) {  
        PackageManager pm = context.getPackageManager();  
        boolean installed =false;  
        try {  
            pm.getPackageInfo(packageName,PackageManager.GET_ACTIVITIES);  
            installed =true;  
        } catch(PackageManager.NameNotFoundException e) {  
            installed =false;  
        }  
        return installed;  
    } 

	public static boolean installApk(Context context, String filePath) {
		String cmd = "pm install -r " + filePath;
		Logger.i("install===cmd=" + cmd);
		Process process;
		try {
			process = CmdUtil.run(cmd);
			process.waitFor();
			String res = CmdUtil.readResult(process);
//			Success
			Logger.i("install====" + res);//Success 包含换行符
			if(res.contains("Success")){
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			process = null;
		}
		return false;
	}
	public static void openApk(Context context,String fullPackageName,String fullActivityName){
		String cmd = "am start -n " + fullPackageName+"/"+fullActivityName;
		Logger.i("CMD  openApk===cmd=" + cmd);
		Process process;
		try {
			process = CmdUtil.run(cmd);
			process.waitFor();
			String res = CmdUtil.readResult(process);
			Logger.i("CMD openApk====" + res);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			process = null;
		}
	}
}
