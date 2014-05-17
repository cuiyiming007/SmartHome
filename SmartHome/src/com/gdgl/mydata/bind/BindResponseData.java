package com.gdgl.mydata.bind;
/***
 * { "request_id":	1234, "response_params":	{ "status":	0, "status_msg":	"success" } }
 * @author justek
 *
 */
public class BindResponseData {

	private String request_id;
	private BindResponse_params response_params;
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public BindResponse_params getResponse_params() {
		return response_params;
	}
	public void setResponse_params(BindResponse_params response_params) {
		this.response_params = response_params;
	}
	
	
	
}
