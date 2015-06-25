package com.gdgl.mydata.video;

import java.util.ArrayList;

/***
 * { "action":	"getipclist", 
 * "response_params":	
 * { "status":	0, "status":	0, "status_msg":	"get ipc list success" },
 *  "list":	[
		 *  { "id":	0, "ipc_ipaddr":	"192.168.1.164", "name":	"admin", "password":	"12345", "rtspport":	"554", "httpport":	"81", "aliases":	"camera1" }, 
		 *  { "id":	1, "ipc_ipaddr":	"192.168.1.184", "name":	"admin", "password":	"12345", "rtspport":	"554", "httpport":	"80", "aliases":	"camera2" }, 
		 *  { "id":	2, "ipc_ipaddr":	"192.168.1.194", "name":	"admin", "password":	"12345", "rtspport":	"554", "httpport":	"80", "aliases":	"camera3" }
 *          ]
 *   }
 * @author justek
 *
 */
public class VideoResponse {
	private String action = "";
	private VideoResponseParams response_params;
	private ArrayList<VideoNode> list;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public VideoResponseParams getResponse_params() {
		return response_params;
	}
	public void setResponse_params(VideoResponseParams response_params) {
		this.response_params = response_params;
	}
	public ArrayList<VideoNode> getList() {
		return list;
	}
	public void setList(ArrayList<VideoNode> list) {
		this.list = list;
	}
}
