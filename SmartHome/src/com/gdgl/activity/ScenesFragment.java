package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.activity.SmartHome.refreshAdapter;
import com.gdgl.mydata.DataUtil;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;

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

public class ScenesFragment extends Fragment implements refreshAdapter{

	GridView content_view;
	View mView;
	List<String> mScenes;
	CustomeAdapter mCustomeAdapter;
	
	ViewGroup nodevices;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		Log.i("initData", "initData");
		mScenes=new ArrayList<String>();
		
		mScenes=DataUtil.getScenes((Context)getActivity(), null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.devices_main_fragment, null);
		initview();
		return mView;
	}

	private void initview() {
		// TODO Auto-generated method stub
		nodevices=(ViewGroup)mView.findViewById(R.id.nodevices);
		content_view = (GridView) mView.findViewById(R.id.content_view);
		mCustomeAdapter=new CustomeAdapter();
		mCustomeAdapter.setString(mScenes);
		content_view.setAdapter(mCustomeAdapter);
		content_view.setLayoutAnimation(UiUtils.getAnimationController((Context)getActivity()));
		content_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i=new Intent((Context)getActivity(),SceneDevicesActivity.class);
				i.putExtra(SceneDevicesActivity.SCENE_NAME, mScenes.get(arg2).trim());
				startActivity(i);
			}

		});
		if(null==mScenes || mScenes.size()==0){
			nodevices.setVisibility(View.VISIBLE);
		}else{
			nodevices.setVisibility(View.GONE);
		}
//		registerForContextMenu(content_view);
	}
	
	
	
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		// TODO Auto-generated method stub
//		menu.setHeaderTitle("删除");
//		menu.add(0, 1, 0, "删除");
//		super.onCreateContextMenu(menu, v, menuInfo);
//	}
//
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
//				.getMenuInfo();
//		int position = info.position;
//		int menuIndex = item.getItemId();
//		if (1 == menuIndex) {
//			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
//					(Context)getActivity());
//			mMyOkCancleDlg.setDialogCallback(this);
//			mMyOkCancleDlg.setContent("确定要删除场景  "
//					+ mScenes.get(position) + " 吗?");
//			mMyOkCancleDlg.show();
//		}
//		return super.onContextItemSelected(item);
//	}
	
	
	private class CustomeAdapter extends BaseAdapter{
		
		private List<String> mString;
		
		public CustomeAdapter(){}
		
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
			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from((Context)getActivity()).inflate(
						R.layout.gridview_item, null);
				mViewHolder.funcImg = (ImageView) convertView
						.findViewById(R.id.func_img);
				mViewHolder.funcText = (TextView) convertView
						.findViewById(R.id.func_name);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			mViewHolder.funcImg.setImageResource(R.drawable.scene);
			mViewHolder.funcText.setText(mString.get(position));
			return convertView;
		}
		
		class ViewHolder {
			ImageView funcImg;
			TextView funcText;
		}
		
		public void setString(List<String> s){
			mString=null;
			mString=s;
		}
	}
	
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		content_view.setVisibility(View.GONE);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		content_view.setVisibility(View.VISIBLE);
		initData();
		if(null==mScenes || mScenes.size()==0){
			nodevices.setVisibility(View.VISIBLE);
		}else{
			nodevices.setVisibility(View.GONE);
		}
		mCustomeAdapter=new CustomeAdapter();
		mCustomeAdapter.setString(mScenes);
		content_view.setAdapter(mCustomeAdapter);
		content_view.setLayoutAnimation(UiUtils.getAnimationController((Context)getActivity()));
		super.onResume();
	}

	@Override
	public void refreshFragment() {
		// TODO Auto-generated method stub
		Log.i("zgs", "refreshFragment->refreshFragment");
		initData();
		if(null==mScenes || mScenes.size()==0){
			nodevices.setVisibility(View.VISIBLE);
		}else{
			nodevices.setVisibility(View.GONE);
		}
		Log.i("zgs", "refreshFragment->mregions.size()="+mScenes.size());
		mCustomeAdapter.setString(mScenes);
		mCustomeAdapter.notifyDataSetChanged();
	}
}
