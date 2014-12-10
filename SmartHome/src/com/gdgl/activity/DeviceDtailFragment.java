package com.gdgl.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.gdgl.libjingle.LibjingleSendManager;
import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;

/***
 * 设备详情页
 * 
 */
public class DeviceDtailFragment extends BaseFragment {

	public static final int WITH_DEVICE_ABOUT = 0;
	public static final int WITHOUT_DEVICE_ABOUT = 1;

	final int ON = 1;
	final int OFF = 2;
	final int INVERSION = 3;

	View mView;
	DevicesModel mDevices;
	int aboutdevice;
	Boolean aboutdeviceonoffBoolean = false;

	TextView device_nameTextView, device_regionTextView,
			device_currentTextView, device_voltageTextView,
			device_powerTextView, device_energyTextView,
			device_powersourceTextView, device_disableTextView,
			device_ieeeTextView, device_epTextView, device_app_versionTextView,
			device_hw_versionTextView, device_date_codeTextView;
	EditText identify_timeEditText;
	ImageView device_imgImageView, up_down_imageImageView;
	LinearLayout device_contorlLayout, device_seekbarLayout,
			device_contorl_safeLayout, energy_attributeLayout,
			device_aboutLayout, device_about_open_closeLayout,
			device_about_contentLayout;
	Button device_onButton, device_offButton, device_inversionButton,
			device_defense_onButton, device_defense_offButton,
			begin_identifyButton;
	SeekBar device_seekBar;

	CGIManager cgiManager;
	LibjingleSendManager libjingleSendManager;

	DataHelper mDataHelper;

	private static DeviceDtailFragment instance;

	public static DeviceDtailFragment getInstance() {
		if (instance == null) {
			instance = new DeviceDtailFragment();
		}
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		if (null != extras) {
			mDevices = (DevicesModel) extras
					.getSerializable(Constants.PASS_OBJECT);
			aboutdevice = (int) extras.getInt(Constants.PASS_DEVICE_ABOUT);
		}

		cgiManager = CGIManager.getInstance();
		cgiManager.addObserver(DeviceDtailFragment.this);
		DeviceManager.getInstance().addObserver(this);
		libjingleSendManager = LibjingleSendManager.getInstance();

		mDataHelper = new DataHelper((Context) getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.device_detail, null);
		initView();
		return mView;
	}

