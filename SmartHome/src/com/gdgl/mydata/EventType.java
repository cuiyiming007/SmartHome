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
	
	
	static
	{
		LOGIN=new EventType("login",id);
	}
	
	private static int nextId()
	{
		return id++;
	}

}
