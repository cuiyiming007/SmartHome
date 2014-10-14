package com.gdgl.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.widget.ImageView;

import com.gdgl.service.SmartService;
import com.gdgl.smarthome.R;

public class StartAnimationActivity extends Activity {
	private AnimationDrawable animationDrawable;
	ImageView image;
	private Intent serviceIntent;
	private final static long SPLASH_DELAY_MILLIS = 6000;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startanimation);

		image = (ImageView) findViewById(R.id.animationimage);
		image.setBackgroundResource(R.drawable.startanimation);
		animationDrawable = (AnimationDrawable) image.getBackground();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				animationDrawable.stop();
				animationDrawable = null; // key point of resolving out of
											// memory这句话为解决内存溢出的关键，开始没有添加
				goLogin();
			}
		}, SPLASH_DELAY_MILLIS);

	}

	@Override
	protected void onStart() {
		super.onStart();
		serviceIntent = new Intent(this, SmartService.class);
		startService(serviceIntent);

	}

	private void goLogin() {
		Intent intent = new Intent(StartAnimationActivity.this,
				LoginActivity.class);
		StartAnimationActivity.this.startActivity(intent);
		StartAnimationActivity.this.finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (animationDrawable != null) {
			animationDrawable.start();
		}
		super.onWindowFocusChanged(hasFocus);
	}

}
