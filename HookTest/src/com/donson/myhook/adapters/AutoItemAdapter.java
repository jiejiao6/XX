package com.donson.myhook.adapters;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.donson.config.Logger;
import com.donson.myhook.bean.OptAppMode;
import com.donson.utils.AppInfosUtil;
import com.donson.utils.GetPackageSizeUtil;
import com.donson.xxxiugaiqi.R;

public class AutoItemAdapter extends BaseAdapter{
	
	List<OptAppMode> list;
	PackageManager pm;
	Context context;
	LayoutInflater inflater;
	public AutoItemAdapter(Context context,List<OptAppMode> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		pm = context.getPackageManager();
	}
	public void setLsit(List<OptAppMode> list){
		this.list = list;
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
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_auto_opt, null);
			holder.icon = (ImageView) convertView.findViewById(R.id.iv_auto_icon);
			holder.app = (TextView) convertView.findViewById(R.id.tv_auto_appname);
			holder.packageName = (TextView) convertView.findViewById(R.id.tv_auto_packagename);
			holder.tips1 = (TextView) convertView.findViewById(R.id.tv_auto_tips1);
			holder.tips2 = (TextView) convertView.findViewById(R.id.tv_auto_tips2);
			holder.tips3 = (TextView) convertView.findViewById(R.id.tv_auto_tips3);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		OptAppMode optAppMode = list.get(position);
		Drawable icon = AppInfosUtil.getAppIconByPackageInfo(optAppMode.getPackageInfo(), pm);
		if(icon!=null){
			holder.icon.setImageDrawable(icon);
		}
		String app = AppInfosUtil.getAppNameByPackageInfo(optAppMode.getPackageInfo(), pm);
		holder.app.setText(app);
		holder.packageName.setText(optAppMode.getPackageInfo().packageName);
		switch (optAppMode.getFlag()) {
		case OptAppMode.FLAG_UNINSTALL:
			holder.tips1.setVisibility(View.VISIBLE);
			holder.tips2.setVisibility(View.VISIBLE);
			holder.tips1.setText("");
			holder.tips2.setText("");
			holder.tips3.setText(context.getString(R.string.auto_uninstall_apk_ing));
			break;
		case OptAppMode.FLAG_INSTALL:
			holder.tips1.setVisibility(View.VISIBLE);
			holder.tips2.setVisibility(View.VISIBLE);
			holder.tips1.setText("");
			holder.tips2.setText("");
			holder.tips3.setText(context.getString(R.string.auto_install_apk));
			break;
		case OptAppMode.FLAG_CLOSE:
			holder.tips1.setVisibility(View.VISIBLE);
			holder.tips2.setVisibility(View.VISIBLE);
			holder.tips1.setText("");
			holder.tips2.setText("");
			holder.tips3.setText(context.getString(R.string.auto_close_apk_ing));
			break;
		case OptAppMode.FLAG_CLEAR:
			holder.tips2.setVisibility(View.VISIBLE);
			holder.tips1.setVisibility(View.GONE);
			holder.tips2.setText(GetPackageSizeUtil.formatFileSize(optAppMode.getDataSize())/*String.format(context.getString(R.string.auto_clear_apk_data_size), optAppMode.getDataSize())*/);
			holder.tips3.setText(context.getString(R.string.auto_clear_apk_data_ing));
			break;
		case OptAppMode.FLAG_SYS_OPT:
			holder.tips2.setVisibility(View.VISIBLE);
			holder.tips1.setVisibility(View.VISIBLE);
			holder.tips1.setText(String.format(context.getString(R.string.auto_recover_sys_count),optAppMode.getSysCount()));
			if(optAppMode.getSysFailCount()!=-1){
				holder.tips2.setTextColor(context.getResources().getColorStateList(R.color.red));
				holder.tips2.setText(String.format(context.getString(R.string.auto_recover_sys_err_count), optAppMode.getSysFailCount()));
				holder.tips3.setText(context.getString(R.string.auto_recover_sys_ok));
			}else {
				holder.tips2.setVisibility(View.GONE);
				holder.tips3.setText(context.getString(R.string.auto_recover_sys_ing));
			}
			break;
		case OptAppMode.FLAG_DELETE_FILE:
			holder.tips2.setVisibility(View.GONE);
			holder.tips1.setVisibility(View.VISIBLE);
			if (optAppMode.getFileErrSize() == -1) {
				holder.tips1.setText(context.getString(R.string.auto_delete_file_ing));
			}else if (optAppMode.getFileErrSize() == 0) {
				holder.tips1.setText(context.getString(R.string.auto_delete_file_ok));
			}else if (optAppMode.getFileErrSize() > 0) {
				holder.tips1.setText(String.format(context.getString(R.string.auto_delete_file_err_count), optAppMode.getFileErrSize()));
			}
			if(optAppMode.getFolderErrSize() == -1){
				holder.tips3.setText(context.getString(R.string.auto_delete_folder_ing));
			}else if(optAppMode.getFolderErrSize() == 0){
				holder.tips3.setText(context.getString(R.string.auto_delete_folder_ok));
			}else if (optAppMode.getFolderErrSize() > 0) {
				holder.tips3.setText(String.format(context.getString(R.string.auto_delete_folder_err_count), optAppMode.getFolderErrSize()));
			}
			break;
		case OptAppMode.FLAG_VPN:
			holder.tips1.setText("");
			holder.tips2.setText("");
			holder.tips3.setText(context.getString(R.string.auto_connect_vpn));
			break;
		default:
			holder.tips1.setText("");
			holder.tips2.setText("");
			holder.tips3.setText(context.getString(R.string.auto_param_generation_ing));
			break;
		}
		return convertView;
	}
	class ViewHolder{
		ImageView icon;
		TextView app;
		TextView packageName;
		TextView tips1;
		TextView tips2;
		TextView tips3;
	}

}
