package com.gdgl.mydata;

public class EventType {

	private static int id = 0;
	private static String name;

	public EventType(String name, int id) {
		this.id = id;
		this.name = name;
	}

	public static final EventType LOGIN;
	public static final EventType LIBJINGLE_STATUS;
	public static final EventType MODIFYPASSWORD;
	public static final EventType MODIFYALIAS;
	public static final EventType INITGATEWAY;
	public static final EventType ONOFFOUTPUTOPERATION;

	public static final EventType DOORLOCKOPERATION;
	public static final EventType INTITIALDVIVCEDATA;
	public static final EventType LIGHTSENSOROPERATION;
	public static final EventType IASWARNINGDEVICOPERATION;
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
	public static final EventType MOVE_TO_LEVEL;
	public static final EventType CURRENT;
	public static final EventType VOLTAGE;
	public static final EventType POWER;
	public static final EventType ENERGY;
	public static final EventType HUMIDITY;
	public static final EventType GETICELIST;
	public static final EventType BINDDEVICE;
	public static final EventType UNBINDDEVICE;
	
	public static final EventType ADDIPC;
	public static final EventType EDITIPC;
	public static final EventType DELETEIPC;
	public static final EventType IPCONLINESTATUS;
	
	public static final EventType GETBINDLIST;
	public static final EventType BEGINLEARNIR;
	public static final EventType BEGINAPPLYIR;
	public static final EventType DELETEIR;
	public static final EventType WARN;
	public static final EventType IPC_LINKAGE_MSG;
	public static final EventType GETDEVICELEARNED;
	public static final EventType SCAPEDDEVICE;
	public static final EventType POWER_SOURCE;

	public static final EventType GETALLROOM;
	public static final EventType GETEPBYROOMINDEX;
	public static final EventType GETEPBYROOMINDEXINIT;
	public static final EventType ROOMDATAMAIN;
	public static final EventType MODIFYDEVICEROOMID;
	
	public static final EventType ADDTIMINGACTION;
	public static final EventType EDITTIMINGACTION;
	public static final EventType DELTIMINGACTION;
	public static final EventType ENABLETIMINGACTION;
	
	public static final EventType ADDSCENE;
	public static final EventType EDITSCENE;
	public static final EventType DELSCENE;
	public static final EventType DOSCENE;
	
	public static final EventType CHANGEDEVICENAME;
	public static final EventType HEARTTIME;
	public static final EventType REQUESTVIDEO;
	
	public static final EventType GETHISTORYDATA;
	public static final EventType FEEDBACKTOSERVER;

	public static final EventType ADDLINKAGE;
	public static final EventType EDITLINKAGE;
	public static final EventType DELETELINKAGE;
	public static final EventType ENABLELINKAGE;
	
	public static final EventType NETWORKCHANGE;
	public static final EventType GATEWAYUPDATEBEGINE;
	public static final EventType GATEWAYUPDATECOMPLETE;
	public static final EventType GATEWAYAUTH;
	
	//RF EVENT
	public static final EventType RF_DEVICE_STATUS;
	public static final EventType RF_DEVICE_BYPASS;
	public static final EventType RF_DEVICE_ALL_BYPASS;
	public static final EventType RF_CHANGEDEVICENAME;
	public static final EventType RF_DEVICE_ENABLE;
	public static final EventType RF_DEVICE_LIST_UPDATE;
	public static final EventType RF_DEVICE_ONLINE_STATUS;
	public static final EventType RF_GETEPBYROOMINDEX;
	public static final EventType RF_GETEPBYROOMINDEXINIT;
	
	public static final EventType CHANGEEMAILADDRESS;
	public static final EventType GETEMAILADDRESS;
	
