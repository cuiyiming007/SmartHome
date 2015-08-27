package com.gdgl.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.historydata.HistoryData;
import com.gdgl.model.historydata.HistoryPoint;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.smarthome.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class DeviceHistoryFragment extends Fragment implements UIListener {
	static Map<String, String> attributeNameMap = new HashMap<String, String>();
	static {
		attributeNameMap.put("电流", "current");
		attributeNameMap.put("电压", "voltage");
		attributeNameMap.put("功率", "power");
		attributeNameMap.put("电能", "energy");
		attributeNameMap.put("开关", "switch");
		attributeNameMap.put("布撤防", "bypass");
		attributeNameMap.put("告警", "warn");
		attributeNameMap.put("滑动开关", "level");
		attributeNameMap.put("温度", "temperature");
		attributeNameMap.put("湿度", "humidity");
		attributeNameMap.put("光强", "brightness");
		attributeNameMap.put("NULL", "null");
	}

	List<String> spinnerList = new ArrayList<String>();
	DevicesModel mDevices;
	String attrName;
	List<HistoryPoint> historyDataList;

	View mView;
	TextView nohistoryTextView;
	Spinner spinner;
	Button get_historyButton;
	LinearLayout dataTitleLayout;
	ListView dataListView;

	HistoryDataAdapter historyDataAdapter;
	CGIManager cgiManager;

	public DeviceHistoryFragment(DevicesModel devicesModel) {
		mDevices = devicesModel;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		cgiManager = CGIManager.getInstance();
		cgiManager.addObserver(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.history_fragment, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		nohistoryTextView = (TextView) mView.findViewById(R.id.nohistory);
		spinner = (Spinner) mView.findViewById(R.id.spinner);
		get_historyButton = (Button) mView.findViewById(R.id.begin_identify);
		dataTitleLayout = (LinearLayout) mView.findViewById(R.id.data_title);
		dataListView = (ListView) mView.findViewById(R.id.data_list);
		historyDataAdapter = new HistoryDataAdapter();
		dataListView.setAdapter(historyDataAdapter);

		setSpinnerList();
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				spinnerList);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				attrName = attributeNameMap.get(parent.getItemAtPosition(
						position).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		get_historyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cgiManager.getHistoryDataNum(mDevices.getmIeee(),
						mDevices.getmEP(), attrName, 10);
			}
		});
	}

	private void setSpinnerList() {
		int deviceId = mDevices.getmDeviceId();
		String modelId = mDevices.getmModelId();
		if (mDevices.getmCurrent() != null
				&& modelId.indexOf(DataHelper.Curtain_control_switch) != 0
				&& modelId
						.indexOf(DataHelper.Wireless_Intelligent_valve_switch) != 0) {
			spinnerList.add("电流");
			spinnerList.add("电压");
			spinnerList.add("功率");
			spinnerList.add("电能");
		}
		switch (deviceId) {
		case DataHelper.ON_OFF_SWITCH_DEVICETYPE:
		case DataHelper.RANGE_EXTENDER_DEVICETYPE:
		case DataHelper.DIMEN_SWITCH_DEVICETYPE:
		case DataHelper.IAS_ACE_DEVICETYPE:
		case DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE:
			spinnerList.add("NULL");
			spinner.setVisibility(View.GONE);
			get_historyButton.setVisibility(View.GONE);
			nohistoryTextView.setVisibility(View.VISIBLE);
			break;
		case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
		case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
		case DataHelper.ON_OFF_LIGHT_DEVICETYPE:
			spinnerList.add("开关");
			break;
		case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
		case DataHelper.SHADE_DEVICETYPE:
			spinnerList.add("滑动开关");
			break;
		case DataHelper.LIGHT_SENSOR_DEVICETYPE:
			spinnerList.add("光强");
			break;
		case DataHelper.TEMPTURE_SENSOR_DEVICETYPE:
			spinnerList.add("温度");
			spinnerList.add("湿度");
			break;
		case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
			spinnerList.add("布撤防");
			spinnerList.add("告警");
			break;
		case DataHelper.IAS_ZONE_DEVICETYPE:

			if (modelId.indexOf(DataHelper.Motion_Sensor) == 0
					|| modelId.indexOf(DataHelper.Magnetic_Window) == 0
					|| modelId
							.indexOf(DataHelper.Doors_and_windows_sensor_switch) == 0) { // ZigBee动作感应器,ZigBee窗磁,门窗感应开关
				spinnerList.add("布撤防");
				spinnerList.add("告警");
			}
			if (modelId.indexOf(DataHelper.Emergency_Button) == 0
					|| modelId.indexOf(DataHelper.Emergency_Button_On_Wall) == 0) { // ZigBee紧急按钮，ZigBee墙面紧急按钮
				spinnerList.add("NULL");
				spinner.setVisibility(View.GONE);
				get_historyButton.setVisibility(View.GONE);
				nohistoryTextView.setVisibility(View.VISIBLE);
			}
			if (modelId.indexOf(DataHelper.Smoke_Detectors) == 0
					|| modelId.indexOf(DataHelper.Combustible_Gas_Detector_Gas) == 0
					|| modelId.indexOf(DataHelper.Combustible_Gas_Detector_CO) == 0
					|| modelId
							.indexOf(DataHelper.Combustible_Gas_Detector_Natural_gas) == 0) { // 烟雾感应器，可燃气体探测器（煤气)器，可燃气体探测器（一氧化碳)，可燃气体探测器（天然气)
				spinnerList.add("告警");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cgiManager.deleteObserver(this);
	}

	public class HistoryDataAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (historyDataList != null) {
				return historyDataList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (historyDataList != null) {
				return historyDataList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View mView = convertView;
			ViewHolder mHolder;
			HistoryPoint point = historyDataList.get(position);

			if (null == mView) {
				mHolder = new ViewHolder();

				mView = LayoutInflater.from(getActivity()).inflate(
						R.layout.history_fragment_data_item, null);
				mHolder.data_value = (TextView) mView
						.findViewById(R.id.data_value);
				mHolder.data_time = (TextView) mView
						.findViewById(R.id.data_time);
				mView.setTag(mHolder);

			} else {
				mHolder = (ViewHolder) mView.getTag();
			}

			if (attrName.equals("switch") || attrName.equals("bypass")) {
				switch (mDevices.getmDeviceId()) {
				case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
				case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
				case DataHelper.ON_OFF_LIGHT_DEVICETYPE:
					mHolder.data_value
							.setText(point.getValue().equals("1") ? "开" : "关");
					break;
				case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
				case DataHelper.IAS_ZONE_DEVICETYPE:
					mHolder.data_value
							.setText(point.getValue().equals("1") ? "撤防" : "布防");
					break;
				default:
					break;
				}
			} else {
				mHolder.data_value.setText(point.getValue());
			}
			mHolder.data_time.setText(point.getTime());
			return mView;
		}

		public class ViewHolder {
			public TextView data_value;
			public TextView data_time;
		}
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		Event event = (Event) object;
		if (EventType.GETHISTORYDATA == event.getType()) {
			if (event.isSuccess() == true) {
				HistoryData historyData = (HistoryData) event.getData();
				if (historyData.getDeviceIeee().equals(mDevices.getmIeee())
						&& historyData.getDeviceEp().equals(mDevices.getmEP())) {
					if (historyData.getDataPoint().get(0).getAttrName()
							.equals(attrName)) {
						historyDataList = historyData.getDataPoint().get(0)
								.getPoint();
					}
					mView.post(new Runnable() {
						@Override
						public void run() {
							dataTitleLayout.setVisibility(View.VISIBLE);
							dataListView.setVisibility(View.VISIBLE);
							historyDataAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		}
	}
}
