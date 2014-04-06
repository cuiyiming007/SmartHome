package com.gdgl.adapter;

import java.util.ArrayList;

import com.gdgl.model.TabInfo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

public class ViewPagerAdapter extends FragmentPagerAdapter{

    private static final String TAG = "ViewPagerAdapter";
    ArrayList<TabInfo> tabs = null;
    Context mContext = null;

    public ViewPagerAdapter(FragmentManager fm, Context context,
            ArrayList<TabInfo> tabs) {
        super(fm);
        this.tabs = tabs;
        this.mContext = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        Log.i(TAG, "get fragment " + arg0);
        Fragment fragment = null;
        if (tabs != null && arg0 < tabs.size()) {
            TabInfo tab = tabs.get(arg0);
            if (tab == null)
                return null;
            fragment = tab.createFragment();
        }
        return fragment;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (tabs != null && tabs.size() > 0)
            return tabs.size();
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub

        Fragment fragment = null;
        TabInfo tab = tabs.get(position);
        fragment = (Fragment) super.instantiateItem(container, position);
        tab.mFragment = fragment;
        return fragment;
    }
}
