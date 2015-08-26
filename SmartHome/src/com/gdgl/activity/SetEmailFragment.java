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
import android.widget.Toast;

import com.gdgl.manager.CGIManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOKOnlyDlg;

public class SetEmailFragment extends Fragment implements UIListener {
	private View mView;

	EditText email_name;
	Button btn_commit_email;

	String E_name;
	String gateway_MAC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
		/*
		 * if (E_name != null){ email_name.setText(E_name); } else {
		 * email_name.setHint("XXX@XXXX"); }
		 */
		gateway_MAC = getFromSharedPreferences.getGatewayMAC();
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
					if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
						CGIManager.getInstance().changeEmailAddress(
								gateway_MAC, E_name, 1);
					} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
						MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(getActivity());
						myOKOnlyDlg.setContent(getResources().getString(
								R.string.Unable_In_InternetState));
						myOKOnlyDlg.show();
					}
					getFromSharedPreferences.setEmailName(E_name.trim());
				}
			}
		});

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		CallbackManager.getInstance().deleteObserver(SetEmailFragment.this);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub

	}

}
