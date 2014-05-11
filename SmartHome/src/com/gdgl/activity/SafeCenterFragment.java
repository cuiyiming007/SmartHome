package com.gdgl.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdgl.activity.JoinNetDevicesListFragment.refreshData;
import com.gdgl.activity.SafeCenterFragment.SafeAdapter.ViewHolder;
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
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SafeCenterFragment extends BaseFragment implements UIListener {

	private View mView;

	LinearLayout ch_pwd;
	PullToRefreshListView devices_list;
	int refreshTag = 0;
	refreshData mRefreshData;

	List<SimpleDevicesModel> mList;
	List<SimpleDevicesModel> mOperatorList;
	DataHelper mDH;

	HashMap<SimpleDevicesModel, View> mViewGroupMap;
	HashMap<View, SimpleDevicesModel> mViewToIees;
	ViewGroup no_dev;
	TextView mNoContent;
	LinearLayout deviceslist;
	boolean isSelectedAll = false;

	SafeAdapter mAdapter;
	Button SelectAll, AllOn, AllOff;

	public static final int FINISH_OPERATOR = 1;
	public static final int SUCESS = 2;

	LightManager mLightManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initdata();
	}

	private void initdata() {
		// TODO Auto-generated method stub
		Context c = (Context) getActivity();
		mDH = new DataHelper(c);

		mViewGroupMap = new HashMap<SimpleDevicesModel, View>();
		mViewToIees = new HashMap<View, SimpleDevicesModel>();
		mOperatorList = new ArrayList<SimpleDevicesModel>();
		mList = DataUtil.getOtherManagementDevices(c, mDH,
				UiUtils.SECURITY_CONTROL);

		mAdapter = new SafeAdapter(c);
		mAdapter.setList(mList);
		mLightManager = LightManager.getInstance();
		mLightManager.addObserver(SafeCenterFragment.this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.safe_center, null);
		initView();
		return mView;
	}

	protected SimpleDevicesModel getDevicesByIeee(String iee, String EP) {
		// TODO Auto-generated method stub
		String[] args = { iee, EP };
		String where = " ieee=? and ep=? ";
		Context c = (Context) getActivity();
		List<SimpleDevicesModel> ls = DataUtil.getDevices(c, new DataHelper(c),
				args, where);
		SimpleDevicesModel smd = null;
		if (null != ls && ls.size() > 0) {
			smd = ls.get(0);
		}
		return smd;
	}

	private void initView() {
		// TODO Auto-generated method stub
		ch_pwd = (LinearLayout) mView.findViewById(R.id.ch_pwd);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		deviceslist = (LinearLayout) mView.findViewById(R.id.deviceslist);

		ch_pwd.setLayoutParams(mLayoutParams);

		no_dev = (ViewGroup) mView.findViewById(R.id.no_dev);

		mNoContent = (TextView) no_dev.findViewById(R.id.text_content);
		mNoContent.setText("此项暂无任何内容");

		devices_list = (PullToRefreshListView) mView
				.findViewById(R.id.devices_list);

		if (null == mList || mList.size() == 0) {
			no_dev.setVisibility(View.VISIBLE);
			deviceslist.setVisibility(View.GONE);
		} else {
			devices_list
					.setOnRefreshListener(new OnRefreshListener<ListView>() {

						@Override
						public void onRefresh(
								PullToRefreshBase<ListView> refreshView) {
							// TODO Auto-generated method stub
							if (1 == refreshTag) {
							} else {
								refreshTag = 1;
								String label = DateUtils.formatDateTime(
										getActivity(),
										System.currentTimeMillis(),
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
			devices_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					ViewHolder mViewHolder = (ViewHolder) view.getTag();
					boolean b = mViewHolder.selected.isChecked();

					SimpleDevicesModel sd = mList.get(position - 1);
					if (b) {
						if (mOperatorList.contains(sd)) {
							mOperatorList.remove(sd);
						}
					} else {
						if (!mOperatorList.contains(sd)) {
							mOperatorList.add(sd);
						}
					}
					SafeAdapter.getIsSelected().put(position - 1, !b);
					mViewHolder.selected.toggle();
				}
			});
			devices_list.setAdapter(mAdapter);
		}

		SelectAll = (Button) mView.findViewById(R.id.select_all);
		AllOn = (Button) mView.findViewById(R.id.bufang);
		AllOff = (Button) mView.findViewById(R.id.chefang);

		SelectAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button btn = (Button) v;
				if (!isSelectedAll) {
					for (int i = 0; i < mList.size(); i++) {
						if (!SafeAdapter.getIsSelected().get(i)) {
							SafeAdapter.getIsSelected().put(i, true);
						}
					}
					btn.setText("取消全选");
					mOperatorList.clear();
					for (SimpleDevicesModel sd : mList) {
						mOperatorList.add(sd);
					}
				} else {
					for (int i = 0; i < mList.size(); i++) {
						if (SafeAdapter.getIsSelected().get(i)) {
							SafeAdapter.getIsSelected().put(i, false);
						}
					}
					mOperatorList.clear();
					btn.setText("全选");
				}
				isSelectedAll = !isSelectedAll;
				dataChanged();
			}
		});

		AllOn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLightManager.LocalIASCIEOperation(null, 6);
				AllOn.setEnabled(false);
				AllOff.setEnabled(false);
			}
		});

		AllOff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLightManager.LocalIASCIEOperation(null, 7);
				AllOn.setEnabled(false);
				AllOff.setEnabled(false);
			}
		});
	}

	public void updatedata(boolean on) {
		String on_off = on ? "1" : "0";
		for (SimpleDevicesModel sdm : mList) {
			sdm.setmOnOffStatus(on_off);
		}
		mAdapter.setList(mList);
		mAdapter.notifyDataSetChanged();
		Context c = (Context) getActivity();
		DataHelper dh = new DataHelper(c);
		String where = " ieee=? and ep=? ";

		ContentValues cv;
		for (SimpleDevicesModel sdm : mList) {
			cv = new ContentValues();
			String[] args = { sdm.getmIeee(), sdm.getmEP() };
			cv.put(DevicesModel.ON_OFF_STATUS, on_off);
			dh.update(dh.getReadableDatabase(), DataHelper.DEVICES_TABLE, cv,
					where, args);
		}
		AllOn.setEnabled(true);
		AllOff.setEnabled(true);
	}

	private void dataChanged() {
		// 通知listView刷新
		mAdapter.notifyDataSetChanged();
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLightManager.deleteObserver(SafeCenterFragment.this);
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof refreshData)) {
			throw new IllegalStateException("Activity必须实现refreshData接口");
		}
		mRefreshData = (refreshData) activity;
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub

		final Event event = (Event) object;
		if (EventType.LOCALIASCIEOPERATION == event.getType()) {
			if (event.isSuccess() == true) {
				int data = (Integer) event.getData();
				if (data == 6) {
					updatedata(true);
				} else if (data == 7) {
					updatedata(false);
				}
			}
		}
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			Button btn = (Button) msg.obj;
			switch (what) {
			case FINISH_OPERATOR:
				btn.setEnabled(true);
				break;
			case SUCESS:
				btn.setEnabled(true);
				break;
			default:
				break;
			}
		};
	};

	public static class SafeAdapter extends BaseAdapter {
		private static HashMap<Integer, Boolean> isSelected;
		private List<SimpleDevicesModel> mAdapterList;
		private Context mContext;

		public SafeAdapter(Context c) {
			// mAdapterList = list;
			mContext = c;
			isSelected = new HashMap<Integer, Boolean>();
			if (null != mAdapterList && mAdapterList.size() > 0) {
				for (int i = 0; i < mAdapterList.size(); i++) {
					getIsSelected().put(i, false);
				}
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mAdapterList && mAdapterList.size() > 0) {
				return mAdapterList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mAdapterList && mAdapterList.size() > 0) {
				return mAdapterList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mAdapterList && mAdapterList.size() > 0) {
				return mAdapterList.get(position).getID();
			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final SimpleDevicesModel sd = (SimpleDevicesModel) getItem(position);
			ViewHolder mViewHolder;

			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.all_devices_item, null);
				mViewHolder.devImg = (ImageView) convertView
						.findViewById(R.id.devices_img);
				mViewHolder.devName = (TextView) convertView
						.findViewById(R.id.devices_name);
				mViewHolder.devState = (TextView) convertView
						.findViewById(R.id.devices_state);
				mViewHolder.selected = (CheckBox) convertView
						.findViewById(R.id.selected);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			int devModeleId = sd.getmDeviceId();

			if (DataHelper.IAS_ZONE_DEVICETYPE == devModeleId
					|| DataHelper.IAS_ACE_DEVICETYPE == devModeleId) {

				mViewHolder.devImg.setImageResource(UiUtils
						.getDevicesSmallIconByModelId(sd.getmModelId().trim()));
			} else if (sd.getmModelId().indexOf(
					DataHelper.Multi_key_remote_control) == 0) {
				mViewHolder.devImg.setImageResource(UiUtils
						.getDevicesSmallIconForRemote(devModeleId));
			} else {
				mViewHolder.devImg.setImageResource(UiUtils
						.getDevicesSmallIcon(devModeleId));
			}
			mViewHolder.devName.setText(sd.getmUserDefineName());

			if (sd.getmDeviceId() == DataHelper.ON_OFF_SWITCH_DEVICETYPE) {
				String state = "";
				String s = sd.getmOnOffStatus();
				String[] result = s.split(",");
				for (String string : result) {
					if (string.trim().equals("1")) {
						state += "开 ";
					} else {
						state += "关 ";
					}
				}
				mViewHolder.devState.setText(state);
			} else if (sd.getmDeviceId() == DataHelper.IAS_ZONE_DEVICETYPE) {
				if (sd.getmOnOffStatus().trim().equals("1")) {
					mViewHolder.devState.setText("布防");
				} else {
					mViewHolder.devState.setText("撤防");
				}
			} else if (sd.getmDeviceId() == DataHelper.LIGHT_SENSOR_DEVICETYPE) {
				mViewHolder.devState.setText("当前室内亮度为: 30");
			} else if (sd.getmDeviceId() == DataHelper.TEMPTURE_SENSOR_DEVICETYPE) {
				mViewHolder.devState.setText("当前室内温度为: 30°C");
			} else {
				if (sd.getmOnOffStatus().trim().equals("1")) {
					mViewHolder.devState.setText("开");
				} else {
					mViewHolder.devState.setText("关");
				}
			}
			mViewHolder.selected.setChecked(getIsSelected().get(position));
			return convertView;
		}

		public static HashMap<Integer, Boolean> getIsSelected() {
			return isSelected;
		}

		public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
			isSelected = isSelected;
		}

		public void setList(List<SimpleDevicesModel> list) {
			mAdapterList = null;
			mAdapterList = list;
		}

		public class ViewHolder {
			public ImageView devImg;
			public TextView devName;
			public TextView devState;
			public CheckBox selected;
		}
	}

	@Override
	public void stopRefresh() {
		// TODO Auto-generated method stub
		devices_list.onRefreshComplete();
		refreshTag = 0;
	}
}
