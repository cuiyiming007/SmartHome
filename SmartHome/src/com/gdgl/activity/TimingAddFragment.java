package com.gdgl.activity;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.gdgl.activity.TimingAddDeviceDialog.DeviceSelected;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.timing.TimingAction;
import com.gdgl.smarthome.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.ToggleButton;

public class TimingAddFragment extends Fragment implements DeviceSelected {
	TimingAction mTimingAction;
	List<DevicesModel> mAddDevicesList;
	DevicesModel mDevicesSelected;

	private View mView;
	private CardView mAddDevice;
	private TextView mNoDevice;
	private View mDeviceItem;
	private ImageView mDeviceImage;
	private TextView mDeviceName;
	private TextView mStatusDescribe;
	private TextView mDeviceStatus;
	private Switch mSwitchBtn;

	private LinearLayout weekGroup;
	private ToggleButton[] weekByDayButtons = new ToggleButton[7];
	private TimePicker timePicker;
	private TextView dateText;
	private DatePicker datePicker;

	private final Calendar mCalendar = Calendar.getInstance();
	private int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
	private int minute = mCalendar.get(Calendar.MINUTE);
	private int day = mCalendar.get(Calendar.DAY_OF_MONTH);
	private int month = mCalendar.get(Calendar.MONTH);
	private int year = mCalendar.get(Calendar.YEAR);

