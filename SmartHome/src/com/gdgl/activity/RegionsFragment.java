package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gc.materialdesign.views.ButtonFloat;
import com.gdgl.libjingle.LibjingleResponseHandlerManager;
import com.gdgl.libjingle.LibjingleSendManager;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Region.Room;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.AddDlg;
import com.gdgl.util.AddDlg.AddDialogcallback;
import com.gdgl.util.MyOKOnlyDlg;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/***
 * 最外层区域菜单
 * 
 * @author Trice
 * 
 */
public class RegionsFragment extends Fragment implements Dialogcallback, UIListener, AddDialogcallback {

	PullToRefreshGridView content_view;
	// GridView content_view;
	View mView;
	List<Room> mregions;
	Room currentRoom;
	CustomeAdapter mCustomeAdapter;
	ViewGroup nodevices;
	ButtonFloat mButtonFloat;

	DataHelper mDateHelper;

	int menuIndex = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		CGIManager.getInstance().addObserver(RegionsFragment.this);
		LibjingleResponseHandlerManager.getInstance().addObserver(this);
		mregions = new ArrayList<Room>();
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
			CGIManager.getInstance().GetAllRoomInfo();
		} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
			LibjingleSendManager.getInstance().GetAllRoomInfo();
		}
//		mDateHelper = new DataHelper((Context) getActivity());
//		SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
//		mregions = mDateHelper.queryForRoomList(mSQLiteDatabase, DataHelper.ROOMINFO_TABLE, null, null, null, null,
//				null, null, null);
//		mDateHelper.close(mSQLiteDatabase);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.devices_main_fragment_new, null);
		initview();
		return mView;
	}

	private void initview() {
		// TODO Auto-generated method stub
		nodevices = (ViewGroup) mView.findViewById(R.id.nodevices);
		content_view = (PullToRefreshGridView) mView.findViewById(R.id.content_view);
		mButtonFloat = (ButtonFloat) mView.findViewById(R.id.buttonFloat);
		mCustomeAdapter = new CustomeAdapter();
		mCustomeAdapter.setList(mregions);
		content_view.setAdapter(mCustomeAdapter);
		// content_view.setLayoutAnimation(UiUtils
		// .getAnimationController((Context) getActivity()));
		content_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
				// TODO Auto-generated method stub
				Intent i = new Intent((Context) getActivity(), RegionDevicesActivity.class);
				i.putExtra(RegionDevicesActivity.REGION_NAME, mregions.get(index).getroom_name());
				i.putExtra(RegionDevicesActivity.REGION_ID, mregions.get(index).getroom_id());
				startActivity(i);
			}

		});
		content_view.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(),
						System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				Log.i("setOnRefreshListener", "setOnRefreshListener");
				new GetDataTask().execute();
			}
		});
		mButtonFloat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					AddDlg mAddDlg = new AddDlg(getActivity(), AddDlg.REGION);
					mAddDlg.setContent("添加区域");
					mAddDlg.setType("区域名称");
					mAddDlg.setDialogCallback(RegionsFragment.this);
					mAddDlg.show();
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(getActivity());
					myOKOnlyDlg.setContent(getResources().getString(
							R.string.Unable_In_InternetState));
					myOKOnlyDlg.show();
				}
			}
		});
		if (null == mregions || mregions.size() == 0) {
			nodevices.setVisibility(View.VISIBLE);
		} else {
			nodevices.setVisibility(View.GONE);
		}

		registerForContextMenu(content_view.getRefreshableView());
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		CGIManager.getInstance().deleteObserver(this);
		LibjingleResponseHandlerManager.getInstance().deleteObserver(this);
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
				CGIManager.getInstance().GetAllRoomInfo();
			} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
				LibjingleSendManager.getInstance().GetAllRoomInfo();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			content_view.onRefreshComplete();
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		initData();
		super.onResume();
	}

	private class CustomeAdapter extends BaseAdapter {

		private List<Room> mRoomList;

		public CustomeAdapter() {
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mRoomList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mRoomList.get(position);
		}

		public String getItemName(int position) {
			return mRoomList.get(position).getroom_name();
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return mRoomList.get(position).getroom_id();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder mViewHolder;
			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from((Context) getActivity()).inflate(R.layout.gridview_card_item, null);
				mViewHolder.funcImg = (ImageView) convertView.findViewById(R.id.func_img);
				mViewHolder.funcText = (TextView) convertView.findViewById(R.id.func_name);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			mViewHolder.funcImg.setImageResource(R.drawable.ui2_region_style);
			mViewHolder.funcText.setText(getItemName(position));
			return convertView;
		}

		class ViewHolder {
			ImageView funcImg;
			TextView funcText;
		}

		public void setList(List<Room> s) {
			mRoomList = null;
			mRoomList = s;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("编辑&删除");
		menu.add(1, 1, 0, "编辑");
		menu.add(1, 2, 0, "删除");
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getGroupId() != 1) {
			return false;
		}
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		currentRoom = mregions.get(position);
		menuIndex = item.getItemId();
		if (1 == menuIndex) {
			if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
				AddDlg mEditDlg = new AddDlg(getActivity(), currentRoom);
				mEditDlg.setContent("编辑区域");
				mEditDlg.setType("区域名称");
				mEditDlg.setDialogCallback(RegionsFragment.this);
				mEditDlg.show();
			} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
				MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(getActivity());
				myOKOnlyDlg.setContent(getResources().getString(
						R.string.Unable_In_InternetState));
				myOKOnlyDlg.show();
				menuIndex = 0;
			}
		}
		if (2 == menuIndex) {
			if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
				MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg((Context) getActivity());
				mMyOkCancleDlg.setDialogCallback(this);
				mMyOkCancleDlg.setContent("确定要删除区域  " + mregions.get(position).getroom_name() + " 吗?");
				mMyOkCancleDlg.show();
			} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
				MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(getActivity());
				myOKOnlyDlg.setContent(getResources().getString(
						R.string.Unable_In_InternetState));
				myOKOnlyDlg.show();
			}
			menuIndex = 0;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		if (event.getType() == EventType.GETALLROOM) {
			if (event.isSuccess()) {
				mregions = (List<Room>) event.getData();
				nodevices.post(new Runnable() {

					@Override
					public void run() {
						if (null == mregions || mregions.size() == 0) {
							nodevices.setVisibility(View.VISIBLE);
						} else {
							nodevices.setVisibility(View.GONE);
						}
						mCustomeAdapter.setList(mregions);
						mCustomeAdapter.notifyDataSetChanged();
					}
				});
			}
		} else if (event.getType() == EventType.ROOMDATAMAIN) {
			if (event.isSuccess()) {
				nodevices.post(new Runnable() {

					@Override
					public void run() {
						// refreshFragment();
					}
				});
			}
		}
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		String id = String.valueOf(currentRoom.getroom_id());
//		String[] ids = new String[] { id };
//		String where = " rid = ? ";
//		SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
//		ContentValues c = new ContentValues();
//		c.put(DevicesModel.DEVICE_REGION, "");
//		c.put(DevicesModel.R_ID, "-1");
//		mDateHelper.update(mSQLiteDatabase, DataHelper.DEVICES_TABLE, c, where, ids);
//		mDateHelper.update(mSQLiteDatabase, DataHelper.RF_DEVICES_TABLE, c, where, ids);
		// 删除所选区域
		CGIManager.getInstance().ZBDeleteRoomDataMainByID(id);
//		int result = mDateHelper.delete(mSQLiteDatabase, DataHelper.ROOMINFO_TABLE, " room_id = ? ", ids);
//		mSQLiteDatabase.close();
//		if (result == 1) {
			// refreshFragment();
//		}
		mregions.remove(currentRoom);
		mCustomeAdapter.notifyDataSetChanged();
	}

	@Override
	public void refreshdata(Room room) {
		// TODO Auto-generated method stub
		if (menuIndex == 0) {
			mregions.add(room);
			mCustomeAdapter.notifyDataSetChanged();
		} else if (menuIndex == 1) {
			for (int i = 0; i < mregions.size(); i++) {
				if (room.getroom_id() == mregions.get(i).getroom_id()) {
					mregions.get(i).setroom_name(room.getroom_name());
					break;
				}
			}
			mCustomeAdapter.notifyDataSetChanged();
			menuIndex = 0;
		}
	}

}
