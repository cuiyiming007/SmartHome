package com.gdgl.activity;

/***
 * 最外层设备菜单
 */
import java.util.ArrayList;
import java.util.List;

import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Linkage;
import com.gdgl.mydata.LinkageAct;
import com.gdgl.mydata.video.VideoNode;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyApplicationFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.gdgl.activity.LinkageDetailActivity;
import com.gdgl.activity.LinkageDevicesAddFragment.AddChecked;
import com.gdgl.activity.LinkageDevicesAddFragment.LinkageDevicesAddListAdapter;
import com.gdgl.activity.SceneDevicesFragment.ChangeFragment;

public class LinkageDetailFragment extends Fragment implements
	ChangeFragment, AddChecked{
	private static final String SPINNER_ONOFF_STRING[] = {"开", "关"};
	private static final String SPINNER_ONOFF_DATA[] = {"1", "0"};
	private static final String SPINNER_SIGN_STRING[] = {"大于", "等于", "小于"};
	private static final String SPINNER_SIGN_DATA[] = {"bt", "eq", "lt"};
	private static final String SPINNER_TRG_STRING[] = {"触发"};
	private static final String SPINNER_VIDEO_STRING[] = {"拍照"};
	ImageView img_act_device, img_trg_device;
	TextView txt_act_device, txt_trg_device;
	Spinner spinner_trg_type, spinner_act_type;
	Switch switch_act_type;
	EditText edit_trg_data;
	View layout_trg_data;

	private Linkage mLinkage;
	private int linkage_type;
	
	List<DevicesModel> mTrgAddList, mActAddList;
	List<VideoNode> mVideoAddList;
	DevicesModel actDevices, trgDevices;
	LinkageDevicesAddListAdapter mLinkageDevicesAddListAdapter;
	
	View mView;
	DataHelper mDataHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDataHelper = new DataHelper((Context) getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.linkage_detail_fragment, null);
		initview();
		return mView;
	}

	private void initview() {
		// TODO Auto-generated method stub
		img_trg_device = (ImageView)mView.findViewById(R.id.img_trg_device);
		img_act_device = (ImageView)mView.findViewById(R.id.img_act_device);	
		txt_trg_device = (TextView)mView.findViewById(R.id.txt_trg_device);
		txt_act_device = (TextView)mView.findViewById(R.id.txt_act_device);
		spinner_trg_type = (Spinner)mView.findViewById(R.id.spinner_trg_type);
		spinner_act_type = (Spinner)mView.findViewById(R.id.spinner_act_type);
		edit_trg_data = (EditText)mView.findViewById(R.id.edit_trg_data);	
		layout_trg_data = (View)mView.findViewById(R.id.layout_trg_data);
		mVideoAddList = mDataHelper.getVideoList((Context) getActivity(),
				mDataHelper);
		Bundle bundle = getArguments();
		linkage_type = bundle.getInt(LinkageDetailActivity.TYPE, 1);
		if(linkage_type == 2){
			trgDevices = DataUtil.getDeviceModelByIeee(mLinkage.getTrgieee(), mDataHelper, mDataHelper.getSQLiteDatabase());
			LinkageAct linkageAct = new LinkageAct(mLinkage.getLnkact());
			if(linkageAct.getType().equals("4")){
				actDevices = getVideoDevice(linkageAct.getIeee());
			}else{
				actDevices = DataUtil.getDeviceModelByIeee(linkageAct.getIeee(), mDataHelper, mDataHelper.getSQLiteDatabase());
			}
			initLinkageDetailData();
		}
		setListeners();
	}
	
	public DevicesModel getVideoDevice(String videoId){
		DevicesModel mDevices = new DevicesModel();
		for(VideoNode mVideo : mVideoAddList){
			if(mVideo.getId().equals(videoId)){
				mDevices.setID(Integer.parseInt(mVideo.getId()));
				mDevices.setmDevicePriority(VideoNode.PRIORITY);
				mDevices.setmDefaultDeviceName(mVideo.getName());
			}
		}
		return mDevices;
	}
	
	private void setListeners() {
		
		 img_trg_device.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initTrgAddFragmentDevicesList();
				LinkageDevicesAddFragment mLinkageAddFragment = new LinkageDevicesAddFragment();
				mLinkageAddFragment.setAddChecked(LinkageDetailFragment.this);
				mLinkageDevicesAddListAdapter = mLinkageAddFragment.new LinkageDevicesAddListAdapter(mTrgAddList);
				mLinkageAddFragment.setAdapter(mLinkageDevicesAddListAdapter);
				Bundle bundle = new Bundle();
				bundle.putInt(LinkageDetailActivity.TYPE, 1);	
				mLinkageAddFragment.setArguments(bundle);
				FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
						.beginTransaction();
				fragmentTransaction.add(R.id.container, mLinkageAddFragment);
				fragmentTransaction.commit();
				MyApplicationFragment.getInstance().addFragment(mLinkageAddFragment);
			}
		});
		
		img_act_device.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initActAddFragmentDevicesList();
				LinkageDevicesAddFragment mLinkageAddFragment = new LinkageDevicesAddFragment();
				mLinkageAddFragment.setAddChecked(LinkageDetailFragment.this);
				mLinkageDevicesAddListAdapter = mLinkageAddFragment.new LinkageDevicesAddListAdapter(mActAddList);
				mLinkageAddFragment.setAdapter(mLinkageDevicesAddListAdapter);
				Bundle bundle = new Bundle();
				bundle.putInt(LinkageDetailActivity.TYPE, 2);	
				mLinkageAddFragment.setArguments(bundle);
				FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
						.beginTransaction();
				fragmentTransaction.add(R.id.container, mLinkageAddFragment);
				fragmentTransaction.commit();
				MyApplicationFragment.getInstance().addFragment(mLinkageAddFragment);
			}
		});
	}
	
	public void setLinkage(Linkage mLinkage){
		this.mLinkage = mLinkage;
	}
	
	public void updateLinkage(){
		if(linkage_type == 1){
			mLinkage.setTrgieee(trgDevices.getmIeee());
			mLinkage.setTrgep(trgDevices.getmEP());
			mLinkage.setTrgcnd(getTrgcnd());
			mLinkage.setLnkact(getLnkact());
			mLinkage.setEnable(1);
			
		}else if(linkage_type == 2){
			mLinkage.setTrgieee(trgDevices.getmIeee());
			mLinkage.setTrgep(trgDevices.getmEP());
			mLinkage.setTrgcnd(getTrgcnd());
			mLinkage.setLnkact(getLnkact());
		}
	}
	
	private String getTrgcnd(){
		String trgcnd = "";
		if(trgDevices.getmModelId().indexOf((DataHelper.Magnetic_Window)) == 0){ //窗磁Z311A
			trgcnd += "Burglar@eq@0";
		}else if(trgDevices.getmModelId().indexOf((DataHelper.Motion_Sensor)) == 0){ //动作感应器ZB11A
			trgcnd += "Burglar@eq@0";
		}else if(trgDevices.getmModelId().indexOf((DataHelper.Smoke_Detectors)) == 0){ //烟雾感应器ZA01A
			trgcnd += "Fire@eq@0";
		}else if(trgDevices.getmModelId().indexOf((DataHelper.Indoor_temperature_sensor)) == 0){ //温湿度感应器Z711
			trgcnd += "temp@" + SPINNER_SIGN_DATA[spinner_trg_type.getSelectedItemPosition()] + "@" + edit_trg_data.getText().toString();	
		}
		return trgcnd;
	}
	
	private String getLnkact(){
		String lnkact = "";
		if(actDevices.getmDevicePriority() == 1000){
			lnkact = "4:" + actDevices.getID();
		}else{
			if(actDevices.getmModelId().indexOf((DataHelper.Magnetic_Window)) == 0){ //窗磁Z311A
				lnkact += 1;
			}else if(actDevices.getmModelId().indexOf((DataHelper.Motion_Sensor)) == 0){ //动作感应器ZB11A
				lnkact += 1;
			}else if(actDevices.getmModelId().indexOf((DataHelper.Smoke_Detectors)) == 0){ //烟雾感应器ZA01A
				lnkact += 1;
			}else if(actDevices.getmModelId().indexOf((DataHelper.Siren)) == 0){ //警报器Z602A
				lnkact += 1;
			}else if(actDevices.getmModelId().indexOf((DataHelper.Indoor_temperature_sensor)) == 0){ //温湿度感应器Z711
				lnkact += 1;
			}else if(actDevices.getmModelId().indexOf((DataHelper.Power_detect_wall)) == 0){ //电能检测墙面插座Z816H
				lnkact += 3;
			}else if(actDevices.getmModelId().indexOf((DataHelper.One_key_operator)) == 0){ //物联网网关
				lnkact += 2;
			}else { //
				lnkact += 3;
			}
			lnkact += ":" + actDevices.getmIeee() + "-" + actDevices.getmEP() + "-" + SPINNER_ONOFF_DATA[spinner_act_type.getSelectedItemPosition()];
		}
		return lnkact;
	}
	
	private void initTrgAddFragmentDevicesList() {
		mTrgAddList = null;
		mTrgAddList = new ArrayList<DevicesModel>();
		List<DevicesModel> mAddList = mDataHelper.queryForDevicesList(
				mDataHelper.getSQLiteDatabase(), DataHelper.DEVICES_TABLE,
				null, null, null, null, null, DevicesModel.DEVICE_PRIORITY,
				null);
		for(DevicesModel mDevices : mAddList){
			if(mDevices.getmModelId().indexOf((DataHelper.Magnetic_Window)) == 0){ //窗磁Z311A
				mTrgAddList.add(mDevices);
			}else if(mDevices.getmModelId().indexOf((DataHelper.Motion_Sensor)) == 0){ //动作感应器ZB11A
				mTrgAddList.add(mDevices);
			}else if(mDevices.getmModelId().indexOf((DataHelper.Smoke_Detectors)) == 0){ //烟雾感应器ZA01A
				mTrgAddList.add(mDevices);
			}else if(mDevices.getmModelId().indexOf((DataHelper.Indoor_temperature_sensor)) == 0){ //温湿度感应器Z711
				mTrgAddList.add(mDevices);	
			}
		}
	}
	
	private void initActAddFragmentDevicesList() {
		mActAddList = null;
		mActAddList = new ArrayList<DevicesModel>();
		List<DevicesModel> mAddList = mDataHelper.queryForDevicesList(
				mDataHelper.getSQLiteDatabase(), DataHelper.DEVICES_TABLE,
				null, null, null, null, null, DevicesModel.DEVICE_PRIORITY,
				null);
		for(DevicesModel mDevices : mAddList){
			if(mDevices.getmModelId().indexOf((DataHelper.Power_detect_wall)) == 0){ //电能检测墙面插座Z816H
				mActAddList.add(mDevices);
			}else if(mDevices.getmModelId().indexOf((DataHelper.Siren)) == 0){ //警报器Z602A
				mActAddList.add(mDevices);
			}
		}
		for(VideoNode mVideo : mVideoAddList){
			DevicesModel mDevices = new DevicesModel();
			mDevices.setID(Integer.parseInt(mVideo.getId()));
			mDevices.setmDevicePriority(VideoNode.PRIORITY);
			mDevices.setmDefaultDeviceName(mVideo.getName());
			mActAddList.add(mDevices);
		}
		
	}
	
	private void initLinkageDetailData(){
		updateActDevices();
		updatetrgDevices();
	}
	
	public void AddActCheckedDevices(DevicesModel mDevices) {
		// TODO Auto-generated method stub
		actDevices = mDevices;
		updateActDevices();
	}
	
	private void updateActDevices(){
		if(actDevices.getmDevicePriority() == 1000){
			img_act_device.setImageResource(R.drawable.ui_video_play);
		}else{	
			img_act_device.setImageResource(DataUtil
					.getDefaultDevicesSmallIcon(actDevices.getmDeviceId(),
							actDevices.getmModelId().trim()));
		}
//		img_act_device.setImageResource(DataUtil.getDefaultDevicesSmallIcon(
//				actDevices.getmDeviceId(), actDevices.getmModelId()));
		txt_act_device.setText(actDevices.getmDefaultDeviceName());
		if(actDevices.getmDevicePriority() == 1000){
			ArrayAdapter spinner_act_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SPINNER_VIDEO_STRING);   
			spinner_act_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item); 
			spinner_act_type.setAdapter(spinner_act_adapter);
		}else{
			ArrayAdapter spinner_act_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SPINNER_ONOFF_STRING);   
			spinner_act_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item); 
			spinner_act_type.setAdapter(spinner_act_adapter);
		}
		
	}
	
	public void AddTrgCheckedDevices(DevicesModel mDevices){
		trgDevices = mDevices;
		updatetrgDevices();
	}
	
	private void updatetrgDevices(){
		img_trg_device.setImageResource(DataUtil.getDefaultDevicesSmallIcon(
				trgDevices.getmDeviceId(), trgDevices.getmModelId()));
		txt_trg_device.setText(trgDevices.getmDefaultDeviceName());
		if(trgDevices.getmModelId().indexOf((DataHelper.Indoor_temperature_sensor)) == 0){
			ArrayAdapter spinner_trg_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SPINNER_SIGN_STRING);   
			spinner_trg_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item); 
			spinner_trg_type.setAdapter(spinner_trg_adapter);
			layout_trg_data.setVisibility(View.VISIBLE);
			
		}else{
			ArrayAdapter spinner_trg_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SPINNER_TRG_STRING);   
			spinner_trg_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item); 
			spinner_trg_type.setAdapter(spinner_trg_adapter);
			layout_trg_data.setVisibility(View.GONE);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	

	@Override
	public void setFragment(Fragment f) {
		// TODO Auto-generated method stub
		
	}

}
