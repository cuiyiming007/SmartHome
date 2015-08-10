package com.gdgl.util;

import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.smarthome.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditDevicesDlg {
	private Context mContext;
	private DevicesModel mDevicesModel;
	EditDialogcallback dialogcallback;
	Dialog dialog;
	Button save;
	Button cancle;
	TextView textView;

	EditText mName;
	// EditText mRegion;
	LinearLayout devices_region;

	public EditDevicesDlg(Context c, DevicesModel s) {
		mContext = c;
		mDevicesModel = s;

		dialog = new Dialog(mContext, R.style.MyDialog);
		dialog.setContentView(R.layout.edit_devices_dlg);
		textView = (TextView) dialog.findViewById(R.id.txt_title);

		devices_region = (LinearLayout) dialog
				.findViewById(R.id.devices_region);

		mName = (EditText) dialog.findViewById(R.id.edit_name);
		// mRegion = (EditText) dialog.findViewById(R.id.edit_region);
		String mDefaultDeviceName = "";
		String mDefaultDeviceNameLast = "";
		if(mDevicesModel.getmDefaultDeviceName().length() >= 6){
			mDefaultDeviceName = mDevicesModel.getmDefaultDeviceName().substring(0, mDevicesModel.getmDefaultDeviceName().length()-6);
			mDefaultDeviceNameLast = mDevicesModel.getmDefaultDeviceName().substring(mDevicesModel.getmDefaultDeviceName().length()-6);
		}else{
			mDefaultDeviceName = mDevicesModel.getmDefaultDeviceName().trim();
		}
		
		
		final String name = mDefaultDeviceName;
		final String name_last = mDefaultDeviceNameLast;

//		if (mDevicesModel.getmModelId().contains(
//				DataHelper.Wall_switch_double)
//				|| mDevicesModel.getmModelId().contains(
//						DataHelper.Wall_switch_triple)) {
//			name = mDevicesModel.getmDefaultDeviceName();
//			devices_region.setVisibility(View.GONE);
//		} else {
//		}

		mName.setText(name);
		// mRegion.setText(region);

		save = (Button) dialog.findViewById(R.id.btn_save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String mN = mName.getText().toString();
				// String mR = mRegion.getText().toString();
				// if ((!name.equals(mN)) || (!mRegion.equals(mR))) {
				// dialogcallback.saveedit(mSimpleDevicesModel.getmIeee(),mSimpleDevicesModel.getmEP(),mN,
				// mR);
				// }
				if ((!name.equals(mN))) {
//					dialogcallback.saveedit(mSimpleDevicesModel.getmIeee(),
//							mSimpleDevicesModel.getmEP(), mN, mR);
					dialogcallback.saveedit(mDevicesModel, mN + name_last);
				}
				dismiss();

			}
		});

		cancle = (Button) dialog.findViewById(R.id.btn_cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	public interface EditDialogcallback {
//		public void saveedit(String ieee, String ep, String name, String region);
		public void saveedit(DevicesModel mDevicesModel, String name);
	}

	public void setDialogCallback(EditDialogcallback dialogcallback) {
		this.dialogcallback = dialogcallback;
	}

	public void setContent(String content) {
		textView.setText(content);
	}

	public void show() {
		dialog.show();
	}

	public void hide() {
		dialog.hide();
	}

	public void dismiss() {
		dialog.dismiss();
	}

}
