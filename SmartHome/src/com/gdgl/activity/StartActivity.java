package com.gdgl.activity;



import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.UtilString;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends Activity {
	
//	private String mName="";
//	private String mPwd="";
	private boolean mIsRem=false;
	private boolean mIsAuto=false;
	private getFromSharedPreferences mGetFromSharedPreferences=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		
		initParem();
		
		startLoginAvtivity();
	}

	private void initParem() {
		// TODO Auto-generated method stub
		mGetFromSharedPreferences=new getFromSharedPreferences(getApplicationContext());
		
		mIsRem=mGetFromSharedPreferences.getIsRemerber();
		mIsAuto=mGetFromSharedPreferences.getIsAutoLoging();
	}
	
	private Intent getNextIntent()
	{
		Intent intent = null;
		
		if(mIsAuto)
		{
			intent=new Intent(UtilString.AUTO_LOGIN_ACT);
			
			//intent.setClass(StartActivity.this, LoginActivity.class);
		}
		else if(mIsRem)
		{
			intent=new Intent(UtilString.REM_PWD_ACT);
			intent.setClass(StartActivity.this, LoginActivity.class);
		}
		else
		{
			intent=new Intent(UtilString.N_REM_PWD_ACT);
			intent.setClass(StartActivity.this, LoginActivity.class);
		}
		return intent;
	}
	
	private void startLoginAvtivity() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent =getNextIntent();
				startActivity(intent);
				StartActivity.this.finish();
			}
		}, 1000);
	}
}
