package com.gdgl.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.binding.BindingDataEntity;
import com.gdgl.mydata.binding.BindingDivice;
import com.gdgl.mydata.getlocalcielist.elserec;

public class BindManager {
	private final static String TAG = "BindManager";

	// Key 定义为String类型：Ieee+ep
	private HashMap<String, ArrayList<BindingDivice>> bindInfoMap = new HashMap<String, ArrayList<BindingDivice>>();

	private static BindManager instance;

	private boolean initialedMap = false;
	
	private int  currentDeviceNum=0;
	
	public int totalDeviceNum=0;

	public static BindManager getInstance() {
		if (instance == null) {
			instance = new BindManager();
			instance.initialedMap=false;
		}
		return instance;
	}

	public void initalBindInfoMapFromServer(List<DevicesModel> devicesModels) {
		totalDeviceNum=devicesModels.size();
		currentDeviceNum=0;
		if (initialedMap==false) {
			for (Iterator iterator = devicesModels.iterator(); iterator.hasNext();) {
				DevicesModel devicesModel = (DevicesModel) iterator.next();
				CGIManager.getInstance().getBindList(devicesModel);
			}
		}
	}

	public void setBindInfoMap(BindingDataEntity data) {
		String keyString = data.getResponse_params().getIeee().trim()
				+ data.getResponse_params().getEp().trim();
		ArrayList<BindingDivice> list = data.getResponse_params().getList();

		if (list != null && list.size() > 0) {
			bindInfoMap.put(keyString, data.getResponse_params().getList());
		}
	}

	public ArrayList<BindingDivice> getBindingList(DevicesModel devicesModel) {
		ArrayList<BindingDivice> bindingDivices = bindInfoMap
				.get(getIeeeEPString(devicesModel));
		return bindingDivices;
	}

	private String getIeeeEPString(DevicesModel devicesModel) {
		return devicesModel.getmIeee().trim() + devicesModel.getmEP().trim();
	}

	/***
	 * 判断devicesModel是否有已绑定的设备
	 * 
	 * @param devicesModel
	 * @return
	 */
	public boolean hasBinded(DevicesModel devicesModel) {
		ArrayList<BindingDivice> bindingDivices = getBindingList(devicesModel);
		if (bindingDivices != null && bindingDivices.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/***
	 * 判断两个设备是否互相绑定
	 */
	public boolean isBindtoBind(DevicesModel devicesModel1,
			DevicesModel devicesModel2) {
		ArrayList<BindingDivice> bindingDivices = getBindingList(devicesModel1);
		for (BindingDivice bindingDivice : bindingDivices) {
			if (bindingDivice != null
					&& isEqualDevice(bindingDivice, devicesModel2)) {
				return true;
			}
		}
		return false;
	}

	public boolean isEqualDevice(BindingDivice bindingDivice,
			DevicesModel devicesModel) {
		if (bindingDivice.getIeee().equals(devicesModel.getmIeee())
				&& bindingDivice.getEp().equals(devicesModel.getmEP())) {
			return true;

		} else {
			return false;
		}
	}

	public void removeBinded(DevicesModel devicesModel, DevicesModel bindModel) {
		ArrayList<BindingDivice> bindedList = getBindingList(devicesModel);
		if (bindedList == null) {
			Log.e(TAG,
					"removeBinded(): try to remove a unbinded device in cache bindInfoMap,devicesModel "
							+ devicesModel.toString() + "has no bindlist");
		} else {
			for (BindingDivice bindingDivice : bindedList) {
				if (isEqualDevice(bindingDivice, devicesModel)) {
					bindedList.remove(bindingDivice);
					break;
				}
			}
		}

	}

	public void setBinded(DevicesModel devicesModel, DevicesModel bindModel) {
		ArrayList<BindingDivice> bindedList = getBindingList(devicesModel);
		if (bindedList == null) {
			bindedList = new ArrayList<BindingDivice>();
		}
		BindingDivice bindingDivice = convertDeviceModelToBindingDivice(bindModel);
		bindedList.add(bindingDivice);
	}

	public BindingDivice convertDeviceModelToBindingDivice(
			DevicesModel devicesModel) {
		BindingDivice bindingDivice = new BindingDivice();
		bindingDivice.setIeee(devicesModel.getmIeee());
		bindingDivice.setEp(devicesModel.getmEP());
		bindingDivice.setCid(devicesModel.getmClusterID());
		return bindingDivice;
	}

	public void addInitialedDeviceNum()
	{
		currentDeviceNum++;
		if (totalDeviceNum==currentDeviceNum) {
			setInitialedMap(true);
			currentDeviceNum=0;
		}
	}
	public boolean isInitialedMap() {
		return initialedMap;
	}

	public void setInitialedMap(boolean initialedMap) {
		this.initialedMap = initialedMap;
	}

	public int getDevicemodeSize() {
		return currentDeviceNum;
	}

	public void setDevicemodeSize(int devicemodeSize) {
		this.currentDeviceNum = devicemodeSize;
	}

}
