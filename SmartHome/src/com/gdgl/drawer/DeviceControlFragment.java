package com.gdgl.drawer;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

import com.gdgl.activity.DeviceHistoryFragment;
import com.gdgl.libjingle.LibjingleSendManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.RfCGIManager;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.tabstrip.PagerSlidingTabStrip;

public class DeviceControlFragment extends Fragment implements UIListener {

	final int ON = 1;
	final int OFF = 2;
	final int INVERSION = 3;

	View mView;
	DevicesModel mDevices;

	TextView device_contor_statusTextView, device_temperatureTextView,
			device_humidityTextView;
	LinearLayout device_temperaturesensorLayout;
	ToggleButton device_controlButton;
	SeekBar device_seekBar;

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	private ArrayList<Fragment> mfragments;

	CGIManager cgiManager;
	RfCGIManager rfCGIManager;//====王晓飞====
	LibjingleSendManager libjingleSendManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		if (null != extras) {
			mDevices = (DevicesModel) extras
					.getSerializable(Constants.PASS_OBJECT);
		}

		cgiManager = CGIManager.getInstance();
		cgiManager.addObserver(DeviceControlFragment.this);
		rfCGIManager = RfCGIManager.getInstance();//======王晓飞
		rfCGIManager.addObserver(DeviceControlFragment.this);//====王晓飞
		CallbackManager.getInstance().addObserver(DeviceControlFragment.this);
		libjingleSendManager = LibjingleSendManager.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.device_control_fragment, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		device_controlButton = (ToggleButton) mView
				.findViewById(R.id.device_control_button);
		device_controlButton.setChecked(getDeviceControlOnOff());
		device_controlButton.setBackgroundResource(DataUtil
				.getDefaultDevicesControlIcon(mDevices.getmDeviceId(),
						mDevices.getmModelId()));
		device_contor_statusTextView = (TextView) mView
				.findViewById(R.id.device_control_status);

		device_seekBar = (SeekBar) mView.findViewById(R.id.device_seek_bar);

		device_temperaturesensorLayout = (LinearLayout) mView
				.findViewById(R.id.device_temperaturesensor);
		device_temperatureTextView = (TextView) mView
				.findViewById(R.id.device_temperature);
		device_humidityTextView = (TextView) mView
				.findViewById(R.id.device_humidity);
		if (mDevices.getmDeviceId() == DataHelper.TEMPTURE_SENSOR_DEVICETYPE) {
			device_temperatureTextView.setText(mDevices.getmTemperature()
					+ "°C");
			device_humidityTextView.setText(mDevices.getmHumidity() + "%");
		}

		pager = (ViewPager) mView.findViewById(R.id.pager);

		tabs = (PagerSlidingTabStrip) mView.findViewById(R.id.tabs);
		tabs.setShouldExpand(true);
		tabs.setTextSize((int) (getActivity().getResources()
				.getDisplayMetrics().scaledDensity * 15));
		tabs.setTextColorResource(R.color.text_gray);
		tabs.setIndicatorColorResource(R.color.blue_default);

		mfragments = new ArrayList<Fragment>();
		mfragments.add(new DeviceDetailFragment_new(mDevices));
		mfragments.add(new DeviceHistoryFragment(mDevices));
		adapter = new MyPagerAdapter(getChildFragmentManager(), mfragments);

		pager.setAdapter(adapter);
		tabs.setViewPager(pager);

		// 设备控制布局
		setDeviceControlText(getDeviceControlOnOff());

