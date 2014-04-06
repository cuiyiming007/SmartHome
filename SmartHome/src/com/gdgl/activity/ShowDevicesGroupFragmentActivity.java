package com.gdgl.activity;

import java.util.List;

import com.gdgl.GalleryFlow.FancyCoverFlow;
import com.gdgl.activity.BaseControlFragment.SaveDevicesName;
import com.gdgl.activity.DevicesListFragment.refreshData;
import com.gdgl.adapter.DevicesBaseAdapter;
import com.gdgl.adapter.ViewGroupAdapter;
import com.gdgl.adapter.DevicesBaseAdapter.DevicesObserver;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataUtil;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.SelectPicPopupWindow;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;
import com.gdgl.util.UiUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class ShowDevicesGroupFragmentActivity extends FragmentActivity
		implements refreshData, DevicesObserver, SaveDevicesName,
		Dialogcallback {

	private static final String TAG = "ShowDevicesGroupFragmentActivity";
	LinearLayout mBack;
	List<List<DevicesModel>> mList;
	List<DevicesModel> mCurrentList;
	private int mListIndex = 0;

	private int[] images;
	private String[] tags;

	FancyCoverFlow fancyCoverFlow;

	FragmentManager fragmentManager;

	DevicesListFragment mDevicesListFragment;

	DevicesBaseAdapter mDevicesBaseAdapter;
	
	SelectPicPopupWindow mSetWindow;
	

	private int type;
	private int devicesId = 0;
	private int mCurrentListItemPostion;

	public static final String ACTIVITY_SHOW_DEVICES_TYPE = "activity_show_devices_type";

	boolean isDelete = false;

	TextView title;
	LinearLayout parents_need, devices_need;
	Button mDelete;
	Button set;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_devices_group);
		Bundle mBundle = getIntent().getExtras();
		if (null != mBundle) {
			type = mBundle.getInt(ACTIVITY_SHOW_DEVICES_TYPE, 0);
		}
		initData();
		initFancyCoverFlow();
		initDevicesListFragment();
		initTitle();
		initTitleByTag(mListIndex);
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		final MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
				ShowDevicesGroupFragmentActivity.this);
		mMyOkCancleDlg.setDialogCallback(ShowDevicesGroupFragmentActivity.this);
		mBack = (LinearLayout) findViewById(R.id.goback);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				back();
			}
		});

		title = (TextView) findViewById(R.id.title);
		parents_need = (LinearLayout) findViewById(R.id.parent_need);
		devices_need = (LinearLayout) findViewById(R.id.devices_need);

		mDelete = (Button) findViewById(R.id.delete);
		mDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mMyOkCancleDlg.setContent("确定要删除"+mList.get(mListIndex).get(devicesId).getDevicesName()+"吗?");
				mMyOkCancleDlg.show();
			}
		});
		set=(Button) findViewById(R.id.set);
		set.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSetWindow();
			}
		});
	}
	
	public void showSetWindow() {
		mSetWindow = new SelectPicPopupWindow(ShowDevicesGroupFragmentActivity.this, mSetOnClick);
		mSetWindow.showAtLocation(ShowDevicesGroupFragmentActivity.this.findViewById(R.id.set),
				Gravity.TOP | Gravity.RIGHT, 10, 150);
	}

	
	private OnClickListener mSetOnClick = new OnClickListener() {

		public void onClick(View v) {
			mSetWindow.dismiss();
		}
	};
	
	private void initData() {
		// TODO Auto-generated method stub
		mList = DataUtil.getdata(type);
		images = UiUtils.getDevicesManagerImage(type);
		tags = UiUtils.getDevicesManagerTag(type);

		fragmentManager = this.getFragmentManager();

		mCurrentList = mList.get(mListIndex);
	}

	private void initDevicesListFragment() {
		// TODO Auto-generated method stub
		mDevicesBaseAdapter = new DevicesBaseAdapter(
				ShowDevicesGroupFragmentActivity.this, mCurrentList, this);
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		mDevicesListFragment = new DevicesListFragment();
		fragmentTransaction.replace(R.id.devices_control_fragment,
				mDevicesListFragment, "LightsControlFragment");
		mDevicesListFragment.setAdapter(mDevicesBaseAdapter);
		fragmentTransaction.commit();
	}
	
	private void back(){
		if (fragmentManager.getBackStackEntryCount() > 0) {
			fragmentManager.popBackStack();
			initTitleByTag(mListIndex);
		} else {
			finish();
		}
	}
	
	private void initFancyCoverFlow() {
		// TODO Auto-generated method stub
		fancyCoverFlow = (FancyCoverFlow) findViewById(R.id.devices_gallery);
		fancyCoverFlow.setAdapter(new ViewGroupAdapter(getApplicationContext(),
				images, tags, 250, 200));
		fancyCoverFlow.setCallbackDuringFling(false);
		fancyCoverFlow.setSelection(1);
		fancyCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				changeForCoverFlow(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		fancyCoverFlow.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				changeForCoverFlow(position);
			}
		});
	}

	public void changeForCoverFlow(int p) {
		if (fragmentManager.getBackStackEntryCount() > 0) {
			fragmentManager.popBackStack();
		}
		if (mListIndex != p) {
			mListIndex = p;
			devicesId = 0;
			initTitleByTag(mListIndex);
			refreshAdapter(mListIndex);
		}
	}

	public void refreshAdapter(int postion) {
		mCurrentList = null;
		mCurrentList = mList.get(mListIndex);
		mDevicesBaseAdapter.setList(mCurrentList);
		mDevicesBaseAdapter.notifyDataSetChanged();
		mDevicesListFragment.setLayout();
	}

	private class GetDataTask extends AsyncTask<Void, Void, List<DevicesModel>> {

		@Override
		protected List<DevicesModel> doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return mList.get(mListIndex);
		}

		@Override
		protected void onPostExecute(List<DevicesModel> result) {
			super.onPostExecute(result);
			mDevicesListFragment.stopRefresh();
		}
	}

	public void refreshListData() {
		// TODO Auto-generated method stub
		new GetDataTask().execute();
	}

	public interface adapterSeter {
		public void setAdapter(BaseAdapter mAdapter);

		public void stopRefresh();

		public void setSelectedPostion(int postion);
	}

	@Override
	public void setDevicesId(int id) {
		// TODO Auto-generated method stub
		devicesId = id;
	}

	@Override
	public void setFragment(Fragment mFragment, int postion) {
		// TODO Auto-generated method stub
		Log.i(TAG, "zzz->setFragment postion=" + postion);
		mCurrentListItemPostion = postion;
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.devices_control_fragment, mFragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		initTitleByDevices(devicesId);
	}

	public void initTitleByTag(int postion) {
		title.setText(tags[postion]);
		parents_need.setVisibility(View.VISIBLE);
		devices_need.setVisibility(View.GONE);
	}

	public void initTitleByDevices(int devicesId) {
		title.setText(mList.get(mListIndex).get(devicesId).getDevicesName());
		parents_need.setVisibility(View.GONE);
		devices_need.setVisibility(View.VISIBLE);
	}

	public interface EditDevicesName {
		public void editDevicesName();
	}

	@Override
	public void saveDevicesName(String name) {
		// TODO Auto-generated method stub

	}
	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		back();
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		mList.get(mListIndex).remove(devicesId);
		mCurrentList = null;
		fragmentManager.popBackStack();
		refreshAdapter(mListIndex);
		initTitleByTag(mListIndex);
	}
}
