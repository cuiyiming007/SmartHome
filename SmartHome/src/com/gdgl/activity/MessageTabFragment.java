package com.gdgl.activity;

import java.util.ArrayList;

import com.gdgl.smarthome.R;
import com.gdgl.tabstrip.PagerSlidingTabStrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessageTabFragment extends Fragment {

	View mView;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	private ArrayList<Fragment> mfragments;
	MessageListFragment fragment0;
	MessageIpcLinkageFragment fragment1;
	
	int initTab = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle extras = getArguments();
		if (null != extras) {
			initTab = extras.getInt("INITTAB");
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.device_tab_fragment, null);

		initView();
		// TODO Auto-generated method stub
		return mView;
	}

	public void initView() {
		pager = (ViewPager) mView.findViewById(R.id.pager);

		tabs = (PagerSlidingTabStrip) mView.findViewById(R.id.tabs);
		tabs.setVisibility(View.VISIBLE);
		tabs.setShouldExpand(true);
		tabs.setUnderlineUpside(true);
		tabs.setIndicatorColorResource(R.color.blue_default);

		fragment0 = new MessageListFragment();
		fragment1 = new MessageIpcLinkageFragment();
		mfragments = new ArrayList<Fragment>();
		mfragments.add(fragment0);
		mfragments.add(fragment1);
		adapter = new MyPagerAdapter(getChildFragmentManager(), mfragments);

		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
		pager.setCurrentItem(initTab);
	}

	public void clickDelete() {
		if (pager.getCurrentItem() == 0) {
			fragment0.deleteClick();
		} else {
			fragment1.deleteClick();
		}
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "告警消息", "IPC联动消息" };

		private ArrayList<Fragment> fragments;

		public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int pos) {
			return fragments.get(pos);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

	}
}