	static {
		LOGIN = new EventType("login", id);
		LIBJINGLE_STATUS = new EventType("libjingle_status", nextId());
		MODIFYPASSWORD = new EventType("ModifyPassword", nextId());
		MODIFYALIAS = new EventType("modifyAlias", nextId());
		INITGATEWAY = new EventType("InitGateway", nextId());
		ONOFFOUTPUTOPERATION = new EventType("onOffOutputOperation", nextId());
		INTITIALDVIVCEDATA = new EventType("initialDiviceData", nextId());
		GETICELIST = new EventType("getICEList", nextId());
		LIGHTSENSOROPERATION = new EventType("lightSensorOperation", nextId());
		DOORLOCKOPERATION = new EventType("doorLockOperation", nextId());
		IASWARNINGDEVICOPERATION = new EventType("iasWarningDeviceOperation",
				nextId());
		SHADEOPERATION = new EventType("shadeOperation", nextId());
		IASZONEOPERATION = new EventType("iasZoneOperation", nextId());
		TEMPERATURESENSOROPERATION = new EventType(
				"temperatureSensorOperation", nextId());
		HUMIDITY = new EventType("humidity", nextId());
		RANGEEXTENDER = new EventType("RangeExtender", nextId());
		IASACE = new EventType("IASACE", nextId());
		REMOTECONTROL = new EventType("RemoteControl", nextId());
		MAINSOUTLETOPERATION = new EventType("MainsOutLetOperation", nextId());
		LOCALIASCIEBYPASSZONE = new EventType("localIASCIEByPassZone", nextId());
		LOCALIASCIEUNBYPASSZONE = new EventType("localIASCIEByPassZone",
				nextId());
		LOCALIASCIEOPERATION = new EventType("LocalIASCIEOperation", nextId());
		GETVIDEOLIST = new EventType("getvideolist", nextId());
		DELETENODE = new EventType("deleteNode", nextId());
		SETPERMITJOINON = new EventType("setPermitJoinOn", nextId());
		ENROLL = new EventType("enroll", nextId());
		ON_OFF_STATUS = new EventType("on_off_status", nextId());
		MOVE_TO_LEVEL = new EventType("move_to_level", nextId());
		CURRENT = new EventType("current", nextId());
		VOLTAGE = new EventType("voltage", nextId());
		POWER = new EventType("power", nextId());
		ENERGY = new EventType("energy", nextId());
		BINDDEVICE = new EventType("bindDevice", nextId());
		UNBINDDEVICE = new EventType("unbindDevice", nextId());
		
		ADDIPC = new EventType("addIPC", nextId());
		EDITIPC = new EventType("editIPC", nextId());
		DELETEIPC = new EventType("deleteIPC", nextId());
		IPCONLINESTATUS = new EventType("ipcOnlineStatus", nextId());
		
		GETBINDLIST = new EventType("getBindList", nextId());
		BEGINLEARNIR = new EventType("beginLearnIR", nextId());
		BEGINAPPLYIR = new EventType("beginApplyIR", nextId());
		DELETEIR = new EventType("deleteIR", nextId());
		WARN = new EventType("warm", nextId());
		IPC_LINKAGE_MSG = new EventType("ipc_linkage_msg", nextId());
		GETDEVICELEARNED = new EventType("GetDeviceLearned", nextId());
		SCAPEDDEVICE = new EventType("ScapedDevice", nextId());
		POWER_SOURCE = new EventType("PowerSource", nextId());

		GETALLROOM = new EventType("getAllRoomInfo", nextId());
		GETEPBYROOMINDEX = new EventType("getEPByRoomIndex", nextId());
		GETEPBYROOMINDEXINIT = new EventType("getEPByRoomIndexInit", nextId());
		ROOMDATAMAIN = new EventType("roomDataMain", nextId());
		MODIFYDEVICEROOMID = new EventType("modifyDeviceRoomId", nextId());
		
		ADDTIMINGACTION = new EventType("AddTimeAction", nextId());
		EDITTIMINGACTION = new EventType("EditTimeAction", nextId());
		DELTIMINGACTION = new EventType("DelTimeAction", nextId());
		ENABLETIMINGACTION = new EventType("EnableTimeAction", nextId());
		
		ADDSCENE = new EventType("AddScene", nextId());
		EDITSCENE = new EventType("EditScene", nextId());
		DELSCENE = new EventType("DelScene", nextId());
		DOSCENE = new EventType("DoScene", nextId());
		
		CHANGEDEVICENAME = new EventType("changedevicename", nextId());
		HEARTTIME = new EventType("hearttime", nextId());
		REQUESTVIDEO = new EventType("requestvideo", nextId());
		
		ADDLINKAGE = new EventType("AddLinkage", nextId());
		EDITLINKAGE = new EventType("EditLinkage", nextId());
		DELETELINKAGE = new EventType("DeleteLinkage", nextId());
		ENABLELINKAGE = new EventType("EnableLinkage", nextId());
		
		GETHISTORYDATA = new EventType("gethistorydata", nextId());
		FEEDBACKTOSERVER = new EventType("feedbacktoserver", nextId());
		
		NETWORKCHANGE = new EventType("networkchange", nextId());
		GATEWAYUPDATEBEGINE = new EventType("gatewayUpdateBegine", nextId());
		GATEWAYUPDATECOMPLETE = new EventType("gatewayUpdateComplete", nextId());
		GATEWAYAUTH = new EventType("gatewayAuth", nextId());
		
		RF_DEVICE_STATUS = new EventType("rfDeviceStatus", nextId());
		RF_DEVICE_BYPASS = new EventType("rfDeviceBypass", nextId());
		RF_DEVICE_ALL_BYPASS = new EventType("rfDeviceAllBypass", nextId());
		RF_CHANGEDEVICENAME = new EventType("rfChangeDeviceName", nextId());
		RF_DEVICE_ENABLE = new EventType("rfDeviceEnable", nextId());
		RF_DEVICE_LIST_UPDATE = new EventType("rfDeviceListUpdate", nextId());
		RF_DEVICE_ONLINE_STATUS = new EventType("rfDeviceOnlineStatus", nextId());
		RF_GETEPBYROOMINDEX = new EventType("rfGetEpByRoomIndex", nextId());
		RF_GETEPBYROOMINDEXINIT = new EventType("rfGetEpByRoomIndexInit", nextId());
		
		CHANGEEMAILADDRESS = new EventType("changeEmailAddress", nextId());
		GETEMAILADDRESS = new EventType("getEmailAddress", nextId());
	}

	private static int nextId() {
		return id++;
	}

}
