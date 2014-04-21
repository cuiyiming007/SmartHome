package com.gdgl.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.gdgl.app.ApplicationController;
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
	
	
	
	public static String URLDir="/cgi-bin/rest/network/";
	public static String HTTPHeadStr="http://";
	public static String encodeStr="&callback=1234&encodemethod=NONE&sign=AAA";
	public String IP="192.168.1.239";
	public String loginIP="192.168.1.100";
	private static NetUtil instance;
	
	public static NetUtil getInstance() {
		if (instance==null) {
			instance= new NetUtil();
		}
		return instance;
	}
	public String getCumstomURL(String serverIP,String resource,String param)
	{
		return HTTPHeadStr+serverIP+URLDir+resource+"?"+param+encodeStr;
	}

}
