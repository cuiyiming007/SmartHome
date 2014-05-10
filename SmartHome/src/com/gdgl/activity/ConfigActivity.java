package com.gdgl.activity;

import com.gdgl.smarthome.R;
import com.gdgl.util.SlideMenu;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;


public class ConfigActivity extends BaseSlideMenuActivity {
	private SlideMenu mSlideMenu;
	private boolean isFirst=true;
	int mSelectedColor = 0xffEE3B3B;
	int mUnSelectedColor = 0xff20B2AA;
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		setSlideRole(R.layout.fragment_content);
		setSlideRole(R.layout.config_menu);
		
		
		mSlideMenu=getSlideMenu();
		
		mSlideMenu.setInterpolator(new DecelerateInterpolator());
		mSlideMenu.setSlideMode(SlideMenu.MODE_SLIDE_CONTENT);
		mSlideMenu.setPrimaryShadowWidth(100);
		
		final TextView text_content=(TextView)findViewById(R.id.text_content);
		
		TextView tv=(TextView)findViewById(R.id.modify_pwd);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getSlideMenu().close(true);
				text_content.setText("修改密码");
				TextView t=(TextView)v;
				t.setTextColor(mSelectedColor);
			}
		});
		mHandler.sendEmptyMessageDelayed(1, 150);
	}
	
	Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			int what =msg.what;
			switch (what) {
			case 1:
				mSlideMenu.open(false, true);
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		Log.i(STORAGE_SERVICE, "zgs=>onWindowFocusChanged hasFocus="+hasFocus+" mSlideMenu==null"+(mSlideMenu==null));
		if(hasFocus && isFirst){
			if(null!=mSlideMenu){
				mSlideMenu.open(false, true);
				isFirst=false;
			}
		}
	}
	
}
