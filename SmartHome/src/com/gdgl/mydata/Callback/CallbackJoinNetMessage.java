package com.gdgl.mydata.Callback;
/***
 * messagetype=29
 * @author Trice
 *
 */
public class CallbackJoinNetMessage {
	private String msgtype;
	private CallbackJoinNet_device device;
	
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public CallbackJoinNet_device getDevice() {
		return device;
	}
}
