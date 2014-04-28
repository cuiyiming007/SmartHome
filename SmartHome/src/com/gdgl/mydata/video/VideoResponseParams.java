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
