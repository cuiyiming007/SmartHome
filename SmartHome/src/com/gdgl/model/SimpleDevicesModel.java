package com.gdgl.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SimpleDevicesModel implements Parcelable{
	

	private int ID;
	private int mDeviceId;
	private String mIeee = "";
	private String mNWKAddr = "";
	private String mNodeENNAme = "";
	private String mModelId = "";
	private String mEP = "";
	private String mName = "";
	private String mOnOffStatus = "";
	private String mUserDefineName = "";

	// 自定义
	private String mDeviceRegion = "";
	private long mLastDateTime;
	private int mOnOffLine = DevicesModel.DEVICE_ON_LINE;
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getmDeviceId() {
		return mDeviceId;
	}

	public void setmDeviceId(int mDeviceId) {
		this.mDeviceId = mDeviceId;
	}

	public String getmIeee() {
		return mIeee;
	}

	public void setmIeee(String mIeee) {
		this.mIeee = mIeee;
	}

	public String getmNWKAddr() {
		return mNWKAddr;
	}

	public void setmNWKAddr(String mNWKAddr) {
		this.mNWKAddr = mNWKAddr;
	}

	public String getmNodeENNAme() {
		return mNodeENNAme;
	}

	public void setmNodeENNAme(String mNodeENNAme) {
		this.mNodeENNAme = mNodeENNAme;
	}

	public String getmModelId() {
		return mModelId;
	}

	public void setmModelId(String mModelId) {
		this.mModelId = mModelId;
	}

	public String getmEP() {
		return mEP;
	}

	public void setmEP(String mEP) {
		this.mEP = mEP;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmOnOffStatus() {
		return mOnOffStatus;
	}

	public void setmOnOffStatus(String mOnOffStatus) {
		this.mOnOffStatus = mOnOffStatus;
	}


	public String getmDeviceRegion() {
		return mDeviceRegion;
	}

	public void setmDeviceRegion(String mDeviceRegion) {
		this.mDeviceRegion = mDeviceRegion;
	}

	public long getmLastDateTime() {
		return mLastDateTime;
	}

	public void setmLastDateTime(long mLastDateTime) {
		this.mLastDateTime = mLastDateTime;
	}

	public int getmOnOffLine() {
		return mOnOffLine;
	}

	public void setmOnOffLine(int mOnOffLine) {
		this.mOnOffLine = mOnOffLine;
	}
	
	public String getmUserDefineName() {
		return mUserDefineName;
	}

	public void setmUserDefineName(String mUserDefineName) {
		this.mUserDefineName = mUserDefineName;
	}
	
	public SimpleDevicesModel(){}
	
	
	public static final Parcelable.Creator<SimpleDevicesModel> CREATOR = new Creator<SimpleDevicesModel>() {
		public SimpleDevicesModel createFromParcel(Parcel source) {
			SimpleDevicesModel mDevicesModel = new SimpleDevicesModel();

			mDevicesModel.setID(source.readInt());
			mDevicesModel.setmDeviceId(source.readInt());
			mDevicesModel.setmDeviceRegion(source.readString());
			mDevicesModel.setmEP(source.readString());
			mDevicesModel.setmIeee(source.readString());
			mDevicesModel.setmLastDateTime(source.readLong());
			mDevicesModel.setmName(source.readString());
			mDevicesModel.setmNodeENNAme(source.readString());
			mDevicesModel.setmNWKAddr(source.readString());
			mDevicesModel.setmOnOffLine(source.readInt());
			mDevicesModel.setmOnOffStatus(source.readString());
			mDevicesModel.setmModelId(source.readString());
			mDevicesModel.setmUserDefineName(source.readString());

			return mDevicesModel;
		}

		public SimpleDevicesModel[] newArray(int size) {
			return new SimpleDevicesModel[size];
		}
	};

	
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(ID);
		parcel.writeInt(mDeviceId);
		parcel.writeString(mDeviceRegion);
		parcel.writeString(mEP);
		parcel.writeString(mIeee);
		parcel.writeLong(mLastDateTime);
		parcel.writeString(mName);
		parcel.writeString(mNodeENNAme);
		parcel.writeString(mNWKAddr);
		parcel.writeInt(mOnOffLine);
		parcel.writeString(mOnOffStatus);
		parcel.writeString(mModelId);
		parcel.writeString(mUserDefineName);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
