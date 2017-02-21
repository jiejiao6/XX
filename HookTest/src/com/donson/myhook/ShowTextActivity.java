package com.donson.myhook;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.donson.utils.MyfileUtil;
import com.donson.xxxiugaiqi.R;
import com.mz.annotation.ContentViewInject;
import com.mz.annotation.InjectUtils;
import com.mz.annotation.ViewInject;

@ContentViewInject(R.layout.activity_show_text)
public class ShowTextActivity extends BaseActivity {
	@ViewInject(R.id.tv_text)
	TextView text;

	@ViewInject(R.id.btn_delete_file)
	Button delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		InjectUtils.injectAll(this);
		Intent intent = getIntent();
		final String path = intent.getStringExtra("path");
		String texts = MyfileUtil.readFileFromSDCard(path);
		text.setText("" + texts);

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File file = new File(path);
				file.delete();
				text.setText("");
			}
		});
	}

}
