package com.donson.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.donson.utils.SPrefHookUtil;
import com.donson.viewinterface.LiuCunViewInterface;
import com.donson.xxxiugaiqi.R;
import com.param.bean.LiuCunMode;
import com.param.config.SPrefUtil;
import com.param.dao.DbDao;

public class LiuCunViewController {
	private Activity context;
	private LiuCunViewInterface viewInterface;
	private DbDao dao;
	List<LiuCunMode> setList;

	// List<String> packageList ;
	public LiuCunViewController(Activity context,
			LiuCunViewInterface viewInterface) {
		this.context = context;
		this.viewInterface = viewInterface;
		dao = DbDao.getInstance(context);
		getpackageList();
	}
	public String getListenPackageName(){
		return SPrefHookUtil.getSettingStr(context,SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME);
	}
	public List<String> getpackageList() {
		setList = dao.showLiuCunSetting();
		List<String> packageList = new ArrayList<String>();
		for (LiuCunMode liuCunMode : setList) {
			packageList.add(liuCunMode.getPackageName());
		}
		String curListenPac = getListenPackageName();
		if(!packageList.contains(curListenPac)&&!TextUtils.isEmpty(curListenPac)){
			packageList.add(curListenPac);
		}
		return packageList;
	}
	public void init() {
//		int runCount = SPrefUtil.getInt(context, SPrefUtil.C_RUN_COUNT, SPrefUtil.D_RUN_COUNT);
		int runCount = SPrefHookUtil.getTaskInt(context, SPrefHookUtil.KEY_TASK_CUR_RUN_TIME, SPrefHookUtil.D_TASK_CUR_RUN_TIME);
		int runLiucunCount = SPrefUtil.getInt(context, SPrefUtil.C_RUN_LIU_CUN_COUNT, SPrefUtil.D_RUN_LIU_CUN_COUNT);
		int totalCount = SPrefHookUtil.getTaskInt(context, SPrefHookUtil.KEY_TASK_RUN_TIMES, SPrefHookUtil.D_TASK_RUN_TIMES);
		viewInterface.setLiuCunDataText(runCount, runLiucunCount);
		viewInterface.setTotalCountText(totalCount);
	}
	public void setLiuCunPercent() {
		String result = viewInterface.getPercentSet();
		if (result != null) {
			boolean succeed = dao.insertOrReplaceLiuCunSetting(getListenPackageName(), result);
			getpackageList();
			if (succeed) {
				viewInterface.toast(context.getString(R.string.liucun_set_ok));
			} else {
				viewInterface.toast(context.getString(R.string.liucun_set_err));
			}
		}
	}
	/**
	 * Edittext textWatcher
	 * @param etLiucun
	 * @param flag
	 */
	public void addTextChangedListener(EditText etLiucun, final int flag) {
		etLiucun.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s)) {
					viewInterface.setEt(flag, true,
							context.getString(R.string.et_not_null));
				} else if (Float.parseFloat(s.toString()) > 100) {
					viewInterface.setEt(flag, true,
							context.getString(R.string.et_must_less_than_100));
				}
			}
		});
	}

	public void handleSpinnerSelected(AdapterView<?> parent, View view,
			int position, long id) {
		if (position >= setList.size()) {
			for (int i = 1; i <= 9; i++) {
				viewInterface.setEt(i, false, "");
			}
		} else {
			LiuCunMode mode = setList.get(position);
			for (int i = 1; i <= 9; i++) {
				viewInterface.setEt(i, false, String.valueOf(mode.getPercentByID(i-1)));
			}
		}
	}

	/**
	 * @param i
	 */
	public void hanldeLiucunRemove(final int i) {
		if (setList == null || setList.size() <= 0) {
			viewInterface.toast(context.getString(R.string.no_liucun_set));
			return;
		}
		Dialog alertDialog = new AlertDialog.Builder(context)
				.setMessage(
						String.format(context
								.getString(R.string.btn_liucun_remove_tips),
								setList.get(i).getPackageName()))
				.setPositiveButton(context.getString(R.string.btn_ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dao.deleteLiucunSetByPackageName(setList.get(i)
										.getPackageName());
								viewInterface.notifySpinnerDelete(setList
										.get(i).getPackageName());
							}
						})
				.setNegativeButton(context.getString(R.string.btn_cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).create();
		alertDialog.show();

	}

	public void handleSetTotalCount() {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View dialogView = layoutInflater.inflate(R.layout.dialog_edit_text,null);
		final EditText et = (EditText) dialogView.findViewById(R.id.et_dialog_text);
		
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.liucun_dialog_set_total_title));
		builder.setView(dialogView);
		builder.setNegativeButton(context.getString(R.string.liucun_dialog_btn_cancel), null);
		builder.setPositiveButton(context.getString(R.string.liucun_dialog_btn_ok),null);
		final AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String count = et.getText().toString();
						if (TextUtils.isEmpty(count)) {
							et.setError(context.getString(R.string.liucun_tip_total_count_not_null));
							viewInterface.toast(context.getString(R.string.liucun_tip_total_count_not_null));
						} else {
							int integer = Integer.parseInt(count);
							boolean result = SPrefHookUtil.putTaskInt(context,SPrefHookUtil.KEY_TASK_RUN_TIMES,integer);
							if (result) {
								viewInterface.toast(context.getString(R.string.liucun_tip_total_count_set_ok));
								int totalCount = SPrefHookUtil.getTaskInt(context,SPrefHookUtil.KEY_TASK_RUN_TIMES,SPrefHookUtil.D_TASK_RUN_TIMES);
								viewInterface.setTotalCountText(totalCount);
							} else {
								viewInterface.toast(context.getString(R.string.liucun_tip_total_count_set_err));
							}
							dialog.dismiss();
						}
					}
				});
		
	}
}
