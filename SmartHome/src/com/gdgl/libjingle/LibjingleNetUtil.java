package com.gdgl.libjingle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Looper;
import android.util.Log;


public class LibjingleNetUtil {

	private final static String TAG = "LibjingleNetUtil";
	
	private static LibjingleNetUtil instance;
	
	public static Socket tcpSocket;
	public InputStream inputStream;
	public OutputStream outputStream;
	
	public static LibjingleNetUtil getInstance() {
		if (instance == null) {
			instance = new LibjingleNetUtil();
		}
		return instance;
	}
	
	public String getLocalhostURL(String resource, String param) {
		String LocalhostStr="http://localhost/cgi-bin/rest/network/";
		return LocalhostStr + resource + "?" + param;
	}
	
	
	public void startLibjingleSocket() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Looper.prepare();
				connectAndRecieveFromLibjingleSocket();
			}
		}).start();
	}
	
	public void connectAndRecieveFromLibjingleSocket() {
		Log.i(TAG, "start ConnectLibjingleSocket");
		initLibjingleSocket();
		connectLibjingleSocket();
		recieveFromLibjingleSocket();
	}
	
	public void initLibjingleSocket() {
		if (tcpSocket != null) {
			try {
				tcpSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tcpSocket = null;
		}
	}
	
	public void connectLibjingleSocket() {
		try {
			tcpSocket = new Socket("127.0.0.1", 5020);
			Log.i(TAG, "getReceiveBufferSize"+tcpSocket.getReceiveBufferSize());
			inputStream = tcpSocket.getInputStream();
			outputStream = tcpSocket.getOutputStream();
			Log.i(TAG, "connectLibjingleSocketsuccessful");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "connectLibjingleSocket error：" + e.getMessage());
		}
	}
	
	public void sendMsgToLibjingleSocket(String msg) {
		byte buffer[]=msg.getBytes();
		try {
			outputStream.write(buffer);
			outputStream.flush();
		} catch (IOException e) {
			// TODO: handle exception
			Log.e(TAG, "sendMsgToLibjingleSocket failed" + e.getMessage());
		}
		Log.i(TAG, "sendMsgToLibjingleSocket successful");
	}
	
	public void recieveFromLibjingleSocket() {
		try {
			while (true) {
				if (inputStream != null && inputStream.available() > 0) {
					LibjingleResponseHandlerManager.handleInputStream(inputStream);
					// Log.i("callbakcSocket recieve", inputStream.toString());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "RecieveFromLibjingleSocket error：" + e.getMessage());
		}
	}

	
}
