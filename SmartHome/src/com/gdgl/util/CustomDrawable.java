package com.gdgl.util;

import com.gdgl.smarthome.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

public class CustomDrawable extends ShapeDrawable {
	private Drawable mDrawable;
	private Bitmap mBitmap;
	int TOP_LEFT=0;
	int TOP_CENTER=TOP_LEFT+1;
	int TOP_RIGHT=TOP_LEFT+2;
	int MIDDLE_CENTER=TOP_LEFT+3;
	int BOTTOM_LEFT=TOP_LEFT+4;
	int BOTTOM_CENTER=TOP_LEFT+5;
	int BOTTOM_RIGHT=TOP_LEFT+6;
	
	public CustomDrawable(Context c){
		mBitmap=BitmapFactory.decodeResource(c.getResources(), R.drawable.my_bc);
		mDrawable=new ShapeDrawable(new CustomSharp(new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP)));
	}
	
	public void updateBounds(){
		Rect rect=getBounds();
		mDrawable.setBounds(rect.left, rect.top, rect.right, rect.bottom);
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.draw(canvas);
		updateBounds();
		mDrawable.draw(canvas);
		
	}
	public class CustomSharp extends RectShape{
		private Shader mShader;
		public CustomSharp(Shader s){
			mShader=s;
		}
		@Override
		public void draw(Canvas canvas, Paint paint) {
			// TODO Auto-generated method stub
			super.draw(canvas, paint);
			if(null!=mShader){
				paint.setShader(mShader);
				canvas.drawRect(rect(), paint);
			}
		}
		
	}
	
}
