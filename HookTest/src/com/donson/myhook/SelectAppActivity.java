package com.donson.myhook;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.donson.config.ConstantsHookConfig;
import com.donson.myhook.adapters.AppInfosAdapter;
import com.donson.myhook.bean.AppInfosMode;
import com.donson.utils.AppInfosUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.SendBroadCastUtil;
import com.donson.xxxiugaiqi.R;

public class SelectAppActivity extends BaseActivity {
	private ListView lv_list;
	public ArrayList<AppInfosMode> mAppList;
	private AppInfosAdapter mSelectAppAdapter;
	public PackageManager mPm;
	Button btn_clear;
	private String packageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_app);
		lv_list = (ListView) findViewById(R.id.lv_list);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		getInstallApp();
		setListner();
		mPm = getPackageManager();
		packageName = SPrefHookUtil.getSettingStr(SelectAppActivity.this, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
		if (TextUtils.isEmpty(packageName))
			btn_clear.setVisibility(View.GONE);
	}
	private void setListner() {
		lv_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AppInfosMode packageInfoMode = mAppList.get(position);
				showDialog(packageInfoMode);
			}
		});
		btn_clear.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showClearListnerDialog();
			}
		});

	}

	AlertDialog dialog;

	protected void showDialog(final AppInfosMode packageInfoMode) {
		final String new_packageName = AppInfosUtil.getPackageName(packageInfoMode);
		if (new_packageName.equals(packageName)) {
			showToast(getResources().getString(R.string.no_need_to_reListen));
			return;
		}
		View view = getAppInfoView(packageInfoMode, mPm, SelectAppActivity.this);
		Builder builder = new AlertDialog.Builder(SelectAppActivity.this, AlertDialog.THEME_HOLO_LIGHT);
		dialog = builder.create();
		dialog.setTitle(getResources().getString(R.string.please_affirm_listen_app));
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.btn_ok), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				boolean commit = SPrefHookUtil.putSettingStr(SelectAppActivity.this,
						SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME, new_packageName);
				if (commit) {
					SendBroadCastUtil.listenApp(/*ConstantsHookConfig.FLAG_NEW_LISTNER_APP,*/ SelectAppActivity.this,false);
					/*Intent intent = new Intent();
					intent.setAction(ConstantsHookConfig.ACTION_LISTNER_APP);
					intent.putExtra(ConstantsHookConfig.FLAG, ConstantsHookConfig.FLAG_NEW_LISTNER_APP);
					Bundle bundle = new Bundle();
					bundle.putParcelable(ConstantsHookConfig.KEY_SELECT_APP, packageInfoMode);
					intent.putExtras(bundle);
					sendBroadcast(intent);*/
					finish();
				} else {
					showToast(getResources().getString(R.string.opera_fail));
				}

			}
		});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE,getString(R.string.btn_cancel), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		dialog.setView(view);
		builder.setNegativeButton(getResources().getString(R.string.cancel), null);
		dialog.show();
	}

	AlertDialog dialog2;

	protected void showClearListnerDialog() {
		Builder builder2 = new AlertDialog.Builder(SelectAppActivity.this, AlertDialog.THEME_HOLO_LIGHT);
		dialog2 = builder2.create();
		dialog2.setTitle(getResources().getString(R.string.please_affirm_clear_listen_app));
		dialog2.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.affirm),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						boolean commit = SPrefHookUtil.putSettingStr(SelectAppActivity.this, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME,"");
						if (commit) {
							SendBroadCastUtil.listenApp(/*ConstantsHookConfig.FLAG_CLEAR_LISTNER_APP,*/ SelectAppActivity.this,false);
							finish();
						} else {
							showToast(getResources().getString(R.string.opera_fail));
						}
					}
				});
		dialog2.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		dialog2.show();
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

	public static View getAppInfoView(PackageInfo packageInfo, PackageManager mPm, Context context) {
		CharSequence name = AppInfosAdapter.getName(packageInfo, mPm);
		View view = View.inflate(context, R.layout.item_select_app, null);
		ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
		TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
		tv_text.setText(name);
		AppInfosAdapter.setImage(iv_image, packageInfo, mPm);
		return view;
	}

	private void getInstallApp() {
		mAppList = AppInfosUtil.getInstallApp(SelectAppActivity.this);
		mSelectAppAdapter = new AppInfosAdapter(SelectAppActivity.this, mAppList, true, false);
		lv_list.setAdapter(mSelectAppAdapter);
	}

}
