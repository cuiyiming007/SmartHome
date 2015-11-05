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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gc.materialdesign.views.CheckBox;
import com.gc.materialdesign.views.CheckBox.OnCheckListener;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;

public class SetEmailFragment extends Fragment implements UIListener {
	private View mView;
	public static boolean ENABLE_EMAIL_TXT = true;
	public static boolean ENABLE__EMAIL_VEDIO = true;
	public static boolean ENABLE_EMAIL_PIC = true;
	private int sendEmailContentFlag = 0;
	private String [] emailData ;
	private int sendEmailFLAG;
	private String emailAddress = "";
	EditText email_name;
	Button btn_commit_email;

	String E_name;
	String gateway_MAC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		CGIManager.getInstance().addObserver(SetEmailFragment.this);
		gateway_MAC = getFromSharedPreferences.getGatewayMAC();
		CGIManager.getInstance().getEmailAddrStatus(gateway_MAC);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.set_email, null);
		initView();
		//CGIManager.getInstance().getEmailAddrStatus(gateway_MAC);
		return mView;
	}
	
	private void initView() {
		// TODO Auto-generated method stub

//		getFromSharedPreferences.setsharedPreferences((Context) getActivity());
		email_name = (EditText) mView.findViewById(R.id.email_name);
		email_name.setText(emailAddress);
		email_name.setSelection(email_name.getText().length());
		/*
		 * if (E_name != null){ email_name.setText(E_name); } else {
		 * email_name.setHint("XXX@XXXX"); }
		 */
		final CheckBox enableEmail = (CheckBox) mView
				.findViewById(R.id.enableEmail);
		final CheckBox enableSendVideo = (CheckBox) mView
				.findViewById(R.id.enableSendVideo);
		final CheckBox enableSendPic = (CheckBox) mView
				.findViewById(R.id.enableSendPic);
		
			if(sendEmailFLAG == 4) {
				enableEmail.setChecked(true);
				enableSendVideo.setChecked(true);
				enableSendPic.setChecked(true);
				ENABLE_EMAIL_TXT = true;
				ENABLE__EMAIL_VEDIO = true; 
				ENABLE_EMAIL_PIC = true;
			}else if(sendEmailFLAG == 3){
				enableEmail.setChecked(true);
				enableSendVideo.setChecked(true);
				enableSendPic.setChecked(false);
				ENABLE_EMAIL_TXT = true;
				ENABLE__EMAIL_VEDIO = true; 
				ENABLE_EMAIL_PIC = false;
			}else if(sendEmailFLAG == 2){
				enableEmail.setChecked(true);
				enableSendVideo.setChecked(false);
				enableSendPic.setChecked(true);
				ENABLE_EMAIL_TXT = true;
				ENABLE__EMAIL_VEDIO = false; 
				ENABLE_EMAIL_PIC = true;
			}else if(sendEmailFLAG == 1){
				enableEmail.setChecked(true);
				enableSendVideo.setChecked(false);
				enableSendPic.setChecked(false);
				ENABLE_EMAIL_TXT = true;
				ENABLE__EMAIL_VEDIO = false; 
				ENABLE_EMAIL_PIC = false;
			}else {
				enableEmail.setChecked(false);
				enableSendVideo.setChecked(false);
				enableSendPic.setChecked(false);
				ENABLE_EMAIL_TXT = false;
				ENABLE__EMAIL_VEDIO = false; 
				ENABLE_EMAIL_PIC = false;
			}
		
		enableEmail.setOncheckListener(new OnCheckListener() {

			@Override
			public void onCheck(boolean check) {
				// TODO Auto-generated method stub
				ENABLE_EMAIL_TXT = check;
				if(!ENABLE_EMAIL_TXT ) {
					enableSendVideo.setChecked(false);
					enableSendPic.setChecked(false);
				}
			}
		});
		enableEmail.post(new Runnable() {
			@Override
			public void run() {
				//enableEmail.setChecked(ENABLE_EMAIL_TXT);	
			}
		});
		
		enableSendVideo.setOncheckListener(new OnCheckListener() {

			@Override
			public void onCheck(boolean check) {
				// TODO Auto-generated method stub
				//ENABLE__EMAIL_VEDIO = check;				
				if (ENABLE_EMAIL_TXT){
					ENABLE__EMAIL_VEDIO = check;
				}else {
					ENABLE__EMAIL_VEDIO = false;
				}
				enableSendVideo.setChecked(ENABLE__EMAIL_VEDIO);
			}
		});
		enableSendVideo.post(new Runnable() {
			@Override
			public void run() {
				//enableSendVideo.setChecked(ENABLE__EMAIL_VEDIO);
			}
		});
		
		enableSendPic.setOncheckListener(new OnCheckListener() {

			@Override
			public void onCheck(boolean check) {
				// TODO Auto-generated method stub
				//ENABLE_EMAIL_PIC = check;
				if (ENABLE_EMAIL_TXT){
					ENABLE_EMAIL_PIC = check;
				}else {
					ENABLE_EMAIL_PIC = false;
				}
				enableSendPic.setChecked(ENABLE_EMAIL_PIC);
			}
		});
		enableSendPic.post(new Runnable() {
			@Override
			public void run() {
				//enableSendPic.setChecked(ENABLE_EMAIL_PIC);
			}
		});
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
				}else if(!isEmail(E_name)){
					Toast.makeText(getActivity(), "邮箱格式错误", Toast.LENGTH_SHORT)
					.show();
					email_name.requestFocus();
					return;
				}else {
					if (ENABLE_EMAIL_TXT  && ENABLE__EMAIL_VEDIO && ENABLE_EMAIL_PIC ){
						sendEmailContentFlag = 4;
					}else if(ENABLE_EMAIL_TXT  && ENABLE__EMAIL_VEDIO && !ENABLE_EMAIL_PIC ){
						sendEmailContentFlag = 3;
					}else if(ENABLE_EMAIL_TXT  && !ENABLE__EMAIL_VEDIO && ENABLE_EMAIL_PIC ){
						sendEmailContentFlag = 2;
					}else if(ENABLE_EMAIL_TXT  && !ENABLE__EMAIL_VEDIO && !ENABLE_EMAIL_PIC ){
						sendEmailContentFlag = 1;
					} else {
						sendEmailContentFlag = 0;
					}
//					getFromSharedPreferences.setEmailEnable(ENABLE_EMAIL_TXT);
//					getFromSharedPreferences.setEmailVideoEnable(ENABLE__EMAIL_VEDIO);
//					getFromSharedPreferences.setEmailPicEnable(ENABLE_EMAIL_PIC);
//					getFromSharedPreferences.setEmailFlag(sendEmailContentFlag);
//					getFromSharedPreferences.setEmailName(emailAddress);
//					getFromSharedPreferences.setEmailName(E_name.trim());
					CGIManager.getInstance().changeEmailAddress(gateway_MAC,
							E_name, sendEmailContentFlag);
				}
			}
		});

	}
	public boolean isEmail(String email) {

		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

		Pattern p = Pattern.compile(str);

		Matcher m = p.matcher(email);

		return m.matches();

		}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		CGIManager.getInstance().deleteObserver(SetEmailFragment.this);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		if (EventType.CHANGEEMAILADDRESS == event.getType()) {
			
			if (event.isSuccess() == true) {
				Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_SHORT)
						.show();
				//MyApplicationFragment.getInstance().removeLastFragment();
			} else {
				// if failed,prompt a Toast
				Toast.makeText(getActivity(), "设置失败，请稍后重试", Toast.LENGTH_SHORT)
						.show();
			}
		}
		if (EventType.GETEMAILADDRESS == event.getType()) {
			if (event.isSuccess() == true) {
				emailData = (String[]) event.getData();
				sendEmailFLAG = Integer.parseInt(emailData [0]);
				emailAddress = emailData [1];
				if(emailAddress == null||emailAddress.equals("")){
					Toast.makeText(getActivity(), "请输入邮箱地址", Toast.LENGTH_SHORT)
					.show();
				}else{
					Toast.makeText(getActivity(), "获取邮箱成功", Toast.LENGTH_SHORT)
					.show();
				}
								
				//MyApplicationFragment.getInstance().removeLastFragment();
			} else {
				// if failed,prompt a Toast
				//sendEmailFLAG = getFromSharedPreferences.getEmailFlag();
				//emailAddress = getFromSharedPreferences.getEmailName();
				sendEmailFLAG = 0;
				emailAddress =	null;	
				Toast.makeText(getActivity(), "获取邮箱失败，请稍后重新获取", Toast.LENGTH_SHORT)
						.show();
			}
			initView();
		}
	}

}
