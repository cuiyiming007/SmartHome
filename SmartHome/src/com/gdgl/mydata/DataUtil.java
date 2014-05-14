package com.gdgl.mydata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gdgl.activity.ShowDevicesGroupFragmentActivity;
import com.gdgl.model.DevicesGroup;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;

public class DataUtil {

	public static String[] getArgs(int type) {
		String[] args = null;
		switch (type) {
		case UiUtils.LIGHTS_MANAGER:
			args = new String[3];
			args[0] = DataHelper.Energy_detection_dimming_module + "%";
			args[1] = DataHelper.Switch_Module_Single + "%";
			args[2] = "1";
			break;
		case UiUtils.ELECTRICAL_MANAGER:
			args = new String[5];
			args[0] = DataHelper.Power_detect_wall + "%";
			args[1] = DataHelper.Power_detect_socket + "%";
			args[2] = DataHelper.Curtain_control_switch + "%";
			args[3] = DataHelper.Infrared_controller + "%";
			args[4] = "1";
			break;
		case UiUtils.SECURITY_CONTROL:
			args = new String[12];
			args[0] = DataHelper.Motion_Sensor + "%";
			args[1] = DataHelper.Magnetic_Window + "%";
			args[2] = DataHelper.Emergency_Button + "%";
			args[3] = DataHelper.Doors_and_windows_sensor_switch + "%";
			args[4] = DataHelper.Smoke_Detectors + "%";
			args[5] = DataHelper.Combustible_Gas_Detector_Gas + "%";
			args[6] = DataHelper.Combustible_Gas_Detector_CO + "%";
			args[7] = DataHelper.Combustible_Gas_Detector_Natural_gas + "%";
			args[8] = DataHelper.Wireless_Intelligent_valve_switch + "%";
			args[9] = DataHelper.Siren + "%";
			args[10] = DataHelper.One_key_operator + "%";
			args[11] = "1";
			break;
		case UiUtils.ENVIRONMENTAL_CONTROL:
			args = new String[3];
			args[0] = DataHelper.Indoor_temperature_sensor + "%";
			args[1] = DataHelper.Light_Sensor + "%";
			args[2] = "1";
			break;
		case UiUtils.ENERGY_CONSERVATION:
			break;
		case UiUtils.OTHER:
			args = new String[2];
			args[0] = DataHelper.Multi_key_remote_control + "%";
			args[1] = "1";
			break;
		default:
			break;

		}
		return args;
	}

