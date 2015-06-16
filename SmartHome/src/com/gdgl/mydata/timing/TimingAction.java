package com.gdgl.mydata.timing;

import java.io.Serializable;

import android.content.ContentValues;
import android.provider.BaseColumns;

interface TimingActionColumns extends BaseColumns {
	public static final String TIMING_ID = "tid";
	public static final String TIMING_NAME = "actname";
	public static final String TIMING_ACTPARA = "actpara";
	public static final String TIMING_ACTMODE = "actmode";
	public static final String TIMING_PARA1 = "para1";
	public static final String TIMING_PARA2 = "para2";
	public static final String TIMING_PARA3 = "para3";
	public static final String TIMING_ENABLE = "enable";
}

public class TimingAction implements TimingActionColumns, Serializable {
	/**
	 * serialVersionUID作用： 序列化时为了保持版本的兼容性，即在版本升级时反序列化仍保持对象的唯一性。
	 */
	private static final long serialVersionUID = 1916335173244841402L;
	private int tid;
	private String actname;
	private String actpara;
	private int actmode;
	private String para1;
	private int para2;
	private String para3;
	private int enable;

	private int actionType;
	private String ieee;
	private String ep;
	private int devicesStatus;

	private boolean[] para3Status = new boolean[7];

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public String getTimingname() {
		return actname;
	}

	public void setTimingname(String actname) {
		this.actname = actname;
	}

	public String getActpara() {
		return actpara;
	}

	public void setActpara(String actpara) {
		this.actpara = actpara;
		String[] params = actpara.split(":");
		actionType = Integer.parseInt(params[0]);
		String[] subparams = params[1].split("-");
		ieee = subparams[0];
		ep = subparams[1];
		devicesStatus = Integer.parseInt(subparams[2]);
	}

	public int getActmode() {
		return actmode;
	}

	public void setActmode(int actmode) {
		this.actmode = actmode;
	}

	public String getPara1() {
		return para1;
	}

	public void setPara1(String para1) {
		this.para1 = para1;
	}

	public int getPara2() {
		return para2;
	}

	public void setPara2(int para2) {
		this.para2 = para2;
	}

	public String getPara3() {
		return para3;
	}

	public void setPara3(String para3) {
		this.para3 = para3;
		if (para3.length() == 7) {
			para3Status = string2Boolean();
		}
	}

	public int getTimingEnable() {
		return enable;
	}

	public void setTimingEnable(int enable) {
		this.enable = enable;
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

	public boolean[] getPara3Status() {
		return para3Status;
	}

	public void setPara3Status(int i, boolean b) {
		para3Status[i] = b;
	}

	public ContentValues convertContentValues() {
		ContentValues mContentValues = new ContentValues();

		mContentValues.put(TIMING_ID, tid);
		mContentValues.put(TIMING_NAME, actname);
		mContentValues.put(TIMING_ACTPARA, actpara);
		mContentValues.put(TIMING_ACTMODE, actmode);
		mContentValues.put(TIMING_PARA1, para1);
		mContentValues.put(TIMING_PARA2, para2);
		mContentValues.put(TIMING_PARA3, para3);
		mContentValues.put(TIMING_ENABLE, enable);

		return mContentValues;
	}

	public boolean[] string2Boolean() {
		for (int i = 0; i < para3.length(); i++) {
			para3Status[i] = para3.charAt(i) == 49 ? true : false;
		}
		return para3Status;
	}

	public String boolean2String() {
		String temp = "";
		for (boolean b : para3Status) {
			temp = temp + String.valueOf(b ? 1 : 0);
		}
		return temp;
	}
	
	public String combine2Actpara() {
		return actionType + ":" + ieee + "-" + ep + "-" + devicesStatus;
	}
}
