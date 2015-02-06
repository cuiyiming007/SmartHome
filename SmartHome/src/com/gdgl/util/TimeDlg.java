package com.gdgl.util;

import com.gdgl.manager.CGIManager;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.AddDlg.AddDialogcallback;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TimeDlg {
	private Context mContext;
	Dialog dialog;
	Button save;
	Button cancle;
	TextView textTitle;
	EditText editHour, editMinute, editSecond;
	TextView text_name;

	AddDialogcallback mAddDialogcallback;

	public TimeDlg(Context c,  DevicesModel mDevicesModel, String timeStr) {
		final DevicesModel mDevices = mDevicesModel;
		mContext = c;
		dialog = new Dialog(mContext, R.style.MyDialog);
		dialog.setContentView(R.layout.time_dlg);
		textTitle = (TextView) dialog.findViewById(R.id.txt_title);
		editHour = (EditText) dialog.findViewById(R.id.edit_hour);
		editMinute = (EditText) dialog.findViewById(R.id.edit_minute);
		editSecond = (EditText) dialog.findViewById(R.id.edit_second);
		if(timeStr != null && !timeStr.equals("")){
			String time[] = timeStr.split(":");
			editHour.setText(time[0]);
			editMinute.setText(time[1]);
			editSecond.setText(time[2]);
		}	
		save = (Button) dialog.findViewById(R.id.btn_save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int newTime = Integer.parseInt(editHour.getText().toString()) * 3600
							+ Integer.parseInt(editMinute.getText().toString()) * 60
							+ Integer.parseInt(editSecond.getText().toString());
				if(newTime < 30){
					Toast.makeText(mContext, "心跳周期不能低于30秒,请重新设置!",Toast.LENGTH_SHORT).show();
					return;
				}
				CGIManager.getInstance().setHeartTime(mDevices, newTime);
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

	public void setTitle(String content) {
		textTitle.setText(content);
	}

	public void setName(String type) {
		text_name.setText(type);
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

	public void setDialogCallback(AddDialogcallback dialogcallback) {
		this.mAddDialogcallback = dialogcallback;
	}
}
