package com.gdgl.model;




import com.gdgl.activity.SmartHome.refreshAdapter;

import android.support.v4.app.Fragment;

public class TabInfo{

	public Fragment mFragment = null;
	public boolean notifyChange = false;
	refreshAdapter mrefreshAdapter;
	//public Class fragmentClass = null;

	public TabInfo(Fragment fragment) {
		mFragment = fragment;
	}

	public Fragment createFragment() {
		return mFragment;
	}


	public void refreshFragment() {
		// TODO Auto-generated method stub
		if(mFragment instanceof refreshAdapter){
			mrefreshAdapter=(refreshAdapter)mFragment;
			mrefreshAdapter.refreshFragment();
		}
		
	}
}
