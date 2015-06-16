package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.activity.LinkageDevicesAddFragment.AddChecked;
import com.gdgl.activity.LinkageDevicesAddFragment.LinkageDevicesAddListAdapter;
import com.gdgl.activity.SceneDevicesFragment;
import com.gdgl.activity.SceneDevicesFragment.ChangeFragment;
import com.gdgl.activity.SceneDevicesFragment.SceneDevicesListAdapter;
import com.gdgl.manager.SceneLinkageManager;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Linkage;
import com.gdgl.mydata.LinkageAct;
import com.gdgl.mydata.scene.SceneDevice;
import com.gdgl.mydata.scene.SceneInfo;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyApplicationFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class LinkageDetailActivity extends ActionBarActivity implements
		ChangeFragment {
	private static final String SPINNER_ONOFF_STRING[] = {"开", "关"};
	private static final String SPINNER_ONOFF_DATA[] = {"1", "0"};
	private static final String SPINNER_SIGN_STRING[] = {"大于", "等于", "小于"};
	private static final String SPINNER_SIGN_DATA[] = {"bt", "eq", "lt"};
	private static final String SPINNER_TRG_STRING[] = {"触发"};
	public static final String TYPE = "type";
	public static final String INDEX = "index";
	public static final int CREATE = 1;
	public static final int EDIT = 2;

	public static final int EDIT_FRAGMENT = 2;
	public static final int ADD_FRAGMENT = 1;

	// 判断是创建情景还是编辑情景
	int linkage_type;
	int fragment_flag;
	int spinner_type;

	private Toolbar mToolbar;
	private ActionBar mActionBar;

	private String linkageName = "";
	private int linkageId;
	private int linkageIndex;

	private Linkage mLinkage;
	// 点击图片后出现的设备表
	List<DevicesModel> mTrgAddList, mActAddList;
	DevicesModel actDevices, trgDevices;

	List<SceneDevice> mAddToSceneList = new ArrayList<SceneDevice>();

	DataHelper mDataHelper;
	
	LinkageDetailFragment mLinkageDetailFragment;

	FragmentManager fragmentManager;

	DevicesListFragment mDevicesListFragment;

	SceneDevicesFragment mAllDevicesFragment;

	SceneDevicesListAdapter mSceneDevicesListAdapter;
	LinkageDevicesAddListAdapter mLinkageDevicesAddListAdapter;

	EditText titleEditText;
	
	ImageView img_act_device, img_trg_device;
	TextView txt_act_device, txt_trg_device;
	Spinner spinner_trg_type, spinner_act_type;
	Switch switch_act_type;
	EditText edit_trg_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.linkage_detail_activity);
		MyApplicationFragment.getInstance().setActivity(this);

		mDataHelper = new DataHelper(this);

		img_trg_device = (ImageView)findViewById(R.id.img_trg_device);
		img_act_device = (ImageView)findViewById(R.id.img_act_device);	
		txt_trg_device = (TextView)findViewById(R.id.txt_trg_device);
		txt_act_device = (TextView)findViewById(R.id.txt_act_device);
		spinner_trg_type = (Spinner)findViewById(R.id.spinner_trg_type);
		spinner_act_type = (Spinner)findViewById(R.id.spinner_act_type);
		edit_trg_data = (EditText)findViewById(R.id.edit_trg_data);

		Bundle bundle = getIntent().getExtras();
		Intent i = getIntent();
		linkage_type = i.getIntExtra(TYPE, 2);
		if (linkage_type == CREATE) {
			linkageIndex = i.getIntExtra(LinkageDetailActivity.INDEX, 0);
			linkageName = "联动" + (linkageIndex + 1);
			mLinkage = new Linkage();
		} else {
			mLinkage = (Linkage) i.getSerializableExtra(Constants.PASS_OBJECT);
			linkageName = mLinkage.getLnkname();
		}

		titleEditText = new EditText(this);
		Toolbar.LayoutParams toolbarParams = new Toolbar.LayoutParams(
				Toolbar.LayoutParams.WRAP_CONTENT,
				Toolbar.LayoutParams.WRAP_CONTENT, Gravity.LEFT);
		toolbarParams.setMargins(5, 0, 5, 0);
		titleEditText.setLayoutParams(toolbarParams);
		titleEditText.setText(linkageName);

		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		mToolbar.addView(titleEditText);
		setSupportActionBar(mToolbar);
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(false);
		// mActionBar.setCustomView(titleEditText);
	
		mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
				case R.id.menu_ok:
					mLinkageDetailFragment.updateLinkage();
					if (linkage_type == CREATE) {
						SceneLinkageManager.getInstance().AddLinkage(
								Uri.encode(titleEditText.getText().toString()), 
								mLinkage.getTrgieee(), 
								mLinkage.getTrgep(), 
								mLinkage.getTrgcnd(), 
								mLinkage.getLnkact(), 
								mLinkage.getEnable());
						finish();
					}else if(linkage_type == EDIT){
						SceneLinkageManager.getInstance().EditLinkage(
								Uri.encode(titleEditText.getText().toString()), 
								mLinkage.getTrgieee(), 
								mLinkage.getTrgep(), 
								mLinkage.getTrgcnd(), 
								mLinkage.getLnkact(), 
								mLinkage.getEnable(), 
								mLinkage.getLid());	
						finish();
					}
					break;

				default:
					break;
				}
				return false;
			}
		});
		mLinkageDetailFragment = new LinkageDetailFragment();
		mLinkageDetailFragment.setLinkage(mLinkage);
		Bundle bundle1 = new Bundle();
		bundle1.putInt(TYPE, linkage_type);	
		mLinkageDetailFragment.setArguments(bundle1);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.container, mLinkageDetailFragment);
		fragmentTransaction.commit();
		MyApplicationFragment.getInstance().addNewTask(mLinkageDetailFragment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_ok, menu);
		return true;
	}
	
	private boolean checkLinkageData(){
		if(trgDevices == null){
			Toast.makeText(LinkageDetailActivity.this,
					"请选择触发设备", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(actDevices == null){
			Toast.makeText(LinkageDetailActivity.this,
					"请选择联动设备", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(spinner_trg_type.getSelectedItemPosition() == 0){
			Toast.makeText(LinkageDetailActivity.this,
					"请选择触发方式", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(spinner_act_type.getSelectedItemPosition() == 0){
			Toast.makeText(LinkageDetailActivity.this,
					"请选择联动方式", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(edit_trg_data.getText().toString().equals("")){
			Toast.makeText(LinkageDetailActivity.this,
					"请选择触发数值", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private void updateLinkageData(){
		if(linkage_type == CREATE){
			
			SceneLinkageManager.getInstance().AddLinkage(
					Uri.encode(titleEditText.getText().toString()), 
					trgDevices.getmIeee(), 
					trgDevices.getmEP(), 
					getTrgcnd(), 
					getLnkact(), 
					1);
		}else if(linkage_type == EDIT){
			SceneLinkageManager.getInstance().EditLinkage(
					Uri.encode(titleEditText.getText().toString()), 
					trgDevices.getmIeee(), 
					trgDevices.getmEP(), 
					getTrgcnd(), 
					getLnkact(), 
					1,
					mLinkage.getLid());
		}
	}
	 
	
	private String getTrgcnd(){
		String trgcnd = "";
		if(actDevices.getmModelId().equals(DataHelper.Magnetic_Window)){ //窗磁Z311A
			trgcnd += "Burglar@eq@0";
		}else if(actDevices.getmModelId().equals(DataHelper.Motion_Sensor)){ //动作感应器ZB11A
			trgcnd += "Burglar@eq@0";
		}else if(actDevices.getmModelId().equals(DataHelper.Smoke_Detectors)){ //烟雾感应器ZA01A
			trgcnd += "Fire@eq@0";
		}else if(actDevices.getmModelId().equals(DataHelper.Indoor_temperature_sensor)){ //温湿度感应器Z711
			trgcnd += "temp" + SPINNER_SIGN_DATA[spinner_trg_type.getSelectedItemPosition()] + edit_trg_data.getText().toString();	
		}
		return trgcnd;
	}
	
	private String getLnkact(){
		String lnkact = "";
		if(actDevices.getmModelId().equals(DataHelper.Magnetic_Window)){ //窗磁Z311A
			lnkact += 1;
		}else if(actDevices.getmModelId().equals(DataHelper.Motion_Sensor)){ //动作感应器ZB11A
			lnkact += 1;
		}else if(actDevices.getmModelId().equals(DataHelper.Smoke_Detectors)){ //烟雾感应器ZA01A
			lnkact += 1;
		}else if(actDevices.getmModelId().equals(DataHelper.Siren)){ //警报器Z602A
			lnkact += 1;
		}else if(actDevices.getmModelId().equals(DataHelper.Indoor_temperature_sensor)){ //温湿度感应器Z711
			lnkact += 1;
		}else if(actDevices.getmModelId().equals(DataHelper.Power_detect_wall)){ //电能检测墙面插座Z816H
			lnkact += 3;
		}else if(actDevices.getmModelId().equals(DataHelper.One_key_operator)){ //物联网网关
			lnkact += 2;
		}else { //
			lnkact += 3;
		}
		lnkact += ":" + actDevices.getmIeee() + "-" + actDevices.getmEP() + "-" + SPINNER_ONOFF_DATA[spinner_act_type.getSelectedItemPosition()];
		return lnkact;
	}

	@Override
	public void setFragment(Fragment f) {
//		initAddFragmentDevicesList();
//		LinkageDevicesAddFragment mSceneAddFragment = (LinkageDevicesAddFragment) f;
//		mLinkageDevicesAddListAdapter = mSceneAddFragment.new LinkageDevicesAddListAdapter(
//				mAddList, LinkageDetailActivity.this);
//		mSceneAddFragment.setAdapter(mLinkageDevicesAddListAdapter);
//
//		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
//				.beginTransaction();
//		fragmentTransaction.add(R.id.container, f);
//		fragmentTransaction.commit();
//		fragment_flag = ADD_FRAGMENT;
//		MyApplicationFragment.getInstance().addFragment(f);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (MyApplicationFragment.getInstance().getFragmentListSize() > 1) {
			MyApplicationFragment.getInstance().removeLastFragment();
			fragment_flag = EDIT_FRAGMENT;
			mAddToSceneList.clear();
		} else {
			finish();
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		// TODO Auto-generated method stub
		if (MyApplicationFragment.getInstance().getFragmentListSize() > 1) {
			MyApplicationFragment.getInstance().removeLastFragment();
			fragment_flag = EDIT_FRAGMENT;
			mAddToSceneList.clear();
			return super.onSupportNavigateUp();
		}
		finish();
		return super.onSupportNavigateUp();
	}
	
}
