package com.gdgl.libjingle;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class LibjingleVideoSocket {
		private final static String TAG = "LibjingleVideoSocket";
		public static Socket videosocket; // tcpSocket

		private static int reqID = 0;

		public static int getReqID() {
			reqID = reqID % 65536;
			return reqID++;
		}
		
		public static void connectServer() {
			Socket so = null;
			try {
				// so = new Socket(ip, Integer.parseInt(port));
				so = new Socket();
				InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 5021);
				so.connect(isa, 5000);
				if (null != so) {
					videosocket = so;
					// sendVideoTCPReq();
				}
			} catch (NumberFormatException e) {
				Log.e(TAG, "NumberFormatException:" + e.getMessage());
			} catch (UnknownHostException e) {
				Log.e(TAG, "UnknownHostException:" + e.getMessage());
			} catch (IOException e) {
				Log.e(TAG, "IO:" + e.getMessage());
			}
		}

		public static boolean isVideoSocketConnect() {
			if (videosocket!=null&&videosocket.isConnected()) {
				return true;
			}else {
				return false;
			}
		}
		public static void closeVideoSocket()
		{
			if (videosocket!=null) {
				try {
					videosocket.close();
					videosocket=null;
				} catch (IOException e) {
					e.printStackTrace();
					Log.e(TAG, "close socket faild");
				}
			}
		}

		public static boolean sendVideoReq(int channelNum) {

			byte[] sendBuffer1;
			String jid = LibjinglePackHandler.getJid();
			int reqid = getReqID();
			JSONObject videoCommand = new JSONObject();
			JSONObject send = new JSONObject();
			try {
				send.put("action", "startvideo");
				send.put("channel", channelNum);
				videoCommand.put("request_id", reqid);
				videoCommand.put("gl_msgtype", LibjinglePackHandler.MT_IpcVideo);
				videoCommand.put("jid", jid);
				videoCommand.put("send", send);
				sendBuffer1 = videoCommand.toString().getBytes();
				Log.i(TAG, videoCommand.toString());
			} catch (JSONException ex) {
				return false;
			}

			try {
				if (videosocket != null) {
					DataOutputStream out = new DataOutputStream(
							videosocket.getOutputStream());
					out.write(sendBuffer1);
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				Log.e(TAG, "sendVideoTCPReq " + e.getMessage());
				return false;
			}
		}
}
