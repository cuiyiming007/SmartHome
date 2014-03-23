package com.gdgl.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.model.lights;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class lights_adapter extends BaseAdapter {

	private static final String TAG = "lights_adapter";

	private static final int STATE_CHANGE_SUCESS = 1;
	private static final int STATE_CHANGE_FAILED = 2;
	private Context mContext;
	double mLevel = -1.0;
	MyOkCancleDlg mDialog;
	ImageView mImag;
	int mCurrentId = 0;
	CheckBox light_on_off;
	List<lights> mList = new ArrayList<lights>();
	
	SeekBar mSeekBar;
	
	int viewType = 0;

	static final int NORMAL = 0;
	static final int SEEK = 1;

	public lights_adapter(Context context, List<lights> list) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (null != mList) {
			Log.i(TAG, "zgs->getCount()" + mList.size());
			return mList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		Log.i(TAG, "zgs->getItemId()" + arg0);
		return arg0;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		mLevel = mList.get(position).getLevel();
		if (-1.0 == mLevel) {
			return NORMAL;
		} else if (-1.0 != mLevel) {
			return SEEK;
		}
		return SEEK;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Log.i("lights_adapter", "zgs->arg0=" + arg0);
		View mView = arg1;
		ViewHolder mHolder;
		lights mLights = mList.get(arg0);
		mLevel = mLights.getLevel();
		viewType = getItemViewType(arg0);
		if (null == mView) {
			mHolder = new ViewHolder();
			switch (viewType) {
			case SEEK:
				mView = LayoutInflater.from(mContext).inflate(
						R.layout.seek_light_item, null);
				mHolder.text_level = (TextView) mView
						.findViewById(R.id.text_level);
				mHolder.delete = (Button) mView.findViewById(R.id.delete);
				mHolder.light_name = (TextView) mView
						.findViewById(R.id.light_name);
				mHolder.light_region = (TextView) mView
						.findViewById(R.id.light_region);
				mHolder.seekB=(SeekBar)mView
						.findViewById(R.id.light_level);
				break;
			case NORMAL:
				mView = LayoutInflater.from(mContext).inflate(
						R.layout.light_manager_item, null);
				mHolder.light_on_off = (CheckBox) mView
						.findViewById(R.id.light_on_off);
				mHolder.delete = (Button) mView.findViewById(R.id.delete);
				mHolder.light_name = (TextView) mView
						.findViewById(R.id.light_name);
				mHolder.light_region = (TextView) mView
						.findViewById(R.id.light_region);
				break;
			default:
				break;
			}
			mView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) mView.getTag();
		}

		mHolder.delete.setTag(arg0);
		mHolder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mCurrentId = Integer.parseInt(v.getTag().toString());
				mDialog = new MyOkCancleDlg(mContext);
				mDialog.setContent("确定要删除" + mList.get(mCurrentId).getName()
						+ "吗?");
				mDialog.setDialogCallback(dialogcallback);
				mDialog.show();

			}
		});
		mHolder.light_name.setText(mLights.getName());
		mHolder.light_region.setText(mLights.getRegion());
		switch (viewType) {
		case NORMAL:
			mHolder.light_on_off.setChecked(mList.get(arg0).getState());
			mHolder.light_on_off.setOnClickListener(new myCheckBoxListener(
					mImag, arg0));
			break;
		case SEEK:
			mHolder.seekB.setProgress((int)(mLevel*100));
			mHolder.text_level.setText((int)(mLevel*100)+"%");
			myOnSeekBarChangeListener mSeekBarListener=new myOnSeekBarChangeListener(mHolder.text_level);
			mHolder.seekB.setOnSeekBarChangeListener(mSeekBarListener);
		default:
			break;

		}
		return mView;
	}

	public class myCheckBoxListener implements OnClickListener {
		private ImageView mImagView;

		private int mId;

		public myCheckBoxListener(ImageView mImag, int arg0) {
			// TODO Auto-generated constructor stub
			mImagView = mImag;
			mId = arg0;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			v.setVisibility(View.INVISIBLE);
			boolean b = mList.get(mId).getState();
			mList.get(mId).setOnOrOff(!b);
			Message msg = myHandler.obtainMessage();
			msg.what = STATE_CHANGE_SUCESS;
			msg.obj = v;
			myHandler.sendMessageDelayed(msg, 1000);
		}
	}
	
	class myOnSeekBarChangeListener implements OnSeekBarChangeListener
	{
		TextView mtextView;
		public myOnSeekBarChangeListener(TextView textView)
		{
			mtextView=textView;
		}
		
		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
			mtextView.setText(arg1+"%");
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STATE_CHANGE_SUCESS:
				View v = (View) msg.obj;
				v.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	Dialogcallback dialogcallback = new Dialogcallback() {
		@Override
		public void dialogdo() {
			// TODO Auto-generated method stub
			mList.remove(mCurrentId);
			lights_adapter.this.notifyDataSetChanged();
		}
	};

	class ViewHolder {
		CheckBox light_on_off;
		Button delete;
		TextView light_name;
		TextView light_region;
		TextView text_level;
		SeekBar seekB;
	}

}
