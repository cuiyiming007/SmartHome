package com.gdgl.mydata.Callback;
/***
 * messagetype=16 enroll  同样适用msgtype=17的情况
 * @author justek
 *
 */
public class CallbackEnrollMessage {
	
	private String msgtype;
	private String cie_IEEE;
	private String cie_EP;
	private String zone_IEEE;
	private String zone_EP;
	private String zoneid;
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public String getCie_IEEE() {
		return cie_IEEE;
	}
	public void setCie_IEEE(String cie_IEEE) {
		this.cie_IEEE = cie_IEEE;
	}
	public String getCie_EP() {
		return cie_EP;
	}
	public void setCie_EP(String cie_EP) {
		this.cie_EP = cie_EP;
	}
	public String getZone_IEEE() {
		return zone_IEEE;
	}
	public void setZone_IEEE(String zone_IEEE) {
		this.zone_IEEE = zone_IEEE;
	}
	public String getZone_EP() {
		return zone_EP;
	}
	public void setZone_EP(String zone_EP) {
		this.zone_EP = zone_EP;
	}
	public String getZoneid() {
		return zoneid;
	}
	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}
	@Override
	public String toString() {
		return "CallbackEnrollMessage [msgtype=" + msgtype + ", cie_IEEE="
				+ cie_IEEE + ", cie_EP=" + cie_EP + ", zone_IEEE=" + zone_IEEE
				+ ", zone_EP=" + zone_EP + ", zoneid=" + zoneid + "]";
	}
	
	
	
}
