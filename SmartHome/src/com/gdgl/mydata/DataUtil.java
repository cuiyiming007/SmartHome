package com.gdgl.mydata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gdgl.app.ApplicationController;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.Callback.CallbackWarnMessage;
import com.gdgl.mydata.Region.Room;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;

public class DataUtil {

	// DevicesModel属性设置
	// 设置cluster
	public static Map<String, String> clusterMap;
	static {
		DataUtil.clusterMap = new HashMap<String, String>();
		DataUtil.clusterMap.put("ZB02A", "0006OUT"); // 墙面开关（单键）
		DataUtil.clusterMap.put("ZB02B", "0006OUT"); // 墙面开关（双键）
		DataUtil.clusterMap.put("ZB02C", "0006OUT"); // 墙面开关（三键）
		DataUtil.clusterMap.put("Z311J", "0006OUT"); // 门窗感应开关
		DataUtil.clusterMap.put("Z302J", "0006OUT"); // 门窗感应开关
		DataUtil.clusterMap.put("ZB02F", "0008OUT"); // 调光开关
		DataUtil.clusterMap.put("Z312", "0502OUT"); // 门铃按键

		DataUtil.clusterMap.put("ZA10", "0006IN"); // 无线智能阀门开关
		DataUtil.clusterMap.put("Z805B", "0006IN"); // 开关模块（单路）
		DataUtil.clusterMap.put("Z806", "0006IN"); // 开关模块（双路）
		DataUtil.clusterMap.put("Z809A", "0006IN"); // 电能检测插座
		DataUtil.clusterMap.put("Z811", "0006IN"); // 开关模块（四路）
		DataUtil.clusterMap.put("Z816H", "0006IN"); // 中规电能检测墙面插座
		DataUtil.clusterMap.put("Z817B", "0008IN"); // 吸顶电能检测调光模块
		DataUtil.clusterMap.put("Z815N", "0008IN"); // 幕帘控制开关
		DataUtil.clusterMap.put("Z602A", "0502IN"); // 警报器
	}

	// 设置cluster
	public static String getClusterIdByDeviceid_Modelid(String model_id,
			String Ep) {
		if (model_id.indexOf("ZB02A") == 0) {
			return clusterMap.get("ZB02A");
		} else if (model_id.indexOf("ZB02B") == 0) {
			return clusterMap.get("ZB02B");
		} else if (model_id.indexOf("ZB02C") == 0) {
			return clusterMap.get("ZB02C");
		} else if (model_id.indexOf("Z311J") == 0 && Ep.equals("01")) {
			return clusterMap.get("Z311J");
		} else if (model_id.indexOf("Z302J") == 0 && Ep.equals("01")) {
			return clusterMap.get("Z302J");
		} else if (model_id.indexOf("ZB02F") == 0) {
			return clusterMap.get("ZB02F");
		} else if (model_id.indexOf("Z312") == 0) {
			return clusterMap.get("Z312");
		} else if (model_id.indexOf("ZA10") == 0) {
			return clusterMap.get("ZA10");
		} else if (model_id.indexOf("Z805B") == 0) {
			return clusterMap.get("Z805B");
		} else if (model_id.indexOf("Z806") == 0) {
			return clusterMap.get("Z806");
		} else if (model_id.indexOf("Z809A") == 0) {
			return clusterMap.get("Z809A");
		} else if (model_id.indexOf("Z811") == 0) {
			return clusterMap.get("Z811");
		} else if (model_id.indexOf("Z816H") == 0) {
			return clusterMap.get("Z816H");
		} else if (model_id.indexOf("Z817B") == 0) {
			return clusterMap.get("Z817B");
		} else if (model_id.indexOf("Z815N") == 0) {
			return clusterMap.get("Z815N");
		} else if (model_id.indexOf("Z602A") == 0) {
			return clusterMap.get("Z602A");
		} else {
			return "";
		}
	}

