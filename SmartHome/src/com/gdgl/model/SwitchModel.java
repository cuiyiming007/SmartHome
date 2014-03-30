package com.gdgl.model;

public class SwitchModel {
	private int mId;
	private String mName;
	private String mRegion;
	private boolean[] isOnOrOff;
	
	public static final int SINGLE_SWITCH=1;
	public static final int DOUBLE_SWITCH=2;
	public static final int TRIPLE_SWITCH=3;

	public SwitchModel(int id, String name, String region, boolean[] bool ) {
		// TODO Auto-generated constructor stub
		mId = id;
		mName = name;
		mRegion = region;
		isOnOrOff = bool;
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

	public void setOnOrOff(boolean[] b) {
		
			isOnOrOff = b;
	
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

	public boolean[] getState() {
		return isOnOrOff;
	}
	
	public String getSwitchesState()
	{	
		StringBuilder SwitchesState = new StringBuilder();
		for (boolean b : isOnOrOff) {
			if(b)
			{
				SwitchesState.append("1");
			}
			else
			{
				SwitchesState.append("0");
			}
		}
		return SwitchesState.toString();
	}
	
	public int getSwitchType()
	{
		return isOnOrOff.length;
	}
}
