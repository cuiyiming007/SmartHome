package com.gdgl.activity;

/***
 * 设置列表界面
 */
import com.gdgl.activity.BindControlFragment.backAction;
import com.gdgl.activity.ConfigDevicesExpandableList.IntoDeviceDetailFragment;
import com.gdgl.activity.JoinNetFragment.ChangeFragment;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.service.LibjingleService;
import com.gdgl.service.SmartService;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyApplication;
import com.gdgl.util.MyApplicationFragment;
import com.gdgl.util.MyLogoutDlg;
import com.gdgl.util.MyLogoutDlg.DialogCheckBoxcallback;
import com.gdgl.util.MyLogoutDlg.Dialogcallback;
import com.gdgl.util.SlideMenu;
import com.gdgl.util.VersionDlg;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ConfigActivity extends BaseSlideMenuActivity implements
		ChangeFragment, backAction, IntoDeviceDetailFragment,
		Dialogcallback, DialogCheckBoxcallback {
	private SlideMenu mSlideMenu;
	private MyLogoutDlg mMyLogoutDlg;
	int mSelectedColor = 0xfff55656;
	int mUnSelectedColor = 0xff00b6ee;
	TextView modify_pwd, modify_name, join_net, all_dev, bind_control,
			messagemenu, user_control, logout, version;

	// TextView safe_center;
	TextView config_name, user_name;
	TextView tempView;
	Button clearMessageImageButton;
	FragmentManager fragmentManager;
	BaseFragment mFragment;

	LinearLayout mfragment_continer, parents_need_Layout;

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		MyApplication.getInstance().addActivity(this);
		MyApplicationFragment.getInstance().setActivity(this);
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
		// mSlideMenu.setPrimaryShadowWidth(100);
		// mSlideMenu.setEdgetSlideWidth(50);
		mfragment_continer = (LinearLayout) findViewById(R.id.fragment_continer);
		parents_need_Layout = (LinearLayout) findViewById(R.id.parents_need);
		parents_need_Layout.setVisibility(View.GONE);
		config_name = (TextView) findViewById(R.id.title);
		user_name = (TextView) findViewById(R.id.user_name);
		user_name.setText(name);
		clearMessageImageButton = (Button) findViewById(R.id.clear_message);
		clearMessageImageButton.setVisibility(View.GONE);

		all_dev = (TextView) findViewById(R.id.all_dev);
		tempView = all_dev;
		join_net = (TextView) findViewById(R.id.join_net);
		bind_control = (TextView) findViewById(R.id.bind_control);
		messagemenu = (TextView) findViewById(R.id.message_menu);
		user_control = (TextView) findViewById(R.id.user_control);
		modify_pwd = (TextView) findViewById(R.id.modify_pwd);
		modify_name = (TextView) findViewById(R.id.modify_name);
		version = (TextView) findViewById(R.id.version);
		logout = (TextView) findViewById(R.id.logout);

		all_dev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) v;
				// ConfigDevicesListWithGroup mAllDevicesListFragment = new
				// ConfigDevicesListWithGroup();
				ConfigDevicesExpandableList mAllDevicesListFragment = new ConfigDevicesExpandableList();
				mFragment = mAllDevicesListFragment;
				changeFragment(tv, mAllDevicesListFragment);
				// MyApplicationFragment.getInstance().addNewTask(mAllDevicesListFragment);
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
		version.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				VersionDlg vd = new VersionDlg(ConfigActivity.this);
				vd.show();
			}

		});
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mMyLogoutDlg = new MyLogoutDlg(ConfigActivity.this);
				mMyLogoutDlg.setDialogCallback(ConfigActivity.this);
				mMyLogoutDlg.setDialogCheckBoxCallback(ConfigActivity.this);
				mMyLogoutDlg.setContent("确定要退出系统吗?");
				mMyLogoutDlg.show();
			}

		});
		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
			join_net.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView tv = (TextView) v;
					changeFragment(tv, new JoinNetFragment());
				}
			});
			bind_control.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView tv = (TextView) v;
					BindListFragment f = new BindListFragment();
					changeFragment(tv, f);
					// MyApplicationFragment.getInstance().addNewTask(f);
				}
			});
			modify_pwd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView tv = (TextView) v;
					changeFragment(tv, new ChangePWDFragment());
				}
			});
			modify_name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView tv = (TextView) v;
					changeFragment(tv, new ChangeNameFragment());
				}
			});
		} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
			join_net.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					VersionDlg vd = new VersionDlg(ConfigActivity.this);
					vd.setContent(getResources().getString(R.string.Unable_In_InternetState));
					vd.show();
				}
			});
			bind_control.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					VersionDlg vd = new VersionDlg(ConfigActivity.this);
					vd.setContent(getResources().getString(R.string.Unable_In_InternetState));
					vd.show();
				}
			});
			modify_pwd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					VersionDlg vd = new VersionDlg(ConfigActivity.this);
					vd.setContent(getResources().getString(R.string.Unable_In_InternetState));
					vd.show();
				}
			});
			modify_name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					VersionDlg vd = new VersionDlg(ConfigActivity.this);
					vd.setContent(getResources().getString(R.string.Unable_In_InternetState));
					vd.show();
				}
			});
		}

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

		initFragment();

		mHandler.sendEmptyMessageDelayed(1, 150);
		fragmentManager = getSupportFragmentManager();
		Log.i("FragmentManager Count", fragmentManager.getBackStackEntryCount()
				+ "");
	}

	private void initFragment() {
		// TODO Auto-generated method stub
		config_name.setText(all_dev.getText().toString());
		if (null == fragmentManager) {
			fragmentManager = this.getSupportFragmentManager();
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
			// Fragment f = new ConfigDevicesListWithGroup();
			Fragment f = new ConfigDevicesExpandableList();
			fragmentTransaction.replace(R.id.fragment_continer, f);
			MyApplicationFragment.getInstance().addNewTask(f);
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
		// fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		MyApplicationFragment.getInstance().addNewTask(f);
		v.setTextColor(mSelectedColor);
		tempView = v;
	}

	private void showClearMesssageBtn(final Fragment f) {
		clearMessageImageButton.setVisibility(View.VISIBLE);
		clearMessageImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ((MessageListFragment) f).dialog();
				((MessageListFragment) f).deleteClick();
			}
		});
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
		// fragmentTransaction.replace(R.id.fragment_continer, f);
		fragmentTransaction.add(R.id.fragment_continer, f);
		// fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		MyApplicationFragment.getInstance().addFragment(f);
	}

	@Override
	public void intoDeviceDetailFragment(Fragment f) {
		// TODO Auto-generated method stub
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.fragment_continer, f);
		// fragmentTransaction.replace(R.id.fragment_continer, f);
		// fragmentTransaction.addToBackStack(null);
		String string = "0";
		if (fragmentTransaction.isAddToBackStackAllowed()) {
			string = "1";
		}
		Log.i("fragment_continer", string);
		Log.i("fragment_continer",
				"" + fragmentManager.getBackStackEntryCount());
		fragmentTransaction.commit();
		MyApplicationFragment.getInstance().addFragment(f);
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
		if (!isChecked) {
			Log.i("is checked ", " no........ ");
			Intent libserviceIntent = new Intent(this, LibjingleService.class);
			stopService(libserviceIntent);
			Intent smartServiceIntent = new Intent(this, SmartService.class);
			stopService(smartServiceIntent);
		}
	}

	public void onBackPressed() {
		if (MyApplicationFragment.getInstance().getFragmentListSize() > 1) {
			MyApplicationFragment.getInstance().removeLastFragment();
			return;
		}
		super.onBackPressed();
	}

}
