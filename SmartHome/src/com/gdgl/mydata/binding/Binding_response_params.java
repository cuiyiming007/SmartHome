package com.gdgl.mydata.binding;

import java.util.ArrayList;

public class Binding_response_params {
	private String ieee;
	private String ep;
	private String count;
	private ArrayList<BindingDivice> list;
	public String getIeee() {
		return ieee;
	}
	public void setIeee(String ieee) {
		this.ieee = ieee;
	}
	public String getEp() {
		return ep;
	}
	public void setEp(String ep) {
		this.ep = ep;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public ArrayList<BindingDivice> getList() {
		return list;
	}
	public void setList(ArrayList<BindingDivice> list) {
		this.list = list;
	}
}
