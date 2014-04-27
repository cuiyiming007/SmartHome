package com.gdgl.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.gdgl.smarthome.R;

public class VideoFragment extends Fragment {
	GridView content_view;
	View mView;
	ViewGroup nodevices;
	ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();

	String[] listItemName = { "通道1", "通道2", "通道3", "通道4", "通道5" };
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

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
		nodevices.setVisibility(View.GONE);
		content_view = (GridView) mView.findViewById(R.id.content_view);
		content_view.setAdapter(new CustomeAdapter());
		content_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent((Context)getActivity(),
						VideoViewActivity.class);
				intent.putExtra("ipc_channel", arg2);
				startActivity(intent);
			}

		});
	}
	
	private class CustomeAdapter extends BaseAdapter{
		
		public CustomeAdapter(){}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItemName.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listItemName[position];
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
			mViewHolder.funcText.setText(listItemName[position]);
			return convertView;
		}
		
		class ViewHolder {
			ImageView funcImg;
			TextView funcText;
		}
		
	}
}
