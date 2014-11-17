package com.gdgl.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdgl.model.DevicesGroup;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SceneDevicesAdapter extends BaseAdapter {
	
	private static final int ITEM_TYPE_COUNT=4;
	
	public static final int LIGHT = 0;
	public static final int ON_OFF = 1;
	public static final int WITH_VALUE = 2;
	public static final int NO_OPERATOR = 3;
	
	
	protected Context mContext;
	protected List<DevicesModel> mDevicesList;
	protected AddChecked mDevicesObserver;
	
	private static  Map<Integer,Boolean> isSelected;
	private static  Map<Integer,Boolean> isOnOff;
	private static  Map<Integer,Integer> DevicesValue;

	public static final String DEVICES_ID = "devices_id";
	public static final String BOLLEAN_ARRARY = "devices_state";
	public static final String DEVIVES_VALUE = "devices_value";
	
	private int GroupId=-1;
	private String GroupName="";

	ViewHolder lay;
	
	public SceneDevicesAdapter(Context c, List<DevicesModel> list,
			AddChecked mObserver,String groupname) {
		mContext = c;
		mDevicesList = list;
		mDevicesObserver = mObserver;
		isSelected = new HashMap<Integer, Boolean>();
		isOnOff = new HashMap<Integer, Boolean>();
		DevicesValue = new HashMap<Integer, Integer>();
		Log.i(DEVICES_ID, "zgs-> "+isSelected.size());
		for(int i=0;i<mDevicesList.size();i++){
			isSelected.put(i, false);
			isOnOff.put(i, true);
			DevicesValue.put(i, 50);
		}
		GroupName=groupname;
		getFromSharedPreferences.setsharedPreferences(c);
		GroupId=getFromSharedPreferences.getSceneId();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (null != mDevicesList) {
			return mDevicesList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (null != mDevicesList) {
			return mDevicesList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		if (null != mDevicesList) {
			return (long) mDevicesList.get(position).getID();
		}
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return UiUtils.getSceneItemType(mDevicesList.get(position).getmDeviceId());
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return ITEM_TYPE_COUNT;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View mView = convertView;
		final ViewHolder mHolder;
		final DevicesModel mDevices = mDevicesList.get(position);
		final DevicesGroup mDevicesGroup=new DevicesGroup(mContext);
		mDevicesGroup.setDevicesState(mDevices.getmOnOffStatus().trim().equals("1"));
		mDevicesGroup.setEp(mDevices.getmEP());
		mDevicesGroup.setGroupId(GroupId);
		mDevicesGroup.setGroupName(GroupName);
		mDevicesGroup.setIeee(mDevices.getmIeee());
		
		int type = getItemViewType(position);
		if(null == mView){
			mHolder = new ViewHolder();
			switch (type) {
			case LIGHT:
				mView = LayoutInflater.from(mContext).inflate(
				R.layout.scene_item_light, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mHolder.devices_state = (TextView) mView
						.findViewById(R.id.devices_state);
				mHolder.devices_region = (TextView) mView
						.findViewById(R.id.devices_region);
				mHolder.onoff_check = (CheckBox) mView
						.findViewById(R.id.onoff_check);
				mHolder.selected = (CheckBox) mView
						.findViewById(R.id.selected);
				mHolder.config_state=(LinearLayout)mView.findViewById(R.id.config_state);
				mHolder.mSimpleDevicesModel=mDevices;
				mView.setTag(mHolder);
				break;
			case ON_OFF:
				mView = LayoutInflater.from(mContext).inflate(
				R.layout.scene_item_onoff, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mHolder.devices_state = (TextView) mView
						.findViewById(R.id.devices_state);
				mHolder.devices_region = (TextView) mView
						.findViewById(R.id.devices_region);
				mHolder.onoff_check = (CheckBox) mView
						.findViewById(R.id.onoff_check);
				mHolder.selected = (CheckBox) mView
						.findViewById(R.id.selected);
				mHolder.config_state=(LinearLayout)mView.findViewById(R.id.config_state);
				mHolder.mSimpleDevicesModel=mDevices;
				mView.setTag(mHolder);
				break;
			case WITH_VALUE:
				mView = LayoutInflater.from(mContext).inflate(
				R.layout.scene_item_withvalue, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mHolder.devices_state = (TextView) mView
						.findViewById(R.id.devices_state);
				mHolder.devices_region = (TextView) mView
						.findViewById(R.id.devices_region);
				mHolder.text_value = (TextView) mView
						.findViewById(R.id.text_value);
				mHolder.devices_seek = (SeekBar) mView
						.findViewById(R.id.devices_seek);
				mHolder.selected = (CheckBox) mView
						.findViewById(R.id.selected);
				mHolder.config_state=(LinearLayout)mView.findViewById(R.id.config_state);
				mHolder.mSimpleDevicesModel=mDevices;
				mView.setTag(mHolder);
				break;
			case NO_OPERATOR:
				mView = LayoutInflater.from(mContext).inflate(
				R.layout.scene_item_nooperator, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mHolder.selected = (CheckBox) mView
						.findViewById(R.id.selected);
				mHolder.mSimpleDevicesModel=mDevices;
				mView.setTag(mHolder);
				break;
			default:
				break;
			}
		}else {
			mHolder = (ViewHolder) mView.getTag();
		}
		mHolder.devices_name.setText(mDevices.getmDefaultDeviceName().replace(" ",
				""));

		mHolder.devices_img.setImageResource(DataUtil
				.getDefaultDevicesSmallIcon(mDevices.getmDeviceId(),mDevices.getmModelId().trim()));
//		if (DataHelper.IAS_ZONE_DEVICETYPE == mDevices.getmDeviceId()
//				|| DataHelper.IAS_ACE_DEVICETYPE == mDevices.getmDeviceId()) {
//
//			mHolder.devices_img
//					.setImageResource(UiUtils
//							.getDevicesSmallIconByModelId(mDevices
//									.getmModelId().trim()));
//		} else if (mDevices.getmModelId().indexOf(
//				DataHelper.Multi_key_remote_control) == 0) {
//			mHolder.devices_img.setImageResource(UiUtils
//					.getDevicesSmallIconForRemote(mDevices.getmDeviceId()));
//		}else {
//			mHolder.devices_img.setImageResource(UiUtils
//					.getDevicesSmallIcon(mDevices.getmDeviceId()));
//		}
		if(null!=mHolder.onoff_check){
			
			mHolder.onoff_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					isOnOff.put(position, isChecked);
					mHolder.selected.setChecked(true);
					isSelected.put(position, true);
				}
			});
			mHolder.onoff_check.setChecked(isOnOff.get(position));
		}
		if(null!=mHolder.devices_seek){
			mHolder.devices_seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// TODO Auto-generated method stub
					mHolder.text_value.setText(progress+"%");
					mDevicesGroup.setDevicesValue(progress);
					mHolder.selected.setChecked(true);
					isSelected.put(position, true);
					DevicesValue.put(position, progress);
				}
			});
			mHolder.text_value.setText(DevicesValue.get(position)+"%");
			mHolder.devices_seek.setProgress(DevicesValue.get(position));
		}

		
		mHolder.selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				Log.i("zzz", "zgszz->mHolder.selected.setOnCheckedChangeListener mposition="+position);
				isSelected.put(position, isChecked);
				if(isChecked){
					mDevicesObserver.AddCheckedDevices(mDevicesGroup);
				}else{
					mDevicesObserver.DeletedCheckedDevices(mDevicesGroup);
				}
			}
		});
		mHolder.selected.setChecked(isSelected.get(position));
		return mView;
	}


	
	public interface AddChecked{
		public void AddCheckedDevices(DevicesGroup s);
		public void DeletedCheckedDevices(DevicesGroup s);
	}
	
	public class ViewHolder {
		public ImageView devices_img;
		public TextView devices_name;
		public TextView devices_state;
		public TextView devices_region;
		public TextView text_value;
		public CheckBox selected;
		public CheckBox onoff_check;
		public SeekBar devices_seek;
		public LinearLayout config_state;
		public DevicesModel mSimpleDevicesModel;
	}
	
	public interface DevicesObserver {

		public void setLayout();

		public void deleteDevices(String id);

	}
	

	public void setList(List<DevicesModel> list) {
		mDevicesList = null;
		mDevicesList = list;
	}

}
