package com.gdgl.model;

import java.io.Serializable;

import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.DevParam;
import com.gdgl.mydata.Node;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.util.UiUtils;

import android.content.ContentValues;
import android.provider.BaseColumns;
import android.util.Log;

interface EnergyBaseColumns extends BaseColumns {
	public static final String IEEE = "ieee";	//节点IEEE
	public static final String ADDR = "addr";	//节点Nwk_addr
	public static final String D_NUM = "d_num";	//子设备编号
	public static final String D_TYPE = "d_type";	//子设备总类型
	public static final String D_SUBTYPE = "d_subtype";	//子设备类型
	public static final String D_INFO = "d_info";	//子设备名称
	public static final String D_STATUS = "d_status"; 	//撤防布防 开关
	public static final String D_DATA = "d_data";	//表数据
	public static final String D_ROOM = "d_room";	//子设备房间
	public static final String D_SORT = "device_sort"; //子设备分类
	public static final String D_METER_NUM = "d_meter_num"; //抄表模块子设备编号
	
	//API相关
	public static final String JSONSTRING = "JsonString";
	
	public static final String MSGTYPE = "MsgType";
	public static final String SN = "Sn";
	public static final String GLOBA = "Globa10pt";
	public static final String NODELIST = "NodeList";
	public static final String SECURITYNODEID = "SecurityNodeID";
	public static final String NWKADDR = "Nwkaddr";
	public static final String SUBNODE = "SubNode";
	public static final String TYPE = "Type";
	public static final String SUBTYPE = "SubType";
	public static final String NUM = "Num";
	public static final String INFO = "Info";
	public static final String OPERATORTYPE = "OperatorType";
	
	//开关API
	public static final String SWITCHNUM = "SwitchNum";
	public static final String SWITCHSTATUS = "SwitchStatus";
	//传感器API
	public static final String SENSORNUM = "SensorNum";
	public static final String SENSORSTATUS = "SensorStatus";
	
	public static final String TYPELIST[] = {"", "安防节点", "家用电器"};
	
	//心跳下发
	public static final int HEART_SEND = 0x10; 
	public static final int HEART_BACK = 0x20;
	
	//安防配置下发
	public static final int SET_DEVICES_SEND = 0x70;
	public static final int SET_DEVICES_BACK = 0x80;
	
	//安防配置查询
	public static final int GET_DEVICES_SEND = 0x71;
	public static final int GET_DEVICES_BACK = 0x81;
	
	//开关状态查询
	public static final int GET_ONOFF_SEND = 0x73;
	public static final int GET_ONOFF_BACK = 0x83;
	
	//开关状态控制
	public static final int SET_ONOFF_SEND = 0x74;
	public static final int SET_ONOFF_BACK = 0x84;
	
	//开关状态广播
	public static final int RECEIVE_ONOFF_SEND = 0x85;
	public static final int RECEIVE_ONOFF_BACK = 0x75;
	
	//传感器状态广播
	public static final int RECEIVE_SENSOR_SEND = 0x86;
	public static final int RECEIVE_SENSOR_BACK = 0x76;
	
	//设置全局布防
	public static final int SET_ALL_DEFENSE_SEND = 0x77;
	public static final int SET_ALL_DEFENSE_BACK = 0x87;
	
	//查询全局布防
	public static final int GET_ALL_DEFENSE_SEND = 0x78;
	public static final int GET_ALL_DEFENSE_BACK = 0x88;
	
	//设置单设备布防
	public static final int SET_DEFENSE_SEND = 0x78;
	public static final int SET_DEFENSE_BACK = 0x88;
	
	//查询单设备布防
	public static final int GET_DEFENSE_SEND = 0x7A;
	public static final int GET_DEFENSE_BACK = 0x8A;
	
	//全局布防广播
	public static final int RECEIVE_ALL_DEFENSE_SEND = 0x89;
	public static final int RECEIVE_ALL_DEFENSE_BACK = 0x79;
	
	//单设备布防广播
	public static final int RECEIVE_DEFENSE_SEND = 0x8a;
	public static final int RECEIVE_DEFENSE_BACK = 0x7a;
	
	/*
	 * 抄表模块
	 */
	//设置表配置
	public static final int SET_METER_LIST_SEND = 0x11;
	public static final int SET_METER_LIST_BACK = 0x21;
	
	//查询表配置
	public static final int GET_METER_LIST_SEND = 0x12;
	public static final int GET_METER_LIST_BACK = 0x22;
		
	//查询表读数
	public static final int GET_METER_DATA_SEND = 0x13;
	public static final int GET_METER_DATA_BACK = 0x23;
	
