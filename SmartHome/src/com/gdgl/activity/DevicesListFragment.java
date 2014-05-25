package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.activity.ShowDevicesGroupFragmentActivity.adapterSeter;
import com.gdgl.adapter.DevicesBaseAdapter;
import com.gdgl.manager.Manger;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.smarthome.R;
import com.gdgl.util.EditDevicesDlg;
import com.gdgl.util.EditDevicesDlg.EditDialogcallback;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;
import com.gdgl.util.UiUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

public class DevicesListFragment extends BaseFragment implements adapterSeter {

	private static final String TAG = "DevicesListFragment";
	private View mView;
	PullToRefreshListView devices_list;
	private setData setDataActivity;
	SimpleDevicesModel mSimpleDevicesModel;
	int refreshTag = 0;

	BaseAdapter mBaseAdapter;
	private refreshData mRefreshData;

	LinearLayout list_root;

	public static final String PASS_OBJECT = "pass_object";
	
	public static final String PASS_ONOFFIMG = "pass_on_off_img";
	
	public static final String OPERATOR = "with_or_not_operator";

	Context mContext;

	AdapterContextMenuInfo selectedMenuInfo = null;
	
    public static final int WITH_OPERATE = 0;
    public static final int WITHOUT_OPERATE = 2;
    
    private int type;
    
    List<SimpleDevicesModel> mList;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		if (null != extras) {
			type = extras.getInt(OPERATOR, WITH_OPERATE);
		}else{
			type=WITH_OPERATE;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.devices_list_fragment, null);
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
					mRefreshData.refreshListData();
				}
			}
		});
		
		if(type==WITH_OPERATE){
			devices_list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Log.i(TAG, "tagzgs->position=" + position);
					mSimpleDevicesModel = mRefreshData.getDeviceModle(position - 1);
					mRefreshData.setDevicesId(mSimpleDevicesModel.getmIeee());
					
					if(mSimpleDevicesModel.getmIeee().equals("00137A0000010148") && mSimpleDevicesModel.getmEP().equals("01")){
						Intent intent=new Intent();
						intent.setClass((Context)getActivity(), KongtiaoTvControlActivity.class);
						startActivity(intent);
					}else{
						Fragment mFragment;

						if (mSimpleDevicesModel.getmModelId().indexOf(
								DataHelper.Doorbell_button) == 0) {
							mFragment = new DoorBellFragment();
						}
//						else if (mSimpleDevicesModel.getmModelId().indexOf(
//								DataHelper.Infrared_controller) == 0) {
//							mFragment = new RemoteControlFragment();
//						}
						else if (mSimpleDevicesModel.getmModelId().indexOf(
								DataHelper.Wireless_Intelligent_valve_switch) == 0) {
							mFragment = new OutPutFragment();
						} else if(mSimpleDevicesModel.getmModelId().indexOf(
								DataHelper.One_key_operator) == 0){
							mFragment=new SafeSimpleOperation();
						}
						else {
							mFragment = UiUtils.getFragment(mSimpleDevicesModel
									.getmDeviceId());
						}
						if (null != mFragment) {
							Bundle extras = new Bundle();
							if (DataHelper.IAS_ZONE_DEVICETYPE == mSimpleDevicesModel
									.getmDeviceId()
									|| DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE == mSimpleDevicesModel
											.getmDeviceId()
									|| DataHelper.ON_OFF_OUTPUT_DEVICETYPE == mSimpleDevicesModel
											.getmDeviceId()
									|| DataHelper.IAS_WARNNING_DEVICE_DEVICETYPE == mSimpleDevicesModel
											.getmDeviceId()		
									|| DataHelper.MAINS_POWER_OUTLET_DEVICETYPE == mSimpleDevicesModel
											.getmDeviceId()) {
								int[] OnOffImg = { R.drawable.bufang_on,
										R.drawable.chefang_off };
								extras.putIntArray(PASS_ONOFFIMG, OnOffImg);
							}
							// PASS_OBKECT
							extras.putParcelable(PASS_OBJECT, mSimpleDevicesModel);
							mFragment.setArguments(extras);
							mRefreshData.setFragment(mFragment, position - 1);
						}
					}
				}
			});
		}
