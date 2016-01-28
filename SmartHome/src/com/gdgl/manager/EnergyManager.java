package com.gdgl.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.gdgl.activity.ShowDevicesGroupFragmentActivity;
import com.gdgl.app.ApplicationController;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.EnergyModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.mydata.Callback.CallbackWarnMessage;
import com.gdgl.util.UiUtils;

public class EnergyManager extends Manger{
	public static final String[] strings = new String[] { "3" };
	private static EnergyManager instance;
	DataHelper mDataHelper = new DataHelper(ApplicationController.getInstance());
	SQLiteDatabase mSQLiteDatabase;

	public static EnergyManager getInstance() {
		if (instance == null) {
			instance = new EnergyManager();
		}
		return instance;
	}
	
	EnergyManager(){
		
	}
	
	/**
     * 处理安防回复信息
     * 
     * @throws 
     */
    public void handleMessage(String msg){
    	mSQLiteDatabase = mDataHelper.getWritableDatabase();
		try {
			JSONObject json = new JSONObject(msg);
			int MsgType = json.getInt(EnergyModel.MSGTYPE);
			switch(MsgType){
				//安防配置响应
				case EnergyModel.SET_DEVICES_BACK:
					//getFromSharedPreferences.setEnergyDefense(json.getInt("GlobalOpt"));
					ArrayList<EnergyModel> list = new ArrayList<EnergyModel>();
					JSONArray nodeArray = json.getJSONArray("NodeList");
					for(int i=0; i<nodeArray.length(); i++){
						JSONObject nodeObject = nodeArray.getJSONObject(i);
						String SecurityNodeID = nodeObject.getString("SecurityNodeID");
						DevicesModel mDevicesModel = DataUtil.getDeviceModelByIeee(
								SecurityNodeID, mDataHelper, mSQLiteDatabase);
						String Nwkaddr = nodeObject.getString("Nwkaddr");
						JSONArray termArray = nodeObject.getJSONArray("SubNode");
							for(int n=0; n<termArray.length(); n++){
								JSONObject termObject = termArray.getJSONObject(n);
								EnergyModel mEnergyModel = new EnergyModel();
								mEnergyModel.setIeee(SecurityNodeID);
								mEnergyModel.setAddr(Nwkaddr);
								mEnergyModel.setNum(termObject.getInt("Num"));
								mEnergyModel.setInfo(termObject.getString("Info"));
								mEnergyModel.setType(termObject.getInt("Type"));
								mEnergyModel.setSubType(termObject.getInt("SubType"));
								mEnergyModel.setStatus(termObject.getInt("OperatorType"));
								switch(termObject.getInt("Type")){
									case 1:
										mEnergyModel.setSort(UiUtils.SECURITY_CONTROL);
										break;
									case 2:
										mEnergyModel.setSort(UiUtils.ELECTRICAL_MANAGER);
										break;
									case 3:
										mEnergyModel.setSort(UiUtils.ENVIRONMENTAL_CONTROL);
										break;
								}
								if(mDevicesModel != null){
									mEnergyModel.setRoom(Integer.parseInt(mDevicesModel.getmRid()));
								}
								list.add(mEnergyModel);
							}
							mDataHelper.delete(mSQLiteDatabase, DataHelper.ENERGY_TABLE, EnergyModel.IEEE+"=\""+SecurityNodeID+"\"", null);
					}
					mDataHelper.insertEnergy(mSQLiteDatabase, DataHelper.ENERGY_TABLE, null, list);
					Event event = new Event(EventType.SET_DEVICES_BACK, true);
					notifyObservers(event);
					break;
				//安防查询响应
				case EnergyModel.GET_DEVICES_BACK:
					if(json.getInt("Sn") == -6){
						mSQLiteDatabase = mDataHelper.getWritableDatabase();
						mDataHelper.delete(mSQLiteDatabase, DataHelper.ENERGY_TABLE, EnergyModel.D_TYPE+"!=? ", strings);
						return;
					}
					
					//getFromSharedPreferences.setEnergyDefense(json.getInt("GlobalOpt"));
					ArrayList<EnergyModel> list1 = new ArrayList<EnergyModel>();
					JSONArray nodeArray1 = json.getJSONArray("NodeList");
					for(int i=0; i<nodeArray1.length(); i++){
						JSONObject nodeObject = nodeArray1.getJSONObject(i);
						String SecurityNodeID = nodeObject.getString("SecurityNodeID");
						DevicesModel mDevicesModel = DataUtil.getDeviceModelByIeee(
								SecurityNodeID, mDataHelper, mSQLiteDatabase);
						String Nwkaddr = nodeObject.getString("Nwkaddr");
						JSONArray termArray = nodeObject.getJSONArray("SubNode");
							for(int n=0; n<termArray.length(); n++){
								JSONObject termObject = termArray.getJSONObject(n);
								EnergyModel mEnergyModel = new EnergyModel();
								mEnergyModel.setIeee(SecurityNodeID);
								mEnergyModel.setAddr(Nwkaddr);
								mEnergyModel.setNum(termObject.getInt("Num"));
								mEnergyModel.setInfo(termObject.getString("Info"));
								mEnergyModel.setType(termObject.getInt("Type"));
								mEnergyModel.setSubType(termObject.getInt("SubType"));
								mEnergyModel.setStatus(termObject.getInt("OperatorType"));
								switch(termObject.getInt("Type")){
									case 1:
										mEnergyModel.setSort(UiUtils.SECURITY_CONTROL);
										break;
									case 2:
										mEnergyModel.setSort(UiUtils.ELECTRICAL_MANAGER);
										break;
									case 3:
										mEnergyModel.setSort(UiUtils.ENVIRONMENTAL_CONTROL);
										break;
								}
								if(mDevicesModel != null){
									mEnergyModel.setRoom(Integer.parseInt(mDevicesModel.getmRid()));
								}
								list1.add(mEnergyModel);
							}
							//mDataHelper.delete(mSQLiteDatabase, DataHelper.ENERGY_TABLE, EnergyModel.IEEE+"=\""+SecurityNodeID+"\"", null);
					}
					mDataHelper.insertEnergy(mSQLiteDatabase, DataHelper.ENERGY_TABLE, null, list1);
//					mSQLiteDatabase = mDataHelper.getWritableDatabase();
//					mDataHelper.delete(mSQLiteDatabase, DataHelper.ENERGY_TABLE, EnergyModel.D_TYPE+"!=? ", strings);
//					mDataHelper.insertEnergy(mSQLiteDatabase, DataHelper.ENERGY_TABLE, null, list1);
					Event event1 = new Event(EventType.GET_DEVICES_BACK, true);
					notifyObservers(event1);
					break;	
				//开关状态查询响应
				case EnergyModel.GET_ONOFF_BACK:
					Event event2 = new Event(EventType.GET_ONOFF_BACK, true);
					notifyObservers(event2);
					break;
				//开关控制响应
				case EnergyModel.SET_ONOFF_BACK:
					Event event3 = new Event(EventType.SET_ONOFF_BACK, true);
					notifyObservers(event3);
					break;
				//开关状态广播
				case EnergyModel.RECEIVE_ONOFF_BACK:
					String SecurityNodeID = json.getString("SecurityNodeID");
					String Nwkaddr = json.getString("Nwkaddr");
					String SwitchNum = json.getString("SwitchNum");
					String SwitchStatus = json.getString("SwitchStatus");
					 ContentValues cv = new ContentValues();         
					 cv.put(EnergyModel.D_STATUS, SwitchStatus); 
					mDataHelper.update(mSQLiteDatabase, DataHelper.ENERGY_TABLE, cv, 
							EnergyModel.IEEE+"=? and " +
							EnergyModel.D_NUM+"=? and "+
							EnergyModel.D_TYPE+"=? ",
							new String[] {SecurityNodeID, String.valueOf(SwitchNum),"2"});
					Event event4 = new Event(EventType.SET_DEVICES_BACK, true);
					notifyObservers(event4);
					break;
				//传感器状态广播
				case EnergyModel.RECEIVE_SENSOR_BACK:
					String SecurityNodeID1 = json.getString("SecurityNodeID");
					String Nwkaddr1 = json.getString("Nwkaddr");
					int SwitchNum1 = json.getInt("SensorNum");
					int SwitchStatus1 = json.getInt("SensorStatus");
					if(SwitchStatus1 == 2){
						return;
					}
					ArrayList<EnergyModel> mList = mDataHelper.queryForEnergyList(mSQLiteDatabase, DataHelper.ENERGY_TABLE, 
							EnergyModel.IEEE+"=? and " +
							EnergyModel.D_NUM+"=? ", 
							new String[] {SecurityNodeID1, String.valueOf(SwitchNum1)});
					if(mList.size() == 0){
						return;
					}
					mSQLiteDatabase = mDataHelper.getWritableDatabase();
					DevicesModel mDevice = DataUtil.getDeviceModelByModelid(DataHelper.One_key_operator, mDataHelper, mSQLiteDatabase);
					Date date = new Date();
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					CallbackWarnMessage warnMessage = new CallbackWarnMessage();
					warnMessage.setDetailmessage(mList.get(0).getInfo()+"(In_"+SecurityNodeID1.substring(SecurityNodeID1.length()-4)+")告警");
					warnMessage.setMsgtype("energy");
					warnMessage.setTime(df.format(date));
					warnMessage.setCie_ieee(mDevice.getmIeee());
					warnMessage.setHouseIEEE(mDevice.getmIeee());
					//warnMessage.setRoomId(""+mList.get(0).getRoom());
					CallbackManager.getInstance().handlerWarnMessage(warnMessage);
					
					
//					ContentValues cv1 = new ContentValues();         
//					cv1.put(EnergyModel.D_STATUS, SwitchStatus1); 
//					mDataHelper.update(mSQLiteDatabase, DataHelper.ENERGY_TABLE, cv1, 
//							EnergyModel.IEEE+"=? and " +
//							EnergyModel.D_NUM+"=? ",
//							new String[] {SecurityNodeID1, String.valueOf(SwitchNum1)});
					
//					Event event5 = new Event(EventType.RECEIVE_SENSOR_BACK, true);
//					notifyObservers(event5);
					break;
				//全局布防广播
				case EnergyModel.RECEIVE_ALL_DEFENSE_BACK:
//					getFromSharedPreferences.setEnergyDefense(json.getInt("OperatorType")); 
//					Event event6 = new Event(EventType.RECEIVE_ALL_DEFENSE_BACK, true);
//					notifyObservers(event6);
					break;
				//设备布防广播
				case EnergyModel.RECEIVE_DEFENSE_BACK:
					String SecurityNodeID2 = json.getString("SecurityNodeID");
					String Nwkaddr2 = json.getString("Nwkaddr");
					int SwitchNum2 = json.getInt("SensorNum");
					int SwitchStatus2 = json.getInt("OperatorType");
					 ContentValues cv2 = new ContentValues();         
					 cv2.put(EnergyModel.D_STATUS, SwitchStatus2); 
					mDataHelper.update(mSQLiteDatabase, DataHelper.ENERGY_TABLE, cv2, 
							EnergyModel.IEEE+"=? and " +
							EnergyModel.D_NUM+"=? and "+
							EnergyModel.D_TYPE+"=? ",
							new String[] {SecurityNodeID2, String.valueOf(SwitchNum2),"1"});
					Event event7 = new Event(EventType.SET_DEVICES_BACK, true);
					notifyObservers(event7);
					break;
				//全局布防响应
				case EnergyModel.SET_ALL_DEFENSE_BACK:
					Event event8 = new Event(EventType.SET_ALL_DEFENSE_BACK, true);
					notifyObservers(event8);
					break;
				//设备布防响应
				case EnergyModel.GET_ALL_DEFENSE_BACK:
					Event event9 = new Event(EventType.GET_ALL_DEFENSE_BACK, true);
					notifyObservers(event9);
					break;
				//设置表配置响应	
				case EnergyModel.SET_METER_LIST_BACK:
					ArrayList<EnergyModel> list10 = new ArrayList<EnergyModel>();
					JSONArray nodeArray10 = json.getJSONArray("NodeList");
					for(int i=0; i<nodeArray10.length(); i++){
						JSONObject nodeObject = nodeArray10.getJSONObject(i);
						String SecurityNodeID10 = nodeObject.getString("IEEE");
						DevicesModel mDevicesModel = DataUtil.getDeviceModelByIeee(
								SecurityNodeID10, mDataHelper, mSQLiteDatabase);
						String Nwkaddr10 = nodeObject.getString("Nwkaddr");
						JSONArray termArray = nodeObject.getJSONArray("TermList");
							for(int n=0; n<termArray.length(); n++){
								JSONObject termObject = termArray.getJSONObject(n);
								EnergyModel mEnergyModel = new EnergyModel();
								mEnergyModel.setIeee(SecurityNodeID10);
								mEnergyModel.setAddr(Nwkaddr10);
								mEnergyModel.setMeterNum(termObject.getString("TermCode"));
								mEnergyModel.setInfo(termObject.getString("TermInfo"));
								mEnergyModel.setType(3);
								mEnergyModel.setSubType(termObject.getInt("TermType"));
								mEnergyModel.setStatus(termObject.getInt("TermPeriod"));
								mEnergyModel.setSort(UiUtils.ENERGY_CONSERVATION);
								if(mDevicesModel != null){
									mEnergyModel.setRoom(Integer.parseInt(mDevicesModel.getmRid()));
								}
								list10.add(mEnergyModel);
							}
					}
					mSQLiteDatabase = mDataHelper.getWritableDatabase();
					mDataHelper.delete(mSQLiteDatabase, DataHelper.ENERGY_TABLE, EnergyModel.D_TYPE+"=? ", strings);
					mDataHelper.insertEnergy(mSQLiteDatabase, DataHelper.ENERGY_TABLE, null, list10);
					Event event10 = new Event(EventType.SET_METER_LIST_BACK, true);
					notifyObservers(event10);
					break;
				//查询表配置响应
				case EnergyModel.GET_METER_LIST_BACK:
					ArrayList<EnergyModel> list11 = new ArrayList<EnergyModel>();
					JSONArray nodeArray11 = json.getJSONArray("NodeList");
					for(int i=0; i<nodeArray11.length(); i++){
						JSONObject nodeObject = nodeArray11.getJSONObject(i);
						String SecurityNodeID11 = nodeObject.getString("IEEE");
						DevicesModel mDevicesModel = DataUtil.getDeviceModelByIeee(
								SecurityNodeID11, mDataHelper, mSQLiteDatabase);
						String Nwkaddr11 = nodeObject.getString("Nwkaddr");
						JSONArray termArray = nodeObject.getJSONArray("TermList");
							for(int n=0; n<termArray.length(); n++){
								JSONObject termObject = termArray.getJSONObject(n);
								EnergyModel mEnergyModel = new EnergyModel();
								mEnergyModel.setIeee(SecurityNodeID11);
								mEnergyModel.setAddr(Nwkaddr11);
								mEnergyModel.setMeterNum(termObject.getString("TermCode"));
								mEnergyModel.setInfo(termObject.getString("TermInfo"));
								mEnergyModel.setType(3);
								mEnergyModel.setSubType(termObject.getInt("TermType"));
								mEnergyModel.setStatus(termObject.getInt("TermPeriod"));
								mEnergyModel.setSort(UiUtils.ENERGY_CONSERVATION);
								if(mDevicesModel != null){
									mEnergyModel.setRoom(Integer.parseInt(mDevicesModel.getmRid()));
								}
								list11.add(mEnergyModel);
							}
					}
					mSQLiteDatabase = mDataHelper.getWritableDatabase();
					//mDataHelper.delete(mSQLiteDatabase, DataHelper.ENERGY_TABLE, EnergyModel.D_TYPE+"=? ", strings);
					mDataHelper.insertEnergy(mSQLiteDatabase, DataHelper.ENERGY_TABLE, null, list11);
					Event event11 = new Event(EventType.GET_METER_LIST_BACK, true);
					notifyObservers(event11);
					break;
				//查询表读数响应
				case EnergyModel.GET_METER_DATA_BACK:
					String meterNum = json.getString("TermCode");
					String data	= ""+json.get("ReportData");
					ContentValues cv3 = new ContentValues();         
					cv3.put(EnergyModel.D_DATA, data); 
					mDataHelper.update(mSQLiteDatabase, DataHelper.ENERGY_TABLE, cv3, 
							EnergyModel.D_METER_NUM+"=? ",
							new String[] {meterNum});
					Event event12 = new Event(EventType.SET_DEVICES_BACK, true);
					notifyObservers(event12);
					break;
				//表读数上报广播
				case EnergyModel.RECEIVE_METER_DATA:
					String meterNum1 = json.getString("TermCode");
					String data1	= ""+json.get("ReportData");
					ContentValues cv4 = new ContentValues();         
					cv4.put(EnergyModel.D_DATA, data1); 
					mDataHelper.update(mSQLiteDatabase, DataHelper.ENERGY_TABLE, cv4, 
							EnergyModel.D_METER_NUM+"=? ",
							new String[] {meterNum1});
					Event event13 = new Event(EventType.SET_DEVICES_BACK, true);
					notifyObservers(event13);
					break;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
