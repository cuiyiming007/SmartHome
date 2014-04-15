package com.gdgl.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.gdgl.manager.DeviceManager;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.ResponseParamsEndPoint;

public class SmartService extends Service {
	
	public final static String TAG="SmartService" ;

//	private Intent brodcastIntent = new Intent("com.gdgl.activity.RECIEVER");

	public IBinder onBind(Intent intent) {
		return null;
	}

	public void initial() {
//		DeviceManager.getInstance().getDeviceList();
		
		new Thread(){
			@Override
			public void run() {
				DataHelper mDateHelper = new DataHelper(SmartService.this);
				 SQLiteDatabase mSQLiteDatabase = mDateHelper
				 .getSQLiteDatabase();
				 List<DevicesModel> mList = mDateHelper.queryForList(
					 mSQLiteDatabase, DataHelper.DEVICES_TABLE, null, null,
					 null, null, null, null, null);
				 if (mList.size()<=0) {
					 ArrayList<ResponseParamsEndPoint> devDataList = DeviceManager
							 .getInstance().getDeviceListFromLocalString();
					
					 mDateHelper.insertList(mSQLiteDatabase,
							 DataHelper.DEVICES_TABLE, null, devDataList);
				}
				 mDateHelper.close(mSQLiteDatabase);
			}
		}.run();
		
	}

	public class MsgBinder extends Binder {
		/**
		 * ��ȡ��ǰService��ʵ��
		 * 
		 * @return
		 */
		public SmartService getService() {
			return SmartService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.i(TAG,"SmartHome service starts!");
		initial();
//		sendBroadcast(brodcastIntent);
		return super.onStartCommand(intent, flags, startId);
	}

}
