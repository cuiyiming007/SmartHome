package com.gdgl.mydata;

public class Constants {
	// 1234 ({ "request_id": 12453, "response_params": [{ "totalcount": 2,
	// "curcount": 1, "node": { "ieee": "00137A00000121F1", "nwk_addr": "0000",
	// "hw_version": "0B", "name": "Combined Interface 1", "date_code":
	// "20131216", "manufactory": "netvox", "zcl_version": "03",
	// "stack_version": "33", "app_version": "28", "model_id": "Z103AE3C",
	// "node_type": 0 } }, { "totalcount": 2, "curcount": 2, "node": { "ieee":
	// "00137A000000DC86", "nwk_addr": "C9B0", "hw_version": "0C", "name":
	// "Mains Power Outlet 1", "date_code": "20130826", "manufactory": "netvox",
	// "zcl_version": "03", "stack_version": "2F", "app_version": "15",
	// "model_id": "Z809AE3R", "node_type": 1 } }] } )
	// {"weatherinfo":{"city":"广州","cityid":"101280101","temp1":"25℃","temp2":"14℃","weather":"晴转多云","img1":"d0.gif","img2":"n1.gif","ptime":"08:00"}}
	public static String jsonStringforNode = "1234 ({ \"request_id\":	12453, \"response_params\":	[{ \"totalcount\":	2, \"curcount\":	1, \"node\":	{ \"ieee\":	\"00137A00000121F1\", \"nwk_addr\":	\"0000\", \"hw_version\":	\"0B\", \"name\":	\"Combined Interface 1\", \"date_code\":	\"20131216\", \"manufactory\":	\"netvox\", \"zcl_version\":	\"03\", \"stack_version\":	\"33\", \"app_version\":	\"28\", \"model_id\":	\"Z103AE3C\", \"node_type\":	0 } }, { \"totalcount\":	2, \"curcount\":	2, \"node\":	{ \"ieee\":	\"00137A000000DC86\", \"nwk_addr\":	\"C9B0\", \"hw_version\":	\"0C\", \"name\":	\"Mains Power Outlet 1\", \"date_code\":	\"20130826\", \"manufactory\":	\"netvox\", \"zcl_version\":	\"03\",\"stack_version\":	\"2F\", \"app_version\":	\"15\", \"model_id\":	\"Z809AE3R\", \"node_type\":	1 } }] } )";
	public static String jasonURLforWeather = "http://www.weather.com.cn/data/cityinfo/101280101.html";

	// 1.95 GetZBNode
	public static String getZBNodeURL = "http://192.168.1.239/cgi-bin/rest/network/getZBNode.cgi?callback=1234&encodemethod=NONE&sign=AAA";
	// 1.95 SetDelayAction
	public static String setDelayActionURL = "http://192.168.1.239/cgi-bin/rest/network/mainsOutLetOperation.cgi?ieee=00137A000000DC86&ep=01&operatortype=2&param1=1&param2=2&param3=3&callback=1234&encodemethod=NONE&sign=AAA";// 1234
																																																									// ({
																																																									// "request_id":
																																																									// 1234,
																																																									// "response_params":
																																																									// {
																																																									// }
																																																									// }
																																																									// )
	public static String meituanURL = "http://api.mobile.meituan.com/group/v1/deal/new-cate-list/android/4.1?cityId=1";
	public static String testString = "111\n\t222\n\tt";
	public static String locationURL = "http://gc.ditu.aliyun.com/regeocoding?l=23.131162,%20113.321528&type=001";

	// 1. GetZBNode
	// http://192.168.1.239/cgi-bin/rest/network/getZBNode.cgi?callback=1234&encodemethod=NONE&sign=AAA

	// 2. GetEndPoint
	public static String getEndPointURl = " http://192.168.1.239/cgi-bin/rest/network/getendpoint.cgi?callback=1234&encodemethod=NONE&sign=AAA";

	// 3. ZBGetZBNodeCount
	public static String getZBGetZBNodeCountURl = "http://192.168.1.239/cgi-bin/rest/network/zbGetZBNodeCount.cgi?callback=1234&encodemethod=NONE&sign=AAA";

	// 4. ZBGetZBNodeByIEEE
	public static String getZBGetZBNodeByIEEEURl = "http://192.168.1.239/cgi-bin/rest/network/zbGetZBNodeByIEEE.cgi?ieee=00137A000000FC73&callback=1234&encodemethod=NONE&sign=AAA";

