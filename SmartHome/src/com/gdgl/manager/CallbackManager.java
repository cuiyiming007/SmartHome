package com.gdgl.manager;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.gdgl.activity.ShowDevicesGroupFragmentActivity;
import com.gdgl.app.ApplicationController;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Callback.CallbackBeginLearnIRMessage;
import com.gdgl.mydata.Callback.CallbackBindListDevices;
import com.gdgl.mydata.Callback.CallbackBindListMessage;
import com.gdgl.mydata.Callback.CallbackEnrollMessage;
import com.gdgl.mydata.Callback.CallbackJoinNetMessage;
import com.gdgl.mydata.Callback.CallbackResponseCommon;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.mydata.Callback.CallbackWarnMessage;
import com.gdgl.mydata.binding.BindingDataEntity;
import com.gdgl.network.VolleyOperation;
import com.gdgl.smarthome.R;
import com.gdgl.util.NetUtil;
import com.gdgl.util.UiUtils;
import com.google.gson.Gson;

public class CallbackManager extends Manger {
	private final static String TAG = "CallbackManager";
	private static CallbackManager instance;
	DataHelper mDateHelper = new DataHelper(ApplicationController.getInstance());

	public static CallbackManager getInstance() {
		if (instance == null) {
			instance = new CallbackManager();
		}
		return instance;
	}

	public void startConnectServerByTCPTask() {
		ConnectServerByTCPTask connectServerByTCPTask = new ConnectServerByTCPTask();
		connectServerByTCPTask.start();
	}

	public void stopConnectServerByTCPTask() {
		NetUtil.getInstance().initalCallbackSocket();
	}

	class ConnectServerByTCPTask extends Thread {

		@Override
		public void run() {
			connectAndRecieveFromCallback();
		}

	}

	public void connectAndRecieveFromCallback() {
		// try {
		Log.i(TAG, "start ConnectServerByTCPTask");
		NetUtil.getInstance().initalCallbackSocket();
		NetUtil.getInstance().connectServerWithTCPSocket();
		NetUtil.getInstance().recieveFromCallback();
		// } catch (Exception e) {
		// Log.e(TAG, "connectAndRecieveFromCallback error：" + e.getMessage());
		// }
	}

	public void classifyCallbackResponse(String message) {
		if (message.contains("}{")) {
			String messages[] = message.split("\\}\\{");
			for (int i = 0; i < messages.length; i++) {
				String json = null;
				if (i == 0) {
					json = messages[i] + "}";
				} else if (i == messages.length - 1) {
					json = "{" + messages[i];
				} else {
					json = "{" + messages[i] + "}";
				}
				Log.i("Callbackjson", json);
				handleCallbackResponse(json);
			}
		} else {
			handleCallbackResponse(message);
		}
	}

