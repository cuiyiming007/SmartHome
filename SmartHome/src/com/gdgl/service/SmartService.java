package com.gdgl.service;

import java.security.PublicKey;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SmartService extends Service {
	
	public final static String TAG="SmartService" ;

	private Intent brodcastIntent = new Intent("com.gdgl.activity.RECIEVER");

	public IBinder onBind(Intent intent) {
		return null;
	}

	public void initial() {
		// [todo] initial some network status and opreation while app start!
	}

	public class MsgBinder extends Binder {
		/**
		 * 获取当前Service的实例
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
		sendBroadcast(brodcastIntent);
		return super.onStartCommand(intent, flags, startId);
	}

}