	// 设置默认设备分类
	public static int getDefaultDevicesSort(int deviceId, String modelId) {
		int result = 6;
		if (deviceId == DataHelper.ON_OFF_SWITCH_DEVICETYPE) {
			result = UiUtils.SWITCH_DEVICE;
		} else if (deviceId == DataHelper.ON_OFF_OUTPUT_DEVICETYPE) {
			if (modelId.indexOf(DataHelper.Wireless_Intelligent_valve_switch) == 0) {
				result = UiUtils.SECURITY_CONTROL;
			} else {
				result = UiUtils.ELECTRICAL_MANAGER;
			}
		} else if (deviceId == DataHelper.REMOTE_CONTROL_DEVICETYPE) {
			result = UiUtils.SWITCH_DEVICE;
		} else if (deviceId == DataHelper.COMBINED_INTERFACE_DEVICETYPE) {
			result = UiUtils.SECURITY_CONTROL;
		} else if (deviceId == DataHelper.RANGE_EXTENDER_DEVICETYPE) {
			result = UiUtils.ELECTRICAL_MANAGER;
		} else if (deviceId == DataHelper.MAINS_POWER_OUTLET_DEVICETYPE) {
			// if(modelId.indexOf(DataHelper.Switch_Module_Single)==0) {
			// result = UiUtils.LIGHTS_MANAGER;
			// } else {
			result = UiUtils.ELECTRICAL_MANAGER;
			// }
		} else if (deviceId == DataHelper.ON_OFF_LIGHT_DEVICETYPE) {
			result = UiUtils.ELECTRICAL_MANAGER;
		} else if (deviceId == DataHelper.DIMEN_LIGHTS_DEVICETYPE) {
			result = UiUtils.ELECTRICAL_MANAGER;
		} else if (deviceId == DataHelper.DIMEN_SWITCH_DEVICETYPE) {
			result = UiUtils.SWITCH_DEVICE;
		} else if (deviceId == DataHelper.LIGHT_SENSOR_DEVICETYPE) {
			result = UiUtils.ENVIRONMENTAL_CONTROL;
		} else if (deviceId == DataHelper.SHADE_DEVICETYPE) {
			result = UiUtils.ELECTRICAL_MANAGER;
		} else if (deviceId == DataHelper.TEMPTURE_SENSOR_DEVICETYPE) {
			result = UiUtils.ENVIRONMENTAL_CONTROL;
		} else if (deviceId == DataHelper.IAS_ACE_DEVICETYPE) {
			result = UiUtils.SWITCH_DEVICE;
		} else if (deviceId == DataHelper.IAS_ZONE_DEVICETYPE) {
			result = UiUtils.SECURITY_CONTROL;
		} else if (deviceId == DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE) {
			result = UiUtils.SECURITY_CONTROL;
		}
		return result;
	}

