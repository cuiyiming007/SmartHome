package com.gdgl.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.binding.BindingDataEntity;
import com.gdgl.mydata.binding.BindingDivice;

public class BindManager {
	private final static String TAG = "BindManager";

	// Key 定义为String类型：Ieee+ep
	private HashMap<String, ArrayList<BindingDivice>> bindInfoMap = new HashMap<String, ArrayList<BindingDivice>>();

	private static BindManager instance;

	public static BindManager getInstance() {
		if (instance == null) {
			instance = new BindManager();
		}
		return instance;
	}

	public void initalBindInfoMapFromServer(List<DevicesModel> devicesModels) {
		bindInfoMap.clear();
		for (Iterator iterator = devicesModels.iterator(); iterator.hasNext();) {
			DevicesModel devicesModel = (DevicesModel) iterator.next();
			LightManager.getInstance().getBindList(devicesModel);
		}

	}

	public void setBindInfoMap(BindingDataEntity data) {
		String keyString = data.getResponse_params().getIeee().trim()
				+ data.getResponse_params().getEp();
		ArrayList< BindingDivice> list=data.getResponse_params().getList();

		if (list!=null&&list.size()>0) {
			bindInfoMap.put(keyString, data.getResponse_params().getList());
		}
	}

}
