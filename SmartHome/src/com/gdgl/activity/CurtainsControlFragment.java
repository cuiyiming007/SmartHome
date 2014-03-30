package com.gdgl.activity;

import com.gdgl.smarthome.R;
import com.gdgl.util.CircleProgressBar;

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

@SuppressLint("NewApi")
public class CurtainsControlFragment extends Fragment {

	int mCurtainsId;
	double mCurtainState;

	View mView;
	CircleProgressBar mCircleProgressBar;
	Button mOpen, mStop, mClose;

	public static final String CURTAIN_ID = "curtain_id";
	public static final String CURTAIN_STATE = "curtain_state";

	int mState = 0;
	int mProgress = 0;
	private static final int STOP = 0;
	private static final int OPENING = 1;
	private static final int CLOSING = 2;

	private int openThreadState = 0;
	private int closeThreadState = 0;

	private static final int CHANGE_PROGRESSBAR = 1;

	@SuppressLint("NewApi")
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		// mOpen = (Button) mView.findViewById(R.id.btn_open);
		// mStop = (Button) mView.findViewById(R.id.btn_stop);
		// mClose = (Button) mView.findViewById(R.id.btn_close);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Bundle extras = getArguments();
		if (null != extras) {
			mCurtainsId = extras.getInt(CURTAIN_ID, -1);
			mCurtainState = extras.getDouble(CURTAIN_STATE, 0.0);
		}
		mProgress = (int) (mCurtainState * 100);
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
		mOpen = (Button) mView.findViewById(R.id.btn_open);
		mStop = (Button) mView.findViewById(R.id.btn_stop);
		mClose = (Button) mView.findViewById(R.id.btn_close);

		mCircleProgressBar = (CircleProgressBar) mView
				.findViewById(R.id.circleProgressbar);

		Log.i("zgs", "zz->mCircleProgressBar==null"
				+ (mCircleProgressBar == null));

		mCircleProgressBar.setProgress(0);

		mOpen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (STOP == mState) {
					mState = OPENING;
					if (1 == openThreadState) {
						mOpenThread.onResume();
					} else {
						mOpenThread.start();
						openThreadState = 1;
					}

				} else if (CLOSING == mState) {
					mState = OPENING;
					mCloseThread.onPause();
					if (1 == openThreadState) {
						mOpenThread.onResume();
					} else {
						mOpenThread.start();
						openThreadState = 1;
					}
				}
			}
		});

		mStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (OPENING == mState) {
					mOpenThread.onPause();
				} else if (CLOSING == mState) {
					mCloseThread.onPause();
				}
				mState = STOP;
			}
		});

		mClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (STOP == mState) {
					mState = CLOSING;
					if (1 == closeThreadState) {
						mCloseThread.onResume();
					} else {
						mCloseThread.start();
						closeThreadState = 1;
					}
				} else if (OPENING == mState) {
					mState = CLOSING;
					mOpenThread.onPause();
					if (1 == closeThreadState) {
						mCloseThread.onResume();
					} else {
						mCloseThread.start();
						closeThreadState = 1;
					}
				}

			}
		});
	}

	myThread mCloseThread = new myThread(2);

	myThread mOpenThread = new myThread(1);

	class myThread extends Thread implements Runnable {
		private Object mPauseLock;
		private boolean mPauseFlag;
		private int type;
		private int mThreadProgress;

		public myThread(int t) {
			mPauseLock = new Object();
			mPauseFlag = false;
			mThreadProgress = mProgress;
			type = t;
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

		public synchronized int getProcess(int i) {
			if (1 == i) {
				return ++mThreadProgress;
			} else
				return --mThreadProgress;
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
			while (mThreadProgress <= 100) {
				pauseThread();
				Message msg = Message.obtain();
				msg.what = CHANGE_PROGRESSBAR;
				msg.arg1 = mThreadProgress;
				myHandler.sendMessage(msg);
				mProgress = getProcess(type);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			Log.i("zgs", "zz->handleMessage() msg.arg1=" + msg.arg1);
			switch (msg.what) {
			case CHANGE_PROGRESSBAR:
				mCircleProgressBar.setProgress(msg.arg1);
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
		super.onDestroy();
	}

}