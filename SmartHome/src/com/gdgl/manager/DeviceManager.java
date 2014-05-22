package com.gdgl.manager;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.gdgl.activity.SmartHome;
import com.gdgl.app.ApplicationController;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DevParam;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Node;
import com.gdgl.mydata.RespondDataEntity;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.mydata.Weather;
import com.gdgl.mydata.getlocalcielist.CIEresponse_params;
import com.gdgl.network.CustomRequest;
import com.gdgl.network.VolleyErrorHelper;
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
		RespondDataEntity<ResponseParamsEndPoint> data = VolleyOperation
				.handleResponseString(Constants.jsonStringforEndpointNew);
		ArrayList<ResponseParamsEndPoint> devDataList = data
				.getResponseparamList();

		return devDataList;
	}

	public void getLocalCIEList() {
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

		StringRequest req = new StringRequest(Constants.getLocalCIEList,
				responseListener, errorListener);

		// add the request object to the queue to be executed
		Log.i("request url", Constants.getLocalCIEList);
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/***
	 * url=http://192.168.1.184/cgi-bin/rest/network/getZBNode.cgi?user_name=
	 * aaaa&callback=1234&enco demethod=NONE&sign=AAA
	 */
	// 1,更新数据 2，组网管理
	public void getDeviceList(int type) {
		Listener<String> responseListener = null;
		if (1 == type) {
			responseListener = new Listener<String>() {
				@Override
				public void onResponse(String response) {
					// Time-consuming operation need to use AsyncTask
					new InitialDataTask().execute(response);
				}
			};
		} else if (2 == type) {
			responseListener = new Listener<String>() {
				@Override
				public void onResponse(String response) {
					// Time-consuming operation need to use AsyncTask
					new InitialDataTaskWithNoInsert().execute(response);
				}
			};
		}

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

		StringRequest req = new StringRequest(Constants.getEndPointURl,
				responseListener, errorListener);

		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void getDeviceList() {
		// callbakc listener
		getDeviceList(1);
	}

	class InitialDataTask extends AsyncTask<String, Object, Object> {
		@Override
		protected Object doInBackground(String... params) {
			RespondDataEntity data = VolleyOperation
					.handleResponseString(params[0]);
			ArrayList<ResponseParamsEndPoint> devDataList = data
					.getResponseparamList();

			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
			List<DevicesModel> mList = mDateHelper.queryForList(
					mSQLiteDatabase, DataHelper.DEVICES_TABLE, null, null,
					null, null, null, null, null);
			// 是否数据个数有更新
			if (mList.size() != devDataList.size()) {
				mDateHelper.emptyTable(mSQLiteDatabase,
						DataHelper.DEVICES_TABLE);
				mDateHelper.insertList(mSQLiteDatabase,
						DataHelper.DEVICES_TABLE, null, devDataList);
			}
			// mDateHelper.close(mSQLiteDatabase);
			// [TODO]transfer to SimpleDevicesModel
			return devDataList;
		}

		@Override
		protected void onPostExecute(Object result) {
			Event event = new Event(EventType.INTITIALDVIVCEDATA, true);
			event.setData(result);
			notifyObservers(event);
		}

	}

	class InitialDataTaskWithNoInsert extends AsyncTask<String, Object, Object> {
		@Override
		protected Object doInBackground(String... params) {
			RespondDataEntity data = VolleyOperation
					.handleResponseString(params[0]);
			ArrayList<ResponseParamsEndPoint> devDataList = data
					.getResponseparamList();

			// DataHelper mDateHelper = new DataHelper(
			// ApplicationController.getInstance());
			// SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
			// mDateHelper.insertList(mSQLiteDatabase, DataHelper.DEVICES_TABLE,
			// null, devDataList);
			// [TODO]transfer to SimpleDevicesModel
			return devDataList;
		}

		@Override
		protected void onPostExecute(Object result) {
			Event event = new Event(EventType.INTITIALDVIVCEDATA, true);
			event.setData(result);
			notifyObservers(event);
		}
	}

	class InitialCIETask extends AsyncTask<String, Object, Object> {
		@Override
		protected Object doInBackground(String... params) {
			RespondDataEntity<CIEresponse_params> data = VolleyOperation
					.handleCIEString(params[0]);
			ArrayList<CIEresponse_params> devDataList = data
					.getResponseparamList();

			// DataHelper mDateHelper = new DataHelper(
			// ApplicationController.getInstance());
			// SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
			// mDateHelper.insertList(mSQLiteDatabase, DataHelper.DEVICES_TABLE,
			// null, devDataList);
			// [TODO]transfer to SimpleDevicesModel
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
