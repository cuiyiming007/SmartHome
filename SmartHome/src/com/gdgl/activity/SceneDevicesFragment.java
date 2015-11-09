package com.gdgl.activity;

import java.util.List;

import com.gc.materialdesign.views.ButtonFloat;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.scene.SceneDevice;
import com.gdgl.smarthome.R;

import android.support.v4.app.Fragment;
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
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class SceneDevicesFragment extends Fragment {

	private View mView;
	ListView devices_list_view;
	ButtonFloat mButtonFloat;

	public List<SceneDevice> mSceneDevicesList;

	BaseAdapter mBaseAdapter;

	DataHelper mDataHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDataHelper = new DataHelper(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.scene_devices_fragment, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub

		devices_list_view = (ListView) mView.findViewById(R.id.devices_list);
		mButtonFloat = (ButtonFloat) mView.findViewById(R.id.buttonFloat);
		mButtonFloat.setVisibility(View.VISIBLE);
		devices_list_view.setAdapter(mBaseAdapter);
		mButtonFloat.attachToListView(devices_list_view);

		mButtonFloat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeFragment changeFragment = (ChangeFragment) getActivity();
				changeFragment.setFragment(new SceneDevicesAddFragment());
			}
		});
		registerForContextMenu(devices_list_view);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.setHeaderTitle("删除设备");
		menu.add(0, 1, 0, "删除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		int menuIndex = item.getItemId();
		if (1 == menuIndex) {
			mSceneDevicesList.remove(position);
			mBaseAdapter.notifyDataSetChanged();
		}
		return super.onContextItemSelected(item);
	}

	public class SceneDevicesListAdapter extends BaseAdapter {

		public SceneDevicesListAdapter(List<SceneDevice> list) {
			mSceneDevicesList = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mSceneDevicesList) {
				return mSceneDevicesList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mSceneDevicesList) {
				return mSceneDevicesList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mSceneDevicesList) {
				return (long) mSceneDevicesList.get(position).getSid();
			}
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (null == mSceneDevicesList) {
				return convertView;
			}
			View mView = convertView;
			final ViewHolder mHolder;
			final SceneDevice mDevices = mSceneDevicesList.get(position);

			if (null == mView) {
				mHolder = new ViewHolder();
				mView = LayoutInflater.from(getActivity()).inflate(
						R.layout.scene_device_item, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mHolder.devices_state = (TextView) mView
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
			String[] args = { mDevices.getIeee(), mDevices.getEp() };

			Cursor cursor;
			if(mDevices.getIeee().length()<16) {
				cursor = mDataHelper.query(db, DataHelper.RF_DEVICES_TABLE,
						columns, where, args, null, null, null, null);
			} else {
				cursor = mDataHelper.query(db, DataHelper.DEVICES_TABLE,
						columns, where, args, null, null, null, null);
			}
			int deivceID = 0;
			String picSource = Integer
					.toString(R.drawable.ui2_device_alarm);
			String deviceName = "";
			while (cursor.moveToNext()) {
				deivceID = cursor.getInt(cursor
						.getColumnIndex(DevicesModel.DEVICE_ID));
				deviceName = cursor.getString(cursor
						.getColumnIndex(DevicesModel.DEFAULT_DEVICE_NAME));
				picSource = cursor.getString(cursor
						.getColumnIndex(DevicesModel.PIC_NAME));
			}
			cursor.close();
			db.close();

			final int deviceid = deivceID;

			mHolder.devices_name.setText(deviceName);
			mHolder.devices_img.setImageResource(Integer.parseInt(picSource));
			if (mDevices.getDevicesStatus() == 0) {
				mHolder.devices_switch.setChecked(false);
				switch (deivceID) {
				case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
				case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
				case DataHelper.ON_OFF_LIGHT_DEVICETYPE:
				case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
				case DataHelper.SHADE_DEVICETYPE:
					mHolder.devices_state.setText("关闭");
					break;
				case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
				case DataHelper.IAS_ZONE_DEVICETYPE:
				case DataHelper.RF_DEVICE:
					mHolder.devices_state.setText("撤防");
					break;
				default:
					mHolder.devices_state.setText("关闭");
					break;
				}
			}
			if (mDevices.getDevicesStatus() == 1) {
				mHolder.devices_switch.setChecked(true);
				switch (deivceID) {
				case DataHelper.ON_OFF_OUTPUT_DEVICETYPE:
				case DataHelper.MAINS_POWER_OUTLET_DEVICETYPE:
				case DataHelper.ON_OFF_LIGHT_DEVICETYPE:
				case DataHelper.DIMEN_LIGHTS_DEVICETYPE:
				case DataHelper.SHADE_DEVICETYPE:
					mHolder.devices_state.setText("打开");
					break;
				case DataHelper.COMBINED_INTERFACE_DEVICETYPE:
				case DataHelper.IAS_ZONE_DEVICETYPE:
				case DataHelper.RF_DEVICE:
					mHolder.devices_state.setText("布防");
					break;
				default:
					mHolder.devices_state.setText("打开");
					break;
				}
			}
			mHolder.devices_switch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mHolder.devices_switch.isChecked()) {
						mDevices.setDevicesStatus(1);
						notifyDataSetChanged();
					} else {
						mDevices.setDevicesStatus(0);
						notifyDataSetChanged();
					}
				}
			});

			return mView;
		}

		public void setList(List<SceneDevice> list) {
			mSceneDevicesList = null;
			mSceneDevicesList = list;
		}

		public class ViewHolder {
			ImageView devices_img;
			TextView devices_name;
			TextView devices_state;
			Switch devices_switch;
		}
	}

	public void setAdapter(BaseAdapter mAdapter) {
		// TODO Auto-generated method stub
		mBaseAdapter = null;
		mBaseAdapter = mAdapter;
	}

	public interface ChangeFragment {
		public void setFragment(Fragment f);
	}
}
