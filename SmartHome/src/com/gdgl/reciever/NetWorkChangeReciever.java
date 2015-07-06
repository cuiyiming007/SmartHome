package com.gdgl.reciever;

import java.util.Vector;

import com.gdgl.drawer.MainActivity;
import com.gdgl.libjingle.Libjingle;
import com.gdgl.libjingle.LibjinglePackHandler;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.service.LibjingleService;
import com.gdgl.service.SmartService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetWorkChangeReciever extends BroadcastReceiver {
	private Vector<UIListener> observers = new Vector<UIListener>();

	public static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	
	private boolean startWork = false; // 注册Reciever时不执行动作(第一次不执行)

	public synchronized void addObserver(UIListener o) {
		if (o == null) {
			throw new NullPointerException();
		}
		if (!observers.contains(o)) {
			observers.addElement(o);
		}
	}

	public synchronized void deleteObserver(UIListener o) {
		observers.removeElement(o);
	}

	public void notifyObservers(Object data) {
		for (int i = 0; i < observers.size(); i++) {
			try {
				((UIListener) observers.elementAt(i)).update(
						CGIManager.getInstance(), data);
			} catch (Exception e) {
				Log.e("Manger", "notifyObservers exception" + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (ACTION.equals(intent.getAction())) {
			if (startWork && MainActivity.LOGIN_STATUS) {
				NetworkConnectivity.networkStatus = NetworkConnectivity
						.getInstance().getConnecitivityNetwork();
				NetworkConnectivity.netStatusLastTime = NetworkConnectivity.networkStatus;
			}
			if (startWork && !MainActivity.LOGIN_STATUS) {

				NetworkConnectivity.networkStatus = NetworkConnectivity
						.getInstance().getConnecitivityNetwork();
				Log.i("networkStatus", NetworkConnectivity.networkStatus + "");
				Log.i("netStatusLastTime",
						NetworkConnectivity.netStatusLastTime + "");
				getFromSharedPreferences.setsharedPreferences(context);

				switch (NetworkConnectivity.networkStatus) {
				case NetworkConnectivity.LAN:
					if (NetworkConnectivity.netStatusLastTime == NetworkConnectivity.INTERNET) {
//						Intent libserviceIntent = new Intent(context,
//								LibjingleService.class);
//						context.stopService(libserviceIntent);
						Libjingle.getInstance().stop();
						Intent smartServiceIntent = new Intent(context,
								SmartService.class);
						context.startService(smartServiceIntent);
					} else {
						Intent smartServiceIntent = new Intent(context,
								SmartService.class);
						context.startService(smartServiceIntent);
					}
					NetworkConnectivity.netStatusLastTime = NetworkConnectivity.LAN;
					
					Event event = new Event(EventType.NETWORKCHANGE, true);
					event.setData(NetworkConnectivity.networkStatus);
					notifyObservers(event);
					break;
				case NetworkConnectivity.INTERNET:
					if (NetworkConnectivity.netStatusLastTime == NetworkConnectivity.INTERNET) {
						String name = getFromSharedPreferences.getName();
						String pwd = getFromSharedPreferences.getPwd();
						Libjingle.getInstance().login(
								LibjinglePackHandler.getJid(), name + pwd,
								"121.199.21.14:5222");
					} else {
						Intent smartServiceIntent = new Intent(context,
								SmartService.class);
						context.stopService(smartServiceIntent);
						Intent libserviceIntent = new Intent(context,
								LibjingleService.class);
						context.startService(libserviceIntent);
					}
					NetworkConnectivity.netStatusLastTime = NetworkConnectivity.INTERNET;
					break;
				default:
					Event event_nonetwork = new Event(EventType.NETWORKCHANGE, true);
					event_nonetwork.setData(NetworkConnectivity.networkStatus);
					notifyObservers(event_nonetwork);
					break;
				}
			}
//			MainActivity.LOGIN_STATUS = false;
			startWork = true;
		}
	}

}
