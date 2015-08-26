package com.gdgl.activity;

import com.gc.materialdesign.views.CheckBox;
import com.gc.materialdesign.views.CheckBox.OnCheckListener;
import com.gdgl.activity.JoinNetFragment.ChangeFragment;
import com.gdgl.drawer.DeviceTabFragment;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOKOnlyDlg;
import com.gdgl.util.VersionDlg;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConfigurationFragment extends Fragment {
	ChangeFragment changeFragment;

	View mView;
	TextView changeAlias, changePassword, setemail, feedBack, aboutApp;

	// CheckBox enableIPC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		changeFragment = (ChangeFragment) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getFromSharedPreferences.setsharedPreferences(getActivity());

		mView = inflater.inflate(R.layout.configurationfragment, null);
		changeAlias = (TextView) mView.findViewById(R.id.changealias);
		changePassword = (TextView) mView.findViewById(R.id.changepassword);
		setemail = (TextView) mView.findViewById(R.id.setemail);//===王晓飞===设置邮箱--20150820
		feedBack = (TextView) mView.findViewById(R.id.feedback);
		aboutApp = (TextView) mView.findViewById(R.id.aboutapp);
		final CheckBox enableIPC = (CheckBox) mView
				.findViewById(R.id.enableipc);

		changeAlias.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					changeFragment.setFragment(new ChangeNameFragment());
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(getActivity());
					myOKOnlyDlg.setContent(getResources().getString(
							R.string.Unable_In_InternetState));
					myOKOnlyDlg.show();
				}
			}
		});
		changeAlias.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					changeAlias.setBackgroundColor(getResources().getColor(
							R.color.myDrawerBackground));
					return false;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					changeAlias.setBackgroundColor(Color.TRANSPARENT);
					return false;
				}
				return true;
			}
		});
		changePassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					changeFragment.setFragment(new ChangePWDFragment());
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(getActivity());
					myOKOnlyDlg.setContent(getResources().getString(
							R.string.Unable_In_InternetState));
					myOKOnlyDlg.show();
				}
			}
		});
		changePassword.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					changePassword.setBackgroundColor(getResources().getColor(
							R.color.myDrawerBackground));
					return false;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					changePassword.setBackgroundColor(Color.TRANSPARENT);
					return false;
				}
				return true;
			}
		});
		//=====王晓飞===设置邮箱==
		setemail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					changeFragment.setFragment(new SetEmailFragment());
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(getActivity());
					myOKOnlyDlg.setContent(getResources().getString(
							R.string.Unable_In_InternetState));
					myOKOnlyDlg.show();
				}
			}
		});
		setemail.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					setemail.setBackgroundColor(getResources().getColor(
							R.color.myDrawerBackground));
					return false;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					setemail.setBackgroundColor(Color.TRANSPARENT);
					return false;
				}
				return true;
			}
		});
		
		feedBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeFragment.setFragment(new FeedbackFragment());
			}
		});
		feedBack.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					feedBack.setBackgroundColor(getResources().getColor(
							R.color.myDrawerBackground));
					return false;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					feedBack.setBackgroundColor(Color.TRANSPARENT);
					return false;
				}
				return true;
			}
		});
		aboutApp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				VersionDlg vd = new VersionDlg(getActivity());
				vd.show();
			}
		});
		aboutApp.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					aboutApp.setBackgroundColor(getResources().getColor(
							R.color.myDrawerBackground));
					return false;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					aboutApp.setBackgroundColor(Color.TRANSPARENT);
					return false;
				}
				return true;
			}
		});
		enableIPC.setOncheckListener(new OnCheckListener() {

			@Override
			public void onCheck(boolean check) {
				// TODO Auto-generated method stub
				DeviceTabFragment.ENABLE_VEDIO = check;
				getFromSharedPreferences.setEnableIPC(check);
			}
		});
		enableIPC.post(new Runnable() {
			@Override
			public void run() {
				enableIPC.setChecked(DeviceTabFragment.ENABLE_VEDIO);
			}
		});
		return mView;
	}
}
