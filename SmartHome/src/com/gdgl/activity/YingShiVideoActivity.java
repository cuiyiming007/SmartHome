package com.gdgl.activity;

	/* 
	 * @ProjectName VideoGo
	 * @Copyright HangZhou Hikvision System Technology Co.,Ltd. All Right Reserved
	 * 
	 * @FileName RealPlayerActivity.java
	 * @Description 这里对文件进行描述
	 * 
	 * @author Dengshihua
	 * @data 2012-8-20
	 * 
	 * @note 这里写本文件的详细功能描述和注释
	 * @note 历史记录
	 * 
	 * @warning 这里写本文件的相关警告
	 */

	import java.util.ArrayList;
	import java.util.Random;

	import android.app.Activity;
	import android.app.AlertDialog;
	import android.content.BroadcastReceiver;
	import android.content.Context;
	import android.content.Intent;
	import android.content.IntentFilter;
	import android.content.pm.ActivityInfo;
	import android.content.res.Configuration;
	import android.graphics.Rect;
	import android.os.Bundle;
	import android.os.Handler;
	import android.os.Message;
	import android.text.TextUtils;
	import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
	import android.view.MotionEvent;
	import android.view.SurfaceHolder;
	import android.view.SurfaceView;
	import android.view.View;
	import android.view.View.OnClickListener;
	import android.view.View.OnTouchListener;
	import android.view.WindowManager;
	import android.widget.AdapterView;
	import android.widget.AdapterView.OnItemSelectedListener;
	import android.widget.ImageButton;
	import android.widget.RelativeLayout;
	import android.widget.TextView;
	import com.videogo.constant.Constant;
	import com.videogo.constant.IntentConsts;
	import com.videogo.exception.ErrorCode;
	import com.videogo.exception.HCNetSDKException;
	import com.videogo.exception.InnerException;
	import com.videogo.exception.RtspClientException;
	import com.videogo.openapi.EzvizAPI;
	import com.videogo.openapi.bean.resp.CameraInfo;
	import com.videogo.realplay.RealPlayMsg;
	import com.videogo.realplay.RealPlayStatus;
	import com.videogo.realplay.RealPlayerHelper;
	import com.videogo.realplay.RealPlayerManager;
	import com.gdgl.model.DeviceDiscoverInfo;
import com.gdgl.smarthome.R;
import com.gdgl.adapter.SimpleRealPlayerAdapter;
	import com.gdgl.adapter.SimpleRealPlayerAdapter.RealPlayerHolder;
