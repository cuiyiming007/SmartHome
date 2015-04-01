package com.gdgl.activity;

import h264.com.VideoInfoAddDialog;
import h264.com.VideoInfoDialog;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gdgl.adapter.ViewPagerAdapter;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.manager.WarnManager;
import com.gdgl.model.TabInfo;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.AddDlg;
import com.gdgl.util.AddDlg.AddDialogcallback;
import com.gdgl.util.MyApplication;
import com.gdgl.util.PullToRefreshViewPager;
import com.gdgl.util.SelectPicPopupWindow;
import com.gdgl.util.VersionDlg;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class SmartHome extends FragmentActivity implements
		OnRefreshListener<ViewPager>, OnPageChangeListener, AddDialogcallback,
		UIListener {

	private final static String TAG = "SmartHome";

	SelectPicPopupWindow mSetWindow;
	Button set;

	// private NetWorkChangeReciever networkreChangeReciever;

	PullToRefreshViewPager pull_refresh_viewpager;

	TextView mTitle;
	Button notifyButton;
	VideoInfoAddDialog mAddDlg;

	ImageButton devicesButton, videoButton, regionButton, scenceButton;
	List<ImageButton> mImageButtonsList = new ArrayList<ImageButton>();

	TextView unreadMessageView;

	int mCurrentTab = 0;
	int mLastTab = -1;
	ArrayList<TabInfo> mList;
	Button mAdd;

	ViewPager mViewPager;
	VideoFragment videoFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		saveLoginData();
		init();

	}
	
	private void saveLoginData(){
		getFromSharedPreferences.setsharedPreferences(SmartHome.this);
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		String name = intent.getStringExtra("name");
		String pwd = intent.getStringExtra("pwd");
		String cloud = intent.getStringExtra("cloud");
		boolean remenber = intent.getBooleanExtra("remenber", true);
		getFromSharedPreferences.setUid(id);
		if (remenber) {
			getFromSharedPreferences.setLogin(name, pwd, true);
		} else {
			getFromSharedPreferences.setLogin(name, pwd, false);
		}
		getFromSharedPreferences.setUserList(name, pwd);
		getFromSharedPreferences.setCloud(cloud);
		getFromSharedPreferences.setCloudList(cloud);
	}

	private void init() {
		setContentView(R.layout.main_activity);
		pull_refresh_viewpager = (PullToRefreshViewPager) findViewById(R.id.pull_refresh_viewpager);

		pull_refresh_viewpager.setOnRefreshListener(this);

		mViewPager = pull_refresh_viewpager.getRefreshableView();

		videoFragment = new VideoFragment();
		mList = new ArrayList<TabInfo>();
		// mList.add(new TabInfo(new CommonUseFragment()));
		mList.add(new TabInfo(new DevicesFragment()));
		mList.add(new TabInfo(videoFragment));
		mList.add(new TabInfo(new RegionsFragment()));
		mList.add(new TabInfo(new ScenesFragment()));

		ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(
				getSupportFragmentManager(), SmartHome.this, mList);

		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
		set = (Button) findViewById(R.id.set);
		set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// showSetWindow();
				startConfigActivity();
			}
		});

		mLastTab = mCurrentTab;

		mTitle = (TextView) findViewById(R.id.title);
		notifyButton = (Button) findViewById(R.id.alarm_btn);
		unreadMessageView = (TextView) findViewById(R.id.unread_tv);

		devicesButton = (ImageButton) findViewById(R.id.devices_btn);
		videoButton = (ImageButton) findViewById(R.id.video_urveillance_btn);
		regionButton = (ImageButton) findViewById(R.id.region_btn);
		scenceButton = (ImageButton) findViewById(R.id.scence_btn);

		mImageButtonsList.add(devicesButton);
		mImageButtonsList.add(videoButton);
		mImageButtonsList.add(regionButton);
		mImageButtonsList.add(scenceButton);
		setTab_TitleButtonColour(mCurrentTab);

		TitleClickLister mTitleClickLister = new TitleClickLister();
		for (int m = 0; m < mImageButtonsList.size(); m++) {
			mImageButtonsList.get(m).setOnClickListener(mTitleClickLister);
		}
		initadd();

	}

	private void initadd() {
		// TODO Auto-generated method stub
		mAdd = (Button) findViewById(R.id.add);
		if (mCurrentTab == 0) {
			mAdd.setVisibility(View.GONE);
		} else {
			mAdd.setVisibility(View.VISIBLE);
		}
		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
			mAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (2 == mCurrentTab) {
						AddDlg mAddDlg = new AddDlg(SmartHome.this,
								AddDlg.REGION);
						mAddDlg.setContent("添加区域");
						mAddDlg.setType("区域名称");
						mAddDlg.setDialogCallback(SmartHome.this);
						mAddDlg.show();
					} else if (3 == mCurrentTab) {
						AddDlg mAddDlg = new AddDlg(SmartHome.this,
								AddDlg.SCENE);
						mAddDlg.setContent("添加场景");
						mAddDlg.setType("场景名称");
						mAddDlg.setDialogCallback(SmartHome.this);
						mAddDlg.show();
						// } else if (0 == mCurrentTab) {
						// Intent i = new Intent();
						// i.setClass(SmartHome.this,
						// AddCommonUsedActivity.class);
						// startActivity(i);
					} else if (1 == mCurrentTab) {
						if (mAddDlg == null) {
							mAddDlg = new VideoInfoAddDialog(SmartHome.this,
									VideoInfoDialog.Add, videoFragment);
						} else {
							mAddDlg.getInitVideoNode();
						}
						mAddDlg.setContent("添加");
						// mAddDlg.setType("区域名称");
						// mAddDlg.setDialogCallback(SmartHome.this);
						mAddDlg.show();
					}
				}
			});
		} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
			mAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					VersionDlg vd = new VersionDlg(SmartHome.this);
					vd.setContent(getResources().getString(R.string.Unable_In_InternetState));
					vd.show();
				}
			});
		}
		notifyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(SmartHome.this, ConfigActivity.class);
				i.putExtra("fragid", 1);
				startActivity(i);
				WarnManager.getInstance().intilMessageNum();
			}
		});
	}

	class TitleClickLister implements OnClickListener {

		int index;

		public TitleClickLister() {

		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (mImageButtonsList.contains(v)) {
				index = getTheNumberOfTabTitlePressed(v);
			}
			mViewPager.setCurrentItem(index);

			mImageButtonsList.get(index).setBackgroundResource(
					TAB_TITLE_SELECTED[index]);

			mTitle.setText(TAB_TITLE_NAME[index]);
			// mTextViewList.get(index).setTextColor(mSelectedColor);
			//
			// mTitle.setText(mTextViewList.get(index).getText());
		}
	}

	private static String[] TAB_TITLE_NAME = { "设备", "监控", "区域", "场景" };
	private static int[] TAB_TITLE_SELECTED = {
			R.drawable.ui_tabtitle_devices_pressed,
			R.drawable.ui_tabtitle_video_pressed,
			R.drawable.ui_tabtitle_region_pressed,
			R.drawable.ui_tabtitle_scence_pressed };
	private static int[] TAB_TITLE_UNSELECTED = {
			R.drawable.ui_tabtitle_devices, R.drawable.ui_tabtitle_video,
			R.drawable.ui_tabtitle_region, R.drawable.ui_tabtitle_scence };

	public void setTab_TitleButtonColour(int m) {
		getTheNumberOfTabTitlePressed(null);
		mImageButtonsList.get(m).setBackgroundResource(TAB_TITLE_SELECTED[m]);
		mTitle.setText(TAB_TITLE_NAME[m]);
	}

	public int getTheNumberOfTabTitlePressed(View v) {
		int m;
		for (m = 0; m < mImageButtonsList.size(); m++) {
			if (mImageButtonsList.get(m).equals(v)) {
				return m;
			}
			mImageButtonsList.get(m).setBackgroundResource(
					TAB_TITLE_UNSELECTED[m]);
		}
		return m;
	}

	public void showSetWindow() {
		mSetWindow = new SelectPicPopupWindow(SmartHome.this, mSetOnClick);
		mSetWindow.showAtLocation(SmartHome.this.findViewById(R.id.set),
				Gravity.TOP | Gravity.RIGHT, 10, 150);
	}

	private OnClickListener mSetOnClick = new OnClickListener() {

		public void onClick(View v) {
			mSetWindow.dismiss();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_MENU)) {
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {
		// networkreChangeReciever = new NetWorkChangeReciever();
		// IntentFilter networkintentFilter = new IntentFilter();
		// networkintentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		// registerReceiver(networkreChangeReciever, networkintentFilter);

		super.onStart();

	}

	@Override
	protected void onResume() {
		CallbackManager.getInstance().addObserver(this);
		updateMessageNum();
		super.onResume();
	}

	private void updateMessageNum() {
		int messageNum = WarnManager.getInstance().getMessageNum();

		if (messageNum == 0) {
			unreadMessageView.setVisibility(View.GONE);
		} else {
			unreadMessageView.setVisibility(View.VISIBLE);
			unreadMessageView.setText(String.valueOf(messageNum));
		}
	}

	@Override
	protected void onStop() {
		// unregisterReceiver(networkreChangeReciever);

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		CallbackManager.getInstance().deleteObserver(this);
		super.onDestroy();
		// stopService(serviceIntent);
		// unregisterReceiver(msgReceiver);

	}

	// public class MsgReceiver extends BroadcastReceiver {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // int progress = intent.getIntExtra("progress", 0);
	//
	// Log.i(TAG, "MsgReceiver has recieved");
	// }
	//
	// }

	private class GetDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			pull_refresh_viewpager.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<ViewPager> refreshView) {
		// TODO Auto-generated method stub
		new GetDataTask().execute();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
			mLastTab = mCurrentTab;
			if (mCurrentTab == 0) {
				mAdd.setVisibility(View.GONE);
			} else {
				mAdd.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		mCurrentTab = arg0;
		setTab_TitleButtonColour(arg0);
		// setMyTextColor(arg0);
		if (mCurrentTab == 0) {
			mAdd.setVisibility(View.GONE);
		} else {
			mAdd.setVisibility(View.VISIBLE);
		}
	}

	public interface refreshAdapter {
		public void refreshFragment();
	}

	@Override
	public void refreshdata() {
		// TODO Auto-generated method stub
		mList.get(mCurrentTab).refreshFragment();
		// refreshAdapter mrefreshAdapter=(refreshAdapter)fragmentsList.get(2);
		// mrefreshAdapter.refreshFragment();
	}

	@Override
	public void update(Manger observer, Object object) {
		Event data = (Event) object;
		if (EventType.WARN == data.getType()) {
			notifyButton.post(new Runnable() {

				@Override
				public void run() {
					updateMessageNum();
				}
			});
		}

	}

	public void startConfigActivity() {
		Intent i = new Intent();
		i.setClass(SmartHome.this, ConfigActivity.class);
		SmartHome.this.startActivity(i);
	}

}
