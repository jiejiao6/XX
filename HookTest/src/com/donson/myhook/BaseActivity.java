package com.donson.myhook;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.donson.config.Logger;
import com.donson.utils.StringUtil;
import com.donson.xxxiugaiqi.R;

public class BaseActivity extends Activity {
	protected ImageView topLeftBtn;
	protected ImageView topRightBtn;
	protected TextView topTitleTxt;
	protected TextView topLeftTitleTxt;
	protected TextView topRightTitleTxt;
	
//	protected ViewGroup topContentView;
	protected ViewGroup topBar;
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		topContentView=(ViewGroup)LayoutInflater.from(this).inflate(R.layout.common_bar,null);
//		ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
//		topContentView.setLayoutParams(params);
		
	};
	public void initTopBar() {
		topBar = (ViewGroup) findViewById(R.id.topbar);
		topTitleTxt = (TextView) findViewById(R.id.base_fragment_title);
		topLeftTitleTxt = (TextView) findViewById(R.id.left_text);
		topLeftBtn = (ImageView) findViewById(R.id.left_image);
		
		topRightTitleTxt = (TextView) findViewById(R.id.right_text);
		topRightBtn = (ImageView) findViewById(R.id.right_image);
		topLeftBtn.setVisibility(View.GONE);
		topLeftTitleTxt.setVisibility(View.GONE);
		topRightBtn.setVisibility(View.GONE);
		topRightTitleTxt.setVisibility(View.GONE);
	}
	/**
	 * set and show Title
	 * @param title
	 */
	protected void setTopTitle(String title) {
		
		topTitleTxt.setVisibility(View.VISIBLE);
		if (title == null) {
			return;
		}
		if (title.length() > 30) {
			title = title.substring(0, 29) + "...";
		}
		topTitleTxt.setText(title);
	}
	/**
	 * hide title
	 */
	protected void hideTopTitle() {
		topTitleTxt.setVisibility(View.GONE);
	}
	/**
	/**
	 * RightText
	 * @param text
	 */
	protected void setTopRightText(String text) {
		if (null == text) {
			return;
		}
		topRightTitleTxt.setText(text);
		topRightTitleTxt.setVisibility(View.VISIBLE);
	}
	/**
	 * RightButton
	 * @param int
	 */
	protected void setTopRightButton(int resID) {
		if (resID <= 0) {
			return;
		}
		topRightBtn.setImageResource(resID);
		topRightBtn.setVisibility(View.VISIBLE);
	}
	
	protected void hideTopRightButton() {
		topRightBtn.setVisibility(View.GONE);
	}
	/**
	 * RightText
	 * @param text
	 */
	protected void setTopLeftText(String text) {
		if (null == text) {
			return;
		}
		topLeftTitleTxt.setText(text);
		topLeftTitleTxt.setVisibility(View.VISIBLE);
	}
	/**
	 * RightButton
	 * @param int
	 */
	protected void setTopLeftButton(int resID) {
		if (resID <= 0) {
			return;
		}
		topLeftBtn.setImageResource(resID);
		topLeftBtn.setVisibility(View.VISIBLE);
	}
	protected void setTopLeftButtonDrawable(Drawable drawable) {
		if (drawable == null) {
			return;
		}
		topLeftBtn.setImageDrawable(drawable);
		topLeftBtn.setVisibility(View.VISIBLE);
	}
	protected void setLefButtonClick(OnClickListener clickListener) {
		topLeftBtn.setOnClickListener(clickListener);
	}
	public void setLeftBtnBack(){
		topLeftBtn.setVisibility(View.VISIBLE);
		topLeftBtn.setImageDrawable(getResources().getDrawable(R.drawable.back));
		topLeftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BaseActivity.this.finish();
				overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);
			}
		});
	}
	protected void hideTopLeftButton() {
		topLeftBtn.setVisibility(View.GONE);
	}
	Toast toast = null;
	public void showToast(final String tips){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				toast = toast==null?Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT):toast;
				toast.setText(tips);
				toast.show();
			}
		});
	}
	Toast bigToast = null;
	public void showBigToast(final Context context,final String tips){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				bigToast = bigToast==null?Toast.makeText(BaseActivity.this, "", Toast.LENGTH_SHORT):bigToast;
				CharSequence charSequence = StringUtil.SpanColor(StringUtil.SpanSize(tips, 35), getResources().getColor(R.color.yellow2));
				bigToast.setText(charSequence);
				bigToast.show();
			}
		});
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		finish();
		overridePendingTransition( R.anim.slide_left_in,R.anim.slide_right_out);
	}
	

}
