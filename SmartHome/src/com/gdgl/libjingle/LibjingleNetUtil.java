package com.gdgl.libjingle;


public class LibjingleNetUtil {

	private static LibjingleNetUtil instance;
	
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
	
}
