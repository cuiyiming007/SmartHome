package com.gdgl.activity;

import java.util.HashMap;
import java.util.List;

import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class JoinNetDevicesListFragment extends BaseFragment implements
		UIListener {

	private View mView;

	LinearLayout ch_pwd;
	PullToRefreshListView devices_list;
	int refreshTag = 0;
	refreshData mRefreshData;

	List<SimpleDevicesModel> mInnetList;
	List<DevicesModel> mDevList;
	// List<SimpleDevicesModel> mDevList;
	DataHelper mDH;
	JoinNetAdapter mJoinNetAdapter;

	HashMap<DevicesModel, View> mViewGroupMap;
	HashMap<View, DevicesModel> mViewToIees;
	ViewGroup no_dev;
	Button mBack;
	TextView mNoContent;

	LinearLayout deviceslist;

	public static final int FINISH_OPERATOR = 1;
	public static final int SUCESS = 2;

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
		mInnetList = DataUtil.getDevices(c, mDH, null, null);
		mViewGroupMap = new HashMap<DevicesModel, View>();
		mViewToIees = new HashMap<View, DevicesModel>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.join_net_list, null);
		initView();
		return mView;
	}

	public void setList(List<DevicesModel> list) {
		mDevList = list;
		mJoinNetAdapter.notifyDataSetChanged();
	}

	public boolean isInNet(DevicesModel s) {

		for (SimpleDevicesModel sd : mInnetList) {
			if (sd.getmIeee().equals(s.getmIeee())
					&& sd.getmEP().equals(s.getmEP())) {
				return true;
			}
		}
		return false;
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

		ch_pwd.setLayoutParams(mLayoutParams);
		mJoinNetAdapter = new JoinNetAdapter();
		no_dev = (ViewGroup) mView.findViewById(R.id.no_dev);

		deviceslist = (LinearLayout) mView.findViewById(R.id.deviceslist);

		mNoContent = (TextView) no_dev.findViewById(R.id.text_content);
		mNoContent.setText("未扫描到新设备");

		mBack = (Button) mView.findViewById(R.id.back);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getActivity().getFragmentManager();
				fm.popBackStack();
			}
		});

		devices_list = (PullToRefreshListView) mView
				.findViewById(R.id.devices_list);

		if (null == mDevList || mDevList.size() == 0) {
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
			devices_list.setAdapter(mJoinNetAdapter);
		}

	}

	@Override
	public void stopRefresh() {
		devices_list.onRefreshComplete();
		refreshTag = 0;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
		// if (EventType.MODIFYPASSWORD == event.getType()) {
		// }
		// getDevicesByIeee add to mViewToIees
		// 若成功则Handler SUCESS,然后，设备入库或出库
	}

	public interface refreshData {
		public void refreshListData();

		public SimpleDevicesModel getDeviceModle(int postion);

		public void setFragment(Fragment mFragment, int postion);

		public void setDevicesId(String id);
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

	public DevicesModel getDevicesByIeeeAndEp(String ieee, String ep) {
		if (null == ieee || ep == null || null == mDevList) {
			return null;
		}

		for (DevicesModel ds : mDevList) {
			if (ds.getmIeee().trim().equals(ieee.trim())
					&& ds.getmEP().trim().equals(ep.trim())) {
				return ds;
			}
		}
		return null;
	}

	public class JoinNetAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mDevList && mDevList.size() > 0) {
				return mDevList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mDevList && mDevList.size() > 0) {
				return mDevList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mDevList && mDevList.size() > 0) {
				return mDevList.get(position).getID();
			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final DevicesModel sd = (DevicesModel) getItem(position);
			ViewHolder mViewHolder;
			final boolean isInner = isInNet(sd);
			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.join_net_item, null);
				mViewHolder.devImg = (ImageView) convertView
						.findViewById(R.id.dev_img);
				mViewHolder.devName = (TextView) convertView
						.findViewById(R.id.dev_name);
				mViewHolder.btnEnter = (Button) convertView
						.findViewById(R.id.join);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			int devModeleId = Integer.parseInt(sd.getmDeviceId());
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
			if (isInner) {
				mViewHolder.btnEnter.setText("出网");
			} else {
				mViewHolder.btnEnter.setText("入网");
			}

			mViewHolder.btnEnter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Button btn = (Button) v;
					mViewGroupMap.put(sd, btn);
					btn.setEnabled(false);
					if (isInner) {
						// 出网
					} else {
						// 入网
					}
					mHandler.sendEmptyMessageDelayed(FINISH_OPERATOR, 3000);
				}
			});

			return convertView;
		}

		class ViewHolder {
			public ImageView devImg;
			public TextView devName;
			public Button btnEnter;
		}
	}
}
