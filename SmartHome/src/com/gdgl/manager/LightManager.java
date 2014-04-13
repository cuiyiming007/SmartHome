package com.gdgl.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.EventType;
import com.gdgl.util.NetUtil;

public class LightManager extends Manger {

	private static LightManager instance;

	public static LightManager getInstance() {
		if (instance == null) {
			instance = new LightManager();
		}
		return instance;
	}

	/***
	 * 2.10 on off light switch operation
	 * http://192.168.1.184/cgi-bin/rest/network/onOffLightSwitchOperation.cgi?
	 * ieee=00137A00000031
	 * 1B&ep=01&operatortype=0&param1=1&param2=2&param3=3&callback
	 * =1234&encodemethod =NONE&sign=AAA
	 */
	public void OnOffLightSwitchOperation() {
		String param = "ieee=00137A0000010AB5&ep=0A&operatortype=2&param1=1&param2=2&param3=3";
		String url = NetUtil.getInstance().getCumstomURL(
				"onOffLightSwitchOperation.cgi", param);
		simpleVolleyRequset(url,EventType.ONOFFLIGHTSWITCHOPERATION);
	}

	/***
	 * 2.11 Function Describe:Features provided to the OnOffOutput device: ON or
	 * OFF and toggle features; provides information update feature of the
	 * current state. Toggling: if a device is in its ��Off�� state it shall
	 * enter its ��On�� state. Otherwise, if it is in its ��On�� state it shall
	 * enter its ��Off�� state
	 * http://192.168.1.184/cgi-bin/rest/network/onOffOutputOperation.cgi?
	 * ieee=1234&ep=12&operator
	 * type=1&param1=1&param2=2&param3=3&callback=1234&encodemethod
	 * =NONE&sign=AAA
	 */
	public void OnOffOutputOperation() {
		// String param =
		// "ieee=00137A0000010AB5&ep=0A&operatortype=0&param1=1&param2=2&param3=3";
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", "00137A0000010AB5");
		paraMap.put("ep", "0A");
		paraMap.put("operatortype", "0");
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				"onOffOutputOperation.cgi", param);

		simpleVolleyRequset(url, EventType.ONOFFOUTPUTOPERATION);

	}

	/***
	 * 2.13 LightSensor Operation Function Describe:Features provided to the
	 * light sensor device: Feature that read the current light intensity value;
	 * feature that processes and stores information.
	 * 
	 */
	public void lightSensorOperation() {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", "00137A000001181F");
		paraMap.put("ep", "0A");
		paraMap.put("operatortype", "0");
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				"onOffOutputOperation.cgi", param);

		simpleVolleyRequset(url, EventType.ONOFFOUTPUTOPERATION);

	}

	/***
	 * 2.4 DimmableLightOperation
	 * http://192.168.1.184/cgi-bin/rest/network/dimmableLightOperation.cgi?
	 * ieee=1234&ep=12&opera
	 * tortype=1&param1=1&param2=2&param3=3&callback=1234&encodemethod
	 * =NONE&sign=AA A Function Describe:Features provided to the DimmableLight
	 * device: Fully-open or fully-close, toggle and output brightness level
	 * control features; provides information update feature of the brightness
	 * level.
	 */
	public void dimmableLightOperation() {
		HashMap<String, String> paraMap=new HashMap<String, String>();
		paraMap.put("ieee", "00137A000001181F");
		paraMap.put("ep", "01");
		paraMap.put("operatortype", "0");
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");
		String param=hashMap2ParamString(paraMap);
		
		String url = NetUtil.getInstance().getCumstomURL(
				"lightSensorOperation.cgi", param);
		
		simpleVolleyRequset(url, EventType.ONOFFOUTPUTOPERATION);
	}

	/***
	 * 2.5 OnOffLight Operation Function Describe:Features provided to the Light
	 * device: on, off or toggle features; provides information update feature
	 * of the current state.
	 * 
	 * http://192.168.1.184/cgi-bin/rest/network/onOffLightOperation.cgi?
	 * ieee=00137A0000008110&ep
	 * =01&operatortype=0&param1=1&param2=2&param3=3&callback
	 * =1234&encodemethod=NON E&sign=AAA
	 * 
	 */
	public void onOffLightOperation() {

		String param = "ieee=00137A0000010AB5&ep=0A&operatortype=2&param1=1&param2=2&param3=3";
		String url = NetUtil.getInstance().getCumstomURL(
				"onOffLightSwitchOperation.cgi", param);
		EventType type = EventType.ONOFFLIGHTOPERATION;
		simpleVolleyRequset(url, type);

	}


	/***
	 * 2.7.1DoorLockOperationCommon 暂定为门窗感应开关的接口
	 */
	public void doorLockOperationCommon() {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", "00137A0000011598");
		paraMap.put("ep", "01");
		paraMap.put("operatortype", "7");//7为锁门，8为解锁
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				"doorLockOperation.cgi", param);

		simpleVolleyRequset(url, EventType.DOORLOCKOPERATION);

	}
	/***

	/***
	 * 2.8.1IASWarningDeviceOperationCommon 暂定为开始和结束警报
	 * 
	 */
	public void IASWarningDeviceOperationCommon()
	{
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", "00137A0000011949");
		paraMap.put("ep", "01");
		paraMap.put("operatortype", "7");//7为锁门，8为解锁
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("operatortype", "1");
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				"doorLockOperation.cgi", param);

		simpleVolleyRequset(url, EventType.DOORLOCKOPERATION);
	}
	/***
	 * 2.12OnOffSwitch Operation
	 */
	public void onOffSwitchOperation() {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("ieee", "00137A0000010AB5");
		paraMap.put("ep", "01");
		paraMap.put("operatortype", "1");
		paraMap.put("param1", "1");
		paraMap.put("param2", "2");
		paraMap.put("param3", "3");
		String param = hashMap2ParamString(paraMap);
		
		String url = NetUtil.getInstance().getCumstomURL(
				"onOffSwitchOperation.cgi", param);
		
		simpleVolleyRequset(url, EventType.ONOFFSWITCHOPERATION);
	}
}
