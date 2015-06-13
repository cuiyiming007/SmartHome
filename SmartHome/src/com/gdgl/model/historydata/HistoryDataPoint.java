package com.gdgl.model.historydata;

import java.util.List;

public class HistoryDataPoint {
	private String attrName;
	private List<HistoryPoint> point;
	
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrname) {
		this.attrName = attrname;
	}
	
	public List<HistoryPoint> getPoint() {
		return point;
	}
	public void setPoint(List<HistoryPoint> point) {
		this.point = point;
	}
}
