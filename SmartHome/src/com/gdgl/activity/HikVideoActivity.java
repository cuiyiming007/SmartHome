/**
 * <p>DemoActivity Class</p>
 * @author zhuzhenlei 2014-7-17
 * @version V1.0  
 * @modificationHistory
 * @modify by user: 
 * @modify by reason:
 */
package com.gdgl.activity;

import h264.com.BigScreenshotDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.MediaPlayer.PlayM4.Player;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gdgl.mydata.video.VideoNode;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.ComUtil;
import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_CHANNELSTATE_V30;
import com.hikvision.netsdk.NET_DVR_CLIENTINFO;
import com.hikvision.netsdk.NET_DVR_COMPRESSIONCFG_V30;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_IPPARACFG_V40;
import com.hikvision.netsdk.NET_DVR_PLAYBACK_INFO;
import com.hikvision.netsdk.NET_DVR_RESOLVE_DEVICEINFO;
import com.hikvision.netsdk.NET_DVR_TIME;
import com.hikvision.netsdk.NET_DVR_WORKSTATE_V30;
import com.hikvision.netsdk.PTZCommand;
import com.hikvision.netsdk.PTZPresetCmd;
import com.hikvision.netsdk.PlaybackCallBack;
import com.hikvision.netsdk.PlaybackControlCommand;
import com.hikvision.netsdk.RealPlayCallBack;
//import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;

/**
 * <pre>
 *  ClassName  DemoActivity Class
 * </pre>
 * 
 * @author zhuzhenlei
 * @version V1.0
 * @modificationHistory
 */
public class HikVideoActivity extends ActionBarActivity implements Callback {
	private Player m_oPlayerSDK = null;
	private Button m_oLoginBtn = null;
	private Button m_oPreviewBtn = null;
	private Button m_oPlaybackBtn = null;
	private Button m_oParamCfgBtn = null;
	private Button m_oCaptureBtn = null;
	private Button m_oRecordBtn = null;
	private Button m_oSoundBtn = null;
	private Button m_oPTZBtn = null;
	private Button m_oPresetBtn = null;
	private SurfaceView m_osurfaceView = null;

	private Toolbar mToolbar;
	private ActionBar mActionBar;
	private Button btn_last, btn_next;
	private Gallery gallery_pic;
	private View lay_pic, toolbar_card,play_main_lay;//
	//private ImageView img_big_pic;
	public static ArrayList<Bitmap> screenPictures; 		//=====0915
	
	private ArrayList<String> mListPic;
	private ImageAdapter mImageAdapter;

	private NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;
	private NET_DVR_RESOLVE_DEVICEINFO m_oNetDvrResolveDeviceInfo = null;

	private int iFirstChannelNo = -1; // get start channel no
	private int m_iLogID = -1; // return by NET_DVR_Login_v30
	private int m_iPlayID = -1; // return by NET_DVR_RealPlay_V30
	private int m_iPlaybackID = -1; // return by NET_DVR_PlayBackByTime
	private int m_iPort = -1; // play port
	private int width,height,Position;
	private float height_to_width,width_to_height;
	
	private final String TAG = "HikVideoActivity";

	Player.MPInteger StWidth,StHeight;
	
	private boolean m_bRecord = false;
	private boolean m_bSoundOn = false;
	private boolean m_bPreset1 = false;
	private boolean m_bPTZL = false;
	private boolean playOrnot = false;

	private String m_titleName;
	private String m_DVRName;
	private String m_DVRSerialNumber;
	private String m_oIPAddr;
	private String m_oPort;
	private String m_oUser;
	private String m_oPsd;

	private VideoNode mVideoNode;

	private int LAN = 1;
	private int INTERNET = 2;
	
