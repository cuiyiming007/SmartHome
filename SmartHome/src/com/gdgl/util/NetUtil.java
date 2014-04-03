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
	 * request a JsonString object from server
	 */
	public static void addStringRequest() {

		// callbakc listener
		Listener<String> responseListener = new Listener<String>() {

			@Override
			public void onResponse(String response) {
				handleString(response);
			}
		};

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Error: ", error.getMessage());
			}
		};

		StringRequest req = new StringRequest(Constants.locationURL,
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
//		stringToJSON(response);
		parseMeituanJSON(response);

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
		response = customString(response);
		return response;
	}

	public static String customString(String s) {
		s = s.substring(s.indexOf("{"), s.length() - 1);
		return s;
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
	public static void parseJSON(String s) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		ArrayList<ResponseParams> list = new ArrayList<ResponseParams>();
		JsonObject jsonObject = parser.parse(s).getAsJsonObject();
		RespondDataEntity dataEntity;
		dataEntity=gson.fromJson(jsonObject, RespondDataEntity.class);
		JsonArray jsonArray = jsonObject.getAsJsonArray("response_params");
		Type type = new TypeToken<ResponseParams>() {
		}.getType();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement el = jsonArray.get(i);
			ResponseParams tmp = gson.fromJson(el, type);
			list.add(tmp);
			//System.out.println(tmp.get("STRPRODUCTCODE"));
		}
//		 dataEntity=new RespondDataEntity();
		dataEntity.setResponseparamList(list);
//		dataEntity.setRequest_id(jsonObject.get("request_id").getAsString());
/*		jsonObject.remove("response_params");
		jsonObject
				.add("response_params", parser.parse(gson.toJson(list)).getAsJsonArray());
//		��������
		Type dataEntitytype = new TypeToken<RespondDataEntity>() {
		}.getType();
		RespondDataEntity dataEntity=gson.fromJson(jsonObject, dataEntitytype);
//		RespondDataEntity dataEntity=new RespondDataEntity();
//		dataEntity.setRequest_id(jsonObject.get);
		System.out.println(gson.toJson(jsonObject));*/
	}
	/***
	 * /*{"stid":"113261570606080",
"data":{
   "morepage":
    [
         {"id":-1,"count":18209,"onRed":false,"iconUrl":"http://ms0.meituan.net/group/deal/cate/android/4.0/-1-small.png","name":"ȫ������"},
         {"id":1,"count":8466,"onRed":false,"iconUrl":"http://ms0.meituan.net/group/deal/cate/android/4.0/1-small.png","name":"��ʳ",
             "list":[
                  {"id":1,"onRed":false,"name":"ȫ��"},
	 * 
	 * @param s
	 */
	public static void parseMeituanJSON(String s) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		List<String> locateList = new ArrayList<String>();
		List<String> nearestPointsList=new ArrayList<String>();
		JsonObject jsonObject = parser.parse(s).getAsJsonObject();
		JsonArray jsonArray = jsonObject.getAsJsonArray("queryLocation");
		JsonArray jsonArray2=jsonObject.getAsJsonArray("nearestPoint");
		Type type = new TypeToken<String>() {
		}.getType();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement el = jsonArray.get(i);
			String tmp = gson.fromJson(el, type);
			locateList.add(tmp);
			//System.out.println(tmp.get("STRPRODUCTCODE"));
		}
		jsonObject.remove("queryLocation");
		jsonObject
				.add("queryLocation", parser.parse(gson.toJson(locateList)).getAsJsonArray());

		for (int i = 0; i < jsonArray2.size(); i++) {
			JsonElement el = jsonArray2.get(i);
			String tmp = gson.fromJson(el, type);
			nearestPointsList.add(tmp);
		}
		jsonObject.remove("nearestPoint");
		jsonObject
				.add("nearestPoint", parser.parse(gson.toJson(locateList)).getAsJsonArray());

		//		Type addrListtype = new TypeToken<LocationData>() {
//		}.getType();
//		meituan dataEntity=gson.fromJson(jsonObject, type);
//		RespondDataEntity dataEntity=new RespondDataEntity();
//		dataEntity.setRequest_id(jsonObject.get);
		System.out.println(gson.toJson(jsonObject));
	}

}
