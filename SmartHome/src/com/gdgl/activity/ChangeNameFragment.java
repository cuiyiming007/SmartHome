package com.gdgl.activity;
/***
 * 修改用户名界面
 */
import com.gdgl.manager.LoginManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.AccountInfo;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.LoginResponse;
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
import android.widget.Toast;

public class ChangeNameFragment extends Fragment implements UIListener{

	private View mView;

	EditText old_name;
	EditText new_name;
	Button btn_commit;

	String odlPwd,name;
	String newName,oldName;
	String id;
	LoginManager mLoginManager;
	
	RelativeLayout ch_name;
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
		
		getFromSharedPreferences.setsharedPreferences((Context) getActivity());
		odlPwd = getFromSharedPreferences.getPwd();
		name=getFromSharedPreferences.getName();
		id=getFromSharedPreferences.getUid();
		new_name = (EditText) mView.findViewById(R.id.new_name);
		
		old_name = (EditText) mView.findViewById(R.id.old_name);
		old_name.setText(name);
		
		btn_commit = (Button) mView.findViewById(R.id.commit);

		ch_name=(RelativeLayout)mView.findViewById(R.id.ch_name);
		RelativeLayout.LayoutParams mLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		
		ch_name.setLayoutParams(mLayoutParams);
		
		mLoginManager=LoginManager.getInstance();
		mLoginManager.addObserver(ChangeNameFragment.this);
		btn_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				oldName = old_name.getText().toString();
				newName = new_name.getText().toString();
				
				if (null == oldName || oldName.length()<=0) {
					Toast.makeText(getActivity(), "请输入旧用户名", Toast.LENGTH_SHORT).show();
					old_name.requestFocus();
					return;
				} else if (oldName.length()>5 && oldName.length()<17) {
					
					if (null == newName || newName.length()<=0) {
						Toast.makeText(getActivity(), "请输入新用户名", Toast.LENGTH_SHORT).show();
						new_name.requestFocus();
						return;
					} else if (newName.length()>5 && newName.length()<17) {
						AccountInfo account=new AccountInfo();
						account.setAccount(oldName);
						account.setPassword(odlPwd);
						account.setId(id);
						account.setAlias(oldName);
						mLoginManager.modifyAlias(account, newName);
					} else {
						Toast.makeText(getActivity(), "新用户名应为6-16字符", Toast.LENGTH_SHORT).show();
						new_name.requestFocus();
						return;
					}
				} else {
					Toast.makeText(getActivity(), "旧用户名应为6-16字符", Toast.LENGTH_SHORT).show();
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
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		
		
		final Event event = (Event) object;
		if (EventType.MODIFYALIAS == event.getType()) {
			
			if (event.isSuccess()==true) {
				// data maybe null
				LoginResponse response=(LoginResponse) event.getData();
				changeNameSwitch(response);
			}else {
				//if failed,prompt a Toast
				Toast.makeText(getActivity(), "连接网关失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void changeNameSwitch(LoginResponse response) {
		int i=Integer.parseInt(response.getResponse_params().getStatus());
		switch (i) {
		case 0:
			Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
			getFromSharedPreferences.setsharedPreferences((Context) getActivity());
			getFromSharedPreferences.setName(newName.trim());
			break;
		case 39:
			Toast.makeText(getActivity(), "原用户名错误，请重新输入", Toast.LENGTH_SHORT).show();
			break;
		case 44:
			Toast.makeText(getActivity(), "用户名未做任何修改", Toast.LENGTH_SHORT).show();
			break;
		default:
			Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
