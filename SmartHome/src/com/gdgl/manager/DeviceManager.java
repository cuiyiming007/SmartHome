package com.gdgl.manager;

import java.util.ArrayList;

import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DevParam;
import com.gdgl.mydata.Node;
import com.gdgl.mydata.RespondDataEntity;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.mydata.Weather;
import com.gdgl.network.CustomRequest;
import com.gdgl.network.VolleyOperation;

public class DeviceManager extends Manger {
	private static DeviceManager instance;

	public static DeviceManager getInstance() {
		if (instance == null) {
			instance = new DeviceManager();
		}
		return instance;
	}

	/***
	 * local device data for test
	 */
	public ArrayList<ResponseParamsEndPoint> getDeviceListFromLocalString() {
		RespondDataEntity data = VolleyOperation
				.handleResponseString(Constants.jsonStringforEndpoint);
		ArrayList<ResponseParamsEndPoint> devDataList = data.getResponseparamList();
		
           return devDataList;
	}

	/***
	 * url=http://192.168.1.184/cgi-bin/rest/network/getZBNode.cgi?user_name=
	 * aaaa&callback=1234&enco demethod=NONE&sign=AAA
	 */
	public void getDeviceList() {
		// callbakc listener
		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				RespondDataEntity data = VolleyOperation
						.handleResponseString(response);
				ArrayList<DevParam> devDataList = data.getResponseparamList();
				notifyObservers();
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
