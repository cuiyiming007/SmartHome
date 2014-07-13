package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.mydata.SimpleResponseData;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyDlg;
import com.gdgl.util.UiUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SwitchControlFragment extends BaseControlFragment implements
		UIListener {
	boolean[] mBoolean = { false, false, false };
	List<String> mName = new ArrayList<String>();
	List<String> EP = new ArrayList<String>();
	View mView;
	int mCount;
	SimpleDevicesModel mDevices;

	CGIManager mLightManager;

	TextView txt_devices_name, txt_devices_region;

	ImageView mSwitch1, mSwitch2, mSwitch3;
	TextView mSwichName1, mSwichName2, mSwichName3;
	RelativeLayout viewGroup1, viewGroup2;

	List<SimpleDevicesModel> mSimpleDevicesModel;

	private int mCurrent = 0;
	private int mPostion = 1;

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
		mLightManager = CGIManager.getInstance();
		mLightManager.addObserver(SwitchControlFragment.this);
		if (null != mDevices) {
			String s = mDevices.getmOnOffStatus();
			String name = mDevices.getmNodeENNAme();
			String ieee = mDevices.getmIeee();
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
//			for (int i = 0; i < Ieee.length; i++) {
//				if (!Ieee[i].trim().equals("")) {
//					mIeee.add(Ieee[i]);
//				}
//			}

			for (int i = 0; i < mEp.length; i++) {
				if (!mEp[i].trim().equals("")) {
					EP.add(mEp[i]);
				}
			}
			mCount = result.length;
		}

		mSimpleDevicesModel = new ArrayList<SimpleDevicesModel>();

		SimpleDevicesModel mdev;
		for (int i = 0; i < mCount; i++) {
			mdev = new SimpleDevicesModel();

			mdev.setmDeviceId(mDevices.getmDeviceId());
			mdev.setmDeviceRegion(mDevices.getmDeviceRegion());
			mdev.setmEP(EP.get(i));
			mdev.setmIeee(mDevices.getmIeee());
			mdev.setmLastDateTime(System.currentTimeMillis());
			mdev.setmModelId(mDevices.getmModelId());
			mdev.setmName(mDevices.getmName());
			mdev.setmNodeENNAme(mName.get(i));
			mdev.setmNWKAddr(mDevices.getmNWKAddr());
			mdev.setmOnOffLine(mBoolean[i] ? 1 : 0);
			mdev.setmOnOffStatus(mBoolean[i] ? "1" : "0");

			mSimpleDevicesModel.add(mdev);
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
		mSwitch1 = (ImageView) mView.findViewById(R.id.switch_state1);
		mSwitch2 = (ImageView) mView.findViewById(R.id.switch_state2);
		mSwitch3 = (ImageView) mView.findViewById(R.id.switch_state3);

		mSwichName1 = (TextView) mView.findViewById(R.id.switch_name1);
		mSwichName2 = (TextView) mView.findViewById(R.id.switch_name2);
		mSwichName3 = (TextView) mView.findViewById(R.id.switch_name3);

		txt_devices_name = (TextView) mView.findViewById(R.id.txt_devices_name);
		txt_devices_region = (TextView) mView
				.findViewById(R.id.txt_devices_region);

		txt_devices_name.setText(mDevices.getmUserDefineName().trim());
		txt_devices_region.setText(mDevices.getmDeviceRegion().trim());

		viewGroup1 = (RelativeLayout) mView.findViewById(R.id.switch_group1);
		viewGroup2 = (RelativeLayout) mView.findViewById(R.id.switch_group2);

		switch (mCount) {
		case 1:
			viewGroup1.setVisibility(View.VISIBLE);
			viewGroup2.setVisibility(View.GONE);
			setImagRes(mSwitch1, mBoolean[0]);
			mSwichName1.setText("M");

			mSwitch1.setOnClickListener(new SwitchClickListener(1,0));

			break;
		case 2:
			viewGroup1.setVisibility(View.GONE);
			viewGroup2.setVisibility(View.VISIBLE);

			setImagRes(mSwitch2, mBoolean[0]);
			setImagRes(mSwitch3, mBoolean[1]);

			mSwichName2.setText("L");
			mSwichName3.setText("R");

			mSwitch2.setOnClickListener(new SwitchClickListener(2,0));
			mSwitch3.setOnClickListener(new SwitchClickListener(3,1));

			break;
		case 3:

			setImagRes(mSwitch1, mBoolean[0]);
			setImagRes(mSwitch2, mBoolean[1]);
			setImagRes(mSwitch3, mBoolean[2]);

			mSwichName1.setText("M");
			mSwichName2.setText("L");
			mSwichName3.setText("R");

			mSwitch1.setOnClickListener(new SwitchClickListener(1,0));
			mSwitch2.setOnClickListener(new SwitchClickListener(2,1));
			mSwitch3.setOnClickListener(new SwitchClickListener(3,2));

			break;
		default:
			break;
		}
	}

	private void setImagRes(ImageView mSwitch, boolean b) {
		// TODO Auto-generated method stub
		if (b) {
			mSwitch.setImageResource(R.drawable.light_on_small);
		} else {
			mSwitch.setImageResource(R.drawable.light_off_small);
		}
	}

	public class SwitchClickListener implements OnClickListener {
		int postion;
		int current;
		public SwitchClickListener(int index,int c) {
			postion = index;
			current=c;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onoffswitch(postion,current);
		}

		private void onoffswitch(int postion2, int c) {
			// TODO Auto-generated method stub
			if (null == mDialog) {
				mDialog = MyDlg.createLoadingDialog((Context) getActivity(),
						"操作正在进行...");
				mDialog.show();
			}else{
				mDialog.show();
			}

			mCurrent = c;
			mPostion = postion2;
//			mLightManager.OnOffLightSwitchOperation(
//					mSimpleDevicesModel.get(mCurrent), 2, getChangeValue());
			mLightManager.OnOffOutputOperation(mSimpleDevicesModel.get(mCurrent), getChangeValue());
		}
	}
	private int getChangeValue() {
//		SimpleDevicesModel	s = mSimpleDevicesModel.get(mCurrent);
		if (mBoolean[mCurrent]==true) {
			return 0x01;
		}else {
			return 0x00;
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
		if(null!=mDialog){
			mDialog.dismiss();
			mDialog=null;
		}
		
		
		final Event event = (Event) object;
		if (EventType.ONOFFLIGHTSWITCHOPERATION == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				SimpleResponseData data = (SimpleResponseData) event.getData();
				// refresh UI data

				ContentValues c;
				SimpleDevicesModel s;

				mBoolean[mCurrent] = !mBoolean[mCurrent];

				switch (mPostion) {
				case 1:
					setImagRes(mSwitch1, mBoolean[mCurrent]);
					break;
				case 2:
					setImagRes(mSwitch2, mBoolean[mCurrent]);
					break;
				case 3:
					setImagRes(mSwitch3, mBoolean[mCurrent]);
					break;
				default:
					break;
				}

				s = mSimpleDevicesModel.get(mCurrent);
				c = new ContentValues();
				c.put(DevicesModel.ON_OFF_STATUS, mBoolean[mCurrent] ? "1"
						: "o");
				mUpdateDevice.updateDevices(s, c);
			} else {
				// if failed,prompt a Toast
				Toast toast=UiUtils.getToast((Context) getActivity());
				toast.show();
			}

		}
		
		if (EventType.INTITIALDVIVCEDATA == event.getType()) {
//			ArrayList<ResponseParamsEndPoint> devDataList = (ArrayList<ResponseParamsEndPoint>) event
//					.getData();
//			if (null == allList || allList.size() == 0) {
//				Log.i("scape devices", "SCAPDEV-> the first scape,get "+devDataList.size()+" result");
//				allList = devDataList;
//			} else {
//				for (ResponseParamsEndPoint responseParamsEndPoint : devDataList) {
//					if (!allList.contains(responseParamsEndPoint)) {
//						Log.i("scape devices", "SCAPDEV-> get a devices not in allList,add");
//						allList.add(responseParamsEndPoint);
//					}
//				}
//			}
		}
	}

}
