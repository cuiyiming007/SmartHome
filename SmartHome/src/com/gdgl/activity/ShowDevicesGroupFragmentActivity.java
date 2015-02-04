package com.gdgl.activity;

/***
 * 进入设备菜单的某一类设备，显示的界面
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdgl.GalleryFlow.FancyCoverFlow;
import com.gdgl.activity.BaseControlFragment.UpdateDevice;
import com.gdgl.activity.DevicesListFragment.refreshData;
import com.gdgl.activity.DevicesListFragment.setData;
import com.gdgl.adapter.DevicesBaseAdapter;
import com.gdgl.adapter.ViewGroupAdapter;
import com.gdgl.adapter.DevicesBaseAdapter.DevicesObserver;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.manager.WarnManager;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.mydata.getlocalcielist.CIEresponse_params;
import com.gdgl.smarthome.R;
import com.gdgl.util.EditDevicesDlg.EditDialogcallback;
import com.gdgl.util.SelectPicPopupWindow;
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
		implements refreshData, DevicesObserver, UpdateDevice,
		EditDialogcallback, setData, UIListener {

	private static final String TAG = "ShowDevicesGroupFragmentActivity";
	LinearLayout mBack;

	List<DevicesModel> mCurrentList;
	HashMap<Integer, List<DevicesModel>> mDevicesListCache;

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
	private DevicesModel getModel;

	public static final String ACTIVITY_SHOW_DEVICES_TYPE = "activity_show_devices_type";
	boolean isDelete = false;

	TextView title;
	Button notifyBtn;
	TextView notifyTextView;
	// LinearLayout parents_need, devices_need;
	// Button mDelete;
	Button set;

	CGIManager mcgiManager;
	DeviceManager mDeviceManager;
	DataHelper mDataHelper;

	public boolean isTop = true;

	/***
	 * 
	 * 从数据库获取设备数据
	 * 
	 * 
	 */
	public class GetDevicesInSortTask extends
			AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			// SQLiteDatabase db = mDataHelper.getSQLiteDatabase();
			// 环境监测
			List<DevicesModel> enviromentlist = mDevicesListCache
					.get(UiUtils.ENVIRONMENTAL_CONTROL);
			if (null == enviromentlist) {
				enviromentlist = DataUtil.getSortManagementDevices(
						ShowDevicesGroupFragmentActivity.this, mDataHelper,
						UiUtils.ENVIRONMENTAL_CONTROL);
				mDevicesListCache.put(UiUtils.ENVIRONMENTAL_CONTROL,
						enviromentlist);
			}
			// 安全防护
			List<DevicesModel> safeList = mDevicesListCache
					.get(UiUtils.SECURITY_CONTROL);
			if (null == safeList) {
				safeList = DataUtil.getSortManagementDevices(
						ShowDevicesGroupFragmentActivity.this, mDataHelper,
						UiUtils.SECURITY_CONTROL);

				DevicesModel dm = null;
				DevicesModel tempDevicesModel;
				int index = -1;
				if (null != safeList && safeList.size() > 0) {
					tempDevicesModel = safeList.get(0);
					for (int i = 0; i < safeList.size(); i++) {
						if (safeList.get(i).getmModelId()
								.indexOf(DataHelper.One_key_operator) == 0) {
							dm = safeList.get(i);
							index = i;
						}
					}
					if (index != -1 && index != 0) {
						safeList.add(0, dm);
						safeList.add(index, tempDevicesModel);
					}
				}
				mDevicesListCache.put(UiUtils.SECURITY_CONTROL, safeList);
			}
			// 照明管理
			List<DevicesModel> LightList = mDevicesListCache
					.get(UiUtils.LIGHTS_MANAGER);
			if (null == LightList) {
				// LightList = DataUtil.getLightingManagementDevices(
				// ShowDevicesGroupFragmentActivity.this, mDataHelper, db);
				LightList = DataUtil.getSortManagementDevices(
						ShowDevicesGroupFragmentActivity.this, mDataHelper,
						UiUtils.LIGHTS_MANAGER);
				mDevicesListCache.put(UiUtils.LIGHTS_MANAGER, LightList);
			}
			// 电器控制
			List<DevicesModel> ElecList = mDevicesListCache
					.get(UiUtils.ELECTRICAL_MANAGER);
			if (null == ElecList) {
				ElecList = DataUtil.getSortManagementDevices(
						ShowDevicesGroupFragmentActivity.this, mDataHelper,
						UiUtils.ELECTRICAL_MANAGER);
				mDevicesListCache.put(UiUtils.ELECTRICAL_MANAGER, ElecList);
			}
			// 节能
			List<DevicesModel> envList = mDevicesListCache
					.get(UiUtils.ENERGY_CONSERVATION);
			if (null == envList) {
				envList = DataUtil.getSortManagementDevices(
						ShowDevicesGroupFragmentActivity.this, mDataHelper,
						UiUtils.ENERGY_CONSERVATION);
				mDevicesListCache.put(UiUtils.ENERGY_CONSERVATION, envList);
			}
			// 其他
			List<DevicesModel> anoList = mDevicesListCache.get(UiUtils.OTHER);
			if (null == anoList) {
				anoList = DataUtil.getSortManagementDevices(
						ShowDevicesGroupFragmentActivity.this, mDataHelper,
						UiUtils.OTHER);
				mDevicesListCache.put(UiUtils.OTHER, anoList);
			}
			// mDataHelper.close(db);
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub

			List<DevicesModel> list = mDevicesListCache.get(type);
			mCurrentList = list;

			initNoContent();
			initFancyCoverFlow();
			initDevicesListFragment();

			initTitleByTag(mListIndex);

			requestData(mListIndex);

			// mDeviceManager.getLocalCIEList();
			//
			// if (1 == result) {
			// List<SimpleDevicesModel> temPlist;
			// temPlist = mDevicesListCache.get(UiUtils.ENVIRONMENTAL_CONTROL);
			// if (null != temPlist && temPlist.size() > 0) {
			// for (SimpleDevicesModel sd : temPlist) {
			// if (sd.getmModelId().indexOf(
			// DataHelper.Indoor_temperature_sensor) == 0) {
			// temptureManager.temperatureSensorOperation(sd, 0);
			// temptureManager.temperatureSensorOperation(sd, 1);
			// } else if (sd.getmModelId().indexOf(
			// DataHelper.Light_Sensor) == 0) {
			// temptureManager.lightSensorOperation(sd, 0);
			// }
			// }
			// }
			// }
		}
	}

	public class GetDataTask extends AsyncTask<Void, Void, List<DevicesModel>> {

		@Override
		protected List<DevicesModel> doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return mCurrentList;
		}

		@Override
		protected void onPostExecute(List<DevicesModel> result) {
			super.onPostExecute(result);
			mDevicesListFragment.stopRefresh();
		}
	}

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
		initTitle();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateMessageNum();
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
		// final MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
		// ShowDevicesGroupFragmentActivity.this);
		// //
		// mMyOkCancleDlg.setDialogCallback(ShowDevicesGroupFragmentActivity.this);
		mBack = (LinearLayout) findViewById(R.id.goback);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				back();
			}
		});

		notifyBtn = (Button) findViewById(R.id.alarm_btn_rt);
		notifyTextView = (TextView) findViewById(R.id.unread_ms_rt);
		notifyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(ShowDevicesGroupFragmentActivity.this,
						ConfigActivity.class);
				i.putExtra("fragid", 1);
				startActivity(i);
				WarnManager.getInstance().intilMessageNum();
			}
		});
		title = (TextView) findViewById(R.id.title);
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
		mcgiManager.deleteObserver(this);
		mDeviceManager.deleteObserver(this);
		CallbackManager.getInstance().deleteObserver(this);
	}

	// public SimpleDevicesModel getCurrentDeviceByIeee(String iee) {
	// if (null != mCurrentList) {
	// for (SimpleDevicesModel mSimpleDevicesModel : mCurrentList) {
	// if (iee == mSimpleDevicesModel.getmIeee()) {
	// return mSimpleDevicesModel;
	// }
	// }
	// }
	// return null;
	// }

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

		mDataHelper = new DataHelper(ShowDevicesGroupFragmentActivity.this);

		mDevicesListCache = new HashMap<Integer, List<DevicesModel>>();

		images = UiUtils.getImgByType(UiUtils.SECURITY_CONTROL);
		tags = UiUtils.getTagsByType(UiUtils.SECURITY_CONTROL);
		types = UiUtils.getType(UiUtils.SECURITY_CONTROL);

		mcgiManager = CGIManager.getInstance();
		mcgiManager.addObserver(this);

		mDeviceManager = DeviceManager.getInstance();
		mDeviceManager.addObserver(this);
		CallbackManager.getInstance().addObserver(this);

		fragmentManager = this.getFragmentManager();

		new GetDevicesInSortTask().execute(1);
	}

	private void initDevicesListFragment() {
		// TODO Auto-generated method stub
		mDevicesBaseAdapter = new DevicesBaseAdapter(
				ShowDevicesGroupFragmentActivity.this, this);
		mDevicesBaseAdapter.setList(mCurrentList);
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		mDevicesListFragment = new DevicesListFragment();
		mDevicesListFragment.initList();
		fragmentTransaction.replace(R.id.devices_control_fragment,
				mDevicesListFragment, "LightsControlFragment");
		mDevicesListFragment.setAdapter(mDevicesBaseAdapter);
		fragmentTransaction.commit();
	}

	private void back() {
		if (isTop) {
			finish();
		} else {
			isTop = true;
			resetFragment();
		}
	}

	public void resetFragment() {
		int type = types[mListIndex];
		mCurrentList = mDevicesListCache.get(type);
		mDevicesBaseAdapter.setList(mCurrentList);
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		mDevicesListFragment = new DevicesListFragment();
		fragmentTransaction.replace(R.id.devices_control_fragment,
				mDevicesListFragment, "LightsControlFragment");
		mDevicesListFragment.setAdapter(mDevicesBaseAdapter);
		fragmentTransaction.commit();
		fancyCoverFlow.setVisibility(View.VISIBLE);
		initTitleByTag(mListIndex);
	}

	private void initFancyCoverFlow() {
		// TODO Auto-generated method stub
		fancyCoverFlow = (FancyCoverFlow) findViewById(R.id.devices_gallery);
		float density = getResources().getDisplayMetrics().density;
		fancyCoverFlow.setAdapter(new ViewGroupAdapter(getApplicationContext(),
				images, tags, (int) (density * 100 + 0.5),
				(int) (density * 100 + 0.5)));
		fancyCoverFlow.setCallbackDuringFling(false);
		// fancyCoverFlow.setSpacing(50);
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

	/***
	 * 如果position是指向环境界面，则请求环境数据；如果是在安防界面，则请求布撤防数据
	 * 
	 * @param postion
	 */
	public void requestData(int postion) {
		if (UiUtils.ENVIRONMENTAL_CONTROL == postion) {
			if (null != mCurrentList && mCurrentList.size() > 0) {
				for (DevicesModel sd : mCurrentList) {
					if (sd.getmModelId().indexOf(
							DataHelper.Indoor_temperature_sensor) == 0) {
						mcgiManager.temperatureSensorOperation(sd, 0);
						mcgiManager.temperatureSensorOperation(sd, 1);
					} else if (sd.getmModelId()
							.indexOf(DataHelper.Light_Sensor) == 0) {
						mcgiManager.lightSensorOperation(sd, 0);
					}
				}
			}
//		} else if (UiUtils.SECURITY_CONTROL == postion) {
//			mDeviceManager.getLocalCIEList();
		}
	}

	public void changeForCoverFlow(int p) {
		if (isTop) {
			if (mListIndex != p) {
				mListIndex = p;

				requestData(mListIndex);
				refreshAdapter(mListIndex);
			}
		} else {
			isTop = true;
			resetFragment();
		}
	}

	public void refreshAdapter(int postion) {
		mCurrentList = null;
		int type = types[postion];
		if (null != mDevicesListCache.get(type)) {
			mCurrentList = mDevicesListCache.get(type);
		}
		setdata(mCurrentList);
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
	public DevicesModel getDeviceModle(int postion) {
		// TODO Auto-generated method stub
		if (null != mCurrentList) {
			return mCurrentList.get(postion);
		}
		return null;
	}

	@Override
	public void setDevicesId(DevicesModel devicesModel) {
		// TODO Auto-generated method stub
		getModel = devicesModel;
	}

	@Override
	public void setFragment(Fragment mFragment, int postion) {
		// TODO Auto-generated method stub
		Log.i(TAG, "zzz->setFragment postion=" + postion);
		// mCurrentListItemPostion = postion;
		fancyCoverFlow.setVisibility(View.GONE);
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.devices_control_fragment, mFragment);
		// fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		isTop = false;
		title.setText(getModel.getmDefaultDeviceName().replace(" ", ""));
	}

	public void initTitleByTag(int postion) {
		title.setText(tags[postion]);
		// parents_need.setVisibility(View.VISIBLE);
		// devices_need.setVisibility(View.GONE);
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

	// @Override
	// public void dialogdo() {
	// // TODO Auto-generated method stub
	// LightManager.getInstance().deleteNode(devicesIeee);
	// mDataHelper.delete(ShowDevicesGroupFragmentActivity.this,
	// mDataHelper.getSQLiteDatabase(), DataHelper.DEVICES_TABLE,
	// " ieee=? ", new String[] { devicesIeee });
	// mDevicesListCache.remove(types[mListIndex]);
	// fragmentManager.popBackStack();
	// refreshAdapter(mListIndex);
	// initTitleByTag(mListIndex);
	// }

	@Override
	public void setLayout() {
		// TODO Auto-generated method stub
		mDevicesListFragment.setLayout();
	}

	@Override
	public void deleteDevices(String id) {
		// TODO Auto-generated method stub
		// Log.i(TAG, "tagzgs->delete id=" + id);
		// LightManager.getInstance().deleteNode(devicesIeee);
		// mDataHelper.delete(ShowDevicesGroupFragmentActivity.this,
		// mDataHelper.getSQLiteDatabase(), DataHelper.DEVICES_TABLE,
		// " ieee=? ", new String[] { id });
		// mDevicesListCache.clear();
		// refreshAdapter(mListIndex);
	}

	// public class UpdateDevicesTask extends
	// AsyncTask<Paremeters, Integer, Integer> {
	// public Paremeters par;
	//
	// @Override
	// protected Integer doInBackground(Paremeters... params) {
	// // TODO Auto-generated method stub
	// par = params[0];
	// SimpleDevicesModel sd = par.sd;
	// ContentValues c = par.c;
	// String where = " ieee = ? and ep = ?";
	// String Ieee = sd.getmIeee().trim();
	// String ep = sd.getmEP().trim();
	// String[] args = { Ieee, ep };
	// SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
	// int result = mDataHelper.update(mSQLiteDatabase,
	// DataHelper.DEVICES_TABLE, c, where, args);
	// mDataHelper.close(mSQLiteDatabase);
	// return result;
	// }
	//
	// @Override
	// protected void onPostExecute(Integer result) {
	// // TODO Auto-generated method stub
	// int type = types[mListIndex];
	//
	// DevicesModel sd = par.sd;
	// String Ieee = sd.getmIeee().trim();
	// String ep = sd.getmEP().trim();
	//
	// if (result >= 0) {
	// int m = getDevicesPostion(Ieee, ep, mCurrentList);
	// if (-1 != m) {
	// mDevicesListCache.get(type).set(m, sd);
	// mCurrentList = mDevicesListCache.get(type);
	// setdata(mCurrentList);
	// }
	// }
	// }
	// }

	//
	// @Override
	// public boolean updateDevices(SimpleDevicesModel sd, ContentValues c) {
	// // TODO Auto-generated method stub
	// Paremeters p = new Paremeters();
	// p.sd = sd;
	// p.c = c;
	// new UpdateDevicesTask().execute(p);
	// return false;
	// }

	@Override
	public void saveedit(DevicesModel msDevicesModel, String name) {
		// TODO Auto-generated method stub
		String where = " ieee = ? ";
		String[] args = { msDevicesModel.getmIeee() };

		ContentValues c = new ContentValues();
		c.put(DevicesModel.DEFAULT_DEVICE_NAME, name);
		// c.put(DevicesModel.DEVICE_REGION, region);

		SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
		int result = mDataHelper.update(mSQLiteDatabase,
				DataHelper.DEVICES_TABLE, c, where, args);
		if (result >= 0) {

			mDevicesListCache.clear();
			refreshAdapter(mListIndex);
		}
	}

	/***
	 * 设置当前mDevicesListFragment的数据源list，并刷新列表
	 */
	@Override
	public void setdata(List<DevicesModel> list) {
		// TODO Auto-generated method stub

		if (null == list || list.size() == 0) {
			mNoDevices.setVisibility(View.VISIBLE);
		} else {
			mNoDevices.setVisibility(View.GONE);
			mDevicesBaseAdapter.setList(list);
			mDevicesListFragment.initList();
			mDevicesListFragment.setLayout();
			mDevicesBaseAdapter.notifyDataSetChanged();

			int type = types[mListIndex];
			mDevicesListCache.put(type, list);
		}
		initTitleByTag(mListIndex);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		if (EventType.LIGHTSENSOROPERATION == event.getType()) {
			// data maybe null
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				List<DevicesModel> temList = mDevicesListCache
						.get(UiUtils.ENVIRONMENTAL_CONTROL);
				int m = getDevicesPostion(bundle.getString("IEEE"),
						bundle.getString("EP"), temList);
				if (m != -1) {
					String light = bundle.getString("PARAM");
					temList.get(m).setmBrightness(Integer.parseInt(light));

					// ContentValues c = new ContentValues();
					// c.put(DevicesModel.BRIGHTNESS, Integer.parseInt(light));
					// Paremeters p = new Paremeters();
					// p.dm = temList.get(m);
					// p.c = c;
					// new UpdateDeviceStatusToDatabaseTask().execute(p);

					if (UiUtils.ENVIRONMENTAL_CONTROL == mListIndex) {
						title.post(new Runnable() {
							@Override
							public void run() {
								// setdata(mCurrentList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			} else {
			}
		} else if (EventType.TEMPERATURESENSOROPERATION == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				List<DevicesModel> temList = mDevicesListCache
						.get(UiUtils.ENVIRONMENTAL_CONTROL);
				int m = getDevicesPostion(bundle.getString("IEEE"),
						bundle.getString("EP"), temList);
				if (m != -1) {
					String temperature = bundle.getString("PARAM");
					temList.get(m).setmTemperature(
							Float.parseFloat(temperature));

					// ContentValues c = new ContentValues();
					// c.put(DevicesModel.TEMPERATURE,
					// Float.parseFloat(temperature));
					// Paremeters p = new Paremeters();
					// p.dm = temList.get(m);
					// p.c = c;
					// new UpdateDeviceStatusToDatabaseTask().execute(p);

					if (UiUtils.ENVIRONMENTAL_CONTROL == mListIndex) {
						title.post(new Runnable() {
							@Override
							public void run() {
								// setdata(mCurrentList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			}
		} else if (EventType.HUMIDITY == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				List<DevicesModel> temList = mDevicesListCache
						.get(UiUtils.ENVIRONMENTAL_CONTROL);
				int m = getDevicesPostion(bundle.getString("IEEE"),
						bundle.getString("EP"), temList);
				if (m != -1) {
					String humidity = bundle.getString("PARAM");
					temList.get(m).setmHumidity(Float.parseFloat(humidity));

					// ContentValues c = new ContentValues();
					// c.put(DevicesModel.HUMIDITY, Float.parseFloat(humidity));
					// Paremeters p = new Paremeters();
					// p.dm = temList.get(m);
					// p.c = c;
					// new UpdateDeviceStatusToDatabaseTask().execute(p);

					if (UiUtils.ENVIRONMENTAL_CONTROL == mListIndex) {
						title.post(new Runnable() {
							@Override
							public void run() {
								// setdata(mCurrentList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			}
//		} else if (EventType.GETICELIST == event.getType()) {
//			if (event.isSuccess()) {
//				ArrayList<CIEresponse_params> devDataList = (ArrayList<CIEresponse_params>) event
//						.getData();
//
//				List<DevicesModel> safeList = mDevicesListCache
//						.get(UiUtils.SECURITY_CONTROL);
//
//				List<DevicesModel> updatsLis = new ArrayList<DevicesModel>();// 需要刷新的集合
//				if (null != devDataList && devDataList.size() > 0) {
//					for (int i = 0; i < devDataList.size(); i++) {
//						CIEresponse_params cp = devDataList.get(i);
//
//						int m = getDevicesPostion(cp.getCie().getIeee(), cp
//								.getCie().getEp(), safeList);
//						if (-1 != m) {
//							String s = cp.getCie().getElserec().getBbypass();
//							String status = s.trim().toLowerCase()
//									.equals("true") ? "1" : "0";
//							if (!status.equals(safeList.get(m)
//									.getmOnOffStatus())) {
//								safeList.get(m).setmOnOffStatus(status);
//								updatsLis.add(safeList.get(m));
//							}
//						}
//					}
//					if (UiUtils.SECURITY_CONTROL == mListIndex) {
//						// mCurrentList = safeList;
//						title.post(new Runnable() {
//							@Override
//							public void run() {
//								// setdata(mCurrentList);
//								mDevicesBaseAdapter.notifyDataSetChanged();
//							}
//						});
//					}
//					if (null != updatsLis && updatsLis.size() > 0) {
//						new UpdateICELestTask().execute(updatsLis);
//					}
//				}
//			}
		} else if (EventType.LOCALIASCIEBYPASSZONE == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				List<DevicesModel> temList = mDevicesListCache
						.get(UiUtils.SECURITY_CONTROL);
				int m = getDevicesPostion(bundle.getString("IEEE"),
						bundle.getString("EP"), temList);
				if (m != -1) {
					temList.get(m).setmOnOffStatus(bundle.getString("PARAM"));

					if (UiUtils.SECURITY_CONTROL == mListIndex) {
						title.post(new Runnable() {
							@Override
							public void run() {
								// setdata(mCurrentList);
								mDevicesBaseAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			}
		} else if (EventType.LOCALIASCIEOPERATION == event.getType()) {
			if (event.isSuccess() == true) {

				List<DevicesModel> safeList = mDevicesListCache
						.get(UiUtils.SECURITY_CONTROL);

				// List<DevicesModel> updatsLis = new ArrayList<DevicesModel>();

				final String status = (String) event.getData();

				safeList.get(0).setmOnOffStatus(status);
				// updatsLis.add(safeList.get(0));
				if (UiUtils.SECURITY_CONTROL == mListIndex) {
					// mCurrentList = safeList;
					title.post(new Runnable() {
						@Override
						public void run() {
							// setdata(mCurrentList);
							mDevicesBaseAdapter.notifyDataSetChanged();
							DeviceDtailFragment.getInstance()
							.refreshSecurity(status);
						}
					});
				}
				// new UpdateICELestTask().execute(updatsLis);
			}
		} else if (EventType.ON_OFF_STATUS == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				final CallbackResponseType2 detaildata = data;
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mCurrentList);
				if (-1 != m) {
					if (null != data.getValue()) {
						mCurrentList.get(m).setmOnOffStatus(data.getValue());
						title.post(new Runnable() {
							@Override
							public void run() {
								// setDataActivity.setdata(mDeviceList);
								mDevicesBaseAdapter.notifyDataSetChanged();
								DeviceDtailFragment.getInstance()
								.refreshOnOffStatus(detaildata);
							}
						});

						// ContentValues c = new ContentValues();
						// c.put(DevicesModel.ON_OFF_STATUS, data.getValue());
						// Paremeters p = new Paremeters();
						// p.dm = mCurrentList.get(m);
						// p.c = c;
						// new UpdateDeviceStatusToDatabaseTask().execute(p);
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
				final CallbackResponseType2 detaildata = data;
				// Log.i("MOVE_TO_LEVEL", mCurrentList.toString());
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mCurrentList);
				final String valueString = data.getValue();
				if (-1 != m) {
					if (null != data.getValue()) {
						mCurrentList.get(m).setmLevel(valueString);
						title.post(new Runnable() {
							@Override
							public void run() {
								// setDataActivity.setdata(mDeviceList);
								mDevicesBaseAdapter.notifyDataSetChanged();
//								if (!isTop) {
//									// DeviceDtailFragment.getInstance()
//									// .refreshLevel(valueString);
//									DeviceDtailFragment.getInstance()
//											.refreshLevel(detaildata);
//								}
							}
						});

						// ContentValues c = new ContentValues();
						// c.put(DevicesModel.LEVEL, data.getValue());
						// Paremeters p = new Paremeters();
						// p.dm = mCurrentList.get(m);
						// p.c = c;
						// new UpdateDeviceStatusToDatabaseTask().execute(p);
					}
				}
			} else {
				// if failed,prompt a Toast
				// mError.setVisibility(View.VISIBLE);
			}
		} 
//			else if (EventType.CURRENT == event.getType()) {
//			if (event.isSuccess() == true) {
//				// data maybe null
//				CallbackResponseType2 data = (CallbackResponseType2) event
//						.getData();
//				int m = getDevicesPostion(data.getDeviceIeee(),
//						data.getDeviceEp(), mCurrentList);
//				final String valueString = data.getValue();
//				if (-1 != m) {
//					if (null != data.getValue()) {
//						mCurrentList.get(m).setmCurrent(valueString);
//
//						title.post(new Runnable() {
//							@Override
//							public void run() {
//								// setDataActivity.setdata(mDeviceList);
//								mDevicesBaseAdapter.notifyDataSetChanged();
//								if (!isTop) {
//									DeviceDtailFragment.getInstance()
//											.refreshCurrent(valueString);
//								}
//							}
//						});
//
//						// ContentValues c = new ContentValues();
//						// c.put(DevicesModel.CURRENT, data.getValue());
//						// Paremeters p = new Paremeters();
//						// p.dm = mCurrentList.get(m);
//						// p.c = c;
//						// new UpdateDeviceStatusToDatabaseTask().execute(p);
//					}
//				}
//			} else {
//				// if failed,prompt a Toast
//				// mError.setVisibility(View.VISIBLE);
//			}
//		} else if (EventType.VOLTAGE == event.getType()) {
//			if (event.isSuccess() == true) {
//				// data maybe null
//				CallbackResponseType2 data = (CallbackResponseType2) event
//						.getData();
//				int m = getDevicesPostion(data.getDeviceIeee(),
//						data.getDeviceEp(), mCurrentList);
//				final String valueString = data.getValue();
//				if (-1 != m) {
//					if (null != data.getValue()) {
//						mCurrentList.get(m).setmVoltage(data.getValue());
//						title.post(new Runnable() {
//							@Override
//							public void run() {
//								// setDataActivity.setdata(mDeviceList);
//								mDevicesBaseAdapter.notifyDataSetChanged();
//								if (!isTop) {
//									DeviceDtailFragment.getInstance()
//											.refreshVoltage(valueString);
//								}
//							}
//						});
//
//						// ContentValues c = new ContentValues();
//						// c.put(DevicesModel.VOLTAGE, data.getValue());
//						// Paremeters p = new Paremeters();
//						// p.dm = mCurrentList.get(m);
//						// p.c = c;
//						// new UpdateDeviceStatusToDatabaseTask().execute(p);
//					}
//				}
//			} else {
//				// if failed,prompt a Toast
//				// mError.setVisibility(View.VISIBLE);
//			}
//		} else if (EventType.ENERGY == event.getType()) {
//			if (event.isSuccess() == true) {
//				// data maybe null
//				CallbackResponseType2 data = (CallbackResponseType2) event
//						.getData();
//				int m = getDevicesPostion(data.getDeviceIeee(),
//						data.getDeviceEp(), mCurrentList);
//				final String valueString = String.valueOf(Float.parseFloat(data
//						.getValue()));
//				if (-1 != m) {
//					if (null != data.getValue()) {
//						mCurrentList.get(m).setmEnergy(data.getValue());
//						title.post(new Runnable() {
//							@Override
//							public void run() {
//								// setDataActivity.setdata(mDeviceList);
//								mDevicesBaseAdapter.notifyDataSetChanged();
//								if (!isTop) {
//									DeviceDtailFragment.getInstance()
//											.refreshEnergy(valueString);
//								}
//							}
//						});
//
//						// ContentValues c = new ContentValues();
//						// c.put(DevicesModel.ENERGY, data.getValue());
//						// Paremeters p = new Paremeters();
//						// p.dm = mCurrentList.get(m);
//						// p.c = c;
//						// new UpdateDeviceStatusToDatabaseTask().execute(p);
//					}
//				}
//			} else {
//				// if failed,prompt a Toast
//				// mError.setVisibility(View.VISIBLE);
//			}
//		} else if (EventType.POWER == event.getType()) {
//			if (event.isSuccess() == true) {
//				// data maybe null
//				CallbackResponseType2 data = (CallbackResponseType2) event
//						.getData();
//				int m = getDevicesPostion(data.getDeviceIeee(),
//						data.getDeviceEp(), mCurrentList);
//				final String valueString = data.getValue();
//				if (-1 != m) {
//					if (null != data.getValue()) {
//						mCurrentList.get(m).setmPower(data.getValue());
//						title.post(new Runnable() {
//							@Override
//							public void run() {
//								// setDataActivity.setdata(mDeviceList);
//								mDevicesBaseAdapter.notifyDataSetChanged();
//								if (!isTop) {
//									DeviceDtailFragment.getInstance()
//											.refreshPower(valueString);
//								}
//							}
//						});
//
//						// ContentValues c = new ContentValues();
//						// c.put(DevicesModel.POWER, data.getValue());
//						// Paremeters p = new Paremeters();
//						// p.dm = mCurrentList.get(m);
//						// p.c = c;
//						// new UpdateDeviceStatusToDatabaseTask().execute(p);
//					}
//				}
//			} else {
//				// if failed,prompt a Toast
//				// mError.setVisibility(View.VISIBLE);
//			}
//		} 
			else if (EventType.WARN == event.getType()) {
			title.post(new Runnable() {

				@Override
				public void run() {
					updateMessageNum();
					setdata(mCurrentList);
				}
			});
		} else if(EventType.SCAPEDDEVICE == event.getType()){
			Log.i("ShowDevices SCAPEDDEVICE", "SCAPEDDEVICE");
			ArrayList<DevicesModel> scapedList = (ArrayList<DevicesModel>) event.getData();
			for(DevicesModel mDevicesModel : scapedList){
				Log.i("mDevicesModel ", "IEEE = "+mDevicesModel.getmIeee());
			}
		} else if (EventType.SCAPEDDEVICE == event.getType()) {
			ArrayList<DevicesModel> scapedList = (ArrayList<DevicesModel>) event
					.getData();
			for (DevicesModel mDevicesModel : scapedList) {
				int sort = mDevicesModel.getmDeviceSort();
				switch (sort) {
				case UiUtils.ENVIRONMENTAL_CONTROL:
					mDevicesListCache.put(UiUtils.ENVIRONMENTAL_CONTROL, DataUtil.getSortManagementDevices(
						ShowDevicesGroupFragmentActivity.this, mDataHelper,
						UiUtils.ENVIRONMENTAL_CONTROL));
					mDevicesListCache.get(UiUtils.ENVIRONMENTAL_CONTROL).add(
							mDevicesModel);
					break;
				case UiUtils.SECURITY_CONTROL:
					List<DevicesModel> safeList = mDevicesListCache
							.get(UiUtils.SECURITY_CONTROL);
					safeList = DataUtil.getSortManagementDevices(
							ShowDevicesGroupFragmentActivity.this, mDataHelper,
							UiUtils.SECURITY_CONTROL);

					DevicesModel dm = null;
					DevicesModel tempDevicesModel;
					int index = -1;
					if (null != safeList && safeList.size() > 0) {
						tempDevicesModel = safeList.get(0);
						for (int i = 0; i < safeList.size(); i++) {
							if (safeList.get(i).getmModelId()
									.indexOf(DataHelper.One_key_operator) == 0) {
								dm = safeList.get(i);
								index = i;
							}
						}
						if (index != -1 && index != 0) {
							safeList.add(0, dm);
							safeList.add(index, tempDevicesModel);
						}
					}
					mDevicesListCache.put(UiUtils.SECURITY_CONTROL, safeList);
					mDevicesListCache.get(UiUtils.SECURITY_CONTROL).add(
							mDevicesModel);
					break;
				case UiUtils.LIGHTS_MANAGER:
					mDevicesListCache.put(UiUtils.LIGHTS_MANAGER, DataUtil.getSortManagementDevices(
							ShowDevicesGroupFragmentActivity.this, mDataHelper,
							UiUtils.LIGHTS_MANAGER));
					mDevicesListCache.get(UiUtils.LIGHTS_MANAGER).add(
							mDevicesModel);
					break;
				case UiUtils.ELECTRICAL_MANAGER:
					mDevicesListCache.put(UiUtils.ELECTRICAL_MANAGER, DataUtil.getSortManagementDevices(
							ShowDevicesGroupFragmentActivity.this, mDataHelper,
							UiUtils.ELECTRICAL_MANAGER));
					mDevicesListCache.get(UiUtils.ELECTRICAL_MANAGER).add(
							mDevicesModel);
					break;
				case UiUtils.ENERGY_CONSERVATION:
					mDevicesListCache.put(UiUtils.ENERGY_CONSERVATION, DataUtil.getSortManagementDevices(
							ShowDevicesGroupFragmentActivity.this, mDataHelper,
							UiUtils.ENERGY_CONSERVATION));
					mDevicesListCache.get(UiUtils.ENERGY_CONSERVATION).add(
							mDevicesModel);
					break;
				case UiUtils.OTHER:
					mDevicesListCache.put(UiUtils.OTHER, DataUtil.getSortManagementDevices(
							ShowDevicesGroupFragmentActivity.this, mDataHelper,
							UiUtils.OTHER));
					break;

				default:
					break;
				}
			}
			title.post(new Runnable() {

				@Override
				public void run() {
					mDevicesBaseAdapter.notifyDataSetChanged();
				}
			});
		}

	}


	class Paremeters {
		public DevicesModel dm;
		public ContentValues c;
	}

	public class UpdateDeviceStatusToDatabaseTask extends
			AsyncTask<Paremeters, Integer, Integer> {
		public Paremeters par;

		@Override
		protected Integer doInBackground(Paremeters... params) {
			// TODO Auto-generated method stub
			par = params[0];
			DevicesModel mDevicesModel = par.dm;
			ContentValues c = par.c;
			String where = " ieee = ? and ep = ?";
			String ieee = mDevicesModel.getmIeee().trim();
			String ep = mDevicesModel.getmEP().trim();
			String[] args = { ieee, ep };
			SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
			int result = mDataHelper.update(mSQLiteDatabase,
					DataHelper.DEVICES_TABLE, c, where, args);
			mDataHelper.close(mSQLiteDatabase);
			return result;
		}

	}

//	public class UpdateICELestTask extends
//			AsyncTask<List<DevicesModel>, Integer, Integer> {
//
//		@Override
//		protected Integer doInBackground(List<DevicesModel>... params) {
//			// TODO Auto-generated method stub
//			List<DevicesModel> updateList = params[0];
//			if (null == updateList || updateList.size() == 0) {
//				return null;
//			} else {
//				String where = " ieee=? and ep=? ";
//				ContentValues v;
//				SQLiteDatabase db = mDataHelper.getSQLiteDatabase();
//				for (DevicesModel devicesModel : updateList) {
//					String[] args = { devicesModel.getmIeee().trim(),
//							devicesModel.getmEP().trim() };
//					String status = devicesModel.getmOnOffStatus();
//					v = new ContentValues();
//					v.put(DevicesModel.ON_OFF_STATUS, status == "1" ? "1" : "0");
//					mDataHelper.update(db, DataHelper.DEVICES_TABLE, v, where,
//							args);
//				}
//				mDataHelper.close(db);
//			}
//			return null;
//		}
//
//	}

	/***
	 * 根据ieee和ep确定该设备在界面对应的列表里面的位置
	 * 
	 * @param ieee
	 * @param ep
	 * @param deviceList
	 * @return
	 */
	private int getDevicesPostion(String ieee, String ep,
			List<DevicesModel> deviceList) {
		if (null == ieee || null == ep) {
			return -1;
		}
		if (ieee.trim().equals("") || ep.trim().equals("")) {
			return -1;
		}
		if (null != deviceList && deviceList.size() > 0) {
			for (int m = 0; m < deviceList.size(); m++) {
				DevicesModel dm = deviceList.get(m);
				if (ieee.trim().equals(dm.getmIeee().trim())
						&& ep.trim().equals(dm.getmEP().trim())) {
					return m;
				}
			}
		}
		return -1;

	}

	private void updateMessageNum() {
		int messageNum = WarnManager.getInstance().getMessageNum();

		if (messageNum == 0) {
			notifyTextView.setVisibility(View.GONE);
		} else {
			notifyTextView.setVisibility(View.VISIBLE);
			notifyTextView.setText(String.valueOf(messageNum));
		}
	}
}
