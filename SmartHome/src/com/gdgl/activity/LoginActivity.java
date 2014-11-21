package com.gdgl.activity;

/***
 * 登录界面
 */
import com.gdgl.app.ApplicationController;
import com.gdgl.manager.LoginManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.AccountInfo;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.LoginResponse;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.service.SmartService;
import com.gdgl.smarthome.R;
import com.gdgl.util.NetUtil;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener,
		UIListener {

	int networkStatus;// 当前网络状态量

	private EditText mName;
	private EditText mPwd;
	private CheckBox mRem;
	private CheckBox mAut;
	private Button mLogin;
	private AccountInfo accountInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Intent intent = getIntent();
		networkStatus = intent.getIntExtra("networkStatus", 0);
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub

		mLogin = (Button) findViewById(R.id.login);
		mName = (EditText) findViewById(R.id.name);
		mPwd = (EditText) findViewById(R.id.pwd);

		getFromSharedPreferences.setsharedPreferences(LoginActivity.this);
		if (!getFromSharedPreferences.getUid().equals("")) {
			mName.setText(getFromSharedPreferences.getUid());
		} else {
			mName.setText(getFromSharedPreferences.getName());
		}
		mPwd.setText(getFromSharedPreferences.getPwd());

		// mName.setText("BC6A2987D431");
		// mPwd.setText("123456");
		mLogin.setOnClickListener(this);
		LoginManager.getInstance().addObserver(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.login:
			accountInfo = new AccountInfo();
			accountInfo.setAccount(mName.getText().toString());
			accountInfo.setPassword(mPwd.getText().toString());
			accountInfo.setAlias(mName.getText().toString());

			if (accountInfo.getAccount() == null
					|| accountInfo.getAccount().length() <= 0) {
				Toast.makeText(getApplicationContext(), "请输入用户名或网关MAC地址",
						Toast.LENGTH_SHORT).show();
			} else if (accountInfo.getAccount().length() > 5
					&& accountInfo.getAccount().length() < 17) {
				if (accountInfo.getPassword().length() > 5
						&& accountInfo.getPassword().length() < 17) {
					startAPPService(accountInfo.getAccount());
					LoginManager.getInstance().doLogin(accountInfo);
				} else if (accountInfo.getPassword() == null
						|| accountInfo.getPassword().length() <= 0)
					Toast.makeText(getApplicationContext(), "密码不能为空",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getApplicationContext(), "密码应为6-16字符",
							Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "用户名应为6-16字符",
						Toast.LENGTH_SHORT).show();
			}
			// Intent intent = new Intent(LoginActivity.this, SmartHome.class);
			// startActivity(intent);
			// this.finish();
			break;
		default:
			break;
		}
	}

	public void startAPPService(String alias) {
		switch (networkStatus) {
		case NetworkConnectivity.NO_NETWORK:
			break;
		case NetworkConnectivity.INTERNET:
			break;
		case NetworkConnectivity.LAN:
			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
			String where = " mac=? or alias=? ";
			String[] args = { alias, alias };
			String[] columns = { "ip" };
			Cursor cursor = mSQLiteDatabase.query(DataHelper.GATEWAY_TABLE, columns, where, args, null, null, null);
					
//				.rawQuery("select * from gateway_table where mac = \'883314EF8B2D\' or alias = \'883314EF8B2D\'",null);
		
			if (cursor.getCount() > 0) {
//				cursor.moveToFirst();
				if (cursor.moveToFirst()) {
					Log.i("cursor", cursor.getString(cursor.getColumnIndex("ip")));
					String ip = cursor.getString(cursor.getColumnIndex("ip"));
					
					NetUtil.getInstance().setGatewayIP(ip);

					Intent serviceIntent = new Intent(this, SmartService.class);
					startService(serviceIntent);
				}
			} else {
				Toast.makeText(getApplicationContext(), "没有网关",
						Toast.LENGTH_SHORT).show();
			}
			mSQLiteDatabase.close();
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

			if (event.isSuccess() == true) {
				LoginResponse response = (LoginResponse) event.getData();
				loginSwitch(response);
			} else {
				// if failed,prompt a Toast
				Toast.makeText(getApplicationContext(), "连接网关失败",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private void loginSwitch(LoginResponse response) {
		int i = Integer.parseInt(response.getResponse_params().getStatus());
		switch (i) {
		case 0:
			accountInfo.setId(response.getId());
			getFromSharedPreferences.setsharedPreferences(LoginActivity.this);
			getFromSharedPreferences.setLogin(accountInfo, false, false);
			// getFromSharedPreferences.setharedPreferences(this);
			// getFromSharedPreferences.setName(newName.trim());
			Intent intent = new Intent(LoginActivity.this, SmartHome.class);
			startActivity(intent);
			this.finish();
			break;
		case 24:
			Toast.makeText(getApplicationContext(), "用户名不正确",
					Toast.LENGTH_SHORT).show();
			break;
		case 29:
			Toast.makeText(getApplicationContext(), "密码不正确", Toast.LENGTH_SHORT)
					.show();
			break;
		// case 1:
		// case 2:
		// case 3:
		// case 4:
		// case 5:
		// case 6:
		// case 10:
		// case 11:
		// case 12:
		// case 23:
		// case 28:
		// Toast.makeText(getApplicationContext(), "登录失败",
		// Toast.LENGTH_SHORT).show();
		// break;
		case 20:
		case 21:
		case 22:
			Toast.makeText(getApplicationContext(), "用户名应为6-16字符",
					Toast.LENGTH_SHORT).show();
			break;
		case 25:
		case 26:
		case 27:
			Toast.makeText(getApplicationContext(), "密码应为6-16字符",
					Toast.LENGTH_SHORT).show();
			break;
		default:
			Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}

}
