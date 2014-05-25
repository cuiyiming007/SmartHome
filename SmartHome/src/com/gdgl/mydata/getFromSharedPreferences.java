package com.gdgl.mydata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gdgl.model.RemoteControl;
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

	public static String getUid() {
		return mSharedPreferences.getString(UiUtils.UID, UiUtils.EMPTY_STR);
	}

	public static boolean getIsRemerber() {
		return mSharedPreferences.getBoolean(UiUtils.IS_REMERBER_PWD, false);
	}

	public static boolean getIsAutoLoging() {
		return mSharedPreferences.getBoolean(UiUtils.IS_AUTO_LOGIN, false);
	}

	public static boolean setLogin(AccountInfo accountInfo, boolean isRemerber,
			boolean isAuto) {
		mEditor = mSharedPreferences.edit();

		mEditor.putString(UiUtils.UID, accountInfo.getId());
		mEditor.putString(UiUtils.NAME, accountInfo.getAlias());
		mEditor.putString(UiUtils.PWD, accountInfo.getPassword());
		mEditor.putBoolean(UiUtils.IS_REMERBER_PWD, isRemerber);
		mEditor.putBoolean(UiUtils.IS_AUTO_LOGIN, isAuto);

		return mEditor.commit();
	}

	public static boolean setPwd(String pwd) {
		mEditor = mSharedPreferences.edit();

		mEditor.putString(UiUtils.PWD, pwd);

		return mEditor.commit();
	}

	public static boolean setName(String name) {
		mEditor = mSharedPreferences.edit();

		mEditor.putString(UiUtils.NAME, name);

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

	public static String getCommonUsed() {
		String commonused = mSharedPreferences.getString(UiUtils.COMMONUSED,
				null);
		return commonused;
	}

	public static boolean setCommonUsed(String comm) {
		mEditor = mSharedPreferences.edit();
		mEditor.putString(UiUtils.COMMONUSED, comm);
		return mEditor.commit();
	}

	public static int getSceneId() {
		int sceneId = mSharedPreferences.getInt(UiUtils.SCENE, 0);

		mEditor = mSharedPreferences.edit();
		mEditor.putInt(UiUtils.SCENE, sceneId + 1);
		mEditor.commit();
		return sceneId + 1;
	}

	public static List<RemoteControl> getRemoteControl() {
		List<RemoteControl> mList = new ArrayList<RemoteControl>();
		RemoteControl rc;
		String controlString = mSharedPreferences.getString(
				UiUtils.REMOTE_CONTROL, "");
		if (null == controlString || controlString.trim().equals("")) {
			return null;
		}
		String[] RemoteControlNode = controlString.split("##");
		for (String string : RemoteControlNode) {
			String[] controls;
			if (!string.trim().equals("")) {
				controls = string.trim().split("@@");
				rc = new RemoteControl();
				rc.Index = controls[0];
				rc.Name = controls[1];
				rc.IsLearn = controls[2];
				mList.add(rc);
			}
		}
		return mList;

	}
	
	public static List<RemoteControl> getTvKongtiaoRemoteControl(int type) {
		List<RemoteControl> mList = new ArrayList<RemoteControl>();
		RemoteControl rc;
		String controlString="";
		if(1==type){
			controlString = mSharedPreferences.getString(
					UiUtils.KONGTIAO, "");
		}else if(2==type){
			controlString = mSharedPreferences.getString(
					UiUtils.TV, "");
		}
		
		if (null == controlString || controlString.trim().equals("")) {
			return null;
		}
		String[] RemoteControlNode = controlString.split("##");
		for (String string : RemoteControlNode) {
			String[] controls;
			if (!string.trim().equals("")) {
				controls = string.trim().split("@@");
				rc = new RemoteControl();
				rc.Index = controls[0];
				rc.Name = controls[1];
				rc.IsLearn = controls[2];
				mList.add(rc);
			}
		}
		return mList;

	}
	
	public static void addTvKongtiaoRemoteControlList(List<RemoteControl> rc,int type) {
		if (null == rc) {
			return;
		}
		String controls = "";
		for (RemoteControl remoteControl : rc) {
			if (null != remoteControl) {
				controls += remoteControl.Index + "@@" + remoteControl.Name
						+ "@@" + remoteControl.IsLearn + "##";
			}
		}

		mEditor = mSharedPreferences.edit();
		if(1==type){
			mEditor.putString(UiUtils.KONGTIAO, controls);
		}else if(2==type){
			mEditor.putString(UiUtils.TV, controls);
		}
		mEditor.commit();
	}
	
	public static int getRemoteControlId() {
		List<RemoteControl> mList = getRemoteControl();
		if (null == mList || mList.size() == 0) {
			return 1;
		}
		int maxId = 0;
		for (RemoteControl rr : mList) {
			maxId = maxId > Integer.parseInt(rr.Index) ? maxId : Integer
					.parseInt(rr.Index);
		}
		return maxId + 1;

	}

	public static void addRemoteControlList(List<RemoteControl> rc) {
		if (null == rc) {
			return;
		}
		String controls = "";
		for (RemoteControl remoteControl : rc) {
			if (null != remoteControl) {
				controls += remoteControl.Index + "@@" + remoteControl.Name
						+ "@@" + remoteControl.IsLearn + "##";
			}
		}

		mEditor = mSharedPreferences.edit();
		mEditor.putString(UiUtils.REMOTE_CONTROL, controls);
		mEditor.commit();
	}

}
