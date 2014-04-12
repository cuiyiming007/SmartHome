package com.gdgl.mydata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gdgl.activity.ShowDevicesGroupFragmentActivity;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.util.UiUtils;

public class DataUtil {

	public static String[] getArgs(int type) {
		String[] args = null;
		switch (type) {
		case UiUtils.LIGHTS_MANAGER:
			args = new String[7];
			args[0] = DataHelper.Wall_switch_touch + "%";
			args[1] = DataHelper.Wall_switch_double + "%";
			args[2] = DataHelper.Wall_switch_triple + "%";
			args[3] = DataHelper.Dimmer_Switch + "%";
			args[4] = DataHelper.Switch_Module_Single + "%";
			args[5] = DataHelper.Power_detect_socket + "%";
			args[6] = "1";
			break;
		case UiUtils.ELECTRICAL_MANAGER:
			args = new String[5];
			args[0] = DataHelper.Power_detect_wall + "%";
			args[1] = DataHelper.Curtain_control_switch + "%";
			args[2] = DataHelper.Infrared_controller + "%";
			args[3] = DataHelper.Magnetic_Window + "%";
			args[4] = "1";
			break;
		case UiUtils.SECURITY_CONTROL:
			args = new String[8];
			args[0] = DataHelper.Emergency_Button + "%";
			args[1] = DataHelper.Doors_and_windows_sensor_switch + "%";
			args[2] = DataHelper.Smoke_Detectors + "%";
			args[3] = DataHelper.Combustible_Gas_Detector_Gas + "%";
			args[4] = DataHelper.Combustible_Gas_Detector_CO + "%";
			args[5] = DataHelper.Combustible_Gas_Detector_Natural_gas + "%";
			args[6] = DataHelper.Siren + "%";
			args[7] = "1";
			break;
		case UiUtils.ENVIRONMENTAL_CONTROL:
			args = new String[3];
			args[0] = DataHelper.Indoor_temperature_sensor + "%";
			args[1] = DataHelper.Infrared_controller + "%";
			args[2] = "1";
			break;
		default:
			args = new String[3];
			args[0] = DataHelper.Indoor_temperature_sensor + "%";
			args[1] = DataHelper.Infrared_controller + "%";
			args[2] = "1";
			break;

		}
		return args;
	}

	public static String getWhere(int type) {
		String where = "";
		switch (type) {
		case UiUtils.LIGHTS_MANAGER:
			where = " ( model_id like ? or model_id like ? or model_id like ? or model_id like ? or model_id like ? or model_id like ? ) and on_off_line=?";
			break;
		case UiUtils.ELECTRICAL_MANAGER:
			where = " ( model_id like ? or model_id like ? or model_id like ? or model_id like ? ) and on_off_line=?";
			break;
		case UiUtils.SECURITY_CONTROL:
			where = "(model_id like ? or model_id like ? or model_id like ? or model_id like ? or model_id like ? or model_id like ? or model_id like ? ) and on_off_line=?";
			break;
		case UiUtils.ENVIRONMENTAL_CONTROL:
			where = " (model_id like ? or model_id like ? ) and on_off_line=?";
			break;
		default:
			where = "( model_id like ? or model_id like ? ) and on_off_line=?";
			break;
		}
		return where;
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

	public static List<SimpleDevicesModel> getLightingManagementDevices(
			DataHelper dh) {

		String[] args;
		HashMap<String, Integer> mMap = new HashMap<String, Integer>();
		List<String> sList = new ArrayList<String>();
		String where = DataUtil.getWhere(UiUtils.LIGHTS_MANAGER);
		SimpleDevicesModel mSimpleDevicesModel;
		List<DevicesModel> listDevicesModel = new ArrayList<DevicesModel>();
		List<SimpleDevicesModel> list = new ArrayList<SimpleDevicesModel>();
		args = DataUtil.getArgs(UiUtils.LIGHTS_MANAGER);

		SQLiteDatabase db = dh.getSQLiteDatabase();
		listDevicesModel = dh.queryForList(db, DataHelper.DEVICES_TABLE, null,
				where, args, null, null, null, null);
		int n = 0;
		for (int m = 0; m < listDevicesModel.size(); m++) {
			DevicesModel mDevicesModel = listDevicesModel.get(m);
			// sList.add(object)
			if (Integer.parseInt(mDevicesModel.getmDeviceId()) == DataHelper.ON_OFF_SWITCH_DEVICETYPE) {
				if (mMap.containsKey(mDevicesModel.getmModelId())) {

					SimpleDevicesModel aSimpleDevicesModel = list.get(mMap
							.get(mDevicesModel.getmModelId()));
					String Ieee = aSimpleDevicesModel.getmIeee();
					String OnOffStatus = aSimpleDevicesModel.getmOnOffStatus();
					String NodeENNAme = aSimpleDevicesModel.getmNodeENNAme();
					aSimpleDevicesModel.setmNodeENNAme(NodeENNAme + ","
							+ mDevicesModel.getmNodeENNAme());
					aSimpleDevicesModel.setmOnOffStatus(OnOffStatus + ","
							+ mDevicesModel.getmOnOffStatus());
					aSimpleDevicesModel.setmIeee(Ieee + ","
							+ mDevicesModel.getmIeee());
				} else {
					mSimpleDevicesModel = new SimpleDevicesModel();
					mSimpleDevicesModel.setID(mDevicesModel.getID());
					mSimpleDevicesModel.setmDeviceId(Integer
							.parseInt(mDevicesModel.getmDeviceId()));
					mSimpleDevicesModel.setmDeviceRegion(mDevicesModel
							.getmDeviceRegion());
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
					list.add(mSimpleDevicesModel);
					mMap.put(mDevicesModel.getmModelId(), n);
					n++;
				}
			} else {
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
				mSimpleDevicesModel.setmNodeENNAme(mDevicesModel
						.getmNodeENNAme());
				mSimpleDevicesModel
						.setmOnOffLine(mDevicesModel.getmOnOffLine());
				mSimpleDevicesModel.setmOnOffStatus(mDevicesModel
						.getmOnOffStatus());
				n++;
				list.add(mSimpleDevicesModel);
			}
		}
		for (SimpleDevicesModel simpleDevicesModel : list) {
			Log.i("", "tagzgs->"+simpleDevicesModel.getmOnOffLine()+" "+simpleDevicesModel.getmModelId());
		}
		return list;
	}

	public static List<SimpleDevicesModel> getOtherManagementDevices(
			DataHelper dh, int type) {
		
		String[] args;
		List<String> sList = new ArrayList<String>();
		String where = DataUtil.getWhere(type);
		SimpleDevicesModel mSimpleDevicesModel;
		List<DevicesModel> listDevicesModel = new ArrayList<DevicesModel>();
		List<SimpleDevicesModel> list = new ArrayList<SimpleDevicesModel>();
		args = DataUtil.getArgs(type);

		SQLiteDatabase db = dh.getSQLiteDatabase();
		listDevicesModel = dh.queryForList(db, DataHelper.DEVICES_TABLE, null,
				where, args, null, null, null, null);
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
			list.add(mSimpleDevicesModel);
		}
		return list;
	}

}