	// 设置默认设备图片
		public static int getDefaultDevicesIcon(int deviceId, String modelId) {
			int result = 0;
			switch (deviceId) {
			case DataHelper.ON_OFF_SWITCH_DEVICETYPE:
				if (modelId.indexOf(DataHelper.Wall_switch_touch) == 0) { // ZigBee墙面开关（单键）
					result = R.drawable.ui_others_singlestroke;
				}
				if (modelId.indexOf(DataHelper.Wall_switch_double) == 0) { // ZigBee墙面开关（双键）
					result = R.drawable.ui_others_doublekeystroke;
				}
				if (modelId.indexOf(DataHelper.Wall_switch_triple) == 0) { // ZigBee墙面开关（三键）
					result = R.drawable.ui_others_triplekeystroke;
				}
				if (modelId.indexOf(DataHelper.Doors_and_windows_sensor_switch) == 0) { // 门窗感应开关
					result = R.drawable.ui_securitycontrol_doormagnetic;
				}
				break;
			case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
				if (modelId.indexOf(DataHelper.Wireless_Intelligent_valve_switch) == 0) { // //
																							// 无线智能阀门开关
					result = R.drawable.ui_securitycontrol_valveswitch;
				}
				if (modelId.indexOf(DataHelper.Switch_Module_Double) == 0) { // ZigBee开关模块（双路）
					result = R.drawable.ui_lightmanage_switchmodule;
				}
				break;
			case DataHelper.REMOTE_CONTROL_DEVICETYPE:
				result = R.drawable.tv_control;
				break;
			case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
				result = R.drawable.ui2_device_gateway;
				break;
			case DataHelper.RANGE_EXTENDER_DEVICETYPE:
				result = R.drawable.ui_electricalcontrol_infraredcontroller;
				break;
			case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
				if (modelId.startsWith(DataHelper.Switch_Module_Single)) { // ZigBee开关模块（单路）
					result = R.drawable.ui_lightmanage_switchmodule;
				} else if (modelId.startsWith(DataHelper.Power_detect_socket)) { // ZigBee开关模块（单路）
					result = R.drawable.ui2_device_switchmodule;
				} else if(modelId.startsWith(DataHelper.Power_detect_wall)) { // ZigBee开关模块（单路）
					result = R.drawable.ui2_device_electricalsocket;
				} else {
					result = R.drawable.ui_electricalcontrol_electricalsocket;
				}
				break;
			case DataHelper.ON_OFF_LIGHT_DEVICETYPE:
				result = R.drawable.ui_lightmanage_switchmodule;
				break;
			case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
				result = R.drawable.ui_lightmanage_lightdimming;
				break;
			case DataHelper.DIMEN_SWITCH_DEVICETYPE:
				result = R.drawable.ui_others_slidingblock;
				break;
			case DataHelper.LIGHT_SENSOR_DEVICETYPE:
				result = R.drawable.ui_environmentalcontrol_lightsensor;
				break;
			case DataHelper.SHADE_DEVICETYPE:
				result = R.drawable.ui_electricalcontrol_curtaincontrol;
				break;
			case DataHelper.TEMPTURE_SENSOR_DEVICETYPE:
				result = R.drawable.ui2_device_temperaturesensor;
				break;
			case DataHelper.IAS_ACE_DEVICETYPE:
				result = R.drawable.ui_others_doorbell;
				break;
			case DataHelper.IAS_ZONE_DEVICETYPE:
				if (modelId.indexOf(DataHelper.Motion_Sensor) == 0) { // ZigBee动作感应器
					result = R.drawable.ui2_device_motionsensor;
				}
				if (modelId.indexOf(DataHelper.Magnetic_Window) == 0) { // ZigBee窗磁
					result = R.drawable.ui2_device_windowmagnetic;
				}
				if (modelId.indexOf(DataHelper.Doors_and_windows_sensor_switch) == 0) { // 门窗感应开关
					result = R.drawable.ui_securitycontrol_doormagnetic;
				}
				if (modelId.startsWith(DataHelper.Emergency_Button) // ZigBee紧急按钮
						|| modelId.startsWith(DataHelper.Emergency_Button_On_Wall)) { // ZigBee墙面紧急按钮
					result = R.drawable.ui_securitycontrol_emergencybutton;
				}
				if (modelId.indexOf(DataHelper.Smoke_Detectors) == 0) { // 烟雾感应器
					result = R.drawable.ui2_device_detectorsmoke;
				}
				if (modelId.indexOf(DataHelper.Combustible_Gas_Detector_Gas) == 0) { // 可燃气体探测器（煤气)器
					result = R.drawable.ui_securitycontrol_detectorgas;
				}
				if (modelId.indexOf(DataHelper.Combustible_Gas_Detector_CO) == 0) { // 可燃气体探测器（一氧化碳)
					result = R.drawable.ui_securitycontrol_detectorco;
				}
				if (modelId
						.indexOf(DataHelper.Combustible_Gas_Detector_Natural_gas) == 0) { // 可燃气体探测器（天然气)
					result = R.drawable.ui_securitycontrol_detectornaturalgas;
				}
				break;
			case DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE:
				result = R.drawable.ui2_device_alarm;
				break;
			default:
				result = R.drawable.ui_lightmanage_switchmodule;
				break;
			}
			if (result == 0) {
				result = R.drawable.ui_securitycontrol_alarm;
			}
			return result;
		}
		
