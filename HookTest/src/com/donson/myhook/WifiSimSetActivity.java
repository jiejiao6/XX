package com.donson.myhook;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.donson.xxxiugaiqi.R;
import com.param.config.SPrefUtil;


public class WifiSimSetActivity extends BaseActivity {
	TextView tvWifiTips,tvSimTips;
	SeekBar sbWifi,sbSim;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_sim);
		tvWifiTips = (TextView) findViewById(R.id.tv_wifi_set);
		tvSimTips = (TextView) findViewById(R.id.tv_sim_set);
		sbWifi = (SeekBar) findViewById(R.id.sb_wifi);
		sbSim = (SeekBar) findViewById(R.id.sb_sim);
		init();
		setRect();
		setEvent();
	}

	private void init() {
		int wifi = SPrefUtil.getInt(getApplicationContext(), SPrefUtil.C_WIFI_PERCENT, SPrefUtil.D_WIFI_PERCENT);
		sbWifi.setProgress(wifi);
		tvWifiTips.setText(String.format(getString(R.string.wifi_tv_wifi_set), wifi));
		int sim = SPrefUtil.getInt(getApplicationContext(), SPrefUtil.C_SIM_PERCENT, SPrefUtil.D_SIM_PERCENT);
		sbSim.setProgress(sim);
		tvSimTips.setText(String.format(getString(R.string.wifi_tv_sim_set), sim));
	}

	private void setRect() {
		initTopBar();
		setLeftBtnBack();
		setTopTitle(getString(R.string.wifi_sim_set_title));
	}

	private void setEvent() {
		sbWifi.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if(sbWifi.getProgress()==0){
					showToast(getString(R.string.wifi_tv_sim_set_no_use));
				}else {
					showToast(String.format(getString(R.string.wifi_tv_wifi_set),seekBar.getProgress()));
				}
				SPrefUtil.putInt(getApplicationContext(), SPrefUtil.C_WIFI_PERCENT, seekBar.getProgress());
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				tvWifiTips.setText(String.format(getString(R.string.wifi_tv_wifi_set), progress));
			}
		});
		sbSim.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if(sbWifi.getProgress()==0){
					showToast(getString(R.string.wifi_tv_sim_set_no_use));
				}else {
					showToast(String.format(getString(R.string.wifi_tv_sim_set),seekBar.getProgress()));
				}
				SPrefUtil.putInt(getApplicationContext(), SPrefUtil.C_SIM_PERCENT, seekBar.getProgress());
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				tvSimTips.setText(String.format(getString(R.string.wifi_tv_sim_set),progress));
			}
		});
	}

}
