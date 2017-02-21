package com.donson.myhook;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.donson.config.Logger;
import com.donson.controller.ChangeWhiteListController;
import com.donson.myhook.adapters.AppInfosAdapter;
import com.donson.myhook.bean.AppInfosMode;
import com.donson.utils.AppInfosUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.viewinterface.ChangeWhiteListViewInterface;
import com.donson.xxxiugaiqi.R;

public class ChangeWhiteListActivity extends BaseActivity implements ChangeWhiteListViewInterface,View.OnClickListener{
	private final static int FLAG_ADD_WHITE = 0;
	private final static int FLAG_REMOVE_WHITE = 1;
	private final static int FLAG_WHITE = 0;
	private final static int FLAG_ALL = 1;
	private Button btnWhiteList,btnChooseApp;
	private ListView lvChangeWhiteList;
	private ChangeWhiteListController controller = null;
	private AppInfosAdapter adapter = null;
	private List<AppInfosMode> mBaiAppList = null;
	private List<AppInfosMode> mAllAppList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chang_whitelist);
		initdata();
		btnWhiteList = (Button)findViewById(R.id.btn_whitelist);
		btnChooseApp = (Button)findViewById(R.id.btn_choose_app);
		lvChangeWhiteList = (ListView)findViewById(R.id.lv_whitelist);
		adapter = new AppInfosAdapter(this, mBaiAppList, false, true);
		lvChangeWhiteList.setAdapter(adapter);
		btnWhiteList.setSelected(true);
		controller = new ChangeWhiteListController(this,this);
		initRect();
		setEvent();
	}
	private void initRect() {
		initTopBar();
		setTopTitle(getString(R.string.btn_change_white_list));
		setLeftBtnBack();
	}
	private void initdata() {
		String changeList = SPrefHookUtil.getSettingStr(getApplicationContext(), SPrefHookUtil.KEY_SETTING_CHANG_WHITE_LIST);
		mBaiAppList = AppInfosUtil.getWhiteList(this, changeList);
		mAllAppList = AppInfosUtil.getInstallApp(this);
		mAllAppList.removeAll(mBaiAppList);
	}
	
	private void setEvent() {
		btnChooseApp.setOnClickListener(this);
		btnWhiteList.setOnClickListener(this);
		lvChangeWhiteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				controller.handleOnItemClick(parent,view,position,id);
			}
		});
	}
	
	@Override
	public void notifyAllAppList(){
		adapter.setData(mAllAppList);
		adapter.notifyDataSetChanged();
	}
	@Override
	public void notifyWhiteList(){
		adapter.setData(mBaiAppList);
		adapter.notifyDataSetChanged();
	}
	@Override
	public void updateMAppList(int flag,AppInfosMode appInfosMode,int location){
		switch (flag) {
		case FLAG_ADD_WHITE:
			mBaiAppList.add(appInfosMode);
			mAllAppList.remove(location);
			break;
		case FLAG_REMOVE_WHITE:
			mAllAppList.add(appInfosMode);
			mBaiAppList.remove(location);
		default:
			break;
		}
	}
	@Override
	public List<AppInfosMode> getAppInfoList(int flag){
		switch (flag) {
		case FLAG_WHITE:
			return mBaiAppList;
		case FLAG_ALL:
			return mAllAppList;
		}
		return null;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_choose_app:
			btnChooseApp.setSelected(true);
			btnWhiteList.setSelected(false);
			notifyAllAppList();
			break;
		case R.id.btn_whitelist:
			btnChooseApp.setSelected(false);
			btnWhiteList.setSelected(true);
			notifyWhiteList();
			break;
		default:
			break;
		}
	}
}
