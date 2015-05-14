package com.gdgl.activity;

/***
 * 最外层设备菜单
 */
import java.util.ArrayList;

import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gdgl.adapter.LinkageAdapter;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Linkage;
import com.gdgl.smarthome.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class LinkageFragment extends Fragment {

	ArrayList<Linkage> linkageList;
	
	View mView;
	ViewGroup nodevices;
	PullToRefreshListView linkage_list;
	ButtonFloat buttonFloat; 
	
	LinkageAdapter linkageAdapter;
	
	
	DataHelper mDateHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDateHelper = new DataHelper((Context) getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.linkage_fragment, null);
		initview();
		return mView;
	}

	private void initview() {
		// TODO Auto-generated method stub
		buttonFloat = (ButtonFloat) mView.findViewById(R.id.buttonFloat);
		nodevices = (ViewGroup) mView.findViewById(R.id.nodevices);
		nodevices.setVisibility(View.GONE);
		linkage_list = (PullToRefreshListView) mView.findViewById(R.id.linkage_list);
		linkageAdapter = new LinkageAdapter(getActivity());
		linkage_list.setAdapter(linkageAdapter);
		setListeners();
	}
	
	public void update(){
		SQLiteDatabase db = mDateHelper.getSQLiteDatabase();
		linkageList = (ArrayList<Linkage>) DataHelper.queryForLinkageList(db, DataHelper.LINKAGE_TABLE, null, null);
		if(linkageList.size() > 0){
			nodevices.setVisibility(View.GONE);
		}else{
			nodevices.setVisibility(View.VISIBLE);
		}
		linkageAdapter.setList(linkageList);		
		linkageAdapter.notifyDataSetChanged();
		db.close();
	}
	
	private void setListeners() {
		buttonFloat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), LinkageDetailActivity.class);
				getActivity().setIntent(intent);
			}
		});
		linkage_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("linkage", linkageList.get(position));
				intent.putExtras(bundle);
				intent.setClass(getActivity(), LinkageDetailActivity.class);
				getActivity().setIntent(intent);
			}
		});
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		update();
		super.onResume();
	}

}
