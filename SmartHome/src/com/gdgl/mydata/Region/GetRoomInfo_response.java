package com.gdgl.mydata.Region;

import android.content.ContentValues;
import android.provider.BaseColumns;

interface RoomInfoColumns extends BaseColumns {
	public static final String ROOM_NAME = "room_name";
	public static final String ROOM_ID = "room_id";
	public static final String ROOM_PIC = "room_pic";
}

public class GetRoomInfo_response implements RoomInfoColumns {
	private Room room;
	
	public Room getroom() {
		return room;
	}
	public void setroom(Room room) {
		this.room=room;
	}
	
	public ContentValues convertContentValues() {
		ContentValues mContentValues = new ContentValues();

		mContentValues.put(GetRoomInfo_response.ROOM_ID,getroom().getroom_id());
		mContentValues.put(GetRoomInfo_response.ROOM_NAME,getroom().getroom_name());
		mContentValues.put(GetRoomInfo_response.ROOM_PIC,getroom().getroompic());
		return mContentValues;
	}
}