package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.smarthome.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class GuideActivity extends Activity implements OnClickListener
{

	private ViewPager mViewPager;
	private PagerAdapter mAdapter;
	private List<View> mViews = new ArrayList<View>();
	// TAB

	private LinearLayout mTabWeixin;
	private LinearLayout mTabFrd;
	private LinearLayout mTabAddress;
	private LinearLayout mTabSetting;
	
	private Button mEnterButton;
	

	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.i("guide", "2222222222222");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);

		initView();

		initEvents();

	}

	private void initEvents()
	{
	

		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int arg0)
			{
				

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{

			}
		});
	}

	private void initView()
	{
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
	

		LayoutInflater mInflater = LayoutInflater.from(this);
		View tab01 = mInflater.inflate(R.layout.tab01, null);
		View tab02 = mInflater.inflate(R.layout.tab02, null);
		View tab03 = mInflater.inflate(R.layout.tab03, null);
		View tab04 = mInflater.inflate(R.layout.tab04, null);
		mViews.add(tab01);
		mViews.add(tab02);
		mViews.add(tab03);
		mViews.add(tab04);
		mEnterButton=(Button)tab04.findViewById(R.id.imgbtn_enter);
		mEnterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(GuideActivity.this,LoginActivity.class);
				startActivity(intent);
				GuideActivity.this.finish();
			}
		});

		mAdapter = new PagerAdapter()
		{

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object)
			{
				container.removeView(mViews.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position)
			{
				View view = mViews.get(position);
				container.addView(view);
				return view;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1)
			{
				return arg0 == arg1;
			}

			@Override
			public int getCount()
			{
				return mViews.size();
			}
		};

		mViewPager.setAdapter(mAdapter);

	}

	@Override
	public void onClick(View v) {
		
	}


}
