package com.gdgl.manager;

import android.R.integer;

import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.Callback.CallbackWarmMessage;

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
	
	
	public void setWarnDetailMessage(CallbackWarmMessage message) {
		String detailmessage = message.getW_description() + "收到报警信息，请注意！";
		if (message.getW_description().equals("Doorbell")) {
			detailmessage = "门铃响了";
		}
		message.setDetailmessage(detailmessage);
	}
	public int getMessageNum()
	{
		return messageNum;
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
