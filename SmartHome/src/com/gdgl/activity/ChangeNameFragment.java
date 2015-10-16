package com.gdgl.activity;

/***
 * 修改用户名界面
 */
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.LoginManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.AccountInfo;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyDlg;

import android.support.v4.app.Fragment;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChangeNameFragment extends Fragment implements UIListener {

	private View mView;

	EditText old_name;
	EditText new_name;
	EditText password;
	Button btn_commit;

	String passWord, name;
	String newName, oldName;
	String id;
	LoginManager mLoginManager;

	RelativeLayout ch_name;
	
	Dialog waitingDlg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mLoginManager = LoginManager.getInstance();
		mLoginManager.addObserver(ChangeNameFragment.this);
		CallbackManager.getInstance().addObserver(ChangeNameFragment.this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.modify_name, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub

		getFromSharedPreferences.setsharedPreferences((Context) getActivity());
		name = getFromSharedPreferences.getAliasName();
		id = getFromSharedPreferences.getGatewayMAC();
		new_name = (EditText) mView.findViewById(R.id.new_name);
		password = (EditText) mView.findViewById(R.id.password);
		
		old_name = (EditText) mView.findViewById(R.id.old_name);
		old_name.setText(name);

		btn_commit = (Button) mView.findViewById(R.id.commit);
		btn_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				oldName = old_name.getText().toString();
				newName = new_name.getText().toString();
				passWord = password.getText().toString();
				boolean decide = false;
				if (null == oldName || oldName.length() <= 0) {
					Toast.makeText(getActivity(), "请输入旧别名", Toast.LENGTH_SHORT)
							.show();
					old_name.requestFocus();
					return;
				} else if (oldName.length() > 5 && oldName.length() < 17) {

					if (null == newName || newName.length() <= 0) {
						Toast.makeText(getActivity(), "请输入新别名",
								Toast.LENGTH_SHORT).show();
						new_name.requestFocus();
						return;
					} else if (newName.length() > 5 && newName.length() < 17) {
						
						for(int i= 0;i<newName.length();i++){
							if('a'<= newName.charAt(i) &&newName.charAt(i)<='z'||'A'<=newName.charAt(i)&&newName.charAt(i)<='Z'
									||'0'<=newName.charAt(i)&&newName.charAt(i)<='9'){
								decide = true;
							}else {
								decide = false;
								break;
							}
						}
						if(decide){
							if (null == passWord || passWord.length() <= 0) {
								Toast.makeText(getActivity(), "请输入密码",
										Toast.LENGTH_SHORT).show();
								password.requestFocus();
								return;
							} else if (passWord.length() > 5 && passWord.length() < 17) {
								AccountInfo account = new AccountInfo();
								account.setAccount(oldName);
								account.setPassword(passWord);
								account.setId(id);
								account.setAlias(oldName);
								mLoginManager.modifyAlias(account, newName);
								if(waitingDlg != null) {
									waitingDlg.show();
								} else {
									waitingDlg = MyDlg.createLoadingDialog(getActivity(), "提交中，请稍后...");
								}
							} else {
								Toast.makeText(getActivity(), "密码应为6-16字符",
										Toast.LENGTH_SHORT).show();
								password.requestFocus();
								return;
							}
						}else {
							Toast.makeText(getActivity(), "别名只能是数字和字母",
									Toast.LENGTH_SHORT).show();
							new_name.requestFocus();
							return;
						}
					} else {
						Toast.makeText(getActivity(), "新别名应为6-16字符",
								Toast.LENGTH_SHORT).show();
						new_name.requestFocus();
						return;
					}
				} else {
					Toast.makeText(getActivity(), "旧别名应为6-16字符",
							Toast.LENGTH_SHORT).show();
					old_name.requestFocus();
					return;
				}
			}
		});

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLoginManager.deleteObserver(ChangeNameFragment.this);
		CallbackManager.getInstance().deleteObserver(ChangeNameFragment.this);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub

		final Event event = (Event) object;
		if (EventType.MODIFYALIAS == event.getType()) {
			waitingDlg.dismiss();
			if (event.isSuccess() == true) {
				// data maybe null
//				LoginResponse response = (LoginResponse) event.getData();
//				changeNameSwitch(response);
				int status = (Integer)event.getData();
				changeNameSwitch(status);
			} else {
				// if failed,prompt a Toast
				Toast.makeText(getActivity(), "连接网关失败", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private void changeNameSwitch(int status) {
//		int i = Integer.parseInt(response.getResponse_params().getStatus());
		switch (status) {
		case 0:
//			mView.post(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					getActivity().finish();
//				}
//			});
			getFromSharedPreferences
					.setsharedPreferences((Context) getActivity());
			getFromSharedPreferences.setAliasName(newName.trim());
			break;
		case -39:
			mView.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "原别名错误，请重新输入", Toast.LENGTH_SHORT).show();
				}
			});
			break;
		case -44:
			mView.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "别名未做任何修改", Toast.LENGTH_SHORT).show();
				}
			});
			break;
		case -49:
			mView.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "别名已存在，请重新输入", Toast.LENGTH_SHORT).show();
				}
			});
			break;
		default:
			mView.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
				}
			});
			break;
		}
	}
}
