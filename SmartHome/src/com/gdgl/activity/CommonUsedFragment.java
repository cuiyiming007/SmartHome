package com.gdgl.activity;

import com.gdgl.adapter.GridviewAdapter;
import com.gdgl.smarthome.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
public class CommonUsedFragment extends Fragment {

	GridView content_view;
	View mView;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
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
		mView=inflater.inflate(R.layout.no_devices, null);
		initview();
		return mView;
	}

	private void initview() {
		// TODO Auto-generated method stub
//		content_view=(GridView)mView.findViewById(R.id.content_view);
//		content_view.setNumColumns(4);
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
