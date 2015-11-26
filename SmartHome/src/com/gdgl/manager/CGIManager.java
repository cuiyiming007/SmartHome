package com.gdgl.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.gdgl.app.ApplicationController;
import com.gdgl.drawer.DeviceControlActivity;
import com.gdgl.libjingle.LibjingleNetUtil;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.historydata.HistoryData;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DeviceLearnedParam;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.RespondDataEntity;
import com.gdgl.mydata.ResponseDataEntityForStatus;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.mydata.SimpleResponseData;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.mydata.Callback.CallbackBindListDevices;
import com.gdgl.mydata.Callback.CallbackBindListMessage;
import com.gdgl.mydata.Region.GetRoomInfo_response;
import com.gdgl.mydata.Region.Room;
import com.gdgl.mydata.Region.RoomData_response_params;
import com.gdgl.mydata.bind.BindResponseData;
import com.gdgl.mydata.binding.BindingDataEntity;
import com.gdgl.mydata.getlocalcielist.LocalIASCIEOperationResponseData;
import com.gdgl.network.CustomRequest;
import com.gdgl.network.StringRequestChina;
import com.gdgl.network.VolleyErrorHelper;
import com.gdgl.network.VolleyOperation;
import com.gdgl.util.NetUtil;
import com.gdgl.util.UiUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/***
 * 
 * @author justek DeviceID Device Class Device Oparation 0x0007
 *         CombinedInterface 2.1 ZBNode Operation 0x0009 MainsPowerOutlet 2.3
 *         MainsOutLet Operation 0x0101 DimmableLight 2.4 DimmableLight
 *         Operation 0x0100 OnOffLight 2.5 OnOffLight Operation 0x0104
 *         DimmerSwitch 2.6 DimmerSwitch Operation 0x000A DoorLock 2.7 DoorLock
 *         Operation 0x0403 IASWarningDevice 2.8 IASWarningDevice Operation
 *         0x0400 IASCIE 2.9 IASCIE Operation 0x0103 OnOffLightSwitch 2.10
 *         OnOffLightSwitch Operation 0x0002 OnOffOutput 2.11 OnOffOutput
 *         Operation 0x0000 OnOffSwitch 2.12 OnOffSwitch Operation 0x0106
 *         LightSensor 2.13 LightSensor Operation 0x0201 ShadeController 2.14
 *         ShadeController Operation 0x0200 Shade 2.15 Shade Operation 0x0303
 *         Pump 2.16 Pump Operation Reserved LocalIASCIE 2.17 LocalIASCIE
 *         Operation 0x0402 IASZone 2.18 IASZone Operation 0x0302
 *         TemperatureSensor 2.19 TemperatureSensor Operation
 */
public class CGIManager extends Manger {

	private final static String TAG = "CGIManager";

	private static CGIManager instance;

	public static CGIManager getInstance() {
		if (instance == null) {
			instance = new CGIManager();
		}
		return instance;
	}