	// 设置默认设备小图片
	public static int getDefaultDevicesSmallIcon(int deviceId, String modelId) {
		int result = 0;
		switch (deviceId) {
		case DataHelper.ON_OFF_SWITCH_DEVICETYPE:
			if (modelId.indexOf(DataHelper.Wall_switch_touch) == 0) { // ZigBee墙面开关（单键）
				result = R.drawable.ui_others_singlestroke;
			}
			if (modelId.indexOf(DataHelper.Wall_switch_double) == 0) { // ZigBee墙面开关（双键）
				result = R.drawable.ui_others_doublekeystroke;
			}
			if (modelId.indexOf(DataHelper.Wall_switch_triple) == 0) { // ZigBee墙面开关（三键）
				result = R.drawable.ui_others_triplekeystroke;
			}
			if (modelId.indexOf(DataHelper.Doors_and_windows_sensor_switch) == 0) { // 门窗感应开关
				result = R.drawable.ui_securitycontrol_doormagnetic;
			}
			break;
		case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
			if (modelId.indexOf(DataHelper.Wireless_Intelligent_valve_switch) == 0) { // //
																						// 无线智能阀门开关
				result = R.drawable.ui_securitycontrol_valveswitch;
			}
			if (modelId.indexOf(DataHelper.Switch_Module_Double) == 0) { // ZigBee开关模块（双路）
				result = R.drawable.ui_lightmanage_switchmodule;
			}
			break;
		case DataHelper.REMOTE_CONTROL_DEVICETYPE:
			result = R.drawable.tv_control;
			break;
		case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
			result = R.drawable.ui2_device_gateway_small;
			break;
		case DataHelper.RANGE_EXTENDER_DEVICETYPE:
			result = R.drawable.ui_electricalcontrol_infraredcontroller;
			break;
		case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
			if (modelId.startsWith(DataHelper.Switch_Module_Single)) { // ZigBee开关模块（单路）
				result = R.drawable.ui_lightmanage_switchmodule;
			} else if (modelId.startsWith(DataHelper.Power_detect_socket)) { // ZigBee开关模块（单路）
				result = R.drawable.ui2_device_switchmodule_small;
			} else if(modelId.startsWith(DataHelper.Power_detect_wall)) { // ZigBee开关模块（单路）
				result = R.drawable.ui2_device_electricalsocket_small;
			} else {
				result = R.drawable.ui_electricalcontrol_electricalsocket;
			}
			break;
		case DataHelper.ON_OFF_LIGHT_DEVICETYPE:
			result = R.drawable.ui_lightmanage_switchmodule;
			break;
		case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
			result = R.drawable.ui_lightmanage_lightdimming;
			break;
		case DataHelper.DIMEN_SWITCH_DEVICETYPE:
			result = R.drawable.ui_others_slidingblock;
			break;
		case DataHelper.LIGHT_SENSOR_DEVICETYPE:
			result = R.drawable.ui_environmentalcontrol_lightsensor;
			break;
		case DataHelper.SHADE_DEVICETYPE:
			result = R.drawable.ui_electricalcontrol_curtaincontrol;
			break;
		case DataHelper.TEMPTURE_SENSOR_DEVICETYPE:
			result = R.drawable.ui2_device_temperaturesensor_small;
			break;
		case DataHelper.IAS_ACE_DEVICETYPE:
			result = R.drawable.ui_others_doorbell;
			break;
		case DataHelper.IAS_ZONE_DEVICETYPE:
			if (modelId.indexOf(DataHelper.Motion_Sensor) == 0) { // ZigBee动作感应器
				result = R.drawable.ui2_device_motionsensor_small;
			}
			if (modelId.indexOf(DataHelper.Magnetic_Window) == 0) { // ZigBee窗磁
				result = R.drawable.ui2_device_windowmagnetic_small;
			}
			if (modelId.indexOf(DataHelper.Doors_and_windows_sensor_switch) == 0) { // 门窗感应开关
				result = R.drawable.ui_securitycontrol_doormagnetic;
			}
			if (modelId.startsWith(DataHelper.Emergency_Button) // ZigBee紧急按钮
					|| modelId.startsWith(DataHelper.Emergency_Button_On_Wall)) { // ZigBee墙面紧急按钮
				result = R.drawable.ui_securitycontrol_emergencybutton;
			}
			if (modelId.indexOf(DataHelper.Smoke_Detectors) == 0) { // 烟雾感应器
				result = R.drawable.ui2_device_detectorsmoke_small;
			}
			if (modelId.indexOf(DataHelper.Combustible_Gas_Detector_Gas) == 0) { // 可燃气体探测器（煤气)器
				result = R.drawable.ui_securitycontrol_detectorgas;
			}
			if (modelId.indexOf(DataHelper.Combustible_Gas_Detector_CO) == 0) { // 可燃气体探测器（一氧化碳)
				result = R.drawable.ui_securitycontrol_detectorco;
			}
			if (modelId
					.indexOf(DataHelper.Combustible_Gas_Detector_Natural_gas) == 0) { // 可燃气体探测器（天然气)
				result = R.drawable.ui_securitycontrol_detectornaturalgas;
			}
			break;
		case DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE:
			result = R.drawable.ui2_device_alarm_samll;
			break;
		default:
			result = R.drawable.ui_lightmanage_switchmodule;
			break;
		}
		if (result == 0) {
			result = R.drawable.ui_securitycontrol_alarm;
		}
		return result;
	}
	
