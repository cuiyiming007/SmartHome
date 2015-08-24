package com.gdgl.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.gdgl.activity.LockFragment;
import com.gdgl.activity.CurtainControlFragment;
import com.gdgl.activity.OutLetControlFragment;
import com.gdgl.activity.SeekLightsControlFragment;
import com.gdgl.activity.SwitchControlFragment;
import com.gdgl.activity.WarnningControlFragment;
import com.gdgl.adapter.SceneDevicesAdapter;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.scene.SceneDevice;
import com.gdgl.smarthome.R;

public class UiUtils {

	public static final String SharedPreferences_SETTING_INFOS = "SmartHome";

	public static final String ENABLE_IPC = "enable_ipc";
	public static final String GATEWAY_LATEST_VERSION = "gateway_latest_version";
	public static final String GATEWAY_CURRENT_VERSION = "gateway_current_version";
	public static final String GATEWAY_UPDATE_FIRSTTIME = "gateway_update_firsttime";
	
	public static final String PWD = "Password";
	public static final String LOGIN_NAME = "Login_Name";
	public static final String GATEWAY_MAC = "GatewayMAC";
	public static final String ALIAS = "alias";
	public static final String EMAIL_NAME = "Email_Name";//=====王晓飞====设置邮箱
	public static final String GATE_WAY_AUTH_STATE = "Gate_Way_Auth_State";//网关授权state
	public static final String GATE_WAY_AUTH_AVAILABLE = "Gate_Way_Auth_Available";//网关授权available
	public static final String IS_REMERBER_PWD = "RemerberPwd";
	public static final String IS_AUTO_LOGIN = "AutoLogin";

	public static final String REMOTE_CONTROL = "RemoteControl";

	public static final String KONGTIAO = "kongtiao";
	public static final String TV = "tv";

	public static final String REGION = "RegionName";
	public static final String SCENE = "SceneId";
	public static final String COMMONUSED = "common_used";
	public static final String REGION_FLAG = "REGION";
	public static final String SCENE_FLAG = "SCENE";
	public static final String DEVICES_FLAG = "DEVICE";

	public static final String JOINNETTIME = "Joinnettime";

	public static final String EMPTY_STR = "";

	public static final String REM_PWD_ACT = "remeber_pwd";
	public static final String N_REM_PWD_ACT = "not_remeber_pwd";

	public static final String AUTO_LOGIN_ACT = "AutoLogin";

	public static final String UUID = "uuid"; // 通道模块参数

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

	public static final int LIGHTS_MANAGER = 10;
	public static final int ELECTRICAL_MANAGER = 1;
	public static final int SECURITY_CONTROL = 0;
	public static final int ENVIRONMENTAL_CONTROL = 2;
	public static final int ENERGY_CONSERVATION = 3;
	public static final int OTHER = 4;
	public static final int SWITCH_DEVICE = 6;

	public static final String CLOUD = "cloud";
	public static final String CLOUDLIST = "cloudlist";
	public static final String USERLIST = "userlist";

	public static String[] getTagsByType(int type) {
		String[] tags = null;
		switch (type) {
		case LIGHTS_MANAGER:
			tags = new String[] { "电器控制", "安全防护", "照明管理", "环境监测", "节能", "其它" };
			break;
		case ELECTRICAL_MANAGER:
			tags = new String[] { "照明管理", "安全防护", "电器控制", "环境监测", "节能", "其它" };
			break;
		case SECURITY_CONTROL:
			tags = new String[] { "安全防护", "电器控制", "环境监测", "节能", "其它" };
			break;
		case ENVIRONMENTAL_CONTROL:
			tags = new String[] { "照明管理", "电器控制", "环境监测", "安全防护", "节能", "其它" };
			break;
		case ENERGY_CONSERVATION:
			tags = new String[] { "照明管理", "电器控制", "节能", "安全防护", "环境监测", "其它" };
			break;
		case OTHER:
			tags = new String[] { "照明管理", "电器控制", "其它", "节能", "安全防护", "环境监测" };
			break;
		}
		return tags;
	}

