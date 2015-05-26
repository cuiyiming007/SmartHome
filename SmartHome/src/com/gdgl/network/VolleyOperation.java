package com.gdgl.network;

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
import com.gdgl.mydata.DeviceLearnedParam;
import com.gdgl.mydata.RespondDataEntity;
import com.gdgl.mydata.ResponseParams;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.mydata.Callback.CallbackBindListDevices;
import com.gdgl.mydata.Callback.CallbackBindListMessage;
import com.gdgl.mydata.Region.GetRoomInfo_response;
import com.gdgl.mydata.binding.BindingDataEntity;
import com.gdgl.mydata.binding.BindingDivice;
import com.gdgl.mydata.binding.Binding_response_params;
import com.gdgl.mydata.getlocalcielist.CIEresponse_params;
import com.gdgl.mydata.scene.SceneInfo;
import com.gdgl.mydata.timing.TimingAction;
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
	 * Suitable for the Json data is very simple��cant't parse data conclude
	 * child object.
	 * eg:Json={"weatherinfo":{"city":"����","cityid":"101280101","temp1"
	 * :"27��","temp2"
	 * :"17��","weather":"����","img1":"d1.gif","img2":"n1.gif","ptime":"11:00"}}
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
				handleEndPointString(response);
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
	public static RespondDataEntity<ResponseParamsEndPoint> handleEndPointString(String response) {
		// format response string to standard json string
		response = UiUtils.formatResponseString(response);

		// convert string to json object
		//return parseJSON2EndPoint(response);
		return parseJSON2EndPoint(response);

	}
	
	public static RespondDataEntity<GetRoomInfo_response> handleRoomInfoString(String response) {
		response=UiUtils.formatResponseString(response);
		return parseJSON2RoomInfo(response);
	}
	
	public static RespondDataEntity<CIEresponse_params> handleCIEString(String response) {
		// format response string to standard json string
		response = UiUtils.formatResponseString(response);
		return parseJSON2GetLocalCIEList(response);

	}
	public static BindingDataEntity handleBindingString(String response) {
		// format response string to standard json string
		response = UiUtils.formatResponseString(response);
		return parseJSON2GetAllBindingList(response);

	}
	public static RespondDataEntity<DeviceLearnedParam> handleDeviceLearnedString(String response) {
		// format response string to standard json string
		response = UiUtils.formatResponseString(response);
		return parseJSON2GetGeviceLearned(response);
		
	}
	public static CallbackBindListMessage handleCallbackBindListString(String response) {
		response = UiUtils.formatResponseString(response);
		return parseJSON2CallBindList(response);
	}
	public static List<SceneInfo> handleSceneInfoListString(String response) {
		response = UiUtils.formatResponseString(response);
		return parseJSON2SceneInfoList(response);
	}
	public static List<TimingAction> handleTimingActionListString(String response) {
		response = UiUtils.formatResponseString(response);
		return parseJSON2TimingActionList(response);
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
	
//	public static 

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
	public static RespondDataEntity<ResponseParamsEndPoint> parseJSON2EndPoint(String s) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		ArrayList<ResponseParamsEndPoint> list = new ArrayList<ResponseParamsEndPoint>();
		JsonObject jsonObject = parser.parse(s).getAsJsonObject();
		RespondDataEntity<ResponseParamsEndPoint> dataEntity = new RespondDataEntity<ResponseParamsEndPoint>();
		JsonElement idElement=jsonObject.get("request_id");
		dataEntity.setRequest_id(idElement.toString());
//		dataEntity = gson.fromJson(jsonObject, RespondDataEntity.class);
		JsonArray jsonArray = jsonObject.getAsJsonArray("response_params");
//		Type type = new TypeToken<ResponseParamsEndPoint>() {
//		}.getType();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement el = jsonArray.get(i);
			ResponseParamsEndPoint tmp = gson.fromJson(el, ResponseParamsEndPoint.class);
			list.add(tmp);
		}
		dataEntity.setResponseparamList(list);
		return dataEntity;
	}
	
	/***
	 * revert String to RespondDataEntity <GetRoomInfo_response>
	 * @param s
	 * @return
	 */
	public static RespondDataEntity<GetRoomInfo_response> parseJSON2RoomInfo(String s) {
		Gson gson = new Gson();
		JsonParser parser=new JsonParser();
		ArrayList<GetRoomInfo_response> list =new ArrayList<GetRoomInfo_response>();
		JsonObject jsonObject=parser.parse(s).getAsJsonObject();
		RespondDataEntity<GetRoomInfo_response> dataEntity=new RespondDataEntity<GetRoomInfo_response>();
		JsonElement idElement=jsonObject.get("request_id");
		dataEntity.setRequest_id(idElement.toString());
		JsonArray jsonArray = jsonObject.getAsJsonArray("response_params");
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement el = jsonArray.get(i);
			GetRoomInfo_response tmp = gson.fromJson(el, GetRoomInfo_response.class);
			list.add(tmp);
		}
		dataEntity.setResponseparamList(list);
		return dataEntity;
	}
	/***
	 * revert String to RespondDataEntity <CIEresponse_params>
	 * @param <T>
	 * 
	 * @param s
	 */
	public static RespondDataEntity<CIEresponse_params> parseJSON2GetLocalCIEList(String s) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		ArrayList<CIEresponse_params> list = new ArrayList<CIEresponse_params>();
		JsonObject jsonObject = parser.parse(s).getAsJsonObject();
		RespondDataEntity<CIEresponse_params> dataEntity = new RespondDataEntity<CIEresponse_params>();
		JsonElement idElement=jsonObject.get("request_id");
		dataEntity.setRequest_id(idElement.toString());
		JsonArray jsonArray = jsonObject.getAsJsonArray("response_params");
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement el = jsonArray.get(i);
			CIEresponse_params tmp = gson.fromJson(el, CIEresponse_params.class);
			list.add(tmp);
		}
		dataEntity.setResponseparamList(list);
		return dataEntity;
	}
	
	/***
	 * revert String to BindingDataEntity
	 * @param s
	 * @return
	 */
	public static BindingDataEntity parseJSON2GetAllBindingList(String s) {
		Gson gson = new Gson();
		BindingDataEntity dataEntity = new BindingDataEntity();
		ArrayList<CallbackBindListMessage> response_params =new ArrayList<CallbackBindListMessage>();
		
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(s).getAsJsonObject();
		JsonElement idElement=jsonObject.get("request_id");
		dataEntity.setRequest_id(idElement.toString());
		JsonArray response_paramsArray=jsonObject.getAsJsonArray("response_params");
		if(response_paramsArray==null) {
			return dataEntity;
		}
		for(int j=0;j<response_paramsArray.size();j++) {
			CallbackBindListMessage binding_params=new CallbackBindListMessage();
			ArrayList<CallbackBindListDevices> list = new ArrayList<CallbackBindListDevices>();
			JsonElement paramsElement=response_paramsArray.get(j);
			JsonObject paramsObject=paramsElement.getAsJsonObject();
			
			String msgtypeString=paramsObject.get("msgtype").getAsString();
			binding_params.setIeee(msgtypeString);
			String ieeeString=paramsObject.get("IEEE").getAsString();
			binding_params.setIeee(ieeeString);
			String ep=paramsObject.get("EP").getAsString();
			binding_params.setEp(ep);
			String countString=paramsObject.get("count").getAsString();
			binding_params.setCount(countString);
			
			JsonArray jsonArray = paramsObject.getAsJsonArray("list");
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonElement el = jsonArray.get(i);
				CallbackBindListDevices tmp = gson.fromJson(el, CallbackBindListDevices.class);
				list.add(tmp);
			}
			binding_params.setList(list);
			response_params.add(binding_params);
		}
		
		dataEntity.setResponse_paramsList(response_params);
		return dataEntity;
	}
	
	public static CallbackBindListMessage parseJSON2CallBindList(String s) {
		Gson gson = new Gson();
		JsonParser parser =new JsonParser();
		ArrayList<CallbackBindListDevices> list = new ArrayList<CallbackBindListDevices>();
		JsonObject jsonObject =parser.parse(s).getAsJsonObject();
		CallbackBindListMessage bindMassage =new CallbackBindListMessage();
		JsonElement element1=jsonObject.get("msgtype");
		bindMassage.setmsgtype(element1.toString());
		JsonElement element2=jsonObject.get("IEEE");
		bindMassage.setIeee(element2.toString().substring(1, 17));
		JsonElement element3=jsonObject.get("EP");
		bindMassage.setEp(element3.toString().substring(1, 3));
		JsonElement element4=jsonObject.get("count");
		bindMassage.setCount(element4.toString());
		JsonArray jsonArray=jsonObject.getAsJsonArray("list");
		for(int i=0;i<jsonArray.size();i++) {
			JsonElement el=jsonArray.get(i);
			CallbackBindListDevices tmp=gson.fromJson(el, CallbackBindListDevices.class);
			list.add(tmp);
		}
		bindMassage.setList(list);
		return bindMassage;
	}
	
	public static RespondDataEntity<DeviceLearnedParam> parseJSON2GetGeviceLearned(String s) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		ArrayList<DeviceLearnedParam> list = new ArrayList<DeviceLearnedParam>();
		JsonObject jsonObject = parser.parse(s).getAsJsonObject();
		RespondDataEntity<DeviceLearnedParam> dataEntity = new RespondDataEntity<DeviceLearnedParam>();
		JsonElement idElement=jsonObject.get("request_id");
		dataEntity.setRequest_id(idElement.toString());
		JsonArray jsonArray = jsonObject.getAsJsonArray("response_params");
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement el = jsonArray.get(i);
			DeviceLearnedParam tmp = gson.fromJson(el, DeviceLearnedParam.class);
			list.add(tmp);
		}
		dataEntity.setResponseparamList(list);
		return dataEntity;
	}
	
	public static List<SceneInfo> parseJSON2SceneInfoList(String s) {
		Gson gson = new Gson();
		JsonParser parser =new JsonParser();
		List<SceneInfo> list = new ArrayList<SceneInfo>();
		JsonObject jsonObject = parser.parse(s).getAsJsonObject();
		JsonElement statusElement = jsonObject.get("status");
		if(Integer.parseInt(statusElement.toString()) == 0) {
			JsonArray jsonArray = jsonObject.getAsJsonArray("list");
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonElement el = jsonArray.get(i);
				SceneInfo tmp = gson.fromJson(el, SceneInfo.class);
				list.add(tmp);
			}
		}
		return list;
	}
	
	public static List<TimingAction> parseJSON2TimingActionList(String s) {
		Gson gson = new Gson();
		JsonParser parser =new JsonParser();
		List<TimingAction> list = new ArrayList<TimingAction>();
		JsonObject jsonObject = parser.parse(s).getAsJsonObject();
		JsonElement statusElement = jsonObject.get("status");
		if(Integer.parseInt(statusElement.toString()) == 0) {
			JsonArray jsonArray = jsonObject.getAsJsonArray("list");
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonElement el = jsonArray.get(i);
				TimingAction tmp = gson.fromJson(el, TimingAction.class);
				list.add(tmp);
			}
		}
		return list;
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