	// 设置默认设备控制图片
		public static int getDefaultDevicesControlIcon(int deviceId, String modelId) {
			int result = 0;
			switch (deviceId) {
			case DataHelper.ON_OFF_SWITCH_DEVICETYPE:
				if (modelId.indexOf(DataHelper.Wall_switch_touch) == 0) { // ZigBee墙面开关（单键）
					result = R.drawable.ui_others_singlestroke;
				}
				if (modelId.indexOf(DataHelper.Wall_switch_double) == 0) { // ZigBee墙面开关（双键）
					result = R.drawable.ui_others_doublekeystroke;
				}
				if (modelId.indexOf(DataHelper.Wall_switch_triple) == 0) { // ZigBee墙面开关（三键）
					result = R.drawable.ui_others_triplekeystroke;
				}
				if (modelId.indexOf(DataHelper.Doors_and_windows_sensor_switch) == 0) { // 门窗感应开关
					result = R.drawable.ui_securitycontrol_doormagnetic;
				}
				break;
			case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
				if (modelId.indexOf(DataHelper.Wireless_Intelligent_valve_switch) == 0) { // //
																							// 无线智能阀门开关
					result = R.drawable.ui_securitycontrol_valveswitch;
				}
				if (modelId.indexOf(DataHelper.Switch_Module_Double) == 0) { // ZigBee开关模块（双路）
					result = R.drawable.ui_lightmanage_switchmodule;
				}
				break;
			case DataHelper.REMOTE_CONTROL_DEVICETYPE:
				result = R.drawable.tv_control;
				break;
			case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
				result = R.drawable.ui2_device_gateway_control_style;
				break;
			case DataHelper.RANGE_EXTENDER_DEVICETYPE:
				result = R.drawable.ui_electricalcontrol_infraredcontroller;
				break;
			case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
				if (modelId.startsWith(DataHelper.Switch_Module_Single)) { // ZigBee开关模块（单路）
					result = R.drawable.ui_lightmanage_switchmodule;
				} else if (modelId.startsWith(DataHelper.Power_detect_socket)) { // ZigBee开关模块（单路）
					result = R.drawable.ui2_device_switchmodule_control_style;
				} else if(modelId.startsWith(DataHelper.Power_detect_wall)) { // ZigBee开关模块（单路）
					result = R.drawable.ui2_device_electricalsocket_control_style;
				} else {
					result = R.drawable.ui_electricalcontrol_electricalsocket;
				}
				break;
			case DataHelper.ON_OFF_LIGHT_DEVICETYPE:
				result = R.drawable.ui_lightmanage_switchmodule;
				break;
			case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
				result = R.drawable.ui_lightmanage_lightdimming;
				break;
			case DataHelper.DIMEN_SWITCH_DEVICETYPE:
				result = R.drawable.ui_others_slidingblock;
				break;
			case DataHelper.LIGHT_SENSOR_DEVICETYPE:
				result = R.drawable.ui_environmentalcontrol_lightsensor;
				break;
			case DataHelper.SHADE_DEVICETYPE:
				result = R.drawable.ui_electricalcontrol_curtaincontrol;
				break;
			case DataHelper.TEMPTURE_SENSOR_DEVICETYPE:
				result = R.drawable.ui2_device_temperaturesensor_control_style;
				break;
			case DataHelper.IAS_ACE_DEVICETYPE:
				result = R.drawable.ui_others_doorbell;
				break;
			case DataHelper.IAS_ZONE_DEVICETYPE:
				if (modelId.indexOf(DataHelper.Motion_Sensor) == 0) { // ZigBee动作感应器
					result = R.drawable.ui2_device_motionsensor_control_style;
				}
				if (modelId.indexOf(DataHelper.Magnetic_Window) == 0) { // ZigBee窗磁
					result = R.drawable.ui2_device_windowmagnetic_control_style;
				}
				if (modelId.indexOf(DataHelper.Doors_and_windows_sensor_switch) == 0) { // 门窗感应开关
					result = R.drawable.ui_securitycontrol_doormagnetic;
				}
				if (modelId.startsWith(DataHelper.Emergency_Button) // ZigBee紧急按钮
						|| modelId.startsWith(DataHelper.Emergency_Button_On_Wall)) { // ZigBee墙面紧急按钮
					result = R.drawable.ui_securitycontrol_emergencybutton;
				}
				if (modelId.indexOf(DataHelper.Smoke_Detectors) == 0) { // 烟雾感应器
					result = R.drawable.ui2_device_detectorsmoke_control_style;
				}
				if (modelId.indexOf(DataHelper.Combustible_Gas_Detector_Gas) == 0) { // 可燃气体探测器（煤气)器
					result = R.drawable.ui_securitycontrol_detectorgas;
				}
				if (modelId.indexOf(DataHelper.Combustible_Gas_Detector_CO) == 0) { // 可燃气体探测器（一氧化碳)
					result = R.drawable.ui_securitycontrol_detectorco;
				}
				if (modelId
						.indexOf(DataHelper.Combustible_Gas_Detector_Natural_gas) == 0) { // 可燃气体探测器（天然气)
					result = R.drawable.ui_securitycontrol_detectornaturalgas;
				}
				break;
			case DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE:
				result = R.drawable.ui2_device_alarm_control_on;
				break;
			default:
				result = R.drawable.ui_lightmanage_switchmodule;
				break;
			}
			if (result == 0) {
				result = R.drawable.ui_securitycontrol_alarm;
			}
			return result;
		}

