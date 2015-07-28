package com.gdgl.activity;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gdgl.drawer.DeviceControlActivity;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyApplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GatewayUpdateDetailDlgFragment extends DialogFragment implements
		UIListener {
	public static boolean UpdateSelf = false;

	private View mView;

	private LinearLayout layout1;
	private TextView currentVersion, latestVersion, updatingText;
	private Button updateButton;
	private ProgressBarCircularIndeterminate progressBar;
	private LinearLayout layout2;
	private TextView updateStatus, updateVersion, rebootText;
	private Button okButton;

	private boolean reboot = false;

	public GatewayUpdateDetailDlgFragment(boolean updateself) {
		UpdateSelf = updateself;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		CallbackManager.getInstance().addObserver(this);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		mView = inflater.inflate(R.layout.gateway_update_detail_fragment, null);
		getFromSharedPreferences.setsharedPreferences(getActivity());
		initView();
		if (!UpdateSelf) {
			updateButton.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			updatingText.setVisibility(View.VISIBLE);
		}
		builder.setView(mView);
		return builder.create();
	}

	private void initView() {
		// TODO Auto-generated method stub
		layout1 = (LinearLayout) mView.findViewById(R.id.layout1);
		currentVersion = (TextView) mView.findViewById(R.id.currentVersion);
		latestVersion = (TextView) mView.findViewById(R.id.latestVersion);
		updateButton = (Button) mView.findViewById(R.id.updateBtn);
		progressBar = (ProgressBarCircularIndeterminate) mView
				.findViewById(R.id.progressBar);
		updatingText = (TextView) mView.findViewById(R.id.updatingText);

		currentVersion.setText("当前版本："
				+ getFromSharedPreferences.getGatewaycurrentVersion());
		latestVersion.setText("可升级版本："
				+ getFromSharedPreferences.getGatewaylatestVersion());
		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CGIManager.getInstance().gatewayDoUpdate();
				updateButton.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				updatingText.setVisibility(View.VISIBLE);
			}
		});

		layout2 = (LinearLayout) mView.findViewById(R.id.layout2);
		updateStatus = (TextView) mView.findViewById(R.id.update_status);
		updateVersion = (TextView) mView.findViewById(R.id.version_now);
		okButton = (Button) mView.findViewById(R.id.ok_btn);
		rebootText = (TextView) mView.findViewById(R.id.rebootText);

		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (reboot) {
					dismiss();
					MyApplication.getInstance().finishSystem();
				} else {
					dismiss();
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		CallbackManager.getInstance().deleteObserver(this);
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		Event event = (Event) object;
		if (EventType.GATEWAYUPDATECOMPLETE == event.getType()) {
			if (event.isSuccess()) {
				String[] data = (String[]) event.getData();
				int rebootdata = Integer.parseInt(data[0]);
				final String version = data[1];
				reboot = rebootdata == 1 ? true : false;
				mView.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						layout1.setVisibility(View.GONE);
						layout2.setVisibility(View.VISIBLE);
						updateStatus.setText("升级成功！");
						updateVersion.setText("已升级到最新版本：" + version);
						if (reboot) {
							rebootText.setVisibility(View.VISIBLE);
						} else {
							rebootText.setVisibility(View.GONE);
						}
					}
				});
				getFromSharedPreferences.setGatewaycurrentVersion(version);
				DeviceControlActivity.GATEWAYUPDATE = false;
				DeviceControlActivity.GATEWAYUPDATE_FIRSTTIME = false;
			} else {
				mView.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						layout1.setVisibility(View.GONE);
						layout2.setVisibility(View.VISIBLE);
						updateStatus.setText("升级失败...");
						updateVersion.setText("");
					}
				});
			}
		}
	}
}
