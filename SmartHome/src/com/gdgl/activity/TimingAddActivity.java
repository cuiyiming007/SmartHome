package com.gdgl.activity;

import com.gdgl.manager.SceneLinkageManager;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.timing.TimingAction;
import com.gdgl.smarthome.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TimingAddActivity extends ActionBarActivity {

	public static final String TYPE = "type";
	public static final int CREATE = 1;
	public static final int EDIT = 2;

	private Toolbar mToolbar;
	private ActionBar mActionBar;

	TimingAction mTimingAction;
	DataHelper mDataHelper;

	int timing_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_activity_secondary);

		mDataHelper = new DataHelper(TimingAddActivity.this);

		Bundle bundle = getIntent().getExtras();
		Intent i = getIntent();
		timing_type = i.getIntExtra(TYPE, 2);
		String titleName;
		if (timing_type == CREATE) {
			mTimingAction = new TimingAction();
			titleName = "添加定时";
		} else {
			mTimingAction = (TimingAction) i
					.getSerializableExtra(Constants.PASS_OBJECT);
			titleName = "编辑定时";
		}

		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(mToolbar);
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		mActionBar.setTitle(titleName);

		mToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
				case R.id.menu_ok:
					if (timing_type == CREATE) {
						if (mTimingAction.getIeee() == null) {
							Toast.makeText(TimingAddActivity.this, "请选择一个设备",
									Toast.LENGTH_SHORT).show();
							break;
						}
						SceneLinkageManager.getInstance().AddTimeAction("",
								mTimingAction.combine2Actpara(), 1,
								mTimingAction.getPara1(),
								mTimingAction.getPara2(),
								mTimingAction.boolean2String(), 1);
						finish();
					}
					if (timing_type == EDIT) {

						SceneLinkageManager.getInstance().EditTimeAction("",
								mTimingAction.combine2Actpara(), 1,
								mTimingAction.getPara1(),
								mTimingAction.getPara2(),
								mTimingAction.boolean2String(),
								mTimingAction.getTimingEnable(),
								mTimingAction.getTid());
						finish();
					}
					break;

				default:
					break;
				}
				return false;
			}
		});

		TimingAddFragment mfragent = new TimingAddFragment();
		mfragent.setArguments(bundle);
		mfragent.setTimingAction(mTimingAction);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.container, mfragent);
		fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_ok, menu);
		return true;
	}

	@Override
	public boolean onSupportNavigateUp() {
		// TODO Auto-generated method stub
		finish();
		return super.onSupportNavigateUp();
	}
}
