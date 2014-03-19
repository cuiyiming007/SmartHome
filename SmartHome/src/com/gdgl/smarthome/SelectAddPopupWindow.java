package com.gdgl.smarthome;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class SelectAddPopupWindow extends PopupWindow {

	private View mMenuView;

	public SelectAddPopupWindow(final Activity context,
			OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.addxml, null);

		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
	
		this.setContentView(mMenuView);
		this.setWidth(w / 2 + 50);
		
		this.setHeight(LayoutParams.WRAP_CONTENT);
		
		this.setFocusable(true);
		
		this.setAnimationStyle(R.style.mystyle);
		
		ColorDrawable dw = new ColorDrawable(0000000000);
		
		this.setBackgroundDrawable(dw);
	
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout2).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

}
