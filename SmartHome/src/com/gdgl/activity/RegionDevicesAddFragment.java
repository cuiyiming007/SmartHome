package com.gdgl.activity;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.CheckBox;
import com.gc.materialdesign.views.CheckBox.OnCheckListener;
import com.gdgl.libjingle.LibjingleResponseHandlerManager;
import com.gdgl.libjingle.LibjingleSendManager;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.RfCGIManager;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;

public class RegionDevicesAddFragment extends Fragment implements UIListener {
	private View mView;
	// PullToRefreshListView devices_list_view;
	ListView devices_list_view;
	public List<DevicesModel> mRegionDevicesAddList;

	RegionDevicesAddListAdapter mBaseAdapter;

	AddChecked mAddChecked;
	
	private Map<Integer, Boolean> isSelected;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		CGIManager.getInstance().addObserver(this);
		RfCGIManager.getInstance().addObserver(this);
		LibjingleResponseHandlerManager.getInstance().addObserver(this);
		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
			CGIManager.getInstance().GetEPByRoomIndex("-1");
		} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
			LibjingleSendManager.getInstance().GetEPByRoomIndex("-1");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.scene_devices_fragment, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub

		devices_list_view = (ListView) mView.findViewById(R.id.devices_list);
		devices_list_view.setAdapter(mBaseAdapter);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		CGIManager.getInstance().deleteObserver(this);
		RfCGIManager.getInstance().deleteObserver(this);
		LibjingleResponseHandlerManager.getInstance().deleteObserver(this);
	}
	
	public class RegionDevicesAddListAdapter extends BaseAdapter {

		protected AddChecked mDevicesObserver;

		public RegionDevicesAddListAdapter(List<DevicesModel> list,
				AddChecked mObserver) {
			mRegionDevicesAddList = list;
			mDevicesObserver = mObserver;
			isSelected = new HashMap<Integer, Boolean>();
			for (int i = 0; i < mRegionDevicesAddList.size(); i++) {
				isSelected.put(i, false);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mRegionDevicesAddList) {
				return mRegionDevicesAddList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mRegionDevicesAddList) {
				return mRegionDevicesAddList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mRegionDevicesAddList) {
				return (long) mRegionDevicesAddList.get(position).getID();
			}
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View mView = convertView;
			final ViewHolder mHolder;
			final DevicesModel mDevices = mRegionDevicesAddList.get(position);

			if (null == mView) {
				mHolder = new ViewHolder();

				mView = LayoutInflater.from(getActivity()).inflate(
						R.layout.scene_device_add_item, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mHolder.selected = (CheckBox) mView.findViewById(R.id.selected);
				mView.setTag(mHolder);

			} else {
				mHolder = (ViewHolder) mView.getTag();
			}

			mHolder.devices_name.setText(mDevices.getmDefaultDeviceName());

			mHolder.devices_img.setImageResource(DataUtil
					.getDefaultDevicesSmallIcon(mDevices.getmDeviceId(),
							mDevices.getmModelId().trim()));

			mHolder.selected.setOncheckListener(new OnCheckListener() {

				@Override
				public void onCheck(boolean check) {
					// TODO Auto-generated method stub
					isSelected.put(position, check);
					if (check) {
						mDevicesObserver.AddCheckedDevices(mDevices);
					} else {
						mDevicesObserver.DeletedCheckedDevices(mDevices);
					}
				}
			});
			mHolder.selected.setChecked(isSelected.get(position));
			return mView;
		}

		public class ViewHolder {
			public ImageView devices_img;
			public TextView devices_name;
			public CheckBox selected;
		}

		public void setList(List<DevicesModel> list) {
			mRegionDevicesAddList = null;
			mRegionDevicesAddList = list;
		}
	}

	public void setAdapter(BaseAdapter mAdapter) {
		// TODO Auto-generated method stub
		mBaseAdapter = null;
		mBaseAdapter = (RegionDevicesAddListAdapter)mAdapter;
	}

	public interface AddChecked {
		public void AddCheckedDevices(DevicesModel model);

		public void DeletedCheckedDevices(DevicesModel model);
	}
	
	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		if (EventType.GETEPBYROOMINDEX == event.getType()) {
			if (event.isSuccess() == true) {
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					RfCGIManager.getInstance().GetRFDevByRoomId("-1");
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					LibjingleSendManager.getInstance().GetRFDevByRoomId("-1");
				}
				mRegionDevicesAddList = (List<DevicesModel>) event.getData();
				Collections.sort(mRegionDevicesAddList, new Comparator<DevicesModel>() {

					@Override
					public int compare(DevicesModel lhs, DevicesModel rhs) {
						// TODO Auto-generated method stub
						return (lhs.getmDevicePriority()-rhs.getmDevicePriority());
					}
				});

			}
		} else if (EventType.RF_GETEPBYROOMINDEX == event.getType()) {
			if (event.isSuccess() == true) {
				List<DevicesModel> temp = (List<DevicesModel>) event.getData();
				Collections.sort(temp, new Comparator<DevicesModel>() {

					@Override
					public int compare(DevicesModel lhs, DevicesModel rhs) {
						// TODO Auto-generated method stub
						return (lhs.getmDevicePriority()-rhs.getmDevicePriority());
					}
				});
				mRegionDevicesAddList.addAll(temp);
				for (int i = 0; i < mRegionDevicesAddList.size(); i++) {
					isSelected.put(i, false);
				}
				mBaseAdapter.setList(mRegionDevicesAddList);
				mView.post(new Runnable() {
					@Override
					public void run() {
						mBaseAdapter.notifyDataSetChanged();
					}
				});

			}
		}
	}
}
