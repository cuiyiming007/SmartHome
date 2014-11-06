package com.gdgl.mydata;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gdgl.model.DevicesGroup;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.Callback.CallbackWarnMessage;
import com.gdgl.smarthome.R;

public class DataUtil {

	public static boolean isSecrity(String modelID) {
		if (null == modelID || modelID.trim().equals("")) {
			return false;
		}
		if (modelID.trim().indexOf(DataHelper.Motion_Sensor) == 0
				|| modelID.trim().indexOf(
						DataHelper.Combustible_Gas_Detector_Natural_gas) == 0
				|| modelID.trim().indexOf(DataHelper.Magnetic_Window) == 0
				|| modelID.trim().indexOf(DataHelper.Emergency_Button) == 0
				|| modelID.trim().indexOf(
						DataHelper.Doors_and_windows_sensor_switch) == 0
				|| modelID.trim().indexOf(DataHelper.Smoke_Detectors) == 0
				|| modelID.trim().indexOf(
						DataHelper.Combustible_Gas_Detector_Gas) == 0
				|| modelID.trim().indexOf(
						DataHelper.Combustible_Gas_Detector_CO) == 0
				|| modelID.trim().indexOf(
						DataHelper.Wireless_Intelligent_valve_switch) == 0
				|| modelID.trim().indexOf(DataHelper.Siren) == 0) {
			return true;
		}
		return false;

	}

	public static List<DevicesModel> convertToDevicesModel(
			RespondDataEntity<ResponseParamsEndPoint> r) {
		List<DevicesModel> mList = new ArrayList<DevicesModel>();
		DevicesModel mDevicesModel = null;
		for (ResponseParamsEndPoint mResponseParamsEndPoint : r
				.getResponseparamList()) {
			if (null != mResponseParamsEndPoint) {
				mDevicesModel = new DevicesModel(mResponseParamsEndPoint);
			}
			mList.add(mDevicesModel);
		}
		return mList;
	}

	public static List<DevicesModel> getSortManagementDevices(Context c,
			DataHelper dh, int type) {

		List<DevicesModel> listDevicesModel = new ArrayList<DevicesModel>();
		String where = " device_sort=? ";
		String[] args = { Integer.toString(type) };
		listDevicesModel = dh.queryForDevicesList(dh.getSQLiteDatabase(),
				DataHelper.DEVICES_TABLE, null, where, args, null, null, null,
				null);
		if (null != listDevicesModel && listDevicesModel.size() > 0) {
			for (DevicesModel devicesModel : listDevicesModel) {
				if (null == devicesModel.getmUserDefineName()
						|| devicesModel.getmUserDefineName().trim().equals("")) {
					devicesModel.setmUserDefineName(getDefaultUserDefinname(c,
							devicesModel.getmModelId(), devicesModel.getmEP()));
				}
			}
		}

		return listDevicesModel;
	}

	public static List<SimpleDevicesModel> getSortManagementDevices(Context c,
			DataHelper dh, SQLiteDatabase db, int type) {

		String where = " device_sort=? ";
		String[] args = { Integer.toString(type) };

		SimpleDevicesModel mSimpleDevicesModel;
		List<DevicesModel> listDevicesModel = new ArrayList<DevicesModel>();
		List<SimpleDevicesModel> list = new ArrayList<SimpleDevicesModel>();

		listDevicesModel = dh.queryForDevicesList(db, DataHelper.DEVICES_TABLE,
				null, where, args, null, null, null, null);
		DevicesModel mDevicesModel;
		for (int m = 0; m < listDevicesModel.size(); m++) {
			mDevicesModel = listDevicesModel.get(m);
			mSimpleDevicesModel = new SimpleDevicesModel();
			mSimpleDevicesModel.setID(mDevicesModel.getID());
			mSimpleDevicesModel.setmDeviceId(Integer.parseInt(mDevicesModel
					.getmDeviceId()));
			mSimpleDevicesModel.setmDeviceRegion(mDevicesModel
					.getmDeviceRegion());
			mSimpleDevicesModel.setmEP(mDevicesModel.getmEP());
			mSimpleDevicesModel.setmIeee(mDevicesModel.getmIeee());
			mSimpleDevicesModel.setmLastDateTime(mDevicesModel
					.getmLastDateTime());
			mSimpleDevicesModel.setmModelId(mDevicesModel.getmModelId());
			mSimpleDevicesModel.setmName(mDevicesModel.getmName());
			mSimpleDevicesModel.setmNodeENNAme(mDevicesModel.getmNodeENNAme());
			mSimpleDevicesModel.setmOnOffLine(mDevicesModel.getmOnOffLine());
			mSimpleDevicesModel
					.setmOnOffStatus(mDevicesModel.getmOnOffStatus());
			if (mDevicesModel.getmUserDefineName() == null
					|| mDevicesModel.getmUserDefineName().trim().equals("")) {
				mSimpleDevicesModel.setmUserDefineName(getDefaultUserDefinname(
						c, mSimpleDevicesModel.getmModelId(), mSimpleDevicesModel.getmEP()));
			} else {
				mSimpleDevicesModel.setmUserDefineName(mDevicesModel
						.getmUserDefineName());
			}

			if (mDevicesModel.getmCurrent() != null
					&& !mDevicesModel.getmCurrent().trim().equals("")) {
				mSimpleDevicesModel.setmValue(mDevicesModel.getmCurrent()
						.trim());
			}

			list.add(mSimpleDevicesModel);
		}
		return list;
	}

