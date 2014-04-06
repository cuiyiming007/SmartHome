package com.gdgl.mydata;



import com.gdgl.util.UiUtils;

import android.content.Context;
import android.content.SharedPreferences;

public class getFromSharedPreferences {
    
    
    private Context mContext;
    private SharedPreferences mSharedPreferences=null;
    private SharedPreferences.Editor mEditor = null;

    
    public getFromSharedPreferences(Context context) {
        mContext = context;
        mSharedPreferences=mContext.getSharedPreferences(UiUtils.SharedPreferences_SETTING_INFOS, mContext.MODE_PRIVATE);
    }
    
    public String getPwd()
    {
        return  mSharedPreferences.getString(UiUtils.PWD, UiUtils.EMPTY_STR);
    }
    
    public String getName()
    {
        return  mSharedPreferences.getString(UiUtils.NAME, UiUtils.EMPTY_STR);
    }
    
    public int getUid()
    {
        return mSharedPreferences.getInt(UiUtils.UID, UiUtils.ILLEGAI_UID);
    }
    
    public boolean getIsRemerber()
    {
        return  mSharedPreferences.getBoolean(UiUtils.IS_REMERBER_PWD, false);
    }
    
    public boolean getIsAutoLoging()
    {
        return  mSharedPreferences.getBoolean(UiUtils.IS_AUTO_LOGIN, false);
    }
    
    public boolean setLogin(String name,String pwd,boolean isRemerber,boolean isAuto)
    {
        mEditor=mSharedPreferences.edit();
        
        mEditor.putString(UiUtils.NAME, name);
        mEditor.putString(UiUtils.PWD, pwd);
        mEditor.putBoolean(UiUtils.IS_REMERBER_PWD, isRemerber);
        mEditor.putBoolean(UiUtils.IS_AUTO_LOGIN, isAuto);
        
        return mEditor.commit();
    }
}
