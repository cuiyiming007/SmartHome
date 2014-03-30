package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.activity.ElectricalControl.MyAdapterUpdater;
import com.gdgl.adapter.CurtainsAdapter;
import com.gdgl.adapter.CurtainsAdapter.gotoCurtainsControlFragment;
import com.gdgl.adapter.SwitchAdapter;
import com.gdgl.adapter.SwitchAdapter.gotoSwitchControlFragment;
import com.gdgl.model.CurtainModel;
import com.gdgl.model.SwitchModel;
import com.gdgl.smarthome.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

@SuppressLint("NewApi")
public class ElectricalControlFragment extends Fragment implements
        MyAdapterUpdater {

    private BaseAdapter mSwitchAdapter;
    private BaseAdapter mCurtainAdapter;
    private GetDataTask mGetDataTask;
    private View mView;
    PullToRefreshListView equipment_list;
    List<SwitchModel> mSwitchModelList = new ArrayList<SwitchModel>();
    List<CurtainModel> mCurtainModellList = new ArrayList<CurtainModel>();

    int refreshTag = 0;
    
    int adapterNum=1;
    
    private static final String ADAPTER_NUMBER="adapter_number";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if(null!=savedInstanceState){
            adapterNum=savedInstanceState.getInt(ADAPTER_NUMBER);
        }
        initSwitchModelList();
        initSCurtainModellList();

    }

    private void initSCurtainModellList() {
        // TODO Auto-generated method stub
        mCurtainModellList
                .add(new CurtainModel(1, "CurtainModel_1", "卧室", true));
        mCurtainModellList
                .add(new CurtainModel(2, "CurtainModel_2", "卧室", true));
        mCurtainModellList.add(new CurtainModel(3, "CurtainModel_3", "客厅",
                false));
        mCurtainModellList
                .add(new CurtainModel(4, "CurtainModel_4", "餐厅", true));
        mCurtainModellList
                .add(new CurtainModel(5, "CurtainModel_5", "客房", true));

        
    }

    private void initSwitchModelList() {
        // TODO Auto-generated method stub
        SwitchModel mSwitchModel;
        boolean[] bool1 = { true, true, false };
        boolean[] bool2 = { true, false };
        boolean[] bool3 = { false };
        boolean[] bool4 = { true };
        boolean[] bool5 = { true, true };
        for (int m = 0; m < 15; m++) {
            boolean[] bool = null;
            if (m % 5 == 0) {
                bool = bool1;
            } else if (m % 5 == 1) {
                bool = bool2;
            } else if (m % 5 == 2) {
                bool = bool3;
            } else if (m % 5 == 3) {
                bool = bool4;
            } else {
                bool = bool5;
            }
            mSwitchModel = new SwitchModel(m, "switch_" + m, "客厅" + m, bool);
            mSwitchModelList.add(mSwitchModel);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mView = inflater.inflate(R.layout.electrical_control_fragment, null);
        initView();
        return mView;
    }

    private void initView() {
        // TODO Auto-generated method stub
        equipment_list = (PullToRefreshListView) mView
                .findViewById(R.id.equipment_list);

        equipment_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                if (1 == refreshTag) {
                } else {
                    refreshTag = 1;
                    String label = DateUtils.formatDateTime(getActivity(),
                            System.currentTimeMillis(),
                            DateUtils.FORMAT_SHOW_TIME
                                    | DateUtils.FORMAT_SHOW_DATE
                                    | DateUtils.FORMAT_ABBREV_ALL);

                    // Update the LastUpdatedLabel
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
                            label);

                    // Do work to refresh the list here.
                    new MyGetDataTask().execute();
                }
            }
        });

        equipment_list.setClickable(true);

        equipment_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // TODO Auto-generated method stub
                Log.i("zgs", "zgs->onItemClick() mCurrentPage=0");
            }
        });
        this.setAdapter(adapterNum);
    }

    public interface GetDataTask {
        public void getDataTask();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mGetDataTask = (GetDataTask) activity;
        mSwitchAdapter = new SwitchAdapter(getActivity(), mSwitchModelList,
                (gotoSwitchControlFragment) activity);
        mCurtainAdapter = new CurtainsAdapter(getActivity(), mCurtainModellList,(gotoCurtainsControlFragment)activity);

    }

    private class MyGetDataTask extends
            AsyncTask<Void, Void, List<SwitchModel>> {

        @Override
        protected List<SwitchModel> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return mSwitchModelList;
        }

        @Override
        protected void onPostExecute(List<SwitchModel> result) {
            // mListItems.addFirst("Added after refresh...");
            // mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.

            equipment_list.onRefreshComplete();
            refreshTag = 0;
            super.onPostExecute(result);
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void setAdapter(int index) {
        // TODO Auto-generated method stub
        if (1 == refreshTag) {
            equipment_list.onRefreshComplete();
            refreshTag = 0;
        }
        adapterNum=index;
        switch (index) {
        case 0:
            equipment_list.setAdapter(mSwitchAdapter);
            break;
        case 1:
            equipment_list.setAdapter(mCurtainAdapter);
            break;
        default:
            equipment_list.setAdapter(mSwitchAdapter);
            break;
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        outState.putInt(ADAPTER_NUMBER, adapterNum);
        
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setListSelectedIndex(int postion) {
        // TODO Auto-generated method stub
        equipment_list.getRefreshableView().setSelection(postion);
    }
}
