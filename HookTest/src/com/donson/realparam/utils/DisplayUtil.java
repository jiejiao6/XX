package com.donson.realparam.utils;

import com.donson.myhook.bean.DisplayMode;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * dip�����豸�޹ص�����ֵ������˵�������ܶ�����ʾ��ֵΪ50dip���ڵ��ܶȵ��豸����ʾ��ֵ��Ϊ50*0.75=37.5
 * px:����������أ�����˵�����ܶ�����ʾ��ֵΪ50px��ע�ⵥλ��px���������ܶ��豸����ʾ��ֵҲ��50px
 * densityDpi :ÿӢ����ٸ��㣬�ҵ������ÿӢ����ٸ�dip
 * density��densityDpi /160;
 * drawable-hdpi  ���ܶ�,ͨ����ָ240Ҳ�������������е�densityDpiΪ240
 * drawable-ldpi ���ܶȣ�ͨ����ָ120
 * drawable-mdpi �е��ܶȣ�ͨ����ָ160
 * drawable-xhdpi �����ܶȣ�ͨ����ָ320
 *  px = dp * (dpi / 160) ��px =dp*density;(density=densityDpi/160)
 *   @author mz
 */
public class DisplayUtil {
	public static DisplayMode getDisplayMode(Activity mActivity) {
		DisplayMode displayMode = new DisplayMode();
		DisplayMetrics metric = new DisplayMetrics();
		Display display = mActivity.getWindowManager().getDefaultDisplay();
		display.getMetrics(metric);
		float xdpi = metric.xdpi;//120 160 240 320
		float ydpi = metric.ydpi;
		displayMode.setXdpi(xdpi);
		displayMode.setYdpi(ydpi);

		float density = metric.density;// ��Ļ�ܶȣ�0.75 / 1.0 / 1.5��=densityDpi/160
		int densityDpi = metric.densityDpi;// ��Ļ�ܶ�DPI��120 / 160 / 240��
		displayMode.setDensity(density);
		displayMode.setDensityDpi(densityDpi);

		int heightPixels = metric.heightPixels;
		int widthPixels = metric.widthPixels;
		float scaledDensity = metric.scaledDensity;  //= density
		displayMode.setHeightPixels(heightPixels);
		displayMode.setWidthPixels(widthPixels);
		displayMode.setScaledDensity(scaledDensity);

		
		int height = display.getHeight();
		int width = display.getWidth();
		displayMode.setHeight(height);
		displayMode.setWidth(width);
		Point point = new Point();
		display.getSize(point);
		String displaySize = point.toString();
		displayMode.setDisplaySize(displaySize);

		return displayMode;
	}
	public static DisplayMode getDisplayMode2(Activity mActivity) {
		DisplayMode displayMode = new DisplayMode();
		DisplayMetrics metric = mActivity.getResources().getDisplayMetrics();
		float xdpi = metric.xdpi;
		float ydpi = metric.ydpi;
		displayMode.setXdpi(xdpi);
		displayMode.setYdpi(ydpi);

		float density = metric.density;// ��Ļ�ܶȣ�0.75 / 1.0 / 1.5��
		int densityDpi = metric.densityDpi;// ��Ļ�ܶ�DPI��120 / 160 / 240��
		displayMode.setDensity(density);
		displayMode.setDensityDpi(densityDpi);

		int heightPixels = metric.heightPixels;
		float scaledDensity = metric.scaledDensity;
		int widthPixels = metric.widthPixels;
		displayMode.setHeightPixels(heightPixels);
		displayMode.setScaledDensity(scaledDensity);
		displayMode.setWidthPixels(widthPixels);

		return displayMode;
	}



}
