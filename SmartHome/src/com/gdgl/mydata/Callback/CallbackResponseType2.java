package com.gdgl.mydata.Callback;

public class CallbackResponseType2 {
	private String msgtype;
	private String houseIEEE;
	private String roomId;
	private String deviceIeee;
	private String deviceEp;
	private String clusterId;
	private String attributeId;
	private String value;
	private String attributename;
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public String getHouseIEEE() {
		return houseIEEE;
	}
	public void setHouseIEEE(String houseIEEE) {
		this.houseIEEE = houseIEEE;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getDeviceIeee() {
		return deviceIeee;
	}
	public void setDeviceIeee(String deviceIeee) {
		this.deviceIeee = deviceIeee;
	}
	public String getDeviceEp() {
		return deviceEp;
	}
	public void setDeviceEp(String deviceEp) {
		this.deviceEp = deviceEp;
	}
	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public String getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAttributename() {
		return attributename;
	}
	public void setAttributename(String attributename) {
		this.attributename = attributename;
	}
	
	@Override
	public String toString() {
		return "CallbackResponseType2 [msgtype=" + msgtype + ", houseIEEE="
				+ houseIEEE + ", roomId=" + roomId + ", deviceIeee="
				+ deviceIeee + ", deviceEp=" + deviceEp + ", clusterId="
				+ clusterId + ", attributeId=" + attributeId + ", value="
				+ value + ", attributename=" + attributename + "]";
	}
	
	
	

}
