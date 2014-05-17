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
	public static final  EventType MODIFYPASSWORD;
	public static final  EventType	MODIFYALIAS;
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
	public static final EventType LOCALIASCIEBYPASSZONE;
	public static final EventType LOCALIASCIEOPERATION;
	public static final EventType LOCALIASCIEUNBYPASSZONE;
	public static final EventType GETVIDEOLIST;
	public static final EventType DELETENODE;
	public static final EventType SETPERMITJOINON;
	public static final EventType ENROLL;
	public static final EventType ON_OFF_STATUS;
	public static final EventType HUMIDITY;
	public static final EventType GETICELIST;
	public static final EventType BINDDEVICE;
	public static final EventType UNBINDDEVICE;
	

	
	static
	{
		LOGIN=new EventType("login",id);
		MODIFYPASSWORD =new EventType("ModifyPassword", nextId());
	    MODIFYALIAS=new EventType("modifyAlias", nextId());
		ONOFFLIGHTOPERATION=new EventType("onofflightoperation", nextId());
		ONOFFLIGHTSWITCHOPERATION=new EventType("onofflightswitchoperation", nextId());
		ONOFFOUTPUTOPERATION=new EventType("onOffOutputOperation", nextId());
		ONOFFSWITCHOPERATION=new EventType("onOffSwitchOperation", nextId());
		INTITIALDVIVCEDATA=new EventType("initialDiviceData",nextId());
		GETICELIST=new EventType("getICEList",nextId());
		LIGHTSENSOROPERATION=new EventType("lightSensorOperation",nextId());
		DOORLOCKOPERATION=new EventType("doorLockOperation", nextId());
		IASWARNINGDEVICOPERATION=new EventType("iasWarningDeviceOperation", nextId());
		SHADEcONTROLLEROPERATION=new EventType("shadeControllerOperation", nextId());

		SHADEOPERATION=new EventType("shadeOperation", nextId());
		IASZONEOPERATION=new EventType("iasZoneOperation", nextId());
		TEMPERATURESENSOROPERATION=new EventType("temperatureSensorOperation", nextId());
		HUMIDITY=new EventType("humidity", nextId());
		RANGEEXTENDER=new EventType("RangeExtender", nextId());
		IASACE=new EventType("IASACE", nextId());
		REMOTECONTROL=new EventType("RemoteControl", nextId());
		MAINSOUTLETOPERATION=new EventType("MainsOutLetOperation", nextId());
		LOCALIASCIEBYPASSZONE=new EventType("localIASCIEByPassZone", nextId());
		LOCALIASCIEUNBYPASSZONE=new EventType("localIASCIEByPassZone", nextId());
		LOCALIASCIEOPERATION=new EventType("LocalIASCIEOperation", nextId());
		GETVIDEOLIST=new EventType("getvideolist", nextId());
		DELETENODE=new EventType("deleteNode",nextId());
		SETPERMITJOINON=new EventType("setPermitJoinOn", nextId());
		ENROLL=new EventType("enroll", nextId());
		ON_OFF_STATUS=new EventType("on_off_status", nextId());
		BINDDEVICE=new EventType("bindDevice", nextId());
		UNBINDDEVICE=new EventType("unbindDevice", nextId());
	}
	
	private static int nextId()
	{
		return id++;
	}

}
