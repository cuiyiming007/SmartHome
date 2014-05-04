package com.gdgl.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gdgl.adapter.AllDevicesAdapter;
import com.gdgl.adapter.AllDevicesAdapter.ViewHolder;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AddCommonUsedActivity extends Activity {

	List<String> mRegion;
	List<String> mExitRegion;

	List<String> mScene;
	List<String> mExitScene;
	Button mAdd;
	boolean RegionRefreshTag = false;
	boolean SceneRefreshTag = false;
	TextView no_region, no_scene;
	
	PullToRefreshListView region_list, scene_list;

	public static final int REGION = 1;
	public static final int SCENE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_commonused);
		initdata();
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		LinearLayout mBack = (LinearLayout) findViewById(R.id.goback);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mAdd=(Button)findViewById(R.id.add_use);
		mAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StringBuilder sb=new StringBuilder();
				if(null!=mExitRegion && mExitRegion.size()>0){
					for (String s : mExitRegion) {
						sb.append(UiUtils.REGION_FLAG+s+"@@");
					}
				}
				if(null!=mExitScene && mExitScene.size()>0){
					for (String s : mExitScene) {
						sb.append(UiUtils.SCENE_FLAG+s+"@@");
					}
				}
				getFromSharedPreferences.setharedPreferences(AddCommonUsedActivity.this);
				getFromSharedPreferences.setCommonUsed(sb.toString());
				finish();
			}
		});
		
		region_list = (PullToRefreshListView) findViewById(R.id.region_list);

		region_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (!RegionRefreshTag) {
				} else {
					RegionRefreshTag = true;
					String label = DateUtils.formatDateTime(
							AddCommonUsedActivity.this,
							System.currentTimeMillis(),
							DateUtils.FORMAT_SHOW_TIME
									| DateUtils.FORMAT_SHOW_DATE
									| DateUtils.FORMAT_ABBREV_ALL);

					// Update the LastUpdatedLabel
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
							label);

					// Do work to refresh the list here.
					refreshListData();
				}
			}
		});

		scene_list = (PullToRefreshListView) findViewById(R.id.scene_list);

		scene_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (!SceneRefreshTag) {
				} else {
					SceneRefreshTag = true;
					String label = DateUtils.formatDateTime(
							AddCommonUsedActivity.this,
							System.currentTimeMillis(),
							DateUtils.FORMAT_SHOW_TIME
									| DateUtils.FORMAT_SHOW_DATE
									| DateUtils.FORMAT_ABBREV_ALL);

					// Update the LastUpdatedLabel
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
							label);

					// Do work to refresh the list here.
					refreshListData();
				}
			}
		});
		
		no_region=(TextView)findViewById(R.id.no_region);
		no_scene=(TextView)findViewById(R.id.no_scene);
		
		Log.i("tag", "zgs-> mRegion.size()="+mRegion.size());
		Log.i("tag", "zgs-> mScene.size()="+mScene.size());
		
		if(null==mRegion || mRegion.size()==0){
			no_region.setVisibility(View.VISIBLE);
			region_list.setVisibility(View.GONE);
		}else{
			region_list.setAdapter(new CommonUsedAdapter(mRegion, REGION,AddCommonUsedActivity.this));
		}
		
		if(null==mScene || mScene.size()==0){
			no_scene.setVisibility(View.VISIBLE);
			scene_list.setVisibility(View.GONE);
		}else{
			scene_list.setAdapter(new CommonUsedAdapter(mScene, SCENE,AddCommonUsedActivity.this));
		}

		
	}

	private class GetDataTask extends AsyncTask<Void, Void, List<String>> {

		@Override
		protected List<String> doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			if (RegionRefreshTag) {
				return mRegion;
			}
			if (SceneRefreshTag) {
				return mScene;
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			super.onPostExecute(result);
			if (RegionRefreshTag) {
				region_list.onRefreshComplete();
			}
			if (SceneRefreshTag) {
				scene_list.onRefreshComplete();
			}

		}
	}

	private void refreshListData() {
		// TODO Auto-generated method stub
			new GetDataTask().execute();
	}

	private void initdata() {
		// TODO Auto-generated method stub
		mRegion = new ArrayList<String>();
		mExitRegion = new ArrayList<String>();
		mScene = new ArrayList<String>();
		mExitScene = new ArrayList<String>();

		getFromSharedPreferences
				.setharedPreferences(AddCommonUsedActivity.this);
		String comm = getFromSharedPreferences.getCommonUsed();
		if (null != comm && !comm.trim().equals("")) {
			String[] result = comm.split("@@");
			for (String string : result) {
				if (!string.trim().equals("")) {
					if (string.indexOf(UiUtils.REGION_FLAG) == 0) {
						mExitRegion.add(string.replace(UiUtils.REGION_FLAG, "")
								.trim());
					} else if (string.indexOf(UiUtils.SCENE_FLAG) == 0) {
						mExitScene.add(string.replace(UiUtils.SCENE_FLAG, "")
								.trim());
					}
				}
			}
		}

		List<String> temp = new ArrayList<String>();
		String[] mregion = null;
		String reg = getFromSharedPreferences.getRegion();
		if (null != reg && !reg.trim().equals("")) {
			mregion = reg.split("@@");
		}
		if (null != mregion) {
			for (String string : mregion) {
				if (!string.equals("")) {
					temp.add(string);
				}
			}
		}
		for (String string : temp) {
			if (!mExitRegion.contains(string)) {
				mRegion.add(string);
				Log.i("tag", "zgs-> add to region "+ string);
			}
		}

		temp = null;
		temp = DataUtil.getScenes(AddCommonUsedActivity.this, null);

		for (String string : temp) {
			if (!mExitScene.contains(string)) {
				mScene.add(string);
				Log.i("tag", "zgs-> add to scene "+ string);
			}
		}

	}

	private class CommonUsedAdapter extends BaseAdapter {

		private List<String> mList;
		private int type;
		private Context mContext;
		private Map<Integer, Boolean> isSelected;

		public CommonUsedAdapter(List<String> m, int t,Context c) {
			mList = m;
			type = t;
			mContext=c;
			isSelected = new HashMap<Integer, Boolean>();
			for (int i = 0; i < mList.size(); i++) {
				isSelected.put(i, false);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mList) {
				return mList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mList && mList.size() > 0) {
				return mList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			final String s = mList.get(position);
			ViewHolder mViewHolder;
			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.common_used_item, null);
				mViewHolder.use_img = (ImageView) convertView
						.findViewById(R.id.used_img);
				mViewHolder.use_text = (TextView) convertView
						.findViewById(R.id.used_name);
				mViewHolder.selected = (CheckBox) convertView
						.findViewById(R.id.selected);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			Log.i("zgs", "zgs->s="+s);
			mViewHolder.use_text.setText(s);
			if(type==REGION){
				mViewHolder.use_img.setImageResource(R.drawable.region);
			}else if(type==SCENE){
				mViewHolder.use_img.setImageResource(R.drawable.scene);
			}
			mViewHolder.selected
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							isSelected.put(position, isChecked);
							switch (type) {
							case REGION:
								if (isChecked) {
									if (!mExitRegion.contains(s)) {
										mExitRegion.add(s);
									} else if (mExitRegion.contains(s)) {
										mExitRegion.remove(s);
									}
								}
								break;
							case SCENE:
								if (isChecked) {
									if (!mExitScene.contains(s)) {
										mExitScene.add(s);
									} else if (mExitScene.contains(s)) {
										mExitScene.remove(s);
									}
								}
								break;
							default:
								break;
							}
						}
					});
			mViewHolder.selected.setChecked(isSelected.get(position));
			return convertView;
		}

		class ViewHolder {
			ImageView use_img;
			TextView use_text;
			CheckBox selected;
		}

	}

}
