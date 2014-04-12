package com.gdgl.activity;

import com.gdgl.activity.ShowDevicesGroupFragmentActivity.adapterSeter;
import com.gdgl.smarthome.R;
import com.gdgl.util.ActionSlideExpandableListView;
import com.gdgl.util.SlideExpandableListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

public class DevicesListFragment extends Fragment implements adapterSeter,OnRefreshListener<ListView>  {

	private static final String TAG = "DevicesListFragment";
	private View mView;
	PullToRefreshListView devices_list;

	int refreshTag = 0;

	BaseAdapter mBaseAdapter;
	private refreshData mRefreshData;

	LinearLayout list_root;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "zzz->onCreateView");
		mView = inflater.inflate(R.layout.devices_list_fragment, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		Log.i(TAG, "zzz->initView");

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
					mRefreshData.refreshListData();
				}
			}
		});

		devices_list.setAdapter(new SlideExpandableListAdapter(mBaseAdapter,
				R.id.expandable_toggle_button, R.id.expandable));
	}

	public void setLayout() {
		LayoutParams mLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		list_root.setLayoutParams(mLayoutParams);
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.i(TAG, "zzz->onAttach");
		if (!(activity instanceof refreshData)) {
			throw new IllegalStateException("Activity必须实现refreshData接口");
		}
		mRefreshData = (refreshData) activity;

	}

	public interface refreshData {
		public void refreshListData();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mRefreshData = null;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		setLayout();
		super.onResume();
	}

	@Override
	public void setAdapter(BaseAdapter mAdapter) {
		// TODO Auto-generated method stub
		Log.i("zzz", "zzz->DevicesListFragment setAdapter");
		mBaseAdapter = null;
		mBaseAdapter = mAdapter;
	}

	@Override
	public void setSelectedPostion(int postion) {
		// TODO Auto-generated method stub
		devices_list.getRefreshableView().setSelection(postion);
	}

	@Override
	public void stopRefresh() {
		// TODO Auto-generated method stub
		devices_list.onRefreshComplete();
		refreshTag = 0;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
	}
}
