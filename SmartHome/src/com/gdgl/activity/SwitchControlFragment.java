package com.gdgl.activity;

import com.gdgl.smarthome.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class SwitchControlFragment extends Fragment {

	private boolean[] mBoolean = { false, false, false };
	private String switch_state_string = null;
	private int mSwitchId = -1;
	private View mView;

	private int mCount;

	public static final String SWITCH_ID = "switch_id";
	public static final String BOLLEAN_ARRARY = "switch_state";
	
	private removeSwitchControlFragment mremoveSwitchControlFragment;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Bundle extras = getArguments();
		if (null != extras) {
			mSwitchId = extras.getInt(SWITCH_ID, -1);
			switch_state_string = extras.getString(BOLLEAN_ARRARY, "");
		}
		initstate();
	}

	private void initstate() {
		// TODO Auto-generated method stub
		char[] mChar = null;
		if (null != switch_state_string) {
			mChar = switch_state_string.toCharArray();
			mCount = mChar.length;
			for (int m = 0; m < mCount; m++) {
				if('1'==mChar[m])
				{
					mBoolean[m]=true;
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.switch_control, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		CheckBox mSwitch1 = (CheckBox) mView.findViewById(R.id.switch_state1);
		CheckBox mSwitch2 = (CheckBox) mView.findViewById(R.id.switch_state2);
		CheckBox mSwitch3 = (CheckBox) mView.findViewById(R.id.switch_state3);
		
		TextView mSwichName1=(TextView)mView.findViewById(R.id.switch_name1);
		TextView mSwichName2=(TextView)mView.findViewById(R.id.switch_name2);
		TextView mSwichName3=(TextView)mView.findViewById(R.id.switch_name3);
		
		RelativeLayout viewGroup1 = (RelativeLayout) mView
				.findViewById(R.id.switch_group1);
		RelativeLayout viewGroup2 = (RelativeLayout) mView
				.findViewById(R.id.switch_group2);

		switch (mCount) {
		case 1:
			viewGroup1.setVisibility(View.VISIBLE);
			viewGroup2.setVisibility(View.GONE);
			mSwitch1.setChecked(mBoolean[0]);
			break;
		case 2:
			viewGroup1.setVisibility(View.GONE);
			viewGroup2.setVisibility(View.VISIBLE);
			mSwitch2.setChecked(mBoolean[0]);
			mSwitch3.setChecked(mBoolean[1]);
			
			mSwichName2.setText("开关1");
			mSwichName3.setText("开关2");
			break;
		case 3:
			mSwitch1.setChecked(mBoolean[0]);
			mSwitch2.setChecked(mBoolean[1]);
			mSwitch3.setChecked(mBoolean[2]);
			break;
		default:
			break;
		}

		RelativeLayout back = (RelativeLayout) mView.findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mremoveSwitchControlFragment.removeFragment(SwitchControlFragment.this);
			}
		});
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mremoveSwitchControlFragment=(removeSwitchControlFragment)activity;
	}


	public interface removeSwitchControlFragment{
		public void removeFragment(Fragment fg);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
