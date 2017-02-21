package com.donson.controller;

import android.app.Activity;

import com.donson.myhook.MarketActivity;
import com.donson.myhook.bean.MARKET;
import com.donson.utils.EasyClickUtil;
import com.donson.utils.SPrefHookUtil;
import com.donson.utils.SendBroadCastUtil;
import com.donson.viewinterface.MarketViewInterface;
import com.donson.xxxiugaiqi.R;

public class MarketViewController {

	Activity mActivity;
	MarketViewInterface viewInterface;
	public MarketViewController(Activity mActivity,
			MarketViewInterface viewInterface) {
		this.mActivity = mActivity;
		this.viewInterface = viewInterface;
		init();
	}
	private void init() {
		market = MARKET.valueOf(SPrefHookUtil.getSettingInt(mActivity, SPrefHookUtil.KEY_SETTING_MARKET, 0));
		viewInterface.checkRadioButton(market);
		viewInterface.isMarketChecked(EasyClickUtil.getMarketHookFlag(mActivity));
		initMarket(market);
		String downApp = SPrefHookUtil.getSettingStr(mActivity, SPrefHookUtil.KEY_SETTING_DOWN_PACKAGE_NAME, "");
		viewInterface.setDownApp(downApp);
	}
	
	public void initMarket(MARKET market){
		if(EasyClickUtil.getMarketHookFlag(mActivity)&&!SPrefHookUtil.getSettingBoolean(mActivity, SPrefHookUtil.KEY_SETTING_NET_DEBUG, SPrefHookUtil.D_SETTING_NET_DEBUG)){
			SPrefHookUtil.putSettingStr(mActivity, SPrefHookUtil.KEY_HHOOK_PACKAGE_NAME, MARKET.getPackageName(market.value()));
			SendBroadCastUtil.listenApp(mActivity, false);
		}
	}
	MARKET market;
	/**
	 * 更改应用市场选择
	 * @param checkedId
	 */
	public void changeMarket(int checkedId){
		switch (checkedId) {
		case R.id.rb_bd:
			market = MARKET.MARKET_BAIDU;
			break;
		case R.id.rb_360:
			market = MARKET.MARKET_360;
			break;
		case R.id.rb_yyb:
			market = MARKET.MARKET_YYB;
			break;
		case R.id.rb_wdj:
			market = MARKET.MARKET_WDJ;
//			viewInterface.toast(mActivity.getString(R.string.wdj_no_half));
			break;
		case R.id.rb_pp_assistant:
			market = MARKET.MARKET_PP;
			break;
		case R.id.rb_az:
			market = MARKET.MARKET_AZ;
			break;
		case R.id.rb_mu:
			market = MARKET.MARKET_MU;
			break;
		case R.id.rb_gp:
			market = MARKET.MARKET_GP;
			break;
		default:
			break;
		}
//		if(checkedId==R.id.rb_wdj){
//			choseMarketView.toast(context.getString(R.string.wdj_no_half));
//		}
		SPrefHookUtil.putSettingInt(mActivity, SPrefHookUtil.KEY_SETTING_MARKET, market.value());

		initMarket(market);
		
		viewInterface.checkRadioButton(market);
	}

}
