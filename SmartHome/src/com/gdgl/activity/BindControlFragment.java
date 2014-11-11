package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyDlg;

import android.annotation.SuppressLint;
import android.app.Dialog;
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

/***
 * 绑定二级菜单
 * @author Trice
 *
 */
public class BindControlFragment extends BaseFragment implements UIListener {

	public static final String DevicesId = "devices_id";
	public static final String BindId = "bind_id";

	private View mView;
	
	ViewGroup no_dev;
	Button mBack;

	backAction backAct;
	LinearLayout bind_list;
	LinearLayout deviceslistLayout;
	ListView bindListView;

	DataHelper dh;

	public Dialog mDialog;

	private String devout_ieee;
	private String devout_ep;
	private String cluster_id;
	
	List<DevicesModel> bindableDevicesLis;
	List<DevicesModel> bindedDevicesList;
	
	CGIManager cgiManager;

	public static final int FINISH_DLG = 1;

	BindAdapter mBindAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		cgiManager = CGIManager.getInstance();
		cgiManager.addObserver(this);
		initData();
	}

	@SuppressLint("NewApi")
	private void initData() {
		// TODO Auto-generated method stub
		dh = new DataHelper((Context) getActivity());

		Bundle b = getArguments();
		if (null != b) {
			devout_ieee=b.getString(DevicesModel.IEEE,"");
			devout_ep=b.getString(DevicesModel.EP,"");
			cluster_id=b.getString(DevicesModel.CLUSTER_ID,"");
		}
		new GetBindableDevicesListTask().execute(1);
		
		SQLiteDatabase db = dh.getSQLiteDatabase();
		String where=" devout_ieee=? and devout_ep=? and cluster=? ";
		String[] args={devout_ieee,devout_ep,cluster_id};
		bindedDevicesList=dh.queryForBindDevicesList(db, DataHelper.BIND_TABLE, where, args);
	}
	
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cgiManager.deleteObserver(this);
	}



	public class GetBindableDevicesListTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			SQLiteDatabase db = dh.getSQLiteDatabase();
			String where = " cluster_id = ? ";
			String[] args={cluster_id+"IN"};
			
			bindableDevicesLis=new ArrayList<DevicesModel>();
			bindableDevicesLis = dh.queryForDevicesList(db,
					DataHelper.DEVICES_TABLE, null, where, args, null, null, null,
					null);
			if (null != bindableDevicesLis && bindableDevicesLis.size() > 0) {
				for (DevicesModel devicesModel : bindableDevicesLis) {
					if (null == devicesModel.getmDefaultDeviceName()
							|| devicesModel.getmDefaultDeviceName().trim().equals("")) {
						devicesModel.setmDefaultDeviceName(DataUtil.getDefaultDevicesName((Context) getActivity(),
								devicesModel.getmModelId(), devicesModel.getmEP()));
					}
				}
			}
			dh.close(db);

			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			initView();
		}
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
		bind_list = (LinearLayout) mView.findViewById(R.id.bind_control_list);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		bind_list.setLayoutParams(mLayoutParams);
		no_dev = (ViewGroup) mView.findViewById(R.id.no_dev);

		deviceslistLayout = (LinearLayout) mView.findViewById(R.id.deviceslist);

		bindListView = (ListView) mView.findViewById(R.id.devices_list);

		if (null == bindableDevicesLis || bindableDevicesLis.size() == 0) {
			no_dev.setVisibility(View.VISIBLE);
			deviceslistLayout.setVisibility(View.GONE);
		} else {
			mBindAdapter = new BindAdapter();
			bindListView.setAdapter(mBindAdapter);
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

	public int getPostionInList(String ieee, String ep) {
		if (null == bindableDevicesLis || null == ieee || null == ep) {
			return -1;
		}
		if (ieee.trim().equals("") || ep.trim().equals("")) {
			return -1;
		}
		for (int i = 0; i < bindableDevicesLis.size(); i++) {
			DevicesModel dm = bindableDevicesLis.get(i);
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
			if (null != bindableDevicesLis && bindableDevicesLis.size() > 0) {
				return bindableDevicesLis.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != bindableDevicesLis && bindableDevicesLis.size() > 0) {
				return bindableDevicesLis.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != bindableDevicesLis && bindableDevicesLis.size() > 0) {
				return bindableDevicesLis.get(position).getID();
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

			mHolder.devices_name.setText(currentPositionDevice.getmDefaultDeviceName().replace(
					" ", ""));
			
			
			mHolder.bindBtn.setText("绑定");
			mHolder.devices_state.setText("未绑定");
			for(DevicesModel devicesModel:bindedDevicesList) {
				if(currentPositionDevice.getmIeee().equals(devicesModel.getmIeee())&&
						currentPositionDevice.getmEP().equals(devicesModel.getmEP())) {
					mHolder.bindBtn.setText("解除绑定");
					mHolder.devices_state.setText("已绑定");
					break;
				}
			}
			

			int devId = Integer.parseInt(currentPositionDevice.getmDeviceId());
			mHolder.devices_img.setImageResource(DataUtil
					.getDefaultDevicesSmallIcon(devId,currentPositionDevice.getmModelId().trim()));
//			if (DataHelper.IAS_ZONE_DEVICETYPE == devId
//					|| DataHelper.IAS_ACE_DEVICETYPE == devId) {
//
//				mHolder.devices_img.setImageResource(UiUtils
//						.getDevicesSmallIconByModelId(currentPositionDevice.getmModelId()
//								.trim()));
//			} else if (currentPositionDevice.getmModelId().indexOf(
//					DataHelper.Multi_key_remote_control) == 0) {
//				mHolder.devices_img.setImageResource(UiUtils
//						.getDevicesSmallIconForRemote(devId));
//			} else {
//				mHolder.devices_img.setImageResource(UiUtils
//						.getDevicesSmallIcon(devId));
//			}
			
			mHolder.bindBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Button btn = (Button) v;
					String s = btn.getText().toString();
					
					if (s.equals("解除绑定")) {
						// 解除绑定
						cgiManager.delBindData("0",devout_ieee,devout_ep, currentPositionDevice,cluster_id);
					} else {
						// 绑定
						cgiManager.addBindData("0",devout_ieee,devout_ep, currentPositionDevice,cluster_id);
					}
					
					if (null == mDialog) {
						mDialog = MyDlg.createLoadingDialog(
								(Context) getActivity(), "操作正在进行...");
						mDialog.show();
					} else {
						mDialog.show();
					}
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

		final Event event = (Event) object;
		if (EventType.GETBINDLIST == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				mHandler.sendEmptyMessage(FINISH_DLG);
				SQLiteDatabase db = dh.getSQLiteDatabase();
				String where=" devout_ieee=? and devout_ep=? and cluster=? ";
				String[] args={devout_ieee,devout_ep,cluster_id};
				bindedDevicesList=dh.queryForBindDevicesList(db, DataHelper.BIND_TABLE, where, args);
				mBindAdapter.notifyDataSetChanged();
			} else {
				// if failed,prompt a Toast

			}
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
