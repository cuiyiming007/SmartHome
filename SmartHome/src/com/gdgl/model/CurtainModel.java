package com.gdgl.model;

public class CurtainModel {

	private int mId;
	private String mName;
	private String mRegion;
	private boolean isOnOrOff;
	private double mLightLevel = 0.0;

	public CurtainModel(int id, String name, String region, boolean onoff) {
		// TODO Auto-generated constructor stub
		this(id, name, region, onoff, 0.0);
	}

	public CurtainModel(int id, String name, String region, boolean onoff,
			double lightLevel) {
		// TODO Auto-generated constructor stub
		mId = id;
		mName = name;
		mRegion = region;
		isOnOrOff = onoff;
		mLightLevel = lightLevel;
	}

	public void setID(int id) {
		if (id > 0) {
			mId = id;
		}

	}

	public void setName(String name) {
		if (null != name) {
			mName = name;
		}

	}

	public void setRegion(String region) {
		if (null != region) {
			mRegion = region;
		}

	}

	public void setOnOrOff(boolean b) {
		if (isOnOrOff ^ b) {
			isOnOrOff = b;
		}
	}

	public void setLevel(double level) {
		mLightLevel = level;
	}

	public int getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	public String getRegion() {
		return mRegion;
	}

	public boolean getState() {
		return isOnOrOff;
	}

	public double getLevel() {
		return mLightLevel;
	}
}
