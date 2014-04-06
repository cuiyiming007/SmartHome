package com.gdgl.model;

public class DevicesGroup {
	private int groupId;
	private int groupType; // 场景，区域等
	private String groupName;
	private boolean[] devicesState; // 预设设备状态
	private float devicesValue;

	public DevicesGroup(int id, int type, String name, boolean[] state) {
		// TODO Auto-generated constructor stub
		this(id, type, name, state, -1);
	}

	public DevicesGroup(int id, int type, String name, boolean[] state,
			float value) {
		// TODO Auto-generated constructor stub
		setGroupId(id);
		setGroupType(type);
		setGroupName(name);
		setDevicesState(state);
		setDevicesValue(value);
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public boolean[] getDevicesState() {
		return devicesState;
	}

	public void setDevicesState(boolean[] devicesState) {
		this.devicesState = devicesState;
	}

	public float getDevicesValue() {
		return devicesValue;
	}

	public void setDevicesValue(float devicesValue) {
		this.devicesValue = devicesValue;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
}
