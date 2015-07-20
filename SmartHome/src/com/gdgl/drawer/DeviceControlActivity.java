package com.gdgl.drawer;

import com.gdgl.manager.CGIManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.EditDevicesDlg;
import com.gdgl.util.EditDevicesDlg.EditDialogcallback;
import com.gdgl.util.MyOKOnlyDlg;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;

public class DeviceControlActivity extends ActionBarActivity implements
		EditDialogcallback, Dialogcallback, UIListener {
	private Toolbar mToolbar;
	private ActionBar mActionBar;
	private DevicesModel mDevicesModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_activity_secondary);
		CallbackManager.getInstance().addObserver(this);
		Bundle mBundle = getIntent().getExtras();
		String name = "";
		if (null != mBundle) {
			mDevicesModel = (DevicesModel) mBundle
					.getSerializable(Constants.PASS_OBJECT);
			name = mDevicesModel.getmDefaultDeviceName();
		}

		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		mActionBar = getSupportActionBar();
		// mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBar.setTitle(name);

		mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
				case R.id.menu_changename:
					if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
						EditDevicesDlg mEditDevicesDlg = new EditDevicesDlg(
								DeviceControlActivity.this, mDevicesModel);
						mEditDevicesDlg
								.setDialogCallback(DeviceControlActivity.this);

						mEditDevicesDlg.setContent("编辑"
								+ mDevicesModel.getmDefaultDeviceName().trim());
						mEditDevicesDlg.show();
					} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
						MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(DeviceControlActivity.this);
						myOKOnlyDlg.setContent(getResources().getString(
								R.string.Unable_In_InternetState));
						myOKOnlyDlg.show();
					}
					break;
				case R.id.menu_deletedevice:
					if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
						MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
								DeviceControlActivity.this);
						mMyOkCancleDlg
								.setDialogCallback((Dialogcallback) DeviceControlActivity.this);
						mMyOkCancleDlg.setContent("确定要删除" + " "
								+ mDevicesModel.getmDefaultDeviceName().trim()
								+ " " + "吗?");
						mMyOkCancleDlg.show();
					} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
						MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(DeviceControlActivity.this);
						myOKOnlyDlg.setContent(getResources().getString(
								R.string.Unable_In_InternetState));
						myOKOnlyDlg.show();
					}
					break;
				default:
					break;
				}
				return false;
			}
		});

		Fragment mfragent = new DeviceControlFragment();
		mfragent.setArguments(mBundle);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();

		fragmentTransaction.replace(R.id.container, mfragent);
		fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_devicecontrol, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onSupportNavigateUp() {
		// TODO Auto-generated method stub
		finish();
		return super.onSupportNavigateUp();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		CallbackManager.getInstance().deleteObserver(this);
	}
	
	@Override
	public void saveedit(DevicesModel mDevicesModel, String name) {
		// TODO Auto-generated method stub
		CGIManager.getInstance().ChangeDeviceName(mDevicesModel,
				Uri.encode(name));
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		CGIManager.getInstance().deleteNode(mDevicesModel.getmIeee());
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		Event event = (Event) object;
		if (EventType.CHANGEDEVICENAME == event.getType()) {
			if (event.isSuccess()) {
				String[] changeName = (String[]) event.getData();
				final String name = changeName[2];
				mDevicesModel.setmDefaultDeviceName(name);
				mToolbar.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mActionBar.setTitle(name);
					}
				});
			}
		} else if (EventType.DELETENODE == event.getType()) {
			if (event.isSuccess()) {
				String delete_ieee = (String) event.getData();
				if (mDevicesModel.getmIeee().equals(delete_ieee)) {
					mToolbar.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							finish();
						}
					});
				}

			}
		}
	}
}
