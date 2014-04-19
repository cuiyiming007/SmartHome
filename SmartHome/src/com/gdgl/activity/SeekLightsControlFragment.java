package com.gdgl.activity;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.gdgl.manager.LightManager;
import com.gdgl.manager.Manger;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.SimpleResponseData;
import com.gdgl.smarthome.R;

public class SeekLightsControlFragment extends BaseControlFragment {
	SeekBar devices_seek;
	ImageView devices_img;
	TextView text_process;
	View mView;
	
	TextView txt_devices_region,txt_devices_name;
	
	SimpleDevicesModel mDevices;
	
	int currentProgress=0;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		Bundle extras = getArguments();
		if (null != extras) {
			mDevices = (SimpleDevicesModel) extras
					.getParcelable(DevicesListFragment.PASS_OBJECT);
		}
		initstate();
		
	}

	private void initstate() {
		// TODO Auto-generated method stub
		
	}

	private void setDevicesImg(int i) {
		// TODO Auto-generated method stub
		if(i>=0 && i<10){
			devices_img.setImageResource(R.drawable.light_seek_0);
		}else if(i>=10 && i<20){
			devices_img.setImageResource(R.drawable.light_seek_10);
		}else if(i>=20 && i<30){
			devices_img.setImageResource(R.drawable.light_seek_20);
		}
		else if(i>=30 && i<40){
			devices_img.setImageResource(R.drawable.light_seek_30);
		}
		else if(i>=40 && i<50){
			devices_img.setImageResource(R.drawable.light_seek_40);
		}
		else if(i>=50 && i<60){
			devices_img.setImageResource(R.drawable.light_seek_50);
		}
		else if(i>=60 && i<70){
			devices_img.setImageResource(R.drawable.light_seek_60);
		}
		else if(i>=70 && i<80){
			devices_img.setImageResource(R.drawable.light_seek_70);
		}
		else if(i>=80 && i<90){
			devices_img.setImageResource(R.drawable.light_seek_80);
		}
		else if(i>=90 && i<100){
			devices_img.setImageResource(R.drawable.light_seek_90);
		}else{
			devices_img.setImageResource(R.drawable.light_seek_100);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		LightManager.getInstance().addObserver(this);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView=inflater.inflate(R.layout.seek_light_control,null);
		initview();
		return mView;
	}

	private void initview() {
		// TODO Auto-generated method stub
		devices_seek=(SeekBar)mView.findViewById(R.id.devices_seek);
		devices_img=(ImageView)mView.findViewById(R.id.devices_img);
		text_process=(TextView)mView.findViewById(R.id.text_process);
		text_process.setText("0%");
		devices_seek.setProgress(0);
		setDevicesImg(0);
		
		
		txt_devices_region=(TextView)mView.findViewById(R.id.txt_devices_region);
		txt_devices_name=(TextView)mView.findViewById(R.id.txt_devices_name);
		
		txt_devices_region.setText(mDevices.getmDeviceRegion().trim());
		txt_devices_name.setText(mDevices.getmNodeENNAme().trim());
		
		devices_seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				currentProgress=progress;
				text_process.setText(progress+"%");
				LightManager.getInstance().dimmableLightOperation(mDevices,progress/10);
				
				
			}
		});
	}

	@Override
	public void editDevicesName() {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(Manger observer, Object object) {
		final Event event = (Event) object;
		if (EventType.ONOFFOUTPUTOPERATION == event.getType()&&event.isSuccess()==true) {
			
			if (event.isSuccess()==true) {
				// data maybe null
				SimpleResponseData data = (SimpleResponseData) event.getData();
				//  refresh UI data
				setDevicesImg(currentProgress);
			}else {
				//if failed,prompt a Toast
			}
		}

	}
	@Override
	public void onDestroy() {
		LightManager.getInstance().deleteObserver(this);
		super.onDestroy();
	}
	class operatortype {
		public static final int TurnOff = 0;
		
		public static final int TurnOn = 1;
		
		public static final int Toggle = 2;
		public static final int GetOnOffStatus = 3;
		public static final int GetLevel = 4;
		public static final int MoveToLevel = 7;
		public static final int LevelStepUp = 8;
		public static final int LevelStepDown = 9;
		public static final int Move = 13;
	}
}
