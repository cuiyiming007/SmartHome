package com.gdgl.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.cetc54.ndksocket.IReceive;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConvertTools {
	public static String long2IP(int longip) {
		//  将10进制整数形式转换成127.0.0.1形式的ip地址，在命令提示符下输入ping 3396362403l
		StringBuffer sb = new StringBuffer("");
		sb.append(String.valueOf(longip & 0x000000ff));
		sb.append(".");
		sb.append(String.valueOf((longip & 0x0000ffff) >>> 8));
		sb.append(".");
		sb.append(String.valueOf((longip & 0x00ffffff) >>> 16)); // 将高8位置0，然后右移16位
		sb.append(".");
		sb.append(String.valueOf(longip >>> 24));// 直接右移24位
		return sb.toString();
	}

	public static String getLocalIpAddress2() {

		try {

			for (Enumeration<NetworkInterface> en = NetworkInterface

			.getNetworkInterfaces(); en.hasMoreElements();) {

				NetworkInterface intf = en.nextElement();

				for (Enumeration<InetAddress> enumIpAddr = intf

				.getInetAddresses(); enumIpAddr.hasMoreElements();) {

					InetAddress inetAddress = enumIpAddr.nextElement();

					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()) {

						return inetAddress.getHostAddress().toString();

					}

				}

			}

		} catch (SocketException ex) {

			Log.e("WifiPreference IpAddress", ex.toString());

		}

		return null;

	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("ConvertTools", ex.toString());
		}
		return null;
	}

	/**
	 *  获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
	 * 
	 * @param context
	 * @return
	 */
	public static int getAPNType(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null) {
			//return IReceive.NOCON_STATUS;
		}
		int nType = networkInfo.getType();
		System.out.println("networkInfo.getExtraInfo() is "
				+ networkInfo.getExtraInfo());

		int netType = 0;
		switch (nType) {
		case ConnectivityManager.TYPE_MOBILE:
			netType = IReceive.CON_3G_STATUS;
			break;
		case ConnectivityManager.TYPE_WIFI:
			netType = IReceive.CON_WIFI_STATUS;
			break;
		}

		return netType;
	}
}