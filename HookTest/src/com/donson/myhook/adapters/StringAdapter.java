package com.donson.myhook.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.donson.xxxiugaiqi.R;

public class StringAdapter extends BaseAdapter {
	Context context;
	List<String> list;
	LayoutInflater inflater;
	public StringAdapter(Context context,List<String> list) {
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
		TextView textView = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_textview, null);
			textView = (TextView) convertView.findViewById(R.id.tv_text_view);
			convertView.setTag(textView);
		}else {
			textView = (TextView) convertView.getTag();
		}
		textView.setText(list.get(position)+"");
		
		return convertView;
	}

}
