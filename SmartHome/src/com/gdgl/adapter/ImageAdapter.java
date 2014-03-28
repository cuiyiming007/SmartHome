package com.gdgl.adapter;




import com.gdgl.GalleryFlow.FancyCoverFlow;
import com.gdgl.util.BitmapUtil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	private Integer[] mImageIds;
	private ImageView[] mImages;

	private int newHeight = 0;
	private int newWidth = 0;

	private final int DEFAULT_HEIGHT = 300;
	private final int DEFAULT_WIDTH = 200;

	private final float DEFAULT_ROUND = 16;

	public ImageAdapter(Context c, Integer[] ImageIds, int Height, int Width) {
		mContext = c;
		mImageIds = ImageIds;
		mImages = new ImageView[mImageIds.length];

		newHeight = Height;
		newWidth = Width;

		initImages();
	}

	@SuppressWarnings("deprecation")
	private void initImages() {
		int index = 0;
		Bitmap originalImage = null;

		Resources res = mContext.getResources();
		for (int imageId : mImageIds) {
			originalImage = BitmapUtil.getBitmap(res, imageId);
			newWidth = newWidth <= 0 ? DEFAULT_WIDTH : newWidth;
			newHeight = newHeight <= 0 ? DEFAULT_HEIGHT : newHeight;
			originalImage = BitmapUtil.zoomBitmap(originalImage, newWidth,
					newHeight);
			originalImage = BitmapUtil.getRoundedCornerBitmap(originalImage,
					DEFAULT_ROUND);
			if (null == originalImage) {
				continue;
			}
			ImageView imageView = new ImageView(mContext);
			imageView.setImageBitmap(originalImage);
			imageView.setLayoutParams(new FancyCoverFlow.LayoutParams(newWidth,
					newHeight));
			mImages[index++] = imageView;
		}
	}

	@SuppressWarnings("unused")
	private Resources getResources() {
		return null;
	}

	public int getCount() {
		return mImageIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return mImages[position];
	}
}
