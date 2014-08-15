package com.gdgl.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;
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

import android.R.integer;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.gdgl.app.ApplicationController;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.DeviceManager;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.Node;
import com.gdgl.mydata.RespondDataEntity;
import com.gdgl.mydata.ResponseParams;
import com.gdgl.mydata.getlocalcielist.elserec;
//import com.gdgl.mydata.meituan.FenleiEntity;
//import com.gdgl.mydata.meituan.LocationData;
//import com.gdgl.mydata.meituan.Page;
//import com.gdgl.mydata.meituan.meituan;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

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
	public String IP = "192.168.1.237";
//	public String loginIP = "192.168.1.100";
	private static NetUtil instance;
	public static String heartbeat = "0200070007";
	public static byte buffer[] = heartbeat.getBytes();
	public static Socket callbakcSocket;
	public InputStream inputStream;
	public OutputStream outputStream;
	
	
	public int broadcastPort=5002;
	DatagramSocket udpSocket;

	public static NetUtil getInstance() {
		if (instance == null) {
			instance = new NetUtil();
		}
		return instance;
	}
/***
 *  http://192.168.1.239/cgi-bin/rest/network/addIPC.cgi?
	 * ipaddr=192.168.1.90&rtspport=554&httpport=80&name=admin&password=12345&alias=camera4
 * @param serverIP
 * @param resource
 * @param param
 * @return
 */
	public String getVideoURL(String serverIP, String resource, String param) 
	{
		return HTTPHeadStr + serverIP + URLDir + resource + "?" + param;
	}
	public String getCumstomURL(String serverIP, String resource, String param) {
		return HTTPHeadStr + serverIP + URLDir + resource + "?" + param
				+ encodeStr;
	}

	public void connectServerWithTCPSocket() throws Exception {
		callbakcSocket = new Socket(IP, 5002);
		inputStream = callbakcSocket.getInputStream();
		outputStream = callbakcSocket.getOutputStream();
		Log.v(TAG, "callbakcSocket connect server successful");
	}

	public void sendHeartBeat()  {
		if (isConnectedCallback()) {
			try {
				outputStream.write(buffer, 0, buffer.length);
				outputStream.flush();
			} catch (IOException e) {
				Log.e(TAG, "sendHeartBeat failed");
				e.printStackTrace();
				CallbackManager.getInstance().startConnectServerByTCPTask();
			}
			Log.i(TAG, "sendHeartBeat successful");
		}else {
			CallbackManager.getInstance().startConnectServerByTCPTask();
		}
	}

	public void recieveFromCallback() throws IOException {
		while (true) {
			if (inputStream!=null&&inputStream.available()>0) {
				handleInputStream(inputStream);
//				Log.i("callbakcSocket recieve", result);
			}
			
		}
	}
	public static void handleInputStream(InputStream inputStream) throws IOException{
		byte[] buffer = new byte[2048];
		int readBytes = 0;
		while((readBytes = inputStream.read(buffer)) > 0){
			String message=new String(buffer, 0, readBytes);
			String messages[]=message.split("\\}");
			
				for (int i = 0; i < messages.length; i++) {
					String json=null;
					int num=messages[i].split("\\{", -1).length-1;
					//如果‘{‘有2个，那么type=7，是个嵌套2层的json格式，需多加一个’}‘在后面
					if (num>1) {
						json=messages[i]+"}}";
						i++;
					}else if (num==1) {
						json=messages[i]+"}";
					}else
					{
						continue;
					}
						
//					Log.d(TAG, json);
					CallbackManager.getInstance().handleCallbackResponse(json);
					
				}
			
			
		}
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		int i=-1;
//		while((i=is.read())!=-1){;
//			Log.e(TAG+"+++++++++++++", String.valueOf(i));
//		baos.write(i);
//		}
//		return stringBuilder.toString();
		}

//	public static String[] splitMessage(String message,String regular) {
//		String messages[]=message.split("}");
//		return messages;
//		
//	}

	public boolean isConnectedCallback() {

		if (callbakcSocket == null || callbakcSocket.isConnected() == false
				|| callbakcSocket.isClosed() == true) {
			return false;
		} else {
			return true;
		}
	}

	public void initalCallbackSocket() {
		if (callbakcSocket!=null) {
			try {
				callbakcSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			callbakcSocket = null;
		}
	}
	public void connectServerWithUDPSocket() {
		
		try {
			//创建DatagramSocket对象并指定一个端口号，注意，如果客户端需要接收服务器的返回数据,
			//还需要使用这个端口号来receive，所以一定要记住
			udpSocket = new DatagramSocket(broadcastPort);
			//使用InetAddress(Inet4Address).getByName把IP地址转换为网络地址  
			InetAddress serverAddress = InetAddress.getByName("192.168.1.255");
			//Inet4Address serverAddress = (Inet4Address) Inet4Address.getByName("192.168.1.32");  
			String str = "Who is smart gateway?";//设置要发送的报文  
			byte data[] = str.getBytes();//把字符串str字符串转换为字节数组  
			//创建一个DatagramPacket对象，用于发送数据。  
			//参数一：要发送的数据  参数二：数据的长度  参数三：服务端的网络地址  参数四：服务器端端口号 
			DatagramPacket packet = new DatagramPacket(data, data.length ,serverAddress ,broadcastPort);  
			udpSocket.send(packet);//把数据发送到服务端。  
			recieveFromUdp();
		} catch (SocketException e) {
			Log.e(TAG, "connectServerWithUDPSocket(): broadcast socket error");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	public void recieveFromUdp()   {
		//recieve==============================================================================
		byte recieveData[] = new byte[4 * 1024];  
		//参数一:要接受的data 参数二：data的长度  
		DatagramPacket recievepaPacket = new DatagramPacket(recieveData, recieveData.length);  
		try {
			udpSocket.receive(recievepaPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}  
			String result = new String(recievepaPacket.getData(), recievepaPacket.getOffset(),  
					recievepaPacket.getLength());  
			Log.i(TAG, "broadcast relust"+result);
			if (result.equals("I am smart gateway.")) {
				Log.i(TAG, "The gateway is local!!");
			}
		//把接收到的data转换为String字符串  
//		udpSocket.close();
	}

}
