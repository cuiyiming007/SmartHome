package com.gdgl.mydata;

import android.R.integer;


public class EventType {
	
	private static int id=0;
	private static String name;
	
	public EventType(String name, int id) {
		this.id=id;
		this.name=name;
	}
   
	public static final  EventType LOGIN;
	public static final  EventType ONOFFLIGHTSWITCHOPERATION;
	public static final EventType  INTITIALDVIVCEDATA;
	
	
	static
	{
		LOGIN=new EventType("login",id);
		ONOFFLIGHTSWITCHOPERATION=new EventType("onofflightswitchoperation", nextId());
		INTITIALDVIVCEDATA=new EventType("initialDiviceData",nextId());
	}
	
	private static int nextId()
	{
		return id++;
	}

}
