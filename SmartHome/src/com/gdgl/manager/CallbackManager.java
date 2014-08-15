package com.gdgl.manager;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.gdgl.activity.ShowDevicesGroupFragmentActivity;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Callback.CallbackBeginLearnIRMessage;
import com.gdgl.mydata.Callback.CallbackEnrollMessage;
import com.gdgl.mydata.Callback.CallbackResponseCommon;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.mydata.Callback.CallbackWarnMessage;
import com.gdgl.smarthome.R;
import com.gdgl.util.NetUtil;
import com.gdgl.util.UiUtils;
import com.google.gson.Gson;

public class CallbackManager extends Manger {
	private final static String TAG = "CallbackManager";
	private static CallbackManager instance;

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

	class ConnectServerByTCPTask extends Thread {

		@Override
		public void run() {
			connectAndRecieveFromCallback();
		}

	}

	public void connectAndRecieveFromCallback() {
		try {
			Log.i(TAG, "start ConnectServerByTCPTask");
			NetUtil.getInstance().initalCallbackSocket();
			NetUtil.getInstance().connectServerWithTCPSocket();
			NetUtil.getInstance().recieveFromCallback();
		} catch (Exception e) {
			Log.e(TAG, "connectAndRecieveFromCallback error：" + e.getMessage());
		}
	}

	// class CallbackTask extends Thread {
	//
	// @Override
	// public void run() {
	// try {
	// NetUtil.getInstance().recieveFromCallback();
	//
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	//
	// }

	public void handleCallbackResponse(String response) {
		try {
			Gson gson = new Gson();
			JSONObject jsonRsponse = new JSONObject(response);
			int msgType = (Integer) jsonRsponse.get("msgtype");
			// Log.i(TAG, "Callback msgType=" + msgType);
			switch (msgType) {
			case 1:
				Log.i(TAG, "Callback msgType=" + msgType + "heartbeat");
				break;
			case 2:
				CallbackResponseType2 common = gson.fromJson(response,
						CallbackResponseType2.class);
				handleAttribute(common);

				// Log.i(TAG, "Callback msgType=" + msgType + "energy");
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
				//Log.i(TAG, "Callback msgType=" + msgType + "OnOffLightSwitch");
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
				Log.i(TAG, "Callback msgType=" + msgType + "OnOffOutPut");
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
				Log.i(TAG, "Callback msgType=" + msgType + "Brand Style");
				CallbackBeginLearnIRMessage learnIR=gson.fromJson(response, CallbackBeginLearnIRMessage.class);
				Log.i("CallbackManager BeginLearnIR Response:%n %s", learnIR.toString());
				Event event1 = new Event(EventType.BEGINLEARNIR, true);
				event1.setData(learnIR);
				notifyObservers(event1);
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
				Log.i(TAG, "Callback msgType=" + msgType + " Enroll");
				CallbackEnrollMessage enrollMessage = gson.fromJson(response,
						CallbackEnrollMessage.class);
				Event event = new Event(EventType.ENROLL, true);
				event.setData(enrollMessage);
				notifyObservers(event);
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
			default:
				break;
			}
		} catch (JSONException e) {
			Log.e(TAG, "error callback json: " + response);
			// e.printStackTrace();
		}

	}

	private void handlerWarnMessage(CallbackWarnMessage warnmessage) {
		
		warnmessage=WarnManager.getInstance().setWarnDetailMessage(warnmessage);
		WarnManager.getInstance().setCurrentWarnInfo(warnmessage);
		
		new UpdateDBTask().execute(warnmessage);
		Intent i = new Intent(ApplicationController.getInstance(),
				ShowDevicesGroupFragmentActivity.class);
		i.putExtra(ShowDevicesGroupFragmentActivity.ACTIVITY_SHOW_DEVICES_TYPE,
				UiUtils.SECURITY_CONTROL);
		makeNotify(i, warnmessage.getW_description(), warnmessage.toString());
		
		

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
			if (6 == clusterId) {
				Event event = new Event(EventType.ON_OFF_STATUS, true);
				event.setData(common);
				notifyObservers(event);
			}
			break;
		case 1:

			break;
		case 3:

			break;
		case 57344:// E000
			Log.i(TAG, "attributeId:E000");
			Event event = new Event(EventType.ENROLL, true);
			event.setData(common);
			notifyObservers(event);
			break;
		case 57345:// 0xE001:
			Log.i(TAG, "attributeId:E001");
			break;
		case 57346:// 0xE002:
			Log.i(TAG, "attributeId:E002");
			break;
		case 57347:// 0xE003:
			Log.i(TAG, "attributeId:E003");
			break;

		default:
			break;
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

		Notification noti = new Notification(R.drawable.icon, title,
				System.currentTimeMillis());
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
}
