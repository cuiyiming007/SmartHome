package com.gdgl.activity;

import com.gdgl.manager.LoginManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
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

public class ChangeNameFragment extends Fragment implements UIListener{

	private View mView;

	EditText new_name;
	Button btn_commit;
	TextView error_message;

	String odlPwd,name;
	String newName;
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
		mView = inflater.inflate(R.layout.modify_name, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		
		getFromSharedPreferences.setharedPreferences((Context) getActivity());
		odlPwd = getFromSharedPreferences.getPwd();
		name=getFromSharedPreferences.getName();
		
		new_name = (EditText) mView.findViewById(R.id.new_name);
		new_name.setText(name);
		
		error_message = (TextView) mView.findViewById(R.id.error_message);
		btn_commit = (Button) mView.findViewById(R.id.commit);

		ch_pwd=(RelativeLayout)mView.findViewById(R.id.ch_pwd);
		RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		
		ch_pwd.setLayoutParams(mLayoutParams);
		
		mLoginManager=LoginManager.getInstance();
		mLoginManager.addObserver(ChangeNameFragment.this);
		btn_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				newName = new_name.getText().toString();

				if (null == newName || newName.trim().equals("")) {
					error_message.setText("请输入新别名");
					error_message.setVisibility(View.VISIBLE);
					new_name.requestFocus();
					return;
				} else if (newName.trim().equals(name)) {
					error_message.setText("别名未做任何修改");
					error_message.setVisibility(View.VISIBLE);
					new_name.requestFocus();
					return;
				} 
				error_message.setVisibility(View.INVISIBLE);
				
//				AccountInfo account=new AccountInfo();
//				account.setAccount(name);
//				account.setPassword(odlPwd);
//				mLoginManager.modifyAlias(id, old_alias, new_alias);
			}
		});

	}
	
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLoginManager.deleteObserver(ChangeNameFragment.this);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		
		
		final Event event = (Event) object;
		if (EventType.MODIFYPASSWORD == event.getType()) {
			
			if (event.isSuccess()==true) {
				// data maybe null
				error_message.setVisibility(View.VISIBLE);
				error_message.setText("修改成功");
				
				getFromSharedPreferences.setharedPreferences((Context) getActivity());
				getFromSharedPreferences.setName(newName.trim());
			}else {
				//if failed,prompt a Toast
				error_message.setText("修改失败");
				error_message.setVisibility(View.VISIBLE);
			}
		}
	}

}
