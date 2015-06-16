package com.gdgl.mydata.video;

import com.gdgl.model.ContentValuesListener;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

interface VideoNodeColumns extends BaseColumns
{
	public static final String _ID= "_id";
	public static final String ID= "id";
	public static final String IPC_IPADDR="ipc_ipaddr";
	public static final String NAME="name";
	public static final String PASSWORD="password";
	public static final String RTSPORT="rtspport";
	public static final String HTTPPORT="httpport";
	public static final String ALIAS="alias";
	public static final String IPC_INDEX = "ipc_index";
	public static final String ROOMID = "roomid";
	public static final String IPC_STATUS = "ipc_status";
	public static final int PRIORITY = 1000;
	
	}

/***
 * "list": [ { "id": 0, "ipc_ipaddr": "192.168.1.164", "name": "admin",
 * "password": "12345", "rtspport": "554", "httpport": "81", "aliases":
 * "camera1" }]
 * 
 * @author justek
 * 
 */

public class VideoNode implements VideoNodeColumns, ContentValuesListener,
		Parcelable {
	private String id;
	private String ipc_ipaddr;
	private String name;
	private String password;
	private String rtspport;
	private String httpport;
	private String alias;
	private int index;
	private int roomid;
	private String ipc_status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIpc_ipaddr() {
		return ipc_ipaddr;
	}

	public void setIpc_ipaddr(String ipc_ipaddr) {
		this.ipc_ipaddr = ipc_ipaddr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRtspport() {
		return rtspport;
	}

	public void setRtspport(String rtspport) {
		this.rtspport = rtspport;
	}

	public String getHttpport() {
		return httpport;
	}

	public void setHttpport(String httpport) {
		this.httpport = httpport;
	}

	public String getAliases() {
		return alias;
	}

	public void setAliases(String aliases) {
		this.alias = aliases;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getRoomId() {
		return roomid;
	}
	
	public void setRoomId(int roomid) {
		this.roomid = roomid;
	}
	
	public String getIpc_status() {
		return ipc_status;
	}
	
	public void setIpc_status(String status) {
		this.ipc_status = status;
	}

	public ContentValues convertContentValues() {
		ContentValues mContentValues = new ContentValues();

		mContentValues.put(VideoNodeColumns.ALIAS, getAliases());
		mContentValues.put(VideoNodeColumns.HTTPPORT, getHttpport());
		mContentValues.put(VideoNodeColumns.ID, getId());
		mContentValues.put(VideoNodeColumns.IPC_IPADDR, getIpc_ipaddr());
		mContentValues.put(VideoNodeColumns.NAME, getName());
		mContentValues.put(VideoNodeColumns.PASSWORD, getPassword());
		mContentValues.put(VideoNodeColumns.RTSPORT, getRtspport());
		mContentValues.put(VideoNodeColumns.IPC_INDEX, index);
		mContentValues.put(VideoNodeColumns.ROOMID, roomid);
		mContentValues.put(VideoNodeColumns.IPC_STATUS, ipc_status);

		return mContentValues;
	}

	public static final Parcelable.Creator<VideoNode> CREATOR = new Creator<VideoNode>() {
		public VideoNode createFromParcel(Parcel source) {
			VideoNode mVideoNode = new VideoNode();

			mVideoNode.setAliases(source.readString());
			mVideoNode.setHttpport(source.readString());
			mVideoNode.setId(source.readString());
			mVideoNode.setIpc_ipaddr(source.readString());
			mVideoNode.setName(source.readString());
			mVideoNode.setPassword(source.readString());
			mVideoNode.setRtspport(source.readString());
			mVideoNode.setIndex(source.readInt());
			mVideoNode.setRoomId(source.readInt());
			mVideoNode.setIpc_status(source.readString());

			return mVideoNode;
		}

		public VideoNode[] newArray(int size) {
			return new VideoNode[size];
		}
	};

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(alias);
		parcel.writeString(httpport);
		parcel.writeString(id);
		parcel.writeString(ipc_ipaddr);
		parcel.writeString(name);
		parcel.writeString(password);
		parcel.writeString(rtspport);
		parcel.writeInt(index);
		parcel.writeInt(roomid);
		parcel.writeString(ipc_status);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
