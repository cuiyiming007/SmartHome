package com.gdgl.mydata;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.model.DevicesModel;
import com.gdgl.util.UiUtils;

public class DataUtil {

	public static List<List<DevicesModel>> getdata(int type) {
		DevicesModel mDevicesModel;
		List<List<DevicesModel>> mList = new ArrayList<List<DevicesModel>>();

		boolean[] lightsOn = { true };
		boolean[] lightsOff = { false };

		switch (type) {
		case UiUtils.LIGHTS_MANAGER:
//			List<DevicesModel> mLights = new ArrayList<DevicesModel>();
//			List<DevicesModel> mDimenLights = new ArrayList<DevicesModel>();
//			for (int m = 0; m < 8; m++) {
//				if (m % 3 == 0) {
//					mDevicesModel = new DevicesModel(m, "Light" + m, "Light"
//							+ m, UiUtils.LIGHTS, lightsOn, "卧室");
//				} else {
//					mDevicesModel = new DevicesModel(m, "Light" + m, "Light"
//							+ m, UiUtils.LIGHTS, lightsOff, "客厅");
//				}
//				mLights.add(mDevicesModel);
//			}
//
//			for (int m = 0; m < 8; m++) {
//				if (m % 3 == 0) {
//					mDevicesModel = new DevicesModel(m, "Light" + m, "Light"
//							+ m, UiUtils.DIMEN_LIGHTS, 0.3f, lightsOn, "卧室");
//				} else {
//					mDevicesModel = new DevicesModel(m, "Light" + m, "Light"
//							+ m, UiUtils.DIMEN_LIGHTS, 0.5f, lightsOff, "客厅");
//				}
//				mDimenLights.add(mDevicesModel);
//			}
//			mList.add(mLights);
//			mList.add(mDimenLights);
			break;
		case UiUtils.ELECTRICAL_MANAGER:
//			List<DevicesModel> mSwitches = new ArrayList<DevicesModel>();
//			List<DevicesModel> mCurtains = new ArrayList<DevicesModel>();
//			boolean[] bool1 = { true, true, false };
//			boolean[] bool2 = { true, false };
//			boolean[] bool3 = { false };
//			boolean[] bool4 = { true };
//			boolean[] bool5 = { true, true };
//
//			mDevicesModel = new DevicesModel(1, "Switch_1", "Switch_1",
//					UiUtils.SWITCH, bool1, "卧室");
//			mSwitches.add(mDevicesModel);
//
//			mDevicesModel = new DevicesModel(2, "Switch_2", "Switch_2",
//					UiUtils.SWITCH, bool2, "卧室");
//			mSwitches.add(mDevicesModel);
//
//			mDevicesModel = new DevicesModel(3, "Switch_3", "Switch_3",
//					UiUtils.SWITCH, bool3, "客厅");
//			mSwitches.add(mDevicesModel);
//
//			mDevicesModel = new DevicesModel(4, "Switch_4", "Switch_4",
//					UiUtils.SWITCH, bool4, "卫生间");
//			mSwitches.add(mDevicesModel);
//
//			mDevicesModel = new DevicesModel(5, "Switch_5", "Switch_5",
//					UiUtils.SWITCH, bool5, "卧室");
//			mSwitches.add(mDevicesModel);
//
//			for (int m = 0; m < 5; m++) {
//				if (m % 3 == 0) {
//					mDevicesModel = new DevicesModel(m, "Curtains" + m,
//							"Curtains" + m, UiUtils.CURTAINS, 0.5f, lightsOn,
//							"卧室");
//				} else {
//					mDevicesModel = new DevicesModel(m, "Curtains" + m,
//							"Curtains" + m, UiUtils.CURTAINS, lightsOff, "客厅");
//				}
//				mCurtains.add(mDevicesModel);
//			}
//			mList.add(mSwitches);
//			mList.add(mCurtains);
//			mList.add(mSwitches);
			break;
		case UiUtils.DEVICES_MANAGER:
			List<DevicesModel> mLights = new ArrayList<DevicesModel>();
			List<DevicesModel> mDimenLights = new ArrayList<DevicesModel>();
			for (int m = 0; m <8; m++) {
				if (m % 3 == 0) {
					mDevicesModel = new DevicesModel(m, "Light" + m, "Light"
							+ m, UiUtils.LIGHTS, lightsOn, "卧室");
				} else {
					mDevicesModel = new DevicesModel(m, "Light" + m, "Light"
							+ m, UiUtils.LIGHTS, lightsOff, "客厅");
				}
				mLights.add(mDevicesModel);
			}

			for (int m = 0; m <8; m++) {
				if (m % 3 == 0) {
					mDevicesModel = new DevicesModel(m, "Light" + m, "Light"
							+ m, UiUtils.DIMEN_LIGHTS, 0.3f, lightsOn, "卧室");
				} else {
					mDevicesModel = new DevicesModel(m, "Light" + m, "Light"
							+ m, UiUtils.DIMEN_LIGHTS, 0.5f, lightsOff, "客厅");
				}
				mDimenLights.add(mDevicesModel);
			}
			
			
			List<DevicesModel> mSwitches = new ArrayList<DevicesModel>();
			List<DevicesModel> mCurtains = new ArrayList<DevicesModel>();
			boolean[] bool1 = { true, true, false };
			boolean[] bool2 = { true, false };
			boolean[] bool3 = { false };
			boolean[] bool4 = { true };
			boolean[] bool5 = { true, true };

			mDevicesModel = new DevicesModel(0, "Switch_1", "Switch_1",
					UiUtils.SWITCH, bool1, "卧室");
			mSwitches.add(mDevicesModel);

			mDevicesModel = new DevicesModel(1, "Switch_2", "Switch_2",
					UiUtils.SWITCH, bool2, "卧室");
			mSwitches.add(mDevicesModel);

			mDevicesModel = new DevicesModel(2, "Switch_3", "Switch_3",
					UiUtils.SWITCH, bool3, "客厅");
			mSwitches.add(mDevicesModel);

			mDevicesModel = new DevicesModel(3, "Switch_4", "Switch_4",
					UiUtils.SWITCH, bool4, "卫生间");
			mSwitches.add(mDevicesModel);

			mDevicesModel = new DevicesModel(4, "Switch_5", "Switch_5",
					UiUtils.SWITCH, bool5, "卧室");
			mSwitches.add(mDevicesModel);

			for (int m = 0; m < 5; m++) {
				if (m % 3 == 0) {
					mDevicesModel = new DevicesModel(m, "Curtains" + m,
							"Curtains" + m, UiUtils.CURTAINS, 0.5f, lightsOn,
							"卧室");
				} else {
					mDevicesModel = new DevicesModel(m, "Curtains" + m,
							"Curtains" + m, UiUtils.CURTAINS, lightsOff, "客厅");
				}
				mCurtains.add(mDevicesModel);
			}
			
			mList.add(mLights);
			mList.add(mDimenLights);
			mList.add(mSwitches);
			mList.add(mCurtains);
			mList.add(mSwitches);
		default:
			break;

		}
		return mList;
	}

}
