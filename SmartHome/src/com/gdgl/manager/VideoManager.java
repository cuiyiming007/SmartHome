package com.gdgl.manager;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.util.Log;

import com.gdgl.mydata.Constants;
import com.gdgl.mydata.RespondDataEntity;
import com.gdgl.mydata.ResponseParams;
import com.gdgl.mydata.video.VideoNode;
import com.gdgl.mydata.video.VideoResponse;
import com.gdgl.mydata.video.VideoResponseParams;
import com.gdgl.util.UiUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class VideoManager extends Manger{
	public final static String TAG = "VideoManager";

	private static VideoManager instance;

	public static  VideoManager getInstance() {
		if (instance == null) {
			instance = new VideoManager();
		}
		return instance;
	}

	public VideoResponse handleVideoResponse()
	{
		String json=UiUtils.formatResponseString(Constants.jsonStringforgetIPClist);
//		VideoResponse videoResponse=gson.fromJson(json, VideoResponse.class);
		
		
		
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
}
