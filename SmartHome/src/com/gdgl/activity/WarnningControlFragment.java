package com.gdgl.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.WarnManager;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.SimpleResponseData;
import com.gdgl.mydata.Callback.CallbackWarnMessage;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyDlg;

public class WarnningControlFragment extends BaseControlFragment {

	int OnOffImg[];

	public static final int ON = 0;
	public static final int OFF = 1;

	View mView;
	SimpleDevicesModel mDevices;

	CallbackWarnMessage currentWarmMessage;

	TextView txt_devices_name, txt_devices_region;
	RelativeLayout mError;

	ImageView on_off;

	boolean status = false;

	String Ieee = "";

	String ep = "";

	CGIManager mLightManager;

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
			// if (mDevices.getmOnOffStatus().trim().equals("1")) {
			// status = true;
			// }
			initalCrunentWarnStatus();
			Ieee = mDevices.getmIeee().trim();
			ep = mDevices.getmEP().trim();
		}
	}

	private void initalCrunentWarnStatus() {
		status = WarnManager.getInstance().isWarnning();
		currentWarmMessage = WarnManager.getInstance().getCurrentWarmmessage();
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

		initstate();
		mLightManager = CGIManager.getInstance();
		mLightManager.addObserver(WarnningControlFragment.this);
		CallbackManager.getInstance().addObserver(this);
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
		on_off = (ImageView) mView.findViewById(R.id.devices_on_off);

		txt_devices_name = (TextView) mView.findViewById(R.id.txt_devices_name);
		txt_devices_region = (TextView) mView
				.findViewById(R.id.txt_devices_region);

		txt_devices_name.setText(mDevices.getmUserDefineName().trim());
		txt_devices_region.setText(mDevices.getmDeviceRegion().trim());

		setImagRes(on_off, status);

		mError = (RelativeLayout) mView.findViewById(R.id.error_message);

		on_off.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mError.setVisibility(View.GONE);
				if (null == mDialog) {
					mDialog = MyDlg.createLoadingDialog(
							(Context) getActivity(), "操作正在进行...");
					mDialog.show();
				} else {
					mDialog.show();
				}
				mLightManager.IASWarningDeviceOperationCommon(mDevices, 0);
				txt_devices_name.setText(mDevices.getmUserDefineName().trim());

			}
		});
	}

	@Override
	public void onDestroy() {
		mLightManager.deleteObserver(WarnningControlFragment.this);
		CallbackManager.getInstance().deleteObserver(this);
		super.onDestroy();
	}

	@Override
	public void editDevicesName() {

	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		if (null != mDialog) {
			mDialog.dismiss();
			mDialog = null;
		}

		final Event event = (Event) object;
		if (EventType.IASWARNINGDEVICOPERATION == event.getType()) {

			if (event.isSuccess() == true) {
				// data maybe null
				SimpleResponseData data = (SimpleResponseData) event.getData();
				// refresh UI data

				status = !status;

				setImagRes(on_off, status);

//				ContentValues c = new ContentValues();
//				c.put(DevicesModel.ON_OFF_STATUS, status ? "1" : "o");
//				mDevices.setmOnOffStatus(status ? "1" : "o");
//				mUpdateDevice.updateDevices(mDevices, c);
				WarnManager.getInstance().inialWarn();
			} else {
				// if failed,prompt a Toast
				mError.setVisibility(View.VISIBLE);
			}
		} else if (EventType.WARN == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				final CallbackWarnMessage data = (CallbackWarnMessage) event
						.getData();
				mView.post(new Runnable() {
					@Override
					public void run() {
						updateWarnMessage(data, true);
					}
				});
			}
		}

	}

	@Override
	public void onResume() {
		updateWarnMessage(WarnManager.getInstance().getCurrentWarmmessage(),
				WarnManager.getInstance().isWarnning());
		super.onResume();
	}

	private void updateWarnMessage(CallbackWarnMessage data, boolean isWarning) {
		status = isWarning;
		setImagRes(on_off, status);
		if (isWarning && data != null) {
			txt_devices_name.setText(data.getDetailmessage());
		} else {
			txt_devices_name.setText(mDevices.getmUserDefineName());
		}
	}
}
