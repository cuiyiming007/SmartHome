package com.gdgl.activity;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdgl.manager.Manger;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.smarthome.R;

public class DoorBellFragment extends BaseControlFragment {

	View mView;
	SimpleDevicesModel mDevices;

	TextView txt_devices_name, txt_devices_region, text_alarm;

	ImageView devices_on_off;

	AnimationDrawable animationDrawable;

	public static final int UNDO = 0;
	public static final int STOP = 4;

	private static final int ON = 1;
	private static final int OFF = 0;
	int status = OFF;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		if (null != extras) {
			mDevices = (SimpleDevicesModel) extras
					.getParcelable(DevicesListFragment.PASS_OBJECT);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.door_bell, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub

		txt_devices_name = (TextView) mView.findViewById(R.id.txt_devices_name);
		txt_devices_region = (TextView) mView
				.findViewById(R.id.txt_devices_region);

		devices_on_off = (ImageView) mView.findViewById(R.id.devices_on_off);
		text_alarm = (TextView) mView.findViewById(R.id.text_alarm);

		txt_devices_name.setText(mDevices.getmUserDefineName().trim());
		txt_devices_region.setText(mDevices.getmDeviceRegion().trim());

		devices_on_off.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (status == OFF) {
					status = ON;
					text_alarm.setVisibility(View.VISIBLE);
					Message msg = Message.obtain();

					devices_on_off
							.setBackgroundResource(R.drawable.door_bell_anim);
					animationDrawable = (AnimationDrawable) devices_on_off
							.getBackground();
					animationDrawable.start();
					msg.what = STOP;
					myHandler.sendMessageDelayed(msg, 30000);
				} else {
					myHandler.removeMessages(STOP);
					if (null != animationDrawable) {
						animationDrawable.stop();
					}
					status = OFF;
					devices_on_off.setBackgroundResource(R.drawable.bdundo);
					text_alarm.setVisibility(View.GONE);
				}

			}
		});
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if (status == ON) {
			if (null != animationDrawable) {
				animationDrawable.stop();
			}
			status = OFF;
			devices_on_off.setBackgroundResource(R.drawable.bdundo);
			text_alarm.setVisibility(View.GONE);
		}
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (status == ON) {
			if (null != animationDrawable) {
				animationDrawable.stop();
			}
			status = OFF;
			devices_on_off.setBackgroundResource(R.drawable.bdundo);
			text_alarm.setVisibility(View.GONE);
		}
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

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub

	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			Log.i("zgs", "zz->handleMessage() msg.arg1=" + msg.arg1);
			switch (msg.what) {
			case UNDO:
				devices_on_off.setImageResource(R.drawable.bdundo);
				break;
			case STOP:
				if (null != animationDrawable) {
					animationDrawable.stop();
				}
				status = OFF;
				devices_on_off.setBackgroundResource(R.drawable.bdundo);
				text_alarm.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

}
