package com.gdgl.drawer;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.gdgl.activity.DeviceDtailFragment;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.smarthome.R;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class TestFragment extends Fragment implements UIListener {

	GridView content_view;
	List<DevicesModel> mDeviceList;
	View mView;
	ViewGroup nodevices;

	DataHelper mDh;

	CGIManager cgiManager;
	DeviceManager mDeviceManager;

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

		mDeviceManager = DeviceManager.getInstance();
		mDeviceManager.addObserver(this);

		CallbackManager.getInstance().addObserver(this);

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
//		content_view.setBackgroundResource(R.color.blue_default);
		// content_view.setLayoutAnimation(UiUtils
		// .getAnimationController((Context) getActivity()));
		CustomeAdapter mCustomeAdapter = new CustomeAdapter();
		mCustomeAdapter.setList(mDeviceList);
		content_view.setAdapter(mCustomeAdapter);
		content_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DevicesModel mDevicesModel = (DevicesModel) mDeviceList.get(position);
				Intent intent = new Intent();
				
				Bundle extras = new Bundle();
				extras.putSerializable(Constants.PASS_OBJECT,
						mDevicesModel);
				extras.putInt(Constants.PASS_DEVICE_ABOUT,
						DeviceDtailFragment.WITH_DEVICE_ABOUT);
				intent.putExtras(extras);
				intent.setClass(getActivity(), DeviceDetailActivity.class);
				startActivity(intent);
//				Fragment mFragment = new DeviceDtailFragment();
//				mFragment.setArguments(extras);
//				FragmentTransaction mFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//				mFragmentTransaction.replace(R.id.container, mFragment);
//				mFragmentTransaction.commit();
			}
		});
	}

	private class CustomeAdapter extends BaseAdapter {
		private List<DevicesModel> mAdapterDevicesList;

		public CustomeAdapter() {
		}

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

		public String getItemName(int position) {
			return mAdapterDevicesList.get(position).getmDefaultDeviceName();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (null == mAdapterDevicesList) {
				return convertView;
			}
			DevicesModel mAdapeterDevicesModel = mAdapterDevicesList.get(position);
			// TODO Auto-generated method stub
			ViewHolder mViewHolder;
			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from((Context) getActivity())
						.inflate(R.layout.gridview_card_item, null);
				mViewHolder.funcImg = (ImageView) convertView
						.findViewById(R.id.func_img);
				mViewHolder.funcText = (TextView) convertView
						.findViewById(R.id.func_name);
				mViewHolder.funcCardView = (CardView) convertView.findViewById(R.id.card_view);
//				mViewHolder.funcCardView.setCardBackgroundColor(getResources().getColor(R.color.ui_cardview_selector_blue));
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			
			mViewHolder.funcImg.setImageResource(DataUtil.getDefaultDevicesSmallIcon(
					mAdapeterDevicesModel.getmDeviceId(), mAdapeterDevicesModel.getmModelId().trim()));
			mViewHolder.funcText.setText(mAdapeterDevicesModel.getmDefaultDeviceName());
			
//			mViewHolder.funcCardView.setCardBackgroundColor(getResources().getColor(R.color.ui_cardview_selector_blue));
			return convertView;
		}

		class ViewHolder {
			ImageView funcImg;
			TextView funcText;
			CardView funcCardView;
		}

		public void setList(List<DevicesModel> s) {
			mAdapterDevicesList = null;
			mAdapterDevicesList = s;
		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		content_view.setVisibility(View.GONE);
	}

//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		content_view.setVisibility(View.VISIBLE);
//
//		// content_view.setLayoutAnimation(UiUtils
//		// .getAnimationController((Context) getActivity()));
//		CustomeAdapter mGridviewAdapter = new GridviewAdapter(0,
//				(Context) getActivity());
//		content_view.setAdapter(mGridviewAdapter);
//		super.onResume();
//	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub

	}

	public class getDataInBackgroundTask extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			mDeviceList = mDh.queryForDevicesList(mDh.getSQLiteDatabase(),
							DataHelper.DEVICES_TABLE, null, null, null, null, null,
							DevicesModel.DEVICE_PRIORITY, null);
			mDh.getSQLiteDatabase().close();
			return 1;
		}
	}
}
