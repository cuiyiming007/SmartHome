package com.gdgl.drawer;

import android.graphics.drawable.Drawable;

/**
 * Created by poliveira on 24/10/2014.
 */
public class NavigationItem {
    private String mText;
    private Drawable mDrawable;
    private Drawable mDrawable_pressed;

    public NavigationItem(String text, Drawable drawable, Drawable drawable_pressed) {
        mText = text;
        mDrawable = drawable;
        mDrawable_pressed = drawable_pressed;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Drawable getDrawable_press() {
        return mDrawable_pressed;
    }

    public void setDrawable_press(Drawable drawable) {
        mDrawable_pressed = drawable;
    }
    
    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }
}
