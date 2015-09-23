package com.gdgl.activity;

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
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataUtil;
import com.gdgl.smarthome.R;

public class RegionDevicesAddFragment extends Fragment {
	private View mView;
	// PullToRefreshListView devices_list_view;
	ListView devices_list_view;
	public List<DevicesModel> mRegionDevicesAddList;

	BaseAdapter mBaseAdapter;

	AddChecked mAddChecked;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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

	public class RegionDevicesAddListAdapter extends BaseAdapter {

		protected AddChecked mDevicesObserver;

		private Map<Integer, Boolean> isSelected;

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
		mBaseAdapter = mAdapter;
	}

	public interface AddChecked {
		public void AddCheckedDevices(DevicesModel model);

		public void DeletedCheckedDevices(DevicesModel model);
	}
}
