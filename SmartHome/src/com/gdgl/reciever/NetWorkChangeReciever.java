package com.gdgl.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.widget.Toast;

public class NetWorkChangeReciever extends BroadcastReceiver {
	State wifiState = null;  
    State mobileState = null;  
    public static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";  
    @Override  
    public void onReceive(Context context, Intent intent) {  
        // TODO Auto-generated method stub  
        if (ACTION.equals(intent.getAction())) {  
            //获取手机的连接服务管理器，这里是连接管理器类  
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);    
            wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();      
            mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();  
      
           // Intent intent2 = new Intent(context , BroadCastActivity2_SMS.class);  
            if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED == mobileState) {  
               // context.startService(intent2);  
                Toast.makeText(context, "手机网络连接成功！", Toast.LENGTH_SHORT).show();  
            } else if (wifiState != null && mobileState != null && State.CONNECTED == wifiState && State.CONNECTED != mobileState) {  
              //  context.startService(intent2);  
                Toast.makeText(context, "无线网络连接成功！", Toast.LENGTH_SHORT).show();  
            } else if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED != mobileState) {  
               // context.startService(intent2);  
                Toast.makeText(context, "手机没有任何网络...", Toast.LENGTH_SHORT).show();  
            }  
        }  
    }  
  
}
