/* 
 * @ProjectName VideoGo
 * @Copyright HangZhou Hikvision System Technology Co.,Ltd. All Right Reserved
 * 
 * @FileName RealPlayActivity.java
 * @Description 这里对文件进行描述
 * 
 * @author chenxingyf1
 * @data 2014-6-11
 * 
 * @note 这里写本文件的详细功能描述和注释
 * @note 历史记录
 * 
 * @warning 这里写本文件的相关警告
 */
package com.gdgl.activity;

import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

//import com.videogo.open.R;
import com.videogo.constant.Config;
import com.videogo.constant.Constant;
import com.videogo.constant.IntentConsts;
import com.videogo.demo.DemoRealPlayer;
import com.videogo.device.DeviceInfoEx;
import com.videogo.exception.BaseException;
import com.videogo.exception.CASClientSDKException;
import com.videogo.exception.ErrorCode;
import com.videogo.exception.HCNetSDKException;
import com.videogo.exception.InnerException;
import com.videogo.exception.RtspClientException;
import com.videogo.exception.TTSClientSDKException;
import com.videogo.openapi.EzvizAPI;
import com.videogo.openapi.bean.resp.CameraInfo;
import com.videogo.realplay.RealPlayMsg;
import com.videogo.realplay.RealPlayStatus;
import com.videogo.realplay.RealPlayerHelper;
import com.videogo.realplay.RealPlayerManager;
import com.gdgl.smarthome.R;
import com.gdgl.util.AudioPlayUtil;
//import com.videogo.ui.util.OpenYSService;
//import com.videogo.ui.util.SecureValidate;
import com.videogo.util.ConnectionDetector;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.RotateViewUtil;
import com.videogo.util.SDCardUtil;
import com.videogo.util.Utils;
import com.videogo.voicetalk.VoiceTalkManager;
import com.videogo.widget.CustomRect;
import com.videogo.widget.CustomTouchListener;
import com.videogo.widget.RingView;
import com.videogo.widget.TitleBar;
//import com.videogo.widget.WaitDialog;

/**
 * 实时预览2.0
 * 
 * @author chenxingyf1
 * @data 2014-6-11
 */
