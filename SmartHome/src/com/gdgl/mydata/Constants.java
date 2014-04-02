package com.gdgl.mydata;

public class Constants {
//	1234 ({ "request_id":	12453, "response_params":	[{ "totalcount":	2, "curcount":	1, "node":	{ "ieee":	"00137A00000121F1", "nwk_addr":	"0000", "hw_version":	"0B", "name":	"Combined Interface 1", "date_code":	"20131216", "manufactory":	"netvox", "zcl_version":	"03", "stack_version":	"33", "app_version":	"28", "model_id":	"Z103AE3C", "node_type":	0 } }, { "totalcount":	2, "curcount":	2, "node":	{ "ieee":	"00137A000000DC86", "nwk_addr":	"C9B0", "hw_version":	"0C", "name":	"Mains Power Outlet 1", "date_code":	"20130826", "manufactory":	"netvox", "zcl_version":	"03", "stack_version":	"2F", "app_version":	"15", "model_id":	"Z809AE3R", "node_type":	1 } }] } )
 //{"weatherinfo":{"city":"广州","cityid":"101280101","temp1":"25℃","temp2":"14℃","weather":"晴转多云","img1":"d0.gif","img2":"n1.gif","ptime":"08:00"}}
	public static String jsonStringforNode="1234 ({ \"request_id\":	12453, \"response_params\":	[{ \"totalcount\":	2, \"curcount\":	1, \"node\":	{ \"ieee\":	\"00137A00000121F1\", \"nwk_addr\":	\"0000\", \"hw_version\":	\"0B\", \"name\":	\"Combined Interface 1\", \"date_code\":	\"20131216\", \"manufactory\":	\"netvox\", \"zcl_version\":	\"03\", \"stack_version\":	\"33\", \"app_version\":	\"28\", \"model_id\":	\"Z103AE3C\", \"node_type\":	0 } }, { \"totalcount\":	2, \"curcount\":	2, \"node\":	{ \"ieee\":	\"00137A000000DC86\", \"nwk_addr\":	\"C9B0\", \"hw_version\":	\"0C\", \"name\":	\"Mains Power Outlet 1\", \"date_code\":	\"20130826\", \"manufactory\":	\"netvox\", \"zcl_version\":	\"03\",\"stack_version\":	\"2F\", \"app_version\":	\"15\", \"model_id\":	\"Z809AE3R\", \"node_type\":	1 } }] } )";
	public static String jasonURLforWeather="http://www.weather.com.cn/data/cityinfo/101280101.html";
	
  
  //1.95 GetZBNode
  public static String getZBNodeURL="http://192.168.1.239/cgi-bin/rest/network/getZBNode.cgi?callback=1234&encodemethod=NONE&sign=AAA";
  //1.95 SetDelayAction
  public static String setDelayActionURL="http://192.168.1.239/cgi-bin/rest/network/mainsOutLetOperation.cgi?ieee=00137A000000DC86&ep=01&operatortype=2&param1=1&param2=2&param3=3&callback=1234&encodemethod=NONE&sign=AAA";//1234 ({ "request_id":	1234, "response_params":	{ } } )
  public static String meituanURL="http://api.mobile.meituan.com/group/v1/deal/new-cate-list/android/4.1?cityId=1";
  public static String testString="111\n\t222\n\tt";
  public static String locationURL="http://gc.ditu.aliyun.com/regeocoding?l=23.131162,%20113.321528&type=001";
}
