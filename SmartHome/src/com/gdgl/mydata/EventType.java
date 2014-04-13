package com.gdgl.mydata;

import android.R.integer;


public class EventType {
	
	private static int id=0;
	private static String name;
	
	public EventType(String name, int id) {
		this.id=id;
		this.name=name;
	}
   
	public static final  EventType LOGIN;
	public static final EventType ONOFFLIGHTOPERATION;
	public static final  EventType ONOFFLIGHTSWITCHOPERATION;
	public static final EventType ONOFFOUTPUTOPERATION;
	public static final EventType ONOFFSWITCHOPERATION;
	
	public static final EventType DOORLOCKOPERATION;
	public static final EventType  INTITIALDVIVCEDATA;
	
	
	static
	{
		LOGIN=new EventType("login",id);
		ONOFFLIGHTOPERATION=new EventType("onofflightoperation", nextId());
		ONOFFLIGHTSWITCHOPERATION=new EventType("onofflightswitchoperation", nextId());
		ONOFFOUTPUTOPERATION=new EventType("onOffOutputOperation", nextId());
		ONOFFSWITCHOPERATION=new EventType("onOffSwitchOperation", nextId());
		INTITIALDVIVCEDATA=new EventType("initialDiviceData",nextId());
		DOORLOCKOPERATION =new EventType("doorLockOperation", nextId());
	}
	
	private static int nextId()
	{
		return id++;
	}

}