//import com.videogo.ui.util.OpenYSService;
	//import com.videogo.ui.util.SecureValidate;
	import com.videogo.util.ConnectionDetector;
	import com.videogo.util.LogUtil;
	import com.videogo.util.Utils;
	import com.gdgl.widgets.PagesGallery;
	//import com.videogo.widget.WaitDialog;

	/**
	 * 简单实时预览界面
	 * 
	 */
	public class YingShiVideoActivity extends Activity implements OnClickListener, SurfaceHolder.Callback,
	        OnItemSelectedListener, Handler.Callback, OnTouchListener {
	    /** 打印标签 */
	    private static final String TAG = "EzvizRealPlayActivity";
	    
	    /** 实时预览控制对象 */
	    private RealPlayerManager mRealPlayMgr = null;
	    /** 预览取流任务处理对象 */
	    private RealPlayerHelper mRealPlayerHelper = null;
	    
	    /** 设备信息 */
	    private ArrayList<CameraInfo> mCameraInfoList = null;
	    /** 选中通道信息 */
	    private CameraInfo mCameraInfo = null;
	    private DeviceDiscoverInfo mDeviceDiscoverInfo = null;
	    
	    private RelativeLayout mDisplayView = null;
	    // 标题栏控件
	    /** 标题栏区域 */
	    private RelativeLayout mTitleArea = null;
	    /** 界面返回按钮 */
	    private ImageButton mBackBtn = null;
	    /** 标题文本控件 */
	    private TextView mTitleTv = null;
	    /** 播放区域布局 */
	    private RelativeLayout mPlayArea = null;
	    /** 播放界面 */
	    private SurfaceView mSurfaceView = null;
	    private SurfaceHolder mSurfaceHolder = null;
	    private RelativeLayout mControlArea = null;
	    /** 暂停按钮 */
	    private ImageButton mStopBtn = null;
	    /** 播放按钮 */
	    private ImageButton mPlayBtn = null;

	    // 实时预览滑动显示控件
	    private PagesGallery mPagesGallery = null;
	    private SimpleRealPlayerAdapter mPagesAdapter = null;
	    private RealPlayerHolder mSelectedRealPlayerHolder = null;
	    private int mPosition = 0;
	    /** 屏幕当前方向 */
	    private int mOrientation = Configuration.ORIENTATION_PORTRAIT;

	    /** 标识是否正在播放 */
	    private boolean mIsPlaying = false;
	    /** 视频窗口可以显示的区域 */
	    private Rect mCanDisplayRect = null;
	    /** 竖屏时的宽度 */
	    private int mDisplayWidth = 0;
	    /** 竖屏时的高度 */
	    private int mDisplayHeight = 0;
	    private double mPlayRatio = 0;

	    /** 消息句柄 */
	    private Handler mHandler = null;
	    /** 变量/常量说明 */
	    private GestureDetector mGestureDetector;
	    /** 监听锁屏解锁的事件 */
	    private ScreenBroadcastReceiver mScreenBroadcastReceiver = null;
	    
	    private AlertDialog mPlayFailDialog = null;
	   // private WaitDialog mWaitDialog = null;
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Log.i("aaa", "00000000000");
	        setContentView(R.layout.realplayer_page);
	        Log.i("aaa", "aaaaaaaaaaaaaa");
	        // 初始化数据
	        initData();

	        //初始化控件
	        initViews();
	    }
	    
	    /**
	     * 初始化数据
	     * 
	     * @see
	     * @since V2.0
	     */
	    private void initData() {           
	        // 获取播放设备信息
	        Intent intent = getIntent();
	        if (intent != null) {
	            mCameraInfo = (CameraInfo) intent.getParcelableExtra(IntentConsts.EXTRA_CAMERA_INFO);
	            mCameraInfoList = intent.getParcelableArrayListExtra(IntentConsts.EXTRA_CAMERA_INFO_LIST);
	            mDeviceDiscoverInfo = (DeviceDiscoverInfo) intent.getParcelableExtra("DeviceDiscoverInfo");
	            if (mCameraInfo != null && mCameraInfoList != null) {
	                CameraInfo cameraInfo = null;
	                for (int i = 0; i < mCameraInfoList.size(); i++) {
	                    cameraInfo = mCameraInfoList.get(i);
	                    if (cameraInfo != null && cameraInfo.getCameraId().equals(mCameraInfo.getCameraId())) {
	                        mPosition = i;
	                        break;
	                    }
	                }
	            } else if(mCameraInfo != null) {
	                mCameraInfoList = new ArrayList<CameraInfo>();
	                mCameraInfoList.add(mCameraInfo);
	                mPosition = 0;
	            } else if(mDeviceDiscoverInfo != null) {
	                mCameraInfo = new CameraInfo();
	                mCameraInfo.setCameraName(mDeviceDiscoverInfo.deviceName);
	                mCameraInfo.setDeviceId(mDeviceDiscoverInfo.deviceID);
	                mCameraInfoList = new ArrayList<CameraInfo>();
	                mCameraInfoList.add(mCameraInfo);
	                mPosition = 0;                
	            } else {
	                finish();
	                return;
	            }
	        }
	        
	        mRealPlayerHelper = RealPlayerHelper.getInstance(getApplication());
	        mHandler = new Handler(this);
	        mCanDisplayRect = new Rect(0, 0, 0, 0);
	        
	        mScreenBroadcastReceiver = new ScreenBroadcastReceiver();
	        IntentFilter filter = new IntentFilter();
	        filter.addAction(Intent.ACTION_USER_PRESENT);
	        filter.addAction(Intent.ACTION_SCREEN_OFF);
	        registerReceiver(mScreenBroadcastReceiver, filter);
	    }
	    
	    @Override
	    protected void onResume() {
	        super.onResume();
	        
	        mSurfaceView.setVisibility(View.VISIBLE);
	        if(mCameraInfo == null) {
	            return;
	        }

	        if (mSelectedRealPlayerHolder != null && !mIsPlaying) {
	            // 开始播放
	            startRealPlay();
	        }
	    }
	    
	    /**
	     * 初始化控件
	     * 
	     * @see
	     * @since V1.0
	     */
	    private void initViews() {
	        // 保持屏幕常亮
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        
	        //获取屏幕长宽
	        DisplayMetrics metric = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(metric);
	        mDisplayWidth = metric.widthPixels;
	        mDisplayHeight = metric.heightPixels;
	        mGestureDetector = new GestureDetector(YingShiVideoActivity.this, onGestureListener);
	        
	        mDisplayView = (RelativeLayout) findViewById(R.id.realplay_display_view);
	        // 标题栏控件
	        mTitleArea = (RelativeLayout) findViewById(R.id.realplay_top_bar);
	        mBackBtn = (ImageButton) findViewById(R.id.realplay_back_btn);
	        mTitleTv = (TextView) findViewById(R.id.realplay_title_tv);
	        if (mCameraInfo != null) {
	            mTitleTv.setText(mCameraInfo.getCameraName());
	        }
	        
	        // 播放区域
	        mPlayArea = (RelativeLayout) findViewById(R.id.realplay_area);
	        mSurfaceView = (SurfaceView) findViewById(R.id.realplay_wnd_sv);
	        mSurfaceView.getHolder().addCallback(this);
	        mSurfaceView.setVisibility(View.INVISIBLE);

	        // 控制栏控件
	        mControlArea = (RelativeLayout) findViewById(R.id.realplay_control_bar);
	        mStopBtn = (ImageButton) findViewById(R.id.realplay_stop_btn);
	        mPlayBtn = (ImageButton) findViewById(R.id.realplay_play_btn);
	        
	        mStopBtn.setOnClickListener(this);
	        mPlayBtn.setOnClickListener(this);
	        mBackBtn.setOnClickListener(this);
	        mTitleArea.setOnClickListener(this);
	        mControlArea.setOnClickListener(this);
	        mTitleTv.setOnClickListener(this);
	        
	        mPagesAdapter = new SimpleRealPlayerAdapter(this);
	        mPagesAdapter.setOrientation(mOrientation);
	        mPagesAdapter.setDisplayWidthHeight(mDisplayWidth, mDisplayHeight);
	        mPagesAdapter.setCameraList(mCameraInfoList);
	        
	        mPagesGallery = (PagesGallery) findViewById(R.id.realplay_pages_gallery);
	        mPagesGallery.setVerticalFadingEdgeEnabled(false);
	        mPagesGallery.setHorizontalFadingEdgeEnabled(false);
	        mPagesGallery.setOnItemSelectedListener(this);
	        mPagesGallery.setAdapter(mPagesAdapter);
	        mPagesGallery.setSelection(mPosition);
	        mPagesGallery.setOnTouchListener(this);
	        
	       // mWaitDialog = new WaitDialog(SimpleRealPlayActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
	       // mWaitDialog.setCancelable(false);
	    }

	    @Override
	    protected void onStop() {
	        super.onStop();
	        
	        stopRealPlay(false);
	        mSurfaceView.setVisibility(View.INVISIBLE);
	    }
	    
	    private void setPlayLoadingUI() {
	        mSurfaceView.setVisibility(View.VISIBLE);
	        mPlayBtn.setVisibility(View.GONE);
	        mStopBtn.setVisibility(View.VISIBLE);
	        
	        if (mSelectedRealPlayerHolder != null) {
	            mSelectedRealPlayerHolder.mFigureIv.setVisibility(View.VISIBLE);
	            mSelectedRealPlayerHolder.mLoadingRL.setVisibility(View.VISIBLE);
	            mSelectedRealPlayerHolder.mPlayIv.setVisibility(View.GONE);
	        }        
	    }
	    
	    private void setPlayStopUI() {
	        mIsPlaying = false;
	        mSurfaceView.setVisibility(View.INVISIBLE);
	        mPlayBtn.setVisibility(View.VISIBLE);
	        mStopBtn.setVisibility(View.GONE);
	        
	        if (mSelectedRealPlayerHolder != null) {
	            mSelectedRealPlayerHolder.mFigureIv.setVisibility(View.VISIBLE);
	            mSelectedRealPlayerHolder.mLoadingRL.setVisibility(View.GONE);
	            mSelectedRealPlayerHolder.mPlayIv.setVisibility(View.VISIBLE);
	        }
	    }
	    
	    private void setPlayStartUI() {
	        mIsPlaying = true;
	        mSurfaceView.setVisibility(View.VISIBLE);
	        mPlayBtn.setVisibility(View.GONE);
	        mStopBtn.setVisibility(View.VISIBLE);
	        
	        if (mSelectedRealPlayerHolder != null) {
	            mSelectedRealPlayerHolder.mFigureIv.setVisibility(View.GONE);
	            mSelectedRealPlayerHolder.mLoadingRL.setVisibility(View.GONE);
	            mSelectedRealPlayerHolder.mPlayIv.setVisibility(View.GONE);
	        }          
	    }
	    
	    /**
	     * 停止播放
	     * 
	     * @see
	     * @since V2.0
	     */
	    private void stopRealPlay(boolean onScroll) {
	        LogUtil.debugLog(TAG, "stopRealPlay");

	        if (mRealPlayMgr != null) {
	            //停止预览任务
	            mRealPlayerHelper.stopRealPlayTask(mRealPlayMgr);
	        }

	        setPlayStopUI();

	        if (onScroll) {
	            if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
	                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	            }
	        } else {
	            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        }
	    }

	    /*
	     * (non-Javadoc)
	     * @see android.app.Activity#onDestroy()
	     */
	    @Override
	    protected void onDestroy() {     
	        super.onDestroy();
	        if (mScreenBroadcastReceiver != null) {
	            // 取消锁屏广播的注册
	            unregisterReceiver(mScreenBroadcastReceiver);
	        }

	        if (mPagesAdapter != null) {
	            mPagesAdapter.setCameraList(null);
	            mPagesAdapter.notifyDataSetChanged();
	            mPagesAdapter = null;
	        }
	        if (mPagesGallery != null) {
	            mSelectedRealPlayerHolder = null;
	            mPagesGallery.setAdapter(null);
	            mPagesGallery = null;
	        }
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {
	    }

	    @Override
	    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
	        if (null == mPagesAdapter || null == mPagesGallery || null == v) {
	            return;
	        }

	        LogUtil.debugLog(TAG, "onItemSelected");
		
	        if (mSelectedRealPlayerHolder == null) {
	            mSelectedRealPlayerHolder = (RealPlayerHolder) v.getTag();
	            if (mSelectedRealPlayerHolder != null) {
	                // 开始播放
	                startRealPlay();
	            }
	            return;
	        }

	        if (mPosition == position) {
	            return;
	        }

	        if (position < 0 || position > (mPagesAdapter.getCount() - 1)) {
	            return;
	        }

	        // 如果当前视频还在播放，还需要停止播放
	        stopRealPlay(true);

	        mSelectedRealPlayerHolder = (RealPlayerHolder) v.getTag();
	        mPosition = position;

	        CameraInfo cameraInfo = mCameraInfoList.get(mPosition);
	        if (cameraInfo == null) {
	            return;
	        }
	        mPlayRatio = 0;
	        mCameraInfo = cameraInfo;

	        mTitleTv.setText(mCameraInfo.getCameraName());

	        startRealPlay();
	    }

	    /**
	     * 定义屏幕的点击事件
	     * 
	     * @author fangzhihua
	     * @data 2013-3-19
	     */
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	        return mGestureDetector.onTouchEvent(event);
	    }

	    /**
	     * 定义左右滑动的监听
	     */
	    private final GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
	        @Override
	        public boolean onSingleTapUp(MotionEvent e) {
	            // TODO Auto-generated method stub
	            onSurfaceViewClick();
	            return true;
	        }

	        @Override
	        public boolean onDoubleTap(MotionEvent e) {
	            return true;
	        }
	    };

	    /**
	     * 开始播放
	     * 
	     * @see
	     * @since V1.0
	     */
	    private void startRealPlay() {
	        LogUtil.debugLog(TAG, "startRealPlay");
	        
	        // 检查网络是否可用
	        if (!ConnectionDetector.isNetworkAvailable(this)) {
	            // 提示没有连接网络
	            showPlayFailDialog(getString(R.string.realplay_play_fail_becauseof_network));
	            setPlayStopUI();
	            return;
	        }
	        
	        setPlayLoadingUI();
	        
	        mRealPlayMgr = new RealPlayerManager(this);
	        //设置Handler，接收处理消息
	        mRealPlayMgr.setHandler(mHandler);
	        //设置播放Surface
	        mRealPlayMgr.setPlaySurface(mSurfaceHolder);
	        //开启预览任务
	        if(TextUtils.isEmpty(mCameraInfo.getCameraId())) {
	            if(!TextUtils.isEmpty(EzvizAPI.getInstance().getUserCode())) {
	                mRealPlayerHelper.startLocalRealPlayTask(mRealPlayMgr, mCameraInfo.getDeviceId(), mDeviceDiscoverInfo.localIP, null);
	            } else {
	                mRealPlayerHelper.startEncryptLocalRealPlayTask(this, mRealPlayMgr, mCameraInfo.getDeviceId(), 
	                        mDeviceDiscoverInfo.localIP, R.string.realplay_password_error_title, R.string.realplay_login_password_msg, 0);
	            }
	        } else {
	            mRealPlayerHelper.startRealPlayTask(mRealPlayMgr, mCameraInfo.getCameraId());
	        }
	        
	        updateLoadingProgress(0);
	    }
	    
	    private void updateLoadingProgress(final int progress) {
	        if(mSelectedRealPlayerHolder == null || mSelectedRealPlayerHolder.mWaittingTv == null) {
	            return;
	        }
	        mSelectedRealPlayerHolder.mWaittingTv.setText(progress + "%");
	        mHandler.postDelayed(new Runnable() {

	            @Override
	            public void run() {
	                if(mSelectedRealPlayerHolder == null || mSelectedRealPlayerHolder.mWaittingTv == null) {
	                    return;
	                }
	                Random r = new Random();
	                mSelectedRealPlayerHolder.mWaittingTv.setText((progress + r.nextInt(20)) + "%");
	            }

	        }, 500);
	    }
	    
	    @Override
	    public boolean handleMessage(Message msg) {
	        switch (msg.what) {            
	            case RealPlayMsg.MSG_REALPLAY_PLAY_SUCCESS:
	                //处理预览成功
	                handlePlaySuccess(msg);
	                break;
	            case RealPlayMsg.MSG_REALPLAY_PLAY_FAIL:
	                //处理预览失败
	                handlePlayFail(msg.arg1);
	                break;
	            case RealPlayMsg.MSG_REALPLAY_PASSWORD_ERROR:
	                //处理播放密码错误
	                if(TextUtils.isEmpty(mCameraInfo.getCameraId())) {
	                    Utils.showToast(this, R.string.realplay_login_password_error, msg.arg1);
	                    handlePasswordError(R.string.realplay_password_error_title, 
	                            R.string.realplay_login_password_msg, 0, false);                    
	                } else {
	                    handlePasswordError(R.string.realplay_password_error_title, 
	                            R.string.realplay_password_error_message3,
	                            R.string.realplay_password_error_message1, false);
	                }
	                break;
	            case RealPlayMsg.MSG_REALPLAY_ENCRYPT_PASSWORD_ERROR:
	                // 处理播放密码错误
	                handlePasswordError(R.string.realplay_encrypt_password_error_title,
	                        R.string.realplay_encrypt_password_error_message, 0, true);
	                break;    
	                
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
	                break;                
	            default:
	                break;
	        }
	        return false;
	    }

	    /**
	     * 处理播放成功的情况
	     * 
	     * @see
	     * @since V1.0
	     */
	    private void handlePlaySuccess(Message msg) {
	        if (msg.arg1 != 0) {
	            mPlayRatio = Math.min((double) msg.arg2 / msg.arg1, Constant.LIVE_VIEW_RATIO);
	        }
	        if (mPlayRatio != 0) {
	            setPlayViewPosition();
	        }
	        
	        setPlayStartUI();
	        
	        // 开启旋转传感器
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
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
	        stopRealPlay(false);

	        String msg = null;
	        // 判断返回的错误码
	        switch (errorCode) {
	            case HCNetSDKException.NET_DVR_PASSWORD_ERROR:
	            case HCNetSDKException.NET_DVR_NOENOUGHPRI:
		     case ErrorCode.ERROR_DVR_LOGIN_USERID_ERROR:
	                // 弹出密码输入框
	                handlePasswordError(R.string.realplay_password_error_title,
	                        R.string.realplay_password_error_message3, 
	                        R.string.realplay_password_error_message1,
	                        false);
	                return;
	            case ErrorCode.ERROR_WEB_SESSION_ERROR:
	            case ErrorCode.ERROR_WEB_SESSION_EXPIRE:
	            case ErrorCode.ERROR_CAS_PLATFORM_CLIENT_NO_SIGN_RELEATED:
	            case ErrorCode.ERROR_WEB_HARDWARE_SIGNATURE_ERROR:
	                EzvizAPI.getInstance().gotoLoginPage();
	                return;               
	            case RtspClientException.RTSPCLIENT_DEVICE_CONNECTION_LIMIT:
	            case HCNetSDKException.NET_DVR_RTSP_OVER_MAX_CHAN:
	            case RtspClientException.RTSPCLIENT_OVER_MAXLINK:
	            case HCNetSDKException.NET_DVR_OVER_MAXLINK:
	                msg = getString(R.string.remoteplayback_over_link);
	                break;
	            case ErrorCode.ERROR_WEB_DIVICE_NOT_ONLINE:
	            case ErrorCode.ERROR_RTSP_NOT_FOUND:
	            case ErrorCode.ERROR_CAS_PLATFORM_CLIENT_REQUEST_NO_PU_FOUNDED:
	                msg = getString(R.string.realplay_fail_device_not_exist);
	                break;
	            case ErrorCode.ERROR_WEB_DIVICE_SO_TIMEOUT:
	                msg = getString(R.string.realplay_fail_connect_device);
	                break;
	            case HCNetSDKException.NET_DVR_RTSP_PRIVACY_STATUS:
	            case RtspClientException.RTSPCLIENT_PRIVACY_STATUS:
	                msg = getString(R.string.realplay_set_fail_status);
	                break;
	            case InnerException.INNER_DEVICE_NOT_EXIST:
	                msg = getString(R.string.camera_not_online);
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
	                break; */          
	            default:
	                msg = Utils.getErrorTip(this, R.string.realplay_play_fail, errorCode);
	                break;
	        }

	        if(!TextUtils.isEmpty(msg)) {
	            showPlayFailDialog(msg);
	        }
	    }

	    // 处理密码错误
	    private void handlePasswordError(int title_resid, int msg1_resid, int msg2_resid, final boolean isEncrypt) {
	        stopRealPlay(false);
	        setPlayStopUI();
	        
	        LogUtil.debugLog(TAG, "startRealPlay");
	        
	        // 检查网络是否可用
	        if (!ConnectionDetector.isNetworkAvailable(this)) {
	            // 提示没有连接网络
	            showPlayFailDialog(getString(R.string.realplay_play_fail_becauseof_network));
	            return;
	        }
	        
	        setPlayLoadingUI();
	        
	        mRealPlayMgr = new RealPlayerManager(this);
	        //设置Handler，接收处理消息
	        mRealPlayMgr.setHandler(mHandler);
	        //设置播放Surface
	        mRealPlayMgr.setPlaySurface(mSurfaceHolder);
	        //开启加密预览任务
	        if(TextUtils.isEmpty(mCameraInfo.getCameraId())) {
	            mRealPlayerHelper.startEncryptLocalRealPlayTask(this, mRealPlayMgr, mCameraInfo.getDeviceId(), 
	                    mDeviceDiscoverInfo.localIP, title_resid, msg1_resid, msg2_resid);
	        } else {
	            mRealPlayerHelper.startEncryptRealPlayTask(this, mRealPlayMgr, mCameraInfo.getCameraId(), title_resid, msg1_resid, msg2_resid);
	        }     
	        
	        updateLoadingProgress(0);  
	    }

	    /**
	     * 设置播放窗口位置
	     * 
	     * @see
	     * @since V1.0
	     */
	    private void setPlayViewPosition() {
	        int canDisplayHeight = mCanDisplayRect.height();
	        int canDisplayWidth = mCanDisplayRect.width();
	        if (canDisplayHeight == 0 || canDisplayWidth == 0) {
	            return;
	        }

	        // 设置播放窗口位置
	        RelativeLayout.LayoutParams lp = Utils.getPlayViewLp(mPlayRatio, mOrientation, canDisplayWidth, canDisplayHeight,
	                mDisplayWidth, mDisplayHeight);
	        mSurfaceView.setLayoutParams(lp);
	        if (mPagesAdapter != null) {
	            mPagesAdapter.setOrientation(mOrientation);
	            if (mSelectedRealPlayerHolder != null) {
	                mPagesAdapter.updateFigureIvLayoutParams(mSelectedRealPlayerHolder.mFigureIv, lp.height + 2);
	            }
	        }
	    }

	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        mOrientation = newConfig.orientation;
	        onOrientation();
	        super.onConfigurationChanged(newConfig);

	    }

	    /**
	     * 屏幕旋转响应
	     * 
	     * @see
	     * @since V1.0
	     */
	    private void onOrientation() {
	        setPlayViewPosition();

	        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
	            // 横屏处理
	            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

	            mDisplayView.setBackgroundColor(getResources().getColor(R.color.black));

	            mTitleArea.setVisibility(View.GONE);
	            mControlArea.setVisibility(View.GONE);
	        } else {
	            // 竖屏处理
	            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

	            mDisplayView.setBackgroundColor(getResources().getColor(R.color.azure));
	            mTitleArea.setVisibility(View.VISIBLE);
	            mControlArea.setVisibility(View.VISIBLE);

	            if (!mIsPlaying) {
	                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	            }
	        }
	    }

	    /*
	     * (non-Javadoc)
	     * @see android.view.View.OnClickListener#onClick(android.view.View)
	     */
	    @Override
	    public void onClick(View view) {
	        if (view.getId() == R.id.realplay_back_btn) {
	            finish();
	        } else if (view.getId() == R.id.realplay_stop_btn) {
	            stopRealPlay(false);
	        } else if (view.getId() == R.id.realplay_play_btn) {
	            startRealPlay();
	        } else if (view.getId() == R.id.realplay_play_iv) {
	            startRealPlay();            
	        }
	    }

	    /**
	     * 播放窗口事件
	     * 
	     * @since V1.0
	     */
	    private void onSurfaceViewClick() {
	        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
	            if(mTitleArea.getVisibility() == View.VISIBLE) {
	                // 显示窗口
	                mTitleArea.setVisibility(View.VISIBLE);
	                mControlArea.setVisibility(View.VISIBLE);
	            } else {
	                mTitleArea.setVisibility(View.GONE);
	                mControlArea.setVisibility(View.GONE);
	            }
	        }
	    }

	    /**
	     * screen状态广播接收者
	     */
	    private class ScreenBroadcastReceiver extends BroadcastReceiver {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
	                stopRealPlay(false);
	            }
	        }
	    }

	    @Override
	    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	        // TODO Auto-generated method stub
	    }
	    
	    private void showPlayFailDialog(String msg) {
	        dismissDialog(mPlayFailDialog);
	        mPlayFailDialog = null;
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage(msg);
	        builder.setPositiveButton(R.string.confirm, null);
	        if (!isFinishing()) {
	            mPlayFailDialog = builder.show();
	        }
	    }

	    /*
	     * (non-Javadoc)
	     * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
	     */
	    @Override
	    public void surfaceCreated(SurfaceHolder holder) {
	        // 获取surfaceView可现实区域的大小
	        if (mCanDisplayRect.width() <= 0 || mCanDisplayRect.height() <= 0) {
	            int left = 0;
	            int right = mPlayArea.getWidth();
	            int top = mTitleArea.getHeight();
	            int bottom = mPlayArea.getHeight() - mControlArea.getHeight();
	            mCanDisplayRect.set(left, top, right, bottom);
	        }

	        setPlayViewPosition();

	        if (mRealPlayMgr != null) {
	            //设置播放Surface
	            mRealPlayMgr.setPlaySurface(holder);
	        }
	        mSurfaceHolder = holder;

	        LogUtil.debugLog(TAG, "surfaceCreated");
	    }

	    /*
	     * (non-Javadoc)
	     * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
	     */
	    @Override
	    public void surfaceDestroyed(SurfaceHolder holder) {
	        LogUtil.debugLog(TAG, "surfaceDestroyed");
	        if (mRealPlayMgr != null) {
	            //设置播放Surface
	            mRealPlayMgr.setPlaySurface(holder);
	        }
	        mSurfaceHolder = null;
	    }

	    private void dismissDialog(AlertDialog popDialog) {
	        if (popDialog != null && popDialog.isShowing() && !isFinishing()) {
	            try {
	                popDialog.dismiss();
	            } catch (Exception e) {
	                // TODO: handle exception
	            }
	        }
	    }

	    /* (non-Javadoc)
	     * @see com.videogo.ui.util.OpenYSService.OpenYSServiceListener#onOpenYSService(int)
	     */
	    /*@Override
	    public void onOpenYSService(int result) {
	        if(result == 0) {
	            // 开始播放
	            startRealPlay();
	        }
	    }

	     (non-Javadoc)
	     * @see com.videogo.ui.util.SecureValidate.SecureValidateListener#onSecureValidate(int)
	     
	    @Override
	    public void onSecureValidate(int result) {
	        if(result == 0) {
	            // 开始播放
	            startRealPlay();
	        }
	    }*/
	}


