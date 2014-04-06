package com.gdgl.activity;

import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class firstFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub //devices_manager
		View mView;
		mView = inflater.inflate(R.layout.first_fragment, null);
		LinearLayout devices_manager=(LinearLayout)mView.findViewById(R.id.devices_manager);
		devices_manager.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra(ShowDevicesGroupFragmentActivity.ACTIVITY_SHOW_DEVICES_TYPE, UiUtils.DEVICES_MANAGER);
				intent.setClass(getActivity(), ShowDevicesGroupFragmentActivity.class);
				startActivity(intent);
			}
		});
		return mView;
	}

}
