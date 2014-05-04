package com.gdgl.activity;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.mime.MinimalField;

import com.gdgl.activity.BaseControlFragment.UpdateDevice;
import com.gdgl.activity.DevicesListFragment.refreshData;
import com.gdgl.adapter.AllDevicesAdapter;
import com.gdgl.adapter.AllDevicesAdapter.AddChecked;
import com.gdgl.adapter.DevicesBaseAdapter;
import com.gdgl.adapter.DevicesBaseAdapter.DevicesObserver;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.UiUtils;
import com.gdgl.util.EditDevicesDlg.EditDialogcallback;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegionDevicesActivity extends Activity implements DevicesObserver,
		AddChecked, refreshData, UpdateDevice, EditDialogcallback,
		Dialogcallback {
	public static final String REGION_NAME = "region_name";

	private String mRegion = "";

	String where = " device_region=? ";

	List<SimpleDevicesModel> mList;

	List<SimpleDevicesModel> mAddList;
	
	List<SimpleDevicesModel> mAddToRegionList;

	DataHelper mDh;

	FragmentManager fragmentManager;

	DevicesListFragment mDevicesListFragment;
	
	AllDevicesFragment mAllDevicesFragment;

	DevicesBaseAdapter mDevicesBaseAdapter;

	TextView mNoDevices,region_name;
	Button mAdd,delete;
	
	DataHelper mDataHelper;
	
	private String devicesIeee = "";
	private boolean deleteType=false;
	private boolean isAdd=false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_region);

		Intent i = getIntent();
		if (null != i) {
			Bundle extras = i.getExtras();
			if (null != extras) {
				mRegion = extras.getString(REGION_NAME, "");
			}
		}
		mDataHelper=new DataHelper(RegionDevicesActivity.this);
		initData();
		initView();
	}
	
	
	private void initRegionDevicesList(){
		mList=null;
		String[] args = { mRegion };
		if (!mRegion.trim().equals("")) {
			mDh = new DataHelper(RegionDevicesActivity.this);
			mList = DataUtil.getDevices(RegionDevicesActivity.this, mDh, args,
					where);
		}
	}
	
	private void initAddFragmentDevicesList(){
		mAddList=null;
		List<SimpleDevicesModel> mTempList = DataUtil.getDevices(
				RegionDevicesActivity.this, mDh, null, null);
		if (null == mList || mList.size() == 0) {
			mAddList = mTempList;
		} else {
			mAddList = new ArrayList<SimpleDevicesModel>();
			for (SimpleDevicesModel simpleDevicesModel : mTempList) {
				if (!isInList(simpleDevicesModel)) {
					mAddList.add(simpleDevicesModel);
				}
			}
		}
	}
	
	
	private boolean isInList(SimpleDevicesModel simpleDevicesModel){
		
		for (SimpleDevicesModel msimpleDevicesModel : mList) {
			if (msimpleDevicesModel.getmIeee().equals(
					simpleDevicesModel.getmIeee())) {
				return true;
			}
		}
		return false;
	}
	
	private void initAddToRegionDevicesList(){
		mAddToRegionList=new ArrayList<SimpleDevicesModel>();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		mNoDevices = (TextView) findViewById(R.id.no_devices);
		mAdd = (Button) findViewById(R.id.add_devices);
		delete = (Button) findViewById(R.id.delete);
		region_name=(TextView) findViewById(R.id.region_name);
		
		region_name.setText(mRegion);
		
		if(null!=mList && mList.size()>0){
			mNoDevices.setVisibility(View.GONE);
			initDevicesListFragment();
		}
		
		mAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isAdd){
					isAdd=true;
					mAdd.setText("添加");
					mAdd.setTextColor(Color.RED);
					initRegionDevicesList();
					initAddFragmentDevicesList();
					mAllDevicesFragment = new AllDevicesFragment();
					AllDevicesAdapter mAllDevicesAdapter = new AllDevicesAdapter(
							RegionDevicesActivity.this, mAddList,
							RegionDevicesActivity.this);
					mAllDevicesFragment.setAdapter(mAllDevicesAdapter);
					setFragment(mAllDevicesFragment, 0);
				}else{
					isAdd=false;
					String[] args = { mRegion };
					ContentValues c = new ContentValues();
					c.put(DevicesModel.DEVICE_REGION, mRegion);
					for (SimpleDevicesModel s : mAddToRegionList) {
						updateDevices(s.getmIeee(), s.getmEP(), c);
					}
					mAddToRegionList.clear();
					initRegionDevicesList();
					mDevicesBaseAdapter.setList(mList);
					mDevicesBaseAdapter.notifyDataSetChanged();
					fragmentManager.popBackStack();
					initDevicesListFragment();
				}
			}
		});

		LinearLayout mBack = (LinearLayout) findViewById(R.id.goback);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
						RegionDevicesActivity.this);
				mMyOkCancleDlg.setDialogCallback(RegionDevicesActivity.this);
				mMyOkCancleDlg.setContent("确定要删除区域  " + mRegion + " 吗?");
				mMyOkCancleDlg.show();
				deleteType=true;
			}
		});
		
	}

	private void initData() {
		// TODO Auto-generated method stub
		
		fragmentManager = getFragmentManager();
		
		initRegionDevicesList();
		initAddToRegionDevicesList();
		mDevicesBaseAdapter = new DevicesBaseAdapter(
				RegionDevicesActivity.this, mList, this);
	}

	private void initDevicesListFragment() {
		// TODO Auto-generated method stub
		mAdd.setText("添加设备");
		mAdd.setTextColor(Color.BLACK);
		if(null==mList || mList.size()==0){
			mNoDevices.setVisibility(View.VISIBLE);
		}else{
			mNoDevices.setVisibility(View.GONE);
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			mDevicesListFragment = new DevicesListFragment();
			fragmentTransaction.replace(R.id.devices_control_fragment,
					mDevicesListFragment, "LightsControlFragment");
			mDevicesListFragment.setAdapter(mDevicesBaseAdapter);
			fragmentTransaction.commit();
		}
	}

	@Override
	public void setLayout() {
		// TODO Auto-generated method stub
		mDevicesListFragment.setLayout();
	}

	@Override
	public void deleteDevices(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void AddCheckedDevices(SimpleDevicesModel s) {
		// TODO Auto-generated method stub
		if(!mAddToRegionList.contains(s)){
			mAddToRegionList.add(s);
		}
	}

	@Override
	public void DeletedCheckedDevices(SimpleDevicesModel s) {
		// TODO Auto-generated method stub
		if(mAddToRegionList.contains(s)){
			mAddToRegionList.remove(s);
		}
	}

	@Override
	public void refreshListData() {
		// TODO Auto-generated method stub
		new GetDataTask().execute();
	}

	@Override
	public SimpleDevicesModel getDeviceModle(int postion) {
		// TODO Auto-generated method stub
		if (null != mList) {
			return mList.get(postion);
		}
		return null;
	}

	@Override
	public void setFragment(Fragment mFragment, int postion) {
		// TODO Auto-generated method stub
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.devices_control_fragment, mFragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	@Override
	public void setDevicesId(String id) {
		// TODO Auto-generated method stub
		devicesIeee = id;
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, List<SimpleDevicesModel>> {

		@Override
		protected List<SimpleDevicesModel> doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return mList;
		}

		@Override
		protected void onPostExecute(List<SimpleDevicesModel> result) {
			super.onPostExecute(result);
			if(isAdd){
				mAllDevicesFragment.stopRefresh();
			}else{
				mDevicesListFragment.stopRefresh();
			}
			
		}
	}

	@Override
	public void saveDevicesName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updateDevices(String Ieee, String ep, ContentValues c) {
		// TODO Auto-generated method stub
		int result=0;
		String[] eps={ep};
		if(ep.contains(",")){
			eps=ep.trim().split(",");
		} 
		for (String string : eps) {
			String wheres = " ieee = ? and ep = ?";
			String[] args = { Ieee ,string };
			SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
			int temp = mDataHelper.update(mSQLiteDatabase,
					DataHelper.DEVICES_TABLE, c, wheres, args);
			result=temp>result?temp:result;
		}
		if (result >= 0) {
			return true;
		}
		return false;
	}


	@Override
	public void saveedit(String ieee, String ep, String name) {
		// TODO Auto-generated method stub
		String where = " ieee = ? ";
		String[] args = { ieee };

		ContentValues c = new ContentValues();
		c.put(DevicesModel.USER_DEFINE_NAME, name);
//		c.put(DevicesModel.DEVICE_REGION, region);
		
		SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
		int result = mDataHelper.update(mSQLiteDatabase,
				DataHelper.DEVICES_TABLE, c, where, args);
		if (result >= 0) {
			initRegionDevicesList();
			mDevicesBaseAdapter.setList(mList);
			mDevicesBaseAdapter.notifyDataSetChanged();
			if(null==mList || mList.size()==0){
				mNoDevices.setVisibility(View.VISIBLE);
			}
		}
	}


	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		if(deleteType){
			String where = " ieee = ? ";
			for (SimpleDevicesModel s : mList) {
				ContentValues c = new ContentValues();
				c.put(DevicesModel.DEVICE_REGION, "");
				String[] args = { s.getmIeee() };
				SQLiteDatabase mSQLiteDatabase = mDataHelper
						.getSQLiteDatabase();
				mDataHelper.update(mSQLiteDatabase, DataHelper.DEVICES_TABLE,
						c, where, args);
			}
			List<String> mregions = new ArrayList<String>();
			getFromSharedPreferences
					.setharedPreferences(RegionDevicesActivity.this);
			String[] mregion = null;
			String reg=getFromSharedPreferences.getRegion();
			if(null!=reg && !reg.trim().equals("")){
				mregion=reg.split("@@");
			}
			if(null!=mregion){
				for (String string : mregion) {
		        	if(!string.equals("")){
		        		mregions.add(string);
		        	}
				}
			}
			int index=0;
			for (; index < mregions.size(); index++) {
				if(mregions.get(index).trim().equals(mRegion)){
					break;
				}
			}
			mregions.remove(index);
			StringBuilder ms = new StringBuilder();
			for (String string : mregions) {
				ms.append(string + "@@");
			}
			getFromSharedPreferences.setRegion(ms.toString());

			List<String> mreg=new ArrayList<String>();
 			String comm = getFromSharedPreferences.getCommonUsed();
			if (null != comm && !comm.trim().equals("")) {
				String[] result = comm.split("@@");
				for (String string : result) {
					if (!string.trim().equals("")) {
						mreg.add(string);
					}
				}
			}
			if(mreg.contains(UiUtils.REGION_FLAG+mRegion)){
				mreg.remove(UiUtils.REGION_FLAG+mRegion);
				StringBuilder sb=new StringBuilder();
				if(null!=mreg && mreg.size()>0){
					for (String s : mreg) {
						sb.append(s+"@@");
					}
				}else{
					sb.append("");
				}
				getFromSharedPreferences.setCommonUsed(sb.toString());
			}
			this.finish();
		}else{
			String where = " ieee = ? ";
			String[] args = { devicesIeee };

			ContentValues c = new ContentValues();
			c.put(DevicesModel.DEVICE_REGION, "");
			
			SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
			int result = mDataHelper.update(mSQLiteDatabase,
					DataHelper.DEVICES_TABLE, c, where, args);
			if (result >= 0) {
				initRegionDevicesList();
				mDevicesBaseAdapter.setList(mList);
				mDevicesBaseAdapter.notifyDataSetChanged();
				if(null==mList || mList.size()==0){
					mNoDevices.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(isAdd){
			isAdd=false;
			fragmentManager.popBackStack();
			initDevicesListFragment();
		}else{
			if(fragmentManager.getBackStackEntryCount()>0){
				fragmentManager.popBackStack();
			}else{
				finish();
			}
		}
	}
	
	
}
