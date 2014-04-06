package com.gdgl.network;

import java.lang.reflect.Type;
import java.util.ArrayList;

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
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.RespondDataEntity;
import com.gdgl.mydata.ResponseParams;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.util.UiUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class VolleyOperation {

	private static VolleyOperation instance = new VolleyOperation();

	public static VolleyOperation getInstance() {
		return instance;
	}

	/***
	 * Suitable for the Json data is very simple£¨cant't parse data conclude
	 * child object.
	 * eg:Json={"weatherinfo":{"city":"π„÷›","cityid":"101280101","temp1"
	 * :"27°Ê","temp2"
	 * :"17°Ê","weather":"∂‡‘∆","img1":"d1.gif","img2":"n1.gif","ptime":"11:00"}}
	 * 1.successful key = weatherinfo 2.failed key=null
	 * 
	 * @param URL
	 * @param t
	 * @param key
	 * @return
	 */
	public <T> T getSimpleJsonByVolley(String URL, T t, String key) {
		//
		Listener<T> respondListener = new Listener<T>() {
			@Override
			public void onResponse(T arg0) {
				T t = arg0;
				Log.i("onResponse", t.toString());
				// weather=arg0;
			}
		};
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Error: ", error.getMessage());
			}
		};
		CustomRequest<T> request = new CustomRequest<T>(URL, key, (Class<T>) t,
				respondListener, errorListener);
		ApplicationController.getInstance().addToRequestQueue(request);
		return t;
	}

	/***
	 * request a Json object from server,if need to parse JsonArray, transfer
	 * JSONObject to JSONArray
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
	 * request a JsonString from server
	 */
	public static void addVolleyRequestForString() {

		// callbakc listener
		Listener<String> responseListener = new Listener<String>() {

			@Override
			public void onResponse(String response) {
				handleResponseString(response);
			}
		};

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Error: ", error.getMessage());
			}
		};

		StringRequest req = new StringRequest(Constants.getZBNodeURL,
				responseListener, errorListener);

		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);

	}

	/***
	 * convert json String to object
	 * 
	 * @param response
	 */
	public static RespondDataEntity handleResponseString(String response) {
		// format response string to standard json string
		response = UiUtils.formatResponseString(response);

		// convert string to json object
		//return parseJSON2EndPoint(response);
		return parseJSONGeneric(response,ResponseParamsEndPoint.class);

	}

	/***
	 * 
	 * @param s
	 * @return
	 */
	public static JSONObject stringToJSON(String s) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(s).getJSONObject("weatherinfo");

		} catch (JSONException e) {
			Log.e("", e.getMessage());
		}
		return jsonObject;
	}

	/***
	 * http://blog.sina.com.cn/s/blog_a2f078300101hbe3.html
	 * 
	 * @param s
	 */
	public static RespondDataEntity parseJSON(String s) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		ArrayList<ResponseParams> list = new ArrayList<ResponseParams>();
		JsonObject jsonObject = parser.parse(s).getAsJsonObject();
		RespondDataEntity dataEntity = new RespondDataEntity();
		JsonElement idElement=jsonObject.get("request_id");
		JsonArray jsonArray = jsonObject.getAsJsonArray("response_params");
		Type type = new TypeToken<ResponseParams>() {
		}.getType();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement el = jsonArray.get(i);
			ResponseParams tmp = gson.fromJson(el, type);
			list.add(tmp);
		}
		dataEntity.setResponseparamList(list);
		return dataEntity;
	}
	/***
	 * revert String to RespondDataEntity <ResponseParamsEndPoint>
	 * @param <T>
	 * 
	 * @param s
	 */
	public static RespondDataEntity parseJSON2EndPoint(String s) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		ArrayList<ResponseParamsEndPoint> list = new ArrayList<ResponseParamsEndPoint>();
		JsonObject jsonObject = parser.parse(s).getAsJsonObject();
		RespondDataEntity dataEntity = new RespondDataEntity();
		JsonElement idElement=jsonObject.get("request_id");
//		dataEntity = gson.fromJson(jsonObject, RespondDataEntity.class);
		JsonArray jsonArray = jsonObject.getAsJsonArray("response_params");
		Type type = new TypeToken<ResponseParamsEndPoint>() {
		}.getType();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement el = jsonArray.get(i);
			ResponseParamsEndPoint tmp = gson.fromJson(el, type);
			list.add(tmp);
		}
		dataEntity.setResponseparamList(list);
		return dataEntity;
	}
	/***
	 * Generic failed! because Type type = new TypeToken<T>() {}.getType();can't get the type
	 * @param <T>
	 */
	public  static <T> RespondDataEntity<T> parseJSONGeneric(String s, T t) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		ArrayList<T> list = new ArrayList<T>();
		JsonObject jsonObject = parser.parse(s).getAsJsonObject();
		RespondDataEntity<T> dataEntity = new RespondDataEntity<T>();
		JsonElement idElement=jsonObject.get("request_id");
		dataEntity.setRequest_id(idElement.toString());
//		dataEntity = gson.fromJson(jsonObject, RespondDataEntity.class);
		JsonArray jsonArray = jsonObject.getAsJsonArray("response_params");
		Type type = new TypeToken<T>() {
		}.getType();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement el = jsonArray.get(i);
			T tmp = gson.fromJson(el, type);
			list.add(tmp);
		}
		dataEntity.setResponseparamList(list);
		return dataEntity;
	}
}
