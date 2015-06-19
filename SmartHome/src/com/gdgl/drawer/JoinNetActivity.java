package com.gdgl.drawer;

import com.gdgl.activity.JoinNetFragment;
import com.gdgl.activity.JoinNetFragment.ChangeFragment;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyApplicationFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class JoinNetActivity extends ActionBarActivity implements ChangeFragment{
    private Toolbar mToolbar;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_secondary);
        MyApplicationFragment.getInstance().setActivity(this);
        
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setTitle("添加设备");
        
        Fragment mfragent = new JoinNetFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        
        fragmentTransaction.replace(R.id.container, mfragent);
        fragmentTransaction.commit();
        MyApplicationFragment.getInstance().addNewTask(mfragent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
	@Override
	public void setFragment(Fragment f) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.container, f);
		fragmentTransaction.commit();
		MyApplicationFragment.getInstance().addFragment(f);
	}
	
    @Override
    public boolean onSupportNavigateUp() {
    	// TODO Auto-generated method stub
    	if (MyApplicationFragment.getInstance().getFragmentListSize() > 1) {
			MyApplicationFragment.getInstance().removeLastFragment();
			return super.onSupportNavigateUp();
		}
    	finish();
    	return super.onSupportNavigateUp();
    }
}
