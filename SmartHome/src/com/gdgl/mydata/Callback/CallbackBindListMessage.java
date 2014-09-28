package com.gdgl.mydata.Callback;

import java.util.ArrayList;

public class CallbackBindListMessage {
	private String msgtype;
	private String IEEE;
	private String EP;
	private String count;
	private ArrayList<CallbackBindListDevices> list;
	public String getmsgtype() {
		return msgtype;
	}
	public void setmsgtype(String type) {
		this.msgtype=type;
	}
	public String getIeee() {
		return IEEE;
	}
	public void setIeee(String ieee) {
		this.IEEE = ieee;
	}
	public String getEp() {
		return EP;
	}
	public void setEp(String ep) {
		this.EP = ep;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public ArrayList<CallbackBindListDevices> getList() {
		return list;
	}
	public void setList(ArrayList<CallbackBindListDevices> list) {
		this.list = list;
	}
}
