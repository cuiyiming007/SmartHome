package com.gdgl.libjingle;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.gdgl.manager.Manger;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.LibjingleSendStructure;

public class LibjingleSendManager extends Manger {

	private final static String TAG = "LibjingleManager";

	public static ArrayList<LibjingleSendStructure> sendList = new ArrayList<LibjingleSendStructure>();

	private static LibjingleSendManager instance;

	public static LibjingleSendManager getInstance() {
		if (instance == null) {
			instance = new LibjingleSendManager();
		}
		return instance;
	}

	private static int reqID = 0;

	public int getReqID() {
		reqID = reqID % 65536;
		return reqID++;
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"getendpoint.cgi", param);
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.GETENDPOINT);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"AddBindData.cgi", param);
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.ADDBINDDATA);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"DelBindData.cgi", param);
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.DELBINDDATA);
		sendList.add(mStructure);
	}

	public void GetAllBindList() {
		HashMap<String, String> paraMap = new HashMap<String, String>();

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);
		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"GetAllBindList.cgi", param);
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.GETALLBINDLIST);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"manageLeaveNode.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.MANAGELEAVENODE);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"setPermitJoinOn.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.SETPERMITJOINON);
		sendList.add(mStructure);
	}

	public void setAllPermitJoinOn(int time) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("second", String.valueOf(time));

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"SetAllPermitJoinOn.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.SETALLPERMITJOINON);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"mainsOutLetOperation.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.MAINSOUTLETOPERATION);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"dimmableLightOperation.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.DIMMABLELIGHTOPERATION);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"doorLockOperation.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		// mStructure.setAPI_type(LibjingleSendStructure.d);
		sendList.add(mStructure);

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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"iasWarningDeviceOperation.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure
				.setAPI_type(LibjingleSendStructure.IASWARNINGDEVICEOPERATION);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"onOffOutputOperation.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.ONOFFOUTPUTOPERATION);
		sendList.add(mStructure);

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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"lightSensorOperation.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.LIGHTSENSOROPERATION);
		sendList.add(mStructure);

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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"shadeOperation.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.SHADEOPERATION);
		sendList.add(mStructure);

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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"localIASCIEOperation.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.LOCALIASCIEOPERATION);
		sendList.add(mStructure);
	}
	//operatortype=5 全局布防状态
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"localIASCIEOperation.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.GETLOCALIASCIEOPERATION);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"localIASCIEByPassZone.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.LOCALIASCIEBYPASSZONE);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"localIASCIEUnByPassZone.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.LOCALIASCIEUNBYPASSZONE);
		sendList.add(mStructure);
	}

	public void getLocalCIEList() {
		HashMap<String, String> paraMap = new HashMap<String, String>();

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"GetLocalCIEList.cgi", param);
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.GETLOCALCIELIST);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"iasZoneOperation.cgi", param);
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.IASZONEOPERATION);
		sendList.add(mStructure);
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
	public void humiditySensorOperation(DevicesModel model) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("operatortype", "1");
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"temperatureSensorOperation.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.HUMIDITY);
		sendList.add(mStructure);
	}

	public void temperatureSensorOperation(DevicesModel model) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());
		paraMap.put("operatortype", "0");
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"temperatureSensorOperation.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure
				.setAPI_type(LibjingleSendStructure.TEMPERATURESENSOROPERATION);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"RangeExtender.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		// mStructure.setAPI_type(LibjingleSendStructure.range);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"IASACE.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		// mStructure.setAPI_type(LibjingleSendStructure.iasace);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"BeginLearnIR.cgi", param);
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.BEGINLEARNIR);
		sendList.add(mStructure);

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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"BeginApplyIR.cgi", param);
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.BEGINAPPLYIR);
		sendList.add(mStructure);

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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"DeleteIR.cgi", param);
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.DELETEIR);
		sendList.add(mStructure);
	}

	public void getDeviceLearnedIRDataInformation(DevicesModel model) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", model.getmIeee());
		paraMap.put("ep", model.getmEP());

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(

		"GetDeviceLearnedIRDataInformation.cgi", param);
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure
				.setAPI_type(LibjingleSendStructure.GETDEVICELEARNEDIRDATAINFORMATION);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"getAllRoomInfo.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.GETALLROOMINFO);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"getEPByRoomIndex.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.GETEPBYROOMINDEX);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"zbAddRoomDataMain.cgi", param);
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.ZBADDROOMDATAMAIN);
		sendList.add(mStructure);
	}

	/***
	 * 删除指定房间
	 * 
	 * @param rid
	 */
	public void ZBDeleteRoomDataMainByID(String rid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("rid", rid);

		paraMap.put("callback", "1234");
		paraMap.put("encodemethod", "NONE");
		paraMap.put("sign", "AAA");
		String param = hashMap2ParamString(paraMap);

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"zbDeleteRoomDataMainByID.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.ZBDELETEROOMDATAMAINBYID);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"ModifyDeviceRoomId.cgi", param);

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.MODIFYDEVICEROOMID);
		sendList.add(mStructure);
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

		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"identifyDevice.cgi", param);
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.IDENTIFYDEVICE);
		sendList.add(mStructure);
	}
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
		
		String url = LibjingleNetUtil.getInstance().getLocalhostURL(
				"iasZoneOperation.cgi", param);
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.READHEARTTIME);
		sendList.add(mStructure);
	}
	
	/**
	 * *********************************************************************************************************************
	 * 视频请求
	 */
	
	//获取摄像头列表
	public void getIPClist() {
		String url = LibjingleNetUtil.getInstance().getVideoURL("getIPClist.cgi");

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.GETVIDEOLIST);
		sendList.add(mStructure);
	}
	//申请视频数据
	public void sendVideoReq(int channelNum) {
		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();
		JSONObject videoCommand = new JSONObject();
		JSONObject send = new JSONObject();
		try {
			send.put("action", "startvideo");
			send.put("channel", channelNum);
			videoCommand.put("request_id", reqid);
			videoCommand.put("gl_msgtype", LibjinglePackHandler.MT_IpcVideo);
			videoCommand.put("jid", jid);
			videoCommand.put("send", send);
			String packag = videoCommand.toString();
			Log.i(TAG, packag);
			LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);
		} catch (JSONException ex) {
			
		}
		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_IpcVideo);
		mStructure.setAPI_type(LibjingleSendStructure.REQUESTVIDEO);
		sendList.add(mStructure);
	}
	
	//获取场景列表
	public void GetSceneList() {
		String url = LibjingleNetUtil.getInstance().getVideoURL("GetSceneList.cgi");

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.GETVIDEOLIST);
		sendList.add(mStructure);
	}
	//获取联动列表
	public void GetLinkageList() {
		String url = LibjingleNetUtil.getInstance().getVideoURL("GetLinkageList.cgi");

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.GETLINKAGELIST);
		sendList.add(mStructure);
	}
	//获取定时列表
	public void GetTimeActionList() {
		String url = LibjingleNetUtil.getInstance().getVideoURL("GetTimeActionList.cgi");

		String jid = LibjinglePackHandler.getJid();
		int reqid = getReqID();

		String packag = LibjinglePackHandler.packUrl(reqid, jid, url);
		// Log.i(TAG, packag);
		LibjingleNetUtil.getInstance().sendMsgToLibjingleSocket(packag);

		LibjingleSendStructure mStructure = new LibjingleSendStructure(sendList);
		mStructure.setRequest_id(reqid);
		mStructure.setGl_msgtype(LibjinglePackHandler.MT_URL);
		mStructure.setAPI_type(LibjingleSendStructure.GETTIMEACTIONLIST);
		sendList.add(mStructure);
	}
}
