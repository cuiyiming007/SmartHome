package com.gdgl.model;

import com.videogo.openapi.bean.resp.CameraInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceDiscoverInfo implements Parcelable{
	 public String deviceName;
	    public String deviceID;
	    public boolean isWifiConnected = false;
	    public boolean isPlatConnected = false;
	    public String localIP;
	    public int localPort = 8000;
	    public CameraInfo cameraInfo = null;
	    public boolean isAdded = false;
	    
	    public DeviceDiscoverInfo() {
	    }
	    
	    /**
	     * Parcel构造函数
	     * 
	     * @param in
	     */
	    protected DeviceDiscoverInfo(Parcel in) {
	        deviceName = in.readString();
	        deviceID = in.readString();
	        isWifiConnected = in.readInt()==1?true:false;
	        isPlatConnected = in.readInt()==1?true:false;
	        localIP = in.readString();
	        localPort = in.readInt();
	        cameraInfo = (CameraInfo)in.readValue(CameraInfo.class.getClassLoader());
	    }
	    
	    @Override
	    public int describeContents() {
	        return 0;
	    }

	    @Override
	    public void writeToParcel(Parcel dest, int flags) {
	        dest.writeString(deviceName);
	        dest.writeString(deviceID);
	        dest.writeInt(isWifiConnected?1:0);
	        dest.writeInt(isPlatConnected?1:0);
	        dest.writeString(localIP);
	        dest.writeInt(localPort);
	        dest.writeValue(cameraInfo);
	    }

	    public static final Parcelable.Creator<DeviceDiscoverInfo> CREATOR = new Parcelable.Creator<DeviceDiscoverInfo>() {
	        @Override
	        public DeviceDiscoverInfo createFromParcel(Parcel in) {
	            return new DeviceDiscoverInfo(in);
	        }

	        @Override
	        public DeviceDiscoverInfo[] newArray(int size) {
	            return new DeviceDiscoverInfo[size];
	        }
	    };
}
