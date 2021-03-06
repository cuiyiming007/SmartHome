package com.gdgl.activity;

import h264.com.VideoInfoDialog;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

import com.gdgl.adapter.ViewPagerAdapter;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.manager.WarnManager;
import com.gdgl.model.TabInfo;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.smarthome.R;
import com.gdgl.util.AddDlg;
import com.gdgl.util.AddDlg.AddDialogcallback;
import com.gdgl.util.PullToRefreshViewPager;
import com.gdgl.util.SelectPicPopupWindow;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class SmartHome extends FragmentActivity implements
		OnRefreshListener<ViewPager>, OnPageChangeListener, AddDialogcallback,UIListener{

	private final static String TAG = "SmartHome";

	SelectPicPopupWindow mSetWindow;
	Button set;

	// private NetWorkChangeReciever networkreChangeReciever;

	PullToRefreshViewPager pull_refresh_viewpager;

	TextView mTitle;
	Button notifyButton;
	TextView unreadMessageView;

	TextView devices;
	TextView region;
	TextView scence;
	TextView common_used;
	TextView video_urveillance;
	List<TextView> mTextViewList = new ArrayList<TextView>();

	int mSelectedColor = 0xff228B22;
	int mUnSelectedColor = Color.BLACK;

	int mCurrentTab = 0;
	int mLastTab = -1;
	ArrayList<TabInfo> mList;
	Button mAdd;

	ViewPager mViewPager;
	VideoFragment videoFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();

	}

	private void init() {
		setContentView(R.layout.main_activity);
		pull_refresh_viewpager = (PullToRefreshViewPager) findViewById(R.id.pull_refresh_viewpager);

		pull_refresh_viewpager.setOnRefreshListener(this);

		mViewPager = pull_refresh_viewpager.getRefreshableView();
		videoFragment = new VideoFragment();

		mList = new ArrayList<TabInfo>();
		mList.add(new TabInfo(new CommonUseFragment()));
		mList.add(new TabInfo(new DevicesFragment()));
		mList.add(new TabInfo(new RegionsFragment()));
		mList.add(new TabInfo(new ScenesFragment()));
		mList.add(new TabInfo(videoFragment));

		ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(
				getSupportFragmentManager(), SmartHome.this, mList);

		mViewPager.setAdapter(mViewPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
		set = (Button) findViewById(R.id.set);
		set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSetWindow();
			}
		});

		mLastTab = mCurrentTab;

		mTitle = (TextView) findViewById(R.id.title);
		notifyButton=(Button) findViewById(R.id.alarm_btn);
		unreadMessageView=(TextView) findViewById(R.id.unread_tv);
		devices = (TextView) findViewById(R.id.devices);
		region = (TextView) findViewById(R.id.region);
		scence = (TextView) findViewById(R.id.scence);
		common_used = (TextView) findViewById(R.id.common_used);
		video_urveillance = (TextView) findViewById(R.id.video_urveillance);

		mTextViewList.add(common_used);
		mTextViewList.add(devices);
		mTextViewList.add(region);
		mTextViewList.add(scence);
		mTextViewList.add(video_urveillance);
		setMyTextColor(mCurrentTab);

		TitleClickLister mTitleClickLister = new TitleClickLister();
		for (int m = 0; m < mTextViewList.size(); m++) {
			mTextViewList.get(m).setOnClickListener(mTitleClickLister);
		}
		initadd();
	}

	private void initadd() {
		// TODO Auto-generated method stub
		mAdd = (Button) findViewById(R.id.add);
		mAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (2 == mCurrentTab) {
					AddDlg mAddDlg = new AddDlg(SmartHome.this, AddDlg.REGION);
					mAddDlg.setContent("添加区域");
					mAddDlg.setType("区域名称");
					mAddDlg.setDialogCallback(SmartHome.this);
					mAddDlg.show();
				} else if (3 == mCurrentTab) {
					AddDlg mAddDlg = new AddDlg(SmartHome.this, AddDlg.SCENE);
					mAddDlg.setContent("添加场景");
					mAddDlg.setType("场景名称");
					mAddDlg.setDialogCallback(SmartHome.this);
					mAddDlg.show();
				} else if (0 == mCurrentTab) {
					Intent i = new Intent();
					i.setClass(SmartHome.this, AddCommonUsedActivity.class);
					startActivity(i);
				} else if (4 == mCurrentTab) {
					VideoInfoDialog mAddDlg = new VideoInfoDialog(
							SmartHome.this, VideoInfoDialog.Add, videoFragment);
					mAddDlg.setContent("添加");
					// mAddDlg.setType("区域名称");
					// mAddDlg.setDialogCallback(SmartHome.this);
					mAddDlg.show();
				}
			}
		});
		notifyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SmartHome.this,
						ConfigActivity.class);
				i.putExtra("fragid",1);
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

			if (mTextViewList.contains(v)) {
				index = getMId(v);
			}
			mViewPager.setCurrentItem(index);

			mTextViewList.get(index).setTextColor(mSelectedColor);

			mTitle.setText(mTextViewList.get(index).getText());
		}
	}

	public void setMyTextColor(int m) {
		getMId(null);
		mTextViewList.get(m).setTextColor(mSelectedColor);
		mTitle.setText(mTextViewList.get(m).getText());
	}

	public int getMId(View v) {
		// TODO Auto-generated method stub
		int m;
		for (m = 0; m < mTextViewList.size(); m++) {
			if (mTextViewList.get(m).equals(v)) {
				return m;
			}
			mTextViewList.get(m).setTextColor(mUnSelectedColor);
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
		int messageNum=WarnManager.getInstance().getMessageNum();
		
		if (messageNum==0) {
			unreadMessageView.setVisibility(View.GONE);
		}else{
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
			if (mCurrentTab==1) {
				mAdd.setVisibility(View.GONE);
			}else
			{
				mAdd.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		mCurrentTab = arg0;
		setMyTextColor(arg0);
		if (mCurrentTab==1) {
			mAdd.setVisibility(View.GONE);
		}else
		{
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
		Event data=(Event) object;
		if (EventType.WARN==data.getType()) {
			notifyButton.post(new Runnable() {
				
				@Override
				public void run() {
					updateMessageNum();
				}
			});
		}
		
	}
}
