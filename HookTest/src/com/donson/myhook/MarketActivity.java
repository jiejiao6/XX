package com.donson.myhook;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.donson.controller.MarketViewController;
import com.donson.myhook.bean.MARKET;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.viewinterface.MarketViewInterface;
import com.donson.xxxiugaiqi.R;
import com.mz.annotation.ContentViewInject;
import com.mz.annotation.InjectUtils;
import com.mz.annotation.ViewInject;

@ContentViewInject(R.layout.activity_chose_market)
public class MarketActivity extends BaseActivity implements MarketViewInterface {
	@ViewInject(R.id.rg_market)
	private RadioGroup rgMarket;

	@ViewInject(R.id.rb_bd)
	private RadioButton rbBaiDu;

	@ViewInject(R.id.rb_360)
	private RadioButton rb360;

	@ViewInject(R.id.rb_yyb)
	private RadioButton rbYyb;

	@ViewInject(R.id.rb_wdj)
	private RadioButton rbWdj;

	@ViewInject(R.id.rb_pp_assistant)
	private RadioButton rbPPAssistant;

	@ViewInject(R.id.rb_az)
	private RadioButton rbAnZhi;

	@ViewInject(R.id.rb_mu)
	private RadioButton rbMu;

	@ViewInject(R.id.rb_gp)
	private RadioButton rbGp;

	@ViewInject(R.id.tv_chose_market_tips)
	TextView tvMarketTips;

	@ViewInject(R.id.cb_market_is_run_market)
	CheckBox cbMarketIsRun;

	@ViewInject(R.id.rl_market_is_run)
	RelativeLayout rlMarketIsRun;
	
	@ViewInject(R.id.et_down_package_name)
	EditText etDownPackageName;
	
	@ViewInject(R.id.btn_down_package_name_set)
	Button btnDownPackageSet;

	MarketViewController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		InjectUtils.injectAll(this);
		controller = new MarketViewController(this, this);
		initRect();
		setEvent();
	}

	private void initRect() {
		initTopBar();
		setLeftBtnBack();
		setTopTitle(getString(R.string.market_title));
	}

	private void setEvent() {
		rgMarket.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				controller.changeMarket(checkedId);
			}
		});
		rlMarketIsRun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cbMarketIsRun.setChecked(!cbMarketIsRun.isChecked());
				EasyClickUtil.setMarketHookFlag(MarketActivity.this,
						cbMarketIsRun.isChecked());
				controller.initMarket(market);
			}
		});
		btnDownPackageSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SPrefHookUtil.putSettingStr(MarketActivity.this, SPrefHookUtil.KEY_SETTING_DOWN_PACKAGE_NAME, etDownPackageName.getText().toString());
				toast(String.format(getString(R.string.market_set_down_ok), etDownPackageName.getText().toString()));
			}
		});

	}

	@Override
	public void toast(String tips) {
		showToast(tips);
	}

	String marketTips = "";
	MARKET market;

	@Override
	public void checkRadioButton(MARKET market) {
		this.market = market;
		switch (market) {
		case MARKET_BAIDU:
			rbBaiDu.setChecked(true);
			marketTips = getString(R.string.market_baidu);
			break;
		case MARKET_360:
			rb360.setChecked(true);
			marketTips = getString(R.string.market_360);
			break;
		case MARKET_YYB:
			rbYyb.setChecked(true);
			marketTips = getString(R.string.market_yyb);
			break;
		case MARKET_WDJ:
			rbWdj.setChecked(true);
			marketTips = getString(R.string.market_wdj);
			break;
		case MARKET_PP:
			rbPPAssistant.setChecked(true);
			marketTips = getString(R.string.market_pp_assistant);
			break;
		case MARKET_AZ:
			rbAnZhi.setChecked(true);
			marketTips = getString(R.string.market_anzhi);
			break;
		case MARKET_MU:
			rbMu.setChecked(true);
			marketTips = getString(R.string.market_miu);
			break;
		case MARKET_GP:
			rbGp.setChecked(true);
			marketTips = getString(R.string.market_gp);
			// tvGoogelAccount.setVisibility(View.VISIBLE);
			// presenter.googleAccountInjectSet();
			break;
		default:
			break;
		}
		toast(marketTips + "");
		marketTips(/* String.format(getString(R.string.choose_market_tips), */marketTips/* ) */);
	}
	@Override
	public void marketTips(String tips) {
		tvMarketTips.setText(String.format(
				getString(R.string.choose_market_tips), tips));
	}
	@Override
	public void isMarketChecked(boolean isChecked) {
		cbMarketIsRun.setChecked(isChecked);
	}

	@Override
	public void setDownApp(String downApp) {
		etDownPackageName.setText(""+downApp);
	}

}
