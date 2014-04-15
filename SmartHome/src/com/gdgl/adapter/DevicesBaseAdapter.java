package com.gdgl.adapter;

import java.util.List;

import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.smarthome.R;
import com.gdgl.util.EditDevicesDlg;
import com.gdgl.util.ExpandCollapseAnimation;
import com.gdgl.util.EditDevicesDlg.EditDialogcallback;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;
import com.gdgl.util.UiUtils;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DevicesBaseAdapter extends BaseAdapter implements Dialogcallback {

	protected Context mContext;
	protected List<SimpleDevicesModel> mDevicesList;
	protected DevicesObserver mDevicesObserver;

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

	private String index;

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
		final SimpleDevicesModel mDevices = mDevicesList.get(position);
		
		if (null == mView) {
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
		
		if (mDevices.getmModelId().contains(DataHelper.Wall_switch_double)
				|| mDevices.getmModelId().contains(
						DataHelper.Wall_switch_triple)) {
			mHolder.devices_name.setText(mDevices.getmName().replace(" ", ""));
		} else {
			mHolder.devices_name.setText(mDevices.getmNodeENNAme().replace(" ",
					""));
			
		}
		mHolder.devices_region.setText(mDevices.getmDeviceRegion().replace(
				" ", ""));
		
		if (DataHelper.IAS_ZONE_DEVICETYPE == mDevices.getmDeviceId()) {
			
			mHolder.devices_img.setImageResource(UiUtils.getDevicesSmallIconByModelId(mDevices.getmModelId().trim()));
		} else {
			mHolder.devices_img.setImageResource(UiUtils.getDevicesSmallIcon(mDevices.getmDeviceId()));
		}
		
		
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
		} else {
			if (mDevices.getmOnOffStatus().trim().equals("1")) {
				mHolder.devices_state.setText("开");
			} else {
				mHolder.devices_state.setText("关");
			}
		}
		
		if (DataHelper.LIGHT_SENSOR_DEVICETYPE == mDevices.getmDeviceId()) {
			boolean b = false;
			if (null != mDevices.getmOnOffStatus()) {
				if (mDevices.getmOnOffStatus().trim().equals("1")) {
					b = true;
				}
			}
			mHolder.devices_img.setImageResource(UiUtils.getLightsSmallIcon(b));
		} 
		
		return mView;
	}

	public void setList(List<SimpleDevicesModel> list) {
		mDevicesList = null;
		mDevicesList = list;
	}

	public class ViewHolder {
		ImageView devices_img;
		TextView devices_name;
		TextView devices_region;
		TextView devices_state;
	}

	public interface DevicesObserver {

		public void setLayout();

		public void deleteDevices(String id);

	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		// lay.expandable_toggle_button.setChecked(true);
		// Animation anim = new ExpandCollapseAnimation(lay.expand,
		// ExpandCollapseAnimation.COLLAPSE);
		// anim.setDuration(300);
		// //lay.expand.startAnimation(anim);
		// mDevicesObserver.deleteDevices(index);
	}

}
