package com.gdgl.mydata.video;

/***
 * "list": [ { "id": 0, "ipc_ipaddr": "192.168.1.164", "name": "admin",
 * "password": "12345", "rtspport": "554", "httpport": "81", "aliases":
 * "camera1" }]
 * 
 * @author justek
 * 
 */
public class VideoNode {
	private String id;
	private String ipc_ipaddr;
	private String name;
	private String password;
	private String rtspport;
	private String httpport;
	private String alias;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIpc_ipaddr() {
		return ipc_ipaddr;
	}

	public void setIpc_ipaddr(String ipc_ipaddr) {
		this.ipc_ipaddr = ipc_ipaddr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRtspport() {
		return rtspport;
	}

	public void setRtspport(String rtspport) {
		this.rtspport = rtspport;
	}

	public String getHttpport() {
		return httpport;
	}

	public void setHttpport(String httpport) {
		this.httpport = httpport;
	}

	public String getAliases() {
		return alias;
	}

	public void setAliases(String aliases) {
		this.alias = aliases;
	}

}
