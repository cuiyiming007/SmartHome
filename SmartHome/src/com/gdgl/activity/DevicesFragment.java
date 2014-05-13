package com.gdgl.activity;

import com.gdgl.adapter.GridviewAdapter;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class DevicesFragment extends Fragment{

	GridView content_view;
	View mView;
	ViewGroup nodevices;
	int[] types;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.main_fragment, null);
		initview();
		return mView;
	}

	private void initview() {
		// TODO Auto-generated method stub
		types = new int[] { UiUtils.LIGHTS_MANAGER, UiUtils.ELECTRICAL_MANAGER,
				UiUtils.SECURITY_CONTROL, UiUtils.ENVIRONMENTAL_CONTROL,
				UiUtils.ENERGY_CONSERVATION,UiUtils.OTHER };
		
		nodevices=(ViewGroup)mView.findViewById(R.id.nodevices);
		nodevices.setVisibility(View.GONE);
		content_view = (GridView) mView.findViewById(R.id.content_view);
		content_view.setLayoutAnimation(UiUtils.getAnimationController((Context)getActivity()));
		GridviewAdapter mGridviewAdapter = new GridviewAdapter(1,
				(Context) getActivity());
		content_view.setAdapter(mGridviewAdapter);
		content_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(
						ShowDevicesGroupFragmentActivity.ACTIVITY_SHOW_DEVICES_TYPE,
						position);
				intent.setClass(getActivity(),
						ShowDevicesGroupFragmentActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		content_view.setVisibility(View.GONE);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		content_view.setVisibility(View.VISIBLE);

		content_view.setLayoutAnimation(UiUtils.getAnimationController((Context)getActivity()));
		GridviewAdapter mGridviewAdapter = new GridviewAdapter(1,
				(Context) getActivity());
		content_view.setAdapter(mGridviewAdapter);
		super.onResume();
	}

}
