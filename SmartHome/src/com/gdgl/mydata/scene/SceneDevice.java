package com.gdgl.mydata.scene;

import android.content.ContentValues;
import android.provider.BaseColumns;

interface SceneDevicesColumn extends BaseColumns {

	public static final String SCENE_ID = "sid";
	public static final String ACTION_TYPE = "action_type";
	public static final String DEVICE_IEEE = "device_ieee";
	public static final String DEVICE_EP = "ep";
	public static final String DEVICESTATS = "device_status";

}

public class SceneDevice implements SceneDevicesColumn {
	private int sid; // 场景，区域等
	private int actionType;
	private String ieee;
	private String ep;
	private int devicesStatus; // 预设设备状态

	public SceneDevice() {
		// TODO Auto-generated constructor stub
	}
	
	public SceneDevice(String actionParam) {
		// TODO Auto-generated constructor stub
		String[] params = actionParam.split(":");
		actionType = Integer.parseInt(params[0]);
		String[] subparams = params[1].split("-");
		ieee = subparams[0];
		ep = subparams[1];
		devicesStatus = Integer.parseInt(subparams[2]);
	}
	
	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}
	
	public int getActionType() {
		return actionType;
	}
	
	public void setActionType(int type) {
		this.actionType = type;
	}
	public String getIeee() {
		return ieee;
	}

	public void setIeee(String ieee) {
		this.ieee = ieee;
	}

	public String getEp() {
		return ep;
	}

	public void setEp(String ep) {
		this.ep = ep;
	}
	
	public int getDevicesStatus() {
		return devicesStatus;
	}

	public void setDevicesStatus(int devicesStatus) {
		this.devicesStatus = devicesStatus;
	}

	public ContentValues convertContentValues() {
		ContentValues mContentValues = new ContentValues();

		mContentValues.put(SceneDevice.SCENE_ID, sid);
		mContentValues.put(SceneDevice.ACTION_TYPE, actionType);
		mContentValues.put(SceneDevice.DEVICE_IEEE, ieee);
		mContentValues.put(SceneDevice.DEVICE_EP, ep);
		mContentValues.put(SceneDevice.DEVICESTATS, devicesStatus);

		return mContentValues;
	}
	
	public String creatSceneParam() {
		String actionParam = actionType + ":" + ieee + "-" + ep + "-" + devicesStatus;
		
		return actionParam;
	}
}
