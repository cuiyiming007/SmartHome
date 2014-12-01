package com.gdgl.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.gdgl.app.ApplicationController;
import com.gdgl.util.NetUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class NetworkConnectivity {
	public static int networkStatus;// 当前网络状态量
	
	public static final int LAN = 0;
	public static final int INTERNET = 1;
	public static final int NO_NETWORK = 2;

	private Context mContext = ApplicationController.getInstance();
	private static NetworkConnectivity instance;

	public static NetworkConnectivity getInstance() {
		if (instance == null) {
			instance = new NetworkConnectivity();
		}
		return instance;
	}

	public int getConnecitivityNetwork() {
		ConnectivityManager connManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

		// 判断有无网络连接
		if (networkInfo==null||!networkInfo.isConnected()) {
			Toast.makeText(mContext, "未连接任何网络...", Toast.LENGTH_SHORT).show();
			return NO_NETWORK;
		} else {
			if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				Toast.makeText(mContext, "当前为手机移动网络", Toast.LENGTH_SHORT)
						.show();
				return INTERNET;
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				Toast.makeText(mContext, "当前为WiFi网络", Toast.LENGTH_SHORT)
						.show();
				String addrress = getWIFILocalIpAdress(mContext);
				String subAddrress = addrress.substring(0,
						addrress.lastIndexOf("."));
				String serveraddress = subAddrress + ".255";
				Log.i("ServerIpBroadcastAddress", serveraddress);
				String result="Task failde!";
				try {
					result = new FindGatewayTask().execute(serveraddress).get();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				if (result.equals("I am smart gateway.")) {
					return LAN;
				} else {
					return INTERNET;
				}
			} else {
				Toast.makeText(mContext, "当前为未知网络", Toast.LENGTH_SHORT).show();
				return INTERNET;
			}
		}
	}

	public static String getWIFILocalIpAdress(Context context) {

		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = formatIpAddress(ipAddress);
		return ip;
	}

	// 整形地址转换为字符串
	private static String formatIpAddress(int ipAdress) {

		return (ipAdress & 0xFF) + "." + ((ipAdress >> 8) & 0xFF) + "."
				+ ((ipAdress >> 16) & 0xFF) + "." + (ipAdress >> 24 & 0xFF);
	}
	
	class FindGatewayTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String address = params[0];
			String result = NetUtil.getInstance().findTheGateway(
					address);
			return result;
		}
	}

	/***
	 * 小米系统MIui，通过这种方式获取到的是一个固定的IP：10.0.2.15，其他系统均正常。 不知道什么原因。暂不使用这种方法。
	 * 
	 * @return
	 */
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						String address = inetAddress.getHostAddress()
								.toString();
						Log.i("LocalIpAddress", address);
						return address;
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}
}
