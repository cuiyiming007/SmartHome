package com.gdgl.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdgl.adapter.DevicesBaseAdapter;
import com.gdgl.manager.Manger;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.smarthome.R;

public class LightsControlFragment extends BaseControlFragment {
	View mView;
	SimpleDevicesModel mDevices;

	TextView txt_devices_name, txt_devices_region;

	ImageView light_on_off;

	boolean status = false;

	String Ieee = "";
	
	String ep="";

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Bundle extras = getArguments();
		if (null != extras) {
			mDevices = (SimpleDevicesModel) extras
					.getParcelable(DevicesListFragment.PASS_OBJECT);
		}
		initstate();
	}

	private void initstate() {
		// TODO Auto-generated method stub
		if (null != mDevices) {
			if (mDevices.getmOnOffStatus().trim().equals("1")) {
				status = true;
			}
			Ieee = mDevices.getmIeee().trim();
			ep = mDevices.getmEP().trim();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.lights_control, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		light_on_off = (ImageView) mView.findViewById(R.id.lights_on_off);

		txt_devices_name = (TextView) mView.findViewById(R.id.txt_devices_name);
		txt_devices_region = (TextView) mView
				.findViewById(R.id.txt_devices_region);

		txt_devices_name.setText(mDevices.getmName());
		txt_devices_region.setText(mDevices.getmDeviceRegion());

		if (status) {
			light_on_off.setImageResource(R.drawable.light_on_big);
		} else {
			light_on_off.setImageResource(R.drawable.light_off_big);
		}

		light_on_off.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean result = updateDevice(status);
				if (status && result) {
					light_on_off.setImageResource(R.drawable.light_off_big);
				} else {
					light_on_off.setImageResource(R.drawable.light_on_big);
				}
				status = !status;
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		if (!(activity instanceof UpdateDevice)) {
			throw new IllegalStateException("Activity必须实现SaveDevicesName接口");
		}
		mUpdateDevice = (UpdateDevice) activity;
		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void editDevicesName() {
		// TODO Auto-generated method stub

	}

	public boolean updateDevice(boolean b) {
		ContentValues c = new ContentValues();
		boolean result;
		if (b) {
			c.put(DevicesModel.ON_OFF_STATUS, "0");
		} else {
			c.put(DevicesModel.ON_OFF_STATUS, "1");
		}
		result = mUpdateDevice.updateDevices(Ieee,ep, c);
		return result;
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub

	}
	class operatortype {
		/***
		 * 获取设备类型
		 */
		public static final int GetOnOffSwitchType = 0;
		/***
		 * 获取状态
		 */
		public static final int GetOnOffSwitchActions = 1;
		/***
		 * 当操作类型是2时，para1有以下意义 Param1: switchaction: 0x00: Off 0x01: On 0x02:
		 * Toggle
		 */
		public static final int ChangeOnOffSwitchActions = 2;
	}
}
