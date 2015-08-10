package com.gdgl.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gdgl.activity.EnergyEditFragment.EnergyAdapter.ViewHolder;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.Manger;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.EnergyModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.smarthome.R;

/***
 * 设备详情页
 * 
 */
public class EnergyEditFragment extends BaseFragment implements OnClickListener{
	public static final int LISTNUM_ARRAY [] = {0, 5, 4, 10};
	public static final String[] LISTMODULE= {"安防模块", "抄表模块"};
	public static final String[] LISTMETERTYPE= {"请选择表类型", "电表", "冷水表", "热水表", "燃气表"};
	
	EditText edit_info_0, edit_info_1, edit_info_2, edit_info_3, edit_info_4, edit_info_5, edit_info_6, edit_info_7, edit_info_8, edit_info_9;
	EditText edit_meter_num_0, edit_meter_num_1, edit_meter_num_2, edit_meter_num_3, edit_meter_num_4, edit_meter_num_5, edit_meter_num_6, edit_meter_num_7, edit_meter_num_8, edit_meter_num_9;
	Spinner spinner_type_0, spinner_type_1, spinner_type_2, spinner_type_3, spinner_type_4, spinner_type_5, spinner_type_6, spinner_type_7, spinner_type_8, spinner_type_9;
	Button btn_control_0, btn_control_1, btn_control_2, btn_control_3, btn_control_4, btn_control_5, btn_control_6, btn_control_7, btn_control_8, btn_control_9;
	
	ArrayList<EditText> edit_info = new ArrayList<EditText>();
	ArrayList<EditText> edit_meter_num = new ArrayList<EditText>();
	ArrayList<Spinner> spinner_type = new ArrayList<Spinner>();
	
	//private EditText[] edit_info = {edit_info_0, edit_info_1, edit_info_2, edit_info_3, edit_info_4, edit_info_5, edit_info_6, edit_info_7, edit_info_8, edit_info_9};
	//private EditText[] edit_meter_num = {edit_meter_num_0, edit_meter_num_1, edit_meter_num_2, edit_meter_num_3, edit_meter_num_4, edit_meter_num_5, edit_meter_num_6, edit_meter_num_7, edit_meter_num_8, edit_meter_num_9};
	//private Spinner[] spinner_type = {spinner_type_0, spinner_type_1, spinner_type_2, spinner_type_3, spinner_type_4, spinner_type_5, spinner_type_6, spinner_type_7, spinner_type_8, spinner_type_9};
	
	
	ArrayList<EnergyModel> securityList, onoffList, meterList;
	boolean isSet = false;
	int module = 0;
	int listNum = 0;
	Spinner module_spinner;
	Button btn_save, btn_cancel, btn_clear, btn_period;
	ListView security_list, onoff_list;
	View mView, security_layout, meter_layout;
	DevicesModel mDevices;
	EnergyAdapter securityAdapter, onoffAdapter;
	
	CGIManager cgiManager;

	DataHelper mDataHelper;
	SQLiteDatabase mSQLiteDatabase;

	private static EnergyEditFragment instance;

	public static EnergyEditFragment getInstance() {
		if (instance == null) {
			instance = new EnergyEditFragment();
		}
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		if (null != extras) {
			mDevices = (DevicesModel) extras
					.getSerializable(Constants.PASS_OBJECT);
		}

		cgiManager = CGIManager.getInstance();
		cgiManager.addObserver(EnergyEditFragment.this);
		DeviceManager.getInstance().addObserver(this);

		mDataHelper = new DataHelper((Context) getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.energy_fragment, null);
		initView();
		return mView;
	}

