package com.gdgl.activity;

import java.util.HashMap;
import java.util.List;

import com.gdgl.manager.Manger;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
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
import android.util.Log;
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
/***
 * 组网后获得的新设备列表
 * @author Trice
 *
 */
public class JoinNetDevicesListFragment extends BaseFragment {

	private View mView;

	LinearLayout join_net_listLayout;
	PullToRefreshListView devices_list;
	int refreshTag = 0;
	refreshData mRefreshData;

	List<DevicesModel> mDevList;
	DataHelper mDH;
	JoinNetAdapter mJoinNetAdapter;

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
		mJoinNetAdapter = new JoinNetAdapter();
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
		if (null != mJoinNetAdapter) {
			mJoinNetAdapter.setList(mDevList);
			mJoinNetAdapter.notifyDataSetChanged();
		}
	}

//	public boolean isInNet(DevicesModel s) {
//
//		for (SimpleDevicesModel sd : mInnetList) {
//			if (sd.getmIeee().equals(s.getmIeee())
//					&& sd.getmEP().equals(s.getmEP())) {
//				return true;
//			}
//		}
//		return false;
//	}

//	protected SimpleDevicesModel getDevicesByIeee(String iee, String EP) {
//		// TODO Auto-generated method stub
//		String[] args = { iee, EP };
//		String where = " ieee=? and ep=? ";
//		Context c = (Context) getActivity();
//		List<SimpleDevicesModel> ls = DataUtil.getDevices(c, new DataHelper(c),
//				args, where);
//		SimpleDevicesModel smd = null;
//		if (null != ls && ls.size() > 0) {
//			smd = ls.get(0);
//		}
//		return smd;
//	}

	private void initView() {
		// TODO Auto-generated method stub
		join_net_listLayout = (LinearLayout) mView.findViewById(R.id.join_net_list);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		join_net_listLayout.setLayoutParams(mLayoutParams);
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
			mJoinNetAdapter.setList(mDevList);
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

//	public int isInList(String iee, String ep) {
//		if (null == mDevList || mDevList.size() == 0) {
//			return -1;
//		}
//		if (iee == null || ep == null) {
//			return -1;
//		}
//		DevicesModel sd;
//		for (int m = 0; m < mDevList.size(); m++) {
//			sd = mDevList.get(m);
//			if (iee.trim().equals(sd.getmIeee().trim())
//					&& ep.trim().equals(sd.getmEP().trim())) {
//				return m;
//			}
//		}
//		return -1;
//	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
//
//		final Event event = (Event) object;
//		if (EventType.ON_OFF_STATUS == event.getType()) {
//			if (event.isSuccess() == true) {
//				// data maybe null
//				CallbackResponseType2 data = (CallbackResponseType2) event
//						.getData();
//				int m = isInList(data.getDeviceIeee(), data.getDeviceEp());
//				if (-1 != m) {
//					if (null != data.getValue()) {
//						mDevList.get(m).setmOnOffStatus(data.getValue());
//						mJoinNetAdapter.setList(mDevList);
//						mView.post(new Runnable() {
//
//							@Override
//							public void run() {
//								mJoinNetAdapter.notifyDataSetChanged();
//							}
//						});
//					}
//				}
//				ProcessUpdate(data);
//			} else {
//				// if failed,prompt a Toast
//				// mError.setVisibility(View.VISIBLE);
//			}
//		}
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

//	public DevicesModel getDevicesByIeeeAndEp(String ieee, String ep) {
//		if (null == ieee || ep == null || null == mDevList) {
//			return null;
//		}
//
//		for (DevicesModel ds : mDevList) {
//			if (ds.getmIeee().trim().equals(ieee.trim())
//					&& ds.getmEP().trim().equals(ep.trim())) {
//				return ds;
//			}
//		}
//		return null;
//	}

	public class JoinNetAdapter extends BaseAdapter {

		List<DevicesModel> mdev;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mdev && mdev.size() > 0) {
				return mdev.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mdev && mdev.size() > 0) {
				return mdev.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mdev && mdev.size() > 0) {
				return mdev.get(position).getID();
			}
			return position;
		}

		public void setList(List<DevicesModel> ml) {
			mdev = null;
			mdev = ml;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (null == mdev) {
				return convertView;
			}
			View mView = convertView;
			ViewHolder mHolder;
			final DevicesModel mDevices = mdev.get(position);

			if (null == mView) {
				mHolder = new ViewHolder();
				mView = LayoutInflater.from((Context) getActivity()).inflate(
						R.layout.devices_list_item_nooperator, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mHolder.devices_region = (TextView) mView
						.findViewById(R.id.devices_region);
				mHolder.devices_state = (TextView) mView
						.findViewById(R.id.devices_state);
				mView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) mView.getTag();
			}
			Log.e("devices_name", mDevices.getmUserDefineName());
			mHolder.devices_name.setText(mDevices.getmUserDefineName().replace(
					" ", ""));
			
			int devModeleId = Integer.parseInt(mDevices.getmDeviceId());

			if (DataHelper.IAS_ZONE_DEVICETYPE == devModeleId
					|| DataHelper.IAS_ACE_DEVICETYPE == devModeleId) {

				mHolder.devices_img.setImageResource(UiUtils
						.getDevicesSmallIconByModelId(mDevices.getmModelId()
								.trim()));
			} else if (mDevices.getmModelId().indexOf(
					DataHelper.Multi_key_remote_control) == 0) {
				mHolder.devices_img.setImageResource(UiUtils
						.getDevicesSmallIconForRemote(devModeleId));
			} else {
				mHolder.devices_img.setImageResource(UiUtils
						.getDevicesSmallIcon(devModeleId));
			}
			
			mHolder.devices_region.setVisibility(View.INVISIBLE);
			mHolder.devices_state.setVisibility(View.INVISIBLE);

			return mView;
		}

		public class ViewHolder {
			ImageView devices_img;
			TextView devices_name;
			TextView devices_region;
			TextView devices_state;
		}
	}
}
