package com.gdgl.activity;

import java.util.List;

import com.gdgl.activity.JoinNetDevicesListFragment.refreshData;
import com.gdgl.activity.JoinNetFragment.ChangeFragment;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.SlideMenu;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConfigActivity extends BaseSlideMenuActivity implements
		ChangeFragment, refreshData {
	private SlideMenu mSlideMenu;
	int mSelectedColor = 0xffEE3B3B;
	int mUnSelectedColor = 0xff20B2AA;

	TextView modify_pwd, modify_name, join_net,all_dev ;
	
//	TextView safe_center;
	TextView config_name, user_name;
	TextView tempView;
	
	BaseFragment mFragment;

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		setSlideRole(R.layout.fragment_content);
		setSlideRole(R.layout.config_menu);

		getFromSharedPreferences.setharedPreferences(ConfigActivity.this);
		String name = getFromSharedPreferences.getName().trim();
		if (null == name || name.trim().equals("")) {
			name = "Adminstartor";
		}

		mSlideMenu = getSlideMenu();

		mSlideMenu.setInterpolator(new DecelerateInterpolator());
		mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
		mSlideMenu.setPrimaryShadowWidth(100);
		mSlideMenu.setEdgetSlideWidth(50);

		config_name = (TextView) findViewById(R.id.config_name);
		user_name = (TextView) findViewById(R.id.user_name);
		user_name.setText(name);
		modify_pwd = (TextView) findViewById(R.id.modify_pwd);
		tempView = modify_pwd;
		modify_pwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) v;
				changeFragment(tv, new ChangePWDFragment());
			}
		});

		modify_name = (TextView) findViewById(R.id.modify_name);
		modify_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) v;
				changeFragment(tv, new ChangeNameFragment());
			}
		});

		LinearLayout mBack = (LinearLayout) findViewById(R.id.goback);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mSlideMenu.isOpen()) {
					mSlideMenu.close(true);
					return;
				}
				finish();
			}
		});

		join_net = (TextView) findViewById(R.id.join_net);
		join_net.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) v;
				changeFragment(tv, new JoinNetFragment());
			}
		});

//		safe_center = (TextView) findViewById(R.id.safe_center);
//		safe_center.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				TextView tv = (TextView) v;
//				SafeCenterFragment mSafeCenterFragment=new SafeCenterFragment();
//				mFragment=mSafeCenterFragment;
//				changeFragment(tv, mSafeCenterFragment);
//			}
//		});
		
		all_dev=(TextView) findViewById(R.id.all_dev);
		all_dev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) v;
				AllDevicesListFragment mAllDevicesListFragment=new AllDevicesListFragment();
				mFragment=mAllDevicesListFragment;
				changeFragment(tv, mAllDevicesListFragment);
			}
		});
		mHandler.sendEmptyMessageDelayed(1, 150);
	}

	private void changeFragment(TextView v, Fragment f) {
		mSlideMenu.close(true);
		if (null != tempView) {
			tempView.setTextColor(mUnSelectedColor);
		}
		config_name.setText(v.getText().toString());
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		fragmentTransaction.replace(R.id.fragment_continer, f);
		fragmentTransaction.commit();
		v.setTextColor(mSelectedColor);
		tempView = v;
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, List<SimpleDevicesModel>> {

		@Override
		protected List<SimpleDevicesModel> doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<SimpleDevicesModel> result) {
			super.onPostExecute(result);
			 mFragment.stopRefresh();
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case 1:
				mSlideMenu.open(false, true);
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void setFragment(Fragment f) {
		mFragment=(BaseFragment) f;
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_continer, f);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	@Override
	public void refreshListData() {
		// TODO Auto-generated method stub
		new GetDataTask().execute();
	}

	@Override
	public SimpleDevicesModel getDeviceModle(int postion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFragment(Fragment mFragment, int postion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDevicesId(String id) {
		// TODO Auto-generated method stub

	}
}
