package com.gdgl.util;

import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditDevicesDlg {
	private Context mContext;
	private SimpleDevicesModel mSimpleDevicesModel;

	EditDialogcallback dialogcallback;
	Dialog dialog;
	Button save;
	Button cancle;
	TextView textView;

	EditText mName;
	// EditText mRegion;
	LinearLayout devices_region;

	public EditDevicesDlg(Context c, SimpleDevicesModel s) {
		mContext = c;
		mSimpleDevicesModel = s;

		dialog = new Dialog(mContext, R.style.MyDialog);
		dialog.setContentView(R.layout.edit_devices_dlg);
		textView = (TextView) dialog.findViewById(R.id.txt_title);

		devices_region = (LinearLayout) dialog
				.findViewById(R.id.devices_region);

		mName = (EditText) dialog.findViewById(R.id.edit_name);
		// mRegion = (EditText) dialog.findViewById(R.id.edit_region);

		final String name;
		if (mSimpleDevicesModel.getmModelId().contains(
				DataHelper.Wall_switch_double)
				|| mSimpleDevicesModel.getmModelId().contains(
						DataHelper.Wall_switch_triple)) {
			name = mSimpleDevicesModel.getmDefaultDeviceName();
			devices_region.setVisibility(View.GONE);
		} else {
			name = mSimpleDevicesModel.getmDefaultDeviceName();
		}
		final String region = mSimpleDevicesModel.getmDeviceRegion();

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
					dialogcallback.saveedit(mSimpleDevicesModel.getmIeee(),
							mSimpleDevicesModel.getmEP(), mN);
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
		public void saveedit(String ieee, String ep, String name);
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
