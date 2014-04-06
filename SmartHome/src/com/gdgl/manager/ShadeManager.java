package com.gdgl.manager;

public class ShadeManager {
	private static NodeManager instance;

	public static NodeManager getInstance() {
		if (instance == null) {
			instance = new NodeManager();
		}
		return instance;
	}

	/***
	 * 2.14 ShadeController Operation Function Describe:Features provided to the
	 * curtain controller device: provides distance control feature; feature
	 * that processes and stores information.
	 * http://192.168.1.184/cgi-bin/rest/network/shadeControllerOperation.cgi?
	 * ieee=1234&ep=12&oper
	 * atortype=1&param1=1&param2=2&param3=3&callback=1234&encodemethod
	 * =NONE&sign=A AA
	 */
	public void shadeControllerOperation() {

	}
}
