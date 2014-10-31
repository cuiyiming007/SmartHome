package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.activity.SmartHome.refreshAdapter;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Region.Room;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

/***
 * 最外层区域菜单
 * @author Trice
 *
 */
public class RegionsFragment extends Fragment implements refreshAdapter,Dialogcallback,UIListener{

	GridView content_view;
	View mView;
	List<Room> mregions;
	CustomeAdapter mCustomeAdapter;
	ViewGroup nodevices;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		CGIManager.getInstance().addObserver(RegionsFragment.this);
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		mregions=new ArrayList<Room>();
		CGIManager.getInstance().GetAllRoomInfo();
		DataHelper mDateHelper = new DataHelper((Context)getActivity());
		SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
		mregions=mDateHelper.queryForRoomList(getActivity(), mSQLiteDatabase, DataHelper.ROOMINFO_TABLE, null, null, null, null, null, null, null);
		
//		GetRoomInfo_response mregion=null;
//		String reg=getFromSharedPreferences.getRegion();
//		if(null!=reg && !reg.trim().equals("")){
//			mregion=reg.split("@@");
//		}
//		if(null!=mregion){
//			for (String string : mregion) {
//	        	if(!string.equals("")){
//	        		mregions.add(string);
//	        	}
//			}
//		}
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
		mCustomeAdapter.setList(mregions);
		content_view.setAdapter(mCustomeAdapter);
		content_view.setLayoutAnimation(UiUtils.getAnimationController((Context)getActivity()));
		content_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i=new Intent((Context)getActivity(),RegionDevicesActivity.class);
				i.putExtra(RegionDevicesActivity.REGION_NAME, mregions.get(index).getroom_name());
				i.putExtra(RegionDevicesActivity.REGION_ID, mregions.get(index).getroom_id());
				startActivity(i);
			}

		});
		if(null==mregions || mregions.size()==0){
			nodevices.setVisibility(View.VISIBLE);
		}else{
			nodevices.setVisibility(View.GONE);
		}
//		registerForContextMenu(content_view);
	}

	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		content_view.setVisibility(View.GONE);
	}

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
//			mMyOkCancleDlg.setContent("确定要删除区域  "
//					+ mregions.get(position) + " 吗?");
//			mMyOkCancleDlg.show();
//		}
//		return super.onContextItemSelected(item);
//	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		content_view.setVisibility(View.VISIBLE);
		refreshFragment();
		super.onResume();
	}

	
	private class CustomeAdapter extends BaseAdapter{
		
		private List<Room> mRoomList;
		
		public CustomeAdapter(){}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mRoomList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mRoomList.get(position);
		}
		
		public String getItemName(int position) {
			return mRoomList.get(position).getroom_name();
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return mRoomList.get(position).getroom_id();
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
			mViewHolder.funcImg.setImageResource(R.drawable.ui_region_default_style);
			mViewHolder.funcText.setText(getItemName(position));
			return convertView;
		}
		
		class ViewHolder {
			ImageView funcImg;
			TextView funcText;
		}
		
		public void setList(List<Room> s){
			mRoomList=null;
			mRoomList=s;
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
		mCustomeAdapter.setList(mregions);
		content_view.setAdapter(mCustomeAdapter);
//		mCustomeAdapter.notifyDataSetChanged();
		content_view.setLayoutAnimation(UiUtils.getAnimationController((Context)getActivity()));
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
//		final Event event = (Event) object;
//		if (EventType.GETALLROOM == event.getType()) {
//			// data maybe null
//			if (event.isSuccess()) {
//				String string=(String)event.getData();
//				new GetAllRoomInfoTask().execute(string);
//			}
//		} else {
//			
//		}
//		
	}
	
	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		
	}
	
}