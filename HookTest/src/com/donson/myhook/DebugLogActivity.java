package com.donson.myhook;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.myhook.adapters.StringAdapter;
import com.donson.utils.MyfileUtil;
import com.donson.utils.OpenActivityUtil;
import com.donson.xxxiugaiqi.R;
import com.mz.annotation.ContentViewInject;
import com.mz.annotation.InjectUtils;
import com.mz.annotation.ViewInject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@ContentViewInject(R.layout.activity_show_debug_log)
public class DebugLogActivity extends BaseActivity {
	@ViewInject(R.id.files)
	ListView logs;
	public static int LOGFLAG_ERR = 0;
	public static int LOGFLAG_UPLOAD = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		InjectUtils.injectAll(this);
		initRect();
		initText();
	}
	private void initRect() {
		initTopBar();
		setTopTitle(getString(R.string.title_upload_err_log));
		setLeftBtnBack();
	}
	private void initText() {
			final File dir = new File(ConstantsHookConfig.PATH_RECORD_UPLOAD_ERR_LOG);
			if(dir.exists()){
				
				dir.listFiles();
				String[] files = dir.list();
				Logger.i("files::"+files+"  "+files.length);
				List<String> fileLists = Arrays.asList(files);
				StringAdapter adapter = new StringAdapter(getApplicationContext(), fileLists);
				logs.setAdapter(adapter);
				logs.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						String path1 = (String) parent.getItemAtPosition(position);
						String path = dir.getAbsolutePath()+"/"+path1;
						OpenActivityUtil.openTextActivity(DebugLogActivity.this,path);
					}
				});
			}
	}
}
