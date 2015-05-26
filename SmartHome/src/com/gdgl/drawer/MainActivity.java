package com.gdgl.drawer;

import com.gdgl.activity.RegionsFragment;
import com.gdgl.activity.ScenesFragment;
import com.gdgl.activity.TimingFragment;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.AddDlg.AddDialogcallback;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks, AddDialogcallback {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    int currentTab = 0;
    private Fragment mfragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setTitle("设备");

        mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
//				case R.id.menu_search:
//					if (1 == currentTab) {
//						AddDlg mAddDlg = new AddDlg(MainActivity.this,
//								AddDlg.REGION);
//						mAddDlg.setContent("添加区域");
//						mAddDlg.setType("区域名称");
//						mAddDlg.setDialogCallback(MainActivity.this);
//						mAddDlg.show();
////					} else if (3 == currentTab) {
////						AddDlg mAddDlg = new AddDlg(MainActivity.this,
////								AddDlg.SCENE);
////						mAddDlg.setContent("添加场景");
////						mAddDlg.setType("场景名称");
////						mAddDlg.setDialogCallback(MainActivity.this);
////						mAddDlg.show();
//						// } else if (0 == mCurrentTab) {
//						// Intent i = new Intent();
//						// i.setClass(SmartHome.this,
//						// AddCommonUsedActivity.class);
//						// startActivity(i);
//					}
//					break;

				default:
					break;
				}
				return false;
			}
		});
        
        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        
        saveLoginData();
    }
    
	private void saveLoginData(){
		getFromSharedPreferences.setsharedPreferences(MainActivity.this);
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		String name = intent.getStringExtra("name");
		String pwd = intent.getStringExtra("pwd");
		String cloud = intent.getStringExtra("cloud");
		boolean remenber = intent.getBooleanExtra("remenber", true);
		getFromSharedPreferences.setUid(id);
		if (remenber) {
			getFromSharedPreferences.setLogin(name, pwd, true);
		} else {
			getFromSharedPreferences.setLogin(name, pwd, false);
		}
		getFromSharedPreferences.setUserList(name, pwd);
		getFromSharedPreferences.setCloud(cloud);
		getFromSharedPreferences.setCloudList(cloud);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public void onNavigationDrawerItemSelected(int position) {
    	currentTab = position;
        switch (position) {
		case 0:
			mfragment = new DeviceTabFragment();
			break;
		case 1:
			mfragment = new RegionsFragment();
			break;
		case 2:
			mfragment = new ScenesFragment();
			break;
		case 3:
			mfragment = new TimingFragment();
			break;
		default:
			break;
		}
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, mfragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

	public interface refreshAdapter {
		public void refreshFragment();
	}
    
	@Override
	public void refreshdata() {
		// TODO Auto-generated method stub
		refreshAdapter mRefresh = (refreshAdapter)mfragment;
		mRefresh.refreshFragment();
	}
}
