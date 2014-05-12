package com.gdgl.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
	public String IP = "192.168.1.239";
	public String loginIP = "192.168.1.100";
	private static NetUtil instance;
	public static String heartbeat = "0200070007";
	public static byte buffer[] = heartbeat.getBytes();
	public static Socket callbakcSocket;
	public InputStream inputStream;
	public OutputStream outputStream;

	public static NetUtil getInstance() {
		if (instance == null) {
			instance = new NetUtil();
		}
		return instance;
	}

	public String getCumstomURL(String serverIP, String resource, String param) {
		return HTTPHeadStr + serverIP + URLDir + resource + "?" + param
				+ encodeStr;
	}

	public void connectServerWithTCPSocket() throws Exception {
		callbakcSocket = new Socket("192.168.1.239", 5002);
		inputStream = callbakcSocket.getInputStream();
		outputStream = callbakcSocket.getOutputStream();
		Log.v(TAG, "callbakcSocket connect server successful");
	}

	public void sendHeartBeat() throws IOException {
		if (isConnectedCallback()) {
			outputStream.write(buffer, 0, buffer.length);
			outputStream.flush();
			Log.i(TAG, "sendHeartBeat successful");
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
			
			for (String string : messages) {
				int num=string.split("\\{", -1).length-1;
				//如果‘{‘有2个，那么type=7，是个嵌套2层的json格式，需多加一个’}‘在后面
				if (num>1) {
					string=string+"}";
				}
				CallbackManager.getInstance().handleCallbackResponse(string+"}");
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

}
