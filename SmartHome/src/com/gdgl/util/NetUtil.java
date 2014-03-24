package com.gdgl.util;

import org.json.JSONException;
import org.json.JSONObject;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.Constants;


public class NetUtil {
	private void requestJsonObject() {

		JsonObjectRequest req = new JsonObjectRequest(Constants.jasonURLforWeather, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Log.i("Response:%n %s", response.toString(4));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void addStringRequest() {

		StringRequest req = new StringRequest(Constants.jasonURLforWeather,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.i("Response:%n %s", response);
						
						handleString(response);  
                           
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("Error: ", error.getMessage());
					}
				});

		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);

	}
	private void handleString(String response) {
		
		response=formatResponseString(response);
		
		stringToJSON(response);
         
         Message m = Message.obtain();  
         
         m.what = 1;  

         m.obj = response;  

         new JsonHandler().sendMessage(m);
	}

	/***
	 * take out \t\n
	 * 
	 * @param response
	 * @return
	 */
	public static String formatResponseString(String response) {
		response = response.replaceAll("\n", "");
		response = response.replaceAll("\t", "");
		return response;
	}

	public static JSONObject stringToJSON(String s) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(s).getJSONObject("weatherinfo");

		} catch (JSONException e) {
			Log.e("", e.getMessage());
		}
		return jsonObject;
	}
	class JsonHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
//        	jsonTextView.setText(msg.obj.toString());
        }
    }
}
