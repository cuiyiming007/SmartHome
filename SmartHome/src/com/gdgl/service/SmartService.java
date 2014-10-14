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
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.reciever.HeartReceiver;

public class SmartService extends Service {

	public final static String TAG = "SmartService";
	public final static int HEARTBEAT_INTERVAL = 40000;

	// private Intent brodcastIntent = new Intent("com.gdgl.activity.RECIEVER");

	public IBinder onBind(Intent intent) {
		return null;
	}

	public void initial() {

		// 判断网关是否和客户端在同一局域网下面
		int networkStatus = NetworkConnectivity.getInstance()
				.getConnecitivityNetwork();
		switch (networkStatus) {
		case NetworkConnectivity.NO_NETWORK:
			stopSelf();
			break;
		case NetworkConnectivity.INTERNET:
			stopSelf();
			break;
		case NetworkConnectivity.LAN:
			startLANService();
			break;
		default:
			break;
		}

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// ChannalManager.getInstace(SmartService.this).init();
		// }
		// }).start();

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

	public void startLANService() {
		// =============================server======================
		DeviceManager.getInstance().getDeviceEndPoint();
		CGIManager.getInstance().GetAllRoomInfo();
		CGIManager.getInstance().GetAllBindList();
		VideoManager.getInstance().getIPClist();
		// ===============================loacl=====================
		CallbackManager.getInstance().startConnectServerByTCPTask();
		startHB();
	}

	public void startHB() {
		AlarmManager mAlarmManager;
		PendingIntent mPendingIntent;

		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(this, HeartReceiver.class);
		intent.setAction(Constants.ACTION_HEARTBEAT);
		mPendingIntent = PendingIntent.getBroadcast(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// 启动心跳定时器
		long triggerAtTime = SystemClock.elapsedRealtime() + HEARTBEAT_INTERVAL;
		mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				triggerAtTime, HEARTBEAT_INTERVAL, mPendingIntent);
	}
}
