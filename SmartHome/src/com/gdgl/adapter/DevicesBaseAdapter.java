package com.gdgl.adapter;

import java.util.List;

import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DevicesBaseAdapter extends BaseAdapter {

	protected Context mContext;
	protected List<SimpleDevicesModel> mDevicesList;
	protected DevicesObserver mDevicesObserver;

	public static final String DEVICES_ID = "devices_id";
	public static final String BOLLEAN_ARRARY = "devices_state";
	public static final String DEVIVES_VALUE = "devices_value";

	public static final String PASS_OBJECT = "pass_object";

	public class SwitchModel {
		public String modelId;
		public String name;
		public String[] anotherName;
		public String[] ieee;
		public boolean[] state;

	}

	public DevicesBaseAdapter(Context c, List<SimpleDevicesModel> list,
			DevicesObserver mObserver) {
		mContext = c;
		mDevicesList = list;
		mDevicesObserver = mObserver;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (null != mDevicesList) {
			Log.i(DEVICES_ID,
					"zzzgs->mDevicesList.size()=" + mDevicesList.size());
			return mDevicesList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (null != mDevicesList) {
			return mDevicesList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if (null != mDevicesList) {
			return (long) mDevicesList.get(position).getID();
		}
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View mView = convertView;
		ViewHolder mHolder;
		final int mPostion = position;
		final SimpleDevicesModel mDevices = mDevicesList.get(position);
		// float mValue = mDevices.getDevicesValue();
		if (null == mView) {
			Log.i(DEVICES_ID, "zzzzzzz->getView() null == mView");
			mHolder = new ViewHolder();
			mView = LayoutInflater.from(mContext).inflate(
					R.layout.devices_list_item, null);
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
		mHolder.devices_name.setText(mDevices.getmName());
		mHolder.devices_region.setText(mDevices.getmDeviceRegion());

		mHolder.devices_img.setImageResource(UiUtils
				.getDevicesSmallIcon(mDevices.getmDeviceId()));


		if (mDevices.getmDeviceId() == DataHelper.ON_OFF_SWITCH) {
			String state ="";
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
			Log.i("tag", "tag->"+state);
			mHolder.devices_state.setText(state);
		}else{
			if(mDevices.getmOnOffStatus().trim().equals("1")){
				mHolder.devices_state.setText("开");
			}else{
				mHolder.devices_state.setText("关");
			}
		}
		if (DataHelper.LIGHT_SENSOR == mDevices.getmDeviceId()) {
			boolean b = false;
			if (null != mDevices.getmOnOffStatus()) {
				if (mDevices.getmOnOffStatus().trim().equals("1")) {
					b = true;
				}
			}
			mHolder.devices_img.setImageResource(UiUtils.getLightsSmallIcon(b));
		} else {
			mHolder.devices_img.setImageResource(UiUtils
					.getDevicesSmallIcon(mDevices.getmDeviceId()));
		}
		mView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDevicesObserver.setDevicesId(mPostion);
				Log.i("tag", "tag->mDevices.getmName()="+mDevices.getmName()+";mDevices.getmIeee()="+mDevices.getmIeee()+";mDevices.getmNodeENNAme()="+mDevices.getmNodeENNAme());
				 Fragment mFragment=UiUtils.getFragment(mDevices.getmDeviceId());
				 if(null!=mFragment){
					 Bundle extras=new Bundle();//PASS_OBKECT
					 extras.putParcelable(PASS_OBJECT, mDevices);
					 mFragment.setArguments(extras);
					 mDevicesObserver.setFragment(mFragment,mPostion);
				 }
			}
		});
		return mView;
	}

	public void setList(List<SimpleDevicesModel> list) {
		mDevicesList = null;
		mDevicesList = list;
	}

	class ViewHolder {
		ImageView devices_img;
		TextView devices_name;
		TextView devices_region;
		TextView devices_state;
	}

	public interface DevicesObserver {
		public void setDevicesId(int id);

		public void setFragment(Fragment mFragment, int postion);
	}

}
