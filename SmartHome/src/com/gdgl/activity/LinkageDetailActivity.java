package com.gdgl.activity;


import java.util.ArrayList;
import java.util.List;

import com.gdgl.activity.BaseControlFragment.UpdateDevice;
import com.gdgl.activity.DevicesListFragment.refreshData;
import com.gdgl.activity.DevicesListFragment.setData;
import com.gdgl.adapter.SceneDevicesAdapter;
import com.gdgl.adapter.SceneDevicesAdapter.AddChecked;
import com.gdgl.adapter.SceneDevicesListAdapter;
import com.gdgl.adapter.SceneDevicesListAdapter.DevicesObserver;
import com.gdgl.model.DevicesGroup;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.DispatchOperator;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.UiUtils;
import com.gdgl.util.EditDevicesDlg.EditDialogcallback;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LinkageDetailActivity extends FragmentActivity implements DevicesObserver,
		AddChecked, refreshData, UpdateDevice,EditDialogcallback,Dialogcallback,setData {
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.linkage_detail_activity);

	}

	@Override
	public void setdata(List<DevicesModel> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveedit(DevicesModel mDevicesModel, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveDevicesName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshListData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DevicesModel getDeviceModle(int postion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFragment(Fragment mFragment, int postion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDevicesId(DevicesModel DevicesModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AddCheckedDevices(DevicesGroup s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeletedCheckedDevices(DevicesGroup s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLayout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteDevices(String id) {
		// TODO Auto-generated method stub
		
	}
	
	
}
