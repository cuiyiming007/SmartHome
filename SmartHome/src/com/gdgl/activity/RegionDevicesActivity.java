package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.activity.DevicesListFragment.refreshData;
import com.gdgl.activity.DevicesListFragment.setData;
import com.gdgl.adapter.AllDevicesAdapter;
import com.gdgl.adapter.AllDevicesAdapter.AddChecked;
import com.gdgl.adapter.DevicesBaseAdapter;
import com.gdgl.adapter.DevicesBaseAdapter.DevicesObserver;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.EditDevicesDlg.EditDialogcallback;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/***
 * 区域设备列表
 * 
 * @author Trice
 * 
 */
public class RegionDevicesActivity extends FragmentActivity implements DevicesObserver,
		AddChecked, refreshData, EditDialogcallback,
		Dialogcallback, setData, UIListener {
	public static final String REGION_NAME = "region_name";
	public static final String REGION_ID = "region_id";

	public static final int INLIST = 1;
	public static final int INCONTROL = 2;
	public static final int INADD = 3;

	private String mRoomname = "";
	private String mRoomid = "";

	// String where = " device_region=? ";

	List<DevicesModel> mList;

	List<DevicesModel> mAddList;

	List<DevicesModel> mAddToRegionList;

	DataHelper mDh;

	FragmentManager fragmentManager;

	DevicesListFragment mDevicesListFragment;

	AllDevicesFragment mAllDevicesFragment;

	DevicesBaseAdapter mDevicesBaseAdapter;

	TextView mNoDevices, region_name;
	Button mAdd, delete;

	DataHelper mDataHelper;
	private DevicesModel getModel;
	private boolean deleteType = false;
	private boolean isAdd = false;
	
	CGIManager mcgiManager;
	DeviceManager mDeviceManager;

	private int currentState = INLIST;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_region);

		Intent i = getIntent();
		if (null != i) {
			Bundle extras = i.getExtras();
			if (null != extras) {
				mRoomname = extras.getString(REGION_NAME, "");
				mRoomid = Integer.toString(extras.getInt(REGION_ID));
			}
		}
		mDataHelper = new DataHelper(RegionDevicesActivity.this);
		initData();
		initView();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mcgiManager.deleteObserver(this);
		mDeviceManager.deleteObserver(this);
		CallbackManager.getInstance().deleteObserver(this);
	}

	private void initRegionDevicesList() {
		mList = null;
		String[] args = { mRoomid };
		String where = " rid=? ";
		if (!mRoomid.trim().equals("")) {
			mDh = new DataHelper(RegionDevicesActivity.this);
			mList = mDh.queryForDevicesList(
					mDh.getSQLiteDatabase(), DataHelper.DEVICES_TABLE,
					null, where, args, null, null, null, null);
		}
	}

	private void initAddFragmentDevicesList() {
		String where = "rid=? and device_sort!=?";
		String[] args = { "-1", "6" };
		mAddList = mDh.queryForDevicesList(
				mDh.getSQLiteDatabase(), DataHelper.DEVICES_TABLE,
				null, where, args, null, null, DevicesModel.DEVICE_PRIORITY, null);
//		List<DevicesModel> mTempList = DataUtil.getDevices(
//				RegionDevicesActivity.this, mDh, null, null);
//		mAddList = new ArrayList<DevicesModel>();
//		for (DevicesModel devicesModel : mTempList) {
//			if (devicesModel.getmRid().equals("-1")) {
//				mAddList.add(devicesModel);
//			}
//		}
	}

	// private boolean isInList(SimpleDevicesModel simpleDevicesModel) {
	//
	// for (SimpleDevicesModel msimpleDevicesModel : mList) {
	// if (msimpleDevicesModel.getmIeee().equals(
	// simpleDevicesModel.getmIeee())) {
	// return true;
	// }
	// }
	// return false;
	// }

	private void initAddToRegionDevicesList() {
		mAddToRegionList = new ArrayList<DevicesModel>();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mNoDevices = (TextView) findViewById(R.id.no_devices);
		mAdd = (Button) findViewById(R.id.add_devices);
		delete = (Button) findViewById(R.id.clear_message);
		region_name = (TextView) findViewById(R.id.title);

		region_name.setText(mRoomname);

		if (null != mList && mList.size() > 0) {
			mNoDevices.setVisibility(View.GONE);
			initDevicesListFragment();
		}

		mAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (currentState == INLIST) {
					isAdd = true;
					mAdd.setText("添加");
					mAdd.setTextColor(Color.RED);
					// initRegionDevicesList();
					initAddFragmentDevicesList();
					mAllDevicesFragment = new AllDevicesFragment();
					AllDevicesAdapter mAllDevicesAdapter = new AllDevicesAdapter(
							RegionDevicesActivity.this, mAddList,
							RegionDevicesActivity.this);
					mAllDevicesFragment.setAdapter(mAllDevicesAdapter);
					setFragment(mAllDevicesFragment, -1);
					currentState = INADD;
				} else if (currentState == INADD) {
					isAdd = false;
					ContentValues c = new ContentValues();
					c.put(DevicesModel.DEVICE_REGION, mRoomname);
					c.put(DevicesModel.R_ID, mRoomid);
					for (DevicesModel s : mAddToRegionList) {
						CGIManager.getInstance().ModifyDeviceRoomId(s, mRoomid);
						s.setmDeviceRegion(mRoomname);
						s.setmRid(mRoomid);
						updateDevices(s, c);
						mList.add(s);
					}
					mAddToRegionList.clear();
					mDevicesBaseAdapter.setList(mList);
					mDevicesBaseAdapter.notifyDataSetChanged();
					fragmentManager.popBackStack();
					initDevicesListFragment();
					currentState = INLIST;
				} else if (currentState == INCONTROL) {
					fragmentManager.popBackStack();
					initDevicesListFragment();
					currentState = INLIST;
				}
			}
		});

		LinearLayout mBack = (LinearLayout) findViewById(R.id.goback);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
						RegionDevicesActivity.this);
				mMyOkCancleDlg.setDialogCallback(RegionDevicesActivity.this);
				mMyOkCancleDlg.setContent("确定要删除区域  " + mRoomname + " 吗?");
				mMyOkCancleDlg.show();
				deleteType = true;
			}
		});

	}

	private void initData() {
		// TODO Auto-generated method stub

		fragmentManager = getSupportFragmentManager();
		mcgiManager = CGIManager.getInstance();
		mcgiManager.addObserver(this);

		mDeviceManager = DeviceManager.getInstance();
		mDeviceManager.addObserver(this);
		CallbackManager.getInstance().addObserver(this);
		initRegionDevicesList();
		initAddToRegionDevicesList();
		mDevicesBaseAdapter = new DevicesBaseAdapter(
				RegionDevicesActivity.this, this);
		mDevicesBaseAdapter.setList(mList);
	}

	private void initDevicesListFragment() {
		// TODO Auto-generated method stub
		mAdd.setText("添加设备");
		mAdd.setTextColor(Color.BLACK);
		if (null == mList || mList.size() == 0) {
			mNoDevices.setVisibility(View.VISIBLE);
		} else {
			mNoDevices.setVisibility(View.GONE);
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			mDevicesListFragment = new DevicesListFragment();
			Bundle extras = new Bundle();
			extras.putInt(Constants.OPERATOR, DevicesListFragment.WITH_OPERATE);
			extras.putString(REGION_ID, mRoomid);
			mDevicesListFragment.setArguments(extras);
			fragmentTransaction.replace(R.id.devices_control_fragment,
					mDevicesListFragment, "LightsControlFragment");
			mDevicesListFragment.setAdapter(mDevicesBaseAdapter);
			fragmentTransaction.commit();
		}
	}

	@Override
	public void setLayout() {
		// TODO Auto-generated method stub
		mDevicesListFragment.setLayout();
	}

	@Override
	public void deleteDevices(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void AddCheckedDevices(int postion) {
		// TODO Auto-generated method stub
		DevicesModel s = mAddList.get(postion);
		if (!mAddToRegionList.contains(s)) {
			mAddToRegionList.add(s);
		}
	}

	@Override
	public void DeletedCheckedDevices(int postion) {
		// TODO Auto-generated method stub
		DevicesModel s = mAddList.get(postion);
		if (mAddToRegionList.contains(s)) {
			mAddToRegionList.remove(s);
		}
	}

	@Override
	public void refreshListData() {
		// TODO Auto-generated method stub
		new GetDataTask().execute();
	}

	@Override
	public DevicesModel getDeviceModle(int postion) {
		// TODO Auto-generated method stub
		if (null != mList) {
			return mList.get(postion);
		}
		return null;
	}

	@Override
	public void setFragment(Fragment mFragment, int postion) {
		// TODO Auto-generated method stub
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.devices_control_fragment, mFragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

		if (postion != -1) {
			mAdd.setText("返回");
			mAdd.setTextColor(Color.BLACK);
			currentState = INCONTROL;
		}
	}

	@Override
	public void setDevicesId(DevicesModel simpleDevicesModel) {
		// TODO Auto-generated method stub
		getModel = simpleDevicesModel;
	}

	private class GetDataTask extends AsyncTask<Void, Void, List<DevicesModel>> {

		@Override
		protected List<DevicesModel> doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return mList;
		}

		@Override
		protected void onPostExecute(List<DevicesModel> result) {
			super.onPostExecute(result);
			if (currentState == INADD) {
				mAllDevicesFragment.stopRefresh();
			} else if (currentState == INLIST) {
				mDevicesListFragment.stopRefresh();
			}

		}
	}

	public boolean updateDevices(DevicesModel sd, ContentValues c) {
		// TODO Auto-generated method stub
		String ep = sd.getmEP().trim();
		int result = 0;
		String[] eps = { ep };
		if (ep.contains(",")) {
			eps = ep.trim().split(",");
		}
		for (String string : eps) {
			String wheres = " ieee = ? and ep = ?";
			String[] args = { sd.getmIeee(), string };
			SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
			int temp = mDataHelper.update(mSQLiteDatabase,
					DataHelper.DEVICES_TABLE, c, wheres, args);
			result = temp > result ? temp : result;
		}
		if (result >= 0) {
			return true;
		}
		return false;
	}

	@Override
	public void saveedit(DevicesModel mDevicesModel, String name) {
		// TODO Auto-generated method stub
		String where = " ieee = ? ";
		String[] args = { mDevicesModel.getmIeee() };

		ContentValues c = new ContentValues();
		c.put(DevicesModel.DEFAULT_DEVICE_NAME, name);
		// c.put(DevicesModel.DEVICE_REGION, region);

		SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
		int result = mDataHelper.update(mSQLiteDatabase,
				DataHelper.DEVICES_TABLE, c, where, args);
		if (result >= 0) {
			initRegionDevicesList();
			mDevicesBaseAdapter.setList(mList);
			mDevicesBaseAdapter.notifyDataSetChanged();
			if (null == mList || mList.size() == 0) {
				mNoDevices.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		if (deleteType) {
			// //删除设备中的区域信息
			// String where = " ieee = ? ";
			// SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
			// for (DevicesModel s : mList) {
			// ContentValues c = new ContentValues();
			// c.put(DevicesModel.DEVICE_REGION, "");
			// c.put(DevicesModel.R_ID, "-1");
			// String[] args = { s.getmIeee() };
			// mDataHelper.update(mSQLiteDatabase, DataHelper.DEVICES_TABLE,
			// c, where, args);
			// }
			// //删除所选区域
			// CGIManager.getInstance().ZBDeleteRoomDataMainByID(mRoomid);
			// String[] strings=new String[] {mRoomid};
			// mDataHelper.delete(mSQLiteDatabase, DataHelper.ROOMINFO_TABLE,
			// " room_id = ? ", strings);
			// //删除常用中对应的区域名称
			// getFromSharedPreferences.setsharedPreferences(RegionDevicesActivity.this);
			//
			// List<String> mreg=new ArrayList<String>();
			// String comm = getFromSharedPreferences.getCommonUsed();
			// if (null != comm && !comm.trim().equals("")) {
			// String[] result = comm.split("@@");
			// for (String string : result) {
			// if (!string.trim().equals("")) {
			// mreg.add(string);
			// }
			// }
			// }
			// if(mreg.contains(UiUtils.REGION_FLAG+mRoomname)){
			// mreg.remove(UiUtils.REGION_FLAG+mRoomname);
			// StringBuilder sb=new StringBuilder();
			// if(null!=mreg && mreg.size()>0){
			// for (String s : mreg) {
			// sb.append(s+"@@");
			// }
			// }else{
			// sb.append("");
			// }
			// getFromSharedPreferences.setCommonUsed(sb.toString());
			// }
			// this.finish();
		} else {
			String where = " ieee = ? ";
			String[] args = { getModel.getmIeee() };

			ContentValues c = new ContentValues();
			c.put(DevicesModel.DEVICE_REGION, "");
			c.put(DevicesModel.R_ID, "-1");
			SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
			CGIManager.getInstance().ModifyDeviceRoomId(getModel, "-1");
			int result = mDataHelper.update(mSQLiteDatabase,
					DataHelper.DEVICES_TABLE, c, where, args);
			if (result >= 0) {
				initRegionDevicesList();
				mDevicesBaseAdapter.setList(mList);
				mDevicesBaseAdapter.notifyDataSetChanged();
				if (null == mList || mList.size() == 0) {
					mNoDevices.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (isAdd) {
			isAdd = false;
			fragmentManager.popBackStack();
			initDevicesListFragment();
		} else {
			if (fragmentManager.getBackStackEntryCount() > 0) {
				fragmentManager.popBackStack();
			} else {
				finish();
			}
		}
	}

	@Override
	public void setdata(List<DevicesModel> list) {
		// TODO Auto-generated method stub

	}
	
	private int getDevicesPostion(String ieee, String ep, List<DevicesModel> list){
		int position = -1;
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getmIeee().equals(ieee) && list.get(i).getmEP().equals(ep)){
				return i;
			}
		}
		return position;
	}
	
	private int getSecurityPosition(List<DevicesModel> list){
		int position = -1;
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getmModelId().indexOf(DataHelper.One_key_operator) == 0){
				return i;
			}
		}
		return position;
	}
	
	
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		if(isAdd){
			return;
		}
		final Event event = (Event) object;
		if (EventType.LIGHTSENSOROPERATION == event.getType()) {
			// data maybe null
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				int m = getDevicesPostion(bundle.getString("IEEE"),
						bundle.getString("EP"), mList);
				if (m != -1) {
					String light = bundle.getString("PARAM");
					mList.get(m).setmBrightness(Integer.parseInt(light));
					region_name.post(new Runnable() {
						@Override
						public void run() {
							// setDataActivity.setdata(mDeviceList);
							mDevicesBaseAdapter.notifyDataSetChanged();
						}
					});
				}
			} 
		} else if (EventType.TEMPERATURESENSOROPERATION == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				int m = getDevicesPostion(bundle.getString("IEEE"),
						bundle.getString("EP"), mList);
				if (m != -1) {
					String temperature = bundle.getString("PARAM");
					mList.get(m).setmTemperature(
							Float.parseFloat(temperature));
					region_name.post(new Runnable() {
						@Override
						public void run() {
							// setDataActivity.setdata(mDeviceList);
							mDevicesBaseAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		} else if (EventType.HUMIDITY == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				int m = getDevicesPostion(bundle.getString("IEEE"),
						bundle.getString("EP"), mList);
				if (m != -1) {
					String humidity = bundle.getString("PARAM");
					mList.get(m).setmHumidity(Float.parseFloat(humidity));
					region_name.post(new Runnable() {
						@Override
						public void run() {
							// setDataActivity.setdata(mDeviceList);
							mDevicesBaseAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		} else if (EventType.LOCALIASCIEBYPASSZONE == event.getType()) {
				if (event.isSuccess()) {
					Bundle bundle = (Bundle) event.getData();
					int m = getDevicesPostion(bundle.getString("IEEE"),
							bundle.getString("EP"), mList);
					if (m != -1) {
						mList.get(m).setmOnOffStatus(bundle.getString("PARAM"));
						region_name.post(new Runnable() {
							@Override
							public void run() {
								// setDataActivity.setdata(mDeviceList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
					}
				}
		} else if (EventType.LOCALIASCIEOPERATION == event.getType()) {
			if (event.isSuccess() == true) {

				int m = getSecurityPosition(mList);
				Log.i("LOCALIASCIEOPERATION", "m = "+m);
				if (m != -1) {
					String status = (String) event.getData();
					mList.get(m).setmOnOffStatus(status);
					region_name.post(new Runnable() {
						@Override
						public void run() {
							// setDataActivity.setdata(mDeviceList);
							mDevicesBaseAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		} else if (EventType.ON_OFF_STATUS == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mList);
				Log.i("ON_OFF_STATUS", "m = "+m);
				if (-1 != m) {
					if (null != data.getValue()) {
						mList.get(m).setmOnOffStatus(data.getValue());
						region_name.post(new Runnable() {
							@Override
							public void run() {
								// setDataActivity.setdata(mDeviceList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			} 
		} else if (EventType.MOVE_TO_LEVEL == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				final CallbackResponseType2 detaildata = data;
				// Log.i("MOVE_TO_LEVEL", mCurrentList.toString());
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mList);
				final String valueString = data.getValue();
				if (-1 != m) {
					if (null != data.getValue()) {
						mList.get(m).setmLevel(valueString);
						if(Integer.parseInt(valueString) < 7){
							mList.get(m).setmOnOffStatus("0");
						}else{
							mList.get(m).setmOnOffStatus("1");
						}
						region_name.post(new Runnable() {
							@Override
							public void run() {
								// setDataActivity.setdata(mDeviceList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			} else {
				// if failed,prompt a Toast
				// mError.setVisibility(View.VISIBLE);
			}
		} else if (EventType.CURRENT == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mList);
				final String valueString = data.getValue();
				if (-1 != m) {
					if (null != data.getValue()) {
						mList.get(m).setmCurrent(valueString);
						region_name.post(new Runnable() {
							@Override
							public void run() {
								// setDataActivity.setdata(mDeviceList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
						//DeviceDtailFragment.getInstance().refreshCurrent(valueString);
					}
				}
			} else {
				// if failed,prompt a Toast
				// mError.setVisibility(View.VISIBLE);
			}
		} else if (EventType.VOLTAGE == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mList);
				final String valueString = data.getValue();
				if (-1 != m) {
					if (null != data.getValue()) {
						mList.get(m).setmVoltage(data.getValue());
						region_name.post(new Runnable() {
							@Override
							public void run() {
								// setDataActivity.setdata(mDeviceList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
						//DeviceDtailFragment.getInstance().refreshVoltage(valueString);
					}
				}
			} else {
				// if failed,prompt a Toast
				// mError.setVisibility(View.VISIBLE);
			}
		} else if (EventType.ENERGY == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mList);
				final String valueString = String.valueOf(Float.parseFloat(data
						.getValue()));
				if (-1 != m) {
					if (null != data.getValue()) {
						mList.get(m).setmEnergy(data.getValue());
						region_name.post(new Runnable() {
							@Override
							public void run() {
								// setDataActivity.setdata(mDeviceList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
						//DeviceDtailFragment.getInstance().refreshEnergy(valueString);
					}
				}
			} else {
				// if failed,prompt a Toast
				// mError.setVisibility(View.VISIBLE);
			}
		} else if (EventType.POWER == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mList);
				final String valueString = data.getValue();
				if (-1 != m) {
					if (null != data.getValue()) {
						mList.get(m).setmPower(data.getValue());
						region_name.post(new Runnable() {
							@Override
							public void run() {
								// setDataActivity.setdata(mDeviceList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
						//DeviceDtailFragment.getInstance().refreshPower(valueString);
					}
				}
			} else {
				// if failed,prompt a Toast
				// mError.setVisibility(View.VISIBLE);
			}
		} else if(EventType.GETEPBYROOMINDEX == event.getType()){
			if (event.isSuccess() == true) {
				mList = (List<DevicesModel>) event
						.getData();
				mDevicesBaseAdapter.setList(mList);
				Log.i("device first", mList.get(0).toString());
				for(DevicesModel mdevice : mList){
					mdevice.setmDeviceRegion(mRoomname);
					Log.i("IEEE", mdevice.getmIeee());
				}
				region_name.post(new Runnable() {
					@Override
					public void run() {
						mDevicesBaseAdapter.notifyDataSetChanged();
					}
				});
				
			}
		}
	}

}
