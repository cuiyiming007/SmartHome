package com.gdgl.mydata;

import java.util.HashSet;
import java.util.Set;

import com.gdgl.util.UiUtils;

import android.content.Context;
import android.content.SharedPreferences;

public class getFromSharedPreferences {

	
	private static SharedPreferences mSharedPreferences = null;
	private static SharedPreferences.Editor mEditor = null;

	public static void setharedPreferences(Context context) {
		mSharedPreferences = context.getSharedPreferences(
				UiUtils.SharedPreferences_SETTING_INFOS, context.MODE_PRIVATE);
	}

	public static String getPwd() {
		return mSharedPreferences.getString(UiUtils.PWD, UiUtils.EMPTY_STR);
	}

	public static String getName() {
		return mSharedPreferences.getString(UiUtils.NAME, UiUtils.EMPTY_STR);
	}

	public static int getUid() {
		return mSharedPreferences.getInt(UiUtils.UID, UiUtils.ILLEGAI_UID);
	}

	public static boolean getIsRemerber() {
		return mSharedPreferences.getBoolean(UiUtils.IS_REMERBER_PWD, false);
	}

	public static boolean getIsAutoLoging() {
		return mSharedPreferences.getBoolean(UiUtils.IS_AUTO_LOGIN, false);
	}

	public static boolean setLogin(String name, String pwd, boolean isRemerber,
			boolean isAuto) {
		mEditor = mSharedPreferences.edit();

		mEditor.putString(UiUtils.NAME, name);
		mEditor.putString(UiUtils.PWD, pwd);
		mEditor.putBoolean(UiUtils.IS_REMERBER_PWD, isRemerber);
		mEditor.putBoolean(UiUtils.IS_AUTO_LOGIN, isAuto);

		return mEditor.commit();
	}

	public static boolean setRegion(String mSet) {
		mEditor = mSharedPreferences.edit();
		mEditor.putString(UiUtils.REGION, mSet);
		return mEditor.commit();
	}

	public static String getRegion() {
		String region = mSharedPreferences.getString(UiUtils.REGION, null);
		return region;
	}
}
