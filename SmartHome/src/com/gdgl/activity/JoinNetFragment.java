package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.manager.DeviceManager;
import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.AddDlg.AddDialogcallback;
import com.gdgl.util.CircleProgressBar;
import com.gdgl.util.JoinNetTimeDlg;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/***
 * 组网管理界面
 * 
 * @author Trice
 * 
 */
public class JoinNetFragment extends Fragment implements UIListener,
		AddDialogcallback {

	private View mView;

	RelativeLayout join_netLayout;

	CircleProgressBar circleProgressBar;

	Button btn_scape, btn_close, btn_look;

	TextView text_result;
	/***
	 * 入网扫描时间
	 */
	int JoinNetTimer;

	private static final int SCAPE = 1;
	private static final int STOP = 2;
	

	DeviceManager mDeviceManager;
	
	/***
	 * 扫描到的设备
	 */
	List<DevicesModel> scapedDeviveList;


	DataHelper mDH;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDH = new DataHelper((Context) getActivity());
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
		getFromSharedPreferences.setsharedPreferences((Context) getActivity());
		if (!getFromSharedPreferences.getJoinNetTime().equals("")) {
			JoinNetTimer = Integer.valueOf(getFromSharedPreferences
					.getJoinNetTime());
		} else {
			JoinNetTimer = 60;
		}
		scapedDeviveList = new ArrayList<DevicesModel>();
		circleProgressBar = (CircleProgressBar) mView
				.findViewById(R.id.seek_time);
		circleProgressBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JoinNetTimeDlg jTimeDlg = new JoinNetTimeDlg(
						(Context) getActivity());
				jTimeDlg.setTitle("设置入网扫描时间");
				jTimeDlg.setName("扫描时间(s)");
				jTimeDlg.setDialogCallback(JoinNetFragment.this);
				jTimeDlg.show();
			}
		});
		circleProgressBar.setText("扫描完毕");
		circleProgressBar.setMaxProgress(JoinNetTimer);
		join_netLayout = (RelativeLayout) mView.findViewById(R.id.add_to_net);
		RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		join_netLayout.setLayoutParams(mLayoutParams);

		text_result = (TextView) mView.findViewById(R.id.text_result);

		mDeviceManager = DeviceManager.getInstance();
		mDeviceManager.addObserver(JoinNetFragment.this);
		CGIManager.getInstance().addObserver(this);

		btn_scape = (Button) mView.findViewById(R.id.scape);
		btn_close = (Button) mView.findViewById(R.id.close);
		btn_look = (Button) mView.findViewById(R.id.look);
		btn_look.setEnabled(false);
		// btn_look.setBackgroundColor(Color.DKGRAY);

		btn_scape.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_scape.setEnabled(false);
				scapedDeviveList.clear();
				CGIManager.getInstance().setAllPermitJoinOn(JoinNetTimer);
				text_result.setText("正在扫描...");
				text_result.setVisibility(View.VISIBLE);
				Message msg = Message.obtain();
				msg.what = SCAPE;
				msg.arg1 = JoinNetTimer - 1;
				mHandler.sendMessageDelayed(msg, 1000);
				// mHandler.sendEmptyMessage(SCAPE_DEVICES);
			}
		});

		btn_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(STOP);
				CGIManager.getInstance().setAllPermitJoinOn(0);
				circleProgressBar.setProgress(JoinNetTimer);
			}
		});

		// btn_look.setEnabled(false);
		btn_look.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeFragment c = (ChangeFragment) getActivity();
				JoinNetDevicesListFragment mJoinNetDevicesListFragment = new JoinNetDevicesListFragment();
				if (null != scapedDeviveList && scapedDeviveList.size() > 0) {
					mJoinNetDevicesListFragment.setList(scapedDeviveList);
				}
				c.setFragment((Fragment)mJoinNetDevicesListFragment);
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mDeviceManager.deleteObserver(JoinNetFragment.this);
		CGIManager.getInstance().deleteObserver(this);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case SCAPE:
				int second = msg.arg1;
				if (second >= 0) {
					circleProgressBar.setProgress(second);
					Message msg1 = Message.obtain();
					msg1.what = SCAPE;
					msg1.arg1 = second - 1;
					mHandler.sendMessageDelayed(msg1, 1000);
				} else {
					mHandler.sendEmptyMessage(STOP);
				}

				break;
			case STOP:
				btn_scape.setEnabled(true);
				if (mHandler.hasMessages(SCAPE)) {
					mHandler.removeMessages(SCAPE);
				}
				if (scapedDeviveList.size() <= 0) {
					text_result.setText("未扫描到任何设备");
					text_result.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
		}

	};

	private void updateScapeSuccessful(ArrayList<DevicesModel> newscapedList) {
		for (DevicesModel devicesModel : newscapedList) {
			scapedDeviveList.add(devicesModel);
		}
		text_result.setText("扫描到" + scapedDeviveList.size() + "个设备");
		text_result.setVisibility(View.VISIBLE);
		btn_look.setEnabled(true);
//		new InsertTask().execute(newscapedList);
	}

//	public class InsertTask extends
//			AsyncTask<List<DevicesModel>, Integer, Integer> {
//
//		@Override
//		protected Integer doInBackground(List<DevicesModel>... params) {
//			// TODO Auto-generated method stub
//			List<DevicesModel> list = params[0];
//			if (null == list || list.size() == 0) {
//				return 1;
//			}
//			mDH.insertDevList(mDH.getSQLiteDatabase(),
//					DataHelper.DEVICES_TABLE, null, list);
//			return 1;
//		}
//
//	}

	public interface ChangeFragment {
		public void setFragment(Fragment f);
	}

	@Override
	public void refreshdata() {
		// TODO Auto-generated method stub
		JoinNetTimer=Integer.valueOf(getFromSharedPreferences
				.getJoinNetTime());
		circleProgressBar.setMaxProgress(JoinNetTimer);
		circleProgressBar.setProgress(JoinNetTimer);
		text_result.setVisibility(View.INVISIBLE);
		btn_look.setEnabled(false);
		scapedDeviveList.clear();
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		
		if (EventType.SCAPEDDEVICE == event.getType()) {
			if (event.isSuccess()) {
				ArrayList<DevicesModel> scapedList = (ArrayList<DevicesModel>) event
						.getData();
				updateScapeSuccessful(scapedList);
			}
		} else if (EventType.SETPERMITJOINON == event.getType()) {
			if (!event.isSuccess()) {
				Toast.makeText(getActivity(), "打开组网设备失败！", Toast.LENGTH_SHORT)
						.show();
				mHandler.sendEmptyMessage(STOP);
				circleProgressBar.setProgress(60);
			}
		}
	}

}
