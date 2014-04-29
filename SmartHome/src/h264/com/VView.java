package h264.com;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
import android.content.Context;
//import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.View;

public class VView extends View implements Runnable {
	Thread decodethread = null;
	public DataInputStream dataInputStream = null;
	int gdeviceHeight = 0;
	int gdeviceWith = 0;

	// int h264Width = 352; // 264 CIF�����ֱ��� ok
	// int h264Height = 288;
	int h264Width = 640; // 264 CIF�����ֱ���
	int h264Height = 480;

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
	// Bitmap VideoBit = Bitmap.createBitmap(h264Width, h264Height,
	// Config.RGB_565);
	Bitmap VideoBit = Bitmap
			.createBitmap(h264Width, h264Height, Config.RGB_565);

	int mTrans = 0x0F0F0F0F;

	public native int InitDecoder(int width, int height);

	public native int UninitDecoder();

	public native int DecoderNal(byte[] in, int insize, byte[] out);

	static {
		System.loadLibrary("H264Android");
	}

	public VView(Context context, int deviceWidth, int deviceheight) {
		super(context);
		// TODO Auto-generated constructor stub
		setFocusable(true);
		this.gdeviceHeight = deviceheight;
		this.gdeviceWith = deviceWidth;
		setPortrait();
		int i = 0;
		for (i = 0; i < mPixel.length; i++) {
			mPixel[i] = (byte) 0x00;
		}
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
		top = (this.gdeviceHeight - newHeight) / 2 - 50;
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
	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);

		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// ����
			setLandScape();
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// ����
			setPortrait();
		}
	}

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
		Bitmap newbm = Bitmap.createBitmap(VideoBit, 0, 0, h264Width,
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
		try {
			if (Network.socket != null) {
				Network.socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
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

		while (true) {
			try {
				// �������ж�ȡ264��ݻ���
				readNum = dataInputStream.read(buffFromSocket, 0, 8192);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("DecodeH264 run", "dataInputStream.read error!");
				// e.printStackTrace();
			}

			String recstr = new String(buffFromSocket);
			try {
				// ����ַ����JSON����
				JSONObject responseCommand = new JSONObject(recstr);
				// ��ȡ�����
				String str1 = responseCommand.getString("action");
				// System.out.println("responseCommand.getString");
				// System.out.println(str1);
				if (str1.equals("startvideo")) {
					System.out.println("str1.equals(startvideo)");
					try {
						// ��ȡ��ݶ���
						JSONObject user = responseCommand
								.getJSONObject("response_params");
						// String nickname = user.getString("status");
						int status = user.getInt("status");
						if (status == 0x01) {
							start_flag = true;
							ret = true;
							Log.d("DecodeH264 run",
									"recieved start flag ipc is online");
						} else if (status == 0x02) {
							start_flag = true;
							ret = false;
							Log.e("DecodeH264 run",
									"recieved start flag ipc is not online");
						}
						break;
					} catch (JSONException ex) {
						ret = false;
						Log.e("DecodeH264 run", "responseCommand.getJSONObject");
						break;
					}
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
		// TODO Auto-generated method stub
		// ��˯��3��ȴ��л�ʱ�ڴ���ա�
		Log.d("DecodeH264 run", "have recieved data thread");
		try {
			Thread.currentThread().sleep(1500);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		InputStream is = null;
		FileInputStream fileIS = null;

		int iTemp = 0;
		int nalLen;

		boolean bFirst = true;
		boolean bFindPPS = true;

		int bytesReadFromSocketNum = 0;
		int NalBufUsed = 0;
		int SockBufUsed = 0;

		// byte[] decoderInBuf = new byte[40980]; // 40k //ok
		// byte[] buffFromSocket = new byte[2048];
		byte[] decoderInBuf = new byte[81920]; // 80k
		// byte[] decoderInBuf = new byte[1024*160]; // 40k
		// byte[] buffFromSocket = new byte[8192];
		byte[] buffFromSocket = new byte[2048];

		InitDecoder(h264Width, h264Height);

		while (!Thread.currentThread().isInterrupted()) {
			try {
				// �������ж�ȡ264��ݻ���
				bytesReadFromSocketNum = dataInputStream.read(buffFromSocket,
						0, 2048);
				// bytesReadFromSocketNum = dataInputStream.read(buffFromSocket,
				// 0, 8192);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("DecodeH264 run", "dataInputStream.read error!");
				// e.printStackTrace();
			}

			if (bytesReadFromSocketNum <= 0) {
				Log.d("DecodeH264 run", "have not recieved data!");
				break;
			}

			Log.d("DecodeH264 run", "have recieved data = "
					+ bytesReadFromSocketNum);

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
						// ����һ��NAL��Ԫ org
						// Log.i("DecodeH264 run", "000**DecoderNal**000");
						iTemp = DecoderNal(decoderInBuf, NalBufUsed - 4, mPixel);
						// Log.i("DecodeH264 run", "****DecoderNal******");
						// ʹ��postInvalidate����ֱ�����߳��и��½���
						// postInvalidate ֪ͨonDraw(Canvas canvas)����view����
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
		try {
			if (fileIS != null)
				fileIS.close();

			if (is != null)
				is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		UninitDecoder();
	}
}
