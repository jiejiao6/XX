package Xposed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;

import android.text.TextUtils;

import com.donson.config.Logger;
import com.donson.utils.MyfileUtil;
import com.param.config.ConstantsConfig;
import com.param.utils.FileUtil;

public class FileSysOptRecordToFile {
	public static String APP_RECORD_FOLDER = ConstantsConfig.EXTRA_PATH;
	public static String RECORD_FILE_OPT_PATH = APP_RECORD_FOLDER+ File.separator + "file_re.txt";
	public static String RECORD_SYSTEM_OPT_PATH = APP_RECORD_FOLDER+ File.separator + "system_re.txt";
//	public static String RECORD_FILE_OPT_NAME = "record.txt";

	// public static String GDT_DOWN_PATH =
	// Environment.getExternalStorageDirectory() + File.separator +
	// GlobalConstant.DOWN_GDTDOWNLOAD;
	/*
	 * Environment.getExternalStorageDirectory() + File.separator + "007" +
	 * File.separator + "file"; public static String GDT_DOWN_PATH =
	 * Environment.getExternalStorageDirectory() + File.separator +
	 * GlobalConstant.DOWN_GDTDOWNLOAD; public static HashMap<String, Integer>
	 * appPathMap; public static HashMap<String, Integer> filePathMap; public
	 * static String foldPath = getFoldPath(GlobalConstant.FOLDER_FILE_PATH);
	 */

	public static void saveFilePath(String packageName, final String path, final Object[] args) {
		new Thread(new Runnable() {

			@Override
			public void run() {
//				Logger.f("saveFilePath========" + path);
				if (TextUtils.isEmpty(path)
						|| path.contains(APP_RECORD_FOLDER)
						/*|| path.equals(APP_RECORD_FOLDER)
						|| ((path.length() >= APP_RECORD_FOLDER.length()) && path
								.substring(0, APP_RECORD_FOLDER.length())
								.equals(APP_RECORD_FOLDER))*/) {
					return;
				}
				Logger.f("saveFilePath========" + path+"  ");
				if(path.contains("ucgamesdk")){
					if(args.length>=2){
						Logger.t2("saveFilePath=22=======" + path+"  "+args[0]+args[1]);
					}else if (args.length==1) {
						Logger.t2("saveFilePath==1======" + path+"  "+args[0]);
					}
					
				}
				appendStringToFile(RECORD_FILE_OPT_PATH, path);
//				recordFileOperationPath(path);
			}
		}).start();

	}

	/**
	 * 
	 * @param path
	 *//*
	public static void recordFileOperationPath(String path) {
		RandomAccessFile accessFile = null;
		try {
			File dir = FileUtil.checkDir();
			File file = new File(dir, RECORD_FILE_OPT_NAME);
			if (!file.exists()) {
				file.createNewFile();
			}
			accessFile = new RandomAccessFile(RECORD_FILE_OPT_PATH, "rw");
			accessFile.seek(accessFile.length());
			accessFile.writeBytes(path + "\n");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/

	public static Set<String> getAllrecordedFile(String listenPacName) {
		return getSetString(listenPacName, RECORD_FILE_OPT_PATH,FLAG_FILE);
	}
	public static Set<String> getSysStringset(String listenPackage){
		return getSetString(listenPackage, RECORD_SYSTEM_OPT_PATH,FLAGE_SYS);
	}
	
	public final static int FLAG_FILE = 0;
	public final static int FLAGE_SYS = 1;
	public static Set<String> getSetString(String listenPacName,String Filepath,int flag) {
		Set<String> set = new HashSet<String>();
		File file = new File(Filepath);
		FileUtil.checkDir(file.getParentFile());
		BufferedReader reader = null;
		if (file.exists()) {
			try {
				reader = new BufferedReader(new FileReader(file));
				String tmp = null;
				while ((tmp = reader.readLine()) != null) {
					switch (flag) {
					case FLAG_FILE:
						if (MyfileUtil.isExternalStorageFile(tmp)
								|| MyfileUtil.isDataStorageFile(tmp, listenPacName)) {
							File tmpFile = new File(tmp);
							if (tmpFile.exists()) {
								set.add(tmp);
							}
						}
						break;
					case FLAGE_SYS:
						set.add(tmp);
						break;
					default:
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (reader != null) {
						reader.close();
						reader = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return set;
	}
	/**
	 * @param packageName
	 * @param hashMap
	 */
	public static void saveSystemOpt(String packageName,final String result) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				appendStringToFile(RECORD_SYSTEM_OPT_PATH, result);
			}
		}).start();
	}

	protected static void appendStringToFile(String filePath,String string) {
		File file = new File(filePath);
		FileUtil.checkDir(file.getParentFile());
		RandomAccessFile accessFile = null;
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			accessFile = new RandomAccessFile(file, "rw");
			accessFile.seek(file.length());
			accessFile.writeBytes(string + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(accessFile!=null){
					accessFile.close();
					accessFile = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
