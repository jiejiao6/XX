package com.donson.utils;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.donson.config.Logger;
import com.donson.realparam.utils.NetUtil;

public class VpnUtil {
	public static boolean isVpnConnected1() {
		String string = "netstat -anp | grep :1723";
		try {
			Process p = CmdUtil.run(string);
			p.waitFor();
			String out = CmdUtil.readResult(p).toUpperCase();
			Logger.i("==VpnUtil====isVpnConnected=======" + out);
			if (!TextUtils.isEmpty(out) && out.contains("ESTABLISHED")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static final int VPNNOTCONNECTED = 0;
	public static final int VPNCONNECTED = 1;
	public static final int VPNCONNECTERR = 2;

	// CLOSE_WAIT TIME_WAIT
	public static int isVpnConnected2() {
		String string = "netstat -anp | grep :1723";
		try {
			Process p = CmdUtil.run(string);
			Logger.i("p==" + string);
			// p.waitFor();
			String out = CmdUtil.readResult(p).toUpperCase();
			Logger.i("==VpnUtil====isVpnConnected=======" + out);
			if (!TextUtils.isEmpty(out) && out.contains("ESTABLISHED")) {
				return isVpnConnectFromIpRo2();

			} else if (!TextUtils.isEmpty(out)
					&& (out.contains("CLOSE_WAIT")/* ||out.contains("TIME_WAIT") */)
					|| TextUtils.isEmpty(out)) {
				return VPNCONNECTERR;
			}
		} catch (Exception e) {
			Logger.i("vpn exe::" + e);
			e.printStackTrace();
		}
		return VPNNOTCONNECTED;
	}

	public static boolean isVpnConnected() {
		String string = "netstat -anp | grep :1723";
		try {
			Process p = CmdUtil.run(string);
			Logger.i("p==" + string);
			// p.waitFor();
			String out = CmdUtil.readResult(p).toUpperCase();
			Logger.i("==VpnUtil====isVpnConnected=======" + out);
			if (!TextUtils.isEmpty(out) && out.contains("ESTABLISHED")) {
				return isVpnConnectFromIpRo();
			}
		} catch (Exception e) {
			Logger.i("vpn exe::" + e);
			e.printStackTrace();
		}
		return false;
	}

	public static int isVpnConnectFromIpRo2() {
		try {
			Process p = CmdUtil.run("ip ro");
			p.waitFor();
			String out = CmdUtil.readResult(p);
			Logger.i("==VpnUtil====isVpnConnectFromIpRo=======" + out
					+ "  \n ===============");
			if (!TextUtils.isEmpty(out) && out.contains("ppp")) {
				return VPNCONNECTED;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return VPNNOTCONNECTED;
	}

	public static boolean isVpnConnectFromIpRo() {
		try {
			Process p = CmdUtil.run("ip ro");
			p.waitFor();
			String out = CmdUtil.readResult(p);
			Logger.i("==VpnUtil====isVpnConnectFromIpRo=======" + out
					+ "  \n ===============");
			if (!TextUtils.isEmpty(out) && out.contains("ppp")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isVpnUsed() {
		try {
			Enumeration<NetworkInterface> niList = NetworkInterface
					.getNetworkInterfaces();
			if (niList != null) {
				for (NetworkInterface intf : Collections.list(niList)) {
					if (!intf.isUp()
							|| intf.getInterfaceAddresses().size() == 0) {
						continue;
					}
					Logger.i("isVpnUsed() NetworkInterface Name: "
							+ intf.getName());
					if ("tun0".equals(intf.getName())
							|| "ppp0".equals(intf.getName())
							|| "ppp9".equals(intf.getName())) {
						return true; // The VPN is up
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * public static void operaVpn(final Context context, final int times) {
	 * 
	 * int conn = SPrefHookUtil.getSettingInt(context,
	 * SPrefHookUtil.KEY_SETTING_VPN_CONN_TIME,
	 * SPrefHookUtil.D_SETTING_VPN_CONN_TIME); int reConn =
	 * SPrefHookUtil.getSettingInt(context,
	 * SPrefHookUtil.KEY_SETTING_VPN_RECONN_TIME,
	 * SPrefHookUtil.D_SETTING_VPN_RECONN_TIME);
	 * System.out.println("times==="+times+"  conn:"+conn+" reConn: "+reConn);
	 * try { for (int i = 0; i < times; i++) { Intent intent = new Intent();
	 * intent.setAction("android.net.vpn.SETTINGS");
	 * context.startActivity(intent); Thread.sleep(2500); //
	 * OpenActivityUtil.openAPK(context, context.getPackageName());
	 * OpenActivityUtil.openApkByDetailInfo(context.getApplicationContext(),
	 * context.getPackageName(), "com.donson.myhook.AutoActivity");
	 * if(times==1){ Thread.sleep(conn*1000);} else if(times==2&&i==0){
	 * Thread.sleep(reConn*1000); }else if (times==2&&i==1) {
	 * Thread.sleep(conn*1000); } } SendBroadCastUtil.checkVpn(context); } catch
	 * (Exception e) { e.printStackTrace(); } }
	 */
	static int lastConnectFlag = 0;

	public static void operaVpn(final Context context, final int times) {
		final int conn = SPrefHookUtil.getSettingInt(context,
				SPrefHookUtil.KEY_SETTING_VPN_CONN_TIME,
				SPrefHookUtil.D_SETTING_VPN_CONN_TIME);
		Logger.i("operaVpn=============wifiConn==="+NetUtil.isWifiConnect(context.getApplicationContext()));
		System.out.println("operaVpn=============wifiConn==="+NetUtil.isWifiConnect(context.getApplicationContext()));

		if (!NetUtil.isWifiConnect(context.getApplicationContext())) {
//			boolean isContinue = SPrefHookUtil.getTaskBoolean(getApplicationContext(), SPrefHookUtil.KEY_TASK_CONTINUOUS,SPrefHookUtil.D_TASK_CONTINUOUS);
			SPrefHookUtil.putTaskBoolean(context, SPrefHookUtil.KEY_TASK_CONTINUOUS , false);
			waitFor(context, conn);
			return;
		}
		int reConn = SPrefHookUtil.getSettingInt(context,
				SPrefHookUtil.KEY_SETTING_VPN_RECONN_TIME,
				SPrefHookUtil.D_SETTING_VPN_RECONN_TIME);
		System.out.println("times===" + times + "  conn:" + conn + " reConn: "
				+ reConn);
		try {
			for (int i = 0; i < times; i++) {
				OpenActivityUtil.openVpnConnect(context);
				// Intent intent = new Intent();
				// intent.setAction("android.net.vpn.SETTINGS");
				// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// context.startActivity(intent);

				// Thread.sleep(2000);
				// // OpenActivityUtil.openAPK(context,
				// context.getPackageName());
				// // CmdUtil.killProcess(ConstantsHookConfig.SETTINGS);
				// OpenActivityUtil.openApkByDetailInfo(context.getApplicationContext(),
				// context.getPackageName(), "com.donson.myhook.AutoActivity");
				// if(times==1){ Thread.sleep(conn*1000);}
				if (times == 2 && i == 0) {
					Thread.sleep(reConn * 1000);
				} else if (times == 2 && i == 1) {
					// Thread.sleep(conn*1000);
				}
			}
			waitFor(context, conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void waitFor(final Context context,final int conn) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);// 先等1s再去判断连接
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int i = 0;
				while (i < conn) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					int isconnected2 = VpnUtil.isVpnConnected2();
					Logger.i("//////////////////isconnected2&&&&&&===="
							+ isconnected2);
					if (isconnected2 == VPNCONNECTED
							|| isconnected2 == VPNCONNECTERR) {
						if (lastConnectFlag == isconnected2) {
							SendBroadCastUtil.checkVpn(context);
							i = conn + 4;
							return;
						} else {
							lastConnectFlag = isconnected2;
						}
					}
					Logger.i("************isconnected：：***********"
							+ isconnected2 + "  " + Thread.currentThread()
							+ "  " + i);
					i += 2;
				}
				SendBroadCastUtil.checkVpn(context);
			}
		}).start();
	}

	// public static void disConnectVpn(final Context context) {
	// int reConn = SPrefHookUtil.getSettingInt(context,
	// SPrefHookUtil.KEY_SETTING_VPN_RECONN_TIME,
	// SPrefHookUtil.D_SETTING_VPN_RECONN_TIME);
	// try {
	// Intent intent = new Intent();
	// intent.setAction("android.net.vpn.SETTINGS");
	// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// context.startActivity(intent);
	// Thread.sleep(reConn*1000);
	// OpenActivityUtil.openApkByDetailInfo(context.getApplicationContext(),
	// context.getPackageName(), "com.donson.myhook.AutoActivity");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// public static void disConnectVpn1(Context context) {
	// if (VpnUtil.isVpnConnected()) {
	// EasyClickUtil.setvpnConnectFlag(context, EasyClickUtil.CONNECT_VPN);
	// int reConn = SPrefHookUtil.getSettingInt(context,
	// SPrefHookUtil.KEY_SETTING_VPN_RECONN_TIME,
	// SPrefHookUtil.D_SETTING_VPN_RECONN_TIME);
	// try {
	// Intent intent = new Intent();
	// intent.setAction("android.net.vpn.SETTINGS");
	// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// context.startActivity(intent);
	// Thread.sleep(reConn * 1000);
	// Logger.i("==^^^^^^^^^^^^^^^^^^^^^^disConnectVpn^^-------" + reConn);
	// OpenActivityUtil.openApkByDetailInfo(
	// context.getApplicationContext(),
	// context.getPackageName(),
	// "com.donson.myhook.MainActivity");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// EasyClickUtil.setvpnConnectFlag(context,
	// EasyClickUtil.NOT_CONNECT_VPN);
	// } else {
	// return;
	// }
	// }
}
