package com.gdgl.mydata;

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
	public static final EventType SHADECONTROLLEROPERATION;
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
	public static final EventType ADDIPC;
	public static final EventType EDITIPC;
	public static final EventType DELETEIPC;
	public static final EventType GETBINDLIST;
	public static final EventType BEGINLEARNIR;
	public static final EventType BEGINAPPLYIR;
	public static final EventType DELETEIR;
	public static final EventType WARN;
	public static final EventType GETDEVICELEARNED;
	public static final EventType SCAPEDDEVICE;
	
	public static final EventType GETALLROOM;
	public static final EventType GETEPBYROOMINDEX;
	public static final EventType ROOMDATAMAIN;
	public static final EventType MODIFYDEVICEROOMID;
	

	
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
		SHADECONTROLLEROPERATION=new EventType("shadeControllerOperation", nextId());

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
		ADDIPC=new EventType("addIPC", nextId());
		EDITIPC=new EventType("editIPC", nextId());
		DELETEIPC=new EventType("deleteIPC", nextId());
		GETBINDLIST=new EventType("getBindList", nextId());
		BEGINLEARNIR=new EventType("beginLearnIR", nextId());
		BEGINAPPLYIR=new EventType("beginApplyIR", nextId());
		DELETEIR=new EventType("deleteIR", nextId());
		WARN=new EventType("warm", nextId());
		GETDEVICELEARNED=new EventType("GetDeviceLearned", nextId());
		SCAPEDDEVICE=new EventType("ScapedDevice", nextId());
		
		GETALLROOM=new EventType("getAllRoomInfo", nextId());
		GETEPBYROOMINDEX=new EventType("getEPByRoomIndex", nextId());
		ROOMDATAMAIN=new EventType("roomDataMain", nextId());
		MODIFYDEVICEROOMID=new EventType("modifyDeviceRoomId", nextId());
	}
	
	private static int nextId()
	{
		return id++;
	}

}