	public static String getWhere(int type) {
		String where = "";
		switch (type) {
		case UiUtils.LIGHTS_MANAGER:
			where = " ( model_id like ? or model_id like ? ) and on_off_line=? ";
			break;
		case UiUtils.ELECTRICAL_MANAGER:
			where = " ( model_id like ? or model_id like ? or  model_id like ? or model_id like ?  ) and on_off_line=? ";
			break;
		case UiUtils.SECURITY_CONTROL:
			where = "( model_id like ? or model_id like ? or model_id like ? or model_id like ? or model_id like ? or model_id like ? or model_id like ? or model_id like ? or model_id like ? or model_id like ? or model_id like ? ) and on_off_line=? ";
			break;
		case UiUtils.ENVIRONMENTAL_CONTROL:
			where = " ( model_id like ? or model_id like ? ) and on_off_line=? ";
			;
			break;
		case UiUtils.ENERGY_CONSERVATION:

			break;
		case UiUtils.OTHER:
			where = " model_id like ? and on_off_line=? ";
			break;
		default:
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
            Context c, DataHelper dh) {

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

			if (Integer.parseInt(mDevicesModel.getmDeviceId()) == DataHelper.ON_OFF_SWITCH_DEVICETYPE) {
				if (mMap.containsKey(mDevicesModel.getmIeee())) {

					SimpleDevicesModel aSimpleDevicesModel = list.get(mMap
							.get(mDevicesModel.getmIeee()));
					String OnOffStatus = aSimpleDevicesModel.getmOnOffStatus();
					String NodeENNAme = aSimpleDevicesModel.getmNodeENNAme();
					String EP = aSimpleDevicesModel.getmEP();
					aSimpleDevicesModel.setmNodeENNAme(NodeENNAme + ","
							+ mDevicesModel.getmNodeENNAme());
					aSimpleDevicesModel.setmOnOffStatus(OnOffStatus + ","
							+ mDevicesModel.getmOnOffStatus());
					aSimpleDevicesModel.setmEP(EP + ","
							+ mDevicesModel.getmEP());
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
					if (mDevicesModel.getmUserDefineName() == null
							|| mDevicesModel.getmUserDefineName().trim()
									.equals("")) {
						mSimpleDevicesModel
								.setmUserDefineName(getDefaultUserDefinname(c,
										mSimpleDevicesModel.getmModelId()));
					} else {
						mSimpleDevicesModel.setmUserDefineName(mDevicesModel
								.getmUserDefineName());
					}
					list.add(mSimpleDevicesModel);
					mMap.put(mDevicesModel.getmIeee(), n);
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
				if (mDevicesModel.getmUserDefineName() == null
						|| mDevicesModel.getmUserDefineName().trim().equals("")) {
					mSimpleDevicesModel
							.setmUserDefineName(getDefaultUserDefinname(c,
									mSimpleDevicesModel.getmModelId()));
				} else {
					mSimpleDevicesModel.setmUserDefineName(mDevicesModel
							.getmUserDefineName());
				}
				n++;
				list.add(mSimpleDevicesModel);
			}
		}
		for (SimpleDevicesModel simpleDevicesModel : list) {
			Log.i("", "tagzgs->" + simpleDevicesModel.getmOnOffLine() + " "
					+ simpleDevicesModel.getmModelId());
		}
		return list;
	}

    public static List<SimpleDevicesModel> getOtherManagementDevices(Context c,
            DataHelper dh, int type) {

        String[] args;
        List<String> sList = new ArrayList<String>();
        String where = DataUtil.getWhere(type);
        SimpleDevicesModel mSimpleDevicesModel;
        List<DevicesModel> listDevicesModel = new ArrayList<DevicesModel>();
        List<SimpleDevicesModel> list = new ArrayList<SimpleDevicesModel>();
        args = DataUtil.getArgs(type);
        
        if(null==where || null==args){
            return null;
        }
        
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
            if (mDevicesModel.getmUserDefineName() == null
                    || mDevicesModel.getmUserDefineName().trim().equals("")) {
                mSimpleDevicesModel.setmUserDefineName(getDefaultUserDefinname(
                        c, mSimpleDevicesModel.getmModelId()));
            } else {
                mSimpleDevicesModel.setmUserDefineName(mDevicesModel
                        .getmUserDefineName());
            }
            list.add(mSimpleDevicesModel);
        }
        return list;
    }

