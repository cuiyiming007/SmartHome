package com.gdgl.model;

import java.util.List;

import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;

import android.content.ContentValues;
import android.content.Context;
import android.provider.BaseColumns;



interface DevicesGroupColumns extends BaseColumns {

	public static final String GROUP_ID = "group_id";
	public static final String GROUP_NAME = "group_name";
	public static final String DEVICES_IEEE = "devices_ieee";
	public static final String EP = "ep";
	public static final String ON_OFF_STATUS = "on_off_status";
	public static final String DEVICES_VALUE = "devices_value";
	public static final String GROUP_STATE = "group_state";

}
public class DevicesGroup implements DevicesGroupColumns{
	private int groupId; // 场景，区域等
	private String groupName;
	private String ieee;
	private String ep;
	private boolean devicesState; // 预设设备状态
	private int devicesValue;
	private boolean groupState=true;
	private SimpleDevicesModel sModel;
	
	public static int STATE_ON=1;
	public static int STATE_OFF=0;
	
	Context mContext;
	
	public DevicesGroup(Context c) {
		mContext=c;
	}

	public DevicesGroup(int id, String name, boolean state,
			int value,String ieee,String ep,boolean gpstate,Context c) {
		// TODO Auto-generated constructor stub
		setGroupId(id);
		setGroupName(name);
		setDevicesState(state);
		setDevicesValue(value);
		setIeee(ieee);
		setEp(ep);
		setGroupState(gpstate);
		mContext=c;
	}
	
	public ContentValues convertContentValues() {
		ContentValues mContentValues = new ContentValues();

		mContentValues.put(DevicesGroup.DEVICES_IEEE, getIeee());
		mContentValues.put(DevicesGroup.DEVICES_VALUE, getDevicesValue());
		mContentValues.put(DevicesGroup.EP,getEp());
		mContentValues.put(DevicesGroup.GROUP_ID, getGroupId());
		mContentValues.put(DevicesGroup.GROUP_NAME, getGroupName());
		mContentValues.put(DevicesGroup.ON_OFF_STATUS, getDevicesState()?STATE_ON:STATE_OFF);
		mContentValues.put(DevicesGroup.GROUP_STATE, getDevicesState()?STATE_ON:STATE_OFF);

		return mContentValues;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public boolean getDevicesState() {
		return devicesState;
	}

	public void setDevicesState(boolean devicesState) {
		this.devicesState = devicesState;
	}

	public int getDevicesValue() {
		return devicesValue;
	}

	public void setDevicesValue(int devicesValue) {
		this.devicesValue = devicesValue;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getIeee() {
		return ieee;
	}

	public void setIeee(String ieee) {
		this.ieee = ieee;
	}

	public SimpleDevicesModel getsModel() {
		String args[]={ieee};
		String where=" ieee=? ";
		List<SimpleDevicesModel> mList= DataUtil.getDevices(mContext, new DataHelper(mContext), args, where);
		if(null!=mList && mList.size()>0){
			return mList.get(0);
		}
		return null;
	}

	public String getEp() {
		return ep;
	}

	public void setEp(String ep) {
		this.ep = ep;
	}

	public boolean isGroupState() {
		return groupState;
	}

	public void setGroupState(boolean groupState) {
		this.groupState = groupState;
	}
}
