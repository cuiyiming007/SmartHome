package com.gdgl.mydata;

import java.util.ArrayList;

/***
 /"request_id":	12453, "response_params":	[
	//{ "totalcount":	2, "curcount":	1,
	//"node":	{ "ieee":	"00137A00000121F1", "nwk_addr":	"0000", "hw_version":	"0B", "name":	"Combined Interface 1", "date_code":"20131216", 
	//"manufactory":	"netvox", "zcl_version":	"03", "stack_version":	"33", "app_version":	"28", "model_id":	"Z103AE3C", "node_type":	0 }
	//},
	//{ "totalcount":	2, "curcount":	2,//
	//"node":	{ "ieee":	"00137A000000DC86", "nwk_addr":	"C9B0", "hw_version":	"0C", "name":	"Mains Power Outlet 1", "date_code":"20130826",
	//"manufactory":	"netvox", "zcl_version":	"03", "stack_version":	"2F", "app_version":	"15", "model_id":	"Z809AE3R", "node_type":	1 } 
	//}] 
	 * 
	 * @author justek
	 *
	 */
public class RespondDataEntity<T> {

	private String request_id;
	private ArrayList<T> responseparamList;
	
	
	public String getRequest_id() {
		return request_id;
	}
	public ArrayList<T> getResponseparamList() {
		return responseparamList;
	}
	public void setResponseparamList(ArrayList<T> responseparamList) {
		this.responseparamList = responseparamList;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
}
