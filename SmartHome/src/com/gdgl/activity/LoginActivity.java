package com.gdgl.activity;

/***
 * 登录界面
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.gdgl.app.ApplicationController;
import com.gdgl.libjingle.LibjingleResponseHandlerManager;
import com.gdgl.libjingle.LibjingleSendManager;
import com.gdgl.manager.LoginManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.AccountInfo;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.LoginResponse;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.service.LibjingleService;
import com.gdgl.service.SmartService;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;
import com.gdgl.util.NetUtil;
import com.gdgl.util.UiUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener,
		UIListener {
	int networkStatus;// 当前网络状态量
	private EditText mName, mPwd, mCloud;
	private CheckBox mRem, mAut;
	private TextView gaoji_text;
	private Button mLogin;
	private ImageView user_dropdown, cloud_dropdown, gaoji_image;
	private ViewGroup gaoji, gaoji_Layout, user_item, cloud_item;
	private AccountInfo accountInfo;
	private PopupWindow userPop, cloudPop;
	public static AccountInfo loginAccountInfo; // 把用户名密码传到libjingleService
	private static final boolean isAsc = false;

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
		mCloud = (EditText) findViewById(R.id.cloud);
		user_dropdown = (ImageView) findViewById(R.id.user_dropdown);
		cloud_dropdown = (ImageView) findViewById(R.id.cloud_dropdown);
		gaoji_image = (ImageView) findViewById(R.id.gaoji_image);
		gaoji_text = (TextView) findViewById(R.id.gaoji_text);
		gaoji = (ViewGroup) findViewById(R.id.gaoji);
		gaoji_Layout = (ViewGroup) findViewById(R.id.gaoji_layout);
		user_item = (ViewGroup) findViewById(R.id.user_item);
		cloud_item = (ViewGroup) findViewById(R.id.cloud_item);
		mRem = (CheckBox) findViewById(R.id.checkBox1);

		getFromSharedPreferences.setsharedPreferences(LoginActivity.this);
		if (!getFromSharedPreferences.getUid().equals("")) {
			mName.setText(getFromSharedPreferences.getUid());
		} else {
			mName.setText(getFromSharedPreferences.getName());
		}
		mPwd.setText(getFromSharedPreferences.getPwd());
		if (!getFromSharedPreferences.getCloud().equals("")) {
			mCloud.setText(getFromSharedPreferences.getCloud());
		}
		if (getFromSharedPreferences.getIsRemerber()) {
			mRem.setChecked(true);
		}
		// mName.setText("BC6A2987D431");
		// mPwd.setText("123456");
		mLogin.setOnClickListener(this);
		user_dropdown.setOnClickListener(this);
		cloud_dropdown.setOnClickListener(this);
		gaoji.setOnClickListener(this);
		LoginManager.getInstance().addObserver(this);
		LibjingleResponseHandlerManager.getInstance().addObserver(this);
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

			loginAccountInfo = accountInfo;

			if (accountInfo.getAccount() == null
					|| accountInfo.getAccount().length() <= 0) {
				Toast.makeText(getApplicationContext(), "请输入用户名或网关MAC地址",
						Toast.LENGTH_SHORT).show();
			} else if (accountInfo.getAccount().length() > 5
					&& accountInfo.getAccount().length() < 17) {
				if (accountInfo.getPassword().length() > 5
						&& accountInfo.getPassword().length() < 17) {
					startAPPService(accountInfo.getAccount());

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
		case R.id.gaoji:
			if (gaoji_Layout.getVisibility() == View.GONE) {
				gaoji_Layout.setVisibility(View.VISIBLE);
				gaoji_image
						.setBackgroundResource(R.drawable.ui_login_arrow_blue);
				gaoji_text.setTextColor(Color.parseColor("#0086b1"));
			} else {
				gaoji_Layout.setVisibility(View.GONE);
				gaoji_image
						.setBackgroundResource(R.drawable.ui_login_arrow_white);
				gaoji_text.setTextColor(Color.parseColor("#ffffff"));
			}
			break;
		case R.id.user_dropdown:
			if (userPop != null) {
				if (!userPop.isShowing()) {
					userPop.showAsDropDown(user_item);
				} else {
					userPop.dismiss();
				}
			} else {
				if (getFromSharedPreferences.getUserList().size() > 0) {
					initUserPopView();
					if (!userPop.isShowing()) {
						userPop.showAsDropDown(user_item);
					} else {
						userPop.dismiss();
					}
				} else {
					Toast.makeText(this, "没有记录", Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.cloud_dropdown:
			if (cloudPop != null) {
				if (!cloudPop.isShowing()) {
					cloudPop.showAsDropDown(cloud_item);
				} else {
					cloudPop.dismiss();
				}
			} else {
				if (getFromSharedPreferences.getCloudList().size() > 0) {
					initCloudPopView();
					if (!cloudPop.isShowing()) {
						cloudPop.showAsDropDown(cloud_item);
					} else {
						cloudPop.dismiss();
					}
				} else {
					Toast.makeText(this, "没有记录", Toast.LENGTH_LONG).show();
				}
			}
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
			Intent libserviceIntent = new Intent(this, LibjingleService.class);
			getFromSharedPreferences.setsharedPreferences(LoginActivity.this);
			getFromSharedPreferences.setLogin(accountInfo, false, false);
			startService(libserviceIntent);

			break;
		case NetworkConnectivity.LAN:
			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
			String where = " mac=? or alias=? ";
			String[] args = { alias, alias };
			String[] columns = { "ip" };
			Cursor cursor = mSQLiteDatabase.query(DataHelper.GATEWAY_TABLE,
					columns, where, args, null, null, null);

			// .rawQuery("select * from gateway_table where mac = \'883314EF8B2D\' or alias = \'883314EF8B2D\'",null);

			if (cursor.getCount() > 0) {
				// cursor.moveToFirst();
				if (cursor.moveToFirst()) {
					Log.i("cursor",
							cursor.getString(cursor.getColumnIndex("ip")));
					String ip = cursor.getString(cursor.getColumnIndex("ip"));

					NetUtil.getInstance().setGatewayIP(ip);

					Intent serviceIntent = new Intent(this, SmartService.class);
					startService(serviceIntent);
					LoginManager.getInstance().doLogin(accountInfo);
				}
				mSQLiteDatabase.close();
			} else {
				Toast.makeText(getApplicationContext(), "没有网关",
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		LoginManager.getInstance().deleteObserver(this);
		LibjingleResponseHandlerManager.getInstance().deleteObserver(this);
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
		if (EventType.LIBJINGLE_STATUS == event.getType()) {
			if (event.isSuccess() == true) {
				int status = Integer.parseInt((String) event.getData());
				switch (status) {
				case 2:
				case 3:
					new Thread(new Runnable() {

						@Override
						public void run() {
							LibjingleSendManager.getInstance()
									.getDeviceEndPoint();
						}
					}).start();
					
					Intent intent = new Intent(LoginActivity.this, SmartHome.class);
					startActivity(intent);
					this.finish();
					break;
				case -1:
				case -2:
				case -3:
					Toast.makeText(getApplicationContext(), "通道连接网关失败",
							Toast.LENGTH_SHORT).show();	
					break;
				default:
					break;
				}
			}
		}

	}

	private void loginSwitch(LoginResponse response) {
		int i = Integer.parseInt(response.getResponse_params().getStatus());
		switch (i) {
		case 0:
			accountInfo.setId(response.getId());
			getFromSharedPreferences.setsharedPreferences(LoginActivity.this);
			if (mRem.isChecked()) {
				getFromSharedPreferences.setLogin(accountInfo, true, false);
			} else {
				getFromSharedPreferences.setLogin(accountInfo, false, false);
			}
			// getFromSharedPreferences.setLogin(accountInfo, false, false);
			getFromSharedPreferences.setUserList(mName.getText().toString(),
					mPwd.getText().toString());
			getFromSharedPreferences.setCloud(mCloud.getText().toString());
			getFromSharedPreferences.setCloudList(mCloud.getText().toString());
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

	private void initUserPopView() {
		MyAdapter userAdapter = new MyAdapter(
				sequenceList(getFromSharedPreferences.getUserList()),
				R.drawable.ui_login_user_blue, UiUtils.USERLIST);
		ListView listView = new ListView(this);
		listView.setDivider(this.getResources().getDrawable(
				R.drawable.ui_login_list_line));
		listView.setAdapter(userAdapter);
		userPop = new PopupWindow(listView, user_item.getWidth(),
				user_item.getHeight() * 2, true);
		userPop.setFocusable(true);
		userPop.setOutsideTouchable(true);
		userPop.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.ui_login_user_list_bg));
	}

	private void initCloudPopView() {
		MyAdapter cloudAdapter = new MyAdapter(
				sequenceList(getFromSharedPreferences.getCloudList()),
				R.drawable.ui_login_cloud_blue, UiUtils.CLOUDLIST);
		ListView listView = new ListView(this);
		listView.setDivider(this.getResources().getDrawable(
				R.drawable.ui_login_list_line));
		listView.setAdapter(cloudAdapter);
		cloudPop = new PopupWindow(listView, cloud_item.getWidth(),
				user_item.getHeight() * 2, true);
		cloudPop.setFocusable(true);
		cloudPop.setOutsideTouchable(true);
		cloudPop.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.ui_login_user_list_bg));
	}

	public ArrayList<HashMap<String, String>> sequenceList(
			ArrayList<HashMap<String, String>> list) {
		if (isAsc) {

		} else {
			Collections.reverse(list);
		}
		return list;
	}

	class MyAdapter extends BaseAdapter implements Dialogcallback {
		String useCur;
		String pwdCur;
		String cloudCur;
		int posi;
		private MyOkCancleDlg mMyOkCancleDlg;
		private ArrayList<HashMap<String, String>> list;
		private int logoResource;
		private String tag;

		public MyAdapter(ArrayList<HashMap<String, String>> list, int resource,
				String tag) {
			this.list = list;
			this.logoResource = resource;
			this.tag = tag;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(LoginActivity.this).inflate(
						R.layout.login_item, null);
				holder.img = (ImageView) convertView.findViewById(R.id.logo);
				holder.btn = (ImageButton) convertView
						.findViewById(R.id.delete);
				holder.tv = (TextView) convertView.findViewById(R.id.textview);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.img.setBackgroundResource(logoResource);
			if (tag.equals(UiUtils.USERLIST)) {
				final String use = list.get(position).get("use").toString();
				final String pwd = list.get(position).get("pwd").toString();
				holder.tv.setText(use);
				holder.tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mName.setText(use);
						mPwd.setText(pwd);
						userPop.dismiss();
					}
				});
				holder.btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						useCur = use;
						pwdCur = pwd;
						posi = position;
						showOkCancelDialog();

					}
				});
			}
			if (tag.equals(UiUtils.CLOUDLIST)) {
				final String cloud = list.get(position).get("cloud").toString();
				holder.tv.setText(cloud);
				holder.tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mCloud.setText(cloud);
						cloudPop.dismiss();
					}
				});
				holder.btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						cloudCur = cloud;
						posi = position;
						showOkCancelDialog();
					}
				});
			}

			return convertView;
		}

		class ViewHolder {
			private ImageView img;
			private TextView tv;
			private ImageButton btn;
		}

		public void showOkCancelDialog() {
			mMyOkCancleDlg = new MyOkCancleDlg(LoginActivity.this);
			mMyOkCancleDlg.setDialogCallback(MyAdapter.this);
			mMyOkCancleDlg.setContent("确定要删除这笔资料吗?");
			mMyOkCancleDlg.show();
		}

		@Override
		public void dialogdo() {
			// TODO Auto-generated method stub
			if (userPop != null && userPop.isShowing()) {
				Log.i("user", "use = " + useCur + " pwd = " + pwdCur
						+ " posi = " + posi);
				getFromSharedPreferences.removeUserList(useCur, pwdCur);
				list.remove(posi);
				notifyDataSetChanged();
				userPop.dismiss();
			}
			if (cloudPop != null && cloudPop.isShowing()) {
				Log.i("cloud", "cloud = " + cloudCur + " posi = " + posi);
				getFromSharedPreferences.removeCloudList(cloudCur);
				list.remove(posi);
				notifyDataSetChanged();
				cloudPop.dismiss();
				if (mCloud.getText().toString().equals(cloudCur)) {
					if (list.size() > 0) {
						mCloud.setText(list.get(0).get("cloud").toString());
					} else {
						mCloud.setText("");
					}
				}
			}
		}
	}
}
