package h264.com;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.gdgl.activity.HikVideoActivity;
import com.gdgl.smarthome.R;

public class BigScreenshotDialog  {
	 private ViewPager mViewPager;
	private CustomPagerAdapter mCustomPagerAdapter;
	Dialog dialog;
	Bitmap bitMap;
	PhotoViewAttacher attacher;
	//ImageView shotview ;
	public ArrayList<Bitmap> screenshots = HikVideoActivity.screenPictures;
	
	
	public BigScreenshotDialog(Context c,int k) {
		//shotview = new ImageView(c);
		dialog = new Dialog(c, R.style.MyDialog);
		dialog.setContentView(R.layout.big_screenshot_dlg);
		dialog.setCanceledOnTouchOutside(false);
		mViewPager = (ViewPager) dialog.findViewById(R.id.pager);
		mCustomPagerAdapter = new CustomPagerAdapter(c);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setCurrentItem(k);
        /*shotview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});*/
	}
	
	public void show() {
		dialog.show();
	}
	class CustomPagerAdapter extends PagerAdapter {
		 
	    Context mContext;
	    LayoutInflater mLayoutInflater;
	 
	    public CustomPagerAdapter(Context context) {
	        mContext = context;
	        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	 
	    @Override
	    public int getCount() {
	        return screenshots.size();
	    }
	 
	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	        return view == ((LinearLayout) object);
	    }
	 
	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {
	        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
	        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
	        imageView.setImageBitmap(screenshots.get(position));
	        attacher = new PhotoViewAttacher(imageView);
	        container.addView(itemView);
	        //shotview = imageView;
	        return itemView;
	    }
	 
	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        container.removeView((LinearLayout) object);
	    }
	}
	
	
	
}

