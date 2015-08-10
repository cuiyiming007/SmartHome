package com.gdgl.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gdgl.app.ApplicationController;
import com.gdgl.libjingle.LibjingleSendManager;
import com.gdgl.manager.EnergyManager;
import com.gdgl.model.EnergyModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.net.tcp.SocketConnect;
import com.gdgl.net.tcp.SocketCallback;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.util.NetUtil;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class EnergyService extends Service{
	private static final String TAG = "EnergyService";
//	private static final String IP = "192.168.1.124";
//	private static final int PORT = 8000;
	private String IP = "192.168.1.137";
	private static final int PORT = 5030;
	private static final String HEARTSTRING = "{\"MsgType\":"+EnergyModel.HEART_SEND+",\"Sn\":10}";
	private static final int HEARTTIME = 30000;
	private SocketConnect connect;
	private Thread mConnectThread;
	private Thread mHeartThread;
	private boolean isFirst = true;
	private boolean isStop = false;
	
	DataHelper mDataHelper = new DataHelper(ApplicationController.getInstance());
	SQLiteDatabase mSQLiteDatabase;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onCreate(){
		Log.i(TAG, "EnergyService onCreate");
	}
	
	
	public int onStartCommand(Intent intent, int flags, int startId) {  
		Log.i(TAG, "EnergyService onStartCommand");
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle == null){
				initConnect();
			}
		}
		
		return super.onStartCommand(intent, flags, startId);
    }  
	
	public void onStart(Intent intent, int startId){
		Log.i(TAG, "EnergyService onStart");
		sendMessage(intent);
	}
	
	private void initConnect(){
		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
			if(connect == null){
				connect = new SocketConnect(new SocketCallback() {
		             
		            @Override
//		            public void receive(byte[] buffer) {
//		                System.out.println("Server Message ：" + new String(buffer));
//		                replyMessage(new String(buffer));
//		                EnergyManager.getInstance().handleMessage(new String(buffer));
//		            }
		            public void receive(String message) {
		                System.out.println("Server Message ：" + message);
		                replyMessage(message);
		                EnergyManager.getInstance().handleMessage(message);
		            }
		             
		            @Override
		            public void disconnect() {
		            	System.out.println("SocketConnect ：disconnect");
		            	//stopHeart();
		            }
		             
		            @Override
		            public void connected() {
		            	System.out.println("SocketConnect ：connected");
		            	startHeart();
		            }
		        });
				String IP = NetUtil.getInstance().getGatewayIP();
		        connect.setRemoteAddress(IP, PORT);
		        if(mConnectThread == null){
		        	mConnectThread = new Thread(connect);
		        	mConnectThread.start();
		        }
		        //new Thread(connect).start();   
			}
			loadData();
		}else{
			if(isFirst){
				//loadData();
		        isFirst = false;
			}
			
		}
		
	}
	
	private void loadData(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mSQLiteDatabase = mDataHelper.getWritableDatabase();
				mDataHelper.delete(mSQLiteDatabase, DataHelper.ENERGY_TABLE, null, null);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sendMessage(EnergyModel.GET_METER_LIST_SEND); 
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sendMessage(EnergyModel.GET_DEVICES_SEND);
			}
		}).start();
	}
	
	private void stopHeart(){
		isStop = true;
	}
	
	private void startHeart(){
		isStop = false;
		if(mHeartThread == null){
			mHeartThread = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(!isStop){
						
						Log.i(TAG, "SEND HEARTSTRING");
						try {
							Thread.sleep(HEARTTIME);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						connect.write(HEARTSTRING.getBytes());
					}
					Log.i(TAG, "Thread finish");
				}
			});
			mHeartThread.start();
		}
	}
	
	private void connectWrite(String msg){
		Log.i("connectWrite", "="+msg);
		connect.write(msg.getBytes());
	}
	
	public void onDestroy(){
		stopHeart();
		if(connect != null){
			connect.disconnect();
		}
		super.onDestroy();
	}
	
	public void sendMessage(Intent intent){
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				String json = bundle.getString(EnergyModel.JSONSTRING);
				if(json != null && !json.equals("")){
					if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
						connectWrite(json.toString());
					}else if(NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET){
						LibjingleSendManager.getInstance().energySendMessage(json.toString());
					}
				}
			}
		}
	}
	
	public void sendMessage(int MsgType){
		
		JSONObject json = new JSONObject();
		try {
			json.put("MsgType", MsgType);
			json.put("Sn", 10);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
			connectWrite(json.toString());
		}else if(NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET){
			LibjingleSendManager.getInstance().energySendMessage(json.toString());
		}
	}
	
	/**
     * 处理安防回复信息
     * 
     * @throws 
     */
    public void replyMessage(String msg){
		try {
			JSONObject json = new JSONObject(msg);
			int MsgType = json.getInt(EnergyModel.MSGTYPE);
			switch(MsgType){		
				//开关状态广播
				case EnergyModel.RECEIVE_ONOFF_BACK:		
					//sendMessage(EnergyModel.RECEIVE_ONOFF_SEND);
					break;
				//传感器状态广播
				case EnergyModel.RECEIVE_SENSOR_BACK:
					//sendMessage(EnergyModel.RECEIVE_SENSOR_SEND);
					break;
				//全局布防广播
				case EnergyModel.RECEIVE_ALL_DEFENSE_BACK:	
					//sendMessage(EnergyModel.RECEIVE_ALL_DEFENSE_SEND);
					break;
				//设备布防广播
				case EnergyModel.RECEIVE_DEFENSE_BACK:		
					//sendMessage(EnergyModel.RECEIVE_DEFENSE_SEND);
					break;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}