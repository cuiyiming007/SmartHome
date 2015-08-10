package com.gdgl.libjingle;

import com.gdgl.app.ApplicationController;
import com.gdgl.drawer.MainActivity;
import com.gdgl.manager.Manger;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.network.NetworkConnectivity;

import android.util.Log;

public class Libjingle extends Manger {

	private static Libjingle instance;

	public static Libjingle getInstance() {
		if (instance == null) {
			instance = new Libjingle();
		}
		return instance;
	}

	public final static String TAG = "Libjingle Class";

	static {
		System.loadLibrary("ssl");
		System.loadLibrary("crypto");
		System.loadLibrary("srtp");
		System.loadLibrary("jingle");
		System.loadLibrary("jingle_client");
	}

	/** XMPP会话状态定义 */
	public class XmppState {
		public final static int NONE = 0; // !< Nonexistent state.
		public final static int START = 1; // !< Initial state.
		public final static int OPENING = 2; // !< Exchanging stream headers,
												// authenticating and so on.
		public final static int OPEN = 3; // !< Authenticated and bound.
		public final static int CLOSED = 4; // !< Session closed, possibly due
											// to error.
	}

	/** XMPP会话错误定义 */
	public class XmppError {
		public final static int NONE = 0; // !< No error
		public final static int XML = 1; // !< Malformed XML or encoding error
		public final static int STREAM = 2; // !< XMPP stream error - see
											// GetStreamError()
		public final static int VERSION = 3; // !< XMPP version error
		public final static int UNAUTHORIZED = 4; // !< User is not authorized
													// (rejected credentials)
		public final static int TLS = 5; // !< TLS could not be negotiated
		public final static int AUTH = 6; // !< Authentication could not be
											// negotiated
		public final static int BIND = 7; // !< Resource or session binding
											// could not be negotiated
		public final static int CONNECTION_CLOSED = 8; // !< Connection closed
														// by output handler.
		public final static int DOCUMENT_CLOSED = 9; // !< Closed by
														// </stream:stream>
		public final static int SOCKET = 10; // !< Socket error
		public final static int NETWORK_TIMEOUT = 11; // !< Some sort of timeout
														// (eg., we never got
														// the roster)
		public final static int MISSING_USERNAME = 12; // !< User has a Google
														// Account but no
														// nickname
	}

	/** 网关在线状态定义 */
	public class GatewayState {
		public final static int OFFLINE = 0;
		public final static int ONLINE = 1;
	}

	/**
	 * 功能简介：初始化libjingle 注意事项：需在线程中运行
	 * 
	 * @return 0：如果初始化成功则什么都不返回，在退出时回返回0 -1：初始化失败，无法找到Native对象对应的Java类
	 */
	public native int init();

	/**
	 * 功能简介：登录XMPP服务器 注意事项：此方法可在libjingle初始化成功后重复调用
	 * 
	 * @param jid
	 *            ：客户端登陆所使用的JID
	 * @param pwd
	 *            ：登陆密码
	 * @param hostPort
	 *            ：XMPP服务器域名/IP和端口号，格式必须为 “域名/IP:端口号”
	 * @return 0：开始尝试登陆XMPP服务器 -1：hostPort参数不可用
	 */
	public native int login(String jid, String pwd, String hostPort);

	/**
	 * 功能简介：结束XMPP会话，并停止向服务器发送心跳，不再尝试重新登陆 注意事项：调用此方法后，libjingle不会退出，会继续在后台运行
	 * 
	 * @return 此方法总是返回0
	 */
	public native int stop();

	/**
	 * 功能简介：退出登陆，释放资源，然后结束libjingle线程 注意事项：此方法调用成功后，init()方法会执行完成并返回0
	 * 
	 * @return 此方法总是返回0
	 */
	public native int exit();

	/**
	 * 功能简介：发送数据到网关
	 * 
	 * @param msg
	 *            ：要发送的数据（以JSON串的形式）
	 * @return 0：发送成功 -1：XMPP会话未建立成功 -2：网关不在线
	 */
	public native int sendToGateway(String msg);

	/**
	 * 功能简介：发送数据到XMPP服务器
	 * 
	 * @param msg
	 *            ：要发送的数据（以JSON串的形式）
	 * @return 0：发送成功 -1：XMPP会话未建立成功
	 */
	public native int sendToServer(String msg);

	public void onXmppStateChange(int stat, int err) {
		switch (stat) {
		case XmppState.NONE:
			Log.i(TAG, "=====> XMPP State: NONE");
			// TODO
			break;
		case XmppState.START:
			Log.i(TAG, "=====> XMPP State: START");
			// TODO
			break;
		case XmppState.OPENING:
			Log.i(TAG, "=====> XMPP State: OPENING");
			// TODO
			break;
		case XmppState.OPEN: // XMPP会话建立成功
			Log.i(TAG, "=====> XMPP State: OPEN");
			// TODO
			break;
		case XmppState.CLOSED: // XMPP会话建立失败或结束会话
			Log.i(TAG, "=====> XMPP State: CLOSED  Error: " + err);
			// TODO
			switch (err) {
			case XmppError.NONE:
				Log.i(TAG, "=====> XMPP State: NONE");
				// TODO
				break;
			case XmppError.UNAUTHORIZED:
				Log.i(TAG, "=====> XMPP State: START");
				// TODO
				Event event = new Event(EventType.LIBJINGLE_STATUS, true);
				event.setData(err);
				notifyObservers(event);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	public void onGatewayStateChange(int stat, String mac) {
		switch (stat) {
		case GatewayState.OFFLINE:
			Log.i(TAG, "=====> Gateway Offline");
			// TODO
			break;
		case GatewayState.ONLINE:
			Log.i(TAG, "=====> Gateway Online");
			Log.i(TAG, "=====> MAC: " + mac);
			// TODO
			if (MainActivity.LOGIN_STATUS) {
				getFromSharedPreferences
						.setsharedPreferences(ApplicationController
								.getInstance());
				getFromSharedPreferences.setGatewayMAC(mac);
				Event event = new Event(EventType.LIBJINGLE_STATUS, true);
				event.setData(stat);
				notifyObservers(event);
			} else {
				Event event = new Event(EventType.NETWORKCHANGE, true);
				event.setData(NetworkConnectivity.networkStatus);
				notifyObservers(event);
			}
			break;
		default:
			break;
		}
	}

	/** 返回网关发送给客户端的数据 */
	public void onMsgFromGateway(String msg) {
		Log.i(TAG, "=====> onMsgFromGateway:\n" + msg);
		// TODO
		LibjingleResponseHandlerManager.handle_Json(msg);
	}

	/** 返回服务器发送给客户端的数据（心跳直接在libjingle内部处理，不返回） */
	public void onMsgFromServer(String msg) {
		Log.i(TAG, "=====> onMsgFromServer:\n" + msg);
		// TODO
	}
}
