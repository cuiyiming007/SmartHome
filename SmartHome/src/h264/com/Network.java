package h264.com;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;

import org.json.JSONException;
import org.json.JSONObject;

import com.gdgl.util.NetUtil;

import android.provider.SyncStateContract.Constants;
import android.util.Log;

public class Network {
	private final static String TAG = "VedioNetWorkUtil";
	public static String IPserver = NetUtil.getInstance().IP;
	// private static int udpPort = 5000;
	public static int tcpPort = 5012;
	public static Socket socket; // tcpSocket
	public static DatagramSocket udpSocket = null; // udpSocket
	public static Timer timer;

	public static void recieveVideoFromUdp() {
		byte data[] = new byte[10240];
		DatagramPacket receivePack = new DatagramPacket(data, data.length);
		while (true) {
			try {
				Network.udpSocket.receive(receivePack);
				// videoView.dataCatcher(receivePack.getData(),
				// NetUtil.udpSocket.getReceiveBufferSize());
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	// ͨ��udp������Ϣ
	public static void sendUdpMsg(String message) {
		// message = (message == null ? "Hello IdeasAndroid!" : message);
		/*
		 * try { udpSocket = new DatagramSocket(); } catch (SocketException e) {
		 * e.printStackTrace(); } InetAddress address = null; try { //
		 * ���ɷ�������IP address = InetAddress.getByName(IPserver); } catch
		 * (UnknownHostException e) { e.printStackTrace(); } // int msg_length =
		 * message.length(); // byte[] messageByte = message.getBytes(); //
		 * ��ʼ����Ϣ byte[] sendBuffer1 = new byte[128]; int i; for (i = 0; i <
		 * 128; i++) { sendBuffer1[i] = 0x05; } DatagramPacket p = new
		 * DatagramPacket(sendBuffer1, sendBuffer1.length, address, udpPort);
		 * try { udpSocket.send(p); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
	}

	public static void setIPMessage(String ip, int port) {
		IPserver = ip;
		tcpPort = port;
	}

	public static void connectServer() {
		Socket so = null;
		try {
			// so = new Socket(ip, Integer.parseInt(port));
			so = new Socket();
			InetSocketAddress isa = new InetSocketAddress(IPserver, tcpPort);
			so.connect(isa, 5000);
			if (null != so) {
				Network.socket = so;
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
		if (socket!=null&&socket.isConnected()) {
			return true;
		}else {
			return false;
		}
	}
	public static void closeVideoSocket()
	{
		if (socket!=null) {
			try {
				socket.close();
				socket=null;
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, "close socket faild");
			}
		}
	}

	public static boolean sendVideoReq(int channelNum) {

		byte[] sendBuffer1;
		JSONObject videoCommand = new JSONObject();
		try {
			videoCommand.put("action", "startvideo");
			videoCommand.put("channel", channelNum);
			int j = videoCommand.toString().getBytes().length;
			sendBuffer1 = new byte[j];
			sendBuffer1 = videoCommand.toString().getBytes();
		} catch (JSONException ex) {
			return false;
		}

		try {
			if (socket != null) {
				DataOutputStream out = new DataOutputStream(
						socket.getOutputStream());
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

//	public static void connectServerWithTCPSocket() {
//		String heartbeat = "0200070007";
//		Socket socket;
//		try {// 创建一个Socket对象，并指定服务端的IP及端口号
//			socket = new Socket("192.168.1.239", 5002);
//			OutputStream outputStream = socket.getOutputStream();
//			byte buffer[] = heartbeat.getBytes();
//			int temp = 0;
//			outputStream.write(buffer, 0, buffer.length);
//			// 发送读取的数据到服务端
//			outputStream.flush();
//
//			InputStream inputStream = socket.getInputStream();
//			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//			byte[] data = new byte[1024];
//			int count = -1;
//			while ((count = inputStream.read(data, 0, 1024)) != -1) {
//				outStream.write(data, 0, count);
//			}
//			data = null;
//			String result = new String(outStream.toByteArray());
//			Log.i("socket recieve", result);
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
}
