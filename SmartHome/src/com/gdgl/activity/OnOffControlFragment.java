package com.gdgl.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdgl.activity.BaseControlFragment.UpdateDevice;
import com.gdgl.manager.LightManager;
import com.gdgl.manager.Manger;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyDlg;

public class OnOffControlFragment extends BaseControlFragment {

	int OnOffImg[];

	public static final int ON = 0;
	public static final int OFF = 1;

	View mView;
	SimpleDevicesModel mDevices;

	TextView txt_devices_name, txt_devices_region;

	ImageView on_off;

	boolean status = false;

	String Ieee = "";

	String ep = "";

	LightManager mLightManager;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		if (!(activity instanceof UpdateDevice)) {
			throw new IllegalStateException("Activity必须实现SaveDevicesName接口");
		}
		mUpdateDevice = (UpdateDevice) activity;
		super.onAttach(activity);
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

	private void setImagRes(ImageView mSwitch, boolean b) {
		// TODO Auto-generated method stub
		if (b) {
			mSwitch.setImageResource(OnOffImg[ON]);
		} else {
			mSwitch.setImageResource(OnOffImg[OFF]);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		if (null != extras) {
			mDevices = (SimpleDevicesModel) extras
					.getParcelable(DevicesListFragment.PASS_OBJECT);
			OnOffImg = extras.getIntArray(DevicesListFragment.PASS_ONOFFIMG);
		}

		mLightManager = LightManager.getInstance();
		mLightManager.addObserver(OnOffControlFragment.this);
		initstate();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.on_off_control, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		on_off = (ImageView) mView.findViewById(R.id.devices_on_off);

		txt_devices_name = (TextView) mView.findViewById(R.id.txt_devices_name);
		txt_devices_region = (TextView) mView
				.findViewById(R.id.txt_devices_region);

		txt_devices_name.setText(mDevices.getmName());
		txt_devices_region.setText(mDevices.getmDeviceRegion());

		setImagRes(on_off, status);

		on_off.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (null == mDialog) {
					mDialog = MyDlg.createLoadingDialog(
							(Context) getActivity(), "操作正在进行...");
					mDialog.show();
				} else {
					mDialog.show();
				}
				mLightManager.onOffLightOperation();
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLightManager.deleteObserver(OnOffControlFragment.this);
	}

	@Override
	public void editDevicesName() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		if (null != mDialog) {
			mDialog.dismiss();
			mDialog = null;
		}

		status = !status;

		setImagRes(on_off, status);

		ContentValues c = new ContentValues();
		c.put(DevicesModel.ON_OFF_STATUS, status ? "1" : "o");
		mUpdateDevice.updateDevices(Ieee, ep, c);
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
