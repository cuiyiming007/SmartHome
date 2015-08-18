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
import com.gdgl.mydata.LinkageCnd;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
	
	public static int tem_hum = 0;//=====判断温度还是湿度====王
	public static int show_cho =0;//=====判断是显示联动中的温湿度，还是从设备列表中选择温湿度设备======王==0811
	
	ImageView devices_img, act_img;
	TextView devices_txt, act_txt,tem_hum_label;//tem_hum_label====温湿度符号===
	View devices_choice_lay, act_choice_lay, devices_notype_lay, act_notype_lay, devices_temp_lay,
			devices_trg_lay, act_onoff_lay, act_photo_lay,tem_hum_btn_lay;
	//tem_hum_btn_lay====温湿度选择按钮布局=====王晓飞
	Button devices_bt_btn, devices_eq_btn, devices_lt_btn, devices_trg_btn, act_on_btn, 
	act_off_btn, act_photo_btn,tem_btn,hum_btn;//tem_btn,hum_btn温湿度按钮
	EditText devices_data_edit;
	
	int onoff = 0;
	int temp = 0;
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
		devices_img = (ImageView)mView.findViewById(R.id.devices_img);
		act_img = (ImageView)mView.findViewById(R.id.act_img);
		tem_hum_label=(TextView)mView.findViewById(R.id.tem_hum_label);//温湿度符号=====王
		devices_txt = (TextView)mView.findViewById(R.id.devices_txt); 
		act_txt = (TextView)mView.findViewById(R.id.act_txt);
		tem_hum_btn_lay =  (View)mView.findViewById(R.id.tem_hum_btn_lay);//===温湿度布局===王
		devices_choice_lay = (View)mView.findViewById(R.id.devices_choice_lay); 
		act_choice_lay = (View)mView.findViewById(R.id.act_choice_lay);
		devices_notype_lay = (View)mView.findViewById(R.id.devices_notype_lay); 
		act_notype_lay = (View)mView.findViewById(R.id.act_notype_lay); 
		devices_temp_lay = (View)mView.findViewById(R.id.devices_temp_lay);
		devices_trg_lay = (View)mView.findViewById(R.id.devices_trg_lay);
		act_onoff_lay = (View)mView.findViewById(R.id.act_onoff_lay);
		act_photo_lay = (View)mView.findViewById(R.id.act_photo_lay);
		tem_btn = (Button)mView.findViewById(R.id.tem_btn);//===温度按钮====王
		hum_btn = (Button)mView.findViewById(R.id.hum_btn);//===温度按钮====王
		devices_bt_btn = (Button)mView.findViewById(R.id.devices_bt_btn);
		devices_eq_btn = (Button)mView.findViewById(R.id.devices_eq_btn); 
		devices_lt_btn = (Button)mView.findViewById(R.id.devices_lt_btn);
		devices_trg_btn = (Button)mView.findViewById(R.id.devices_trg_btn);
		act_on_btn = (Button)mView.findViewById(R.id.act_on_btn);
		act_off_btn = (Button)mView.findViewById(R.id.act_off_btn);
		act_photo_btn = (Button)mView.findViewById(R.id.act_photo_btn);
		devices_data_edit = (EditText)mView.findViewById(R.id.devices_data_edit);	
			
		mVideoAddList = DataHelper.getVideoList((Context) getActivity(),
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
			show_cho = 0;//=======选择编辑联动列表，显示温湿度=====0811======王
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
				mDevices.setmDefaultDeviceName(mVideo.getAliases());
				break;
			}
		}
		return mDevices;
	}
	
	private void setListeners() {
		
		devices_choice_lay.setOnClickListener(new OnClickListener() {
			
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
		
		act_choice_lay.setOnClickListener(new OnClickListener() {
			
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
		
		tem_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tem_hum = 0;//====0表示温度====王
				devices_temp_lay.setVisibility(View.VISIBLE);
				devices_notype_lay.setVisibility(View.GONE);
				devices_trg_lay.setVisibility(View.GONE);
				tem_hum_btn_lay.setVisibility(View.GONE);
				tem_hum_label.setText("°C");
			}
		});
		hum_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tem_hum = 1;//====1表示湿度====王
				devices_temp_lay.setVisibility(View.VISIBLE);
				devices_notype_lay.setVisibility(View.GONE);
				devices_trg_lay.setVisibility(View.GONE);
				tem_hum_btn_lay.setVisibility(View.GONE);
				tem_hum_label.setText("%");
			}
		});
		
		devices_bt_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				temp = 0;
				devices_bt_btn.setBackgroundResource(R.drawable.ui2_linkage_button_press);
				devices_eq_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
				devices_lt_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
			}
		});
		devices_eq_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				temp = 1;
				devices_bt_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
				devices_eq_btn.setBackgroundResource(R.drawable.ui2_linkage_button_press);
				devices_lt_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
			}
		});
		devices_lt_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				temp = 2;
				devices_bt_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
				devices_eq_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
				devices_lt_btn.setBackgroundResource(R.drawable.ui2_linkage_button_press);
			}
		});
		act_on_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onoff = 1;
				act_on_btn.setBackgroundResource(R.drawable.ui2_linkage_button_press);
				act_off_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
			}
		});
		act_off_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onoff = 0;
				act_on_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
				act_off_btn.setBackgroundResource(R.drawable.ui2_linkage_button_press);
			}
		});
	}
	
	public void setLinkage(Linkage mLinkage){
		this.mLinkage = mLinkage;
	}
	
	public boolean updateLinkage(){
		if(trgDevices == null){
			Toast.makeText(getActivity(),"请选择触发设备", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(actDevices == null){
			Toast.makeText(getActivity(),"请选择联动设备", Toast.LENGTH_SHORT).show();
			return false;
		}
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
			mLinkage.setEnable(1);
		}
		return true;
	}
	
	private String getTrgcnd(){
		String trgcnd = "";
		if(trgDevices.getmModelId().indexOf((DataHelper.Magnetic_Window)) == 0){ //窗磁Z311A
//			trgcnd += "Burglar@eq@0";
			trgcnd += "alarm@eq@0";
		}else if(trgDevices.getmModelId().indexOf((DataHelper.Motion_Sensor)) == 0){ //动作感应器ZB11A
//			trgcnd += "Burglar@eq@0";
			trgcnd += "alarm@eq@0";
		}else if(trgDevices.getmModelId().indexOf((DataHelper.Smoke_Detectors)) == 0){ //烟雾感应器ZA01A
//			trgcnd += "Fire@eq@0";
			trgcnd += "alarm@eq@0";
		}else if(trgDevices.getmModelId().indexOf((DataHelper.Indoor_temperature_sensor)) == 0){ //温湿度感应器Z711
			if(tem_hum == 0)
				trgcnd += "temp@" + SPINNER_SIGN_DATA[temp] + "@" + devices_data_edit.getText().toString();	
			else
				trgcnd += "hum@" + SPINNER_SIGN_DATA[temp] + "@" + devices_data_edit.getText().toString();
		}
		return trgcnd;
	}
	
	private String getLnkact(){
		String lnkact = "";
		if(actDevices.getmDevicePriority() == 1000){
			lnkact = "4:" + actDevices.getID() + "-1";
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
			}else if(actDevices.getmModelId().indexOf((DataHelper.Power_detect_socket)) == 0){ //电能检测插座Z809A=====王晓飞
				lnkact += 3;
			}else if(actDevices.getmModelId().indexOf((DataHelper.One_key_operator)) == 0){ //物联网网关
				lnkact += 2;
			}else { //
				lnkact += 3;
			}
			lnkact += ":" + actDevices.getmIeee() + "-" + actDevices.getmEP() + "-" + onoff;
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
		for(DevicesModel mDevices : mAddList){//----王晓飞---添加||power_detect_socket
			if(mDevices.getmModelId().indexOf((DataHelper.Power_detect_wall)) == 0||
					mDevices.getmModelId().indexOf((DataHelper.Power_detect_socket)) == 0){ //电能检测墙面插座Z816H
				mActAddList.add(mDevices);
			}
//			else if(mDevices.getmModelId().indexOf((DataHelper.Siren)) == 0){ //警报器Z602A
//				mActAddList.add(mDevices);
//			}
		}
		for(VideoNode mVideo : mVideoAddList){
			DevicesModel mDevices = new DevicesModel();
			mDevices.setID(Integer.parseInt(mVideo.getId()));
			mDevices.setmDevicePriority(VideoNode.PRIORITY);
			mDevices.setmDefaultDeviceName(mVideo.getAliases());
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
			act_img.setBackgroundResource(R.drawable.ui2_device_video);
		}else{	
			act_img.setBackgroundResource(DataUtil
					.getDefaultDevicesSmallIcon(actDevices.getmDeviceId(),
							actDevices.getmModelId().trim()));
		}
		act_txt.setText(actDevices.getmDefaultDeviceName());
		if(actDevices.getmDevicePriority() == 1000){
			act_notype_lay.setVisibility(View.GONE);
			act_onoff_lay.setVisibility(View.GONE);
			act_photo_lay.setVisibility(View.VISIBLE);
		}else{
			act_notype_lay.setVisibility(View.GONE);
			act_photo_lay.setVisibility(View.GONE);
			act_onoff_lay.setVisibility(View.VISIBLE);
			if(linkage_type == 2){
				LinkageAct linkageAct = new LinkageAct(mLinkage.getLnkact());
				if(linkageAct.getArm().equals("0")){	//关
					onoff = 0;
					act_on_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
					act_off_btn.setBackgroundResource(R.drawable.ui2_linkage_button_press);
				}else{		//开
					onoff = 1;
					act_on_btn.setBackgroundResource(R.drawable.ui2_linkage_button_press);
					act_off_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
				}
			}else{
				onoff = 1;
			}
		}
		
	}
	
	public void AddTrgCheckedDevices(DevicesModel mDevices){
		trgDevices = mDevices;
		updatetrgDevices();
	}
	
	private void updatetrgDevices(){
		devices_img.setBackgroundResource(DataUtil.getDefaultDevicesSmallIcon(
				trgDevices.getmDeviceId(), trgDevices.getmModelId()));
		devices_txt.setText(trgDevices.getmDefaultDeviceName());
		if(trgDevices.getmModelId().indexOf((DataHelper.Indoor_temperature_sensor)) == 0){
			if(linkage_type == 2&&show_cho == 0){
				//===如果是编辑联动，则显示原来设定的温度湿度值====0811==添加show_cho
				LinkageCnd linkageCnd = new LinkageCnd(mLinkage.getTrgcnd());
				tem_hum_btn_lay.setVisibility(View.GONE);
				devices_temp_lay.setVisibility(View.VISIBLE);
				devices_notype_lay.setVisibility(View.GONE);
				devices_trg_lay.setVisibility(View.GONE);
				if(linkageCnd.getMark().equals("bt")){
					temp = 0;
					devices_bt_btn.setBackgroundResource(R.drawable.ui2_linkage_button_press);
					devices_eq_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
					devices_lt_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
				}else if(linkageCnd.getMark().equals("eq")){
					temp = 1;
					devices_bt_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
					devices_eq_btn.setBackgroundResource(R.drawable.ui2_linkage_button_press);
					devices_lt_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
				}else if(linkageCnd.getMark().equals("lt")){
					temp = 2;
					devices_bt_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
					devices_eq_btn.setBackgroundResource(R.drawable.ui2_linkage_button_normal);
					devices_lt_btn.setBackgroundResource(R.drawable.ui2_linkage_button_press);
				}
				devices_data_edit.setText(linkageCnd.getData());
				if(tem_hum == 0) tem_hum_label.setText("°C");
				else tem_hum_label.setText("%");
			}else{//====如果是添加联动，则显示温度湿度按钮
				tem_hum_btn_lay.setVisibility(View.VISIBLE);
				devices_temp_lay.setVisibility(View.GONE);
				devices_notype_lay.setVisibility(View.GONE);
				devices_trg_lay.setVisibility(View.GONE);
			}
		}else{
			devices_trg_lay.setVisibility(View.VISIBLE);
			devices_temp_lay.setVisibility(View.GONE);
			devices_notype_lay.setVisibility(View.GONE);
			tem_hum_btn_lay.setVisibility(View.GONE);
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
