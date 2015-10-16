package com.gdgl.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/* to resolve the conflicts between viewpager and listview*/

public class ViewPagerCompat extends ViewPager {

	private boolean mViewTouchMode = false;

	public ViewPagerCompat(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ViewPagerCompat(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setViewTouchMode(boolean b) {
		if (b && !isFakeDragging()) {

			beginFakeDrag();
		} else if (!b && isFakeDragging()) {

			endFakeDrag();
		}
		mViewTouchMode = b;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (mViewTouchMode) {
			return false;
		}
		return super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		try {
			return super.onTouchEvent(ev);
		} catch (Exception e) {
			return false;
		}
	}
 
	@Override
	public boolean arrowScroll(int direction) {
		if (mViewTouchMode)
			return false;
		if (direction != FOCUS_LEFT && direction != FOCUS_RIGHT)
			return false;
		return super.arrowScroll(direction);
	}

}
