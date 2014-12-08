package com.gdgl.libjingle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.gdgl.app.ApplicationController;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.LibjingleSendStructure;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DeviceLearnedParam;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.RespondDataEntity;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.mydata.SimpleResponseData;
import com.gdgl.mydata.Callback.CallbackBindListDevices;
import com.gdgl.mydata.Callback.CallbackBindListMessage;
import com.gdgl.mydata.Region.GetRoomInfo_response;
import com.gdgl.mydata.Region.Room;
import com.gdgl.mydata.Region.RoomData_response_params;
import com.gdgl.mydata.bind.BindResponseData;
import com.gdgl.mydata.binding.BindingDataEntity;
import com.gdgl.mydata.getlocalcielist.CIEresponse_params;
import com.gdgl.mydata.getlocalcielist.LocalIASCIEOperationResponseData;
import com.gdgl.network.VolleyOperation;
import com.gdgl.util.UiUtils;
import com.google.gson.Gson;

/***
 * libjingle通道tcp接收包的处理
 * 
 * @author trice
 * 
 */
public class LibjingleResponseHandlerManager extends Manger {

	private final static String TAG = "LibjingleRecievedResponseHandlerManager";

	private static LibjingleResponseHandlerManager instance;

	static LibjinglePackHandler packHandler;

	public static LibjingleResponseHandlerManager getInstance() {
		if (instance == null) {
			instance = new LibjingleResponseHandlerManager();
		}
		return instance;
	}

