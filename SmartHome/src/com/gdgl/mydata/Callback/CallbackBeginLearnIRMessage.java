package com.gdgl.mydata.Callback;

/***
 * messagetype=12
 * 
 * @author Trice
 * 
 */
public class CallbackBeginLearnIRMessage extends CallbackResponseCommon {
	private String Brand;
	private String Style;
	private String irseqno;
	private String status;

	public String getBrand() {
		return Brand;
	}

	public void setBrand(String brand) {
		Brand = brand;
	}

	public String getStyle() {
		return Style;
	}

	public void setStyle(String style) {
		Style = style;
	}

	public String getIrseqno() {
		return irseqno;
	}

	public void setIrseqno(String Irseqno) {
		irseqno = Irseqno;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String Status) {
		status = Status;
	}

	public String toString() {
		return "LearnIR [msgtype=" + getMsgtype() + ", IEEE=" + getIEEE()
				+ ", EP=" + getEP() + ", Brand=" + Brand + ", Style=" + Style
				+ ", irseqno=" + irseqno + ", status=" + status + "]";
	}
}
