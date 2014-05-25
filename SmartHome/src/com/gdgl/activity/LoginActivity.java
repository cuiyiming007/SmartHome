package com.gdgl.activity;

import com.gdgl.manager.LoginManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.AccountInfo;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.LoginResponse;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener ,UIListener{

	private EditText mName;
	private EditText mPwd;
	private CheckBox mRem;
	private CheckBox mAut;
	private Button mLogin;
	private Button mCancle;
	private AccountInfo accountInfo;

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
		LoginManager.getInstance().addObserver(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.login:
			accountInfo=new AccountInfo();
			accountInfo.setAccount(mName.getText().toString());
			accountInfo.setPassword(mPwd.getText().toString());
			accountInfo.setAlias(mName.getText().toString());
			LoginManager.getInstance().doLogin(accountInfo);
			
			Intent intent = new Intent(LoginActivity.this, SmartHome.class);
			startActivity(intent);
			this.finish();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		LoginManager.getInstance().deleteObserver(this);
		super.onDestroy();
	}

	@Override
	public void update(Manger observer, Object object) {
		final Event event = (Event) object;
		if (EventType.LOGIN == event.getType()) {
			
			if (event.isSuccess()==true) {
				LoginResponse response=(LoginResponse) event.getData();
				accountInfo.setId(response.getId());
				getFromSharedPreferences.setharedPreferences(LoginActivity.this);
				getFromSharedPreferences.setLogin(accountInfo, false, false);
//				getFromSharedPreferences.setharedPreferences(this);
//				getFromSharedPreferences.setName(newName.trim());
			}else {
				//if failed,prompt a Toast
//				error_message.setText("修改失败");
//				error_message.setVisibility(View.VISIBLE);
			}
		}
		
	}
	
}
