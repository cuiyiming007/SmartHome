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
import com.gdgl.model.SimpleDevicesModel;
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
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SceneDevicesActivity extends Activity implements DevicesObserver,
		AddChecked, refreshData, UpdateDevice,EditDialogcallback,Dialogcallback,setData {
	public static final String SCENE_NAME = "scene_name";

	private String mScene = "";

	String where = " device_region=? ";

	List<DevicesModel> mList;

	List<DevicesModel> mAddList;
	
	List<DevicesGroup> mAddToSceneList;

	DataHelper mDh;

	FragmentManager fragmentManager;

	DevicesListFragment mDevicesListFragment;
	
	AllSceneDevicesFragment mAllDevicesFragment;

	SceneDevicesListAdapter mDevicesBaseAdapter;

	TextView mNoDevices,region_name;
	Button mAdd,delete;
	DataHelper mDataHelper;
	
	private DevicesModel getModel;
	private boolean deleteType=false;
	private boolean isAdd=false;
	CheckBox mSceneOn;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_scene);

		Intent i = getIntent();
		if (null != i) {
			Bundle extras = i.getExtras();
			if (null != extras) {
				mScene = extras.getString(SCENE_NAME, "");
			}
		}
		mDataHelper=new DataHelper(SceneDevicesActivity.this);
		initData();
		initView();
	}
	
	
	private void initSceneDevicesList(){
		mList=null;
		if (!mScene.trim().equals("")) {
			mDh = new DataHelper(SceneDevicesActivity.this);
			mList = DataUtil.getScenesDevices(SceneDevicesActivity.this, mDh, mScene);
		}
	}
	
	private void initAddFragmentDevicesList(){
		mAddList=null;
		mAddList=new ArrayList<DevicesModel>();
		List<DevicesModel> ls=new ArrayList<DevicesModel>();
		ls =  mDh.queryForDevicesList(mDh.getSQLiteDatabase(),
				DataHelper.DEVICES_TABLE, null, null, null, null, null,
				null, null);
		for (DevicesModel mModel : ls) {
			if(!isInList(mModel)){
				mAddList.add(mModel);
			}
		}
	}
	

	private boolean isInList(DevicesModel mModel){
		
		for (DevicesModel msimpleDevicesModel : mList) {
			if(msimpleDevicesModel.getmIeee().equals(mModel.getmIeee())){
				return true;
			}
		}
		return false;
	}
	
	private void initAddToRegionDevicesList(){
		mAddToSceneList=new ArrayList<DevicesGroup>();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		mNoDevices = (TextView) findViewById(R.id.no_devices);
		mAdd = (Button) findViewById(R.id.add_devices);
		delete = (Button) findViewById(R.id.delete);
		region_name=(TextView) findViewById(R.id.region_name);
		
		mSceneOn=(CheckBox)findViewById(R.id.SceneOn);
		
		mSceneOn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				initSceneDevicesList();
				SceneDevicesActivity.OperatorDevices so;
				List<SceneDevicesActivity.OperatorDevices> ml=new ArrayList<SceneDevicesActivity.OperatorDevices>();
				if(isChecked){
					for (DevicesModel sd : mList) {
						DevicesGroup ds=getModelByID(sd.getmIeee());
						if(null!=ds){
							so=new SceneDevicesActivity.OperatorDevices(ds.getDevicesState(),ds.getDevicesValue(),sd);
							ml.add(so);
						}
					}
				}else{
					for (DevicesModel sd : mList) {
						DevicesGroup ds=getModelByID(sd.getmIeee());
						if(null!=ds){
							so=new SceneDevicesActivity.OperatorDevices(!ds.getDevicesState(),0,sd);
							ml.add(so);
						}
					}
				}
				DispatchOperator dp=new DispatchOperator(SceneDevicesActivity.this,ml);
				dp.operator();
				ContentValues cv;
				String where=" group_name=? ";
				String[] args={mScene};
				cv=new ContentValues();
				cv.put(DevicesGroup.GROUP_STATE, isChecked? 1: 0);
				mDataHelper.update(mDataHelper.getSQLiteDatabase(), DataHelper.GROUP_TABLE, cv, where, args);
				if(isChecked){
					mSceneOn.setText("启用");
				}else{
					mSceneOn.setText("未启用");
				}
			}
		});
		
		region_name.setText(mScene);
		
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
					initSceneDevicesList();
					initAddFragmentDevicesList();
					mAllDevicesFragment=new AllSceneDevicesFragment();
					SceneDevicesAdapter mSceneDevicesAdapter=new SceneDevicesAdapter(SceneDevicesActivity.this, mAddList, SceneDevicesActivity.this,mScene);
					mAllDevicesFragment.setAdapter(mSceneDevicesAdapter);
					setFragment(mAllDevicesFragment, 0);
				}else{
					isAdd=false;
					for (DevicesGroup s : mAddToSceneList) {
						DevicesModel sd=new DevicesModel();
						updateDevices(sd, s.convertContentValues());
					}
					mAddToSceneList.clear();
					initSceneDevicesList();
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
						SceneDevicesActivity.this);
				mMyOkCancleDlg.setDialogCallback(SceneDevicesActivity.this);
				mMyOkCancleDlg.setContent("确定要删除场景  "
						+ mScene + " 吗?");
				mMyOkCancleDlg.show();
				deleteType=true;
			}
		});
	}
	
	private DevicesGroup getModelByID(String ieee){
		return DataUtil.getOneScenesDevices(SceneDevicesActivity.this, mDh, ieee);
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		
		fragmentManager = getFragmentManager();
		
		initSceneDevicesList();
		initAddToRegionDevicesList();
		mDevicesBaseAdapter = new SceneDevicesListAdapter(
				SceneDevicesActivity.this, mList, this);
	}

	private void initDevicesListFragment() {
		// TODO Auto-generated method stub
		mAdd.setText("添加设备");
		mAdd.setTextColor(Color.BLACK);
		if(null==mList || mList.size()==0){
			mNoDevices.setVisibility(View.VISIBLE);
		}else{
			mNoDevices.setVisibility(View.GONE);
			Bundle extras = new Bundle();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			extras.putInt(Constants.OPERATOR, DevicesListFragment.WITHOUT_OPERATE);
			mDevicesListFragment = new DevicesListFragment();
			mDevicesListFragment.setArguments(extras);
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
	public void AddCheckedDevices(DevicesGroup s) {
		// TODO Auto-generated method stub
		Log.i(SCENE_NAME, "zgs->AddCheckedDevices");
		if(!mAddToSceneList.contains(s)){
			mAddToSceneList.add(s);
		}
	}

	@Override
	public void DeletedCheckedDevices(DevicesGroup s) {
		// TODO Auto-generated method stub
		Log.i(SCENE_NAME, "zgs->DeletedCheckedDevices");
		if(mAddToSceneList.contains(s)){
			mAddToSceneList.remove(s);
		}
	}

	@Override
	public void refreshListData() {
		// TODO Auto-generated method stub
		new GetDataTask().execute();
	}

	@Override
	public DevicesModel getDeviceModle(int postion) {
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
	public void setDevicesId(DevicesModel simpleDevicesModel) {
		// TODO Auto-generated method stub
		getModel=simpleDevicesModel;
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, List<DevicesModel>> {

		@Override
		protected List<DevicesModel> doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return mList;
		}

		@Override
		protected void onPostExecute(List<DevicesModel> result) {
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

	public boolean updateDevices(DevicesModel sd, ContentValues c) {
		// TODO Auto-generated method stub
		int result=0;
		SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
		result=(int) mDataHelper.insertGroup(mSQLiteDatabase, DataHelper.GROUP_TABLE, null, c);
		if (result >= 0) {
			return true;
		}
		return false;
	}


	@Override
	public void saveedit(DevicesModel mDevicesModel, String name) {
		// TODO Auto-generated method stub
		String where = " ieee = ? ";
		String[] args = { mDevicesModel.getmIeee() };

		ContentValues c = new ContentValues();
		c.put(DevicesModel.DEFAULT_DEVICE_NAME, name);
		
		SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
		int result = mDataHelper.update(mSQLiteDatabase,
				DataHelper.DEVICES_TABLE, c, where, args);
		if (result >= 0) {
			initSceneDevicesList();
			mDevicesBaseAdapter.setList(mList);
			mDevicesBaseAdapter.notifyDataSetChanged();
			if(null==mList || mList.size()==0){
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.hide(mDevicesListFragment);
				mNoDevices.setVisibility(View.VISIBLE);
			}
		}
	}


	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		if(deleteType){
			String where = " group_name = ? ";
			String[] args = { mScene };
			
			SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
			mDataHelper.delete(mSQLiteDatabase,
					DataHelper.GROUP_TABLE,  where, args);
			
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
			if(mreg.contains(UiUtils.SCENE_FLAG+mScene)){
				mreg.remove(UiUtils.SCENE_FLAG+mScene);
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
			
			finish();
		}else{
			String where = " group_name = ? and devices_ieee=? ";
			String[] args = { mScene,getModel.getmIeee()};
			SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
			int result=mDataHelper.delete(mSQLiteDatabase,
					DataHelper.GROUP_TABLE,  where, args);
			if (result >= 0) {
				initSceneDevicesList();
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
	
	public static class OperatorDevices{
		public boolean state;
		public int value;
		public DevicesModel sModel;
		public OperatorDevices(boolean st,int v,DevicesModel s){
			state=st;
			value=v;
			sModel=s;
		}
	}

	@Override
	public void setdata(List<DevicesModel> list) {
		// TODO Auto-generated method stub
		
	}
}
