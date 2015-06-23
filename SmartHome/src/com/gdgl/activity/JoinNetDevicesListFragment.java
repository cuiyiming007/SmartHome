package com.gdgl.activity;

import java.util.List;

import com.gc.materialdesign.views.CheckBox;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.smarthome.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
 * 组网后获得的新设备列表
 * @author Trice
 *
 */
public class JoinNetDevicesListFragment extends Fragment {

	private View mView;

	LinearLayout join_net_listLayout;
	ListView devices_list;

	List<DevicesModel> mDevList;
	DataHelper mDH;
	JoinNetAdapter mJoinNetAdapter;

	ViewGroup no_dev;
	Button mBack;
	TextView mNoContent;

	LinearLayout deviceslist;

	public static final int FINISH_OPERATOR = 1;
	public static final int SUCESS = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initdata();
	}

	private void initdata() {
		// TODO Auto-generated method stub
		Context c = (Context) getActivity();
		mDH = new DataHelper(c);
		if(mJoinNetAdapter==null) {
			mJoinNetAdapter = new JoinNetAdapter();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.join_net_list, null);
		initView();
		return mView;
	}

	public void setList(List<DevicesModel> list) {
		mDevList = list;
		if (null != mJoinNetAdapter) {
			mJoinNetAdapter.setList(mDevList);
			mJoinNetAdapter.notifyDataSetChanged();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		join_net_listLayout = (LinearLayout) mView.findViewById(R.id.join_net_list);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		join_net_listLayout.setLayoutParams(mLayoutParams);
		no_dev = (ViewGroup) mView.findViewById(R.id.no_dev);

		deviceslist = (LinearLayout) mView.findViewById(R.id.deviceslist);

		mNoContent = (TextView) no_dev.findViewById(R.id.text_content);
		mNoContent.setText("未扫描到新设备");

		mBack = (Button) mView.findViewById(R.id.back);
		mBack.setVisibility(View.GONE);
//		mBack.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				FragmentManager fm = getActivity().getSupportFragmentManager();
//				fm.popBackStack();
//			}
//		});

		devices_list = (ListView) mView.findViewById(R.id.devices_list);
		if (null == mDevList || mDevList.size() == 0) {
			no_dev.setVisibility(View.VISIBLE);
			deviceslist.setVisibility(View.GONE);
		} else {
			mJoinNetAdapter.setList(mDevList);
			devices_list.setAdapter(mJoinNetAdapter);
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

//	public int isInList(String iee, String ep) {
//		if (null == mDevList || mDevList.size() == 0) {
//			return -1;
//		}
//		if (iee == null || ep == null) {
//			return -1;
//		}
//		DevicesModel sd;
//		for (int m = 0; m < mDevList.size(); m++) {
//			sd = mDevList.get(m);
//			if (iee.trim().equals(sd.getmIeee().trim())
//					&& ep.trim().equals(sd.getmEP().trim())) {
//				return m;
//			}
//		}
//		return -1;
//	}


	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			Button btn = (Button) msg.obj;
			switch (what) {
			case FINISH_OPERATOR:
				btn.setEnabled(true);
				break;
			case SUCESS:
				btn.setEnabled(true);
				break;
			default:
				break;
			}
		};
	};

	public class JoinNetAdapter extends BaseAdapter {

		List<DevicesModel> mdev;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mdev && mdev.size() > 0) {
				return mdev.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mdev && mdev.size() > 0) {
				return mdev.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mdev && mdev.size() > 0) {
				return mdev.get(position).getID();
			}
			return position;
		}

		public void setList(List<DevicesModel> ml) {
			mdev = null;
			mdev = ml;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (null == mdev) {
				return convertView;
			}
			View mView = convertView;
			ViewHolder mHolder;
			final DevicesModel mDevices = mdev.get(position);

			if (null == mView) {
				mHolder = new ViewHolder();

				mView = LayoutInflater.from(getActivity()).inflate(
						R.layout.scene_device_add_item, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mHolder.custom_divider = (View) mView
						.findViewById(R.id.custom_divider);
				mHolder.selected = (CheckBox) mView.findViewById(R.id.selected);
				mView.setTag(mHolder);

			} else {
				mHolder = (ViewHolder) mView.getTag();
			}

			mHolder.devices_name.setText(mDevices.getmDefaultDeviceName());

			mHolder.devices_img.setImageResource(DataUtil
					.getDefaultDevicesSmallIcon(mDevices.getmDeviceId(),
							mDevices.getmModelId().trim()));
			mHolder.custom_divider.setVisibility(View.GONE);
			mHolder.selected.setVisibility(View.GONE);

			return mView;
		}

		public class ViewHolder {
			public ImageView devices_img;
			public TextView devices_name;
			public CheckBox selected;
			public View custom_divider;
		}
	}
}
