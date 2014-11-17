package com.gdgl.activity;

import java.util.List;

import com.gdgl.activity.DevicesListFragment.refreshData;
import com.gdgl.manager.Manger;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.smarthome.R;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class AllDevicesListFragment extends BaseFragment {

	private View mView;
	ListView devices_list;

	int refreshTag = 0;

	DevicesAdapter mDevicesAdapter;
	private refreshData mRefreshData;
	LinearLayout list_root;
	List<DevicesModel> mDevicesList;
	LinearLayout deviceslist;
	ViewGroup no_dev;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initdata();
	}

	private void initdata() {
		// TODO Auto-generated method stub
		Context c = (Context) getActivity();
		DataHelper dh = new DataHelper(c);

		mDevicesList = DataUtil.getDevices(c, dh, null, null, true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.all_devices_list, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		list_root = (LinearLayout) mView.findViewById(R.id.list_root);
		setLayout();
		devices_list = (ListView) mView.findViewById(R.id.devices_list);
		no_dev = (ViewGroup) mView.findViewById(R.id.no_dev);
		deviceslist = (LinearLayout) mView.findViewById(R.id.deviceslist);
		if (null == mDevicesList || mDevicesList.size() == 0) {
			no_dev.setVisibility(View.VISIBLE);
			deviceslist.setVisibility(View.GONE);
		} else {
			DevicesAdapter mDevicesAdapter=new DevicesAdapter();
			mDevicesAdapter.setList(mDevicesList);
			devices_list.setAdapter(mDevicesAdapter);
		}

	}

	public void setLayout() {
		LayoutParams mLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		list_root.setLayoutParams(mLayoutParams);
	}

	@Override
	public void stopRefresh() {
		// TODO Auto-generated method stub
		// devices_list.onRefreshComplete();
		refreshTag = 0;
	}

	public class DevicesAdapter extends BaseAdapter {
		private List<DevicesModel> mDevList;
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mDevList) {
				return mDevList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mDevList) {
				return mDevList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mDevList) {
				return (long) mDevList.get(position).getID();
			}
			return position;
		}
		
		public void setList(List<DevicesModel> list){
			mDevList=null;
			mDevList=list;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (null == mDevList) {
				return convertView;
			}
			View mView = convertView;
			ViewHolder mHolder;
			final DevicesModel mDevices = mDevList.get(position);

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

			mHolder.devices_name.setText(mDevices.getmDefaultDeviceName().replace(
					" ", ""));
			mHolder.devices_region.setText(mDevices.getmDeviceRegion().replace(
					" ", ""));
			
			mHolder.devices_img.setImageResource(DataUtil
					.getDefaultDevicesSmallIcon(mDevices.getmDeviceId(),mDevices.getmModelId().trim()));
//			if (DataHelper.IAS_ZONE_DEVICETYPE == mDevices.getmDeviceId()
//					|| DataHelper.IAS_ACE_DEVICETYPE == mDevices.getmDeviceId()) {
//
//				mHolder.devices_img.setImageResource(UiUtils
//						.getDevicesSmallIconByModelId(mDevices.getmModelId()
//								.trim()));
//			} else if (mDevices.getmModelId().indexOf(
//					DataHelper.Multi_key_remote_control) == 0) {
//				mHolder.devices_img.setImageResource(UiUtils
//						.getDevicesSmallIconForRemote(mDevices.getmDeviceId()));
//			} else {
//				mHolder.devices_img.setImageResource(UiUtils
//						.getDevicesSmallIcon(mDevices.getmDeviceId()));
//			}

			if (mDevices.getmDeviceId() == DataHelper.ON_OFF_SWITCH_DEVICETYPE) {
				String state = "";
				String s = mDevices.getmOnOffStatus();
				Log.i("tag", "tag->" + s);
				String[] result = s.split(",");
				for (String string : result) {
					if (string.trim().equals("1")) {
						state += "开 ";
					} else {
						state += "关 ";
					}
				}
				Log.i("tag", "tag->" + state);
				mHolder.devices_state.setText(state);
			} else if (mDevices.getmDeviceId() == DataHelper.IAS_ZONE_DEVICETYPE) {
				if (mDevices.getmOnOffStatus().trim().equals("0")) {
					mHolder.devices_state.setText("已布防");
				} else {
					mHolder.devices_state.setText("已撤防");
				}
			} else if (mDevices.getmDeviceId() == DataHelper.LIGHT_SENSOR_DEVICETYPE) {
				 mHolder.devices_state.setText("亮度: "+getFromSharedPreferences.getLight());
			} else if (mDevices.getmDeviceId() == DataHelper.TEMPTURE_SENSOR_DEVICETYPE) {
	            mHolder.devices_state.setText("温度: "+getFromSharedPreferences.getTemperature()+"\n湿度: "+getFromSharedPreferences.getHumidity()+"%");

			} else {
				if (mDevices.getmOnOffStatus().trim().equals("1")) {
					mHolder.devices_state.setText("开");
				} else {
					mHolder.devices_state.setText("关");
				}
			}

			return mView;
		}

		public class ViewHolder {
			ImageView devices_img;
			TextView devices_name;
			TextView devices_region;
			TextView devices_state;
		}
	}
	
	public int isInList(String iee,String ep){
		if(null==mDevicesList || mDevicesList.size()==0){
			return -1;
		}
		if(iee==null || ep==null){
			return -1;
		}
		DevicesModel sd;
		for(int m=0;m<mDevicesList.size();m++){
			sd=mDevicesList.get(m);
			if(iee.trim().equals(sd.getmIeee().trim()) && ep.trim().equals(sd.getmEP().trim())){
				return m;
			}
		}
		return -1;
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
				int m=isInList(data.getDeviceIeee(), data.getDeviceEp());
				if(-1!=m){
					if(null!=data.getValue()){
						mDevicesList.get(m).setmOnOffStatus(data.getValue());
						mDevicesAdapter.setList(mDevicesList);
						mView.post(new Runnable() {
							@Override
							public void run() {
								mDevicesAdapter.notifyDataSetChanged();
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
//		else if (EventType.WARM == event.getType()) {
//			if (event.isSuccess() == true) {
//				// data maybe null
//				final CallbackWarmMessage data = (CallbackWarmMessage) event
//						.getData();
////				if (data.getCie_ieee() == mDevices.getmIeee()) {
//					mView.post(new Runnable() {
//						@Override
//						public void run() {
//							
//						}
//					});
////				}
//			}
//		}
	}
}
