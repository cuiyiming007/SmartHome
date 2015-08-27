package com.gdgl.drawer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.gc.materialdesign.views.ButtonFloat;
import com.gdgl.libjingle.LibjingleResponseHandlerManager;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.RfCGIManager;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOKOnlyDlg;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.GridView;

public class TestFragment extends Fragment implements UIListener {

	GridView content_view;
	List<DevicesModel> mDeviceList;
	View mView;
	ViewGroup nodevices;
	ButtonFloat mButtonFloat;

	CustomeAdapter mCustomeAdapter;
	DataHelper mDh;

	CGIManager cgiManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub

		cgiManager = CGIManager.getInstance();
		cgiManager.addObserver(this);
		DeviceManager.getInstance().addObserver(this);
		CallbackManager.getInstance().addObserver(this);
		RfCGIManager.getInstance().addObserver(this);
		LibjingleResponseHandlerManager.getInstance().addObserver(this);

		mDh = new DataHelper((Context) getActivity());
		try {
			new getDataInBackgroundTask().execute(1).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.devices_main_fragment, null);
		initview();
		return mView;
	}

	private void initview() {
		// TODO Auto-generated method stub

		nodevices = (ViewGroup) mView.findViewById(R.id.nodevices);
		nodevices.setVisibility(View.GONE);
		content_view = (GridView) mView.findViewById(R.id.content_view);
		mButtonFloat = (ButtonFloat) mView.findViewById(R.id.buttonFloat);
		mButtonFloat.setVisibility(View.GONE); // 广电服务器控制，不允许设备入网。
		// content_view.setBackgroundResource(R.color.blue_default);
		// content_view.setLayoutAnimation(UiUtils
		// .getAnimationController((Context) getActivity()));
		mCustomeAdapter = new CustomeAdapter();
		mCustomeAdapter.setList(mDeviceList);
		content_view.setAdapter(mCustomeAdapter);
		// content_view.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // TODO Auto-generated method stub
		// DevicesModel mDevicesModel = (DevicesModel) mDeviceList
		// .get(position);
		// Intent intent = new Intent();
		//
		// Bundle extras = new Bundle();
		// extras.putSerializable(Constants.PASS_OBJECT, mDevicesModel);
		// intent.putExtras(extras);
		// intent.setClass(getActivity(), DeviceControlActivity.class);
		// startActivity(intent);
		// }
		// });

		mButtonFloat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					Intent i = new Intent();
					i.setClass(getActivity(), JoinNetActivity.class);
					startActivity(i);
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(getActivity());
					myOKOnlyDlg.setContent(getResources().getString(
							R.string.Unable_In_InternetState));
					myOKOnlyDlg.show();
				}
			}
		});
	}

	private class CustomeAdapter extends BaseAdapter {
		private List<DevicesModel> mAdapterDevicesList;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mAdapterDevicesList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mAdapterDevicesList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (null == mAdapterDevicesList) {
				return convertView;
			}
			final DevicesModel mAdapeterDevicesModel = mAdapterDevicesList
					.get(position);
			// TODO Auto-generated method stub
			final ViewHolder mViewHolder;
			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from((Context) getActivity())
						.inflate(R.layout.gridview_card_item, null);
				mViewHolder.funcImg = (ImageView) convertView
						.findViewById(R.id.func_img);
				mViewHolder.funcText = (TextView) convertView
						.findViewById(R.id.func_name);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}

			if (mAdapeterDevicesModel.getmModelId().indexOf(
					DataHelper.One_key_operator) == 0
					&& DeviceControlActivity.GATEWAYUPDATE == true) {
				mViewHolder.funcImg
						.setImageResource(R.drawable.ui2_device_gateway_update);
			} else {
				mViewHolder.funcImg.setImageResource(DataUtil
						.getDefaultDevicesIcon(
								mAdapeterDevicesModel.getmDeviceId(),
								mAdapeterDevicesModel.getmModelId().trim()));
			}
			mViewHolder.funcImg.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {

					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						mViewHolder.funcImg.setColorFilter(Color
								.parseColor("#55888888"));
						return true;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_MOVE:
						mViewHolder.funcImg.clearColorFilter();
						return true;
					case MotionEvent.ACTION_UP:
						mViewHolder.funcImg.clearColorFilter();
						Intent intent = new Intent();
						Bundle extras = new Bundle();
						extras.putSerializable(Constants.PASS_OBJECT,
								mAdapeterDevicesModel);
						intent.putExtras(extras);
						intent.setClass(getActivity(),
								DeviceControlActivity.class);
						startActivity(intent);
						return true;
					}
					return true;
				}
			});
			mViewHolder.funcText.setText(mAdapeterDevicesModel
					.getmDefaultDeviceName());
			return convertView;
		}

		class ViewHolder {
			ImageView funcImg;
			TextView funcText;
		}

		public void setList(List<DevicesModel> s) {
			mAdapterDevicesList = null;
			mAdapterDevicesList = s;
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cgiManager.deleteObserver(this);
		DeviceManager.getInstance().deleteObserver(this);
		CallbackManager.getInstance().deleteObserver(this);
		RfCGIManager.getInstance().deleteObserver(this);
		LibjingleResponseHandlerManager.getInstance().deleteObserver(this);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		if (EventType.LIGHTSENSOROPERATION == event.getType()) {
			// data maybe null
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				int m = getDevicesPostion(bundle.getString("IEEE"),
						bundle.getString("EP"), mDeviceList);
				if (m != -1) {
					String light = bundle.getString("PARAM");
					mDeviceList.get(m).setmBrightness(Integer.parseInt(light));
				}
			} else {
			}
		} else if (EventType.TEMPERATURESENSOROPERATION == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				int m = getDevicesPostion(bundle.getString("IEEE"),
						bundle.getString("EP"), mDeviceList);
				if (m != -1) {
					String temperature = bundle.getString("PARAM");
					mDeviceList.get(m).setmTemperature(
							Float.parseFloat(temperature));
				}
			}
		} else if (EventType.HUMIDITY == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				int m = getDevicesPostion(bundle.getString("IEEE"),
						bundle.getString("EP"), mDeviceList);
				if (m != -1) {
					String humidity = bundle.getString("PARAM");
					mDeviceList.get(m).setmHumidity(Float.parseFloat(humidity));
				}
			}
		} else if (EventType.LOCALIASCIEBYPASSZONE == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				int m = getDevicesPostion(bundle.getString("IEEE"),
						bundle.getString("EP"), mDeviceList);
				if (m != -1) {
					mDeviceList.get(m).setmOnOffStatus(
							bundle.getString("PARAM"));
				}
			}
		} else if (EventType.RF_DEVICE_BYPASS == event.getType()) {
			if (event.isSuccess()) {
				Bundle bundle = (Bundle) event.getData();
				int m = getDevicesPostion(bundle.getString("IEEE"), "01",
						mDeviceList);
				if (m != -1) {
					mDeviceList.get(m).setmOnOffStatus(
							bundle.getString("PARAM"));
				}
			}
		} else if (EventType.RF_DEVICE_ALL_BYPASS == event.getType()) {
			if (event.isSuccess()) {
				int status = (Integer) event.getData();
				for (int i = 0; i < mDeviceList.size(); i++) {
					if (mDeviceList.get(i).getmModelId()
							.equals(DataHelper.RF_Magnetic_Door)
							|| mDeviceList.get(i).getmModelId()
									.equals(DataHelper.RF_Magnetic_Door_Roll)
							|| mDeviceList
									.get(i)
									.getmModelId()
									.equals(DataHelper.RF_Infrared_Motion_Sensor)) {
						mDeviceList.get(i).setmOnOffStatus(
								String.valueOf(status));
					}
				}
			}
		} else if (EventType.LOCALIASCIEOPERATION == event.getType()) {
			if (event.isSuccess() == true) {

				String status = (String) event.getData();
				if (null != mDeviceList && mDeviceList.size() > 0) {
					for (int m = 0; m < mDeviceList.size(); m++) {
						if (mDeviceList.get(m).getmModelId()
								.indexOf(DataHelper.One_key_operator) == 0) {
							mDeviceList.get(m).setmOnOffStatus(status);
							break;
						}
					}
				}
			}
		} else if (EventType.ON_OFF_STATUS == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mDeviceList);
				if (-1 != m) {
					if (null != data.getValue()) {
						mDeviceList.get(m).setmOnOffStatus(data.getValue());
					}
				}
			}
		} else if (EventType.MOVE_TO_LEVEL == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mDeviceList);
				String valueString = data.getValue();
				if (-1 != m) {
					if (null != data.getValue()) {
						mDeviceList.get(m).setmLevel(valueString);
						if (Integer.parseInt(valueString) < 7) {
							mDeviceList.get(m).setmOnOffStatus("0");
						} else {
							mDeviceList.get(m).setmOnOffStatus("1");
						}
					}
				}
			}
		} else if (EventType.CURRENT == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mDeviceList);
				String valueString = data.getValue();
				if (-1 != m) {
					if (null != data.getValue()) {
						mDeviceList.get(m).setmCurrent(valueString);
					}
				}
			}
		} else if (EventType.VOLTAGE == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mDeviceList);
				if (-1 != m) {
					if (null != data.getValue()) {
						mDeviceList.get(m).setmVoltage(data.getValue());
					}
				}
			}
		} else if (EventType.ENERGY == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mDeviceList);
				if (-1 != m) {
					if (null != data.getValue()) {
						mDeviceList.get(m).setmEnergy(data.getValue());
					}
				}
			}
		} else if (EventType.POWER == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m = getDevicesPostion(data.getDeviceIeee(),
						data.getDeviceEp(), mDeviceList);
				if (-1 != m) {
					if (null != data.getValue()) {
						mDeviceList.get(m).setmPower(data.getValue());
					}
				}
			}
		} else if (EventType.DELETENODE == event.getType()) {
			if (event.isSuccess()) {
				String delete_ieee = (String) event.getData();
				for (int i = 0; i < mDeviceList.size(); i++) {
					DevicesModel tempDevModel = mDeviceList.get(i);
					if (tempDevModel.getmIeee().equals(delete_ieee)) {
						mDeviceList.remove(i);
						mView.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								mCustomeAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			}
		} else if (EventType.CHANGEDEVICENAME == event.getType()) {
			if (event.isSuccess()) {
				String[] changeName = (String[]) event.getData();
				String ieee = changeName[0];
				String ep = changeName[1];
				String name = changeName[2];
				for (int i = 0; i < mDeviceList.size(); i++) {
					DevicesModel tempDevModel = mDeviceList.get(i);
					if (tempDevModel.getmIeee().equals(ieee)
							&& tempDevModel.getmEP().equals(ep)) {
						mDeviceList.get(i).setmDefaultDeviceName(name);
						mView.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								mCustomeAdapter.notifyDataSetChanged();
							}
						});
						break;
					}
				}
			}
		} else if (EventType.SCAPEDDEVICE == event.getType()) {
			ArrayList<DevicesModel> scapedList = (ArrayList<DevicesModel>) event
					.getData();
			for (DevicesModel mDevicesModel : scapedList) {
				mDeviceList.add(mDevicesModel);
			}
			mView.post(new Runnable() {

				@Override
				public void run() {
					mCustomeAdapter.notifyDataSetChanged();
				}
			});
		} else if (EventType.GATEWAYUPDATECOMPLETE == event.getType()) {
			if (event.isSuccess()) {
				mView.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mCustomeAdapter.notifyDataSetChanged();
					}
				}, 500);
			}
		} else if (EventType.RF_DEVICE_LIST_UPDATE == event.getType()) {
			if (event.isSuccess()) {
				new getDataInBackgroundTask().execute(1);
				mView.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.i("RF_DEVICE_LIST_UPDATE",
								"=====================>Runnable begin");
						mCustomeAdapter.setList(mDeviceList);
						mCustomeAdapter.notifyDataSetChanged();
					}
				}, 500);
			}
		}

	}

	private int getDevicesPostion(String ieee, String ep,
			List<DevicesModel> deviceList) {
		if (null == ieee || null == ep) {
			return -1;
		}
		if (ieee.trim().equals("") || ep.trim().equals("")) {
			return -1;
		}
		if (null != deviceList && deviceList.size() > 0) {
			for (int m = 0; m < deviceList.size(); m++) {
				DevicesModel dm = deviceList.get(m);
				if (ieee.trim().equals(dm.getmIeee().trim())
						&& ep.trim().equals(dm.getmEP().trim())) {
					return m;
				}
			}
		}
		return -1;

	}

	public class getDataInBackgroundTask extends
			AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			SQLiteDatabase dataBase = mDh.getSQLiteDatabase();
			mDeviceList = mDh.queryForDevicesList(dataBase,
					DataHelper.DEVICES_TABLE, null, null, null, null, null,
					DevicesModel.DEVICE_PRIORITY, null);
			mDeviceList.addAll(mDh.queryForDevicesList(dataBase,
					DataHelper.RF_DEVICES_TABLE, null, null, null, null, null,
					DevicesModel.DEVICE_PRIORITY, null));
			dataBase.close();
			Log.i("RF_DEVICE_LIST_UPDATE", "=====================>Task done");
			return 1;
		}
	}
}
