package com.gdgl.util;

import com.gdgl.smarthome.R;



public class UtilString {
	
	public static final String SharedPreferences_SETTING_INFOS = "SmartHome";

	public static final String PWD = "Password";
	public static final String NAME = "Name";
	public static final String UID = "UserID";
	public static final String IS_REMERBER_PWD = "RemerberPwd";
	public static final String IS_AUTO_LOGIN = "AutoLogin";

	public static final String EMPTY_STR = "";
	
	public static final String REM_PWD_ACT="remeber_pwd";
	public static final String N_REM_PWD_ACT="not_remeber_pwd";
	
	public static final String AUTO_LOGIN_ACT="AutoLogin";
	
	
	public static int ILLEGAI_UID=-1;
	
	public static int[] GONENG_ICON={R.drawable.find_more_friend_photograph_icon,R.drawable.find_more_friend_scan,R.drawable.find_more_friend_shake,
		R.drawable.find_more_friend_near_icon,R.drawable.find_more_friend_bottle,R.drawable.more_game
	};
	
	/***
	 * take out \t\n
	 * 
	 * @param response
	 * @return
	 */
	public static String formatResponseString(String response) {
		response = response.replaceAll("\n", "");
		response = response.replaceAll("\t", "");
		response = customString(response);
		return response;
	}

	public static String customString(String s) {
		s = s.substring(s.indexOf("{"), s.length() - 1);
		return s;
	}
}
