package com.donson.myhook.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.donson.controller.LeftListController;
import com.donson.utils.AppInfosUtil;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.xxxiugaiqi.R;

public class LeftListAdapter extends BaseAdapter {
	List<String> list;
	LayoutInflater inflater;
	Activity context;
	public LeftListAdapter(Activity context, List<String> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_textview, null);
			holder.text = (TextView) convertView
					.findViewById(R.id.tv_text_center_view);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text.setVisibility(View.VISIBLE);
		holder.checkBox.setVisibility(View.VISIBLE);
		holder.checkBox.setFocusable(false);
		holder.checkBox.setClickable(false);
		holder.text.setText(list.get(position) + "");
		switch (position) {
		case LeftListController.GLOBAL_CHANGE:
			holder.checkBox.setChecked(SPrefHookUtil.getSettingBoolean(context, SPrefHookUtil.KEY_SETTING_GLOBAL_CHANGEDE, SPrefHookUtil.D_SETTING_GLOBAL_CHANGEDE));
			break;
		case LeftListController.CHANGE_DENSITY:
			holder.checkBox.setChecked(SPrefHookUtil.getSettingBoolean(context, SPrefHookUtil.KEY_SETTING_DENSITY_CHEANGE, SPrefHookUtil.D_SETTING_DENSITY_CHEANGE));
			break;
		case LeftListController.OPEN_APP:
			holder.checkBox.setChecked(SPrefHookUtil.getSettingBoolean(context, SPrefHookUtil.KEY_SETTING_OPEN_APK, SPrefHookUtil.D_SETTING_OPEN_APK));
			break;
		case LeftListController.UNINSTALL_APP:
			holder.checkBox.setChecked(SPrefHookUtil.getSettingBoolean(context, SPrefHookUtil.KEY_SETTING_UNINSTALL_APK, SPrefHookUtil.D_SETTING_UNINSTALL_APK));
			break;
		case LeftListController.NETDEBUG:
			holder.checkBox.setChecked(SPrefHookUtil.getSettingBoolean(context, SPrefHookUtil.KEY_SETTING_NET_DEBUG, SPrefHookUtil.D_SETTING_NET_DEBUG));
			break;
		case LeftListController.CONTINUOUS:
			holder.checkBox.setChecked(SPrefHookUtil.getTaskBoolean(context, SPrefHookUtil.KEY_TASK_CONTINUOUS, SPrefHookUtil.D_TASK_CONTINUOUS));
			break;
		case LeftListController.COUNT_LIMIT:
			holder.checkBox.setChecked(SPrefHookUtil.getSettingBoolean(context, SPrefHookUtil.KEY_SETTING_TOTAL_TIMES_LIMIT, SPrefHookUtil.D_SETTING_TOTAL_TIMES_LIMIT));
			break;
//		case LeftListController.LIUCUN_SET:
//		case LeftListController.VPN_SET:
//		case LeftListController.CHANGE_WHIT_LIST:
//		case LeftListController.SCRIPT_SET:
//		case LeftListController.FILE_OPT:
//		case LeftListController.SYS_OPT:
//		case LeftListController.WIFI_SIM_PERCENT_SET:
		case LeftListController.RANDOM_PARAM:
//		case LeftListController.INSTALL_MANUAL:
			holder.checkBox.setVisibility(View.INVISIBLE);
			break;
		case LeftListController.CHECK_VERSION:
			holder.checkBox.setVisibility(View.INVISIBLE);
			holder.text.setText(list.get(position) + ""+AppInfosUtil.getVersion(context));
			break;
		case LeftListController.MARKET:
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.checkBox.setChecked(EasyClickUtil.getMarketHookFlag(context));
			break;
		default:
			holder.checkBox.setVisibility(View.INVISIBLE);
			break;
		}
		return convertView;
	}

	public class ViewHolder {
		public TextView text;
		public CheckBox checkBox;
	}

}
