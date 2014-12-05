package com.gdgl.activity;

/***
 * 设置列表界面
 */
import java.util.List;

import com.gdgl.activity.BindControlFragment.backAction;
import com.gdgl.activity.ConfigDevicesListWithGroup.IntoDeviceDetailFragment;
import com.gdgl.activity.JoinNetDevicesListFragment.refreshData;
import com.gdgl.activity.JoinNetFragment.ChangeFragment;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.service.LibjingleService;
import com.gdgl.service.SmartService;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyApplication;
import com.gdgl.util.MyLogoutDlg;
import com.gdgl.util.MyLogoutDlg.DialogCheckBoxcallback;
import com.gdgl.util.MyLogoutDlg.Dialogcallback;
import com.gdgl.util.SelectPicPopupWindow;
import com.gdgl.util.SlideMenu;
import com.gdgl.util.VersionDlg;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConfigActivity extends BaseSlideMenuActivity implements
		ChangeFragment, refreshData, backAction, IntoDeviceDetailFragment, Dialogcallback, DialogCheckBoxcallback{
	private SlideMenu mSlideMenu;
	private MyLogoutDlg mMyOkCancleDlg;
	int mSelectedColor = 0xfff55656;
	int mUnSelectedColor = 0xff00b6ee;
	TextView modify_pwd, modify_name, join_net, all_dev, bind_control,
			messagemenu, user_control, logout, version;

	// TextView safe_center;
	TextView config_name, user_name;
	TextView tempView;
	Button clearMessageImageButton;

	BaseFragment mFragment;

	LinearLayout mfragment_continer, parents_need_Layout;

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		MyApplication.getInstance().addActivity(this);
		setSlideRole(R.layout.config_content);
		setSlideRole(R.layout.config_menu_new);

		getFromSharedPreferences.setsharedPreferences(ConfigActivity.this);
		String name = getFromSharedPreferences.getName().trim();
		if (null == name || name.trim().equals("")) {
			name = "Adminstartor";
		}

		mSlideMenu = getSlideMenu();

		mSlideMenu.setInterpolator(new DecelerateInterpolator());
		mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
		//mSlideMenu.setPrimaryShadowWidth(100);
		//mSlideMenu.setEdgetSlideWidth(50);
		mfragment_continer = (LinearLayout) findViewById(R.id.fragment_continer);
		parents_need_Layout = (LinearLayout) findViewById(R.id.parents_need);
		parents_need_Layout.setVisibility(View.GONE);
		config_name = (TextView) findViewById(R.id.title);
		user_name = (TextView) findViewById(R.id.user_name);
		messagemenu = (TextView) findViewById(R.id.message_menu);
		user_control = (TextView) findViewById(R.id.user_control);
		logout = (TextView) findViewById(R.id.logout);
		user_name.setText(name);
		modify_pwd = (TextView) findViewById(R.id.modify_pwd);
		clearMessageImageButton = (Button) findViewById(R.id.clear_message);
		clearMessageImageButton.setVisibility(View.GONE);

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
				// if (mSlideMenu.isOpen()) {
				// mSlideMenu.close(true);
				// return;
				// }
				if (!mSlideMenu.isOpen()) {
					mSlideMenu.open(false, true);
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

		bind_control = (TextView) findViewById(R.id.bind_control);
		bind_control.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) v;
				changeFragment(tv, new BindListFragment());
			}
		});
		all_dev = (TextView) findViewById(R.id.all_dev);
		tempView = all_dev;
		all_dev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) v;
				ConfigDevicesListWithGroup mAllDevicesListFragment = new ConfigDevicesListWithGroup();
				mFragment = mAllDevicesListFragment;
				changeFragment(tv, mAllDevicesListFragment);
			}
		});
		messagemenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MessageListFragment messageListFragment = new MessageListFragment();
				mFragment = messageListFragment;
				changeFragment((TextView) v, messageListFragment);

			}
		});
		version = (TextView) findViewById(R.id.version);
		version.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				VersionDlg vd=new VersionDlg(ConfigActivity.this);
				vd.show();
			}
			
		});
		logout = (TextView) findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mMyOkCancleDlg = new MyLogoutDlg(ConfigActivity.this);
				mMyOkCancleDlg.setDialogCallback(ConfigActivity.this);
				mMyOkCancleDlg.setDialogCheckBoxCallback(ConfigActivity.this);
                mMyOkCancleDlg.setContent("确定要退出系统吗?");
                mMyOkCancleDlg.show();
			}
			
		});
		initFragment();

		mHandler.sendEmptyMessageDelayed(1, 150);
	}

	private void initFragment() {
		// TODO Auto-generated method stub
		config_name.setText(all_dev.getText().toString());
		if (null == fragmentManager) {
			fragmentManager = this.getFragmentManager();
		}
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		Bundle mBundle = getIntent().getExtras();
		// 通过点击通知传过来的，进入消息管理界面
		if (mBundle != null && mBundle.getInt("fragid", -1) == 1) {
			MessageListFragment f = new MessageListFragment();
			config_name.setText(messagemenu.getText().toString());
			showClearMesssageBtn(f);
			fragmentTransaction.replace(R.id.fragment_continer, f);
		} else {
			fragmentTransaction.replace(R.id.fragment_continer,
					new ConfigDevicesListWithGroup());
		}

		fragmentTransaction.commit();
	}

	private void changeFragment(TextView v, final Fragment f) {
		mSlideMenu.close(true);
		if (null != tempView) {
			tempView.setTextColor(mUnSelectedColor);
		}
		clearMessageImageButton.setVisibility(View.GONE);
		if (f instanceof MessageListFragment) {
			showClearMesssageBtn(f);
		}
		config_name.setText(v.getText().toString());
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_continer, f);
		fragmentTransaction.commit();
		v.setTextColor(mSelectedColor);
		tempView = v;
	}

	private void showClearMesssageBtn(final Fragment f) {
		clearMessageImageButton.setVisibility(View.VISIBLE);
		clearMessageImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//((MessageListFragment) f).dialog();
				((MessageListFragment) f).deleteClick();
			}
		});
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
		mFragment = (BaseFragment) f;
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
	public DevicesModel getDeviceModle(int postion) {
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

	@Override
	public void intoDeviceDetailFragment(Fragment f) {
		// TODO Auto-generated method stub
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_continer, f);
		fragmentTransaction.addToBackStack(null);
		String string = "0";
		if (fragmentTransaction.isAddToBackStackAllowed()) {
			string = "1";
		}
		Log.i("fragment_continer", string);
		Log.i("fragment_continer",
				"" + fragmentManager.getBackStackEntryCount());
		fragmentTransaction.commit();
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub
		// LayoutParams mLayoutParams = new LayoutParams(
		// LinearLayout.LayoutParams.MATCH_PARENT,
		// LinearLayout.LayoutParams.MATCH_PARENT);
		// mfragment_continer.setLayoutParams(mLayoutParams);
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		fragmentTransaction.replace(R.id.fragment_continer,
				new BindListFragment());
		fragmentTransaction.commit();
	}
	
	public void dialogdo() {
		// TODO Auto-generated method stub
		MyApplication.getInstance().finishSystem();
	}

	@Override
	public void dialogcheck(boolean isChecked) {
		// TODO Auto-generated method stub
		if(!isChecked){
			Log.i("is checked ", " no........ ");
			Intent libserviceIntent = new Intent(this, LibjingleService.class);
			stopService(libserviceIntent);
			Intent smartServiceIntent = new Intent(this, SmartService.class);
			stopService(smartServiceIntent);
		}
	}
}
