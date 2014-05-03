package com.gdgl.reciever;

import java.io.IOException;

import com.gdgl.mydata.Constants;
import com.gdgl.util.NetUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HeartReceiver extends BroadcastReceiver {
	 
    private static final String TAG = "HeartReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.ACTION_START_HEART.equals(action)) {
                    Log.d(TAG, "Start heart");
            } else if (Constants.ACTION_HEARTBEAT.equals(action)) {
                    Log.d(TAG, "Heartbeat");
                    try {
						NetUtil.getInstance().sendHeartBeat();
					} catch (IOException e) {
						e.printStackTrace();
					}
                    //在此完成心跳需要完成的工作，比如请求远程服务器……
            } else if (Constants.ACTION_STOP_HEART.equals(action)) {
                    Log.d(TAG, "Stop heart");
            }
    }

}
