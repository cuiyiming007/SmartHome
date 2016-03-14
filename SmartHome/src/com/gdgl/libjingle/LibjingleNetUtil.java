package com.gdgl.libjingle;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Looper;
import android.util.Log;

public class LibjingleNetUtil {

	private final static String TAG = "LibjingleNetUtil";

	private static LibjingleNetUtil instance;
	
//	public final static String IP_Server = "121.199.21.14"; //测试服务器
	public final static String IP_Server = "120.25.206.170"; //正式服务器
	public final static String IP_WithHttpHeader = "http://" + IP_Server;

	public static Socket tcpSocket;
	public InputStream inputStream;
	public DataInputStream dataInputStream;
	public OutputStream outputStream;
	private Boolean recieveMsgFlag = false;

	public static LibjingleNetUtil getInstance() {
		if (instance == null) {
			instance = new LibjingleNetUtil();
		}
		return instance;
	}

	public String getLocalhostURL(String resource, String param) {
		String LocalhostStr = "http://localhost/cgi-bin/rest/network/";
		return LocalhostStr + resource + "?" + param;
	}

	public String getVideoURL(String resource) {
		String LocalhostStr = "http://localhost/cgi-bin/rest/network/";
		return LocalhostStr + resource;
	}

	public void startLibjingleSocket() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(500);
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
		recieveMsgFlag = false;
		try {
			if (tcpSocket != null) {
				inputStream.close();
				outputStream.close();
				tcpSocket.close();
				tcpSocket = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void connectLibjingleSocket() {
		try {
			tcpSocket = new Socket("127.0.0.1", 5020);
			Log.i(TAG,
					"getReceiveBufferSize" + tcpSocket.getReceiveBufferSize());
			inputStream = tcpSocket.getInputStream();
			outputStream = tcpSocket.getOutputStream();
			dataInputStream = new DataInputStream(inputStream);
			recieveMsgFlag = true;
			Log.i(TAG, "connectLibjingleSocketsuccessful");
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "connectLibjingleSocket error：" + e.getMessage());
		}
	}

	public void sendMsgToLibjingleSocket(String msg) {
		byte buffer[] = msg.getBytes();
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
			while (recieveMsgFlag) {
				if (dataInputStream != null && dataInputStream.available() > 0) {
					LibjingleResponseHandlerManager
							.handleInputStream(dataInputStream);
					// Log.i("callbakcSocket recieve", inputStream.toString());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG, "RecieveFromLibjingleSocket error：" + e.getMessage());
			recieveFromLibjingleSocket();
		}
	}

}
