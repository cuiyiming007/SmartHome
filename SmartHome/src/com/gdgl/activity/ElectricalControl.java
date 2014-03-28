package com.gdgl.activity;


import java.util.ArrayList;
import java.util.List;

import com.gdgl.GalleryFlow.FancyCoverFlow;
import com.gdgl.adapter.ViewGroupAdapter;
import com.gdgl.adapter.lights_adapter;
import com.gdgl.model.lights;
import com.gdgl.smarthome.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class ElectricalControl extends Activity {
	
	private int[] images={R.drawable.img0001,R.drawable.img0030,R.drawable.img0100,R.drawable.img0130,R.drawable.img0200};
	
	PullToRefreshListView equipment_list ;
	List<lights> mList = new ArrayList<lights>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.electrical_control);
		initBack();
		lights mLights;
		for (int m = 0; m < 25; m++) {
			if (m % 2 == 0) {
				mLights = new lights(m, "light_" + m, "厨房_" + m, false);
			} else {
				mLights = new lights(m, "light_" + m, "厨房_" + m, false, 0.2);
			}

			mList.add(mLights);
		}
		
		FancyCoverFlow fancyCoverFlow=(FancyCoverFlow)findViewById(R.id.equipment_CoverFlow);
		fancyCoverFlow.setAdapter(new ViewGroupAdapter(getApplicationContext(),images,250,200));
		fancyCoverFlow.setCallbackDuringFling(false);
		
		fancyCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		fancyCoverFlow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		equipment_list=(PullToRefreshListView) findViewById(R.id.equipment_list);

		equipment_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = DateUtils.formatDateTime(
						getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				new GetDataTask().execute();
			}
		});

		// Add an end-of-list listener
		equipment_list.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {

			}
		});

		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		equipment_list.setAdapter(new lights_adapter(ElectricalControl.this, mList));
		
		
		
		
	}
	
	private void initBack() {
		// TODO Auto-generated method stub
		LinearLayout mBack = (LinearLayout) findViewById(R.id.goback);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TextView mTitle=(TextView)findViewById(R.id.title);
		mTitle.setText("电气控制");
	}

	private class GetDataTask extends AsyncTask<Void, Void, List<lights>> {

		@Override
		protected List<lights> doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return mList;
		}

		@Override
		protected void onPostExecute(List<lights> result) {
			// mListItems.addFirst("Added after refresh...");
			// mAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			equipment_list.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
