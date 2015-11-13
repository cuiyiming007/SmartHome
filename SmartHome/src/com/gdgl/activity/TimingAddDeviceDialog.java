package com.gdgl.activity;

import java.util.List;

import com.gc.materialdesign.views.CheckBox;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataUtil;
import com.gdgl.smarthome.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TimingAddDeviceDialog extends DialogFragment {
	private View mView;
	ListView devices_list_view;

	List<DevicesModel> mAddDevicesList;
	DeviceSelected deviceSelected;

	public TimingAddDeviceDialog(List<DevicesModel> list,
			DeviceSelected observer) {
		mAddDevicesList = list;
		deviceSelected = observer;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.devices_list_fragment, null);
        initView();
        builder.setView(mView).setTitle("选择设备");
		return builder.create();
		
	}
	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		mView = inflater.inflate(R.layout.scene_devices_fragment, null);
//		initView();
//		return mView;
//	}

	private void initView() {
		// TODO Auto-generated method stub

		devices_list_view = (ListView) mView.findViewById(R.id.devices_list);
		devices_list_view.setAdapter(new TimingAddDeviceListAdapter());
		devices_list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				deviceSelected.deviceSelectedByDialog(position);
				dismiss();
			}
		});

	}

	public class TimingAddDeviceListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mAddDevicesList) {
				return mAddDevicesList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mAddDevicesList) {
				return mAddDevicesList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mAddDevicesList) {
				return (long) mAddDevicesList.get(position).getID();
			}
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View mView = convertView;
			final ViewHolder mHolder;
			final DevicesModel mDevices = mAddDevicesList.get(position);

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

	public interface DeviceSelected {
		public void deviceSelectedByDialog(int position);
	}
}
