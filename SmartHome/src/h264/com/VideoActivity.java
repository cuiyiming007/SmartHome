package h264.com;

import java.io.DataInputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gdgl.smarthome.R;
import com.gdgl.util.ComUtil;

public class VideoActivity extends FragmentActivity {
	private final static String TAG = "VideoActivity";
	public static int ipc_channel = -1;
	public Button captureImageBtn;
	Display display;
	VView decodeh264;
	public static int ret = 0;

	private boolean isVisible=false;
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
		Bundle extras_ipc_channel = getIntent().getExtras();
		ipc_channel = extras_ipc_channel.getInt("ipc_channel");

		Resources res = getResources();
		Drawable backDrawable = res.getDrawable(R.drawable.background);
		this.getWindow().setBackgroundDrawable(backDrawable);

		// getWindow().setBackgroundDrawableResource(R.drawable.new_bacg);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenWidth = size.x;
		int screenHeight = size.y;

		// 获取屏幕宽度和高度
		// display = getWindowManager().getDefaultDisplay();
		decodeh264 = new VView(this, screenWidth, screenHeight);
		setContentView(decodeh264);// ���ò���
		addTitle();
		addRecordBtn();
		new playVideoTask().execute(ipc_channel);
	}

	private void addRecordBtn() {
		captureImageBtn = new Button(this);
		captureImageBtn.setId(0);
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
		FrameLayout.LayoutParams params = setTitlePortrait();
		addContentView(viewTitle, params);
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

	private FrameLayout.LayoutParams setTitlePortrait() {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin = 0;
		params.gravity = Gravity.TOP;
		return params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Log.i(TAG, "finish video ipc_channel=" + String.valueOf(ipc_channel));
		decodeh264.setIsVideoRun(false);
		decodeh264.initalThread();
		Network.closeVideoSocket();
		super.finish();
	}

	// ��ʾ�������ӷ�����
	public void showMessage(int type) {
		Builder dl = new AlertDialog.Builder(VideoActivity.this);
		dl.setTitle(R.string.sure);
		if (type == 2) {
			dl.setMessage(R.string.ipc_offline);
		} else if (type == 3) {
			dl.setMessage(R.string.faile);
		}

		dl.setPositiveButton(R.string.back,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();// ȷ����ȥ��ǰ����
					}
				});
		dl.setNegativeButton(R.string.close,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						VideoActivity.this.finish();// ȡ�����˳�����
					}
				});
		dl.show();
	}

	class captureImageTask extends AsyncTask<Integer, Object, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... params) {
//			ComUtil.mkdirH264("/PictureShot");
			return decodeh264.screenShot(ipc_channel);
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(getApplicationContext(), "截图成功",
						Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(getApplicationContext(), "截图失败，请检查SD卡是否正常",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	class playVideoTask extends AsyncTask<Integer, Object, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... params) {
			Log.i(TAG, "start video ipc_channel=" + String.valueOf(ipc_channel));
			decodeh264.initalThread();
			boolean isconnect = Network.connectServer(Network.IPserver,
					Network.tcpPort);
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
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						decodeh264.PlayVideo();
					}
				}).start();
			} else {
				handleError();
			}
		}

	}

	public void handleError() {
		decodeh264.initalThread();
		Toast.makeText(getApplicationContext(), "打开视频失败，请检查网络连接或重试",
				Toast.LENGTH_SHORT).show();
		// finish();
	}
	public boolean isVideoVisble()
	{
		return isVisible;
	}
	@Override
	protected void onResume() {
		isVisible=true;
		super.onResume();
	}
	@Override
	protected void onPause() {
		isVisible=false;
		super.onPause();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	/*
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent data) { // TODO Auto-generated method stub if
	 * (resultCode == RESULT_OK) { Uri uri = data.getData(); String
	 * filPath=uri.getPath(); try { FileInputStream fileIS = new
	 * FileInputStream(filPath); DataInputStream inputStream=new
	 * DataInputStream(fileIS); vv.playLocalVideo(inputStream);
	 * 
	 * } catch (IOException e) { Log.e(TAG,
	 * "onActivityResult IOException"+e.getMessage()); return; } }
	 * super.onActivityResult(requestCode, resultCode, data); }
	 */

	/*
	 * private FrameLayout.LayoutParams setPortrait() { FrameLayout.LayoutParams
	 * params = new FrameLayout.LayoutParams(
	 * FrameLayout.LayoutParams.MATCH_PARENT,
	 * FrameLayout.LayoutParams.WRAP_CONTENT); // ���ù����ֵ�λ��(���ڶ���)
	 * params.bottomMargin = 0; //params.rightMargin = display.getWidth() / 2;
	 * params.gravity = Gravity.TOP | Gravity.LEFT; return params; }
	 * 
	 * @Override public void onConfigurationChanged(Configuration newConfig) {
	 * // TODO Auto-generated method stub
	 * 
	 * if (this.getResources().getConfiguration().orientation ==
	 * Configuration.ORIENTATION_LANDSCAPE) { FrameLayout.LayoutParams params =
	 * new FrameLayout.LayoutParams(70, FrameLayout.LayoutParams.MATCH_PARENT);
	 * // ���ù����ֵ�λ��(���ڵײ�) params.bottomMargin = 0;
	 * 
	 * } else if (this.getResources().getConfiguration().orientation ==
	 * Configuration.ORIENTATION_PORTRAIT) { FrameLayout.LayoutParams params =
	 * setPortrait(); }
	 * 
	 * super.onConfigurationChanged(newConfig); }
	 */
}
