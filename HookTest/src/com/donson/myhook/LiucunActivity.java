package com.donson.myhook;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.donson.controller.LiuCunViewController;
import com.donson.viewinterface.LiuCunViewInterface;
import com.donson.xxxiugaiqi.R;

public class LiucunActivity extends BaseActivity implements LiuCunViewInterface,OnClickListener{
	
	private EditText etLiucun1,etLiucun2,etLiucun3,etLiucun4,etLiucun5,etLiucun6,etLiucun7,etLiucun8,etLiucun9;
	private TextView tvLiuAllDataNum,tvLiucunDataRunNum,tvCancel,tvOk,tvLiuCunTotalCount;
	private Button btnLiucunExport,btnLiuCunRemove,btnLiuCunRemoveAll,btnLiucunSetTotal;
	private LiuCunViewController controller;
	private Spinner spPackage;
	private List<String> packageLists;
	private ArrayAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liucun_setting2);
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		packageLists = new ArrayList<String>();
		spPackage = (Spinner) findViewById(R.id.sp_package);
		tvLiuAllDataNum = (TextView) findViewById(R.id.tv_all_data_num);
		tvLiucunDataRunNum = (TextView) findViewById(R.id.tv_liucun_data_run_num);
		tvLiuCunTotalCount = (TextView) findViewById(R.id.tv_liucun_total_num);
		etLiucun1 = (EditText) findViewById(R.id.et_liucun_1);
		etLiucun2 = (EditText) findViewById(R.id.et_liucun_2);
		etLiucun3 = (EditText) findViewById(R.id.et_liucun_3);
		etLiucun4 = (EditText) findViewById(R.id.et_liucun_4);
		etLiucun5 = (EditText) findViewById(R.id.et_liucun_5);
		etLiucun6 = (EditText) findViewById(R.id.et_liucun_6);
		etLiucun7 = (EditText) findViewById(R.id.et_liucun_7);
		etLiucun8 = (EditText) findViewById(R.id.et_liucun_8);
		etLiucun9 = (EditText) findViewById(R.id.et_liucun_9);
		tvCancel = (TextView) findViewById(R.id.btn_cancel);
		tvOk = (TextView) findViewById(R.id.btn_ok);
		btnLiucunExport = (Button) findViewById(R.id.btn_liucun_export);
		btnLiuCunRemove = (Button) findViewById(R.id.btn_liucun_remove);
		btnLiuCunRemoveAll = (Button) findViewById(R.id.btn_liucun_remove_all);
		btnLiucunSetTotal = (Button) findViewById(R.id.btn_liucun_set_total);
		controller = new LiuCunViewController(this, this);
		controller.init();
		initRect();
		setEvent();
	}
	private void initRect() {
		initTopBar();
		setTopTitle(getString(R.string.title_liucun_set));
		setLeftBtnBack();
		
	}
	private void setEvent() {
		packageLists.addAll(controller.getpackageList());
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, packageLists);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spPackage.setAdapter(adapter);
        spPackage.setSelection(packageLists.size()-1);
        spPackage.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				controller.handleSpinnerSelected(parent,view,position,  id);
//				adapter.add("dalian");
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				for (int i = 1; i <= 9; i++) {
					setEt(i, false, "");
					toast(getString(R.string.no_liucun_set));
				}
			}
		});
		tvOk.setOnClickListener(this);
		btnLiuCunRemove.setOnClickListener(this);
		btnLiucunSetTotal.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public void toast(String tips) {
		showToast(tips);
	}
	public boolean isEtErr(EditText editText){
		System.out.println(TextUtils.isEmpty(editText.getText()));
		if(TextUtils.isEmpty(editText.getText())||Float.parseFloat(editText.getText().toString())>100){
			return true;
		}else {
			return false;
		}
	}
	@Override
	public String getPercentSet() {
		for(int i = 1;i<=9;i++){
			if(isEtErr(getEditTextById(i))){
				setEt(i, true, getString(R.string.et_must_less_than_100));
				toast(getString(R.string.liucun_set_err)+"\n"+getString(R.string.et_not_null_or_100));
				return null;
			}
		}
			String result = etLiucun1.getText().toString()+"_"+etLiucun2.getText().toString()
				+"_"+etLiucun3.getText().toString()
				+"_"+etLiucun4.getText().toString()
				+"_"+etLiucun5.getText().toString()
				+"_"+etLiucun6.getText().toString()
				+"_"+etLiucun7.getText().toString()
				+"_"+etLiucun8.getText().toString()
				+"_"+etLiucun9.getText().toString();
			toast(result);
			System.out.println(result);
		return result;
	}
	@Override
	public void setEt(int flag, boolean isError, String str) {
		switch (flag) {
		case 1:
			if(isError){
				etLiucun1.setError(str);
			}else {
				etLiucun1.setText(str);
			}
			break;
		case 2:
			if(isError){
				etLiucun2.setError(str);
			}else {
				etLiucun2.setText(str);
			}
			break;
		case 3:
			if(isError){
				etLiucun3.setError(str);
			}else {
				etLiucun3.setText(str);
			}
			break;
		case 4:
			if(isError){
				etLiucun4.setError(str);
			}else {
				etLiucun4.setText(str);
			}
			break;
		case 5:
			if(isError){
				etLiucun5.setError(str);
			}else {
				etLiucun5.setText(str);
			}
			break;
		case 6:
			if(isError){
				etLiucun6.setError(str);
			}else {
				etLiucun6.setText(str);
			}
			break;
		case 7:
			if(isError){
				etLiucun7.setError(str);
			}else {
				etLiucun7.setText(str);
			}
			break;
		case 8:
			if(isError){
				etLiucun8.setError(str);
			}else {
				etLiucun8.setText(str);
			}
			break;
		case 9:
			if(isError){
				etLiucun9.setError(str);
			}else {
				etLiucun9.setText(str);
			}
			break;
		}
	}
	@Override
	public void notifySpinner(List<String> packageList) {
		/*packageList.clear();
		packageList.addAll(packageList);
		
		adapter.notifyDataSetChanged();*/
	}
	@Override
	public void notifySpinnerDelete(String packageName) {
		packageLists = controller.getpackageList();
		adapter.remove(packageName);
	}
	
	public EditText getEditTextById(int id){
		switch (id) {
		case 1:
			return etLiucun1;
		case 2:
			return etLiucun2;
		case 3:
			return etLiucun3;
		case 4:
			return etLiucun4;
		case 5:
			return etLiucun5;
		case 6:
			return etLiucun6;
		case 7:
			return etLiucun7;
		case 8:
			return etLiucun8;
		case 9:
			return etLiucun9;
		default:
			return null;
		}
	}
	@Override
	public void setLiuCunDataText(int all, int liucun) {
		tvLiuAllDataNum.setText(""+all);
		tvLiucunDataRunNum.setText(""+liucun);
		
	}
	@Override
	public void setTotalCountText(int total) {
		tvLiuCunTotalCount.setText(""+total);
	}
	@Override
	public void toastBig(String tips) {
		showBigToast(this,tips);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			controller.setLiuCunPercent();
			break;
		case R.id.btn_liucun_remove:
			controller.hanldeLiucunRemove(spPackage.getSelectedItemPosition());
			break;
		case R.id.btn_liucun_set_total:
			controller.handleSetTotalCount();
			break;

		default:
			break;
		}
		
	}

}