	public static void handleInputStream(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[51200];
		int readBytes = 0;
		while ((readBytes = inputStream.read(buffer)) > 0) {
			String response = new String(buffer, 0, readBytes);
			Log.i(TAG, "handleInputStream:" + response);
			try {
				packHandler = new LibjinglePackHandler(response);
				getInstance().recievedPackType(packHandler.gl_msgtype);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// message = UiUtils.formatResponseString(message);
		}
	}

	public void recievedPackType(int msgtype) {
		switch (msgtype) {
		case LibjinglePackHandler.MT_URL:
			httpTypeSwitch(packHandler.request_id);
			break;
		case LibjinglePackHandler.MT_CallBack:
			CallbackManager.getInstance().classifyCallbackResponse(
					packHandler.result);
			break;
		default:
			break;
		}
	}

	public void httpTypeSwitch(int requestid) {
		ArrayList<LibjingleSendStructure> list = LibjingleSendManager.sendList;
		Log.i(TAG, "sendList" + list.size());
		int api_type = -1;
		for (LibjingleSendStructure structure : list) {
			if (structure.getRequest_id() == requestid) {
				api_type = structure.getAPI_type();
				structure.removeLibjingleSendStructure();
				break;
			}
		}
		if (api_type > 0) {
			String response = packHandler.result;
			response = UiUtils.formatResponseString(response);

			switch (api_type) {
			case LibjingleSendStructure.GETENDPOINT:
				new GetEndPointTask().execute(response);
				break;
			case LibjingleSendStructure.ADDBINDDATA:
				Gson gson42 = new Gson();
				BindResponseData statusData42 = gson42.fromJson(
						response.toString(), BindResponseData.class);
				Event event42 = new Event(EventType.BINDDEVICE, true);
				event42.setData(statusData42);
				notifyObservers(event42);
				break;
			case LibjingleSendStructure.GETALLBINDLIST:
				if (response != null && response.length() > 0) {
					new GetBindingTask().execute(response);
				}
				break;
			case LibjingleSendStructure.GETLOCALCIELIST:
				new InitialCIETask().execute(response);
				break;
			case LibjingleSendStructure.LIGHTSENSOROPERATION:
				Gson gson82 = new Gson();
				SimpleResponseData statusData82 = gson82.fromJson(
						response.toString(), SimpleResponseData.class);
				Bundle bundle82 = new Bundle();
				bundle82.putString("IEEE", statusData82.getIeee());
				bundle82.putString("EP", statusData82.getEp());
				bundle82.putString("PARAM", statusData82.getParam1());
				Event event82 = new Event(EventType.LIGHTSENSOROPERATION, true);
				event82.setData(bundle82);
				notifyObservers(event82);
				break;

			case LibjingleSendStructure.TEMPERATURESENSOROPERATION:
				Gson gson81 = new Gson();
				SimpleResponseData statusData81 = gson81.fromJson(
						response.toString(), SimpleResponseData.class);
				Bundle bundle81 = new Bundle();
				bundle81.putString("IEEE", statusData81.getIeee());
				bundle81.putString("EP", statusData81.getEp());
				bundle81.putString("PARAM", statusData81.getParam1());
				Event event81 = new Event(EventType.TEMPERATURESENSOROPERATION,
						true);
				event81.setData(bundle81);
				notifyObservers(event81);
				break;
			case LibjingleSendStructure.GETLOCALIASCIEOPERATION:
				new GetLocalIASCIETask().execute(response);
				break;
			case LibjingleSendStructure.HUMIDITY:
				Gson gson80 = new Gson();
				SimpleResponseData statusData80 = gson80.fromJson(
						response.toString(), SimpleResponseData.class);
				Bundle bundle80 = new Bundle();
				bundle80.putString("IEEE", statusData80.getIeee());
				bundle80.putString("EP", statusData80.getEp());
				bundle80.putString("PARAM", statusData80.getParam1());
				Event event80 = new Event(EventType.HUMIDITY, true);
				event80.setData(bundle80);
				notifyObservers(event80);
				break;
			case LibjingleSendStructure.GETDEVICELEARNEDIRDATAINFORMATION:
				new GetDeviceLearnedTask().execute(response);
				break;
			case LibjingleSendStructure.GETALLROOMINFO:
				new GetAllRoomInfoTask().execute(response);
				break;
			case LibjingleSendStructure.GETEPBYROOMINDEX:
				new GetEPbyRoomIndexTask().execute(response);
				break;
			case LibjingleSendStructure.ZBADDROOMDATAMAIN:
				Gson gson93 = new Gson();
				Log.i(TAG, "addroomdatamain" + response);
				RoomData_response_params data93 = gson93.fromJson(
						response.toString(), RoomData_response_params.class);
				String status93 = data93.getstatus();
				Event event93 = new Event(EventType.ROOMDATAMAIN, true);
				event93.setData(status93);
				notifyObservers(event93);
				break;
			case LibjingleSendStructure.ZBDELETEROOMDATAMAINBYID:
				Gson gson94 = new Gson();
				Log.i(TAG, "addroomdatamain" + response);
				RoomData_response_params data94 = gson94.fromJson(
						response.toString(), RoomData_response_params.class);
				String status94 = data94.getstatus();
				Event event94 = new Event(EventType.ROOMDATAMAIN, true);
				event94.setData(status94);
				notifyObservers(event94);
				break;
			case LibjingleSendStructure.MODIFYDEVICEROOMID:
				Gson gson95 = new Gson();
				RoomData_response_params data95 = gson95.fromJson(
						response.toString(), RoomData_response_params.class);
				String status95 = data95.getstatus();
				Event event95 = new Event(EventType.MODIFYDEVICEROOMID, true);
				event95.setData(status95);
				notifyObservers(event95);
				break;

			case LibjingleSendStructure.DELBINDDATA:
			case LibjingleSendStructure.MANAGELEAVENODE:
			case LibjingleSendStructure.SETPERMITJOINON:
			case LibjingleSendStructure.SETALLPERMITJOINON:
			case LibjingleSendStructure.MAINSOUTLETOPERATION:
			case LibjingleSendStructure.IASWARNINGDEVICEOPERATION:
			case LibjingleSendStructure.ONOFFOUTPUTOPERATION:
			case LibjingleSendStructure.DIMMABLELIGHTOPERATION:
			case LibjingleSendStructure.SHADEOPERATION:
			case LibjingleSendStructure.LOCALIASCIEOPERATION:
			case LibjingleSendStructure.LOCALIASCIEBYPASSZONE:
			case LibjingleSendStructure.LOCALIASCIEUNBYPASSZONE:
			case LibjingleSendStructure.BEGINLEARNIR:
			case LibjingleSendStructure.BEGINAPPLYIR:
			case LibjingleSendStructure.DELETEIR:
			case LibjingleSendStructure.IDENTIFYDEVICE:
				break;
			default:
				break;
			}
		}
	}

	class GetEndPointTask extends AsyncTask<String, Object, Object> {
		@Override
		protected Object doInBackground(String... params) {
			RespondDataEntity<ResponseParamsEndPoint> data = VolleyOperation
					.handleEndPointString(params[0]);
			ArrayList<ResponseParamsEndPoint> devDataList = data
					.getResponseparamList();

			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();

			mDateHelper.emptyTable(mSQLiteDatabase, DataHelper.DEVICES_TABLE);
			mDateHelper.insertEndPointList(mSQLiteDatabase,
					DataHelper.DEVICES_TABLE, null, devDataList);
			mSQLiteDatabase.close();
			return devDataList;
		}

		@Override
		protected void onPostExecute(Object result) {
			Event event = new Event(EventType.INTITIALDVIVCEDATA, true);
			event.setData(result);
			notifyObservers(event);
		}
	}

	class GetBindingTask extends AsyncTask<String, Object, Object> {
		@Override
		protected Object doInBackground(String... params) {
			BindingDataEntity data = VolleyOperation
					.handleBindingString(params[0]);
			if (data != null) {
				ArrayList<CallbackBindListMessage> bindingInfo = data
						.getResponse_paramsList();

				DataHelper mDateHelper = new DataHelper(
						ApplicationController.getInstance());
				SQLiteDatabase mSQLiteDatabase = mDateHelper
						.getSQLiteDatabase();

				mSQLiteDatabase.beginTransaction();
				try {
					mSQLiteDatabase.delete(DataHelper.BIND_TABLE, null, null);
					for (CallbackBindListMessage bindingParam : bindingInfo) {
						ArrayList<CallbackBindListDevices> mBindedDevicesList = bindingParam
								.getList();
						if (mBindedDevicesList != null
								&& mBindedDevicesList.size() > 0) {
							for (CallbackBindListDevices bindingDivice : mBindedDevicesList) {
								ContentValues c = new ContentValues();
								c.put(BindingDataEntity.DEVOUT_IEEE,
										bindingParam.getIeee());
								c.put(BindingDataEntity.DEVOUT_EP,
										bindingParam.getEp());
								c.put(BindingDataEntity.DEVIN_IEEE,
										bindingDivice.getIeee());
								c.put(BindingDataEntity.DEVIN_EP,
										bindingDivice.getEp());
								c.put(BindingDataEntity.CLUSTER,
										bindingDivice.getCid());

								mSQLiteDatabase.insert(DataHelper.BIND_TABLE,
										null, c);
							}
						}
					}
					mSQLiteDatabase.setTransactionSuccessful();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					mSQLiteDatabase.endTransaction();
					mSQLiteDatabase.close();
				}
			}
			return 1;
		}

		@Override
		protected void onPostExecute(Object result) {

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

	class GetLocalIASCIETask extends AsyncTask<String, Object, Void> {
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			Gson gson = new Gson();
			LocalIASCIEOperationResponseData data = gson.fromJson(params[0],
					LocalIASCIEOperationResponseData.class);
			String status = data.getResponse_params().getParam1().trim();
			Log.i(TAG,
					"LocalIASCIEOperation get status is "
							+ String.valueOf(status));
			int value = Integer.parseInt(status);
			switch (value) {
			case 0:
				status = "0";
				break;
			case 3:
				status = "1";
			default:
				break;
			}
			DataHelper mDataHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();

			String where = " model_id like ? ";
			String[] args = { DataHelper.One_key_operator + "%" };
			ContentValues c = new ContentValues();
			c.put(DevicesModel.ON_OFF_STATUS, status);

			mDataHelper.update(mSQLiteDatabase, DataHelper.DEVICES_TABLE, c,
					where, args);
			mDataHelper.close(mSQLiteDatabase);
			return null;
		}
	}

	class GetDeviceLearnedTask extends AsyncTask<String, Object, Object> {

		@Override
		protected Object doInBackground(String... params) {
			RespondDataEntity<DeviceLearnedParam> dataEntity = VolleyOperation
					.handleDeviceLearnedString(params[0]);
			return dataEntity;
		}

		@Override
		protected void onPostExecute(Object result) {

			Event event = new Event(EventType.GETDEVICELEARNED, true);
			event.setData(result);
			notifyObservers(event);
		}
	}

	class GetAllRoomInfoTask extends AsyncTask<String, Object, List<Room>> {
		@Override
		protected List<Room> doInBackground(String... params) {
			RespondDataEntity<GetRoomInfo_response> data = VolleyOperation
					.handleRoomInfoString(params[0]);
			ArrayList<GetRoomInfo_response> roomInfoList = data
					.getResponseparamList();
			ArrayList<Room> roomList = new ArrayList<Room>();
			for (GetRoomInfo_response info : roomInfoList) {
				roomList.add(info.getroom());
			}
			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();

			mDateHelper.emptyTable(mSQLiteDatabase, DataHelper.ROOMINFO_TABLE);
			mDateHelper.insertRoomInfoList(mSQLiteDatabase,
					DataHelper.ROOMINFO_TABLE, null, roomInfoList);
			mSQLiteDatabase.close();
			return roomList;
		}

		@Override
		protected void onPostExecute(List<Room> result) {
			Event event = new Event(EventType.GETALLROOM, true);
			event.setData(result);
			notifyObservers(event);
		}
	}

	class GetEPbyRoomIndexTask extends AsyncTask<String, Object, Object> {
		@Override
		protected Object doInBackground(String... params) {
			RespondDataEntity<ResponseParamsEndPoint> data = VolleyOperation
					.handleEndPointString(params[0]);
			ArrayList<ResponseParamsEndPoint> devDataList = data
					.getResponseparamList();

			return devDataList;
		}

		@Override
		protected void onPostExecute(Object result) {
			Event event = new Event(EventType.GETEPBYROOMINDEX, true);
			event.setData(result);
			notifyObservers(event);
		}

	}
}
