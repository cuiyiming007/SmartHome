package com.gdgl.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.gdgl.app.ApplicationController;
import com.gdgl.drawer.MainActivity;
import com.gdgl.manager.CallbackManager;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.getFromSharedPreferences;

/***
 * Netwrok common function
 * 
 * @author justek http://www.it165.net/pro/html/201310/7419.html
 */
public class NetUtil {

	private final static String TAG = "NetUtil";
	public static String URLDir = "/cgi-bin/rest/network/";
	public static String HTTPHeadStr = "http://";
	public static String encodeStr = "&callback=1234&encodemethod=NONE&sign=AAA";
	public String IP;
	private static NetUtil instance;
	public static String heartbeat = "0200070007";
	public static byte buffer[] = heartbeat.getBytes();
	public static Socket callbakcSocket;
	public InputStream inputStream;
	public OutputStream outputStream;

	public int broadcastPort = 5002;
	DatagramSocket udpSocket;

	public static NetUtil getInstance() {
		if (instance == null) {
			instance = new NetUtil();
		}
		return instance;
	}

	// 设置网关IP
	public void setGatewayIP(String ip) {
		IP = ip;
	}

	/***
	 * http://192.168.1.239/cgi-bin/rest/network/addIPC.cgi?
	 * ipaddr=192.168.1.90&
	 * rtspport=554&httpport=80&name=admin&password=12345&alias=camera4
	 * 
	 * @param serverIP
	 * @param resource
	 * @param param
	 * @return
	 */
	public String getVideoURL(String serverIP, String resource) {
		return HTTPHeadStr + serverIP + URLDir + resource;
	}

	public String getCumstomURL(String serverIP, String resource, String param) {
		return HTTPHeadStr + serverIP + URLDir + resource + "?" + param;
	}

