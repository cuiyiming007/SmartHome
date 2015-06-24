package com.gdgl.mydata;

public class LinkageAct{
	private static final String[][] ACTPARA = {
		{"撤防", "布防"},
		{"全局撤防", "全局布防"},
		{"关", "开"}
	};
	
	private String type = "";
	private String ieee = "";
	private String ep = "";
	private String arm = "";
	
	public LinkageAct(){
		
	}
	
	public LinkageAct(String actdata){
		if(actdata != null){
			String[] act = actdata.split(":");
			type = act[0];
			if(act[1].indexOf("-") > 0){
				String[] act_data = act[1].split("-");
				ieee = act_data[0];
				ep = act_data[1];
				arm = act_data[2];
			}else{
				ieee = act[1];
			}
		}
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getIeee(){
		return ieee;
	}
	
	public void setIeee(String ieee){
		this.ieee = ieee;
	}
	
	public String getEp(){
		return ep;
	}
	
	public void setEp(String ep){
		this.ep = ep;
	}
	
	public String getArm(){
		return arm;
	}
	
	public void setArm(String arm){
		this.arm = arm;
	}
	
	//act api数据转换成文字信息
	public String getActString(){
		String actString = "未定义";
		if(!type.equals("") && !arm.equals("")){
			actString = ACTPARA[Integer.parseInt(type) - 1][Integer.parseInt(arm)];
		}else{
			actString = "拍照";
		}
		return actString;
	}
}
