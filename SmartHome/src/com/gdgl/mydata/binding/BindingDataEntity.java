package com.gdgl.mydata.binding;

import java.util.ArrayList;

import com.gdgl.mydata.Callback.CallbackBindListMessage;

import android.provider.BaseColumns;

interface BindColumns extends BaseColumns {
	public static final String DEVOUT_IEEE = "devout_ieee";
	public static final String DEVOUT_EP = "devout_ep";
	public static final String DEVIN_IEEE = "devin_ieee";
	public static final String DEVIN_EP = "devin_ep";
	public static final String CLUSTER = "cluster";
}

public class BindingDataEntity implements BindColumns {
	private String request_id;
	private ArrayList<CallbackBindListMessage> response_params;
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public ArrayList<CallbackBindListMessage> getResponse_paramsList() {
		return response_params;
	}
	public void setResponse_paramsList(ArrayList<CallbackBindListMessage> response_params) {
		this.response_params = response_params;
	}
	

}