	public void connectServerWithTCPSocket() {
		try {
			callbakcSocket = new Socket(IP, 5018);
			inputStream = callbakcSocket.getInputStream();
			outputStream = callbakcSocket.getOutputStream();
			Log.i(TAG, "callbakcSocket connect server successful");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "connectCallbackWithTCP error：" + e.getMessage());
		}
	}

	public void sendHeartBeat() {
		if (isConnectedCallback()) {
			try {
				outputStream.write(buffer, 0, buffer.length);
				outputStream.flush();
			} catch (IOException e) {
				Log.e(TAG, "sendHeartBeat failed" + e.getMessage());
				e.printStackTrace();
				CallbackManager.getInstance().startConnectServerByTCPTask();
			}
			Log.i(TAG, "sendHeartBeat successful");
		} else {
			CallbackManager.getInstance().startConnectServerByTCPTask();
		}
	}

	public void recieveFromCallback() {
		try {
			while (true) {
				if (inputStream != null && inputStream.available() > 0) {
					handleInputStream(inputStream);
					// Log.i("callbakcSocket recieve", inputStream.toString());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "RecieveFromCallback error：" + e.getMessage());
		}
	}

	public static void handleInputStream(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[2048];
		int readBytes = 0;
		while ((readBytes = inputStream.read(buffer)) > 0) {
			String message = new String(buffer, 0, readBytes);
			message = UiUtils.formatResponseString(message);
			// Log.i("message", message);
			CallbackManager.getInstance().classifyCallbackResponse(message);
		}
	}

	public boolean isConnectedCallback() {

		if (callbakcSocket == null || callbakcSocket.isConnected() == false
				|| callbakcSocket.isClosed() == true) {
			return false;
		} else {
			return true;
		}
	}

	public void initalCallbackSocket() {
		if (callbakcSocket != null) {
			try {
				callbakcSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			callbakcSocket = null;
		}
	}

	public String findTheGateway(String serveraddress) {
		String result = "udp receive timeout.";
		try {
			udpSocket = new DatagramSocket();
			udpSocket.setSoTimeout(1000);// 设置超时为1s
			// 使用InetAddress(Inet4Address).getByName把IP地址转换为网络地址
			InetAddress serverAddress = InetAddress.getByName(serveraddress);
			String str = "Who is smart gateway?";// 设置要发送的报文
			byte data[] = str.getBytes();// 把字符串str字符串转换为字节数组
			// 创建一个DatagramPacket对象，用于发送数据。
			// 参数一：要发送的数据 参数二：数据的长度 参数三：服务端的网络地址 参数四：服务器端端口号
			DatagramPacket packet = new DatagramPacket(data, data.length,
					serverAddress, broadcastPort);
			udpSocket.send(packet);// 把数据发送到服务端。

			// recieve==============================================================================
			byte recieveData[] = new byte[4 * 1024];
			// 参数一:要接受的data 参数二：data的长度
			DatagramPacket recievePacket = new DatagramPacket(recieveData,
					recieveData.length);
			List<String> gatewayList = new ArrayList<String>();
			while (true) {
				try {
					udpSocket.receive(recievePacket);
					String ip = recievePacket.getAddress().getHostAddress();
					// NetUtil.getInstance().setGatewayIP(ip);
					String content = new String(recievePacket.getData(),
							recievePacket.getOffset(),
							recievePacket.getLength());
					Log.i(TAG, "GateWay IP:" + ip + ":" + content);
					try {
						JSONObject jsonObject = new JSONObject(content);
						if(MainActivity.LOGIN_STATUS) {
							result = jsonObject.getString("reply");
						} else {
							getFromSharedPreferences.setsharedPreferences(ApplicationController.getInstance());
							String name = getFromSharedPreferences.getLoginName().trim();
							if(name.equals(jsonObject.getString("id"))||name.equals(jsonObject.getString("alias"))) {
								IP = ip;
								result = jsonObject.getString("reply");
							}
						}
						gatewayList.add(ip + "@" + content);
					} catch (Exception e) {
						// TODO: handle exception
						//e.printStackTrace();
					}
				} catch (Exception e) {
					// 这里会抛出接收超时异常
					// e.printStackTrace();
					Log.i(TAG, "udp receive timeout:");
					break;
				}
			}
			if (gatewayList != null || gatewayList.size() > 0) {
				new GatewayResponseToDatabaseTask().execute(gatewayList);
			}

		} catch (SocketException e) {
			Log.e(TAG, "connectServerWithUDPSocket(): broadcast socket error");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i(TAG, "GateWay relust:" + result);
		return result;
	}

	class GatewayResponseToDatabaseTask extends
			AsyncTask<List<String>, Void, Void> {
		@Override
		protected Void doInBackground(List<String>... params) {
			// TODO Auto-generated method stub
			List<String> list = params[0];

			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();

			mDateHelper.emptyTable(mSQLiteDatabase, DataHelper.GATEWAY_TABLE);
			// mDateHelper.insertEndPointList(mSQLiteDatabase,DataHelper.DEVICES_TABLE,
			// null, devDataList);
			for (String gateway : list) {
				String[] substring = gateway.split("@");
				String ip = substring[0];
				String content = substring[1];
				try {
					JSONObject jsonObject = new JSONObject(content);
					String macid = jsonObject.getString("id");
					String alias = jsonObject.getString("alias");

					ContentValues c = new ContentValues();
					c.put("mac", macid);
					c.put("alias", alias);
					c.put("ip", ip);
					mSQLiteDatabase.insert(DataHelper.GATEWAY_TABLE, null, c);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			mSQLiteDatabase.close();
			return null;

		}
	}
	// public void connectServerWithUDPSocket(String serveraddress) {
	//
	// try {
	// udpSocket = new DatagramSocket();
	// udpSocket.setSoTimeout(2000);// 设置超时为2s
	// // 使用InetAddress(Inet4Address).getByName把IP地址转换为网络地址
	// InetAddress serverAddress = InetAddress.getByName(serveraddress);
	// String str = "Who is smart gateway?";// 设置要发送的报文
	// byte data[] = str.getBytes();// 把字符串str字符串转换为字节数组
	// // 创建一个DatagramPacket对象，用于发送数据。
	// // 参数一：要发送的数据 参数二：数据的长度 参数三：服务端的网络地址 参数四：服务器端端口号
	// DatagramPacket packet = new DatagramPacket(data, data.length,
	// serverAddress, broadcastPort);
	// udpSocket.send(packet);// 把数据发送到服务端。
	// recieveFromUdp();
	// } catch (SocketException e) {
	// Log.e(TAG, "connectServerWithUDPSocket(): broadcast socket error");
	// e.printStackTrace();
	// } catch (UnknownHostException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void recieveFromUdp() {
	// //
	// recieve==============================================================================
	// byte recieveData[] = new byte[4 * 1024];
	// // 参数一:要接受的data 参数二：data的长度
	// DatagramPacket recievePacket = new DatagramPacket(recieveData,
	// recieveData.length);
	// try {
	// udpSocket.receive(recievePacket);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// String result = new String(recievePacket.getData(),
	// recievePacket.getOffset(), recievePacket.getLength());
	// Log.i(TAG, "broadcast relust:" + result);
	// if (result.equals("I am smart gateway.")) {
	// Log.i(TAG, "The gateway is local!!");
	// }
	// // 把接收到的data转换为String字符串
	// // udpSocket.close();
	// }

}
