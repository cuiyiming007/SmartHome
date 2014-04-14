package com.gdgl.util;

import android.app.Fragment;

import com.gdgl.activity.LightsControlFragment;
import com.gdgl.activity.SeekLightsControlFragment;
import com.gdgl.activity.SwitchControlFragment;
import com.gdgl.mydata.DataHelper;
import com.gdgl.smarthome.R;

public class UiUtils {

	public static final String SharedPreferences_SETTING_INFOS = "SmartHome";

	public static final String PWD = "Password";
	public static final String NAME = "Name";
	public static final String UID = "UserID";
	public static final String IS_REMERBER_PWD = "RemerberPwd";
	public static final String IS_AUTO_LOGIN = "AutoLogin";

	public static final String EMPTY_STR = "";

	public static final String REM_PWD_ACT = "remeber_pwd";
	public static final String N_REM_PWD_ACT = "not_remeber_pwd";

	public static final String AUTO_LOGIN_ACT = "AutoLogin";

	public static int ILLEGAI_UID = -1;



	public static final int PARLOR = 0; // 客厅
	public static final int MASTER_BEDROOM = 1; // 主卧
	public static final int STUDY = 2; // 书房
	public static final int KITCHEN = 3;
	public static final int CORRIDOR = 4; // 走廊
	public static final int BATHROOM = 5; // 洗手间

	// devices
	public static final int LIGHTS = 0; // 普通灯
	public static final int DIMEN_LIGHTS = 1; // 调光灯
	public static final int SWITCH = 2; // 开关
	public static final int CURTAINS = 3; // 窗帘

	public static final int LIGHTS_MANAGER = 0;
	public static final int ELECTRICAL_MANAGER = 1;
	public static final int SECURITY_CONTROL = 2;
	public static final int ENVIRONMENTAL_CONTROL = 4;
	public static final int ENERGY_CONSERVATION= 8;

	public static int getDevicesSmallIcon(int type) {
		int result = 0;
		switch (type) {
		case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
			result = R.drawable.seek_light;
			break;
		case DataHelper.ON_OFF_SWITCH_DEVICETYPE:
			result = R.drawable.resize_switch;
			break;
		case DataHelper.DIMEN_SWITCH_DEVICETYPE:
			result = R.drawable.dimmable_switch;
			break;
		case DataHelper.LIGHT_SENSOR_DEVICETYPE:
			result = R.drawable.l_on_forreg;
			break;
		case DataHelper.SHADE_DEVICETYPE:
			result = R.drawable.curtain;
			break;
		case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
			result = R.drawable.wall_socket;
			break;
		case DataHelper.IAS_ZONE_DEVICETYPE:
			result = R.drawable.detector;
			break;
		default:
			result = R.drawable.tempture;
			break;
		}
		return result;
	}

	public static int getLightsSmallIcon(boolean state) {
		if (state) {
			return R.drawable.l_on_forreg;
		}
		return R.drawable.l_off_forreg;
	}

	public static String getDevicesRegion(int type) {
		String result = "";
		switch (type) {
		case PARLOR:
			result = "客厅";
			break;
		case MASTER_BEDROOM:
			result = "主卧";
			break;
		case STUDY:
			result = "书房";
			break;
		case KITCHEN:
			result = "厨房";
			break;
		case CORRIDOR:
			result = "走廊";
			break;
		case BATHROOM:
			result = "洗手间";
			break;
		default:
			break;
		}
		return result;
	}

	public static int[] DEVICES_MANAGER_IMAGES = { R.drawable.light_manage,
			R.drawable.electrical_control, R.drawable.safe_control,
			R.drawable.huanjing_jiance, R.drawable.jieneng };
	public static String[] DEVICES_MANAGER_TAGS = { "照明管理", "电器控制", "安全防护",
			"环境监测", "节能" };

	public static int[] getImagResource(int type) {
		int[] mresult = null;
		switch (type) {
		case 0:
		case 1:
			mresult = DEVICES_MANAGER_IMAGES;
			break;
		default:
			break;
		}
		return mresult;
	}
	
	public static String[] getTagResource(int type) {
		String[] mresult = null;
		switch (type) {
		case 0:
		case 1:
			mresult = DEVICES_MANAGER_TAGS;
			break;
		default:
			break;
		}
		return mresult;
	}

	public static Fragment getFragment(int type) {

		Fragment mFragment = null;
		switch (type) {
		case DataHelper.ON_OFF_SWITCH_DEVICETYPE:
			mFragment = new SwitchControlFragment();
			break;
		case DataHelper.LIGHT_SENSOR_DEVICETYPE:
			mFragment = new LightsControlFragment();
			break;
		case DataHelper.DIMEN_SWITCH_DEVICETYPE:
			
		case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
			mFragment = new SeekLightsControlFragment();
			break;
		default:
			break;
		}
		return mFragment;

	}

	public static String formatResponseString(String response) {
		response = response.replaceAll("\n", "");
		response = response.replaceAll("\t", "");
		response = customString(response);
		return response;
	}

	public static String customString(String s) {
		s = s.substring(s.indexOf("{"), s.length() - 1);
		return s;
	}

}
