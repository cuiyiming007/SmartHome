package com.gdgl.activity;

import com.gdgl.GalleryFlow.FancyCoverFlow;
import com.gdgl.activity.ElectricalControlFragment.GetDataTask;
import com.gdgl.activity.SwitchControlFragment.removeSwitchControlFragment;
import com.gdgl.adapter.CurtainsAdapter.gotoCurtainsControlFragment;
import com.gdgl.adapter.SwitchAdapter.gotoSwitchControlFragment;
import com.gdgl.adapter.ViewGroupAdapter;
import com.gdgl.smarthome.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class ElectricalControl extends FragmentActivity implements GetDataTask,
        gotoSwitchControlFragment, removeSwitchControlFragment,gotoCurtainsControlFragment{

    private int[] images = { R.drawable.img0001, R.drawable.img0030,
            R.drawable.img0100 };
    private String[] tags = { "墙面开关", "窗帘", "红外转发器" };

    ElectricalControlFragment mElectricalControlFragment;
    FancyCoverFlow fancyCoverFlow;

    FragmentManager fragmentManager;

    TextView mTitle;

    int itemPostion;
    
    int mCurrentGalleryPostion=1;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.electrical_control);
        initBack();
        initFancyCoverFlow();
        initElectricalControlFragment();

    }

    @SuppressLint("NewApi")
    private void initElectricalControlFragment() {
        // TODO Auto-generated method stub
        fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        mElectricalControlFragment = new ElectricalControlFragment();
        fragmentTransaction.add(R.id.electrical_control_fragment,
                mElectricalControlFragment, "ElectricalControlFragment");
        fragmentTransaction.commit();

    }

    private void initFancyCoverFlow() {
        // TODO Auto-generated method stub
        fancyCoverFlow = (FancyCoverFlow) findViewById(R.id.equipment_CoverFlow);
        fancyCoverFlow.setAdapter(new ViewGroupAdapter(getApplicationContext(),
                images, tags, 250, 200));
        fancyCoverFlow.setCallbackDuringFling(false);
        fancyCoverFlow.setSelection(1);
        fancyCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                // TODO Auto-generated method stub
                reset(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        fancyCoverFlow.setOnItemClickListener(new OnItemClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // TODO Auto-generated method stub
                reset(position);
            }
        });
    }

    private void initBack() {
        // TODO Auto-generated method stub
        LinearLayout mBack = (LinearLayout) findViewById(R.id.goback);
        mTitle = (TextView) findViewById(R.id.title);
        mBack.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                    fancyCoverFlow.setSelection(mCurrentGalleryPostion);
                    mElectricalControlFragment.setAdapter(mCurrentGalleryPostion);
                } else {
                    finish();
                }
            }
        });
        
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public interface MyAdapterUpdater {
        public void setAdapter(int index);

        public void setListSelectedIndex(int postion);
    }

    @Override
    public void getDataTask() {
        // TODO Auto-generated method stub
        // new MyGetDataTask().execute();
    }

    private void setFragmentTitle(int index) {
        switch (index) {
        case 0:
            mTitle.setText("墙面开关");
            break;
        case 1:
            mTitle.setText("窗    帘");
            break;
        case 2:
            mTitle.setText("红外转发器");
            break;
        default:
            break;
        }

    }

    @SuppressLint("NewApi")
    @Override
    public void gotoSwitchControlFragment(int id, String mdata, int postion) {
        // TODO Auto-generated method stub
        itemPostion = postion;
        Bundle extras = new Bundle();
        extras.putInt(SwitchControlFragment.SWITCH_ID, id);
        extras.putString(SwitchControlFragment.BOLLEAN_ARRARY, mdata);
        SwitchControlFragment fragment = new SwitchControlFragment();
        fragment.setArguments(extras);
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.electrical_control_fragment, fragment,
                "SwitchControlFragment");
        fragmentTransaction.addToBackStack("SwitchToElectricalControlFragment");
        fragmentTransaction.commit();

    }

    @SuppressLint("NewApi")
    @Override
    public void removeFragment(Fragment fg) {
        // TODO Auto-generated method stub
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.detach(fg);
        fragmentTransaction.commit();
        fragmentManager.popBackStack();
        fancyCoverFlow.setSelection(mCurrentGalleryPostion);
        mElectricalControlFragment.setAdapter(mCurrentGalleryPostion);
        mElectricalControlFragment.setListSelectedIndex(itemPostion);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return back(mCurrentGalleryPostion);
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("NewApi")
    @Override
    public void gotoCurtainsControlFragment(int id, double mProgress) {
        // TODO Auto-generated method stub
        Bundle extras = new Bundle();
        extras.putInt(CurtainsControlFragment.CURTAIN_ID, id);
        extras.putDouble(CurtainsControlFragment.CURTAIN_STATE, mProgress);
        CurtainsControlFragment fragment = new CurtainsControlFragment();
        fragment.setArguments(extras);
        
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.electrical_control_fragment, fragment,
                "CurtainsControlFragment");
        fragmentTransaction.addToBackStack("CurtainsToElectricalControlFragment");
        fragmentTransaction.commit();
    }
    
    
    @SuppressLint("NewApi")
    public void reset(int resetPostion)
    {
         if (fragmentManager.getBackStackEntryCount() > 0) {
             fragmentManager.popBackStack();
         }
         mCurrentGalleryPostion=resetPostion;
         mElectricalControlFragment.setAdapter(resetPostion);
         setFragmentTitle(resetPostion);
    }
    
    @SuppressLint("NewApi")
    public boolean back(int backPostion) {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            reset(backPostion);
        }
        else
        {
            this.finish();
        }
        return true;
    }
}
