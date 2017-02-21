package com.donson.operation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.utils.CmdUtil;
import com.donson.utils.MyfileUtil;

public class UnInstallRecordApks {
	public UnInstallRecordApks(Context context) {
		// TODO Auto-generated constructor stub
	}
	public void unInstallAllApks(){
		String source=MyfileUtil.readFileFromSDCard(ConstantsHookConfig.PATH_RECORD_APK_INSTALL);
		Set<String> packages = new HashSet<String>();
		packages.addAll(Arrays.asList(source));
		for(String pac:packages){
			Logger.i("unInstallAllApks:::"+pac);
			CmdUtil.unInstallApk(pac);
		}
	}
}
