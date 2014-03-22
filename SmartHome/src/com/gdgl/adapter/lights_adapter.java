package com.gdgl.adapter;

import com.gdgl.smarthome.R;
import com.gdgl.util.MyProcessDlg;
import com.gdgl.util.UtilString;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class lights_adapter extends BaseAdapter {

	private Context mContext;

	public lights_adapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 15;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View mView = arg1;
		if (null == arg1) {
			mView = LayoutInflater.from(mContext).inflate(
					R.layout.light_manager_item, null);

			TextView text = (TextView) mView.findViewById(R.id.light_name);
			text.setText("Light" + arg0);
			ImageView delete=(ImageView)mView.findViewById(R.id.delete);
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Dialog myD=MyProcessDlg.createLoadingDialog(mContext, "正在删除");
					myD.show();
					ImageView myImag=(ImageView)v;
					myImag.setImageResource(R.drawable.actionbar_add_icon);
				}
			});
		}

		return mView;
	}

}
