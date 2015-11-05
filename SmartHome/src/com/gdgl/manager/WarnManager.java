package com.gdgl.manager;

import com.gdgl.model.DevicesModel;
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
	public boolean isDeviceWarning(DevicesModel model)
	{
		if (isWarnning&&currentWarmmessage.getZone_ieee().equals(model.getmIeee())) {
			return true;
		}else
			return false;
	}
	
	public CallbackWarnMessage  setWarnDetailMessage(CallbackWarnMessage message) {
		String detailmessage;
		detailmessage = "报警信息:type"+message.getW_mode() +" " +message.getW_description();
		message.setDetailmessage(detailmessage);
		return message;
	}
	
	public CallbackWarnMessage  setWarnDetailMessageNoSecurity(CallbackWarnMessage message) {
		String detailmessage;
		detailmessage = "提示信息:type"+message.getW_mode() +" " +message.getW_description();
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
