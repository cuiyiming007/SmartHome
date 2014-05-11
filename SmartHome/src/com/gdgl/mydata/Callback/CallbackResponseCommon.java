package com.gdgl.mydata.Callback;
/***
 * messagetype=4,5,6,8,9,10,11
 * @author justek
 *
 */
public class CallbackResponseCommon {
	private String deviceID;
	private String msgtype;
	private String callbackType;
	private String IEEE;
	private String EP;
	private String Value;
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public String getCallbackType() {
		return callbackType;
	}
	public void setCallbackType(String callbackType) {
		this.callbackType = callbackType;
	}
	public String getIEEE() {
		return IEEE;
	}
	public void setIEEE(String iEEE) {
		IEEE = iEEE;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public String getEP() {
		return EP;
	}
	public void setEP(String eP) {
		EP = eP;
	}
	@Override
	public String toString() {
		return "IASZone [deviceID=" + deviceID + ", msgtype=" + msgtype
				+ ", callbackType=" + callbackType + ", IEEE=" + IEEE + ", EP="
				+ EP + ", Value=" + Value + "]";
	}
	
	

}