	// // 5. ZBGetZBNodeByNwk_addr
	public static String getZBGetZBNodeByNwk_addrURl = " http://192.168.1.239/cgi-bin/rest/network/zbGetZBNodeByNwk_addr.cgi?nwk=0000&callback=1234&encodemethod=NONE&sign=AAA";

	// 6. ZBGetZBNodeByIndex
	public static String getZBGetZBNodeByIndexURl = " http://192.168.1.239/cgi-bin/rest/network/zbGetZBNodeByIndex.cgi?index=1&callback=1234&encodemethod=NONE&sign=AAA";

	// 7. ZBGetEndPointCount
	public static String getZBGetEndPointCountURl = " http://192.168.1.239/cgi-bin/rest/network/zbGetEndPointCount.cgi?&callback=1234&encodemethod=NONE&sign=AAA";

	// 8. ZBGetEndPointByIEEE
	public static String getZBGetEndPointByIEEEURl = "  http://192.168.1.239/cgi-bin/rest/network/zbGetEndPointByIEEE.cgi?ieee=00137A0000011870&callback=1234&encodemethod=NONE&sign=AAA";

	// 9. ZBGetEndPointByNwk_addr£®Ã· æ≤Œ ˝¥ÌŒÛ£∫¥ÀCGI”–Œ Ã‚£©
	public static String getZBGetEndPointByNwk_addrURl = "http://192.168.1.239/cgi-bin/rest/network/zbGetEndPointByNwk_addr.cgi?nwk=0000&callback=1234&encodemethod=NONE&sign=AAA";

	// 10. ZBGetEndPointByIndex
	public static String getZBGetEndPointByIndexURl = " http://192.168.1.239/cgi-bin/rest/network/zbGetEndPointByIndex.cgi?index=1&callback=1234&encodemethod=NONE&sign=AAA";

	// ===================================================================================

	// 1. MainsOutLet Operation
	public static String getMainsOutLetURl = " http://192.168.1.239/cgi-bin/rest/network/mainsOutLetOperation.cgi?ieee=00137A000000B657&ep=01&operatortype=2&param1=1&param2=2&param3=3&callback=1234&encodemethod=NONE&sign=AAA";

	// 2. DimmableLight Operation £®≤Ÿ◊˜ ß∞‹£©
	public static String getDimmableLightURl = "  http://192.168.1.239/cgi-bin/rest/network/dimmableLightOperation.cgi?ieee=00137A00000EE66&ep=01&operatortype=2&param1=1&param2=2&param3=3&callback=1234&encodemethod=NONE&sign=AAA";
	// public static String getMainsOutLet="
	// http://192.168.1.239/cgi-bin/rest/network/dimmableLightOperation.cgi?ieee=00137A00000EE66&ep=01&operatortype=7&param1=50&param2=2&param3=3&callback=1234&encodemethod=NONE&sign=AAA

	// 3. OnOffLight Operation
	public static String getOnOffLightURl = " http://192.168.1.239/cgi-bin/rest/network/onOffLightOperation.cgi?ieee=00137A0000010AB5&ep=0A&operatortype=2&param1=1&param2=2&param3=3&callback=1234&encodemethod=NONE&sign=AAA";

	// 4. OnOffOutput Operation
	public static String getOnOffOutputURl = "  http://192.168.1.239/cgi-bin/rest/network/onOffOutputOperation.cgi?ieee=00137A0000010AB5&ep=0A&operatortype=2&param1=1&param2=2&param3=3&callback=1234&encodemethod=NONE&sign=AAA";

	// 5. Shade Operation
	public static String getShadeURl = " http://192.168.1.239/cgi-bin/rest/network/shadeOperation.cgi?ieee=00137A0000010516&ep=01&operatortype=1&param1=1&param2=2&param3=3&callback=1234&encodemethod=NONE&sign=AAA";

	// ===================================================================================

	// 1. TemperatureSensor Operation
	public static String getTemperatureSensorURl = "  http://192.168.1.239/cgi-bin/rest/network/temperatureSensorOperation.cgi?ieee=00137A00000121C2&ep=0A&operatortype=1&param1=1&param2=2&param3=3&callback=1234&encodemethod=NONE&sign=AAA";

}
