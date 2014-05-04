package h264.com;

import java.io.DataInputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;

import com.gdgl.smarthome.R;

public class VideoActivity extends Activity {
	public static int ipc_channel = -1;;
	Display display;
	VView decodeh264;
	public static int ret = 0;

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
		System.out.println("ipc-channel =" + ipc_channel);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		float density = dm.density;

		// 获取屏幕宽度和高度
		// display = getWindowManager().getDefaultDisplay();
		decodeh264 = new VView(this, 900, 1500);
		setContentView(decodeh264);// ���ò���

		new playVideoTask().execute(ipc_channel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		decodeh264.initalThread();
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

	class playVideoTask extends AsyncTask<Integer, Object, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... params) {

			decodeh264.initalThread();

			int isreceive = 0;
			boolean isconnect;
			if (isreceive == 0) {
				isconnect = Network.connectServer(Network.IPserver,
						Network.tcpPort);
				if (isconnect == true) {
					Network.sendVideoReq(ipc_channel);
					try {
						decodeh264.dataInputStream = new DataInputStream(
								Network.socket.getInputStream());
					} catch (IOException e1) {
						decodeh264.initalThread();
						e1.printStackTrace();
						return false;
					}
				}
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
			}

		}

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
