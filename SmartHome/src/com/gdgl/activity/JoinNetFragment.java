package com.gdgl.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.ResponseParamsEndPoint;
import com.gdgl.smarthome.R;
import com.gdgl.util.CircleProgressBar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JoinNetFragment extends Fragment implements UIListener {

	private View mView;

	RelativeLayout ch_pwd;

	CircleProgressBar cb;

	Button btn_scape, btn_close, btn_look;
	
	TextView text_result;
	
	private static final int SCAPE = 1;
	private static final int STOPE = 2;
	private static final int SCAPE_DEVICES = 3;
	
	private static final int SCAPE_TIME_DURING = 50;
	
	DeviceManager mDeviceManager;
	
	boolean isScape=false;
	
	ArrayList<ResponseParamsEndPoint> allList;
	
	List<DevicesModel> mDevList;
	
	List<SimpleDevicesModel> mList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.add_to_net, null);
		initView();
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		cb = (CircleProgressBar) mView.findViewById(R.id.seek_time);
		cb.setText("扫描完毕");
		ch_pwd = (RelativeLayout) mView.findViewById(R.id.ch_pwd);
		RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		ch_pwd.setLayoutParams(mLayoutParams);
		
		text_result=(TextView)mView.findViewById(R.id.text_result);
		
		
		mDeviceManager=DeviceManager.getInstance();
		mDeviceManager.addObserver(JoinNetFragment.this);
		
		btn_scape = (Button) mView.findViewById(R.id.scape);
		btn_close = (Button) mView.findViewById(R.id.close);
		btn_look = (Button) mView.findViewById(R.id.look);

		btn_scape.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isScape){
					isScape=true;
					text_result.setText("正在扫描...");
					text_result.setVisibility(View.VISIBLE);
					Message msg = Message.obtain();
					msg.what = SCAPE;
					msg.arg1 = 49;
					mHandler.sendMessageDelayed(msg, 1000);
					mHandler.sendEmptyMessage(SCAPE_DEVICES);
				}
			}
		});
		
		btn_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(STOPE);
			}
		});
		
//		btn_look.setEnabled(false);
		btn_look.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeFragment c=(ChangeFragment)getActivity();
				JoinNetDevicesListFragment mJoinNetDevicesListFragment=new JoinNetDevicesListFragment();
				if(null!=mDevList && mDevList.size()>0){
					mJoinNetDevicesListFragment.setList(mDevList);
				}
				c.setFragment(mJoinNetDevicesListFragment);
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case SCAPE:
				int second = msg.arg1;
				if (second >= 0) {
					cb.setProgress(second);
					Message msg1 = Message.obtain();
					msg1.what = SCAPE;
					msg1.arg1 = second - 1;
					mHandler.sendMessageDelayed(msg1, 1000);
					if(second % SCAPE_TIME_DURING==0){
						if(second!=0){
							mHandler.sendEmptyMessage(SCAPE_DEVICES);
						}
					}
				} else {
					isScape=false;
					text_result.setText("未扫描到任何设备");
					text_result.setVisibility(View.VISIBLE);
//					btn_look.setEnabled(true);
					mHandler.sendEmptyMessageDelayed(STOPE, 1000);
				}

				break;
			case STOPE:
				isScape=false;
				text_result.setVisibility(View.INVISIBLE);
				if (mHandler.hasMessages(SCAPE)) {
					mHandler.removeMessages(SCAPE);
				}
				if (mHandler.hasMessages(SCAPE_DEVICES)) {
					mHandler.removeMessages(SCAPE_DEVICES);
				}
				getData();
				break;
			case SCAPE_DEVICES:
				Log.i("", "zgs->begin scape");
				mDeviceManager.getDeviceList();
				break;
			default:
				break;
			}

		}
	};
	
	private void getData() {
		// TODO Auto-generated method stub
		if(null!=allList && allList.size()>0){
			mDevList=DataHelper.convertToDevicesModel(allList);
		}
		if(null!=mDevList && mDevList.size()>0){
			mList=new ArrayList<SimpleDevicesModel>();
			Log.i("zgs", "zzz->in getData() mList is not null");
			for (DevicesModel dm : mDevList) {
				SimpleDevicesModel sd=new SimpleDevicesModel();
				sd.setID(dm.getID());
				sd.setmDeviceId(Integer.parseInt(dm.getmDeviceId()));
				sd.setmDeviceRegion(dm.getmDeviceRegion());
				sd.setmEP(dm.getmEP());
				sd.setmIeee(dm.getmIeee());
				sd.setmLastDateTime(System.currentTimeMillis());
				sd.setmModelId(dm.getmModelId());
				sd.setmName(dm.getmName());
				sd.setmNodeENNAme(dm.getmNodeENNAme());
				sd.setmNWKAddr(dm.getmNWKAddr());
				sd.setmOnOffLine(dm.getmOnOffLine());
				sd.setmOnOffStatus(dm.getmOnOffStatus());
				sd.setmUserDefineName(dm.getmUserDefineName());
				
				mList.add(sd);
			}
		}
	};
	
	public interface ChangeFragment{
		public void setFragment(Fragment f);
	}
	
	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		Log.i("zzz", "zgs-> update EventType.INTITIALDVIVCEDATA == event.getType()="+(EventType.INTITIALDVIVCEDATA == event.getType()));
		if (EventType.INTITIALDVIVCEDATA == event.getType()) {
			ArrayList<ResponseParamsEndPoint> devDataList=(ArrayList<ResponseParamsEndPoint>)event.getData();
			if(null==allList || allList.size()==0){
				allList=devDataList;
			}else{
				for (ResponseParamsEndPoint responseParamsEndPoint : devDataList) {
					if(!allList.contains(responseParamsEndPoint)){
						allList.add(responseParamsEndPoint);
					}
				}
			}
		}
	}

}
