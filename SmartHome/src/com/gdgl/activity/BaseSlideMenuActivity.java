package com.gdgl.activity;

import com.gdgl.smarthome.R;
import com.gdgl.util.SlideMenu;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class BaseSlideMenuActivity extends FragmentActivity {
	private SlideMenu mSlideMenu;
	protected FragmentManager fragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slidemenu);
		fragmentManager = this.getFragmentManager();
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		mSlideMenu = (SlideMenu) findViewById(R.id.slideMenu);
	}

	public void setSlideRole(int res) {
		if (null == mSlideMenu) {
			return;
		}

		getLayoutInflater().inflate(res, mSlideMenu, true);
	}

	public SlideMenu getSlideMenu() {
		return mSlideMenu;
	}
}