	int timing_type;
	// boolean[] weekByDayStatus = {false,false,false,false,false,false,false};
	DataHelper mDataHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		timing_type = (int) bundle.getInt(TimingAddActivity.TYPE);
		mDataHelper = new DataHelper(getActivity());
		initdata();

	}

	private void initdata() {
		// TODO Auto-generated method stub
		mAddDevicesList = null;
		mAddDevicesList = mDataHelper.queryForDevicesList(
				mDataHelper.getSQLiteDatabase(), DataHelper.DEVICES_TABLE,
				null, null, null, null, null, DevicesModel.DEVICE_PRIORITY,
				null);
		if (timing_type == TimingAddActivity.CREATE) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			mTimingAction.setPara1(format.format(mCalendar.getTime()));
		}
		if (timing_type == TimingAddActivity.EDIT) {
			for (DevicesModel model : mAddDevicesList) {
				if (mTimingAction.getIeee().equals(model.getmIeee())
						&& mTimingAction.getEp().equals(model.getmEP())) {
					mDevicesSelected = model;
					break;
				}
			}
			year = Integer.parseInt(mTimingAction.getPara1().substring(0, 4));
			month = Integer.parseInt(mTimingAction.getPara1().substring(4, 6)) - 1;
			day = Integer.parseInt(mTimingAction.getPara1().substring(6, 8));
			hour = Integer.parseInt(mTimingAction.getPara1().substring(8, 10));
			minute = Integer.parseInt(mTimingAction.getPara1()
					.substring(10, 12));
		}
		for (int i = 0; i < mAddDevicesList.size(); i++) {
			DevicesModel mModel = mAddDevicesList.get(i);
			if (mModel.getmModelId().indexOf(DataHelper.Siren) == 0
					|| mModel.getmModelId().indexOf(
							DataHelper.Indoor_temperature_sensor) == 0
					|| mModel.getmModelId().indexOf(DataHelper.Smoke_Detectors) == 0) {
				mAddDevicesList.remove(i);
				i--;
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.timing_add_fragment, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		mAddDevice = (CardView) mView.findViewById(R.id.add_device);
		mNoDevice = (TextView) mView.findViewById(R.id.no_device);

		mDeviceItem = (View) mView.findViewById(R.id.device_item);
		mDeviceImage = (ImageView) mDeviceItem.findViewById(R.id.devices_img);
		mDeviceName = (TextView) mDeviceItem.findViewById(R.id.devices_name);
		mStatusDescribe = (TextView) mDeviceItem.findViewById(R.id.describe);
		mDeviceStatus = (TextView) mDeviceItem.findViewById(R.id.devices_state);
		mSwitchBtn = (Switch) mDeviceItem.findViewById(R.id.switch_btn);

		mAddDevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimingAddDeviceDialog timingAddDeviceDialog = new TimingAddDeviceDialog(
						mAddDevicesList, TimingAddFragment.this);
				timingAddDeviceDialog.show(getChildFragmentManager(), "");
			}
		});

		if (timing_type == TimingAddActivity.CREATE) {
			mDeviceItem.setVisibility(View.GONE);
			mNoDevice.setVisibility(View.VISIBLE);
		} else {
			mDeviceItem.setVisibility(View.VISIBLE);
			mNoDevice.setVisibility(View.GONE);
			mDeviceImage.setImageResource(Integer.parseInt(mDevicesSelected
					.getmPicName()));
			mDeviceName.setText(mDevicesSelected.getmDefaultDeviceName());
			mStatusDescribe.setText("设备状态：");
			mDeviceStatus.setText(setDeviceStatusText(mTimingAction
					.getDevicesStatus() == 1 ? true : false, mDevicesSelected.getmDeviceId()));
			mSwitchBtn.setChecked(mTimingAction.getDevicesStatus() == 1 ? true
					: false);
		}
		mSwitchBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				mDeviceStatus.setText(setDeviceStatusText(isChecked, mDevicesSelected.getmDeviceId()));
				mTimingAction.setDevicesStatus(isChecked ? 1 : 0);
			}
		});

		weekGroup = (LinearLayout) mView.findViewById(R.id.weekGroup);
		String[] dayOfWeekString = new DateFormatSymbols().getShortWeekdays();
		for (int i = 0; i < 7; i++) {

			weekByDayButtons[i] = (ToggleButton) weekGroup.getChildAt(i);
			weekByDayButtons[i].setTextOff(dayOfWeekString[i + 1]);
			weekByDayButtons[i].setTextOn(dayOfWeekString[i + 1]);
			weekByDayButtons[i].setChecked(mTimingAction.getPara3Status()[i]);
			weekByDayButtons[i]
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							int itemIdx = -1;
							for (int i = 0; i < 7; i++) {
								if (itemIdx == -1
										&& buttonView == weekByDayButtons[i]) {
									itemIdx = i;
									mTimingAction.setPara3Status(i, isChecked);
								}
							}
							refreshDateView();
						}
					});

		}
		datePicker = (DatePicker) mView.findViewById(R.id.dpPicker);
		dateText = (TextView) mView.findViewById(R.id.dateText);
		timePicker = (TimePicker) mView.findViewById(R.id.tpPicker);
		mCalendar.set(year, month, day, hour, minute);
		datePicker.init(year, month, day, new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int years,
					int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				year = years;
				month = monthOfYear;
				day = dayOfMonth;
				mCalendar.set(year, month, day, hour, minute, 0);
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				mTimingAction.setPara1(format.format(mCalendar.getTime()));
			}
		});
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay,
					int minutes) {
				// TODO Auto-generated method stub
				hour = hourOfDay;
				minute = minutes;
				mCalendar.set(year, month, day, hour, minute, 0);
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				mTimingAction.setPara1(format.format(mCalendar.getTime()));
			}
		});
		refreshDateView();
	}

	public static String setDeviceStatusText(boolean check, int deviceId) {
		String deviceStatus = "";
		if (check) {
			switch (deviceId) {
			case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
			case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
			case DataHelper.ON_OFF_LIGHT_DEVICETYPE:
			case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
			case DataHelper.SHADE_DEVICETYPE:
				deviceStatus = "打开";
				break;
			case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
			case DataHelper.IAS_ZONE_DEVICETYPE:
				deviceStatus = "布防";
				break;
			default:
				deviceStatus = "打开";
				break;
			}
		} else {
			switch (deviceId) {
			case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
			case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
			case DataHelper.ON_OFF_LIGHT_DEVICETYPE:
			case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
			case DataHelper.SHADE_DEVICETYPE:
				deviceStatus = "关闭";
				break;
			case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
			case DataHelper.IAS_ZONE_DEVICETYPE:
				deviceStatus = "撤防";
				break;
			default:
				deviceStatus = "关闭";
				break;
			}
		}
		return deviceStatus;
	}

	public void setTimingAction(TimingAction action) {
		mTimingAction = action;
	}

	@Override
	public void deviceSelectedByDialog(int position) {
		// TODO Auto-generated method stub
		mDevicesSelected = mAddDevicesList.get(position);
		mTimingAction
				.setActionType(DataUtil
						.getTimingSceneActionParamType(mDevicesSelected
								.getmDeviceId()));
		mTimingAction.setIeee(mDevicesSelected.getmIeee());
		mTimingAction.setEp(mDevicesSelected.getmEP());
		mTimingAction.setDevicesStatus(0);
		refreshDeviceView();
	}

	public void refreshDeviceView() {
		mDeviceItem.setVisibility(View.VISIBLE);
		mNoDevice.setVisibility(View.GONE);
		mDeviceImage.setImageResource(Integer.parseInt(mDevicesSelected
				.getmPicName()));
		mDeviceName.setText(mDevicesSelected.getmDefaultDeviceName());
		mStatusDescribe.setText("设备状态：");
		mDeviceStatus.setText(setDeviceStatusText(mTimingAction
				.getDevicesStatus() == 1 ? true : false, mDevicesSelected.getmDeviceId()));
		mSwitchBtn.setChecked(mTimingAction.getDevicesStatus() == 1 ? true
				: false);
	}

	public void refreshDateView() {
		boolean repeat = false;
		for (boolean b : mTimingAction.getPara3Status()) {
			repeat = (repeat || b);
		}
		if (repeat) {
			datePicker.setVisibility(View.GONE);
			dateText.setVisibility(View.GONE);
			mTimingAction.setPara2(1);
		} else {
			datePicker.setVisibility(View.VISIBLE);
			dateText.setVisibility(View.VISIBLE);
			mTimingAction.setPara2(0);
		}
	}
}
