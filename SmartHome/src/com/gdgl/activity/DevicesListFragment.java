package com.gdgl.activity;

/***
 * 某一类设备中的设备列表
 */
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.gc.materialdesign.views.ButtonFloat;
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
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class DevicesListFragment extends Fragment implements
		UIListener, Dialogcallback {

	private static final String TAG = "DevicesListFragment";

	DataHelper mDataHelper;
	private View mView;
	SwipeRefreshLayout swipeRefreshLayout;
	ListView mListView;
//	PullToRefreshListView mListView;
	ButtonFloat mButtonFloat;

	DevicesModel mDevicesModel;
	DevicesModel onekeyopratorModel;
	int refreshTag = 0;
	
	boolean readDeviceList = false;
	/***
	 * 列表上的ui的adapter
	 * 跟ShowDevicesGroupFragmentActivity的DevicesBaseAdapter对应，父类引用指向子类对象
	 */
	DevicesBaseAdapter mBaseAdapter;
	RelativeLayout list_root;

	public static final int WITH_OPERATE = 0;
	public static final int WITHOUT_OPERATE = 2;

	private int type = 1;
	private String mRoomname = "";
	private String mRoomid = "";
	/***
	 * 用于存储当前列表的Item
	 */
	List<DevicesModel> mDeviceList;
	DevicesModel onekeyControlDevice;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		if (null != extras) {
			type = extras.getInt(Constants.OPERATOR, WITH_OPERATE);
			switch (type) {
			case WITH_OPERATE:
				mRoomname = extras.getString(RegionDevicesActivity.REGION_NAME,
						"");
				mRoomid = Integer.toString(extras.getInt(
						RegionDevicesActivity.REGION_ID, -1));
				break;
			default:
				break;
			}
		}
		mDataHelper = new DataHelper((Context) getActivity());
		CGIManager.getInstance().addObserver(DevicesListFragment.this);
		CallbackManager.getInstance().addObserver(DevicesListFragment.this);
		RfCGIManager.getInstance().addObserver(DevicesListFragment.this);
		LibjingleResponseHandlerManager.getInstance().addObserver(this);
		readDeviceList = true;
		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
			CGIManager.getInstance().GetEPByRoomIndex(mRoomid);
		} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
			LibjingleSendManager.getInstance().GetEPByRoomIndex(mRoomid);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.devices_list_swiperefresh_fragment, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);
		list_root = (RelativeLayout) mView.findViewById(R.id.list_root);
		mListView = (ListView) mView.findViewById(R.id.devices_list);
		initList();
		mListView.setAdapter(mBaseAdapter);

		swipeRefreshLayout.setColorSchemeResources(R.color.blue_default);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new GetDataByRoomTask().execute(mRoomid);
			}
		});
		
		mButtonFloat = (ButtonFloat) mView.findViewById(R.id.buttonFloat);
		mButtonFloat.setVisibility(View.VISIBLE);
		mButtonFloat.attachToListView(mListView);
		mButtonFloat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeFragment changeFragment = (ChangeFragment) getActivity();
				changeFragment.setFragment(new RegionDevicesAddFragment());
			}
		});
		
		registerForContextMenu(mListView);
	}

	public void initList() {
		// TODO Auto-generated method stub

		if (null != mBaseAdapter) {
			mDeviceList = mBaseAdapter.getList();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		if (type != 1) {
			menu.setHeaderTitle("删除");
			menu.add(0, 1, 0, "删除");
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		mDeviceList = mBaseAdapter.getList();
		mDevicesModel = mDeviceList.get(position);
		int menuIndex = item.getItemId();
		Log.i(TAG, "tagzgs-> menuInfo.position=" + position
				+ " item.getItemId()" + item.getItemId());
		if (type == WITH_OPERATE) {
			if (1 == menuIndex) {
				// mChangeFragment.setDevicesId(mDevicesModel);
				MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
						(Context) getActivity());
				mMyOkCancleDlg.setDialogCallback(this);
				mMyOkCancleDlg.setContent("确定要从此区域删除"
						+ mDevicesModel.getmDefaultDeviceName().trim() + "吗?");
				mMyOkCancleDlg.show();
			}
		} else if (type == WITHOUT_OPERATE) {
			if (1 == menuIndex) {
				// mChangeFragment.setDevicesId(mDevicesModel);
				MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
						(Context) getActivity());
				mMyOkCancleDlg.setDialogCallback(this);
				mMyOkCancleDlg.setContent("确定要从此场景删除"
						+ mDevicesModel.getmDefaultDeviceName().trim() + "吗?");
				mMyOkCancleDlg.show();
			}
		}

		return super.onContextItemSelected(item);
	}

	public interface ChangeFragment {
		public void setFragment(Fragment f);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		CGIManager.getInstance().deleteObserver(DevicesListFragment.this);
		CallbackManager.getInstance().deleteObserver(DevicesListFragment.this);
		RfCGIManager.getInstance().deleteObserver(DevicesListFragment.this);
		LibjingleResponseHandlerManager.getInstance().deleteObserver(this);
	}

	public void setAdapter(BaseAdapter mAdapter) {
		// TODO Auto-generated method stub
		mBaseAdapter = null;
		mBaseAdapter = (DevicesBaseAdapter) mAdapter;
		mDeviceList = mBaseAdapter.getList();
	}

	public int isInList(String iee, String ep) {
		initList();
		if (null == mDeviceList || mDeviceList.size() == 0) {
			return -1;
		}
		if (iee == null || ep == null) {
			return -1;
		}
		DevicesModel sd;
		for (int m = 0; m < mDeviceList.size(); m++) {
			sd = mDeviceList.get(m);
			if (iee.trim().equals(sd.getmIeee().trim())
					&& ep.trim().equals(sd.getmEP().trim())) {
				return m;
			}
		}
		return -1;
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		if (EventType.LIGHTSENSOROPERATION == event.getType()) {
			// data maybe null
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				int m = isInList(bundle.getString("IEEE"),
						bundle.getString("EP"));
				if (m != -1) {
					String light = bundle.getString("PARAM");
					mDeviceList.get(m).setmBrightness(Integer.parseInt(light));
					mView.post(new Runnable() {
						@Override
						public void run() {
							mBaseAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		} else if (EventType.TEMPERATURESENSOROPERATION == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				int m = isInList(bundle.getString("IEEE"),
						bundle.getString("EP"));
				if (m != -1) {
					String temperature = bundle.getString("PARAM");
					mDeviceList.get(m).setmTemperature(Float.parseFloat(temperature));
					mView.post(new Runnable() {
						@Override
						public void run() {
							mBaseAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		} else if (EventType.HUMIDITY == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				int m = isInList(bundle.getString("IEEE"),
						bundle.getString("EP"));
				if (m != -1) {
					String humidity = bundle.getString("PARAM");
					mDeviceList.get(m).setmHumidity(Float.parseFloat(humidity));
					mView.post(new Runnable() {
						@Override
						public void run() {
							mBaseAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		} else if (EventType.ON_OFF_STATUS == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m = isInList(data.getDeviceIeee(), data.getDeviceEp());
				if (-1 != m) {
					if (null != data.getValue()) {
						mDeviceList.get(m).setmOnOffStatus(data.getValue());
						mView.post(new Runnable() {
							@Override
							public void run() {
								mBaseAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			} else {
				// if failed,prompt a Toast
				// mError.setVisibility(View.VISIBLE);
			}
		} else if (EventType.MOVE_TO_LEVEL == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m = isInList(data.getDeviceIeee(),
						data.getDeviceEp());
				final String valueString = data.getValue();
				if (-1 != m) {
					if (null != data.getValue()) {
						mDeviceList.get(m).setmLevel(valueString);
						if (Integer.parseInt(valueString) < 7) {
							mDeviceList.get(m).setmOnOffStatus("0");
						} else {
							mDeviceList.get(m).setmOnOffStatus("1");
						}
						mView.post(new Runnable() {
							@Override
							public void run() {
								mBaseAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			}
		} else if (EventType.LOCALIASCIEBYPASSZONE == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				int m = isInList(bundle.getString("IEEE"),
						bundle.getString("EP"));
				if (m != -1) {
					mDeviceList.get(m).setmOnOffStatus(
							bundle.getString("PARAM"));
					mView.post(new Runnable() {
						@Override
						public void run() {
							mBaseAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		} else if (EventType.RF_DEVICE_BYPASS == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				int m = isInList(bundle.getString("IEEE"), "01");
				if (m != -1) {
					mDeviceList.get(m).setmOnOffStatus(
							bundle.getString("PARAM"));
					mView.post(new Runnable() {
						@Override
						public void run() {
							mBaseAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		} else if (EventType.RF_DEVICE_ALL_BYPASS == event.getType()) {
			if (event.isSuccess()) {
				int status = (Integer) event.getData();
				for (int i = 0; i < mDeviceList.size(); i++) {
					if (mDeviceList.get(i).getmModelId()
							.equals(DataHelper.RF_Magnetic_Door)
							|| mDeviceList.get(i).getmModelId()
									.equals(DataHelper.RF_Magnetic_Door_Roll)
							|| mDeviceList
									.get(i)
									.getmModelId()
									.equals(DataHelper.RF_Infrared_Motion_Sensor)) {
						mDeviceList.get(i).setmOnOffStatus(
								String.valueOf(status));
					}
				}
				mView.post(new Runnable() {
					@Override
					public void run() {
						mBaseAdapter.notifyDataSetChanged();
					}
				});
			}
		} else if (EventType.LOCALIASCIEOPERATION == event.getType()) {
			if (event.isSuccess() == true) {

				String status = (String) event.getData();
				if (null != mDeviceList && mDeviceList.size() > 0) {
					for (int m = 0; m < mDeviceList.size(); m++) {
						if (mDeviceList.get(m).getmModelId()
								.indexOf(DataHelper.One_key_operator) == 0) {
							mDeviceList.get(m).setmOnOffStatus(status);
						}
					}
					mView.post(new Runnable() {
						@Override
						public void run() {
							mBaseAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		} else if (EventType.GETEPBYROOMINDEX == event.getType()) {
			if (event.isSuccess() == true) {
				mDeviceList = (List<DevicesModel>) event.getData();
				if (!readDeviceList) {
					return;
				}
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					RfCGIManager.getInstance().GetRFDevByRoomId(mRoomid);
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					LibjingleSendManager.getInstance()
							.GetRFDevByRoomId(mRoomid);
				}
				Collections.sort(mDeviceList, new Comparator<DevicesModel>() {

					@Override
					public int compare(DevicesModel lhs, DevicesModel rhs) {
						// TODO Auto-generated method stub
						return (lhs.getmDevicePriority() - rhs
								.getmDevicePriority());
					}
				});
//				for (DevicesModel mdevice : mDeviceList) {
//					mdevice.setmDeviceRegion(mRoomname);
//				}

			}
		} else if (EventType.RF_GETEPBYROOMINDEX == event.getType()) {
			if (event.isSuccess() == true) {
				List<DevicesModel> temp = (List<DevicesModel>) event.getData();
				if (!readDeviceList) {
					return;
				}
				Collections.sort(temp, new Comparator<DevicesModel>() {

					@Override
					public int compare(DevicesModel lhs, DevicesModel rhs) {
						// TODO Auto-generated method stub
						return (lhs.getmDevicePriority() - rhs
								.getmDevicePriority());
					}
				});
//				for (DevicesModel mdevice : mDeviceList) {
//					mdevice.setmDeviceRegion(mRoomname);
//				}
				readDeviceList = false;
				mDeviceList.addAll(temp);
				mBaseAdapter.setList(mDeviceList);
				mView.post(new Runnable() {
					@Override
					public void run() {
						mBaseAdapter.notifyDataSetChanged();
						swipeRefreshLayout.setRefreshing(false);
					}
				});

			}
		}
	}

	private class GetDataByRoomTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			readDeviceList = true;
			if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
				CGIManager.getInstance().GetEPByRoomIndex(params[0]);
			} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
				LibjingleSendManager.getInstance().GetEPByRoomIndex(params[0]);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
		}

	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
//		String where = " ieee = ? ";
//		String[] args = { mDevicesModel.getmIeee() };
//
//		ContentValues c = new ContentValues();
//		c.put(DevicesModel.DEVICE_REGION, "");
//		c.put(DevicesModel.R_ID, "-1");
//		SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
		int result = 1;
		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
			if (mDevicesModel.getmDeviceId() > 0) {
				CGIManager.getInstance().ModifyDeviceRoomId(mDevicesModel, "-1");
//				result = mDataHelper.update(mSQLiteDatabase,
//						DataHelper.DEVICES_TABLE, c, where, args);
			} else {
				RfCGIManager.getInstance().ModifyRFDevRoomId(mDevicesModel, "-1");
//				result = mDataHelper.update(mSQLiteDatabase,
//						DataHelper.RF_DEVICES_TABLE, c, where, args);
			}
		} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
			if (mDevicesModel.getmDeviceId() > 0) {
				LibjingleSendManager.getInstance().ModifyDeviceRoomId(mDevicesModel, "-1");
//				result = mDataHelper.update(mSQLiteDatabase,
//						DataHelper.DEVICES_TABLE, c, where, args);
			} else {
				LibjingleSendManager.getInstance().ModifyRFDevRoomId(mDevicesModel, "-1");
//				result = mDataHelper.update(mSQLiteDatabase,
//						DataHelper.RF_DEVICES_TABLE, c, where, args);
			}
		}
		
		if (result >= 0) {
			mDeviceList.remove(mDevicesModel);
			mBaseAdapter.notifyDataSetChanged();
		}

	}

}
