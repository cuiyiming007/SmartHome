package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gdgl.activity.SmartHome.refreshAdapter;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class CommonUsedFragment extends Fragment implements refreshAdapter,
		Dialogcallback {

	GridView content_view;
	View mView;
	
	List<StringTag> mList;
	
	CustomeAdapter mCustomeAdapter;

	ViewGroup nodevices;
	
	public class StringTag{
		public String name;
		public String tag;
		public StringTag(String s1,String s2){
			name=s1;
			tag=s2;
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		mList=new ArrayList<CommonUsedFragment.StringTag>();
		getFromSharedPreferences
				.setharedPreferences((Context)getActivity());
		String comm = getFromSharedPreferences.getCommonUsed();
		if (null != comm && !comm.trim().equals("")) {
			String[] result = comm.split("@@");
			for (String string : result) {
				if (!string.trim().equals("")) {
					if (string.indexOf(UiUtils.REGION_FLAG) == 0) {
						mList.add(new StringTag(string.replace(UiUtils.REGION_FLAG, "")
								.trim(), UiUtils.REGION_FLAG));
					} else if (string.indexOf(UiUtils.SCENE_FLAG) == 0) {
						mList.add(new StringTag(string.replace(UiUtils.SCENE_FLAG, "")
								.trim(), UiUtils.SCENE_FLAG));
					} else if (string.indexOf(UiUtils.DEVICES_FLAG) == 0) {
						mList.add(new StringTag(string.replace(UiUtils.DEVICES_FLAG, "")
								.trim(), UiUtils.DEVICES_FLAG));
					}
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.main_fragment, null);
		initview();
		return mView;
	}

	private void initview() {
		// TODO Auto-generated method stub
		nodevices = (ViewGroup) mView.findViewById(R.id.nodevices);
		content_view = (GridView) mView.findViewById(R.id.content_view);
		mCustomeAdapter = new CustomeAdapter();
		mCustomeAdapter.setString(mList);
		content_view.setAdapter(mCustomeAdapter);
		content_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				StringTag st=mList.get(arg2);
				Intent i=new Intent();
				if(st.tag.equals(UiUtils.REGION_FLAG)){
					i.setClass((Context) getActivity(), RegionDevicesActivity.class);
					i.putExtra(RegionDevicesActivity.REGION_NAME, st.name.trim());
				}else if(st.tag.equals(UiUtils.SCENE_FLAG)){
					i.setClass((Context) getActivity(), SceneDevicesActivity.class);
					i.putExtra(SceneDevicesActivity.SCENE_NAME, st.name.trim());
				}else{
					return;
				}
				startActivity(i);
			}

		});
		
		content_view.setNumColumns(3);
		
		if (null == mList || mList.size() == 0) {
			nodevices.setVisibility(View.VISIBLE);
		} else {
			nodevices.setVisibility(View.GONE);
		}
	}

	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.i("onResume", "onResume");
		refreshFragment();
		super.onResume();
	}

	private class CustomeAdapter extends BaseAdapter {

		private List<StringTag> mString;

		public CustomeAdapter() {
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mString.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mString.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder mViewHolder;
			StringTag st=mString.get(position);
			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from((Context) getActivity())
						.inflate(R.layout.gridview_item, null);
				mViewHolder.funcImg = (ImageView) convertView
						.findViewById(R.id.func_img);
				mViewHolder.funcText = (TextView) convertView
						.findViewById(R.id.func_name);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			if(st.tag.equals(UiUtils.REGION_FLAG)){
				mViewHolder.funcImg.setImageResource(R.drawable.region);
			}else if(st.tag.equals(UiUtils.SCENE_FLAG)){
				mViewHolder.funcImg.setImageResource(R.drawable.scene);
			}else if(st.tag.equals(UiUtils.DEVICES_FLAG)){
				Context c=(Context)getActivity();
				String[] args={st.name};
				String where=" ieee=? ";
				List<SimpleDevicesModel> ls=DataUtil.getDevices(c,new DataHelper(c),args,where);
				SimpleDevicesModel smd = null;
				if(null!=ls && ls.size()>0){
					smd=ls.get(0);
				}
				if(null!=smd){
					if (DataHelper.IAS_ZONE_DEVICETYPE == smd.getmDeviceId()
							|| DataHelper.IAS_ACE_DEVICETYPE == smd
									.getmDeviceId()) {

						mViewHolder.funcImg.setImageResource(UiUtils
								.getDevicesSmallIconByModelId(smd.getmModelId()
										.trim()));
					} else if (smd.getmModelId().indexOf(
							DataHelper.Multi_key_remote_control) == 0) {
						mViewHolder.funcImg
								.setImageResource(UiUtils
										.getDevicesSmallIconForRemote(smd
												.getmDeviceId()));
					} else {
						mViewHolder.funcImg.setImageResource(UiUtils
								.getDevicesSmallIcon(smd.getmDeviceId()));
					}
				}else{
					mViewHolder.funcImg.setImageResource(R.drawable.scene);
				}
				mViewHolder.funcText.setText(smd.getmUserDefineName().trim());
			}
			if(!st.tag.equals(UiUtils.DEVICES_FLAG)){
				mViewHolder.funcText.setText(st.name.trim());
			}
			return convertView;
		}

		class ViewHolder {
			ImageView funcImg;
			TextView funcText;
		}

		public void setString(List<StringTag> s) {
			mString = null;
			mString = s;
		}
	}

	@Override
	public void refreshFragment() {
		// TODO Auto-generated method stub
		Log.i("zgs", "refreshFragment->refreshFragment");
		initData();
		if (null == mList || mList.size() == 0) {
			nodevices.setVisibility(View.VISIBLE);
		} else {
			nodevices.setVisibility(View.GONE);
		}
		Log.i("zgs", "refreshFragment->mregions.size()=" + mList.size());
		mCustomeAdapter.setString(mList);
		mCustomeAdapter.notifyDataSetChanged();
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub

	}

}
