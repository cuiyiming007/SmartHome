package com.gdgl.mydata;

public class LinkageCnd{
	private String name = "";
	private String mark = "";
	private String data = "";
	
	public LinkageCnd(){
		
	}
	
	public LinkageCnd(String cnddata){
		if(cnddata != null){
			String[] cnd = cnddata.split("@");
			name = cnd[0];
		    mark = cnd[1];
			data = cnd[2];
		}
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getMark(){
		return mark;
	}
	
	public void setMark(String mark){
		this.mark = mark;
	}
	
	public String getData(){
		return data;
	}
	
	public void setData(String data){
		this.data = data;
	}
	
	//cnd api数据转换成文字信息
	public String getCndString(){
		String cndString = "未定义";
		if(!mark.equals("")){
			cndString = name;
			if(name.equals("temp")){
				if(mark.equals("eq")){
					cndString += "等于";
				}else if(mark.equals("bt")){
					cndString += "大于";
				}else if(mark.equals("lt")){
					cndString += "小于";			
				}else if(mark.equals("be")){
					cndString += "大于等于";
				}else if(mark.equals("le")){
					cndString += "小于等于";
				}
			}else if(name.equals("Fire")){
				cndString += "eq";
			}else if(name.equals("pass")){
				cndString += "eq";
			}
			cndString += data;
		}
		return cndString;
	}
}
