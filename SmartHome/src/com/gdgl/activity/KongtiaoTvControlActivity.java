package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.manager.CGIManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.RemoteControl;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DeviceLearnedParam;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.RespondDataEntity;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.mydata.Callback.CallbackBeginLearnIRMessage;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyDlg;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class KongtiaoTvControlActivity extends Activity implements UIListener,
		Dialogcallback {

	TextView kongtiao, tv;
	LinearLayout kongtiao_control;
	ScrollView tv_control;
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

	int currentPostion = 2;
	boolean b = false;

	CGIManager cgiManager;
	CallbackManager callbackManager;
	DevicesModel mControlModel;
	TouchDelayShow touchDelayShow;
	Dialog mDialog;
	public static final int FINISH_DLG = 3;
	public static final int FINISH_GETDATA = 4;
	public static final int INIT_TV = 2;
	public static final int INIT_KONGTIAO = 1;
	public static final int TOUCH_DELAY=200;

	// 电视按钮
	ImageButton ibtn_mute, ibtn_lock, ibtn_power, ibtn_num_1, ibtn_num_2,
			ibtn_num_3, ibtn_num_4, ibtn_num_5, ibtn_num_6, ibtn_num_7,
			ibtn_num_8, ibtn_num_9, ibtn_num_0, ibtn_mutil, ibtn_repeat,
			ibtn_top, ibtn_down, ibtn_left, ibtn_right, ibtn_center,
			ibtn_vedio_up, ibtn_vedio_down, ibtn_vol_up, ibtn_vol_down,
			ibtn_menu, ibtn_vedio_source;

	Button btn_up, btn_down;
	ImageView on_off_remote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kongtiao_tv_control);
		Intent intent=getIntent();
		mControlModel=(DevicesModel)intent.getSerializableExtra(Constants.PASS_OBJECT);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		getFromSharedPreferences
				.setsharedPreferences(KongtiaoTvControlActivity.this);
		mKongtiaoControl = getFromSharedPreferences
				.getTvKongtiaoRemoteControl(1);
		mtvControl = getFromSharedPreferences.getTvKongtiaoRemoteControl(2);
		if (null == mKongtiaoControl) {
			mKongtiaoControl = new ArrayList<RemoteControl>();
		}
		if (null == mtvControl) {
			mtvControl = new ArrayList<RemoteControl>();
		}

