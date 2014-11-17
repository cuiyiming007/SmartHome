package com.gdgl.activity;
/***
 * 窗帘控制
 */
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.smarthome.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CurtainControlFragment extends Fragment implements UIListener{

    String mCurtainsId;
    double mCurtainState;

    View mView;
//    CircleProgressBar mCircleProgressBar;
    Button mOpen, mClose;
    TextView txtOpen, txtClose;
    RelativeLayout back;
    
    ImageView curtainImg;

    int mState = 0;
    int mProgress = 0;
    private static final int STOP = 0;
    private static final int OPENING = 1;
    private static final int CLOSING = 2;

    private int openThreadState = 0;
    private int closeThreadState = 0;

    private static final int CHANGE_PROGRESSBAR = 1;
    private static final int OPENFULLY = 2;
    private static final int CLOSEFULLY = 3;

	DevicesModel mDevices;
	
	CGIManager mLightManager;

	@SuppressLint("NewApi")
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Bundle extras = getArguments();
		if (null != extras) {
			mDevices = (DevicesModel) extras
					.getParcelable(Constants.PASS_OBJECT);
		}
		mProgress = 30;
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.curtains_control_fragment, null);
		initView();
		return mView;
	}

    private void initView() {
        // TODO Auto-generated method stub
    	mLightManager=CGIManager.getInstance();
    	mLightManager.addObserver(CurtainControlFragment.this);
        mOpen = (Button) mView.findViewById(R.id.btn_open);
        mClose = (Button) mView.findViewById(R.id.btn_close);
        txtOpen = (TextView) mView.findViewById(R.id.txt_open);
        txtClose = (TextView) mView.findViewById(R.id.txt_close);

//        mCircleProgressBar = (CircleProgressBar) mView
//                .findViewById(R.id.circleProgressbar);
//
//        Log.i("zgs", "zz->mCircleProgressBar==null"
//                + (mCircleProgressBar == null));
//
//        mCircleProgressBar.setProgress(mProgress);
        
        curtainImg=(ImageView)mView.findViewById(R.id.curtain_img);
        
        setImg(mProgress);
        
        mOpen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	CGIManager.getInstance().shadeOperation(mDevices, operatortype.TurnOn,1);
                if (mProgress == 0) {
                    txtClose.setText("关闭窗帘");
                }

                if (STOP == mState) {
                    mState = OPENING;
                    if (1 == openThreadState) {
                        mOpenThread.onResume();
                    } else {
                        mOpenThread.start();
                        openThreadState = 1;
                    }
                    if (mProgress <= 100) {
                        txtOpen.setText("正在打开...");
                        v.setBackgroundResource(R.drawable.ongoing);
                    } else {
                        txtOpen.setText("已经完全打开");
                    }

                } else if (CLOSING == mState) {
                    mState = OPENING;
                    mCloseThread.onPause();
                    txtClose.setText("关闭窗帘");
                    if (1 == openThreadState) {
                        mOpenThread.onResume();
                    } else {
                        mOpenThread.start();
                        openThreadState = 1;
                    }
                    mClose.setBackgroundResource(R.drawable.close_off);
                    if (mProgress <= 100) {
                        txtOpen.setText("正在打开...");
                        v.setBackgroundResource(R.drawable.ongoing);
                    } else {
                        txtOpen.setText("已经完全打开");
                    }
                } else {
                    mState = STOP;
                    txtOpen.setText("打开窗帘");
                    mOpenThread.onPause();
                    v.setBackgroundResource(R.drawable.open_off);
                }
            }
        });

        mClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	CGIManager.getInstance().shadeOperation(mDevices, operatortype.TurnOff,1);
            	
                // TODO Auto-generated method stub
                if (mProgress == 100) {
                    txtOpen.setText("打开窗帘");
                }
                if (STOP == mState) {
                    mState = CLOSING;
                    if (1 == closeThreadState) {
                        mCloseThread.onResume();
                    } else {
                        mCloseThread.start();
                        closeThreadState = 1;
                    }
                    if (mProgress >= 0) {
                        txtClose.setText("正在关闭...");
                        v.setBackgroundResource(R.drawable.ongoing);
                    } else {
                        txtClose.setText("已经完全关闭");
                    }
                } else if (OPENING == mState) {
                    mState = CLOSING;
                    mOpenThread.onPause();
                    txtOpen.setText("打开窗帘");
                    if (1 == closeThreadState) {
                        mCloseThread.onResume();
                    } else {
                        mCloseThread.start();
                        closeThreadState = 1;
                    }
                    mOpen.setBackgroundResource(R.drawable.open_off);
                    if (mProgress >= 0) {
                        txtClose.setText("正在关闭...");
                        v.setBackgroundResource(R.drawable.ongoing);
                    } else {
                        txtClose.setText("已经完全关闭");
                    }
                } else {
                    mState = STOP;
                    txtClose.setText("关闭窗帘");
                    mCloseThread.onPause();
                    v.setBackgroundResource(R.drawable.close_off);
                }

            }
        });

    }

    private void setImg(int mProgress2) {
		// TODO Auto-generated method stub
    	if(mProgress>0){
        	curtainImg.setImageResource(R.drawable.curtains_on);
        }else{
        	curtainImg.setImageResource(R.drawable.curtans_off);
        }
	}

	CloseThread mCloseThread = new CloseThread();

    OpenThread mOpenThread = new OpenThread();

    class OpenThread extends Thread implements Runnable {
        private Object mPauseLock;
        private boolean mPauseFlag;

        public OpenThread() {
            mPauseLock = new Object();
            mPauseFlag = false;
        }

        public void onPause() {
            synchronized (mPauseLock) {
                mPauseFlag = true;
            }
        }

        public void onResume() {
            synchronized (mPauseLock) {
                mPauseFlag = false;
                mPauseLock.notifyAll();
            }
        }

        private void pauseThread() {
            synchronized (mPauseLock) {
                if (mPauseFlag) {
                    try {
                        mPauseLock.wait();
                    } catch (Exception e) {
                        Log.v("thread", "fails");
                    }
                }
            }
        }

        public void run() {
            while (mProgress <= 100) {
                pauseThread();
                inc();
                Message msg = Message.obtain();
                msg.what = CHANGE_PROGRESSBAR;
                msg.arg1 = mProgress;
                myHandler.sendMessage(msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
                if (100 == mProgress) {
                    this.onPause();
                    Message msg1 = Message.obtain();
                    msg1.what = OPENFULLY;
                    myHandler.sendMessage(msg1);
                }
            }
        }
    }

    class CloseThread extends Thread implements Runnable {
        private Object mPauseLock;
        private boolean mPauseFlag;

        public CloseThread() {
            mPauseLock = new Object();
            mPauseFlag = false;
        }

        public void onPause() {
            synchronized (mPauseLock) {
                mPauseFlag = true;
            }
        }

        public void onResume() {
            synchronized (mPauseLock) {
                mPauseFlag = false;
                mPauseLock.notifyAll();
            }
        }

        private void pauseThread() {
            synchronized (mPauseLock) {
                if (mPauseFlag) {
                    try {
                        mPauseLock.wait();
                    } catch (Exception e) {
                        Log.v("thread", "fails");
                    }
                }
            }
        }

        public void run() {
            while (mProgress >= 0) {
                pauseThread();
                dec();
                Message msg = Message.obtain();
                msg.what = CHANGE_PROGRESSBAR;
                msg.arg1 = mProgress;
                myHandler.sendMessage(msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
                if (0 == mProgress) {
                    this.onPause();
                    Message msg1 = Message.obtain();
                    msg1.what = CLOSEFULLY;
                    myHandler.sendMessage(msg1);
                }
            }

        }
    }

    private synchronized void inc() {
        if (mProgress < 100) {
            mProgress++;
        }
    }

    private synchronized void dec() {
        if (mProgress > 0) {
            mProgress--;
        }
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.i("zgs", "zz->handleMessage() msg.arg1=" + msg.arg1);
            switch (msg.what) {
            case CHANGE_PROGRESSBAR:
            	setImg(msg.arg1);
                break;
            case OPENFULLY:
                txtOpen.setText("已经完全打开");
                mOpen.setBackgroundResource(R.drawable.open_off);
                mState = STOP;
                break;
            case CLOSEFULLY:
                txtClose.setText("已经完全关闭");
                mState = STOP;
                mClose.setBackgroundResource(R.drawable.close_off);
                break;
            default:
                break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (null != mOpenThread) {
            mOpenThread.onPause();
            mOpenThread.interrupt();
        }
        if (null != mCloseThread) {
            mCloseThread.onPause();
            mCloseThread.interrupt();
        }
        mLightManager.deleteObserver(CurtainControlFragment.this);
        super.onDestroy();
    }
    public static class operatortype {
		
		public static final int TurnOn = 0;
		public static final int TurnOff = 1;
		
		public static final int Toggle = 2;
		public static final int GetLevel = 3;
		public static final int GetOnOffStatus = 4;
		public static final int MoveToLevel = 8;
		public static final int LevelStepUp = 9;
		public static final int LevelStepDown = 10;
		public static final int Move = 11;
		public static final int Stop=13;
			
		
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		
	}
}