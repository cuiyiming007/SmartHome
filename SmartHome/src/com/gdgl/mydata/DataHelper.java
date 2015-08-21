package com.gdgl.mydata;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.model.ContentValuesListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Callback.CallbackIpcLinkageMessage;
import com.gdgl.mydata.Callback.CallbackWarnMessage;
import com.gdgl.mydata.Region.GetRoomInfo_response;
import com.gdgl.mydata.Region.Room;
import com.gdgl.mydata.binding.BindingDataEntity;
import com.gdgl.mydata.scene.SceneDevice;
import com.gdgl.mydata.scene.SceneInfo;
import com.gdgl.mydata.timing.TimingAction;
import com.gdgl.mydata.video.VideoNode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper extends SQLiteOpenHelper {

	public static final int ON_OFF_SWITCH_DEVICETYPE = 0; // 开关（如“三键开关”、“双键开关”、门窗感应开关）
	public static final int ON_OFF_OUTPUT_DEVICETYPE = 2; // 无线智能阀门、开关模块（双路）
	public static final int REMOTE_CONTROL_DEVICETYPE = 6; // 多键遥控器
	public static final int COMBINED_INTERFACE_DEVICETYPE = 7; // 协调器(即一键布防)
	public static final int RANGE_EXTENDER_DEVICETYPE = 8; // 红外控制器
	public static final int MAINS_POWER_OUTLET_DEVICETYPE = 9; // 开关模块（单路）、中规电能检测墙面插座、电能检测插座
	public static final int ON_OFF_LIGHT_DEVICETYPE = 256; // 开关模块（四路）
	public static final int DIMEN_LIGHTS_DEVICETYPE = 257; // 吸顶电能检测调光模块
	public static final int DIMEN_SWITCH_DEVICETYPE = 260; // 调光开关(开关）
	public static final int LIGHT_SENSOR_DEVICETYPE = 262; // 光线感应器
	public static final int SHADE_DEVICETYPE = 512; // 幕帘控制开关
	public static final int TEMPTURE_SENSOR_DEVICETYPE = 770; // 室内型温湿度感应器
	public static final int IAS_ACE_DEVICETYPE = 1025; // 门铃按键、多键遥控器(不可控制)
	public static final int IAS_ZONE_DEVICETYPE = 1026; // 烟雾感应器、可燃气体探测器（煤气）、（天然气）、（一氧化碳）、窗磁、门窗感应开关、紧急按钮、ZigBee墙面紧急按钮、作感应器
	public static final int IAS_WARNNING_DEVICE_DEVICETYPE = 1027; // 警报器
	public static final int RF_DEVICE = -1; //RF设备

	//zigbee devices
	public static final String Motion_Sensor = "ZB11A"; // ZigBee动作感应器
	public static final String Magnetic_Window = "Z311A"; // ZigBee窗磁
	public static final String Emergency_Button = "Z308"; // ZigBee紧急按钮 （！改）
	public static final String Emergency_Button_On_Wall = "ZB02I"; // ZigBee墙面紧急按钮（！新加）
	public static final String Doors_and_windows_sensor_switch = "Z311J"; // 门窗感应开关（！改）
	public static final String Smoke_Detectors = "ZA01A"; // 烟雾感应器
	public static final String Combustible_Gas_Detector_Gas = "ZA01B"; // 可燃气体探测器（煤气）
	public static final String Combustible_Gas_Detector_CO = "ZA01C"; // 可燃气体探测器（一氧化碳）
	public static final String Combustible_Gas_Detector_Natural_gas = "ZA01D"; // 可燃气体探测器（天然气）
	public static final String Wireless_Intelligent_valve_switch = "ZA10"; // 无线智能阀门开关
	public static final String Siren = "Z602A"; // ZigBee警报器
	public static final String Wall_switch_touch = "ZB02A"; // ZigBee墙面开关（单键）
	public static final String Wall_switch_double = "ZB02B"; // ZigBee墙面开关（双键）
	public static final String Wall_switch_triple = "ZB02C"; // ZigBee墙面开关（三键）
	public static final String Dimmer_Switch = "ZB02F"; // ZigBee调光开关
	public static final String Power_detect_wall = "Z816H"; // 中规电能检测墙面插座
	public static final String Curtain_control_switch = "Z815N"; // ZigBee幕帘控制开关
	public static final String Infrared_controller = "Z211"; // ZigBee红外控制器
	public static final String Indoor_temperature_sensor = "Z711"; // ZigBee室内型温湿度感应器
	public static final String Light_Sensor = "Z311G"; // ZigBee光线感应器
	public static final String Light_Sensor_With_OnOff = "Z311B"; // 灯控光感器 （！新加）
	public static final String Light_Sensor_With_Dimmer = "Z311H"; // 调光器 （！新加）
	public static final String Multi_key_remote_control = "Z503"; // ZigBee多键遥控器
	public static final String Doorbell_button = "Z312"; // ZigBee门铃按键
	public static final String Switch_Module_Single = "Z805B"; // ZigBee开关模块（单路）
	public static final String Switch_Module_Double = "Z806"; // ZigBee开关模块（双路）（！新加）
	public static final String Switch_Module_Quadruple = "Z811"; // ZigBee开关模块（四路）（！新加）
	public static final String Energy_detection_dimming_module = "Z817B"; // 吸顶电能检测调光模块
	public static final String Pro_RF = "Z100BI"; // ZigBee Pro RF ģ��
	public static final String RS232_adapter = "ZL01A"; // ��ҵ��ZigBee RS232适配器
	public static final String Power_detect_socket = "Z809A"; // ZigBee电能检测插座
	public static final String One_key_operator = "Z103A"; // 安防中心

	//RF devices
	public static final String RF_Magnetic_Door = "RX87K"; // RF门磁
	public static final String RF_Magnetic_Door_Roll = "RX87LD"; // RF卷闸门磁
	public static final String RF_Emergency_Button = "RX89"; // RF紧急按钮
	public static final String RF_Infrared_Motion_Sensor = "RX80LF"; // RF红外探测器
	public static final String RF_Smoke_Detectors = "RX83L"; // RF烟雾感应器
	public static final String RF_Combustible_Gas_Detector = "RX85"; // 燃气探测器
	public static final String RF_Siren = "RX103W"; // RF警报器
	public static final String RF_Siren_Relay = "RX103WL"; // RF中继警报器
	public static final String RF_Siren_Outside = "RX103HA"; // RF室外警报器
	public static final String RF_remote_control = "RX88"; // RF遥控器
	
	public static final String DATABASE_NAME = "smarthome";
	public static final String DEVICES_TABLE = "devices";
	public static final String SCENE_TABLE = "scene_table";
	public static final String SCENE_DEVICES_TABLE = "scene_devices_table";
	public static final String VIDEO_TABLE = "video";
	public static final String MESSAGE_TABLE = "message_table";
	public static final String ROOMINFO_TABLE = "roominfo_table";
	public static final String BIND_TABLE = "bind_table";
	public static final String GATEWAY_TABLE = "gateway_table";
	public static final String LINKAGE_TABLE = "linkage_table";
	public static final String TIMINGACTION_TABLE = "timingaction_table";
	public static final String IPC_LINKAGE_TABLE = "ipc_linkage_table";

	public static final String RF_DEVICES_TABLE = "rf_devices_table";
	public static final int DATEBASE_VERSTION = 6;

	public StringBuilder deviceStringBuilder;
	public StringBuilder sceneStringBuilder;
	public StringBuilder sceneDevicesStringBuilder;
	public StringBuilder videoStringBuilder;
	public StringBuilder messageStringBuilder;
	public StringBuilder roominfoStringBuilder;
	public StringBuilder bindStringBuilder;
	public StringBuilder gatewayStringBuilder;
	public StringBuilder linkageStringBuilder;
	public StringBuilder timingactionStringBuilder;
	public StringBuilder ipc_linkageStringBuilder;
	public StringBuilder rfdeviceStringBuilder;

	// public SQLiteDatabase db;

	public DataHelper(Context contex) {
		super(contex, DATABASE_NAME, null, DATEBASE_VERSTION);
		deviceStringBuilder = new StringBuilder();
		sceneStringBuilder = new StringBuilder();
		sceneDevicesStringBuilder = new StringBuilder();
		videoStringBuilder = new StringBuilder();
		messageStringBuilder = new StringBuilder();
		roominfoStringBuilder = new StringBuilder();
		bindStringBuilder = new StringBuilder();
		gatewayStringBuilder = new StringBuilder();
		linkageStringBuilder = new StringBuilder();
		timingactionStringBuilder = new StringBuilder();
		ipc_linkageStringBuilder = new StringBuilder();
		rfdeviceStringBuilder = new StringBuilder();
		// db = getWritableDatabase();
		// TODO Auto-generated constructor stub
	}

	private void initStringBuilder() {
		// TODO Auto-generated method stub
		// device table create string
		deviceStringBuilder.append("CREATE TABLE " + DEVICES_TABLE + " (");
		deviceStringBuilder.append(DevicesModel._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		deviceStringBuilder.append(DevicesModel.ALL_COUNT + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.CURCOUNT + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.DEVICE_ID + " INTEGER,");
		deviceStringBuilder.append(DevicesModel.R_ID + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.PIC_NAME + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.PROFILE_ID + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.POWER_RESOURCE + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.CUR_POWER_RESOURCE
				+ " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.CURPOWERSOURCELEVEL
				+ " VARCHAR,");

		deviceStringBuilder.append(DevicesModel.IEEE + " VARCHAR(16),");
		deviceStringBuilder.append(DevicesModel.NWK_ADDR + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.NODE_EN_NAME + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.MANUFACTORY + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.ZCL_VERSTION + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.STACK_VERSTION + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.APP_VERSTION + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.HW_VERSTION + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.DATE_CODE + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.MODEL_ID + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.NODE_TYPE + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.NODE_STATUS + " INTEGER,");

		deviceStringBuilder.append(DevicesModel.EP + " VARCHAR(2),");
		deviceStringBuilder.append(DevicesModel.NAME + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.CURRENT + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.ENERGY + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.POWER + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.VOLTAGE + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.LEVEL + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.ON_OFF_STATUS + " VARCHAR(1),");
		deviceStringBuilder.append(DevicesModel.TEMPERATURE + " FLOAT,");
		deviceStringBuilder.append(DevicesModel.HUMIDITY + " FLOAT,");
		deviceStringBuilder.append(DevicesModel.BRIGHTNESS + " INTEGER,");
		deviceStringBuilder.append(DevicesModel.EP_MODEL_ID + " VARCHAR(6),");

		deviceStringBuilder.append(DevicesModel.CURRENT_MAX + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.CURRENT_MIN + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.VOLTAGE_MAX + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.VOLTAGE_MIN + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.ENERGY_MAX + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.ENERGY_MIN + " VARCHAR,");

		deviceStringBuilder.append(DevicesModel.CLUSTER_ID + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.DEVICE_SORT + " INTEGER,");
		deviceStringBuilder.append(DevicesModel.DEVICE_REGION + " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.DEFAULT_DEVICE_NAME
				+ " VARCHAR,");
		deviceStringBuilder.append(DevicesModel.DEVICE_PRIORITY + " INTEGER,");

		deviceStringBuilder.append(DevicesModel.LAST_UPDATE_TIME + " INTEGER,");
		deviceStringBuilder.append(DevicesModel.HEART_TIME + " INTEGER,");
		deviceStringBuilder.append(DevicesModel.ON_OFF_LINE + " INTEGER )");

		// scene table create string
		sceneStringBuilder.append("CREATE TABLE " + SCENE_TABLE + " (");
		sceneStringBuilder.append(SceneInfo._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		sceneStringBuilder.append(SceneInfo.SCENE_ID + " INTEGER,");
		sceneStringBuilder.append(SceneInfo.SCENE_NAME + " VARCHAR(48),");
		sceneStringBuilder.append(SceneInfo.SCENE_ACTIONS + " VARCHAR,");
		sceneStringBuilder.append(SceneInfo.SCENE_INDEX + " INTEGER )");

		// scene_devices table create string
		sceneDevicesStringBuilder.append("CREATE TABLE " + SCENE_DEVICES_TABLE
				+ " (");
		sceneDevicesStringBuilder.append(SceneDevice._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		sceneDevicesStringBuilder.append(SceneDevice.SCENE_ID + " INTEGER,");
		sceneDevicesStringBuilder.append(SceneDevice.ACTION_TYPE + " INTEGER,");
		sceneDevicesStringBuilder.append(SceneDevice.DEVICE_IEEE
				+ " VARCHAR(16),");
		sceneDevicesStringBuilder.append(SceneDevice.DEVICE_EP
				+ " VARCHAR(16),");
		sceneDevicesStringBuilder
				.append(SceneDevice.DEVICESTATS + " INTEGER )");

		// video table create string
		videoStringBuilder.append("CREATE TABLE " + VIDEO_TABLE + " (");
		videoStringBuilder.append(VideoNode._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		videoStringBuilder.append(VideoNode.ALIAS + " VARCHAR(16),");
		videoStringBuilder.append(VideoNode.ID + " VARCHAR(16),");
		videoStringBuilder.append(VideoNode.HTTPPORT + " VARCHAR(48),");
		videoStringBuilder.append(VideoNode.IPC_IPADDR + " INTEGER,");
		videoStringBuilder.append(VideoNode.NAME + " VARCHAR(16),");
		videoStringBuilder.append(VideoNode.PASSWORD + " INTEGER,");
		videoStringBuilder.append(VideoNode.RTSPORT + " INTEGER,");
		videoStringBuilder.append(VideoNode.IPC_INDEX + " INTEGER,");
		videoStringBuilder.append(VideoNode.ROOMID + " INTEGER,");
		videoStringBuilder.append(VideoNode.IPC_STATUS + " VARCHAR,");
		videoStringBuilder.append(VideoNode.DOMAIN_NAME + " VARCHAR,");
		videoStringBuilder.append(VideoNode.SERIAL_NUM + " VARCHAR)");

		// message table create string
		messageStringBuilder.append("CREATE TABLE " + MESSAGE_TABLE + " (");
		messageStringBuilder.append(CallbackWarnMessage._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		messageStringBuilder.append(CallbackWarnMessage.MSGTYPE + " INTEGER,");
		messageStringBuilder.append(CallbackWarnMessage.W_MODE
				+ " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarnMessage.W_DESCRIPTION
				+ " VARCHAR(64),");
		messageStringBuilder.append(CallbackWarnMessage.HOME_ID
				+ " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarnMessage.HOME_NAME
				+ " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarnMessage.HOUSEIEEE
				+ " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarnMessage.ROOMID
				+ " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarnMessage.TIME + " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarnMessage.ZONE_EP
				+ " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarnMessage.ZONE_IEEE
				+ " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarnMessage.ZONE_NAME
				+ " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarnMessage.CIE_EP
				+ " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarnMessage.CIE_IEEE
				+ " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarnMessage.CIE_NAME
				+ " VARCHAR(48),");
		messageStringBuilder.append(CallbackWarnMessage.DETAILMESSAGE
				+ " VARCHAR(64))");

		// roominfo table create string
		roominfoStringBuilder.append("CREATE TABLE " + ROOMINFO_TABLE + " (");
		roominfoStringBuilder.append(GetRoomInfo_response._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		roominfoStringBuilder
				.append(GetRoomInfo_response.ROOM_ID + " INTEGER,");
		roominfoStringBuilder.append(GetRoomInfo_response.ROOM_NAME + " TEXT,");
		roominfoStringBuilder.append(GetRoomInfo_response.ROOM_PIC
				+ " VARCHAR(48))");

		// bind table create string
		bindStringBuilder.append("CREATE TABLE " + BIND_TABLE + " (");
		bindStringBuilder.append(BindingDataEntity._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		bindStringBuilder.append(BindingDataEntity.DEVOUT_IEEE
				+ " VARCHAR(16),");
		bindStringBuilder.append(BindingDataEntity.DEVOUT_EP + " VARCHAR(2),");
		bindStringBuilder
				.append(BindingDataEntity.DEVIN_IEEE + " VARCHAR(16),");
		bindStringBuilder.append(BindingDataEntity.DEVIN_EP + " VARCHAR(2),");
		bindStringBuilder.append(BindingDataEntity.CLUSTER + " VARCHAR)");

		// gateway table create string
		gatewayStringBuilder.append("CREATE TABLE " + GATEWAY_TABLE + " (");
		gatewayStringBuilder.append("_id"
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		gatewayStringBuilder.append("mac" + " VARCHAR(14),");
		gatewayStringBuilder.append("alias" + " VARCHAR(16),");
		gatewayStringBuilder.append("ip" + " VARCHAR)");

		// linkage table create string
		linkageStringBuilder.append("CREATE TABLE " + LINKAGE_TABLE + " (");
		linkageStringBuilder.append("_id"
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		linkageStringBuilder.append(Linkage.LID + " INTEGER,");
		linkageStringBuilder.append(Linkage.LNKNAME + " VARCHAR,");
		linkageStringBuilder.append(Linkage.TRGIEEE + " VARCHAR(16),");
		linkageStringBuilder.append(Linkage.TRGEP + " VARCHAR(16),");
		linkageStringBuilder.append(Linkage.TRGCND + " VARCHAR(48),");
		linkageStringBuilder.append(Linkage.LNKACT + " VARCHAR(48),");
		linkageStringBuilder.append(Linkage.ENABLE + " INTEGER)");

		// timingaction table create string
		timingactionStringBuilder.append("CREATE TABLE " + TIMINGACTION_TABLE
				+ " (");
		timingactionStringBuilder.append("_id"
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		timingactionStringBuilder.append(TimingAction.TIMING_ID + " INTEGER,");
		timingactionStringBuilder
				.append(TimingAction.TIMING_NAME + " VARCHAR,");
		timingactionStringBuilder.append(TimingAction.TIMING_ACTPARA
				+ " VARCHAR,");
		timingactionStringBuilder.append(TimingAction.TIMING_ACTMODE
				+ " INTEGER,");
		timingactionStringBuilder.append(TimingAction.TIMING_PARA1
				+ " VARCHAR,");
		timingactionStringBuilder.append(TimingAction.TIMING_PARA2
				+ " INTEGER,");
		timingactionStringBuilder.append(TimingAction.TIMING_PARA3
				+ " VARCHAR,");
		timingactionStringBuilder.append(TimingAction.TIMING_ENABLE
				+ " INTEGER)");

		// ipc_linkage table create string
		ipc_linkageStringBuilder.append("CREATE TABLE " + IPC_LINKAGE_TABLE
				+ " (");
		ipc_linkageStringBuilder.append("_id"
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		ipc_linkageStringBuilder.append(CallbackIpcLinkageMessage.GATEWAYMAC
				+ " VARCHAR,");
		ipc_linkageStringBuilder.append(CallbackIpcLinkageMessage.TYPE + " INTEGER,");
		ipc_linkageStringBuilder.append(CallbackIpcLinkageMessage.DEVICE_IEEE
				+ " VARCHAR,");
		ipc_linkageStringBuilder.append(CallbackIpcLinkageMessage.DEVICE_NAME
				+ " VARCHAR,");
		ipc_linkageStringBuilder.append(CallbackIpcLinkageMessage.DEVICE_PIC
				+ " VARCHAR,");
		ipc_linkageStringBuilder
				.append(CallbackIpcLinkageMessage.IPC_ID + " INTEGER,");
		ipc_linkageStringBuilder.append(CallbackIpcLinkageMessage.IPC_NAME
				+ " VARCHAR,");
		ipc_linkageStringBuilder.append(CallbackIpcLinkageMessage.TIME + " VARCHAR,");
		ipc_linkageStringBuilder.append(CallbackIpcLinkageMessage.PICCOUNT
				+ " INTEGER,");
		ipc_linkageStringBuilder.append(CallbackIpcLinkageMessage.PICNAME
				+ " VARCHAR,");
		ipc_linkageStringBuilder.append(CallbackIpcLinkageMessage.DESCRIPTION
				+ " VARCHAR)");

		// rf_devices table create string
		rfdeviceStringBuilder.append("CREATE TABLE " + RF_DEVICES_TABLE + " (");
		rfdeviceStringBuilder.append(DevicesModel._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		rfdeviceStringBuilder.append(DevicesModel.ALL_COUNT + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.CURCOUNT + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.DEVICE_ID + " INTEGER,");
		rfdeviceStringBuilder.append(DevicesModel.R_ID + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.PIC_NAME + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.PROFILE_ID + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.POWER_RESOURCE + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.CUR_POWER_RESOURCE
				+ " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.CURPOWERSOURCELEVEL
				+ " VARCHAR,");

		rfdeviceStringBuilder.append(DevicesModel.IEEE + " VARCHAR(16),");
		rfdeviceStringBuilder.append(DevicesModel.NWK_ADDR + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.NODE_EN_NAME + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.MANUFACTORY + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.ZCL_VERSTION + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.STACK_VERSTION + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.APP_VERSTION + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.HW_VERSTION + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.DATE_CODE + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.MODEL_ID + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.NODE_TYPE + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.NODE_STATUS + " INTEGER,");

		rfdeviceStringBuilder.append(DevicesModel.EP + " VARCHAR(2),");
		rfdeviceStringBuilder.append(DevicesModel.NAME + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.CURRENT + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.ENERGY + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.POWER + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.VOLTAGE + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.LEVEL + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.ON_OFF_STATUS
				+ " VARCHAR(1),");
		rfdeviceStringBuilder.append(DevicesModel.TEMPERATURE + " FLOAT,");
		rfdeviceStringBuilder.append(DevicesModel.HUMIDITY + " FLOAT,");
		rfdeviceStringBuilder.append(DevicesModel.BRIGHTNESS + " INTEGER,");
		rfdeviceStringBuilder.append(DevicesModel.EP_MODEL_ID + " VARCHAR(6),");

		rfdeviceStringBuilder.append(DevicesModel.CURRENT_MAX + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.CURRENT_MIN + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.VOLTAGE_MAX + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.VOLTAGE_MIN + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.ENERGY_MAX + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.ENERGY_MIN + " VARCHAR,");

		rfdeviceStringBuilder.append(DevicesModel.CLUSTER_ID + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.DEVICE_SORT + " INTEGER,");
		rfdeviceStringBuilder.append(DevicesModel.DEVICE_REGION + " VARCHAR,");
		rfdeviceStringBuilder.append(DevicesModel.DEFAULT_DEVICE_NAME
				+ " VARCHAR,");
		rfdeviceStringBuilder
				.append(DevicesModel.DEVICE_PRIORITY + " INTEGER,");

		rfdeviceStringBuilder.append(DevicesModel.LAST_UPDATE_TIME
				+ " INTEGER,");
		rfdeviceStringBuilder.append(DevicesModel.HEART_TIME + " INTEGER,");
		rfdeviceStringBuilder.append(DevicesModel.ON_OFF_LINE + " INTEGER )");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		initStringBuilder();

		db.execSQL(deviceStringBuilder.toString());
		db.execSQL(sceneStringBuilder.toString());
		db.execSQL(sceneDevicesStringBuilder.toString());
		db.execSQL(videoStringBuilder.toString());
		db.execSQL(messageStringBuilder.toString());
		db.execSQL(roominfoStringBuilder.toString());
		db.execSQL(bindStringBuilder.toString());
		db.execSQL(gatewayStringBuilder.toString());
		db.execSQL(linkageStringBuilder.toString());
		db.execSQL(timingactionStringBuilder.toString());
		db.execSQL(ipc_linkageStringBuilder.toString());
		db.execSQL(rfdeviceStringBuilder.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + DEVICES_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SCENE_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SCENE_DEVICES_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + VIDEO_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + MESSAGE_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ROOMINFO_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + BIND_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + GATEWAY_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + LINKAGE_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + TIMINGACTION_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + IPC_LINKAGE_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + RF_DEVICES_TABLE);
		onCreate(db);
	}

	public static List<DevicesModel> convertToDevicesModel(
			ArrayList<ResponseParamsEndPoint> r) {
		List<DevicesModel> mList = new ArrayList<DevicesModel>();
		DevicesModel mDevicesModel = null;
		for (ResponseParamsEndPoint mResponseParamsEndPoint : r) {
			if (null != mResponseParamsEndPoint) {
				mDevicesModel = new DevicesModel(mResponseParamsEndPoint);
			}
			mList.add(mDevicesModel);
		}
		return mList;
	}

	public long insertDevice(SQLiteDatabase db, String table,
			String nullColumnHack, DevicesModel values) {
		long result = 0;
		try {
			result = db.insert(table, nullColumnHack,
					values.convertContentValues());
			// db.close();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.close();
		}
		return result;

	}

	public long insertDevList(SQLiteDatabase db, String table,
			String nullColumnHack, List<DevicesModel> values) {
		long result = 0;
		try {
			for (DevicesModel devicesModel : values) {
				result = db.insert(table, nullColumnHack,
						devicesModel.convertContentValues());
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.close();
		}
		return result;

	}

	public long insertEndPointList(SQLiteDatabase db, String table,
			String nullColumnHack, ArrayList<ResponseParamsEndPoint> r) {

		long result = -100;
		List<DevicesModel> mList = convertToDevicesModel(r);
		db.beginTransaction();
		try {
			for (DevicesModel devicesModel : mList) {
				if (isFilterDevice(devicesModel)) {
					continue;
				}
				ContentValues c = devicesModel.convertContentValues();
				long m = db.insert(table, nullColumnHack, c);
				if (-1 == m) {
					result = m;
				}

			}
			db.setTransactionSuccessful();
			// db.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}

	private boolean isFilterDevice(DevicesModel devicesModel) {
		boolean ret = false;
		// 多键遥控器不显示显示
		if (devicesModel.getmModelId().indexOf(Multi_key_remote_control) == 0) {
			ret = true;
		}
		return ret;
	}

	/***
	 * insert a list which implement the ContentValuesListener
	 * 
	 * @param db
	 * @param table
	 * @param nullColumnHack
	 * @param arrayList
	 * @return
	 */
	public long insertVideoList(SQLiteDatabase db, String table,
			String nullColumnHack, ArrayList<VideoNode> arrayList) {

		long result = -100;
		// List<DevicesModel> mList = convertToDevicesModel(r);
		db.beginTransaction();
		try {
			for (ContentValuesListener contentvalue : arrayList) {
				ContentValues c = contentvalue.convertContentValues();
				long m = db.insert(table, nullColumnHack, c);
				if (-1 == m) {
					result = m;
				}

			}
			db.setTransactionSuccessful();
			// db.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}

	public long insertMessageList(SQLiteDatabase db, String table,
			String nullColumnHack, ArrayList<CallbackWarnMessage> arrayList) {
		long result = -100;
		db.beginTransaction();
		try {
			for (ContentValuesListener contentvalue : arrayList) {
				ContentValues c = contentvalue.convertContentValues();
				long m = db.insert(table, nullColumnHack, c);
				if (-1 != m) {
					result = m;
				}

			}
			db.setTransactionSuccessful();
			// db.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}

	public void emptyTable(SQLiteDatabase db, String table) {
		// try {
		db.execSQL("delete from " + table + " where 1=1");
		// db.close();
		// } catch (Exception e) {
		// // TODO: handle exception
		// }finally{
		// db.close();
		// }

	}

	public long insertAddRoomInfo(SQLiteDatabase db, String table,
			String nullColumnHack, ArrayList<Room> r) {

		long result = -100;
		db.beginTransaction();
		try {
			for (Room roomifo : r) {
				ContentValues c = roomifo.convertContentValues();
				long m = db.insert(table, nullColumnHack, c);
				if (-1 == m) {
					result = m;
				}
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}

	public long insertRoomInfoList(SQLiteDatabase db, String table,
			String nullColumnHack, ArrayList<GetRoomInfo_response> r) {

		long result = -100;
		db.beginTransaction();
		try {
			for (GetRoomInfo_response roomifo : r) {
				ContentValues c = roomifo.convertContentValues();
				long m = db.insert(table, nullColumnHack, c);
				if (-1 == m) {
					result = m;
				}

			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}

	public long insertLinkage(SQLiteDatabase db, String table,
			String nullColumnHack, Linkage values) {
		long result = 0;
		try {
			result = db.insert(table, nullColumnHack,
					values.convertContentValues());
			// db.close();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.close();
		}
		return result;

	}

	public long insertLinkageList(SQLiteDatabase db, String table,
			String nullColumnHack, List<Linkage> values) {
		long result = 0;
		try {
			for (Linkage linkage : values) {
				result = db.insert(table, nullColumnHack,
						linkage.convertContentValues());
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.close();
		}
		return result;

	}

	public int delete(SQLiteDatabase db, String table, String whereClause,
			String[] whereArgs) {
		return db.delete(table, whereClause, whereArgs);
	}

	public int update(SQLiteDatabase db, String table, ContentValues values,
			String whereClause, String[] whereArgs) {
		int result = db.update(table, values, whereClause, whereArgs);
		// db.close();
		return result;
	}

	public void execSQL(SQLiteDatabase db, String sql) {
		db.execSQL(sql);
	}

	public Cursor query(SQLiteDatabase db, String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		Cursor c = null;
		try {
			c = db.query(table, columns, selection, selectionArgs, groupBy,
					having, orderBy, limit);
			// db.close();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			// db.close();
		}
		return c;
	}

	public static List<VideoNode> getVideoList(Context c, DataHelper dh) {
		List<VideoNode> mList = new ArrayList<VideoNode>();
		Cursor cursor = null;
		SQLiteDatabase db = dh.getSQLiteDatabase();
		cursor = db.query(DataHelper.VIDEO_TABLE, null, null, null, null, null,
				VideoNode.ID, null);
		VideoNode mVideoNode;
		while (cursor.moveToNext()) {
			mVideoNode = new VideoNode();
			mVideoNode.setAliases(cursor.getString(cursor
					.getColumnIndex(VideoNode.ALIAS)));
			mVideoNode.setHttpport(cursor.getString(cursor
					.getColumnIndex(VideoNode.HTTPPORT)));
			mVideoNode.setId(cursor.getString(cursor
					.getColumnIndex(VideoNode.ID)));
			mVideoNode.setIpc_ipaddr(cursor.getString(cursor
					.getColumnIndex(VideoNode.IPC_IPADDR)));
			mVideoNode.setName(cursor.getString(cursor
					.getColumnIndex(VideoNode.NAME)));
			mVideoNode.setPassword(cursor.getString(cursor
					.getColumnIndex(VideoNode.PASSWORD)));
			mVideoNode.setRtspport(cursor.getString(cursor
					.getColumnIndex(VideoNode.RTSPORT)));
			mVideoNode.setIndex(cursor.getInt(cursor
					.getColumnIndex(VideoNode.IPC_INDEX)));
			mVideoNode.setRoomId(cursor.getInt(cursor
					.getColumnIndex(VideoNode.ROOMID)));
			mVideoNode.setIpc_status(cursor.getString(cursor
					.getColumnIndex(VideoNode.IPC_STATUS)));
			mVideoNode.setDomainName(cursor.getString(cursor
					.getColumnIndex(VideoNode.DOMAIN_NAME)));
			mVideoNode.setSerialNum(cursor.getString(cursor
					.getColumnIndex(VideoNode.SERIAL_NUM)));
			mList.add(mVideoNode);
		}
		cursor.close();
		// db.close();
		return mList;

	}

	public static List<Linkage> queryForLinkageList(SQLiteDatabase db,
			String table, String selection, String[] selectionArgs) {
		List<Linkage> mList = new ArrayList<Linkage>();
		Linkage linkage = null;
		Cursor c = db.query(table, null, selection, selectionArgs, null, null,
				null, null);
		while (c.moveToNext()) {
			linkage = new Linkage();
			linkage.setLid(c.getInt(c.getColumnIndex(Linkage.LID)));
			linkage.setLnkname(c.getString(c.getColumnIndex(Linkage.LNKNAME)));
			linkage.setTrgieee(c.getString(c.getColumnIndex(Linkage.TRGIEEE)));
			linkage.setTrgep(c.getString(c.getColumnIndex(Linkage.TRGEP)));
			linkage.setTrgcnd(c.getString(c.getColumnIndex(Linkage.TRGCND)));
			linkage.setLnkact(c.getString(c.getColumnIndex(Linkage.LNKACT)));
			linkage.setEnable(c.getInt(c.getColumnIndex(Linkage.ENABLE)));
			mList.add(linkage);
		}
		c.close();
		// db.close();
		return mList;

	}

	public List<DevicesModel> queryForBindDevicesList(SQLiteDatabase db,
			String table, String selection, String[] selectionArgs) {

		List<DevicesModel> mList = new ArrayList<DevicesModel>();
		DevicesModel mDevicesModel = null;
		Cursor c = db.query(table, null, selection, selectionArgs, null, null,
				null, null);
		while (c.moveToNext()) {
			mDevicesModel = new DevicesModel();
			mDevicesModel.setmIeee(c.getString(c
					.getColumnIndex(BindingDataEntity.DEVIN_IEEE)));
			mDevicesModel.setmEP(c.getString(c
					.getColumnIndex(BindingDataEntity.DEVIN_EP)));

			mList.add(mDevicesModel);
		}
		c.close();
		// db.close();
		return mList;
	}

	public List<DevicesModel> queryForDevicesList(SQLiteDatabase db,
			String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {

		List<DevicesModel> mList = new ArrayList<DevicesModel>();
		DevicesModel mDevicesModel = null;
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy, limit);
		while (c.moveToNext()) {
			mDevicesModel = new DevicesModel();
			mDevicesModel.setmAllCount(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel.setmCurCount(c.getString(c
					.getColumnIndex(DevicesModel.CURCOUNT)));
			mDevicesModel.setmDeviceId(c.getInt(c
					.getColumnIndex(DevicesModel.DEVICE_ID)));
			mDevicesModel.setmRid(c.getString(c
					.getColumnIndex(DevicesModel.R_ID)));
			mDevicesModel.setmPicName(c.getString(c
					.getColumnIndex(DevicesModel.PIC_NAME)));
			mDevicesModel.setmProfileId(c.getString(c
					.getColumnIndex(DevicesModel.PROFILE_ID)));
			mDevicesModel.setmPowerResource(c.getString(c
					.getColumnIndex(DevicesModel.POWER_RESOURCE)));
			mDevicesModel.setmCurPowerResource(c.getString(c
					.getColumnIndex(DevicesModel.CUR_POWER_RESOURCE)));
			mDevicesModel.setCurpowersourcelevel(c.getString(c
					.getColumnIndex(DevicesModel.CURPOWERSOURCELEVEL)));
			mDevicesModel.setmIeee(c.getString(c
					.getColumnIndex(DevicesModel.IEEE)));
			mDevicesModel.setmNWKAddr(c.getString(c
					.getColumnIndex(DevicesModel.NWK_ADDR)));
			mDevicesModel.setmNodeENNAme(c.getString(c
					.getColumnIndex(DevicesModel.NODE_EN_NAME)));
			mDevicesModel.setmManufactory(c.getString(c
					.getColumnIndex(DevicesModel.MANUFACTORY)));
			mDevicesModel.setmZCLVersion(c.getString(c
					.getColumnIndex(DevicesModel.ZCL_VERSTION)));
			mDevicesModel.setmStackVerstion(c.getString(c
					.getColumnIndex(DevicesModel.STACK_VERSTION)));
			mDevicesModel.setmAppVersion(c.getString(c
					.getColumnIndex(DevicesModel.APP_VERSTION)));
			mDevicesModel.setmHwVersion(c.getString(c
					.getColumnIndex(DevicesModel.HW_VERSTION)));
			mDevicesModel.setmDateCode(c.getString(c
					.getColumnIndex(DevicesModel.DATE_CODE)));
			mDevicesModel.setmModelId(c.getString(c
					.getColumnIndex(DevicesModel.MODEL_ID)));
			mDevicesModel.setmNodeType(c.getString(c
					.getColumnIndex(DevicesModel.NODE_TYPE)));
			mDevicesModel.setmStatus(c.getInt(c
					.getColumnIndex(DevicesModel.NODE_STATUS)));
			mDevicesModel
					.setmEP(c.getString(c.getColumnIndex(DevicesModel.EP)));
			mDevicesModel.setmName(c.getString(c
					.getColumnIndex(DevicesModel.NAME)));
			mDevicesModel.setmCurrent(c.getString(c
					.getColumnIndex(DevicesModel.CURRENT)));
			mDevicesModel.setmEnergy(c.getString(c
					.getColumnIndex(DevicesModel.ENERGY)));
			mDevicesModel.setmPower(c.getString(c
					.getColumnIndex(DevicesModel.POWER)));
			mDevicesModel.setmVoltage(c.getString(c
					.getColumnIndex(DevicesModel.VOLTAGE)));
			mDevicesModel.setmLevel(c.getString(c
					.getColumnIndex(DevicesModel.LEVEL)));
			mDevicesModel.setmOnOffStatus(c.getString(c
					.getColumnIndex(DevicesModel.ON_OFF_STATUS)));
			mDevicesModel.setmTemperature(c.getFloat(c
					.getColumnIndex(DevicesModel.TEMPERATURE)));
			mDevicesModel.setmHumidity(c.getFloat(c
					.getColumnIndex(DevicesModel.HUMIDITY)));
			mDevicesModel.setmBrightness(c.getInt(c
					.getColumnIndex(DevicesModel.BRIGHTNESS)));
			mDevicesModel.setmEPModelId(c.getString(c
					.getColumnIndex(DevicesModel.EP_MODEL_ID)));
			mDevicesModel.setmCurrentMax(c.getString(c
					.getColumnIndex(DevicesModel.CURRENT_MAX)));
			mDevicesModel.setmCurrentMin(c.getString(c
					.getColumnIndex(DevicesModel.CURRENT_MIN)));
			mDevicesModel.setmVoltageMax(c.getString(c
					.getColumnIndex(DevicesModel.VOLTAGE_MAX)));
			mDevicesModel.setmVoltageMin(c.getString(c
					.getColumnIndex(DevicesModel.VOLTAGE_MIN)));
			mDevicesModel.setmEnergyMax(c.getString(c
					.getColumnIndex(DevicesModel.ENERGY_MAX)));
			mDevicesModel.setmEnergyMin(c.getString(c
					.getColumnIndex(DevicesModel.ENERGY_MIN)));
			mDevicesModel.setmClusterID(c.getString(c
					.getColumnIndex(DevicesModel.CLUSTER_ID)));

			mDevicesModel.setmDeviceRegion(c.getString(c
					.getColumnIndex(DevicesModel.DEVICE_REGION)));
			mDevicesModel.setmLastDateTime(c.getLong(c
					.getColumnIndex(DevicesModel.LAST_UPDATE_TIME)));
			mDevicesModel.setmOnOffLine(c.getShort(c
					.getColumnIndex(DevicesModel.ON_OFF_LINE)));
			mDevicesModel.setID(c.getInt(c.getColumnIndex(DevicesModel._ID)));
			mDevicesModel.setmDefaultDeviceName(c.getString(c
					.getColumnIndex(DevicesModel.DEFAULT_DEVICE_NAME)));
			mDevicesModel.setmHeartTime(c.getInt(c
					.getColumnIndex(DevicesModel.HEART_TIME)));

			mList.add(mDevicesModel);
		}
		c.close();
		// db.close();
		return mList;
	}

	public List<SceneInfo> queryForSceneInfoList(SQLiteDatabase db,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) {
		List<SceneInfo> mList = new ArrayList<SceneInfo>();
		SceneInfo mSceneInfo = null;
		Cursor c = db.query(DataHelper.SCENE_TABLE, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		while (c.moveToNext()) {

			mSceneInfo = new SceneInfo();
			mSceneInfo.setSid(c.getInt(c.getColumnIndex(SceneInfo.SCENE_ID)));
			mSceneInfo.setScnname(c.getString(c
					.getColumnIndex(SceneInfo.SCENE_NAME)));
			mSceneInfo.setScnaction(c.getString(c
					.getColumnIndex(SceneInfo.SCENE_ACTIONS)));
			mSceneInfo.setScnindex(c.getInt(c
					.getColumnIndex(SceneInfo.SCENE_INDEX)));
			mList.add(mSceneInfo);
		}
		c.close();
		db.close();
		return mList;
	}

	public List<SceneDevice> queryForSceneDevicesList(SQLiteDatabase db,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) {
		List<SceneDevice> mList = new ArrayList<SceneDevice>();
		SceneDevice mSceneDevice = null;
		Cursor c = db.query(DataHelper.SCENE_DEVICES_TABLE, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		while (c.moveToNext()) {

			mSceneDevice = new SceneDevice();
			mSceneDevice
					.setSid(c.getInt(c.getColumnIndex(SceneDevice.SCENE_ID)));
			mSceneDevice.setActionType(c.getInt(c
					.getColumnIndex(SceneDevice.ACTION_TYPE)));
			mSceneDevice.setIeee(c.getString(c
					.getColumnIndex(SceneDevice.DEVICE_IEEE)));
			mSceneDevice.setEp(c.getString(c
					.getColumnIndex(SceneDevice.DEVICE_EP)));
			mSceneDevice.setDevicesStatus(c.getInt(c
					.getColumnIndex(SceneDevice.DEVICESTATS)));
			mList.add(mSceneDevice);
		}
		c.close();
		db.close();
		return mList;
	}

	public List<TimingAction> queryForTimingActionList(SQLiteDatabase db,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) {
		List<TimingAction> mList = new ArrayList<TimingAction>();
		TimingAction mTimingAction = null;
		Cursor c = db.query(DataHelper.TIMINGACTION_TABLE, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		while (c.moveToNext()) {

			mTimingAction = new TimingAction();
			mTimingAction.setTid(c.getInt(c
					.getColumnIndex(TimingAction.TIMING_ID)));
			mTimingAction.setTimingname(c.getString(c
					.getColumnIndex(TimingAction.TIMING_NAME)));
			mTimingAction.setActpara(c.getString(c
					.getColumnIndex(TimingAction.TIMING_ACTPARA)));
			mTimingAction.setActmode(c.getInt(c
					.getColumnIndex(TimingAction.TIMING_ACTMODE)));
			mTimingAction.setPara1(c.getString(c
					.getColumnIndex(TimingAction.TIMING_PARA1)));
			mTimingAction.setPara2(c.getInt(c
					.getColumnIndex(TimingAction.TIMING_PARA2)));
			mTimingAction.setPara3(c.getString(c
					.getColumnIndex(TimingAction.TIMING_PARA3)));
			mTimingAction.setTimingEnable(c.getInt(c
					.getColumnIndex(TimingAction.TIMING_ENABLE)));
			mList.add(mTimingAction);
		}
		c.close();
		db.close();
		return mList;
	}

	public List<Room> queryForRoomList(SQLiteDatabase db, String table,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) {
		List<Room> mList = new ArrayList<Room>();
		Room roominfo = null;
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy, limit);
		if (c.moveToFirst()) {
			do {
				roominfo = new Room();
				roominfo.setroom_id(c.getInt(c
						.getColumnIndex(GetRoomInfo_response.ROOM_ID)));
				roominfo.setroom_name(c.getString(c
						.getColumnIndex(GetRoomInfo_response.ROOM_NAME)));
				roominfo.setroom_pic(c.getString(c
						.getColumnIndex(GetRoomInfo_response.ROOM_PIC)));

				mList.add(roominfo);
			} while (c.moveToNext());
			c.close();
		}
		db.close();
		return mList;
	}

	public void close(SQLiteDatabase db) {
		db.close();
	}

	public SQLiteDatabase getSQLiteDatabase() {
		return getWritableDatabase();
	}
}
