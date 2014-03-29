package com.gdgl.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.Constants;

/***
 * Netwrok common function
 * 
 * @author justek
 * http://www.it165.net/pro/html/201310/7419.html
 */
public class NetUtil {
	/***
	 * request a Json object from server,if need to parse JsonArray, transfer JSONObject to JSONArray
	 */
	private void requestJsonObject() {

		JsonObjectRequest req = new JsonObjectRequest(
				Constants.jasonURLforWeather, null,
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

	/***
	 * request a JsonString object from server
	 */
	public static void addStringRequest() {
		
		// callbakc listener
		Listener<String> responseListener = new Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.i("Response:%n %s", response);
				handleString(response);
			}
		};
		
		ErrorListener errorListener=new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Error: ", error.getMessage());
			}
		};
		
		
		StringRequest req = new StringRequest(Constants.jasonURLforWeather,
				responseListener, errorListener);

		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);

	}

	/***
	 * convert json String to json object and get it on the main thread
	 * 
	 * @param response
	 */
	private static void handleString(String response) {
		// format response string to json string
		response = formatResponseString(response);
		// convert string to json object
		stringToJSON(response);

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

}
