package com.gdgl.model.historydata;

import java.util.List;

public class HistoryData {
	private String deviceID;
	private String deviceEp;
	private List<HistoryDataPoint> datapoints;
	
	public String getDeviceIeee() {
		return deviceID;
	}
	public void setDeviceIeee(String deviceID) {
		this.deviceID = deviceID;
	}
	
	public String getDeviceEp() {
		return deviceEp;
	}
	public void setDeviceEp(String deviceEp) {
		this.deviceEp = deviceEp;
	}
	
	public List<HistoryDataPoint> getDataPoint() {
		return datapoints;
	}
	public void setDataPoint(List<HistoryDataPoint> datapoints) {
		this.datapoints = datapoints;
	}
}
