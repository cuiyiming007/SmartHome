package com.gdgl.manager;

import java.util.HashMap;

import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.gdgl.app.ApplicationController;
import com.gdgl.network.StringRequestChina;
import com.gdgl.util.NetUtil;

public class RfCGIManager extends Manger{
	private final static String TAG = "CGIManager";

	private static RfCGIManager instance;

	public static RfCGIManager getInstance() {
		if (instance == null) {
			instance = new RfCGIManager();
		}
		return instance;
	}
	
	public void GetRFDevList() {
		String url = NetUtil.getInstance().getVideoURL(
				NetUtil.getInstance().IP, "GetRFDevList.cgi");
		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}
	
	public void ChangeRFDevName(int rfid, String name) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("rfid", rfid+"");
		paraMap.put("name", Uri.encode(name.replace(" ", "%20")));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "ChangeRFDevName.cgi", param);
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						
					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}
	
	
}
