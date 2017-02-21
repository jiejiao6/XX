package com.donson.myhook.adapters;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.donson.config.ConstantsHookConfig;
import com.donson.config.Logger;
import com.donson.myhook.bean.AppInfosMode;
import com.donson.utils.StringUtil;
import com.donson.xxxiugaiqi.R;

public class AppInfosAdapter extends BaseAdapter {

	private Activity mActivity;
	private List<AppInfosMode> mAppList;
	private PackageManager mPm;
	private boolean mIsShowAlpha;
	private boolean mIsShowCheckBox;

	public AppInfosAdapter(Activity mActivity, List<AppInfosMode> mAppList, boolean mIsShowAlpha,
			boolean mIsShowCheckBox) {
		
		this.mActivity = mActivity;
		this.mAppList = mAppList;
		this.mIsShowAlpha = mIsShowAlpha;
		this.mIsShowCheckBox = mIsShowCheckBox;
		mPm = mActivity.getPackageManager();
	}

	public void setData(List<AppInfosMode> mAppList) {
		this.mAppList = mAppList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAppList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mAppList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class Holder {
		TextView tv_alpha;
		TextView tv_text;
		ImageView iv_image;
		CheckBox cb_checkbox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(mActivity, R.layout.item_select_app, null);
			holder.tv_alpha = (TextView) convertView.findViewById(R.id.tv_alpha);
			holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			holder.cb_checkbox = (CheckBox) convertView.findViewById(R.id.cb_checkbox);
			holder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		AppInfosMode packageInfoMode = mAppList.get(position);
		holder.tv_text.setText(getName(packageInfoMode, mPm));
		
		setImage(holder.iv_image, packageInfoMode, mPm);
		if (mIsShowAlpha) {
			setAlpha(position, holder.tv_alpha);
		}
		if (mIsShowCheckBox) {
			holder.cb_checkbox.setVisibility(View.VISIBLE);
			holder.cb_checkbox.setChecked(packageInfoMode.isIscheck());
		}
		if(packageInfoMode.getPackageInfo().packageName.equals(mActivity.getPackageName())
				||packageInfoMode.getPackageInfo().packageName.equals(ConstantsHookConfig.XPOSED_01_PACKAGE_NAME)
				||packageInfoMode.getPackageInfo().packageName.equals(ConstantsHookConfig.XPOSED_02_PACKAGE_NAME)
				||packageInfoMode.getPackageInfo().packageName.equals(ConstantsHookConfig.Root_PACKAGE_NAME)
				||packageInfoMode.getPackageInfo().packageName.equals(ConstantsHookConfig.CONTROL_PACKAGE_NAME)
				||packageInfoMode.getPackageInfo().packageName.equals(ConstantsHookConfig.HDJ_HOOK_PACKAGE_NAME)){
			holder.cb_checkbox.setVisibility(View.INVISIBLE);
			ColorStateList color = mActivity.getResources().getColorStateList(R.color.blue01);
			holder.tv_text.setTextColor(color);
		}else {
			ColorStateList color = mActivity.getResources().getColorStateList(R.color.black);
			holder.tv_text.setTextColor(color);
		}
		return convertView;
	}

	private void setAlpha(int position, TextView tv_alpha) {
		AppInfosMode packageInfoMode = mAppList.get(position);
		String alpha = packageInfoMode.getAlpha();
		if (position == 0) {
			setTvAlpha(alpha, tv_alpha);
		} else {
			AppInfosMode bf_packageInfoMode = mAppList.get(position - 1);
			String bf_alpha = bf_packageInfoMode.getAlpha();
			if (TextUtils.isEmpty(alpha) || alpha.equals(bf_alpha)) {
				tv_alpha.setVisibility(View.GONE);
			} else {
				setTvAlpha(alpha, tv_alpha);
			}
		}
	}

	private void setTvAlpha(String alpha, TextView tv_alpha) {
		tv_alpha.setText(alpha);
		tv_alpha.setVisibility(View.VISIBLE);
	}

	public static CharSequence getName(AppInfosMode packageInfoMode, PackageManager mPm) {
		PackageInfo packageInfo = packageInfoMode.getPackageInfo();
		ApplicationInfo applicationInfo = packageInfo.applicationInfo;
		CharSequence loadLabel = applicationInfo.loadLabel(mPm);
		String packageName = packageInfo.packageName;
		CharSequence spanColor = StringUtil.SpanColor(packageName, Color.GRAY);
		CharSequence spanAppendLn = StringUtil.SpanAppendLn(loadLabel, spanColor);
		return spanAppendLn;
	}

	public static CharSequence getName(PackageInfo packageInfo, PackageManager mPm) {
		ApplicationInfo applicationInfo = packageInfo.applicationInfo;
		CharSequence loadLabel = applicationInfo.loadLabel(mPm);
		String packageName = packageInfo.packageName;
		CharSequence spanColor = StringUtil.SpanColor(packageName, Color.GRAY);
		CharSequence spanAppendLn = StringUtil.SpanAppendLn(loadLabel, spanColor);
		return spanAppendLn;
	}

	public static void setImage(ImageView imageView, AppInfosMode packageInfoMode, PackageManager mPm) {
		PackageInfo packageInfo = packageInfoMode.getPackageInfo();
		ApplicationInfo applicationInfo = packageInfo.applicationInfo;
		Drawable loadIcon = applicationInfo.loadIcon(mPm);
		imageView.setImageDrawable(loadIcon);
	}
	public static void setImage(ImageView imageView, PackageInfo packageInfo, PackageManager mPm) {
		ApplicationInfo applicationInfo = packageInfo.applicationInfo;
		Drawable loadIcon = applicationInfo.loadIcon(mPm);
		imageView.setImageDrawable(loadIcon);
	}

}