	// 设置默认设备名称
	public static String getDefaultDevicesName(Context c, String modelID,
			String ep) {
		String result = c.getResources().getString(R.string.Unknown_Device);

		if (modelID.indexOf(DataHelper.Motion_Sensor) == 0) { // ZigBee动作感应器
			result = c.getResources().getString(R.string.Motion_Sensor);
		}
		if (modelID.indexOf(DataHelper.Magnetic_Window) == 0) { // ZigBee窗磁
			result = c.getResources().getString(R.string.Magnetic_Window);
		}
		if (modelID.indexOf(DataHelper.Emergency_Button) == 0) { // ZigBee紧急按钮
			result = c.getResources().getString(R.string.Emergency_Button);
		}
		if (modelID.indexOf(DataHelper.Emergency_Button_On_Wall) == 0) { // ZigBee墙面紧急按钮
			result = c.getResources().getString(
					R.string.Emergency_Button_On_Wall);
		}
		if (modelID.indexOf(DataHelper.Doors_and_windows_sensor_switch) == 0) { // 门窗感应开关
			if (ep.equals("01")) {
				result = c.getResources().getString(
						R.string.Doors_and_windows_sensor_switch_01);
			}
			if (ep.equals("02")) {
				result = c.getResources().getString(
						R.string.Doors_and_windows_sensor_switch_02);
			}

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
		if (modelID.indexOf(DataHelper.Switch_Module_Double) == 0) { // ZigBee开关模块（双路）
			if (ep.equals("01")) {
				result = c.getResources().getString(
						R.string.Switch_Module_Double_1);
			}
			if (ep.equals("02")) {
				result = c.getResources().getString(
						R.string.Switch_Module_Double_2);
			}
		}
		if (modelID.indexOf(DataHelper.Switch_Module_Quadruple) == 0) { // ZigBee开关模块（四路）
			if (ep.equals("01")) {
				result = c.getResources().getString(
						R.string.Switch_Module_Quadruple_1);
			}
			if (ep.equals("02")) {
				result = c.getResources().getString(
						R.string.Switch_Module_Quadruple_2);
			}
			if (ep.equals("03")) {
				result = c.getResources().getString(
						R.string.Switch_Module_Quadruple_3);
			}
			if (ep.equals("04")) {
				result = c.getResources().getString(
						R.string.Switch_Module_Quadruple_4);
			}
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

	// 设置默认设备优先级
	public static int getDefaultDevicesPriority(String modelID) {
		int result = 500;

		if (modelID.indexOf(DataHelper.One_key_operator) == 0) { // 安防中心
			result = 0;
		}
		if (modelID.indexOf(DataHelper.Siren) == 0) { // ZigBee警报器
			result = 1;
		}
		if (modelID.indexOf(DataHelper.Doors_and_windows_sensor_switch) == 0) { // 门窗感应开关
			result = 2;
		}
		if (modelID.indexOf(DataHelper.Magnetic_Window) == 0) { // ZigBee窗磁
			result = 3;
		}
		if (modelID.indexOf(DataHelper.Motion_Sensor) == 0) { // ZigBee动作感应器
			result = 4;
		}
		if (modelID.indexOf(DataHelper.Wireless_Intelligent_valve_switch) == 0) { // 无线智能阀门开关
			result = 5;
		}
		if (modelID.indexOf(DataHelper.Smoke_Detectors) == 0) { // 烟雾感应器
			result = 6;
		}
		if (modelID.indexOf(DataHelper.Combustible_Gas_Detector_Gas) == 0) { // 可燃气体探测器（煤气）
			result = 7;
		}
		if (modelID.indexOf(DataHelper.Combustible_Gas_Detector_CO) == 0) { // 可燃气体探测器（一氧化碳）
			result = 8;
		}
		if (modelID.indexOf(DataHelper.Combustible_Gas_Detector_Natural_gas) == 0) { // 可燃气体探测器（天然气）
			result = 9;
		}
		if (modelID.indexOf(DataHelper.Emergency_Button) == 0) { // ZigBee紧急按钮
			result = 10;
		}
		if (modelID.indexOf(DataHelper.Emergency_Button_On_Wall) == 0) { // ZigBee墙面紧急按钮
			result = 11;
		}

		if (modelID.indexOf(DataHelper.Power_detect_socket) == 0) { // ZigBee电能检测插座
			result = 100;
		}
		if (modelID.indexOf(DataHelper.Power_detect_wall) == 0) { // 中规电能检测墙面插座
			result = 101;
		}
		if (modelID.indexOf(DataHelper.Curtain_control_switch) == 0) { // ZigBee幕帘控制开关
			result = 102;
		}
		if (modelID.indexOf(DataHelper.Switch_Module_Single) == 0) { // ZigBee开关模块（单路）
			result = 103;
		}
		if (modelID.indexOf(DataHelper.Switch_Module_Double) == 0) { // ZigBee开关模块（双路）
			result = 104;
		}
		if (modelID.indexOf(DataHelper.Switch_Module_Quadruple) == 0) { // ZigBee开关模块（四路）
			result = 105;
		}
		if (modelID.indexOf(DataHelper.Energy_detection_dimming_module) == 0) { // 吸顶电能检测调光模块
			result = 106;
		}
		if (modelID.indexOf(DataHelper.Infrared_controller) == 0) { // ZigBee红外控制器
			result = 107;
		}

		if (modelID.indexOf(DataHelper.Indoor_temperature_sensor) == 0) { // ZigBee室内型温湿度感应器
			result = 200;
		}
		if (modelID.indexOf(DataHelper.Light_Sensor) == 0) { // ZigBee光线感应器
			result = 201;
		}

		if (modelID.indexOf(DataHelper.Wall_switch_touch) == 0) { // ZigBee墙面开关（单键）
			result = 300;
		}
		if (modelID.indexOf(DataHelper.Wall_switch_double) == 0) { // ZigBee墙面开关（双键）
			result = 301;
		}
		if (modelID.indexOf(DataHelper.Wall_switch_triple) == 0) { // ZigBee墙面开关（三键）
			result = 302;
		}
		if (modelID.indexOf(DataHelper.Dimmer_Switch) == 0) { // ZigBee调光开关
			result = 303;
		}
		if (modelID.indexOf(DataHelper.Doorbell_button) == 0) { // ZigBee门铃按键
			result = 304;
		}

		return result;
	}
	
	//Timing and Scene ActionParam type
	public static int getTimingSceneActionParamType(int deviceId) {
		int type = 3;
		if(deviceId == DataHelper.COMBINED_INTERFACE_DEVICETYPE) {
			type = 2;
		}
		if(deviceId == DataHelper.IAS_ZONE_DEVICETYPE) {
			type = 1;
		}
		return type;
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
				DataHelper.DEVICES_TABLE, null, where, args, null, null,
				DevicesModel.DEVICE_PRIORITY, null);
		// if (null != listDevicesModel && listDevicesModel.size() > 0) {
		// for (DevicesModel devicesModel : listDevicesModel) {
		// if (null == devicesModel.getmDefaultDeviceName()
		// || devicesModel.getmDefaultDeviceName().trim().equals("")) {
		// devicesModel.setmDefaultDeviceName(getDefaultDevicesName(c,
		// devicesModel.getmModelId(), devicesModel.getmEP()));
		// }
		// }
		// }
		dh.getSQLiteDatabase().close();
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
			mSimpleDevicesModel.setmDeviceId(mDevicesModel.getmDeviceId());
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
			if (mDevicesModel.getmDefaultDeviceName() == null
					|| mDevicesModel.getmDefaultDeviceName().trim().equals("")) {
				mSimpleDevicesModel
						.setmDefaultDeviceName(getDefaultDevicesName(c,
								mSimpleDevicesModel.getmModelId(),
								mSimpleDevicesModel.getmEP()));
			} else {
				mSimpleDevicesModel.setmDefaultDeviceName(mDevicesModel
						.getmDefaultDeviceName());
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
				null, where, args, null, null, DevicesModel.DEVICE_PRIORITY, null);
		dh.close(db);
//		if (null != listDevicesModel && listDevicesModel.size() > 0) {
//			for (DevicesModel devicesModel : listDevicesModel) {
//				if (null == devicesModel.getmDefaultDeviceName()
//						|| devicesModel.getmDefaultDeviceName().trim()
//								.equals("")) {
//					devicesModel.setmDefaultDeviceName(getDefaultDevicesName(c,
//							devicesModel.getmModelId(), devicesModel.getmEP()));
//				}
//			}
//		}

		return listDevicesModel;

	}

	public static List<CallbackWarnMessage> getWarmMessage(Context c,
			DataHelper dh) {
		getFromSharedPreferences.setsharedPreferences(ApplicationController
				.getInstance());
		String usename = getFromSharedPreferences.getName();
		List<CallbackWarnMessage> mList = new ArrayList<CallbackWarnMessage>();
		Cursor cursor = null;
		SQLiteDatabase db = dh.getSQLiteDatabase();
		cursor = db.query(DataHelper.MESSAGE_TABLE, null,
				CallbackWarnMessage.USENAME + "='" + usename + "'", null, null,
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
	
	public static Room getRoomByID(String id, DataHelper dh,
			SQLiteDatabase db) {
		String where = " _id=? ";
		String[] args = { id + "" };
		List<Room> mList = dh.queryForRoomList(db,
				DataHelper.ROOMINFO_TABLE, null, where, args, null, null, null,
				null);
		if (null != mList && mList.size() > 0) {
			return mList.get(0);
		}
		return null;
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
}
