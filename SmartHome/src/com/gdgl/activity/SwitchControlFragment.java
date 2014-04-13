package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.adapter.DevicesBaseAdapter;
import com.gdgl.manager.LightManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.smarthome.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class SwitchControlFragment extends BaseControlFragment implements
		UIListener {
	boolean[] mBoolean = { false, false, false };
	List<String> mName = new ArrayList<String>();
	List<String> mIeee = new ArrayList<String>();
	List<String> EP = new ArrayList<String>();
	View mView;
	int mCount;
	SimpleDevicesModel mDevices;

	LightManager mLightManager;

	TextView txt_devices_name, txt_devices_region;

	CheckBox mSwitch1, mSwitch2, mSwitch3;
	TextView mSwichName1, mSwichName2, mSwichName3;
	RelativeLayout viewGroup1, viewGroup2;

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
		mLightManager = LightManager.getInstance();
		mLightManager.addObserver(SwitchControlFragment.this);
		if (null != mDevices) {
			String s = mDevices.getmOnOffStatus();
			String name = mDevices.getmNodeENNAme();
			String ieee = mDevices.getmNodeENNAme();
			String ep = mDevices.getmEP();

			String[] result = s.split(",");
			String[] nameR = name.split(",");
			String[] Ieee = ieee.split(",");
			String[] mEp = ep.split(",");

			for (int n = 0; n < nameR.length; n++) {
				if (nameR[n].trim().equals("")) {
					mName.add("Switch_" + n);
				} else {
					mName.add(nameR[n]);
				}
			}
			for (int m = 0; m < result.length; m++) {
				if (result[m].trim().equals("1")) {
					mBoolean[m] = true;
				}
			}
			for (int i = 0; i < Ieee.length; i++) {
				if (!Ieee[i].trim().equals("")) {
					mIeee.add(Ieee[i]);
				}
			}

			for (int i = 0; i < mEp.length; i++) {
				if (!mEp[i].trim().equals("")) {
					EP.add(mEp[i]);
				}
			}
			mCount = result.length;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.switch_control, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		mSwitch1 = (CheckBox) mView.findViewById(R.id.switch_state1);
		mSwitch2 = (CheckBox) mView.findViewById(R.id.switch_state2);
		mSwitch3 = (CheckBox) mView.findViewById(R.id.switch_state3);

		mSwichName1 = (TextView) mView.findViewById(R.id.switch_name1);
		mSwichName2 = (TextView) mView.findViewById(R.id.switch_name2);
		mSwichName3 = (TextView) mView.findViewById(R.id.switch_name3);

		txt_devices_name = (TextView) mView.findViewById(R.id.txt_devices_name);
		txt_devices_region = (TextView) mView
				.findViewById(R.id.txt_devices_region);

		txt_devices_name.setText(mDevices.getmName());
		txt_devices_region.setText(mDevices.getmDeviceRegion());

		viewGroup1 = (RelativeLayout) mView.findViewById(R.id.switch_group1);
		viewGroup2 = (RelativeLayout) mView.findViewById(R.id.switch_group2);

		switch (mCount) {
		case 1:
			viewGroup1.setVisibility(View.VISIBLE);
			viewGroup2.setVisibility(View.GONE);
			mSwitch1.setChecked(mBoolean[0]);
			mSwichName1.setText(mName.get(0));
			mSwitch1.setOnCheckedChangeListener(new SwitchClickListener(mIeee
					.get(0), mBoolean[0]));
			break;
		case 2:
			viewGroup1.setVisibility(View.GONE);
			viewGroup2.setVisibility(View.VISIBLE);
			mSwitch2.setChecked(mBoolean[0]);
			mSwitch3.setChecked(mBoolean[1]);

			mSwitch2.setOnCheckedChangeListener(new SwitchClickListener(mIeee
					.get(0), mBoolean[0]));
			mSwitch3.setOnCheckedChangeListener(new SwitchClickListener(mIeee
					.get(1), mBoolean[1]));

			mSwichName2.setText(mName.get(0));
			mSwichName3.setText(mName.get(1));
			break;
		case 3:
			mSwitch1.setChecked(mBoolean[0]);
			mSwitch2.setChecked(mBoolean[1]);
			mSwitch3.setChecked(mBoolean[2]);

			mSwitch1.setOnCheckedChangeListener(new SwitchClickListener(mIeee
					.get(0), mBoolean[0]));
			mSwitch2.setOnCheckedChangeListener(new SwitchClickListener(mIeee
					.get(1), mBoolean[1]));
			mSwitch3.setOnCheckedChangeListener(new SwitchClickListener(mIeee
					.get(2), mBoolean[2]));

			mSwichName1.setText(mName.get(0));
			mSwichName2.setText(mName.get(1));
			mSwichName3.setText(mName.get(2));
			break;
		default:
			break;
		}
	}

	public class SwitchClickListener implements OnCheckedChangeListener {
		String CIeee;
		boolean CB;

		public SwitchClickListener(String ieee, boolean b) {
			CIeee = ieee;
			CB = b;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub

		}

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
		mLightManager.deleteObserver(SwitchControlFragment.this);
		super.onDestroy();
	}

	@Override
	public void editDevicesName() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Manger observer, Object object) {
		final Event event = (Event) object;
		if (EventType.ONOFFSWITCHOPERATION == event.getType()) {
			// data maybe null
			SimpleDevicesModel data = (SimpleDevicesModel) event.getData();
			// TODO refresh UI data
		}

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
