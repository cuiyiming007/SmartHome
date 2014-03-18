package com.gdgl.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private EditText mName;
	private EditText mPwd;
	private CheckBox mRem;
	private CheckBox mAut;
	private Button mLogin;
	private Button mCancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		initView();
		setLieners();
	}

	private void setLieners() {
		// TODO Auto-generated method stub
		mLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(LoginActivity.this, SmartHome.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
//		mName = (EditText) findViewById(R.id.login_edit_account);
//		mPwd = (EditText) findViewById(R.id.login_edit_pwd);
//
//		mRem = (CheckBox) findViewById(R.id.login_cb_savepwd);
//		mRem = (CheckBox) findViewById(R.id.login_cb_savepwd);
//
		mLogin = (Button) findViewById(R.id.login_btn_login);
//		mCancle = (Button) findViewById(R.id.login_btn_cancle);
	}
}
