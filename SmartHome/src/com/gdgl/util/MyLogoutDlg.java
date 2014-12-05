package com.gdgl.util;

import com.gdgl.smarthome.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyLogoutDlg {

	Context context;
	Dialogcallback dialogcallback;
	DialogCheckBoxcallback dialogcheckboxcallback;
	Dialog dialog;
	Button sure;
	Button cancle;
	TextView textView;
	CheckBox checkBox;

	public MyLogoutDlg(Context con) {
		this.context = con;
		dialog = new Dialog(context, R.style.MyDialog);
		dialog.setContentView(R.layout.logout_dlg);
		textView = (TextView) dialog.findViewById(R.id.txt_title);
		checkBox = (CheckBox) dialog.findViewById(R.id.logout_checkbox);
		sure = (Button) dialog.findViewById(R.id.btn_ok);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogcheckboxcallback.dialogcheck(checkBox.isChecked());
				dialogcallback.dialogdo();
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

	public interface Dialogcallback {
		public void dialogdo();
	}

	public void setDialogCallback(Dialogcallback dialogcallback) {
		this.dialogcallback = dialogcallback;
	}
	
	public interface DialogCheckBoxcallback {
		public void dialogcheck(boolean isChecked);
	}

	public void setDialogCheckBoxCallback(DialogCheckBoxcallback dialogcheckboxcallback) {
		this.dialogcheckboxcallback = dialogcheckboxcallback;
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