    public static String getDefaultUserDefinname(Context c, String modelID) {
        String result = "";

        if (modelID.indexOf(DataHelper.Motion_Sensor) == 0) {
            result = c.getResources().getString(R.string.Motion_Sensor);
        }
        if (modelID.indexOf(DataHelper.Magnetic_Window) == 0) {
            result = c.getResources().getString(R.string.Magnetic_Window);
        }
        if (modelID.indexOf(DataHelper.Emergency_Button) == 0) {
            result = c.getResources().getString(R.string.Emergency_Button);
        }
        if (modelID.indexOf(DataHelper.Doors_and_windows_sensor_switch) == 0) {
            result = c.getResources().getString(
                    R.string.Doors_and_windows_sensor_switch);
        }
        if (modelID.indexOf(DataHelper.Smoke_Detectors) == 0) {
            result = c.getResources().getString(R.string.Smoke_Detectors);
        }
        if (modelID.indexOf(DataHelper.Combustible_Gas_Detector_Gas) == 0) {
            result = c.getResources().getString(
                    R.string.Combustible_Gas_Detector_Gas);
        }

        if (modelID.indexOf(DataHelper.Combustible_Gas_Detector_CO) == 0) {
            result = c.getResources().getString(
                    R.string.Combustible_Gas_Detector_CO);
        }
        if (modelID.indexOf(DataHelper.Combustible_Gas_Detector_Natural_gas) == 0) {
            result = c.getResources().getString(
                    R.string.Combustible_Gas_Detector_Natural_gas);
        }
        if (modelID.indexOf(DataHelper.Wireless_Intelligent_valve_switch) == 0) {
            result = c.getResources().getString(
                    R.string.Wireless_Intelligent_valve_switch);
        }
        if (modelID.indexOf(DataHelper.Siren) == 0) {
            result = c.getResources().getString(R.string.Siren);
        }
        if (modelID.indexOf(DataHelper.Wall_switch_touch) == 0) {
            result = c.getResources().getString(R.string.Wall_switch_touch);
        }
        if (modelID.indexOf(DataHelper.Wall_switch_double) == 0) {
            result = c.getResources().getString(R.string.Wall_switch_double);
        }
        if (modelID.indexOf(DataHelper.Wall_switch_triple) == 0) {
            result = c.getResources().getString(R.string.Wall_switch_triple);
        }
        if (modelID.indexOf(DataHelper.Dimmer_Switch) == 0) {
            result = c.getResources().getString(R.string.Dimmer_Switch);
        }
        if (modelID.indexOf(DataHelper.Wall_switch_triple) == 0) {
            result = c.getResources().getString(R.string.Wall_switch_triple);
        }
        if (modelID.indexOf(DataHelper.Wall_switch_triple) == 0) {
            result = c.getResources().getString(R.string.Wall_switch_triple);
        }
        if (modelID.indexOf(DataHelper.Power_detect_wall) == 0) {
            result = c.getResources().getString(R.string.Power_detect_wall);
        }
        if (modelID.indexOf(DataHelper.Curtain_control_switch) == 0) {
            result = c.getResources()
                    .getString(R.string.Curtain_control_switch);
        }

        if (modelID.indexOf(DataHelper.Indoor_temperature_sensor) == 0) {
            result = c.getResources().getString(
                    R.string.Indoor_temperature_sensor);
        }
        if (modelID.indexOf(DataHelper.Light_Sensor) == 0) {
            result = c.getResources().getString(R.string.Light_Sensor);
        }
        if (modelID.indexOf(DataHelper.Multi_key_remote_control) == 0) {
            result = c.getResources().getString(
                    R.string.Multi_key_remote_control);
        }
        if (modelID.indexOf(DataHelper.Doorbell_button) == 0) {
            result = c.getResources().getString(R.string.Doorbell_button);
        }
        if (modelID.indexOf(DataHelper.Switch_Module_Single) == 0) {
            result = c.getResources().getString(R.string.Switch_Module_Single);
        }
        if (modelID.indexOf(DataHelper.Energy_detection_dimming_module) == 0) {
            result = c.getResources().getString(
                    R.string.Energy_detection_dimming_module);
        }
        if (modelID.indexOf(DataHelper.Pro_RF) == 0) {
            result = c.getResources().getString(R.string.Pro_RF);
        }
        if (modelID.indexOf(DataHelper.RS232_adapter) == 0) {
            result = c.getResources().getString(R.string.RS232_adapter);
        }
        if (modelID.indexOf(DataHelper.Power_detect_socket) == 0) {
            result = c.getResources().getString(R.string.Power_detect_socket);
        }
        if (modelID.indexOf(DataHelper.Infrared_controller) == 0) {
            result = c.getResources().getString(R.string.Infrared_controller);
        }
        return result;

	}

	public static Set<String> getRegions(Context c, DataHelper dh) {
		List<DevicesModel> listDevicesModel = new ArrayList<DevicesModel>();
		Set<String> region = new HashSet<String>();

		SQLiteDatabase db = dh.getSQLiteDatabase();
		listDevicesModel = dh.queryForList(db, DataHelper.DEVICES_TABLE, null,
				null, null, null, null, null, null);

		for (int m = 0; m < listDevicesModel.size(); m++) {
			String mRegion = listDevicesModel.get(m).getmDeviceRegion();
			if (!region.contains(mRegion)) {
				region.add(mRegion);
			}
		}
		return region;
	}

	public static List<SimpleDevicesModel> getDevices(Context c, DataHelper dh,
			String[] args, String where, boolean b) {
		HashMap<String, Integer> mMap = new HashMap<String, Integer>();
		List<String> sList = new ArrayList<String>();
		SimpleDevicesModel mSimpleDevicesModel;
		List<DevicesModel> listDevicesModel = new ArrayList<DevicesModel>();
		List<SimpleDevicesModel> list = new ArrayList<SimpleDevicesModel>();

		SQLiteDatabase db = dh.getSQLiteDatabase();
		listDevicesModel = dh.queryForList(db, DataHelper.DEVICES_TABLE, null,
				where, args, null, null, null, null);

		if (b) {
			for (int m = 0; m < listDevicesModel.size(); m++) {
				DevicesModel mDevicesModel = listDevicesModel.get(m);
				if (Integer.parseInt(mDevicesModel.getmDeviceId()) != DataHelper.ON_OFF_SWITCH_DEVICETYPE) {
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
					if (mDevicesModel.getmUserDefineName() == null
							|| mDevicesModel.getmUserDefineName().trim()
									.equals("")) {
						mSimpleDevicesModel
								.setmUserDefineName(getDefaultUserDefinname(c,
										mSimpleDevicesModel.getmModelId()));
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
									mSimpleDevicesModel.getmModelId()));
				} else {
					mSimpleDevicesModel.setmUserDefineName(mDevicesModel
							.getmUserDefineName());
				}
				list.add(mSimpleDevicesModel);
			}
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
