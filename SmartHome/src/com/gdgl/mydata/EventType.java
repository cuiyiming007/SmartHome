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
	public static final EventType  LIGHTSENSOROPERATION;
	public static final EventType IASWARNINGDEVICOPERATION;
	public static final EventType SHADEcONTROLLEROPERATION;
	public static final EventType SHADEOPERATION;
	public static final EventType IASZONEOPERATION;
	public static final EventType TEMPERATURESENSOROPERATION;
	public static final EventType RANGEEXTENDER;
	public static final EventType IASACE;
	public static final EventType REMOTECONTROL;
	public static final EventType MAINSOUTLETOPERATION;
	

	
	static
	{
		LOGIN=new EventType("login",id);
		ONOFFLIGHTOPERATION=new EventType("onofflightoperation", nextId());
		ONOFFLIGHTSWITCHOPERATION=new EventType("onofflightswitchoperation", nextId());
		ONOFFOUTPUTOPERATION=new EventType("onOffOutputOperation", nextId());
		ONOFFSWITCHOPERATION=new EventType("onOffSwitchOperation", nextId());
		INTITIALDVIVCEDATA=new EventType("initialDiviceData",nextId());
		LIGHTSENSOROPERATION=new EventType("lightSensorOperation",nextId());
		DOORLOCKOPERATION=new EventType("doorLockOperation", nextId());
		IASWARNINGDEVICOPERATION=new EventType("iasWarningDeviceOperation", nextId());
		SHADEcONTROLLEROPERATION=new EventType("shadeControllerOperation", nextId());

		SHADEOPERATION=new EventType("shadeOperation", nextId());
		IASZONEOPERATION=new EventType("iasZoneOperation", nextId());
		TEMPERATURESENSOROPERATION=new EventType("temperatureSensorOperation", nextId());
		RANGEEXTENDER=new EventType("RangeExtender", nextId());
		IASACE=new EventType("IASACE", nextId());
		REMOTECONTROL=new EventType("RemoteControl", nextId());
		MAINSOUTLETOPERATION=new EventType("MainsOutLetOperation", nextId());

	}
	
	private static int nextId()
	{
		return id++;
	}

}
