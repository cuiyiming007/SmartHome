package com.gdgl.model;




import android.support.v4.app.Fragment;

public class TabInfo {

	public Fragment mFragment = null;
	public boolean notifyChange = false;

	//public Class fragmentClass = null;

	public TabInfo(Fragment fragment) {
		mFragment = fragment;
	}

	public Fragment createFragment() {
		return mFragment;
	}
}