	public static int[] getImgByType(int type) {
		int[] imgs = null;
		switch (type) {
		case LIGHTS_MANAGER:
			imgs = new int[] {
					R.drawable.ui_cercleswitch_electricalcontrol_pressed,
					R.drawable.ui_cercleswitch_securitycontrol_pressed,
					R.drawable.ui_cercleswitch_lightmanage_pressed,
					R.drawable.ui_cercleswitch_environmentalcontrol_pressed,
					R.drawable.ui_cercleswitch_energyconservation_pressed,
					R.drawable.ui_cercleswitch_others_pressed };
			break;
		case ELECTRICAL_MANAGER:
			imgs = new int[] { R.drawable.ui_cercleswitch_lightmanage_pressed,
					R.drawable.ui_cercleswitch_securitycontrol_pressed,
					R.drawable.ui_cercleswitch_electricalcontrol_pressed,
					R.drawable.ui_cercleswitch_environmentalcontrol_pressed,
					R.drawable.ui_cercleswitch_energyconservation_pressed,
					R.drawable.ui_cercleswitch_others_pressed };
			break;
		case SECURITY_CONTROL:
			imgs = new int[] {
					R.drawable.ui_cercleswitch_securitycontrol_pressed,
					R.drawable.ui_cercleswitch_electricalcontrol_pressed,
					R.drawable.ui_cercleswitch_environmentalcontrol_pressed,
					R.drawable.ui_cercleswitch_energyconservation_pressed,
					R.drawable.ui_cercleswitch_others_pressed };
			break;
		case ENVIRONMENTAL_CONTROL:
			imgs = new int[] { R.drawable.ui_cercleswitch_lightmanage_pressed,
					R.drawable.ui_cercleswitch_electricalcontrol_pressed,
					R.drawable.ui_cercleswitch_environmentalcontrol_pressed,
					R.drawable.ui_cercleswitch_securitycontrol_pressed,
					R.drawable.ui_cercleswitch_energyconservation_pressed,
					R.drawable.ui_cercleswitch_others_pressed };
			break;
		case ENERGY_CONSERVATION:
			imgs = new int[] { R.drawable.ui_cercleswitch_lightmanage_pressed,
					R.drawable.ui_cercleswitch_electricalcontrol_pressed,
					R.drawable.ui_cercleswitch_energyconservation_pressed,
					R.drawable.ui_cercleswitch_securitycontrol_pressed,
					R.drawable.ui_cercleswitch_environmentalcontrol_pressed,
					R.drawable.ui_cercleswitch_others_pressed };
			break;
		case OTHER:
			imgs = new int[] { R.drawable.ui_cercleswitch_lightmanage_pressed,
					R.drawable.ui_cercleswitch_electricalcontrol_pressed,
					R.drawable.ui_cercleswitch_others_pressed,
					R.drawable.ui_cercleswitch_energyconservation_pressed,
					R.drawable.ui_cercleswitch_securitycontrol_pressed,
					R.drawable.ui_cercleswitch_environmentalcontrol_pressed };
			break;
		}
		return imgs;
	}

	public static int[] getType(int type) {
		int[] imgs = null;
		switch (type) {
		case LIGHTS_MANAGER:
			imgs = new int[] { ELECTRICAL_MANAGER, SECURITY_CONTROL,
					LIGHTS_MANAGER, ENVIRONMENTAL_CONTROL, ENERGY_CONSERVATION,
					OTHER };
			break;
		case ELECTRICAL_MANAGER:
			imgs = new int[] { LIGHTS_MANAGER, SECURITY_CONTROL,
					ELECTRICAL_MANAGER, ENVIRONMENTAL_CONTROL,
					ENERGY_CONSERVATION, OTHER };
			break;
		case SECURITY_CONTROL:
			imgs = new int[] { SECURITY_CONTROL, ELECTRICAL_MANAGER,
					ENVIRONMENTAL_CONTROL, ENERGY_CONSERVATION, OTHER };
			break;
		case ENVIRONMENTAL_CONTROL:
			imgs = new int[] { LIGHTS_MANAGER, ELECTRICAL_MANAGER,
					ENVIRONMENTAL_CONTROL, SECURITY_CONTROL,
					ENERGY_CONSERVATION, OTHER };
			break;
		case ENERGY_CONSERVATION:
			imgs = new int[] { LIGHTS_MANAGER, ELECTRICAL_MANAGER,
					ENERGY_CONSERVATION, SECURITY_CONTROL,
					ENVIRONMENTAL_CONTROL, OTHER };
			break;
		case OTHER:
			imgs = new int[] { LIGHTS_MANAGER, ELECTRICAL_MANAGER, OTHER,
					ENERGY_CONSERVATION, SECURITY_CONTROL,
					ENVIRONMENTAL_CONTROL };
			break;
		}
		return imgs;
	}

