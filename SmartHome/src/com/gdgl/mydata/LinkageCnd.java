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
	
	//cnd api数据转换成文字信息   -----王晓飞---20150818==湿度
	public String getCndString(){
		String cndString = "未定义";
		if(!mark.equals("")){
			if(name.equals("temp")){
				if(mark.equals("eq")){
					cndString = "温度等于";
				}else if(mark.equals("bt")){
					cndString = "温度大于";
				}else if(mark.equals("lt")){
					cndString = "温度小于";			
				}
				cndString += data + "°C";
			}else if(name.equals("hum")){  //=========添加
				if(mark.equals("eq")){
					cndString = "湿度等于";
				}else if(mark.equals("bt")){
					cndString = "湿度大于";
				}else if(mark.equals("lt")){
					cndString = "湿度小于";			
				}
				cndString += data + "%";
			}
			else if(name.equals("alarm")){
				cndString = "报警";
			}
			
		}
		return cndString;
	}
}
