package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.activity.SceneDevicesAddFragment.AddChecked;
import com.gdgl.activity.SceneDevicesAddFragment.SceneDevicesAddListAdapter;
import com.gdgl.activity.SceneDevicesFragment;
import com.gdgl.activity.SceneDevicesFragment.ChangeFragment;
import com.gdgl.activity.SceneDevicesFragment.SceneDevicesListAdapter;
import com.gdgl.libjingle.LibjingleSendManager;
import com.gdgl.manager.SceneLinkageManager;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.scene.SceneDevice;
import com.gdgl.mydata.scene.SceneInfo;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyApplicationFragment;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class SceneDevicesActivity extends ActionBarActivity implements
		ChangeFragment, AddChecked, Dialogcallback {
	public static final String TYPE = "type";
	public static final int CREATE = 1;
	public static final int EDIT = 2;

	public static final int EDIT_FRAGMENT = 1;
	public static final int ADD_FRAGMENT = 2;

	// 判断是创建情景还是编辑情景
	int scene_type;
	int fragment_flag;

	private Toolbar mToolbar;
	private ActionBar mActionBar;

	private String sceneName = "";
	private int sceneId;
	private int sceneIndex;

	// 情景中的设备
	List<SceneDevice> mSceneDevicesList;
	// 用于判断编辑时设备是否改变
	List<SceneDevice> mSceneDevicesListBackup;
	// 点击添加后出现的设备表
	List<DevicesModel> mAddList;

	List<SceneDevice> mAddToSceneList = new ArrayList<SceneDevice>();

	DataHelper mDataHelper;

	DevicesListFragment mDevicesListFragment;

	SceneDevicesFragment mAllDevicesFragment;

	SceneDevicesListAdapter mSceneDevicesListAdapter;
	SceneDevicesAddListAdapter mSceneDevicesAddListAdapter;

	EditText titleEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_activity_secondary);
		MyApplicationFragment.getInstance().setActivity(this);

		mDataHelper = new DataHelper(SceneDevicesActivity.this);

		Bundle bundle = getIntent().getExtras();
		Intent i = getIntent();
		scene_type = i.getIntExtra(TYPE, 2);
		if (scene_type == CREATE) {
			sceneIndex = i.getIntExtra(SceneInfo.SCENE_INDEX, 0);
			sceneName = "情景" + (sceneIndex + 1);
			mSceneDevicesList = new ArrayList<SceneDevice>();
		} else {
			SceneInfo sceneInfo = (SceneInfo) i
					.getSerializableExtra(Constants.PASS_OBJECT);
			sceneIndex = sceneInfo.getScnindex();
			sceneName = sceneInfo.getScnname();
			sceneId = sceneInfo.getSid();

			initSceneDevicesList();
		}

		titleEditText = new EditText(this);
		Toolbar.LayoutParams toolbarParams = new Toolbar.LayoutParams(
				Toolbar.LayoutParams.WRAP_CONTENT,
				Toolbar.LayoutParams.WRAP_CONTENT, Gravity.LEFT);
		toolbarParams.setMargins(5, 0, 5, 0);
		titleEditText.setLayoutParams(toolbarParams);
		titleEditText.setTextColor(getResources().getColor(R.color.white));
		titleEditText.setText(sceneName);
		titleEditText.setSelection(sceneName.length());

		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		mToolbar.addView(titleEditText);
		setSupportActionBar(mToolbar);
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(false);

		mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
				case R.id.menu_ok:
					if (fragment_flag == EDIT_FRAGMENT) {
						sceneName = Uri.encode(titleEditText.getText()
								.toString());
						if (scene_type == CREATE) {
							if (mSceneDevicesList.size() < 1) {
								Toast.makeText(SceneDevicesActivity.this,
										"请至少包含一个设备", Toast.LENGTH_SHORT).show();
								break;
							}
							String sceneParams = createSceneParams();
							if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
								SceneLinkageManager.getInstance().AddScene(
										sceneName, sceneParams, sceneIndex);
							} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
								LibjingleSendManager.getInstance().AddScene(
										sceneName, sceneParams, sceneIndex);
							}
							finish();
						}
						if (scene_type == EDIT) {
							if (sceneName.equals(titleEditText.getText()
									.toString())) {
								if (mSceneDevicesList
										.equals(mSceneDevicesListBackup)) {
									finish();
								} else {
									if (mSceneDevicesList.size() < 1) {
										Toast.makeText(
												SceneDevicesActivity.this,
												"请至少包含一个设备", Toast.LENGTH_SHORT)
												.show();
										break;
									}
									String sceneParams = createSceneParams();
									if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
										SceneLinkageManager.getInstance()
												.EditScene(sceneName,
														sceneParams,
														sceneIndex, sceneId);
									} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
										LibjingleSendManager.getInstance()
												.EditScene(sceneName,
														sceneParams,
														sceneIndex, sceneId);
									}

									finish();
								}
							} else {
								sceneName = Uri.encode(titleEditText.getText()
										.toString());
								String sceneParams = createSceneParams();
								if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
									SceneLinkageManager.getInstance()
											.EditScene(sceneName, sceneParams,
													sceneIndex, sceneId);
								} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
									LibjingleSendManager.getInstance()
											.EditScene(sceneName, sceneParams,
													sceneIndex, sceneId);
								}

								finish();
							}
						}
					} else if (fragment_flag == ADD_FRAGMENT) {
						if (mAddToSceneList.size() > 0) {
							mSceneDevicesList.addAll(mAddToSceneList);
							mAddToSceneList.clear();
						}
						MyApplicationFragment.getInstance()
								.removeLastFragment();
						fragment_flag = EDIT_FRAGMENT;
						mSceneDevicesListAdapter.notifyDataSetChanged();
					}
					break;

				default:
					break;
				}
				return false;
			}
		});

		SceneDevicesFragment mfragent = new SceneDevicesFragment();
		mSceneDevicesListAdapter = mfragent.new SceneDevicesListAdapter(
				mSceneDevicesList);
		mfragent.setAdapter(mSceneDevicesListAdapter);
		mfragent.setArguments(bundle);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.container, mfragent);
		fragmentTransaction.commit();
		fragment_flag = EDIT_FRAGMENT;
		MyApplicationFragment.getInstance().addNewTask(mfragent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_ok, menu);
		return true;
	}

	private void initSceneDevicesList() {
		mSceneDevicesList = null;
		SQLiteDatabase db = mDataHelper.getSQLiteDatabase();
		String where = " sid=? ";
		String[] args = { Integer.toString(sceneId) };
		mSceneDevicesList = mDataHelper.queryForSceneDevicesList(db, null,
				where, args, null, null, null, null);
		mSceneDevicesListBackup = new ArrayList<SceneDevice>();
		for (SceneDevice sceneDevice : mSceneDevicesList) {
			SceneDevice newSceneDevice = new SceneDevice();
			newSceneDevice.setActionType(sceneDevice.getActionType());
			newSceneDevice.setDevicesStatus(sceneDevice.getDevicesStatus());
			newSceneDevice.setEp(sceneDevice.getEp());
			newSceneDevice.setIeee(sceneDevice.getIeee());
			newSceneDevice.setSid(sceneDevice.getSid());
			mSceneDevicesListBackup.add(newSceneDevice);
		}
	}

	private void initAddFragmentDevicesList() {
		mAddList = null;
		mAddList = mDataHelper.queryForDevicesList(
				mDataHelper.getSQLiteDatabase(), DataHelper.DEVICES_TABLE,
				null, null, null, null, null, DevicesModel.DEVICE_PRIORITY,
				null);
		if (mSceneDevicesList.size() > 0) {
			for (int i = 0; i < mAddList.size(); i++) {
				DevicesModel mModel = mAddList.get(i);
				if (isInSceneDevicesList(mModel)) {
					mAddList.remove(i);
					i--;
				}
			}
		} else {
			for (int i = 0; i < mAddList.size(); i++) {
				DevicesModel mModel = mAddList.get(i);
				if (mModel.getmModelId().indexOf(DataHelper.Siren) == 0
						|| mModel.getmModelId().indexOf(
								DataHelper.Indoor_temperature_sensor) == 0
						|| mModel.getmModelId().indexOf(
								DataHelper.Smoke_Detectors) == 0) {
					mAddList.remove(i);
					i--;
				}
			}
		}
	}

	private boolean isInSceneDevicesList(DevicesModel mModel) {

		for (SceneDevice mSceneDevice : mSceneDevicesList) {
			if (mModel.getmModelId().indexOf(DataHelper.Siren) == 0
					|| mModel.getmModelId().indexOf(
							DataHelper.Indoor_temperature_sensor) == 0
					|| mModel.getmModelId().indexOf(DataHelper.Smoke_Detectors) == 0) {
				return true;
			}
			if (mSceneDevice.getIeee().equals(mModel.getmIeee())
					&& mSceneDevice.getEp().equals(mModel.getmEP())) {
				return true;
			}
		}
		return false;
	}

	private String createSceneParams() {
		StringBuilder paramStringBuilder = new StringBuilder();
		for (SceneDevice msceneDevice : mSceneDevicesList) {
			paramStringBuilder.append(msceneDevice.creatSceneParam());
			paramStringBuilder.append("@");
		}
		paramStringBuilder.deleteCharAt(paramStringBuilder.length() - 1);
		return paramStringBuilder.toString();
	}

	@Override
	public void AddCheckedDevices(SceneDevice s) {
		// TODO Auto-generated method stub
		// Log.i(SCENE_NAME, "zgs->AddCheckedDevices");
		if (!mAddToSceneList.contains(s)) {
			mAddToSceneList.add(s);
		}
	}

	@Override
	public void DeletedCheckedDevices(SceneDevice s) {
		// TODO Auto-generated method stub
		// Log.i(SCENE_NAME, "zgs->DeletedCheckedDevices");
		if (mAddToSceneList.contains(s)) {
			mAddToSceneList.remove(s);
		}
	}

	@Override
	public void setFragment(Fragment f) {
		initAddFragmentDevicesList();
		SceneDevicesAddFragment mSceneAddFragment = (SceneDevicesAddFragment) f;
		mSceneDevicesAddListAdapter = mSceneAddFragment.new SceneDevicesAddListAdapter(
				mAddList, SceneDevicesActivity.this);
		mSceneAddFragment.setAdapter(mSceneDevicesAddListAdapter);

		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.container, f);
		fragmentTransaction.commit();
		fragment_flag = ADD_FRAGMENT;
		MyApplicationFragment.getInstance().addFragment(f);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (MyApplicationFragment.getInstance().getFragmentListSize() > 1) {
			MyApplicationFragment.getInstance().removeLastFragment();
			fragment_flag = EDIT_FRAGMENT;
			mAddToSceneList.clear();
		} else {
			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(this);
			mMyOkCancleDlg.setDialogCallback((Dialogcallback) this);
			mMyOkCancleDlg.setContent("确定要放弃本次编辑?");
			mMyOkCancleDlg.show();
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		// TODO Auto-generated method stub
		if (MyApplicationFragment.getInstance().getFragmentListSize() > 1) {
			MyApplicationFragment.getInstance().removeLastFragment();
			fragment_flag = EDIT_FRAGMENT;
			mAddToSceneList.clear();
			return super.onSupportNavigateUp();
		}
		MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(this);
		mMyOkCancleDlg.setDialogCallback((Dialogcallback) this);
		mMyOkCancleDlg.setContent("确定要放弃本次编辑?");
		mMyOkCancleDlg.show();
		return super.onSupportNavigateUp();
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		finish();
	}
}
