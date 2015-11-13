package com.gdgl.activity;

import java.util.List;

import com.gc.materialdesign.views.ButtonFloat;
import com.gdgl.libjingle.LibjingleSendManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.SceneLinkageManager;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.timing.TimingAction;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class TimingFragment extends Fragment implements UIListener,
		Dialogcallback {

	public static final String[] WEEKS = { "周日", "周一", "周二", "周三", "周四", "周五",
			"周六" };

	private View mView;
	ListView devices_list_view;
	ButtonFloat mButtonFloat;

	List<TimingAction> mTimingaActionsList;
	TimingActionListAdapter mTimingActionListAdapter;
	DataHelper mDataHelper;

	int currentposition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		CallbackManager.getInstance().addObserver(this);
		mDataHelper = new DataHelper(getActivity());
		SQLiteDatabase mSQLiteDatabase = mDataHelper.getSQLiteDatabase();
		mTimingaActionsList = mDataHelper.queryForTimingActionList(
				mSQLiteDatabase, null, null, null, null, null,
				TimingAction.TIMING_ID, null);
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

		devices_list_view = (ListView) mView.findViewById(R.id.devices_list);
		mButtonFloat = (ButtonFloat) mView.findViewById(R.id.buttonFloat);
		mButtonFloat.setVisibility(View.VISIBLE);
		mTimingActionListAdapter = new TimingActionListAdapter();
		devices_list_view.setAdapter(mTimingActionListAdapter);
		mButtonFloat.attachToListView(devices_list_view);
		devices_list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), TimingAddActivity.class);
				i.putExtra(TimingAddActivity.TYPE, TimingAddActivity.EDIT);
				i.putExtra(Constants.PASS_OBJECT,
						mTimingaActionsList.get(position));
				startActivity(i);
			}
		});

		mButtonFloat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), TimingAddActivity.class);
				i.putExtra(TimingAddActivity.TYPE, TimingAddActivity.CREATE);
				startActivity(i);
			}
		});
		registerForContextMenu(devices_list_view);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.setHeaderTitle("删除定时");
		menu.add(0, 1, 0, "删除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		currentposition = info.position;
		int menuIndex = item.getItemId();
		if (1 == menuIndex) {
			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(getActivity());
			mMyOkCancleDlg
					.setDialogCallback((Dialogcallback) TimingFragment.this);
			mMyOkCancleDlg.setContent("确定要删除该定时吗?");
			mMyOkCancleDlg.show();
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		CallbackManager.getInstance().deleteObserver(this);
		super.onDestroy();
	}

	public class TimingActionListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mTimingaActionsList) {
				return mTimingaActionsList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mTimingaActionsList) {
				return mTimingaActionsList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mTimingaActionsList) {
				return (long) mTimingaActionsList.get(position).getTid();
			}
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (null == mTimingaActionsList) {
				return convertView;
			}
			View mView = convertView;
			final ViewHolder mHolder;
			final TimingAction mTimingAction = mTimingaActionsList
					.get(position);

			String year = mTimingAction.getPara1().substring(0, 4);
			String month = mTimingAction.getPara1().substring(4, 6);
			String day = mTimingAction.getPara1().substring(6, 8);
			String hour = mTimingAction.getPara1().substring(8, 10);
			String minite = mTimingAction.getPara1().substring(10, 12);

			if (null == mView) {
				mHolder = new ViewHolder();
				mView = LayoutInflater.from(getActivity()).inflate(
						R.layout.timingaction_item, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mHolder.timing_date = (TextView) mView
						.findViewById(R.id.timing_date);
				mHolder.repeat_status = (TextView) mView
						.findViewById(R.id.repeat_status);
				mHolder.timing_state = (TextView) mView
						.findViewById(R.id.devices_state);
				mHolder.devices_switch = (Switch) mView
						.findViewById(R.id.switch_btn);
				mView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) mView.getTag();
			}

			SQLiteDatabase db = mDataHelper.getSQLiteDatabase();
			String[] columns = { DevicesModel.DEVICE_ID, DevicesModel.PIC_NAME,
					DevicesModel.DEFAULT_DEVICE_NAME };
			String where = " ieee=? and ep=? ";
			String[] args = { mTimingAction.getIeee(), mTimingAction.getEp() };

			Cursor cursor;
			if(mTimingAction.getIeee().length()<16) {
				cursor = mDataHelper.query(db, DataHelper.RF_DEVICES_TABLE,
						columns, where, args, null, null, null, null);
			} else {
				cursor = mDataHelper.query(db, DataHelper.DEVICES_TABLE,
						columns, where, args, null, null, null, null);
			}
			int deviceId = 0;
			String picSource = Integer
					.toString(R.drawable.ui2_device_alarm);
			String deviceName = "";
			while (cursor.moveToNext()) {
				deviceId = cursor.getInt(cursor
						.getColumnIndex(DevicesModel.DEVICE_ID));
				deviceName = cursor.getString(cursor
						.getColumnIndex(DevicesModel.DEFAULT_DEVICE_NAME));
				picSource = cursor.getString(cursor
						.getColumnIndex(DevicesModel.PIC_NAME));
			}
			cursor.close();
			db.close();

			mHolder.devices_name.setText(deviceName);
			mHolder.devices_img.setImageResource(Integer.parseInt(picSource));
			if (mTimingAction.getPara2() == 0) { // 是否重复
				mHolder.timing_date.setText(year
						+ "."
						+ month
						+ "."
						+ day
						+ " "
						+ hour
						+ ":"
						+ minite
						+ " "
						+ TimingAddFragment.setDeviceStatusText(mTimingAction
								.getDevicesStatus() == 1 ? true : false,
								deviceId));
				mHolder.repeat_status.setText("仅一次");
			}
			if (mTimingAction.getPara2() == 1) {
				mHolder.timing_date.setText(hour
						+ ":"
						+ minite
						+ " "
						+ TimingAddFragment.setDeviceStatusText(mTimingAction
								.getDevicesStatus() == 1 ? true : false,
								deviceId));
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < 7; i++) {
					if (mTimingAction.getPara3Status()[i]) {
						builder.append(WEEKS[i] + ",");
					}
				}
				builder.deleteCharAt(builder.length() - 1);
				mHolder.repeat_status.setText(builder.toString());
			}
			if (mTimingAction.getTimingEnable() == 0) {
				mHolder.devices_switch.setChecked(false);
				mHolder.timing_state.setText("未启用");
			}
			if (mTimingAction.getTimingEnable() == 1) {
				mHolder.devices_switch.setChecked(true);
				mHolder.timing_state.setText("启用");
			}
			mHolder.devices_switch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mHolder.devices_switch.isChecked()) {
						mHolder.timing_state.setText("启用");
						if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
							SceneLinkageManager.getInstance().EnableTimeAction(
									1, mTimingAction.getTid());
						} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
							LibjingleSendManager
									.getInstance()
									.EnableTimeAction(1, mTimingAction.getTid());
						}
					} else {
						mHolder.timing_state.setText("未启用");
						if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
							SceneLinkageManager.getInstance().EnableTimeAction(
									0, mTimingAction.getTid());
						} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
							LibjingleSendManager
									.getInstance()
									.EnableTimeAction(0, mTimingAction.getTid());
						}
					}
				}
			});

			return mView;
		}

		public void setList(List<TimingAction> list) {
			mTimingaActionsList = null;
			mTimingaActionsList = list;
		}

		public class ViewHolder {
			ImageView devices_img;
			TextView devices_name;
			TextView timing_date;
			TextView repeat_status;
			TextView timing_state;
			Switch devices_switch;
		}
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		Event event = (Event) object;
		if (event.getType() == EventType.ADDTIMINGACTION) {
			if (event.isSuccess()) {
				mTimingaActionsList.add((TimingAction) event.getData());
				devices_list_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mTimingActionListAdapter.notifyDataSetChanged();
					}
				});
			}
		} else if (event.getType() == EventType.EDITTIMINGACTION) {
			if (event.isSuccess()) {
				TimingAction timingAction = (TimingAction) event.getData();
				for (int i = 0; i < mTimingaActionsList.size(); i++) {
					if (mTimingaActionsList.get(i).getTid() == timingAction
							.getTid()) {
						mTimingaActionsList.remove(i);
						mTimingaActionsList.add(i, timingAction);
						break;
					}
				}
				devices_list_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mTimingActionListAdapter.notifyDataSetChanged();
					}
				});
			}
		} else if (event.getType() == EventType.DELTIMINGACTION) {
			if (event.isSuccess()) {
				int tid = (Integer) event.getData();
				for (int i = 0; i < mTimingaActionsList.size(); i++) {
					if (mTimingaActionsList.get(i).getTid() == tid) {
						mTimingaActionsList.remove(i);
						break;
					}
				}
				devices_list_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mTimingActionListAdapter.notifyDataSetChanged();
					}
				});
			}
		} else if (event.getType() == EventType.ENABLETIMINGACTION) {
			if (event.isSuccess()) {
				int[] ints = (int[]) event.getData();
				int tid = ints[0];
				int enable = ints[1];
				for (int i = 0; i < mTimingaActionsList.size(); i++) {
					if (mTimingaActionsList.get(i).getTid() == tid) {
						mTimingaActionsList.get(i).setTimingEnable(enable);
						break;
					}
				}
				devices_list_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mTimingActionListAdapter.notifyDataSetChanged();
					}
				});
			}
		}
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
			SceneLinkageManager.getInstance().DelTimeAction(
					mTimingaActionsList.get(currentposition).getTid());
		} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
			LibjingleSendManager.getInstance().DelTimeAction(
					mTimingaActionsList.get(currentposition).getTid());
		}
	}
}
