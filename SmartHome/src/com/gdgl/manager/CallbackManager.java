package com.gdgl.manager;

import java.io.IOException;

import com.gdgl.util.NetUtil;

public class CallbackManager extends Manger {
	private static CallbackManager instance;

	public static CallbackManager getInstance() {
		if (instance == null) {
			instance = new CallbackManager();
		}
		return instance;
	}

	class ConnectServerByTCPTask implements Runnable {
		@Override
		public void run() {
			try {
				NetUtil.getInstance().connectServerWithTCPSocket();
				if (NetUtil.getInstance().isConnectedCallback()) {
					NetUtil.getInstance().sendHeartBeat();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class CallbackTask implements Runnable {

		@Override
		public void run() {
			while (true) {

				try {
					if (NetUtil.getInstance().isConnectedCallback()) {
						NetUtil.getInstance().recieveFromCallback();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}
}
