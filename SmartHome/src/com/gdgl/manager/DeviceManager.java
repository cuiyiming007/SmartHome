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
import com.gdgl.activity.SmartHome;
import com.gdgl.activity.JoinNetFragment.InsertTask;
import com.gdgl.app.ApplicationController;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
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
	
//	/***
//	 * 扫描到的设备
//	 */
//	List<DevicesModel> mNewDevList;
	
	/***
	 * 从数据库中读出的设备列表
	 */
	List<SimpleDevicesModel> mInnetListFromDB;

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
	 * url=http://192.168.1.184/cgi-bin/rest/network/getZBNode.cgi?user_name=
	 * aaaa&callback=1234&enco demethod=NONE&sign=AAA
	 */
	// 1,更新数据 2，组网管理
	public void getDeviceList(int type) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		
		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);
		
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "getendpoint.cgi",param);
		
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

		StringRequest req = new StringRequest(url,
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
					.handleEndPointString(params[0]);
			ArrayList<ResponseParamsEndPoint> devDataList = data
					.getResponseparamList();

			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
			List<DevicesModel> mList = mDateHelper.queryForDevicesList(
					mSQLiteDatabase, DataHelper.DEVICES_TABLE, null, null,
					null, null, null, null, null);
			// 是否数据个数有更新
//			if (mList.size() != devDataList.size()) {
				mDateHelper.emptyTable(mSQLiteDatabase,
						DataHelper.DEVICES_TABLE);
				mDateHelper.insertEndPointList(mSQLiteDatabase,
						DataHelper.DEVICES_TABLE, null, devDataList);
//			}
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
					.handleEndPointString(params[0]);
			ArrayList<ResponseParamsEndPoint> devDataList = data
					.getResponseparamList();
			DataHelper mDH = new DataHelper(ApplicationController.getInstance());
			mInnetListFromDB = DataUtil.getDevices(ApplicationController.getInstance(), mDH, null, null, false);
//			mNewDevList = new ArrayList<DevicesModel>();
//			mAddDevList = new ArrayList<DevicesModel>();
			
			if (null == allList || allList.size() == 0) {
				Log.i("scape devices", "SCAPDEV-> the first scape,get "
						+ devDataList.size() + " result");
				allList = devDataList;
			} else {
				for (ResponseParamsEndPoint responseParamsEndPoint : devDataList) {
					if (!isInAllList(responseParamsEndPoint)) {
						Log.i("scape devices",
								"SCAPDEV-> get a devices not in allList,add");
						allList.add(responseParamsEndPoint);
					}
				}
			}
			//对比数据库，返回新增的设备列表
			ArrayList<DevicesModel> scapedList=getNewDeviceListByCompareDB();
			

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
	/***
	 * 判断该设备是否在之前读到的服务器列表allList里面
	 * @param responseParamsEndPoint
	 * @return
	 */
	public boolean isInAllList(ResponseParamsEndPoint responseParamsEndPoint) {
		if (null == allList || allList.size() == 0) {
			return false;
		}
		for (ResponseParamsEndPoint rp : allList) {
			if (rp.getDevparam()
					.getNode()
					.getIeee()
					.equals(responseParamsEndPoint.getDevparam().getNode()
							.getIeee())
					&& rp.getDevparam()
							.getEp()
							.equals(responseParamsEndPoint.getDevparam()
									.getEp())) {
				return true;
			}
		}
		return false;
	}
	public void scapeDevice(ArrayList<ResponseParamsEndPoint> devDataList)
	{
		for (ResponseParamsEndPoint responseParamsEndPoint : devDataList) {
			if (!isInAllList(responseParamsEndPoint)) {
				Log.i("scape devices",
						"SCAPDEV-> get a devices not in allList,add");
				allList.add(responseParamsEndPoint);
			}
		}
	}
	
	private ArrayList<DevicesModel> getNewDeviceListByCompareDB() {
		/***
		 * 扫描到的设备
		 */
		ArrayList<DevicesModel> newDevList=new ArrayList<DevicesModel>();
		if (null != allList && allList.size() > 0) {
			mDevList = DataHelper.convertToDevicesModel(allList);
		}
		
		newDevList.clear();
		if (null != mDevList && mDevList.size() > 0) {
			for (DevicesModel dm : mDevList) {
				if (!isInDB(dm)) {
					if (!(dm.getmIeee().trim().equals("00137A0000010264") && dm
							.getmEP().trim().equals("0A"))) {
						dm.setmUserDefineName(DataUtil.getDefaultUserDefinname(
								ApplicationController.getInstance(), dm.getmModelId()));
						newDevList.add(dm);
//						mAddDevList.add(dm);
					}
				}
			}
		}
		if (newDevList.size() > 0) {
//			totlaDevices += mNewDevList.size();
			SimpleDevicesModel sd;
			for (DevicesModel dm : newDevList) {
				sd = new SimpleDevicesModel();
				sd.setmIeee(dm.getmIeee());
				sd.setmEP(dm.getmEP());
				sd.setmDeviceId(Integer.parseInt(dm.getmDeviceId()));
				sd.setmDeviceRegion(dm.getmDeviceRegion());
				sd.setmRid(dm.getmRid());
				sd.setmUserDefineName(dm.getmUserDefineName());
				sd.setmModelId(dm.getmModelId());
				sd.setmName(dm.getmName());
				sd.setmNodeENNAme(dm.getmNodeENNAme());
				sd.setmOnOffLine(dm.getmOnOffLine());
				mInnetListFromDB.add(sd);
			}
//			new InsertTask().execute(mNewDevList);
		}
		return newDevList;

	}
	
	public boolean isInDB(DevicesModel s) {

		for (SimpleDevicesModel sd : mInnetListFromDB) {
			if (sd.getmIeee().trim().equals(s.getmIeee().trim())
					&& sd.getmEP().trim().equals(s.getmEP().trim())) {
				return true;
			}
		}
		Log.i("new enroll device",
				"ieee: " + s.getmIeee() + " ep:" + s.getmEP());
		return false;
	}

}