	private void initView() {	
		// mLightManager.iASZoneOperationCommon(mDevices, 7, 1);
		// TODO Auto-generated method stub
		security_layout = (View)mView.findViewById(R.id.security_layout);
		meter_layout = (View)mView.findViewById(R.id.meter_layout);
		btn_save = (Button)mView.findViewById(R.id.btn_save);
		btn_cancel = (Button)mView.findViewById(R.id.btn_cancel);
		btn_clear = (Button)mView.findViewById(R.id.btn_clear);
		btn_period = (Button)mView.findViewById(R.id.btn_period);
		btn_save.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		btn_clear.setOnClickListener(this);
		btn_period.setOnClickListener(this);
		security_list = (ListView)mView.findViewById(R.id.security_list);
		onoff_list = (ListView)mView.findViewById(R.id.onoff_list);
		module_spinner = (Spinner)mView.findViewById(R.id.module_spinner);
		ArrayAdapter module_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LISTMODULE);   
		module_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);   
		module_spinner.setAdapter(module_adapter);   
		module_spinner.setOnItemSelectedListener(new SpinnerSelectedListener());   

		securityList = getEnergyDevice(1);
		securityAdapter = new EnergyAdapter(securityList);
		security_list.setAdapter(securityAdapter);
		
		onoffList = getEnergyDevice(2);
		onoffAdapter = new EnergyAdapter(onoffList);
		onoff_list.setAdapter(onoffAdapter);
	
		initMeterView();
		initMeterData();
	}
	
	public void initMeterData(){
		meterList = getEnergyMeterDevice(3);
		for(int i=0; i<meterList.size(); i++){
			edit_info.get(i).setText(""+meterList.get(i).getInfo());
			edit_meter_num.get(i).setText(""+meterList.get(i).getMeterNum());
			spinner_type.get(i).setSelection(meterList.get(i).getSubType());
		}
	}
	
	public void initMeterView(){
		edit_info_0 = (EditText)mView.findViewById(R.id.edit_info_0);
		edit_info_1 = (EditText)mView.findViewById(R.id.edit_info_1);
		edit_info_2 = (EditText)mView.findViewById(R.id.edit_info_2);
		edit_info_3 = (EditText)mView.findViewById(R.id.edit_info_3);
		edit_info_4 = (EditText)mView.findViewById(R.id.edit_info_4);
		edit_info_5 = (EditText)mView.findViewById(R.id.edit_info_5);
		edit_info_6 = (EditText)mView.findViewById(R.id.edit_info_6);
		edit_info_7 = (EditText)mView.findViewById(R.id.edit_info_7);
		edit_info_8 = (EditText)mView.findViewById(R.id.edit_info_8);
		edit_info_9 = (EditText)mView.findViewById(R.id.edit_info_9);
		edit_info.add(edit_info_0);
		edit_info.add(edit_info_1);
		edit_info.add(edit_info_2);
		edit_info.add(edit_info_3);
		edit_info.add(edit_info_4);
		edit_info.add(edit_info_5);
		edit_info.add(edit_info_6);
		edit_info.add(edit_info_7);
		edit_info.add(edit_info_8);
		edit_info.add(edit_info_9);
		
		edit_meter_num_0 = (EditText)mView.findViewById(R.id.edit_meter_num_0);
		edit_meter_num_1 = (EditText)mView.findViewById(R.id.edit_meter_num_1);
		edit_meter_num_2 = (EditText)mView.findViewById(R.id.edit_meter_num_2);
		edit_meter_num_3 = (EditText)mView.findViewById(R.id.edit_meter_num_3);
		edit_meter_num_4 = (EditText)mView.findViewById(R.id.edit_meter_num_4);
		edit_meter_num_5 = (EditText)mView.findViewById(R.id.edit_meter_num_5);
		edit_meter_num_6 = (EditText)mView.findViewById(R.id.edit_meter_num_6);
		edit_meter_num_7 = (EditText)mView.findViewById(R.id.edit_meter_num_7);
		edit_meter_num_8 = (EditText)mView.findViewById(R.id.edit_meter_num_8);
		edit_meter_num_9 = (EditText)mView.findViewById(R.id.edit_meter_num_9);
		edit_meter_num.add(edit_meter_num_0);
		edit_meter_num.add(edit_meter_num_1);
		edit_meter_num.add(edit_meter_num_2);
		edit_meter_num.add(edit_meter_num_3);
		edit_meter_num.add(edit_meter_num_4);
		edit_meter_num.add(edit_meter_num_5);	
		edit_meter_num.add(edit_meter_num_6);
		edit_meter_num.add(edit_meter_num_7);
		edit_meter_num.add(edit_meter_num_8);
		edit_meter_num.add(edit_meter_num_9);
		
		ArrayAdapter module_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, LISTMETERTYPE);   
		module_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);   
		spinner_type_0 = (Spinner)mView.findViewById(R.id.spinner_type_0);
		spinner_type_0.setAdapter(module_adapter);
		spinner_type_1 = (Spinner)mView.findViewById(R.id.spinner_type_1);
		spinner_type_1.setAdapter(module_adapter);
		spinner_type_2 = (Spinner)mView.findViewById(R.id.spinner_type_2);
		spinner_type_2.setAdapter(module_adapter);
		spinner_type_3 = (Spinner)mView.findViewById(R.id.spinner_type_3);
		spinner_type_3.setAdapter(module_adapter);
		spinner_type_4 = (Spinner)mView.findViewById(R.id.spinner_type_4);
		spinner_type_4.setAdapter(module_adapter);
		spinner_type_5 = (Spinner)mView.findViewById(R.id.spinner_type_5);
		spinner_type_5.setAdapter(module_adapter);
		spinner_type_6 = (Spinner)mView.findViewById(R.id.spinner_type_6);
		spinner_type_6.setAdapter(module_adapter);
		spinner_type_7 = (Spinner)mView.findViewById(R.id.spinner_type_7);
		spinner_type_7.setAdapter(module_adapter);
		spinner_type_8 = (Spinner)mView.findViewById(R.id.spinner_type_8);
		spinner_type_8.setAdapter(module_adapter);
		spinner_type_9 = (Spinner)mView.findViewById(R.id.spinner_type_9);
		spinner_type_9.setAdapter(module_adapter);
		spinner_type.add(spinner_type_0);
		spinner_type.add(spinner_type_1);
		spinner_type.add(spinner_type_2);
		spinner_type.add(spinner_type_3);
		spinner_type.add(spinner_type_4);
		spinner_type.add(spinner_type_5);
		spinner_type.add(spinner_type_6);
		spinner_type.add(spinner_type_7);
		spinner_type.add(spinner_type_8);
		spinner_type.add(spinner_type_9);
		
		btn_control_0 = (Button)mView.findViewById(R.id.btn_control_0);
		btn_control_1 = (Button)mView.findViewById(R.id.btn_control_1);
		btn_control_2 = (Button)mView.findViewById(R.id.btn_control_2);
		btn_control_3 = (Button)mView.findViewById(R.id.btn_control_3);
		btn_control_4 = (Button)mView.findViewById(R.id.btn_control_4);
		btn_control_5 = (Button)mView.findViewById(R.id.btn_control_5);
		btn_control_6 = (Button)mView.findViewById(R.id.btn_control_6);
		btn_control_7 = (Button)mView.findViewById(R.id.btn_control_7);
		btn_control_8 = (Button)mView.findViewById(R.id.btn_control_8);
		btn_control_9 = (Button)mView.findViewById(R.id.btn_control_9);
		btn_control_0.setOnClickListener(this);
		btn_control_1.setOnClickListener(this);
		btn_control_2.setOnClickListener(this);
		btn_control_3.setOnClickListener(this);
		btn_control_4.setOnClickListener(this);
		btn_control_5.setOnClickListener(this);
		btn_control_6.setOnClickListener(this);
		btn_control_7.setOnClickListener(this);
		btn_control_8.setOnClickListener(this);
		btn_control_9.setOnClickListener(this);
	}
	
	public void clearItem(int position){
		edit_info.get(position).setText("");
		edit_meter_num.get(position).setText("");
		spinner_type.get(position).setSelection(0);
		clearMeterDevice(meterList, position);
	}
	
	public void clearAllItem(){
		for(int i=0; i<edit_info.size(); i++){
			clearItem(i);
		}
	}
	
	public ArrayList<EnergyModel> getEnergyDevice(int type){
		ArrayList<EnergyModel> returnList = new ArrayList<EnergyModel>();
		mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
		ArrayList<EnergyModel> queryList = mDataHelper.queryForEnergyList(mSQLiteDatabase, DataHelper.ENERGY_TABLE, 
				EnergyModel.IEEE+"=\""+mDevices.getmIeee()+"\" and "+
				EnergyModel.D_TYPE + "=" +type, null);
		for(int i=0; i<LISTNUM_ARRAY[type]; i++){
			returnList.add(getSingleDevice(i, type, queryList));
		}
		return returnList;
	}
	
	public ArrayList<EnergyModel> getEnergyMeterDevice(int type){
		ArrayList<EnergyModel> returnList = new ArrayList<EnergyModel>();
		mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
		ArrayList<EnergyModel> queryList = mDataHelper.queryForEnergyList(mSQLiteDatabase, DataHelper.ENERGY_TABLE, 
				EnergyModel.IEEE+"=\""+mDevices.getmIeee()+"\" and "+
				EnergyModel.D_TYPE + "=" +type, null);
		if(queryList.size() > 0){
			btn_period.setText(Time2String(queryList.get(0).getStatus()));
			module = 1;
			module_spinner.setSelection(1);
		}
		for(int i=0; i<queryList.size(); i++){
			queryList.get(i).setNum(i);
			returnList.add(queryList.get(i));
		}
		for(int i=queryList.size(); i<LISTNUM_ARRAY[type]; i++){
			returnList.add(getSingleDevice(i, type, queryList));
		}
		return returnList;
	}
	
	public HashMap<String, ArrayList<EnergyModel>> getEnergyMeterAllDevice(){
		String ieee_addr = "";
		HashMap<String, ArrayList<EnergyModel>> map = new HashMap<String, ArrayList<EnergyModel>>();
		ArrayList<EnergyModel> list = new ArrayList<EnergyModel>();
		mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
		ArrayList<EnergyModel> queryList = mDataHelper.queryForEnergyList(mSQLiteDatabase, DataHelper.ENERGY_TABLE, 
				EnergyModel.IEEE+"!=\""+mDevices.getmIeee()+"\" and "+
				EnergyModel.D_TYPE + "=" +3, null);
		for(int i=0; i<queryList.size(); i++){
			EnergyModel mEnergyModel = queryList.get(i);
			String mieee_addr = mEnergyModel.getIeee() + "," + mEnergyModel.getAddr();
			if(mieee_addr.equals(ieee_addr)){
				list.add(mEnergyModel);
			}else{
				if(ieee_addr.equals("")){
					ieee_addr = mieee_addr;
					list.add(mEnergyModel);
				}else{
					map.put(ieee_addr, list);
					ieee_addr = mieee_addr;
					list = new ArrayList<EnergyModel>() ;
					list.add(mEnergyModel);
				}
			}
		}
		if(list.size() > 0){
			map.put(ieee_addr, list);  //最后一个节点
		}
		return map;
	}
	

	public EnergyModel getSingleDevice(int num, int type, ArrayList<EnergyModel> queryList){
		if(type != 3){
			for(int i=0; i<queryList.size(); i++){
				if(queryList.get(i).getNum() == num){
					return queryList.get(i);
				}
			}
			return new EnergyModel(
					mDevices.getmIeee(),
					mDevices.getmNWKAddr(),
					num,
					type,
					Integer.parseInt(mDevices.getmRid()));
		}
		return new EnergyModel(
				mDevices.getmIeee(),
				mDevices.getmNWKAddr(),
				num,
				type,
				Integer.parseInt(mDevices.getmRid()),
				0);
	}
	
	public class EnergyAdapter extends BaseAdapter{
		private ArrayList<EnergyModel> mList;
		private ArrayList<ViewHolder> mHolderList;
    	
    	public EnergyAdapter(ArrayList<EnergyModel> list){
    		mList = list;
    		mHolderList = new ArrayList<ViewHolder>();
    	}
//    	public void setTermList(){
//    		energylist = list;
//    	}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mHolderList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder mHolder;
			final EnergyModel mEnergyModel = mList.get(position);

			if (null == convertView) {
				mHolder = new ViewHolder();
				convertView = LayoutInflater.from((Context) getActivity()).inflate(
						R.layout.energy_item, null);
				mHolder.term_num = (TextView) convertView.findViewById(R.id.text_num);
				mHolder.term_info = (EditText) convertView.findViewById(R.id.edit_info);
				mHolder.term_btn = (Button) convertView.findViewById(R.id.btn_control);
				mHolder.term_btn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mList.get(position).setInfo("");
						notifyDataSetChanged();
					}
				});
				
				convertView.setTag(mHolder);
				mHolderList.add(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			if(mEnergyModel.getType() == 1){
				mHolder.term_num.setText("安防:"+mEnergyModel.getNum());
			}else if(mEnergyModel.getType() == 2){
				mHolder.term_num.setText("家电:"+mEnergyModel.getNum());
			}
			
			mHolder.term_info.setText(""+mEnergyModel.getInfo());
			
			
			
			return convertView;
		}

		public class ViewHolder {
			TextView term_num;
			EditText term_info;
			Button term_btn;
		}
    	
    }


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cgiManager.deleteObserver(EnergyEditFragment.this);
		DeviceManager.getInstance().deleteObserver(this);
	}

	@Override
	public void stopRefresh() {
		// TODO Auto-generated method stub

	}
	
	public void clearDevice(ArrayList<EnergyModel> clearList){
		for(EnergyModel mEnergyModel : clearList){
			mEnergyModel.setInfo("");
		}
	}
	
	public void clearMeterDevice(ArrayList<EnergyModel> clearList){
		for(EnergyModel mEnergyModel : clearList){
			mEnergyModel.setInfo("");
			mEnergyModel.setMeterNum("");
			mEnergyModel.setSubType(0);
		}
	}
	
	public void clearMeterDevice(ArrayList<EnergyModel> clearList, int position){
		clearList.get(position).setInfo("");
		clearList.get(position).setMeterNum("");
		clearList.get(position).setSubType(0);
	}
	
	
	
	public void updateInfo(){
		
		if(module == 1){
			for(int i=0; i<10; i++){
				if(!edit_info.get(i).getText().toString().equals("")){
					meterList.get(i).setInfo(edit_info.get(i).getText().toString());
					if(!edit_meter_num.get(i).getText().toString().equals("")){
						meterList.get(i).setMeterNum(edit_meter_num.get(i).getText().toString());
					}else{
						Toast.makeText(getActivity(), "请输入 " + "编号:" + i + " 表编号", Toast.LENGTH_SHORT).show();
						return;
					}
					if(spinner_type.get(i).getSelectedItemPosition() > 0){
						meterList.get(i).setSubType(spinner_type.get(i).getSelectedItemPosition());
					}else{
						Toast.makeText(getActivity(), "请选择 " + "编号:" + i + " 表类型", Toast.LENGTH_SHORT).show();
						return;
					}		
				}
			}
		}else{
			for(int i=0; i<securityList.size(); i++){
				ViewHolder mHolder = (ViewHolder)securityAdapter.getItem(i);
				securityList.get(i).setInfo(mHolder.term_info.getText().toString());
			}
			for(int i=0; i<onoffList.size(); i++){
				ViewHolder mHolder = (ViewHolder)onoffAdapter.getItem(i);
				onoffList.get(i).setInfo(mHolder.term_info.getText().toString());
			}
		}
		updateNode();
	}
	
	class SpinnerSelectedListener implements OnItemSelectedListener{   
		  public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,   
                long arg3) {    
            module = arg2;
            if(module == 0){
            	security_layout.setVisibility(View.VISIBLE);
            	meter_layout.setVisibility(View.GONE);
            }
            if(module == 1){
            	security_layout.setVisibility(View.GONE);
            	meter_layout.setVisibility(View.VISIBLE);
            }
		  }   
		  public void onNothingSelected(AdapterView<?> arg0) {   
		  }   
	}   


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.btn_save:
//			if(module == 1){
//				JSONObject json = new JSONObject();
//				try{
//					
//					json.put("MsgType", EnergyModel.GET_METER_LIST_SEND);
//					json.put("Sn", 10);
//				}catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				Intent intent = new Intent("com.gdgl.service.EnergyService");
//				Bundle bundle = new Bundle();
//				bundle.putString(EnergyModel.JSONSTRING, json.toString());
//				intent.putExtras(bundle);
//				getActivity().startService(intent);
//				return;
//			}
			updateInfo();
			break;
		case R.id.btn_cancel:
			getActivity().onBackPressed();
			break;
		case R.id.btn_clear:
			clearDevice(securityList);
			clearDevice(onoffList);
			clearMeterDevice(meterList);
			securityAdapter.notifyDataSetChanged();
			onoffAdapter.notifyDataSetChanged();
			clearAllItem();
			break;	
		case R.id.btn_period:
			int time = String2Time(btn_period.getText().toString());
			int hour = time/60;
			int minute = time%60;
			new TimePickerDialog(getActivity(), new OnTimeSetListener() {			
				
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
					int time = hourOfDay*60 + minute;
					btn_period.setText(Time2String(time));
				}
			}, hour, minute, true).show();
			break;
		case R.id.btn_control_0:
			clearItem(0);
			break;
		case R.id.btn_control_1:
			clearItem(1);
			break;
		case R.id.btn_control_2:
			clearItem(2);
			break;
		case R.id.btn_control_3:
			clearItem(3);
			break;
		case R.id.btn_control_4:
			clearItem(4);
			break;
		case R.id.btn_control_5:
			clearItem(5);
			break;
		case R.id.btn_control_6:
			clearItem(6);
			break;
		case R.id.btn_control_7:
			clearItem(7);
			break;
		case R.id.btn_control_8:
			clearItem(8);
			break;
		case R.id.btn_control_9:
			clearItem(9);
			break;
		}
	}
	
	public void updateNode(){
		JSONObject json = new JSONObject();
		try{
			if(module != 1){
				json.put("MsgType", EnergyModel.SET_DEVICES_SEND);
				json.put("Sn", 10);
				json.put("GlobalOpt", 0);
				JSONArray nodeArray = new JSONArray();
				JSONObject nodeObject = new JSONObject();
					nodeObject.put("SecurityNodeID", mDevices.getmIeee());
					nodeObject.put("Nwkaddr", mDevices.getmNWKAddr());
					JSONArray termArray = new JSONArray();
					for(EnergyModel mEnergyModel : securityList){
						if(mEnergyModel.getInfo().equals("")){
							continue;
						}
						JSONObject termObject = new JSONObject();
							termObject.put("Type", mEnergyModel.getType());
							termObject.put("SubType", mEnergyModel.getSubType());
							termObject.put("Num", mEnergyModel.getNum());
							termObject.put("Info", mEnergyModel.getInfo());
	//						termObject.put("OperatorType", mEnergyModel.getStatus());
							termObject.put("OperatorType", 2);
						termArray.put(termObject);
					}
					for(EnergyModel mEnergyModel : onoffList){
						if(mEnergyModel.getInfo().equals("")){
							continue;
						}
						JSONObject termObject = new JSONObject();
							termObject.put("Type", mEnergyModel.getType());
							termObject.put("SubType", mEnergyModel.getSubType());
							termObject.put("Num", mEnergyModel.getNum());
							termObject.put("Info", mEnergyModel.getInfo());
	//						termObject.put("OperatorType", mEnergyModel.getStatus());
							termObject.put("OperatorType", 2);
						termArray.put(termObject);
					}
					
					nodeObject.put("SubNode", termArray);
					nodeArray.put(nodeObject);
				json.put("NodeList", nodeArray);
			}else{
				json.put("MsgType", EnergyModel.SET_METER_LIST_SEND);
				json.put("Sn", 10);
				JSONArray nodeArray = new JSONArray();
					JSONObject nodeObject = new JSONObject();
					nodeObject.put("IEEE", mDevices.getmIeee());
					nodeObject.put("Nwkaddr", mDevices.getmNWKAddr());
					JSONArray termArray = new JSONArray();
					for(EnergyModel mEnergyModel : meterList){
						if(!mEnergyModel.getInfo().equals("")){
							JSONObject termObject = new JSONObject();
							termObject.put("TermCode", mEnergyModel.getMeterNum());
							termObject.put("TermType", mEnergyModel.getSubType());
							termObject.put("TermInfo", mEnergyModel.getInfo());
							termObject.put("TermPeriod", String2Time(btn_period.getText().toString()));
							termArray.put(termObject);
						}	
					}	
					nodeObject.put("TermList", termArray);
					nodeArray.put(nodeObject);
					
					HashMap<String, ArrayList<EnergyModel>> map = getEnergyMeterAllDevice();
					Iterator iter = map.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						String key = (String) entry.getKey();
						ArrayList<EnergyModel> val = (ArrayList<EnergyModel>) entry.getValue();
						String[] keyString = key.split(",");
						
						JSONObject nodeObject1 = new JSONObject();
						nodeObject1.put("IEEE", keyString[0]);
						nodeObject1.put("Nwkaddr", keyString[1]);
						JSONArray termArray1 = new JSONArray();
						for(EnergyModel mEnergyModel : val){
							if(!mEnergyModel.getInfo().equals("") && !mEnergyModel.getMeterNum().equals("") && mEnergyModel.getSubType() != 0){
								JSONObject termObject1 = new JSONObject();
								termObject1.put("TermCode", mEnergyModel.getMeterNum());
								termObject1.put("TermType", mEnergyModel.getSubType());
								termObject1.put("TermInfo", mEnergyModel.getInfo());
								termObject1.put("TermPeriod", mEnergyModel.getStatus());
								termArray1.put(termObject1);
							}	
						}	
						nodeObject1.put("TermList", termArray1);
						nodeArray.put(nodeObject1);
						
					}
				json.put("NodeList", nodeArray);
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent intent = new Intent("com.gdgl.service.EnergyService");
		Bundle bundle = new Bundle();
		bundle.putString(EnergyModel.JSONSTRING, json.toString());
		intent.putExtras(bundle);
		getActivity().startService(intent);
		Toast.makeText(getActivity(), "保存成功!", Toast.LENGTH_SHORT).show();
	}
	
	public String Time2String(int time){
		String hour = "" + time/60;
		String minute = "" + time%60;
		if(hour.length() == 1){
			hour = "0" + hour; 
		}
		if(minute.length() == 1){
			minute = "0" + minute; 
		}
		return hour + ":" + minute;
	}
	
	public int String2Time(String timeString){
		String[] timeStrings = timeString.split(":");
		return Integer.parseInt(timeStrings[0])*60 + Integer.parseInt(timeStrings[1]);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		
	}
}
