package com.gdgl.video;

import java.io.DataOutputStream;
import java.io.IOException;
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

import android.util.Log;

public class Network 
{
	private final static String TAG = "NetWorkUtil";
	public static String IPserver = "192.168.1.230";
	//private static int udpPort = 5000;
	public static int tcpPort = 5012;
	public static Socket socket; // tcpSocket
	public static DatagramSocket udpSocket = null; // udpSocket
	public static Timer timer;

	public static void recieveVideoFromUdp()
	{
		byte data[] = new byte[10240];
		DatagramPacket receivePack = new DatagramPacket(data, data.length);
		while (true) 
		{
			try 
			{
				Network.udpSocket.receive(receivePack);
				// videoView.dataCatcher(receivePack.getData(),
				// NetUtil.udpSocket.getReceiveBufferSize());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				break;
			}
		}
	}

	// ͨ��udp������Ϣ
	public static void sendUdpMsg(String message)
	{
		// message = (message == null ? "Hello IdeasAndroid!" : message);
/*
		try 
		{
			udpSocket = new DatagramSocket();
		}
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
		InetAddress address = null;
		try
		{
			// ���ɷ�������IP
			address = InetAddress.getByName(IPserver);
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
		// int msg_length = message.length();
		// byte[] messageByte = message.getBytes();
		// ��ʼ����Ϣ
		byte[] sendBuffer1 = new byte[128];
		int i;
		for (i = 0; i < 128; i++) 
		{
			sendBuffer1[i] = 0x05;
		}
		DatagramPacket p = new DatagramPacket(sendBuffer1, sendBuffer1.length,
				address, udpPort);
		try 
		{
			udpSocket.send(p);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		*/
	}

	public static void getIPMessage(String ip, int port)
	{
		IPserver = ip;
		tcpPort = port;
	}
	
	public static boolean connectServer(String ip, int port)
	{
		Socket so = null;
		IPserver = ip;
		tcpPort = port;
		try
		{
			// so = new Socket(ip, Integer.parseInt(port));
			so = new Socket();
			InetSocketAddress isa = new InetSocketAddress(ip, port);
			so.connect(isa, 5000);
			if (null != so)
			{
				Network.socket = so;
				// sendVideoTCPReq();
			}
		} 
		catch (NumberFormatException e)
		{
			Log.e(TAG, "NumberFormatException:" + e.getMessage());
			return false;
		}
		catch (UnknownHostException e)
		{
			Log.e(TAG, "UnknownHostException:" + e.getMessage());
			return false;
		} 
		catch (IOException e)
		{
			Log.e(TAG, "IO:" + e.getMessage());
			return false;
		}

		return true;
	}

	
	
	public static boolean sendVideoTCPReq(int channelNum) 
	{
		//������Ƶ
		/*
		byte[] sendBuffer1 = new byte[128];
		byte chann = (byte) (0xff & (channelNum));
		// byte chann=(byte) (channelNum<<2);
		for (int i = 0; i < 40; i++) 
		{
			sendBuffer1[i] = chann;
		}
		sendBuffer1[0] = 0x55;
		sendBuffer1[1] = 0x55;
		sendBuffer1[2] = 0x55;
		sendBuffer1[3] = 0x55;
		sendBuffer1[4] = 0x01;
		*/
		
		byte[] sendBuffer1;
		JSONObject videoCommand = new JSONObject();
		try
		{
			videoCommand.put("action","startvideo");  
			videoCommand.put("channel",channelNum);
			int j = videoCommand.toString().getBytes().length;
			sendBuffer1 = new byte[j];
			sendBuffer1 = videoCommand.toString().getBytes();
		}
		catch (JSONException ex) 
		{
			return false;
		}
        
		try 
		{
			if (socket!=null) 
			{
				DataOutputStream out = new DataOutputStream(
						socket.getOutputStream());
				out.write(sendBuffer1);
				return true;
			}
			else 
			{
				return false;
			}
		} 
		catch (IOException e) 
		{
			Log.e(TAG, "sendVideoTCPReq " + e.getMessage());
			return false;
		}
	}
}
