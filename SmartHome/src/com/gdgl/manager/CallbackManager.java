package com.gdgl.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
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
import com.gdgl.mydata.Linkage;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.mydata.Callback.CallbackBeginLearnIRMessage;
import com.gdgl.mydata.Callback.CallbackBindListDevices;
import com.gdgl.mydata.Callback.CallbackBindListMessage;
import com.gdgl.mydata.Callback.CallbackEnrollMessage;
import com.gdgl.mydata.Callback.CallbackJoinNetMessage;
import com.gdgl.mydata.Callback.CallbackResponseCommon;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.mydata.Callback.CallbackWarnMessage;
import com.gdgl.mydata.binding.BindingDataEntity;
import com.gdgl.mydata.scene.SceneDevice;
import com.gdgl.mydata.scene.SceneInfo;
import com.gdgl.mydata.timing.TimingAction;
import com.gdgl.mydata.video.VideoNode;
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
			// Log.i(TAG, "Callback msgType=" + msgType);
			switch (msgType) {
			case 0:
				handleGlexerCallback(response);
				break;
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
				getFromSharedPreferences.setsharedPreferences(ApplicationController
						.getInstance());
				warmmessage.setHouseIEEE(getFromSharedPreferences.getGatewayMAC());
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
				// Log.i(TAG, "Callback msgType=" + msgType +
				// "OnOffLightSwitch");
				JSONObject json = new JSONObject(response);
				if (json.getInt("callbackType") == 3) {
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
				Log.i(TAG, "Callback msgType=" + msgType + " joinnet"
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

				String[] changeName = { ieee31, ep31, newname31 };
				Event event31 = new Event(EventType.CHANGEDEVICENAME, true);
				event31.setData(changeName);
				notifyObservers(event31);
				break;
			case 32:
				String ieee32 = (String) jsonRsponse.get("ZONE_IEEE");
				String ep32 = (String) jsonRsponse.get("ZONE_EP");
				String statusString32 = (String) jsonRsponse.get("state");
				// Log.i(TAG, "Callback msgType=" + msgType
				// + " on-off bypass:"+ieee32+ep32+statusString32);
				String status32 = "";
				if (statusString32.equals("unbypass")) {
					status32 = "0";
				}
				if (statusString32.equals("bypass")) {
					status32 = "1";
				}

				Bundle bundle32 = new Bundle();
				bundle32.putString("IEEE", ieee32);
				bundle32.putString("EP", ep32);
				bundle32.putString("PARAM", status32);
				Event event32 = new Event(EventType.LOCALIASCIEBYPASSZONE, true);
				event32.setData(bundle32);
				notifyObservers(event32);

				String where32 = " ieee = ? and ep = ?";
				String[] args32 = { ieee32, ep32 };
				ContentValues c32 = new ContentValues();
				c32.put(DevicesModel.ON_OFF_STATUS, status32);
				SQLiteDatabase mSQLiteDatabase32 = mDateHelper
						.getSQLiteDatabase();
				mDateHelper.update(mSQLiteDatabase32, DataHelper.DEVICES_TABLE,
						c32, where32, args32);
				mDateHelper.close(mSQLiteDatabase32);
				break;
			case 33:
				String statusString33 = (String) jsonRsponse.get("status");
				String status33 = "";
				if (statusString33.equals("DisArm")) {
					status33 = "0";
				}
				if (statusString33.equals("ArmAllZone")) {
					status33 = "1";
				}
				Event event33 = new Event(EventType.LOCALIASCIEOPERATION, true);
				event33.setData(status33);
				notifyObservers(event33);

				SQLiteDatabase mSQLiteDatabase33 = mDateHelper
						.getSQLiteDatabase();
				String where33 = " model_id like ? ";
				String[] args33 = { DataHelper.One_key_operator + "%" };
				ContentValues c33 = new ContentValues();
				c33.put(DevicesModel.ON_OFF_STATUS, status33);
				mDateHelper.update(mSQLiteDatabase33, DataHelper.DEVICES_TABLE,
						c33, where33, args33);
				mDateHelper.close(mSQLiteDatabase33);
				break;
			case 34:
				String ieee34 = (String) jsonRsponse.get("IEEE");
				Log.i(TAG, "Callback msgType=" + msgType + "  " + ieee34);
				SQLiteDatabase mSQLiteDatabase34 = mDateHelper
						.getSQLiteDatabase();
				String where34 = " ieee=? ";
				String[] args34 = { ieee34 };
				mDateHelper.delete(mSQLiteDatabase34, DataHelper.DEVICES_TABLE,
						where34, args34);
				mDateHelper.close(mSQLiteDatabase34);

				Event event34 = new Event(EventType.DELETENODE, true);
				event34.setData(ieee34);
				notifyObservers(event34);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Log.e(TAG, "error callback json: " + response);
			e.printStackTrace();
		}

	}

	private void handleGlexerCallback(String response) throws JSONException {
		Gson gson = new Gson();
		JSONObject jsonRsponse = new JSONObject(response);
		int mainid = (Integer) jsonRsponse.get("mainid");
		int subid = (Integer) jsonRsponse.get("subid");
		Log.i(TAG, response);
		if (mainid == 1) { // user operations message of Guanglian APIs
			switch (subid) {
			case 1: // modifyPassword
				int status1 = (Integer) jsonRsponse.get("status");

				Event event1 = new Event(EventType.MODIFYPASSWORD, true);
				event1.setData(status1);
				notifyObservers(event1);
				break;
			case 2: // modifyAlias
				int status2 = (Integer) jsonRsponse.get("status");

				Event event2 = new Event(EventType.MODIFYALIAS, true);
				event2.setData(status2);
				notifyObservers(event2);
				break;
			default:
				break;
			}
		}
		if (mainid == 2) { // video operations
			switch (subid) {
			case 1: // addIPC
				int status1 = (Integer) jsonRsponse.get("status");
				if (status1 < 0) {
					Event event1_error = new Event(EventType.ADDIPC, false);
					event1_error.setData(status1);
					notifyObservers(event1_error);
					break;
				}
				try {
					new URLDecoder();
					response = URLDecoder.decode(response, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				VideoNode videoNode1 = gson.fromJson(response, VideoNode.class);
				Event event1 = new Event(EventType.ADDIPC, true);
				event1.setData(videoNode1);

				SQLiteDatabase mSqLiteDatabase1 = mDateHelper
						.getSQLiteDatabase();
				mSqLiteDatabase1.insert(DataHelper.VIDEO_TABLE, null,
						videoNode1.convertContentValues());
				mSqLiteDatabase1.close();

				notifyObservers(event1);
				break;
			case 2: // editIPC
				int status2 = (Integer) jsonRsponse.get("status");
				if (status2 < 0) {
					Event event2_error = new Event(EventType.EDITIPC, false);
					event2_error.setData(status2);
					notifyObservers(event2_error);
					break;
				}
				try {
					new URLDecoder();
					response = URLDecoder.decode(response, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				VideoNode videoNode2 = gson.fromJson(response, VideoNode.class);

				Event event2 = new Event(EventType.EDITIPC, true);
				event2.setData(videoNode2);

				SQLiteDatabase mSqLiteDatabase2 = mDateHelper
						.getSQLiteDatabase();
				String where2 = VideoNode.ID + " = ? ";
				String[] arg2 = { videoNode2.getId() };
				mSqLiteDatabase2.delete(DataHelper.VIDEO_TABLE, where2, arg2);
				mSqLiteDatabase2.insert(DataHelper.VIDEO_TABLE, null,
						videoNode2.convertContentValues());
				mSqLiteDatabase2.close();

				notifyObservers(event2);
				break;
			case 3: // deleteIPC
				int status3 = (Integer) jsonRsponse.get("status");
				if (status3 < 0) {
					Event event3_error = new Event(EventType.DELETEIPC, false);
					event3_error.setData(status3);
					notifyObservers(event3_error);
					break;
				}
				int videoID3 = (Integer) jsonRsponse.get("id");
				Event event3 = new Event(EventType.DELETEIPC, true);
				event3.setData(videoID3);
				notifyObservers(event3);

				SQLiteDatabase mSqLiteDatabase3 = mDateHelper
						.getSQLiteDatabase();
				String where3 = VideoNode.ID + " = ? ";
				String[] arg3 = { videoID3 + "" };
				mSqLiteDatabase3.delete(DataHelper.VIDEO_TABLE, where3, arg3);
				break;
			case 4: // ipc online status
				String ipc_status = (String) jsonRsponse.get("ipc_status_list");
				char[] ipcStatusList = ipc_status.toCharArray();

				Event event4 = new Event(EventType.IPCONLINESTATUS, true);
				event4.setData(ipcStatusList);
				notifyObservers(event4);

				SQLiteDatabase mSqLiteDatabase4 = mDateHelper
						.getSQLiteDatabase();
				ContentValues c4 = new ContentValues();
				String where4 = VideoNode.ID + " = ? ";
				for (int i = 0; i < ipcStatusList.length; i++) {
					if (ipcStatusList[i] != 'c') {
						String[] arg4 = { i + "" };
						c4.put(VideoNode.IPC_STATUS,
								String.valueOf(ipcStatusList[i]));
						mDateHelper.update(mSqLiteDatabase4,
								DataHelper.VIDEO_TABLE, c4, where4, arg4);
					}
				}
				mSqLiteDatabase4.close();
				break;
			default:
				break;
			}
		}
		if (mainid == 3) { // timing operations
			switch (subid) {
			case 1: // add timingact
				int status1 = (Integer) jsonRsponse.get("status");
				if (status1 < 0) {
					Event event1_error = new Event(EventType.ADDTIMINGACTION,
							false);
					event1_error.setData(status1);
					notifyObservers(event1_error);
					break;
				}
				TimingAction timingAction1 = gson.fromJson(response,
						TimingAction.class);
				timingAction1.setActpara(timingAction1.getActpara());
				timingAction1.setPara3(timingAction1.getPara3());
				Event event1 = new Event(EventType.ADDTIMINGACTION, true);
				event1.setData(timingAction1);

				SQLiteDatabase mSqLiteDatabase1 = mDateHelper
						.getSQLiteDatabase();
				mSqLiteDatabase1.insert(DataHelper.TIMINGACTION_TABLE, null,
						timingAction1.convertContentValues());
				mSqLiteDatabase1.close();

				notifyObservers(event1);
				break;
			case 2: // edit timingact
				int status2 = (Integer) jsonRsponse.get("status");
				if (status2 < 0) {
					Event event2_error = new Event(EventType.EDITTIMINGACTION,
							false);
					event2_error.setData(status2);
					notifyObservers(event2_error);
					break;
				}
				TimingAction timingAction2 = gson.fromJson(response,
						TimingAction.class);
				timingAction2.setActpara(timingAction2.getActpara());
				timingAction2.setPara3(timingAction2.getPara3());
				Event event2 = new Event(EventType.EDITTIMINGACTION, true);
				event2.setData(timingAction2);

				SQLiteDatabase mSqLiteDatabase2 = mDateHelper
						.getSQLiteDatabase();
				String where2 = TimingAction.TIMING_ID + " = ? ";
				String[] arg2 = { timingAction2.getTid() + "" };
				mSqLiteDatabase2.delete(DataHelper.TIMINGACTION_TABLE, where2,
						arg2);
				mSqLiteDatabase2.insert(DataHelper.TIMINGACTION_TABLE, null,
						timingAction2.convertContentValues());
				mSqLiteDatabase2.close();

				notifyObservers(event2);
				break;
			case 3: // delete timingact
				int status3 = (Integer) jsonRsponse.get("status");
				if (status3 < 0) {
					Event event3_error = new Event(EventType.DELTIMINGACTION,
							false);
					event3_error.setData(status3);
					notifyObservers(event3_error);
					break;
				}
				int tid3 = (Integer) jsonRsponse.get("tid");
				Event event3 = new Event(EventType.DELTIMINGACTION, true);
				event3.setData(tid3);
				notifyObservers(event3);

				SQLiteDatabase mSqLiteDatabase3 = mDateHelper
						.getSQLiteDatabase();
				String where3 = TimingAction.TIMING_ID + " = ? ";
				String[] arg3 = { tid3 + "" };
				mSqLiteDatabase3.delete(DataHelper.TIMINGACTION_TABLE, where3,
						arg3);
				break;
			case 4: // enable timingact
				int status4 = (Integer) jsonRsponse.get("status");
				if (status4 < 0) {
					Event event4_error = new Event(
							EventType.ENABLETIMINGACTION, false);
					event4_error.setData(status4);
					notifyObservers(event4_error);
					break;
				}
				int tid4 = (Integer) jsonRsponse.get("tid");
				int enable4 = (Integer) jsonRsponse.get("enable");
				int[] temp = { tid4, enable4 };
				Event event4 = new Event(EventType.ENABLETIMINGACTION, true);
				event4.setData(temp);
				notifyObservers(event4);

				SQLiteDatabase mSqLiteDatabase4 = mDateHelper
						.getSQLiteDatabase();
				String where4 = TimingAction.TIMING_ID + " = ? ";
				String[] arg4 = { tid4 + "" };
				ContentValues c4 = new ContentValues();
				c4.put(TimingAction.TIMING_ENABLE, enable4);
				mDateHelper.update(mSqLiteDatabase4,
						DataHelper.TIMINGACTION_TABLE, c4, where4, arg4);
				mSqLiteDatabase4.close();
				break;
			default:
				break;
			}
		}
		if (mainid == 4) { // scene operations
			switch (subid) {
			case 1: // add scene
				int status1 = (Integer) jsonRsponse.get("status");
				if (status1 < 0) {
					Event event1_error = new Event(EventType.ADDSCENE, false);
					event1_error.setData(status1);
					notifyObservers(event1_error);
					break;
				}
				SceneInfo sceneInfo1 = gson.fromJson(response, SceneInfo.class);
				Event event1 = new Event(EventType.ADDSCENE, true);
				event1.setData(sceneInfo1);

				SQLiteDatabase mSqLiteDatabase1 = mDateHelper
						.getSQLiteDatabase();
				mSqLiteDatabase1.insert(DataHelper.SCENE_TABLE, null,
						sceneInfo1.convertContentValues());
				List<SceneDevice> sceneDevicesList1 = UiUtils
						.parseActionParamsToSceneDevices(sceneInfo1.getSid(),
								sceneInfo1.getScnaction());
				for (SceneDevice sceneDevice : sceneDevicesList1) {
					mSqLiteDatabase1.insert(DataHelper.SCENE_DEVICES_TABLE,
							null, sceneDevice.convertContentValues());
				}
				mSqLiteDatabase1.close();

				notifyObservers(event1);
				break;
			case 2: // edit scene
				int status2 = (Integer) jsonRsponse.get("status");
				if (status2 < 0) {
					Event event2_error = new Event(EventType.EDITSCENE, false);
					event2_error.setData(status2);
					notifyObservers(event2_error);
					break;
				}
				SceneInfo sceneInfo2 = gson.fromJson(response, SceneInfo.class);
				Event event2 = new Event(EventType.EDITSCENE, true);
				event2.setData(sceneInfo2);

				SQLiteDatabase mSqLiteDatabase2 = mDateHelper
						.getSQLiteDatabase();
				String where2 = SceneInfo.SCENE_ID + " = ? ";
				String[] arg2 = { sceneInfo2.getSid() + "" };
				mSqLiteDatabase2.delete(DataHelper.SCENE_TABLE, where2, arg2);
				mSqLiteDatabase2.insert(DataHelper.SCENE_TABLE, null,
						sceneInfo2.convertContentValues());

				where2 = SceneDevice.SCENE_ID + " = ? ";
				mSqLiteDatabase2.delete(DataHelper.SCENE_DEVICES_TABLE, where2,
						arg2);
				List<SceneDevice> sceneDevicesList2 = UiUtils
						.parseActionParamsToSceneDevices(sceneInfo2.getSid(),
								sceneInfo2.getScnaction());
				for (SceneDevice sceneDevice : sceneDevicesList2) {
					mSqLiteDatabase2.insert(DataHelper.SCENE_DEVICES_TABLE,
							null, sceneDevice.convertContentValues());
				}
				mSqLiteDatabase2.close();

				notifyObservers(event2);
				break;
			case 3:
				int status3 = (Integer) jsonRsponse.get("status");
				if (status3 < 0) {
					Event event3_error = new Event(EventType.DELSCENE, false);
					event3_error.setData(status3);
					notifyObservers(event3_error);
					break;
				}
				int sid3 = (Integer) jsonRsponse.get("sid");
				Event event3 = new Event(EventType.DELSCENE, true);
				event3.setData(sid3);
				notifyObservers(event3);

				SQLiteDatabase mSqLiteDatabase3 = mDateHelper
						.getSQLiteDatabase();
				String where3 = SceneInfo.SCENE_ID + " = ? ";
				String[] arg3 = { sid3 + "" };
				mSqLiteDatabase3.delete(DataHelper.SCENE_TABLE, where3, arg3);
				where3 = SceneDevice.SCENE_ID + " = ? ";
				mSqLiteDatabase3.delete(DataHelper.SCENE_DEVICES_TABLE, where3,
						arg3);
				break;
			case 4:
				int status4 = (Integer) jsonRsponse.get("status");
				int sid4 = (Integer) jsonRsponse.get("sid");
				Event event4;
				if (status4 == 0) {
					event4 = new Event(EventType.DOSCENE, true);
				} else {
					event4 = new Event(EventType.DOSCENE, false);
				}
				event4.setData(sid4);
				notifyObservers(event4);
			default:
				break;
			}
		}
		if (mainid == 5) { // linkage
			switch (subid) {
			case 1: // add linkage
				int status1 = (Integer) jsonRsponse.get("status");
				if (status1 < 0) {
					Event event1_error = new Event(EventType.ADDLINKAGE, false);
					event1_error.setData(status1);
					notifyObservers(event1_error);
					break;
				}
				Linkage linkage1 = gson.fromJson(response, Linkage.class);
				Event event1 = new Event(EventType.ADDLINKAGE, true);
				event1.setData(linkage1);

				SQLiteDatabase mSqLiteDatabase1 = mDateHelper
						.getSQLiteDatabase();
				mSqLiteDatabase1.insert(DataHelper.LINKAGE_TABLE, null,
						linkage1.convertContentValues());
				mSqLiteDatabase1.close();

				notifyObservers(event1);
				break;
			case 2: // edit linkage
				int status2 = (Integer) jsonRsponse.get("status");
				if (status2 < 0) {
					Event event2_error = new Event(EventType.EDITLINKAGE, false);
					event2_error.setData(status2);
					notifyObservers(event2_error);
					break;
				}
				Linkage linkage2 = gson.fromJson(response, Linkage.class);
				Event event2 = new Event(EventType.EDITLINKAGE, true);
				event2.setData(linkage2);

				SQLiteDatabase mSqLiteDatabase2 = mDateHelper
						.getSQLiteDatabase();
				String where2 = Linkage.LID + " = ? ";
				String[] arg2 = { linkage2.getLid() + "" };
				mSqLiteDatabase2.update(DataHelper.LINKAGE_TABLE,
						linkage2.convertContentValues(), where2, arg2);
				mSqLiteDatabase2.close();

				notifyObservers(event2);
				break;
			case 3: // delete linkage
				int status3 = (Integer) jsonRsponse.get("status");
				if (status3 < 0) {
					Event event3_error = new Event(EventType.DELETELINKAGE,
							false);
					event3_error.setData(status3);
					notifyObservers(event3_error);
					break;
				}
				int lid3 = (Integer) jsonRsponse.get("lid");
				Event event3 = new Event(EventType.DELETELINKAGE, true);
				event3.setData(lid3);

				SQLiteDatabase mSqLiteDatabase3 = mDateHelper
						.getSQLiteDatabase();
				String where3 = Linkage.LID + " = ? ";
				String[] arg3 = { lid3 + "" };
				mSqLiteDatabase3.delete(DataHelper.LINKAGE_TABLE, where3, arg3);

				notifyObservers(event3);
				break;
			case 4: // enable linkage
				int status4 = (Integer) jsonRsponse.get("status");
				if (status4 < 0) {
					Event event4_error = new Event(EventType.ENABLELINKAGE,
							false);
					event4_error.setData(status4);
					notifyObservers(event4_error);
					break;
				}
				int lid4 = (Integer) jsonRsponse.get("lid");
				int enable4 = (Integer) jsonRsponse.get("enable");
				int[] temp = {lid4, enable4};
				Event event4 = new Event(EventType.ENABLELINKAGE, true);
				event4.setData(temp);

				SQLiteDatabase mSqLiteDatabase4 = mDateHelper
						.getSQLiteDatabase();
				ContentValues cv = new ContentValues();
				cv.put(Linkage.ENABLE, enable4);
				String where4 = Linkage.LID + " = ? ";
				String[] arg4 = { lid4 + "" };
				mSqLiteDatabase4.update(DataHelper.LINKAGE_TABLE, cv, where4,
						arg4);
				mSqLiteDatabase4.close();

				notifyObservers(event4);
				break;
			default:
				break;
			}
		}
		if (mainid == 6) { // gateway update
			switch (subid) {
			case 1:
				Event event1 = new Event(EventType.GATEWAYUPDATEBEGINE, true);
				notifyObservers(event1);
				break;
			case 2:
				int status2 = (Integer) jsonRsponse.get("status");
				if (status2 < 0) {
					Event event2_error = new Event(
							EventType.GATEWAYUPDATECOMPLETE, false);
					notifyObservers(event2_error);
					break;
				}
				int reboot = (Integer) jsonRsponse.get("need_reboot");
				String version_now = (String) jsonRsponse.get("cur_sw_version");
				String[] data = { reboot + "", version_now };
				Event event2 = new Event(EventType.GATEWAYUPDATECOMPLETE, true);
				event2.setData(data);
				notifyObservers(event2);
				break;
			}
		}

	}

	private void handlerCallbackResponseCommon(CallbackResponseCommon response) {
		int callbackType = Integer.parseInt(response.getCallbackType());
		switch (callbackType) {
		case 3:
			Log.i(TAG, "HEART_TIME " + response.getValue());
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
		WarnManager.getInstance().setCurrentWarnInfo(warnmessage);

		// new UpdateDBTask().execute(warnmessage);
		long _id = updateWarnMessage(warnmessage);
		warnmessage.setId(_id + "");
		Intent i = new Intent(ApplicationController.getInstance(),
				ShowDevicesGroupFragmentActivity.class);
		i.putExtra(ShowDevicesGroupFragmentActivity.ACTIVITY_SHOW_DEVICES_TYPE,
				UiUtils.SECURITY_CONTROL);
		i.putExtra("ieee", warnmessage.getZone_ieee());
		i.putExtra("ep", warnmessage.getZone_ep());
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
				if (Integer.parseInt(common.getValue()) < 7) {
					c.put(DevicesModel.ON_OFF_STATUS, "0");

				} else {
					c.put(DevicesModel.ON_OFF_STATUS, "1");
				}
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
				float value = Float.parseFloat(common.getValue());
				common.setValue(String.valueOf(value));
				ContentValues c = new ContentValues();
				c.put(DevicesModel.ENERGY, String.valueOf(value));
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
