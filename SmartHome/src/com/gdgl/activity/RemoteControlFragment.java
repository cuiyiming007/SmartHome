package com.gdgl.activity;

import java.util.Calendar;

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

public class RemoteControlFragment extends BaseControlFragment {

	View mView;
	SimpleDevicesModel mDevices;

	TextView mTime;
	ImageView on_off_remote;
	private String AM_PM = "";
	private int hour;
	private int min;
	private int sec;
	
	boolean b=false;
	
	@Override
	public void editDevicesName() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub

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
		mView = inflater.inflate(R.layout.remote_control, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
//		mTime = (TextView) mView.findViewById(R.id.text_time);
//		mTime.setText(getTime());
//		handler.sendEmptyMessage(0);
		
		on_off_remote=(ImageView)mView.findViewById(R.id.on_off_remote);
		on_off_remote.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("tag", "zgtagzgs->onclick");
				ImageView mImageView=(ImageView)v;
				if(!b){
					mImageView.setImageResource(R.drawable.kongtao_on_small);
				}else{
					mImageView.setImageResource(R.drawable.kongtao_off_small);
				}
				b=!b;
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private String getTime() {
		Calendar c = Calendar.getInstance();
		if (c.get(Calendar.AM) == 0) {
			AM_PM = "AM";
		} else {
			AM_PM = "PM";
		}
		hour = c.get(Calendar.HOUR);
		min = c.get(Calendar.MINUTE);
		sec = c.get(Calendar.SECOND);
		String s = AM_PM + " " + hour + ":" + min + ":" + sec;
		System.out.println(s);
		return s;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Message msg1 = handler.obtainMessage();
				msg1.what = 1;
				// 延迟1000毫秒发送
				handler.sendMessageDelayed(msg1, 1000);
				mTime.setText(getTime());
				break;
			case 1:
				Message msg2 = handler.obtainMessage();
				msg2.what = 0;
				handler.sendMessageDelayed(msg2, 1000);
				mTime.setText(getTime());
				break;

			default:
				break;
			}
		};
	};

}