		// 设备控制
		if (setDeviceControlLayout()) {
			device_controlButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.i("ToggleButton", "====================>change ");
					if (device_controlButton.isChecked()) {
						if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
							DeviceContorlOnOffClickDo(ON);
						} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
							DeviceContorlOnOffClickDo_Internet(ON);
						}
					} else {
						if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
							DeviceContorlOnOffClickDo(OFF);
						} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
							DeviceContorlOnOffClickDo_Internet(OFF);
						}
					}
				}
			});
		}
		if (device_controlButton.isClickable()) {
			device_controlButton.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Drawable mDrawable = getResources().getDrawable(
							DataUtil.getDefaultDevicesControlIcon(
									mDevices.getmDeviceId(),
									mDevices.getmModelId()));
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						mDrawable.setColorFilter(Color.parseColor("#55888888"),
								Mode.SRC_ATOP);
						device_controlButton.setBackground(mDrawable);
						return false;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_MOVE:
					case MotionEvent.ACTION_UP:
						mDrawable.clearColorFilter();
						device_controlButton.setBackground(mDrawable);
						return false;
					}
					return true;
				}
			});
		}
		if (mDevices.getmDeviceId() == DataHelper.DIMEN_LIGHTS_DEVICETYPE) {
			int level = Integer.parseInt(mDevices.getmLevel());
			int state = level * 100 / 254;
			device_seekBar.setProgress(state);
			device_seekBar.invalidate();
			device_seekBar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						int currentLevel = Integer.parseInt(mDevices
								.getmLevel());

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub
							Log.i("device_seekBar", "onStopTrackingTouch");
							if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
								cgiManager.dimmableLightOperation(mDevices, 16,
										currentLevel);
							} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
								libjingleSendManager.dimmableLightOperation(
										mDevices, 16, currentLevel);
							}
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub
							Log.i("device_seekBar", "onStartTrackingTouch");
						}

						@Override
						public void onProgressChanged(SeekBar seekBar,
								int progress, boolean fromUser) {
							// TODO Auto-generated method stub
							Log.i("device_seekBar", "onProgressChanged");
							currentLevel = progress * 255 / 100;
							mDevices.setmLevel(String.valueOf(currentLevel));
						}
					});
		}
		if (mDevices.getmDeviceId() == DataHelper.SHADE_DEVICETYPE) {
			int level = Integer.parseInt(mDevices.getmLevel());
			int state = level * 100 / 254;
			device_seekBar.setProgress(state);
			device_seekBar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						int currentLevel = Integer.parseInt(mDevices
								.getmLevel());

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub
							if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
								cgiManager.shadeOperation(mDevices, 14,
										currentLevel);
							} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
								libjingleSendManager.shadeOperation(mDevices,
										14, currentLevel);
							}
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onProgressChanged(SeekBar seekBar,
								int progress, boolean fromUser) {
							// TODO Auto-generated method stub
							currentLevel = progress * 255 / 100;
							mDevices.setmLevel(String.valueOf(currentLevel));
						}
					});
		}
	}

	public boolean setDeviceControlLayout() {
		int deviceId = mDevices.getmDeviceId();
		String modelId = mDevices.getmModelId();
		switch (deviceId) {
		case DataHelper.ON_OFF_SWITCH_DEVICETYPE:
			device_contor_statusTextView.setVisibility(View.GONE);
			device_controlButton.setClickable(false);
			return false;
		case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
			break;
		case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
			break;
		case DataHelper.RANGE_EXTENDER_DEVICETYPE:
			break;
		case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
			break;
		case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
			device_seekBar.setVisibility(View.VISIBLE);
			break;
		case DataHelper.DIMEN_SWITCH_DEVICETYPE:
			device_contor_statusTextView.setVisibility(View.GONE);
			device_controlButton.setClickable(false);
			return false;
		case DataHelper.LIGHT_SENSOR_DEVICETYPE:
			device_contor_statusTextView.setVisibility(View.GONE);
			device_controlButton.setClickable(false);
			return false;
		case DataHelper.SHADE_DEVICETYPE:
			device_seekBar.setVisibility(View.VISIBLE);
			break;
		case DataHelper.TEMPTURE_SENSOR_DEVICETYPE:
			device_contor_statusTextView.setVisibility(View.GONE);
			device_controlButton.setClickable(false);
			device_temperaturesensorLayout.setVisibility(View.VISIBLE);
			return false;
		case DataHelper.IAS_ACE_DEVICETYPE:
			device_contor_statusTextView.setVisibility(View.GONE);
			device_controlButton.setClickable(false);
			return false;
		case DataHelper.IAS_ZONE_DEVICETYPE:
			// if (modelId.indexOf(DataHelper.Motion_Sensor) == 0) { //
			// ZigBee动作感应器
			// }
			// if (modelId.indexOf(DataHelper.Magnetic_Window) == 0) { //
			// ZigBee窗磁
			// }
			// if (modelId.indexOf(DataHelper.Doors_and_windows_sensor_switch)
			// == 0) { // 门窗感应开关
			// }
			if (modelId.indexOf(DataHelper.Emergency_Button) == 0) { // ZigBee紧急按钮
				// device_contor_statusTextView.setVisibility(View.GONE);
				device_controlButton.setClickable(false);
				return false;
			}
			if (modelId.indexOf(DataHelper.Emergency_Button_On_Wall) == 0) { // ZigBee墙面紧急按钮
				// device_contor_statusTextView.setVisibility(View.GONE);
				device_controlButton.setClickable(false);
				return false;
			}
			if (modelId.indexOf(DataHelper.Smoke_Detectors) == 0) { // 烟雾感应器
				// device_contor_statusTextView.setVisibility(View.GONE);
				device_controlButton.setClickable(false);
				return false;
			}
			if (modelId.indexOf(DataHelper.Combustible_Gas_Detector_Gas) == 0) { // 可燃气体探测器（煤气)器
				// device_contor_statusTextView.setVisibility(View.GONE);
				device_controlButton.setClickable(false);
				return false;
			}
			if (modelId.indexOf(DataHelper.Combustible_Gas_Detector_CO) == 0) { // 可燃气体探测器（一氧化碳)
				// device_contor_statusTextView.setVisibility(View.GONE);
				device_controlButton.setClickable(false);
				return false;
			}
			if (modelId
					.indexOf(DataHelper.Combustible_Gas_Detector_Natural_gas) == 0) { // 可燃气体探测器（天然气)
				// device_contor_statusTextView.setVisibility(View.GONE);
				device_controlButton.setClickable(false);
				return false;
			}
		case DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE:
			// device_contor_statusTextView.setVisibility(View.GONE);
			// device_controlButton.setClickable(false);
			break;
		default:
			break;
		}
		return true;
	}

	public boolean getDeviceControlOnOff() {
		int deviceId = mDevices.getmDeviceId();
		String modelId = mDevices.getmModelId();
		boolean on_off = true;
		switch (deviceId) {
		case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
		case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
		case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
		case DataHelper.ON_OFF_LIGHT_DEVICETYPE:
		case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
		case DataHelper.SHADE_DEVICETYPE:
			on_off = mDevices.getmOnOffStatus().equals("1") ? true : false;
			break;
		case DataHelper.IAS_ZONE_DEVICETYPE:
			on_off = mDevices.getmOnOffStatus().equals("0") ? true : false;
			if (modelId.indexOf(DataHelper.Emergency_Button) == 0
					|| modelId.indexOf(DataHelper.Emergency_Button_On_Wall) == 0
					|| modelId.indexOf(DataHelper.Smoke_Detectors) == 0
					|| modelId.indexOf(DataHelper.Combustible_Gas_Detector_Gas) == 0
					|| modelId.indexOf(DataHelper.Combustible_Gas_Detector_CO) == 0
					|| modelId
							.indexOf(DataHelper.Combustible_Gas_Detector_Natural_gas) == 0) {
				// ZigBee紧急按钮,ZigBee墙面紧急按钮,烟雾感应器,可燃气体探测器（煤气)器,可燃气体探测器（一氧化碳),可燃气体探测器（天然气)
				on_off = true;
			}
			break;
		case DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE:
			on_off = true;
			break;
		case DataHelper.RF_DEVICE:
			on_off = mDevices.getmOnOffStatus().equals("1") ? true : false;
		default:
			break;
		}
		return on_off;
	}

	public void setDeviceControlText(boolean check) {
		if (check) {
			device_contor_statusTextView.setTextColor(getResources().getColor(
					R.color.text_open_green));
			int deviceId = mDevices.getmDeviceId();
			String modelId = mDevices.getmModelId();
			switch (deviceId) {
			case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
			case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
			case DataHelper.ON_OFF_LIGHT_DEVICETYPE:
			case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
			case DataHelper.SHADE_DEVICETYPE:
				device_contor_statusTextView.setText("打开");
				break;
			case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
			case DataHelper.IAS_ZONE_DEVICETYPE:
				device_contor_statusTextView.setText("布防");
				if (modelId.indexOf(DataHelper.Emergency_Button) == 0
						|| modelId.indexOf(DataHelper.Emergency_Button_On_Wall) == 0
						|| modelId.indexOf(DataHelper.Smoke_Detectors) == 0
						|| modelId
								.indexOf(DataHelper.Combustible_Gas_Detector_Gas) == 0
						|| modelId
								.indexOf(DataHelper.Combustible_Gas_Detector_CO) == 0
						|| modelId
								.indexOf(DataHelper.Combustible_Gas_Detector_Natural_gas) == 0) {
					// ZigBee紧急按钮,ZigBee墙面紧急按钮,烟雾感应器,可燃气体探测器（煤气)器,可燃气体探测器（一氧化碳),可燃气体探测器（天然气)
					device_contor_statusTextView.setText("24小时布防");
				}
				break;
			case DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE:
				device_contor_statusTextView.setText("停止报警");
				break;
			case DataHelper.RF_DEVICE:
				device_contor_statusTextView.setText("布防");
				break;
			default:
				break;
			}
		} else {
			device_contor_statusTextView.setTextColor(getResources().getColor(
					R.color.text_gray));
			int deviceId = mDevices.getmDeviceId();
			switch (deviceId) {
			case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
			case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
			case DataHelper.ON_OFF_LIGHT_DEVICETYPE:
			case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
			case DataHelper.SHADE_DEVICETYPE:
				device_contor_statusTextView.setText("关闭");
				break;
			case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
			case DataHelper.IAS_ZONE_DEVICETYPE:
				device_contor_statusTextView.setText("撤防");
				break;
			case DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE:
				device_contor_statusTextView.setText("停止报警");
				break;
			case DataHelper.RF_DEVICE:
				device_contor_statusTextView.setText("撤防");
				break;
			default:
				break;
			}
		}
	}

	public void DeviceContorlOnOffClickDo(int status) {
		int deviceId = mDevices.getmDeviceId();
		String modelId = mDevices.getmModelId();//=======王晓飞=====
		switch (deviceId) {
		case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
			switch (status) {
			case ON:
				cgiManager.OnOffOutputOperation(mDevices, 0);
				break;
			case OFF:
				cgiManager.OnOffOutputOperation(mDevices, 1);
				break;
			case INVERSION:
				cgiManager.OnOffOutputOperation(mDevices, 2);
				break;
			default:
				break;
			}
			break;
		case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
			switch (status) {
			case ON:
				cgiManager.LocalIASCIEOperation(null, 7);
				break;
			case OFF:
				cgiManager.LocalIASCIEOperation(null, 6);
				break;
			default:
				break;
			}
			break;
		case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
			switch (status) {
			case ON:
				cgiManager.MainsOutLetOperation(mDevices, 1);
				break;
			case OFF:
				cgiManager.MainsOutLetOperation(mDevices, 0);
				break;
			case INVERSION:
				cgiManager.MainsOutLetOperation(mDevices, 2);
				break;
			default:
				break;
			}
			break;
		case DataHelper.ON_OFF_LIGHT_DEVICETYPE:
			switch (status) {
			case ON:
				cgiManager.MainsOutLetOperation(mDevices, 1);
				break;
			case OFF:
				cgiManager.MainsOutLetOperation(mDevices, 0);
				break;
			case INVERSION:
				cgiManager.MainsOutLetOperation(mDevices, 2);
				break;
			default:
				break;
			}
			break;
		case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
			switch (status) {
			case ON:
				device_seekBar.setProgress(100);
				cgiManager.dimmableLightOperation(mDevices, 16, 254);
				break;
			case OFF:
				device_seekBar.setProgress(0);
				cgiManager.dimmableLightOperation(mDevices, 0, 1);
				break;
			case INVERSION:
				cgiManager.dimmableLightOperation(mDevices, 2, 1);
				break;
			default:
				break;
			}
			break;
		case DataHelper.SHADE_DEVICETYPE:
			switch (status) {
			case ON:
				device_seekBar.setProgress(100);
				cgiManager.shadeOperation(mDevices, 0, 1);
				break;
			case OFF:
				device_seekBar.setProgress(0);
				cgiManager.shadeOperation(mDevices, 1, 1);
				break;
			case INVERSION:
				cgiManager.shadeOperation(mDevices, 2, 1);
				break;
			default:
				break;
			}
			break;
		case DataHelper.IAS_ZONE_DEVICETYPE:
			switch (status) {
			case ON:
				cgiManager.LocalIASCIEUnByPassZone(mDevices);
				break;
			case OFF:
				cgiManager.LocalIASCIEByPassZone(mDevices);
				break;
			default:
				break;
			}
			break;
		case DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE:
			cgiManager.IASWarningDeviceOperationCommon(mDevices, 0);
			break;
			//=====20150819====王晓飞=====
		case DataHelper.RF_DEVICE:
			switch (status) {
			case ON:
				if (modelId.indexOf(DataHelper.RF_Siren) == 0||
						modelId.indexOf(DataHelper.RF_Siren_Outside) == 0||
						modelId.indexOf(DataHelper.RF_Siren_Relay) == 0){
					cgiManager.stopAlarm();
				} else if (modelId.indexOf(DataHelper.RF_remote_control) == 0){
					
				} else {
					rfCGIManager.ChangeRFDevArmState(mDevices.getmIeee(), 1);
				}
				break;
			case OFF:
				if (modelId.indexOf(DataHelper.RF_Siren) == 0||
						modelId.indexOf(DataHelper.RF_Siren_Outside) == 0||
						modelId.indexOf(DataHelper.RF_Siren_Relay) == 0) {
					cgiManager.stopAlarm();
				} else if (modelId.indexOf(DataHelper.RF_remote_control) == 0){
				
				} else {
					rfCGIManager.ChangeRFDevArmState(mDevices.getmIeee(), 0);
				}
				break;
			default:
				break;
			}
			break;
			
		default:
			break;
		}
	}

	public void DeviceContorlOnOffClickDo_Internet(int status) {
		int deviceId = mDevices.getmDeviceId();
		String modelId = mDevices.getmModelId();//====王晓飞=====
		switch (deviceId) {
		case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
			switch (status) {
			case ON:
				libjingleSendManager.OnOffOutputOperation(mDevices, 0);
				break;
			case OFF:
				libjingleSendManager.OnOffOutputOperation(mDevices, 1);
				break;
			case INVERSION:
				libjingleSendManager.OnOffOutputOperation(mDevices, 2);
				break;
			default:
				break;
			}
			break;
		case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
			switch (status) {
			case ON:
				libjingleSendManager.LocalIASCIEOperation(null, 7);
				break;
			case OFF:
				libjingleSendManager.LocalIASCIEOperation(null, 6);
				break;
			default:
				break;
			}
			break;
		case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
			switch (status) {
			case ON:
				libjingleSendManager.MainsOutLetOperation(mDevices, 1);
				break;
			case OFF:
				libjingleSendManager.MainsOutLetOperation(mDevices, 0);
				break;
			case INVERSION:
				libjingleSendManager.MainsOutLetOperation(mDevices, 2);
				break;
			default:
				break;
			}
			break;
		case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
			switch (status) {
			case ON:
				device_seekBar.setProgress(100);
				libjingleSendManager.dimmableLightOperation(mDevices, 16, 254);
				break;
			case OFF:
				device_seekBar.setProgress(0);
				libjingleSendManager.dimmableLightOperation(mDevices, 0, 1);
				break;
			case INVERSION:
				libjingleSendManager.dimmableLightOperation(mDevices, 2, 1);
				break;
			default:
				break;
			}
			break;
		case DataHelper.SHADE_DEVICETYPE:
			switch (status) {
			case ON:
				device_seekBar.setProgress(100);
				libjingleSendManager.shadeOperation(mDevices, 0, 1);
				break;
			case OFF:
				device_seekBar.setProgress(0);
				libjingleSendManager.shadeOperation(mDevices, 1, 1);
				break;
			case INVERSION:
				libjingleSendManager.shadeOperation(mDevices, 2, 1);
				break;
			default:
				break;
			}
			break;
		case DataHelper.IAS_ZONE_DEVICETYPE:
			switch (status) {
			case ON:
				libjingleSendManager.LocalIASCIEUnByPassZone(mDevices);
				break;
			case OFF:
				libjingleSendManager.LocalIASCIEByPassZone(mDevices);
				break;
			default:
				break;
			}
			break;
		case DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE:
			libjingleSendManager.IASWarningDeviceOperationCommon(mDevices, 0);
			break;
			
		case DataHelper.RF_DEVICE://====王晓飞====
			switch (status) {
			case ON:
				if (modelId.indexOf(DataHelper.RF_Siren) == 0||
						modelId.indexOf(DataHelper.RF_Siren_Outside) == 0||
						modelId.indexOf(DataHelper.RF_Siren_Relay) == 0){
			//		libjingleSendManager.RFWarningDevOperation(mDevices.getmIeee(), 1,0);
					libjingleSendManager.stopAlarm();
				}else if (modelId.indexOf(DataHelper.RF_remote_control) == 0){
					
				}else {
					libjingleSendManager.ChangeRFDevArmState(mDevices.getmIeee(), 1);
				}
				break;
			case OFF:
				if (modelId.indexOf(DataHelper.RF_Siren) == 0||
						modelId.indexOf(DataHelper.RF_Siren_Outside) == 0||
						modelId.indexOf(DataHelper.RF_Siren_Relay) == 0) {
//					libjingleSendManager.RFWarningDevOperation(mDevices.getmIeee(), 0,0);
					libjingleSendManager.stopAlarm();
				} else if (modelId.indexOf(DataHelper.RF_remote_control) == 0) {
					
				} else {
					libjingleSendManager.ChangeRFDevArmState(mDevices.getmIeee(), 0);
				}
				break;
			default:
				break;
			}
			break;
			
		default:
			break;
		}
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "设备详情", "历史记录" };

		private ArrayList<Fragment> fragments;

		public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int pos) {
			return fragments.get(pos);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cgiManager.deleteObserver(this);
		libjingleSendManager.deleteObserver(this);
		CallbackManager.getInstance().deleteObserver(this);
	}

	public void refreshLevel(String string) {
		int level = Integer.parseInt(string);
		int state = level * 100 / 254;
		device_seekBar.setProgress(state);
		device_seekBar.invalidate();
	}

	public void refreshLevel(CallbackResponseType2 data) {
		if (mDevices != null) {
			if (!data.getDeviceIeee().equals(mDevices.getmIeee())) {
				return;
			}
			if (Integer.parseInt(data.getValue()) < 7) {
				mDevices.setmOnOffStatus("0");
			} else {
				mDevices.setmOnOffStatus("1");
			}
			int level = Integer.parseInt(data.getValue());
			int state = level * 100 / 254;
			device_seekBar.setProgress(state);
			device_seekBar.invalidate();
		}
	}

	public void refreshOnOffStatus(CallbackResponseType2 data) {
		if (mDevices != null) {
			if (mDevices.getmIeee().equals(data.getDeviceIeee())
					&& mDevices.getmEP().equals(data.getDeviceEp())) {
				mDevices.setmOnOffStatus(data.getValue());
			}
		}
	}

	@Override
	public void update(Manger observer, Object object) {
		final Event event = (Event) object;
		if (EventType.LOCALIASCIEBYPASSZONE == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				if (bundle.getString("IEEE").equals(mDevices.getmIeee())
						&& bundle.getString("EP").equals(mDevices.getmEP())) {
					mDevices.setmOnOffStatus(bundle.getString("PARAM"));

					mView.post(new Runnable() {
						@Override
						public void run() {
							setDeviceControlText(mDevices.getmOnOffStatus()
									.equals("0") ? true : false);
							device_controlButton.setChecked(mDevices
									.getmOnOffStatus().equals("0") ? true
									: false);
						}
					});
				}
			}
		} else if (EventType.LOCALIASCIEOPERATION == event.getType()) {
			if (event.isSuccess() == true) {
				String status = (String) event.getData();

				if (mDevices.getmModelId().indexOf(DataHelper.One_key_operator) == 0) {
					mDevices.setmOnOffStatus(status);

					mView.post(new Runnable() {
						@Override
						public void run() {
							setDeviceControlText(mDevices.getmOnOffStatus()
									.equals("1") ? true : false);
							device_controlButton.setChecked(mDevices
									.getmOnOffStatus().equals("1") ? true
									: false);
						}
					});
				}
			}
		} else if (EventType.ON_OFF_STATUS == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				if (data.getDeviceIeee().equals(mDevices.getmIeee())
						&& data.getDeviceEp().equals(mDevices.getmEP())) {
					if (null != data.getValue()) {
						mDevices.setmOnOffStatus(data.getValue());

						mView.post(new Runnable() {
							@Override
							public void run() {
								setDeviceControlText(mDevices.getmOnOffStatus()
										.equals("1") ? true : false);
								device_controlButton.setChecked(mDevices
										.getmOnOffStatus().equals("1") ? true
										: false);
							}
						});
					}
				}
			}
		} else if (EventType.MOVE_TO_LEVEL == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				if (!data.getDeviceIeee().equals(mDevices.getmIeee())) {
					return;
				}
				String valueString = data.getValue();
				if (null != data.getValue()) {
					mDevices.setmLevel(valueString);
					if (Integer.parseInt(valueString) < 7) {
						mDevices.setmOnOffStatus("0");
					} else {
						mDevices.setmOnOffStatus("1");
					}
					int level = Integer.parseInt(valueString);
					final int state = level * 100 / 254;
					device_seekBar.post(new Runnable() {
						@Override
						public void run() {
							device_seekBar.setProgress(state);
							device_seekBar.invalidate();
						}
					});
				}
			}
		} else if (EventType.TEMPERATURESENSOROPERATION == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				if (bundle.getString("IEEE").equals(mDevices.getmIeee())
						&& bundle.getString("EP").equals(mDevices.getmEP())) {

					final String temperature = bundle.getString("PARAM");
					mDevices.setmTemperature(Float.parseFloat(temperature));

					mView.post(new Runnable() {
						@Override
						public void run() {
							device_temperatureTextView.setText(temperature
									+ "°C");
						}
					});
				}
			}
		} else if (EventType.HUMIDITY == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				if (bundle.getString("IEEE").equals(mDevices.getmIeee())
						&& bundle.getString("EP").equals(mDevices.getmEP())) {
					final String humidity = bundle.getString("PARAM");
					mDevices.setmHumidity(Float.parseFloat(humidity));
					mView.post(new Runnable() {
						@Override
						public void run() {
							// setdata(mCurrentList);
							device_humidityTextView.setText(humidity + "%");
						}
					});
				}
			}
		} else if (EventType.RF_DEVICE_BYPASS == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				if (bundle.getString("IEEE").equals(mDevices.getmIeee())) {
					mDevices.setmOnOffStatus(bundle.getString("PARAM"));

					mView.post(new Runnable() {
						@Override
						public void run() {
							setDeviceControlText(mDevices.getmOnOffStatus()
									.equals("1") ? true : false);
							device_controlButton.setChecked(mDevices
									.getmOnOffStatus().equals("1") ? true
									: false);
						}
					});
				}
			}
		}
	}
}
