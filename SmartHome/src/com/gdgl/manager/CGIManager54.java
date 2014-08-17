package com.gdgl.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.widget.Toast;

import com.cetc54.ndksocket.IReceive;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.network.ChannalManager;

public class CGIManager54 extends Manger {
	private final static String TAG = "CGIManager54";

	private static CGIManager54 instance;

	private static Context mContext = ApplicationController.getInstance();

	private static ChannalManager mChannalManager;

	public static CGIManager54 getInstance() {
		if (instance == null) {
			instance = new CGIManager54();
			mChannalManager = ChannalManager.getInstace(mContext);

		}
		return instance;
	}

	public void mainsOutLetOperationBy54() {
		String str3 = "{\"request_id\":1002,\"gl_msgtype\":3,\"jid\":\"LGP990\",\"send\":{\"url\":\"http://192.168.1.237/cgi-bin/rest/network/mainsOutLetOperation.cgi?ieee=00137A000000DC86&ep=01&operatortype=2&param1=1&param2=2&param3=3&callback=1234&encodemethod=NONE&sign=AAA\"}}";

		byte[] fsdata3 = null;
		try {
			fsdata3 = str3.getBytes("utf-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		long timestamp3 = System.currentTimeMillis();
		byte[] newdata3 = null;
		// if(cpkuse){
		// newdata3=cpk.SymEncrypt(fsdata3);
		// }else{
		newdata3 = fsdata3;

		IReceive i = new IReceive() {

			@Override
			public void receiveData(int type, int status, byte[] datas,
					int len, long timestamp) throws IOException {
				// 处理返回的结果,后台线程

				if (true) {
					Event event = new Event(EventType.BINDDEVICE, true);
					event.setData(null);
					notifyObservers(event);
				} else {
					Event event = new Event(EventType.BINDDEVICE, true);
					event.setData(null);
					notifyObservers(event);
				}
			}
		};
		if (mChannalManager.isInitialed()) {
			mChannalManager.do54(newdata3, i);
		} else {
			Toast.makeText(mContext, "初始化通道失败", Toast.LENGTH_LONG).show();
		}
	}
}
