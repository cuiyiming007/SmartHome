package com.gdgl.drawer;

import com.gdgl.libjingle.LibjingleSendManager;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.RfCGIManager;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOKOnlyDlg;
import com.gdgl.util.TimeDlg;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeviceDetailFragment_new extends Fragment implements UIListener {

	View mView;
	DevicesModel mDevices;

	TextView device_currentTextView, device_voltageTextView,
			device_powerTextView, device_energyTextView,
			device_powersourceTextView, device_ieeeTextView, device_epTextView,
			device_app_versionTextView, device_hw_versionTextView,
			device_date_codeTextView, rf_device_ieeeTextView;
	EditText identify_timeEditText;
	LinearLayout energy_attributeLayout, device_aboutLayout,
			device_about_contentLayout, device_heart_layout,
			device_identifyLayout, device_alarm_learnLayout,
			rf_device_about_contentLayout;
	Button begin_identifyButton, device_heartButton, alarm_learnButton;

	CGIManager cgiManager;
	LibjingleSendManager libjingleSendManager;

	DeviceDetailFragment_new(DevicesModel mDevicesModel) {
		mDevices = mDevicesModel;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		cgiManager = CGIManager.getInstance();
		cgiManager.addObserver(this);
		CallbackManager.getInstance().addObserver(this);
		libjingleSendManager = LibjingleSendManager.getInstance();
		libjingleSendManager.addObserver(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mView = inflater.inflate(R.layout.device_detail_fragment, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub

		energy_attributeLayout = (LinearLayout) mView
				.findViewById(R.id.energy_attribute);
		device_aboutLayout = (LinearLayout) mView
				.findViewById(R.id.device_about);
		device_about_contentLayout = (LinearLayout) mView
				.findViewById(R.id.device_about_content);
		rf_device_about_contentLayout = (LinearLayout) mView
				.findViewById(R.id.rf_device_about_content);
		device_heart_layout = (LinearLayout) mView
				.findViewById(R.id.device_heart_layout);
		device_identifyLayout =  (LinearLayout) mView
				.findViewById(R.id.device_identify);
		device_alarm_learnLayout =  (LinearLayout) mView
				.findViewById(R.id.alarm_learn);

		device_heartButton = (Button) mView.findViewById(R.id.device_heart_btn);
		alarm_learnButton = (Button) mView.findViewById(R.id.begin_learn);

		identify_timeEditText = (EditText) mView
				.findViewById(R.id.identify_time);
		begin_identifyButton = (Button) mView.findViewById(R.id.begin_identify);

		device_currentTextView = (TextView) mView
				.findViewById(R.id.device_current);
		device_voltageTextView = (TextView) mView
				.findViewById(R.id.device_voltage);
		device_powerTextView = (TextView) mView.findViewById(R.id.device_power);
		device_energyTextView = (TextView) mView
				.findViewById(R.id.device_energy);
		device_powersourceTextView = (TextView) mView
				.findViewById(R.id.device_powersource);

		device_ieeeTextView = (TextView) mView.findViewById(R.id.device_ieee);
		device_epTextView = (TextView) mView.findViewById(R.id.device_ep);
		device_app_versionTextView = (TextView) mView
				.findViewById(R.id.device_app_version);
		device_hw_versionTextView = (TextView) mView
				.findViewById(R.id.device_hw_version);
		device_date_codeTextView = (TextView) mView
				.findViewById(R.id.device_date_code);
		rf_device_ieeeTextView = (TextView) mView.findViewById(R.id.rf_device_ieee);
		// 设备详情布局
		setDeviceDetailLayout();

		// 警号学习
		alarm_learnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					RfCGIManager.getInstance().RFWarningDevOperation(mDevices.getmIeee(), 3, 0);
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(getActivity());
					myOKOnlyDlg.setContent(getResources().getString(
							R.string.Unable_In_InternetState));
					myOKOnlyDlg.show();
				}
			}
		});
		
		// 心跳周期
		device_heartButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(getActivity());
					myOKOnlyDlg.setContent(getResources().getString(
							R.string.Unable_In_InternetState));
					myOKOnlyDlg.show();
					return;
				}
				TimeDlg timeDlg = new TimeDlg((Context) getActivity(),
						mDevices, device_heartButton.getText().toString());
				timeDlg.show();
			}
		});

		// 识别设置
		begin_identifyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String identify_time = identify_timeEditText.getText()
						.toString();
				if (identify_time == null || identify_time.equals("")) {
					if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
						cgiManager.IdentifyDevice(mDevices, "10");
					} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
						libjingleSendManager.IdentifyDevice(mDevices, "10");
					}
				} else {
					if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
						cgiManager.IdentifyDevice(mDevices, identify_time);
					} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
						libjingleSendManager.IdentifyDevice(mDevices,
								identify_time);
					}
				}
			}
		});

		// 电能属性
		if (mDevices.getmCurrent() == null
				|| mDevices.getmModelId().indexOf(
						DataHelper.Curtain_control_switch) == 0
				|| mDevices.getmModelId().indexOf(
						DataHelper.Wireless_Intelligent_valve_switch) == 0) {
			energy_attributeLayout.setVisibility(View.GONE);
		} else {
			energy_attributeLayout.setVisibility(View.VISIBLE);
			device_currentTextView.setText(mDevices.getmCurrent());
			device_voltageTextView.setText(mDevices.getmVoltage());
			device_powerTextView.setText(mDevices.getmPower());
			device_energyTextView.setText(mDevices.getmEnergy());
		}

		// 供电方式
		device_powersourceTextView.setText(DevicePowersourceSwitch(mDevices
				.getmCurPowerResource()));

		device_ieeeTextView.setText(mDevices.getmIeee());
		device_epTextView.setText(mDevices.getmEP());
		device_app_versionTextView.setText(mDevices.getmAppVersion());
		device_hw_versionTextView.setText(mDevices.getmHwVersion());
		device_date_codeTextView.setText(mDevices.getmDateCode());
		rf_device_ieeeTextView.setText(mDevices.getmIeee());

	}

	public void setDeviceDetailLayout() {
		int deviceId = mDevices.getmDeviceId();
		String modelId = mDevices.getmModelId();
		switch (deviceId) {
		case DataHelper.IAS_ACE_DEVICETYPE:
		case DataHelper.IAS_ZONE_DEVICETYPE:
		case DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE:
			device_heart_layout.setVisibility(View.VISIBLE);
			getHeartTime();
			break;
		case DataHelper.RF_DEVICE:
			device_identifyLayout.setVisibility(View.GONE);
			device_about_contentLayout.setVisibility(View.GONE);
			rf_device_about_contentLayout.setVisibility(View.VISIBLE);
			if (modelId.indexOf(DataHelper.RF_Siren) == 0
					|| modelId.indexOf(DataHelper.RF_Siren_Outside) == 0
					|| modelId.indexOf(DataHelper.RF_Siren_Relay) == 0) {
				device_alarm_learnLayout.setVisibility(View.VISIBLE);
			}
		default:
			break;
		}
	}

	public void getHeartTime() {
		if (mDevices.getmHeartTime() == 0) {
			if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
				LibjingleSendManager.getInstance().getHeartTime(mDevices);
				return;
			}
			CGIManager.getInstance().getHeartTime(mDevices);
		} else {
			device_heartButton.setText(secToTime(mDevices.getmHeartTime()));
		}
	}

	public String DevicePowersourceSwitch(String string) {
		String result = "";
		if (string != null) {
			int i = Integer.parseInt(string);
			switch (i) {
			case 1:
			case 2:
			case 3:
				result = "外部电源";
				break;
			case 4:
				result = "电池";
				break;
			default:
				result = "";
				break;
			}
		}
		return result;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cgiManager.deleteObserver(this);
		CallbackManager.getInstance().deleteObserver(this);
		libjingleSendManager.deleteObserver(this);
	}

	public void refreshCurrent(String string) {
		device_currentTextView.setText(string);
		device_currentTextView.invalidate();
	}

	public void refreshVoltage(String string) {
		device_voltageTextView.setText(string);
		device_voltageTextView.postInvalidate();
	}

	public void refreshPower(String string) {
		device_powerTextView.setText(string);
		device_powerTextView.postInvalidate();
	}

	public void refreshEnergy(String string) {
		device_energyTextView.setText(string);
		device_energyTextView.postInvalidate();
	}

	@Override
	public void update(Manger observer, Object object) {
		final Event event = (Event) object;
		if (EventType.HEARTTIME == event.getType()) {
			if (event.isSuccess() == true) {
				final Bundle data = (Bundle) event.getData();
				if (data.getString("ieee").equals(mDevices.getmIeee())
						&& data.getString("ep").equals(mDevices.getmEP())) {
					if (data.getInt("time") == 0) {
						return;
					}
					mDevices.setmHeartTime(data.getInt("time"));
					device_heartButton.post(new Runnable() {
						@Override
						public void run() {
							device_heartButton.setText(secToTime(data
									.getInt("time")));
						}
					});
				}
			}
		} else if (EventType.CURRENT == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				if (!data.getDeviceIeee().equals(mDevices.getmIeee())) {
					return;
				}
				final String valueString = data.getValue();
				if (null != data.getValue()) {
					mDevices.setmCurrent(valueString);

					device_currentTextView.post(new Runnable() {
						@Override
						public void run() {
							refreshCurrent(valueString);
						}
					});
				}
			}
		} else if (EventType.VOLTAGE == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				if (!data.getDeviceIeee().equals(mDevices.getmIeee())) {
					return;
				}
				final String valueString = data.getValue();
				if (null != data.getValue()) {
					mDevices.setmVoltage(valueString);

					device_currentTextView.post(new Runnable() {
						@Override
						public void run() {
							refreshVoltage(valueString);
						}
					});
				}
			}
		} else if (EventType.ENERGY == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				if (!data.getDeviceIeee().equals(mDevices.getmIeee())) {
					return;
				}
				final String valueString = data.getValue();
				if (null != data.getValue()) {
					mDevices.setmEnergy(valueString);

					device_currentTextView.post(new Runnable() {
						@Override
						public void run() {
							refreshEnergy(valueString);
						}
					});
				}
			}
		} else if (EventType.POWER == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				if (!data.getDeviceIeee().equals(mDevices.getmIeee())) {
					return;
				}
				final String valueString = data.getValue();
				if (null != data.getValue()) {
					mDevices.setmPower(valueString);

					device_currentTextView.post(new Runnable() {
						@Override
						public void run() {
							refreshPower(valueString);
						}
					});
				}
			}
		}
	}

	public String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = "00" + ":" + unitFormat(minute) + ":"
						+ unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
						+ unitFormat(second);
			}
		}
		return timeStr;
	}

	public String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}
}
