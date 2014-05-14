package com.gdgl.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.gdgl.app.ApplicationController;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getlocalcielist.LocalIASCIEOperationResponseData;
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

public class VideoManager extends Manger{
	public final static String TAG = "VideoManager";

	private static VideoManager instance;

	public static  VideoManager getInstance() {
		if (instance == null) {
			instance = new VideoManager();
		}
		return instance;
	}
	public void getIPClist() {

		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				new GetVideoListTast().execute(response);
			}
		};
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if (error!=null&&error.getMessage()!=null) {
					Log.e("ResponseError: ", error.getMessage());
					VolleyErrorHelper.getMessage(error, ApplicationController.getInstance());
					}
			}
		};
		StringRequest req = new StringRequest(Constants.getVideoListURL, responseListener,
				errorListener);
		ApplicationController.getInstance().addToRequestQueue(req);
	}
	public VideoResponse handleVideoResponse(String result)
	{
		String json=UiUtils.formatResponseString(result);
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(json).getAsJsonObject();
		VideoResponse dataEntity = new VideoResponse();
		JsonElement actionElement=jsonObject.get("action");
		
		ArrayList<VideoNode> list = new ArrayList<VideoNode>();
		JsonArray jsonArray = jsonObject.getAsJsonArray("list");
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonElement el = jsonArray.get(i);
			VideoNode tmp = gson.fromJson(el, VideoNode.class);
			list.add(tmp);
		}
		JsonElement paramsElement=jsonObject.get("response_params");
		VideoResponseParams params=gson.fromJson(paramsElement, VideoResponseParams.class);
		
		
		dataEntity.setAction(actionElement.toString());
		dataEntity.setResponse_params(params);
		dataEntity.setList(list);
		Log.i(TAG, dataEntity.getAction());
		return dataEntity;
	}
	class GetVideoListTast extends AsyncTask<String, Object, VideoResponse>
	{

		@Override
		protected VideoResponse doInBackground(String... params) {
			VideoResponse response=handleVideoResponse(params[0]);
			
			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper
					.getSQLiteDatabase();
			mDateHelper.emptyTable(mSQLiteDatabase, DataHelper.VIDEO_TABLE);
			if (response.getList()!=null&&response.getList().size()>0) {
				mDateHelper.insertVideoList(mSQLiteDatabase, DataHelper.VIDEO_TABLE,
						null, response.getList());
			}
//			mDateHelper.close(mSQLiteDatabase);
			return response;
		}
		@Override
		protected void onPostExecute(VideoResponse result) {
			Event event = new Event(EventType.GETVIDEOLIST, true);
			if (result==null||result.getList().size()==0) {
				event.setSuccess(false);
				notifyObservers(event);
			}else if (result.getList().size()>0) {
				event.setData(result);
				notifyObservers(event);
			}
			super.onPostExecute(result);
		}

	}
}
