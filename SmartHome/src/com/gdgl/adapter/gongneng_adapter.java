package com.gdgl.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.gdgl.smarthome.R;
import com.gdgl.util.UtilString;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class gongneng_adapter extends BaseAdapter {

	private Context mContext;

	private String[] mString;

	ImageView img;
	TextView text;

	List<myInfor> mList=new ArrayList<myInfor>();

	public gongneng_adapter(Context context) {
		mContext = context;
		mString = mContext.getResources().getStringArray(R.array.gongneng_name);
		myInfor m;
		for(int i=0;i<mString.length;i++)
		{
			m=new myInfor(UtilString.GONENG_ICON[i], mString[i]);
			mList.add(m);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();

	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View mView = arg1;
		if (null == arg1) {
			mView = LayoutInflater.from(mContext).inflate(
					R.layout.gongneng_item, null);
		}
		img = (ImageView) mView.findViewById(R.id.goneng_imag);
		text = (TextView) mView.findViewById(R.id.goneng_text);
		img.setImageResource(UtilString.GONENG_ICON[arg0]);
		text.setText(mString[arg0]);
		return mView;
	}

  class myInfor {
		private int mImag;
		private String mText;

		public myInfor(int imagId, String textId) {
			mImag = imagId;
			mText = textId;
		}

		public int getImag() {
			return mImag;
		};

		public String getText() {
			return mText;
		};

	}
}
