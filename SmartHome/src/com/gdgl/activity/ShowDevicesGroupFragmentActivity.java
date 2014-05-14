package com.gdgl.activity;

import java.util.HashMap;
import java.util.List;

import com.gdgl.GalleryFlow.FancyCoverFlow;
import com.gdgl.activity.BaseControlFragment.UpdateDevice;
import com.gdgl.activity.DevicesListFragment.refreshData;
import com.gdgl.activity.DevicesListFragment.setData;
import com.gdgl.adapter.DevicesBaseAdapter;
import com.gdgl.adapter.ViewGroupAdapter;
import com.gdgl.adapter.DevicesBaseAdapter.DevicesObserver;
import com.gdgl.manager.LightManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.SimpleResponseData;
import com.gdgl.mydata.getlocalcielist.elserec;
import com.gdgl.smarthome.R;
import com.gdgl.util.EditDevicesDlg.EditDialogcallback;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.SelectPicPopupWindow;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;
import com.gdgl.util.UiUtils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class ShowDevicesGroupFragmentActivity extends FragmentActivity
		implements refreshData, DevicesObserver, UpdateDevice, Dialogcallback,
		EditDialogcallback, setData, UIListener {

	private static final String TAG = "ShowDevicesGroupFragmentActivity";
	LinearLayout mBack;

	List<SimpleDevicesModel> mCurrentList;
	HashMap<Integer, List<SimpleDevicesModel>> mDevicesListCache;

	private int mListIndex = 0;

	private int[] types;
	private int[] images;
	private String[] tags;

	FancyCoverFlow fancyCoverFlow;

	FragmentManager fragmentManager;

	DevicesListFragment mDevicesListFragment;

	DevicesBaseAdapter mDevicesBaseAdapter;

	SelectPicPopupWindow mSetWindow;

	ViewGroup mNoDevices;
	TextView mContent;

	private int type;
	private String devicesIeee = "";
	private int mCurrentListItemPostion;

	public static final String ACTIVITY_SHOW_DEVICES_TYPE = "activity_show_devices_type";
	public static final int cacheSize = 512 * 1024;
	boolean isDelete = false;

	TextView title;
	LinearLayout parents_need, devices_need;
	Button mDelete;
	Button set;

	LightManager temptureManager;

	DataHelper mDataHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_devices_group);
		Bundle mBundle = getIntent().getExtras();
		if (null != mBundle) {
			type = mBundle.getInt(ACTIVITY_SHOW_DEVICES_TYPE, 0);
		}
		mListIndex = type;
		initData();
		initNoContent();
		initFancyCoverFlow();
		initDevicesListFragment();
		initTitle();
		initTitleByTag(mListIndex);
	}

	private void initNoContent() {
		// TODO Auto-generated method stub
		mNoDevices = (ViewGroup) findViewById(R.id.nodevices_layout);
		mContent = (TextView) mNoDevices.findViewById(R.id.text_content);
		mContent.setText("此项暂无任何设备");
		if (null == mCurrentList || mCurrentList.size() == 0) {
			mNoDevices.setVisibility(View.VISIBLE);
		}
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
				mMyOkCancleDlg.setContent("确定要删除"
						+ getCurrentDeviceByIeee(devicesIeee).getmNodeENNAme()
						+ "吗?");
				mMyOkCancleDlg.show();
			}
		});
		set = (Button) findViewById(R.id.set);
		set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSetWindow();
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		temptureManager.deleteObserver(this);
	}

	public SimpleDevicesModel getCurrentDeviceByIeee(String iee) {
		if (null != mCurrentList) {
			for (SimpleDevicesModel mSimpleDevicesModel : mCurrentList) {
				if (iee == mSimpleDevicesModel.getmIeee()) {
					return mSimpleDevicesModel;
				}
			}
		}
		return null;
	}

	public void showSetWindow() {
		mSetWindow = new SelectPicPopupWindow(
				ShowDevicesGroupFragmentActivity.this, mSetOnClick);
		mSetWindow.showAtLocation(
				ShowDevicesGroupFragmentActivity.this.findViewById(R.id.set),
				Gravity.TOP | Gravity.RIGHT, 10, 150);
	}

	private OnClickListener mSetOnClick = new OnClickListener() {

		public void onClick(View v) {
			mSetWindow.dismiss();
		}
	};

	private void initData() {
		// TODO Auto-generated method stub
		temptureManager = LightManager.getInstance();
		temptureManager.addObserver(this);
		mDataHelper = new DataHelper(ShowDevicesGroupFragmentActivity.this);
		List<SimpleDevicesModel> list;
		if (type == UiUtils.LIGHTS_MANAGER) {
			list = DataUtil.getLightingManagementDevices(
					ShowDevicesGroupFragmentActivity.this, mDataHelper);
		} else {
			list = DataUtil.getOtherManagementDevices(
					ShowDevicesGroupFragmentActivity.this, mDataHelper, type);
		}

		List<SimpleDevicesModel> temPlist;
		temPlist = DataUtil.getOtherManagementDevices(
				ShowDevicesGroupFragmentActivity.this, mDataHelper,
				UiUtils.ENVIRONMENTAL_CONTROL);
		images = UiUtils.getImgByType(UiUtils.SECURITY_CONTROL);
		tags = UiUtils.getTagsByType(UiUtils.SECURITY_CONTROL);
		types = UiUtils.getType(UiUtils.SECURITY_CONTROL);
		mCurrentList = list;
		mDevicesListCache = new HashMap<Integer, List<SimpleDevicesModel>>();
		mDevicesListCache.put(type, list);
		mDevicesListCache.put(UiUtils.ENVIRONMENTAL_CONTROL, temPlist);
		fragmentManager = this.getFragmentManager();

		if (null != temPlist && temPlist.size() > 0) {
			for (SimpleDevicesModel sd : temPlist) {
				if (sd.getmModelId().indexOf(
						DataHelper.Indoor_temperature_sensor) == 0) {
					temptureManager.temperatureSensorOperation(sd, 0);
					temptureManager.temperatureSensorOperation(sd, 1);
				} else if (sd.getmModelId().indexOf(DataHelper.Light_Sensor) == 0) {
					temptureManager.lightSensorOperation(sd, 0);
				}
			}
		}
	}

	private void initDevicesListFragment() {
		// TODO Auto-generated method stub
		mDevicesBaseAdapter = new DevicesBaseAdapter(
				ShowDevicesGroupFragmentActivity.this, this);
		mDevicesBaseAdapter.setList(mCurrentList);
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		mDevicesListFragment = new DevicesListFragment();
		fragmentTransaction.replace(R.id.devices_control_fragment,
				mDevicesListFragment, "LightsControlFragment");
		mDevicesListFragment.setAdapter(mDevicesBaseAdapter);
		fragmentTransaction.commit();
	}

	private void back() {
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
		fancyCoverFlow.setSpacing(50);
		fancyCoverFlow.setSelection(mListIndex);
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
			devicesIeee = "";
			refreshAdapter(mListIndex);
		}
		if (null == mCurrentList || mCurrentList.size() == 0) {
			mNoDevices.setVisibility(View.VISIBLE);
		} else {
			mNoDevices.setVisibility(View.GONE);
		}
		initTitleByTag(mListIndex);
	}

	public void refreshAdapter(int postion) {
		mCurrentList = null;
		postion = mListIndex;
		int type = types[postion];
		if (null != mDevicesListCache.get(type)) {
			mCurrentList = mDevicesListCache.get(type);
		} else {
			if (type == UiUtils.LIGHTS_MANAGER) {
				mCurrentList = DataUtil.getLightingManagementDevices(
						ShowDevicesGroupFragmentActivity.this, mDataHelper);
			} else {
				mCurrentList = DataUtil.getOtherManagementDevices(
						ShowDevicesGroupFragmentActivity.this, mDataHelper,
						type);
			}
			mDevicesListCache.put(type, mCurrentList);
		}
		if (4 == postion) {
			if (null != mCurrentList && mCurrentList.size() > 0) {
				for (SimpleDevicesModel sd : mCurrentList) {
					if (sd.getmModelId().indexOf(
							DataHelper.Indoor_temperature_sensor) == 0) {
						temptureManager.temperatureSensorOperation(sd, 0);
						temptureManager.temperatureSensorOperation(sd, 1);
					} else if (sd.getmModelId()
							.indexOf(DataHelper.Light_Sensor) == 0) {
						temptureManager.lightSensorOperation(sd, 0);
					}
				}
			}
		}
		setdata(mCurrentList);
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
			return mCurrentList;
		}

		@Override
		protected void onPostExecute(List<SimpleDevicesModel> result) {
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
	public void setDevicesId(String id) {
		// TODO Auto-generated method stub
		devicesIeee = id;
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
		initTitleByDevices(devicesIeee);
	}

	public void initTitleByTag(int postion) {
		title.setText(tags[postion]);
		parents_need.setVisibility(View.VISIBLE);
		devices_need.setVisibility(View.GONE);
	}

	public void initTitleByDevices(String devicesId) {
		SimpleDevicesModel ms = getCurrentDeviceByIeee(devicesIeee);
		if (null != ms) {
			title.setText(ms.getmUserDefineName().replace(" ", ""));
			parents_need.setVisibility(View.GONE);
			devices_need.setVisibility(View.VISIBLE);
		}

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
		LightManager.getInstance().deleteNode(devicesIeee);
		mDataHelper.delete(mDataHelper.getSQLiteDatabase(),
				DataHelper.DEVICES_TABLE, " ieee=? ",
				new String[] { devicesIeee });
		mDevicesListCache.remove(types[mListIndex]);
		fragmentManager.popBackStack();
		refreshAdapter(mListIndex);
		initTitleByTag(mListIndex);
	}

	@Override
	public void setLayout() {
		// TODO Auto-generated method stub
		mDevicesListFragment.setLayout();
	}

	// @Override
	// public void saveedit(String ieee, String ep, String name, String region)
	// {
	// // TODO Auto-generated method stub
	// String where = " ieee = ? ";
	// String[] args = { ieee };
	//
	// ContentValues c = new ContentValues();
	// c.put(DevicesModel.USER_DEFINE_NAME, name);
	// c.put(DevicesModel.DEVICE_REGION, region);
	//
	// SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
	// int result = mDataHelper.update(mSQLiteDatabase,
	// DataHelper.DEVICES_TABLE, c, where, args);
	// if (result >= 0) {
	//
	// mDevicesListCache.clear();
	// refreshAdapter(mListIndex);
	// }
	//
	// }

	@Override
	public void deleteDevices(String id) {
		// TODO Auto-generated method stub
		Log.i(TAG, "tagzgs->delete id=" + id);
		LightManager.getInstance().deleteNode(devicesIeee);
		mDataHelper.delete(mDataHelper.getSQLiteDatabase(),
				DataHelper.DEVICES_TABLE, " ieee=? ", new String[] { id });
		mDevicesListCache.clear();
		refreshAdapter(mListIndex);
	}

	@Override
	public SimpleDevicesModel getDeviceModle(int postion) {
		// TODO Auto-generated method stub
		if (null != mCurrentList) {
			return mCurrentList.get(postion);
		}
		return null;
	}

	@Override
	public boolean updateDevices(String Ieee, String ep, ContentValues c) {
		// TODO Auto-generated method stub
		String where = " ieee = ? and ep = ?";
		String[] args = { Ieee, ep };
		SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
		int result = mDataHelper.update(mSQLiteDatabase,
				DataHelper.DEVICES_TABLE, c, where, args);
		// mDataHelper
		if (result >= 0) {

			mDevicesListCache.clear();
			refreshAdapter(mListIndex);

			return true;
		}
		return false;
	}

	@Override
	public void saveedit(String ieee, String ep, String name) {
		// TODO Auto-generated method stub
		String where = " ieee = ? ";
		String[] args = { ieee };

		ContentValues c = new ContentValues();
		c.put(DevicesModel.USER_DEFINE_NAME, name);
		// c.put(DevicesModel.DEVICE_REGION, region);

		SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
		int result = mDataHelper.update(mSQLiteDatabase,
				DataHelper.DEVICES_TABLE, c, where, args);
		if (result >= 0) {

			mDevicesListCache.clear();
			refreshAdapter(mListIndex);
		}
	}

	@Override
	public void setdata(List<SimpleDevicesModel> list) {
		// TODO Auto-generated method stub
		mDevicesBaseAdapter.setList(list);
		mDevicesBaseAdapter.notifyDataSetChanged();
		mDevicesListFragment.setLayout();
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		if (EventType.LIGHTSENSOROPERATION == event.getType()) {
			// data maybe null
			if (event.isSuccess()) {

				SimpleResponseData data = (SimpleResponseData) event.getData();
				int m = getDevicesPostion(data.getIeee(), data.getEp());
				if (m != -1) {
					List<SimpleDevicesModel> temList = mDevicesListCache
							.get(UiUtils.ENVIRONMENTAL_CONTROL);
					temList.get(m).setmValue(data.getParam1());
					if(UiUtils.ENVIRONMENTAL_CONTROL==mListIndex){
						mCurrentList=temList;
						title.post(new Runnable() {
							@Override
							public void run() {
								setdata(mCurrentList);
							}
						});
					}
				}
				// Toast.makeText(getActivity(),
				// "当前光线亮度"+data.getParam1(),3000).show();
			} else {
				// Toast.makeText(getActivity(), "获取亮度失败",3000).show();
			}
		}else if (EventType.TEMPERATURESENSOROPERATION==event.getType()) {
			if (event.isSuccess()) {
				SimpleResponseData data = (SimpleResponseData) event.getData();
				int m = getDevicesPostion(data.getIeee(), data.getEp());
				if (m != -1) {
					List<SimpleDevicesModel> temList = mDevicesListCache
							.get(UiUtils.ENVIRONMENTAL_CONTROL);
					
					temList.get(m).setmValue(String.valueOf(Float.valueOf(data.getParam1())/1000));
					if (4 == mListIndex) {
						mCurrentList = temList;
						title.post(new Runnable() {
							@Override
							public void run() {
								setdata(mCurrentList);
							}
						});
					}
				}
				// Toast.makeText(getActivity(),
				// "当前光线亮度"+data.getParam1(),3000).show();
			} 
		}else if (EventType.HUMIDITY==event.getType()) {
			if (event.isSuccess()) {
				SimpleResponseData data = (SimpleResponseData) event.getData();
				int m = getDevicesPostion(data.getIeee(), data.getEp());
				if (m != -1) {
					List<SimpleDevicesModel> temList = mDevicesListCache
							.get(UiUtils.ENVIRONMENTAL_CONTROL);
					
					temList.get(m).setHumidityValue(String.valueOf(Float.valueOf(data.getParam1())/1000));
					if (4 == mListIndex) {
						mCurrentList = temList;
						title.post(new Runnable() {
							@Override
							public void run() {
								setdata(mCurrentList);
							}
						});
					}
				}
				// Toast.makeText(getActivity(),
				// "当前光线亮度"+data.getParam1(),3000).show();
			} 
		}
		
	}

	private int getDevicesPostion(String ieee, String ep) {
		if (null == ieee || null == ep) {
			return -1;
		}
		if (ieee.trim().equals("") || ep.trim().equals("")) {
			return -1;
		}
		List<SimpleDevicesModel> temList = mDevicesListCache
				.get(UiUtils.ENVIRONMENTAL_CONTROL);
		if (null != temList && temList.size() > 0) {
			for (int m = 0; m < temList.size(); m++) {
				if (ieee.trim().equals(temList.get(m).getmIeee())
						&& ep.trim().equals(temList.get(m).getmEP())) {
					return m;
				}
			}
		}
		return -1;

	}

	// @Override
	// public void update(Manger observer, Object object) {
	// // TODO Auto-generated method stub
	//
	// }
}