//		registerForContextMenu(devices_list.getRefreshableView());
		devices_list.setAdapter(mBaseAdapter);
		initList();
	}

	private void initList() {
		// TODO Auto-generated method stub
		if(null!=mBaseAdapter){
			int m=mBaseAdapter.getCount();
			mList=new ArrayList<SimpleDevicesModel>();
			for (int i = 0; i < m; i++) {
				SimpleDevicesModel sd=(SimpleDevicesModel)mBaseAdapter.getItem(i);
				mList.add(sd);
			}
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.setHeaderTitle("编辑&删除");
		menu.add(0, 1, 0, "编辑");
		menu.add(0, 2, 0, "删除");
		Log.i(TAG, "tagzgs->menuInfo==null =" + (menuInfo == null));
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	public void setLayout() {
		LayoutParams mLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		list_root.setLayoutParams(mLayoutParams);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		mSimpleDevicesModel = mRefreshData.getDeviceModle(position - 1);
		int menuIndex = item.getItemId();
		Log.i(TAG, "tagzgs-> menuInfo.position=" + position
				+ " item.getItemId()" + item.getItemId());
		if (1 == menuIndex) {
			EditDevicesDlg mEditDevicesDlg = new EditDevicesDlg(
					(Context) getActivity(), mSimpleDevicesModel);
			mEditDevicesDlg
					.setDialogCallback((EditDialogcallback) mRefreshData);

			mEditDevicesDlg.setContent("编辑"
					+ mSimpleDevicesModel.getmUserDefineName().trim());
			mEditDevicesDlg.show();
		}
		if (2 == menuIndex) {
			mRefreshData.setDevicesId(mSimpleDevicesModel.getmIeee());
			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
					(Context) getActivity());
			mMyOkCancleDlg.setDialogCallback((Dialogcallback) mRefreshData);
			mMyOkCancleDlg.setContent("确定要删除"
					+ mSimpleDevicesModel.getmUserDefineName().trim() + "吗?");
			mMyOkCancleDlg.show();
		}
		return super.onContextItemSelected(item);
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.i(TAG, "zzz->onAttach");
		if (!(activity instanceof refreshData)) {
			throw new IllegalStateException("Activity必须实现refreshData接口");
		}
		mRefreshData = (refreshData) activity;
		setDataActivity=(setData)activity;
	}

	public interface refreshData {
		public void refreshListData();

		public SimpleDevicesModel getDeviceModle(int postion);

		public void setFragment(Fragment mFragment, int postion);

		public void setDevicesId(String id);
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
	
	public int isInList(String iee,String ep){
		
		if(null==mList || mList.size()==0){
			return -1;
		}
		if(iee==null || ep==null){
			return -1;
		}
		SimpleDevicesModel sd;
		for(int m=0;m<mList.size();m++){
			sd=mList.get(m);
			if(iee.trim().equals(sd.getmIeee().trim()) && ep.trim().equals(sd.getmEP().trim())){
				return m;
			}
		}
		return -1;
	}
	
	public interface setData{
		public void setdata(List<SimpleDevicesModel> list);
	}
	
	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		if (EventType.ON_OFF_STATUS == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				CallbackResponseType2 data = (CallbackResponseType2) event
						.getData();
				int m=isInList(data.getDeviceIeee(), data.getDeviceEp());
				if(-1!=m){
					if(null!=data.getValue()){
						mList.get(m).setmOnOffStatus(data.getValue());
						mView.post(new Runnable() {
							@Override
							public void run() {
								setDataActivity.setdata(mList);
							}
						});
					}
				}
				ProcessUpdate(data);
			} else {
				// if failed,prompt a Toast
				// mError.setVisibility(View.VISIBLE);
			}
		}
	}
}