public class YingShiRealPlayActivity extends ActionBarActivity implements OnClickListener, SurfaceHolder.Callback, Handler.Callback,
        OnTouchListener{
    private static final String TAG = "RealPlayActivity";

    // UI消息
    public static final int MSG_PLAY_UI_UPDATE = 200;
    
    public static final int MSG_HIDE_PTZ_DIRECTION = 201;

    private String mRtspUrl = null;
    private CameraInfo mCameraInfo = null;

    /** 实时预览控制对象 */
    private RealPlayerManager mRealPlayMgr = null;
    /** 演示点预览控制对象 */
    private DemoRealPlayer mDemoRealPlayer = null;

    private RealPlayerHelper mRealPlayerHelper = null;
    private AudioPlayUtil mAudioPlayUtil = null;
    private LocalInfo mLocalInfo = null;
    private Handler mHandler = null;
    private VoiceTalkManager mVoiceTalkManager = null;

    private float mRealRatio = Constant.LIVE_VIEW_RATIO;
    /** 标识是否正在播放 */
    private int mStatus = RealPlayStatus.STATUS_INIT;
    private boolean mIsOnStop = false;
    /** 屏幕当前方向 */
    private int mOrientation = Configuration.ORIENTATION_PORTRAIT;
    /** 存放上一次的流量 */
    private long mStreamFlow = 0;
    private long mTotalStreamFlow = 0;
    private Rect mRealPlayRect = null;

    private LinearLayout mRealPlayPageLy = null;
    //private TitleBar mTitleBar = null;
     private Toolbar mTitleBar = null;
     private ActionBar mActionBar;
    private Button mTiletRightBtn = null;
    private RelativeLayout mRealPlayPlayRl = null;

    private SurfaceView mRealPlaySv = null;
    private SurfaceHolder mRealPlaySh = null;
    private CustomTouchListener mRealPlayTouchListener = null;

    private LinearLayout mRealPlayLoadingLy = null;
    private LinearLayout mRealPlayLoadingPbLy = null;
    private TextView mRealPlayLoadingTv = null;
    private TextView mRealPlayTipTv = null;
    private ImageView mRealPlayIv = null;

    private RelativeLayout mRealPlayControlRl = null;
    private ImageButton mRealPlayBtn = null;
    private ImageButton mRealPlaySoundBtn = null;
    private TextView mRealPlayFlowTv = null;
    private int mControlDisplaySec = 0;
    // 播放比例
    private float mPlayScale = 1;

    private RelativeLayout mRealPlayCaptureRl = null;
    private RelativeLayout.LayoutParams mRealPlayCaptureRlLp = null;
    private ImageView mRealPlayCaptureIv = null;
    private ImageView mRealPlayCaptureWatermarkIv = null;
    private int mCaptureDisplaySec = 0;
    private LinearLayout mRealPlayRecordLy = null;
    private ImageView mRealPlayRecordIv = null;
    private TextView mRealPlayRecordTv = null;

    /** 录像文件路径 */
    private String mRecordFilePath = null;
    private String mRecordTime = null;
    /** 录像时间 */
    private int mRecordSecond = 0;

    private HorizontalScrollView mRealPlayOperateBar = null;
    private ImageButton mRealPlayTalkBtn = null;
    private ImageButton mRealPlayCaptureBtn = null;
    private ImageButton mRealPlayRecordBtn = null;
    private ImageButton mRealPlayRecordStartBtn = null;
    private View mRealPlayRecordContainer = null;
    private RotateViewUtil mRecordRotateViewUtil = null;

    private ImageButton mRealPlayQualityBtn = null;
    private ImageButton mRealPlayPtzBtn = null;

    private RelativeLayout mRealPlayFullOperateBar = null;
    private ImageButton mRealPlayFullPlayBtn = null;
    private ImageButton mRealPlayFullSoundBtn = null;
    private ImageButton mRealPlayFullCaptureBtn = null;
    private ImageButton mRealPlayFullRecordBtn = null;
    private ImageButton mRealPlayFullRecordStartBtn = null;
    private View mRealPlayFullRecordContainer = null;
    private LinearLayout mRealPlayFullFlowLy = null;
    private TextView mRealPlayFullRateTv = null;
    private TextView mRealPlayFullFlowTv = null;
    private TextView mRealPlayRatioTv = null;

    private PopupWindow mQualityPopupWindow = null;
    private PopupWindow mPtzPopupWindow = null;
    private LinearLayout mPtzControlLy = null;
    private PopupWindow mTalkPopupWindow = null;
    private RingView mTalkRingView = null;
    private Button mTalkBackControlBtn = null;
    private View toolbarCard;

  //  private WaitDialog mWaitDialog = null;

    /** 监听锁屏解锁的事件 */
    private ScreenBroadcastReceiver mScreenBroadcastReceiver = null;
    /** 定时器 */
    private Timer mUpdateTimer = null;
    /** 定时器执行的任务 */
    private TimerTask mUpdateTimerTask = null;

    // 弱提示预览信息
    private long mStartTime = 0;
    private long mStopTime = 0;
    
    // 云台控制状态
    private ImageView mRealPlayPtzDirectionIv = null;
    private float mZoomScale = 0;
    private int mCommand = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        initUI();
        mRealPlaySv.setVisibility(View.VISIBLE);
        if (mRtspUrl == null) {
            if (mCameraInfo == null) {
                return;
            }
            if (mCameraInfo.getStatus() == 1) {
                if (mStatus == RealPlayStatus.STATUS_INIT || mStatus == RealPlayStatus.STATUS_PAUSE) {
                    // 开始播放
                    startRealPlay();
                }
            } else {
                if (mStatus != RealPlayStatus.STATUS_STOP) {
                    stopRealPlay();
                }
                setRealPlayFailUI(getString(R.string.realplay_fail_device_not_exist));
            }
        } else {
            if (mStatus == RealPlayStatus.STATUS_INIT || mStatus == RealPlayStatus.STATUS_PAUSE) {
                // 开始播放
                startRealPlay();
            }
        }
        mIsOnStop = false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mCameraInfo == null && mRtspUrl == null) {
            return;
        }

        if (mStatus != RealPlayStatus.STATUS_STOP) {
            mIsOnStop = true;
            stopRealPlay();
            setRealPlayStopUI();
            mStatus = RealPlayStatus.STATUS_PAUSE;
        }
        mRealPlaySv.setVisibility(View.INVISIBLE);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        mHandler.removeMessages(MSG_PLAY_UI_UPDATE);
        if (mVoiceTalkManager != null) {
            mVoiceTalkManager.setHandler(null);
        }

        if (mScreenBroadcastReceiver != null) {
            // 取消锁屏广播的注册
            unregisterReceiver(mScreenBroadcastReceiver);
        }
    }

    // 初始化数据对象
    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mRtspUrl = intent.getStringExtra(IntentConsts.EXTRA_RTSP_URL);
            mCameraInfo = (CameraInfo) intent.getParcelableExtra(IntentConsts.EXTRA_CAMERA_INFO);
        }
        // 获取本地信息
        Application application = (Application) getApplication();
        mRealPlayerHelper = RealPlayerHelper.getInstance(application);
        mAudioPlayUtil = AudioPlayUtil.getInstance(application);
        // 获取配置信息操作对象
        mLocalInfo = LocalInfo.getInstance();
        // 获取屏幕参数
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mLocalInfo.setScreenWidthHeight(metric.widthPixels, metric.heightPixels);
        mLocalInfo.setNavigationBarHeight((int) Math.ceil(25 * getResources().getDisplayMetrics().density));

        mHandler = new Handler(this);
        mRecordRotateViewUtil = new RotateViewUtil();

        mScreenBroadcastReceiver = new ScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenBroadcastReceiver, filter);
    }

    /**
     * screen状态广播接收者
     */
    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                if (mStatus != RealPlayStatus.STATUS_STOP) {
                    stopRealPlay();
                    setRealPlayStopUI();
                    mStatus = RealPlayStatus.STATUS_PAUSE;
                }
            }
        }
    }

    // 初始化界面
    private void initView() {
        setContentView(R.layout.realplay_page);
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

       // mTitleBar = (TitleBar) findViewById(R.id.title_bar); 
        toolbarCard = (View)findViewById(R.id.toolbarCard);
        mTitleBar = (Toolbar) findViewById(R.id.toolbar_Action);
         setSupportActionBar(mTitleBar); 
         mActionBar = getSupportActionBar();
        /* mTitleBar.addBackButton(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mStatus != RealPlayStatus.STATUS_STOP) {
                    stopRealPlay();
                    setRealPlayStopUI();
                }
                finish();
            }
        });
        if (mCameraInfo != null) {
            mTiletRightBtn = mTitleBar.addRightButton(R.drawable.common_title_setup_selector, new OnClickListener() {

                @Override
                public void onClick(View v) {
                    EzvizAPI.getInstance().gotoSetDevicePage(mCameraInfo.getDeviceId(), mCameraInfo.getCameraId());
                }
            });
        }*/
        mRealPlayPageLy = (LinearLayout) findViewById(R.id.realplay_page_ly);
        /** 测量状态栏高度 **/
        ViewTreeObserver viewTreeObserver = mRealPlayPageLy.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mRealPlayRect == null) {
                    // 获取状况栏高度
                    mRealPlayRect = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(mRealPlayRect);
                }
            }
        });
        mRealPlayPlayRl = (RelativeLayout) findViewById(R.id.realplay_play_rl);
        mRealPlaySv = (SurfaceView) findViewById(R.id.realplay_sv);
        mRealPlaySv.getHolder().addCallback(this);
        mRealPlayTouchListener = new CustomTouchListener() {

            @Override
            public boolean canZoom(float scale) {
                if (mStatus == RealPlayStatus.STATUS_PLAY) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean canDrag(int direction) {
                if (mRealPlayMgr != null) {
                    // 出界判断
                    if (DRAG_LEFT == direction || DRAG_RIGHT == direction) {
                        // 左移/右移出界判断
                        if (mRealPlayMgr.getSupportPtzLeftRight() == 1) {
                            return true;
                        }
                    } else if (DRAG_UP == direction || DRAG_DOWN == direction) {
                        // 上移/下移出界判断
                        if (mRealPlayMgr.getSupportPtzTopBottom() == 1) {
                            return true;
                        }
                    }
                }
                return false;                
            }

            @Override
            public void onSingleClick() {
                onRealPlaySvClick();
            }

            @Override
            public void onDoubleClick(MotionEvent e) {
            }

            @Override
            public void onZoom(float scale) {
                LogUtil.debugLog(TAG, "onZoom:" + scale);
                if (mRealPlayMgr != null && mRealPlayMgr.getSupportPtzZoom() == 1) {
                    startZoom(scale);
                }
            }

            @Override
            public void onDrag(int direction, float distance, float rate) {
                LogUtil.debugLog(TAG, "onDrag:" + direction);
                if (mRealPlayMgr != null) {
                    //Utils.showLog(RealPlayActivity.this, "onDrag rate:" + rate);
                    startDrag(direction, distance, rate);
                }
            }

            @Override
            public void onEnd(int mode) {
                LogUtil.debugLog(TAG, "onEnd:" + mode);
                if (mRealPlayMgr != null) {
                    stopDrag(false);
                }
                if (mRealPlayMgr != null && mRealPlayMgr.getSupportPtzZoom() == 1) {
                    stopZoom();
                }
            }
            
            @Override
            public void onZoomChange(float scale, CustomRect oRect, CustomRect curRect) {
                LogUtil.debugLog(TAG, "onZoomChange:" + scale);
                if (mRealPlayMgr != null && mRealPlayMgr.getSupportPtzZoom() == 1) {
                    //采用云台调焦
                    return;
                }
                if (mStatus == RealPlayStatus.STATUS_PLAY) {
                    if (scale > 1.0f && scale < 1.1f) {
                        scale = 1.1f;
                    }
                    setPlayScaleUI(scale, oRect, curRect);
                }
            }
        };
        mRealPlaySv.setOnTouchListener(mRealPlayTouchListener);
        mRealPlayPtzDirectionIv = (ImageView) findViewById(R.id.realplay_ptz_direction_iv);
        mRealPlayLoadingLy = (LinearLayout) findViewById(R.id.realplay_loading_ly);
        mRealPlayLoadingPbLy = (LinearLayout) findViewById(R.id.realplay_loading_pb_ly);
        mRealPlayLoadingTv = (TextView) findViewById(R.id.realplay_loading_tv);
        mRealPlayTipTv = (TextView) findViewById(R.id.realplay_tip_tv);
        mRealPlayIv = (ImageView) findViewById(R.id.realplay_play_iv);

        mRealPlayControlRl = (RelativeLayout) findViewById(R.id.realplay_control_rl);
        mRealPlayBtn = (ImageButton) findViewById(R.id.realplay_play_btn);
        mRealPlaySoundBtn = (ImageButton) findViewById(R.id.realplay_sound_btn);
        mRealPlayFlowTv = (TextView) findViewById(R.id.realplay_flow_tv);
        mRealPlayFlowTv.setText("0k/s 0MB");

        mRealPlayCaptureRl = (RelativeLayout) findViewById(R.id.realplay_capture_rl);
        mRealPlayCaptureRlLp = (RelativeLayout.LayoutParams) mRealPlayCaptureRl.getLayoutParams();
        mRealPlayCaptureIv = (ImageView) findViewById(R.id.realplay_capture_iv);
        mRealPlayCaptureWatermarkIv = (ImageView) findViewById(R.id.realplay_capture_watermark_iv);
        mRealPlayRecordLy = (LinearLayout) findViewById(R.id.realplay_record_ly);
        mRealPlayRecordIv = (ImageView) findViewById(R.id.realplay_record_iv);
        mRealPlayRecordTv = (TextView) findViewById(R.id.realplay_record_tv);

        mRealPlayOperateBar = (HorizontalScrollView) findViewById(R.id.realplay_operate_bar);

        mRealPlayTalkBtn = (ImageButton) findViewById(R.id.realplay_talk_btn);
        mRealPlayCaptureBtn = (ImageButton) findViewById(R.id.realplay_previously_btn);
        mRealPlayRecordContainer = findViewById(R.id.realplay_video_container);
        mRealPlayRecordBtn = (ImageButton) findViewById(R.id.realplay_video_btn);
        mRealPlayRecordStartBtn = (ImageButton) findViewById(R.id.realplay_video_start_btn);

        mRealPlayQualityBtn = (ImageButton) findViewById(R.id.realplay_quality_btn);
        mRealPlayPtzBtn = (ImageButton) findViewById(R.id.realplay_ptz_btn);

        mRealPlayFullOperateBar = (RelativeLayout) findViewById(R.id.realplay_full_operate_bar);
        mRealPlayFullPlayBtn = (ImageButton) findViewById(R.id.realplay_full_play_btn);
        mRealPlayFullSoundBtn = (ImageButton) findViewById(R.id.realplay_full_sound_btn);
        mRealPlayFullCaptureBtn = (ImageButton) findViewById(R.id.realplay_full_previously_btn);
        mRealPlayFullRecordContainer = findViewById(R.id.realplay_full_video_container);
        mRealPlayFullRecordBtn = (ImageButton) findViewById(R.id.realplay_full_video_btn);
        mRealPlayFullRecordStartBtn = (ImageButton) findViewById(R.id.realplay_full_video_start_btn);

        mRealPlayFullFlowLy = (LinearLayout) findViewById(R.id.realplay_full_flow_ly);
        mRealPlayFullRateTv = (TextView) findViewById(R.id.realplay_full_rate_tv);
        mRealPlayFullFlowTv = (TextView) findViewById(R.id.realplay_full_flow_tv);
        mRealPlayRatioTv = (TextView) findViewById(R.id.realplay_ratio_tv);
        mRealPlayFullRateTv.setText("0k/s");
        mRealPlayFullFlowTv.setText("0MB");

        //mWaitDialog = new WaitDialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        //mWaitDialog.setCancelable(false);

        if (mRtspUrl == null) {
            LinearLayout.LayoutParams realPlayPlayRlLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            realPlayPlayRlLp.gravity = Gravity.CENTER;
            mRealPlayPlayRl.setLayoutParams(realPlayPlayRlLp);
            mRealPlayOperateBar.setVisibility(View.VISIBLE);
        } else {
            mRealPlayPageLy.setPadding(0, 0, 0, Utils.dip2px(this, 80));
            mRealPlayPlayRl.setBackgroundColor(getResources().getColor(R.color.common_bg));
            LinearLayout.LayoutParams realPlayPlayRlLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            realPlayPlayRlLp.gravity = Gravity.CENTER;
            realPlayPlayRlLp.weight = 1;
            mRealPlayPlayRl.setLayoutParams(realPlayPlayRlLp);
            mRealPlayOperateBar.setVisibility(View.GONE);
        }
        
        initOperateBarUI();
        setRealPlaySvLayout();
    }
    
    public void startDrag(int direction, float distance, float rate) {
        if (mRealPlayMgr == null) {
            return;
        }

        mRealPlayControlRl.setVisibility(View.GONE);
        mRealPlayFullOperateBar.setVisibility(View.GONE);
        int command = -1;
        switch (direction) {
            case CustomTouchListener.DRAG_LEFT:
                command = RealPlayStatus.PTZ_LEFT;
                break;
            case CustomTouchListener.DRAG_RIGHT:
                command = RealPlayStatus.PTZ_RIGHT;
                break;
            case CustomTouchListener.DRAG_UP:
                command = RealPlayStatus.PTZ_UP;
                break;
            case CustomTouchListener.DRAG_DOWN:
                command = RealPlayStatus.PTZ_DOWN;
                break;
            default:
                break;
        }

        if (mCommand != -1 && mCommand != command) {
            LogUtil.debugLog(TAG, "startDrag stop:" + mCommand);
            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, mCommand, RealPlayStatus.PTZ_SPEED_DEFAULT, false);
            mCommand = -1;
        }

        if (command != -1 && mCommand != command) {
            mCommand = command;
            setPtzDirectionIv(mCommand);
            LogUtil.debugLog(TAG, "startDrag start:" + mCommand);
            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, mCommand, RealPlayStatus.PTZ_SPEED_DEFAULT, true);
        }
    }

    public void stopDrag(boolean control) {
        if (mRealPlayMgr == null) {
            return;
        }

        if (mCommand != -1) {
            LogUtil.debugLog(TAG, "stopDrag stop:" + mCommand);
            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, mCommand, RealPlayStatus.PTZ_SPEED_DEFAULT, false);
            mCommand = -1;
            if (control) {
                setPtzDirectionIv(mCommand);
            }
        }
    }

    private void startZoom(float scale) {
        if (mRealPlayMgr == null) {
            return;
        }

        mRealPlayControlRl.setVisibility(View.GONE);
        mRealPlayFullOperateBar.setVisibility(View.GONE);
        boolean preZoomIn = mZoomScale > 1.01 ? true : false;
        boolean zoomIn = scale > 1.01 ? true : false;
        if (mZoomScale != 0 && preZoomIn != zoomIn) {
            LogUtil.debugLog(TAG, "startZoom stop:" + mZoomScale);
            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, mZoomScale > 1.01 ? RealPlayStatus.PTZ_ZOOMIN
                    : RealPlayStatus.PTZ_ZOOMOUT, RealPlayStatus.PTZ_SPEED_DEFAULT, false);
            mZoomScale = 0;
        }
        if (scale != 0 && (mZoomScale == 0 || preZoomIn != zoomIn)) {
            mZoomScale = scale;
            LogUtil.debugLog(TAG, "startZoom start:" + mZoomScale);
            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, mZoomScale > 1.01 ? RealPlayStatus.PTZ_ZOOMIN
                    : RealPlayStatus.PTZ_ZOOMOUT, RealPlayStatus.PTZ_SPEED_DEFAULT, true);
        }
    }

    private void stopZoom() {
        if (mRealPlayMgr == null) {
            return;
        }
        if (mZoomScale != 0) {
            LogUtil.debugLog(TAG, "stopZoom stop:" + mZoomScale);
            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, mZoomScale > 1.01 ? RealPlayStatus.PTZ_ZOOMIN
                    : RealPlayStatus.PTZ_ZOOMOUT, RealPlayStatus.PTZ_SPEED_DEFAULT, false);
            mZoomScale = 0;
        }
    }
    
    private void setPtzDirectionIv(int command) {
        setPtzDirectionIv(command, 0);
    }
    
    private void setPtzDirectionIv(int command, int errorCode) {
        if (command != -1 && errorCode == 0) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            switch (command) {
                case RealPlayStatus.PTZ_LEFT:
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.left_twinkle);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_RIGHT:
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.right_twinkle);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_UP:
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.up_twinkle);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_DOWN:
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.down_twinkle);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.realplay_sv);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                default:
                    break;
            }
            mRealPlayPtzDirectionIv.setVisibility(View.VISIBLE);
            mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
            Message msg = new Message();
            msg.what = MSG_HIDE_PTZ_DIRECTION;
            msg.arg1 = 1;
            mHandler.sendMessageDelayed(msg, 500);
        } else if(errorCode != 0) {
            RelativeLayout.LayoutParams svParams = (RelativeLayout.LayoutParams)mRealPlaySv.getLayoutParams();
            RelativeLayout.LayoutParams params = null;
            switch (errorCode) {
                case ErrorCode.ERROR_CAS_PTZ_ROTATION_LEFT_LIMIT_FAILED:   
                    params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                            svParams.height);
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.ptz_left_limit);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case ErrorCode.ERROR_CAS_PTZ_ROTATION_RIGHT_LIMIT_FAILED:
                    params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                            svParams.height);
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.ptz_right_limit);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case ErrorCode.ERROR_CAS_PTZ_ROTATION_UP_LIMIT_FAILED:
                    params = new RelativeLayout.LayoutParams(svParams.width,
                            LayoutParams.WRAP_CONTENT);
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.ptz_top_limit);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                case ErrorCode.ERROR_CAS_PTZ_ROTATION_DOWN_LIMIT_FAILED:
                    params = new RelativeLayout.LayoutParams(svParams.width,
                            LayoutParams.WRAP_CONTENT);
                    mRealPlayPtzDirectionIv.setBackgroundResource(R.drawable.ptz_bottom_limit);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.realplay_sv);
                    mRealPlayPtzDirectionIv.setLayoutParams(params);
                    break;
                default:
                    break;
            }
            mRealPlayPtzDirectionIv.setVisibility(View.VISIBLE);
            mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
            Message msg = new Message();
            msg.what = MSG_HIDE_PTZ_DIRECTION;
            msg.arg1 = 1;
            mHandler.sendMessageDelayed(msg, 500);            
        } else {
            mRealPlayPtzDirectionIv.setVisibility(View.GONE);
            mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        }
    }
    
    // 初始化UI
    @SuppressWarnings("deprecation")
    private void initUI() {
        if (mCameraInfo != null) {
        	mActionBar.setTitle(mCameraInfo.getCameraName());
           /* if (mCameraInfo.getIsShared() >= 2) {
                mTiletRightBtn.setVisibility(View.INVISIBLE);
            } else {
                mTiletRightBtn.setVisibility(View.VISIBLE);
            }*/
        	if (mLocalInfo.isSoundOpen()) {
	            mRealPlaySoundBtn.setBackgroundResource(R.drawable.remote_list_soundon_btn_selector);
	            mRealPlayFullSoundBtn.setBackgroundResource(R.drawable.play_full_soundon_btn_selector);
	        } else {
	            mRealPlaySoundBtn.setBackgroundResource(R.drawable.remote_list_soundoff_btn_selector);
	            mRealPlayFullSoundBtn.setBackgroundResource(R.drawable.play_full_soundoff_btn_selector);
	        }
        } else if(mRtspUrl != null) {
            String cameraName = Utils.getUrlValue(mRtspUrl, "cameraname=", "&");
            if(cameraName != null) {
            	mActionBar.setTitle(URLDecoder.decode(cameraName));
            }
            String soundType = Utils.getUrlValue(mRtspUrl, "soundtype=", "&");
            if(soundType == null || TextUtils.equals(soundType, "0")) {
                mRealPlaySoundBtn.setBackgroundResource(R.drawable.remote_list_soundoff_btn_selector);
                mRealPlayFullSoundBtn.setBackgroundResource(R.drawable.play_full_soundoff_btn_selector);
            } else {
                mRealPlaySoundBtn.setBackgroundResource(R.drawable.remote_list_soundon_btn_selector);
                mRealPlayFullSoundBtn.setBackgroundResource(R.drawable.play_full_soundon_btn_selector);
            }
        }

    }

    private void initOperateBarUI() {
        int imgWidth = Utils.dip2px(this, 64);
        int leftMargin = (int)(mLocalInfo.getScreenWidth() - (3.5 * imgWidth))/4;
        leftMargin = Math.max(Utils.dip2px(this, 25), leftMargin);
        leftMargin = Math.min(Utils.dip2px(this, 50), leftMargin);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mRealPlayPtzBtn.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp = (LinearLayout.LayoutParams)mRealPlayTalkBtn.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp = (LinearLayout.LayoutParams)mRealPlayQualityBtn.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp = (LinearLayout.LayoutParams)mRealPlayCaptureBtn.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp = (LinearLayout.LayoutParams)mRealPlayRecordContainer.getLayoutParams();
        lp.leftMargin = leftMargin;
        lp = (LinearLayout.LayoutParams)mRealPlayQualityBtn.getLayoutParams();
        lp.leftMargin = leftMargin; 
    }
    
    private void setVideoLevel() {
        if (mCameraInfo == null || mCameraInfo.getStatus() != 1) {
            mRealPlayQualityBtn.setEnabled(false);
        }
        if (mRealPlayMgr == null) {
            mRealPlayQualityBtn.setEnabled(false);
            return;
        }
        // 视频质量，2-高清，1-标清，0-流畅
        if (mRealPlayMgr.getVideoLevel() == RealPlayerManager.VIDEO_LEVEL_FLUNET) {
            mRealPlayQualityBtn.setBackgroundResource(R.drawable.play_flunet_selector);
        } else if (mRealPlayMgr.getVideoLevel() == RealPlayerManager.VIDEO_LEVEL_BALANCED) {
            mRealPlayQualityBtn.setBackgroundResource(R.drawable.play_balanced_selector);
        } else if (mRealPlayMgr.getVideoLevel() == RealPlayerManager.VIDEO_LEVEL_HD) {
            mRealPlayQualityBtn.setBackgroundResource(R.drawable.play_hd_selector);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mOrientation = newConfig.orientation;
        onOrientationChanged();
        super.onConfigurationChanged(newConfig);
    }

    private void updateOperatorUI() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            // 显示状态栏
            fullScreen(false);
            toolbarCard.setVisibility(View.VISIBLE);
            mTitleBar.setVisibility(View.VISIBLE);
            if (mRtspUrl == null) {
                mRealPlayOperateBar.setVisibility(View.VISIBLE);
            } else {
                mRealPlayPageLy.setPadding(0, 0, 0, Utils.dip2px(this, 80));
                mRealPlayPlayRl.setBackgroundColor(getResources().getColor(R.color.common_bg));
                mRealPlayOperateBar.setVisibility(View.GONE);
            }
            mRealPlayFullOperateBar.setVisibility(View.GONE);
            if (mCameraInfo != null && mCameraInfo.getStatus() != 1) {
                mRealPlayControlRl.setVisibility(View.GONE);
            } else {
                mRealPlayControlRl.setVisibility(View.VISIBLE);
            }
            if (mRecordFilePath != null) {
                mRealPlayRecordBtn.setVisibility(View.GONE);
                mRealPlayRecordStartBtn.setVisibility(View.VISIBLE);
            } else {
                mRealPlayRecordBtn.setVisibility(View.VISIBLE);
                mRealPlayRecordStartBtn.setVisibility(View.GONE);
            }
        } else {
            // 隐藏状态栏
            fullScreen(true);
            toolbarCard.setVisibility(View.GONE);
            mTitleBar.setVisibility(View.GONE);
            mRealPlayOperateBar.setVisibility(View.GONE);
            if (mRtspUrl == null) {
                mRealPlayFullOperateBar.setVisibility(View.GONE);
                mRealPlayControlRl.setVisibility(View.GONE);
            } else {
                mRealPlayPageLy.setPadding(0, 0, 0, 0);
                mRealPlayPlayRl.setBackgroundColor(getResources().getColor(R.color.black_bg));
                mRealPlayFullOperateBar.setVisibility(View.GONE);
                mRealPlayControlRl.setVisibility(View.VISIBLE);
            }
            if (mRecordFilePath != null) {
                mRealPlayFullRecordBtn.setVisibility(View.GONE);
                mRealPlayFullRecordStartBtn.setVisibility(View.VISIBLE);
            } else {
                mRealPlayFullRecordBtn.setVisibility(View.VISIBLE);
                mRealPlayFullRecordStartBtn.setVisibility(View.GONE);
            }
        }
    }

    private void fullScreen(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void onOrientationChanged() {
        mRealPlaySv.setVisibility(View.INVISIBLE);
        setRealPlaySvLayout();
        mRealPlaySv.setVisibility(View.VISIBLE);

        updateOperatorUI();
        updateCaptureUI();
    }

    /*
     * (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int,
     * int)
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mRealPlayMgr != null) {
            mRealPlayMgr.setPlaySurface(holder);
        } else if (mDemoRealPlayer != null) {
            mDemoRealPlayer.setPlaySurface(holder);
        }
        mRealPlaySh = holder;
    }

    /*
     * (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mRealPlayMgr != null) {
            mRealPlayMgr.setPlaySurface(null);
        } else if (mDemoRealPlayer != null) {
            mDemoRealPlayer.setPlaySurface(null);
        }
        mRealPlaySh = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mStatus != RealPlayStatus.STATUS_STOP) {
                stopRealPlay();
                setRealPlayStopUI();
            }
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /*
     * (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.realplay_play_btn:
            case R.id.realplay_full_play_btn:
            case R.id.realplay_play_iv:
                if (mStatus != RealPlayStatus.STATUS_STOP) {
                    stopRealPlay();
                    setRealPlayStopUI();
                } else {
                    startRealPlay();
                }
                break;
            case R.id.realplay_previously_btn:
            case R.id.realplay_full_previously_btn:
                onCapturePicBtnClick();
                break;
            case R.id.realplay_capture_rl:
                onCaptureRlClick();
                break;
            case R.id.realplay_video_btn:
            case R.id.realplay_video_start_btn:
            case R.id.realplay_full_video_btn:
            case R.id.realplay_full_video_start_btn:
                onRecordBtnClick();
                break;
            case R.id.realplay_talk_btn:
                startVoiceTalk();
                break;
            case R.id.realplay_quality_btn:
                openQualityPopupWindow(mRealPlayPageLy);
                break;
            case R.id.realplay_ptz_btn:
                openPtzPopupWindow(mRealPlayPageLy);
                break;
            case R.id.realplay_sound_btn:
            case R.id.realplay_full_sound_btn:
                onSoundBtnClick();
                break;
            default:
                break;
        }
    }
    
    private void onSoundBtnClick() {
        if (mLocalInfo.isSoundOpen()) {
            mLocalInfo.setSoundOpen(false);
            mRealPlaySoundBtn.setBackgroundResource(R.drawable.remote_list_soundoff_btn_selector);
            mRealPlayFullSoundBtn.setBackgroundResource(R.drawable.play_full_soundoff_btn_selector);
        } else {
            mLocalInfo.setSoundOpen(true);
            mRealPlaySoundBtn.setBackgroundResource(R.drawable.remote_list_soundon_btn_selector);
            mRealPlayFullSoundBtn.setBackgroundResource(R.drawable.play_full_soundon_btn_selector);
        }

        setRealPlaySound();
    }

    private void setRealPlaySound() {
        if (mRealPlayMgr != null) {
            if (mLocalInfo.isSoundOpen()) {
                mRealPlayMgr.openSound();
            } else {
                mRealPlayMgr.closeSound();
            }
        } else if (mDemoRealPlayer != null) {
            if (mLocalInfo.isSoundOpen()) {
                mDemoRealPlayer.openSound();
            } else {
                mDemoRealPlayer.closeSound();
            }
        }
    }
    
    /**
     * 设备对讲
     * 
     * @see
     * @since V2.0
     */
    private void startVoiceTalk() {
        LogUtil.debugLog(TAG, "startVoiceTalk");
        if (mCameraInfo == null) {
            return;
        }

        Utils.showToast(this, R.string.start_voice_talk);
        mRealPlayTalkBtn.setEnabled(false);
        if(mRealPlayMgr != null) {
            mRealPlayMgr.closeSound();
        }
        mVoiceTalkManager = new VoiceTalkManager(this);
        mVoiceTalkManager.setHandler(mHandler);
        mRealPlayerHelper.startVoiceTalkTask(mVoiceTalkManager, mCameraInfo.getCameraId(), 
                mRealPlayMgr!=null?mRealPlayMgr.getRealPlayType():RealPlayerManager.VIDEO_PLAY_TYPE_RTSP);
    }

    /**
     * 停止对讲
     * 
     * @see
     * @since V2.0
     */
    private void stopVoiceTalk() {
        if (mCameraInfo == null) {
            return;
        }
        LogUtil.debugLog(TAG, "stopVoiceTalk");

        if (mVoiceTalkManager != null) {
            mRealPlayerHelper.stopVoiceTalkTask(mVoiceTalkManager);
        }
        handleVoiceTalkStoped();
    }

    private OnClickListener mOnPopWndClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.quality_close_btn:
                    closeQualityPopupWindow();
                    break;
                case R.id.quality_hd_btn:
                    setQualityMode(RealPlayerManager.VIDEO_LEVEL_HD);
                    break;
                case R.id.quality_balanced_btn:
                    setQualityMode(RealPlayerManager.VIDEO_LEVEL_BALANCED);
                    break;
                case R.id.quality_flunet_btn:
                    setQualityMode(RealPlayerManager.VIDEO_LEVEL_FLUNET);
                case R.id.ptz_close_btn:
                    closePtzPopupWindow();
                    break;
                case R.id.ptz_flip_btn:
                    setPtzFlip();
                    break;
                case R.id.talkback_close_btn:
                    closeTalkPopupWindow();
                    break;
                default:
                    break;
            }
        }
    };

    private OnTouchListener mOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionevent) {
            int action = motionevent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    switch (view.getId()) {
                        case R.id.talkback_control_btn:
                            mTalkRingView.setVisibility(View.VISIBLE);
                            if (mVoiceTalkManager != null) {
                                mVoiceTalkManager.setVoiceTalkStatus(true);
                            }
                            break;
                        case R.id.ptz_top_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_up_sel);
                            setPtzDirectionIv(RealPlayStatus.PTZ_UP);
                            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, RealPlayStatus.PTZ_UP, true);
                            break;
                        case R.id.ptz_bottom_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bottom_sel);
                            setPtzDirectionIv(RealPlayStatus.PTZ_DOWN);
                            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, RealPlayStatus.PTZ_DOWN, true);
                            break;
                        case R.id.ptz_left_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_left_sel);
                            setPtzDirectionIv(RealPlayStatus.PTZ_LEFT);
                            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, RealPlayStatus.PTZ_LEFT, true);
                            break;
                        case R.id.ptz_right_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_right_sel);
                            setPtzDirectionIv(RealPlayStatus.PTZ_RIGHT);
                            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, RealPlayStatus.PTZ_RIGHT, true);
                            break;
                        default:
                            break;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    switch (view.getId()) {
                        case R.id.talkback_control_btn:
                            if (mVoiceTalkManager != null) {
                                mVoiceTalkManager.setVoiceTalkStatus(false);
                            }
                            mTalkRingView.setVisibility(View.GONE);
                            break;
                        case R.id.ptz_top_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, RealPlayStatus.PTZ_UP, false);
                            break;
                        case R.id.ptz_bottom_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, RealPlayStatus.PTZ_DOWN, false);
                            break;
                        case R.id.ptz_left_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, RealPlayStatus.PTZ_LEFT, false);
                            break;
                        case R.id.ptz_right_btn:
                            mPtzControlLy.setBackgroundResource(R.drawable.ptz_bg);
                            mRealPlayerHelper.setPtzCommand(mRealPlayMgr, mHandler, RealPlayStatus.PTZ_RIGHT, false);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private void setPtzFlip() {
        if (!ConnectionDetector.isNetworkAvailable(YingShiRealPlayActivity.this)) {
            Utils.showToast(this, R.string.local_network_exception);
            return;
        }
        if (mRealPlayMgr != null) {
            mRealPlayerHelper.setPtzFlip(mRealPlayMgr, mHandler);
        }
    }

    /**
     * 码流配置 清晰度 2-高清，1-标清，0-流畅
     * 
     * @see
     * @since V2.0
     */
    private void setQualityMode(final int mode) {
        // 检查网络是否可用
        if (!ConnectionDetector.isNetworkAvailable(YingShiRealPlayActivity.this)) {
            // 提示没有连接网络
            Utils.showToast(this, R.string.realplay_set_fail_network);
            return;
        }

        if (mRealPlayMgr != null) {
            //mWaitDialog.setWaitText(this.getString(R.string.setting_video_level));
            //mWaitDialog.show();
            mRealPlayerHelper.setVedioModeTask(mRealPlayMgr, mHandler, mode);
        }
    }

    /**
     * 打开对讲控制窗口
     * 
     * @see
     * @since V1.8.3
     */
    private void openTalkPopupWindow(View parent) {
        if (mRealPlayMgr == null) {
            return;
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layoutView = (ViewGroup) layoutInflater.inflate(R.layout.realplay_talkback_wnd, null, true);
        layoutView.setFocusable(true);
        layoutView.setFocusableInTouchMode(true); 
        layoutView.setOnKeyListener(new OnKeyListener(){  
           @Override  
           public boolean onKey(View arg0, int arg1, KeyEvent arg2) {  
               if (arg1 == KeyEvent.KEYCODE_BACK){  
                   LogUtil.infoLog(TAG, "KEYCODE_BACK DOWN");
                   closeTalkPopupWindow();
               }  
               return false;   
           }  
        }); 
        
        ImageButton talkbackCloseBtn = (ImageButton) layoutView.findViewById(R.id.talkback_close_btn);
        talkbackCloseBtn.setOnClickListener(mOnPopWndClickListener);
        mTalkRingView = (RingView) layoutView.findViewById(R.id.talkback_rv);
        mTalkBackControlBtn = (Button) layoutView.findViewById(R.id.talkback_control_btn);
        mTalkBackControlBtn.setOnTouchListener(mOnTouchListener);

        if (mRealPlayMgr.getSupportTalk() == DeviceInfoEx.TALK_FULL_DUPLEX) {
            mTalkRingView.setVisibility(View.VISIBLE);
            mTalkBackControlBtn.setEnabled(false);
            mTalkBackControlBtn.setText(R.string.talking);
        }

        int height = mLocalInfo.getScreenHeight() - mTitleBar.getHeight() - mRealPlayPlayRl.getHeight()
                - (mRealPlayRect != null ? mRealPlayRect.top : mLocalInfo.getNavigationBarHeight());
        mTalkPopupWindow = new PopupWindow(layoutView, LayoutParams.MATCH_PARENT, height, true);
//        mTalkPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mTalkPopupWindow.setAnimationStyle(R.style.popwindowUpAnim);
        mTalkPopupWindow.setFocusable(true);
        mTalkPopupWindow.setOutsideTouchable(false);
        mTalkPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
//        mTalkPopupWindow.setOnDismissListener(new OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//                LogUtil.infoLog(TAG, "KEYCODE_BACK DOWN");
//                closeTalkPopupWindow();
//            }
//        });
        mTalkPopupWindow.update();
        mTalkRingView.post(new Runnable() {
            @Override
            public void run() {
                if(mTalkRingView != null) {
                    mTalkRingView.setMinRadiusAndDistance(mTalkBackControlBtn.getHeight() / 2f, 
                            Utils.dip2px(YingShiRealPlayActivity.this, 22));
                }
            }
        });
    }

    private void closeTalkPopupWindow() {
        if (mTalkPopupWindow != null) {
            LogUtil.infoLog(TAG, "closeTalkPopupWindow");
            mTalkRingView = null;
            dismissPopWindow(mTalkPopupWindow);
            mTalkPopupWindow = null;
        }
        stopVoiceTalk();
        if (mStatus == RealPlayStatus.STATUS_PLAY) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }

    /**
     * 打开云台控制窗口
     * 
     * @see
     * @since V1.8.3
     */
    private void openPtzPopupWindow(View parent) {
        closePtzPopupWindow();
        if (mRealPlayMgr == null) {
            return;
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layoutView = (ViewGroup) layoutInflater.inflate(R.layout.realplay_ptz_wnd, null, true);

        mPtzControlLy = (LinearLayout) layoutView.findViewById(R.id.ptz_control_ly);
        ImageButton ptzCloseBtn = (ImageButton) layoutView.findViewById(R.id.ptz_close_btn);
        ptzCloseBtn.setOnClickListener(mOnPopWndClickListener);
        ImageButton ptzTopBtn = (ImageButton) layoutView.findViewById(R.id.ptz_top_btn);
        ptzTopBtn.setOnTouchListener(mOnTouchListener);
        ImageButton ptzBottomBtn = (ImageButton) layoutView.findViewById(R.id.ptz_bottom_btn);
        ptzBottomBtn.setOnTouchListener(mOnTouchListener);
        ImageButton ptzLeftBtn = (ImageButton) layoutView.findViewById(R.id.ptz_left_btn);
        ptzLeftBtn.setOnTouchListener(mOnTouchListener);
        ImageButton ptzRightBtn = (ImageButton) layoutView.findViewById(R.id.ptz_right_btn);
        ptzRightBtn.setOnTouchListener(mOnTouchListener);
        ImageButton ptzFlipBtn = (ImageButton) layoutView.findViewById(R.id.ptz_flip_btn);
        ptzFlipBtn.setOnClickListener(mOnPopWndClickListener);

        int height = mLocalInfo.getScreenHeight() - mTitleBar.getHeight() - mRealPlayPlayRl.getHeight()
                - (mRealPlayRect != null ? mRealPlayRect.top : mLocalInfo.getNavigationBarHeight());
        mPtzPopupWindow = new PopupWindow(layoutView, LayoutParams.MATCH_PARENT, height, true);
        mPtzPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPtzPopupWindow.setAnimationStyle(R.style.popwindowUpAnim);
        mPtzPopupWindow.setFocusable(true);
        mPtzPopupWindow.setOutsideTouchable(false);
        mPtzPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        mPtzPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                LogUtil.infoLog(TAG, "KEYCODE_BACK DOWN");
                mPtzPopupWindow = null;
                mPtzControlLy = null;
                closePtzPopupWindow();
            }
        });
        mPtzPopupWindow.update();
    }

    private void closePtzPopupWindow() {
        if (mPtzPopupWindow != null) {
            dismissPopWindow(mPtzPopupWindow);
            mPtzPopupWindow = null;
            mPtzControlLy = null;
        }
        if (mStatus == RealPlayStatus.STATUS_PLAY) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }

    /**
     * 打开清晰度设置窗口
     * 
     * @see
     * @since V1.8.3
     */
    private void openQualityPopupWindow(View parent) {
        closeQualityPopupWindow();
        if (mRealPlayMgr == null) {
            return;
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layoutView = (ViewGroup) layoutInflater.inflate(R.layout.realplay_quality_wnd, null, true);

        ImageButton qualityCloseBtn = (ImageButton) layoutView.findViewById(R.id.quality_close_btn);
        qualityCloseBtn.setOnClickListener(mOnPopWndClickListener);
        ImageButton qualityHdBtn = (ImageButton) layoutView.findViewById(R.id.quality_hd_btn);
        qualityHdBtn.setOnClickListener(mOnPopWndClickListener);
        ImageButton qualityBalancedBtn = (ImageButton) layoutView.findViewById(R.id.quality_balanced_btn);
        qualityBalancedBtn.setOnClickListener(mOnPopWndClickListener);
        ImageButton qualityFlunetBtn = (ImageButton) layoutView.findViewById(R.id.quality_flunet_btn);
        qualityFlunetBtn.setOnClickListener(mOnPopWndClickListener);

        // 视频质量，2-高清，1-标清，0-流畅
        if (mRealPlayMgr.getVideoLevel() == RealPlayerManager.VIDEO_LEVEL_FLUNET) {
            qualityFlunetBtn.setBackgroundResource(R.drawable.play_flunet_sel);
            qualityFlunetBtn.setEnabled(false);
        } else if (mRealPlayMgr.getVideoLevel() == RealPlayerManager.VIDEO_LEVEL_BALANCED) {
            qualityBalancedBtn.setBackgroundResource(R.drawable.play_balanced_sel);
            qualityBalancedBtn.setEnabled(false);
        } else if (mRealPlayMgr.getVideoLevel() == RealPlayerManager.VIDEO_LEVEL_HD) {
            qualityHdBtn.setBackgroundResource(R.drawable.play_hd_sel);
            qualityHdBtn.setEnabled(false);
        }

        // 2-2-1 支持低中高，低中为子码流，高为主码流；2-1-0 支持低中，不支持高品质，低为子码流，中为主码流
        String capability = mRealPlayMgr.getCapability();
        LogUtil.debugLog(TAG, "capability=" + capability);
        String[] quality = capability.split("-");
        if (Integer.parseInt(quality[0]) == 0) {
            qualityFlunetBtn.setVisibility(View.GONE);
        } else {
            qualityFlunetBtn.setVisibility(View.VISIBLE);
        }
        if (Integer.parseInt(quality[1]) == 0) {
            qualityBalancedBtn.setVisibility(View.GONE);
        } else {
            qualityBalancedBtn.setVisibility(View.VISIBLE);
        }
        if (Integer.parseInt(quality[2]) == 0) {
            qualityHdBtn.setVisibility(View.GONE);
        } else {
            qualityHdBtn.setVisibility(View.VISIBLE);
        }

        int height = mLocalInfo.getScreenHeight() - mTitleBar.getHeight() - mRealPlayPlayRl.getHeight()
                - (mRealPlayRect != null ? mRealPlayRect.top : mLocalInfo.getNavigationBarHeight());
        mQualityPopupWindow = new PopupWindow(layoutView, LayoutParams.MATCH_PARENT, height, true);
        mQualityPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mQualityPopupWindow.setAnimationStyle(R.style.popwindowUpAnim);
        mQualityPopupWindow.setFocusable(true);
        mQualityPopupWindow.setOutsideTouchable(true);
        mQualityPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        mQualityPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                LogUtil.infoLog(TAG, "KEYCODE_BACK DOWN");
                mQualityPopupWindow = null;
                closeQualityPopupWindow();
            }
        });
        mQualityPopupWindow.update();
    }

    private void closeQualityPopupWindow() {
        if (mQualityPopupWindow != null) {
            dismissPopWindow(mQualityPopupWindow);
            mQualityPopupWindow = null;
        }
        if (mStatus == RealPlayStatus.STATUS_PLAY) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }

    /**
     * 开始录像
     * 
     * @see
     * @since V1.0
     */
    private void onRecordBtnClick() {
        mControlDisplaySec = 0;
        if (mRecordFilePath != null) {
            stopRealPlayRecord();
            return;
        }

        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            Utils.showToast(this, R.string.remoteplayback_SDCard_disable_use);
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            Utils.showToast(this, R.string.remoteplayback_record_fail_for_memory);
            return;
        }

        if (mRealPlayMgr != null) {
            mCaptureDisplaySec = 4;
            updateCaptureUI();
            mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
            mRealPlayerHelper.startRecordTask(mRealPlayMgr, YingShiRealPlayActivity.this.getResources(),
                    R.drawable.video_file_watermark);
        }
    }

    /**
     * 停止录像
     * 
     * @see
     * @since V1.0
     */
    private void stopRealPlayRecord() {
        if (mRealPlayMgr == null || mRecordFilePath == null) {
            return;
        }

        // 设置录像按钮为check状态
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            if (!mIsOnStop) {
                mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mRealPlayRecordStartBtn,
                        mRealPlayRecordBtn, 0, 90);
            } else {
                mRealPlayRecordStartBtn.setVisibility(View.GONE);
                mRealPlayRecordBtn.setVisibility(View.VISIBLE);
            }
            mRealPlayFullRecordStartBtn.setVisibility(View.GONE);
            mRealPlayFullRecordBtn.setVisibility(View.VISIBLE);
        } else {
            if (!mIsOnStop) {
                mRecordRotateViewUtil.applyRotation(mRealPlayFullRecordContainer, mRealPlayFullRecordStartBtn,
                        mRealPlayFullRecordBtn, 0, 90);
            } else {
                mRealPlayFullRecordStartBtn.setVisibility(View.GONE);            
                mRealPlayFullRecordBtn.setVisibility(View.VISIBLE);

            }
            mRealPlayRecordStartBtn.setVisibility(View.GONE);
            mRealPlayRecordBtn.setVisibility(View.VISIBLE);
        }
        mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
        mRealPlayerHelper.stopRecordTask(mRealPlayMgr);

        // 计时按钮不可见
        mRealPlayRecordLy.setVisibility(View.GONE);
        mRealPlayCaptureRl.setVisibility(View.VISIBLE);
        mCaptureDisplaySec = 0;
        try {
            mRealPlayCaptureIv.setImageURI(Uri.parse(mRecordFilePath));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        mRealPlayCaptureWatermarkIv.setTag(mRecordFilePath);
        mRecordFilePath = null;
        updateCaptureUI();
    }

    /**
     * 进入图像管理页面
     * 
     * @see
     * @since V2.0
     */
    private void onCaptureRlClick() {
        mRealPlayCaptureRl.setVisibility(View.GONE);
        mRealPlayCaptureIv.setImageURI(null);
        mRealPlayCaptureWatermarkIv.setTag(null);
        mRealPlayCaptureWatermarkIv.setVisibility(View.GONE);
    }

    /**
     * 抓拍按钮响应函数
     * 
     * @since V1.0
     */
    private void onCapturePicBtnClick() {
        mControlDisplaySec = 0;
        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            Utils.showToast(this, R.string.remoteplayback_SDCard_disable_use);
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            Utils.showToast(this, R.string.remoteplayback_capture_fail_for_memory);
            return;
        }

        if (mRealPlayMgr != null) {
            mCaptureDisplaySec = 4;
            updateCaptureUI();
            mRealPlayerHelper.capturePictureTask(mRealPlayMgr);
        }
    }

    private void onRealPlaySvClick() {
        if ((mCameraInfo != null && mCameraInfo.getStatus() == 1) || mRtspUrl != null) {
            if (mOrientation == Configuration.ORIENTATION_PORTRAIT || mRtspUrl != null) {
                if (mRealPlayControlRl.getVisibility() == View.VISIBLE) {
                    mRealPlayControlRl.setVisibility(View.GONE);
                } else {
                    mRealPlayControlRl.setVisibility(View.VISIBLE);
                    mControlDisplaySec = 0;
                }
            } else {
                if (mRealPlayFullOperateBar.getVisibility() == View.VISIBLE) {
                    mRealPlayFullOperateBar.setVisibility(View.GONE);
                } else {
                    mRealPlayFullOperateBar.setVisibility(View.VISIBLE);
                    mControlDisplaySec = 0;
                }
            }
        }
    }

    /**
     * 开始播放
     * 
     * @see
     * @since V2.0
     */
    private void startRealPlay() {
        LogUtil.debugLog(TAG, "startRealPlay");

        if (mStatus == RealPlayStatus.STATUS_START || mStatus == RealPlayStatus.STATUS_PLAY) {
            return;
        }

        // 检查网络是否可用
        if (!ConnectionDetector.isNetworkAvailable(this)) {
            // 提示没有连接网络
            setRealPlayFailUI(getString(R.string.realplay_play_fail_becauseof_network));
            return;
        }

        mStatus = RealPlayStatus.STATUS_START;
        setRealPlayLoadingUI();

        if (mCameraInfo != null) {
            mRealPlayMgr = new RealPlayerManager(this);
            mRealPlayMgr.setHandler(mHandler);
            mRealPlayMgr.setPlaySurface(mRealPlaySh);
            
            mRealPlayerHelper.startRealPlayTask(mRealPlayMgr, mCameraInfo.getCameraId());
        } else if (mRtspUrl != null) {
            mDemoRealPlayer = new DemoRealPlayer(this);
            mDemoRealPlayer.setHandler(mHandler);
            mDemoRealPlayer.setPlaySurface(mRealPlaySh);

            int len = mRtspUrl.indexOf("&");

            mRealPlayerHelper.startDemoRealPlayTask(mDemoRealPlayer,
                    mRtspUrl.substring(0, len > 0 ? len : mRtspUrl.length()));
        }

        updateLoadingProgress(0);
    }

    /**
     * 停止播放
     * 
     * @see
     * @since V1.0
     */
    private void stopRealPlay() {
        LogUtil.debugLog(TAG, "stopRealPlay");
        mStatus = RealPlayStatus.STATUS_STOP;

        stopUpdateTimer();
        if (mRealPlayMgr != null) {
            stopRealPlayRecord();

            mRealPlayerHelper.stopRealPlayTask(mRealPlayMgr);
            mTotalStreamFlow += mRealPlayMgr.getStreamFlow();
        } else if (mDemoRealPlayer != null) {
            mRealPlayerHelper.stopDemoRealPlayTask(mDemoRealPlayer);
            mTotalStreamFlow += mDemoRealPlayer.getStreamFlow();
        }

        mStreamFlow = 0;
    }

    private void setRealPlayLoadingUI() {
        mStartTime = System.currentTimeMillis();
        mRealPlaySv.setVisibility(View.INVISIBLE);
        mRealPlaySv.setVisibility(View.VISIBLE);
        mRealPlayTipTv.setVisibility(View.GONE);
        mRealPlayLoadingLy.setVisibility(View.VISIBLE);
        mRealPlayLoadingPbLy.setVisibility(View.VISIBLE);
        mRealPlayIv.setVisibility(View.GONE);
        mRealPlayBtn.setBackgroundResource(R.drawable.play_stop_selector);

        mRealPlayTalkBtn.setEnabled(false);
        mRealPlayCaptureBtn.setEnabled(false);
        mRealPlayRecordBtn.setEnabled(false);
        mRealPlayQualityBtn.setEnabled(false);
        mRealPlayPtzBtn.setEnabled(false);
        
        mRealPlayFullPlayBtn.setBackgroundResource(R.drawable.play_full_stop_selector);
        mRealPlayFullCaptureBtn.setEnabled(false);
        mRealPlayFullRecordBtn.setEnabled(false);
        mRealPlayFullFlowLy.setVisibility(View.GONE);

        if (mOrientation == Configuration.ORIENTATION_PORTRAIT || mRtspUrl != null) {
            mRealPlayControlRl.setVisibility(View.VISIBLE);
            mRealPlayFullOperateBar.setVisibility(View.GONE);
        } else {
            mRealPlayControlRl.setVisibility(View.GONE);
            mRealPlayFullOperateBar.setVisibility(View.VISIBLE);
        }
    }

    private void setRealPlayStopUI() {
        stopUpdateTimer();
        setRealPlaySvLayout();
        closeTalkPopupWindow();
        closePtzPopupWindow();
        mRealPlayTipTv.setVisibility(View.GONE);
        mRealPlayLoadingLy.setVisibility(View.GONE);
        mRealPlayIv.setVisibility(View.VISIBLE);
        if (mTotalStreamFlow > 0) {
            mRealPlayFlowTv.setVisibility(View.VISIBLE);
            mRealPlayFullFlowLy.setVisibility(View.VISIBLE);
            updateRealPlayFlowTv(mStreamFlow);
        } else {
            mRealPlayFlowTv.setVisibility(View.GONE);
            mRealPlayFullFlowLy.setVisibility(View.GONE);
        }
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT || mRtspUrl != null) {
            mRealPlayControlRl.setVisibility(View.VISIBLE);
            mRealPlayFullOperateBar.setVisibility(View.GONE);
        } else {
            mRealPlayControlRl.setVisibility(View.GONE);
            mRealPlayFullOperateBar.setVisibility(View.VISIBLE);
        }
        mRealPlayBtn.setBackgroundResource(R.drawable.play_play_selector);

        mRealPlayTalkBtn.setEnabled(false);
        mRealPlayCaptureBtn.setEnabled(false);
        mRealPlayRecordBtn.setEnabled(false);
        if (mCameraInfo != null && mCameraInfo.getStatus() == 1 && mRealPlayMgr != null
                && !TextUtils.isEmpty(mRealPlayMgr.getCapability())) {
            mRealPlayQualityBtn.setEnabled(true);
        } else {
            mRealPlayQualityBtn.setEnabled(false);
        }
        mRealPlayPtzBtn.setEnabled(false);

        mRealPlayFullPlayBtn.setBackgroundResource(R.drawable.play_full_play_selector);
        mRealPlayFullCaptureBtn.setEnabled(false);
        mRealPlayFullRecordBtn.setEnabled(false);
    }

    private void setRealPlayFailUI(String errorStr) {
        mStopTime = System.currentTimeMillis();
        showType();
        stopUpdateTimer();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        closeTalkPopupWindow();
        closePtzPopupWindow();
        mRealPlayTipTv.setVisibility(View.VISIBLE);
        mRealPlayTipTv.setText(errorStr);

        mRealPlayLoadingLy.setVisibility(View.GONE);
        mRealPlayIv.setVisibility(View.GONE);
        mRealPlayFlowTv.setVisibility(View.GONE);
        mRealPlayFullFlowLy.setVisibility(View.GONE);
        mRealPlayBtn.setBackgroundResource(R.drawable.play_play_selector);
        if ((mCameraInfo != null && mCameraInfo.getStatus() == 1) || mRtspUrl != null) {
            if (mOrientation == Configuration.ORIENTATION_PORTRAIT || mRtspUrl != null) {
                mRealPlayControlRl.setVisibility(View.VISIBLE);
            } else {
                mRealPlayFullOperateBar.setVisibility(View.VISIBLE);
            }
        } else {
            mRealPlayControlRl.setVisibility(View.GONE);
            mRealPlayFullOperateBar.setVisibility(View.GONE);
        }

        mRealPlayTalkBtn.setEnabled(false);
        mRealPlayCaptureBtn.setEnabled(false);
        mRealPlayRecordBtn.setEnabled(false);
        if (mCameraInfo != null && mCameraInfo.getStatus() == 1 && mRealPlayMgr != null
                && !TextUtils.isEmpty(mRealPlayMgr.getCapability())) {
            mRealPlayQualityBtn.setEnabled(true);
        } else {
            mRealPlayQualityBtn.setEnabled(false);
        }
        mRealPlayPtzBtn.setEnabled(false);

        mRealPlayFullPlayBtn.setBackgroundResource(R.drawable.play_full_play_selector);
        mRealPlayFullCaptureBtn.setEnabled(false);
        mRealPlayFullRecordBtn.setEnabled(false);
    }

    private void setRealPlaySuccessUI() {
        mStopTime = System.currentTimeMillis();
        showType();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        mRealPlayTipTv.setVisibility(View.GONE);
        mRealPlayLoadingLy.setVisibility(View.GONE);
        mRealPlayIv.setVisibility(View.GONE);
        mRealPlayFlowTv.setVisibility(View.VISIBLE);
        mRealPlayFullFlowLy.setVisibility(View.VISIBLE);
        mRealPlayBtn.setBackgroundResource(R.drawable.play_stop_selector);

        mRealPlayTalkBtn.setEnabled(true);
        mRealPlayCaptureBtn.setEnabled(true);
        mRealPlayRecordBtn.setEnabled(true);
        mRealPlayQualityBtn.setEnabled(true);
        mRealPlayPtzBtn.setEnabled(true);

        mRealPlayFullPlayBtn.setBackgroundResource(R.drawable.play_full_stop_selector);
        mRealPlayFullCaptureBtn.setEnabled(true);
        mRealPlayFullRecordBtn.setEnabled(true);
        setRealPlaySound();

        startUpdateTimer();
    }

	/**
	 * 更新流量统计
	 * 
	 * @param str
	 * @see
	 * @since V1.0
	 */
	private void checkRealPlayFlow() {
		if ((mRealPlayMgr != null || mDemoRealPlayer != null)
				&& mRealPlayFlowTv.getVisibility() == View.VISIBLE) {
                	// 更新流量数据
        	long streamFlow = mRealPlayMgr != null ? mRealPlayMgr
				.getStreamFlow() : mDemoRealPlayer.getStreamFlow();
            updateRealPlayFlowTv(streamFlow);
		}
	}

    private void updateRealPlayFlowTv(long streamFlow) {
		long streamFlowUnit = streamFlow - mStreamFlow;
		if (streamFlowUnit < 0)
			streamFlowUnit = 0;
		float fKBUnit = (float) streamFlowUnit / (float) Constant.KB;
		String descUnit = String.format("%.2f k/s ", fKBUnit);
		String desc = null;
		float fMB = 0;
		long totalstreamFlow = mTotalStreamFlow + streamFlow;
		if (totalstreamFlow >= Constant.GB) {
			float fGB = (float) totalstreamFlow / (float) Constant.GB;
			fMB = 1024 * fGB;
			desc = String.format("%.2f GB ", fGB);
		} else {
			fMB = (float) totalstreamFlow / (float) Constant.MB;
			desc = String.format("%.2f MB ", fMB);
		}

		// 显示流量
		mRealPlayFlowTv.setText(descUnit + " " + desc);
		mRealPlayFullRateTv.setText(descUnit);
		mRealPlayFullFlowTv.setText(desc);
		mStreamFlow = streamFlow;
    }

    private void updateLoadingProgress(final int progress) {
        if (mRealPlayLoadingTv == null) {
            return;
        }
        mRealPlayLoadingTv.setTag(Integer.valueOf(progress));
        mRealPlayLoadingTv.setText(progress + "%");
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mRealPlayLoadingTv != null) {
                    Integer tag = (Integer)mRealPlayLoadingTv.getTag();
                    if(tag != null && tag.intValue() == progress) {
                        Random r = new Random();
                        mRealPlayLoadingTv.setText((progress + r.nextInt(20)) + "%");
                    }
                }
            }

        }, 500);
    }

    /*
     * (non-Javadoc)
     * @see android.os.Handler.Callback#handleMessage(android.os.Message)
     */
    @Override
    public boolean handleMessage(Message msg) {
        //LogUtil.infoLog(TAG, "handleMessage:" + msg.what);
        switch (msg.what) {
            case RealPlayMsg.MSG_REALPLAY_PLAY_START:
                updateLoadingProgress(40);
                break;
            case RealPlayMsg.MSG_REALPLAY_CONNECTION_START:
                updateLoadingProgress(60);
                break;
            case RealPlayMsg.MSG_REALPLAY_CONNECTION_SUCCESS:
                updateLoadingProgress(80);
                break;
            case RealPlayMsg.MSG_GET_CAMERA_INFO_SUCCESS:
                updateLoadingProgress(20);
                handleGetCameraInfoSuccess();
                break;
            case RealPlayMsg.MSG_REALPLAY_PLAY_SUCCESS:
                handlePlaySuccess(msg);
                break;
            case RealPlayMsg.MSG_REALPLAY_PLAY_FAIL:
                handlePlayFail(msg.arg1);
                break;
            case RealPlayMsg.MSG_REALPLAY_PASSWORD_ERROR:
                // 处理播放密码错误
                handlePasswordError(R.string.realplay_password_error_title, R.string.realplay_password_error_message3,
                        R.string.realplay_password_error_message1);
                break;
            case RealPlayMsg.MSG_REALPLAY_ENCRYPT_PASSWORD_ERROR:
                // 处理播放密码错误
                handlePasswordError(R.string.realplay_encrypt_password_error_title,
                        R.string.realplay_encrypt_password_error_message, 0);
                break;
            case RealPlayMsg.MSG_SET_VEDIOMODE_SUCCESS:
                handleSetVedioModeSuccess();
                break;
            case RealPlayMsg.MSG_SET_VEDIOMODE_FAIL:
                handleSetVedioModeFail(msg.arg1);
                break;
            case RealPlayMsg.MSG_START_RECORD_SUCCESS:
                handleStartRecordSuccess((String) msg.obj);
                break;
            case RealPlayMsg.MSG_START_RECORD_FAIL:
                Utils.showToast(this, R.string.remoteplayback_record_fail);
                break;
            case RealPlayMsg.MSG_CAPTURE_PICTURE_SUCCESS:
                handleCapturePictureSuccess((String) msg.obj);
                break;
            case RealPlayMsg.MSG_CAPTURE_PICTURE_FAIL:
                // 提示抓图失败
                Utils.showToast(this, R.string.remoteplayback_capture_fail);
                break;
            case RealPlayMsg.MSG_REALPLAY_VOICETALK_SUCCESS:
                handleVoiceTalkSucceed();
                break;
            case RealPlayMsg.MSG_REALPLAY_VOICETALK_STOP:
                handleVoiceTalkStoped();
                break;
            case RealPlayMsg.MSG_REALPLAY_VOICETALK_FAIL:
                handleVoiceTalkFailed(msg.arg1, msg.arg2);
                break;
            case RealPlayMsg.MSG_PTZ_SET_FAIL:
                handlePtzControlFail(msg);
                break;                
            case MSG_PLAY_UI_UPDATE:
                updateRealPlayUI();
                break;
            case MSG_HIDE_PTZ_DIRECTION:
                handleHidePtzDirection(msg);
                break;                
            default:
                break;
        }
        return false;
    }
    
    private void handleHidePtzDirection(Message msg) {
        if (mHandler == null) {
            return;
        }
        mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        if (msg.arg1 > 2) {
            mRealPlayPtzDirectionIv.setVisibility(View.GONE);
        } else {
            mRealPlayPtzDirectionIv.setVisibility(msg.arg1 == 1 ? View.GONE : View.VISIBLE);
            Message message = new Message();
            message.what = MSG_HIDE_PTZ_DIRECTION;
            message.arg1 = msg.arg1 + 1;
            mHandler.sendMessageDelayed(message, 500);
        }
    }
    
    private void handlePtzControlFail(Message msg) {
        LogUtil.debugLog(TAG, "handlePtzControlFail:" + msg.arg1);
        switch(msg.arg1) {
            case ErrorCode.ERROR_CAS_PTZ_CONTROL_CALLING_PRESET_FAILED://正在调用预置点，键控动作无效
                Utils.showToast(this,R.string.camera_lens_too_busy, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRESET_PRESETING_FAILE:// 当前正在调用预置点
                Utils.showToast(this,R.string.ptz_is_preseting, msg.arg1);
                break;              
            case ErrorCode.ERROR_CAS_PTZ_CONTROL_TIMEOUT_SOUND_LACALIZATION_FAILED://当前正在声源定位
                break;            
            case ErrorCode.ERROR_CAS_PTZ_CONTROL_TIMEOUT_CRUISE_TRACK_FAILED://键控动作超时(当前正在轨迹巡航)
                Utils.showToast(this,R.string.ptz_control_timeout_cruise_track_failed, msg.arg1);
                break;              
            case ErrorCode.ERROR_CAS_PTZ_PRESET_INVALID_POSITION_FAILED://当前预置点信息无效
                Utils.showToast(this,R.string.ptz_preset_invalid_position_failed, msg.arg1);
                break;             
            case ErrorCode.ERROR_CAS_PTZ_PRESET_CURRENT_POSITION_FAILED://该预置点已是当前位置
                Utils.showToast(this,R.string.ptz_preset_current_position_failed, msg.arg1);
                break;                
            case ErrorCode.ERROR_CAS_PTZ_PRESET_SOUND_LOCALIZATION_FAILED:// 设备正在响应本次声源定位
                Utils.showToast(this,R.string.ptz_preset_sound_localization_failed, msg.arg1);
                break;            
            case ErrorCode.ERROR_CAS_PTZ_OPENING_PRIVACY_FAILED://当前正在开启隐私遮蔽
            case ErrorCode.ERROR_CAS_PTZ_CLOSING_PRIVACY_FAILED://当前正在关闭隐私遮蔽  
            case ErrorCode.ERROR_CAS_PTZ_MIRRORING_FAILED://设备正在镜像操作（设备镜像要几秒钟，防止频繁镜像操作）                
                Utils.showToast(this,R.string.ptz_operation_too_frequently, msg.arg1);
                break;
        case ErrorCode.ERROR_CAS_PTZ_CONTROLING_FAILED://设备正在键控动作（上下左右）(一个客户端在上下左右控制，另外一个在开其它东西)
                break;
            case ErrorCode.ERROR_CAS_PTZ_FAILED://云台当前操作失败    
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRESET_EXCEED_MAXNUM_FAILED://当前预置点超过最大个数   
                Utils.showToast(this,R.string.ptz_preset_exceed_maxnum_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_PRIVACYING_FAILED:// 设备处于隐私遮蔽状态（关闭了镜头，再去操作云台相关）
                Utils.showToast(this,R.string.ptz_privacying_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_TTSING_FAILED:// 设备处于语音对讲状态(区别以前的语音对讲错误码，云台单独列一个）
                Utils.showToast(this,R.string.ptz_mirroring_failed, msg.arg1);
                break;
            case ErrorCode.ERROR_CAS_PTZ_ROTATION_UP_LIMIT_FAILED://设备云台旋转到达上限位
            case ErrorCode.ERROR_CAS_PTZ_ROTATION_DOWN_LIMIT_FAILED://设备云台旋转到达下限位
            case ErrorCode.ERROR_CAS_PTZ_ROTATION_LEFT_LIMIT_FAILED://设备云台旋转到达左限位
            case ErrorCode.ERROR_CAS_PTZ_ROTATION_RIGHT_LIMIT_FAILED://设备云台旋转到达右限位
                setPtzDirectionIv(-1, msg.arg1);
                break;
            default:
                Utils.showToast(this,R.string.ptz_operation_failed, msg.arg1);
                break;
        }
    }
    
    /**
     * 获取设备播放信息成功
     * 
     * @see
     * @since V1.0
     */
    private void handleGetCameraInfoSuccess() {
        if (mRealPlayMgr != null && (mRealPlayMgr.getSupportTalk() == DeviceInfoEx.TALK_HALF_DUPLEX 
                || mRealPlayMgr.getSupportTalk() == DeviceInfoEx.TALK_FULL_DUPLEX)) {
            mRealPlayTalkBtn.setVisibility(View.VISIBLE);
        } else {
            mRealPlayTalkBtn.setVisibility(View.GONE);
        }

        setVideoLevel();

        if (getSupportPtz() == 1) {
            mRealPlayPtzBtn.setVisibility(View.VISIBLE);
        } else {
            mRealPlayPtzBtn.setVisibility(View.GONE);
        }
    }
    
    private int getSupportPtz() {
        if (mRealPlayMgr == null) {
            return 0;
        }

        if (mRealPlayMgr.getSupportPtzLeftRight() == 1 || mRealPlayMgr.getSupportPtzTopBottom() == 1) {
            return 1;
        } else {
            return 0;
        }
    }
    
    private void handleVoiceTalkSucceed() {
        openTalkPopupWindow(mRealPlayPageLy);
    }

    private void handleVoiceTalkFailed(int errorCode, int sysErrorCode) {
        closeTalkPopupWindow();

        if (errorCode == TTSClientSDKException.TTSCLIENT_MSG_DEVICE_TAKLING_NOW
                || errorCode == CASClientSDKException.CASCLIENT_CAS_TALK_CHANNEL_BUSY) {
            Utils.showToast(this, getString(R.string.realplay_play_talkback_fail_ison));
        } else if (errorCode == CASClientSDKException.CASCLIENT_CAS_PU_OPEN_PRIVACY
                || errorCode == TTSClientSDKException.TTSCLIENT_MSG_DEV_PRIVACY_ON
                || errorCode == ErrorCode.ERROR_TTS_DEV_PRIVACY_ON) {
            Utils.showToast(this, getString(R.string.realplay_play_talkback_fail_privacy));
        } else {
            Utils.showToast(this, R.string.realplay_play_talkback_fail, errorCode);
        }
    }

    private void handleVoiceTalkStoped() {
        if (mVoiceTalkManager != null) {
            mVoiceTalkManager.setHandler(null);
        }

        if (mStatus == RealPlayStatus.STATUS_PLAY) {
            mRealPlayTalkBtn.setEnabled(true);
            if (mRealPlayMgr != null && mLocalInfo.isSoundOpen()) {
                mRealPlayMgr.openSound();
            } else {
                mRealPlayMgr.closeSound();
            }
        }
    }

    private void handleSetVedioModeSuccess() {
        closeQualityPopupWindow();
        setVideoLevel();
        try {
            //mWaitDialog.setWaitText(null);
           // mWaitDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mStatus == RealPlayStatus.STATUS_PLAY) {
            // 停止播放
            stopRealPlay();
            // 开始播放
            startRealPlay();
        }
    }

    private void handleSetVedioModeFail(int errorCode) {
        closeQualityPopupWindow();
        setVideoLevel();
        try {
            //mWaitDialog.setWaitText(null);
            //mWaitDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Utils.showToast(this, R.string.realplay_set_vediomode_fail, errorCode);
    }

    /**
     * 开始录像成功
     * 
     * @param errorCode
     * @see
     * @since V2.0
     */
    private void handleStartRecordSuccess(String recordFilePath) {
        // 设置录像按钮为check状态
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            if (!mIsOnStop) {
                mRecordRotateViewUtil.applyRotation(mRealPlayRecordContainer, mRealPlayRecordBtn, mRealPlayRecordStartBtn,
                        0, 90);
            } else {
                mRealPlayRecordBtn.setVisibility(View.GONE);
                mRealPlayRecordStartBtn.setVisibility(View.VISIBLE);                
            }
            mRealPlayFullRecordBtn.setVisibility(View.GONE);
            mRealPlayFullRecordStartBtn.setVisibility(View.VISIBLE);
        } else {
            if (!mIsOnStop) {
                mRecordRotateViewUtil.applyRotation(mRealPlayFullRecordContainer, mRealPlayFullRecordBtn,
                        mRealPlayFullRecordStartBtn, 0, 90);
            } else {
                mRealPlayFullRecordBtn.setVisibility(View.GONE);
                mRealPlayFullRecordStartBtn.setVisibility(View.VISIBLE);             
            }
            mRealPlayRecordBtn.setVisibility(View.GONE);
            mRealPlayRecordStartBtn.setVisibility(View.VISIBLE);
        }
        mRecordFilePath = recordFilePath;
        // 计时按钮可见
        mRealPlayRecordLy.setVisibility(View.VISIBLE);
        mRealPlayRecordTv.setText("00:00");
        mRecordSecond = 0;
    }

    /**
     * 抓图成功，返回图片文件路径
     * 
     * @param errorCode
     * @see
     * @since V2.0
     */
    private void handleCapturePictureSuccess(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        // 播放抓拍音频
        mAudioPlayUtil.playAudioFile(AudioPlayUtil.CAPTURE_SOUND);

        mRealPlayCaptureRl.setVisibility(View.VISIBLE);
        mCaptureDisplaySec = 0;
        try {
            mRealPlayCaptureIv.setImageURI(Uri.parse(filePath));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        updateCaptureUI();
    }

    private void updateRealPlayUI() {
        if (mControlDisplaySec == 5) {
            mControlDisplaySec = 0;
            mRealPlayControlRl.setVisibility(View.GONE);
            mRealPlayFullOperateBar.setVisibility(View.GONE);
        }

        updateCaptureUI();

        if (mRecordFilePath != null) {
            updateRecordTime();
        }

        checkRealPlayFlow();
    }

    // 更新抓图/录像显示UI
    private void updateCaptureUI() {
        if (mRealPlayCaptureRl.getVisibility() == View.VISIBLE) {
            if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
                if (mRealPlayControlRl.getVisibility() == View.VISIBLE) {
                    mRealPlayCaptureRlLp.setMargins(0, 0, 0, Utils.dip2px(this, 40));
                } else {
                    mRealPlayCaptureRlLp.setMargins(0, 0, 0, 0);
                }
                mRealPlayCaptureRl.setLayoutParams(mRealPlayCaptureRlLp);
            } else {
                RelativeLayout.LayoutParams realPlayCaptureRlLp = new RelativeLayout.LayoutParams(
                        Utils.dip2px(this, 65), Utils.dip2px(this, 45));
                realPlayCaptureRlLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                realPlayCaptureRlLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                mRealPlayCaptureRl.setLayoutParams(realPlayCaptureRlLp);
            }
            if (mRealPlayCaptureWatermarkIv.getTag() != null) {
                mRealPlayCaptureWatermarkIv.setVisibility(View.VISIBLE);
                mRealPlayCaptureWatermarkIv.setTag(null);
            }
        }
        if (mCaptureDisplaySec >= 4) {
            mCaptureDisplaySec = 0;
            mRealPlayCaptureRl.setVisibility(View.GONE);
            mRealPlayCaptureIv.setImageURI(null);
            mRealPlayCaptureWatermarkIv.setTag(null);
            mRealPlayCaptureWatermarkIv.setVisibility(View.GONE);
        }
    }

    /**
     * 更新录像时间
     * 
     * @see
     * @since V1.0
     */
    private void updateRecordTime() {
        if (mRealPlayRecordIv.getVisibility() == View.VISIBLE) {
            mRealPlayRecordIv.setVisibility(View.INVISIBLE);
        } else {
            mRealPlayRecordIv.setVisibility(View.VISIBLE);
        }
        // 计算分秒
        int leftSecond = mRecordSecond % 3600;
        int minitue = leftSecond / 60;
        int second = leftSecond % 60;

        // 显示录像时间
        String recordTime = String.format("%02d:%02d", minitue, second);
        mRealPlayRecordTv.setText(recordTime);
    }

    // 处理密码错误
    private void handlePasswordError(int title_resid, int msg1_resid, int msg2_resid) {
        stopRealPlay();
        setRealPlayStopUI();
        LogUtil.debugLog(TAG, "startRealPlay");

        if (mCameraInfo == null || mStatus == RealPlayStatus.STATUS_START || mStatus == RealPlayStatus.STATUS_PLAY) {
            return;
        }

        // 检查网络是否可用
        if (!ConnectionDetector.isNetworkAvailable(this)) {
            // 提示没有连接网络
            setRealPlayFailUI(getString(R.string.realplay_play_fail_becauseof_network));
            return;
        }

        mStatus = RealPlayStatus.STATUS_START;
        setRealPlayLoadingUI();

        mRealPlayMgr = new RealPlayerManager(this);
        mRealPlayMgr.setHandler(mHandler);
        mRealPlayMgr.setPlaySurface(mRealPlaySh);
        
        mRealPlayerHelper.startEncryptRealPlayTask(this, mRealPlayMgr, mCameraInfo.getCameraId(), title_resid, msg1_resid, msg2_resid);

        updateLoadingProgress(0);        
    }

    /**
     * 处理播放成功的情况
     * 
     * @see
     * @since V1.0
     */
    private void handlePlaySuccess(Message msg) {
        mStatus = RealPlayStatus.STATUS_PLAY;

        if (msg.arg1 != 0) {
            mRealRatio = (float) msg.arg2 / msg.arg1;
        } else {
            mRealRatio = Constant.LIVE_VIEW_RATIO;
        }
        setRealPlaySvLayout();
        setRealPlaySuccessUI();
        setRealPlaySound();
    }

    private void setRealPlaySvLayout() {
        // 设置播放窗口位置
        final int screenWidth = mLocalInfo.getScreenWidth();
        final int screenHeight = (mOrientation == Configuration.ORIENTATION_PORTRAIT) ? (mLocalInfo.getScreenHeight() - mLocalInfo
                .getNavigationBarHeight()) : mLocalInfo.getScreenHeight();
        final RelativeLayout.LayoutParams realPlaySvlp = Utils.getPlayViewLp(mRealRatio, mOrientation,
                mLocalInfo.getScreenWidth(), (int) (mLocalInfo.getScreenWidth() * Constant.LIVE_VIEW_RATIO),
                screenWidth, screenHeight);

        RelativeLayout.LayoutParams svLp = new RelativeLayout.LayoutParams(realPlaySvlp.width, realPlaySvlp.height);
        svLp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mRealPlaySv.setLayoutParams(svLp);

        if (mRtspUrl == null) {
            LinearLayout.LayoutParams realPlayPlayRlLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            realPlayPlayRlLp.gravity = Gravity.CENTER;
            mRealPlayPlayRl.setLayoutParams(realPlayPlayRlLp);
        } else {
            LinearLayout.LayoutParams realPlayPlayRlLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            realPlayPlayRlLp.gravity = Gravity.CENTER;
            realPlayPlayRlLp.weight = 1;
            mRealPlayPlayRl.setLayoutParams(realPlayPlayRlLp);
        }
        mRealPlayTouchListener.setSacaleRect(Constant.MAX_SCALE, 0, 0, realPlaySvlp.width, realPlaySvlp.height);
        setPlayScaleUI(1, null, null);
    }

    /**
     * 处理播放失败的情况
     * 
     * @param errorCode
     *            - 错误码
     * @see
     * @since V1.0
     */
    private void handlePlayFail(int errorCode) {
        LogUtil.debugLog(TAG, "handlePlayFail:" + errorCode);

        stopRealPlay();

        String txt = null;
        // 判断返回的错误码
        switch (errorCode) {
            case HCNetSDKException.NET_DVR_PASSWORD_ERROR:
            case HCNetSDKException.NET_DVR_NOENOUGHPRI:
	     case ErrorCode.ERROR_DVR_LOGIN_USERID_ERROR:
                // 弹出密码输入框
                handlePasswordError(R.string.realplay_password_error_title, R.string.realplay_password_error_message3,
                        R.string.realplay_password_error_message1);
                return;
            case ErrorCode.ERROR_WEB_SESSION_ERROR:
            case ErrorCode.ERROR_WEB_SESSION_EXPIRE:
            case ErrorCode.ERROR_CAS_PLATFORM_CLIENT_NO_SIGN_RELEATED:
            case ErrorCode.ERROR_WEB_HARDWARE_SIGNATURE_ERROR:
            case ErrorCode.ERROR_CAS_VERIFY_SESSION_ERROR:
            case ErrorCode.ERROR_RTSP_SESSION_NOT_EXIST:                
                EzvizAPI.getInstance().gotoLoginPage();
                return;
                // case HCNetSDKException.NET_DVR_RTSP_DESCRIBESERVERERR:
            case RtspClientException.RTSPCLIENT_DEVICE_CONNECTION_LIMIT:
            case HCNetSDKException.NET_DVR_RTSP_OVER_MAX_CHAN:
            case RtspClientException.RTSPCLIENT_OVER_MAXLINK:
            case HCNetSDKException.NET_DVR_OVER_MAXLINK:
            case CASClientSDKException.CASCLIENT_MSG_PU_NO_RESOURCE:
                txt = getString(R.string.remoteplayback_over_link);
                break;
            case ErrorCode.ERROR_WEB_DIVICE_NOT_ONLINE:
            case ErrorCode.ERROR_RTSP_NOT_FOUND:
            case ErrorCode.ERROR_CAS_PLATFORM_CLIENT_REQUEST_NO_PU_FOUNDED:
                txt = getString(R.string.realplay_fail_device_not_exist);
                break;
            case ErrorCode.ERROR_WEB_DIVICE_SO_TIMEOUT:
                txt = getString(R.string.realplay_fail_connect_device);
                break;
            case HCNetSDKException.NET_DVR_RTSP_PRIVACY_STATUS:
            case RtspClientException.RTSPCLIENT_PRIVACY_STATUS:
                txt = getString(R.string.realplay_set_fail_status);
                break;
            case InnerException.INNER_DEVICE_NOT_EXIST:
                txt = getString(R.string.camera_not_online);
                break;
            case ErrorCode.ERROR_INNER_DEVICE_ENCRYPT_PASSWORD_IS_NULL:
                txt = null;
                break;
            /*case ErrorCode.ERROR_WEB_CODE_ERROR:
                //VerifySmsCodeUtil.openSmsVerifyDialog(Constant.SMS_VERIFY_LOGIN, this, this);
                OpenYSService.openYSServiceDialog(this, this);
                //txt = Utils.getErrorTip(this, R.string.check_feature_code_fail, errorCode);
                break;
            case ErrorCode.ERROR_WEB_HARDWARE_SIGNATURE_OP_ERROR:
                //VerifySmsCodeUtil.openSmsVerifyDialog(Constant.SMS_VERIFY_HARDWARE, this, this);
                SecureValidate.secureValidateDialog(this, this);
                //txt = Utils.getErrorTip(this, R.string.check_feature_code_fail, errorCode);
                break;*/
            case ErrorCode.ERROR_INNER_DEVICE_PASSWORD_IS_NULL:
                txt = getString(R.string.device_password_is_null);
                break;
            default:
                txt = Utils.getErrorTip(this, R.string.realplay_play_fail, errorCode);
                break;
        }
        
        if(!TextUtils.isEmpty(txt)) {
            setRealPlayFailUI(txt);
        } else {
            setRealPlayStopUI();
        }
    }

    /**
     * 启动定时器
     * 
     * @see
     * @since V1.0
     */
    private void startUpdateTimer() {
        stopUpdateTimer();
        // 开始录像计时
        mUpdateTimer = new Timer();
        mUpdateTimerTask = new TimerTask() {
            @Override
            public void run() {
                if ((mRealPlayControlRl.getVisibility() == View.VISIBLE || mRealPlayFullOperateBar.getVisibility() == View.VISIBLE)
                        && mControlDisplaySec < 5) {
                    mControlDisplaySec++;
                }
                if (mRealPlayCaptureRl.getVisibility() == View.VISIBLE && mCaptureDisplaySec < 4) {
                    mCaptureDisplaySec++;
                }

                // 更新录像时间
                if (mRealPlayMgr != null && mRecordFilePath != null) {
                    // 更新录像时间
                    Calendar OSDTime = mRealPlayMgr.getOSDTime();
                    if (OSDTime != null) {
                        String playtime = Utils.OSD2Time(OSDTime);
                        if (!TextUtils.equals(playtime, mRecordTime)) {
                            mRecordSecond++;
                            mRecordTime = playtime;
                        }
                    }
                }
                sendMessage(MSG_PLAY_UI_UPDATE, 0, 0);
            }
        };
        // 延时1000ms后执行，1000ms执行一次
        mUpdateTimer.schedule(mUpdateTimerTask, 0, 1000);
    }

    /**
     * 停止定时器
     * 
     * @see
     * @since V1.0
     */
    private void stopUpdateTimer() {
        mCaptureDisplaySec = 4;
        updateCaptureUI();
        mHandler.removeMessages(MSG_PLAY_UI_UPDATE);
        // 停止录像计时
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }

        if (mUpdateTimerTask != null) {
            mUpdateTimerTask.cancel();
            mUpdateTimerTask = null;
        }
    }

    /**
     * 发送消息
     * 
     * @param msg
     *            - 消息对象
     * @param arg1
     *            - 错误码
     * @since V1.0
     */
    public void sendMessage(int msg, int arg1, int arg2) {
        if (mHandler != null) {
            Message message = Message.obtain();
            message.what = msg;
            message.arg1 = arg1;
            message.arg2 = arg2;
            mHandler.sendMessage(message);
        }
    }

    private void dismissPopWindow(PopupWindow popupWindow) {
        if (popupWindow != null && !isFinishing()) {
            try {
                popupWindow.dismiss();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }
    
    private void setPlayScaleUI(float scale, CustomRect oRect, CustomRect curRect) {
        if (scale == 1) {
            if (mPlayScale == scale) {
                return;
            }
            mRealPlayRatioTv.setVisibility(View.GONE);
            try {
                if (mRealPlayMgr != null) {
                    mRealPlayMgr.setDisplayRegion(false, null, null);
                } else if(mDemoRealPlayer != null) {
                    mDemoRealPlayer.setDisplayRegion(false, null, null);
                }
            } catch (BaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            if (mPlayScale == scale) {
                try {
                    if (mRealPlayMgr != null) {
                        mRealPlayMgr.setDisplayRegion(true, oRect, curRect);
                    } else if(mDemoRealPlayer != null) {
                        mDemoRealPlayer.setDisplayRegion(true, oRect, curRect);
                    }
                } catch (BaseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
            RelativeLayout.LayoutParams realPlayRatioTvLp = (RelativeLayout.LayoutParams) mRealPlayRatioTv
                    .getLayoutParams();
            if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
                realPlayRatioTvLp.setMargins(Utils.dip2px(this, 10), Utils.dip2px(this, 10), 0, 0);
            } else {
                realPlayRatioTvLp.setMargins(Utils.dip2px(this, 70), Utils.dip2px(this, 20), 0, 0);
            }
            mRealPlayRatioTv.setLayoutParams(realPlayRatioTvLp);
            String sacleStr = String.valueOf(scale);
            mRealPlayRatioTv.setText(sacleStr.subSequence(0, Math.min(3, sacleStr.length())) + "X");
            mRealPlayRatioTv.setVisibility(View.VISIBLE);
            mRealPlayControlRl.setVisibility(View.GONE);
            mRealPlayFullOperateBar.setVisibility(View.GONE);
            try {
                if (mRealPlayMgr != null) {
                    mRealPlayMgr.setDisplayRegion(true, oRect, curRect);
                } else if(mDemoRealPlayer != null) {
                    mDemoRealPlayer.setDisplayRegion(true, oRect, curRect);
                }
            } catch (BaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        mPlayScale = scale;
    }
    
    /*
     * (non-Javadoc)
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.realplay_full_operate_bar:
                return true;
            default:
                break;
        }
        return false;
    }
	
    private void showType() {
        if (Config.LOGGING && mRealPlayMgr != null) {
            Utils.showLog(YingShiRealPlayActivity.this, mRealPlayMgr.getType() + ",取流耗时：" + (mStopTime - mStartTime));
        }
    }

    /* (non-Javadoc)
     * @see com.videogo.ui.util.SecureValidate.SecureValidateListener#onSecureValidate(int)
     */
   /* @Override
    public void onSecureValidate(int result) {
        if(result == 0) {
            // 开始播放
            startRealPlay();
        }
    }
*/
    /* (non-Javadoc)
     * @see com.videogo.ui.util.OpenYSService.OpenYSServiceListener#onOpenYSService(int)
     */
    /*@Override
   public void onOpenYSService(int result) {
        if(result == 0) {
            // 开始播放
            startRealPlay();
        }
    }*/
}
