package com.gdgl.smarthome;



import com.gdgl.mylistener.OnViewChangeListener;

import android.R.bool;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.Scroller;

public class MyScrollLayout extends ViewGroup{

    private static final String TAG = "ScrollLayout";      
    private VelocityTracker mVelocityTracker;  			// 用于判断甩动手势    
    private static final int SNAP_VELOCITY = 400;        
    private Scroller  mScroller;						// 滑动控制�??	
    private int mCurScreen;    						    
	private int mDefaultScreen = 0;    						 
    private float mLastMotionX;       
    private float mLastMotionY;       
    
    private boolean isPass = false;
    
    private OnViewChangeListener mOnViewChangeListener;	 
	public MyScrollLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}	
	public MyScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public MyScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub		
		init(context);
	}
	
	private void init(Context context)
	{
		mCurScreen = mDefaultScreen;    	  
	 //   mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();    	        
	    mScroller = new Scroller(context); 
	    
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub		
		 if (changed) {    
	            int childLeft = 0;    
	            final int childCount = getChildCount();    	                
	            for (int i=0; i<childCount; i++) {    
	                final View childView = getChildAt(i);    
	                if (childView.getVisibility() != View.GONE) {    
	                    final int childWidth = childView.getMeasuredWidth();    
	                    childView.layout(childLeft, 0,     
	                            childLeft+childWidth, childView.getMeasuredHeight());    
	                    childLeft += childWidth;    
	                }    
	            }    
	        }    
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);		
		final int width = MeasureSpec.getSize(widthMeasureSpec);       
	    final int widthMode = MeasureSpec.getMode(widthMeasureSpec);      
	    		
		final int count = getChildCount();       
        for (int i = 0; i < count; i++) {       
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);       
        }                
        scrollTo(mCurScreen * width, 0);		
	}

	 public void snapToDestination() {    
	        final int screenWidth = getWidth();    
	        final int destScreen = (getScrollX()+ screenWidth/2)/screenWidth;    
	        snapToScreen(destScreen);    
	 }  
	
	 public void snapToScreen(int whichScreen) {    	
	        // get the valid layout page    
	        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount()-1));    
	        if (getScrollX() != (whichScreen*getWidth())) {    	                
	            final int delta = whichScreen*getWidth()-getScrollX();    
	      	            mScroller.startScroll(getScrollX(), 0,     
	                    delta, 0, 300);
	            
	            mCurScreen = whichScreen;    
	            invalidate();       // Redraw the layout    	            
	            if (mOnViewChangeListener != null)
	            {
	            	mOnViewChangeListener.OnViewChange(mCurScreen);
	            }
	        }    
	    }    

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {    
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
            postInvalidate();    
        }   
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub           	            
	        final int action = event.getAction();    
	        final float x = event.getX();    
	        final float y = event.getY();    
	            
	        switch (action) {    
	        case MotionEvent.ACTION_DOWN: 	
	        	System.out.println("������onTouchEvent");
	        	  Log.i("", "onTouchEvent  ACTION_DOWN");	        	  
	        	if (mVelocityTracker == null) {    
			            mVelocityTracker = VelocityTracker.obtain();    
			            mVelocityTracker.addMovement(event); 
			    }        	 
	            if (!mScroller.isFinished()){    
	                mScroller.abortAnimation();    
	            }                
	            mLastMotionX = x;	           
	            mLastMotionY = y;	           
	            break;    
	                
	        case MotionEvent.ACTION_MOVE:  
	        	System.out.println("���໬��onTouchEvent");
		           int deltaX = (int)(mLastMotionX - x);	           
	        	   if (IsCanMove(deltaX))
	        	   {
	        		 if (mVelocityTracker != null)
	  		         {
	  		            	mVelocityTracker.addMovement(event); 
	  		         }   
	  	            mLastMotionX = x;     
	  	            scrollBy(deltaX, 0);	
	        	   }
         
	           break;    	                
	        case MotionEvent.ACTION_UP:       
	        	System.out.println("����ſ�onTouchEvent");
	        	int velocityX = 0;
	            if (mVelocityTracker != null)
	            {
	            	mVelocityTracker.addMovement(event); 
	            	mVelocityTracker.computeCurrentVelocity(1000);  
	            	velocityX = (int) mVelocityTracker.getXVelocity();
	            }	               	                
	            if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {       
	                // Fling enough to move left       
	                Log.e(TAG, "snap left");    
	                snapToScreen(mCurScreen - 1);       
	            } else if (velocityX < -SNAP_VELOCITY       
	                    && mCurScreen < getChildCount() - 1) {       
	                // Fling enough to move right       
	                Log.e(TAG, "snap right");    
	                snapToScreen(mCurScreen + 1);       
	            } else {       
	                snapToDestination();       
	            }      
	            	            
	            if (mVelocityTracker != null) {       
	                mVelocityTracker.recycle();       
	                mVelocityTracker = null;       
	            }       
	      //      mTouchState = TOUCH_STATE_REST;
	            break;      
	        }    	            
	        return true;    
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: 
			System.out.println("������onInterceptTouchEvent");
			if(isPass){
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE: 
			System.out.println("���໬��onInterceptTouchEvent");
			if(isPass){
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			System.out.println("����ſ�onInterceptTouchEvent");
			break;
		}
		return super.onInterceptTouchEvent(event);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: 
			mLastMotionX = event.getX();	           
            mLastMotionY = event.getY();
			System.out.println("������dispatchTouchEvent");
			break;
		case MotionEvent.ACTION_MOVE: 
			System.out.println(Math.abs(event.getX()- mLastMotionX));
			System.out.println(Math.abs(event.getY()- mLastMotionY));
			double tanNum = Math.atan(Math.abs(event.getY()-mLastMotionY)/Math.abs(event.getX()- mLastMotionX));
			double retote = tanNum/3.14*180;
			System.out.println("�Ƕ�:"+retote);
			if (retote<45) {
				System.out.println("---------���໬��dispatchTouchEvent");
				isPass= true;
			}else{
				isPass = false;
			}
			onInterceptTouchEvent(event);
			System.out.println("***************"+isPass);
			break;
		case MotionEvent.ACTION_UP:
			System.out.println("����ſ�dispatchTouchEvent");
			break;
		}
		return super.dispatchTouchEvent(event);
	}

	private boolean IsCanMove(int deltaX)
	{
		if (getScrollX() <= 0 && deltaX < 0 ){
			return false;
		}	
		if  (getScrollX() >=  (getChildCount() - 1) * getWidth() && deltaX > 0){
			return false;
		}		
		return true;
	}
	
	public void SetOnViewChangeListener(OnViewChangeListener listener)
	{
		mOnViewChangeListener = listener;
	}
}