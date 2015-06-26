package com.gdgl.service;

import com.gdgl.activity.LoginActivity;
import com.gdgl.app.ApplicationController;
import com.gdgl.libjingle.Libjingle;
import com.gdgl.libjingle.LibjingleNetUtil;
import com.gdgl.libjingle.LibjinglePackHandler;
import com.gdgl.mydata.AccountInfo;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.network.NetworkConnectivity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class LibjingleService extends Service {
	Libjingle libjingle;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		libjingle = Libjingle.getInstance();
		Log.i("LibjingleService", "LibjingleService starts!");
		try {
			new Thread(new Runnable() {

				@Override
				public void run() {
					Log.i("LibjingleService", "LibjingleInit starts!");
					libjingle.init();
					Log.i("LibjingleService", "Libjingle exit!");
				}
			}).start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i("LibjingleService", "LibjingleService onStartCommand!");
		try {
			new Thread(new Runnable() {

				@Override
				public void run() {
					Log.i("LibjingleService", "LibjingleLogin starts!");
					String cloudip = "121.199.21.14:5222";
					getFromSharedPreferences
							.setsharedPreferences(ApplicationController
									.getInstance());
					if (!getFromSharedPreferences.getCloud().equals("")) {
						cloudip = getFromSharedPreferences.getCloud();
					}
					AccountInfo info = LoginActivity.loginAccountInfo;
					String name = info.getAccount();
					String passwd = info.getPassword();
					Log.i("LibjingleService", LibjinglePackHandler.getJid()
							+ "  " + name + passwd + "  " + cloudip
							+ "  networkStatus"
							+ NetworkConnectivity.networkStatus);
					libjingle.login(LibjinglePackHandler.getJid(), name + passwd, cloudip);
//					libjingle.libjinglInit(LibjinglePackHandler.getJid(),
//							name + passwd, NetworkConnectivity.networkStatus,
//							cloudip);
					// libjingleInit
					// .libjinglInit(
					// "ffeeddccbbaa@121.199.21.14/Cabcdefg123456",
					// "FFEEDDCCBBAACCBBAA", 2, "121.199.21.14");
					Log.i("LibjingleService", "LibjingleLogin done!");
				}
			}).start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		LibjingleNetUtil.getInstance().startLibjingleSocket();
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// try {
		// Thread.sleep(5000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// LibjingleSendManager.getInstance().getDeviceEndPoint();
		// }
		// }).start();

		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("LibjingleService", "LibjingleService onDestroy!");
		LibjingleNetUtil.getInstance().initLibjingleSocket();
		System.gc();
		stopSelf();
		super.onDestroy();
	}
}
