package com.gdgl.adapter;

import java.util.List;

import com.gdgl.model.SwitchModel;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SwitchAdapter extends BaseAdapter {

	private List<SwitchModel> mList;
	private Context mContext;
	private int mCurrentId;
	private MyOkCancleDlg mDialog;
	
	gotoSwitchControlFragment mgotoSwitchControlFragment;
	
	public SwitchAdapter(Context context, List<SwitchModel> list,gotoSwitchControlFragment gs) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mList = list;
		mgotoSwitchControlFragment=gs;
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
		if (null != mList) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View mView = convertView;
		ViewHolder mHolder;
		final int index=position;
		final SwitchModel mSwitch = mList.get(position);
		if (null == convertView) {
			mView = LayoutInflater.from(mContext).inflate(
					R.layout.switch_list_item, null);
			mHolder = new ViewHolder();
			mHolder.img_switch1 = (ImageView) mView
					.findViewById(R.id.img_switch1);
			mHolder.img_switch2 = (ImageView) mView
					.findViewById(R.id.img_switch2);
			mHolder.img_switch3 = (ImageView) mView
					.findViewById(R.id.img_switch3);
			mHolder.switch_name = (TextView) mView
					.findViewById(R.id.switch_name);
			mHolder.switch_region = (TextView) mView
					.findViewById(R.id.switch_region);
			mHolder.delete = (Button) mView.findViewById(R.id.delete);

			mView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) mView.getTag();
		}
		initImag(mHolder, mSwitch.getState());

		mHolder.switch_name.setText(mList.get(position).getName());
		mHolder.switch_region.setText(mList.get(position).getRegion());

		mHolder.delete.setTag(position);
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
		
		mView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mgotoSwitchControlFragment.gotoSwitchControlFragment(mSwitch.getId(), mSwitch.getSwitchesState(),index);
			}
		});
		
		return mView;
	}
	
	private void initImag(ViewHolder mHolder, boolean[] state) {
		// TODO Auto-generated method stub
		int length = state.length;
		switch (length) {
		case 1:
			mHolder.img_switch1.setVisibility(View.VISIBLE);
			if (state[0]) {
				mHolder.img_switch1.setImageResource(R.drawable.switch_on);
			} else {
				mHolder.img_switch1.setImageResource(R.drawable.switch_off);
			}
			break;
		case 2:
			mHolder.img_switch1.setVisibility(View.VISIBLE);
			mHolder.img_switch2.setVisibility(View.VISIBLE);
			if (state[0]) {
				mHolder.img_switch1.setImageResource(R.drawable.switch_on);
			} else {
				mHolder.img_switch1.setImageResource(R.drawable.switch_off);
			}
			if (state[1]) {
				mHolder.img_switch2.setImageResource(R.drawable.switch_on);
			} else {
				mHolder.img_switch2.setImageResource(R.drawable.switch_off);
			}
			break;
		case 3:
			mHolder.img_switch1.setVisibility(View.VISIBLE);
			mHolder.img_switch2.setVisibility(View.VISIBLE);
			mHolder.img_switch3.setVisibility(View.VISIBLE);
			if (state[0]) {
				mHolder.img_switch1.setImageResource(R.drawable.switch_on);
			} else {
				mHolder.img_switch1.setImageResource(R.drawable.switch_off);
			}
			if (state[1]) {
				mHolder.img_switch2.setImageResource(R.drawable.switch_on);
			} else {
				mHolder.img_switch2.setImageResource(R.drawable.switch_off);
			}
			if (state[2]) {
				mHolder.img_switch3.setImageResource(R.drawable.switch_on);
			} else {
				mHolder.img_switch3.setImageResource(R.drawable.switch_off);
			}
			break;
		default:
			break;
		}
	}

	class ViewHolder {
		Button delete;
		ImageView img_switch1;
		ImageView img_switch2;
		ImageView img_switch3;
		TextView switch_name;
		TextView switch_region;
	}

	Dialogcallback dialogcallback = new Dialogcallback() {
		@Override
		public void dialogdo() {
			// TODO Auto-generated method stub
			mList.remove(mCurrentId);
			SwitchAdapter.this.notifyDataSetChanged();
		}
	};
	
	public interface gotoSwitchControlFragment{
		public void gotoSwitchControlFragment(int id,String mdata,int postion);
	}
	
}
