package com.gdgl.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.video.VideoNode;
import com.gdgl.mydata.video.VideoResponse;
import com.gdgl.mydata.video.VideoResponseParams;
import com.gdgl.network.VolleyErrorHelper;
import com.gdgl.util.NetUtil;
import com.gdgl.util.UiUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VideoManager extends Manger {
	public final static String TAG = "VideoManager";

	private static VideoManager instance;

	public static VideoManager getInstance() {
		if (instance == null) {
			instance = new VideoManager();
		}
		return instance;
	}

	public void getIPClist() {
		String url = NetUtil.getInstance().getVideoURL(
				NetUtil.getInstance().IP, "getIPClist.cgi");

		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				new GetVideoListTast().execute(response);
			}
		};
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if (error != null && error.getMessage() != null) {
					Log.e("ResponseError: ", error.getMessage());
					VolleyErrorHelper.getMessage(error,
							ApplicationController.getInstance());
				}
			}
		};
		StringRequest req = new StringRequest(url, responseListener,
				errorListener);
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/***
	 * 添加摄像头 http://192.168.1.239/cgi-bin/rest/network/addIPC.cgi?
	 * ipaddr=192.168
	 * .1.90&rtspport=554&httpport=80&name=admin&password=12345&alias=camera4
	 * 
	 * @param videoNode
	 */

	public void addIPC(VideoNode videoNode) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ipaddr", videoNode.getIpc_ipaddr());
		paraMap.put("rtspport", videoNode.getRtspport());
		paraMap.put("httpport", videoNode.getHttpport());
		paraMap.put("name", encodeString(videoNode.getName()));
		paraMap.put("password", videoNode.getPassword());
		paraMap.put("alias", encodeString(videoNode.getAliases()));
		String param = hashMap2ParamString(paraMap);
		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new Gson();
				String jsonString = UiUtils.customString(response);
				VideoResponse videoResponse = gson.fromJson(jsonString,
						VideoResponse.class);

				Event event = new Event(EventType.ADDIPC, true);
				event.setData(videoResponse);
				if (!videoResponse.getResponse_params().getStatus().equals("0")) {
					event.setSuccess(false);
				}
				notifyObservers(event);
			}
		};
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				String errorString = null;
				if (error != null && error.getMessage() != null) {
					Log.e("ResponseError: ", error.getMessage());
					errorString = VolleyErrorHelper.getMessage(error,
							ApplicationController.getInstance());
				}
				Event event = new Event(EventType.ADDIPC, false);
				event.setData(errorString);
				notifyObservers(event);
			}
		};
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "addIPC.cgi", param);
		StringRequest req = new StringRequest(url, responseListener,
				errorListener);
		ApplicationController.getInstance().addToRequestQueue(req);

	}

	public void editIPC(VideoNode videoNode) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ipaddr", videoNode.getIpc_ipaddr());
		paraMap.put("ipc_id", videoNode.getId());
		paraMap.put("rtspport", videoNode.getRtspport());
		paraMap.put("httpport", videoNode.getHttpport());
		paraMap.put("name", encodeString(videoNode.getName()));
		paraMap.put("password", videoNode.getPassword());
		paraMap.put("alias", encodeString(videoNode.getAliases()));
		String param = hashMap2ParamString(paraMap);
		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new Gson();
				String jsonString = UiUtils.customString(response);
				VideoResponse videoResponse = gson.fromJson(jsonString,
						VideoResponse.class);

				Event event = new Event(EventType.EDITIPC, true);
				event.setData(videoResponse);
				if (!videoResponse.getResponse_params().getStatus().equals("0")) {
					event.setSuccess(false);
				}
				notifyObservers(event);
			}
		};
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				String errorString = null;
				if (error != null && error.getMessage() != null) {
					Log.e("ResponseError: ", error.getMessage());
					errorString = VolleyErrorHelper.getMessage(error,
							ApplicationController.getInstance());
				}
				Event event = new Event(EventType.EDITIPC, false);
				event.setData(errorString);
				notifyObservers(event);
			}
		};
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "editIPC.cgi", param);

		StringRequest req = new StringRequest(url, responseListener,
				errorListener);
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	private String encodeString(String alias) {
		if (alias == null) {
			return null;
		}
		try {
			alias = URLEncoder.encode(alias, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return alias;
	}

	/***
	 * http://192.168.1.239/cgi-bin/rest/network/deleteIPC.cgi?ipc_id=3
	 * 
	 * @param videoNode
	 */
	public void deleteIPC(VideoNode videoNode) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ipc_id", videoNode.getId());
		String param = hashMap2ParamString(paraMap);
		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new Gson();
				String jsonString = UiUtils.customString(response);
				VideoResponse videoResponse = gson.fromJson(jsonString,
						VideoResponse.class);

				Event event = new Event(EventType.DELETEIPC, true);
				event.setData(videoResponse);
				if (!videoResponse.getResponse_params().getStatus().equals("0")) {
					event.setSuccess(false);
				}
				notifyObservers(event);
			}
		};
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				String errorString = null;
				if (error != null && error.getMessage() != null) {
					Log.e("ResponseError: ", error.getMessage());
					errorString = VolleyErrorHelper.getMessage(error,
							ApplicationController.getInstance());
				}
				Event event = new Event(EventType.DELETEIPC, false);
				event.setData(errorString);
				notifyObservers(event);
			}
		};
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "deleteIPC.cgi", param);
		StringRequest req = new StringRequest(url, responseListener,
				errorListener);
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public VideoResponse handleVideoResponse(String result) {
		String json = UiUtils.formatResponseString(result);
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(json).getAsJsonObject();
		VideoResponse dataEntity = new VideoResponse();
		JsonElement actionElement = jsonObject.get("action");

		ArrayList<VideoNode> list = new ArrayList<VideoNode>();
		JsonArray jsonArray = jsonObject.getAsJsonArray("list");
		if (jsonArray != null) {
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonElement el = jsonArray.get(i);
				VideoNode tmp = gson.fromJson(el, VideoNode.class);
				list.add(tmp);
			}
		}
		JsonElement paramsElement = jsonObject.get("response_params");
		VideoResponseParams params = gson.fromJson(paramsElement,
				VideoResponseParams.class);

		dataEntity.setAction(actionElement.toString());
		dataEntity.setResponse_params(params);
		dataEntity.setList(list);
		Log.i(TAG, dataEntity.getAction());
		return dataEntity;
	}

	class GetVideoListTast extends AsyncTask<String, Object, VideoResponse> {

		@Override
		protected VideoResponse doInBackground(String... params) {
			VideoResponse response = handleVideoResponse(params[0]);

			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();

			// List<VideoNode>
			// mList=DataHelper.getVideoList(ApplicationController.getInstance(),mDateHelper);
			// if
			// (response.getList()!=null&&response.getList().size()!=mList.size())
			// {
			mDateHelper.emptyTable(mSQLiteDatabase, DataHelper.VIDEO_TABLE);
			ArrayList<VideoNode> videoNodesFromSever = decodeAlias2Chinese(response);
			if(videoNodesFromSever!=null) {
				mDateHelper.insertVideoList(mSQLiteDatabase,
						DataHelper.VIDEO_TABLE, null, videoNodesFromSever);
			}
			// }
			// mDateHelper.close(mSQLiteDatabase);
			mSQLiteDatabase.close();
			return response;
		}

		private ArrayList<VideoNode> decodeAlias2Chinese(VideoResponse response) {
			ArrayList<VideoNode> videoNodesFromSever = response.getList();
			if(videoNodesFromSever!=null) {
				for (Iterator<VideoNode> iterator = videoNodesFromSever.iterator(); iterator
						.hasNext();) {
					VideoNode videoNode = (VideoNode) iterator.next();
					try {
						videoNode.setAliases(URLDecoder.decode(
								videoNode.getAliases(), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
				}
			}
			return videoNodesFromSever;
		}

		@Override
		protected void onPostExecute(VideoResponse result) {
			Event event = new Event(EventType.GETVIDEOLIST, true);
			if (result == null || result.getList().size() == 0) {
				event.setSuccess(false);
				notifyObservers(event);
			} else if (result.getList().size() > 0) {
				event.setData(result);
				notifyObservers(event);
			}
			super.onPostExecute(result);
		}

	}
}
