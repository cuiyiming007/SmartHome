package com.gdgl.util;

import com.gdgl.service.LibjingleService;
import com.gdgl.smarthome.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MyOkCancleDlg {

	Context context;
	Dialogcallback dialogcallback;
	Dialog dialog;
	Button sure;
	Button cancle;
	TextView textView;

	public MyOkCancleDlg(Context con) {
		this.context = con;
		dialog = new Dialog(context, R.style.MyDialog);
		dialog.setContentView(R.layout.ok_cancle_dlg);
		textView = (TextView) dialog.findViewById(R.id.txt_title);
		sure = (Button) dialog.findViewById(R.id.btn_ok);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
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
