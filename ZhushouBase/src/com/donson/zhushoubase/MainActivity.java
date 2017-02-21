package com.donson.zhushoubase;

import java.util.ArrayList;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.donson.receivecode.HttpCallBackListener;
import com.donson.receivecode.Item;
import com.donson.receivecode.LoginInfo;
import com.donson.receivecode.ReceiveCode;

public class MainActivity extends Activity implements OnClickListener, HttpCallBackListener {

	private boolean isLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("Test_case");
		registerReceiver(new TestBroadcastReceiver(), myIntentFilter);

		findViewById(R.id.button1).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// Http.get("http://sfeng.08tk.cn/sd/jc.php", null);
		ReceiveCode receiveCode = ReceiveCode.getReceiveCode();
		receiveCode.setListener(this);
		if (!isLogin)
			receiveCode.login();
		else
			receiveCode.getItems();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(Object response) {
		TextView textView = (TextView) findViewById(R.id.textView1);
		if (response instanceof LoginInfo) {
			isLogin = true;
			LoginInfo info = (LoginInfo) response;
			textView.setText(info.getToken());
		} else if (response instanceof ArrayList<?>) {
			StringBuilder sb = new StringBuilder();
			for (Item i : (ArrayList<Item>)response) {
				sb.append(i.getName());
				sb.append("\n");
			}
			textView.setText(sb.toString());
		}
	}

	@Override
	public void onEror(Exception e) {
		e.printStackTrace();
	}
}
