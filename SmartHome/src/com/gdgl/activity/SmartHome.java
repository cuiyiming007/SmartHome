package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.adapter.ViewPagerAdapter;
import com.gdgl.model.TabInfo;
import com.gdgl.service.SmartService;
import com.gdgl.smarthome.R;
import com.gdgl.util.SelectAddPopupWindow;
import com.gdgl.util.SelectPicPopupWindow;
import com.gdgl.util.ViewPagerCompat;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SmartHome extends FragmentActivity implements OnPageChangeListener {
    
    private final static String TAG="SmartHome";

    private ImageView set;
    private ImageView add;

    public static final String EXTRA_TAB = "tab";
    public static final String EXTRA_QUIT = "extra.quit";
    
    private ViewPagerCompat mViewPager;

    int mCurrentTab = 0;
    int mLastTab = -1;

    SelectPicPopupWindow mSetWindow;
    SelectAddPopupWindow mAddWindow;

    TextView devices;
    TextView region;
    TextView scence;
    TextView common_used;
    TextView video_urveillance;
    List<TextView> mTextViewList = new ArrayList<TextView>();

    int mSelectedColor = 0xff228B22;
    int mUnSelectedColor = Color.BLACK;
    
    Intent serviceIntent;
    private MsgReceiver msgReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        
        msgReceiver = new MsgReceiver();  
        IntentFilter intentFilter = new IntentFilter();  
        intentFilter.addAction("com.gdgl.activity.RECIEVER");  
        registerReceiver(msgReceiver, intentFilter);  
    }

    private void init() {

        Intent intent = getIntent();
        if (intent != null) {
            mCurrentTab = intent.getIntExtra(EXTRA_TAB, mCurrentTab);
        }
        mViewPager = (ViewPagerCompat) findViewById(R.id.mViewPager);

        ArrayList<TabInfo> mList = new ArrayList<TabInfo>();
        mList.add(new TabInfo(new RegionFragment()));
        mList.add(new TabInfo(new DevicesFragment()));
        mList.add(new TabInfo(new SceneFragment()));
        mList.add(new TabInfo(new Commonly_used_Fragment()));
        mList.add(new TabInfo(new VideoUrveillanceFragment()));

        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(), SmartHome.this, mList);

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(mCurrentTab);
        mLastTab = mCurrentTab;

        set = (ImageView) findViewById(R.id.set);
        add = (ImageView) findViewById(R.id.add);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showSetWindow(SmartHome.this);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showAddWindow(SmartHome.this);
            }
        });

        devices = (TextView) findViewById(R.id.devices);
        region = (TextView) findViewById(R.id.region);
        scence = (TextView) findViewById(R.id.scence);
        common_used = (TextView) findViewById(R.id.common_used);
        video_urveillance = (TextView) findViewById(R.id.video_urveillance);

        mTextViewList.add(region);
        mTextViewList.add(devices);
        mTextViewList.add(scence);
        mTextViewList.add(common_used);
        mTextViewList.add(video_urveillance);
        setMyTextColor(mCurrentTab);

        TitleClickLister mTitleClickLister = new TitleClickLister();
        for (int m = 0; m < mTextViewList.size(); m++) {
            mTextViewList.get(m).setOnClickListener(mTitleClickLister);
        }

    }

    class TitleClickLister implements OnClickListener {

        int index;

        public TitleClickLister() {

        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            if (mTextViewList.contains(v)) {
                index = getMId(v);
                ;
            }
            mViewPager.setCurrentItem(index);

            mTextViewList.get(index).setTextColor(mSelectedColor);
        }
    }

    public void setMyTextColor(int m) {
        getMId(null);
        mTextViewList.get(m).setTextColor(mSelectedColor);
    }

    public int getMId(View v) {
        // TODO Auto-generated method stub
        int m;
        for (m = 0; m < mTextViewList.size(); m++) {
            if (mTextViewList.get(m).equals(v)) {
                return m;
            }
            mTextViewList.get(m).setTextColor(mUnSelectedColor);
        }
        return m;
    }

    public void showSetWindow(final Activity context) {
        mSetWindow = new SelectPicPopupWindow(SmartHome.this, mSetOnClick);
        mSetWindow.showAtLocation(SmartHome.this.findViewById(R.id.set),
                Gravity.TOP | Gravity.RIGHT, 10, 240);
    }

    public void showAddWindow(final Activity context) {
        mAddWindow = new SelectAddPopupWindow(SmartHome.this, mAddOnClick);
        mAddWindow.showAtLocation(SmartHome.this.findViewById(R.id.add),
                Gravity.TOP | Gravity.RIGHT, 10, 240);
    }

    private OnClickListener mSetOnClick = new OnClickListener() {

        public void onClick(View v) {
            mSetWindow.dismiss();
        }
    };

    private OnClickListener mAddOnClick = new OnClickListener() {

        public void onClick(View v) {
            mAddWindow.dismiss();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_MENU)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub
        if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
            mLastTab = mCurrentTab;
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub
        mCurrentTab = arg0;
        setMyTextColor(arg0);
    }
    @Override
    protected void onStart() {
        super.onStart();
         serviceIntent = new Intent(this, SmartService.class);
         startService(serviceIntent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(serviceIntent);
        unregisterReceiver(msgReceiver);
        
    }
     public class MsgReceiver extends BroadcastReceiver{  
          
            @Override  
            public void onReceive(Context context, Intent intent) {  
//                int progress = intent.getIntExtra("progress", 0);  
                
                Log.i(TAG, "MsgReceiver has recieved");
            }  
              
        }  
}
