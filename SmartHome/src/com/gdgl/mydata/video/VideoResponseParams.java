package com.gdgl.mydata.video;

/***
 * "response_params": { "status": 0, "status": 0, "status_msg":
 * "get ipc list success" }
 * 
 * @author justek
 * 
 */
public class VideoResponseParams {

	public static String KEY = "response_params";
	private String status;
	private String status_msg;
	private String ipc_id;
	
	public String getIpc_id() {
		return ipc_id;
	}
	public void setIpc_id(String ipc_id) {
		this.ipc_id = ipc_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus_msg() {
		return status_msg;
	}
	public void setStatus_msg(String status_msg) {
		this.status_msg = status_msg;
	}
	

}
