package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.smarthome.R;
import com.gdgl.util.EditDevicesDlg;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.UiUtils;
import com.gdgl.util.EditDevicesDlg.EditDialogcallback;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/***
 * 设置菜单，设备列表NEW
 * 
 * @author Trice
 * 
 */
public class ConfigDevicesExpandableList extends BaseFragment implements
		Dialogcallback, UIListener, EditDialogcallback {

	private IntoDeviceDetailFragment inToDeviceDetailFragment;

	List<String> mDeviceSort_GroupList; // 设备分类标题列表
	List<List<DevicesModel>> mDeviceSort_ChildList; // 设备分类子列表

	ExpandableListView deviceExpandableListView;
	ExpandableAdapter expandableAdapter;

	List<DevicesModel> mSecurityControl;
	List<DevicesModel> mElecManager;
	List<DevicesModel> mEneronmentControl;
	List<DevicesModel> mEnergy;
	List<DevicesModel> mOther;

	List<DevicesModel> mCurrentList;
	int currentpostion;

	View mView;

	DataHelper mDh;

	CGIManager temptureManager;
	DeviceManager mDeviceManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub

		temptureManager = CGIManager.getInstance();
		temptureManager.addObserver(this);

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

		mDeviceSort_GroupList = new ArrayList<String>();
		for (String name : UiUtils.DEVICES_MANAGER_TAGS_NEW) {
			mDeviceSort_GroupList.add(name);
		}
		mDeviceSort_ChildList = new ArrayList<List<DevicesModel>>();
		mDeviceSort_ChildList.add(mSecurityControl);
		mDeviceSort_ChildList.add(mElecManager);
		mDeviceSort_ChildList.add(mEneronmentControl);
		mDeviceSort_ChildList.add(mEnergy);
		mDeviceSort_ChildList.add(mOther);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.all_devices_expandablelist_withgroup,
				null);
		initview();
		return mView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		temptureManager.deleteObserver(this);
		mDeviceManager.deleteObserver(this);
		CallbackManager.getInstance().deleteObserver(this);
	}

	private void initview() {
		// TODO Auto-generated method stub

		deviceExpandableListView = (ExpandableListView) mView
				.findViewById(R.id.expandableListView);
		expandableAdapter = new ExpandableAdapter(getActivity());
		deviceExpandableListView.setAdapter(expandableAdapter);
		deviceExpandableListView.expandGroup(0);

		deviceExpandableListView
				.setOnChildClickListener(new OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						// TODO Auto-generated method stub
						DevicesModel mDevicesModel = (DevicesModel) mDeviceSort_ChildList
								.get(groupPosition).get(childPosition);
						Fragment mFragment = new DeviceDtailFragment();
						Bundle extras = new Bundle();
						extras.putSerializable(Constants.PASS_OBJECT,
								mDevicesModel);
						extras.putInt(Constants.PASS_DEVICE_ABOUT,
								DeviceDtailFragment.WITH_DEVICE_ABOUT);
						mFragment.setArguments(extras);
						inToDeviceDetailFragment = (IntoDeviceDetailFragment) getActivity();
						inToDeviceDetailFragment
								.intoDeviceDetailFragment(mFragment);
						return false;
					}
				});

		registerForContextMenu(deviceExpandableListView);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub

		super.onCreateContextMenu(menu, v, menuInfo);

		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;

		int type = ExpandableListView
				.getPackedPositionType(info.packedPosition);

		// Only create a context menu for child items
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			// Array created earlier when we built the expandable list

			menu.setHeaderTitle("编辑&删除");
			menu.add(0, 1, 0, "编辑");
			menu.add(0, 2, 0, "删除");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item
				.getMenuInfo();

		int groupPos = 0, childPos = 0;

		int type = ExpandableListView
				.getPackedPositionType(info.packedPosition);
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			groupPos = ExpandableListView
					.getPackedPositionGroup(info.packedPosition);
			childPos = ExpandableListView
					.getPackedPositionChild(info.packedPosition);
		}
		mCurrentList = mDeviceSort_ChildList.get(groupPos);
		currentpostion = childPos;
		DevicesModel mDevicesModel = (DevicesModel) mDeviceSort_ChildList.get(
				groupPos).get(childPos);
		int menuIndex = item.getItemId();

		if (1 == menuIndex) {
			EditDevicesDlg mEditDevicesDlg = new EditDevicesDlg(
					(Context) getActivity(), mDevicesModel);
			mEditDevicesDlg.setDialogCallback(this);

			mEditDevicesDlg.setContent("编辑"
					+ mDevicesModel.getmDefaultDeviceName().trim());
			mEditDevicesDlg.show();
		}
		if (2 == menuIndex) {
			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
					(Context) getActivity());
			mMyOkCancleDlg.setDialogCallback((Dialogcallback) this);
			mMyOkCancleDlg.setContent("确定要删除"
					+ mDevicesModel.getmDefaultDeviceName().trim() + "吗?");
			mMyOkCancleDlg.show();
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.i("onResume", "onResume");

		super.onResume();
	}

	private class ExpandableAdapter extends BaseExpandableListAdapter {
		private Context mContext;

		public ExpandableAdapter(Context c) {
			mContext = c;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return mDeviceSort_ChildList.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ViewHolder mViewHolder;
			DevicesModel ds = (DevicesModel) mDeviceSort_ChildList.get(
					groupPosition).get(childPosition);

			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.devices_list_item_config, null);
				mViewHolder.devImg = (ImageView) convertView
						.findViewById(R.id.devices_img);
				mViewHolder.devName = (TextView) convertView
						.findViewById(R.id.devices_name);
				mViewHolder.devRegion = (TextView) convertView
						.findViewById(R.id.devices_region);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			mViewHolder.devName.setText(ds.getmDefaultDeviceName());
			if (null != ds.getmDeviceRegion()
					&& !ds.getmDeviceRegion().trim().equals("")) {
				mViewHolder.devRegion.setText(ds.getmDeviceRegion());
			} else {
				mViewHolder.devRegion.setVisibility(View.GONE);
			}

			if (ds.getmModelId().indexOf(DataHelper.RS232_adapter) == 0) {
				mViewHolder.devRegion.setText("");
				mViewHolder.devRegion.setVisibility(View.GONE);
			}

			mViewHolder.devImg.setImageResource(DataUtil
					.getDefaultDevicesSmallIcon(ds.getmDeviceId(), ds
							.getmModelId().trim()));

			return convertView;
		}

		class ViewHolder {
			ImageView devImg;
			TextView devName;
			TextView devRegion;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return mDeviceSort_ChildList.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return mDeviceSort_GroupList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return mDeviceSort_GroupList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// ViewHolder1 viewHolder1;
			// if (null == convertView) {
			// viewHolder1 = new ViewHolder1();
			// convertView = LayoutInflater.from(mContext).inflate(
			// R.layout.devices_list_title_config, null);
			// viewHolder1.arrowImg = (ImageView) convertView
			// .findViewById(R.id.arrow_up_down);
			// viewHolder1.titleName = (TextView) convertView
			// .findViewById(R.id.expandableListTitle);
			// convertView.setTag(viewHolder1);
			// } else {
			// viewHolder1 = (ViewHolder1) convertView.getTag();
			// }
			// viewHolder1.titleName.setText(mDeviceSort_GroupList.get(groupPosition));
			TextView myText = null;
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.devices_list_title_config, null);
			myText = (TextView) convertView
					.findViewById(R.id.expandableListTitle);
			myText.setText(mDeviceSort_GroupList.get(groupPosition));
			return convertView;
		}

		// class ViewHolder1 {
		// ImageView arrowImg;
		// TextView titleName;
		// }

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		DevicesModel ds = mCurrentList.get(currentpostion);
		CGIManager.getInstance().deleteNode(ds.getmIeee().trim());
		int result = mDh.deleteDeviceWithGroup((Context) getActivity(),
				mDh.getSQLiteDatabase(), DataHelper.DEVICES_TABLE, " ieee=? ",
				new String[] { ds.getmIeee().trim() });
		if (result > 0) {
			mCurrentList.remove(currentpostion);
			expandableAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void update(Manger observer, Object object) {
		final Event event = (Event) object;
		if (EventType.CHANGEDEVICENAME == event.getType()) {
			if (event.isSuccess()) {
				final String name = (String) event.getData();
				deviceExpandableListView.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mCurrentList.get(currentpostion).setmDefaultDeviceName(
								name);
						expandableAdapter.notifyDataSetChanged();
					}
				});
			}
		}
	}

	@Override
	public void saveedit(DevicesModel mDevicesModel, String name) {
		// TODO Auto-generated method stub
		CGIManager.getInstance().ChangeDeviceName(mDevicesModel,
				Uri.encode(name));
	}

	public class getDataInBackgroundTask extends
			AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub

			if (null == mElecManager) {
				mElecManager = DataUtil.getSortManagementDevices(
						(Context) getActivity(), mDh,
						UiUtils.ELECTRICAL_MANAGER);
			}
			if (null == mSecurityControl) {
				mSecurityControl = DataUtil.getSortManagementDevices(
						(Context) getActivity(), mDh, UiUtils.SECURITY_CONTROL);
			}
			if (null == mEnergy) {
				mEnergy = DataUtil.getSortManagementDevices(
						(Context) getActivity(), mDh,
						UiUtils.ENERGY_CONSERVATION);
			}
			if (null == mEneronmentControl) {
				mEneronmentControl = DataUtil.getSortManagementDevices(
						(Context) getActivity(), mDh,
						UiUtils.ENVIRONMENTAL_CONTROL);
			}
			if (null == mOther) {
				mOther = DataUtil.getSortManagementDevices(
						(Context) getActivity(), mDh, UiUtils.SWITCH_DEVICE);
			}

			return 1;
		}
	}

	@Override
	public void stopRefresh() {
		// TODO Auto-generated method stub

	}

	public interface IntoDeviceDetailFragment {
		public void intoDeviceDetailFragment(Fragment mFragment);
	}

	public interface ChangeFragment {
		public void setFragment(Fragment f);
	}
}
