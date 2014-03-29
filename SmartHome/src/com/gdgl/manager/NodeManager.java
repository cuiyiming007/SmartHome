package com.gdgl.manager;

import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.Node;
import com.gdgl.mydata.Weather;
import com.gdgl.network.CustomRequest;

public class NodeManager {
	private static NodeManager instance = new NodeManager();
	Weather weather=null;
	public static NodeManager getInstance() {
		return instance;
	}
	
	public Weather getNodeFromValley()
	{
	//	 
		Listener<Weather> respondListener=new Listener<Weather>() {
			@Override
			public void onResponse(Weather arg0) {
				weather=arg0;
			}
		};
		ErrorListener errorListener=new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Error: ", error.getMessage());
			}
		};
		CustomRequest<Weather> request=new CustomRequest<Weather>(Constants.jasonURLforWeather,"weatherinfo", Weather.class, respondListener, errorListener);
		ApplicationController.getInstance().addToRequestQueue(request);
		return weather;
	}

}
