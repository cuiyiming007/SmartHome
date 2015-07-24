package com.gdgl.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.gdgl.drawer.MainActivity;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.SceneLinkageManager;
import com.gdgl.manager.VideoManager;
import com.gdgl.mydata.Constants;
import com.gdgl.util.NetUtil;

public class SmartService extends Service {
	AlarmManager mAlarmManager;
	PendingIntent mPendingIntent;

	public final static String TAG = "SmartService";
	public final static int HEARTBEAT_INTERVAL = 20000;

	// private Intent brodcastIntent = new Intent("com.gdgl.activity.RECIEVER");
	private IntentFilter intentFilter;
	private HeartReceiver heartReceiver;

	public IBinder onBind(Intent intent) {
		return null;
	}

	public void initial() {
		if (MainActivity.LOGIN_STATUS) {
			startLANService();
		}
		// ===============================loacl=====================
		CallbackManager.getInstance().startConnectServerByTCPTask();
		startHB();
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

		Log.i(TAG, "SmartHome service onStartCommand!");
		initial();
		intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.ACTION_HEARTBEAT);
		heartReceiver = new HeartReceiver();
		registerReceiver(heartReceiver, intentFilter);
		// sendBroadcast(brodcastIntent);
		// return super.onStartCommand(intent, flags, startId);
		return START_NOT_STICKY;
	}

	// public void onStart(Intent intent, int startId) {
	// Log.i(TAG, "SmartHome service onStart!");
	// initial();
	// }

	public void startLANService() {
		// =============================server======================
		DeviceManager.getInstance().getDeviceEndPoint();
		VideoManager.getInstance().getIPClist();
		SceneLinkageManager.getInstance().GetSceneList();
		SceneLinkageManager.getInstance().GetTimeActionList();
		SceneLinkageManager.getInstance().GetLinkageList();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				CGIManager.getInstance().GetLocalIASCIEOperation();
				CGIManager.getInstance().getGatewaySwVersion();
				DeviceManager.getInstance().getLocalCIEList();
			}
		}).start();
	}

	public void startHB() {
		if (mAlarmManager != null) {
			return;
		}

		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		// Intent intent = new Intent(this, HeartReceiver.class);
		// intent.setAction(Constants.ACTION_HEARTBEAT);
		Intent intent = new Intent(Constants.ACTION_HEARTBEAT);
		mPendingIntent = PendingIntent.getBroadcast(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// 启动心跳定时器
		long triggerAtTime = SystemClock.elapsedRealtime() + HEARTBEAT_INTERVAL;
		mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
				triggerAtTime, HEARTBEAT_INTERVAL, mPendingIntent);
	}

	public void onDestroy() {
		// TODO Auto-generated method stub
		mAlarmManager.cancel(mPendingIntent);
		// CallbackManager.getInstance().stopConnectServerByTCPTask();
		Log.i("SmartService", "SmartService onDestroy!");
		unregisterReceiver(heartReceiver);
		super.onDestroy();
	}

	class HeartReceiver extends BroadcastReceiver {

		private static final String TAG = "HeartReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			NetUtil.getInstance().sendHeartBeat();
			Log.i(TAG, "");
		}

	}

}
