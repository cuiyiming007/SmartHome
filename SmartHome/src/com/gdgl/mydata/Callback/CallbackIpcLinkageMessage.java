package com.gdgl.mydata.Callback;

import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.getFromSharedPreferences;

import android.content.ContentValues;
import android.provider.BaseColumns;

interface CallbackIpcLinkageColumns extends BaseColumns {
	public static final String _ID = "_id";
	public static final String GATEWAYMAC = "gateway_mac";
	public static final String TYPE = "type"; //1 for picture_shot, 2 for short_video
	public static final String DEVICE_IEEE = "ieee";
	public static final String DEVICE_NAME = "device_name";
	public static final String DEVICE_PIC = "device_pic";
	public static final String IPC_ID = "ipc_id";
	public static final String IPC_NAME = "ipc_name";
	public static final String TIME = "time";
	public static final String PICCOUNT = "pic_count";
	public static final String PICNAME = "pic_name";
	public static final String DESCRIPTION = "description";
}

public class CallbackIpcLinkageMessage implements CallbackIpcLinkageColumns {
	private String id;
	private int type;
	private String ieee;
	private String device_name;
	private String device_pic;
	private int ipc_id;
	private String ipc_name;
	private String time;
	private int pic_count;
	private String pic_name;
	private String description;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public String getDeviceIeee() {
		return ieee;
	}
	public void setDeviceIeee(String ieee) {
		this.ieee = ieee;
	}
	
	public String getDeviceName() {
		return device_name;
	}
	public void setDeviceName(String name) {
		this.device_name = name;
	}
	
	public String getDevicePic() {
		return device_pic;
	}
	public void setDevicePic(String pic) {
		this.device_pic = pic;
	}
	
	public int getIpcId() {
		return ipc_id;
	}
	public void setIpcId(int id) {
		this.ipc_id = id;
	}
	
	public String getIpcName() {
		return ipc_name;
	}
	public void setIpcName(String ipc_name) {
		this.ipc_name = ipc_name;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public int getPicCount() {
		return pic_count;
	}
	public void setPicCount(int count) {
		this.pic_count = count;
	}

	public String getPicName() {
		return pic_name;
	}
	public void setPicName(String pic_name) {
		this.pic_name = pic_name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public ContentValues convertContentValues() {
		ContentValues mContentValues = new ContentValues();
		getFromSharedPreferences.setsharedPreferences(ApplicationController.getInstance());
		
		mContentValues.put(GATEWAYMAC, getFromSharedPreferences.getGatewayMAC());
		mContentValues.put(TYPE, type);
		mContentValues.put(DEVICE_IEEE, ieee);
		mContentValues.put(DEVICE_NAME, device_name);
		mContentValues.put(DEVICE_PIC, device_pic);
		mContentValues.put(IPC_ID, ipc_id);
		mContentValues.put(IPC_NAME, ipc_name);
		mContentValues.put(TIME, time);
		mContentValues.put(PICCOUNT, pic_count);
		mContentValues.put(PICNAME, pic_name);
		mContentValues.put(DESCRIPTION, description);
		return mContentValues;
	}
}
