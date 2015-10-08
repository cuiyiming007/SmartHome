package com.gdgl.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.gdgl.activity.DevicesListFragment.ChangeFragment;
import com.gdgl.activity.RegionDevicesAddFragment.AddChecked;
import com.gdgl.activity.RegionDevicesAddFragment.RegionDevicesAddListAdapter;
import com.gdgl.adapter.DevicesBaseAdapter;
import com.gdgl.libjingle.LibjingleResponseHandlerManager;
import com.gdgl.libjingle.LibjingleSendManager;
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
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyApplicationFragment;
import com.gdgl.util.VersionDlg;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

/***
 * 区域设备列表
 * 
 * @author Trice
 * 
 */
public class RegionDevicesActivity extends MyActionBarActivity implements
		ChangeFragment, AddChecked, UIListener {
	public static final String REGION_NAME = "region_name";
	public static final String REGION_ID = "region_id";

	public static final int CONTROL_FRAGMENT = 1;
	public static final int ADD_FRAGMENT = 2;

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

	CGIManager mcgiManager;

	private int fragment_flag = CONTROL_FRAGMENT;

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
		RfCGIManager.getInstance().addObserver(this);
		LibjingleResponseHandlerManager.getInstance().addObserver(this);
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
						ContentValues c = new ContentValues();
						c.put(DevicesModel.DEVICE_REGION, mRoomname);
						c.put(DevicesModel.R_ID, mRoomid);
						for (DevicesModel s : mAddToRegionList) {
							if (s.getmDeviceId() > 0) {
								CGIManager.getInstance().ModifyDeviceRoomId(s,
										mRoomid);
								updateDevices(s, c, DataHelper.DEVICES_TABLE);
							} else {
								RfCGIManager.getInstance().ModifyRFDevRoomId(s,
										mRoomid);
								updateDevices(s, c, DataHelper.RF_DEVICES_TABLE);
							}
							s.setmDeviceRegion(mRoomname);
							s.setmRid(mRoomid);
						}
						mList.addAll(mAddToRegionList);
						mAddToRegionList.clear();
						mDevicesBaseAdapter.notifyDataSetChanged();
					}
					MyApplicationFragment.getInstance().removeLastFragment();
					fragment_flag = CONTROL_FRAGMENT;
					invalidateOptionsMenu();
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
		fragment_flag = CONTROL_FRAGMENT;
		MyApplicationFragment.getInstance().addNewTask(mDevicesListFragment);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		if(fragment_flag == ADD_FRAGMENT) {
//			getMenuInflater().inflate(R.menu.menu_ok, menu);
//		}
//		return true;
//	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.clear();
		if(fragment_flag == CONTROL_FRAGMENT) {
			getMenuInflater().inflate(R.menu.menu_empty, menu);
		} else {
			getMenuInflater().inflate(R.menu.menu_ok, menu);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mcgiManager.deleteObserver(this);
		RfCGIManager.getInstance().deleteObserver(this);
		LibjingleResponseHandlerManager.getInstance().deleteObserver(this);
		CallbackManager.getInstance().deleteObserver(this);
	}

	private void initRegionDevicesList() {
		mList = new ArrayList<DevicesModel>();
//		String[] args = { mRoomid };
//		String where = " rid=? ";
//		if (!mRoomid.trim().equals("")) {
//			SQLiteDatabase db = mDataHelper.getSQLiteDatabase();
//			mList = mDataHelper.queryForDevicesList(db,
//					DataHelper.DEVICES_TABLE, null, where, args, null, null,
//					DevicesModel.DEVICE_PRIORITY, null);
//			mList.addAll(mDataHelper.queryForDevicesList(db,
//					DataHelper.RF_DEVICES_TABLE, null, where, args, null, null,
//					DevicesModel.DEVICE_PRIORITY, null));
//			db.close();
//		}
//		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
//			mcgiManager.GetEPByRoomIndexInit(mRoomid);
//		} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
//			LibjingleSendManager.getInstance().GetEPByRoomIndexInit(mRoomid);
//		}
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
//		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
//			mcgiManager.GetEPByRoomIndexInit("-1");
//		} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
//			LibjingleSendManager.getInstance().GetEPByRoomIndexInit("-1");
//		}
	}

	private void initAddToRegionDevicesList() {
		mAddToRegionList = new ArrayList<DevicesModel>();
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
		fragmentTransaction.add(R.id.container, mSceneAddFragment);
		fragmentTransaction.commit();
		fragment_flag = ADD_FRAGMENT;
		invalidateOptionsMenu();
		MyApplicationFragment.getInstance().addFragment(mSceneAddFragment);
	}

	public boolean updateDevices(DevicesModel sd, ContentValues c, String table) {
		// TODO Auto-generated method stub
		int result = 0;
		String wheres = " ieee = ? and ep = ?";
		String[] args = { sd.getmIeee(), sd.getmEP() };
		SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
		int temp = mDataHelper.update(mSQLiteDatabase,
				table, c, wheres, args);
		result = temp > result ? temp : result;
		if (result >= 0) {
			return true;
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (MyApplicationFragment.getInstance().getFragmentListSize() > 1) {
			MyApplicationFragment.getInstance().removeLastFragment();
			fragment_flag = CONTROL_FRAGMENT;
			invalidateOptionsMenu();
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
			fragment_flag = CONTROL_FRAGMENT;
			invalidateOptionsMenu();
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
		} else if (EventType.GETEPBYROOMINDEXINIT == event.getType()) {
			if (event.isSuccess() == true) {
				List<DevicesModel> mDeviceList = (List<DevicesModel>) event.getData();
				Collections.sort(mDeviceList, new Comparator<DevicesModel>() {

					@Override
					public int compare(DevicesModel lhs, DevicesModel rhs) {
						// TODO Auto-generated method stub
						return (lhs.getmDevicePriority()-rhs.getmDevicePriority());
					}
				});
				if(mDeviceList.get(0).getmRid().equals("-1")) {
					if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
						RfCGIManager.getInstance().GetRFDevByRoomIdInit("-1");
					} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
						LibjingleSendManager.getInstance().GetRFDevByRoomIdInit("-1");
					}
					RfCGIManager.getInstance().GetRFDevByRoomIdInit("-1");
					mAddList = mDeviceList;
				} else {
					if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
						RfCGIManager.getInstance().GetRFDevByRoomIdInit(mRoomid);
					} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
						LibjingleSendManager.getInstance().GetRFDevByRoomIdInit(mRoomid);
					}
					mList = mDeviceList;
				}

			}
		} else if (EventType.RF_GETEPBYROOMINDEXINIT == event.getType()) {
			if (event.isSuccess() == true) {
				List<DevicesModel> temp = (List<DevicesModel>) event.getData();
				Collections.sort(temp, new Comparator<DevicesModel>() {

					@Override
					public int compare(DevicesModel lhs, DevicesModel rhs) {
						// TODO Auto-generated method stub
						return (lhs.getmDevicePriority()-rhs.getmDevicePriority());
					}
				});
				if(temp.get(0).getmRid().equals("-1")) {
					mAddList.addAll(temp);
				} else {
					mList.addAll(temp);
				}
			}
		}
	}
}
