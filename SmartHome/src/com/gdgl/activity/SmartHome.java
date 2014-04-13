package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.adapter.ViewPagerAdapter;
import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.LightManager;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.TabInfo;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DevParam;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.reciever.NetWorkChangeReciever;
import com.gdgl.service.SmartService;
import com.gdgl.smarthome.R;
import com.gdgl.util.SelectPicPopupWindow;
import com.gdgl.util.ViewPagerCompat;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SmartHome extends FragmentActivity {

	private final static String TAG = "SmartHome";

	SelectPicPopupWindow mSetWindow;
	Button set;

	Intent serviceIntent;
	private MsgReceiver msgReceiver;
	private NetWorkChangeReciever networkreChangeReciever;

	DataHelper mDateHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();

	}

	private void init() {
		setContentView(R.layout.activity_main);
		ViewPagerCompat mView = (ViewPagerCompat) findViewById(R.id.mViewPager);

		ArrayList<TabInfo> mList = new ArrayList<TabInfo>();
		mList.add(new TabInfo(new firstFragment()));
		mList.add(new TabInfo(new secondFragment()));

		ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(
				getSupportFragmentManager(), SmartHome.this, mList);

		mView.setAdapter(mViewPagerAdapter);

		set = (Button) findViewById(R.id.set);
		set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSetWindow();
//				ArrayList<ResponseParamsEndPoint> devDataList = DeviceManager
//						.getInstance().getDeviceListFromLocalString();
//				mDateHelper = new DataHelper(SmartHome.this);
//				SQLiteDatabase mSQLiteDatabase = mDateHelper
//						.getSQLiteDatabase();
//				mDateHelper.insertList(mSQLiteDatabase,
//						DataHelper.DEVICES_TABLE, null, devDataList);
//				Log.i(TAG, "tag-> begin query" + System.currentTimeMillis());
//
//				List<DevicesModel> mList = mDateHelper.queryForList(
//						mSQLiteDatabase, DataHelper.DEVICES_TABLE, null, null,
//						null, null, null, null, null);
//				for (DevicesModel devicesModel : mList) {
//					Log.i(TAG, "tag->" + devicesModel.getID());
//				}
//				mDateHelper.close(mSQLiteDatabase);
//				Log.i(TAG, "tag-> begin query" + System.currentTimeMillis());
//				
//				LightManager.getInstance().OnOffOutputOperation();
			}
		});

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
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {
		super.onStart();
		msgReceiver = new MsgReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.gdgl.activity.RECIEVER");
		registerReceiver(msgReceiver, intentFilter);

		serviceIntent = new Intent(this, SmartService.class);
		startService(serviceIntent);
	}

	@Override
	protected void onResume() {
		networkreChangeReciever = new NetWorkChangeReciever();
		IntentFilter networkintentFilter = new IntentFilter();
		networkintentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(networkreChangeReciever, networkintentFilter);

		super.onResume();
	}
	@Override
	protected void onStop() {
		unregisterReceiver(networkreChangeReciever);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(serviceIntent);
		unregisterReceiver(msgReceiver);
		
	}

	public class MsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// int progress = intent.getIntExtra("progress", 0);

			Log.i(TAG, "MsgReceiver has recieved");
		}

	}
}
