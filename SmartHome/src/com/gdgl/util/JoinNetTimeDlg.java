package com.gdgl.util;

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

public class JoinNetTimeDlg {
	private Context mContext;
	Dialog dialog;
	Button save;
	Button cancle;
	TextView textTitle;
	EditText setTime;
	TextView text_name;
	
	AddDialogcallback mAddDialogcallback;
	
	public JoinNetTimeDlg(Context c) {
		mContext = c;
		dialog = new Dialog(mContext, R.style.MyDialog);
		dialog.setContentView(R.layout.add_dlg);
		textTitle = (TextView) dialog.findViewById(R.id.txt_title);
		setTime = (EditText) dialog.findViewById(R.id.edit_name);
		setTime.setHint("请输入时间(60~250)");
		text_name = (TextView) dialog.findViewById(R.id.text_name);
		save = (Button) dialog.findViewById(R.id.btn_save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String time = setTime.getText().toString();
				int t=Integer.valueOf(time);
				if(t>59&&t<251) {
					getFromSharedPreferences.setsharedPreferences(mContext);
					getFromSharedPreferences.setJoinNetTime(time);
					
					mAddDialogcallback.refreshdata();
				} else {
					Toast.makeText(mContext, "请输入60~250", Toast.LENGTH_SHORT).show();
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
