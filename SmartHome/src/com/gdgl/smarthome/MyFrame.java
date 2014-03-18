package com.gdgl.smarthome;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class MyFrame extends FrameLayout {
	
	private int downX;
	private boolean isSlide = false;
	public MyFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: 
			
			break;
		case MotionEvent.ACTION_MOVE: 
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		}
		return super.onTouchEvent(event);
	}

	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: 
			
			break;
		case MotionEvent.ACTION_MOVE: 
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return super.dispatchTouchEvent(event);
	}
}
