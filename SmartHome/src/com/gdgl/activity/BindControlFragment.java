package com.gdgl.activity;

import java.util.List;

import com.gdgl.manager.LightManager;
import com.gdgl.manager.Manger;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyDlg;
import com.gdgl.util.UiUtils;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
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
	List<DevicesModel> mList;

	ViewGroup no_dev;
	Button mBack;
	
	updateList mUpList;
	
	LinearLayout ch_pwd;
	LinearLayout deviceslist;
	ListView bindList;

	DataHelper dh;

	public Dialog mDialog;

	private int devId = -1, bindId = -1;
	private int willBindId;
	private DevicesModel mdevices, mBindDev;

	LightManager mLightManager;
	private int mPostion;
	private int mBindPostion = -1;

	SimpleDevicesModel minModel;
	public static final int FINISH_DLG = 1;
	
	BindAdapter mBindAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		dh = new DataHelper((Context) getActivity());
		mList = DataUtil.getCanbeBindDevices((Context) getActivity(), dh);
		Bundle b = getArguments();
		if (null != b) {
			devId = b.getInt(DevicesId, -1);
			bindId = b.getInt(BindId, -1);
		}
		if (devId != -1) {
			mdevices = DataUtil.getDeviceModelById(devId, dh);
		}
		if (bindId != -1) {
			mBindDev = DataUtil.getDeviceModelById(bindId, dh);
		}

		mLightManager = LightManager.getInstance();
		mLightManager.addObserver(this);

		minModel = new SimpleDevicesModel();
		minModel.setmIeee(mdevices.getmIeee());
		minModel.setmEP(mdevices.getmEP());

		if (null != mList && mList.size() > 0) {
			if (null != mBindDev) {
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getmIeee().trim()
							.equals(mBindDev.getmIeee().trim())
							&& mList.get(i).getmEP().trim()
									.equals(mBindDev.getmEP().trim())) {
						mBindPostion=i;

					}
				}
			}
		}
	}
	
	public void setUplist(updateList u){
		mUpList=u;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.bind_control, null);
		initView();
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

		if (null == mList || mList.size() == 0) {
			no_dev.setVisibility(View.VISIBLE);
			deviceslist.setVisibility(View.GONE);
		} else {
			mBindAdapter=new BindAdapter();
			bindList.setAdapter(mBindAdapter);
		}
		
		mBack = (Button) mView.findViewById(R.id.back);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getActivity().getFragmentManager();
				fm.popBackStack();
			}
		});

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
			if (null != mList && mList.size() > 0) {
				return mList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mList && mList.size() > 0) {
				return mList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mList && mList.size() > 0) {
				return mList.get(position).getID();
			}
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			View mView = convertView;
			ViewHolder mHolder;
			final DevicesModel mDevices = (DevicesModel) getItem(position);

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

			mHolder.devices_name.setText(mDevices.getmUserDefineName().replace(
					" ", ""));
			DevicesModel binDev;
			if (null != mDevices.getmBindTo()
					&& !mDevices.getmBindTo().trim().equals("")) {
				binDev = DataUtil.getDeviceModelById(
						Integer.parseInt(mDevices.getmBindTo().trim()), dh);
				mHolder.devices_state.setText("已绑定至"
						+ binDev.getmUserDefineName());
			} else {
				mHolder.devices_state.setText("未绑定到任何设备");
			}

			if (bindId == mDevices.getID()) {
				mHolder.bindBtn.setText("解除绑定");
			}

			int devModeleId = Integer.parseInt(mDevices.getmDeviceId());

			if (DataHelper.IAS_ZONE_DEVICETYPE == devModeleId
					|| DataHelper.IAS_ACE_DEVICETYPE == devModeleId) {

				mHolder.devices_img.setImageResource(UiUtils
						.getDevicesSmallIconByModelId(mDevices.getmModelId()
								.trim()));
			} else if (mDevices.getmModelId().indexOf(
					DataHelper.Multi_key_remote_control) == 0) {
				mHolder.devices_img.setImageResource(UiUtils
						.getDevicesSmallIconForRemote(devModeleId));
			} else {
				mHolder.devices_img.setImageResource(UiUtils
						.getDevicesSmallIcon(devModeleId));
			}
			final SimpleDevicesModel moutModel = new SimpleDevicesModel();
			moutModel.setmIeee(mDevices.getmIeee());
			moutModel.setmEP(mDevices.getmEP());
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

					if (s.equals("解除绑定")) {
						// 解除绑定
						mLightManager.unbindDevice(moutModel, minModel);
					} else {
						// 绑定
						mLightManager.bindDevice(moutModel, minModel);
					}
					willBindId = mDevices.getID();
					btn.setEnabled(false);
					mPostion = position;
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

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		// EventType.BINDDEVICE
		if (null != mDialog) {
			mDialog.dismiss();
			mDialog = null;
		}
		final Event event = (Event) object;
		if (EventType.BINDDEVICE == event.getType()) {

			if (event.isSuccess() == true) {
				// data maybe null
				String where = " ieee=? and ep=? ";
				String[] args = { mdevices.getmIeee(), mdevices.getmEP() };
				ContentValues values = new ContentValues();
				values.put(DevicesModel.BIND_TO, willBindId + "");
				dh.update(dh.getSQLiteDatabase(), DataHelper.DEVICES_TABLE,
						values, where, args);

				DevicesModel dm = DataUtil.getDeviceModelById(willBindId, dh);

				String wheres = " ieee=? and ep=? ";
				String[] argss = { dm.getmIeee(), dm.getmEP() };
				ContentValues v = new ContentValues();
				values.put(DevicesModel.BIND_TO, devId + "");
				dh.update(dh.getSQLiteDatabase(), DataHelper.DEVICES_TABLE, v,
						wheres, argss);

				if (null != mBindDev) {
					String w = " ieee=? and ep=? ";
					String[] a = { mBindDev.getmIeee(), mBindDev.getmEP() };
					ContentValues va = new ContentValues();
					values.put(DevicesModel.BIND_TO, "");
					dh.update(dh.getSQLiteDatabase(), DataHelper.DEVICES_TABLE,
							va, w, a);
				}

				mBindDev.setmBindTo("");
				
				mList.get(mPostion).setmBindTo(BindId + "");
				if(mBindPostion!=-1){
					mList.get(mBindPostion).setmBindTo("");
					mBindPostion=mPostion;
				}
				if(null!=mBindAdapter){
					mBindAdapter.notifyDataSetChanged();
				}
				
				mUpList.upList(mdevices, Integer.parseInt(BindId));
				
			} else {
				// if failed,prompt a Toast

			}
		}
	}
	
	public interface updateList{
		public void upList(DevicesModel dm,int id);
	}
	
	@Override
	public void stopRefresh() {
		// TODO Auto-generated method stub

	}

}
