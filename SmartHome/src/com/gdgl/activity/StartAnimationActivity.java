package com.gdgl.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.widget.ImageView;

import com.gdgl.service.SmartService;
import com.gdgl.smarthome.R;

public class StartAnimationActivity extends Activity {
	private AnimationDrawable animationDrawable;
	ImageView image;
	private Intent serviceIntent;
    private final static long SPLASH_DELAY_MILLIS=4000;
	protected void onCreate(android.os.Bundle savedInstanceState) 
	{
		 super.onCreate(savedInstanceState); 
		  setContentView(R.layout.startanimation); 
		 
		   image = (ImageView) findViewById(R.id.animationimage); 
		  image.setBackgroundResource(R.drawable.startanimation);  
		  animationDrawable = (AnimationDrawable) image.getBackground(); 
		  new Handler().postDelayed(new Runnable() {
	            public void run() {
	                goHome();
	            }
	        }, SPLASH_DELAY_MILLIS);
	}
	@Override
	protected void onStart() {
		super.onStart();
		serviceIntent = new Intent(this, SmartService.class);
		startService(serviceIntent);
		
	}
	private void goHome() {
		Intent intent = new Intent(StartAnimationActivity.this, SmartHome.class);
		StartAnimationActivity.this.startActivity(intent);
		StartAnimationActivity.this.finish();
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (animationDrawable!=null) {
			animationDrawable.start();
		}
		
		super.onWindowFocusChanged(hasFocus);
	}
	@Override
	public void finish() {
		((AnimationDrawable)(image.getBackground())).stop();
		image.setBackgroundDrawable(null);
		animationDrawable=null;
		super.finish();
	}

}
