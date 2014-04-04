package com.gdgl.manager;

import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.Node;
import com.gdgl.mydata.Weather;
import com.gdgl.network.CustomRequest;
import com.gdgl.network.VolleyOperation;

public class NodeManager {
	private static NodeManager instance = new NodeManager();
	Weather weather=null;
	public static NodeManager getInstance() {
		return instance;
	}
	/***
	 * url=http://192.168.1.184/cgi-bin/rest/network/getZBNode.cgi?user_name=aaaa&callback=1234&enco demethod=NONE&sign=AAA
	 */
	public void getZBNode()
	{
		// callbakc listener
		Listener<String> responseListener = new Listener<String>() {
					@Override
					public void onResponse(String response) {
				VolleyOperation.handleResponseString(response);
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
	
	

}