	public static int getSceneItemType(int devicesid) {

		int LIGHT = DataHelper.ON_OFF_SWITCH_DEVICETYPE;
		int[] NOOPER = { DataHelper.LIGHT_SENSOR_DEVICETYPE,
				DataHelper.TEMPTURE_SENSOR_DEVICETYPE };
		int[] WITH_VALUE = { DataHelper.DIMEN_SWITCH_DEVICETYPE,
				DataHelper.DIMEN_LIGHTS_DEVICETYPE, DataHelper.SHADE_DEVICETYPE };
		for (int i : WITH_VALUE) {
			if (i == devicesid) {
				return SceneDevicesAdapter.WITH_VALUE;
			}
		}
		if (devicesid == LIGHT) {
			return SceneDevicesAdapter.LIGHT;
		}
		for (int i : NOOPER) {
			if (i == devicesid) {
				return SceneDevicesAdapter.NO_OPERATOR;
			}
		}
		return SceneDevicesAdapter.ON_OFF;
	}

	public static int[] DEVICES_MANAGER_IMAGES = {
			R.drawable.ui_devices_lightmanage_style,
			R.drawable.ui_devices_electricalcontrol_style,
			R.drawable.ui_devices_securitycontrol_style,
			R.drawable.ui_devices_environmentalcontrol_style,
			R.drawable.ui_devices_energyconservation_style,
			R.drawable.ui_devices_others_style };
	public static int[] DEVICES_MANAGER_IMAGES_NEW = {
			R.drawable.ui_devices_securitycontrol_style,
			R.drawable.ui_devices_electricalcontrol_style,
			R.drawable.ui_devices_environmentalcontrol_style,
			R.drawable.ui_devices_energyconservation_style,
			R.drawable.ui_devices_others_style };
	public static String[] DEVICES_MANAGER_TAGS = { "照明管理", "电器控制", "安全防护",
			"环境监测", "节能", "其它" };
	public static String[] DEVICES_MANAGER_TAGS_NEW = { "安全防护", "电器控制", "环境监测",
			"节能", "其它" };

	public static int[] getImagResource(int type) {
		int[] mresult = null;
		switch (type) {
		case 0:
			mresult = DEVICES_MANAGER_IMAGES_NEW;
			break;
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
			mresult = DEVICES_MANAGER_TAGS_NEW;
			break;
		case 1:
			mresult = DEVICES_MANAGER_TAGS;
			break;
		default:
			break;
		}
		return mresult;
	}

	public static Fragment getDeviceDetailFragment(int type) {

		Fragment mFragment = null;
		switch (type) {
		case DataHelper.ON_OFF_SWITCH_DEVICETYPE:
			mFragment = new SwitchControlFragment();
			break;
		case DataHelper.TEMPTURE_SENSOR_DEVICETYPE:
			mFragment = null;
			break;
		case DataHelper.LIGHT_SENSOR_DEVICETYPE:
			mFragment = null;
			break;
		case DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE:
			mFragment = new WarnningControlFragment();
			break;
		case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
			mFragment = new OutLetControlFragment();
			break;
		case DataHelper.IAS_ZONE_DEVICETYPE:
			mFragment = new LockFragment();
			break;
		case DataHelper.DIMEN_SWITCH_DEVICETYPE:

		case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
			mFragment = new SeekLightsControlFragment();
			break;
		case DataHelper.SHADE_DEVICETYPE:
			mFragment = new CurtainControlFragment();
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
		// s = s.substring(s.indexOf("{"), s.length() - 1);
		if (s.contains("{")) {
			s = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
		}
		return s;
	}

	public static List<SceneDevice> parseActionParamsToSceneDevices(int sId, String params) {
		List<SceneDevice> sceneDevicesList = new ArrayList<SceneDevice>();
		if(params.contains("@")) {
			String[] actionparams = params.split("@");
			for (int i = 0; i < actionparams.length; i++) {
				SceneDevice sceneDevice = new SceneDevice(actionparams[i]);
				sceneDevice.setSid(sId);
				sceneDevicesList.add(sceneDevice);
			}
		} else {
			SceneDevice sceneDevice = new SceneDevice(params);
			sceneDevice.setSid(sId);
			sceneDevicesList.add(sceneDevice);
		}
		return sceneDevicesList;
	}
	
	public static Toast getToast(Context c) {
		LayoutInflater inflater = LayoutInflater.from(c);
		View layout = inflater.inflate(R.layout.custom_toast, null);
		Toast toast = new Toast(c);
		toast.setGravity(Gravity.BOTTOM, 0, 250);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
		return toast;
	}

	public static LayoutAnimationController getAnimationController(Context c) {
		int duration = 500;
		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(duration);
		set.addAnimation(animation);

		animation = AnimationUtils.loadAnimation(c, R.anim.slide_bottom_to_top);
		animation.setDuration(duration);
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.5f);
		controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
		return controller;
	}

}
