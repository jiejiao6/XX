package com.donson.myhook.adapters;

import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.donson.myhook.bean.DataMode;
import com.donson.xxxiugaiqi.R;

public class ParamAdapter extends BaseAdapter {
	private Context context;
	private List<DataMode> list;
	private LayoutInflater inflater;
	public ParamAdapter(Context context,List<DataMode> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	public void setList(List<DataMode> list){
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_param, null);
			holder = new ViewHolder();
			holder.id = (TextView) convertView.findViewById(R.id.tv_id);
			holder.key = (TextView) convertView.findViewById(R.id.tv_key);
			holder.value = (TextView) convertView.findViewById(R.id.tv_vaule);
			holder.explain = (TextView) convertView.findViewById(R.id.tv_explain);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		DataMode dataMode = list.get(position);
		holder.id.setText(position+".");
		holder.key.setText(dataMode.getKey());
		if(!dataMode.isChanged()){
			ColorStateList colors = context.getResources().getColorStateList(R.color.grey2);
			holder.key.setTextColor(colors);
		}
		holder.value.setText(dataMode.getValue());
		if(TextUtils.isEmpty(dataMode.getExplain())){
			holder.explain.setVisibility(View.GONE);
		}else {
			holder.explain.setVisibility(View.VISIBLE);
			holder.explain.setText(dataMode.getExplain());
		}
		
		return convertView;
	}
	class ViewHolder{
		TextView id;
		TextView key;
		TextView value;
		TextView explain;
	}

}
