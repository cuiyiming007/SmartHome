package com.gdgl.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class BitmapUtil {

	public static Bitmap getBitmap(Resources res, int resId) {
		if (null == res || resId <= 0) {
			return null;
		}
		Bitmap newbmp = null;
		try {
			newbmp = BitmapFactory.decodeResource(res, resId);
			if (null == newbmp) {
				return null;
			}
			return newbmp;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		if (null == bitmap || width <= 0 || height <= 0) {
			return null;
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		if (null == newbmp) {
			return null;
		}
		return newbmp;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		if (null == bitmap || roundPx < 0) {
			return null;
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = null;
		try {
			output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, w, h);
			final RectF rectF = new RectF(rect);
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);

			if (!bitmap.equals(output) && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
		} catch (OutOfMemoryError e) {

		}
		return output;
	}
}
