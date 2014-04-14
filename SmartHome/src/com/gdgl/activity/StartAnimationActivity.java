package com.gdgl.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.widget.ImageView;

import com.gdgl.smarthome.R;

public class StartAnimationActivity extends Activity {
	private AnimationDrawable animationDrawable;
    private final static long SPLASH_DELAY_MILLIS=6000;
	protected void onCreate(android.os.Bundle savedInstanceState) 
	{
		 super.onCreate(savedInstanceState); 
		  setContentView(R.layout.startanimation); 
		 
		  ImageView image = (ImageView) findViewById(R.id.animationimage); 
		  image.setBackgroundResource(R.drawable.startanimation);  
		  animationDrawable = (AnimationDrawable) image.getBackground(); 
		  new Handler().postDelayed(new Runnable() {
	            public void run() {
	                goHome();
	            }
	        }, SPLASH_DELAY_MILLIS);
	}
	private void goHome() {
		Intent intent = new Intent(StartAnimationActivity.this, SmartHome.class);
		StartAnimationActivity.this.startActivity(intent);
		StartAnimationActivity.this.finish();
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		animationDrawable.start();
		super.onWindowFocusChanged(hasFocus);
	}

}
