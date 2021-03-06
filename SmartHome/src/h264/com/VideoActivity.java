package h264.com;

/***
 * 视频列表界面
 */
import java.io.DataInputStream;
import java.io.IOException;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdgl.activity.ConfigActivity;
import com.gdgl.activity.VideoFragment;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.manager.WarnManager;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.video.VideoNode;
import com.gdgl.smarthome.R;

public class VideoActivity extends FragmentActivity implements UIListener {
	private final static String TAG = "VideoActivity";
	public static int ipc_channel = -1;
	public Button captureImageBtn;
	Display display;
	VView decodeh264;
	int flag;
	public static int ret = 0;
	VideoNode mVideoNode;
	private boolean isVisible = false;
	TextView unreadMessageView;
	Button notifyButton;
	Button addButton;
	Button setButton;

	// DataInputStream dataInputStream;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate" + String.valueOf(ipc_channel));
		Bundle extras_ipc_channel = getIntent().getExtras();
		if (null != extras_ipc_channel) {
			mVideoNode = (VideoNode) extras_ipc_channel
					.getParcelable(VideoFragment.PASS_OBJECT);
		}
		if (null != mVideoNode) {
			ipc_channel = Integer.parseInt(mVideoNode.getId());
		}

		Resources res = getResources();
		Drawable backDrawable = res.getDrawable(R.drawable.new_bacg);
		this.getWindow().setBackgroundDrawable(backDrawable);

		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// getWindow().setBackgroundDrawableResource(R.drawable.new_bacg);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenWidth = size.x;
		int screenHeight = size.y;
		flag = screenHeight > screenWidth ? 1 : 0;
		addBackground();
		// 获取屏幕宽度和高度
		// display = getWindowManager().getDefaultDisplay();
		decodeh264 = new VView(this, screenWidth, screenHeight, flag);
		setContentView(decodeh264);// ���ò���
		addTitle();
		addRecordBtn();
		CallbackManager.getInstance().addObserver(this);
		new playVideoTask().execute(ipc_channel);
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy" + String.valueOf(ipc_channel));
		closeTheThread();
		super.onDestroy();
	}

	private void addBackground() {
		if (flag == 0) {
			Resources res = getResources();
			Drawable backDrawable = res.getDrawable(R.drawable.backgroundblack);
			this.getWindow().setBackgroundDrawable(backDrawable);
			this.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	private void addRecordBtn() {
		captureImageBtn = new Button(this);
		captureImageBtn.setId(0);
		if (flag == 0)
			captureImageBtn.setVisibility(View.GONE);
		FrameLayout.LayoutParams params = setPortrait();
		captureImageBtn.setText("截图");
		captureImageBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new captureImageTask().execute(0);
			}
		});
		addContentView(captureImageBtn, params);

	}

	private void addTitle() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View viewTitle = layoutInflater.inflate(R.layout.toptitle, null);
		TextView title = (TextView) viewTitle.findViewById(R.id.title);
		if (flag == 0)
			viewTitle.setVisibility(View.GONE);
		String name = "";
		name = mVideoNode.getAliases();
		title.setText(name);
		LinearLayout.LayoutParams params = setTitlePortrait();
		addContentView(viewTitle, params);
		addButton = (Button) findViewById(R.id.add);
		setButton = (Button) findViewById(R.id.set);
		addButton.setVisibility(View.GONE);
		setButton.setVisibility(View.GONE);
		notifyButton = (Button) findViewById(R.id.alarm_btn);
		unreadMessageView = (TextView) findViewById(R.id.unread_tv);
		notifyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(VideoActivity.this, ConfigActivity.class);
				i.putExtra("fragid", 1);
				startActivity(i);
				WarnManager.getInstance().intilMessageNum();
			}
		});
	}

	private FrameLayout.LayoutParams setPortrait() {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		params.bottomMargin = 0;
		// params.rightMargin = display.getWidth() / 2;
		params.gravity = Gravity.BOTTOM | Gravity.LEFT;
		return params;
	}

	private LinearLayout.LayoutParams setTitlePortrait() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin = 0;
		params.gravity = Gravity.TOP;
		return params;
	}
	
	//按手机返回键
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		closeTheThread();
		super.finish();
	}
	
	public void closeTheThread() {
		Log.i(TAG, "finish video ipc_channel=" + String.valueOf(ipc_channel));
		CallbackManager.getInstance().deleteObserver(this);
		decodeh264.setIsVideoRun(false);
		decodeh264.initalThread();
		isVisible = false;
		Network.closeVideoSocket();
	}
	
	class captureImageTask extends AsyncTask<Integer, Object, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... params) {
			// ComUtil.mkdirH264("/PictureShot");
			return decodeh264.screenShot(ipc_channel);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(getApplicationContext(), "截图成功",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "截图失败，请检查SD卡是否正常",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

//	class playVideoTask extends AsyncTask<Integer, Object, Integer> {
//
//		@Override
//		protected Integer doInBackground(Integer... params) {
//			boolean isconnect = Network.isVideoSocketConnect();		
//			if (isconnect == true) {
//				if(decodeh264.dataInputStream==null) {
//					decodeh264.dataInputStream=(DataInputStream)getLastCustomNonConfigurationInstance();
//				}
//				return 1;
//			} else {
//				// handleError();
//				Log.i(TAG, "start video ipc_channel=" + String.valueOf(ipc_channel));
//				decodeh264.initalThread();
//				Network.connectServer();
//				if (Network.isVideoSocketConnect() == true) {
//					Network.sendVideoReq(ipc_channel);
//					try {
//						decodeh264.dataInputStream = new DataInputStream(
//								Network.socket.getInputStream());
//					} catch (IOException e1) {
//						Log.e(TAG, "doInBackground:" + e1.getMessage());
//						// handleError();
//						return 3;
//					}
//				} else {
//					return 3;
//				}
//				if(decodeh264.getStartFlag() == true) {
//					return 2;
//				}
//				return 3;
//			}
//		}
//
//		@Override
//		protected void onPostExecute(Integer result) {
//			super.onPostExecute(result);
//			switch (result) {
//			case 2:
//				decodeh264.PlayVideo();
//				break;
//			case 3:
//				handleError();
//				break;
//			default:
//				break;
//			}
////			if (result) {
////				decodeh264.PlayVideo();
////			} else {
////				handleError();
////			}
//		}
//	}
	
	class playVideoTask extends AsyncTask<Integer, Object, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... params) {
			Log.i(TAG, "start video ipc_channel=" + String.valueOf(ipc_channel));
			decodeh264.initalThread();
			Network.connectServer();
			boolean isconnect = Network.isVideoSocketConnect();
			if (isconnect == true) {
				Network.sendVideoReq(ipc_channel);
				try {
					decodeh264.dataInputStream = new DataInputStream(
							Network.socket.getInputStream());
				} catch (IOException e1) {
					Log.e(TAG, "doInBackground:" + e1.getMessage());
					// handleError();
					return false;
				}
			} else {
				// handleError();
				return false;
			}
			return decodeh264.getStartFlag() == true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				decodeh264.PlayVideo();
			} else {
				handleError();
			}
		}

	}

	public void handleError() {
		decodeh264.initalThread();
		if (isVisible) {
			Toast.makeText(getApplicationContext(), "打开视频失败，请检查网络连接或重试",
					Toast.LENGTH_SHORT).show();
		}
		// finish();
	}

	public boolean isVideoVisble() {
		return isVisible;
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume" + String.valueOf(ipc_channel));
		isVisible = true;
		updateMessageNum();
		super.onResume();
	}
	@Override
	protected void onPause() {
		Log.i(TAG, "onPause" + String.valueOf(ipc_channel));
		isVisible = false;
		super.onPause();
	}

	private void updateMessageNum() {
		int messageNum = WarnManager.getInstance().getMessageNum();

		if (messageNum == 0) {
			unreadMessageView.setVisibility(View.GONE);
		} else {
			unreadMessageView.setVisibility(View.VISIBLE);
			unreadMessageView.setText(String.valueOf(messageNum));
		}
	}

	@Override
	public void update(Manger observer, Object object) {
		Event data = (Event) object;
		if (EventType.WARN == data.getType()) {
			notifyButton.post(new Runnable() {

				@Override
				public void run() {
					updateMessageNum();
				}
			});
		}

	}
//	@Override
//	public Object onRetainCustomNonConfigurationInstance() {
//		// TODO Auto-generated method stub
//		DataInputStream inputStream=decodeh264.dataInputStream;
//		return inputStream;
//	}
}
