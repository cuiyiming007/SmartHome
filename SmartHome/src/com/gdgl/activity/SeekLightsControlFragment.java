package com.gdgl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.gdgl.manager.Manger;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.smarthome.R;

public class SeekLightsControlFragment extends BaseControlFragment {
	SeekBar devices_seek;
	ImageView devices_img;
	TextView text_process;
	View mView;
	
	SimpleDevicesModel mDevices;
	
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
				text_process.setText(progress+"%");
				setDevicesImg(progress);
			}
		});
	}

	@Override
	public void editDevicesName() {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub

	}

}