	public static List<DevicesModel> getBindDevices(Context c, DataHelper dh) {

		String[] args = new String[3];
		args[0] = "0006OUT";
		args[1] = "0008OUT";
		args[2] = "0502OUT";
		String where = " cluster_id = ? or cluster_id = ? or cluster_id = ? ";

		List<DevicesModel> listDevicesModel = new ArrayList<DevicesModel>();
		SQLiteDatabase db = dh.getSQLiteDatabase();
		listDevicesModel = dh.queryForDevicesList(db, DataHelper.DEVICES_TABLE,
				null, where, args, null, null, null, null);
		dh.close(db);
		if (null != listDevicesModel && listDevicesModel.size() > 0) {
			for (DevicesModel devicesModel : listDevicesModel) {
				if (null == devicesModel.getmUserDefineName()
						|| devicesModel.getmUserDefineName().trim().equals("")) {
					devicesModel.setmUserDefineName(getDefaultUserDefinname(c,
							devicesModel.getmModelId(), devicesModel.getmEP()));
				}
			}
		}

		return listDevicesModel;

	}

	public static List<CallbackWarnMessage> getWarmMessage(Context c,
			DataHelper dh) {

		List<CallbackWarnMessage> mList = new ArrayList<CallbackWarnMessage>();
		Cursor cursor = null;
		SQLiteDatabase db = dh.getSQLiteDatabase();
		cursor = db.query(DataHelper.MESSAGE_TABLE, null, null, null, null,
				null, null, null);
		CallbackWarnMessage message;
		while (cursor.moveToNext()) {
			message = new CallbackWarnMessage();
			message.setId(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage._ID)));
			message.setCie_ep(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.CIE_EP)));
			message.setCie_ieee(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.CIE_IEEE)));
			message.setCie_name(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.CIE_NAME)));
			message.setHome_id(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.HOME_ID)));
			message.setHome_name(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.HOME_NAME)));
			message.setHouseIEEE(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.HOUSEIEEE)));
			message.setMsgtype(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.MSGTYPE)));
			message.setRoomId(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.ROOMID)));
			message.setTime(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.TIME)));
			message.setW_description(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.W_DESCRIPTION)));
			message.setDetailmessage(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.DETAILMESSAGE)));
			message.setW_mode(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.W_MODE)));
			message.setZone_ep(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.ZONE_EP)));
			message.setZone_ieee(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.ZONE_IEEE)));
			message.setZone_name(cursor.getString(cursor
					.getColumnIndex(CallbackWarnMessage.ZONE_NAME)));
			mList.add(message);
		}
		cursor.close();
		// db.close();
		return mList;

	}

	public static DevicesModel getDeviceModelByIeee(String ieee, DataHelper dh,
			SQLiteDatabase db) {
		String where = " ieee=? ";
		String[] args = { ieee + "" };
		List<DevicesModel> mList = dh.queryForDevicesList(db,
				DataHelper.DEVICES_TABLE, null, where, args, null, null, null,
				null);
		if (null != mList && mList.size() > 0) {
			return mList.get(0);
		}
		return null;
	}

	public static DevicesModel getDeviceModelByModelid(String modelid,
			DataHelper dh, SQLiteDatabase db) {
		String where = " model_id like ? ";
		String[] args = { modelid + "%" };
		List<DevicesModel> mList = dh.queryForDevicesList(db,
				DataHelper.DEVICES_TABLE, null, where, args, null, null, null,
				null);
		if (null != mList && mList.size() > 0) {
			return mList.get(0);
		}
		return null;
	}

	public static String getDefaultUserDefinname(Context c, String modelID,
			String ep) {
		String result = "";

		if (modelID.indexOf(DataHelper.Motion_Sensor) == 0) { // ZigBee动作感应器
			result = c.getResources().getString(R.string.Motion_Sensor);
		}
		if (modelID.indexOf(DataHelper.Magnetic_Window) == 0) { // ZigBee窗磁
			result = c.getResources().getString(R.string.Magnetic_Window);
		}
		if (modelID.indexOf(DataHelper.Emergency_Button) == 0) { // ZigBee紧急按钮
			result = c.getResources().getString(R.string.Emergency_Button);
		}
		if (modelID.indexOf(DataHelper.Doors_and_windows_sensor_switch) == 0) { // 门窗感应开关
			result = c.getResources().getString(
					R.string.Doors_and_windows_sensor_switch);
		}
		if (modelID.indexOf(DataHelper.Smoke_Detectors) == 0) { // 烟雾感应器
			result = c.getResources().getString(R.string.Smoke_Detectors);
		}
		if (modelID.indexOf(DataHelper.Combustible_Gas_Detector_Gas) == 0) { // 可燃气体探测器（煤气）
			result = c.getResources().getString(
					R.string.Combustible_Gas_Detector_Gas);
		}

		if (modelID.indexOf(DataHelper.Combustible_Gas_Detector_CO) == 0) { // 可燃气体探测器（一氧化碳）
			result = c.getResources().getString(
					R.string.Combustible_Gas_Detector_CO);
		}
		if (modelID.indexOf(DataHelper.Combustible_Gas_Detector_Natural_gas) == 0) { // 可燃气体探测器（天然气）
			result = c.getResources().getString(
					R.string.Combustible_Gas_Detector_Natural_gas);
		}
		if (modelID.indexOf(DataHelper.Wireless_Intelligent_valve_switch) == 0) { // 无线智能阀门开关
			result = c.getResources().getString(
					R.string.Wireless_Intelligent_valve_switch);
		}
		if (modelID.indexOf(DataHelper.Siren) == 0) { // ZigBee警报器
			result = c.getResources().getString(R.string.Siren);
		}
		if (modelID.indexOf(DataHelper.Wall_switch_touch) == 0) { // ZigBee墙面开关（单键）
			result = c.getResources().getString(R.string.Wall_switch_touch);
		}
		if (modelID.indexOf(DataHelper.Wall_switch_double) == 0) { // ZigBee墙面开关（双键）
			if (ep.equals("01")) {
				result = c.getResources().getString(
						R.string.Wall_switch_double_R);
			}
			if (ep.equals("02")) {
				result = c.getResources().getString(
						R.string.Wall_switch_double_L);
			}
		}
		if (modelID.indexOf(DataHelper.Wall_switch_triple) == 0) { // ZigBee墙面开关（三键）
			if (ep.equals("01")) {
				result = c.getResources().getString(
						R.string.Wall_switch_triple_R);
			}
			if (ep.equals("02")) {
				result = c.getResources().getString(
						R.string.Wall_switch_triple_M);
			}
			if (ep.equals("03")) {
				result = c.getResources().getString(
						R.string.Wall_switch_triple_L);
			}
		}
		if (modelID.indexOf(DataHelper.Dimmer_Switch) == 0) { // ZigBee调光开关
			result = c.getResources().getString(R.string.Dimmer_Switch);
		}
		if (modelID.indexOf(DataHelper.Power_detect_wall) == 0) { // 中规电能检测墙面插座
			result = c.getResources().getString(R.string.Power_detect_wall);
		}
		if (modelID.indexOf(DataHelper.Curtain_control_switch) == 0) { // ZigBee幕帘控制开关
			result = c.getResources()
					.getString(R.string.Curtain_control_switch);
		}
		if (modelID.indexOf(DataHelper.Infrared_controller) == 0) { // ZigBee红外控制器
			result = c.getResources().getString(R.string.Infrared_controller);
		}
		if (modelID.indexOf(DataHelper.Indoor_temperature_sensor) == 0) { // ZigBee室内型温湿度感应器
			result = c.getResources().getString(
					R.string.Indoor_temperature_sensor);
		}
		if (modelID.indexOf(DataHelper.Light_Sensor) == 0) { // ZigBee光线感应器
			result = c.getResources().getString(R.string.Light_Sensor);
		}
		if (modelID.indexOf(DataHelper.Multi_key_remote_control) == 0) { // ZigBee多键遥控器
			result = c.getResources().getString(
					R.string.Multi_key_remote_control);
		}
		if (modelID.indexOf(DataHelper.Doorbell_button) == 0) { // ZigBee门铃按键
			result = c.getResources().getString(R.string.Doorbell_button);
		}
		if (modelID.indexOf(DataHelper.Switch_Module_Single) == 0) { // ZigBee开关模块（单路）
			result = c.getResources().getString(R.string.Switch_Module_Single);
		}
		if (modelID.indexOf(DataHelper.Energy_detection_dimming_module) == 0) { // 吸顶电能检测调光模块
			result = c.getResources().getString(
					R.string.Energy_detection_dimming_module);
		}
		if (modelID.indexOf(DataHelper.Pro_RF) == 0) { // ZigBee Pro RF ģ��
			result = c.getResources().getString(R.string.Pro_RF);
		}
		if (modelID.indexOf(DataHelper.RS232_adapter) == 0) { // ��ҵ��ZigBee
																// RS232适配器
			result = c.getResources().getString(R.string.RS232_adapter);
		}
		if (modelID.indexOf(DataHelper.Power_detect_socket) == 0) { // ZigBee电能检测插座
			result = c.getResources().getString(R.string.Power_detect_socket);
		}

		if (modelID.indexOf(DataHelper.One_key_operator) == 0) { // 安防中心
			result = c.getResources().getString(R.string.One_key_operator);
		}
		return result;

	}

	// public static Set<String> getRegions(Context c, DataHelper dh) {
	// List<DevicesModel> listDevicesModel = new ArrayList<DevicesModel>();
	// Set<String> region = new HashSet<String>();
	//
	// SQLiteDatabase db = dh.getSQLiteDatabase();
	// listDevicesModel = dh.queryForDevicesList(db, DataHelper.DEVICES_TABLE,
	// null,
	// null, null, null, null, null, null);
	//
	// for (int m = 0; m < listDevicesModel.size(); m++) {
	// String mRegion = listDevicesModel.get(m).getmDeviceRegion();
	// if (!region.contains(mRegion)) {
	// region.add(mRegion);
	// }
	// }
	// return region;
	// }

	public static List<SimpleDevicesModel> getDevices(Context c, DataHelper dh,
			String[] args, String where, boolean b) {
		SimpleDevicesModel mSimpleDevicesModel;
		List<DevicesModel> listDevicesModel = new ArrayList<DevicesModel>();
		List<SimpleDevicesModel> list = new ArrayList<SimpleDevicesModel>();

		SQLiteDatabase db = dh.getSQLiteDatabase();
		listDevicesModel = dh.queryForDevicesList(db, DataHelper.DEVICES_TABLE,
				null, where, args, null, null, null, null);

		if (b) {
			for (int m = 0; m < listDevicesModel.size(); m++) {
				DevicesModel mDevicesModel = listDevicesModel.get(m);
				if (Integer.parseInt(mDevicesModel.getmDeviceId()) != DataHelper.ON_OFF_SWITCH_DEVICETYPE
						&& Integer.parseInt(mDevicesModel.getmDeviceId()) != DataHelper.DIMEN_SWITCH_DEVICETYPE
						&& mDevicesModel.getmModelId().indexOf(
								DataHelper.Multi_key_remote_control) != 0) {
					mSimpleDevicesModel = new SimpleDevicesModel();
					mSimpleDevicesModel.setID(mDevicesModel.getID());
					mSimpleDevicesModel.setmDeviceId(Integer
							.parseInt(mDevicesModel.getmDeviceId()));
					mSimpleDevicesModel.setmDeviceRegion(mDevicesModel
							.getmDeviceRegion());
					mSimpleDevicesModel.setmRid(mDevicesModel.getmRid());
					mSimpleDevicesModel.setmEP(mDevicesModel.getmEP());
					mSimpleDevicesModel.setmIeee(mDevicesModel.getmIeee());
					mSimpleDevicesModel.setmLastDateTime(mDevicesModel
							.getmLastDateTime());
					mSimpleDevicesModel
							.setmModelId(mDevicesModel.getmModelId());
					mSimpleDevicesModel.setmName(mDevicesModel.getmName());
					mSimpleDevicesModel.setmNodeENNAme(mDevicesModel
							.getmNodeENNAme());
					mSimpleDevicesModel.setmOnOffLine(mDevicesModel
							.getmOnOffLine());
					mSimpleDevicesModel.setmOnOffStatus(mDevicesModel
							.getmOnOffStatus());
					if (mDevicesModel.getmUserDefineName() == null
							|| mDevicesModel.getmUserDefineName().trim()
									.equals("")) {
						mSimpleDevicesModel
								.setmUserDefineName(getDefaultUserDefinname(c,
										mSimpleDevicesModel.getmModelId(), mSimpleDevicesModel.getmEP()));
					} else {
						mSimpleDevicesModel.setmUserDefineName(mDevicesModel
								.getmUserDefineName());
					}
					list.add(mSimpleDevicesModel);
				}
			}
		} else {
			for (int m = 0; m < listDevicesModel.size(); m++) {
				DevicesModel mDevicesModel = listDevicesModel.get(m);
				mSimpleDevicesModel = new SimpleDevicesModel();
				mSimpleDevicesModel.setID(mDevicesModel.getID());
				mSimpleDevicesModel.setmDeviceId(Integer.parseInt(mDevicesModel
						.getmDeviceId()));
				mSimpleDevicesModel.setmDeviceRegion(mDevicesModel
						.getmDeviceRegion());
				mSimpleDevicesModel.setmRid(mDevicesModel.getmRid());
				mSimpleDevicesModel.setmEP(mDevicesModel.getmEP());
				mSimpleDevicesModel.setmIeee(mDevicesModel.getmIeee());
				mSimpleDevicesModel.setmLastDateTime(mDevicesModel
						.getmLastDateTime());
				mSimpleDevicesModel.setmModelId(mDevicesModel.getmModelId());
				mSimpleDevicesModel.setmName(mDevicesModel.getmName());
				mSimpleDevicesModel.setmNodeENNAme(mDevicesModel
						.getmNodeENNAme());
				mSimpleDevicesModel
						.setmOnOffLine(mDevicesModel.getmOnOffLine());
				mSimpleDevicesModel.setmOnOffStatus(mDevicesModel
						.getmOnOffStatus());
				if (mDevicesModel.getmUserDefineName() == null
						|| mDevicesModel.getmUserDefineName().trim().equals("")) {
					mSimpleDevicesModel
							.setmUserDefineName(getDefaultUserDefinname(c,
									mSimpleDevicesModel.getmModelId(), mSimpleDevicesModel.getmEP()));
				} else {
					mSimpleDevicesModel.setmUserDefineName(mDevicesModel
							.getmUserDefineName());
				}
				list.add(mSimpleDevicesModel);
			}
		}
		try {
			dh.close(db);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	public static List<SimpleDevicesModel> getDevices(Context c, DataHelper dh,
			String[] args, String where) {
		return getDevices(c, dh, args, where, true);
	}

	public static List<String> getScenes(Context c, DataHelper dh) {

		if (null == dh) {
			dh = new DataHelper(c);
		}
		List<DevicesGroup> listDevicesModel = new ArrayList<DevicesGroup>();
		List<String> scenes = new ArrayList<String>();

		SQLiteDatabase db = dh.getSQLiteDatabase();
		listDevicesModel = dh.queryForGroupList(c, db, DataHelper.GROUP_TABLE,
				null, null, null, null, null, null, null);

		for (DevicesGroup d : listDevicesModel) {
			if (!scenes.contains(d.getGroupName())) {
				scenes.add(d.getGroupName());
			}
		}
		return scenes;
	}

	public static List<SimpleDevicesModel> getScenesDevices(Context c,
			DataHelper dh, String name) {

		if (null == dh) {
			dh = new DataHelper(c);
		}
		String[] args = { name };
		List<DevicesGroup> listDevicesModel = new ArrayList<DevicesGroup>();
		List<SimpleDevicesModel> scenes = new ArrayList<SimpleDevicesModel>();

		SQLiteDatabase db = dh.getSQLiteDatabase();
		listDevicesModel = dh.queryForGroupList(c, db, DataHelper.GROUP_TABLE,
				null, " group_name=? ", args, null, null, null, null);

		for (DevicesGroup d : listDevicesModel) {
			String ieee = d.getIeee();
			if (!ieee.equals("-1")) {
				String[] ag = { d.getIeee() };
				String where = " ieee=? ";
				List<SimpleDevicesModel> mm = DataUtil.getDevices(c, dh, ag,
						where);
				if (null != mm && mm.size() > 0) {
					scenes.add(mm.get(0));
				}
			}
		}
		return scenes;
	}

	public static DevicesGroup getOneScenesDevices(Context c, DataHelper dh,
			String ieee) {

		if (null == dh) {
			dh = new DataHelper(c);
		}
		String[] args = { ieee };
		List<DevicesGroup> listDevicesModel = new ArrayList<DevicesGroup>();
		List<SimpleDevicesModel> scenes = new ArrayList<SimpleDevicesModel>();

		SQLiteDatabase db = dh.getSQLiteDatabase();
		listDevicesModel = dh.queryForGroupList(c, db, DataHelper.GROUP_TABLE,
				null, " devices_ieee=? ", args, null, null, null, null);

		if (null != listDevicesModel && listDevicesModel.size() > 0) {
			return listDevicesModel.get(0);
		}

		return null;

	}
}
