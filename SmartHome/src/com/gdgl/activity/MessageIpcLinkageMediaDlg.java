package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.R.integer;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.mydata.Callback.CallbackIpcLinkageMessage;
import com.gdgl.smarthome.R;

public class MessageIpcLinkageMediaDlg {

	DownloadManager manager ;  
    DownloadCompleteReceiver receiver;
	
	public static ViewPager mViewPager;
	private CustomPagerAdapter mCustomPagerAdapter;
	public static Dialog dialog;
	CallbackIpcLinkageMessage ipcLinkageMessage;
	Bitmap bitMap;
	int mwidth, mheight;
	PhotoViewAttacher attacher;
	private List<String> urlList = new ArrayList<String>();
	private String httpUrlHead = "http://121.199.21.14:8888/ipc_capture/";

	// public ArrayList<Bitmap> screenshots = HikVideoActivity.screenPictures;

	public MessageIpcLinkageMediaDlg(Context c,
			CallbackIpcLinkageMessage ipcMessage, int dialogwidth,
			int dialogheight) {
		dialog = new Dialog(c, R.style.MyDialog);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.big_screenshot_dlg);
		dialog.setCanceledOnTouchOutside(false);

		ipcLinkageMessage = ipcMessage;

		Window dialogWindow = dialog.getWindow();
//		dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		mwidth = dialogwidth;
		mheight = dialogheight;
		lp.width = dialogwidth;
		lp.height = dialogheight;
		// lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		// lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);

		getFromSharedPreferences.setsharedPreferences(c);
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
		} else {
			urlList.add(httpUrlHead + mac + "/"
					+ ipcLinkageMessage.getPicName());
			
			manager =(DownloadManager)c.getSystemService(Context.DOWNLOAD_SERVICE);  
	        receiver = new DownloadCompleteReceiver();
	        //创建下载请求  
            DownloadManager.Request down = new DownloadManager.Request (Uri.parse(urlList.get(0)));  
            //设置允许使用的网络类型，这里是移动网络和wifi都可以  
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);  
            //禁止发出通知，既后台下载  
            down.setNotificationVisibility(Request.VISIBILITY_HIDDEN);  
            //不显示下载界面  
            down.setVisibleInDownloadsUi(false);  
            //设置下载后文件存放的位置  
            down.setDestinationInExternalFilesDir(c, Environment.DIRECTORY_DOWNLOADS, ipcLinkageMessage.getPicName());  
            //将下载请求放入队列  
            manager.enqueue(down); 
            c.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		}
		mViewPager = (ViewPager) dialog.findViewById(R.id.pager);
		mCustomPagerAdapter = new CustomPagerAdapter(c);
		mViewPager.setAdapter(mCustomPagerAdapter);
		// mViewPager.setCurrentItem(1);

	}

	public void show() {
		dialog.show();
	}

	public void dismiss() {
		dialog.dismiss();
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

		public void setImage() {
			
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
//						.asBitmap().atMost()
//						.dontTransform()
						.fitCenter()
//						.override(mwidth, (int)(mwidth*0.82+1))
						.placeholder(R.drawable.ui2_image_loading_spinner1)
						.error(R.drawable.ui2_error404)
						.crossFade()
						.into(imageView);
			} else {
//				Glide.with(mContext)
//						.load(urlList.get(position))
//						.placeholder(R.drawable.ui2_image_loading_spinner1)
//						.error(R.drawable.ui2_error404)
//						.into(imageView);
				imageView.setImageResource(R.drawable.ui2_image_loading_spinner1);
			}
			// imageView.setImageBitmap(screenshots.get(position));
//			attacher = new PhotoViewAttacher(imageView);
			container.addView(itemView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
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
            if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){  
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);  
                Log.i(""," download complete! id : "+downId); 
                
            }  
        }
    }

}
