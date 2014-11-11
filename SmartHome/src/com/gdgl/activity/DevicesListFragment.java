package com.gdgl.activity;

/***
 * 某一类设备中的设备列表
 */
import java.util.ArrayList;
import java.util.List;

import com.gdgl.activity.ShowDevicesGroupFragmentActivity.adapterSeter;
import com.gdgl.activity.UIinterface.IFragmentCallbak;
import com.gdgl.app.ApplicationController;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.smarthome.R;
import com.gdgl.util.VersionDlg;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;
import com.gdgl.util.UiUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;

public class DevicesListFragment extends BaseFragment implements adapterSeter,
		IFragmentCallbak {

	private static final String TAG = "DevicesListFragment";
	private View mView;
	PullToRefreshListView mPullToRefreshListView;
	Switch switchBar;
	SeekBar seekBar;
	private setData setDataActivity;
	SimpleDevicesModel mSimpleDevicesModel;
	SimpleDevicesModel onekeyopratorModel;
	int refreshTag = 0;
	/***
	 * 列表上的ui的adapter
	 * 跟ShowDevicesGroupFragmentActivity的DevicesBaseAdapter对应，父类引用指向子类对象
	 */
	BaseAdapter mBaseAdapter;
	private refreshData mRefreshData;
	LinearLayout list_root;

	// public static final String PASS_OBJECT = "pass_object";
	//
	// public static final String PASS_ONOFFIMG = "pass_on_off_img";
	//
	// public static final String OPERATOR = "with_or_not_operator";

	Context mContext;

	AdapterContextMenuInfo selectedMenuInfo = null;

	public static final int WITH_OPERATE = 0;
	public static final int WITHOUT_OPERATE = 2;

	private int type = 1;
	/***
	 * 用于存储当前列表的Item
	 */
	List<SimpleDevicesModel> mDeviceList;
	// 一键布撤防的设备 ieee=00137A00000121F0
	DevicesModel totalControlDevice;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		if (null != extras) {
			type = extras.getInt(Constants.OPERATOR, WITH_OPERATE);
		}
		CGIManager.getInstance().addObserver(DevicesListFragment.this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.devices_list_fragment, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub

		list_root = (LinearLayout) mView.findViewById(R.id.list_root);
		setLayout();
		mPullToRefreshListView = (PullToRefreshListView) mView
				.findViewById(R.id.devices_list);
		switchBar = (Switch) mView.findViewById(R.id.switch_btn);
		seekBar = (SeekBar) mView.findViewById(R.id.devices_seek_bar);
		initList();
		setListeners();
		registerForContextMenu(mPullToRefreshListView.getRefreshableView());
		mPullToRefreshListView.setAdapter(mBaseAdapter);
	}

	private void setListeners() {
		// TODO Auto-generated method stub
		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						if (1 == refreshTag) {
						} else {
							refreshTag = 1;
							String label = DateUtils.formatDateTime(
									getActivity(), System.currentTimeMillis(),
									DateUtils.FORMAT_SHOW_TIME
											| DateUtils.FORMAT_SHOW_DATE
											| DateUtils.FORMAT_ABBREV_ALL);

							// Update the LastUpdatedLabel
							refreshView.getLoadingLayoutProxy()
									.setLastUpdatedLabel(label);

							// Do work to refresh the list here.
							mRefreshData.refreshListData();
						}
					}
				});
		if (type != WITHOUT_OPERATE) {
			mPullToRefreshListView
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							Log.i(TAG, "tagzgs->position=" + position);
							mSimpleDevicesModel = mRefreshData
									.getDeviceModle(position - 1);
							mRefreshData.setDevicesId(mSimpleDevicesModel);

							// 判断是除气体感应器及紧急按钮以外的安防设备，且安防控制中心状态是关闭的
							if (mSimpleDevicesModel.getmModelId().indexOf("ZA01") != 0
									&& mSimpleDevicesModel.getmModelId().indexOf(
										DataHelper.Emergency_Button) != 0
									&& mSimpleDevicesModel.getmDeviceId() == DataHelper.IAS_ZONE_DEVICETYPE
									&& totalControlDevice != null
									&& totalControlDevice.getmOnOffStatus().equals("0")) {
								VersionDlg vd = new VersionDlg(
										(Context) getActivity());
								vd.setContent("安防设备已关闭");
								vd.show();
							} else {
								if (mSimpleDevicesModel.getmModelId().indexOf(DataHelper
										.Infrared_controller)==0) { // 红外遥控器
									Intent intent = new Intent();
									intent.setClass((Context) getActivity(),
											KongtiaoTvControlActivity.class);
									intent.putExtra(Constants.PASS_OBJECT, mSimpleDevicesModel);
									startActivity(intent);
								} else {
									Fragment mFragment = null;

									mFragment = new DeviceDtailFragment();
//									if (mSimpleDevicesModel
//											.getmModelId()
//											.indexOf(DataHelper.Doorbell_button) == 0) { // 门铃按键
//										mFragment = new DoorBellFragment();
//									} else if (mSimpleDevicesModel
//											.getmModelId()
//											.indexOf(
//													DataHelper.Wireless_Intelligent_valve_switch) == 0) { // 无线智能阀门开关
//										mFragment = new OutPutFragment();
//									} else if (mSimpleDevicesModel
//											.getmModelId()
//											.indexOf(DataHelper.One_key_operator) == 0) {
//										Bundle extras = new Bundle();
//										extras.putParcelable(
//												Constants.PASS_OBJECT,
//												mSimpleDevicesModel);
//										mFragment = new SafeSimpleOperation(
//												DevicesListFragment.this);
//
//										mFragment.setArguments(extras);
//									} else {
//										mFragment = UiUtils.getDeviceDetailFragment(mSimpleDevicesModel
//													.getmDeviceId());
//									}
									if (null != mFragment) {
										Bundle extras = new Bundle();
										if (isSimpleOnOffDevice()) {
											int[] OnOffImg = {
													R.drawable.bufang_on,
													R.drawable.chefang_off };
											extras.putIntArray(
													Constants.PASS_ONOFFIMG,
													OnOffImg);
										}
										// PASS_OBKECT
										extras.putParcelable(
												Constants.PASS_OBJECT,
												mSimpleDevicesModel);
										mFragment.setArguments(extras);
										mRefreshData.setFragment(mFragment,
												position - 1);
									}
								}
							}
						}

					});

		}
	}

	/***
	 * 判断设备mSimpleDevicesModel是否只有简单开关点击操作
	 * 
	 * @return
	 */

	private boolean isSimpleOnOffDevice() {
		return DataHelper.IAS_ZONE_DEVICETYPE == mSimpleDevicesModel
				.getmDeviceId()
				|| DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE == mSimpleDevicesModel
						.getmDeviceId()
				|| DataHelper.ON_OFF_OUTPUT_DEVICETYPE == mSimpleDevicesModel
						.getmDeviceId()
				|| DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE == mSimpleDevicesModel
						.getmDeviceId()
				|| DataHelper.MAINS_POWER_OUTLET_DEVICETYPE == mSimpleDevicesModel
						.getmDeviceId()
				|| mSimpleDevicesModel.getmModelId().indexOf(
						DataHelper.Doors_and_windows_sensor_switch) == 0;
	}

	public void initList() {
		// TODO Auto-generated method stub

		new intialDataTask().execute(0);
		
		if (null != mBaseAdapter) {
			int m = mBaseAdapter.getCount();
			mDeviceList = new ArrayList<SimpleDevicesModel>();
			for (int i = 0; i < m; i++) {
				SimpleDevicesModel sd = (SimpleDevicesModel) mBaseAdapter
						.getItem(i);
				mDeviceList.add(sd);
			}
		}
		if (null != mPullToRefreshListView) {
			setListeners();
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

	public void setLayout() {
		LayoutParams mLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		list_root.setLayoutParams(mLayoutParams);
	}

	public interface deleteDevicesFromGroup {
		public void deleteDevices(SimpleDevicesModel sd);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		mSimpleDevicesModel = mRefreshData.getDeviceModle(position - 1);
		int menuIndex = item.getItemId();
		Log.i(TAG, "tagzgs-> menuInfo.position=" + position
				+ " item.getItemId()" + item.getItemId());
		if (type == WITH_OPERATE) {
			if (1 == menuIndex) {
				mRefreshData.setDevicesId(mSimpleDevicesModel);
				MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
						(Context) getActivity());
				mMyOkCancleDlg.setDialogCallback((Dialogcallback) mRefreshData);
				mMyOkCancleDlg.setContent("确定要从此区域删除"
						+ mSimpleDevicesModel.getmUserDefineName().trim()
						+ "吗?");
				mMyOkCancleDlg.show();
			}
		} else if (type == WITHOUT_OPERATE) {
			if (1 == menuIndex) {
				mRefreshData.setDevicesId(mSimpleDevicesModel);
				MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
						(Context) getActivity());
				mMyOkCancleDlg.setDialogCallback((Dialogcallback) mRefreshData);
				mMyOkCancleDlg.setContent("确定要从此场景删除"
						+ mSimpleDevicesModel.getmUserDefineName().trim()
						+ "吗?");
				mMyOkCancleDlg.show();
			}
		}

		return super.onContextItemSelected(item);
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.i(TAG, "zzz->onAttach");
		if (!(activity instanceof refreshData)) {
			throw new IllegalStateException("Activity必须实现refreshData接口");
		}
		mRefreshData = (refreshData) activity;
		setDataActivity = (setData) activity;
	}

	public interface refreshData {
		public void refreshListData();

		public SimpleDevicesModel getDeviceModle(int postion);

		public void setFragment(Fragment mFragment, int postion);

		public void setDevicesId(SimpleDevicesModel simpleDevicesModel);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mRefreshData = null;
		CGIManager.getInstance().deleteObserver(DevicesListFragment.this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		setLayout();
		super.onResume();
	}

	@Override
	public void setAdapter(BaseAdapter mAdapter) {
		// TODO Auto-generated method stub
		mBaseAdapter = null;
		mBaseAdapter = mAdapter;
		initList();
	}

	@Override
	public void setSelectedPostion(int postion) {
		// TODO Auto-generated method stub
		mPullToRefreshListView.getRefreshableView().setSelection(postion);
	}

	@Override
	public void stopRefresh() {
		// TODO Auto-generated method stub
		mPullToRefreshListView.onRefreshComplete();
		refreshTag = 0;
	}

	public int isInList(String iee, String ep) {

		if (null == mDeviceList || mDeviceList.size() == 0) {
			return -1;
		}
		if (iee == null || ep == null) {
			return -1;
		}
		SimpleDevicesModel sd;
		for (int m = 0; m < mDeviceList.size(); m++) {
			sd = mDeviceList.get(m);
			if (iee.trim().equals(sd.getmIeee().trim())
					&& ep.trim().equals(sd.getmEP().trim())) {
				return m;
			}
		}
		return -1;
	}

	public interface setData {
		public void setdata(List<SimpleDevicesModel> list);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		if (EventType.ON_OFF_STATUS == event.getType()) {
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
//								setDataActivity.setdata(mDeviceList);
								mBaseAdapter.notifyDataSetChanged();
							}
						});
					}
				}
				ProcessUpdate(data);
			} else {
				// if failed,prompt a Toast
				// mError.setVisibility(View.VISIBLE);
			}
		}
	}
	

//	public class updateOneKeyOperatorDevices extends
//			AsyncTask<Boolean, Integer, Integer> {
//		
//		@Override
//		protected Integer doInBackground(Boolean... params) {
//			// TODO Auto-generated method stub
//			boolean on = params[0];
//			String on_off = on ? "1" : "0";
//			ContentValues c = new ContentValues();
//			c.put(DevicesModel.ON_OFF_STATUS, on_off);
//			mUpdateDevices.updateDevices(onekeyopratorModel, c);
//
//			return 1;
//		}
//
//	}

	class intialDataTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			DataHelper dh = new DataHelper(ApplicationController.getInstance());
			SQLiteDatabase db = dh.getSQLiteDatabase();
			totalControlDevice = DataUtil.getDeviceModelByModelid(
					DataHelper.One_key_operator, dh, db);
			db.close();
			return null;
		}
	}

	@Override
	public void onFragmentResult(int requsetId, boolean result, Object data) {
		String status = (String) data;
		totalControlDevice.setmOnOffStatus(status);
	}

}
