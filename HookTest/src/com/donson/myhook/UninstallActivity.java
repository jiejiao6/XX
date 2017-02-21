package com.donson.myhook;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.donson.myhook.adapters.AppInfosAdapter;
import com.donson.myhook.bean.AppInfosMode;
import com.donson.utils.AppInfosUtil;
import com.donson.utils.CmdUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.SendBroadCastUtil;
import com.donson.xxxiugaiqi.R;

public class UninstallActivity extends BaseActivity {
	private ListView lv_list;
	public ArrayList<AppInfosMode> mAppList;
	private AppInfosAdapter mSelectAppAdapter;
	public PackageManager mPm;
	Button btn_clear;
	private String packageName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_app);
		lv_list = (ListView) findViewById(R.id.lv_list);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_clear.setVisibility(View.GONE);
		getInstallApp();
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppInfosMode packageInfoMode = mAppList.get(position);
				showDialog(packageInfoMode);
			}
		});
	}
	AlertDialog dialog;
	protected void showDialog(final AppInfosMode packageInfoMode) {
		final String new_packageName = AppInfosUtil.getPackageName(packageInfoMode);
		
//		View view = getAppInfoView(packageInfoMode, mPm, UninstallActivity.this);
		Builder builder = new AlertDialog.Builder(UninstallActivity.this, AlertDialog.THEME_HOLO_LIGHT);
		dialog = builder.create();
		dialog.setTitle(getResources().getString(R.string.uninstall_please_confirm_uninstall));
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.btn_ok), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
//				CmdUtil.unInstallApk(new_packageName);
				if(CmdUtil.unInstallApk(new_packageName)){
					ArrayList<AppInfosMode> mAppList2 = AppInfosUtil.getInstallApp(UninstallActivity.this);
					mAppList.clear();
					mAppList.addAll(mAppList2);
					mSelectAppAdapter.notifyDataSetChanged();
					showToast(getResources().getString(R.string.uninstall_ok));
				} else {
					showToast(getResources().getString(R.string.uninstall_err));
				}
			}
		});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE,getString(R.string.btn_cancel), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
//		dialog.setView(view);
		builder.setNegativeButton(getResources().getString(R.string.cancel), null);
		dialog.show();
	}
	public static View getAppInfoView(AppInfosMode packageInfoMode, PackageManager mPm, Context context) {
		CharSequence name = AppInfosAdapter.getName(packageInfoMode, mPm);
		View view = View.inflate(context, R.layout.item_select_app, null);
		ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
		TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
		tv_text.setText(name);
		AppInfosAdapter.setImage(iv_image, packageInfoMode, mPm);
		return view;
	}
	private void getInstallApp() {
		mAppList = AppInfosUtil.getInstallApp(UninstallActivity.this);
		mSelectAppAdapter = new AppInfosAdapter(UninstallActivity.this, mAppList, true, false);
		lv_list.setAdapter(mSelectAppAdapter);
	}

}
