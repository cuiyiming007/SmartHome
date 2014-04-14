package com.gdgl.adapter;

import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridviewAdapter extends BaseAdapter {
	int[] images;
	String[] tags;

	int mType;
	Context mContext;

	public GridviewAdapter(int type, Context c) {
		mType = type;
		mContext = c;
		initres();
	}

	private void initres() {
		// TODO Auto-generated method stub
		images = UiUtils.getImagResource(mType);
		tags = UiUtils.getTagResource(mType);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (null != images) {
			return images.length;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (null != images) {
			return images[position];
		}
		return null;
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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.gridview_item, null);
			mViewHolder.funcImg = (ImageView) convertView
					.findViewById(R.id.func_img);
			mViewHolder.funcText = (TextView) convertView
					.findViewById(R.id.func_name);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		mViewHolder.funcImg.setImageResource(images[position]);
		mViewHolder.funcText.setText(tags[position]);
		return convertView;
	}

	class ViewHolder {
		ImageView funcImg;
		TextView funcText;
	}
}
