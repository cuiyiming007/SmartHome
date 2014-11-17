package com.gdgl.util;

import com.gdgl.smarthome.R;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class VersionDlg {

	Context context;
	Dialog dialog;
	Button sure;
	TextView textView;
	
	public VersionDlg(Context con) {
		this.context = con;
		dialog = new Dialog(context, R.style.MyDialog);
		dialog.setContentView(R.layout.version_dlg);
		
		textView=(TextView)dialog.findViewById(R.id.txt_title);
		try {
			PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			textView.setText(pi.versionName); 
		} catch (Exception e) {
			// TODO: handle exception
		}
		sure = (Button) dialog.findViewById(R.id.btn_ok);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

			}
		});
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
