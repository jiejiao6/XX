package Xposed;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.RandomAccessFile;

import Xposed.HookMain.AppInfos_XC_MethodHook;
import Xposed.HookMain.My_XC_MethodHook;
import android.content.ContentResolver;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.donson.config.Logger;
import com.donson.utils.SPrefHookUtil;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedHelpers;

public class SystmeValueHook extends XHook {
	private static SystmeValueHook instance;
	private SystmeValueHook() {
		// TODO Auto-generated constructor stub
	}
	public static SystmeValueHook getInstance(){
		if(instance==null){
			synchronized (SystmeValueHook.class) {
				if(instance==null){
					instance = new SystmeValueHook();
				}
			}
		}
		return instance;
	}

	@Override
	protected void handleMethod(String packageName, ClassLoader classLoader)
			throws Exception {
//		context = null;
//		HookMethod(Activity.class, "onCreate", packageName, MethodInt.ACCOUNT,Bundle.class);
//		HookMethod(AccountManager.class, "getAccountsByType", packageName, MethodInt.ACCOUNT,String.class);
		Logger.h("======================================监听文件操作==============================================================");
		// 监听文件操作
			XposedHelpers.findAndHookConstructor(File.class.getName(), classLoader,
				new Object[] { String.class.getName(), new My_XC_MethodHook(packageName, "",MethodInt.FILE_CONSTRUCTION) });
			XposedHelpers.findAndHookConstructor(File.class.getName(), classLoader, new Object[] {
				String.class.getName(), String.class.getName(), new My_XC_MethodHook(packageName, "",MethodInt.FILE_CONSTRUCTION) });
			XposedHelpers.findAndHookConstructor(File.class.getName(), classLoader, new Object[] {
			File.class.getName(), String.class.getName(), new My_XC_MethodHook(packageName, "",MethodInt.FILE_CONSTRUCTION) });
//			XposedHelpers.findAndHookConstructor(File.class.getName(), classLoader, new Object[] {
//				URI.class.getName(), new My_XC_MethodHook(packageName, "") });
//			File file = new File(new URI(""));
		  /*******************************RandomAccessFile*******************/
			XposedHelpers.findAndHookConstructor(RandomAccessFile.class.getName(), classLoader, new Object[]{
		    	File.class.getName(),String.class.getName(),new My_XC_MethodHook(packageName, "",MethodInt.FILE_RANDOMFILE_CONSTRUCTION)
		    });
			XposedHelpers.findAndHookConstructor(RandomAccessFile.class.getName(), classLoader, new Object[]{
		    	String.class.getName(),String.class.getName(),new My_XC_MethodHook(packageName, "",MethodInt.FILE_RANDOMFILE_CONSTRUCTION)
		    });
		    /**********************************FileWriter******************/
		    XposedHelpers.findAndHookConstructor(FileWriter.class.getName(), classLoader, new Object[]{
		    	File.class.getName(),new My_XC_MethodHook(packageName, "", MethodInt.FILE_WRITER_CONSTRUCTION)
		    });
		    XposedHelpers.findAndHookConstructor(FileWriter.class.getName(), classLoader, new Object[]{
		    	String.class.getName(),new My_XC_MethodHook(packageName, "", MethodInt.FILE_WRITER_CONSTRUCTION)
		    });
		    XposedHelpers.findAndHookConstructor(FileWriter.class.getName(), classLoader, new Object[]{
		    	File.class.getName(),Boolean.TYPE.getName(),new My_XC_MethodHook(packageName, "", MethodInt.FILE_WRITER_CONSTRUCTION)
		    });
		    XposedHelpers.findAndHookConstructor(FileWriter.class.getName(), classLoader, new Object[]{
		    	String.class.getName(),Boolean.TYPE.getName(),new My_XC_MethodHook(packageName, "", MethodInt.FILE_WRITER_CONSTRUCTION)
		    });
		    /********************************FileOutputStream************************************/
		    XposedHelpers.findAndHookConstructor(FileOutputStream.class.getName(), classLoader, new Object[]{
		    	File.class.getName(),new My_XC_MethodHook(packageName, "", MethodInt.FILE_WRITER_CONSTRUCTION)
		    });
		    XposedHelpers.findAndHookConstructor(FileOutputStream.class.getName(), classLoader, new Object[]{
		    	String.class.getName(),new My_XC_MethodHook(packageName, "", MethodInt.FILE_WRITER_CONSTRUCTION)
		    });
		    XposedHelpers.findAndHookConstructor(FileOutputStream.class.getName(), classLoader, new Object[]{
		    	File.class.getName(),Boolean.TYPE.getName(),new My_XC_MethodHook(packageName, "", MethodInt.FILE_WRITER_CONSTRUCTION)
		    });
		    XposedHelpers.findAndHookConstructor(FileOutputStream.class.getName(), classLoader, new Object[]{
		    	String.class.getName(),Boolean.TYPE.getName(),new My_XC_MethodHook(packageName, "", MethodInt.FILE_WRITER_CONSTRUCTION)
		    });
		    /********************************************************************/
		    if(!packageName.equals("cn.ninegame.gamemanager")){
		    	HookMethod(File.class, "mkdir", packageName, MethodInt.FILE_MKDIR);
			    HookMethod(File.class, "mkdirs", packageName, MethodInt.FILE_MKDIRS);
		    }
			HookMethod(File.class, "createNewFile", packageName, MethodInt.FILE_RECORD);
			HookMethod(File.class, "renameTo", packageName, MethodInt.FILE_REANME_TO, File.class);
//			监听系统值操作
			SystemVauleHook(MethodInt.PUT_STRING, packageName, MethodInt.SYSTEM_VALUE_PUT_STRING, ContentResolver.class,String.class, String.class);
			SystemVauleHook(MethodInt.GET_STRING, packageName, MethodInt.SYSTEM_VALUE_GET_STRING, ContentResolver.class,String.class);
			
			SystemVauleHook(MethodInt.PUT_INT, packageName, MethodInt.SYSTEM_VALUE_PUT_INT, ContentResolver.class,String.class, int.class);
			SystemVauleHook(MethodInt.GET_INT, packageName, MethodInt.SYSTEM_VALUE_GET_INT_2, ContentResolver.class,String.class);
			SystemVauleHook(MethodInt.GET_INT, packageName, MethodInt.SYSTEM_VALUE_GET_INT_3, ContentResolver.class,String.class, int.class);
	}
	@Override
	protected void after(String packageName, MethodHookParam param, int type)throws Exception
		{
		try {
			Object obj = param.thisObject;
			switch (type) {
			case MethodInt.SYSTEM_VALUE_PUT_STRING:
				XposedParamHelpUtil.saveSystemValue(param, packageName,MethodInt.PUT_STRING);
				break;
			case MethodInt.SYSTEM_VALUE_GET_STRING:
				XposedParamHelpUtil.saveSystemValue(param, packageName,MethodInt.GET_STRING);
				break;
			case MethodInt.SYSTEM_VALUE_PUT_INT:
				XposedParamHelpUtil.saveSystemValue(param, packageName,MethodInt.PUT_INT);
				break;
			case MethodInt.SYSTEM_VALUE_GET_INT_2:
				XposedParamHelpUtil.saveSystemValue(param, packageName,MethodInt.GET_INT);
				break;
			case MethodInt.SYSTEM_VALUE_GET_INT_3:
				XposedParamHelpUtil.saveSystemValue(param, packageName,MethodInt.GET_INT);
				break;
			case MethodInt.FILE_RECORD:
				Object objC = param.thisObject;
				if(objC!=null&&!TextUtils.isEmpty(objC.toString())){
					String pathC = objC.toString();
					FileSysOptRecordToFile.saveFilePath(packageName, pathC, null);
//					Logger.t("file:==CreateFile=:"+pathC);
				}
				break;
			case MethodInt.FILE_MKDIR:
				Object objM = param.thisObject;
				if(objM!=null&&!TextUtils.isEmpty(objM.toString())){
					String pathM = objM.toString();
//					if(!pathM.substring(pathM.lastIndexOf("/")+1).startsWith(".")){
						FileSysOptRecordToFile.saveFilePath(packageName, pathM, null);
//					}
					Logger.t("file:==FILE_MKDIR=:"+pathM+"    "+pathM.substring(pathM.lastIndexOf("/")+1));
				}
				break;
			case MethodInt.FILE_MKDIRS:
				Object objMS = param.thisObject;
				if(objMS!=null&&!TextUtils.isEmpty(objMS.toString())){
					String pathMS = objMS.toString();
//					if(!pathMS.substring(pathMS.lastIndexOf("/")+1).startsWith(".")){
						FileSysOptRecordToFile.saveFilePath(packageName, pathMS, null);
//					}
					Logger.t("file:==FILE_MKDIRS=:"+pathMS+"    "+pathMS.substring(pathMS.lastIndexOf("/")+1));
				}
				break;
			case MethodInt.FILE_REANME_TO:
				Object objR = param.thisObject;
				Object[] argsR = param.args;
				if(argsR.length==1){
					String pathR = argsR[0].toString();
					FileSysOptRecordToFile.saveFilePath(packageName, pathR, null);
//					Logger.t("file:==FILE_REANME_TO=:"+pathR);
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Logger.h("Sys====Ex:" + e);
			e.printStackTrace();
		}
		
		
	}

	

}
