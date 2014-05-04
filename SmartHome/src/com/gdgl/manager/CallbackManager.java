package com.gdgl.manager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.gdgl.activity.RegionDevicesActivity;
import com.gdgl.activity.ShowDevicesGroupFragmentActivity;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.Callback.CallbackResponseCommon;
import com.gdgl.mydata.Callback.CallbackWarmMessage;
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

	// public void startCallbackTask() {
	// CallbackTask callbackTask = new CallbackTask();
	// callbackTask.start();
	// }

	class ConnectServerByTCPTask extends Thread {

		@Override
		public void run() {
			connectAndRecieveFromCallback();
		}

	}

	public void connectAndRecieveFromCallback() {
		try {
			NetUtil.getInstance().connectServerWithTCPSocket();
			NetUtil.getInstance().recieveFromCallback();
		} catch (Exception e) {

			Log.e(TAG, "connectAndRecieveFromCallback error" + e.getMessage());
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
				// Log.i(TAG, "Callback msgType=" + msgType + "energy");
				break;
			case 3:
				Log.i(TAG, "Callback msgType=" + msgType + "warm message");
				CallbackWarmMessage warmmessage = gson.fromJson(response,
						CallbackWarmMessage.class);
				Intent i = new Intent(ApplicationController.getInstance(),
						ShowDevicesGroupFragmentActivity.class);
				i.putExtra(
						ShowDevicesGroupFragmentActivity.ACTIVITY_SHOW_DEVICES_TYPE,
						UiUtils.SECURITY_CONTROL);
				makeNotify(i,warmmessage.getW_description(),warmmessage.toString());
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
				makeNotify(i5,iasZone.getValue(),iasZone.toString());
				break;
			case 6:
				Log.i(TAG, "Callback msgType=" + msgType + "DimmerSwitch");
				break;
			case 7:
				// Log.i(TAG, "Callback msgType=" + msgType +
				// "OnOffLightSwitch");
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
				makeNotify(i11,iasZone11.getValue(),iasZone11.toString());

				break;
			case 12:
				Log.i(TAG, "Callback msgType=" + msgType + "Brand Style");
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
			e.printStackTrace();
		}

	}

	void makeNotify(Intent i,String title,String message) {

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

		noti.setLatestEventInfo(ApplicationController.getInstance(),
				title, message, contentIntent);
		nm.notify(R.string.app_name, noti);
	}

}
