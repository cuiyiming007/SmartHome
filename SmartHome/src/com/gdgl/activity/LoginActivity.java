package com.gdgl.activity;

import com.gdgl.manager.LoginManager;
import com.gdgl.mydata.AccountInfo;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {

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
	
	}

	private void initView() {
		// TODO Auto-generated method stub

		mLogin = (Button) findViewById(R.id.login);
		mName=(EditText) findViewById(R.id.name);
		mPwd=(EditText) findViewById(R.id.pwd);
		
		mName.setText("BC6A2987D431");
		mPwd.setText("123456");
		mLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.login:
			AccountInfo account=new AccountInfo();
			account.setAccount(mName.getText().toString());
			account.setPassword(mPwd.getText().toString());
			LoginManager.getInstance().doLogin(account);
			getFromSharedPreferences.setharedPreferences(LoginActivity.this);
			getFromSharedPreferences.setLogin(mName.getText().toString(), mPwd.getText().toString(), false, false);
			Intent intent = new Intent(LoginActivity.this, SmartHome.class);
			startActivity(intent);
			this.finish();
			break;
		default:
			break;
		}
	}
}