	public void handleCallbackResponse(String response) {
		try {
			Gson gson = new Gson();
			JSONObject jsonRsponse = new JSONObject(response);
			int msgType = (Integer) jsonRsponse.get("msgtype");
			//Log.i(TAG, "Callback msgType=" + msgType);
			switch (msgType) {
			case 1:
				Log.i(TAG, "Callback msgType=" + msgType + "heartbeat");
				break;
			case 2:
				CallbackResponseType2 common = gson.fromJson(response,
						CallbackResponseType2.class);
				handleAttribute(common);

				// Log.i(TAG, "Callback msgType=" + msgType +
				// "energy"+common.toString());
				break;
			case 3:
				Log.i(TAG, "Callback msgType=" + msgType + "warm message");
				CallbackWarnMessage warmmessage = gson.fromJson(response,
						CallbackWarnMessage.class);
				Log.i(TAG, warmmessage.toString());
				handlerWarnMessage(warmmessage);
				break;
			case 4:
				Log.i(TAG, "Callback msgType=" + msgType + "doorlock");
				break;
			case 5:
				CallbackResponseCommon iasZone = gson.fromJson(response,
						CallbackResponseCommon.class);
				Log.i(TAG,
						"Callback msgType=" + msgType + "IASZONE"
								+ iasZone.toString());
				Intent i5 = new Intent(ApplicationController.getInstance(),
						ShowDevicesGroupFragmentActivity.class);
				i5.putExtra(
						ShowDevicesGroupFragmentActivity.ACTIVITY_SHOW_DEVICES_TYPE,
						UiUtils.SECURITY_CONTROL);
				makeNotify(i5, iasZone.getValue(), iasZone.toString());
				break;
			case 6:
				Log.i(TAG, "Callback msgType=" + msgType + "DimmerSwitch");
				break;
			case 7:
				 Log.i(TAG, "Callback msgType=" + msgType + "OnOffLightSwitch");
				 JSONObject json = new JSONObject(response);
				 if(json.getInt("callbackType") == 3){
					 CallbackResponseCommon iasZone7 = gson.fromJson(response,
								CallbackResponseCommon.class);
					 handlerCallbackResponseCommon(iasZone7);
				 }
				break;
			case 8:
				CallbackResponseCommon IASWarmingDevice = gson.fromJson(
						response, CallbackResponseCommon.class);
				Log.i(TAG, "Callback msgType=" + msgType + "IASWarmingDevice"
						+ IASWarmingDevice.toString());
				break;
			case 9:
				Log.i(TAG, "Callback msgType=" + msgType + "OnOffSwitch");
				break;
			case 10:
				// CallbackResponseCommon onoff_outputDevice = gson.fromJson(
				// response, CallbackResponseCommon.class);
				// Log.i(TAG, "Callback msgType=" + msgType +
				// "OnOffOutPut"+onoff_outputDevice.toString());
				break;
			// need to distinguish with type 5
			case 11:
				CallbackResponseCommon iasZone11 = gson.fromJson(response,
						CallbackResponseCommon.class);
				Log.i(TAG, "Callback msgType=" + msgType + "IASZONE"
						+ iasZone11.toString());
				Intent i11 = new Intent(ApplicationController.getInstance(),
						ShowDevicesGroupFragmentActivity.class);
				i11.putExtra(
						ShowDevicesGroupFragmentActivity.ACTIVITY_SHOW_DEVICES_TYPE,
						UiUtils.SECURITY_CONTROL);
				makeNotify(i11, iasZone11.getValue(), iasZone11.toString());

				break;
			case 12:
				CallbackBeginLearnIRMessage learnIR = gson.fromJson(response,
						CallbackBeginLearnIRMessage.class);
				Log.i(TAG, "Callback msgType=" + msgType + "Brand Style"
						+ learnIR.toString());
				Log.i("CallbackManager BeginLearnIR Response:%n %s",
						learnIR.toString());
				Event event12 = new Event(EventType.BEGINLEARNIR, true);
				event12.setData(learnIR);
				notifyObservers(event12);
				break;
			case 13:
				Log.i(TAG, "Callback msgType=" + msgType
						+ " low tension warming");
				break;
			case 14:
				Log.i(TAG, "Callback msgType=" + msgType + " Bind result");
				break;
			case 15:
				Log.i(TAG, "Callback msgType=" + msgType + " UnBind result");
				break;
			case 16:
				CallbackEnrollMessage enrollMessage = gson.fromJson(response,
						CallbackEnrollMessage.class);
				Log.i(TAG, "Callback msgType=" + msgType + " Enroll"
						+ enrollMessage.toString());
				Event event16 = new Event(EventType.ENROLL, true);
				event16.setData(enrollMessage);
				notifyObservers(event16);
				break;
			case 17:
				Log.i(TAG, "Callback msgType=" + msgType + " unEnroll");
				break;
			case 18:
				Log.i(TAG, "Callback msgType=" + msgType + " Change to roomID");
				break;
			case 19:
				Log.i(TAG, "Callback msgType=" + msgType + " ScenceSelector");
				break;
			case 28:
				Log.i(TAG, "Callback msgType=" + msgType + response);
				new CallbackBindTask().execute(response);
				break;
			case 29:
				final CallbackJoinNetMessage joinNetMessage = gson.fromJson(
						response, CallbackJoinNetMessage.class);
				Log.i(TAG, "Callback msgType=" + msgType + " jionnet"
						+ joinNetMessage.toString());
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(16000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						DeviceManager.getInstance().getNewJoinNetDevice(
								joinNetMessage);
					}
				}).start();

				break;
			case 31:

				String ieee31 = (String) jsonRsponse.get("IEEE");
				String ep31 = (String) jsonRsponse.get("EP");
				String name31 = (String) jsonRsponse.get("newname");
				String newname31 = new String(name31.getBytes(), "utf-8");

				String where31 = " ieee = ? and ep = ?";
				String[] args31 = { ieee31, ep31 };
				ContentValues c31 = new ContentValues();
				c31.put(DevicesModel.DEFAULT_DEVICE_NAME, newname31);
				SQLiteDatabase mSQLiteDatabase31 = mDateHelper
						.getSQLiteDatabase();
				mDateHelper.update(mSQLiteDatabase31, DataHelper.DEVICES_TABLE,
						c31, where31, args31);
				mDateHelper.close(mSQLiteDatabase31);

				Event event31 = new Event(EventType.CHANGEDEVICENAME, true);
				event31.setData(newname31);
				notifyObservers(event31);
				break;
			case 33:
				String statusString = (String) jsonRsponse.get("status");
				String status = "";
				if (statusString.equals("DisArm")) {
					status = "0";
				}
				if (statusString.equals("ArmAllZone")) {
					status = "1";
				}
				Event event33 = new Event(EventType.LOCALIASCIEOPERATION, true);
				event33.setData(status);
				notifyObservers(event33);

				SQLiteDatabase mSQLiteDatabase33 = mDateHelper
						.getSQLiteDatabase();
				String where33 = " model_id like ? ";
				String[] args33 = { DataHelper.One_key_operator + "%" };
				ContentValues c33 = new ContentValues();
				c33.put(DevicesModel.ON_OFF_STATUS, status);
				mDateHelper.update(mSQLiteDatabase33, DataHelper.DEVICES_TABLE,
						c33, where33, args33);
				mDateHelper.close(mSQLiteDatabase33);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Log.e(TAG, "error callback json: " + response);
			e.printStackTrace();
		}

	}
	
	private void handlerCallbackResponseCommon(CallbackResponseCommon response) {
		int callbackType = Integer.parseInt(response.getCallbackType());
		switch(callbackType){
		case 3:
			ContentValues c = new ContentValues();
			c.put(DevicesModel.HEART_TIME, response.getValue());
			ParemetersResponse p = new ParemetersResponse();
			p.response = response;
			p.c = c;
			Event event = new Event(EventType.HEARTTIME, true);
			event.setData(response);
			notifyObservers(event);
			
			new UpdateDeviceHeartTimeToDatabaseTask().execute(p);

			break;
		default:
			break;
		}
	}

	private void handlerWarnMessage(CallbackWarnMessage warnmessage) {
		if (!getSecurityControlState() && getIsRightModelID(warnmessage)) {
			warnmessage = WarnManager.getInstance()
					.setWarnDetailMessageNoSecurity(warnmessage);
		} else {
			warnmessage = WarnManager.getInstance().setWarnDetailMessage(
					warnmessage);
		}
		// warnmessage = WarnManager.getInstance().setWarnDetailMessage(
		// warnmessage);
		WarnManager.getInstance().setCurrentWarnInfo(warnmessage);

		// new UpdateDBTask().execute(warnmessage);
		long _id = updateWarnMessage(warnmessage);
		warnmessage.setId(_id + "");
		Intent i = new Intent(ApplicationController.getInstance(),
				ShowDevicesGroupFragmentActivity.class);
		i.putExtra(ShowDevicesGroupFragmentActivity.ACTIVITY_SHOW_DEVICES_TYPE,
				UiUtils.SECURITY_CONTROL);
		// makeNotify(i, warnmessage.getDetailmessage(),
		// warnmessage.getDetailmessage() + "收到报警信息，请注意！");
		makeNotify(i, warnmessage.getZone_name(),
				warnmessage.getDetailmessage());
		Event event = new Event(EventType.WARN, true);
		event.setData(warnmessage);
		notifyObservers(event);
	}

	/***
	 * 根据attributeId 和clusterId来确定操作是什么，发送event，刷新UI
	 * 
	 * @param common
	 */
	private void handleAttribute(CallbackResponseType2 common) {

		int attributeId = Integer.parseInt(common.getAttributeId(), 16);
		int clusterId = Integer.parseInt(common.getClusterId(), 16);
		switch (attributeId) {
		case 0:
			if (clusterId == 6) {
				Log.i(TAG,
						"Callback msgType=" + 2 + " on_off_status"
								+ common.toString());
				ContentValues c = new ContentValues();
				c.put(DevicesModel.ON_OFF_STATUS, common.getValue());
				Paremeters p = new Paremeters();
				p.callbackmsg2 = common;
				p.c = c;
				new UpdateDeviceStatusToDatabaseTask().execute(p);

				Event event = new Event(EventType.ON_OFF_STATUS, true);
				event.setData(common);
				notifyObservers(event);
			}
			if (clusterId == 8) {
				Log.i(TAG,
						"Callback msgType=" + 2 + " level" + common.toString());
				ContentValues c = new ContentValues();
				c.put(DevicesModel.LEVEL, common.getValue());
				Paremeters p = new Paremeters();
				p.callbackmsg2 = common;
				p.c = c;
				new UpdateDeviceStatusToDatabaseTask().execute(p);

				Event event = new Event(EventType.MOVE_TO_LEVEL, true);
				event.setData(common);
				notifyObservers(event);
			}
			if (clusterId == 1024) {
				Log.i(TAG,
						"Callback msgType=" + 2 + "brightness"
								+ common.toString());
				ContentValues c = new ContentValues();
				c.put(DevicesModel.BRIGHTNESS,
						Integer.parseInt(common.getValue()));
				Paremeters p = new Paremeters();
				p.callbackmsg2 = common;
				p.c = c;
				new UpdateDeviceStatusToDatabaseTask().execute(p);

				Bundle bundle = new Bundle();
				bundle.putString("IEEE", common.getDeviceIeee());
				bundle.putString("EP", common.getDeviceEp());
				bundle.putString("PARAM", common.getValue());
				Event event = new Event(EventType.LIGHTSENSOROPERATION, true);
				event.setData(bundle);
				notifyObservers(event);
			}
			if (clusterId == 1026) {
				// Log.i(TAG,
				// "Callback msgType=" + 2 + "temperature"
				// + common.toString());
				ContentValues c = new ContentValues();
				c.put(DevicesModel.TEMPERATURE,
						Float.parseFloat(common.getValue().substring(0, 5)));
				Paremeters p = new Paremeters();
				p.callbackmsg2 = common;
				p.c = c;
				new UpdateDeviceStatusToDatabaseTask().execute(p);

				Bundle bundle = new Bundle();
				bundle.putString("IEEE", common.getDeviceIeee());
				bundle.putString("EP", common.getDeviceEp());
				bundle.putString("PARAM", common.getValue().substring(0, 5));
				Event event = new Event(EventType.TEMPERATURESENSOROPERATION,
						true);
				event.setData(bundle);
				notifyObservers(event);
			}
			if (clusterId == 1029) {
				// Log.i(TAG,
				// "Callback msgType=" + 2 + "humidity"
				// + common.toString());
				ContentValues c = new ContentValues();
				c.put(DevicesModel.HUMIDITY,
						Float.parseFloat(common.getValue().substring(0, 5)));
				Paremeters p = new Paremeters();
				p.callbackmsg2 = common;
				p.c = c;
				new UpdateDeviceStatusToDatabaseTask().execute(p);

				Bundle bundle = new Bundle();
				bundle.putString("IEEE", common.getDeviceIeee());
				bundle.putString("EP", common.getDeviceEp());
				bundle.putString("PARAM", common.getValue().substring(0, 5));
				Event event = new Event(EventType.HUMIDITY, true);
				event.setData(bundle);
				notifyObservers(event);
			}
			break;
		case 1:

			break;
		case 3:

			break;
		case 57344:// E000
			if (clusterId == 1794) {
				Log.i(TAG,
						"Callback msgType=" + 2 + " current"
								+ common.toString());
				ContentValues c = new ContentValues();
				c.put(DevicesModel.CURRENT, common.getValue());
				Paremeters p = new Paremeters();
				p.callbackmsg2 = common;
				p.c = c;
				new UpdateDeviceStatusToDatabaseTask().execute(p);

				Event event = new Event(EventType.CURRENT, true);
				event.setData(common);
				notifyObservers(event);
			}
			if (clusterId == 257) {
				// Log.i(TAG,
				// "Callback msgType=" + 2 + " enroll"
				// + common.toString());
				// Event event = new Event(EventType.ENROLL, true);
				// event.setData(common);
				// notifyObservers(event);
			}
			break;
		case 57345:// 0xE001:
			if (clusterId == 1794) {
				Log.i(TAG,
						"Callback msgType=" + 2 + " voltage"
								+ common.toString());
				ContentValues c = new ContentValues();
				c.put(DevicesModel.VOLTAGE, common.getValue());
				Paremeters p = new Paremeters();
				p.callbackmsg2 = common;
				p.c = c;
				new UpdateDeviceStatusToDatabaseTask().execute(p);

				Event event = new Event(EventType.VOLTAGE, true);
				event.setData(common);
				notifyObservers(event);
			}
			break;
		case 57346:// 0xE002:
			if (clusterId == 1794) {
				Log.i(TAG,
						"Callback msgType=" + 2 + " power" + common.toString());
				ContentValues c = new ContentValues();
				c.put(DevicesModel.POWER, common.getValue());
				Paremeters p = new Paremeters();
				p.callbackmsg2 = common;
				p.c = c;
				new UpdateDeviceStatusToDatabaseTask().execute(p);

				Event event = new Event(EventType.POWER, true);
				event.setData(common);
				notifyObservers(event);
			}
			break;
		case 57347:// 0xE003:
			if (clusterId == 1794) {
				Log.i(TAG,
						"Callback msgType=" + 2 + " energy" + common.toString());
				ContentValues c = new ContentValues();
				c.put(DevicesModel.ENERGY, common.getValue());
				Paremeters p = new Paremeters();
				p.callbackmsg2 = common;
				p.c = c;
				new UpdateDeviceStatusToDatabaseTask().execute(p);

				Event event = new Event(EventType.ENERGY, true);
				event.setData(common);
				notifyObservers(event);
			}
			break;

		default:
			break;
		}

	}

	class CallbackBindTask extends AsyncTask<String, Object, Object> {
		@Override
		protected Object doInBackground(String... params) {
			CallbackBindListMessage data = VolleyOperation
					.handleCallbackBindListString(params[0]);
			ArrayList<CallbackBindListDevices> mBindedDevicesList = data
					.getList();

			// DataHelper mDateHelper = new DataHelper(
			// ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
			String where = " devout_ieee=? and devout_ep=? ";
			String[] args = { data.getIeee(), data.getEp() };

			mSQLiteDatabase.beginTransaction();
			try {
				mSQLiteDatabase.delete(DataHelper.BIND_TABLE, where, args);

				if (mBindedDevicesList != null && mBindedDevicesList.size() > 0) {
					for (CallbackBindListDevices bindingDivice : mBindedDevicesList) {
						ContentValues c = new ContentValues();
						c.put(BindingDataEntity.DEVOUT_IEEE, data.getIeee());
						c.put(BindingDataEntity.DEVOUT_EP, data.getEp());
						c.put(BindingDataEntity.DEVIN_IEEE,
								bindingDivice.getIeee());
						c.put(BindingDataEntity.DEVIN_EP, bindingDivice.getEp());
						c.put(BindingDataEntity.CLUSTER, bindingDivice.getCid());

						mSQLiteDatabase.insert(DataHelper.BIND_TABLE, null, c);
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
			return 1;
		}

		@Override
		protected void onPostExecute(Object result) {
			Event event = new Event(EventType.GETBINDLIST, true);
			notifyObservers(event);
		}
	}

	class Paremeters {
		public CallbackResponseType2 callbackmsg2;
		public ContentValues c;
	}
	
	class ParemetersResponse {
		public CallbackResponseCommon response;
		public ContentValues c;
	}

	public class UpdateDeviceStatusToDatabaseTask extends
			AsyncTask<Paremeters, Integer, Integer> {

		@Override
		protected Integer doInBackground(Paremeters... params) {
			// TODO Auto-generated method stub
			Paremeters par = params[0];
			CallbackResponseType2 callbackmsg = par.callbackmsg2;
			ContentValues c = par.c;

			String where = " ieee = ? and ep = ?";
			String ieee = callbackmsg.getDeviceIeee();
			String ep = callbackmsg.getDeviceEp();
			String[] args = { ieee, ep };
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
			int result = mDateHelper.update(mSQLiteDatabase,
					DataHelper.DEVICES_TABLE, c, where, args);
			mDateHelper.close(mSQLiteDatabase);
			return result;
		}

	}
	
	public class UpdateDeviceHeartTimeToDatabaseTask extends
		AsyncTask<ParemetersResponse, Integer, Integer> {
	
		@Override
		protected Integer doInBackground(ParemetersResponse... params) {
			// TODO Auto-generated method stub
			ParemetersResponse par = params[0];
			CallbackResponseCommon response = par.response;
			ContentValues c = par.c;
		
			String where = " ieee = ? and ep = ?";
			String ieee = response.getIEEE();
			String ep = response.getEP();
			String[] args = { ieee, ep };
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
			int result = mDateHelper.update(mSQLiteDatabase,
					DataHelper.DEVICES_TABLE, c, where, args);
			mDateHelper.close(mSQLiteDatabase);
			return result;
		}
	
	}

	void makeNotify(Intent i, String title, String message) {

		NotificationManager nm = (NotificationManager) ApplicationController
				.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
		// Notification noti = new
		// Notification.Builder(ApplicationController.getInstance())
		// .setContentTitle("")
		// .setContentText("")
		// .setSmallIcon(R.drawable.icon)
		// .setLargeIcon(null)
		// .build();
		Notification noti;
		noti = new Notification(R.drawable.ui_notification_icon, title,
				System.currentTimeMillis());
		// if(message.indexOf("提示") != -1){
		// noti = new Notification(R.drawable.warnning, title,
		// System.currentTimeMillis());
		// }else{
		// noti = new Notification(R.drawable.ui_securitycontrol_alarm, title,
		// System.currentTimeMillis());
		// }
		noti.flags = Notification.FLAG_AUTO_CANCEL;
		// Intent i = new Intent(ApplicationController.getInstance(),
		// ShowDevicesGroupFragmentActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		// PendingIntent
		PendingIntent contentIntent = PendingIntent.getActivity(
				ApplicationController.getInstance(), R.string.app_name, i,
				PendingIntent.FLAG_UPDATE_CURRENT);

		noti.setLatestEventInfo(ApplicationController.getInstance(), title,
				message, contentIntent);
		nm.notify(R.string.app_name, noti);
	}

	void makeWarmNotify(Intent i, CallbackWarnMessage warmmessage) {

		NotificationManager nm = (NotificationManager) ApplicationController
				.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
		Notification noti = new Notification(R.drawable.icon,
				warmmessage.getW_description(), System.currentTimeMillis());
		noti.flags = Notification.FLAG_AUTO_CANCEL;
		// Intent i = new Intent(ApplicationController.getInstance(),
		// ShowDevicesGroupFragmentActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		// PendingIntent
		PendingIntent contentIntent = PendingIntent.getActivity(
				ApplicationController.getInstance(), R.string.app_name, i,
				PendingIntent.FLAG_UPDATE_CURRENT);

		noti.setLatestEventInfo(ApplicationController.getInstance(),
				warmmessage.getW_description(), warmmessage.toString(),
				contentIntent);
		nm.notify(R.string.app_name, noti);
	}

	class UpdateDBTask extends AsyncTask<Object, Object, Boolean> {
		@Override
		protected Boolean doInBackground(Object... params) {
			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
			ArrayList<CallbackWarnMessage> callbackWarmMessages = new ArrayList<CallbackWarnMessage>();
			callbackWarmMessages.add((CallbackWarnMessage) params[0]);
			mDateHelper.insertMessageList(mSQLiteDatabase,
					DataHelper.MESSAGE_TABLE, null, callbackWarmMessages);
			return null;
		}
	}

	private long updateWarnMessage(CallbackWarnMessage warnmessage) {
		long _id = -1;
		DataHelper mDateHelper = new DataHelper(
				ApplicationController.getInstance());
		SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
		ArrayList<CallbackWarnMessage> callbackWarmMessages = new ArrayList<CallbackWarnMessage>();
		callbackWarmMessages.add((CallbackWarnMessage) warnmessage);
		_id = mDateHelper.insertMessageList(mSQLiteDatabase,
				DataHelper.MESSAGE_TABLE, null, callbackWarmMessages);
		return _id;

	}

	private boolean getSecurityControlState() {
		final String securityControl = DataHelper.One_key_operator;
		DataHelper dh = new DataHelper(ApplicationController.getInstance());
		SQLiteDatabase db = dh.getSQLiteDatabase();
		DevicesModel device = DataUtil.getDeviceModelByModelid(securityControl,
				dh, db);
		db.close();
		if (device.getmOnOffStatus().equals("0")) {
			return false;
		} else {
			return true;
		}
	}

	private boolean getIsRightModelID(CallbackWarnMessage message) {
		boolean isRight = false;
		final String[] modelIDList = { DataHelper.Motion_Sensor, // 动作感应器
				DataHelper.Magnetic_Window, // 窗磁
				DataHelper.Doors_and_windows_sensor_switch }; // 门窗感应
		DataHelper dh = new DataHelper(ApplicationController.getInstance());
		SQLiteDatabase db = dh.getSQLiteDatabase();
		DevicesModel device = DataUtil.getDeviceModelByIeee(
				message.getZone_ieee(), dh, db);
		db.close();
		String modelID = device.getmModelId();
		for (int i = 0; i < modelIDList.length; i++) {
			if (modelID.indexOf(modelIDList[i]) != -1) {
				isRight = true;
			}
		}
		return isRight;
	}

}