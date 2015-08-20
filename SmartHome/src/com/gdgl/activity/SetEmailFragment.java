package com.gdgl.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.LoginManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.AccountInfo;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;

public class SetEmailFragment extends Fragment implements UIListener {
	private View mView;

	EditText email_name;
	Button btn_commit_email;

	String  E_name;
	String id;
	LoginManager mLoginManager;

	RelativeLayout ch_name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mLoginManager = LoginManager.getInstance();
		mLoginManager.addObserver(SetEmailFragment.this);
		CallbackManager.getInstance().addObserver(SetEmailFragment.this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.set_email, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub

		getFromSharedPreferences.setsharedPreferences((Context) getActivity());
		E_name = getFromSharedPreferences.getEmailName();
		email_name = (EditText) mView.findViewById(R.id.email_name);
		email_name.setText(E_name);
		/*if (E_name != null){
			email_name.setText(E_name);
		} else {
			email_name.setHint("XXX@XXXX");
		}*/
		id = getFromSharedPreferences.getGatewayMAC();
		btn_commit_email = (Button) mView.findViewById(R.id.commit_email);
		btn_commit_email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				E_name = email_name.getText().toString();

				if (null == E_name || E_name.length() <= 0) {
					Toast.makeText(getActivity(), "请输入邮箱名", Toast.LENGTH_SHORT)
							.show();
					email_name.requestFocus();
					return;
				} else {
					//TODO 调用loginmanager中设置邮箱
					getFromSharedPreferences
					.setsharedPreferences((Context) getActivity());
					getFromSharedPreferences.setEmailName(E_name.trim());
				}
			}
		});

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLoginManager.deleteObserver(SetEmailFragment.this);
		CallbackManager.getInstance().deleteObserver(SetEmailFragment.this);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub

	
	}

	
}
