package com.gdgl.activity;
/***
 * 修改密码界面
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

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePWDFragment extends Fragment implements UIListener {

	private View mView;

	EditText old_pwd, new_pwd, new_again;
	Button btn_commit;

	String odlPwd, name;
	String oldpwd, newpwd, newagain;
	LoginManager mLoginManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mLoginManager = LoginManager.getInstance();
		mLoginManager.addObserver(ChangePWDFragment.this);
		CallbackManager.getInstance().addObserver(ChangePWDFragment.this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.change_pwd, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub

		getFromSharedPreferences.setsharedPreferences((Context) getActivity());
		name = getFromSharedPreferences.getLoginName();

		old_pwd = (EditText) mView.findViewById(R.id.old_pwd);
		new_pwd = (EditText) mView.findViewById(R.id.new_pwd);
		new_again = (EditText) mView.findViewById(R.id.new_pwd_again);

		btn_commit = (Button) mView.findViewById(R.id.commit);
		btn_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				oldpwd = old_pwd.getText().toString();
				newpwd = new_pwd.getText().toString();
				newagain = new_again.getText().toString();

				if (null == oldpwd || oldpwd.length()<=0) {
					Toast.makeText(getActivity(), "请输入原密码", Toast.LENGTH_SHORT).show();
					old_pwd.requestFocus();
					return;
				} else {
					if (null == newpwd || newpwd.length()<=0) {
						Toast.makeText(getActivity(), "新密码不能为空", Toast.LENGTH_SHORT).show();
						new_pwd.requestFocus();
						return;
					} else if (newpwd.length()>5 && newpwd.length()<17) {
						if (null == newagain || newagain.length()<=0) {
							Toast.makeText(getActivity(), "请再次输入新密码", Toast.LENGTH_SHORT).show();
							new_again.requestFocus();
							return;
						} else if (!newagain.trim().equals(newpwd)) {
							Toast.makeText(getActivity(), "两次输入密码不相符,请重新输入", Toast.LENGTH_SHORT).show();
							new_again.requestFocus();
							return;
						} else {
							AccountInfo account = new AccountInfo();
							account.setAccount(name);
							account.setPassword(oldpwd);
							mLoginManager.ModifyPassword(account, newpwd);
						}
					} else {
						Toast.makeText(getActivity(), "新密码应为6-16字符", Toast.LENGTH_SHORT).show();
						new_pwd.requestFocus();
						return;
					}
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLoginManager.deleteObserver(ChangePWDFragment.this);
		CallbackManager.getInstance().deleteObserver(ChangePWDFragment.this);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub

		final Event event = (Event) object;
		if (EventType.MODIFYPASSWORD == event.getType()) {

			if (event.isSuccess() == true) {
				// data maybe null
//				LoginResponse response=(LoginResponse) event.getData();
//				changePWDSwitch(response);
				int status = (Integer) event.getData();
				changePWDSwitch(status);
			} else {
				// if failed,prompt a Toast
				Toast.makeText(getActivity(), "连接网关失败", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void changePWDSwitch(int status) {
//		int i=Integer.parseInt(response.getResponse_params().getStatus());
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
			break;
		case -29:
			mView.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "原密码错误，请重新输入", Toast.LENGTH_SHORT).show();
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
