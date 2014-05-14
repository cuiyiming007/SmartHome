package h264.com;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gdgl.util.ComUtil;

//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
import android.content.Context;
//import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class VView extends View implements Runnable {

	private final static String TAG = "VView";
	public boolean isVideoRun = false;

	Context context;
	VideoActivity videoActivity;
	Thread decodethread = null;
	public DataInputStream dataInputStream = null;
	int gdeviceHeight = 0;
	int gdeviceWith = 0;

	int h264Width = 352; // 264 CIF�����ֱ��� ok
	int h264Height = 288;
	// int h264Width = 640; // 264 CIF�����ֱ���
	// int h264Height = 480;

	// ������Ҫ�Ĵ�С
	int newWidth = 0;
	int newHeight = 0;
	// �������ű���
	float scaleWidth = 0;
	float scaleHeight = 0;

	// ͼ���ڽ����е���ƫ����
	int top = 100;
	// ͼ���ڽ����е���ƫ����
	int left = 0;

	byte[] mPixel = new byte[h264Width * h264Height * 2];
	ByteBuffer buffer = ByteBuffer.wrap(mPixel);
	Handler handler;
	// Bitmap VideoBit = Bitmap.createBitmap(h264Width, h264Height,
	// Config.RGB_565);
	Bitmap VideoBit = Bitmap
			.createBitmap(h264Width, h264Height, Config.RGB_565);
	Bitmap newbm;
	int mTrans = 0x0F0F0F0F;

	public native int InitDecoder(int width, int height);

	public native int UninitDecoder();

	public native int DecoderNal(byte[] in, int insize, byte[] out);

	static {
		System.loadLibrary("H264Android");
	}

	public VView(final VideoActivity context, int deviceWidth, int deviceheight) {
		super(context);
		this.context = context;
		videoActivity=context;
		// TODO Auto-generated constructor stub
		setFocusable(true);
		this.gdeviceHeight = deviceheight;
		this.gdeviceWith = deviceWidth;
		setPortrait();
		int i = 0;
		for (i = 0; i < mPixel.length; i++) {
			mPixel[i] = (byte) 0x00;
		}
		 handler=new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				if (msg.what==0&&videoActivity.isVideoVisble()) {
					 Toast.makeText(context,
					 "解析视频失败，请重试",Toast.LENGTH_SHORT).show();
				}
				super.handleMessage(msg);
			}
		};
	}

	// �������ű���
	private void setScale() {
		scaleWidth = ((float) newWidth) / h264Width;
		scaleHeight = ((float) newHeight) / h264Height;
	}

	// ����
	private void setPortrait() {
		this.newWidth = this.gdeviceWith;// ���ſ�ȵ����豸���
		// ���ź�ĸ�=ԭͼ���x���ű���
		this.newHeight = (h264Height * newWidth / h264Width) + 100;
		// top = (this.gdeviceHeight - newHeight) / 2 - 50;
		top = 65;
		left = 0;
		setScale();
	}

	// �M��
	private void setLandScape() {
		this.newHeight = this.gdeviceWith - 50;// �����������Ĵ�Ÿ߶�
		this.newWidth = h264Width * this.newHeight / h264Height + 100;
		top = 0;
		left = (this.gdeviceHeight - newWidth) / 2;
		setScale();
	}

	// �Զ���ת������
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.View#onConfigurationChanged(android.content.res.Configuration
	 * )
	 */
	// @Override
	// protected void onConfigurationChanged(Configuration newConfig) {
	// // TODO Auto-generated method stub
	// super.onConfigurationChanged(newConfig);
	//
	// if (this.getResources().getConfiguration().orientation ==
	// Configuration.ORIENTATION_LANDSCAPE) {
	// // ����
	// setLandScape();
	// } else if (this.getResources().getConfiguration().orientation ==
	// Configuration.ORIENTATION_PORTRAIT) {
	// // ����
	// setPortrait();
	// }
	// }

	// ��view��onDraw�����������ʾ���
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		// buffer�� mPixel����
		buffer.rewind();
		VideoBit.copyPixelsFromBuffer(buffer);

		// ȡ����Ҫ���ŵ�matrix����
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// �õ��µ�ͼƬ
		 newbm = Bitmap.createBitmap(VideoBit, 0, 0, h264Width,
				h264Height, matrix, true);
		canvas.drawBitmap(newbm, left, top, null);
		// canvas.drawBitmap(VideoBit, 50, 100, null);

	}

	// ��ʼ���߳�
	public void initalThread() {
		if (decodethread != null) {
			decodethread.interrupt();
			decodethread = null;
		}
	}

	public boolean getStartFlag() {
		int num;
		boolean ret, start_flag;
		int readNum = 0;
		byte[] buffFromSocket = new byte[8192];

		num = 0;
		ret = false;
		start_flag = false;

		while (dataInputStream != null) {
			try {
				// �������ж�ȡ264��ݻ���
				readNum = dataInputStream.read(buffFromSocket, 0, 8192);
			} catch (IOException e) {
				handleError();
			}

			String recstr = new String(buffFromSocket);
			try {
				// ����ַ����JSON����
				JSONObject responseCommand = new JSONObject(recstr);
				// ��ȡ�����
				int status = responseCommand.getInt("status");
				if (status == 0x01) {
					start_flag = true;
					ret = true;
					Log.d("DecodeH264 run", "recieved start flag ipc is online");
				} else {
					Log.e("getStartFlag()", "video failed");
				}
			} catch (JSONException ex) {
				ret = false;
				Log.d("DecodeH264 run", "JSONObject error");
				break;
			}

			if (num > 20) {
				ret = false;
				Log.d("DecodeH264 run", "recieved start flag");
				break;
			} else {
				if (start_flag == true) {
					break;
				} else {
					num = num + 1;
				}
			}

		}// while(true)
		return ret;
	}

	/*
	 * ////////////////org/////////////////// //public void PlayVideo(int
	 * channelNum) //ok public int PlayVideo(final int channelNum) {
	 * initalThread();
	 * 
	 * // PathFileName = file; // NetUtil.sendUdpMsg(null); new Thread(new
	 * Runnable() {
	 * 
	 * @Override public void run() { boolean isconnect; isconnect =
	 * Network.connectServer(Network.IPserver, Network.tcpPort);
	 * 
	 * if(isconnect == true)//�����Ϸ����� { Network.sendVideoReq(channelNum);
	 * try { //����dataInputStream dataInputStream = new DataInputStream(
	 * Network.socket.getInputStream()); } catch (IOException e1) {
	 * e1.printStackTrace(); }
	 * 
	 * if(getStartFlag()==true)//�յ���ʼ��־�����߳� { decodethread = new
	 * Thread(this); decodethread.start(); // return 1; } else
	 * //�յ��������Ĵ�����룬���߳�ʱû���յ���������ʼ��־,IPC������ { // return 2; }
	 * 
	 * } else//û�������Ϸ����� { // return 3; } } }).start(); return 0;
	 * 
	 * } ///////////////org///////////////////
	 */
	// ////////////////jiang test///////////////////////

	// public void PlayVideo(int channelNum) //ok
	public void PlayVideo() {
		decodethread = new Thread(this);
		decodethread.run();
	}

	// ////////////////jiang test///////////////////////

	// ��socket������ȡһ��NAL��Ƶ��Ԫ
	int MergeBuffer(byte[] outNalBuf, int NalBufUsed, byte[] SockBuf,
			int SockBufUsed, int SockRemainNum) {
		int i = 0;
		byte Temp;

		for (i = 0; i < SockRemainNum; i++) {
			Temp = SockBuf[i + SockBufUsed];

			outNalBuf[i + NalBufUsed] = Temp;

			mTrans <<= 8;
			mTrans |= Temp;// h264���������ԪNAL��ʼ��־λ0x00,0x00,0x00,0x01
							// NAL�����־Ҳ��0x00,0x00,0x00,0x01
			if (mTrans == 1) // �ҵ�һ��NAL��Ԫ
			{
				i++;
				break;
			}

		}
		return i;
	}

	@Override
	public void run() {
		try {
			Thread.currentThread().sleep(1500);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}

		setIsVideoRun(true);
		int iTemp = 0;
		int nalLen;

		boolean bFirst = true;
		boolean bFindPPS = true;

		int bytesReadFromSocketNum = 0;
		int NalBufUsed = 0;
		int SockBufUsed = 0;
		byte[] decoderInBuf = new byte[81920]; // 80k
		byte[] buffFromSocket = new byte[2048];

		InitDecoder(h264Width, h264Height);

		while (!Thread.currentThread().isInterrupted()&&getIsVideoRun()) {
			try {
				bytesReadFromSocketNum = dataInputStream.read(buffFromSocket,
						0, 2048);
			} catch (IOException e) {
				handleError();
				Log.e(TAG, "bytesReadFromSocketNum error" + e.getMessage());
			}

			if (bytesReadFromSocketNum <= 0) {
				Log.d("DecodeH264 run", "have not recieved data!");
				break;
			}

			SockBufUsed = 0;

			while (bytesReadFromSocketNum - SockBufUsed > 0) {
				nalLen = MergeBuffer(decoderInBuf, NalBufUsed, buffFromSocket,
						SockBufUsed, bytesReadFromSocketNum - SockBufUsed);

				NalBufUsed += nalLen;
				SockBufUsed += nalLen;

				while (mTrans == 1) {
					mTrans = 0xFFFFFFFF;

					if (bFirst == true) // the first start flag
					{
						bFirst = false;
					} else // a complete NAL data, include 0x00000001 trail.
					{
						if (bFindPPS == true) // true
						{
							if ((decoderInBuf[4] & 0x1F) == 7) {
								bFindPPS = false;
							} else {
								decoderInBuf[0] = 0;
								decoderInBuf[1] = 0;
								decoderInBuf[2] = 0;
								decoderInBuf[3] = 1;

								NalBufUsed = 4;

								break;
							}
						}
						iTemp = DecoderNal(decoderInBuf, NalBufUsed - 4, mPixel);
						if (iTemp > 0)
							postInvalidate();

					}

					decoderInBuf[0] = 0;
					decoderInBuf[1] = 0;
					decoderInBuf[2] = 0;
					decoderInBuf[3] = 1;

					NalBufUsed = 4;
				}
			}
		}
		UninitDecoder();
	}

	public void handleError() {
		setIsVideoRun(false);
		handler.sendEmptyMessage(0);
		initalThread();
		// Toast.makeText(context,
		// "打开视频失败，请检查网络连接或重试",Toast.LENGTH_SHORT).show();
	}

	public void setIsVideoRun(boolean isVideoRun) {
		this.isVideoRun = isVideoRun;
	}

	public boolean getIsVideoRun() {
		return isVideoRun;
	}
	
	public Boolean screenShot(int channel) 
	{
		String picName=ComUtil.setViedoFileNameBySysTime(channel);
		ComUtil.mkdirH264();
		ComUtil.creatVideoFile(picName);
		return ComUtil.savePic(newbm, ComUtil.picturePath+"/"+picName);
	}
}
