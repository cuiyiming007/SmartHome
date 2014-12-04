package com.gdgl.activity;

import java.util.List;

import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.manager.WarnManager;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.smarthome.R;
import com.gdgl.util.EditDevicesDlg;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.UiUtils;
import com.gdgl.util.EditDevicesDlg.EditDialogcallback;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * 设置菜单，设备列表
 * 
 * @author Trice
 * 
 */
public class ConfigDevicesListWithGroup extends BaseFragment implements
		Dialogcallback, UIListener, EditDialogcallback {

	private IntoDeviceDetailFragment inToDeviceDetailFragment;
	public static final int FINISH_GETDATA = 1;
	boolean isFinishGetView = false;
	boolean isFinishGetData = false;
	boolean hasSetData = false;

	List<DevicesModel> mLightManager;
	List<DevicesModel> mElecManager;
	List<DevicesModel> mSecurityControl;
	List<DevicesModel> mEneronmentControl;
	List<DevicesModel> mEnergy;
	List<DevicesModel> mOther;

	List<DevicesModel> mCurrentList;
	int currentpostion;
	int expan_postion = 0;

	View mView;

	CustomeAdapter LightManagerAdap, ElecManagerAdap, SecurityControlAdap,
			EneronmentControllAdap, EnergyAdap, OtherAdap;

	RelativeLayout LightManagerlay, ElecManagerlay, SecurityControllay,
			EneronmentControlllay, Energylay, Otherlay;
	ImageView up_down_LightManager, up_down_ElecManager,
			up_down_SecurityControl, up_down_EneronmentControll,
			up_down_Energy, up_down_Other;
	LinearLayout LightManager_content, ElecManager_content,
			SecurityControl_content, EneronmentControll_content,
			Energy_content, Other_content;

	TextView no_LightManager, no_ElecManager, no_SecurityControl,
			no_EneronmentControll, no_Energy, no_Other;
	ListView LightManager_list, ElecManager_list, SecurityControl_list,
			EneronmentControll_list, Energy_list, Other_list;

	boolean LightManager_expn = false;
	boolean ElecManager_expn = false;
	boolean SecurityControl_expn = false;
	boolean EneronmentControll_expn = false;
	boolean Energy_expn = false;
	boolean Other_expn = false;

	DataHelper mDh;

	CGIManager temptureManager;
	DeviceManager mDeviceManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub

		temptureManager = CGIManager.getInstance();
		temptureManager.addObserver(this);

		mDeviceManager = DeviceManager.getInstance();
		mDeviceManager.addObserver(this);

		mDh = new DataHelper((Context) getActivity());
		new getDataInBackgroundTask().execute(1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.all_devices_list_withgroup, null);
		initview();
		return mView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		temptureManager.deleteObserver(this);
		mDeviceManager.deleteObserver(this);
	}

	private void initview() {
		// TODO Auto-generated method stub
		LightManagerlay = (RelativeLayout) mView
				.findViewById(R.id.light_manager);
		ElecManagerlay = (RelativeLayout) mView
				.findViewById(R.id.electrical_manager);
		SecurityControllay = (RelativeLayout) mView
				.findViewById(R.id.security_control);
		EneronmentControlllay = (RelativeLayout) mView
				.findViewById(R.id.environmrnt_control);
		Energylay = (RelativeLayout) mView.findViewById(R.id.energy);
		Otherlay = (RelativeLayout) mView.findViewById(R.id.other);

		up_down_LightManager = (ImageView) mView
				.findViewById(R.id.up_down_light_manager);
		up_down_ElecManager = (ImageView) mView
				.findViewById(R.id.up_down_electrical_manager);
		up_down_SecurityControl = (ImageView) mView
				.findViewById(R.id.up_down_security_control);
		up_down_EneronmentControll = (ImageView) mView
				.findViewById(R.id.up_down_environmrnt_control);
		up_down_Energy = (ImageView) mView.findViewById(R.id.up_down_energy);
		up_down_Other = (ImageView) mView.findViewById(R.id.up_down_other);

		LightManager_content = (LinearLayout) mView
				.findViewById(R.id.light_manager_content);
		ElecManager_content = (LinearLayout) mView
				.findViewById(R.id.electrical_manager_content);
		SecurityControl_content = (LinearLayout) mView
				.findViewById(R.id.security_control_content);
		EneronmentControll_content = (LinearLayout) mView
				.findViewById(R.id.environmrnt_control_content);
		Energy_content = (LinearLayout) mView.findViewById(R.id.energy_content);
		Other_content = (LinearLayout) mView.findViewById(R.id.other_content);

		no_LightManager = (TextView) mView.findViewById(R.id.no_light_manager);
		no_ElecManager = (TextView) mView
				.findViewById(R.id.no_electrical_manager);
		no_SecurityControl = (TextView) mView
				.findViewById(R.id.no_security_control);
		no_EneronmentControll = (TextView) mView
				.findViewById(R.id.no_environmrnt_control);
		no_Energy = (TextView) mView.findViewById(R.id.no_energy);
		no_Other = (TextView) mView.findViewById(R.id.no_other);

		LightManager_list = (ListView) mView
				.findViewById(R.id.light_manager_list);
		ElecManager_list = (ListView) mView
				.findViewById(R.id.electrical_manager_list);
		SecurityControl_list = (ListView) mView
				.findViewById(R.id.security_control_list);
		EneronmentControll_list = (ListView) mView
				.findViewById(R.id.environmrnt_control_list);
		Energy_list = (ListView) mView.findViewById(R.id.energy_list);
		Other_list = (ListView) mView.findViewById(R.id.other_list);

		isFinishGetView = true;
		if (isFinishGetData && !hasSetData) {
			expnLightManager();
			hasSetData = true;
		}

		setListeners();

		registerForContextMenu(LightManager_list);
		registerForContextMenu(ElecManager_list);
		registerForContextMenu(SecurityControl_list);
		registerForContextMenu(EneronmentControll_list);
		registerForContextMenu(Energy_list);
		registerForContextMenu(Other_list);

	}

	private void setListeners() {
		// 设备分类点击监听
		LightManagerlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (LightManager_expn) {
					hideLightManager();
				} else {
					hideall();
					expnLightManager();
				}
			}
		});
		ElecManagerlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ElecManager_expn) {
					hideElecManager();
				} else {
					hideall();
					expnElecManager();
				}
			}
		});
		SecurityControllay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (SecurityControl_expn) {
					hideSecurityControl();
				} else {
					hideall();
					expnSecurityControl();
				}
			}
		});
		EneronmentControlllay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (EneronmentControll_expn) {
					hideEneronmentControll();
				} else {
					hideall();
					expnEneronmentControll();
				}
			}
		});
		Energylay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Energy_expn) {
					hideEnergy();
				} else {
					hideall();
					expnEnergy();
				}
			}
		});
		Otherlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Other_expn) {
					hideOther();
				} else {
					hideall();
					expnOther();
				}
			}
		});

		// 设备详情点击监听
		LightManager_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DevicesModel mDevicesModel = (DevicesModel) list
						.getItemAtPosition(position);
				Fragment mFragment = new DeviceDtailFragment();
				Bundle extras = new Bundle();
				extras.putSerializable(Constants.PASS_OBJECT, mDevicesModel);
				extras.putInt(Constants.PASS_DEVICE_ABOUT,
						DeviceDtailFragment.WITH_DEVICE_ABOUT);
				mFragment.setArguments(extras);
				inToDeviceDetailFragment = (IntoDeviceDetailFragment) getActivity();
				inToDeviceDetailFragment.intoDeviceDetailFragment(mFragment);
			}
		});
		ElecManager_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DevicesModel mDevicesModel = (DevicesModel) list
						.getItemAtPosition(position);
				Fragment mFragment = new DeviceDtailFragment();
				Bundle extras = new Bundle();
				extras.putSerializable(Constants.PASS_OBJECT, mDevicesModel);
				extras.putInt(Constants.PASS_DEVICE_ABOUT,
						DeviceDtailFragment.WITH_DEVICE_ABOUT);
				mFragment.setArguments(extras);
				inToDeviceDetailFragment = (IntoDeviceDetailFragment) getActivity();
				inToDeviceDetailFragment.intoDeviceDetailFragment(mFragment);
			}
		});
		SecurityControl_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DevicesModel mDevicesModel = (DevicesModel) list
						.getItemAtPosition(position);
				Fragment mFragment = new DeviceDtailFragment();
				Bundle extras = new Bundle();
				extras.putSerializable(Constants.PASS_OBJECT, mDevicesModel);
				extras.putInt(Constants.PASS_DEVICE_ABOUT,
						DeviceDtailFragment.WITH_DEVICE_ABOUT);
				mFragment.setArguments(extras);
				inToDeviceDetailFragment = (IntoDeviceDetailFragment) getActivity();
				inToDeviceDetailFragment.intoDeviceDetailFragment(mFragment);
			}
		});
		EneronmentControll_list
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> list, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						DevicesModel mDevicesModel = (DevicesModel) list
								.getItemAtPosition(position);
						Fragment mFragment = new DeviceDtailFragment();
						Bundle extras = new Bundle();
						extras.putSerializable(Constants.PASS_OBJECT,
								mDevicesModel);
						extras.putInt(Constants.PASS_DEVICE_ABOUT,
								DeviceDtailFragment.WITH_DEVICE_ABOUT);
						mFragment.setArguments(extras);
						inToDeviceDetailFragment = (IntoDeviceDetailFragment) getActivity();
						inToDeviceDetailFragment
								.intoDeviceDetailFragment(mFragment);
					}
				});
		Energy_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DevicesModel mDevicesModel = (DevicesModel) list
						.getItemAtPosition(position);
				Fragment mFragment = new DeviceDtailFragment();
				Bundle extras = new Bundle();
				extras.putSerializable(Constants.PASS_OBJECT, mDevicesModel);
				extras.putInt(Constants.PASS_DEVICE_ABOUT,
						DeviceDtailFragment.WITH_DEVICE_ABOUT);
				mFragment.setArguments(extras);
				inToDeviceDetailFragment = (IntoDeviceDetailFragment) getActivity();
				inToDeviceDetailFragment.intoDeviceDetailFragment(mFragment);
			}
		});
		Other_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				DevicesModel mDevicesModel = (DevicesModel) list
						.getItemAtPosition(position);
				Fragment mFragment = new DeviceDtailFragment();
				Bundle extras = new Bundle();
				extras.putSerializable(Constants.PASS_OBJECT, mDevicesModel);
				extras.putInt(Constants.PASS_DEVICE_ABOUT,
						DeviceDtailFragment.WITH_DEVICE_ABOUT);
				mFragment.setArguments(extras);
				inToDeviceDetailFragment = (IntoDeviceDetailFragment) getActivity();
				inToDeviceDetailFragment.intoDeviceDetailFragment(mFragment);
			}
		});
	}

	protected void expnOther() {
		// TODO Auto-generated method stub
		Other_expn = true;
		up_down_Other.setImageResource(R.drawable.ui_arrow_up_img);
		Other_content.setVisibility(View.VISIBLE);
		if (null != mOther && mOther.size() > 0) {
			no_Other.setVisibility(View.GONE);
			Other_list.setVisibility(View.VISIBLE);
			OtherAdap.setList(mOther);
			Other_list.setAdapter(OtherAdap);
			OtherAdap.notifyDataSetChanged();
			mCurrentList = mOther;
			expan_postion = 5;
			return;
		}
		no_Other.setVisibility(View.VISIBLE);
		Other_list.setVisibility(View.GONE);
	}

	protected void expnEnergy() {
		// TODO Auto-generated method stub
		Energy_expn = true;
		up_down_Energy.setImageResource(R.drawable.ui_arrow_up_img);
		Energy_content.setVisibility(View.VISIBLE);
		if (null != mEnergy && mEnergy.size() > 0) {
			no_Energy.setVisibility(View.GONE);
			Energy_list.setVisibility(View.VISIBLE);
			EnergyAdap.setList(mEnergy);
			Energy_list.setAdapter(EnergyAdap);
			EnergyAdap.notifyDataSetChanged();
			mCurrentList = mEnergy;
			expan_postion = 4;
			return;
		}
		no_Energy.setVisibility(View.VISIBLE);
		Energy_list.setVisibility(View.GONE);
	}

	protected void expnEneronmentControll() {
		// TODO Auto-generated method stub
		EneronmentControll_expn = true;
		up_down_EneronmentControll.setImageResource(R.drawable.ui_arrow_up_img);
		EneronmentControll_content.setVisibility(View.VISIBLE);
		if (null != mEneronmentControl && mEneronmentControl.size() > 0) {
			no_EneronmentControll.setVisibility(View.GONE);
			EneronmentControll_list.setVisibility(View.VISIBLE);
			EneronmentControllAdap.setList(mEneronmentControl);
			EneronmentControll_list.setAdapter(EneronmentControllAdap);
			EneronmentControllAdap.notifyDataSetChanged();
			mCurrentList = mEneronmentControl;
			expan_postion = 3;
			return;
		}
		no_EneronmentControll.setVisibility(View.VISIBLE);
		EneronmentControll_list.setVisibility(View.GONE);
	}

	private void hideall() {
		if (LightManager_expn) {
			hideLightManager();
		}
		if (ElecManager_expn) {
			hideElecManager();
		}
		if (SecurityControl_expn) {
			hideSecurityControl();
		}
		if (Energy_expn) {
			hideEnergy();
		}
		if (EneronmentControll_expn) {
			hideEneronmentControll();
		}
		if (Other_expn) {
			hideOther();
		}
	}

	private void hideOther() {
		// TODO Auto-generated method stub
		Other_expn = false;
		up_down_Other.setImageResource(R.drawable.ui_arrow_down_img);
		Other_content.setVisibility(View.GONE);
	}

	private void hideEneronmentControll() {
		// TODO Auto-generated method stub
		EneronmentControll_expn = false;
		up_down_EneronmentControll.setImageResource(R.drawable.ui_arrow_down_img);
		EneronmentControll_content.setVisibility(View.GONE);
	}

	private void hideEnergy() {
		// TODO Auto-generated method stub
		Energy_expn = false;
		up_down_Energy.setImageResource(R.drawable.ui_arrow_down_img);
		Energy_content.setVisibility(View.GONE);
	}

	private void hideLightManager() {
		// TODO Auto-generated method stub
		LightManager_expn = false;
		up_down_LightManager.setImageResource(R.drawable.ui_arrow_down_img);
		LightManager_content.setVisibility(View.GONE);
	}

	private void hideElecManager() {
		// TODO Auto-generated method stub
		ElecManager_expn = false;
		up_down_ElecManager.setImageResource(R.drawable.ui_arrow_down_img);

		ElecManager_content.setVisibility(View.GONE);
	}

	private void hideSecurityControl() {
		// TODO Auto-generated method stub
		SecurityControl_expn = false;
		up_down_SecurityControl.setImageResource(R.drawable.ui_arrow_down_img);
		SecurityControl_content.setVisibility(View.GONE);
	}

	private void expnLightManager() {
		// TODO Auto-generated method stub
		LightManager_expn = true;
		up_down_LightManager.setImageResource(R.drawable.ui_arrow_up_img);
		LightManager_content.setVisibility(View.VISIBLE);
		if (null != mLightManager && mLightManager.size() > 0) {
			no_LightManager.setVisibility(View.GONE);
			LightManager_list.setVisibility(View.VISIBLE);
			LightManagerAdap.setList(mLightManager);
			LightManager_list.setAdapter(LightManagerAdap);
			LightManagerAdap.notifyDataSetChanged();
			mCurrentList = mLightManager;
			expan_postion = 0;
			return;
		}
		no_LightManager.setVisibility(View.VISIBLE);
		LightManager_list.setVisibility(View.GONE);

	}

	private void expnElecManager() {
		// TODO Auto-generated method stub
		ElecManager_expn = true;
		up_down_ElecManager.setImageResource(R.drawable.ui_arrow_up_img);
		ElecManager_content.setVisibility(View.VISIBLE);
		if (null != mElecManager && mElecManager.size() > 0) {
			no_ElecManager.setVisibility(View.GONE);
			ElecManager_list.setVisibility(View.VISIBLE);
			ElecManagerAdap.setList(mElecManager);
			ElecManager_list.setAdapter(ElecManagerAdap);
			ElecManagerAdap.notifyDataSetChanged();
			mCurrentList = mElecManager;
			expan_postion = 1;
			return;
		}
		no_ElecManager.setVisibility(View.VISIBLE);
		ElecManager_list.setVisibility(View.GONE);
	}

	private void expnSecurityControl() {
		// TODO Auto-generated method stub
		SecurityControl_expn = true;
		up_down_SecurityControl.setImageResource(R.drawable.ui_arrow_up_img);
		SecurityControl_content.setVisibility(View.VISIBLE);
		if (null != mSecurityControl && mSecurityControl.size() > 0) {
			no_SecurityControl.setVisibility(View.GONE);
			SecurityControl_list.setVisibility(View.VISIBLE);
			SecurityControlAdap.setList(mSecurityControl);
			SecurityControl_list.setAdapter(SecurityControlAdap);
			SecurityControlAdap.notifyDataSetChanged();
			mCurrentList = mSecurityControl;
			expan_postion = 2;
			return;
		}
		no_SecurityControl.setVisibility(View.VISIBLE);
		SecurityControl_list.setVisibility(View.GONE);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.setHeaderTitle("编辑&删除");
		menu.add(0, 1, 0, "编辑");
		menu.add(0, 2, 0, "删除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		currentpostion = position;
		DevicesModel mDevicesModel = mCurrentList.get(position);
		int menuIndex = item.getItemId();

		if (1 == menuIndex) {
			EditDevicesDlg mEditDevicesDlg = new EditDevicesDlg(
					(Context) getActivity(), mDevicesModel);
			mEditDevicesDlg.setDialogCallback(this);

			mEditDevicesDlg.setContent("编辑"
					+ mDevicesModel.getmDefaultDeviceName().trim());
			mEditDevicesDlg.show();
		}
		if (2 == menuIndex) {
			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
					(Context) getActivity());
			mMyOkCancleDlg.setDialogCallback((Dialogcallback) this);
			mMyOkCancleDlg.setContent("确定要删除"
					+ mDevicesModel.getmDefaultDeviceName().trim() + "吗?");
			mMyOkCancleDlg.show();
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.i("onResume", "onResume");

		super.onResume();
	}

	private class CustomeAdapter extends BaseAdapter {

		private Context mContext;
		private List<DevicesModel> mList;

		public CustomeAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mList) {
				return mList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mList) {
				return mList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mList) {
				return mList.get(position).getID();
			}
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder mViewHolder;
			DevicesModel ds = (DevicesModel) getItem(position);

			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.devices_list_item_config, null);
				mViewHolder.devImg = (ImageView) convertView
						.findViewById(R.id.devices_img);
				mViewHolder.devName = (TextView) convertView
						.findViewById(R.id.devices_name);
				mViewHolder.devRegion = (TextView) convertView
						.findViewById(R.id.devices_region);
				// mViewHolder.devOnOffLine = (TextView) convertView
				// .findViewById(R.id.on_off_line);
				mViewHolder.devEnergy = (TextView) convertView
						.findViewById(R.id.enerry);
				mViewHolder.devAlarm = (TextView) convertView
						.findViewById(R.id.alarm);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}

			mViewHolder.devName.setText(ds.getmDefaultDeviceName().replace(" ",
					""));
			if (null != ds.getmDeviceRegion()
					&& !ds.getmDeviceRegion().trim().equals("")) {
				mViewHolder.devRegion.setText(ds.getmDeviceRegion().replace(
						" ", ""));
			} else {
				mViewHolder.devRegion.setVisibility(View.GONE);
			}

			if (ds.getmModelId().indexOf(DataHelper.RS232_adapter) == 0) {
				mViewHolder.devRegion.setText("");
				mViewHolder.devRegion.setVisibility(View.GONE);
			}

			mViewHolder.devImg.setImageResource(DataUtil
					.getDefaultDevicesSmallIcon(ds.getmDeviceId(), ds
							.getmModelId().trim()));
			// if (DataHelper.IAS_ZONE_DEVICETYPE == devcicesId
			// || DataHelper.IAS_ACE_DEVICETYPE == devcicesId) {
			//
			// mViewHolder.devImg.setImageResource(UiUtils
			// .getDevicesSmallIconByModelId(ds.getmModelId().trim()));
			// } else if (ds.getmModelId().indexOf(
			// DataHelper.Multi_key_remote_control) == 0) {
			// mViewHolder.devImg.setImageResource(UiUtils
			// .getDevicesSmallIconForRemote(devcicesId));
			// } else {
			// mViewHolder.devImg.setImageResource(UiUtils
			// .getDevicesSmallIcon(devcicesId));
			// }

			mViewHolder.devAlarm.setVisibility(View.GONE);
			SimpleDevicesModel simpleDevicesModel = new SimpleDevicesModel();
			simpleDevicesModel.setmIeee(ds.getmIeee());
			simpleDevicesModel.setmEP(ds.getmEP());
			if (expan_postion == 2
					&& WarnManager.getInstance().isDeviceWarning(
							simpleDevicesModel)) {
				mViewHolder.devAlarm.setVisibility(View.VISIBLE);
				//
			} else {
				mViewHolder.devAlarm.setVisibility(View.INVISIBLE);
			}

			if (UiUtils.isHaveBattery(ds.getmModelId())) {
				mViewHolder.devEnergy.setVisibility(View.VISIBLE);
				mViewHolder.devEnergy.setText("剩余电量"
						+ ds.getCurpowersourcelevel() + "%");
				//
			} else {
				mViewHolder.devEnergy.setVisibility(View.INVISIBLE);
			}

			return convertView;
		}

		class ViewHolder {
			ImageView devImg;
			TextView devName;
			TextView devRegion;
			// TextView devOnOffLine;
			TextView devEnergy;
			TextView devAlarm;
		}

		public void setList(List<DevicesModel> ls) {
			mList = null;
			mList = ls;
		}
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		DevicesModel ds = mCurrentList.get(currentpostion);
		CGIManager.getInstance().deleteNode(ds.getmIeee().trim());
		int result = mDh.deleteDeviceWithGroup((Context) getActivity(),
				mDh.getSQLiteDatabase(), DataHelper.DEVICES_TABLE, " ieee=? ",
				new String[] { ds.getmIeee().trim() });

		if (result >= 0) {
			if (LightManager_expn) {
				mLightManager.remove(currentpostion);
				expnLightManager();
			} else if (ElecManager_expn) {
				mElecManager.remove(currentpostion);
				expnElecManager();
			} else if (SecurityControl_expn) {
				mSecurityControl.remove(currentpostion);
				expnSecurityControl();
			} else if (Energy_expn) {
				mEnergy.remove(currentpostion);
				expnEnergy();
			} else if (EneronmentControll_expn) {
				mEneronmentControl.remove(currentpostion);
				expnEneronmentControll();
			} else if (Other_expn) {
				mOther.remove(currentpostion);
				expnOther();
			}
		}

	}

	private int getDevicesPostion(String ieee, String ep,
			List<DevicesModel> temList) {
		if (null == ieee || null == ep) {
			return -1;
		}
		if (ieee.trim().equals("") || ep.trim().equals("")) {
			return -1;
		}
		if (null != temList && temList.size() > 0) {
			for (int m = 0; m < temList.size(); m++) {
				if (ieee.trim().equals(temList.get(m).getmIeee())
						&& ep.trim().equals(temList.get(m).getmEP())) {
					return m;
				}
			}
		}
		return -1;

	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		// final Event event = (Event) object;
		// if (EventType.LIGHTSENSOROPERATION == event.getType()) {
		// // data maybe null
		// if (event.isSuccess()) {
		// SimpleResponseData data = (SimpleResponseData) event.getData();
		//
		// int m = getDevicesPostion(data.getIeee(), data.getEp(),
		// mEneronmentControl);
		// if (m != -1) {
		//
		// mEneronmentControl.get(m).setmValue1(
		// (Integer.parseInt(data.getParam1())));
		// if (expan_postion == 3) {
		// EneronmentControlllay.post(new Runnable() {
		// @Override
		// public void run() {
		// updateEneroll();
		// }
		// });
		// }
		// }
		// } else {
		// }
		// } else if (EventType.TEMPERATURESENSOROPERATION == event.getType()) {
		// if (event.isSuccess()) {
		// SimpleResponseData data = (SimpleResponseData) event.getData();
		// int m = getDevicesPostion(data.getIeee(), data.getEp(),
		// mEneronmentControl);
		// if (m != -1) {
		// mEneronmentControl.get(m).setmValue1(
		// (int) (Float.valueOf(data.getParam1().substring(0, data
		// .getParam1().length()-2)) / 10));
		// if (expan_postion == 3) {
		// EneronmentControlllay.post(new Runnable() {
		// @Override
		// public void run() {
		// updateEneroll();
		// }
		// });
		// }
		// }
		// }
		// } else if (EventType.GETICELIST == event.getType()) {
		// if (event.isSuccess()) {
		// ArrayList<CIEresponse_params> devDataList =
		// (ArrayList<CIEresponse_params>) event
		// .getData();
		//
		// }
		// } else if (EventType.HUMIDITY == event.getType()) {
		// if (event.isSuccess()) {
		// SimpleResponseData data = (SimpleResponseData) event.getData();
		//
		// int m = getDevicesPostion(data.getIeee(), data.getEp(),
		// mEneronmentControl);
		// if (m != -1) {
		// mEneronmentControl.get(m).setmValue2(
		// (int) (Float.valueOf(data.getParam1().substring(0, data
		// .getParam1().length()-2)) / 10));
		// if (expan_postion == 3) {
		// EneronmentControlllay.post(new Runnable() {
		// @Override
		// public void run() {
		// updateEneroll();
		// }
		// });
		// }
		// }
		// }
		// }
	}

	protected void updateEneroll() {
		// TODO Auto-generated method stub
		EneronmentControllAdap.setList(mEneronmentControl);
		EneronmentControllAdap.notifyDataSetChanged();
	}

	@Override
	public void saveedit(String ieee, String ep, String name) {
		// TODO Auto-generated method stub
		String where = " ieee = ? ";
		String[] args = { ieee };

		ContentValues c = new ContentValues();
		c.put(DevicesModel.DEFAULT_DEVICE_NAME, name);

		SQLiteDatabase mSQLiteDatabase = mDh.getSQLiteDatabase();
		int result = mDh.update(mSQLiteDatabase, DataHelper.DEVICES_TABLE, c,
				where, args);
		if (result >= 0) {
			if (LightManager_expn) {
				mLightManager.get(currentpostion).setmDefaultDeviceName(name);
				LightManagerAdap.setList(mLightManager);
				LightManagerAdap.notifyDataSetChanged();
			} else if (ElecManager_expn) {
				mElecManager.get(currentpostion).setmDefaultDeviceName(name);
				ElecManagerAdap.setList(mElecManager);
				ElecManagerAdap.notifyDataSetChanged();
			} else if (SecurityControl_expn) {
				mSecurityControl.get(currentpostion)
						.setmDefaultDeviceName(name);
				SecurityControlAdap.setList(mSecurityControl);
				SecurityControlAdap.notifyDataSetChanged();
			} else if (Energy_expn) {
				mEnergy.get(currentpostion).setmDefaultDeviceName(name);
				EnergyAdap.setList(mEnergy);
				EnergyAdap.notifyDataSetChanged();
			} else if (EneronmentControll_expn) {
				mEneronmentControl.get(currentpostion).setmDefaultDeviceName(
						name);
				EneronmentControllAdap.setList(mEneronmentControl);
				EneronmentControllAdap.notifyDataSetChanged();
			} else if (Other_expn) {
				mOther.get(currentpostion).setmDefaultDeviceName(name);
				OtherAdap.setList(mOther);
				OtherAdap.notifyDataSetChanged();
			}
		}
	}

	public class getDataInBackgroundTask extends
			AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub

			if (null == mLightManager) {
				InitLightManager();
			}
			if (null == mElecManager) {
				InitElecManager();
			}
			if (null == mSecurityControl) {
				InitSecurityControl();
			}
			if (null == mEnergy) {
				InitEnergy();
			}
			if (null == mEneronmentControl) {
				InitEneronmentControll();
			}
			if (null == mOther) {
				InitOther();
			}

			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if (1 == result) {
				if (isFinishGetView) {
					Log.i("zgs", "zgs->finish inflate view");
					expnLightManager();
					hasSetData = true;
				}
				isFinishGetData = true;
			}
		}
	}

	private void InitOther() { // 开关等设备
		// TODO Auto-generated method stub
		mOther = DataUtil.getSortManagementDevices((Context) getActivity(),
				mDh, UiUtils.SWITCH_DEVICE);
		OtherAdap = new CustomeAdapter((Context) getActivity());
	}

	private void InitEnergy() {
		// TODO Auto-generated method stub
		mEnergy = DataUtil.getSortManagementDevices((Context) getActivity(),
				mDh, UiUtils.ENERGY_CONSERVATION);
		EnergyAdap = new CustomeAdapter((Context) getActivity());
	}

	private void InitEneronmentControll() {
		// TODO Auto-generated method stub
		mEneronmentControl = DataUtil.getSortManagementDevices(
				(Context) getActivity(), mDh, UiUtils.ENVIRONMENTAL_CONTROL);
		EneronmentControllAdap = new CustomeAdapter((Context) getActivity());
	}

	private void InitSecurityControl() {
		// TODO Auto-generated method stub
		mSecurityControl = DataUtil.getSortManagementDevices(
				(Context) getActivity(), mDh, UiUtils.SECURITY_CONTROL);
		SecurityControlAdap = new CustomeAdapter((Context) getActivity());
	}

	private void InitElecManager() {
		// TODO Auto-generated method stub
		mElecManager = DataUtil.getSortManagementDevices(
				(Context) getActivity(), mDh, UiUtils.ELECTRICAL_MANAGER);
		ElecManagerAdap = new CustomeAdapter((Context) getActivity());
	}

	private void InitLightManager() {
		// TODO Auto-generated method stub
		mLightManager = DataUtil.getSortManagementDevices(
				(Context) getActivity(), mDh, UiUtils.LIGHTS_MANAGER);
		LightManagerAdap = new CustomeAdapter((Context) getActivity());
	}

	@Override
	public void stopRefresh() {
		// TODO Auto-generated method stub

	}

	public interface IntoDeviceDetailFragment {
		public void intoDeviceDetailFragment(Fragment mFragment);
	}

	public interface ChangeFragment {
		public void setFragment(Fragment f);
	}
}