	private Handler mHandler;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hik_activity);
		
		
		if (!initVideoData()) {
			this.finish();
			return;
		}

		if (!initeSdk()) {
			this.finish();
			return;
		}

		if (!initeActivity()) {
			this.finish();
			return;
		}
		mHandler = new Handler(){
			public void handleMessage(Message msg){
				switch(msg.what){
				case 1:
					Toast.makeText(HikVideoActivity.this, "播放失败,请重新访问!",Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};

		loginVideo();
		

		// 萤石
		// m_oPsd.setText("CMXZIZ");
		// mDVRSerialNumber = "451627563";
		// playVideo();
	}
	
	private void makeFile(){
		File file = new File(ComUtil.picturePath + "/" + m_DVRSerialNumber);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	private void updateList(){ 
		File file = new File(ComUtil.picturePath + "/" + m_DVRSerialNumber + "/");
		mListPic = new ArrayList<String>();
		screenPictures = new ArrayList<Bitmap>(); 
        File[] files = file.listFiles();  
        if(files.length > 0){
        	for(int i=files.length-1; i>=0; i--){
        		mListPic.add(files[i].getAbsolutePath());
        		screenPictures.add(getBmpFromFile(files[i].getAbsolutePath(), false));
	        }  
        }
	}
	
	private void updateGallery(){
		updateList();
		lay_pic.setVisibility(View.VISIBLE);
		mImageAdapter.setList(mListPic);
		mImageAdapter.notifyDataSetChanged();
		gallery_pic.setSelection(0);
	}
	
	private class ImageAdapter extends BaseAdapter{
		private Context context;
		private ArrayList<String> mList;		
	    public ImageAdapter(Context c, ArrayList<String> mList){
	    	this.context = c;
	        this.mList = mList;
	    }
	    
	    public void setList(ArrayList<String> mList){
	    	this.mList = mList;
	    }
	    
	    @Override
	    public int getCount() {
	        return mList.size();
	    }
	    @Override
	    public Object getItem(int position) {
	        // TODO Auto-generated method stub
	        return position;
	    }
	    @Override
	    public long getItemId(int position) {
	        // TODO Auto-generated method stub
	        return position;
	    }
	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	        ImageView imageView = new ImageView(context);
	        //imageView.setImageResource(R.drawable.btn_increment_pressed);
	        imageView.setImageBitmap(getBmpFromFile(mList.get(position), true));
	        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	        //imageView.setLayoutParams(new Gallery.LayoutParams(100, 100*9/11));
	        imageView.setLayoutParams(new Gallery.LayoutParams(200, 200));
	        imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BigScreenshotDialog bigPicDialog = new BigScreenshotDialog(HikVideoActivity.this,position,width,height);
					bigPicDialog.show();
				}
			});
	        return imageView;
	    }
	}
	
	private Bitmap getBmpFromFile(String path, boolean isSmall){
		Bitmap bitmap = null;
		if(isSmall){
			BitmapFactory.Options options = new BitmapFactory.Options();  
		    options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false  	
		    bitmap =BitmapFactory.decodeFile(path,options);  
		    options.inJustDecodeBounds = false;  
		    int height = options.outHeight * 100 / options.outWidth; 
		    options.outWidth = 100;
		    options.outHeight = height;  
		    options.inJustDecodeBounds = false; 
		    options.inSampleSize = options.outWidth / 100; 
		    options.inJustDecodeBounds = false;
		    options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // 默认是Bitmap.Config.ARGB_8888 
		    options.inPurgeable = true; 
		    options.inInputShareable = true; 
		    bitmap = BitmapFactory.decodeFile(path,options);  
		}else{
			bitmap =BitmapFactory.decodeFile(path, null);  
		}
		return bitmap;
	}
	
	private void loginVideo() {
		try {
			if (m_iLogID < 0) {
				// login on the device
				m_iLogID = loginDevice();
				if (m_iLogID < 0) {
					Log.e(TAG, "This device logins failed!");
					return;
				}
				// get instance of exception callback and set
				ExceptionCallBack oexceptionCbf = getExceptiongCbf();
				if (oexceptionCbf == null) {
					Log.e(TAG, "ExceptionCallBack object is failed!");
					return;
				}

				if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(
						oexceptionCbf)) {
					Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
					return;
				}

				Log.i(TAG,
						"Login sucess ****************************1***************************");
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						playVideo();
					}
				}).start();
			} else {
				// whether we have logout
				if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID)) {
					Log.e(TAG, " NET_DVR_Logout is failed!");
					return;
				}
				m_iLogID = -1;
			}
		} catch (Exception err) {
			Log.e(TAG, "error: " + err.toString());
		}
	}

	private void playVideo() {
		try {
			if (m_iLogID < 0) {
				Log.e(TAG, "please login on device first");
				return;
			}
			if (m_iPlayID < 0) {
				if (m_iPlaybackID >= 0) {
					Log.i(TAG, "Please stop palyback first");
					return;
				}
				RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
				if (fRealDataCallBack == null) {
					Log.e(TAG, "fRealDataCallBack object is failed!");
					return;
				}

				NET_DVR_IPPARACFG_V40 struIPPara = new NET_DVR_IPPARACFG_V40();
				HCNetSDK.getInstance().NET_DVR_GetDVRConfig(m_iLogID,
						HCNetSDK.NET_DVR_GET_IPPARACFG_V40, 0, struIPPara);

				if (struIPPara.dwAChanNum > 0) {
					iFirstChannelNo = 1;
				} else {
					iFirstChannelNo = struIPPara.dwStartDChan;
				}

				if (iFirstChannelNo <= 0) {
					iFirstChannelNo = 1;
				}

				Log.i(TAG, "iFirstChannelNo:" + iFirstChannelNo);

				NET_DVR_CLIENTINFO ClientInfo = new NET_DVR_CLIENTINFO();
				ClientInfo.lChannel = iFirstChannelNo; // start channel no +
														// preview channel
				ClientInfo.lLinkMode = 1 << 31; // bit 31 -- 0,main stream;1,sub
												// stream
												// bit 0~30 -- link
												// type,0-TCP;1-UDP;2-multicast;3-RTP
				ClientInfo.sMultiCastIP = null;

				// net sdk start preview
				m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V30(
						m_iLogID, ClientInfo, fRealDataCallBack, true);
				if (m_iPlayID < 0) {
					Log.e(TAG, "NET_DVR_RealPlay is failed!Err:"
							+ HCNetSDK.getInstance().NET_DVR_GetLastError());
					return;
				}

				Log.i(TAG,
						"NetSdk Play sucess ***********************3***************************");

			} else {
				stopPlay();
			}
		} catch (Exception err) {
			Log.e(TAG, "error: " + err.toString());
		}
	}

	// @Override
	public void surfaceCreated(SurfaceHolder holder) {//=====20150909====不确定有什么作用、何时调用
		/*Log.i(TAG, "surface is created" + m_iPort);
		
		if (-1 == m_iPort) {
			return;
		}
		Log.i("miport", m_iPort + "");
		Surface surface = holder.getSurface();
		if (null != m_oPlayerSDK && true == surface.isValid()) {
			if (false == m_oPlayerSDK.setVideoWindow(m_iPort, 0, holder)) {
				Log.e(TAG, "Player setVideoWindow failed!");
			}
		}*/
	}

	// @Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	// @Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "Player setVideoWindow release!" + m_iPort);
		if (-1 == m_iPort) {
			return;
		}
		if (null != m_oPlayerSDK && true == holder.getSurface().isValid()) {
			if (false == m_oPlayerSDK.setVideoWindow(m_iPort, 0, null)) {
				Log.e(TAG, "Player setVideoWindow failed!");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("m_iPort", m_iPort);
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		m_iPort = savedInstanceState.getInt("m_iPort");
		super.onRestoreInstanceState(savedInstanceState);
		Log.i(TAG, "onRestoreInstanceState");
	}

	/**
	 * @fn initeSdk
	 * @author zhuzhenlei
	 * @brief SDK init
	 * @param NULL
	 *            [in]
	 * @param NULL
	 *            [out]
	 * @return true - success;false - fail
	 */
	private boolean initeSdk() {
		// init net sdk
		if (!HCNetSDK.getInstance().NET_DVR_Init()) {
			Log.e(TAG, "HCNetSDK init is failed!");
			return false;
		}
		HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/",
				true);

		// init player
		m_oPlayerSDK = Player.getInstance();
		Log.i("m_oPlayerSDK", "success1111111111");
		if (m_oPlayerSDK == null) {
			Log.e(TAG, "PlayCtrl getInstance failed!");
			return false;
		}

		return true;
	}

	private boolean initVideoData() {
		Bundle extras_ipc_channel = getIntent().getExtras();
		if (null != extras_ipc_channel) {
			mVideoNode = (VideoNode) extras_ipc_channel
					.getParcelable(VideoFragment.PASS_OBJECT);
		}

		if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
			m_oIPAddr = "www.hik-online.com"; // 外网IP
			m_oPort = "80"; // 外网端口
		} else {
			m_oIPAddr = mVideoNode.getIpc_ipaddr(); // 内网IP
			m_oPort = mVideoNode.getHttpport(); // 内网端口
		}
		m_titleName = mVideoNode.getAliases();
		m_oUser = mVideoNode.getName();
		m_oPsd = mVideoNode.getPassword();
		m_DVRName = "";
		m_DVRSerialNumber = mVideoNode.getSerialNum();
		makeFile();
		updateList();
		return true;
	}

	// GUI init
	private boolean initeActivity() {
		findViews();
		setListeners();
		initSurface();
		m_osurfaceView.getHolder().addCallback(this);
		//setListeners();
		return true;
	}

	private void initSurface() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		 width = wm.getDefaultDisplay().getWidth();
		 height = wm.getDefaultDisplay().getHeight();
		if (playOrnot == false) {
			
			/*if (m_iPort >= 0) {//------20150908
				return;
			}*/
			m_iPort = m_oPlayerSDK.getPort();
			Player.MPInteger StWidth = new Player.MPInteger();//--20150907
			Player.MPInteger StHeight = new Player.MPInteger();//--20150907
			m_oPlayerSDK.getPictureSize(m_iPort, StWidth, StHeight);
		    height_to_width = (float) StHeight.value / (float) StWidth.value;
		    width_to_height = (float) StWidth.value / (float) StHeight.value;
			if (m_iPort == -1) {
				height_to_width = 0.8f ;
				width_to_height = 1.2f ;
				Toast toast = Toast.makeText(this, "获取通道失败", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
			}
		}
		 		
		if (width < height) {	//=========竖屏
			this.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			m_oCaptureBtn.setVisibility(View.VISIBLE);
			if(mListPic.size() > 0){
				lay_pic.setVisibility(View.VISIBLE);	
			}
			toolbar_card.setVisibility(View.VISIBLE);
			//play_main_lay.setBackgroundColor(getResources().getColor(R.color.white));
			play_main_lay.setBackgroundColor(android.graphics.Color.WHITE);
			m_osurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(
					width, (int) (width *  height_to_width)));
			/*m_osurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(
					width, width / 11 * 9));*/
		} else {			//==========横屏
			this.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			m_oPlayerSDK.play(m_iPort, m_osurfaceView.getHolder());
			//this.getWindow().setBackgroundDrawable(drawable);
			toolbar_card.setVisibility(View.GONE);
			//play_main_lay.setBackgroundColor(getResources().getColor(R.color.black));
			play_main_lay.setBackgroundColor(android.graphics.Color.BLACK);
			m_oCaptureBtn.setVisibility(View.GONE);
			lay_pic.setVisibility(View.GONE);
			m_osurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(
					(int) (height * width_to_height), height));
		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		initSurface();
		if((BigScreenshotDialog.dialog)!= null){
			if((BigScreenshotDialog.dialog).isShowing()){
				Position = (BigScreenshotDialog.mViewPager).getCurrentItem();
				BigScreenshotDialog.dialog.cancel();
				BigScreenshotDialog bigPicDialog = new BigScreenshotDialog(HikVideoActivity.this,Position,width,height);
				bigPicDialog.show();
			}
		}
		//m_oPlayerSDK.play(m_iPort, m_osurfaceView.getHolder());
	}

	// get controller instance
	private void findViews() {
		m_oCaptureBtn = (Button) findViewById(R.id.btn_Capture);
		m_osurfaceView = (SurfaceView) findViewById(R.id.Sur_Player);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		btn_last = (Button)findViewById(R.id.btn_last);
		btn_next = (Button)findViewById(R.id.btn_next);
		gallery_pic = (Gallery)findViewById(R.id.gallery_pic);
		mImageAdapter = new ImageAdapter(this, mListPic);
		gallery_pic.setAdapter(mImageAdapter);
		lay_pic = (View)findViewById(R.id.lay_pic);
		toolbar_card = (View)findViewById(R.id.toolbar_card);
		play_main_lay = (View)findViewById(R.id.play_main_lay);
		setSupportActionBar(mToolbar);
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBar.setTitle(m_titleName);

	}

	// listen
	private void setListeners() {
		btn_last.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(gallery_pic.getSelectedItemPosition() > 0){
					gallery_pic.setSelection(gallery_pic.getSelectedItemPosition()-1);
				}
			}
		});
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(gallery_pic.getSelectedItemPosition() < mListPic.size()-1){
					gallery_pic.setSelection(gallery_pic.getSelectedItemPosition()+1);
				}
			}
		});
		m_oCaptureBtn.setOnClickListener(Capture_Listener);
	}
	

	// ptz listener
	private Button.OnTouchListener PTZ_Listener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			try {
				if (m_iLogID < 0) {
					Log.e(TAG, "please login on a device first");
					return false;
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (m_bPTZL == false) {
						if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
								m_iLogID, iFirstChannelNo, PTZCommand.PAN_LEFT,
								0)) {
							Log.e(TAG,
									"start PAN_LEFT failed with error code: "
											+ HCNetSDK.getInstance()
													.NET_DVR_GetLastError());
						} else {
							Log.i(TAG, "start PAN_LEFT succ");
						}
					} else {
						if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
								m_iLogID, iFirstChannelNo,
								PTZCommand.PAN_RIGHT, 0)) {
							Log.e(TAG,
									"start PAN_RIGHT failed with error code: "
											+ HCNetSDK.getInstance()
													.NET_DVR_GetLastError());
						} else {
							Log.i(TAG, "start PAN_RIGHT succ");
						}
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					if (m_bPTZL == false) {
						if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
								m_iLogID, iFirstChannelNo, PTZCommand.PAN_LEFT,
								1)) {
							Log.e(TAG, "stop PAN_LEFT failed with error code: "
									+ HCNetSDK.getInstance()
											.NET_DVR_GetLastError());
						} else {
							Log.i(TAG, "stop PAN_LEFT succ");
						}
						m_bPTZL = true;
						m_oPTZBtn.setText("PTZ(R)");
					} else {
						if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
								m_iLogID, iFirstChannelNo,
								PTZCommand.PAN_RIGHT, 1)) {
							Log.e(TAG,
									"stop PAN_RIGHT failed with error code: "
											+ HCNetSDK.getInstance()
													.NET_DVR_GetLastError());
						} else {
							Log.i(TAG, "stop PAN_RIGHT succ");
						}
						m_bPTZL = false;
						m_oPTZBtn.setText("PTZ(L)");
					}
				}
				return true;
			} catch (Exception err) {
				Log.e(TAG, "error: " + err.toString());
				return false;
			}
		}
	};
	// preset listener
	private Button.OnClickListener Preset_Listener = new OnClickListener() {
		public void onClick(View v) {
			try {
				if (m_iLogID < 0) {
					Log.e(TAG, "please login on a device first");
					return;
				}
				if (m_bPreset1 == false) {
					if (!HCNetSDK.getInstance().NET_DVR_PTZPreset_Other(
							m_iLogID, iFirstChannelNo,
							PTZPresetCmd.GOTO_PRESET, 1)) {
						Log.e(TAG, "GOTO_PRESET failed with error code:"
								+ HCNetSDK.getInstance().NET_DVR_GetLastError());
						return;
					}
					m_bPreset1 = true;
					m_oPresetBtn.setText("Preset(2)");
				} else {
					if (!HCNetSDK.getInstance().NET_DVR_PTZPreset_Other(
							m_iLogID, iFirstChannelNo,
							PTZPresetCmd.GOTO_PRESET, 2)) {
						Log.e(TAG, "GOTO_PRESET failed with error code:"
								+ HCNetSDK.getInstance().NET_DVR_GetLastError());
						return;
					}
					m_oPresetBtn.setText("Preset(1)");
					m_bPreset1 = false;
				}
			} catch (Exception err) {
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};
	// sound listener
	private Button.OnClickListener Sound_Listener = new Button.OnClickListener() {
		public void onClick(View v) {
			try {
				if (m_iPort < 0) {
					Log.e(TAG, "please start preview first");
					return;
				}
				if (m_bSoundOn == false) {
					if (!m_oPlayerSDK.playSound(m_iPort)) {
						Log.e(TAG, "playSound failed with error code:"
								+ m_oPlayerSDK.getLastError(m_iPort));
						return;
					}
					m_bSoundOn = true;
				} else {
					if (!m_oPlayerSDK.stopSound()) {
						Log.e(TAG, "stopSound failed with error code:"
								+ m_oPlayerSDK.getLastError(m_iPort));
						return;
					}
					m_bSoundOn = false;
				}
			} catch (Exception err) {
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};
	// record listener
	private Button.OnClickListener Record_Listener = new Button.OnClickListener() {
		public void onClick(View v) {
			try {
				if (m_bRecord == false) {
					if (m_iPlayID < 0) {
						Log.e(TAG, "please start preview first");
						return;
					}
					SimpleDateFormat sDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd-hh:mm:ss");
					String date = sDateFormat.format(new java.util.Date());
					if (!HCNetSDK.getInstance().NET_DVR_SaveRealData(m_iPlayID,
							"/mnt/sdcard/" + date + ".mp4")) {
						Log.e(TAG,
								"NET_DVR_SaveRealData failed with error code:"
										+ HCNetSDK.getInstance()
												.NET_DVR_GetLastError());
						return;
					}
					m_bRecord = true;
					m_oRecordBtn.setText("Stop");
				} else {
					HCNetSDK.getInstance().NET_DVR_StopSaveRealData(m_iPlayID);
					m_bRecord = false;
					m_oRecordBtn.setText("Record");
				}
			} catch (Exception err) {
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};
	// capture listener
	private Button.OnClickListener Capture_Listener = new Button.OnClickListener() {
		public void onClick(View v) {
			try {
				if (m_iPort < 0) {
					Log.e(TAG, "please start preview first");
					return;
				}
				Player.MPInteger stWidth = new Player.MPInteger();
				Player.MPInteger stHeight = new Player.MPInteger();
				if (!m_oPlayerSDK.getPictureSize(m_iPort, stWidth, stHeight)) {
					Log.e(TAG, "getPictureSize failed with error code:"
							+ m_oPlayerSDK.getLastError(m_iPort));
					return;
				}
				int nSize = 5 * stWidth.value * stHeight.value;
				byte[] picBuf = new byte[nSize];
				Player.MPInteger stSize = new Player.MPInteger();
				if (!m_oPlayerSDK.getBMP(m_iPort, picBuf, nSize, stSize)) {
					Log.e(TAG,
							"getBMP failed with error code:"
									+ m_oPlayerSDK.getLastError(m_iPort));
					return;
				}

				SimpleDateFormat sDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd-hh:mm:ss");
				String date = sDateFormat.format(new java.util.Date());
				String picName = ComUtil.setViedoFileNameBySysTime(Integer
						.parseInt(mVideoNode.getId()));
				FileOutputStream file = new FileOutputStream(
						ComUtil.picturePath + "/" + m_DVRSerialNumber + "/" +picName);
				Toast.makeText(HikVideoActivity.this,
						"截图保存到" + ComUtil.picturePath + "/" + m_DVRSerialNumber + "/" +picName,
						Toast.LENGTH_SHORT).show();
				file.write(picBuf, 0, stSize.value);
				file.close();
				updateGallery();
			} catch (Exception err) {
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};
	// playback listener
	private Button.OnClickListener Playback_Listener = new Button.OnClickListener() {

		public void onClick(View v) {
			try {
				if (m_iLogID < 0) {
					Log.e(TAG, "please login on a device first");
					return;
				}

				if (m_iPlaybackID < 0) {
					if (m_iPlayID >= 0) {
						Log.i(TAG, "Please stop preview first");
						return;
					}
					PlaybackCallBack fPlaybackCallBack = getPlayerbackPlayerCbf();
					if (fPlaybackCallBack == null) {
						Log.e(TAG, "fPlaybackCallBack object is failed!");
						return;
					}
					NET_DVR_TIME struBegin = new NET_DVR_TIME();
					NET_DVR_TIME struEnd = new NET_DVR_TIME();

					struBegin.dwYear = 2014;
					struBegin.dwMonth = 7;
					struBegin.dwDay = 17;
					struBegin.dwHour = 0;
					struBegin.dwMinute = 0;
					struBegin.dwSecond = 0;

					struEnd.dwYear = 2014;
					struEnd.dwMonth = 7;
					struEnd.dwDay = 17;
					struEnd.dwHour = 23;
					struEnd.dwMinute = 0;
					struEnd.dwSecond = 0;

					m_iPlaybackID = HCNetSDK.getInstance()
							.NET_DVR_PlayBackByTime(m_iLogID, 1, struBegin,
									struEnd);
					if (m_iPlaybackID >= 0) {
						if (!HCNetSDK.getInstance()
								.NET_DVR_SetPlayDataCallBack(m_iPlaybackID,
										fPlaybackCallBack)) {
							Log.e(TAG, "Set playback callback failed!");
							return;
						}
						NET_DVR_PLAYBACK_INFO struPlaybackInfo = null;
						if (!HCNetSDK
								.getInstance()
								.NET_DVR_PlayBackControl_V40(
										m_iPlaybackID,
										PlaybackControlCommand.NET_DVR_PLAYSTART,
										null, 0, struPlaybackInfo)) {
							Log.e(TAG, "net sdk playback start failed!");
							return;
						}
						m_oPlaybackBtn.setText("Stop");
					} else {
						Log.i(TAG,
								"NET_DVR_PlayBackByTime failed, error code: "
										+ HCNetSDK.getInstance()
												.NET_DVR_GetLastError());
					}
				} else {
					if (!HCNetSDK.getInstance().NET_DVR_StopPlayBack(
							m_iPlaybackID)) {
						Log.e(TAG, "net sdk stop playback failed");
					}
					// player stop play
					if (!m_oPlayerSDK.stop(m_iPort)) {
						Log.e(TAG, "player_stop is failed!");
					}
					if (!m_oPlayerSDK.closeStream(m_iPort)) {
						Log.e(TAG, "closeStream is failed!");
					}
					if (!m_oPlayerSDK.freePort(m_iPort)) {
						Log.e(TAG, "freePort is failed!" + m_iPort);
					}
					m_iPort = -1;
					m_oPlaybackBtn.setText("Playback");
					m_iPlaybackID = -1;
				}
			} catch (Exception err) {
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};

	// login listener
	private Button.OnClickListener Login_Listener = new Button.OnClickListener() {
		public void onClick(View v) {
			try {
				if (m_iLogID < 0) {
					// login on the device
					if (m_iLogID < 0) {
						Log.e(TAG, "This device logins failed!");
						return;
					}
					// get instance of exception callback and set
					ExceptionCallBack oexceptionCbf = getExceptiongCbf();
					if (oexceptionCbf == null) {
						Log.e(TAG, "ExceptionCallBack object is failed!");
						return;
					}

					if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(
							oexceptionCbf)) {
						Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
						return;
					}

					m_oLoginBtn.setText("Logout");
					Log.i(TAG,
							"Login sucess ****************************1***************************");
				} else {
					// whether we have logout
					if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID)) {
						Log.e(TAG, " NET_DVR_Logout is failed!");
						return;
					}
					m_oLoginBtn.setText("Login");
					m_iLogID = -1;
				}
			} catch (Exception err) {
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};

	// Preview listener
	private Button.OnClickListener Preview_Listener = new Button.OnClickListener() {
		public void onClick(View v) {
			try {
				if (m_iLogID < 0) {
					Log.e(TAG, "please login on device first");
					return;
				}
				if (m_iPlayID < 0) {
					if (m_iPlaybackID >= 0) {
						Log.i(TAG, "Please stop palyback first");
						return;
					}
					RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
					if (fRealDataCallBack == null) {
						Log.e(TAG, "fRealDataCallBack object is failed!");
						return;
					}

					NET_DVR_IPPARACFG_V40 struIPPara = new NET_DVR_IPPARACFG_V40();
					HCNetSDK.getInstance().NET_DVR_GetDVRConfig(m_iLogID,
							HCNetSDK.NET_DVR_GET_IPPARACFG_V40, 0, struIPPara);

					if (struIPPara.dwAChanNum > 0) {
						iFirstChannelNo = 1;
					} else {
						iFirstChannelNo = struIPPara.dwStartDChan;
					}

					if (iFirstChannelNo <= 0) {
						iFirstChannelNo = 1;
					}

					Log.i(TAG, "iFirstChannelNo:" + iFirstChannelNo);

					NET_DVR_CLIENTINFO ClientInfo = new NET_DVR_CLIENTINFO();
					ClientInfo.lChannel = iFirstChannelNo; // start channel no +
															// preview channel
					ClientInfo.lLinkMode = 1 << 31; // bit 31 -- 0,main
													// stream;1,sub stream
													// bit 0~30 -- link
													// type,0-TCP;1-UDP;2-multicast;3-RTP
					ClientInfo.sMultiCastIP = null;

					// net sdk start preview
					m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V30(
							m_iLogID, ClientInfo, fRealDataCallBack, true);
					if (m_iPlayID < 0) {
						Log.e(TAG, "NET_DVR_RealPlay is failed!Err:"
								+ HCNetSDK.getInstance().NET_DVR_GetLastError());
						return;
					}

					Log.i(TAG,
							"NetSdk Play sucess ***********************3***************************");

					m_oPreviewBtn.setText("Stop");
				} else {
					stopPlay();
					m_oPreviewBtn.setText("Preview");
				}
			} catch (Exception err) {
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};

	// configuration listener
	private Button.OnClickListener ParamCfg_Listener = new Button.OnClickListener() {
		public void onClick(View v) {
			try {
				paramCfg(m_iLogID);
			} catch (Exception err) {
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};

	/**
	 * @fn stopPlay
	 * @author zhuzhenlei
	 * @brief stop preview
	 * @param NULL
	 *            [in]
	 * @param NULL
	 *            [out]
	 * @return NULL
	 */
	private void stopPlay() {
		if (m_iPlayID < 0) {
			Log.e(TAG, "m_iPlayID < 0");
			return;
		}

		// net sdk stop preview
		if (!HCNetSDK.getInstance().NET_DVR_StopRealPlay(m_iPlayID)) {
			Log.e(TAG, "StopRealPlay is failed!Err:"
					+ HCNetSDK.getInstance().NET_DVR_GetLastError());
			return;
		}

		// player stop play
		if (!m_oPlayerSDK.stop(m_iPort)) {
			Log.e(TAG, "stop is failed!");
			return;
		}

		if (!m_oPlayerSDK.closeStream(m_iPort)) {
			Log.e(TAG, "closeStream is failed!");
			return;
		}
		if (!m_oPlayerSDK.freePort(m_iPort)) {
			Log.e(TAG, "freePort is failed!" + m_iPort);
			return;
		}
		m_iPort = -1;
		// set id invalid
		m_iPlayID = -1;
	}

	/**
	 * @fn loginDevice
	 * @author zhuzhenlei
	 * @brief login on device
	 * @param NULL
	 *            [in]
	 * @param NULL
	 *            [out]
	 * @return login ID
	 */
	private int loginDevice() {
		// get instance
		m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
		if (null == m_oNetDvrDeviceInfoV30) {
			Log.e(TAG, "HKNetDvrDeviceInfoV30 new is failed!");
			return -1;
		}
		String sGetIP = m_oIPAddr;
		int dwPort = Integer.parseInt(m_oPort);
		String strUser = m_oUser;
		String strPsd = m_oPsd;
		// call NET_DVR_Login_v30 to login on, port 8000 as default
		if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
			m_oNetDvrResolveDeviceInfo = new NET_DVR_RESOLVE_DEVICEINFO();
			boolean isGetDeviceInfo = HCNetSDK.getInstance()
					.NET_DVR_GetDVRIPByResolveSvr_EX(sGetIP, (short) dwPort,
							m_DVRName, (short) m_DVRName.length(),
							m_DVRSerialNumber,
							(short) m_DVRSerialNumber.length(),
							m_oNetDvrResolveDeviceInfo);
			if (isGetDeviceInfo) {
				Log.i("m_oNetDvrResolveDeviceInfo", "设备信息读取成功！");
			} else {
				Log.i("m_oNetDvrResolveDeviceInfo", "设备信息读取失败！");
			}
			sGetIP = new String(m_oNetDvrResolveDeviceInfo.sGetIP).trim();
			dwPort = m_oNetDvrResolveDeviceInfo.dwPort;
		}
		Log.i("m_oNetDvrResolveDeviceInfo", "sGetIP = " + sGetIP);
		Log.i("m_oNetDvrResolveDeviceInfo", "dwPort = " + dwPort);
		int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(sGetIP, dwPort,
				strUser, strPsd, m_oNetDvrDeviceInfoV30);
		
		NET_DVR_WORKSTATE_V30 m_oNetDvrWorkStateV30 = new NET_DVR_WORKSTATE_V30();
		if(HCNetSDK.getInstance().NET_DVR_GetDVRWorkState_V30(iLogID, m_oNetDvrWorkStateV30)){
			NET_DVR_CHANNELSTATE_V30[] m_oNetDvrChannelStateV30 = m_oNetDvrWorkStateV30.struChanStatic;
			if(m_oNetDvrChannelStateV30[0].dwLinkNum >= 6){
				Toast.makeText(this, "摄像头最多支持6人同时访问,请稍候再试!", Toast.LENGTH_SHORT).show();
			}
		}
		
		if (iLogID < 0) {
			Toast.makeText(this, "摄像头访问失败,请重新访问!", Toast.LENGTH_SHORT).show();
			Log.e(TAG, "NET_DVR_Login is failed!Err:"
					+ HCNetSDK.getInstance().NET_DVR_GetLastError());
			return -1;
		}

		Log.i(TAG, "NET_DVR_Login is Successful!");

		return iLogID;
	}

	/**
	 * @fn paramCfg
	 * @author zhuzhenlei
	 * @brief configuration
	 * @param iUserID
	 *            - login ID [in]
	 * @param NULL
	 *            [out]
	 * @return NULL
	 */
	private void paramCfg(final int iUserID) {
		// whether have logined on
		if (iUserID < 0) {
			Log.e(TAG, "iUserID < 0");
			return;
		}

		NET_DVR_COMPRESSIONCFG_V30 struCompress = new NET_DVR_COMPRESSIONCFG_V30();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,
				HCNetSDK.NET_DVR_GET_COMPRESSCFG_V30, iFirstChannelNo,
				struCompress)) {
			Log.e(TAG, "NET_DVR_GET_COMPRESSCFG_V30 failed with error code:"
					+ HCNetSDK.getInstance().NET_DVR_GetLastError());
		} else {
			Log.i(TAG, "NET_DVR_GET_COMPRESSCFG_V30 succ");
		}
		// set substream resolution to cif
		struCompress.struNetPara.byResolution = 1;
		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID,
				HCNetSDK.NET_DVR_SET_COMPRESSCFG_V30, iFirstChannelNo,
				struCompress)) {
			Log.e(TAG, "NET_DVR_SET_COMPRESSCFG_V30 failed with error code:"
					+ HCNetSDK.getInstance().NET_DVR_GetLastError());
		} else {
			Log.i(TAG, "NET_DVR_SET_COMPRESSCFG_V30 succ");
		}
	}

	/**
	 * @fn getExceptiongCbf
	 * @author zhuzhenlei
	 * @brief process exception
	 * @param NULL
	 *            [in]
	 * @param NULL
	 *            [out]
	 * @return exception instance
	 */
	private ExceptionCallBack getExceptiongCbf() {
		ExceptionCallBack oExceptionCbf = new ExceptionCallBack() {
			public void fExceptionCallBack(int iType, int iUserID, int iHandle) {
				;// you can add process here
			}
		};
		return oExceptionCbf;
	}

	/**
	 * @fn getRealPlayerCbf
	 * @author zhuzhenlei
	 * @brief get realplay callback instance
	 * @param NULL
	 *            [in]
	 * @param NULL
	 *            [out]
	 * @return callback instance
	 */
	private RealPlayCallBack getRealPlayerCbf() {
		RealPlayCallBack cbf = new RealPlayCallBack() {
			public void fRealDataCallBack(int iRealHandle, int iDataType,
					byte[] pDataBuffer, int iDataSize) {
				// player channel 1
				//Log.i("idatatype", iDataType + "");
				//Log.i("idatasize", iDataSize + "");
				HikVideoActivity.this.processRealData(1, iDataType,
						pDataBuffer, iDataSize, Player.STREAM_REALTIME);
			}
		};
		return cbf;
	}

	/**
	 * @fn getPlayerbackPlayerCbf
	 * @author zhuzhenlei
	 * @brief get Playback instance
	 * @param NULL
	 *            [in]
	 * @param NULL
	 *            [out]
	 * @return callback instance
	 */
	private PlaybackCallBack getPlayerbackPlayerCbf() {
		PlaybackCallBack cbf = new PlaybackCallBack() {
			@Override
			public void fPlayDataCallBack(int iPlaybackHandle, int iDataType,
					byte[] pDataBuffer, int iDataSize) {
				// player channel 1
				HikVideoActivity.this.processRealData(1, iDataType,
						pDataBuffer, iDataSize, Player.STREAM_FILE);
			}
		};
		return cbf;
	}

	/**
	 * @fn processRealData
	 * @author zhuzhenlei
	 * @brief process real data
	 * @param iPlayViewNo
	 *            - player channel [in]
	 * @param iDataType
	 *            - data type [in]
	 * @param pDataBuffer
	 *            - data buffer [in]
	 * @param iDataSize
	 *            - data size [in]
	 * @param iStreamMode
	 *            - stream mode [in]
	 * @param NULL
	 *            [out]
	 * @return NULL
	 */
	public void processRealData(int iPlayViewNo, int iDataType,
			byte[] pDataBuffer, int iDataSize, int iStreamMode) {
		// Log.i(TAG, "iPlayViewNo:" + iPlayViewNo + ",iDataType:" + iDataType +
		// ",iDataSize:" + iDataSize);
		if (playOrnot == false) {
			playOrnot = true ;
		} 
		//Log.i("processrealData", "m_iPort: " + m_iPort + "" + "iDataType: " + iDataType + "") ;
		if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
			/*if (m_iPort >= 0) {//------20150908
				return;
			}*/
			/*m_iPort = m_oPlayerSDK.getPort();
			if (m_iPort == -1) {
				Log.e(TAG,
						"getPort is failed with: "
								+ m_oPlayerSDK.getLastError(m_iPort));
				return;
			}*/
			Log.i(TAG, "getPort succ with: " + m_iPort);
			if (iDataSize > 0) {
				if (!m_oPlayerSDK.setStreamOpenMode(m_iPort, iStreamMode)) // set
																			// stream
																			// mode
				{
					Log.e(TAG, "setStreamOpenMode failed");
					return;
				}
				if (!m_oPlayerSDK.setSecretKey(m_iPort, 1,
						"ge_security_3477".getBytes(), 128)) {
					Log.e(TAG, "setSecretKey failed");
					return;
				}
				if (!m_oPlayerSDK.openStream(m_iPort, pDataBuffer, iDataSize,
						2 * 1024 * 1024)) // open stream
				{
					Log.e(TAG, "openStream failed");
					return;
				}
				if (!m_oPlayerSDK.play(m_iPort, m_osurfaceView.getHolder())) {
					Log.e(TAG, "play failed");
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Message msg = new Message();
							msg.what = 1;
							mHandler.sendMessage(msg);
						}
					}).start();
					return;
				}
			}
		} else {
			if (!m_oPlayerSDK.inputData(m_iPort, pDataBuffer, iDataSize)) {
				Log.e(TAG,
						"inputData failed with: "
								+ m_oPlayerSDK.getLastError(m_iPort));
			}
		}
	}

	/**
	 * @fn Cleanup
	 * @author zhuzhenlei
	 * @brief cleanup
	 * @param NULL
	 *            [in]
	 * @param NULL
	 *            [out]
	 * @return NULL
	 */
	public void Cleanup() {
		// release player resource

		m_oPlayerSDK.freePort(m_iPort);
		m_iPort = -1;

		// release net SDK resource
		HCNetSDK.getInstance().NET_DVR_Cleanup();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.menu_empty, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			stopPlay();
			Cleanup();
			finish();
			// android.os.Process.killProcess(android.os.Process.myPid());
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	public boolean onSupportNavigateUp() {
		// TODO Auto-generated method stub
		stopPlay();
   	  	Cleanup();
		finish();
		return super.onSupportNavigateUp();
	}
}
