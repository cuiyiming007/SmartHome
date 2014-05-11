package com.gdgl.activity;

import com.gdgl.manager.LoginManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.AccountInfo;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChangePWDFragment extends Fragment implements UIListener {

	private View mView;

	EditText old_pwd, new_pwd, new_again;
	Button btn_commit;
	TextView error_message;

	String odlPwd, name;
	String oldpwd, newpwd, newagain;
	LoginManager mLoginManager;

	RelativeLayout ch_pwd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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

		getFromSharedPreferences.setharedPreferences((Context) getActivity());
		odlPwd = getFromSharedPreferences.getPwd();
		name = getFromSharedPreferences.getName();

		old_pwd = (EditText) mView.findViewById(R.id.old_pwd);
		new_pwd = (EditText) mView.findViewById(R.id.new_pwd);
		new_again = (EditText) mView.findViewById(R.id.new_pwd_again);

		ch_pwd = (RelativeLayout) mView.findViewById(R.id.ch_pwd);
		RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		ch_pwd.setLayoutParams(mLayoutParams);

		error_message = (TextView) mView.findViewById(R.id.error_message);
		btn_commit = (Button) mView.findViewById(R.id.commit);

		mLoginManager = LoginManager.getInstance();
		mLoginManager.addObserver(ChangePWDFragment.this);
		btn_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				oldpwd = old_pwd.getText().toString();
				newpwd = new_pwd.getText().toString();
				newagain = new_again.getText().toString();

				if (null == oldpwd || oldpwd.trim().equals("")) {
					error_message.setText("请输入原密码");
					error_message.setVisibility(View.VISIBLE);
					old_pwd.requestFocus();
					return;
				} else if (!oldpwd.trim().equals(odlPwd)) {
					error_message.setText("原密码有误");
					error_message.setVisibility(View.VISIBLE);
					old_pwd.requestFocus();
					return;
				} else if (null == newpwd || newpwd.trim().equals("")) {
					error_message.setText("新密码不能为空");
					error_message.setVisibility(View.VISIBLE);
					new_pwd.requestFocus();
					return;
				} else if (null == newagain || newagain.trim().equals("")) {
					error_message.setText("请确认新密码");
					error_message.setVisibility(View.VISIBLE);
					new_again.requestFocus();
					return;
				} else if (!newagain.trim().equals(newpwd)) {
					error_message.setText("两次输入不相符,请确认");
					error_message.setVisibility(View.VISIBLE);
					new_again.requestFocus();
					return;
				}

				error_message.setVisibility(View.INVISIBLE);

				AccountInfo account = new AccountInfo();
				account.setAccount(name);
				account.setPassword(odlPwd);
				mLoginManager.ModifyPassword(account, newpwd);

			}
		});

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLoginManager.deleteObserver(ChangePWDFragment.this);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub

		final Event event = (Event) object;
		if (EventType.MODIFYPASSWORD == event.getType()) {

			if (event.isSuccess() == true) {
				// data maybe null
				error_message.setVisibility(View.VISIBLE);
				error_message.setText("修改成功");

				getFromSharedPreferences
						.setharedPreferences((Context) getActivity());
				getFromSharedPreferences.setPwd(newpwd.trim());
			} else {
				// if failed,prompt a Toast
				error_message.setText("修改失败");
				error_message.setVisibility(View.VISIBLE);
			}
		}
	}

}
