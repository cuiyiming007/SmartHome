package com.gdgl.libjingle;

public class LibjingleInit {

	static {
		System.loadLibrary("ssl");
		System.loadLibrary("crypto");
		System.loadLibrary("srtp");
		System.loadLibrary("jingle");
		System.loadLibrary("jingle_client");
	}

	public native int libjinglInit(String jid, String pwd, int netstatus, String serverip);
}
