package com.gdgl.activity;


import com.gdgl.manager.NodeManager;
import com.gdgl.smarthome.R;
import com.gdgl.util.NetUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class RegionFragment extends Fragment {
	
	Button testButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.region_layout, container, false);
		testButton=(Button) view.findViewById(R.id.testBtn);
		testButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				NetUtil.addStringRequest();
				NodeManager.getInstance().getNodeFromValley();
			}
		});
		// TODO Auto-generated method stub
		return view;
	}

}
