package com.gdgl.mydata.Region;

import android.content.ContentValues;

public class Room {

	private String room_name="";
	private int romm_id;
	private String room_pic="";

	public String getroom_name() {
		return room_name;
	}

	public void setroom_name(String name) {
		this.room_name = name;
	}

	public int getroom_id() {
		return romm_id;
	}

	public void setroom_id(int id) {
		this.romm_id = id;
	}

	public String getroompic() {
		return room_pic;
	}

	public void setroom_pic(String pic) {
		this.room_pic = pic;
	}
	
	public ContentValues convertContentValues() {
		ContentValues mContentValues = new ContentValues();

		mContentValues.put(GetRoomInfo_response.ROOM_ID,getroom_id());
		mContentValues.put(GetRoomInfo_response.ROOM_NAME,getroom_name());
		mContentValues.put(GetRoomInfo_response.ROOM_PIC,getroompic());
		return mContentValues;
	}
}
