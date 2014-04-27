package com.gdgl.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.EditDevicesDlg.EditDialogcallback;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddDlg {
	private Context mContext;
	Dialog dialog;
	Button save;
	Button cancle;
	TextView textView;

	EditText mName;
	LinearLayout devices_region;
	TextView text_name;
	
	AddDialogcallback mAddDialogcallback;
	
	public static final int REGION = 1;
	public static final int SCENE = 2;

	int mType;

	public AddDlg(Context c, int type) {
		mContext = c;
		mType = type;
		dialog = new Dialog(mContext, R.style.MyDialog);
		dialog.setContentView(R.layout.add_dlg);
		textView = (TextView) dialog.findViewById(R.id.txt_title);

		devices_region = (LinearLayout) dialog
				.findViewById(R.id.devices_region);

		mName = (EditText) dialog.findViewById(R.id.edit_name);
		text_name = (TextView) dialog.findViewById(R.id.text_name);

		save = (Button) dialog.findViewById(R.id.btn_save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String mN = mName.getText().toString();
				if (null != mN && !mN.trim().equals("")) {
					save(mN.trim());
				}
				mAddDialogcallback.refreshdata();
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

	protected void save(String mN) {
		// TODO Auto-generated method stub
		List<String> mList = new ArrayList<String>();
		String[] mregions = null;
		getFromSharedPreferences.setharedPreferences(mContext);
		String reg = getFromSharedPreferences.getRegion();
		if (null != reg && !reg.trim().equals("")) {
			mregions = reg.split("@@");
		}
		if (null != mregions) {
			for (String string : mregions) {
				if (!string.equals("")) {
					mList.add(string);
				}
			}
		}
		if (!mList.contains(mN)) {
			mList.add(mN);
		}
		StringBuilder ms = new StringBuilder();
		for (String string : mList) {
			ms.append(string + "@@");
		}
		getFromSharedPreferences.setRegion(ms.toString());
	}

	public void setContent(String content) {
		textView.setText(content);
	}

	public void setType(String type) {
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
	
	public interface AddDialogcallback {
		public void refreshdata();
	}

	public void setDialogCallback(AddDialogcallback dialogcallback) {
		this.mAddDialogcallback = dialogcallback;
	}
	
}
