package com.gdgl.drawer;

import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.Constants;
import com.gdgl.smarthome.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class DeviceControlActivity extends ActionBarActivity {
    private Toolbar mToolbar;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_secondary);
        Bundle mBundle = getIntent().getExtras();
        String name = "";
		if (null != mBundle) {
			DevicesModel devicesModel = (DevicesModel)mBundle.getSerializable(Constants.PASS_OBJECT);
			name = devicesModel.getmDefaultDeviceName();
		}
        
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
//        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setTitle(name);
        
        Fragment mfragent = new DeviceControlFragment();
        mfragent.setArguments(mBundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        
        fragmentTransaction.replace(R.id.container, mfragent);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onSupportNavigateUp() {
    	// TODO Auto-generated method stub
    	finish();
    	return super.onSupportNavigateUp();
    }
}
