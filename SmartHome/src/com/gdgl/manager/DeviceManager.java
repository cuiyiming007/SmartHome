package com.gdgl.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.gdgl.app.ApplicationController;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.RespondDataEntity;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.mydata.Callback.CallbackJoinNetMessage;
import com.gdgl.mydata.getlocalcielist.CIEresponse_params;
import com.gdgl.network.VolleyErrorHelper;
import com.gdgl.network.VolleyOperation;
import com.gdgl.util.NetUtil;

public class DeviceManager extends Manger {
	private static DeviceManager instance;
	/***
	 * 从服务器获得的所有device列表，第一次扫描时赋值，用于对比出后来扫描到的设备
	 */
	ArrayList<ResponseParamsEndPoint> allList;
	
	/***
	 * 从allList转换后的对象层次较为简单的所有device列表，跟服务器数据一致
	 */
	List<DevicesModel> mDevList;
	
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
		RespondDataEntity<ResponseParamsEndPoint> data = VolleyOperation
				.handleEndPointString(Constants.jsonStringforEndpointNew);
		ArrayList<ResponseParamsEndPoint> devDataList = data
				.getResponseparamList();

		return devDataList;
	}

	public void getLocalCIEList() {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		
		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);
		
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "GetLocalCIEList.cgi",param);
		
		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				new InitialCIETask().execute(response);
			}
		};

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// Log.e("Error: ", error.getMessage());
			}
		};

		StringRequest req = new StringRequest(url,
				responseListener, errorListener);

		// add the request object to the queue to be executed
		Log.i("request url", url);
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/***
	 * getEndPoint
	 * url=http://192.168.1.184/cgi-bin/rest/network/getendpoint.cgi?user_name=
	 * aaaa&callback=1234&enco demethod=NONE&sign=AAA
	 */
	// 更新数据 
	public void getDeviceEndPoint() {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);
		
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "getendpoint.cgi",param);
		
		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// Time-consuming operation need to use AsyncTask
				new GetEndPointTask().execute(response);
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

		StringRequest req = new StringRequest(url,responseListener, errorListener);

		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}
	//组网管理得到新入网的设备
	public void getNewJoinNetDevice(CallbackJoinNetMessage joinNetMessage) {
		// callback listener29
		final String newdeviceieee=joinNetMessage.getDevice().getieee();
		final String newdeviceEp=joinNetMessage.getDevice().getEP();
		
		HashMap<String, String> paraMap = new HashMap<String, String>();
		
		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);
		
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "getendpoint.cgi",param);
		
		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Bundle bundle=new Bundle();
				bundle.putString("devicelist", response);
				bundle.putString("newdeviceieee", newdeviceieee);
				bundle.putString("newdeviceep", newdeviceEp);
				// Time-consuming operation need to use AsyncTask
				new GetJoinNetDeviceTask().execute(bundle);
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

		StringRequest req = new StringRequest(url,
				responseListener, errorListener);

		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	class GetEndPointTask extends AsyncTask<String, Object, Object> {
		@Override
		protected Object doInBackground(String... params) {
			RespondDataEntity data = VolleyOperation
					.handleEndPointString(params[0]);
			ArrayList<ResponseParamsEndPoint> devDataList = data
					.getResponseparamList();

			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();

			mDateHelper.emptyTable(mSQLiteDatabase,DataHelper.DEVICES_TABLE);
			mDateHelper.insertEndPointList(mSQLiteDatabase,DataHelper.DEVICES_TABLE, null, devDataList);
			return devDataList;
		}

		@Override
		protected void onPostExecute(Object result) {
			Event event = new Event(EventType.INTITIALDVIVCEDATA, true);
			event.setData(result);
			notifyObservers(event);
		}

	}

	class GetJoinNetDeviceTask extends AsyncTask<Bundle, Object, Object> {
		@Override
		protected Object doInBackground(Bundle... params) {
			Bundle bundle=params[0];
			String newDeviceIeee=bundle.getString("newdeviceieee");
			String newDeviceEp=bundle.getString("newdeviceep");
			
			RespondDataEntity data = VolleyOperation
					.handleEndPointString(bundle.getString("devicelist"));
			ArrayList<ResponseParamsEndPoint> devDataList = data
					.getResponseparamList();
			
			List<DevicesModel> devModelList=DataHelper.convertToDevicesModel(devDataList);
			
			ArrayList<DevicesModel> scapedList=new ArrayList<DevicesModel>();
			
			for (DevicesModel devicesModel : devModelList) {
				if(devicesModel.getmIeee().equals(newDeviceIeee)&&devicesModel.getmEP().equals(newDeviceEp)) {
					devicesModel.setmUserDefineName(DataUtil.getDefaultUserDefinname(
							ApplicationController.getInstance(), devicesModel.getmModelId()));
					scapedList.add(devicesModel);
				}
			}
		
			return scapedList;
		}

		@Override
		protected void onPostExecute(Object result) {
			ArrayList<DevicesModel> scapedList=(ArrayList<DevicesModel>) result;
			//扫描到设备
			if (null != scapedList && scapedList.size() > 0) {
//				updateScapeSuccessful();
				Event event = new Event(EventType.SCAPEDDEVICE, true);
				event.setData(scapedList);
				notifyObservers(event);
			}
		}
	}

	class InitialCIETask extends AsyncTask<String, Object, Object> {
		@Override
		protected Object doInBackground(String... params) {
			RespondDataEntity<CIEresponse_params> data = VolleyOperation
					.handleCIEString(params[0]);
			ArrayList<CIEresponse_params> devDataList = data
					.getResponseparamList();

			return devDataList;
		}

		@Override
		protected void onPostExecute(Object result) {
			Event event = new Event(EventType.GETICELIST, true);
			event.setData(result);
			notifyObservers(event);
		}

	}
}