	/***
	 * addBindData
	 * 
	 * @param bindtype
	 *            0 为正常绑定，1 为虚拟EP绑定
	 * @param devout_ieee
	 * @param devout_ep
	 * @param mDevices
	 * @param cluster_id
	 */
	public void addBindData(String bindtype, String devout_ieee,
			String devout_ep, DevicesModel mDevices, String cluster_id) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("bindtype", bindtype);
		paraMap.put("sourceieee", devout_ieee);
		paraMap.put("sourceep", devout_ep);
		paraMap.put("destieee", mDevices.getmIeee());
		paraMap.put("destep", mDevices.getmEP());
		paraMap.put("clusterid", cluster_id);
		paraMap.put("hasbind", "0");
		paraMap.put("desttype", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);
		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				response = UiUtils.formatResponseString(response);
				Log.i("CGIManager bindDevice Response:%n %s", response);
				Gson gson = new Gson();
				BindResponseData statusData = gson.fromJson(
						response.toString(), BindResponseData.class);
				Event event = new Event(EventType.BINDDEVICE, true);
				event.setData(statusData);
				notifyObservers(event);
			}
		};
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "AddBindData.cgi", param);
		Log.i("CGIManager bindDevice Request:%n %s", url);
		StringRequest req = new StringRequest(url, responseListener,
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String errorString = null;
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("Error: ", error.getMessage());
							errorString = VolleyErrorHelper.getMessage(error,
									ApplicationController.getInstance());
						}
						Event event = new Event(EventType.BINDDEVICE, false);
						event.setData(errorString);
						notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void delBindData(String bindtype, String devout_ieee,
			String devout_ep, DevicesModel mDevices, String cluster_id) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("bindtype", bindtype);
		paraMap.put("sourceieee", devout_ieee);
		paraMap.put("sourceep", devout_ep);
		paraMap.put("destieee", mDevices.getmIeee());
		paraMap.put("destep", mDevices.getmEP());
		paraMap.put("clusterid", cluster_id);
		paraMap.put("hasbind", "0");
		paraMap.put("desttype", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "DelBindData.cgi", param);
		simpleVolleyRequset(url, EventType.UNBINDDEVICE);
	}

	public void GetAllBindList() {
		HashMap<String, String> paraMap = new HashMap<String, String>();

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "GetAllBindList.cgi", param);
		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.i("CGIManager getBindList Response:%n %s", response);
				if (response != null && response.length() > 0) {
					new GetBindingTask().execute(response);
				}
			}
		};

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if (error != null && error.getMessage() != null) {
					Log.e("getBindList Error: ", error.getMessage());
				} else {
					Log.e("getBindList Error: ", "Volley error!");
				}

			}
		};

		StringRequest req = new StringRequest(url, responseListener,
				errorListener);

		// add the request object to the queue to be executed
		Log.i("request url", url);
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/***
	 * 1.7 删除node
	 * 
	 * @param model
	 */
	public void deleteNode(String ieee) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", ieee);

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "manageLeaveNode.cgi", param);

		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.i("manageLeaveNode Response:%n %s", response);
			}
		};
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

			}
		};

		StringRequest req = new StringRequest(url, responseListener,
				errorListener);
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/***
	 * 2.1 入网
	 * 
	 * @param model
	 */

	public void setPermitJoinOn(String ieee, int time) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", ieee);
		paraMap.put("second", String.valueOf(time));

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "setPermitJoinOn.cgi", param);

		simpleVolleyRequset(url, EventType.SETPERMITJOINON);
	}

	public void setAllPermitJoinOn(int time) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("second", String.valueOf(time));

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "SetAllPermitJoinOn.cgi", param);

		simpleVolleyRequset(url, EventType.SETPERMITJOINON);
	}

	/***
	 * 2.3 插座
	 */
	public void MainsOutLetOperation(DevicesModel model, int operationType) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("operatortype", String.valueOf(operationType));
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "mainsOutLetOperation.cgi", param);

		simpleVolleyRequset(url, EventType.MAINSOUTLETOPERATION);
	}

	/***
	 * 2.4 DimmableLightOperation ZigBee调光开关
	 * http://192.168.1.184/cgi-bin/rest/network/dimmableLightOperation.cgi?
	 * ieee=1234&ep=12&opera
	 * tortype=1&param1=1&param2=2&param3=3&callback=1234&encodemethod
	 * =NONE&sign=AA A Function Describe:Features provided to the DimmableLight
	 * device: Fully-open or fully-close, toggle and output brightness level
	 * control features; provides information update feature of the brightness
	 * level.
	 */
	public void dimmableLightOperation(DevicesModel model, int operationType,
			int param1) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("operatortype", String.valueOf(operationType));
		paraMap.put("param1", String.valueOf(param1));
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "dimmableLightOperation.cgi", param);

		simpleVolleyRequset(url, EventType.ONOFFOUTPUTOPERATION);
	}

	/***
	 * 2.7.1DoorLockOperationCommon 暂定为门窗感应开关的接口
	 * 
	 * GetLockState 0
	 * 
	 * GetLockType 1
	 * 
	 * GetActuatorEnabled 2
	 * 
	 * GetDoorState 3
	 * 
	 * GetDoorOpenEvents 4
	 * 
	 * GetDoorCloseEvents 5
	 * 
	 * GetOpenPeriod 6
	 * 
	 * LockDoor 7
	 * 
	 * UnLockDoor 8
	 */
	public void doorLockOperationCommon(DevicesModel model,
			int operationType) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("operatortype", String.valueOf(operationType));// 7为锁门，8为解锁
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "doorLockOperation.cgi", param);

		simpleVolleyRequset(url, EventType.DOORLOCKOPERATION);

	}

	/***
	 * 2.8.1IASWarningDeviceOperationCommon 暂定为开始和结束警报
	 * 
	 * StopWarning 0
	 * 
	 * StartWarningBurglar 1
	 * 
	 * StartWarningFire 2
	 * 
	 * StartWarningEmergency 3
	 * 
	 * StartWarningDoorBell 4
	 * 
	 * StartWarningTrouble 5
	 * 
	 * SetDuration 8
	 * 
	 * GetDuration 9
	 * 
	 * GetZoneState 10
	 * 
	 * GetZoneType 11
	 * 
	 * GetZoneStatus 12
	 * 
	 * GetZoneIASCIEAddress 13
	 */
	public void IASWarningDeviceOperationCommon(DevicesModel model,
			int operationType) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("operatortype", String.valueOf(operationType));
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("operatortype", "1");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "iasWarningDeviceOperation.cgi",
				param);

		simpleVolleyRequset(url, EventType.IASWARNINGDEVICOPERATION);
	}

	/***
	 * 2.11 无线智能阀门开关，开关模块 Function Describe:Features provided to the OnOffOutput
	 * device: ON or OFF and toggle features; provides information update
	 * feature of the current state. Toggling: if a device is in its ��Off��
	 * state it shall enter its ��On�� state. Otherwise, if it is in its ��On��
	 * state it shall enter its ��Off�� state
	 * http://192.168.1.184/cgi-bin/rest/network/onOffOutputOperation.cgi?
	 * ieee=1234&ep=12&operator
	 * type=1&param1=1&param2=2&param3=3&callback=1234&encodemethod
	 * =NONE&sign=AAA
	 * 
	 * TurnOn 0
	 * 
	 * TurnOff 1
	 * 
	 * Toggle 2
	 * 
	 * GetStatus 3
	 */
	public void OnOffOutputOperation(DevicesModel model, int operationType) {
		// String param =
		// "ieee=00137A0000010AB5&ep=0A&operatortype=0&param1=1&param2=2&param3=3";
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("operatortype", String.valueOf(operationType));
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "onOffOutputOperation.cgi", param);

		simpleVolleyRequset(url, EventType.ONOFFOUTPUTOPERATION);

	}

	/***
	 * 2.13 LightSensor Operation ZigBee光线感应器 Function Describe:Features
	 * provided to the light sensor device: Feature that read the current light
	 * intensity value; feature that processes and stores information.
	 * 
	 * GetBrightness 0
	 * 
	 * GetMeasuredV alue 1
	 * 
	 * GetMinMeasuredV alue 2
	 * 
	 * GetMaxMeasuredV alue 3
	 * 
	 * GetTolerance 4
	 * 
	 * GetLightSensorType 5
	 * 
	 * ChangeIlluminanceMeasuredVal ueRptTime 6
	 * 
	 * GetIlluminanceMeasuredValueR ptTime 7
	 */
	public void lightSensorOperation(DevicesModel model, int operationType) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("operatortype", String.valueOf(operationType));
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "lightSensorOperation.cgi", param);

		Listener<SimpleResponseData> respondListener = new Listener<SimpleResponseData>() {
			@Override
			public void onResponse(SimpleResponseData arg0) {
				SimpleResponseData data = arg0;
				Bundle bundle = new Bundle();
				bundle.putString("IEEE", data.getIeee());
				bundle.putString("EP", data.getEp());
				bundle.putString("PARAM", data.getParam1());
				Event event = new Event(EventType.LIGHTSENSOROPERATION, true);
				event.setData(bundle);
				notifyObservers(event);
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
				Event event = new Event(EventType.LIGHTSENSOROPERATION, false);
				event.setData(error);
				notifyObservers(event);
			}
		};

		CustomRequest<SimpleResponseData> request = new CustomRequest<SimpleResponseData>(
				url, "response_params", SimpleResponseData.class,
				respondListener, errorListener);
		ApplicationController.getInstance().addToRequestQueue(request);
		// simpleVolleyRequset(url, EventType.LIGHTSENSOROPERATION);

	}

	/***
	 * 2.15 ShadeOperation窗帘控制
	 */
	public void shadeOperation(DevicesModel model, int operationType, int level) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("operatortype", String.valueOf(operationType));
		paraMap.put("param1", String.valueOf(level));
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "shadeOperation.cgi", param);

		simpleVolleyRequset(url, EventType.SHADEOPERATION);

	}

	/***
	 * 2.17全局布撤防， operatortype=6 撤防 operatortype=7 布防 operatortype=5 全局布防状态
	 * 查看当前全局布防状态（通过Param1的值判断）： 0：全部撤防 1：白天布防模式 2：夜间布防模式 3：全部布防
	 * 
	 */
	public void LocalIASCIEOperation(DevicesModel model, final int operationType) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("operatortype", String.valueOf(operationType));
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "localIASCIEOperation.cgi", param);

		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// String jsonString = UiUtils.formatResponseString(response);
				// if (operationType == 5) {
				// Gson gson = new Gson();
				// LocalIASCIEOperationResponseData data = gson.fromJson(
				// jsonString, LocalIASCIEOperationResponseData.class);
				// String status = data.getResponse_params().getParam1().trim();
				// Log.i(TAG,
				// "LocalIASCIEOperation get status is "
				// + String.valueOf(status));
				// Event event = new Event(EventType.LOCALIASCIEOPERATION,
				// true);
				// event.setData(status);
				// notifyObservers(event);
				// }else {
				// String status=String.valueOf(operationType);
				// Event event = new Event(EventType.LOCALIASCIEOPERATION,
				// true);
				// event.setData(status);
				// notifyObservers(event);
				// }
			}
		};

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Event event = new Event(EventType.LOCALIASCIEOPERATION, false);
				event.setData(operationType);
				notifyObservers(event);
				// Log.e("Error: ", error.getMessage());
			}
		};

		StringRequest req = new StringRequest(url, responseListener,
				errorListener);

		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
		// simpleVolleyRequset(url, EventType.LOCALIASCIEOPERATION);
	}

	// operatortype=5 全局布防状态
	public void GetLocalIASCIEOperation() {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("operatortype", "5");
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "localIASCIEOperation.cgi", param);

		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				String jsonString = UiUtils.formatResponseString(response);
				Gson gson = new Gson();
				LocalIASCIEOperationResponseData data = gson.fromJson(
						jsonString, LocalIASCIEOperationResponseData.class);
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
				SQLiteDatabase mSQLiteDatabase = mDataHelper
						.getSQLiteDatabase();

				String where = " model_id like ? ";
				String[] args = { DataHelper.One_key_operator + "%" };
				ContentValues c = new ContentValues();
				c.put(DevicesModel.ON_OFF_STATUS, status);

				mDataHelper.update(mSQLiteDatabase, DataHelper.DEVICES_TABLE,
						c, where, args);
				mDataHelper.close(mSQLiteDatabase);
				Event event = new Event(EventType.LOCALIASCIEOPERATION, true);
				event.setData(status);
				notifyObservers(event);
			}
		};

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// Log.e("Error: ", error.getMessage());
			}
		};

		StringRequest req = new StringRequest(url, responseListener,
				errorListener);

		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
		// simpleVolleyRequset(url, EventType.LOCALIASCIEOPERATION);
	}

	/***
	 * 2.1安防设备布防LocalIASCIE ByPassZone
	 * 
	 */
	public void LocalIASCIEByPassZone(DevicesModel model) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("zone_ieee", model.getmIeee());
		paraMap.put("zone_ep", model.getmEP());

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "localIASCIEByPassZone.cgi", param);

		simpleVolleyRequset(url, EventType.LOCALIASCIEUNBYPASSZONE);
	}

	/***
	 * 2.17安防设备撤防LocalIASCIE ByPassZone
	 * 
	 */
	public void LocalIASCIEUnByPassZone(DevicesModel model) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("zone_ieee", model.getmIeee());
		paraMap.put("zone_ep", model.getmEP());

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "localIASCIEUnByPassZone.cgi", param);

		simpleVolleyRequset(url, EventType.LOCALIASCIEUNBYPASSZONE);
	}

	/***
	 * 2.18 IASZone Operation Common ZigBee查看状态
	 * 
	 * WriteHeartBeatPeriod 1
	 * 
	 * GetCIEADDR 2
	 * 
	 * GetZoneType 3
	 * 
	 * GetHeartBeatPeriod 4
	 * 
	 * GetBatteryLevel 5
	 * 
	 * GetZoneState 6
	 * 
	 * GetZoneStatus 7
	 * 
	 * SetIRDisableTimeAlarm 9
	 * 
	 * GetIRDisableTime 10
	 */
	public void iASZoneOperationCommon(DevicesModel model, int operationType,
			int param1) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("operatortype", String.valueOf(operationType));
		paraMap.put("param1", String.valueOf(param1));
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				response = UiUtils.formatResponseString(response);
				Log.i("CGIManager iASZoneOperationCommon Response:%n %s",
						response);
				Gson gson = new Gson();
				ResponseDataEntityForStatus statusData = gson.fromJson(
						response.toString(), ResponseDataEntityForStatus.class);
				Event event = new Event(EventType.IASZONEOPERATION, true);
				event.setData(statusData);
				notifyObservers(event);
			}
		};
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "iasZoneOperation.cgi", param);
		Log.i("CGIManager iASZoneOperationCommon Request:%n %s", url);
		StringRequest req = new StringRequest(url, responseListener,
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String errorString = null;
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("Error: ", error.getMessage());
							errorString = VolleyErrorHelper.getMessage(error,
									ApplicationController.getInstance());
						}
						Event event = new Event(EventType.IASZONEOPERATION,
								false);
						event.setData(errorString);
						notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/***
	 * 2.19TemperatureSensorOperation ZigBee室内型温湿度感应器 operationType 0：温度
	 * operationType 1：湿度
	 * 
	 * GetTemperature 0
	 * 
	 * GetHumidity 1
	 * 
	 * GetReportTime 2
	 * 
	 * GetHumidityRptTime 3
	 * 
	 * GetTemperatureRptTime 4
	 * 
	 * GetUltraVioletRptTime 5
	 * 
	 * GetUltraViolet 6
	 * 
	 * ChangeReportTime 7
	 * 
	 * ChangeHumidityRptTime 8
	 * 
	 * ChangeTemperatureRptTime 9
	 * 
	 * ChangeUltravioletRptTime 10
	 * 
	 * GetTemperatureMeasuredValue11
	 */
	public void temperatureSensorOperation(DevicesModel model, int operationType) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("operatortype", String.valueOf(operationType));
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "temperatureSensorOperation.cgi",
				param);
		final EventType type;
		if (operationType == 0) {
			type = EventType.TEMPERATURESENSOROPERATION;
		} else {
			type = EventType.HUMIDITY;
		}

		Listener<SimpleResponseData> respondListener = new Listener<SimpleResponseData>() {
			@Override
			public void onResponse(SimpleResponseData arg0) {
				SimpleResponseData data = arg0;
				Bundle bundle = new Bundle();
				bundle.putString("IEEE", data.getIeee());
				bundle.putString("EP", data.getEp());
				String value = String
						.valueOf(Float.valueOf(data.getParam1()) / 1000);
				bundle.putString("PARAM", value);
				Event event = new Event(type, true);
				event.setData(bundle);
				notifyObservers(event);
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
				Event event = new Event(type, false);
				event.setData(error);
				notifyObservers(event);
			}
		};

		CustomRequest<SimpleResponseData> request = new CustomRequest<SimpleResponseData>(
				url, "response_params", SimpleResponseData.class,
				respondListener, errorListener);
		ApplicationController.getInstance().addToRequestQueue(request);
		// simpleVolleyRequset(url, type);
	}

	/***
	 * 2.22RangeExtender Operation ZigBee红外控制器
	 * 
	 * GetHumidity 0
	 */
	public void rangeExtenderOperation(DevicesModel model, int operationType) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("operatortype", String.valueOf(operationType));
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "RangeExtender.cgi", param);

		simpleVolleyRequset(url, EventType.RANGEEXTENDER);
	}

	/***
	 * 2.25 IAS ACE 1 心跳
	 * 
	 * WriteHeartBeatPeriod 0
	 * 
	 * RefreshDeviceHeartBeat 1
	 * 
	 * GetDeviceHeartBeat 2
	 * 
	 * RefreshDeviceCIEAddr 3
	 */
	public void iASACE(DevicesModel model, int operationType) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("operatortype", String.valueOf(operationType));
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "IASACE.cgi", param);

		simpleVolleyRequset(url, EventType.IASACE);
	}

	/***
	 * 打开红外学习设备，准备学习 成功返回的数据跟跟BindResponseData一样
	 * 
	 * @param model
	 * @param index
	 * @param operation
	 */
	public void beginLearnIR(DevicesModel model, int index, String operation) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("hadaemonindex", String.valueOf(index));
		paraMap.put("irdisplayname", operation);

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				response = UiUtils.formatResponseString(response);
				Log.i("CGIManager beginLearnIR Response:%n %s", response);
				// Gson gson = new Gson();
				// BindResponseData statusData =
				// gson.fromJson(response.toString(), BindResponseData.class);
				// Event event = new Event(EventType.BEGINLEARNIR, true);
				// event.setData(statusData);
				// notifyObservers(event);
			}
		};
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "BeginLearnIR.cgi", param);
		Log.i("CGIManager beginLearnIR Request:%n %s", url);
		StringRequest req = new StringRequest(url, responseListener,
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String errorString = null;
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("Error: ", error.getMessage());
							errorString = VolleyErrorHelper.getMessage(error,
									ApplicationController.getInstance());
						}
						Event event = new Event(EventType.BEGINLEARNIR, false);
						event.setData(errorString);
						notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);

	}

	/***
	 * 红外控制
	 * 
	 * @param model
	 * @param index
	 * @param operation
	 */
	public void beginApplyIR(DevicesModel model, int index) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("hadaemonindex", String.valueOf(index));

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				response = UiUtils.formatResponseString(response);
				Log.i("CGIManager beginApplyIR Response:%n %s", response);
				Gson gson = new Gson();
				BindResponseData statusData = gson.fromJson(
						response.toString(), BindResponseData.class);
				Event event = new Event(EventType.BEGINAPPLYIR, true);
				event.setData(statusData);
				notifyObservers(event);
			}
		};
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "BeginApplyIR.cgi", param);
		Log.i("CGIManager beginApplyIR Request:%n %s", url);
		StringRequest req = new StringRequest(url, responseListener,
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String errorString = null;
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("Error: ", error.getMessage());
							errorString = VolleyErrorHelper.getMessage(error,
									ApplicationController.getInstance());
						}
						Event event = new Event(EventType.BEGINAPPLYIR, false);
						event.setData(errorString);
						notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);

	}

	public void DeleteIR(DevicesModel model, int index) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("hadaemonindex", String.valueOf(index));

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "DeleteIR.cgi", param);
		Log.i("CGIManager DeleteIR Request:%n %s", url);

		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						response = UiUtils.formatResponseString(response);
						Log.i("CGIManager DeleteIR Response:%n %s", response);
						Gson gson = new Gson();
						BindResponseData statusData = gson.fromJson(
								response.toString(), BindResponseData.class);
						Event event = new Event(EventType.DELETEIR, true);
						event.setData(statusData);
						notifyObservers(event);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String errorString = null;
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("Error: ", error.getMessage());
							errorString = VolleyErrorHelper.getMessage(error,
									ApplicationController.getInstance());
						}
						Event event = new Event(EventType.DELETEIR, false);
						event.setData(errorString);
						notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void getDeviceLearnedIRDataInformation(DevicesModel model) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		Listener<String> responseListener = new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.i("CGIManager GetDeviceLearnedIRDataInformation Response:%n %s",
						response);
				new GetDeviceLearnedTask().execute(response);
			}
		};
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP,
				"GetDeviceLearnedIRDataInformation.cgi", param);
		Log.i("CGIManager GetDeviceLearnedIRDataInformation Request:%n %s", url);
		StringRequest req = new StringRequest(url, responseListener,
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String errorString = null;
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("Error: ", error.getMessage());
							errorString = VolleyErrorHelper.getMessage(error,
									ApplicationController.getInstance());
						}
						Event event = new Event(EventType.GETDEVICELEARNED,
								false);
						event.setData(errorString);
						notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/***
	 * 区域设置：得到房间信息
	 */
	public void GetAllRoomInfo() {
		HashMap<String, String> paraMap = new HashMap<String, String>();

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "getAllRoomInfo.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.i("CGIManager GetRoomInfo Response:%n %s", response);
						new GetAllRoomInfoTask().execute(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String errorString = null;
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("Error: ", error.getMessage());
							errorString = VolleyErrorHelper.getMessage(error,
									ApplicationController.getInstance());
						}
						Event event = new Event(EventType.GETALLROOM, false);
						event.setData(errorString);
						notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/***
	 * 获取指定房间的设备EP信息
	 * 
	 * @param rid
	 */
	public void GetEPByRoomIndex(String rid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("rid", rid);

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "getEPByRoomIndex.cgi", param);
		Log.i("getEPByRoomIndex", url);
		StringRequestChina req = new StringRequestChina(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						new GetEPbyRoomIndexTask().execute(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// String errorString = null;
						// if (error != null && error.getMessage() != null) {
						// VolleyLog.e("Error: ", error.getMessage());
						// errorString = VolleyErrorHelper.getMessage(error,
						// ApplicationController.getInstance());
						// }
						// Event event = new Event(EventType.DELETEIR, false);
						// event.setData(errorString);
						// notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void GetEPByRoomIndexInit(String rid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("rid", rid);

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "getEPByRoomIndex.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						new GetEPbyRoomIndexInitTask().execute(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// String errorString = null;
						// if (error != null && error.getMessage() != null) {
						// VolleyLog.e("Error: ", error.getMessage());
						// errorString = VolleyErrorHelper.getMessage(error,
						// ApplicationController.getInstance());
						// }
						// Event event = new Event(EventType.DELETEIR, false);
						// event.setData(errorString);
						// notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}
	/**
	 * 添加房间
	 * 
	 * @param rid
	 * @param roomname
	 * @param roompic
	 */
	public void ZBAddRoomDataMain(String rid, String roomname, String roompic) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("rid", rid);
		paraMap.put("roomname", roomname);
		paraMap.put("roompic", roompic);

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "zbAddRoomDataMain.cgi", param);
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						response = UiUtils.formatResponseString(response);
						Log.i("CGIManager AddRoomData Response:%n %s", response);
						Gson gson = new Gson();
						RoomData_response_params data = gson.fromJson(
								response.toString(),
								RoomData_response_params.class);
						String status = data.getstatus();
						Event event = new Event(EventType.ROOMDATAMAIN, true);
						event.setData(status);
						notifyObservers(event);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String errorString = null;
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("Error: ", error.getMessage());
							errorString = VolleyErrorHelper.getMessage(error,
									ApplicationController.getInstance());
						}
						Event event = new Event(EventType.ROOMDATAMAIN, false);
						event.setData(errorString);
						notifyObservers(event);
					}
				}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				// TODO Auto-generated method stub
				HashMap<String, String> headers = new HashMap<String, String>();
				// headers.put("Charset", "UTF-8");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				headers.put("Accept-Encoding", "gzip,deflate,sdch");
				headers.put("Accept-Language", "zh-CN,zh;q=0.8");
				return headers;
			}
		};
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/***
	 * 删除指定房间
	 * 
	 * @param rid
	 */
	public void ZBDeleteRoomDataMainByID(final String rid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("rid", rid);

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance()
				.getCumstomURL(NetUtil.getInstance().IP,
						"zbDeleteRoomDataMainByID.cgi", param);

		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						response = UiUtils.formatResponseString(response);
						Log.i("CGIManager DeleteRoomData Response:%n %s",
								response);
						Gson gson = new Gson();
						RoomData_response_params data = gson.fromJson(
								response.toString(),
								RoomData_response_params.class);
						String status = data.getstatus();
						Event event = new Event(EventType.ROOMDATAMAIN, true);
						event.setData(status);
						notifyObservers(event);
						NotifyRoomIdHasBeenDeleted(rid);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String errorString = null;
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("Error: ", error.getMessage());
							errorString = VolleyErrorHelper.getMessage(error,
									ApplicationController.getInstance());
						}
						Event event = new Event(EventType.ROOMDATAMAIN, false);
						event.setData(errorString);
						notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/**
	 * 修改设备的room id
	 * 
	 * @param model
	 * @param new_roomid
	 */
	public void ModifyDeviceRoomId(DevicesModel model, String new_roomid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("new_roomid", new_roomid);

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "ModifyDeviceRoomId.cgi", param);

		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						response = UiUtils.formatResponseString(response);
						Log.i("CGIManager ModifyDeviceRoomId Response:%n %s",
								response);
						Gson gson = new Gson();
						RoomData_response_params data = gson.fromJson(
								response.toString(),
								RoomData_response_params.class);
						String status = data.getstatus();
						Event event = new Event(EventType.MODIFYDEVICEROOMID,
								true);
						event.setData(status);
						notifyObservers(event);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						String errorString = null;
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("Error: ", error.getMessage());
							errorString = VolleyErrorHelper.getMessage(error,
									ApplicationController.getInstance());
						}
						Event event = new Event(EventType.MODIFYDEVICEROOMID,
								false);
						event.setData(errorString);
						notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/**
	 * 通知网关某个房间已被删除。
	 * @param roomid
	 */
	public void NotifyRoomIdHasBeenDeleted(String roomid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("roomid", roomid);

		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "NotifyRoomIdHasBeenDeleted.cgi",
				param);
		Log.i(TAG, url);

		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.i("NotifyRoomIdHasBeenDeleted", response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/***
	 * 设备识别 识别某个设备，即让某个设备的指示灯闪烁指定的时间。
	 * 
	 * @param model
	 * @param time
	 */
	public void IdentifyDevice(DevicesModel model, String time) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("time", time);

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "identifyDevice.cgi", param);
		Log.i("CGIManager IdentifyDevice url:%n %s", url);
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						response = UiUtils.formatResponseString(response);
						Log.i("CGIManager IdentifyDevice Response:%n %s",
								response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("CGIManager IdentifyDevice Error: ",
									error.getMessage());

						}
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/***
	 * 修改设备名称 this function is to change device name
	 * 
	 * @author Trice
	 * 
	 */
	public void ChangeDeviceName(DevicesModel model, String newname) {
		String oldname = model.getmName().replace(" ", "%20");
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("oldname", oldname);
		paraMap.put("newname", newname);

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "ChangeDeviceName.cgi?", param);
		Log.i("CGIManager ChangeDeviceName url:%n %s", url);

		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						response = UiUtils.formatResponseString(response);
						// Log.i("CGIManager ChangeDeviceName Response:%n %s",
						// response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("CGIManager ChangeDeviceName Error: ",
									error.getMessage());

						}
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void ChangeDeviceName(String ieee, String ep, String oldname,
			String newname) {
		String old_name = oldname.replace(" ", "%20");
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", ieee);
		paraMap.put("ep", ep);
		paraMap.put("oldname", old_name);
		paraMap.put("newname", newname);

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "ChangeDeviceName.cgi?", param);
		Log.i("CGIManager ChangeDeviceName url:%n %s", url);

		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response = UiUtils.formatResponseString(response);
						// Log.i("CGIManager ChangeDeviceName Response:%n %s",
						// response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("CGIManager ChangeDeviceName Error: ",
									error.getMessage());

						}
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/**
	 * 停止所有声光报警，包括大洋警报器报警和网关声音报警. 新网关可用（米尔）
	 */
	public void stopAlarm() {
		String url = NetUtil.getInstance().getVideoURL(
				NetUtil.getInstance().IP, "StopAlarm.cgi");
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

	/***
	 * 获取安防设备心跳周期
	 * 
	 */
	public void getHeartTime(DevicesModel mDevices) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", mDevices.getmIeee());
		paraMap.put("ep", mDevices.getmEP());
		paraMap.put("operatortype", "4");
		paraMap.put("param1", "");
		paraMap.put("param2", "");
		paraMap.put("param3", "");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "iasZoneOperation.cgi", param);
		Log.i("CGIManager getHeartTime Request:%n %s", url);
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						response = UiUtils.formatResponseString(response);
						new UpdateDeviceHeartTimeToDatabaseTask()
								.execute(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("CGIManager getHeartTime Error: ",
									error.getMessage());

						}
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	/***
	 * 设定安防设备心跳周期
	 * 
	 * @param mDevices
	 * @param time
	 */
	public void setHeartTime(DevicesModel mDevices, int time) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", mDevices.getmIeee());
		paraMap.put("ep", mDevices.getmEP());
		if(mDevices.getmDeviceId() == DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE) {
			paraMap.put("operatortype", "15");
		} else {
			paraMap.put("operatortype", "1");
		}
		paraMap.put("param1", "" + time);
		paraMap.put("param2", "");
		paraMap.put("param3", "");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);
		String url = "";
		if(mDevices.getmDeviceId() == DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE) {
			url = NetUtil.getInstance().getCumstomURL(
					NetUtil.getInstance().IP, "iasWarningDeviceOperation.cgi", param);
		} else {
			url = NetUtil.getInstance().getCumstomURL(
					NetUtil.getInstance().IP, "iasZoneOperation.cgi", param);
		}
		Log.i("CGIManager setHeartTime Request:%n %s", url);
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("CGIManager setHeartTime Error: ",
									error.getMessage());

						}
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	// 获取历史信息
	public void getHistoryDataByTime(String deviceIeee, String ep,
			String attribute, String stratTime, String endTime) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("deviceID", deviceIeee);
		paraMap.put("deviceEp", ep);
		paraMap.put("attrName", attribute);
		paraMap.put("startTime", stratTime);
		paraMap.put("endTime", endTime);
		String param = hashMap2ParamString(paraMap);

		String url = LibjingleNetUtil.IP_WithHttpHeader + ":8888/SmartHome/getdata_byDate?"
				+ param;
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						response = UiUtils.formatResponseString(response);
						Gson gson = new Gson();
						JsonParser parser = new JsonParser();
						JsonObject jsonObject = parser.parse(response)
								.getAsJsonObject();
						int code = jsonObject.get("returnCode").getAsInt();
						if (code == 104) {
							JsonElement data = jsonObject.get("data");
							HistoryData historyData = gson.fromJson(data,
									HistoryData.class);
							Event event = new Event(EventType.GETHISTORYDATA,
									true);
							event.setData(historyData);
							notifyObservers(event);
						} else {
							Event event = new Event(EventType.GETHISTORYDATA,
									false);
							notifyObservers(event);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("CGIManager setHeartTime Error: ",
									error.getMessage());

						}
					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void getHistoryDataNum(String deviceIeee, String ep,
			String attribute, int number) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("deviceID", deviceIeee);
		paraMap.put("deviceEp", ep);
		paraMap.put("attrName", attribute);
		paraMap.put("dataPointNum", number + "");
		String param = hashMap2ParamString(paraMap);

		String url = LibjingleNetUtil.IP_WithHttpHeader + ":8888/SmartHome/getdata_byNum?"
				+ param;
		Log.i("", url);
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						response = UiUtils.formatResponseString(response);
						Gson gson = new Gson();
						JsonParser parser = new JsonParser();
						JsonObject jsonObject = parser.parse(response)
								.getAsJsonObject();
						int code = jsonObject.get("returnCode").getAsInt();
						if (code == 104) {
							JsonElement data = jsonObject.get("data");
							HistoryData historyData = gson.fromJson(data,
									HistoryData.class);
							Event event = new Event(EventType.GETHISTORYDATA,
									true);
							event.setData(historyData);
							notifyObservers(event);
						} else {
							Event event = new Event(EventType.GETHISTORYDATA,
									false);
							notifyObservers(event);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("CGIManager setHeartTime Error: ",
									error.getMessage());
						}
					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void getGateWayAuthState() {// ========获取网关授权状态====
		String url = NetUtil.getInstance().getVideoURL(
				NetUtil.getInstance().IP, "GetAuthState.cgi");
		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						Log.i("", response);
						getFromSharedPreferences
								.setsharedPreferences(ApplicationController
										.getInstance());
						try {
							JSONObject jsonRsponse = new JSONObject(response);
							int state = jsonRsponse.getInt("state");// ====王晓飞
							int available = jsonRsponse.getInt("available");
							String expire_time = jsonRsponse
									.getString("expire_time");
							getFromSharedPreferences.setGWayAuthState(state);
							getFromSharedPreferences
									.setGWayAuthExpire(expire_time);

							int[] data = { state, available };

							Event event = new Event(EventType.GATEWAYAUTH, true);
							event.setData(data);
							notifyObservers(event);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Event event = new Event(EventType.GATEWAYAUTH, false);
						notifyObservers(event);
					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void getGatewaySwVersion() {
		String url = NetUtil.getInstance().getVideoURL(
				NetUtil.getInstance().IP, "GetSwVersion.cgi");
		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						Log.i("", response);
						getFromSharedPreferences
								.setsharedPreferences(ApplicationController
										.getInstance());
						try {
							JSONObject jsonRsponse = new JSONObject(response);
							String cur_version = (String) jsonRsponse
									.get("cur_sw_version");
							getFromSharedPreferences
									.setGatewaycurrentVersion(cur_version);
							String latest_version = (String) jsonRsponse
									.get("latest_sw_version");
							if (!cur_version.equals(latest_version)) {
								DeviceControlActivity.GATEWAYUPDATE = true;
								String latest_version_now = getFromSharedPreferences
										.getGatewaylatestVersion();
								if (!latest_version_now.equals(latest_version)) {
									DeviceControlActivity.GATEWAYUPDATE_FIRSTTIME = true;
									getFromSharedPreferences
											.setGatewaylatestVersion(latest_version);
								} else {
									DeviceControlActivity.GATEWAYUPDATE_FIRSTTIME = false;
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void gatewayDoUpdate() {
		String url = NetUtil.getInstance().getVideoURL(
				NetUtil.getInstance().IP, "DoUpdate.cgi");
		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void feedbackToServer(String user, String content) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("UserID", user);
		paraMap.put("Context", Uri.encode(content.replace(" ", "%20")));
		// paraMap.put("Context", encodeContent);
		String param = hashMap2ParamString(paraMap);

		// String url = "http://192.168.1.149:8888/SmartHome/feedback?" + param;
		String url = LibjingleNetUtil.IP_WithHttpHeader + ":8888/SmartHome/feedback?" + param;
		Log.i("feedbackToServer", url);
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.i("feedbackToServer", response);
						response = UiUtils.formatResponseString(response);
						JsonParser parser = new JsonParser();
						JsonObject jsonObject = parser.parse(response)
								.getAsJsonObject();
						int code = jsonObject.get("returnCode").getAsInt();
						if (code == 1) {
							Event event = new Event(EventType.FEEDBACKTOSERVER,
									true);
							notifyObservers(event);
						} else {
							Event event = new Event(EventType.FEEDBACKTOSERVER,
									false);
							notifyObservers(event);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error != null && error.getMessage() != null) {
							VolleyLog.e("feedbackToServer Error: ",
									error.getMessage());
							Event event = new Event(EventType.FEEDBACKTOSERVER,
									false);
							notifyObservers(event);
						}
					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	// ==设置邮箱====
	public void changeEmailAddress(String mac, String email, int enable) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("gateId", mac);
		paraMap.put("newEmailAddr", email);
		paraMap.put("sendMailFlag", enable + "");
		String param = hashMap2ParamString(paraMap);

		String url = LibjingleNetUtil.IP_WithHttpHeader + ":8888/GLSmartHome/userinfo_changeEmailAddr?"
				+ param;
		Log.i("changeEmailAddress", url);
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						response = UiUtils.formatResponseString(response);
						JsonParser parser = new JsonParser();
						JsonObject jsonObject = parser.parse(response)
								.getAsJsonObject();
						int code = jsonObject.get("returnCode").getAsInt();
						Log.i("code", code + "");
						if (code == 1) {
							Event event = new Event(
									EventType.CHANGEEMAILADDRESS, true);
							notifyObservers(event);
						} else {
							Event event = new Event(
									EventType.CHANGEEMAILADDRESS, false);
							notifyObservers(event);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	// 获取邮箱状态
	public void getEmailAddrStatus(String mac) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("gateId", mac);
		String param = hashMap2ParamString(paraMap);

		String url = LibjingleNetUtil.IP_WithHttpHeader + ":8888/GLSmartHome/userinfo_getEmailAddrStatus?"
				+ param;
		Log.i("EmailAddrStatus", url);
		StringRequest req = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						Log.i("aaa", response);
						response = UiUtils.formatResponseString(response);
						JsonParser parser = new JsonParser();
						JsonObject jsonObject = parser.parse(response)
								.getAsJsonObject();
						int code = jsonObject.get("returnCode").getAsInt();

						if (code == 1) {
							int sendEmailFLAG = jsonObject
									.get("send_mail_flag").getAsInt();
							String emailContent = jsonObject.get("email")
									.getAsString();
							String[] data = { sendEmailFLAG + "", emailContent };
							Event event = new Event(EventType.GETEMAILADDRESS,
									true);
							event.setData(data);
							notifyObservers(event);
						} else {
							Event event = new Event(EventType.GETEMAILADDRESS,
									false);
							notifyObservers(event);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Event event = new Event(EventType.GETEMAILADDRESS,
								false);
						notifyObservers(event);
					}
				});
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);
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

			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
			for (GetRoomInfo_response info : roomInfoList) {
//				ContentValues c = new ContentValues();
//				c.put(DevicesModel.DEVICE_REGION, info.getroom().getroom_name());
//				String where = " rid = ? ";
//				int rid = info.getroom().getroom_id();
//				String[] args = { rid + "" };
//				mDateHelper.update(mSQLiteDatabase, DataHelper.DEVICES_TABLE,
//						c, where, args);
//				mDateHelper.update(mSQLiteDatabase, DataHelper.RF_DEVICES_TABLE,
//						c, where, args);
				roomList.add(info.getroom());
			}
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
			List<DevicesModel> mDevicesList = DataHelper
					.convertToDevicesModel(devDataList);
//			DataHelper mDateHelper = new DataHelper(
//					ApplicationController.getInstance());
//			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
//
//			for (DevicesModel mDevices : mDevicesList) {
//				ContentValues c = new ContentValues();
//				c.put(DevicesModel.R_ID, mDevices.getmRid());
//				mDateHelper.update(mSQLiteDatabase, DataHelper.DEVICES_TABLE,
//						c, " ieee=? ", new String[] { mDevices.getmIeee() });
//			}
//			mSQLiteDatabase.close();
			return mDevicesList;
		}

		@Override
		protected void onPostExecute(Object result) {
			Event event = new Event(EventType.GETEPBYROOMINDEX, true);
			event.setData(result);
			notifyObservers(event);
		}

	}

	class GetEPbyRoomIndexInitTask extends AsyncTask<String, Object, Object> {
		@Override
		protected Object doInBackground(String... params) {
			RespondDataEntity<ResponseParamsEndPoint> data = VolleyOperation
					.handleEndPointString(params[0]);
			ArrayList<ResponseParamsEndPoint> devDataList = data
					.getResponseparamList();
			List<DevicesModel> mDevicesList = DataHelper
					.convertToDevicesModel(devDataList);
//			DataHelper mDateHelper = new DataHelper(
//					ApplicationController.getInstance());
//			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
//
//			for (DevicesModel mDevices : mDevicesList) {
//				ContentValues c = new ContentValues();
//				c.put(DevicesModel.R_ID, mDevices.getmRid());
//				mDateHelper.update(mSQLiteDatabase, DataHelper.DEVICES_TABLE,
//						c, " ieee=? ", new String[] { mDevices.getmIeee() });
//			}
//			mSQLiteDatabase.close();
			return mDevicesList;
		}

		@Override
		protected void onPostExecute(Object result) {
			Event event = new Event(EventType.GETEPBYROOMINDEXINIT, true);
			event.setData(result);
			notifyObservers(event);
		}

	}
	
	class UpdateDeviceHeartTimeToDatabaseTask extends
			AsyncTask<String, Bundle, Bundle> {

		@Override
		protected Bundle doInBackground(String... params) {
			// TODO Auto-generated method stub
			String response = params[0];
			Bundle bundle = new Bundle();
			try {
				JSONObject json = new JSONObject(response);
				JSONObject jsonParams = json.getJSONObject("response_params");
				bundle.putString("ieee", jsonParams.getString("ieee"));
				bundle.putString("ep", jsonParams.getString("ep"));
				bundle.putInt("time", jsonParams.getInt("param1"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ContentValues c = new ContentValues();
			c.put(DevicesModel.HEART_TIME, bundle.getInt("time"));

			String where = " ieee = ? and ep = ?";
			String ieee = bundle.getString("ieee");
			String ep = bundle.getString("ep");
			String[] args = { ieee, ep };
			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
			mDateHelper.update(mSQLiteDatabase, DataHelper.DEVICES_TABLE, c,
					where, args);
			mDateHelper.close(mSQLiteDatabase);
			return bundle;
		}

		@Override
		protected void onPostExecute(Bundle result) {
			// TODO Auto-generated method stub
			Event event = new Event(EventType.HEARTTIME, true);
			event.setData(result);
			notifyObservers(event);
			super.onPostExecute(result);
		}

	}
}
