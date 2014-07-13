
package com.gdgl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter.LengthFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.SimpleResponseData;
import com.gdgl.smarthome.R;

public class TemperatureSensorFragment extends BaseControlFragment {

	View mView;
	SimpleDevicesModel mDevices;

	TextView txt_devices_name, txt_devices_region, txt_title, txt_value;

	SeekBar mSeekBar;

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
		mView = inflater.inflate(R.layout.data_show, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		CGIManager.getInstance().addObserver(this);
		mSeekBar = (SeekBar) mView.findViewById(R.id.devices_seek);
		mSeekBar.setEnabled(false);
        CGIManager.getInstance().temperatureSensorOperation(mDevices,0);
		// txt_devices_name, txt_devices_region,txt_title,txt_value
		txt_devices_name = (TextView) mView.findViewById(R.id.txt_devices_name);
		txt_devices_region = (TextView) mView
				.findViewById(R.id.txt_devices_region);

		txt_title = (TextView) mView.findViewById(R.id.text_title);
		txt_value = (TextView) mView.findViewById(R.id.text_value);

		if (mDevices.getmModelId()
				.indexOf(DataHelper.Indoor_temperature_sensor) == 0) {
			txt_title.setText("当前室内温度:");
		} else if (mDevices.getmModelId().indexOf(DataHelper.Light_Sensor) == 0) {
			txt_title.setText("当前室内亮度:");
		}

		txt_value.setText("30");

		txt_devices_name.setText(mDevices.getmUserDefineName().trim());
		txt_devices_region.setText(mDevices.getmDeviceRegion().trim());
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		CGIManager.getInstance().deleteObserver(this);
		super.onDestroy();
	}

	@Override
	public void editDevicesName() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		if(null!=mDialog){
			mDialog.dismiss();
			mDialog=null;
		}
		final Event event = (Event) object;
		if (EventType.TEMPERATURESENSOROPERATION == event.getType()) {
			// data maybe null
			if (event.isSuccess()) {
				
				SimpleResponseData data = (SimpleResponseData) event.getData();
				
				Toast.makeText(getActivity(), "当前温度"+Float.valueOf(data.getParam1())/1000,3000).show();
			}else {
				Toast.makeText(getActivity(), "获取温度失败",3000).show();
			}
		}

	}

}
