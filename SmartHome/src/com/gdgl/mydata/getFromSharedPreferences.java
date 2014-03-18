package com.gdgl.mydata;



import com.gdgl.util.UtilString;

import android.content.Context;
import android.content.SharedPreferences;

public class getFromSharedPreferences {
	
	
	private Context mContext;
	private SharedPreferences mSharedPreferences=null;
	private SharedPreferences.Editor mEditor = null;

	
	public getFromSharedPreferences(Context context) {
		mContext = context;
		mSharedPreferences=mContext.getSharedPreferences(UtilString.SharedPreferences_SETTING_INFOS, mContext.MODE_PRIVATE);
	}
	
	public String getPwd()
	{
		return  mSharedPreferences.getString(UtilString.PWD, UtilString.EMPTY_STR);
	}
	
	public String getName()
	{
		return  mSharedPreferences.getString(UtilString.NAME, UtilString.EMPTY_STR);
	}
	
	public int getUid()
	{
		return mSharedPreferences.getInt(UtilString.UID, UtilString.ILLEGAI_UID);
	}
	
	public boolean getIsRemerber()
	{
		return  mSharedPreferences.getBoolean(UtilString.IS_REMERBER_PWD, false);
	}
	
	public boolean getIsAutoLoging()
	{
		return  mSharedPreferences.getBoolean(UtilString.IS_AUTO_LOGIN, false);
	}
	
	public boolean setLogin(String name,String pwd,boolean isRemerber,boolean isAuto)
	{
		mEditor=mSharedPreferences.edit();
		
		mEditor.putString(UtilString.NAME, name);
		mEditor.putString(UtilString.PWD, pwd);
		mEditor.putBoolean(UtilString.IS_REMERBER_PWD, isRemerber);
		mEditor.putBoolean(UtilString.IS_AUTO_LOGIN, isAuto);
		
		return mEditor.commit();
	}
}
