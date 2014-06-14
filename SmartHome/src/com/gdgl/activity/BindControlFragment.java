package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.manager.BindManager;
import com.gdgl.manager.LightManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.binding.BindingDataEntity;
import com.gdgl.mydata.binding.BindingDivice;
import com.gdgl.mydata.binding.Binding_response_params;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyDlg;
import com.gdgl.util.UiUtils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class BindControlFragment extends BaseFragment {

	public static final String DevicesId = "devices_id";
	public static final String BindId = "bind_id";

	private View mView;
	List<DevicesModel> canbeBindDevicesList;

	ViewGroup no_dev;
	Button mBack;

	updateList mUpList;
	backAction backAct;
	LinearLayout ch_pwd;
	LinearLayout deviceslist;
	ListView bindList;

	DataHelper dh;

	public Dialog mDialog;

	private int devId = -1;
	private DevicesModel willBindDevices;
	private DevicesModel mdevices;
	private List<DevicesModel> mBindDev;

	LightManager mLightManager;
	private int mPostion;

//	SimpleDevicesModel minModel;
	public static final int FINISH_DLG = 1;

	BindAdapter mBindAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
	}

	@SuppressLint("NewApi")
	private void initData() {
		// TODO Auto-generated method stub
		dh = new DataHelper((Context) getActivity());

		Bundle b = getArguments();
		if (null != b) {
			devId = b.getInt(DevicesId, -1);
		}
		mBindDev = new ArrayList<DevicesModel>();
		new getDataTask().execute(1);
		mLightManager = LightManager.getInstance();
		mLightManager.addObserver(this);

	}
	
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLightManager.deleteObserver(this);
	}



	public class getDataTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			SQLiteDatabase db = dh.getSQLiteDatabase();
			if (devId != -1) {
				mdevices = DataUtil.getDeviceModelById(devId, dh, db);
			}
//			SimpleDevicesModel mSimpleDevicesModel = new SimpleDevicesModel();
//			mSimpleDevicesModel.setmIeee(mdevices.getmIeee());
//			mSimpleDevicesModel.setmEP(mdevices.getmEP());
//			mLightManager.getBindList(mSimpleDevicesModel);

//			if (!bindId.trim().equals("")) {
//				String[] ids = bindId.trim().split("##");
//				DevicesModel dm;
//				for (String string : ids) {
//					if (!string.trim().equals("")) {
//						dm = DataUtil.getDeviceModelById(
//								Integer.parseInt(string), dh, db);
//						if (null != dm) {
//							mBindDev.add(dm);
//						}
//					}
//				}
//			}
			canbeBindDevicesList = DataUtil.getCanbeBindDevices((Context) getActivity(), dh,
					db, mdevices.getmClusterID());
			dh.close(db);

			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
