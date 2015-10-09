package com.gdgl.drawer;

import com.gdgl.activity.GatewayUpdateDetailDlgFragment;
import com.gdgl.activity.MyActionBarActivity;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.RfCGIManager;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.EditDevicesDlg;
import com.gdgl.util.EditDevicesDlg.EditDialogcallback;
import com.gdgl.util.MyOKOnlyDlg;
import com.gdgl.util.MyOKOnlyDlg.DialogOutcallback;
import com.gdgl.util.MyApplication;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyUpdateGatewayDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class DeviceControlActivity extends MyActionBarActivity implements
		EditDialogcallback, Dialogcallback, DialogOutcallback, UIListener {

	public static boolean GATEWAYUPDATE = false;
	public static boolean GATEWAYUPDATE_FIRSTTIME = false;

	private Toolbar mToolbar;
	private ActionBar mActionBar;
	private DevicesModel mDevicesModel;
	private ImageView downLoadView;
	//private String modelID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CallbackManager.getInstance().addObserver(this);
		Bundle mBundle = getIntent().getExtras();
		String name = "";
		if (null != mBundle) {
			mDevicesModel = (DevicesModel) mBundle
					.getSerializable(Constants.PASS_OBJECT);
			name = mDevicesModel.getmDefaultDeviceName();
			//modelID = mDevicesModel.getmModelId();
			//Log.i("modelID", modelID);
		}

		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

		if (mDevicesModel.getmModelId().indexOf(DataHelper.One_key_operator) == 0
				&& GATEWAYUPDATE == true) {
			downLoadView = new ImageView(this);
			Toolbar.LayoutParams toolbarParams = new Toolbar.LayoutParams(
					(int) getResources().getDisplayMetrics().density * 56,
					(int) getResources().getDisplayMetrics().density * 56,
					Gravity.RIGHT);
			downLoadView.setLayoutParams(toolbarParams);
			downLoadView.setAdjustViewBounds(true);
			downLoadView.setImageResource(R.drawable.ui2_menu_gateway_update);
			downLoadView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
						MyUpdateGatewayDlg updateGatewayDlg = new MyUpdateGatewayDlg(
								DeviceControlActivity.this,
								getSupportFragmentManager());
						updateGatewayDlg.setContent("发现网关有新的可用固件,是否升级？");
						updateGatewayDlg.show();
					} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
						MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(
								DeviceControlActivity.this);
						myOKOnlyDlg.setContent(getResources().getString(
								R.string.Unable_In_InternetState));
						myOKOnlyDlg.show();
					}
				}
			});
			downLoadView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					Drawable mDrawable = getResources().getDrawable(
							R.drawable.ui2_menu_gateway_update);
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						mDrawable.setColorFilter(Color.parseColor("#33dddddd"),
								Mode.DST_ATOP);
						downLoadView.setBackground(mDrawable);
						return false;
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_MOVE:
					case MotionEvent.ACTION_UP:
						mDrawable.clearColorFilter();
						downLoadView.setBackground(mDrawable);
						return false;
					}
					return true;
				}
			});
			mToolbar.addView(downLoadView);
			// mToolbar.removeView(downLoadView);
		}

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
						MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(
								DeviceControlActivity.this);
						myOKOnlyDlg.setContent(getResources().getString(
								R.string.Unable_In_InternetState));
						myOKOnlyDlg.show();
					}
					break;
				case R.id.menu_document:	
					MyOKOnlyDlg myOKOnlyDlg1 = new MyOKOnlyDlg(
							DeviceControlActivity.this);
					/*myOKOnlyDlg1.setContent(getResources().getString(
							R.string.RF_Magnetic_Door));*/
					myOKOnlyDlg1.setContent(deviceDocument());
					myOKOnlyDlg1.show();
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
						MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(
								DeviceControlActivity.this);
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

		if (mDevicesModel.getmModelId().indexOf(DataHelper.One_key_operator) == 0) {
			if (DeviceControlActivity.GATEWAYUPDATE == true
					&& DeviceControlActivity.GATEWAYUPDATE_FIRSTTIME == true) {
				MyUpdateGatewayDlg updateGatewayDlg = new MyUpdateGatewayDlg(
						this, getSupportFragmentManager());
				updateGatewayDlg.setContent("发现网关有新的可用固件,是否升级？");
				updateGatewayDlg.show();
			}
		}
	}
	
	private String deviceDocument(){
		if (mDevicesModel.getmModelId().indexOf(DataHelper.RF_Magnetic_Door) == 0){
			return getResources().getString(R.string.RF_Magnetic_Door);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.RF_Magnetic_Door_Roll) == 0){
			return getResources().getString(R.string.RF_Magnetic_Door_Roll);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.RF_Emergency_Button) == 0){
			return getResources().getString(R.string.RF_Emergency_Button);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.RF_Infrared_Motion_Sensor) == 0){
			return getResources().getString(R.string.RF_Infrared_Motion_Sensor);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.RF_Smoke_Detectors) == 0){
			return getResources().getString(R.string.RF_Smoke_Detectors);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.RF_Combustible_Gas_Detector) == 0){
			return getResources().getString(R.string.RF_Combustible_Gas_Detector);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.RF_Siren) == 0){
			return getResources().getString(R.string.RF_Siren);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.RF_Siren_Relay) == 0){
			return getResources().getString(R.string.RF_Siren_Relay);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.RF_Siren_Outside) == 0){
			return getResources().getString(R.string.RF_Siren_Outside);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.RF_remote_control) == 0){
			return getResources().getString(R.string.RF_remote_control);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.One_key_operator) == 0){
			return getResources().getString(R.string.gateWay);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.Siren) == 0){
			return getResources().getString(R.string.ZB_Siren);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.Magnetic_Window) == 0){
			return getResources().getString(R.string.ZB_Magnetic_Window);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.Wireless_Intelligent_valve_switch) == 0){
			return getResources().getString(R.string.ZB_Wireless_Intelligent_valve_switch);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.Power_detect_socket) == 0){
			return getResources().getString(R.string.ZB_Power_detect_socket);
		}else if(mDevicesModel.getmModelId().indexOf(DataHelper.Indoor_temperature_sensor) == 0){
			return getResources().getString(R.string.ZB_Indoor_temperature_sensor);
		}else {
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.menu_devicecontrol, menu); // 广电服务器控制，不允许删除设备。
		getMenuInflater().inflate(R.menu.menu_devicecontrol_withoutdelete, menu);
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
		if (mDevicesModel.getmDeviceId() < 0) {
			RfCGIManager.getInstance().ChangeRFDevName(
					mDevicesModel.getmIeee(), Uri.encode(name));
		} else {
			CGIManager.getInstance().ChangeDeviceName(mDevicesModel,
					Uri.encode(name));
		}

	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		CGIManager.getInstance().deleteNode(mDevicesModel.getmIeee());
	}

	@Override
	public void dialogokdo() {
		// TODO Auto-generated method stub
		MyApplication.getInstance().finishSystem();
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
		} else if (EventType.NETWORKCHANGE == event.getType()) {
			Log.i("NETWORKCHANGE", getClass().getSimpleName());
			if (event.isSuccess() == true) {
				mToolbar.post(new Runnable() {

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
					mToolbar.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(
									DeviceControlActivity.this);
							myOKOnlyDlg.setContent("别名已修改，请重新登录！");
							myOKOnlyDlg.setCannotCanceled();
							myOKOnlyDlg
									.setDialogCallback(DeviceControlActivity.this);
							myOKOnlyDlg.show();
						}
					});
				}
			}
		} else if (EventType.MODIFYPASSWORD == event.getType()) {

			if (event.isSuccess() == true) {
				int status = (Integer) event.getData();
				if (status == 0) {
					mToolbar.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							getFromSharedPreferences
									.setsharedPreferences(DeviceControlActivity.this);
							getFromSharedPreferences.setPwd("");
							MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(
									DeviceControlActivity.this);
							myOKOnlyDlg.setContent("密码已修改，请重新登录！");
							myOKOnlyDlg.setCannotCanceled();
							myOKOnlyDlg
									.setDialogCallback(DeviceControlActivity.this);
							myOKOnlyDlg.show();
						}
					});
				}
			}
		} else if (EventType.INITGATEWAY == event.getType()) {

			if (event.isSuccess() == true) {
				int status = (Integer) event.getData();
				if (status == 1) {
					mToolbar.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(
									DeviceControlActivity.this);
							myOKOnlyDlg.setContent("别名、密码已初始化，请重新登录！");
							myOKOnlyDlg.setCannotCanceled();
							myOKOnlyDlg
									.setDialogCallback(DeviceControlActivity.this);
							myOKOnlyDlg.show();
						}
					});
				} else if (status == 2) {
					mToolbar.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(
									DeviceControlActivity.this);
							myOKOnlyDlg.setContent("网关已恢复出厂设置，请重新登录！");
							myOKOnlyDlg.setCannotCanceled();
							myOKOnlyDlg
									.setDialogCallback(DeviceControlActivity.this);
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
		} else if (EventType.GATEWAYUPDATECOMPLETE == event.getType()) {
			if (event.isSuccess()) {
				mToolbar.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mToolbar.removeView(downLoadView);
					}
				}, 500);
			}
		}  else if (EventType.GATEWAYAUTH == event.getType()) {
			if (event.isSuccess() == true) {
				String[] data = (String[]) event.getData();
				int number = Integer.parseInt(data[0]);
				if(number == 1) {
					final String expire = data[1];
					tipsWithoutNet.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							MyOKOnlyDlg myOKOnlyDlg1 = new MyOKOnlyDlg(DeviceControlActivity.this);
							myOKOnlyDlg1.setContent("网关授权于 " + expire + " 到期，请及时续费以保证正常使用！");
							myOKOnlyDlg1.show();
						}
					});
				} else if(number == 2) {
					tipsWithoutNet.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							MyOKOnlyDlg myOKOnlyDlg2 = new MyOKOnlyDlg(DeviceControlActivity.this);
							myOKOnlyDlg2.setContent("网关未授权，不能正常使用！");
							myOKOnlyDlg2.setCannotCanceled();
							myOKOnlyDlg2.setDialogCallback(DeviceControlActivity.this);
							myOKOnlyDlg2.setSystemAlert();
							myOKOnlyDlg2.show();
						}
					});
				}
			}
		}
	}
}
