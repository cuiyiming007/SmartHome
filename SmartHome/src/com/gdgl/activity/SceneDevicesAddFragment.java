package com.gdgl.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gc.materialdesign.views.CheckBox;
import com.gc.materialdesign.views.CheckBox.OnCheckListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.scene.SceneDevice;
import com.gdgl.smarthome.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SceneDevicesAddFragment extends Fragment {

	private View mView;
	// PullToRefreshListView devices_list_view;
	ListView devices_list_view;
	public List<DevicesModel> mSceneDevicesAddList;

	int refreshTag = 0;

	BaseAdapter mBaseAdapter;

	AddChecked mAddChecked;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
		devices_list_view.setAdapter(mBaseAdapter);

	}

	public class SceneDevicesAddListAdapter extends BaseAdapter {

		protected AddChecked mDevicesObserver;

		private Map<Integer, Boolean> isSelected;

		public SceneDevicesAddListAdapter(List<DevicesModel> list,
				AddChecked mObserver) {
			mSceneDevicesAddList = list;
			mDevicesObserver = mObserver;
			isSelected = new HashMap<Integer, Boolean>();
			for (int i = 0; i < mSceneDevicesAddList.size(); i++) {
				isSelected.put(i, false);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mSceneDevicesAddList) {
				return mSceneDevicesAddList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mSceneDevicesAddList) {
				return mSceneDevicesAddList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mSceneDevicesAddList) {
				return (long) mSceneDevicesAddList.get(position).getID();
			}
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View mView = convertView;
			final ViewHolder mHolder;
			final DevicesModel mDevices = mSceneDevicesAddList.get(position);
			final SceneDevice mAddSceneDevice = new SceneDevice();

			if (null == mView) {
				mHolder = new ViewHolder();

				mView = LayoutInflater.from(getActivity()).inflate(
						R.layout.scene_device_add_item, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mHolder.selected = (CheckBox) mView.findViewById(R.id.selected);
				mView.setTag(mHolder);

			} else {
				mHolder = (ViewHolder) mView.getTag();
			}

			mHolder.devices_name.setText(mDevices.getmDefaultDeviceName());

			mHolder.devices_img.setImageResource(DataUtil
					.getDefaultDevicesSmallIcon(mDevices.getmDeviceId(),
							mDevices.getmModelId().trim()));

			mHolder.selected.setOncheckListener(new OnCheckListener() {

				@Override
				public void onCheck(boolean check) {
					// TODO Auto-generated method stub
					isSelected.put(position, check);
					int scenedevicetype = DataUtil
							.getTimingSceneActionParamType(mDevices
									.getmDeviceId());
					mAddSceneDevice.setActionType(scenedevicetype);
					mAddSceneDevice.setIeee(mDevices.getmIeee());
					mAddSceneDevice.setEp(mDevices.getmEP());
					mAddSceneDevice.setDevicesStatus(0);
					if (check) {
						mDevicesObserver.AddCheckedDevices(mAddSceneDevice);
					} else {
						mDevicesObserver.DeletedCheckedDevices(mAddSceneDevice);
					}
				}
			});
			mHolder.selected.setChecked(isSelected.get(position));
			return mView;
		}

		public class ViewHolder {
			public ImageView devices_img;
			public TextView devices_name;
			public CheckBox selected;
		}

		public void setList(List<DevicesModel> list) {
			mSceneDevicesAddList = null;
			mSceneDevicesAddList = list;
		}
	}

	public void setAdapter(BaseAdapter mAdapter) {
		// TODO Auto-generated method stub
		mBaseAdapter = null;
		mBaseAdapter = mAdapter;
	}

	public interface AddChecked {
		public void AddCheckedDevices(SceneDevice s);

		public void DeletedCheckedDevices(SceneDevice s);
	}

	public interface ChangeFragment {
		public void setFragment(Fragment f);
	}
}
