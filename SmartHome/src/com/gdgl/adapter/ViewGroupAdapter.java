package com.gdgl.adapter;

import com.gdgl.GalleryFlow.FancyCoverFlow;
import com.gdgl.smarthome.R;
import com.gdgl.smarthome.R.color;
import com.gdgl.util.BitmapUtil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ViewGroupAdapter extends FancyCoverFlowAdapter {
    
    private Context mContext;
    private int[] mImageIds;
    private ImageView[] mImages;
    private String[] mTags;
    private int newHeight = 0;
    private int newWidth = 0;

	private final int DEFAULT_HEIGHT = 300;
	private final int DEFAULT_WIDTH = 200;

	private final float DEFAULT_ROUND = 16;

	public ViewGroupAdapter(Context c, int[] ImageIds, String[] tags,
			int Height, int Width) {
		mContext = c;
		mImageIds = ImageIds;
		mImages = new ImageView[mImageIds.length];
		mTags = tags;
		newHeight = Height;
		newWidth = Width;

		// initImages();
	}

	private Bitmap getBitmap(long id) {
		Bitmap originalImage = null;

		Resources res = mContext.getResources();
		originalImage = BitmapUtil.getBitmap(res, id);
		newWidth = newWidth <= 0 ? DEFAULT_WIDTH : newWidth;
		newHeight = newHeight <= 0 ? DEFAULT_HEIGHT : newHeight;
		// originalImage = BitmapUtil.zoomBitmap(originalImage, newWidth,
		// newHeight);
		originalImage = BitmapUtil.getRoundedCornerBitmap(originalImage,
				DEFAULT_ROUND);
		return originalImage;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImageIds.length;
	}

	@Override
	public Integer getItem(int arg0) {
		// TODO Auto-generated method stub
		return mImageIds[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

    @Override
    public View getCoverFlowItem(int position, View reusableView,
            ViewGroup parent) {
        // TODO Auto-generated method stub
        CustomViewGroup customViewGroup = null;

		if (reusableView != null) {
			customViewGroup = (CustomViewGroup) reusableView;
		} else {
			customViewGroup = new CustomViewGroup(mContext);
			customViewGroup.setLayoutParams(new FancyCoverFlow.LayoutParams(
					newWidth, newHeight + 50));
		}

		LayoutParams mLayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.setMargins(5, 5, 5, 5);

		customViewGroup.getImageView().setImageBitmap(
				getBitmap(this.getItem(position)));
		customViewGroup.getImageView().setLayoutParams(mLayoutParams);

		customViewGroup.getTextView().setText(mTags[position]);
		customViewGroup.getTextView().setTextColor(Color.BLUE);

		customViewGroup.setBackgroundResource(R.drawable.corners);

		return customViewGroup;
	}

}

class CustomViewGroup extends LinearLayout {

	// =============================================================================
	// Child views
	// =============================================================================

    private TextView textView;

    private ImageView imageView;

    // =============================================================================
    // Constructor
    // =============================================================================

    CustomViewGroup(Context context) {
        super(context);

        this.setOrientation(VERTICAL);

        this.textView = new TextView(context);
        this.imageView = new ImageView(context);

        LinearLayout.LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams);
        
        
//        this.textView.setLayoutParams(layoutParams);
//        this.imageView.setLayoutParams(layoutParams);
        
//        LinearLayout.LayoutParams layoutParams = new LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
        
        this.textView.setGravity(Gravity.CENTER);
        this.textView.setTextSize(18);
        this.textView.setTextColor(color.blue);
        this.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.imageView.setAdjustViewBounds(true);
        
        this.addView(this.imageView);
        this.addView(this.textView);
        
    }

    // =============================================================================
    // Getters
    // =============================================================================

    TextView getTextView() {
        return textView;
    }

    ImageView getImageView() {
        return imageView;
    }
}
