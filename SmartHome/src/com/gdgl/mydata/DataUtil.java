package com.gdgl.mydata;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.model.DevicesModel;
import com.gdgl.util.UiUtils;

public class DataUtil {
	
	public static String[] getArgs(int type) {
		String[] args=null;
		switch (type) {
		case UiUtils.LIGHTS_MANAGER:
			args=new String[6];
			args[0]=DataHelper.DIMEN_LIGHTS+"";
			args[1]=DataHelper.DIMEN_SWITCH+"";
			args[2]=DataHelper.LIGHT_SENSOR+"";
			args[3]=DataHelper.ON_OFF_OUTPUT+"";
			args[4]=DataHelper.ON_OFF_SWITCH+"";
			args[5]=DataHelper.MAINS_POWER_OUTLET+"";
			break;
		case UiUtils.ELECTRICAL_MANAGER:
			args=new String[6];
			args[0]=DataHelper.DIMEN_LIGHTS+"";
			args[1]=DataHelper.DIMEN_SWITCH+"";
			args[2]=DataHelper.LIGHT_SENSOR+"";
			args[3]=DataHelper.ON_OFF_OUTPUT+"";
			args[4]=DataHelper.ON_OFF_SWITCH+"";
			args[5]=DataHelper.MAINS_POWER_OUTLET+"";
			break;
		case UiUtils.DEVICES_MANAGER:
			args=new String[6];
			args[0]=DataHelper.DIMEN_LIGHTS+"";
			args[1]=DataHelper.DIMEN_SWITCH+"";
			args[2]=DataHelper.LIGHT_SENSOR+"";
			args[3]=DataHelper.ON_OFF_OUTPUT+"";
			args[4]=DataHelper.ON_OFF_SWITCH+"";
			args[5]=DataHelper.MAINS_POWER_OUTLET+"";
		default:
			break;

		}
		return args;
	}

	public static List<DevicesModel> convertToDevicesModel(RespondDataEntity<ResponseParamsEndPoint> r){
		List<DevicesModel> mList=new ArrayList<DevicesModel>();
		DevicesModel mDevicesModel = null;
		for (ResponseParamsEndPoint mResponseParamsEndPoint : r.getResponseparamList()) {
			if(null!=mResponseParamsEndPoint){
				mDevicesModel=new DevicesModel(mResponseParamsEndPoint);
			}
			mList.add(mDevicesModel);
		}
		return mList;
	}

}
