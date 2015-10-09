package com.gdgl.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataUtil;
import com.gdgl.smarthome.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LinkageDevicesAddFragment extends Fragment {

	private int AddType;
	private View mView;
	// PullToRefreshListView devices_list_view;
	ListView devices_list_view;
	public List<DevicesModel> mLinkageDevicesAddList;

	int refreshTag = 0;

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
		Bundle bundle = getArguments();
		AddType = bundle.getInt(LinkageDetailActivity.TYPE, 1);
		Log.i("AddType", "" + AddType);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub

		devices_list_view = (ListView) mView.findViewById(R.id.devices_list);
		devices_list_view.setAdapter(mBaseAdapter);
		devices_list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				LinkageDetailFragment.show_cho = 1;//==========等于1表示是从设备列表中进行选择====王==0811
				if(AddType == 1){
					mAddChecked.AddTrgCheckedDevices(mLinkageDevicesAddList.get(arg2));
				}
				if(AddType == 2){
					mAddChecked.AddActCheckedDevices(mLinkageDevicesAddList.get(arg2));
				}	
				getActivity().onBackPressed();
			}
		});

	}
	
	public void setAddChecked(AddChecked mAddChecked){
		this.mAddChecked = mAddChecked;
	}

	public class LinkageDevicesAddListAdapter extends BaseAdapter {

		protected AddChecked mDevicesObserver;

		private Map<Integer, Boolean> isSelected;

		public LinkageDevicesAddListAdapter(List<DevicesModel> list,
				AddChecked mObserver) {
			mLinkageDevicesAddList = list;
			mDevicesObserver = mObserver;
			isSelected = new HashMap<Integer, Boolean>();
			for (int i = 0; i < mLinkageDevicesAddList.size(); i++) {
				isSelected.put(i, true);
			}
		}
		
		public LinkageDevicesAddListAdapter(List<DevicesModel> list){
			mLinkageDevicesAddList = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mLinkageDevicesAddList) {
				return mLinkageDevicesAddList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mLinkageDevicesAddList) {
				return mLinkageDevicesAddList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mLinkageDevicesAddList) {
				return (long) mLinkageDevicesAddList.get(position).getID();
			}
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View mView = convertView;
			final ViewHolder mHolder;
			final DevicesModel mDevices = mLinkageDevicesAddList.get(position);

			if (null == mView) {
				mHolder = new ViewHolder();

				mView = LayoutInflater.from(getActivity()).inflate(
						R.layout.scene_device_select_item, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mView.setTag(mHolder);

			} else {
				mHolder = (ViewHolder) mView.getTag();
			}

			mHolder.devices_name.setText(mDevices.getmDefaultDeviceName());

			if(mDevices.getmDevicePriority() == 1000){
				mHolder.devices_img.setImageResource(R.drawable.ui2_device_video);
			}else{	
				mHolder.devices_img.setImageResource(DataUtil
						.getDefaultDevicesSmallIcon(mDevices.getmDeviceId(),
								mDevices.getmModelId().trim()));
			}
			
			return mView;
		}

		public class ViewHolder {
			public ImageView devices_img;
			public TextView devices_name;
		}

		public void setList(List<DevicesModel> list) {
			mLinkageDevicesAddList = null;
			mLinkageDevicesAddList = list;
		}
	}

	public void setAdapter(BaseAdapter mAdapter) {
		// TODO Auto-generated method stub
		mBaseAdapter = null;
		mBaseAdapter = mAdapter;
	}

	public interface AddChecked {
		public void AddActCheckedDevices(DevicesModel mDevices);
		public void AddTrgCheckedDevices(DevicesModel mDevices);
	}

	public interface ChangeFragment {
		public void setFragment(Fragment f);
	}
}
