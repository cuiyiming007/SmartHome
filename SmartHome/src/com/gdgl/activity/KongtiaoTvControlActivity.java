package com.gdgl.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.RemoteControl;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DeviceLearnedParam;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.RespondDataEntity;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyDlg;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class KongtiaoTvControlActivity extends Activity implements UIListener,Dialogcallback {

	TextView kongtiao, tv;
	LinearLayout kongtiao_control;
	RelativeLayout tv_control;
	LinearLayout goback;

	public static final String KONGTIAO_ONOFF = "kongtiao_on_off";
	public static final String TEMPTURE_ADD = "tempture_add";
	public static final String TEMPTURE_DEC = "tempture_dec";

	public static final String TV_ONOFF = "tv_on_off";
	public static final String CHANNEL_ADD = "channel_add";
	public static final String CHANNEL_DEC = "channel_dec";
	public static final String VOL_ADD = "vol_add";
	public static final String VOL_DEC = "vol_dec";

	List<RemoteControl> mKongtiaoControl;
	List<RemoteControl> mtvControl;
	RemoteControl currentControl;

	int currentPostion = 1;
	boolean b = false;

	CGIManager mLightManager;
	SimpleDevicesModel mControlModel;
	Dialog mDialog;
	public static final int FINISH_DLG = 1;
	public static final int FINISH_GETDATA = 2;

	Button btn_up, btn_down, btn_tv_power, btn_jian, btn_jia, btn_increment,
			btn_decress;
	ImageView on_off_remote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kongtiao_tv_control);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		getFromSharedPreferences
				.setharedPreferences(KongtiaoTvControlActivity.this);
		mKongtiaoControl = getFromSharedPreferences
				.getTvKongtiaoRemoteControl(1);
		mtvControl = getFromSharedPreferences.getTvKongtiaoRemoteControl(2);
		if (null == mKongtiaoControl) {
			mKongtiaoControl = new ArrayList<RemoteControl>();
		}
		if (null == mtvControl) {
			mtvControl = new ArrayList<RemoteControl>();
		}

		mControlModel = new SimpleDevicesModel();
		mControlModel.setmIeee("00137A0000010148");
		mControlModel.setmEP("01");
		mControlModel.setmModelId("Z211");
		mControlModel.setmDeviceId(8);

		mLightManager = CGIManager.getInstance();
		mLightManager.addObserver(this);
		mLightManager.getDeviceLearnedIRDataInformation(mControlModel);
		mHandler.sendEmptyMessageDelayed(FINISH_GETDATA, 3000);
	}

	private void initView() {
		// TODO Auto-generated method stub
		kongtiao = (TextView) findViewById(R.id.kongtiao_text);
		tv = (TextView) findViewById(R.id.tv_text);
		kongtiao_control = (LinearLayout) findViewById(R.id.kongtiao_control);
		tv_control = (RelativeLayout) findViewById(R.id.tv_control);

		btn_up = (Button) findViewById(R.id.btn_up);
		btn_down = (Button) findViewById(R.id.btn_down);
		on_off_remote = (ImageView) findViewById(R.id.on_off_remote);

		btn_tv_power = (Button) findViewById(R.id.btn_tv_power);
		btn_jian = (Button) findViewById(R.id.btn_jian);
		btn_jia = (Button) findViewById(R.id.btn_jia);
		btn_increment = (Button) findViewById(R.id.btn_increment);
		btn_decress = (Button) findViewById(R.id.btn_decress);

		goback = (LinearLayout) findViewById(R.id.goback);

		goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		kongtiao.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentPostion = 1;
				kongtiao.setBackground(getResources().getDrawable(
						R.drawable.dlg_button_press));
				tv.setBackground(null);

				tv_control.setVisibility(View.GONE);
				kongtiao_control.setVisibility(View.VISIBLE);
			}
		});

		tv.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentPostion = 2;
				tv.setBackground(getResources().getDrawable(
						R.drawable.dlg_button_press));
				kongtiao.setBackground(null);

				kongtiao_control.setVisibility(View.GONE);
				tv_control.setVisibility(View.VISIBLE);
			}
		});

		initKongtiao();
		initTv();
		setListeners();
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
			case FINISH_GETDATA:
				initView();
				break;
			default:
				break;
			}
		};
	};

	private void setListeners() {
		// TODO Auto-generated method stub
		btn_tv_power.setOnClickListener(new learnListeners("1", TV_ONOFF));
		btn_tv_power.setOnLongClickListener(new cleanLearListener("1", TV_ONOFF));

		btn_jian.setOnClickListener(new learnListeners("2", CHANNEL_DEC));
		btn_jian.setOnLongClickListener(new cleanLearListener("2", CHANNEL_DEC));

		btn_jia.setOnClickListener(new learnListeners("3", CHANNEL_ADD));
		btn_jia.setOnLongClickListener(new cleanLearListener("3", CHANNEL_ADD));

		btn_increment.setOnClickListener(new learnListeners("4", VOL_ADD));
		btn_increment.setOnLongClickListener(new cleanLearListener("4", VOL_ADD));

		btn_decress.setOnClickListener(new learnListeners("5", VOL_DEC));
		btn_decress.setOnLongClickListener(new cleanLearListener("5", VOL_DEC));

		btn_up.setOnClickListener(new learnListeners("6", TEMPTURE_DEC));
		btn_up.setOnLongClickListener(new cleanLearListener("6", TEMPTURE_DEC));
		
		btn_down.setOnClickListener(new learnListeners("8", TEMPTURE_ADD));
		btn_down.setOnLongClickListener(new cleanLearListener("8", TEMPTURE_ADD));

		on_off_remote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RemoteControl rc = (RemoteControl) v.getTag();
				if (null == rc) {

					rc = new RemoteControl();
					rc.Index = "7";
					rc.Name = KONGTIAO_ONOFF;
					rc.IsLearn = "0";
				}
				if (!rc.IsLearn.trim().equals("1")) {
					if (null == mDialog) {
						mDialog = MyDlg.createLoadingDialog(
								KongtiaoTvControlActivity.this, "开始学习此功能...");
						mDialog.show();
					} else {
						mDialog.show();
					}

					currentControl = rc;
					mLightManager.beginLearnIR(mControlModel,
							Integer.parseInt(rc.Index), rc.Name);
					mHandler.sendEmptyMessageDelayed(FINISH_DLG, 3000);
				} else {
					mLightManager.beginApplyIR(mControlModel,
							Integer.parseInt(rc.Index));
					ImageView mImageView = (ImageView) v;
					if (!b) {
						mImageView
								.setImageResource(R.drawable.kongtao_on_small);
					} else {
						mImageView
								.setImageResource(R.drawable.kongtao_off_small);
					}
					b = !b;
				}
			}
		});
		
