package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdgl.activity.BaseControlFragment.UpdateDevice;
import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.LightManager;
import com.gdgl.manager.Manger;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.SimpleResponseData;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.mydata.getlocalcielist.CIEresponse_params;
import com.gdgl.mydata.getlocalcielist.elserec;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyDlg;

/***
 * 窗磁布撤防
 * 
 * @author justek
 * 
 */
public class LockFragment extends BaseControlFragment {

	int OnOffImg[];

	public static final int ON = 0;
	public static final int OFF = 1;

	View mView;
	SimpleDevicesModel mDevices;

	TextView txt_devices_name, txt_devices_region;
	RelativeLayout mError;

	ImageView on_off;

	boolean status = false;

	String Ieee = "";

	String ep = "";

	LightManager mLightManager;
	DataHelper mDataHelper;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		if (!(activity instanceof UpdateDevice)) {
			throw new IllegalStateException("Activity必须实现SaveDevicesName接口");
		}
		mUpdateDevice = (UpdateDevice) activity;
		super.onAttach(activity);
	}

	private void initstate() {
		// TODO Auto-generated method stub
		if (null != mDevices) {
			if (mDevices.getmOnOffStatus().trim().equals("0")) {
				status = true;
			}
			Ieee = mDevices.getmIeee().trim();
			ep = mDevices.getmEP().trim();
		}
	}

	private void setImagRes(ImageView mSwitch, boolean b) {
		// TODO Auto-generated method stub
		if (b) {
			mSwitch.setImageResource(OnOffImg[ON]);
		} else {
			mSwitch.setImageResource(OnOffImg[OFF]);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		if (null != extras) {
			mDevices = (SimpleDevicesModel) extras
					.getParcelable(DevicesListFragment.PASS_OBJECT);
			OnOffImg = extras.getIntArray(DevicesListFragment.PASS_ONOFFIMG);
		}

		mLightManager = LightManager.getInstance();
		mLightManager.addObserver(LockFragment.this);
		DeviceManager.getInstance().addObserver(this);
		
		mDataHelper=new DataHelper((Context)getActivity());
		
		initstate();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.on_off_control, null);
		initView();
		return mView;
	}

	private void initView() {
		// mLightManager.iASZoneOperationCommon(mDevices, 7, 1);
		// TODO Auto-generated method stub
		
		on_off = (ImageView) mView.findViewById(R.id.devices_on_off);

		txt_devices_name = (TextView) mView.findViewById(R.id.txt_devices_name);
		txt_devices_region = (TextView) mView
				.findViewById(R.id.txt_devices_region);

		txt_devices_name.setText(mDevices.getmUserDefineName().trim());
		txt_devices_region.setText(mDevices.getmDeviceRegion().trim());

		setImagRes(on_off, status);

		mError = (RelativeLayout) mView.findViewById(R.id.error_message);

		on_off.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mError.setVisibility(View.GONE);
				if (null == mDialog) {
					mDialog = MyDlg.createLoadingDialog(
							(Context) getActivity(), "操作正在进行...");
					mDialog.show();
				} else {
					mDialog.show();
				}
				if (status) {
					mLightManager.LocalIASCIEByPassZone(mDevices, -1);
				}else {
					mLightManager.LocalIASCIEUnByPassZone(mDevices,-1);
				}
				status=!status;
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLightManager.deleteObserver(LockFragment.this);
		DeviceManager.getInstance().deleteObserver(this);
	}

	@Override
	public void editDevicesName() {
		// TODO Auto-generated method stub

	}

	@SuppressLint("ShowToast")
	@Override
	public void update(Manger observer, Object object) {
		if (null != mDialog) {
			mDialog.dismiss();
			mDialog = null;
		}
		final Event event = (Event) object;
		if (EventType.LOCALIASCIEBYPASSZONE == event.getType()||EventType.LOCALIASCIEUNBYPASSZONE==event.getType()) {
			if (event.isSuccess()) {
				mDevices.setmOnOffStatus(status ? "0" : "1");
				updateStatusOnUIThread();
				ContentValues c = new ContentValues();
				c.put(DevicesModel.ON_OFF_STATUS, status ? "0" : "1");
				mUpdateDevice.updateDevices(mDevices, c);
				DeviceManager.getInstance().getLocalCIEList();
			}else
			{
				Toast.makeText(getActivity(), "操作失败", Toast.LENGTH_SHORT);
			}
		} else if (EventType.GETICELIST == event.getType()) {
			if (event.isSuccess()) {
				ArrayList<CIEresponse_params> devDataList = (ArrayList<CIEresponse_params>) event
						.getData();
				updateStatusByList(devDataList);
			}
		}
//		else if (EventType.ON_OFF_STATUS == event.getType()) {
//			if (event.isSuccess() == true) {
//				// data maybe null
//				CallbackResponseType2 data = (CallbackResponseType2) event
//						.getData();
//				List<DevicesModel> mList;
//				DataHelper mDh = new DataHelper((Context) getActivity());
//				String where = " ieee=? and ep=? ";
//				String[] args = {
//						mDevices.getmIeee() == null ? "" : mDevices.getmIeee()
//								.trim(),
//						mDevices.getmEP() == null ? "" : mDevices.getmEP()
//								.trim() };
//				mList = mDh.queryForList(mDh.getSQLiteDatabase(),
//						DataHelper.DEVICES_TABLE, null, where, args, null,
//						null, null, null);
//				boolean result = false;
//				if (null != data.getValue()) {
//					result = data.getValue().trim().equals("1");
//					status = result;
//					setImagRes(on_off, status);
//				}
//				ProcessUpdate(data, mList);
//			} else {
//				// if failed,prompt a Toast
//				// mError.setVisibility(View.VISIBLE);
//			}
//		}
	}

	private void updateStatusByList(ArrayList<CIEresponse_params> devDataList) {
		for (CIEresponse_params ciEresponse_params : devDataList) {
			//过滤出本页面设备的Ieee
			if (ciEresponse_params.getCie().getIeee()
					.equals(mDevices.getmIeee())) {
				String bypass = ciEresponse_params.getCie()
						.getElserec().getBbypass();
				//异或运算，当bypass的值和status的值不同时，刷新界面和数据库
				if(!stringToBoolean(bypass)^status)
				{
					updateStatusOnUIThread();
					//[TODO]刷新数据库
				}
				break;
			}

		}
	}

	private void updateStatusOnUIThread() {
		mView.post(new Runnable() {
			@Override
			public void run() {
				setImagRes(on_off, status);
			}
		});
	}

	private boolean stringToBoolean(String bypass) {
		boolean b;
		if (bypass.trim().equals("true")) {
			b = true;
		} else {
			b = false;
		}
		return b;
	}

	class operatortype {
		/***
		 * 获取设备类型
		 */
		public static final int GetOnOffSwitchType = 0;
		/***
		 * 获取状态
		 */
		public static final int GetOnOffSwitchActions = 1;
		/***
		 * 当操作类型是2时，para1有以下意义 Param1: switchaction: 0x00: Off 0x01: On 0x02:
		 * Toggle
		 */
		public static final int ChangeOnOffSwitchActions = 2;
	}

}
