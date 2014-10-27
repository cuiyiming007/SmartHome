package com.gdgl.util;

import com.gdgl.activity.ConfigActivity;
import com.gdgl.activity.ShowDevicesGroupFragmentActivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class SelectPicPopupWindow extends PopupWindow implements Dialogcallback{

	private Button btn_cancel;
	private View mMenuView;
	
	private MyOkCancleDlg mMyOkCancleDlg;
	
	public SelectPicPopupWindow(final Activity context,
			OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.settings, null);

		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mMyOkCancleDlg = new MyOkCancleDlg(context);
		        mMyOkCancleDlg.setDialogCallback(SelectPicPopupWindow.this);
                mMyOkCancleDlg.setContent("确定要退出系统吗?");
                mMyOkCancleDlg.show();
			}
		});
		
		LinearLayout config=(LinearLayout)mMenuView.findViewById(R.id.config);
		config.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent();
				i.setClass(context, ConfigActivity.class);
				context.startActivity(i);
				dismiss();
			}
		});
		
		
		LinearLayout version=(LinearLayout)mMenuView.findViewById(R.id.version);
		version.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				VersionDlg vd=new VersionDlg(context);
				vd.show();
				dismiss();
			}
		});
		
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.mystyle);
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		System.exit(0);
	}

}
