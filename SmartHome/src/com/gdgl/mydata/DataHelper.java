package com.gdgl.mydata;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper {

	public static final int ON_OFF_SWITCH_DEVICETYPE = 0;
	public static final int ON_OFF_OUTPUT_DEVICETYPE = 2;
	public static final int REMOTE_CONTROL_DEVICETYPE = 6;
	public static final int COMBINED_INTERFACE_DEVICETYPE = 7;
	public static final int RANGE_EXTENDER_DEVICETYPE = 8;
	public static final int MAINS_POWER_OUTLET_DEVICETYPE = 9;
	public static final int DIMEN_LIGHTS_DEVICETYPE = 257;
	public static final int DIMEN_SWITCH_DEVICETYPE = 260;
	public static final int LIGHT_SENSOR_DEVICETYPE = 262;
	public static final int SHADE_DEVICETYPE = 512;
	public static final int IAS_ACE_DEVICETYPE = 1025;
	public static final int IAS_ZONE_DEVICETYPE = 1026;
	public static final int IAS_WARNNING_DEVICE_DEVICETYPE = 1027;

	public static final String Motion_Sensor = "ZB11A"; // ZigBee动作感应器
	public static final String Magnetic_Window = "Z311A"; // ZigBee窗磁
	public static final String Emergency_Button = "Z302D"; // ZigBee紧急按钮
	public static final String Doors_and_windows_sensor_switch = "Z311J"; // 门窗感应开关
	public static final String Smoke_Detectors = "ZA01A"; // 烟雾感应器
	public static final String Combustible_Gas_Detector_Gas = "ZA01B"; // 可燃气体探测器（煤气)
	public static final String Combustible_Gas_Detector_CO = "ZA01C"; // 可燃气体探测器（一氧化碳)
	public static final String Combustible_Gas_Detector_Natural_gas = "ZA01D"; // 可燃气体探测器（天然气)
	public static final String Wireless_Intelligent_valve_switch = "ZA10"; // 无线智能阀门开关
	public static final String Siren = "Z602A"; // ZigBee警报器
	public static final String Wall_switch_touch = "ZB02A"; // ZigBee墙面开关（单键）
	public static final String Wall_switch_double = "ZB02B"; // 墙面开关（双键)
	public static final String Wall_switch_triple = "ZB02C"; // ZigBee动墙面开关（三键）
	public static final String Dimmer_Switch = "ZB02F"; // ZigBee调光开关
	public static final String Power_detect_wall = "Z816H"; // 中规电能检测墙面插座
	public static final String Curtain_control_switch = "Z815N"; // ZigBee幕帘控制开关
	public static final String Infrared_controller = "Z211"; // ZigBee红外控制器
	public static final String Indoor_temperature_sensor = "Z711"; // ZigBee室内型温湿度感应器
	public static final String Light_Sensor = "Z311G"; // ZigBee光线感应器
	public static final String Multi_key_remote_control = "Z503"; // ZigBee多键遥控器
	public static final String Doorbell_button = "Z312"; // ZigBee门铃按键
	public static final String Switch_Module_Single = "Z805B"; // ZigBee开关模块（单路）
	public static final String Energy_detection_dimming_module = "Z817B"; // 吸顶电能检测调光模块
	public static final String Pro_RF = "Z100BI"; // ZigBee Pro RF 模块
	public static final String RS232_adapter = "ZL01A"; // 工业级ZigBee RS232适配器
	public static final String Power_detect_socket = "Z809A"; // ZigBee 电能检测插座

	public static final String DATABASE_NAME = "smarthome";
	public static final String DEVICES_TABLE = "devices";

	public static final int DATEBASE_VERSTION = 1;

	public StringBuilder mStringBuilder;

	// public SQLiteDatabase db;

	public DataHelper(Context contex) {
		super(contex, DATABASE_NAME, null, DATEBASE_VERSTION);
		mStringBuilder = new StringBuilder();
		// db = getWritableDatabase();
		// TODO Auto-generated constructor stub
	}

	private void initStringBuilder() {
		// TODO Auto-generated method stub
		mStringBuilder.append("CREATE TABLE " + DEVICES_TABLE + " (");
		mStringBuilder.append(DevicesModel._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		mStringBuilder.append(DevicesModel.IEEE + " VARCHAR(16),");
		mStringBuilder.append(DevicesModel.EP + " VARCHAR(2),");
		mStringBuilder.append(DevicesModel.NAME + " VARCHAR,");
		mStringBuilder.append(DevicesModel.NODE_EN_NAME + " VARCHAR,");
		mStringBuilder.append(DevicesModel.MODEL_ID + " VARCHAR,");
		mStringBuilder.append(DevicesModel.DEVICE_ID + " VARCHAR(4),");
		mStringBuilder.append(DevicesModel.DEVICE_REGION + " VARCHAR,");
		mStringBuilder.append(DevicesModel.ALL_COUNT + " VARCHAR,");
		mStringBuilder.append(DevicesModel.APP_VERSTION + " VARCHAR,");
		mStringBuilder.append(DevicesModel.CUR_POWER_RESOURCE + " VARCHAR,");
		mStringBuilder.append(DevicesModel.CURCOUNT + " VARCHAR,");
		mStringBuilder.append(DevicesModel.CURRENT + " VARCHAR,");
		mStringBuilder.append(DevicesModel.CURRENT_MAX + " VARCHAR,");
		mStringBuilder.append(DevicesModel.CURRENT_MIN + " VARCHAR,");
		mStringBuilder.append(DevicesModel.DATE_CODE + " VARCHAR,");
		
		mStringBuilder.append(DevicesModel.ENERGY + " VARCHAR,");
		mStringBuilder.append(DevicesModel.ENERGY_MAX + " VARCHAR,");
		mStringBuilder.append(DevicesModel.ENERGY_MIN + " VARCHAR,");
		
		mStringBuilder.append(DevicesModel.EP_MODEL_ID + " VARCHAR(6),");
		mStringBuilder.append(DevicesModel.HW_VERSTION + " VARCHAR,");
		
		mStringBuilder.append(DevicesModel.LAST_UPDATE_TIME + " INTEGER,");
		mStringBuilder.append(DevicesModel.MANUFACTORY + " VARCHAR,");
		
		
		mStringBuilder.append(DevicesModel.NODE_TYPE + " VARCHAR,");
		mStringBuilder.append(DevicesModel.NWK_ADDR + " VARCHAR,");
		mStringBuilder.append(DevicesModel.ON_OFF_LINE + " INTEGER,");
		mStringBuilder.append(DevicesModel.ON_OFF_STATUS + " VARCHAR(1),");
		mStringBuilder.append(DevicesModel.PIC_NAME + " VARCHAR,");
		mStringBuilder.append(DevicesModel.POWER + " VARCHAR,");
		mStringBuilder.append(DevicesModel.POWER_RESOURCE + " VARCHAR,");
		mStringBuilder.append(DevicesModel.PROFILE_ID + " VARCHAR,");
		mStringBuilder.append(DevicesModel.R_ID + " VARCHAR,");
		mStringBuilder.append(DevicesModel.STACK_VERSTION + " VARCHAR,");
		mStringBuilder.append(DevicesModel.VOLTAGE + " VARCHAR,");
		mStringBuilder.append(DevicesModel.VOLTAGE_MAX + " VARCHAR,");
		mStringBuilder.append(DevicesModel.VOLTAGE_MIN + " VARCHAR,");
		mStringBuilder.append(DevicesModel.ZCL_VERSTION + " VARCHAR )");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		initStringBuilder();
		Log.i(DEVICES_TABLE, "zgs-> " + mStringBuilder.toString());

		db.execSQL(mStringBuilder.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + DEVICES_TABLE);
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

	public long insert(SQLiteDatabase db, String table, String nullColumnHack,
			DevicesModel values) {

		return db.insert(table, nullColumnHack, values.convertContentValues());

	}

	public long insertList(SQLiteDatabase db, String table,
			String nullColumnHack, ArrayList<ResponseParamsEndPoint> r) {

		long result = -100;
		List<DevicesModel> mList = convertToDevicesModel(r);
		db.beginTransaction();
		try {
			for (DevicesModel devicesModel : mList) {
				ContentValues c = devicesModel.convertContentValues();
				long m = db.insert(table, nullColumnHack, c);
				if (-1 == m) {
					result = m;
				}

			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		return result;
	}

	public int delete(SQLiteDatabase db, String table, String whereClause,
			String[] whereArgs) {
		ContentValues c = new ContentValues();
		c.put(DevicesModel.ON_OFF_LINE, DevicesModel.DEVICE_OFF_LINE);
		return db.update(table, c, whereClause, whereArgs);
	}

	public int update(SQLiteDatabase db, String table, ContentValues values,
			String whereClause, String[] whereArgs) {
		return db.update(table, values, whereClause, whereArgs);
	}

	public void execSQL(SQLiteDatabase db, String sql) {
		db.execSQL(sql);
	}

	public Cursor query(SQLiteDatabase db, String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy, String limit) {
		return db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy, limit);
	}

	public List<DevicesModel> queryForList(SQLiteDatabase db, String table,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) {

		List<DevicesModel> mList = new ArrayList<DevicesModel>();
		DevicesModel mDevicesModel = null;
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy, limit);
		while (c.moveToNext()) {
			mDevicesModel = new DevicesModel();
			mDevicesModel.setmAllCount(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel.setmAppVersion(c.getString(c
					.getColumnIndex(DevicesModel.APP_VERSTION)));
			mDevicesModel.setmCurCount(c.getString(c
					.getColumnIndex(DevicesModel.CUR_POWER_RESOURCE)));
			mDevicesModel.setmCurPowerResource(c.getString(c
					.getColumnIndex(DevicesModel.CURCOUNT)));
			mDevicesModel.setmCurrent(c.getString(c
					.getColumnIndex(DevicesModel.CURRENT)));
			mDevicesModel.setmCurrentMax(c.getString(c
					.getColumnIndex(DevicesModel.CURRENT_MAX)));
			mDevicesModel.setmCurrentMin(c.getString(c
					.getColumnIndex(DevicesModel.CURRENT_MIN)));
			mDevicesModel.setmDateCode(c.getString(c
					.getColumnIndex(DevicesModel.DATE_CODE)));
			mDevicesModel.setmDeviceId(c.getString(c
					.getColumnIndex(DevicesModel.DEVICE_ID)));
			mDevicesModel.setmEnergy(c.getString(c
					.getColumnIndex(DevicesModel.ENERGY)));
			mDevicesModel.setmEnergyMax(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel.setmEnergyMin(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel
					.setmEP(c.getString(c.getColumnIndex(DevicesModel.EP)));
			mDevicesModel.setmEPModelId(c.getString(c
					.getColumnIndex(DevicesModel.EP_MODEL_ID)));
			mDevicesModel.setmHwVersion(c.getString(c
					.getColumnIndex(DevicesModel.HW_VERSTION)));
			mDevicesModel.setmIeee(c.getString(c
					.getColumnIndex(DevicesModel.IEEE)));
			mDevicesModel.setmManufactory(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel.setmModelId(c.getString(c
					.getColumnIndex(DevicesModel.MODEL_ID)));
			mDevicesModel.setmName(c.getString(c
					.getColumnIndex(DevicesModel.NAME)));
			mDevicesModel.setmNodeENNAme(c.getString(c
					.getColumnIndex(DevicesModel.NODE_EN_NAME)));
			mDevicesModel.setmNodeType(c.getString(c
					.getColumnIndex(DevicesModel.NODE_TYPE)));
			mDevicesModel.setmNWKAddr(c.getString(c
					.getColumnIndex(DevicesModel.NWK_ADDR)));
			mDevicesModel.setmOnOffStatus(c.getString(c
					.getColumnIndex(DevicesModel.ON_OFF_STATUS)));
			mDevicesModel.setmPicName(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel.setmPower(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel.setmPowerResource(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel.setmProfileId(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel.setmRid(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel.setmStackVerstion(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel.setmVoltage(c.getString(c
					.getColumnIndex(DevicesModel.VOLTAGE_MAX)));
			mDevicesModel.setmVoltageMax(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel.setmVoltageMin(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel.setmZCLVersion(c.getString(c
					.getColumnIndex(DevicesModel.ALL_COUNT)));
			mDevicesModel.setmDeviceRegion(c.getString(c
					.getColumnIndex(DevicesModel.DEVICE_REGION)));
			mDevicesModel.setmLastDateTime(c.getLong(c
					.getColumnIndex(DevicesModel.LAST_UPDATE_TIME)));
			mDevicesModel.setmOnOffLine(c.getShort(c
					.getColumnIndex(DevicesModel.ON_OFF_LINE)));
			mDevicesModel.setID(c.getInt(c.getColumnIndex(DevicesModel._ID)));
			mList.add(mDevicesModel);
		}
		c.close();
		return mList;
	}

	public List<SimpleDevicesModel> rawQuery(SQLiteDatabase db, String sql,
			String[] selectionArgs) {
		List<SimpleDevicesModel> mList = new ArrayList<SimpleDevicesModel>();
		SimpleDevicesModel mSimpleDevicesModel = null;
		Cursor c = db.rawQuery(sql, selectionArgs);
		while (c.moveToNext()) {

			mSimpleDevicesModel = new SimpleDevicesModel();
			mSimpleDevicesModel.setID(c.getInt(c
					.getColumnIndex(DevicesModel._ID)));
			mSimpleDevicesModel.setmDeviceId(Integer.parseInt(c.getString(c
					.getColumnIndex(DevicesModel.DEVICE_ID))));
			mSimpleDevicesModel.setmDeviceRegion(c.getString(c
					.getColumnIndex(DevicesModel.DEVICE_REGION)));
			mSimpleDevicesModel.setmEP(c.getString(c
					.getColumnIndex(DevicesModel.EP)));
			mSimpleDevicesModel.setmIeee(c.getString(c
					.getColumnIndex(DevicesModel.IEEE)));
			mSimpleDevicesModel.setmLastDateTime(c.getLong(c
					.getColumnIndex(DevicesModel.LAST_UPDATE_TIME)));
			mSimpleDevicesModel.setmModelId(c.getString(c
					.getColumnIndex(DevicesModel.MODEL_ID)));
			mSimpleDevicesModel.setmName(c.getString(c
					.getColumnIndex(DevicesModel.NAME)));
			mSimpleDevicesModel.setmNodeENNAme(c.getString(c
					.getColumnIndex(DevicesModel.NODE_EN_NAME)));
			mSimpleDevicesModel.setmOnOffLine(c.getShort(c
					.getColumnIndex(DevicesModel.ON_OFF_LINE)));
			mSimpleDevicesModel.setmOnOffStatus(c.getString(c
					.getColumnIndex(DevicesModel.ON_OFF_STATUS)));

			mList.add(mSimpleDevicesModel);
		}
		c.close();
		return mList;
	}

	public void close(SQLiteDatabase db) {
		db.close();
	}

	public SQLiteDatabase getSQLiteDatabase() {
		return getWritableDatabase();
	}
}
