package com.gdgl.manager;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.RespondDataEntity;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.network.StringRequestChina;
import com.gdgl.network.VolleyOperation;
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
						new GetRFDevListTask().execute(response);
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
	
	//===20150818王晓飞===RF设备布撤防
	public void ChangeRFDevArmState(int rfid, int state) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("rfid", rfid+"");
		paraMap.put("state", state+"");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "ChangeRFDevArmState.cgi", param);
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub
					}
					
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						
					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}
	//====开始停止RF警号报警
	public void RFWarningDevOperation(int rfid, int operatortype,int param1) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("rfid", rfid+"");
		paraMap.put("operatortype", operatortype+"");
		paraMap.put("param1", param1+"");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "RFWarningDevOperation.cgi", param);
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub
					}
					
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						
					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}
	//====RF设备启用禁止配置
	public void ChangeRFDevActivationState(int rfid, int state) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("rfid", rfid+"");
		paraMap.put("state", state+"");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "ChangeRFDevActivationState.cgi", param);
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub
					}
					
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						
					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}
	//===RF设备全部布防撤防
	public void ChangeAllRFDevArmState(int state) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("state", state+"");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "ChangeAllRFDevArmState.cgi", param);
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub
					}
					
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						
					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}
	
	
	
	
	class GetRFDevListTask extends AsyncTask<String, Object, Object> {
		@Override
		protected Object doInBackground(String... params) {
			RespondDataEntity<ResponseParamsEndPoint> data = VolleyOperation
					.handleEndPointString(params[0]);
			ArrayList<ResponseParamsEndPoint> devDataList = data
					.getResponseparamList();

			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();

			mDateHelper.emptyTable(mSQLiteDatabase,DataHelper.RF_DEVICES_TABLE);
			mDateHelper.insertEndPointList(mSQLiteDatabase,DataHelper.RF_DEVICES_TABLE, null, devDataList);
			mSQLiteDatabase.close();
			return devDataList;
		}
	}
}
