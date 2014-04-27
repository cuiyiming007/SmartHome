package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.activity.SmartHome.refreshAdapter;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class RegionsFragment extends Fragment implements refreshAdapter,Dialogcallback{

	GridView content_view;
	View mView;
	List<String> mregions;
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
		mregions=new ArrayList<String>();
		getFromSharedPreferences.setharedPreferences((Context)getActivity());
		String[] mregion = null;
		String reg=getFromSharedPreferences.getRegion();
		if(null!=reg && !reg.trim().equals("")){
			mregion=reg.split("@@");
		}
		if(null!=mregion){
			for (String string : mregion) {
	        	if(!string.equals("")){
	        		mregions.add(string);
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
		nodevices=(ViewGroup)mView.findViewById(R.id.nodevices);
		content_view = (GridView) mView.findViewById(R.id.content_view);
		mCustomeAdapter=new CustomeAdapter();
		mCustomeAdapter.setString(mregions);
		content_view.setAdapter(mCustomeAdapter);
		content_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i=new Intent((Context)getActivity(),RegionDevicesActivity.class);
				i.putExtra(RegionDevicesActivity.REGION_NAME, mregions.get(arg2).trim());
				startActivity(i);
			}

		});
		if(null==mregions || mregions.size()==0){
			nodevices.setVisibility(View.VISIBLE);
		}else{
			nodevices.setVisibility(View.GONE);
		}
		registerForContextMenu(content_view);
	}
	
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.setHeaderTitle("删除");
		menu.add(0, 1, 0, "删除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		int menuIndex = item.getItemId();
		if (1 == menuIndex) {
			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
					(Context)getActivity());
			mMyOkCancleDlg.setDialogCallback(this);
			mMyOkCancleDlg.setContent("确定要删除区域  "
					+ mregions.get(position) + " 吗?");
			mMyOkCancleDlg.show();
		}
		return super.onContextItemSelected(item);
	}
	
	
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
			mViewHolder.funcImg.setImageResource(R.drawable.video3);
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
	public void refreshFragment() {
		// TODO Auto-generated method stub
		Log.i("zgs", "refreshFragment->refreshFragment");
		initData();
		if(null==mregions || mregions.size()==0){
			nodevices.setVisibility(View.VISIBLE);
		}else{
			nodevices.setVisibility(View.GONE);
		}
		Log.i("zgs", "refreshFragment->mregions.size()="+mregions.size());
		mCustomeAdapter.setString(mregions);
		mCustomeAdapter.notifyDataSetChanged();
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		
	}

}
