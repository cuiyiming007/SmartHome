package com.gdgl.activity;

import java.util.List;

import com.gdgl.activity.BindControlFragment.backAction;
import com.gdgl.activity.JoinNetFragment.ChangeFragment;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 绑定菜单
 * @author Trice
 *
 */
public class BindListFragment extends Fragment implements UIListener {

	private View mView;
	List<DevicesModel> mList;

	ViewGroup no_dev;
	Button mBack;

	// LinearLayout ch_pwd;
	LinearLayout deviceslistLayout;

	ListView bindListView;
	DataHelper dh;

	BindAdapter mBindAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
		CGIManager.getInstance().addObserver(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		dh = new DataHelper((Context) getActivity());
		new getDataTask().execute(1);
	}

	public class getDataTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			mList = DataUtil.getBindDevices((Context) getActivity(), dh);
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			initView();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.bind_list, null);
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		
		no_dev = (ViewGroup) mView.findViewById(R.id.no_dev);

		deviceslistLayout = (LinearLayout) mView.findViewById(R.id.deviceslist);

		bindListView = (ListView) mView.findViewById(R.id.devices_list);

		if (null == mList || mList.size() == 0) {
			no_dev.setVisibility(View.VISIBLE);
			deviceslistLayout.setVisibility(View.GONE);
		} else {
			mBindAdapter = new BindAdapter();
			bindListView.setAdapter(mBindAdapter);
			bindListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Bundle extra = new Bundle();
					extra.putString(DevicesModel.IEEE, mList.get(position).getmIeee());
					extra.putString(DevicesModel.EP, mList.get(position).getmEP());
					extra.putString(DevicesModel.CLUSTER_ID, mList.get(position).getmClusterID().substring(0, 4));
					
					ChangeFragment c = (ChangeFragment) getActivity();
					BindControlFragment mBindControlFragment = new BindControlFragment();
					mBindControlFragment
							.setBackAction((backAction) getActivity());
					mBindControlFragment.setArguments(extra);
					c.setFragment(mBindControlFragment);
				}
			});
		}

	}

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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View mView = convertView;
			ViewHolder mHolder;
			DevicesModel mDevices = (DevicesModel) getItem(position);
			
			List<DevicesModel> mBindedDeviceList;
			SQLiteDatabase db = dh.getSQLiteDatabase();
			String where = " devout_ieee=? and devout_ep=? and cluster=? ";
			String[] args = {mDevices.getmIeee(), mDevices.getmEP(),mDevices.getmClusterID().substring(0,4)};
			mBindedDeviceList=dh.queryForBindDevicesList(db, DataHelper.BIND_TABLE, where, args);
			
			if (null == mView) {
				mHolder = new ViewHolder();
				mView = LayoutInflater.from((Context) getActivity()).inflate(
						R.layout.bind_item, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mHolder.devices_state = (TextView) mView
						.findViewById(R.id.devices_state);
				mView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) mView.getTag();
			}

			mHolder.devices_name.setText(mDevices.getmUserDefineName().replace(
					" ", ""));
			
			int i;
			if(mBindedDeviceList==null) {
				i=0;
			} else {
				i=mBindedDeviceList.size();
			}
			if (i>0) {
				mHolder.devices_state.setText("已绑定"+i+"个设备");
			} else {
				mHolder.devices_state.setText("未绑定任何设备");
			}

			int devModeleId = Integer.parseInt(mDevices.getmDeviceId());
			mHolder.devices_img.setImageResource(UiUtils
					.getDevicesSmallIcon(devModeleId,mDevices.getmModelId().trim()));
//			if (DataHelper.IAS_ZONE_DEVICETYPE == devModeleId
//					|| DataHelper.IAS_ACE_DEVICETYPE == devModeleId) {
//
//				mHolder.devices_img.setImageResource(UiUtils
//						.getDevicesSmallIconByModelId(mDevices.getmModelId()
//								.trim()));
//			} else if (mDevices.getmModelId().indexOf(
//					DataHelper.Multi_key_remote_control) == 0) {
//				mHolder.devices_img.setImageResource(UiUtils
//						.getDevicesSmallIconForRemote(devModeleId));
//			} else {
//				mHolder.devices_img.setImageResource(UiUtils
//						.getDevicesSmallIcon(devModeleId));
//			}
			return mView;
		}

		public class ViewHolder {
			ImageView devices_img;
			TextView devices_name;
			TextView devices_state;
		}

	}

	@Override
	public void onDestroy() {
		CGIManager.getInstance().deleteObserver(this);
		super.onDestroy();
	}

	@Override
	public void update(Manger observer, Object object) {
		final Event event = (Event) object;
		if (EventType.GETBINDLIST == event.getType()) {
			if (event.isSuccess() == true) {
				mBindAdapter.notifyDataSetChanged();
			} else {

			}
		}
	}
}
