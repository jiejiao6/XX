package com.donson.myhook.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.donson.controller.FileOptListenViewController;
import com.donson.utils.MyfileUtil;
import com.donson.xxxiugaiqi.R;

public class FileRecordAdapter extends BaseAdapter {
	List<String> list;
	Context context;
	LayoutInflater inflater;
	FileOptListenViewController viewController;
	public FileRecordAdapter(Context context,FileOptListenViewController viewController,List<String> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.viewController = viewController;
	}
	public void setList(List<String> list){
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
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_textview, null);
			holder.tvLine = (TextView) convertView.findViewById(R.id.tv_line);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_text_view);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvLine.setText(position+".");
		String path = list.get(position);
		boolean isExternal = MyfileUtil.isExternalStorageFile(path);
		String listenPackage = viewController.getListenPackageName();
		boolean isData = MyfileUtil.isDataStorageFile(path, listenPackage);
		if(isExternal){
			holder.tvContent.setTextColor(context.getResources().getColorStateList(R.color.red));
		}else if (isData) {
			holder.tvContent.setTextColor(context.getResources().getColorStateList(R.color.green01));
		}
		else{
			holder.tvContent.setTextColor(context.getResources().getColorStateList(R.color.black));
		}
		holder.tvContent.setText(path);
		return convertView;
	}
	class ViewHolder{
		TextView tvLine;
		TextView tvContent;
	}

}
