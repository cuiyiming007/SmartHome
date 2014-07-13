package com.gdgl.activity;

import java.util.List;

import com.gdgl.activity.BindControlFragment.backAction;
import com.gdgl.activity.BindControlFragment.updateDevTask;
import com.gdgl.activity.BindControlFragment.updateList;
import com.gdgl.activity.JoinNetFragment.ChangeFragment;
import com.gdgl.manager.BindManager;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.binding.BindingDataEntity;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;

import android.app.Fragment;
import android.content.Context;
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

public class BindListFragment extends Fragment implements updateList,
		UIListener {

	private View mView;
	List<DevicesModel> mList;

	ViewGroup no_dev;
	Button mBack;

	// LinearLayout ch_pwd;
	LinearLayout deviceslist;

	ListView bindList;
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
			BindManager.getInstance().initalBindInfoMapFromServer(mList);
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
		// ch_pwd = (LinearLayout) mView.findViewById(R.id.ch_pwd);
		// LinearLayout.LayoutParams mLayoutParams = new
		// LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.MATCH_PARENT,
		// LinearLayout.LayoutParams.MATCH_PARENT);
		//
		// ch_pwd.setLayoutParams(mLayoutParams);
		no_dev = (ViewGroup) mView.findViewById(R.id.no_dev);

		deviceslist = (LinearLayout) mView.findViewById(R.id.deviceslist);

		bindList = (ListView) mView.findViewById(R.id.devices_list);

		if (null == mList || mList.size() == 0) {
			no_dev.setVisibility(View.VISIBLE);
			deviceslist.setVisibility(View.GONE);
		} else {
			mBindAdapter = new BindAdapter();
			bindList.setAdapter(mBindAdapter);
			bindList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Bundle extra = new Bundle();
					extra.putInt(BindControlFragment.DevicesId,
							mList.get(position).getID());
					// int bindId=mList.get(position).getmBindTo()==null ||
					// mList.get(position).getmBindTo().trim().equals("")?-1:Integer.parseInt(mList.get(position).getmBindTo().trim());
					// extra.putInt(BindControlFragment.BindId, bindId);
					ChangeFragment c = (ChangeFragment) getActivity();
					BindControlFragment mBindControlFragment = new BindControlFragment();
					mBindControlFragment.setUplist(BindListFragment.this);
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
			final DevicesModel mDevices = (DevicesModel) getItem(position);

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
			// if (null != mDevices.getmBindTo()
			// && !mDevices.getmBindTo().trim().equals("")) {
			if (BindManager.getInstance().hasBinded(mDevices)) {
				mHolder.devices_state.setText("已绑定");
			} else {
				mHolder.devices_state.setText("未绑定到任何设备");
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
			return mView;
		}

		public class ViewHolder {
			ImageView devices_img;
			TextView devices_name;
			TextView devices_state;
		}

	}

	@Override
	public void upList(DevicesModel dm, String id) {
//		// TODO Auto-generated method stub
//		int postion = 0;
//		if (null != mList && mList.size() > 0) {
//			for (int i = 0; i < mList.size(); i++) {
//				if (mList.get(i).getmIeee().trim().equals(dm.getmIeee().trim())
//						&& mList.get(i).getmEP().trim()
//								.equals(dm.getmEP().trim())) {
//					postion = i;
//
//				}
//			}
//			mList.get(postion).setmBindTo(id);
//			mBindAdapter.notifyDataSetChanged();
//		}

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
				BindingDataEntity data = (BindingDataEntity) event.getData();
				BindManager.getInstance().setBindInfoMap(data);
				BindManager.getInstance().addInitialedDeviceNum();
				mBindAdapter.notifyDataSetChanged();
			} else {

			}
		}
	}
}
