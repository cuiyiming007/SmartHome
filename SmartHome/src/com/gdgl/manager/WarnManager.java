package com.gdgl.manager;

import android.R.integer;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gdgl.app.ApplicationController;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Callback.CallbackWarmMessage;
import com.gdgl.mydata.getlocalcielist.elserec;

public class WarnManager {
	private static WarnManager instance;
	private boolean isWarnning;
	private CallbackWarmMessage currentWarmmessage;
	private int messageNum=0;

	public static WarnManager getInstance() {
		if (instance == null) {
			instance = new WarnManager();
		}
		return instance;
	}
	WarnManager()
	{
		inialWarn();
	}
	public void inialWarn()
	{
		isWarnning=false;
		currentWarmmessage=null;
		messageNum=0;
	}
	public void setCurrentWarnInfo(CallbackWarmMessage currentWarmmessage)
	{
		messageNum++;
		isWarnning=true;
		setCurrentWarmmessage(currentWarmmessage);
		
	}
	public boolean isDeviceWarning(SimpleDevicesModel model)
	{
		if (isWarnning&&currentWarmmessage.getCie_ieee().equals(model.getmIeee())) {
			return true;
		}else
			return false;
	}
	
	
	public CallbackWarmMessage  setWarnDetailMessage(CallbackWarmMessage message) {
		String detailmessage;
	DataHelper	dh = new DataHelper(ApplicationController.getInstance());
		SQLiteDatabase db = dh.getSQLiteDatabase();
		DevicesModel device= DataUtil.getDeviceModelByIeee(message.getZone_ieee(), dh, db);
		if (message.getW_description().equals("Doorbell")) {
			detailmessage = "门铃响了";
		}else{
			String chineseName=DataUtil.getDefaultUserDefinname(ApplicationController.getInstance(), device.getmModelId());
			message.setW_description(chineseName);
			detailmessage = message.getW_description() + "收到报警信息，请注意！";
		}
		message.setDetailmessage(detailmessage);
		return message;
	}
	public int getMessageNum()
	{
		return messageNum;
	}
	public void intilMessageNum()
	{
		messageNum=0;
	}
	
	public boolean isWarnning() {
		return isWarnning;
	}

	public void setWarnning(boolean isWarnning) {
		this.isWarnning = isWarnning;
	}

	public CallbackWarmMessage getCurrentWarmmessage() {
		return currentWarmmessage;
	}

	public void setCurrentWarmmessage(CallbackWarmMessage currentWarmmessage) {
		this.currentWarmmessage = currentWarmmessage;
	}
	

}
