package com.gdgl.activity;

import com.gdgl.activity.ShowDevicesGroupFragmentActivity.adapterSeter;
import com.gdgl.adapter.AllDevicesAdapter;
import com.gdgl.adapter.AllDevicesAdapter.AddChecked;
import com.gdgl.adapter.AllDevicesAdapter.ViewHolder;
import com.gdgl.smarthome.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;

public class AllDevicesFragment extends Fragment implements adapterSeter {

	private View mView;
	PullToRefreshListView devices_list;

	int refreshTag = 0;

	BaseAdapter mBaseAdapter;
	
	AddChecked mAddChecked;
	LinearLayout list_root;

	Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.all_devices, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub

		list_root = (LinearLayout) mView.findViewById(R.id.list_root);
		setLayout();
		devices_list = (PullToRefreshListView) mView
				.findViewById(R.id.devices_list);

		devices_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (1 == refreshTag) {
				} else {
					refreshTag = 1;
					String label = DateUtils.formatDateTime(getActivity(),
							System.currentTimeMillis(),
							DateUtils.FORMAT_SHOW_TIME
									| DateUtils.FORMAT_SHOW_DATE
									| DateUtils.FORMAT_ABBREV_ALL);

					// Update the LastUpdatedLabel
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
							label);

					// Do work to refresh the list here.
				}
			}
		});
		
		devices_list.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ViewHolder holder = (ViewHolder) view.getTag();
				holder.selected.toggle();
				((AllDevicesAdapter) mBaseAdapter).getIsSelected().put(position-1, holder.selected.isChecked());
				mBaseAdapter.notifyDataSetChanged();
				if (holder.selected.isChecked()) {
					mAddChecked.AddCheckedDevices(holder.mPostion);
				} else {
					mAddChecked.DeletedCheckedDevices(holder.mPostion);
				}
			}
		});
		
//		registerForContextMenu(devices_list.getRefreshableView());
		devices_list.setAdapter(mBaseAdapter);
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mAddChecked=(AddChecked)activity;
	}
	
	public void setLayout() {
		LayoutParams mLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		list_root.setLayoutParams(mLayoutParams);
	}

	@Override
	public void setAdapter(BaseAdapter mAdapter) {
		// TODO Auto-generated method stub
		mBaseAdapter = null;
		mBaseAdapter = mAdapter;
	}

	@Override
	public void stopRefresh() {
		// TODO Auto-generated method stub
		devices_list.onRefreshComplete();
		refreshTag = 0;
	}

	@Override
	public void setSelectedPostion(int postion) {
		// TODO Auto-generated method stub
		devices_list.getRefreshableView().setSelection(postion);
	}
}
