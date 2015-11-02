package com.gdgl.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.mydata.Callback.CallbackIpcLinkageMessage;
import com.gdgl.smarthome.R;

public class MessageIpcLinkageMediaActivity extends Activity {
	DownloadManager manager;
	DownloadCompleteReceiver receiver;
	long myDownloadReference;

	boolean fileExist = false;

	public static ViewPager mViewPager;
	private ImageView mImageView;
	private CustomPagerAdapter mCustomPagerAdapter;
	CallbackIpcLinkageMessage ipcLinkageMessage;
	Bitmap bitMap;
	int mwidth, mheight;
	PhotoViewAttacher attacher;
	private List<String> urlList = new ArrayList<String>();
	private String httpUrlHead = "http://121.199.21.14:8888/ipc_capture/";
	private String fileAddress = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ipc_linkage_media_fragment);
		Intent intent = getIntent();
		ipcLinkageMessage = (CallbackIpcLinkageMessage) intent
				.getSerializableExtra("CallbackIpcLinkageMessage");

		fileAddress = this.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
				.toString() + "/" + ipcLinkageMessage.getPicName();

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setBackgroundResource(android.R.color.black);
		mImageView = (ImageView) findViewById(R.id.imageView);
		mImageView.setBackgroundResource(android.R.color.black);

		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mwidth = wm.getDefaultDisplay().getWidth();
		mheight = wm.getDefaultDisplay().getHeight();
		// WindowManager.LayoutParams lp = getWindow().getAttributes();
		// mwidth = dialogwidth;
		// mheight = dialogheight;
		// lp.width = dialogwidth;
		// lp.height = dialogheight;
		// lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		// lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		// getWindow().setAttributes(lp);

		getFromSharedPreferences.setsharedPreferences(this);
		String mac = getFromSharedPreferences.getGatewayMAC();
		if (ipcLinkageMessage.getType() == 1) {
			for (int i = ipcLinkageMessage.getPicCount(); i > 0; i--) {
				String httpUrl = httpUrlHead
						+ mac
						+ "/"
						+ ipcLinkageMessage.getPicName().replace(
								ipcLinkageMessage.getPicCount() + ".", i + ".");
				Log.i("", httpUrl);
				urlList.add(0, httpUrl);
			}

			mCustomPagerAdapter = new CustomPagerAdapter(this);
			mViewPager.setAdapter(mCustomPagerAdapter);
		} else {
			mViewPager.setVisibility(View.GONE);
			mImageView.setVisibility(View.VISIBLE);
			fileExist = fileIsExists(fileAddress);
			if (fileExist) {
				Glide.with(this)
						.load(fileAddress)
						.placeholder(R.drawable.ui2_image_loading_spinner1)
						.error(R.drawable.ui2_error_video).into(mImageView);

				mImageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						Intent intent = new Intent(Intent.ACTION_VIEW);
//						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						intent.putExtra("oneshot", 0);
//				        intent.putExtra("configchange", 0);
				        Uri data = Uri.parse(fileAddress);
//				        Uri data = Uri.parse(Environment.getExternalStorageDirectory()+"/Movies/Glory_Of_Nature.mp4");
//				        intent.setDataAndType(data, "video/*");
//						Uri data = Uri.parse("http://www.baidu.com");
//				        intent.setData(data);
//				        if (intent.resolveActivity(getPackageManager()) != null) {
//				            startActivity(intent);
//				        } else {
//							Toast.makeText(MessageIpcLinkageMediaActivity.this, "cannot find player to handle video!",Toast.LENGTH_SHORT).show();
							Intent intent2 = new Intent();
							intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent2.setData(data);
							intent2.setClass(MessageIpcLinkageMediaActivity.this, MessageIpcLinkageVideoPlayer.class);
							startActivity(intent2);
							overridePendingTransition(android.R.anim.fade_in,
									android.R.anim.fade_out);
//						}
					}
				});
			} else {
				urlList.add(httpUrlHead + mac + "/"
						+ ipcLinkageMessage.getPicName());

				manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
				receiver = new DownloadCompleteReceiver();
				// 创建下载请求
				DownloadManager.Request down = new DownloadManager.Request(
						Uri.parse(urlList.get(0)));
				// 设置允许使用的网络类型，这里是移动网络和wifi都可以
				down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
						| DownloadManager.Request.NETWORK_WIFI);
				// 禁止发出通知，既后台下载
				down.setNotificationVisibility(Request.VISIBILITY_HIDDEN);
				// 不显示下载界面
				down.setVisibleInDownloadsUi(false);
				// 设置下载后文件存放的位置
				down.setDestinationInExternalFilesDir(this,
						Environment.DIRECTORY_MOVIES,
						ipcLinkageMessage.getPicName());
				// 将下载请求放入队列
				myDownloadReference = manager.enqueue(down);
				mImageView
						.setImageResource(R.drawable.ui2_image_loading_spinner1);
				registerReceiver(receiver, new IntentFilter(
						DownloadManager.ACTION_DOWNLOAD_COMPLETE));
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (ipcLinkageMessage.getType() == 2 && !fileExist) {
			unregisterReceiver(receiver);
		}
	}

	@Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    	if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { 
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); 
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        } else { 
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); 
        }
    }
	
	class CustomPagerAdapter extends PagerAdapter {

		Context mContext;
		LayoutInflater mLayoutInflater;

		public CustomPagerAdapter(Context context) {
			mContext = context;
			mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return urlList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((LinearLayout) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View itemView = mLayoutInflater.inflate(R.layout.pager_item,
					container, false);
			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.imageView);
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			Log.i("Glide", urlList.get(position));
			if (ipcLinkageMessage.getType() == 1) {
				Glide.with(mContext)
						.load(urlList.get(position))
						// .asBitmap().atMost()
						// .dontTransform()
						.fitCenter()
						// .override(mwidth, (int)(mwidth*0.82+1))
						.placeholder(R.drawable.ui2_image_loading_spinner1)
						.error(R.drawable.ui2_error404).crossFade()
						.into(imageView);
			} else {
				Glide.with(mContext).load(urlList.get(position))
						.placeholder(R.drawable.ui2_image_loading_spinner1)
						.error(R.drawable.ui2_error404).into(imageView);
			}
			// imageView.setImageBitmap(screenshots.get(position));
			// attacher = new PhotoViewAttacher(imageView);
			// container.addView(itemView, LayoutParams.MATCH_PARENT,
			// LayoutParams.MATCH_PARENT);
			container.addView(itemView);
			// shotview = imageView;
			return itemView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((LinearLayout) object);
		}
	}

	class DownloadCompleteReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
				long reference = intent.getLongExtra(
						DownloadManager.EXTRA_DOWNLOAD_ID, -1);
				if (myDownloadReference == reference) {
					Log.i("", " download complete! id : " + reference);
					Glide.with(context)
							.load(fileAddress)
							.placeholder(R.drawable.ui2_image_loading_spinner1)
							.error(R.drawable.ui2_error_video).into(mImageView);
					mImageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
//							Intent intent = new Intent(Intent.ACTION_VIEW);
//							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//							intent.putExtra("oneshot", 0);
//					        intent.putExtra("configchange", 0);
					        Uri data = Uri.parse(fileAddress);
//					        Uri data = Uri.parse(Environment.getExternalStorageDirectory()+"/Movies/Glory_Of_Nature.mp4");
//					        intent.setDataAndType(data, "video/*");
//							Uri data = Uri.parse("http://www.baidu.com");
//					        intent.setData(data);
//					        if (intent.resolveActivity(getPackageManager()) != null) {
//					        	startActivity(intent);
//					        } else {
//								Toast.makeText(MessageIpcLinkageMediaActivity.this, "cannot find player to handle video!",Toast.LENGTH_SHORT).show();
								Intent intent2 = new Intent();
								intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent2.setData(data);
								intent2.setClass(MessageIpcLinkageMediaActivity.this, MessageIpcLinkageVideoPlayer.class);
								startActivity(intent2);
								overridePendingTransition(android.R.anim.fade_in,
										android.R.anim.fade_out);
//							}
						}
					});
				}
			}
		}
	}

	public boolean fileIsExists(String strFile) {
		try {
			File f = new File(strFile);
			if (!f.exists()) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}

		return true;
	}
}
