package com.gdgl.util;

import com.gdgl.smarthome.R;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VersionDlg {

	Context context;
	Dialog dialog;
	Button sure;
	TextView companynameText, appnameText, versionText;
	
	public VersionDlg(Context con) {
		this.context = con;
		dialog = new Dialog(context, R.style.MyDialog);
		dialog.setContentView(R.layout.version_dlg);
		
		companynameText=(TextView)dialog.findViewById(R.id.txt_company_name);
		appnameText=(TextView)dialog.findViewById(R.id.txt_app_name);
		versionText=(TextView)dialog.findViewById(R.id.txt_version);
		try {
			PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionText.setText(pi.versionName); 
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
		companynameText.setVisibility(View.GONE);
		appnameText.setVisibility(View.GONE);
		versionText.setText(content);
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
