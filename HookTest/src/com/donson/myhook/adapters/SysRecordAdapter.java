package com.donson.myhook.adapters;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Xposed.XposedParamHelpUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.donson.controller.SysOptListenViewController;
import com.donson.xxxiugaiqi.R;

public class SysRecordAdapter extends BaseAdapter {
	List<String> list;
	Context context;
	LayoutInflater inflater;
	SysOptListenViewController viewController;
	public SysRecordAdapter(Context context,SysOptListenViewController viewController,List<String> list) {
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
		String text = list.get(position);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(text);
			String method = jsonObject.optString(XposedParamHelpUtil.KEY_METHOD);
			String args = jsonObject.optJSONArray(XposedParamHelpUtil.KEY_ARGS).toString();
			String result= jsonObject.optString(XposedParamHelpUtil.KEY_RESULT);
			holder.tvContent.setText(String.format(context.getString(R.string.sys_format), method,args,result));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return convertView;
	}
	class ViewHolder{
		TextView tvLine;
		TextView tvContent;
	}

}
