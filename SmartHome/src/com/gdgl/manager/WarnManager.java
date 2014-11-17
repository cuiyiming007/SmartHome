package com.gdgl.manager;

import android.database.sqlite.SQLiteDatabase;

import com.gdgl.app.ApplicationController;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Callback.CallbackWarnMessage;

public class WarnManager {
	private static WarnManager instance;
	private boolean isWarnning;
	private CallbackWarnMessage currentWarmmessage;
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
	public void setCurrentWarnInfo(CallbackWarnMessage currentWarmmessage)
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
	public boolean isDeviceWarning(DevicesModel model)
	{
		if (isWarnning&&currentWarmmessage.getCie_ieee().equals(model.getmIeee())) {
			return true;
		}else
			return false;
	}
	
	public CallbackWarnMessage  setWarnDetailMessage(CallbackWarnMessage message) {
		String detailmessage;
	DataHelper	dh = new DataHelper(ApplicationController.getInstance());
		SQLiteDatabase db = dh.getSQLiteDatabase();
		DevicesModel device= DataUtil.getDeviceModelByIeee(message.getZone_ieee(), dh, db);
		if (message.getW_description().equals("Doorbell")) {
			detailmessage = "门铃响了";
		}else{
			String chineseName=DataUtil.getDefaultDevicesName(ApplicationController.getInstance(), device.getmModelId(), device.getmEP());
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

	public CallbackWarnMessage getCurrentWarmmessage() {
		return currentWarmmessage;
	}

	public void setCurrentWarmmessage(CallbackWarnMessage currentWarmmessage) {
		this.currentWarmmessage = currentWarmmessage;
	}
	

}
