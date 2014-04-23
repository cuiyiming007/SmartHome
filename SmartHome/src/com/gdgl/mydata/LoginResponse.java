package com.gdgl.mydata;

public class LoginResponse {
	private String id;
	private LoginResponse_params response_params;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LoginResponse_params getResponse_params() {
		return response_params;
	}
	public void setResponse_params(LoginResponse_params response_params) {
		this.response_params = response_params;
	}
}
