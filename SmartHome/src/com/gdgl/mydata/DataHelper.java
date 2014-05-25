package com.gdgl.mydata;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.model.ContentValuesListener;
import com.gdgl.model.DevicesGroup;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Callback.CallbackWarmMessage;
import com.gdgl.mydata.video.VideoNode;
import com.gdgl.util.UiUtils;

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
	public static final int TEMPTURE_SENSOR_DEVICETYPE = 770;
	public static final int IAS_ACE_DEVICETYPE = 1025;
	public static final int IAS_ZONE_DEVICETYPE = 1026;
	public static final int IAS_WARNNING_DEVICE_DEVICETYPE = 1027;

	public static final String Motion_Sensor = "ZB11A"; // ZigBee������Ӧ��
	public static final String Magnetic_Window = "Z311A"; // ZigBee����
	public static final String Emergency_Button = "Z302D"; // ZigBee������ť
	public static final String Doors_and_windows_sensor_switch = "Z311J"; // �Ŵ���Ӧ����
	public static final String Smoke_Detectors = "ZA01A"; // �����Ӧ��
	public static final String Combustible_Gas_Detector_Gas = "ZA01B"; // ��ȼ����̽������ú��)
	public static final String Combustible_Gas_Detector_CO = "ZA01C"; // ��ȼ����̽������һ����̼)
	public static final String Combustible_Gas_Detector_Natural_gas = "ZA01D"; // ��ȼ����̽��������Ȼ��)
	public static final String Wireless_Intelligent_valve_switch = "ZA10"; // �������ܷ��ſ���
	public static final String Siren = "Z602A"; // ZigBee������
	public static final String Wall_switch_touch = "ZB02A"; // ZigBeeǽ�濪�أ�����
	public static final String Wall_switch_double = "ZB02B"; // ǽ�濪�أ�˫��)
	public static final String Wall_switch_triple = "ZB02C"; // ZigBee��ǽ�濪�أ����
	public static final String Dimmer_Switch = "ZB02F"; // ZigBee���⿪��
	public static final String Power_detect_wall = "Z816H"; // �й���ܼ��ǽ�����
	public static final String Curtain_control_switch = "Z815N"; // ZigBeeĻ�����ƿ���
	public static final String Infrared_controller = "Z211"; // ZigBee���������
	public static final String Indoor_temperature_sensor = "Z711"; // ZigBee��������ʪ�ȸ�Ӧ��
	public static final String Light_Sensor = "Z311G"; // ZigBee���߸�Ӧ��
	public static final String Multi_key_remote_control = "Z503"; // ZigBee���ң����
	public static final String Doorbell_button = "Z312"; // ZigBee���尴��
	public static final String Switch_Module_Single = "Z805B"; // ZigBee����ģ�飨��·��
	public static final String Energy_detection_dimming_module = "Z817B"; // ����ܼ�����ģ��
	public static final String Pro_RF = "Z100BI"; // ZigBee Pro RF ģ��
	public static final String RS232_adapter = "ZL01A"; // ��ҵ��ZigBee RS232������
	public static final String Power_detect_socket = "Z809A"; // ZigBee ���ܼ�����
	public static final String One_key_operator = "Z103AE3C"; // 一键布防撤防

	public static final String DATABASE_NAME = "smarthome";
	public static final String DEVICES_TABLE = "devices";
	public static final String GROUP_TABLE = "groups";
	public static final String VIDEO_TABLE = "video";
	public static final String MESSAGE_TABLE = "message_table";
	public static final int DATEBASE_VERSTION = 1;

	public StringBuilder mStringBuilder;
	public StringBuilder mAStringBuilder;
	public StringBuilder videoStringBuilder;
	public StringBuilder messageStringBuilder;

	// public SQLiteDatabase db;

	public DataHelper(Context contex) {
		super(contex, DATABASE_NAME, null, DATEBASE_VERSTION);
		mStringBuilder = new StringBuilder();
		mAStringBuilder= new StringBuilder();
		videoStringBuilder=new StringBuilder();
		messageStringBuilder=new StringBuilder();
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
		mStringBuilder.append(DevicesModel.USER_DEFINE_NAME + " VARCHAR,");
		mStringBuilder.append(DevicesModel.MODEL_ID + " VARCHAR,");
		mStringBuilder.append(DevicesModel.DEVICE_ID + " VARCHAR(4),");
		mStringBuilder.append(DevicesModel.DEVICE_REGION + " VARCHAR,");
		mStringBuilder.append(DevicesModel.CLUSTER_ID + " VARCHAR,");
		mStringBuilder.append(DevicesModel.BIND_TO + " VARCHAR,");
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
		
		mAStringBuilder.append("CREATE TABLE " + GROUP_TABLE + " (");
		mAStringBuilder.append(DevicesGroup._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		mAStringBuilder.append(DevicesGroup.DEVICES_IEEE + " VARCHAR(16),");
		mAStringBuilder.append(DevicesGroup.GROUP_NAME + " VARCHAR(48),");
		mAStringBuilder.append(DevicesGroup.DEVICES_VALUE + " INTEGER,");
		mAStringBuilder.append(DevicesGroup.EP + " VARCHAR(16),");
		mAStringBuilder.append(DevicesGroup.GROUP_ID + " INTEGER,");
		mAStringBuilder.append(DevicesGroup.GROUP_STATE + " INTEGER,");
		mAStringBuilder.append(DevicesGroup.ON_OFF_STATUS + " INTEGER )");
		
		//video table create string
		videoStringBuilder.append("CREATE TABLE " + VIDEO_TABLE + " (");
		videoStringBuilder.append(VideoNode._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		videoStringBuilder.append(VideoNode.ALIAS + " VARCHAR(16),");
		videoStringBuilder.append(VideoNode.ID + " VARCHAR(16),");
		videoStringBuilder.append(VideoNode.HTTPPORT + " VARCHAR(48),");
		videoStringBuilder.append(VideoNode.IPC_IPADDR + " INTEGER,");
		videoStringBuilder.append(VideoNode.NAME + " VARCHAR(16),");
		videoStringBuilder.append(VideoNode.PASSWORD + " INTEGER,");
		videoStringBuilder.append(VideoNode.RTSPORT + " INTEGER)");
		
		
		//message table create string
		messageStringBuilder.append("CREATE TABLE " + MESSAGE_TABLE + " (");
		messageStringBuilder.append(CallbackWarmMessage._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,");
		messageStringBuilder.append(CallbackWarmMessage.CIE_EP + " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarmMessage.CIE_IEEE + " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarmMessage.CIE_NAME + " VARCHAR(48),");
		messageStringBuilder.append(CallbackWarmMessage.HOME_ID + " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarmMessage.HOME_NAME + " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarmMessage.HOUSEIEEE + " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarmMessage.MSGTYPE + " INTEGER,");
		messageStringBuilder.append(CallbackWarmMessage.ROOMID + " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarmMessage.TIME + " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarmMessage.W_DESCRIPTION + " VARCHAR(64),");
		messageStringBuilder.append(CallbackWarmMessage.W_MODE + " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarmMessage.ZONE_EP + " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarmMessage.ZONE_IEEE + " VARCHAR(16),");
		messageStringBuilder.append(CallbackWarmMessage.ZONE_NAME + " VARCHAR(16))");
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		initStringBuilder();
		Log.i(DEVICES_TABLE, "zgs-> " + mStringBuilder.toString());

		db.execSQL(mStringBuilder.toString());
		db.execSQL(mAStringBuilder.toString());
		db.execSQL(videoStringBuilder.toString());
		db.execSQL(messageStringBuilder.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + DEVICES_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + VIDEO_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + MESSAGE_TABLE);
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
		long result = 0;
		try {
			 result= db.insert(table, nullColumnHack, values.convertContentValues());
//			 db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			db.close();
		}
		return result;

	}
	
	public long insertDevList(SQLiteDatabase db, String table, String nullColumnHack,
			List<DevicesModel> values) {
		long result = 0;
		try {
			for (DevicesModel devicesModel : values) {
				result= db.insert(table, nullColumnHack, devicesModel.convertContentValues());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			db.close();
		}
		return result;

	}
	
	public long insertList(SQLiteDatabase db, String table,
			String nullColumnHack, ArrayList<ResponseParamsEndPoint> r) {

		long result = -100;
		List<DevicesModel> mList = convertToDevicesModel(r);
		db.beginTransaction();
		try {
			for (DevicesModel devicesModel : mList) {
				if (devicesModel.getmIeee().trim().equals("00137A0000010264")
						&& devicesModel.getmEP().trim().equals("0A")) {
					continue;
				}
				ContentValues c = devicesModel.convertContentValues();
				long m = db.insert(table, nullColumnHack, c);
				if (-1 == m) {
					result = m;
				}

			}
			db.setTransactionSuccessful();
//			db.close();
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}
	/***
	 * insert a list which implement the ContentValuesListener
	 * @param db
	 * @param table
	 * @param nullColumnHack
	 * @param arrayList
	 * @return
	 */
	public long insertVideoList(SQLiteDatabase db, String table,
			String nullColumnHack, ArrayList<VideoNode> arrayList) {

		long result = -100;
//		List<DevicesModel> mList = convertToDevicesModel(r);
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
//			db.close();
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}
	public long insertMessageList(SQLiteDatabase db, String table,
			String nullColumnHack, ArrayList<CallbackWarmMessage> arrayList) {
		
		long result = -100;
//		List<DevicesModel> mList = convertToDevicesModel(r);
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
//			db.close();
		} finally {
			db.endTransaction();
			db.close();
		}
		return result;
	}
	
	public void emptyTable(SQLiteDatabase db,String table)
	{
//		try {
			db.execSQL("delete from "+table+" where 1=1");
//			db.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}finally{
//			db.close();
//		}
		
	}
	
	public long insertGroup(SQLiteDatabase db, String table,
			String nullColumnHack, ContentValues c) {
		long m = 0;
		try {
			m = db.insert(table, nullColumnHack, c);
//			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			db.close();
		}
		return m;
	}
	
	public int delete(Context c,SQLiteDatabase db, String table, String whereClause,
			String[] whereArgs) {
		String[] iees = null;
		if (whereArgs[0].contains(",")) {
			iees = whereArgs[0].split(",");
		} else {
			iees = whereArgs;
		}
		StringBuilder sb = new StringBuilder();
		getFromSharedPreferences.setharedPreferences(c);
		
		List<String> mIeees = new ArrayList<String>();
		String comm = getFromSharedPreferences.getCommonUsed();
		if (null != comm && !comm.trim().equals("")) {
			String[] result = comm.split("@@");
			for (String string : result) {
				if (!string.trim().equals("")) {
					if (string.indexOf(UiUtils.DEVICES_FLAG) == 0) {
						mIeees.add(string.replace(UiUtils.DEVICES_FLAG, "")
								.trim());
					}
				}
			}
		}
		
		try {
			for (String string : iees) {
//				ContentValues c = new ContentValues();
//				c.put(DevicesModel.ON_OFF_LINE, DevicesModel.DEVICE_OFF_LINE);
//				db.update(table, c, " ieee=? ", new String[] { string });
				db.delete(table, " ieee=? ", new String[] { string });
				db.delete(DataHelper.GROUP_TABLE, " ieee=? ", new String[] { string });
				if(null!=mIeees && mIeees.contains(string.trim())){
					mIeees.remove(string.trim());
				}
			}
//			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			db.close();
		}

		if(null!= mIeees && mIeees.size()>0){
			for (String strings : mIeees) {
				sb.append(UiUtils.REGION_FLAG + strings + "@@");
			}
		}else{
			sb.append("@@");
		}
		getFromSharedPreferences.setCommonUsed(sb.toString());
		return 0;
	}
	
	
	public int deleteDevices(SQLiteDatabase db, String table, String whereClause,
			String[] whereArgs) {
		long m = 0;
		try {
			m=db.delete(table, whereClause, whereArgs);
//			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			db.close();
		}
		return (int) m;
	}
	
	public int deleteGroup(SQLiteDatabase db, String table, String whereClause,
			String[] whereArgs) {
		return db.delete(table, whereClause, whereArgs);
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
		Cursor c = null;
		try {
			c=db.query(table, columns, selection, selectionArgs, groupBy,
					having, orderBy, limit);
//			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			db.close();
		}
		return c;
	}
	
	public static List<VideoNode> getVideoList(Context c, DataHelper dh) {
		List<VideoNode> mList = new ArrayList<VideoNode>();
		Cursor cursor = null;
		SQLiteDatabase db=dh.getSQLiteDatabase();
		cursor = db.query(DataHelper.VIDEO_TABLE, null, null,
				null, null, null, null, null);
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
			mList.add(mVideoNode);
		}
		cursor.close();
//		db.close();
		return mList;

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
			mDevicesModel.setmUserDefineName(c.getString(c.getColumnIndex(DevicesModel.USER_DEFINE_NAME)));
			mDevicesModel.setmClusterID(c.getString(c.getColumnIndex(DevicesModel.CLUSTER_ID)));
			mDevicesModel.setmBindTo(c.getString(c.getColumnIndex(DevicesModel.BIND_TO)));
			mList.add(mDevicesModel);
		}
		c.close();
//		db.close();
		return mList;
	}

	public List<DevicesGroup> queryForGroupList(Context con,SQLiteDatabase db, String table,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) {
		List<DevicesGroup> mList = new ArrayList<DevicesGroup>();
		DevicesGroup mDevicesModel = null;
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy, limit);
		while (c.moveToNext()) {

			mDevicesModel = new DevicesGroup(con);
			mDevicesModel.setDevicesState(c.getInt(c.getColumnIndex(DevicesGroup.ON_OFF_STATUS))==0);
			mDevicesModel.setDevicesValue(c.getInt(c.getColumnIndex(DevicesGroup.DEVICES_VALUE)));
			mDevicesModel.setEp(c.getString(c.getColumnIndex(DevicesGroup.EP)));
			mDevicesModel.setGroupId(c.getInt(c.getColumnIndex(DevicesGroup.GROUP_ID)));
			mDevicesModel.setGroupName(c.getString(c.getColumnIndex(DevicesGroup.GROUP_NAME)));
			mDevicesModel.setIeee(c.getString(c.getColumnIndex(DevicesGroup.DEVICES_IEEE)));
			
			mList.add(mDevicesModel);
		}
		c.close();
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
