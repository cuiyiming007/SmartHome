package com.gdgl.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DevicesModel implements Parcelable {
	private int modelId;
	private String devicesID;
	private String devicesName;
	private String devicesAnotherName = ""; // 别名
	private int devicesType;
	private boolean[] devicesState;
	private float devicesValue = -2;
	private String devicesRegion = "";

	public DevicesModel(int id, String deviceID, String name, int type,
			boolean[] state, String region) {
		// TODO Auto-generated constructor stub
		this(id, deviceID, name, "", type, -2, region, state);
	}

	public DevicesModel(int id, String deviceID, String name,
			String AnotherName, int type, boolean[] state, String region) {
		// TODO Auto-generated constructor stub
		this(id, deviceID, name, AnotherName, type, -2, region, state);
	}

	public DevicesModel(int id, String deviceID, String name, int type,
			float value, boolean[] state, String region) {
		// TODO Auto-generated constructor stub
		this(id, deviceID, name, "", type, value, region, state);
	}

	public DevicesModel(int id, String deviceID, String name,
			String AnotherName, int type, float value, boolean[] state,
			String region) {
		// TODO Auto-generated constructor stub
		this(id, deviceID, name, AnotherName, type, value, region, state);
	}

	public DevicesModel(int id, String deviceID, String name,
			String AnotherName, int type, String region, boolean[] state) {
		// TODO Auto-generated constructor stub
		this(id, deviceID, name, AnotherName, type, -2, region, state);
	}

	public DevicesModel(int id, String deviceID, String name,
			String AnotherName, int type, float value, String region,
			boolean[] state) {
		setModelId(id);
		setDevicesID(deviceID);
		setDevicesName(name);
		setDevicesAnotherName(AnotherName);
		setDevicesType(type);
		setDevicesValue(value);
		setDevicesRegion(region);
		setDevicesState(state);
	}

	public DevicesModel() {
		// TODO Auto-generated constructor stub
	}

	public String getDevicesID() {
		return devicesID;
	}

	public void setDevicesID(String devicesID) {
		this.devicesID = devicesID;
	}

	public String getDevicesName() {
		return devicesName;
	}

	public void setDevicesName(String devicesName) {
		this.devicesName = devicesName;
	}

	public String getDevicesAnotherName() {
		return devicesAnotherName;
	}

	public void setDevicesAnotherName(String devicesAnotherName) {
		this.devicesAnotherName = devicesAnotherName;
	}

	public int getDevicesType() {
		return devicesType;
	}

	public void setDevicesType(int devicesType) {
		this.devicesType = devicesType;
	}

	public float getDevicesValue() {
		return devicesValue;
	}

	public void setDevicesValue(float devicesValue) {
		this.devicesValue = devicesValue;
	}

	public String getDevicesRegion() {
		return devicesRegion;
	}

	public void setDevicesRegion(String devicesRegion) {
		this.devicesRegion = devicesRegion;
	}

	public boolean[] getDevicesState() {
		return devicesState;
	}

	public void setDevicesState(boolean[] devicesState) {
		this.devicesState = devicesState;
	}

	public int getModelId() {
		return modelId;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder result = new StringBuilder();
		if (devicesState[0]) {
			result.append("开");
		} else {
			result.append("关");
		}
		if (devicesState.length > 1) {
			for (int m = 1; m < devicesState.length; m++) {
				if (devicesState[m]) {
					result.append(" 开");
				} else {
					result.append(" 关");
				}
			}
		}

		return result.toString();
	}

	public static final Parcelable.Creator<DevicesModel> CREATOR = new Creator<DevicesModel>() {
		public DevicesModel createFromParcel(Parcel source) {
			DevicesModel mDevicesModel = new DevicesModel();
			mDevicesModel.setDevicesAnotherName(source.readString());
			mDevicesModel.setDevicesID(source.readString());
			mDevicesModel.setDevicesName(source.readString());
			mDevicesModel.setDevicesRegion(source.readString());
			boolean[] mState = null;
			source.readBooleanArray(mState);
			mDevicesModel.setDevicesState(mState);
			mDevicesModel.setDevicesType(source.readInt());
			mDevicesModel.setDevicesValue(source.readFloat());
			mDevicesModel.setModelId(source.readInt());

			return mDevicesModel;
		}

		public DevicesModel[] newArray(int size) {
			return new DevicesModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(devicesAnotherName);
		parcel.writeString(devicesID);
		parcel.writeString(devicesName);
		parcel.writeString(devicesRegion);
		parcel.writeBooleanArray(devicesState);
		parcel.writeInt(devicesType);
		parcel.writeFloat(devicesValue);
		parcel.writeInt(modelId);
	}

}
