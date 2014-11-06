package com.gdgl.adapter;

import java.util.List;

import com.gdgl.app.ApplicationController;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.WarnManager;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;
import com.gdgl.util.UiUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/***
 * 列表上的ui的adapter
 * 
 * @author Administrator
 * 
 */
public class DevicesBaseAdapter extends BaseAdapter implements Dialogcallback {

	protected Context mContext;
	protected List<SimpleDevicesModel> mDevicesList;
	protected DevicesObserver mDevicesObserver;
	DevicesModel oneKeyOperatorDevice;
	CGIManager mcgiManager;

	public static final int ON_OFF = 1;
	public static final int WITH_VALUE = 2;
	public static final int NO_OPERATOR = 3;

	public static final String DEVICES_ID = "devices_id";
	public static final String BOLLEAN_ARRARY = "devices_state";
	public static final String DEVIVES_VALUE = "devices_value";

	public class SwitchModel {
		public String modelId;
		public String name;
		public String[] anotherName;
		public String[] ieee;
		public boolean[] state;

	}

	public DevicesBaseAdapter(Context c, DevicesObserver mObserver) {
		mContext = c;
		// mDevicesList = list;
		mDevicesObserver = mObserver;
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

	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int devicesid = mDevicesList.get(position).getmDeviceId();

		int[] mNOOPER = { DataHelper.LIGHT_SENSOR_DEVICETYPE,
				DataHelper.TEMPTURE_SENSOR_DEVICETYPE,
				DataHelper.REMOTE_CONTROL_DEVICETYPE,
				DataHelper.RANGE_EXTENDER_DEVICETYPE,
				DataHelper.IAS_ACE_DEVICETYPE,
				DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE };
		int[] mWITH_VALUE = { DataHelper.DIMEN_SWITCH_DEVICETYPE,
				DataHelper.DIMEN_LIGHTS_DEVICETYPE };
		for (int i : mWITH_VALUE) {
			if (i == devicesid) {
				return WITH_VALUE;
			}
		}
		for (int i : mNOOPER) {
			if (i == devicesid) {
				return NO_OPERATOR;
			}
		}
		if (devicesid == DataHelper.IAS_ZONE_DEVICETYPE) {
			String modelid = mDevicesList.get(position).getmModelId();
			String str = modelid.substring(0, 4);
			// 如果为各种气体探测器或紧急按钮
			if (str.trim().equals("ZA01")
					|| modelid.indexOf(DataHelper.Emergency_Button) == 0) {
				return NO_OPERATOR;
			} else {
				return ON_OFF;
			}
		}
		return ON_OFF;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		new getOneKeyOperatorStatusTask().execute(0);

		if (null == mDevicesList) {
			return convertView;
		}
		View mView = convertView;
		final SimpleDevicesModel mDevices = mDevicesList.get(position);
		int type = getItemViewType(position);

		mcgiManager = CGIManager.getInstance();

		mView = LayoutInflater.from(mContext).inflate(
				R.layout.devices_list_item, null);
		devices_img = (ImageView) mView.findViewById(R.id.devices_img);
		devices_name = (TextView) mView.findViewById(R.id.devices_name);
		warn_state = (TextView) mView.findViewById(R.id.warn_state);
		devices_region = (TextView) mView.findViewById(R.id.devices_region);
		devices_state = (TextView) mView.findViewById(R.id.devices_state);
		switchlayout = (LinearLayout) mView.findViewById(R.id.switch_layout);
		devices_switch = (Switch) mView.findViewById(R.id.switch_btn);
		seekbar_statelayout = (LinearLayout) mView
				.findViewById(R.id.seekbar_state);
		devices_seek_bar = (SeekBar) mView.findViewById(R.id.devices_seek_bar);
		btn_layout = (LinearLayout) mView.findViewById(R.id.button_layout);
		devices_button = (Button) mView.findViewById(R.id.button_device);

		switch (type) {
		case ON_OFF:
			switchlayout.setVisibility(View.VISIBLE);
			seekbar_statelayout.setVisibility(View.GONE);
			break;
		case WITH_VALUE:
			switchlayout.setVisibility(View.GONE);
			seekbar_statelayout.setVisibility(View.VISIBLE);
			break;
		case NO_OPERATOR:
			switchlayout.setVisibility(View.GONE);
			seekbar_statelayout.setVisibility(View.GONE);
			break;
		default:
			break;
		}

		if (mDevices.getmModelId().indexOf("ZA01") != 0
				&& mDevices.getmModelId().indexOf(DataHelper.Emergency_Button) != 0
				&& mDevices.getmDeviceId() == DataHelper.IAS_ZONE_DEVICETYPE
				&& oneKeyOperatorDevice != null
				&& oneKeyOperatorDevice.getmOnOffStatus().equals("0")) {
			devices_switch.setClickable(false);
		} else {
			devices_switch.setClickable(true);
		}

		devices_name.setText(mDevices.getmUserDefineName().replace(" ", ""));
		devices_region.setText(mDevices.getmDeviceRegion().replace(" ", ""));
		if (mDevices.getmModelId().indexOf(DataHelper.RS232_adapter) == 0) {
			devices_region.setText("");
		}
		warn_state.setText("");
		if (DataHelper.IAS_ZONE_DEVICETYPE == mDevices.getmDeviceId()
				|| DataHelper.IAS_ACE_DEVICETYPE == mDevices.getmDeviceId()
				|| DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE == mDevices
						.getmDeviceId()) {

			if (WarnManager.getInstance().isDeviceWarning(mDevices)) {
				warn_state.setText("正在报警!");
			}
			if (WarnManager.getInstance().isWarnning()
					&& mDevices.getmModelId().indexOf(DataHelper.Siren) == 0) {
				warn_state.setText("报警器响!");
			}
//			devices_img
//					.setImageResource(UiUtils
//							.getDevicesSmallIconByModelId(mDevices
//									.getmModelId().trim()));
//		} else if (mDevices.getmModelId().indexOf(
//				DataHelper.Multi_key_remote_control) == 0) { // 多键遥控器
//			devices_img.setImageResource(UiUtils
//					.getDevicesSmallIconForRemote(mDevices.getmDeviceId()));
//		} else {
//			devices_img.setImageResource(UiUtils.getDevicesSmallIcon(
//					mDevices.getmDeviceId(), mDevices.getmModelId().trim()));
		}
		
		devices_img.setImageResource(UiUtils.getDevicesSmallIcon(
				mDevices.getmDeviceId(), mDevices.getmModelId().trim()));

		if (mDevices.getmDeviceId() == DataHelper.ON_OFF_SWITCH_DEVICETYPE) { // 开关（如“三键开关”、“双键开关”）
			String state = "";
			String s = mDevices.getmOnOffStatus();
			Log.i("tag", "tag->" + s);
			String[] result = s.split(",");
			for (String string : result) {
				if (string.trim().equals("1")) {
					state += "开 ";
					devices_switch.setChecked(true);
				} else {
					state += "关 ";
					devices_switch.setChecked(false);
				}
			}
			Log.i("tag", "tag->" + state);
			devices_state.setText(state);
		} else if (mDevices.getmDeviceId() == DataHelper.IAS_ZONE_DEVICETYPE) { // 烟雾感应器、可燃气体探测器（煤气）、（天然气）、（一氧化碳）、窗磁、门窗感应开关、紧急按钮、动作感应器
			if (mDevices.getmOnOffStatus().trim().equals("0")) {
				devices_state.setText("已布防");
				devices_switch.setChecked(true);
			} else {
				devices_state.setText("已撤防");
				devices_switch.setChecked(false);
			}
		} else if (mDevices.getmDeviceId() == DataHelper.LIGHT_SENSOR_DEVICETYPE) { // 光线感应器
			// devices_state.setText("亮度: "+mDevices.getmValue());
			if(mDevices.getmValue()==null||mDevices.getmValue().equals("")){
				devices_state.setText(getFromSharedPreferences.getLight());
			} else {
				devices_state.setText("亮度: " + mDevices.getmValue());
			}
		} else if (mDevices.getmDeviceId() == DataHelper.TEMPTURE_SENSOR_DEVICETYPE) { // 室内型温湿度感应器
			String temperature, humidity;
			if(mDevices.getmValue()==null||mDevices.getmValue().equals("")){
				temperature=getFromSharedPreferences.getTemperature();
			} else {
				temperature=mDevices.getmValue();
			}
			if(mDevices.getHumidityValue()==null||mDevices.getHumidityValue().equals("")){
				humidity=getFromSharedPreferences.getHumidity();
			} else {
				humidity=mDevices.getHumidityValue();
			}
			devices_state.setText("温度: "
					+ temperature + "\n湿度: "
					+ humidity + "%");
		} else if (mDevices.getmModelId().indexOf(DataHelper.One_key_operator) == 0) {
			// devices_state.setText("一键操作");
			if (mDevices.getmOnOffStatus().trim().equals("1")) {
				devices_state.setText("开启");
				devices_switch.setChecked(true);
			} else {
				devices_state.setText("关闭");
				devices_switch.setChecked(false);
			}
		} else if (mDevices.getmModelId().indexOf(
				DataHelper.Energy_detection_dimming_module) == 0) { // 调光模块
			if (null != mDevices && mDevices.getmValue().trim().equals("")) {
				mDevices.setmValue("0");
			}
			String state = mDevices.getmValue();
			devices_seek_bar.setProgress(Integer.parseInt(state));
			if (null == state || state.trim().equals("")
					|| state.trim().equals("0")) {
				devices_state.setText("关");
			} else if (state.trim().equals("100")) {
				devices_state.setText("开");
			} else {
				devices_state.setText("开" + state + "%");
			}
			devices_seek_bar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						int currentProgress = Integer.parseInt(mDevices
								.getmValue());

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub
							notifyDataSetChanged();
							mcgiManager.dimmableLightOperation(mDevices, 10,
									currentProgress * 255 / 100);
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onProgressChanged(SeekBar seekBar,
								int progress, boolean fromUser) {
							// TODO Auto-generated method stub
							currentProgress = progress;
//							if (progress == 0) {
//								devices_state.setText("关");
//							} else if (progress == 100) {
//								devices_state.setText("开");
//							} else {
//								devices_state.setText("开" + progress + "%");
//							}
							mDevices.setmValue(String.valueOf(currentProgress));
						}
					});
		} else if (mDevices.getmDeviceId() == DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE) { // 警报器
			devices_state.setVisibility(View.GONE);
			btn_layout.setVisibility(View.VISIBLE);
			if (WarnManager.getInstance().isWarnning()) {
				devices_button.setClickable(true);
			}
			devices_button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mcgiManager.IASWarningDeviceOperationCommon(mDevices, 0);
					warn_state.setText("");
					WarnManager.getInstance().setWarnning(false);
					devices_button.setClickable(false);
				}
			});
		} else if (mDevices.getmModelId().indexOf("Z503") == 0
				|| mDevices.getmDeviceId() == DataHelper.RANGE_EXTENDER_DEVICETYPE) {
			devices_state.setVisibility(View.INVISIBLE);
		} else {
			if (mDevices.getmOnOffStatus().trim().equals("1")) {
				devices_state.setText("开");
				devices_switch.setChecked(true);
			} else {
				devices_state.setText("关");
				devices_switch.setChecked(false);
			}
		}
		devices_switch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							if (mDevices
									.getmModelId()
									.indexOf(
											DataHelper.Wireless_Intelligent_valve_switch) == 0) {
								// 无线智能阀门开关
								mcgiManager.OnOffOutputOperation(mDevices, 0);
							}
							if (mDevices.getmDeviceId() == DataHelper.COMBINED_INTERFACE_DEVICETYPE) {
								mcgiManager.LocalIASCIEOperation(null, 7);
								mDevices.setmOnOffStatus("1");
							}
							if (mDevices.getmDeviceId() == DataHelper.MAINS_POWER_OUTLET_DEVICETYPE) {
								// 开关模块（单路）、中规电能检测墙面插座、电能检测插座
								mcgiManager
										.MainsOutLetOperation(mDevices, 2, 1);
							}
							if (mDevices.getmDeviceId() == DataHelper.SHADE_DEVICETYPE) {
								// 窗帘
								mcgiManager.shadeOperation(mDevices, 0);
							}
							if (mDevices.getmDeviceId() == DataHelper.IAS_ZONE_DEVICETYPE) {
								// 烟雾感应器、可燃气体探测器（煤气）、（天然气）、（一氧化碳）、门窗感应开关、窗磁、紧急按钮、动作感应器
								mcgiManager.LocalIASCIEUnByPassZone(mDevices);
							}
						} else {
							if (mDevices
									.getmModelId()
									.indexOf(
											DataHelper.Wireless_Intelligent_valve_switch) == 0) {
								mcgiManager.OnOffOutputOperation(mDevices, 1);
							}
							if (mDevices.getmDeviceId() == DataHelper.COMBINED_INTERFACE_DEVICETYPE) {
								mcgiManager.LocalIASCIEOperation(null, 6);
								mDevices.setmOnOffStatus("0");
							}
							if (mDevices.getmDeviceId() == DataHelper.MAINS_POWER_OUTLET_DEVICETYPE) {
								mcgiManager
										.MainsOutLetOperation(mDevices, 2, 0);
							}
							if (mDevices.getmDeviceId() == DataHelper.SHADE_DEVICETYPE) {
								mcgiManager.shadeOperation(mDevices, 1);
							}
							if (mDevices.getmDeviceId() == DataHelper.IAS_ZONE_DEVICETYPE) {
								mcgiManager.LocalIASCIEByPassZone(mDevices);
							}
						}
					}
				});
		return mView;
	}

	public void setList(List<SimpleDevicesModel> list) {
		mDevicesList = null;
		mDevicesList = list;
	}

	ImageView devices_img;
	TextView devices_name;
	TextView warn_state;
	TextView devices_region;
	TextView devices_state;
	LinearLayout seekbar_statelayout;
	SeekBar devices_seek_bar;
	LinearLayout switchlayout;
	Switch devices_switch;
	LinearLayout btn_layout;
	Button devices_button;

	public interface DevicesObserver {

		public void setLayout();

		public void deleteDevices(String id);

	}

	class getOneKeyOperatorStatusTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			DataHelper dh = new DataHelper(ApplicationController.getInstance());
			SQLiteDatabase db = dh.getSQLiteDatabase();
			oneKeyOperatorDevice = DataUtil.getDeviceModelByModelid(
					DataHelper.One_key_operator, dh, db);
			db.close();
			return null;
		}
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		// lay.expandable_toggle_button.setChecked(true);
		// Animation anim = new ExpandCollapseAnimation(lay.expand,
		// ExpandCollapseAnimation.COLLAPSE);
		// anim.setDuration(300);
		// //lay.expand.startAnimation(anim);
		// mDevicesObserver.deleteDevices(index);
	}
	// public void setListView(ListView listView) {
	// this.listView = listView;
	// }

}