//			minModel = new SimpleDevicesModel();
//			minModel.setmIeee(mdevices.getmIeee());
//			minModel.setmEP(mdevices.getmEP());
//			minModel.setmClusterID(mdevices.getmClusterID());
			initView();
		}
	}

	public void setUplist(updateList u) {
		mUpList = u;
	}

	public void setBackAction(backAction b) {
		backAct = b;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.bind_control, null);
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		ch_pwd = (LinearLayout) mView.findViewById(R.id.ch_pwd);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		ch_pwd.setLayoutParams(mLayoutParams);
		no_dev = (ViewGroup) mView.findViewById(R.id.no_dev);

		deviceslist = (LinearLayout) mView.findViewById(R.id.deviceslist);

		bindList = (ListView) mView.findViewById(R.id.devices_list);

		if (null == canbeBindDevicesList || canbeBindDevicesList.size() == 0) {
			no_dev.setVisibility(View.VISIBLE);
			deviceslist.setVisibility(View.GONE);
		} else {
			mBindAdapter = new BindAdapter();
			bindList.setAdapter(mBindAdapter);
		}

		mBack = (Button) mView.findViewById(R.id.back);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// FragmentManager fm = getActivity().getFragmentManager();
				// fm.popBackStack();
				backAct.back();
			}
		});

	}

	public interface backAction {
		public void back();
	}

	public class updateDevTask extends
			AsyncTask<BindingDataEntity, Integer, Integer> {
		List<DevicesModel> mNewBindList;
		String newBind = "";

		@Override
		protected Integer doInBackground(BindingDataEntity... params) {
			// TODO Auto-generated method stub
			BindingDataEntity data = params[0];
			if (null == data) {
				return -1;
			}
			List<DevicesModel> mDevList = new ArrayList<DevicesModel>();
			List<DevicesModel> tempList;
			Binding_response_params brp = data.getResponse_params();
			ArrayList<BindingDivice> ablist = brp.getList();
			String where = " ieee=? and ep=? ";
			String[] args;

			if (null != ablist && ablist.size() > 0) {
				SQLiteDatabase db = dh.getSQLiteDatabase();
				for (BindingDivice bindingDivice : ablist) {
					args = new String[2];
					args[0] = bindingDivice.getIeee();
					args[1] = bindingDivice.getEp();
					tempList = dh.queryForList(db, DataHelper.DEVICES_TABLE,
							null, where, args, null, null, null, null);
					if (null != tempList && tempList.size() > 0) {
						mDevList.add(tempList.get(0));
					}
				}
				dh.close(db);
			}

			String newBindId = "";

			if (null != mDevList && mDevList.size() > 0) {
				for (DevicesModel dm : mDevList) {
					if (null != dm) {
						newBindId += dm.getID() + "##";
					}
				}
			}

			String[] argss = { brp.getIeee(), brp.getEp() };
			ContentValues values = new ContentValues();
			values.put(DevicesModel.BIND_TO, newBindId);
			SQLiteDatabase db = dh.getSQLiteDatabase();
			dh.update(db, DataHelper.DEVICES_TABLE, values, where, argss);

			dh.close(db);

			mdevices.setmBindTo(newBindId);
			mNewBindList = mDevList;
			newBind = newBindId;
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub

			mUpList.upList(mdevices, newBind);

			if (null != mNewBindList && mNewBindList.size() > 0) {
				for (DevicesModel dm : mNewBindList) {
					int m = getPostionInList(dm.getmIeee(), dm.getmEP());
					if (-1 != m) {
						String ids = canbeBindDevicesList.get(m).getmBindTo();
						String newIds = "";
						String[] idArrary;
						if (null != ids && !ids.trim().equals("")) {
							idArrary = ids.trim().split("##");
							for (String string : idArrary) {
								if (!string.trim().equals("")) {
									newIds += string + "##";
								}
							}
							newIds += mdevices.getID() + "##";
						} else {
							newIds = mdevices.getID() + "##";
						}
						canbeBindDevicesList.get(m).setmBindTo(newIds);
					}
				}
				mBindAdapter.notifyDataSetChanged();
			}
		}
	}

	public int getPostionInList(String ieee, String ep) {
		if (null == canbeBindDevicesList || null == ieee || null == ep) {
			return -1;
		}
		if (ieee.trim().equals("") || ep.trim().equals("")) {
			return -1;
		}
		for (int i = 0; i < canbeBindDevicesList.size(); i++) {
			DevicesModel dm = canbeBindDevicesList.get(i);
			if (ieee.trim().equals(dm.getmIeee().trim())
					&& ep.trim().equals(dm.getmEP().trim())) {
				return i;
			}
		}
		return -1;
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			switch (what) {
			case FINISH_DLG:
				if (null != mDialog) {
					mDialog.dismiss();
					mDialog = null;
				}
				break;

			default:
				break;
			}
		};
	};

	public class BindAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != canbeBindDevicesList && canbeBindDevicesList.size() > 0) {
				return canbeBindDevicesList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != canbeBindDevicesList && canbeBindDevicesList.size() > 0) {
				return canbeBindDevicesList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != canbeBindDevicesList && canbeBindDevicesList.size() > 0) {
				return canbeBindDevicesList.get(position).getID();
			}
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			View mView = convertView;
			ViewHolder mHolder;
			final DevicesModel currentPositionDevice = (DevicesModel) getItem(position);

			if (null == mView) {
				mHolder = new ViewHolder();
				mView = LayoutInflater.from((Context) getActivity()).inflate(
						R.layout.bind_control_item, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mHolder.devices_state = (TextView) mView
						.findViewById(R.id.devices_state);
				mHolder.bindBtn = (Button) mView.findViewById(R.id.btn_bind);
				mView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) mView.getTag();
			}

			mHolder.devices_name.setText(currentPositionDevice.getmUserDefineName().replace(
					" ", ""));
			
			
			if (BindManager.getInstance().isBindtoBind(mdevices,currentPositionDevice)) {
				mHolder.bindBtn.setText("解除绑定");
				mHolder.devices_state.setText("已绑定");
			} else {
				mHolder.bindBtn.setText("绑定");
				mHolder.devices_state.setText("未绑定");
			}
			
