package com.gdgl.mydata.Region;



public class devparam {
	private node node;
	private String ep;
	private String name;
	private String current;
	private String voltage;
	private String power;
	private String energy;
	private String on_off_status;
	private String current_min;
	private String current_max;
	private String voltage_min;
	private String voltage_max;
	private String energy_min;
	private String energy_max;
	private String ep_model_id;
	
	
	public String getEp() {
		return ep;
	}
	public void setEp(String ep) {
		this.ep = ep;
	}
	public String getEp_model_id() {
		return ep_model_id;
	}
	public void setEp_model_id(String ep_model_id) {
		this.ep_model_id = ep_model_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public node getNode() {
		return node;
	}
	public void setNode(node node) {
		this.node = node;
	}
	public String getCurrent() {
		return current;
	}
	public void setCurrent(String current) {
		this.current = current;
	}
	public String getCurrentmax() {
		return current_max;
	}
	public void setCurrentmax(String currentmax) {
		this.current_max = currentmax;
	}
	public String getCurrentmin() {
		return current_min;
	}
	public void setCurrentmin(String currentmin) {
		this.current_min = currentmin;
	}
	public String getEnergy() {
		return energy;
	}
	public void setEnergy(String energy) {
		this.energy = energy;
	}
	public String getEnergymax() {
		return energy_max;
	}
	public void setEnergymax(String energymax) {
		this.energy_max = energymax;
	}
	public String getEnergymin() {
		return energy_min;
	}
	public void setEnergymin(String energymin) {
		this.energy_min = energymin;
	}
	public String getOn_off_status() {
		return on_off_status;
	}
	public void setOn_off_status(String on_off_status) {
		this.on_off_status = on_off_status;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public String getVoltage() {
		return voltage;
	}
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	public String getVoltagemax() {
		return voltage_max;
	}
	public void setVoltagemax(String voltagemax) {
		this.voltage_max = voltagemax;
	}
	public String getVoltagemin() {
		return voltage_min;
	}
	public void setVoltagemin(String voltagemin) {
		this.voltage_min = voltagemin;
	}
}