	//表读数上报
	public static final int RECEIVE_METER_DATA = 0x30;

}

public class EnergyModel implements EnergyBaseColumns, Serializable {

	private String ieee = "";
	private String addr = "";
	private int num = 0;
	private int type = 0;
	private int subtype = 1;
	private String info = "";
	private int status = 0;
	private String data = "0.0";
	private int room = 0;
	private int sort;
	private String meterNum = ""; 
	
	
	//额外添加
	private boolean isEdit;
	
	public void setIeee(String ieee){
		this.ieee = ieee;
	}
	public String getIeee(){
		return ieee;
	}
	public void setAddr(String addr){
		this.addr = addr;
	}
	public String getAddr(){
		return addr;
	}
	public void setNum(int num){
		this.num = num;
	}
	public int getNum(){
		return num;
	}
	public void setType(int type){
		this.type = type;
	}
	public int getType(){
		return type;
	}
	public void setSubType(int subtype){
		this.subtype = subtype;
	}
	public int getSubType(){
		return subtype;
	}
	public void setInfo(String info){
		this.info = info;
	}
	public String getInfo(){
		return info;
	}
	public void setStatus(int status){
		this.status = status;
	}
	public int getStatus(){
		return status;
	}
	public void setData(String data){
		this.data = data;
	}
	public String getData(){
		return data;
	}
	public void setRoom(int room){
		this.room = room;
	}
	public int getRoom(){
		return room;
	}
	public void setIsEdit(boolean isEdit){
		this.isEdit = isEdit;
	}
	public boolean getIsEdit(){
		return isEdit;
	}
	public void setSort(int sort){
		this.sort = sort;
	}
	public int getSort(){
		return sort;
	}
	
	public void setMeterNum(String meterNum){
		this.meterNum = meterNum;
	}
	
	public String getMeterNum(){
		return meterNum;
	}

	public ContentValues convertContentValues() {
		ContentValues mContentValues = new ContentValues();

		mContentValues.put(EnergyBaseColumns.IEEE, getIeee());
		mContentValues.put(EnergyBaseColumns.ADDR, getAddr());
		mContentValues.put(EnergyBaseColumns.D_NUM, getNum());
		mContentValues.put(EnergyBaseColumns.D_TYPE, getType());
		mContentValues.put(EnergyBaseColumns.D_SUBTYPE, getSubType());
		mContentValues.put(EnergyBaseColumns.D_INFO, getInfo());
		mContentValues.put(EnergyBaseColumns.D_STATUS, getStatus());
		mContentValues.put(EnergyBaseColumns.D_ROOM, getRoom());
		mContentValues.put(EnergyBaseColumns.D_DATA, getData());
		mContentValues.put(EnergyBaseColumns.D_SORT, getSort());
		mContentValues.put(EnergyBaseColumns.D_METER_NUM, getMeterNum());
		
		return mContentValues;
	}

	public EnergyModel() {
		
	}

	// ResponseParamsEndPoint
	public EnergyModel(String ieee, String addr, int num, int type, int room) {
		this.ieee = ieee;
		this.addr = addr;
		this.num = num;
		this.type = type;
		this.info = "";
		this.status = 2;
		this.room = room;
		
		switch(type){
		case 1:
			this.sort = UiUtils.SECURITY_CONTROL;
			break;
		case 2:
			this.sort = UiUtils.ELECTRICAL_MANAGER;
			break;
		case 3:
			this.sort = UiUtils.ENERGY_CONSERVATION;
			break;
		default:
			this.sort = UiUtils.OTHER;
		}
	}
	
	// ResponseParamsEndPoint
		public EnergyModel(String ieee, String addr, int num, int type, int room, int subtype) {
			this.ieee = ieee;
			this.addr = addr;
			this.num = num;
			this.type = type;
			this.info = "";
			this.status = 2;
			this.room = room;
			this.subtype = 0;
			
			switch(type){
			case 1:
				this.sort = UiUtils.SECURITY_CONTROL;
				break;
			case 2:
				this.sort = UiUtils.ELECTRICAL_MANAGER;
				break;
			case 3:
				this.sort = UiUtils.ENERGY_CONSERVATION;
				break;
			default:
				this.sort = UiUtils.OTHER;
			}
		}

	@Override
	public String toString() {
		return "EnergyModel [IEEE=" + ieee + ", ADDR=" + addr
				+ ", NUM=" + num + ", TYPE=" + type
				+ ", SUBTYPE=" + subtype + ", INFO=" + info + ", STATUS="
				+ status + ", DATA=" + data
				+ ", ROOM=" + room + ", SORT=" + sort + ", ROOM=" + room
				+ ", METER_NUM=" + meterNum + "]";
	}
}
