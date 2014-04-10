package com.gdgl.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;

import com.gdgl.activity.CurtainsControlFragment;
import com.gdgl.activity.LightsControlFragment;
import com.gdgl.activity.SwitchControlFragment;
import com.gdgl.model.DevicesModel;
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

	public static int[] GONENG_ICON = { R.drawable.lights, R.drawable.outlet,
			R.drawable.emerge, R.drawable.co, R.drawable.huanbao,
			R.drawable.more };

	// region

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

    // icon
    public static final int LIGHTS_ON_ICON = R.drawable.l_on_forreg;
    public static final int LIGHTS_OFF_ICON = R.drawable.l_off_forreg;
    public static final int DIMEN_LIGHT_ICON = R.drawable.seek_light;

    public static final int SWITCH_ICON = R.drawable.resize_switch;
    public static final int CURTAINS_ICON = R.drawable.curtain_controller_small;

    public static final int LIGHTS_MANAGER = 0;
    public static final int ELECTRICAL_MANAGER = 1;
    
    public static final int DEVICES_MANAGER = 2;

	public static int getDevicesSmallIcon(int type) {
		int result = 0;
		switch (type) {
		case DataHelper.DIMEN_LIGHTS:
			result = DIMEN_LIGHT_ICON;
			break;
		case DataHelper.ON_OFF_SWITCH:
			result = SWITCH_ICON;
			break;
		case DataHelper.DIMEN_SWITCH:
			result = SWITCH_ICON;
			break;
		case DataHelper.LIGHT_SENSOR:
			result = LIGHTS_ON_ICON;
			break;
		case DataHelper.COMBINED_INTERFACE:
			result = CURTAINS_ICON;
			break;
		default:
			result = CURTAINS_ICON;
			break;
		}
		return result;
	}

    public static int getLightsSmallIcon(boolean state) {
        if (state) {
            return LIGHTS_ON_ICON;
        }
        return LIGHTS_OFF_ICON;
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

//	public static int[] LIGHT_MANAGER_IMAGES = { R.drawable.img0001,
//			R.drawable.img0030 };
//	public static String[] LIGHT_MANAGER_TAGS = { "普通电灯", "可调电灯" };
//
//	public static int[] ELECTRICAL_MANAGER_IMAGES = { R.drawable.img0001,
//			R.drawable.img0030, R.drawable.img0100 };
//	public static String[] ELECTRICAL_MANAGER_TAGS = { "墙面开关", "窗帘", "红外转发器" };

	public static int[] DEVICES_MANAGER_IMAGES = { R.drawable.img0001,
			R.drawable.img0030, R.drawable.img0100, R.drawable.img0200,
			R.drawable.img0330 };
	public static String[] DEVICES_MANAGER_TAGS = { "照明管理", "电器控制", "安全防护",
			"环境监测", "节能" };

	public static int[] getDevicesManagerImage(int type) {
		int[] mInt = null;
		switch (type) {
//		case LIGHTS_MANAGER:
//			mInt = LIGHT_MANAGER_IMAGES;
//			break;
//		case ELECTRICAL_MANAGER:
//			mInt = ELECTRICAL_MANAGER_IMAGES;
//			break;
		case DEVICES_MANAGER:
			mInt = DEVICES_MANAGER_IMAGES;
			break;
		default:
			break;
		}
		return mInt;
	}

	public static String[] getDevicesManagerTag(int type) {
		String[] mString = null;
		switch (type) {
//		case LIGHTS:
//			mString = LIGHT_MANAGER_TAGS;
//			break;
//		case ELECTRICAL_MANAGER:
//			mString = ELECTRICAL_MANAGER_TAGS;
//			break;
		case DEVICES_MANAGER:
			mString = DEVICES_MANAGER_TAGS;
			break;
		default:
			break;
		}
		return mString;
	}

    public static Fragment getFragment(int type) {

		Fragment mFragment = null;
		switch (type) {
		case DataHelper.ON_OFF_SWITCH:
			mFragment = new SwitchControlFragment();
			break;
		case DataHelper.LIGHT_SENSOR:
			mFragment = new LightsControlFragment();
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