//		on_off_remote.setOnLongClickListener(new cleanLearListener("7", KONGTIAO_ONOFF));
		on_off_remote.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				ImageView mImageView = (ImageView) v;
				RemoteControl rc = (RemoteControl) v.getTag();
				if (null == rc) {
					return true;
				}
				currentControl = rc;
				if (rc.IsLearn.trim().equals("1")) {
					MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
							KongtiaoTvControlActivity.this);
					mMyOkCancleDlg.setDialogCallback(KongtiaoTvControlActivity.this);
					mMyOkCancleDlg.setContent("确定要重新学习此功能吗?");
					mMyOkCancleDlg.show();
					mImageView.setImageResource(R.drawable.kongtao_off_small);
				}
				return true;
			}
		});

	}
	
	public class cleanLearListener implements OnLongClickListener{
		
		public String Index;
		public String Name;

		public cleanLearListener(String index, String name) {
			Index = index;
			Name = name;
		}
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			
			RemoteControl rc = (RemoteControl) v.getTag();
			if (null == rc) {

				return true;
			}
			currentControl = rc;
			if (rc.IsLearn.trim().equals("1")) {
				MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
						KongtiaoTvControlActivity.this);
				mMyOkCancleDlg.setDialogCallback(KongtiaoTvControlActivity.this);
				mMyOkCancleDlg.setContent("确定要重新学习此功能吗?");
				mMyOkCancleDlg.show();
			} 
			
			return true;
		}
		
	}
	
	public class learnListeners implements OnClickListener {

		public String Index;
		public String Name;

		public learnListeners(String index, String name) {
			Index = index;
			Name = name;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			RemoteControl rc = (RemoteControl) v.getTag();
			if (null == rc) {

				rc = new RemoteControl();
				rc.Index = Index;
				rc.Name = Name;
				rc.IsLearn = "0";
			}
			currentControl = rc;
			if (!rc.IsLearn.trim().equals("1")) {
				if (null == mDialog) {
					mDialog = MyDlg.createLoadingDialog(
							KongtiaoTvControlActivity.this, "开始学习此功能...");
					mDialog.show();
				} else {
					mDialog.show();
				}
				mLightManager.beginLearnIR(mControlModel,
						Integer.parseInt(rc.Index), rc.Name);
				mHandler.sendEmptyMessageDelayed(FINISH_DLG, 3000);
			} else {
				mLightManager.beginApplyIR(mControlModel,
						Integer.parseInt(rc.Index));
			}
		}

	}

	@SuppressLint("NewApi")
	private void initTv() {
		// TODO Auto-generated method stub
		btn_tv_power.setTag(getRemoteControl(1, 2));
		btn_jian.setTag(getRemoteControl(2, 2));
		btn_jia.setTag(getRemoteControl(3, 2));
		btn_increment.setTag(getRemoteControl(4, 2));
		btn_decress.setTag(getRemoteControl(5, 2));

		if (btn_tv_power.getTag() == null
				|| !((RemoteControl) btn_tv_power.getTag()).IsLearn.trim()
						.equals("1")) {
			btn_tv_power.setBackground(getResources().getDrawable(
					R.drawable.power_unlearned_btn_style));
		} else {
			btn_tv_power.setBackground(getResources().getDrawable(
					R.drawable.power_learned_btn_style));
		}

		if (btn_jian.getTag() == null
				|| !((RemoteControl) btn_jian.getTag()).IsLearn.trim().equals(
						"1")) {
			btn_jian.setBackground(getResources().getDrawable(
					R.drawable.jian_unlearned_btn_style));
		} else {
			btn_jian.setBackground(getResources().getDrawable(
					R.drawable.jian_learned_btn_style));
		}

		if (btn_jia.getTag() == null
				|| !((RemoteControl) btn_jia.getTag()).IsLearn.trim().equals(
						"1")) {
			btn_jia.setBackground(getResources().getDrawable(
					R.drawable.jia_unlearned_btn_style));
		} else {
			btn_jia.setBackground(getResources().getDrawable(
					R.drawable.jia_learned_btn_style));
		}

		if (btn_increment.getTag() == null
				|| !((RemoteControl) btn_increment.getTag()).IsLearn.trim()
						.equals("1")) {
			btn_increment.setBackground(getResources().getDrawable(
					R.drawable.increment_unlearned_btn_style));
		} else {
			btn_increment.setBackground(getResources().getDrawable(
					R.drawable.increment_learned_btn_style));
		}

		if (btn_decress.getTag() == null
				|| !((RemoteControl) btn_decress.getTag()).IsLearn.trim()
						.equals("1")) {
			btn_decress.setBackground(getResources().getDrawable(
					R.drawable.decress_unlearned_btn_style));
		} else {
			btn_decress.setBackground(getResources().getDrawable(
					R.drawable.decress_learned_btn_style));
		}
	}

	public RemoteControl getRemoteControl(int index, int type) {
		RemoteControl rc = null;
		if (1 == type) {
			if (null == mKongtiaoControl || mKongtiaoControl.size() == 0) {
				return rc;
			} else {
				for (RemoteControl rd : mKongtiaoControl) {
					if (Integer.parseInt(rd.Index) == index) {
						rc = rd;
					}
				}
			}
		} else if (2 == type) {
			if (null == mtvControl || mtvControl.size() == 0) {
				return rc;
			} else {
				for (RemoteControl rd : mtvControl) {
					if (Integer.parseInt(rd.Index) == index) {
						rc = rd;
					}
				}
			}
		}
		return rc;

	}

	@SuppressLint("NewApi")
	private void initKongtiao() {
		// TODO Auto-generated method stub
		btn_up.setTag(getRemoteControl(6, 1));
		on_off_remote.setTag(getRemoteControl(7, 1));
		btn_down.setTag(getRemoteControl(8, 1));

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public int isInList(RemoteControl rc, List<RemoteControl> mList) {
		if (null == rc || null == mList || mList.size() == 0) {
			return -1;
		}
		for (int i = 0; i < mList.size(); i++) {
			if (rc.Index.trim().equals(mList.get(i).Index.trim())) {
				return i;
			}
		}
		return -1;
	}

	private void writeLearnListToSharedPreferences(int type) {
		// TODO Auto-generated method stub
		getFromSharedPreferences
				.setharedPreferences(KongtiaoTvControlActivity.this);
		if (1 == type) {
			getFromSharedPreferences.addTvKongtiaoRemoteControlList(
					mKongtiaoControl, 1);
		} else if (2 == type) {
			getFromSharedPreferences.addTvKongtiaoRemoteControlList(mtvControl,
					2);
		}

	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		if (EventType.BEGINLEARNIR == event.getType()) {
			if (event.isSuccess() == true) {
				// data maybe null
				currentControl.IsLearn="1";
				if (1 == currentPostion) {
					if (mKongtiaoControl.size() == 0) {
						mKongtiaoControl.add(currentControl);
					} else {
						int m = isInList(currentControl, mKongtiaoControl);
						if (-1 == m) {
							mKongtiaoControl.add(currentControl);
						} else {
							mKongtiaoControl.get(m).IsLearn = "1";
						}
					}
					initKongtiao();
					writeLearnListToSharedPreferences(1);
				} else if (2 == currentPostion) {
					if (mtvControl.size() == 0) {
						mtvControl.add(currentControl);
					} else {
						int m = isInList(currentControl, mtvControl);
						if (-1 == m) {
							mtvControl.add(currentControl);
						} else {
							mtvControl.get(m).IsLearn = "1";
						}
					}
					initTv();
					writeLearnListToSharedPreferences(2);
				}

			} else {
				// if failed,prompt a Toast

			}
		}else if (EventType.GETDEVICELEARNED == event.getType()) {
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
						if(mtvControl.size()==0 || mKongtiaoControl.size()==0){
							if(rc.Index.equals("6") || rc.Index.equals("7") || rc.Index.equals("8")){
								mKongtiaoControl.add(rc);
							}else{
								mtvControl.add(rc);
							}
						}else{
							if(rc.Index.equals("6") || rc.Index.equals("7") || rc.Index.equals("8")){
								int m=isInList(rc, mKongtiaoControl);
								if(-1!=m){
									mKongtiaoControl.get(m).IsLearn="1";
								}
							}else{
								int m=isInList(rc, mtvControl);
								if(-1!=m){
									mtvControl.get(m).IsLearn="1";
								}
							}
						}
						initView();
					}
				}
			} else {
				// if failed,prompt a Toast

			}
		}
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		currentControl.IsLearn="0";
		if (1 == currentPostion) {
			if (mKongtiaoControl.size() == 0) {
				mKongtiaoControl.add(currentControl);
			} else {
				int m = isInList(currentControl, mKongtiaoControl);
				if (-1 == m) {
					mKongtiaoControl.add(currentControl);
				} else {
					mKongtiaoControl.get(m).IsLearn = "0";
				}
			}
			initKongtiao();
			writeLearnListToSharedPreferences(1);
		} else if (2 == currentPostion) {
			if (mtvControl.size() == 0) {
				mtvControl.add(currentControl);
			} else {
				int m = isInList(currentControl, mtvControl);
				if (-1 == m) {
					mtvControl.add(currentControl);
				} else {
					mtvControl.get(m).IsLearn = "0";
				}
			}
			initTv();
			writeLearnListToSharedPreferences(2);
		}
	}

}
