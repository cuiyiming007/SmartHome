package com.gdgl.mydata.Callback;

public class CallbackWarmMessage {
	// {"cie_ep":"0A","roomId":"-1","cie_ieee":"00137A00000101D1","cie_name":"NULL","home_id":"00",
	// "time":"2000-01-13 13:37:21","houseIEEE":"00137A00000101D1","w_mode":"1","zone_ieee":"00137A0000011598","zone_name":"","msgtype":3,"room_name":"","w_description":"Burglar","home_name":"","zone_ep":"02"}

	private String cie_ep;
	private String roomId;
	private String cie_ieee;
	private String cie_name;
	private String home_id;
	private String time;
	private String houseIEEE;
	private String w_mode;
	private String zone_ieee;
	private String zone_name;
	private String msgtype;
	private String w_description;
	private String home_name;
	private String zone_ep;
	
	public String getCie_ep() {
		return cie_ep;
	}
	public void setCie_ep(String cie_ep) {
		this.cie_ep = cie_ep;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getCie_ieee() {
		return cie_ieee;
	}
	public void setCie_ieee(String cie_ieee) {
		this.cie_ieee = cie_ieee;
	}
	public String getCie_name() {
		return cie_name;
	}
	public void setCie_name(String cie_name) {
		this.cie_name = cie_name;
	}
	public String getHome_id() {
		return home_id;
	}
	public void setHome_id(String home_id) {
		this.home_id = home_id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getHouseIEEE() {
		return houseIEEE;
	}
	public void setHouseIEEE(String houseIEEE) {
		this.houseIEEE = houseIEEE;
	}
	public String getW_mode() {
		return w_mode;
	}
	public void setW_mode(String w_mode) {
		this.w_mode = w_mode;
	}
	public String getZone_ieee() {
		return zone_ieee;
	}
	public void setZone_ieee(String zone_ieee) {
		this.zone_ieee = zone_ieee;
	}
	public String getZone_name() {
		return zone_name;
	}
	public void setZone_name(String zone_name) {
		this.zone_name = zone_name;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public String getW_description() {
		return w_description;
	}
	public void setW_description(String w_description) {
		this.w_description = w_description;
	}
	public String getHome_name() {
		return home_name;
	}
	public void setHome_name(String home_name) {
		this.home_name = home_name;
	}
	public String getZone_ep() {
		return zone_ep;
	}
	public void setZone_ep(String zone_ep) {
		this.zone_ep = zone_ep;
	}
	@Override
	public String toString() {
		return "CallbackWarmMessage [cie_ep=" + cie_ep + ", roomId=" + roomId
				+ ", cie_ieee=" + cie_ieee + ", cie_name=" + cie_name
				+ ", home_id=" + home_id + ", time=" + time + ", houseIEEE="
				+ houseIEEE + ", w_mode=" + w_mode + ", zone_ieee=" + zone_ieee
				+ ", zone_name=" + zone_name + ", msgtype=" + msgtype
				+ ", w_description=" + w_description + ", home_name="
				+ home_name + ", zone_ep=" + zone_ep + "]";
	}
	
	
}
