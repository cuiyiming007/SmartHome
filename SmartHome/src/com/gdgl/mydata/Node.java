package com.gdgl.mydata;

public class Node {

	//"request_id":	12453, "response_params":	[
	//{ "totalcount":	2, "curcount":	1,
	//"node":	{ "ieee":	"00137A00000121F1", "nwk_addr":	"0000", "hw_version":	"0B", "name":	"Combined Interface 1", "date_code":"20131216", 
	//"manufactory":	"netvox", "zcl_version":	"03", "stack_version":	"33", "app_version":	"28", "model_id":	"Z103AE3C", "node_type":	0 }
	//},
	//{ "totalcount":	2, "curcount":	2,//
	//"node":	{ "ieee":	"00137A000000DC86", "nwk_addr":	"C9B0", "hw_version":	"0C", "name":	"Mains Power Outlet 1", "date_code":"20130826",
	//"manufactory":	"netvox", "zcl_version":	"03", "stack_version":	"2F", "app_version":	"15", "model_id":	"Z809AE3R", "node_type":	1 } 
	//}] 
    private  String ieee;
    private  String nwk_addr;
    private  String name;
    private  String manufactory;
    private  String zcl_version;
    private  String stack_version;
    private  String app_version;
    private  String hw_version;
    private  String date_code;
    private  String model_id;
    private  String node_type;
    private  int status;
    
    public String getIeee() {
		return ieee;
	}
	public void setIeee(String ieee) {
		this.ieee = ieee;
	}
	public String getNwk_addr() {
		return nwk_addr;
	}
	public void setNwk_addr(String nwk_addr) {
		this.nwk_addr = nwk_addr;
	}
	public String getHw_version() {
		return hw_version;
	}
	public void setHw_version(String hw_version) {
		this.hw_version = hw_version;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate_code() {
		return date_code;
	}
	public void setDate_code(String date_code) {
		this.date_code = date_code;
	}
	public String getManufactory() {
		return manufactory;
	}
	public void setManufactory(String manufactory) {
		this.manufactory = manufactory;
	}
	public String getZcl_version() {
		return zcl_version;
	}
	public void setZcl_version(String zcl_version) {
		this.zcl_version = zcl_version;
	}
	public String getStack_version() {
		return stack_version;
	}
	public void setStack_version(String stack_version) {
		this.stack_version = stack_version;
	}
	public String getApp_version() {
		return app_version;
	}
	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}
	public String getModel_id() {
		return model_id;
	}
	public void setModel_id(String model_id) {
		this.model_id = model_id;
	}
	public String getNode_type() {
		return node_type;
	}
	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}
