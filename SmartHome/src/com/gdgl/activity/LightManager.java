package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.adapter.LightsAdapter;
import com.gdgl.model.LightsModel;
import com.gdgl.smarthome.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class LightManager extends Activity {

    PullToRefreshListView mListView;
    LinearLayout mBack;
    List<LightsModel> mList = new ArrayList<LightsModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lights_manager);
        LightsModel mLights;
        for (int m = 0; m < 25; m++) {
            if (m % 2 == 0) {
                mLights = new LightsModel(m, "light_" + m, "厨房_" + m, false);
            } else {
                mLights = new LightsModel(m, "light_" + m, "厨房_" + m, false, 0.2);
            }

            mList.add(mLights);
        }
        Log.i("zgs", "zgs-> LightManager" + mList.get(9).getLevel());
        mListView = (PullToRefreshListView) findViewById(R.id.light_list);

        mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

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
        mListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {

            }
        });

        // You can also just use setListAdapter(mAdapter) or
        // mPullRefreshListView.setAdapter(mAdapter)
        mListView.setAdapter(new LightsAdapter(LightManager.this, mList));
        mBack = (LinearLayout) findViewById(R.id.goback);
        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

    }

    private class GetDataTask extends AsyncTask<Void, Void, List<LightsModel>> {

        @Override
        protected List<LightsModel> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return mList;
        }

        @Override
        protected void onPostExecute(List<LightsModel> result) {
            // mListItems.addFirst("Added after refresh...");
            // mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }
}
