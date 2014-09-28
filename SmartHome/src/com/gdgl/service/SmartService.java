package com.gdgl.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.gdgl.manager.CGIManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.VideoManager;
import com.gdgl.mydata.Constants;
import com.gdgl.network.ChannalManager;
import com.gdgl.reciever.HeartReceiver;

public class SmartService extends Service {

	public final static String TAG = "SmartService";
	public final static int HEARTBEAT_INTERVAL = 40000;

	// private Intent brodcastIntent = new Intent("com.gdgl.activity.RECIEVER");

	public IBinder onBind(Intent intent) {
		return null;
	}

	public void initial() {
		
		//判断网关是否和客户端在同一局域网下面
		/***
		 * 执行这个语句，在没网的情况下，会导致设备模块里面读取数据库的AsyncTask.doInBackground方法不执行，估计是线程池中线程已经满了
		 * 在没网的情况下，该任务会阻塞线程
		 * http://blog.csdn.net/mddy2001/article/details/17127065
		 */
//		new AsyncTask<Object, Object, Object>() {
//
//			@Override
//			protected Object doInBackground(Object... params) {
//				NetUtil.getInstance().connectServerWithUDPSocket();
//				NetUtil.getInstance().recieveFromUdp();
//				return null;
//			}
//		}.execute(0);
		
		
		// =============================server======================
		 DeviceManager.getInstance().getDeviceEndPoint();
		 CGIManager.getInstance().GetAllRoomInfo();
		 CGIManager.getInstance().GetAllBindList();
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
		startHB();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ChannalManager.getInstace(SmartService.this).init();
			}
		}).start();
	
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
