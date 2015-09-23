package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.activity.DevicesListFragment.ChangeFragment;
import com.gdgl.activity.DevicesListFragment.setData;
import com.gdgl.activity.RegionDevicesAddFragment.AddChecked;
import com.gdgl.activity.RegionDevicesAddFragment.RegionDevicesAddListAdapter;
import com.gdgl.adapter.DevicesBaseAdapter;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.RfCGIManager;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyApplicationFragment;
import com.gdgl.util.EditDevicesDlg.EditDialogcallback;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/***
 * 区域设备列表
 * 
 * @author Trice
 * 
 */
public class RegionDevicesActivity extends MyActionBarActivity implements
		ChangeFragment, AddChecked, EditDialogcallback, Dialogcallback,
		setData, UIListener {
	public static final String REGION_NAME = "region_name";
	public static final String REGION_ID = "region_id";

	public static final int INLIST = 1;
	public static final int INCONTROL = 2;
	public static final int INADD = 3;

	private String mRoomname = "";
	private String mRoomid = "";

	DataHelper mDataHelper;

	private Toolbar mToolbar;
	private ActionBar mActionBar;

	List<DevicesModel> mList;

	List<DevicesModel> mAddList;

	List<DevicesModel> mAddToRegionList;

	DevicesListFragment mDevicesListFragment;

	AllDevicesFragment mAllDevicesFragment;

	DevicesBaseAdapter mDevicesBaseAdapter;

	RegionDevicesAddListAdapter mRegionDevicesAddListAdapter;

	TextView mNoDevices, region_name;
	Button mAdd, delete;

	private DevicesModel getModel;
	private boolean deleteType = false;
	private boolean isAdd = false;

	CGIManager mcgiManager;

	private int currentState = INLIST;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplicationFragment.getInstance().setActivity(this);

		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (null != extras) {
			mRoomname = extras.getString(REGION_NAME, "");
			mRoomid = Integer.toString(extras.getInt(REGION_ID));
		}

		mDataHelper = new DataHelper(RegionDevicesActivity.this);

		mcgiManager = CGIManager.getInstance();
		mcgiManager.addObserver(this);
		CallbackManager.getInstance().addObserver(this);
		initAddToRegionDevicesList();
		initRegionDevicesList();

		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBar.setTitle(mRoomname);

		mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
				case R.id.menu_ok:
					if (mAddToRegionList.size() > 0) {
						for (DevicesModel s : mAddToRegionList) {
							if(s.getmDeviceId()>0) {
								CGIManager.getInstance().ModifyDeviceRoomId(s,
										mRoomid);
							} else {
								RfCGIManager.getInstance().ModifyRFDevRoomId(s, mRoomid);
							}
							s.setmDeviceRegion(mRoomname);
							s.setmRid(mRoomid);
						}
						mList.addAll(mAddToRegionList);
						mAddToRegionList.clear();
						mDevicesBaseAdapter.notifyDataSetChanged();
					}
					MyApplicationFragment.getInstance().removeLastFragment();
					break;

				default:
					break;
				}
				return false;
			}
		});

		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		mDevicesListFragment = new DevicesListFragment();
		extras.putInt(Constants.OPERATOR, DevicesListFragment.WITH_OPERATE);
		mDevicesListFragment.setArguments(extras);
		mDevicesBaseAdapter = new DevicesBaseAdapter(RegionDevicesActivity.this);
		mDevicesBaseAdapter.setList(mList);
		mDevicesListFragment.setAdapter(mDevicesBaseAdapter);
		fragmentTransaction.replace(R.id.container, mDevicesListFragment);
		fragmentTransaction.commit();
		MyApplicationFragment.getInstance().addNewTask(mDevicesListFragment);
		// initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_ok, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mcgiManager.deleteObserver(this);
		CallbackManager.getInstance().deleteObserver(this);
	}

	private void initRegionDevicesList() {
		mList = null;
		String[] args = { mRoomid };
		String where = " rid=? ";
		if (!mRoomid.trim().equals("")) {
			SQLiteDatabase db = mDataHelper.getSQLiteDatabase();
			mList = mDataHelper.queryForDevicesList(db,
					DataHelper.DEVICES_TABLE, null, where, args, null, null,
					DevicesModel.DEVICE_PRIORITY, null);
			mList.addAll(mDataHelper.queryForDevicesList(db,
					DataHelper.RF_DEVICES_TABLE, null, where, args, null, null,
					DevicesModel.DEVICE_PRIORITY, null));
			db.close();
		}
	}

	private void initAddFragmentDevicesList() {
		SQLiteDatabase db = mDataHelper.getSQLiteDatabase();
		// String where = "rid=? and device_sort!=?";
		String where = "rid=?";
		String[] args = { "-1" };
		mAddList = mDataHelper.queryForDevicesList(db,
				DataHelper.DEVICES_TABLE, null, where, args, null, null,
				DevicesModel.DEVICE_PRIORITY, null);
		mAddList.addAll(mDataHelper.queryForDevicesList(db,
				DataHelper.RF_DEVICES_TABLE, null, where, args, null, null,
				DevicesModel.DEVICE_PRIORITY, null));
	}

	private void initAddToRegionDevicesList() {
		mAddToRegionList = new ArrayList<DevicesModel>();
	}

	// private void initView() {
	// // TODO Auto-generated method stub
	// mNoDevices = (TextView) findViewById(R.id.no_devices);
	// mAdd = (Button) findViewById(R.id.add_devices);
	// delete = (Button) findViewById(R.id.clear_message);
	// region_name = (TextView) findViewById(R.id.title);
	//
	// region_name.setText(mRoomname);
	//
	// if (null != mList && mList.size() > 0) {
	// mNoDevices.setVisibility(View.GONE);
	// initDevicesListFragment();
	// }
	//
	// mAdd.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// if (currentState == INLIST) {
	// isAdd = true;
	// mAdd.setText("添加");
	// mAdd.setTextColor(Color.RED);
	// // initRegionDevicesList();
	// initAddFragmentDevicesList();
	// mAllDevicesFragment = new AllDevicesFragment();
	// AllDevicesAdapter mAllDevicesAdapter = new AllDevicesAdapter(
	// RegionDevicesActivity.this, mAddList,
	// RegionDevicesActivity.this);
	// mAllDevicesFragment.setAdapter(mAllDevicesAdapter);
	// setFragment(mAllDevicesFragment);
	// currentState = INADD;
	// } else if (currentState == INADD) {
	// isAdd = false;
	// ContentValues c = new ContentValues();
	// c.put(DevicesModel.DEVICE_REGION, mRoomname);
	// c.put(DevicesModel.R_ID, mRoomid);
	// for (DevicesModel s : mAddToRegionList) {
	// CGIManager.getInstance().ModifyDeviceRoomId(s, mRoomid);
	// s.setmDeviceRegion(mRoomname);
	// s.setmRid(mRoomid);
	// updateDevices(s, c);
	// mList.add(s);
	// }
	// mAddToRegionList.clear();
	// mDevicesBaseAdapter.setList(mList);
	// mDevicesBaseAdapter.notifyDataSetChanged();
	// initDevicesListFragment();
	// currentState = INLIST;
	// } else if (currentState == INCONTROL) {
	// initDevicesListFragment();
	// currentState = INLIST;
	// }
	// }
	// });
	//
	// LinearLayout mBack = (LinearLayout) findViewById(R.id.goback);
	// mBack.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// finish();
	// }
	// });
	//
	// delete.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
	// RegionDevicesActivity.this);
	// mMyOkCancleDlg.setDialogCallback(RegionDevicesActivity.this);
	// mMyOkCancleDlg.setContent("确定要删除区域  " + mRoomname + " 吗?");
	// mMyOkCancleDlg.show();
	// deleteType = true;
	// }
	// });
	//
	// }

	private void initDevicesListFragment() {
		// TODO Auto-generated method stub
		mAdd.setText("添加设备");
		mAdd.setTextColor(Color.BLACK);
		if (null == mList || mList.size() == 0) {
			mNoDevices.setVisibility(View.VISIBLE);
		} else {
			mNoDevices.setVisibility(View.GONE);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
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
	public void AddCheckedDevices(DevicesModel model) {
		// TODO Auto-generated method stub
		if (!mAddToRegionList.contains(model)) {
			mAddToRegionList.add(model);
		}
	}

	@Override
	public void DeletedCheckedDevices(DevicesModel model) {
		// TODO Auto-generated method stub
		if (mAddToRegionList.contains(model)) {
			mAddToRegionList.remove(model);
		}
	}

	@Override
	public void setFragment(Fragment mFragment) {
		initAddFragmentDevicesList();
		RegionDevicesAddFragment mSceneAddFragment = (RegionDevicesAddFragment) mFragment;
		mRegionDevicesAddListAdapter = mSceneAddFragment.new RegionDevicesAddListAdapter(
				mAddList, RegionDevicesActivity.this);
		mSceneAddFragment.setAdapter(mRegionDevicesAddListAdapter);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.container, mFragment);
		fragmentTransaction.commit();
		MyApplicationFragment.getInstance().addFragment(mFragment);
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
		if (MyApplicationFragment.getInstance().getFragmentListSize() > 1) {
			MyApplicationFragment.getInstance().removeLastFragment();
			mAddToRegionList.clear();
		} else {
			// MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(this);
			// mMyOkCancleDlg.setDialogCallback((Dialogcallback) this);
			// mMyOkCancleDlg.setContent("确定要放弃本次编辑?");
			// mMyOkCancleDlg.show();
			finish();
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		// TODO Auto-generated method stub
		if (MyApplicationFragment.getInstance().getFragmentListSize() > 1) {
			MyApplicationFragment.getInstance().removeLastFragment();
			mAddToRegionList.clear();
			return super.onSupportNavigateUp();
		}
		// MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(this);
		// mMyOkCancleDlg.setDialogCallback((Dialogcallback) this);
		// mMyOkCancleDlg.setContent("确定要放弃本次编辑?");
		// mMyOkCancleDlg.show();
		finish();
		return super.onSupportNavigateUp();
	}

	@Override
	public void setdata(List<DevicesModel> list) {
		// TODO Auto-generated method stub

	}

	private int getDevicesPostion(String ieee, String ep,
			List<DevicesModel> list) {
		int position = -1;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getmIeee().equals(ieee)
					&& list.get(i).getmEP().equals(ep)) {
				return i;
			}
		}
		return position;
	}

	private int getSecurityPosition(List<DevicesModel> list) {
		int position = -1;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getmModelId().indexOf(DataHelper.One_key_operator) == 0) {
				return i;
			}
		}
		return position;
	}

	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		if (isAdd) {
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
					mToolbar.post(new Runnable() {
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
					mList.get(m).setmTemperature(Float.parseFloat(temperature));
					mToolbar.post(new Runnable() {
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
					mToolbar.post(new Runnable() {
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
					mToolbar.post(new Runnable() {
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
				Log.i("LOCALIASCIEOPERATION", "m = " + m);
				if (m != -1) {
					String status = (String) event.getData();
					mList.get(m).setmOnOffStatus(status);
					mToolbar.post(new Runnable() {
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
				Log.i("ON_OFF_STATUS", "m = " + m);
				if (-1 != m) {
					if (null != data.getValue()) {
						mList.get(m).setmOnOffStatus(data.getValue());
						mToolbar.post(new Runnable() {
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
						if (Integer.parseInt(valueString) < 7) {
							mList.get(m).setmOnOffStatus("0");
						} else {
							mList.get(m).setmOnOffStatus("1");
						}
						mToolbar.post(new Runnable() {
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
						mToolbar.post(new Runnable() {
							@Override
							public void run() {
								// setDataActivity.setdata(mDeviceList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
						// DeviceDtailFragment.getInstance().refreshCurrent(valueString);
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
						mToolbar.post(new Runnable() {
							@Override
							public void run() {
								// setDataActivity.setdata(mDeviceList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
						// DeviceDtailFragment.getInstance().refreshVoltage(valueString);
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
						mToolbar.post(new Runnable() {
							@Override
							public void run() {
								// setDataActivity.setdata(mDeviceList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
						// DeviceDtailFragment.getInstance().refreshEnergy(valueString);
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
						mToolbar.post(new Runnable() {
							@Override
							public void run() {
								// setDataActivity.setdata(mDeviceList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
						// DeviceDtailFragment.getInstance().refreshPower(valueString);
					}
				}
			} else {
				// if failed,prompt a Toast
				// mError.setVisibility(View.VISIBLE);
			}
		}
	}
}
