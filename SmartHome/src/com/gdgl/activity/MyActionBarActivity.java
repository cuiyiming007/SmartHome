package com.gdgl.activity;

import com.gdgl.libjingle.Libjingle;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.reciever.NetWorkChangeReciever;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyApplication;
import com.gdgl.util.MyOKOnlyDlg;
import com.gdgl.util.MyOKOnlyDlg.DialogOutcallback;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public abstract class MyActionBarActivity extends ActionBarActivity implements
		UIListener, DialogOutcallback {

	protected TextView tipsWithoutNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_activity_secondary);
		MyApplication.getInstance().addActivity(this);
		NetWorkChangeReciever.getInstance().addObserver(this);
		CallbackManager.getInstance().addObserver(this);
		Libjingle.getInstance().addObserver(this);
		Log.i("MyActionBarActivity", getClass().getSimpleName());
		tipsWithoutNet = (TextView) findViewById(R.id.checknet);
		setTipText();
	}

	protected void setTipText() {
		if (NetworkConnectivity.networkStatus == 0) {
			tipsWithoutNet.setVisibility(View.VISIBLE);
		} else {
			tipsWithoutNet.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyApplication.getInstance().removeActivity(this);
		NetWorkChangeReciever.getInstance().deleteObserver(this);
		CallbackManager.getInstance().deleteObserver(this);
		Libjingle.getInstance().deleteObserver(this);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		Event event = (Event) object;
		if (EventType.NETWORKCHANGE == event.getType()) {
			Log.i("NETWORKCHANGE", getClass().getSimpleName());
			if (event.isSuccess() == true) {
				tipsWithoutNet.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						setTipText();
					}
				});
			}
		} else if (EventType.MODIFYALIAS == event.getType()) {

			if (event.isSuccess() == true) {
				int status = (Integer) event.getData();
				if (status == 0) {
					tipsWithoutNet.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(
									MyActionBarActivity.this);
							myOKOnlyDlg.setContent("别名已修改，请重新登录！");
							myOKOnlyDlg.setCannotCanceled();
							myOKOnlyDlg
									.setDialogCallback(MyActionBarActivity.this);
							myOKOnlyDlg.show();
						}
					});
				}
			}
		} else if (EventType.MODIFYPASSWORD == event.getType()) {

			if (event.isSuccess() == true) {
				int status = (Integer) event.getData();
				if (status == 0) {
					tipsWithoutNet.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							getFromSharedPreferences
									.setsharedPreferences(MyActionBarActivity.this);
							getFromSharedPreferences.setPwd("");
							MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(
									MyActionBarActivity.this);
							myOKOnlyDlg.setContent("密码已修改，请重新登录！");
							myOKOnlyDlg.setCannotCanceled();
							myOKOnlyDlg
									.setDialogCallback(MyActionBarActivity.this);
							myOKOnlyDlg.show();
						}
					});
				}
			}
		} else if (EventType.GATEWAYUPDATEBEGINE == event.getType()) {
			if (!GatewayUpdateDetailDlgFragment.UpdateSelf) {
				GatewayUpdateDetailDlgFragment gatewayUpdateFragment = new GatewayUpdateDetailDlgFragment(
						false);
				gatewayUpdateFragment.show(getSupportFragmentManager(), "");
			}
		}
	}

	@Override
	public void dialogokdo() {
		// TODO Auto-generated method stub
		MyApplication.getInstance().finishSystem();
	}
}
