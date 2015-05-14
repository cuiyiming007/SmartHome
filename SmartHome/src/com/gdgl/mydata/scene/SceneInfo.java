package com.gdgl.mydata.scene;

import java.io.Serializable;

import android.content.ContentValues;
import android.provider.BaseColumns;

interface SceneInfoColumns extends BaseColumns {
	public static final String SCENE_ID = "sid";
	public static final String SCENE_NAME = "scnname";
	public static final String SCENE_ACTIONS = "scnaction";
	public static final String SCENE_INDEX = "scnindex";
}

public class SceneInfo implements SceneInfoColumns, Serializable {
	/**
	 * serialVersionUID作用： 序列化时为了保持版本的兼容性，即在版本升级时反序列化仍保持对象的唯一性。
	 */
	private static final long serialVersionUID = -6208168269697173670L;
	private int sid;
	private String scnname;
	private String scnaction;
	private int scnindex;
	
	public int getSid() {
		return sid;
	}
	
	public void setSid(int sid) {
		this.sid = sid;
	}
	
	public String getScnname() {
		return scnname;
	}

	public void setScnname(String scnname) {
		this.scnname = scnname;
	}

	public String getScnaction() {
		return scnaction;
	}

	public void setScnaction(String scnaction) {
		this.scnaction = scnaction;
	}
	
	public int getScnindex() {
		return scnindex;
	}
	
	public void setScnindex(int scnindex) {
		this.scnindex = scnindex;
	}
	
	public ContentValues convertContentValues() {
		ContentValues mContentValues = new ContentValues();

		mContentValues.put(SceneInfo.SCENE_ID,sid);
		mContentValues.put(SceneInfo.SCENE_NAME,scnname);
		mContentValues.put(SceneInfo.SCENE_ACTIONS,scnaction);
		mContentValues.put(SceneInfo.SCENE_INDEX,scnindex);
		return mContentValues;
	}
}
