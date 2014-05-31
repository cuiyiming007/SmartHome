package com.gdgl.adapter;

import java.util.HashMap;
import java.util.List;

import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class AllDevicesAdapter extends BaseAdapter {

	protected Context mContext;
	protected List<SimpleDevicesModel> mDevicesList;
	protected AddChecked mDevicesObserver;
	
	private static  HashMap<Integer,Boolean> isSelected;

	public static final String DEVICES_ID = "devices_id";
	public static final String BOLLEAN_ARRARY = "devices_state";
	public static final String DEVIVES_VALUE = "devices_value";

	ViewHolder lay;

	public class SwitchModel {
		public String modelId;
		public String name;
		public String[] anotherName;
		public String[] ieee;
		public boolean[] state;

	}
	
	public AllDevicesAdapter(Context c, List<SimpleDevicesModel> list,
			AddChecked mObserver) {
		mContext = c;
		mDevicesList = list;
		mDevicesObserver = mObserver;
		isSelected = new HashMap<Integer, Boolean>();
		for(int i=0;i<mDevicesList.size();i++){
			isSelected.put(i, false);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (null != mDevicesList) {
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
		final ViewHolder mHolder;
		final SimpleDevicesModel mDevices = (SimpleDevicesModel)getItem(position);
		
		if (null == mView) {
			mHolder = new ViewHolder();
			mView = LayoutInflater.from(mContext).inflate(
					R.layout.all_devices_item, null);
			mHolder.devices_img = (ImageView) mView
					.findViewById(R.id.devices_img);
			mHolder.devices_name = (TextView) mView
					.findViewById(R.id.devices_name);
			mHolder.devices_state = (TextView) mView
					.findViewById(R.id.devices_state);
			mHolder.selected = (CheckBox) mView
					.findViewById(R.id.selected);
			mHolder.mPostion=position;
			mView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) mView.getTag();
			mHolder.mPostion=position;
		}

		mHolder.devices_name.setText(mDevices.getmUserDefineName().replace(" ",
				""));

		if (DataHelper.IAS_ZONE_DEVICETYPE == mDevices.getmDeviceId()
				|| DataHelper.IAS_ACE_DEVICETYPE == mDevices.getmDeviceId()) {

			mHolder.devices_img
					.setImageResource(UiUtils
							.getDevicesSmallIconByModelId(mDevices
									.getmModelId().trim()));
		} else if (mDevices.getmModelId().indexOf(
				DataHelper.Multi_key_remote_control) == 0) {
			mHolder.devices_img.setImageResource(UiUtils
					.getDevicesSmallIconForRemote(mDevices.getmDeviceId()));
		}else {
			mHolder.devices_img.setImageResource(UiUtils
					.getDevicesSmallIcon(mDevices.getmDeviceId()));
		}
		
		mHolder.selected.setChecked(getIsSelected().get(position));

		if (mDevices.getmDeviceId() == DataHelper.ON_OFF_SWITCH_DEVICETYPE) {
			String state = "";
			String s = mDevices.getmOnOffStatus();
			String[] result = s.split(",");
			for (String string : result) {
				if (string.trim().equals("1")) {
					state += "开 ";
				} else {
					state += "关 ";
				}
			}
			mHolder.devices_state.setText(state);
		} else if (mDevices.getmDeviceId() == DataHelper.IAS_ZONE_DEVICETYPE) {
			if (mDevices.getmOnOffStatus().trim().equals("1")) {
				mHolder.devices_state.setText("布防");
			} else {
				mHolder.devices_state.setText("撤防");
			}
		} else if (mDevices.getmDeviceId() == DataHelper.LIGHT_SENSOR_DEVICETYPE) {
			mHolder.devices_state.setText("当前室内亮度为: 30");
		} else if (mDevices.getmDeviceId() == DataHelper.TEMPTURE_SENSOR_DEVICETYPE) {
			mHolder.devices_state.setText("当前室内温度为: 30°C");
		} else {
			if (mDevices.getmOnOffStatus().trim().equals("1")) {
				mHolder.devices_state.setText("开");
			} else {
				mHolder.devices_state.setText("关");
			}
		}

		return mView;
	}

	public void setList(List<SimpleDevicesModel> list) {
		mDevicesList = null;
		mDevicesList = list;
	}

	public class ViewHolder {
		public ImageView devices_img;
		public TextView devices_name;
		public TextView devices_state;
		public CheckBox selected;
		public int mPostion;
	}

	public interface DevicesObserver {

		public void setLayout();

		public void deleteDevices(String id);

	}
	
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		 isSelected = isSelected;
	}

	public interface AddChecked{
		public void AddCheckedDevices(int postion);
		public void DeletedCheckedDevices(int postion);
	}
}
