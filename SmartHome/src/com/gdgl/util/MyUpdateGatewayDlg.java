package com.gdgl.util;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gdgl.activity.GatewayUpdateDetailDlgFragment;
import com.gdgl.drawer.DeviceControlActivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

public class MyUpdateGatewayDlg {
	Context context;
	Dialogcallback dialogcallback;
	Dialog dialog;
	Button sure;
	Button cancle;
	TextView textView;
	FragmentManager fManager;
	
	public MyUpdateGatewayDlg(Context con, FragmentManager fragmentManager) {
		this.context = con;
		fManager = fragmentManager;
		dialog = new Dialog(context, R.style.MyDialog);
		dialog.setContentView(R.layout.ok_cancle_dlg);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		textView = (TextView) dialog.findViewById(R.id.txt_title);
		sure = (Button) dialog.findViewById(R.id.btn_ok);
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				GatewayUpdateDetailDlgFragment gatewayUpdateFragment = new GatewayUpdateDetailDlgFragment();
				gatewayUpdateFragment.show(fManager, "");
				
			}
		});

		cancle = (Button) dialog.findViewById(R.id.btn_cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DeviceControlActivity.GATEWAYUPDATE_FIRSTTIME = false;
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
