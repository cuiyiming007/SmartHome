package com.gdgl.mydata;
/***
 * 1234 ({ "request_id":	1234,
 *  "response_params":	
 * { "ieee":	"00137A00000121EF", "ep":	"01", 
 * "status":	{ 
 * "alarm1":	false, 
 * "alarm2":	false, 
 * "tamper":	false, 
 * "batterly_low":	false, 
 * "supervision_reports": true,
 *  "restore_reports":	
 *  true, "trouble":	
 *  false, "ac_trouble":	
 *  false, "battery_level":	95 } } } )
 * @author justek
 *
 */
public class Status {
	private String alarm1;
	private String alarm2;
	private String tamper;
	private String batterly_low;
	private String supervision_reports;
	private String restore_reports;
	private String trouble;
	private String ac_trouble;
	private String battery_level;
	public String getAlarm1() {
		return alarm1;
	}
	public void setAlarm1(String alarm1) {
		this.alarm1 = alarm1;
	}
	public String getAlarm2() {
		return alarm2;
	}
	public void setAlarm2(String alarm2) {
		this.alarm2 = alarm2;
	}
	public String getTamper() {
		return tamper;
	}
	public void setTamper(String tamper) {
		this.tamper = tamper;
	}
	public String getBatterly_low() {
		return batterly_low;
	}
	public void setBatterly_low(String batterly_low) {
		this.batterly_low = batterly_low;
	}
	public String getSupervision_reports() {
		return supervision_reports;
	}
	public void setSupervision_reports(String supervision_reports) {
		this.supervision_reports = supervision_reports;
	}
	public String getRestore_reports() {
		return restore_reports;
	}
	public void setRestore_reports(String restore_reports) {
		this.restore_reports = restore_reports;
	}
	public String getTrouble() {
		return trouble;
	}
	public void setTrouble(String trouble) {
		this.trouble = trouble;
	}
	public String getAc_trouble() {
		return ac_trouble;
	}
	public void setAc_trouble(String ac_trouble) {
		this.ac_trouble = ac_trouble;
	}
	public String getBattery_level() {
		return battery_level;
	}
	public void setBattery_level(String battery_level) {
		this.battery_level = battery_level;
	}
	
}