//		mControlModel = new SimpleDevicesModel();
//		mControlModel.setmIeee("00137A0000010148");
//		mControlModel.setmEP("01");
//		mControlModel.setmModelId("Z211");
//		mControlModel.setmDeviceId(8);

		CallbackManager.getInstance().addObserver(
				KongtiaoTvControlActivity.this);
		cgiManager = CGIManager.getInstance();
		cgiManager.addObserver(KongtiaoTvControlActivity.this);
		cgiManager.getDeviceLearnedIRDataInformation(mControlModel);
		mHandler.sendEmptyMessageDelayed(FINISH_GETDATA, 2000);
	}

	private void initView() {
		// TODO Auto-generated method stub
		kongtiao = (TextView) findViewById(R.id.kongtiao_text);
		tv = (TextView) findViewById(R.id.tv_text);
		kongtiao_control = (LinearLayout) findViewById(R.id.kongtiao_control);
		tv_control = (ScrollView) findViewById(R.id.tv_control);

		// 电视按钮
		ibtn_lock = (ImageButton) findViewById(R.id.btn_lock);
		ibtn_mute = (ImageButton) findViewById(R.id.btn_mute);
		ibtn_power = (ImageButton) findViewById(R.id.btn_power);
		ibtn_num_0 = (ImageButton) findViewById(R.id.btn_num_0);
		ibtn_num_1 = (ImageButton) findViewById(R.id.btn_num_1);
		ibtn_num_2 = (ImageButton) findViewById(R.id.btn_num_2);
		ibtn_num_3 = (ImageButton) findViewById(R.id.btn_num_3);
		ibtn_num_4 = (ImageButton) findViewById(R.id.btn_num_4);
		ibtn_num_5 = (ImageButton) findViewById(R.id.btn_num_5);
		ibtn_num_6 = (ImageButton) findViewById(R.id.btn_num_6);
		ibtn_num_7 = (ImageButton) findViewById(R.id.btn_num_7);
		ibtn_num_8 = (ImageButton) findViewById(R.id.btn_num_8);
		ibtn_num_9 = (ImageButton) findViewById(R.id.btn_num_9);
		ibtn_mutil = (ImageButton) findViewById(R.id.btn_mutil);
		ibtn_repeat = (ImageButton) findViewById(R.id.btn_repeat);
		ibtn_top = (ImageButton) findViewById(R.id.btn_top);
		ibtn_down = (ImageButton) findViewById(R.id.btn_down);
		ibtn_left = (ImageButton) findViewById(R.id.btn_left);
		ibtn_right = (ImageButton) findViewById(R.id.btn_right);
		ibtn_center = (ImageButton) findViewById(R.id.btn_center);
		ibtn_vedio_up = (ImageButton) findViewById(R.id.btn_vedio_top);
		ibtn_vedio_down = (ImageButton) findViewById(R.id.btn__vedio_down);
		ibtn_vol_up = (ImageButton) findViewById(R.id.btn_vol_up);
		ibtn_vol_down = (ImageButton) findViewById(R.id.btn_vol_down);
		ibtn_menu = (ImageButton) findViewById(R.id.btn_menu);
		ibtn_vedio_source = (ImageButton) findViewById(R.id.btn_vedio);
		
		//空调按钮
		btn_up = (Button) findViewById(R.id.btn_up);
		btn_down = (Button) findViewById(R.id.btn_down_ac);
		on_off_remote = (ImageView) findViewById(R.id.on_off_remote);


		goback = (LinearLayout) findViewById(R.id.goback);

		goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		kongtiao.setOnClickListener(new OnClickListener() {

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
			case INIT_KONGTIAO:
				initView();
				break;
			case INIT_TV:
				initTv();
				break;
			default:
				break;
			}
		};
	};

	private void setListeners() {
		// TODO Auto-generated method stub
		
		//空调按键监听
		btn_up.setOnClickListener(new learnListeners("6", TEMPTURE_DEC));
		btn_up.setOnLongClickListener(new cleanLearnedListener("6",
				TEMPTURE_DEC));
		btn_down.setOnClickListener(new learnListeners("8", TEMPTURE_ADD));
		btn_down.setOnLongClickListener(new cleanLearnedListener("8",
				TEMPTURE_ADD));
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
					currentControl = rc;
					if (null == mDialog) {
						mDialog = MyDlg.createLoadingDialog(
								KongtiaoTvControlActivity.this, "开始学习此功能...");
						mDialog.show();
					} else {
						mDialog.show();
					}
					cgiManager.beginLearnIR(mControlModel,
							Integer.parseInt(rc.Index), rc.Name);
					mHandler.sendEmptyMessageDelayed(FINISH_DLG, 3000);
				} else {
					cgiManager.beginApplyIR(mControlModel,
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
		// on_off_remote.setOnLongClickListener(new cleanLearListener("7",
		// KONGTIAO_ONOFF));
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
					mMyOkCancleDlg
							.setDialogCallback(KongtiaoTvControlActivity.this);
					mMyOkCancleDlg.setContent("确定要重新学习此功能吗?");
					mMyOkCancleDlg.show();
					mImageView.setImageResource(R.drawable.kongtao_off_small);
				}
				return true;
			}
		});

		// 电视按键监听
		int i = 0x40000000 + 10000;
		ibtn_mute.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_mute.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_lock.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_lock.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_power
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_power.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_num_1
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_num_1.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_num_2
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_num_2.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_num_3
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_num_3.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_num_4
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_num_4.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_num_5
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_num_5.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_num_6
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_num_6.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_num_7
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_num_7.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_num_8
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_num_8.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_num_9
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_num_9.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_mutil
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_mutil.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_num_0
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_num_0.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_repeat
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_repeat.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_vedio_up.setOnClickListener(new learnListeners(String.valueOf(i),
				""));
		ibtn_vedio_up.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_top.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_top.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_vol_up
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_vol_up.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_left.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_left.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_center
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_center.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_right
				.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_right.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_vedio_down.setOnClickListener(new learnListeners(
				String.valueOf(i), ""));
		ibtn_vedio_down.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_down.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_down.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_vol_down.setOnClickListener(new learnListeners(String.valueOf(i),
				""));
		ibtn_vol_down.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_menu.setOnClickListener(new learnListeners(String.valueOf(i), ""));
		ibtn_menu.setOnLongClickListener(new cleanLearnedListener(String
				.valueOf(i++), ""));
		ibtn_vedio_source.setOnClickListener(new learnListeners(String
				.valueOf(i), ""));
		ibtn_vedio_source.setOnLongClickListener(new cleanLearnedListener(
				String.valueOf(i++), ""));

	}

	public class cleanLearnedListener implements OnLongClickListener {

		public String Index;
		public String Name;

		public cleanLearnedListener(String index, String name) {
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
				mMyOkCancleDlg
						.setDialogCallback(KongtiaoTvControlActivity.this);
				mMyOkCancleDlg.setContent("确定要清除学习的功能吗?");
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
				cgiManager.beginLearnIR(mControlModel,
						Integer.parseInt(rc.Index), rc.Name);
				mHandler.sendEmptyMessageDelayed(FINISH_DLG, 30000);
			} else {
				cgiManager.beginApplyIR(mControlModel,
						Integer.parseInt(rc.Index));
			}
		}

	}

	private void initTv() {
		// TODO Auto-generated method stub
		int i = 0x40000000 + 10000;
		ibtn_mute.setTag(getRemoteControl(i++, 2));
		ibtn_lock.setTag(getRemoteControl(i++, 2));
		ibtn_power.setTag(getRemoteControl(i++, 2));
		ibtn_num_1.setTag(getRemoteControl(i++, 2));
		ibtn_num_2.setTag(getRemoteControl(i++, 2));
		ibtn_num_3.setTag(getRemoteControl(i++, 2));
		ibtn_num_4.setTag(getRemoteControl(i++, 2));
		ibtn_num_5.setTag(getRemoteControl(i++, 2));
		ibtn_num_6.setTag(getRemoteControl(i++, 2));
		ibtn_num_7.setTag(getRemoteControl(i++, 2));
		ibtn_num_8.setTag(getRemoteControl(i++, 2));
		ibtn_num_9.setTag(getRemoteControl(i++, 2));
		ibtn_mutil.setTag(getRemoteControl(i++, 2));
		ibtn_num_0.setTag(getRemoteControl(i++, 2));
		ibtn_repeat.setTag(getRemoteControl(i++, 2));
		ibtn_vedio_up.setTag(getRemoteControl(i++, 2));
		ibtn_top.setTag(getRemoteControl(i++, 2));
		ibtn_vol_up.setTag(getRemoteControl(i++, 2));
		ibtn_left.setTag(getRemoteControl(i++, 2));
		ibtn_center.setTag(getRemoteControl(i++, 2));
		ibtn_right.setTag(getRemoteControl(i++, 2));
		ibtn_vedio_down.setTag(getRemoteControl(i++, 2));
		ibtn_down.setTag(getRemoteControl(i++, 2));
		ibtn_vol_down.setTag(getRemoteControl(i++, 2));
		ibtn_menu.setTag(getRemoteControl(i++, 2));
		ibtn_vedio_source.setTag(getRemoteControl(i++, 2));

		showImageButton(ibtn_mute, R.drawable.ir_tv_mute,
				R.drawable.ir_tv_mute_down, R.drawable.ir_tv_mute_notlearned,
				R.drawable.ir_tv_mute_notlearned_down);
		showImageButton(ibtn_lock, R.drawable.ir_tv_lock,
				R.drawable.ir_tv_lock_down, R.drawable.ir_tv_lock_notlearned,
				R.drawable.ir_tv_lock_notlearned_down);
		showImageButton(ibtn_power, R.drawable.ir_tv_power,
				R.drawable.ir_tv_power_down, R.drawable.ir_tv_power_notlearned,
				R.drawable.ir_tv_power_notlearned_down);
		showImageButton(ibtn_num_1, R.drawable.ir_1, R.drawable.ir_1_down,
				R.drawable.ir_1_notlearned, R.drawable.ir_1_notlearned_down);
		showImageButton(ibtn_num_2, R.drawable.ir_2, R.drawable.ir_2_down,
				R.drawable.ir_2_notlearned, R.drawable.ir_2_notlearned_down);
		showImageButton(ibtn_num_3, R.drawable.ir_3, R.drawable.ir_3_down,
				R.drawable.ir_3_notlearned, R.drawable.ir_3_notlearned_down);
		showImageButton(ibtn_num_4, R.drawable.ir_4, R.drawable.ir_4_down,
				R.drawable.ir_4_notlearned, R.drawable.ir_4_notlearned_down);
		showImageButton(ibtn_num_5, R.drawable.ir_5, R.drawable.ir_5_down,
				R.drawable.ir_5_notlearned, R.drawable.ir_5_notlearned_down);
		showImageButton(ibtn_num_6, R.drawable.ir_6, R.drawable.ir_6_down,
				R.drawable.ir_6_notlearned, R.drawable.ir_6_notlearned_down);
		showImageButton(ibtn_num_7, R.drawable.ir_7, R.drawable.ir_7_down,
				R.drawable.ir_7_notlearned, R.drawable.ir_7_notlearned_down);
		showImageButton(ibtn_num_8, R.drawable.ir_8, R.drawable.ir_8_down,
				R.drawable.ir_8_notlearned, R.drawable.ir_8_notlearned_down);
		showImageButton(ibtn_num_9, R.drawable.ir_9, R.drawable.ir_9_down,
				R.drawable.ir_9_notlearned, R.drawable.ir_9_notlearned_down);
		showImageButton(ibtn_mutil, R.drawable.ir_mutil,
				R.drawable.ir_mutil_down, R.drawable.ir_mutil_notlearned,
				R.drawable.ir_mutil_notlearned_down);
		showImageButton(ibtn_num_0, R.drawable.ir_0, R.drawable.ir_0_down,
				R.drawable.ir_0_notlearned, R.drawable.ir_0_notlearned_down);
		showImageButton(ibtn_repeat, R.drawable.ir_tv_repeat,
				R.drawable.ir_tv_repeat_down,
				R.drawable.ir_tv_repeat_notlearned,
				R.drawable.ir_tv_repeat_down_notlearned);
		showImageButton(ibtn_vedio_up, R.drawable.ir_tv_up,
				R.drawable.ir_tv_up_down, R.drawable.ir_tv_up_notlearned,
				R.drawable.ir_tv_up_notlearned_down);
		showImageButton(ibtn_top, R.drawable.ir_dvdup,
				R.drawable.ir_dvdup_down, R.drawable.ir_dvdup_notlearned,
				R.drawable.ir_dvdup_down_notlearned);
		showImageButton(ibtn_vol_up, R.drawable.ir_tv_vol_up,
				R.drawable.ir_tv_vol_up_down,
				R.drawable.ir_tv_vol_up_notlearned,
				R.drawable.ir_tv_vol_up_nolearned_down);
		showImageButton(ibtn_left, R.drawable.ir_dvdleft,
				R.drawable.ir_dvdleft_down, R.drawable.ir_dvdleft_notlearned,
				R.drawable.ir_dvdleft_down_notlearned);
		showImageButton(ibtn_center, R.drawable.ir_tv_ok,
				R.drawable.ir_tv_ok_down, R.drawable.ir_tv_ok_notlearned,
				R.drawable.ir_tv_ok_down_notlearned);
		showImageButton(ibtn_right, R.drawable.ir_dvdright,
				R.drawable.ir_dvdright_down, R.drawable.ir_dvdright_notlearned,
				R.drawable.ir_dvdright_down_notlearned);
		showImageButton(ibtn_vedio_down, R.drawable.ir_tv_down,
				R.drawable.ir_tv_down_down, R.drawable.ir_tv_down_notlearned,
				R.drawable.ir_tv_down_notlearned_down);
		showImageButton(ibtn_down, R.drawable.ir_dvddown,
				R.drawable.ir_dvddown_down, R.drawable.ir_dvddown_notlearned,
				R.drawable.ir_dvddown_down_notlearned);
		showImageButton(ibtn_vol_down, R.drawable.ir_tv_vol_down,
				R.drawable.ir_tv_vol_down_down,
				R.drawable.ir_tv_vol_down_notlearned,
				R.drawable.ir_tv_vol_down_notlearned_down);
		showImageButton(ibtn_menu, R.drawable.ir_tv_menu,
				R.drawable.ir_tv_menu_down, R.drawable.ir_tv_menu_notlearned,
				R.drawable.ir_tv_menu_down_down);
		showImageButton(ibtn_vedio_source, R.drawable.ir_tv_adv_vedio,
				R.drawable.ir_tv_adv_vedio_down,
				R.drawable.ir_tv_adv_vedio_notlearned,
				R.drawable.ir_tv_adv_vedio_notlearned_down);

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
		CallbackManager.getInstance().deleteObserver(KongtiaoTvControlActivity.this);
		cgiManager.deleteObserver(KongtiaoTvControlActivity.this);
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
				.setsharedPreferences(KongtiaoTvControlActivity.this);
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
				CallbackBeginLearnIRMessage learnIRMessage = (CallbackBeginLearnIRMessage) event
						.getData();
				int i = Integer.parseInt(learnIRMessage.getStatus());
				if (i == 0) {
					currentControl.IsLearn = "1";
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
						mHandler.sendEmptyMessage(INIT_KONGTIAO);
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
						mHandler.sendEmptyMessage(INIT_TV);
						writeLearnListToSharedPreferences(2);
					}
				} else {
					Toast.makeText(getApplicationContext(), "学习失败请重试",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				// if failed,prompt a Toast
				Toast.makeText(getApplicationContext(), "网络错误，学习失败请重试",
						Toast.LENGTH_SHORT).show();
			}
			mHandler.removeMessages(FINISH_DLG);
			mHandler.sendEmptyMessage(FINISH_DLG);
		} else if (EventType.GETDEVICELEARNED == event.getType()) {
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
						if (mtvControl.size() == 0
								|| mKongtiaoControl.size() == 0) {
							if (rc.Index.equals("6") || rc.Index.equals("7")
									|| rc.Index.equals("8")) {
								mKongtiaoControl.add(rc);
							} else {
								mtvControl.add(rc);
							}
						} else {
							if (rc.Index.equals("6") || rc.Index.equals("7")
									|| rc.Index.equals("8")) {
								int m = isInList(rc, mKongtiaoControl);
								if (-1 != m) {
									mKongtiaoControl.get(m).IsLearn = "1";
								}
							} else {
								int m = isInList(rc, mtvControl);
								if (-1 != m) {
									mtvControl.get(m).IsLearn = "1";
								}
							}
						}	
					}
					initView();
				}
			} else {
				// if failed,prompt a Toast

			}
		}
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		currentControl.IsLearn = "0";
		cgiManager.DeleteIR(mControlModel,
				Integer.parseInt(currentControl.Index));
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

	// 按键touch操作
	public class buttonLearnedOnTouchListenner implements OnTouchListener {
		int picname, picname_down;

		public buttonLearnedOnTouchListenner(int picturename,
				int picturename_down) {
			// TODO Auto-generated constructor stub
			picname = picturename;
			picname_down = picturename_down;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// 更改为按下时的背景图片
				v.setBackground(getResources().getDrawable(picname_down));
			} else if (event.getAction() == MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_MOVE) {
				// 改为抬起时, 延时改变0.5s,改变图片的图片
				touchDelayShow=new TouchDelayShow(v, picname);
				// 延时改变0.5s
				new Handler().postDelayed(new Runnable() {  
		            public void run() {  
		            	touchDelayShow.Show();
		            }  
		  
		        }, TOUCH_DELAY);  
			}
			return false;
		}
	}

	public class buttonNotLearnedOnTouchListenner implements OnTouchListener {
		int picname_notlearned, picname_notlearnd_down;

		public buttonNotLearnedOnTouchListenner(int picturename,
				int picturename_down) {
			// TODO Auto-generated constructor stub
			picname_notlearned = picturename;
			picname_notlearnd_down = picturename_down;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// 更改为按下时的背景图片
				v.setBackground(getResources().getDrawable(
						picname_notlearnd_down));
			} else if (event.getAction() == MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_MOVE) {
				
				touchDelayShow=new TouchDelayShow(v, picname_notlearned);
				// 延时改变0.5s,改变图片
				new Handler().postDelayed(new Runnable() {  
		            public void run() {  
		            	touchDelayShow.Show();
		            }  
		  
		        }, TOUCH_DELAY);  
			}
			return false;
		}
	}
	
	//TouchListener延时显示，参数传递
	public class TouchDelayShow {
		View view;
		int name;
		public TouchDelayShow(View v,int pname) {
			view=v;
			name=pname;
		}
		
		public void Show() {
			view.setBackgroundResource(name);
		}
	}

	// 按键显示
	public void showImageButton(ImageButton ibtn, int picture,
			int picture_down, int picture_notlearned,
			int picture_notlearned_down) {
		int picname, picname_down, picname_notlearned, picname_notlearnd_down;
		picname = picture;
		picname_down = picture_down;
		picname_notlearned = picture_notlearned;
		picname_notlearnd_down = picture_notlearned_down;

		if (ibtn.getTag() == null
				|| !((RemoteControl) ibtn.getTag()).IsLearn.trim().equals("1")) {
			ibtn.setBackgroundResource(picname_notlearned);
			ibtn.setOnTouchListener(new buttonNotLearnedOnTouchListenner(
					picname_notlearned, picname_notlearnd_down));
		} else {
			ibtn.setBackgroundResource(picname);
			ibtn.setOnTouchListener(new buttonLearnedOnTouchListenner(picname,
					picname_down));
		}
	}

}
