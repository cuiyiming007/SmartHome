package h264.com;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.gdgl.activity.HikVideoActivity;
import com.gdgl.smarthome.R;

public class BigScreenshotDialog  {
	 public static ViewPager mViewPager;
	private CustomPagerAdapter mCustomPagerAdapter;
	public static Dialog dialog;
	Bitmap bitMap;
	PhotoViewAttacher attacher;
	public ArrayList<Bitmap> screenshots = HikVideoActivity.screenPictures;
	
		public BigScreenshotDialog(Context c,int k,int dialogwidth,int dialogheight) {
		dialog = new Dialog(c, R.style.MyDialog1);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//dialog.setContentView(R.layout.big_screenshot_dlg);  
		dialog.setContentView(R.layout.activity_view_pager);
		dialog.setCanceledOnTouchOutside(false);
		
		Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = dialogwidth;
        lp.height = dialogheight;
        dialogWindow.setAttributes(lp);
        
		mViewPager = (ViewPager) dialog.findViewById(R.id.view_pager);
		mCustomPagerAdapter = new CustomPagerAdapter(c);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setCurrentItem(k);
        
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
	        return view == object;
	    }
	 
	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {
	        /*View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
	        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
	        imageView.setImageBitmap(screenshots.get(position));
	        attacher = new PhotoViewAttacher(imageView);
	        container.addView(itemView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	        //shotview = imageView;
	        return itemView;*/
	    	PhotoView photoView = new PhotoView(mContext);
			photoView.setImageBitmap(screenshots.get(position));

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
	    }
	 
	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        container.removeView((LinearLayout) object);
	    }
	}
	
	
	
}

