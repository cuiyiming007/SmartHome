package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.manager.LightManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.RemoteControl;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DeviceLearnedParam;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.RespondDataEntity;
import com.gdgl.mydata.SimpleResponseData;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.mydata.bind.BindResponseData;
import com.gdgl.smarthome.R;
import com.gdgl.util.AddDlg;
import com.gdgl.util.MyDlg;
import com.gdgl.util.AddDlg.AddDialogcallback;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

public class RemoteControlActivity extends Activity implements UIListener,
		AddDialogcallback {
	
	public static final int FINISH_DLG = 1;
	
	GridView content_view;
	List<RemoteControl> mRemoteControl;
	ViewGroup nodevices;
	Button addButton;
	LinearLayout goback;
	LightManager mLightManager;
	SimpleDevicesModel mControlModel;
	RemoteControlAdapter mAdapter;
	
	int currentControl;
	
	Dialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_control_activity);
		initData();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		nodevices = (ViewGroup) findViewById(R.id.nodevices);
		content_view = (GridView) findViewById(R.id.content_view);
		addButton = (Button) findViewById(R.id.add_button);
		goback = (LinearLayout) findViewById(R.id.goback);

		goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddDlg mAddDlg = new AddDlg(RemoteControlActivity.this,
						AddDlg.REMOTE_CONTROL);
				mAddDlg.setContent("添加功能");
				mAddDlg.setType("功能名称");
				mAddDlg.setDialogCallback(RemoteControlActivity.this);
				mAddDlg.show();
			}
		});
		
		updateData();
	}

	private void setData(List<RemoteControl> tempList) {
		// TODO Auto-generated method stub
		if (null == mRemoteControl || mRemoteControl.size() == 0) {
			if (null != tempList && tempList.size() > 0) {
				mRemoteControl = tempList;
			}
		} else {
			if (null != tempList && tempList.size() > 0) {
				for (RemoteControl remoteControl : tempList) {
					updateControl(remoteControl);
				}
			}
		}
		if (null == mRemoteControl || mRemoteControl.size() == 0) {
			nodevices.setVisibility(View.VISIBLE);
			content_view.setVisibility(View.GONE);
		} else {
			nodevices.setVisibility(View.GONE);
			content_view.setVisibility(View.VISIBLE);
			if(null==mAdapter){
				mAdapter = new RemoteControlAdapter();
				content_view.setAdapter(mAdapter);
			}else{
				mAdapter.notifyDataSetChanged();
			}
			
		}
		writeLearnListToSharedPreferences();
	}

	private void updateData() {
		// TODO Auto-generated method stub
		if (null == mRemoteControl || mRemoteControl.size() == 0) {
			nodevices.setVisibility(View.VISIBLE);
			content_view.setVisibility(View.GONE);
		} else {
			nodevices.setVisibility(View.GONE);
			content_view.setVisibility(View.VISIBLE);
			mAdapter = new RemoteControlAdapter();
			content_view.setAdapter(mAdapter);
		}
		writeLearnListToSharedPreferences();
	}

	public void updateControl(RemoteControl rc) {
		boolean isExit = false;
		for (RemoteControl control : mRemoteControl) {
			if (rc.Index.trim().equals(control.Index.trim())) {
				control.IsLearn = "1";
				isExit = true;
			}
		}
		if (!isExit) {
			mRemoteControl.add(rc);
		}
	}

	private void writeLearnListToSharedPreferences() {
		// TODO Auto-generated method stub
		getFromSharedPreferences
				.setharedPreferences(RemoteControlActivity.this);
		getFromSharedPreferences.addRemoteControlList(mRemoteControl);
	}

	private void initData() {
		// TODO Auto-generated method stub
		mLightManager = LightManager.getInstance();
		mLightManager.addObserver(this);

		mRemoteControl = new ArrayList<RemoteControl>();

		mControlModel = new SimpleDevicesModel();
		mControlModel.setmIeee("00137A0000010148");
		mControlModel.setmEP("01");
		mControlModel.setmModelId("Z211");
		mControlModel.setmDeviceId(8);

		getFromSharedPreferences
				.setharedPreferences(RemoteControlActivity.this);
		mRemoteControl = getFromSharedPreferences.getRemoteControl();

		mLightManager.getDeviceLearnedIRDataInformation(mControlModel);

	}
	public Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			switch (what) {
			case FINISH_DLG:
				if (null != mDialog) {
					mDialog.dismiss();
					mDialog = null;
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public class RemoteControlAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mRemoteControl || mRemoteControl.size() != 0) {
				return mRemoteControl.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mRemoteControl || mRemoteControl.size() != 0) {
				return mRemoteControl.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mRemoteControl || mRemoteControl.size() != 0) {
				return Integer.parseInt(mRemoteControl.get(position).Index);
			}
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final RemoteControl rc = (RemoteControl) getItem(position);
			ViewHolder mViewHolder;
			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from(RemoteControlActivity.this)
						.inflate(R.layout.add_button_item, null);
				mViewHolder.function_btn = (Button) convertView
						.findViewById(R.id.function_btn);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			mViewHolder.function_btn.setText(rc.Name);
			if(rc.IsLearn.trim().equals("1")){
				mViewHolder.function_btn.setTextColor(0xd020B2AA);
			}
			
			mViewHolder.function_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!rc.IsLearn.trim().equals("1")){
						if (null == mDialog) {
							mDialog = MyDlg.createLoadingDialog(
									RemoteControlActivity.this, "开始学习此功能...");
							mDialog.show();
						} else {
							mDialog.show();
						}
						currentControl=position;
						mLightManager.beginLearnIR(mControlModel, Integer.parseInt(rc.Index), rc.Name);
						mHandler.sendEmptyMessageDelayed(FINISH_DLG, 5000);
					}else{
						mLightManager.beginApplyIR(mControlModel, Integer.parseInt(rc.Index));
					}
				}
			});
			
			return convertView;
		}

		public class ViewHolder {
			public Button function_btn;
		}
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		if (EventType.GETDEVICELEARNED == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				RespondDataEntity<DeviceLearnedParam> dataEntity = (RespondDataEntity<DeviceLearnedParam>) event
						.getData();
				List<DeviceLearnedParam> mLearnedList = dataEntity
						.getResponseparamList();
				final List<RemoteControl> tempList = new ArrayList<RemoteControl>();
				if (null == mLearnedList || mLearnedList.size() == 0) {
					return;
				} else {
					RemoteControl rc;
					for (DeviceLearnedParam deviceLearnedParam : mLearnedList) {
						rc = new RemoteControl();
						rc.Index = deviceLearnedParam.getHadaemonindex();
						rc.Name = deviceLearnedParam.getIrdisplayname();
						rc.IsLearn = "1";
						tempList.add(rc);
					}
				}

				addButton.post(new Runnable() {

					@Override
					public void run() {
						setData(tempList);
					}
				});

			} else {
				// if failed,prompt a Toast

			}
		}else if(EventType.BEGINLEARNIR == event.getType()){
			if (event.isSuccess() == true) {
				// data maybe null
				mRemoteControl.get(currentControl).IsLearn="1";
				addButton.post(new Runnable() {

					@Override
					public void run() {
						mAdapter.notifyDataSetChanged();
					}
				});
			} else {
				// if failed,prompt a Toast

			}
		}
	}

	@Override
	public void refreshdata() {
		// TODO Auto-generated method stub
		getFromSharedPreferences
				.setharedPreferences(RemoteControlActivity.this);
		mRemoteControl = getFromSharedPreferences.getRemoteControl();
		if (null == mAdapter) {
			updateData();
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

}
