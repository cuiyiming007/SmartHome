package com.gdgl.service;

import com.gdgl.libjingle.LibjingleInit;
import com.gdgl.libjingle.LibjingleManager;
import com.gdgl.libjingle.LibjingleNetUtil;
import com.gdgl.libjingle.LibjinglePackHandler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class LibjingleService extends Service {
	LibjingleInit libjingleInit;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		libjingleInit = new LibjingleInit();
		Log.i("LibjingleService", "LibjingleService starts!");
		new Thread(new Runnable() {

			@Override
			public void run() {
				Log.i("LibjingleService", "LibjingleInit starts!");
				libjingleInit.libjinglInit("883314EF8B2D@121.199.21.14",
						"883314EF8B2DEF8B2D",
						LibjinglePackHandler.getUUID());
				Log.i("LibjingleService", "LibjingleInit done!");
			}
		}).start();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		LibjingleNetUtil.getInstance().startLibjingleSocket();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LibjingleManager.getInstance().getDeviceEndPoint();
			}
		}).start();
		

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("LibjingleService", "LibjingleService onDestroy!");
		stopSelf();
		super.onDestroy();
	}
}
