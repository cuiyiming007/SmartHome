package com.gdgl.manager;

import java.util.HashMap;

import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.gdgl.app.ApplicationController;
import com.gdgl.network.StringRequestChina;
import com.gdgl.util.NetUtil;

public class SceneLinkageManager extends Manger {

	private final static String TAG = "SceneLinkageManager";

	private static SceneLinkageManager instance;

	public static SceneLinkageManager getInstance() {
		if (instance == null) {
			instance = new SceneLinkageManager();
		}
		return instance;
	}

	public void AddScene(String scnname, String scnaction, int scnindex) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("scnname", scnname);
		paraMap.put("scnaction", scnaction);
		paraMap.put("scnindex", Integer.toString(scnindex));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "AddScene.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
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
	
	public void EditScene(String scnname, String scnaction, int scnindex, int sid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("scnname", scnname);
		paraMap.put("scnaction", scnaction);
		paraMap.put("scnindex", Integer.toString(scnindex));
		paraMap.put("sid", Integer.toString(sid));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "EditScene.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
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
	
	public void DelScene(int sid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("sid", Integer.toString(sid));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "DelScene.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
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
	
	public void GetSceneList() {
		String url = NetUtil.getInstance().getVideoURL(
				NetUtil.getInstance().IP, "DelScene.cgi");

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
}