//			String bindto = currentPositionDevice.getmBindTo();
//			String[] ids;
//			if (null != bindto && !bindto.trim().equals("")) {
//				ids = bindto.trim().split("##");
//				for (String string : ids) {
//					if (null != string && string.trim().equals(devId + "")) {
//						mHolder.bindBtn.setText("解除绑定");
//						mHolder.devices_state.setText("已绑定");
//					} else {
//						mHolder.bindBtn.setText("绑定");
//						mHolder.devices_state.setText("未绑定");
//					}
//				}
//			} else {
//				mHolder.devices_state.setText("未绑定");
//				mHolder.bindBtn.setText("绑定");
//			}
			int devModeleId = Integer.parseInt(currentPositionDevice.getmDeviceId());

			if (DataHelper.IAS_ZONE_DEVICETYPE == devModeleId
					|| DataHelper.IAS_ACE_DEVICETYPE == devModeleId) {

				mHolder.devices_img.setImageResource(UiUtils
						.getDevicesSmallIconByModelId(currentPositionDevice.getmModelId()
								.trim()));
			} else if (currentPositionDevice.getmModelId().indexOf(
					DataHelper.Multi_key_remote_control) == 0) {
				mHolder.devices_img.setImageResource(UiUtils
						.getDevicesSmallIconForRemote(devModeleId));
			} else {
				mHolder.devices_img.setImageResource(UiUtils
						.getDevicesSmallIcon(devModeleId));
			}
			final SimpleDevicesModel moutModel = new SimpleDevicesModel();
			moutModel.setmIeee(currentPositionDevice.getmIeee());
			moutModel.setmEP(currentPositionDevice.getmEP());
			moutModel.setmClusterID(currentPositionDevice.getmClusterID());
			mHolder.bindBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Button btn = (Button) v;
					String s = btn.getText().toString();

					if (null == mDialog) {
						mDialog = MyDlg.createLoadingDialog(
								(Context) getActivity(), "操作正在进行...");
						mDialog.show();
					} else {
						mDialog.show();
					}
					willBindDevices = currentPositionDevice;
					// btn.setEnabled(false);
					mPostion = position;
					if (s.equals("解除绑定")) {
						// 解除绑定
						mLightManager.unbindDevice(moutModel, mdevices);
						// unBinddev();
					} else {
						// 绑定
						mLightManager.bindDevice(moutModel, mdevices);
						// bindDev();
					}
					mHandler.sendEmptyMessageDelayed(FINISH_DLG, 3000);
				}
			});

			return mView;
		}

		public class ViewHolder {
			ImageView devices_img;
			TextView devices_name;
			TextView devices_state;
			Button bindBtn;
		}

	}

	public void bindDev() {
		String where = " ieee=? and ep=? ";
		String[] args = { mdevices.getmIeee(), mdevices.getmEP() };

		String oldId = mdevices.getmBindTo();
		String newids = "";
		if (null == oldId || oldId.trim().equals("")) {
			newids = willBindDevices.getID() + "##";
		} else {
			String[] ids = oldId.split("##");
			for (String string : ids) {
				if (!string.equals("")) {
					newids += string + "##";
				}
			}
			newids += willBindDevices.getID() + "##";
		}

		ContentValues values = new ContentValues();
		values.put(DevicesModel.BIND_TO, newids);
		SQLiteDatabase db = dh.getSQLiteDatabase();
		dh.update(db, DataHelper.DEVICES_TABLE, values, where, args);
		String temp = newids;
		String wheres = " ieee=? and ep=? ";
		String[] argss = { willBindDevices.getmIeee(), willBindDevices.getmEP() };

		oldId = willBindDevices.getmBindTo();
		newids = "";
		if (null == oldId || oldId.trim().equals("")) {
			newids = mdevices.getID() + "##";
		} else {

			String[] ids = oldId.split("##");
			for (String string : ids) {
				if (!string.equals("")) {
					newids += string + "##";
				}
			}
			newids += mdevices.getID() + "##";
		}
		ContentValues v = new ContentValues();
		v.put(DevicesModel.BIND_TO, newids);
		dh.update(db, DataHelper.DEVICES_TABLE, v, wheres, argss);

		dh.close(db);

		canbeBindDevicesList.get(mPostion).setmBindTo(newids);
		if (null != mBindAdapter) {
			mBindAdapter.notifyDataSetChanged();
		}

		mUpList.upList(mdevices, temp);
	}

	public void unBinddev() {
		String where = " ieee=? and ep=? ";
		String[] args = { mdevices.getmIeee(), mdevices.getmEP() };

		String unbindId = willBindDevices.getID() + "";
		String oldId = mdevices.getmBindTo();
		String[] oldAr = oldId.trim().split("##");
		String newId = "";
		for (String string : oldAr) {
			if (!string.trim().equals("")) {
				if (!string.trim().equals(unbindId)) {
					newId += string.trim() + "##";
				}
			}
		}
		ContentValues values = new ContentValues();
		values.put(DevicesModel.BIND_TO, newId);
		SQLiteDatabase db = dh.getSQLiteDatabase();
		dh.update(db, DataHelper.DEVICES_TABLE, values, where, args);

		String temp = newId;

		String wheres = " ieee=? and ep=? ";
		String[] argss = { willBindDevices.getmIeee(), willBindDevices.getmEP() };

		unbindId = mdevices.getID() + "";
		oldId = willBindDevices.getmBindTo();
		String[] oldUnAr = oldId.trim().split("##");
		String newunBindId = "";
		for (String string : oldUnAr) {
			if (!string.trim().equals("")) {
				if (!string.trim().equals(unbindId)) {
					newunBindId += string.trim() + "##";
				}
			}
		}
		ContentValues v = new ContentValues();
		v.put(DevicesModel.BIND_TO, newunBindId);
		dh.update(db, DataHelper.DEVICES_TABLE, v, wheres, argss);

		dh.close(db);

		canbeBindDevicesList.get(mPostion).setmBindTo(newunBindId);
		if (null != mBindAdapter) {
			mBindAdapter.notifyDataSetChanged();
		}

		mUpList.upList(mdevices, newId);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		// EventType.BINDDEVICE

		final Event event = (Event) object;
		if (EventType.BINDDEVICE == event.getType()) {

			if (event.isSuccess() == true) {
//				bindDev();
				BindManager.getInstance().setBinded(mdevices, willBindDevices);
				mBindAdapter.notifyDataSetChanged();

			} else {
				// if failed,prompt a Toast

			}
		} else if (EventType.UNBINDDEVICE == event.getType()) {

			if (event.isSuccess() == true) {
//				unBinddev();
				BindManager.getInstance().removeBinded(mdevices, willBindDevices);
				mBindAdapter.notifyDataSetChanged();

			} else {
				// if failed,prompt a Toast

			}
		} else if (EventType.GETBINDLIST == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				BindingDataEntity data = (BindingDataEntity) event.getData();
				new updateDevTask().execute(data);

			} else {
				// if failed,prompt a Toast

			}
		}
		if (null != mDialog) {
			mDialog.dismiss();
			mDialog = null;
		}
	}


	public interface updateList {
		public void upList(DevicesModel dm, String id);
	}

	@Override
	public void stopRefresh() {
		// TODO Auto-generated method stub

	}

}
