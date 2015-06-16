package com.gdgl.mydata;

import java.io.Serializable;

import android.content.ContentValues;
import android.provider.BaseColumns;

interface LinkageColumns extends BaseColumns {
	public static final String LID = "lid";
	public static final String LNKNAME = "lnkname";
	public static final String TRGIEEE = "trgieee";
	public static final String TRGEP = "trgep";
	public static final String TRGCND = "trgcnd";
	public static final String LNKACT = "lnkact";
	public static final String ENABLE = "enable";
}

public class Linkage implements LinkageColumns, Serializable{
	/**
	 * serialVersionUID作用： 序列化时为了保持版本的兼容性，即在版本升级时反序列化仍保持对象的唯一性。
	 */
	private static final long serialVersionUID = 4010318745509649607L;
	private int lid;
	private String lnkname = "";
	private String trgieee = "";
	private String trgep = "";
	private String trgcnd = "";
	private String lnkact = "";
	private int enable;
	
	public int getLid(){
		return lid;
	}
	
	public void setLid(int lid){
		this.lid = lid;
	}
	
	public String getLnkname(){
		return lnkname;
	}
	
	public void setLnkname(String lnkname){
		this.lnkname = lnkname;
	}
	
	public String getTrgieee(){
		return trgieee;
	}
	
	public void setTrgieee(String trgieee){
		this.trgieee = trgieee;
	}
	
	public String getTrgep(){
		return trgep;
	}
	
	public void setTrgep(String trgep){
		this.trgep = trgep;
	}
	
	public String getTrgcnd(){
		return trgcnd;
	}
	
	public void setTrgcnd(String trgcnd){
		this.trgcnd = trgcnd;
	}
	
	public String getLnkact(){
		return lnkact;
	}
	
	public void setLnkact(String lnkact){
		this.lnkact = lnkact;
	}
	
	public int getEnable(){
		return enable;
	}
	
	public void setEnable(int enable){
		this.enable = enable;
	}
	
	public ContentValues convertContentValues() {
		ContentValues mContentValues = new ContentValues();

		mContentValues.put(LinkageColumns.LID, getLid());
		mContentValues.put(LinkageColumns.LNKNAME, getLnkname());
		mContentValues.put(LinkageColumns.TRGIEEE, getTrgieee());
		mContentValues.put(LinkageColumns.TRGEP, getTrgep());
		mContentValues.put(LinkageColumns.TRGCND, getTrgcnd());
		mContentValues.put(LinkageColumns.LNKACT, getLnkact());
		mContentValues.put(LinkageColumns.ENABLE, getEnable());
		return mContentValues;
	}
}
