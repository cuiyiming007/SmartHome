package com.gdgl.service;

import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.VideoManager;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.reciever.HeartReceiver;
import com.gdgl.util.NetUtil;

public class SmartService extends Service {

	public final static String TAG = "SmartService";
	public final static int HEARTBEAT_INTERVAL = 40000;

	// private Intent brodcastIntent = new Intent("com.gdgl.activity.RECIEVER");

	public IBinder onBind(Intent intent) {
		return null;
	}

	public void initial() {
		
		//判断网关是否和客户端在同一局域网下面
		NetUtil.getInstance().connectServerWithUDPSocket();
		NetUtil.getInstance().recieveFromUdp();
		
		
		// =============================server======================
		 DeviceManager.getInstance().getDeviceList();
		 VideoManager.getInstance().getIPClist();
		// ===============================loacl=====================
//		new Thread() {
//			@Override
//			public void run() {
//				DataHelper mDateHelper = new DataHelper(SmartService.this);
//				SQLiteDatabase mSQLiteDatabase = mDateHelper
//						.getSQLiteDatabase();
//				List<DevicesModel> mList = mDateHelper.queryForList(
//						mSQLiteDatabase, DataHelper.DEVICES_TABLE, null, null,
//						null, null, null, null, null);
//				if (mList.size() <= 0) {
//					ArrayList<ResponseParamsEndPoint> devDataList = DeviceManager
//							.getInstance().getDeviceListFromLocalString();
//
//					mDateHelper.insertList(mSQLiteDatabase,
//							DataHelper.DEVICES_TABLE, null, devDataList);
//				}
//				mDateHelper.close(mSQLiteDatabase);
//			}
//		}.run();

		CallbackManager.getInstance().startConnectServerByTCPTask();
//		CallbackManager.getInstance().connectAndRecieveFromCallback();
		startHB();
//		 CallbackManager.getInstance().startCallbackTask();
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

		Log.i(TAG, "SmartHome service starts!");
		initial();
		// sendBroadcast(brodcastIntent);
		return super.onStartCommand(intent, flags, startId);
	}

	public void startHB() {
		AlarmManager mAlarmManager;
		PendingIntent mPendingIntent;

		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent intent=new Intent(this,HeartReceiver.class);
		intent.setAction(Constants.ACTION_HEARTBEAT);
		mPendingIntent = PendingIntent.getBroadcast(this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// 启动心跳定时器
		long triggerAtTime = SystemClock.elapsedRealtime() + HEARTBEAT_INTERVAL;
		mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				triggerAtTime, HEARTBEAT_INTERVAL, mPendingIntent);
	}
}
