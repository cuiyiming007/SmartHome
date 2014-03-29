package com.gdgl.mydata;
/***
 * {"weatherinfo":{"city":"广州","cityid":"101280101","temp1":"25℃","temp2":"14℃","weather":"晴转多云","img1":"d0.gif","img2":"n1.gif","ptime":"08:00"}}
 * @author justek
 *
 */
public class Weather {

	private String city;
	private String cityid;
	private String temp1;
	private String temp2;
	private String weather;
	private String img1;
	private String img2;
	private String ptime;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityId() {
		return cityid;
	}
	public void setCityId(String cityId) {
		this.cityid = cityId;
	}
	public String getTemp1() {
		return temp1;
	}
	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}
	public String getTemp2() {
		return temp2;
	}
	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getImg1() {
		return img1;
	}
	public void setImg1(String img1) {
		this.img1 = img1;
	}
	public String getImg2() {
		return img2;
	}
	public void setImg2(String img2) {
		this.img2 = img2;
	}
	public String getPtime() {
		return ptime;
	}
	public void setPtime(String ptime) {
		this.ptime = ptime;
	}
}
