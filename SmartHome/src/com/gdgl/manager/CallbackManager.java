package com.gdgl.manager;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.gdgl.mydata.Callback.CallbackResponseCommon;
import com.gdgl.util.NetUtil;
import com.google.gson.Gson;

public class CallbackManager extends Manger {
	private final static String TAG="CallbackManager";
	private static CallbackManager instance;

	public static CallbackManager getInstance() {
		if (instance == null) {
			instance = new CallbackManager();
		}
		return instance;
	}
	public void startConnectServerByTCPTask()
	{
		ConnectServerByTCPTask connectServerByTCPTask= new ConnectServerByTCPTask();
		connectServerByTCPTask.start();
		if ((NetUtil.getInstance().isConnectedCallback()) ) {
			connectServerByTCPTask=null;
		}
	}
	public void startCallbackTask()
	{
		CallbackTask callbackTask= new CallbackTask();
		callbackTask.start();
	}

	class ConnectServerByTCPTask extends Thread {
		@Override
		public void run() {
			try {
				NetUtil.getInstance().connectServerWithTCPSocket();
				if (NetUtil.getInstance().isConnectedCallback()) {
					NetUtil.getInstance().sendHeartBeat();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class CallbackTask extends Thread {

		@Override
		public void run() {
			while (true) {

				try {
					if (NetUtil.getInstance().isConnectedCallback()) {
						String response = NetUtil.getInstance()
								.recieveFromCallback();

						if (!TextUtils.isEmpty(response)) {
							handleCallbackResponse(response);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	private void handleCallbackResponse(String response) {
		try {
			Gson gson=new Gson();
			JSONObject jsonRsponse = new JSONObject(response);
			int msgType=(Integer) jsonRsponse.get("msgtype");
			Log.i(TAG, "Callback msgType="+msgType);
			switch (msgType) {
			case 1:
				Log.i(TAG, "Callback msgType="+msgType+"heartbeat");
				break;
			case 2:
				Log.i(TAG, "Callback msgType="+msgType+"energy");
				break;
			case 3:
				Log.i(TAG, "Callback msgType="+msgType+"warm message");
				break;
			case 4:
				Log.i(TAG, "Callback msgType="+msgType+"doorlock");
				break;
			case 5:
				CallbackResponseCommon iasZone=gson.fromJson(response, CallbackResponseCommon.class);
				Log.i(TAG, "Callback msgType="+msgType+"IASZONE"+iasZone.toString());
				break;
			case 6:
				Log.i(TAG, "Callback msgType="+msgType+"DimmerSwitch");
				break;
			case 7:
				Log.i(TAG, "Callback msgType="+msgType+"OnOffLightSwitch");
				break;
			case 8:
				CallbackResponseCommon IASWarmingDevice=gson.fromJson(response, CallbackResponseCommon.class);
				Log.i(TAG, "Callback msgType="+msgType+"IASWarmingDevice"+IASWarmingDevice.toString());
				break;
			case 9:
				Log.i(TAG, "Callback msgType="+msgType+"OnOffSwitch");
				break;
			case 10:
				Log.i(TAG, "Callback msgType="+msgType+"OnOffOutPut");
				break;
				//need to distinguish with type 5
			case 11:
				CallbackResponseCommon iasZone11=gson.fromJson(response, CallbackResponseCommon.class);
				Log.i(TAG, "Callback msgType="+msgType+"IASZONE"+iasZone11.toString());
				
				break;
			case 12:
				Log.i(TAG, "Callback msgType="+msgType+"Brand Style");
				break;
			case 13:
				Log.i(TAG, "Callback msgType="+msgType+" low tension warming");
				break;
			case 14:
				Log.i(TAG, "Callback msgType="+msgType+" Bind result");
				break;
			case 15:
				Log.i(TAG, "Callback msgType="+msgType+" UnBind result");
				break;
			case 16:
				Log.i(TAG, "Callback msgType="+msgType+" Enroll");
				break;
			case 17:
				Log.i(TAG, "Callback msgType="+msgType+" unEnroll");
				break;
			case 18:
				Log.i(TAG, "Callback msgType="+msgType+" Change to roomID");
				break;
			case 19:
				Log.i(TAG, "Callback msgType="+msgType+" ScenceSelector");
				break;
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