	private void initView() {
		// mLightManager.iASZoneOperationCommon(mDevices, 7, 1);
		// TODO Auto-generated method stub

		device_contorlLayout = (LinearLayout) mView
				.findViewById(R.id.device_contorl);
		device_seekbarLayout = (LinearLayout) mView
				.findViewById(R.id.device_seekbar);
		device_contorl_safeLayout = (LinearLayout) mView
				.findViewById(R.id.device_contorl_safe);
		energy_attributeLayout = (LinearLayout) mView
				.findViewById(R.id.energy_attribute);
		device_aboutLayout = (LinearLayout) mView
				.findViewById(R.id.device_about);
		device_about_open_closeLayout = (LinearLayout) mView
				.findViewById(R.id.device_about_open_close);
		device_about_contentLayout = (LinearLayout) mView
				.findViewById(R.id.device_about_content);

		device_imgImageView = (ImageView) mView.findViewById(R.id.device_img);
		device_nameTextView = (TextView) mView.findViewById(R.id.device_name);
		device_regionTextView = (TextView) mView
				.findViewById(R.id.device_region);

		device_onButton = (Button) mView.findViewById(R.id.device_on);
		device_offButton = (Button) mView.findViewById(R.id.device_off);
		device_inversionButton = (Button) mView
				.findViewById(R.id.device_inversion);
		device_seekBar = (SeekBar) mView.findViewById(R.id.device_seek_bar);
		device_defense_onButton = (Button) mView
				.findViewById(R.id.device_defense_on);
		device_defense_offButton = (Button) mView
				.findViewById(R.id.device_defense_off);

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
		device_disableTextView = (TextView) mView
				.findViewById(R.id.device_disable);

		up_down_imageImageView = (ImageView) mView
				.findViewById(R.id.up_down_image);
		device_ieeeTextView = (TextView) mView.findViewById(R.id.device_ieee);
		device_epTextView = (TextView) mView.findViewById(R.id.device_ep);
		device_app_versionTextView = (TextView) mView
				.findViewById(R.id.device_app_version);
		device_hw_versionTextView = (TextView) mView
				.findViewById(R.id.device_hw_version);
		device_date_codeTextView = (TextView) mView
				.findViewById(R.id.device_date_code);

		// 设备详情布局
		setDeviceDetailLayout();
		if (aboutdevice == WITH_DEVICE_ABOUT) {
			device_aboutLayout.setVisibility(View.VISIBLE);
		}
		// 设备图片、名称、区域
		device_imgImageView.setImageResource(DataUtil
				.getDefaultDevicesSmallIcon(mDevices.getmDeviceId(), mDevices
						.getmModelId().trim()));
		device_nameTextView.setText(mDevices.getmDefaultDeviceName().trim());
		device_regionTextView.setText(mDevices.getmDeviceRegion().trim());

		// 设备控制
		device_onButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					DeviceContorlOnOffClickDo(ON);
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					DeviceContorlOnOffClickDo_Internet(ON);
				}
			}
		});
		device_offButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					DeviceContorlOnOffClickDo(OFF);
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					DeviceContorlOnOffClickDo_Internet(OFF);
				}
			}
		});
		device_inversionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					DeviceContorlOnOffClickDo(INVERSION);
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					DeviceContorlOnOffClickDo_Internet(INVERSION);
				}
			}
		});
		device_defense_onButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					DeviceContorlDefenseClickDo(ON);
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					DeviceContorlDefenseClickDo_Internet(ON);
				}
			}
		});
		device_defense_offButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					DeviceContorlDefenseClickDo(OFF);
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					DeviceContorlDefenseClickDo_Internet(OFF);
				}
			}
		});
		if (mDevices.getmDeviceId() == DataHelper.DIMEN_LIGHTS_DEVICETYPE) {

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

		// 供电方式、设备异常
		device_powersourceTextView.setText(DevicePowersourceSwitch(mDevices
				.getmCurPowerResource()));
		device_disableTextView.setText("否");

		// 关于设备
		device_about_open_closeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (aboutdeviceonoffBoolean) {
					aboutdeviceonoffBoolean = false;
					up_down_imageImageView
							.setImageResource(R.drawable.ui_arrow_down_img);
					device_about_contentLayout.setVisibility(View.GONE);
				} else {
					aboutdeviceonoffBoolean = true;
					up_down_imageImageView
							.setImageResource(R.drawable.ui_arrow_up_img);
					device_about_contentLayout.setVisibility(View.VISIBLE);
				}
			}
		});
		device_ieeeTextView.setText(mDevices.getmIeee());
		device_epTextView.setText(mDevices.getmEP());
		device_app_versionTextView.setText(mDevices.getmAppVersion());
		device_hw_versionTextView.setText(mDevices.getmHwVersion());
		device_date_codeTextView.setText(mDevices.getmDateCode());

		// device_imgImageView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// mError.setVisibility(View.GONE);
		// if (null == mDialog) {
		// mDialog = MyDlg.createLoadingDialog(
		// (Context) getActivity(), "操作正在进行...");
		// mDialog.show();
		// } else {
		// mDialog.show();
		// }
		// if (status) {
		// mLightManager.LocalIASCIEByPassZone(mDevices);
		// } else {
		// mLightManager.LocalIASCIEUnByPassZone(mDevices);
		// }
		// status = !status;
		// }
		// });
	}

	public void setDeviceDetailLayout() {
		int deviceId = mDevices.getmDeviceId();
		String modelId = mDevices.getmModelId();
		switch (deviceId) {
		case DataHelper.ON_OFF_SWITCH_DEVICETYPE:
			device_contorlLayout.setVisibility(View.GONE);
			break;
		case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
			break;
		case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
			device_contorlLayout.setVisibility(View.GONE);
			device_contorl_safeLayout.setVisibility(View.VISIBLE);
			break;
		case DataHelper.RANGE_EXTENDER_DEVICETYPE:
			break;
		case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
			break;
		case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
			device_seekbarLayout.setVisibility(View.VISIBLE);
			break;
		case DataHelper.DIMEN_SWITCH_DEVICETYPE:
			device_contorlLayout.setVisibility(View.GONE);
			break;
		case DataHelper.LIGHT_SENSOR_DEVICETYPE:
			device_contorlLayout.setVisibility(View.GONE);
			break;
		case DataHelper.SHADE_DEVICETYPE:
			device_seekbarLayout.setVisibility(View.VISIBLE);
			break;
		case DataHelper.TEMPTURE_SENSOR_DEVICETYPE:
			device_contorlLayout.setVisibility(View.GONE);
			break;
		case DataHelper.IAS_ACE_DEVICETYPE:
			device_contorlLayout.setVisibility(View.GONE);
			break;
		case DataHelper.IAS_ZONE_DEVICETYPE:
			if (modelId.indexOf(DataHelper.Motion_Sensor) == 0) { // ZigBee动作感应器
				device_contorlLayout.setVisibility(View.GONE);
				device_contorl_safeLayout.setVisibility(View.VISIBLE);
			}
			if (modelId.indexOf(DataHelper.Magnetic_Window) == 0) { // ZigBee窗磁
				device_contorlLayout.setVisibility(View.GONE);
				device_contorl_safeLayout.setVisibility(View.VISIBLE);
			}
			if (modelId.indexOf(DataHelper.Doors_and_windows_sensor_switch) == 0) { // 门窗感应开关
				device_contorlLayout.setVisibility(View.GONE);
				device_contorl_safeLayout.setVisibility(View.VISIBLE);
			}
			if (modelId.indexOf(DataHelper.Emergency_Button) == 0) { // ZigBee紧急按钮
				device_contorlLayout.setVisibility(View.GONE);
			}
			if (modelId.indexOf(DataHelper.Emergency_Button_On_Wall) == 0) { // ZigBee墙面紧急按钮
				device_contorlLayout.setVisibility(View.GONE);
			}
			if (modelId.indexOf(DataHelper.Smoke_Detectors) == 0) { // 烟雾感应器
				device_contorlLayout.setVisibility(View.GONE);
			}
			if (modelId.indexOf(DataHelper.Combustible_Gas_Detector_Gas) == 0) { // 可燃气体探测器（煤气)器
				device_contorlLayout.setVisibility(View.GONE);
			}
			if (modelId.indexOf(DataHelper.Combustible_Gas_Detector_CO) == 0) { // 可燃气体探测器（一氧化碳)
				device_contorlLayout.setVisibility(View.GONE);
			}
			if (modelId
					.indexOf(DataHelper.Combustible_Gas_Detector_Natural_gas) == 0) { // 可燃气体探测器（天然气)
				device_contorlLayout.setVisibility(View.GONE);
			}
			break;
		case DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE:
			device_contorlLayout.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	public void DeviceContorlOnOffClickDo(int status) {
		int deviceId = mDevices.getmDeviceId();

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
		default:
			break;
		}
	}

	public void DeviceContorlDefenseClickDo(int status) {
		int deviceId = mDevices.getmDeviceId();

		switch (deviceId) {
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
		default:
			break;
		}
	}

	public void DeviceContorlOnOffClickDo_Internet(int status) {
		int deviceId = mDevices.getmDeviceId();

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
		default:
			break;
		}
	}

	public void DeviceContorlDefenseClickDo_Internet(int status) {
		int deviceId = mDevices.getmDeviceId();

		switch (deviceId) {
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
		default:
			break;
		}
	}

	public String DevicePowersourceSwitch(String string) {
		int i = Integer.parseInt(string);
		String result;
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
		return result;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cgiManager.deleteObserver(DeviceDtailFragment.this);
		DeviceManager.getInstance().deleteObserver(this);
	}

	@Override
	public void stopRefresh() {
		// TODO Auto-generated method stub

	}

	public void refreshLevel(String string) {
		int level = Integer.parseInt(string);
		int state = level * 100 / 254;
		device_seekBar.setProgress(state);
		device_seekBar.invalidate();
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
		// if (null != mDialog) {
		// mDialog.dismiss();
		// mDialog = null;
		// }
		// final Event event = (Event) object;
		// if (EventType.LOCALIASCIEBYPASSZONE == event.getType()
		// || EventType.LOCALIASCIEUNBYPASSZONE == event.getType()) {
		// if (event.isSuccess()) {
		// mDevices.setmOnOffStatus(status ? "0" : "1");
		// updateStatusOnUIThread();
		// ContentValues c = new ContentValues();
		// c.put(DevicesModel.ON_OFF_STATUS, status ? "0" : "1");
		// // mUpdateDevice.updateDevices(mDevices, c);
		// } else {
		// Toast.makeText(getActivity(), "操作失败", Toast.LENGTH_SHORT);
		// }
		// }
	}

	// private void updateStatusByList(ArrayList<CIEresponse_params>
	// devDataList) {
	// for (CIEresponse_params ciEresponse_params : devDataList) {
	// // 过滤出本页面设备的Ieee
	// if (ciEresponse_params.getCie().getIeee()
	// .equals(mDevices.getmIeee())) {
	// String bypass = ciEresponse_params.getCie().getElserec()
	// .getBbypass();
	// // 异或运算，当bypass的值和status的值不同时，刷新界面和数据库
	// if (!stringToBoolean(bypass) ^ status) {
	// updateStatusOnUIThread();
	// // [TODO]刷新数据库
	// }
	// break;
	// }
	//
	// }
	// }

	// private void updateStatusOnUIThread() {
	// mView.post(new Runnable() {
	// @Override
	// public void run() {
	// setImagRes(device_imgImageView, status);
	// }
	// });
	// }

}
