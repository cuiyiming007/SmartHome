package com.gdgl.drawer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.activity.ConfigurationActivity_New;
import com.gdgl.activity.LoginActivity;
import com.gdgl.mydata.AccountInfo;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyApplication;

/**
 * Created by poliveira on 24/10/2014.
 */
public class NavigationDrawerFragment extends Fragment implements
		NavigationDrawerCallbacks {
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
	private static final String PREFERENCES_FILE = "my_app_settings"; // TODO:
																		// change
																		// this
																		// to
																		// your
																		// file
	private NavigationDrawerCallbacks mCallbacks;
	private RecyclerView mDrawerList;
	private View mFragmentContainerView;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mActionBarDrawerToggle;
	private boolean mUserLearnedDrawer;
	private boolean mFromSavedInstanceState;
	private int mCurrentSelectedPosition;
	private Toolbar mToolBar;

	// add by Trice
	private TextView mUserName;
	private TextView mUserSet;
	private TextView mExit;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.drawer_fragment_navigation_google, container, false);
		mDrawerList = (RecyclerView) view.findViewById(R.id.drawerList);
		LinearLayoutManager layoutManager = new LinearLayoutManager(
				getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mDrawerList.setLayoutManager(layoutManager);
		mDrawerList.setHasFixedSize(true);

		final List<NavigationItem> navigationItems = getMenu();
		NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(
				navigationItems);
		adapter.setNavigationDrawerCallbacks(this);
		mDrawerList.setAdapter(adapter);
		selectItem(mCurrentSelectedPosition);
		
		AccountInfo info = LoginActivity.loginAccountInfo;
		String name = info.getAccount();
		if (null == name || name.trim().equals("")) {
			name = "Adminstartor";
		}
		mUserName = (TextView) view.findViewById(R.id.txtUsername);
		mUserName.setText(name);
		
		mUserSet = (TextView) view.findViewById(R.id.set_app);
		mUserSet.setText("设置");
		mUserSet.setCompoundDrawablesWithIntrinsicBounds(getResources()
				.getDrawable(R.drawable.ui2_config_set), null, null, null);
		mUserSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), ConfigurationActivity_New.class);
				startActivity(intent);
			}
		});
		mUserSet.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mUserSet.setCompoundDrawablesWithIntrinsicBounds(
							getResources().getDrawable(
									R.drawable.ui2_config_set_press), null,
							null, null);
					mUserSet.setBackgroundColor(getResources().getColor(
							R.color.blue_default));
					mUserSet.setTextColor(Color.WHITE);
					return false;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					mUserSet.setCompoundDrawablesWithIntrinsicBounds(
							getResources().getDrawable(
									R.drawable.ui2_config_set), null, null,
							null);
					mUserSet.setBackgroundColor(Color.TRANSPARENT);
					mUserSet.setTextColor(Color.parseColor("#3d3d3d"));
					return false;
				}
				return true;
			}
		});

		mExit = (TextView) view.findViewById(R.id.exit_app);
		mExit.setText("退出");
		mExit.setCompoundDrawablesWithIntrinsicBounds(getResources()
				.getDrawable(R.drawable.ui2_config_exit), null, null, null);
		mExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.getInstance().finishSystem();
			}
		});
		mExit.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mExit.setCompoundDrawablesWithIntrinsicBounds(
							getResources().getDrawable(
									R.drawable.ui2_config_exit_press), null,
							null, null);
					mExit.setBackgroundColor(getResources().getColor(
							R.color.blue_default));
					mExit.setTextColor(Color.WHITE);
					return false;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_UP:
					mExit.setCompoundDrawablesWithIntrinsicBounds(
							getResources().getDrawable(
									R.drawable.ui2_config_exit), null, null,
							null);
					mExit.setBackgroundColor(Color.TRANSPARENT);
					mExit.setTextColor(Color.parseColor("#3d3d3d"));
					return false;
				}
				return true;
			}
		});

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(getActivity(),
				PREF_USER_LEARNED_DRAWER, "false"));
		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	public ActionBarDrawerToggle getActionBarDrawerToggle() {
		return mActionBarDrawerToggle;
	}

	public void setActionBarDrawerToggle(
			ActionBarDrawerToggle actionBarDrawerToggle) {
		mActionBarDrawerToggle = actionBarDrawerToggle;
	}

	public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
		mToolBar = toolbar;
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		if (mFragmentContainerView.getParent() instanceof ScrimInsetsFrameLayout) {
			mFragmentContainerView = (View) mFragmentContainerView.getParent();
		}
		mDrawerLayout = drawerLayout;
		mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(
				R.color.myPrimaryDarkColor));

		mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(),
				mDrawerLayout, toolbar, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded())
					return;

				mToolBar.setTitle(getMenu().get(mCurrentSelectedPosition)
						.getText());
				getActivity().invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded())
					return;
				if (!mUserLearnedDrawer) {
					mUserLearnedDrawer = true;
					saveSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER,
							"true");
				}
				mToolBar.setTitle(R.string.app_name);

				getActivity().invalidateOptionsMenu();
			}
		};

		if (!mUserLearnedDrawer && !mFromSavedInstanceState)
			mDrawerLayout.openDrawer(mFragmentContainerView);

		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mActionBarDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
	}

	public void openDrawer() {
		mDrawerLayout.openDrawer(mFragmentContainerView);
		mToolBar.setTitle(R.string.app_name);
	}

	public void closeDrawer() {
		mDrawerLayout.closeDrawer(mFragmentContainerView);
		mToolBar.setTitle(getMenu().get(mCurrentSelectedPosition).getText());
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	public List<NavigationItem> getMenu() {
		List<NavigationItem> items = new ArrayList<NavigationItem>();
		items.add(new NavigationItem("设备", getResources().getDrawable(
				R.drawable.ui2_config_device), getResources().getDrawable(
				R.drawable.ui2_config_device_press)));
		items.add(new NavigationItem("情景", getResources().getDrawable(
				R.drawable.ui2_config_scene), getResources().getDrawable(
				R.drawable.ui2_config_scene_press)));
		items.add(new NavigationItem("定时", getResources().getDrawable(
				R.drawable.ui2_config_time), getResources().getDrawable(
				R.drawable.ui2_config_time_press)));
		items.add(new NavigationItem("联动", getResources().getDrawable(
				R.drawable.ui2_config_linkage), getResources().getDrawable(
				R.drawable.ui2_config_linkage_press)));
		return items;
	}

	/**
	 * Changes the icon of the drawer to back
	 */
	public void showBackButton() {
		if (getActivity() instanceof ActionBarActivity) {
			((ActionBarActivity) getActivity()).getSupportActionBar()
					.setDisplayHomeAsUpEnabled(true);
		}
	}

	/**
	 * Changes the icon of the drawer to menu
	 */
	public void showDrawerButton() {
		if (getActivity() instanceof ActionBarActivity) {
			((ActionBarActivity) getActivity()).getSupportActionBar()
					.setDisplayHomeAsUpEnabled(false);
		}
		mActionBarDrawerToggle.syncState();
	}

	void selectItem(int position) {
		mCurrentSelectedPosition = position;
		if (mDrawerLayout != null) {
			closeDrawer();
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
		((NavigationDrawerAdapter) mDrawerList.getAdapter())
				.selectPosition(position);
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mActionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		selectItem(position);
		// mToolBar.setTitle(getMenu().get(position).getText());
	}

	public DrawerLayout getDrawerLayout() {
		return mDrawerLayout;
	}

	public void setDrawerLayout(DrawerLayout drawerLayout) {
		mDrawerLayout = drawerLayout;
	}

	public static void saveSharedSetting(Context ctx, String settingName,
			String settingValue) {
		SharedPreferences sharedPref = ctx.getSharedPreferences(
				PREFERENCES_FILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(settingName, settingValue);
		editor.apply();
	}

	public static String readSharedSetting(Context ctx, String settingName,
			String defaultValue) {
		SharedPreferences sharedPref = ctx.getSharedPreferences(
				PREFERENCES_FILE, Context.MODE_PRIVATE);
		return sharedPref.getString(settingName, defaultValue);
	}
}
