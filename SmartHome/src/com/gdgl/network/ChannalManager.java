package com.gdgl.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cetc54.ndksocket.IReceive;
import com.cetc54.ndksocket.NDKSocketAPI;
import com.gdgl.app.ApplicationController;

public class ChannalManager {
	private final static String TAG = "ChannalManager";
	private static ChannalManager instance;

	private static Context mContext;

	private boolean isInitialed = false;

	private NDKSocketAPI api;

	public boolean isInitialed() {
		return isInitialed;
	}

	public void setInitialed(boolean isInitialed) {
		this.isInitialed = isInitialed;
	}

	public static ChannalManager getInstace(Context context) {
		if (instance == null) {
			instance = new ChannalManager();
			mContext = context;
		}
		return instance;
	}

	public void init() {
		api = NDKSocketAPI.getInstance();

		api.setIReceiveData(new IReceive() {

			@Override
			public void receiveData(int type, int status, byte[] datas,
					int len, long timestamp) throws IOException {
				if (type == 1) {
					switch (status) {
					case IReceive.CON_3G_STATUS:
						String txt1 = "当前连接为:3G网络";
						setInitialed(true);
						break;
					case IReceive.CON_WIFI_STATUS:
						String txt2 = "当前连接为:WIFI网络";
						setInitialed(true);
						break;
					case IReceive.NOKEYCON_STATUS:
						String txt3 = "连接不到公网服务器";
						setInitialed(true);
						break;
					case IReceive.NONAMECON_STATUS:
						String txt4 = "网关号不存在";
						break;
					case 4:
						String txt5 = "网关密码错误";
						break;
					}
				}
			}
		});

		// 取本机IP地址
		String ip = ConvertTools.getLocalIpAddress2();// "192.168.2.102";//
		// 取当前网络类型
		int netType = ConvertTools.getAPNType(ApplicationController
				.getInstance());// 0;//

		// 网关号或者别名
		String name = "00000001";
		// 登陆密码
		String key = "00000001";
		// 通道初始化
		api.init(ip, netType, name, key);

	}


	public void do54(byte[] newdata3,IReceive i)
	{
		 new ChanalTask(i).execute(newdata3);
	}
	
	class ChanalTask extends AsyncTask<byte[], Void, String> implements
			IReceive {
		private IReceive i;
		public ChanalTask(IReceive iReceive) {
			i=iReceive;
		}
		@Override
		protected String doInBackground(byte[]... arg0) {
			long timestamp3 = System.currentTimeMillis();
			api.setIReceiveData(i);
			api.SendMsg(1, arg0[0], timestamp3);
			return null;
		}

		@Override
		public void receiveData(int type, int status, byte[] datas, int len,
				long timestamp) throws IOException {
			if (type == 1) {

				return;
			} else if (type == 2) {
			}
		}
	}
}
