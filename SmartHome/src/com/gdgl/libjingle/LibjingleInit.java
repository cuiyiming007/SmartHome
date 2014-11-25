package com.gdgl.libjingle;

public class LibjingleInit {

	static {
		System.loadLibrary("crypto");
		System.loadLibrary("jingle_client");
		System.loadLibrary("jingle");
		System.loadLibrary("srtp");
		System.loadLibrary("ssl");
	}

	public native int libjinglInit(String name, String pwd, String uuid);
}
