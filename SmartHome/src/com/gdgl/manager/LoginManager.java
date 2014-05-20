package com.gdgl.manager;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.AccountInfo;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.LoginResponse;
import com.gdgl.util.NetUtil;
import com.google.gson.Gson;

public class LoginManager extends Manger {
	// http://192.168.1.100/cgi-bin/rest/network/clientLogin.cgi?account=001122334455&password=334455
	public final static String TAG = "LoginManager";

	private static LoginManager instance;

	public static  LoginManager getInstance() {
		if (instance == null) {
			instance = new LoginManager();
		}
		return instance;
	}

	public void doLogin(AccountInfo accountInfo) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("account", accountInfo.getAccount());
		paraMap.put("password", accountInfo.getPassword());
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getVideoURL(
				NetUtil.getInstance().IP, "clientLogin.cgi", param);
		JsonObjectRequest req = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Log.i("LoginManager doLogin Response:%n %s", response.toString(4));
							Gson gson = new Gson();  
							LoginResponse loginResponse = gson.fromJson(response.toString(), LoginResponse.class); 
							Event event = new Event(EventType.LOGIN, true);
							event.setData(loginResponse);
							notifyObservers(event);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error!=null&&error.getMessage()!=null) {
							VolleyLog.e("Error: ", error.getMessage());
						}
						Event event = new Event(EventType.LOGIN, false);
						event.setData(error);
						notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}
	public void ModifyPassword(AccountInfo accountInfo,String newPWD)
	{
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("account", accountInfo.getAccount());
		paraMap.put("old_password", accountInfo.getPassword());
		paraMap.put("new_password", newPWD);
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getVideoURL(
				NetUtil.getInstance().IP, "modifyPassword.cgi", param);
		JsonObjectRequest req = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Log.i("LoginManager ModifyPassword Response:%n %s", response.toString(4));
							Gson gson = new Gson();  
							LoginResponse person = gson.fromJson(response.toString(), LoginResponse.class); 
							Event event = new Event(EventType.MODIFYPASSWORD, true);
							event.setData(person);
							notifyObservers(event);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error!=null&&error.getMessage()!=null) {
							VolleyLog.e("Error: ", error.getMessage());
						}
						Event event = new Event(EventType.MODIFYPASSWORD, false);
						event.setData(error);
						notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}
	public void modifyAlias(String id,String old_alias,String new_alias)
	{
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("id", id);
		paraMap.put("old_alias", old_alias);
		paraMap.put("new_alias", new_alias);
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getVideoURL(
				NetUtil.getInstance().IP, "modifyAlias.cgi", param);
		JsonObjectRequest req = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Log.i("LoginManager modifyAlias Response:%n %s", response.toString(4));
							Gson gson = new Gson();  
							LoginResponse person = gson.fromJson(response.toString(), LoginResponse.class); 
							Event event = new Event(EventType.MODIFYALIAS, true);
							event.setData(person);
							notifyObservers(event);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error!=null&&error.getMessage()!=null) {
							VolleyLog.e("Error: ", error.getMessage());
						}
						Event event = new Event(EventType.MODIFYALIAS, false);
						event.setData(error);
						notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}
}
