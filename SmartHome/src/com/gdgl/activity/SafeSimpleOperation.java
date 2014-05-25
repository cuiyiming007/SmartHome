
package com.gdgl.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdgl.activity.BaseControlFragment.UpdateDevice;
import com.gdgl.manager.LightManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.SimpleResponseData;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyDlg;
import com.gdgl.util.UiUtils;

public class SafeSimpleOperation extends Fragment implements UIListener {


	View mView;
	ImageView on_off_img;

	boolean is_on_operation = false;

	Button btn_on,btn_off;
	Animation loadAnim;
	LightManager mLightManager;
	
	TextView load_message;
	
	List<SimpleDevicesModel> mList;
	DataHelper mDh;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		if (!(activity instanceof UpdateDevice)) {
			throw new IllegalStateException("Activity必须实现SaveDevicesName接口");
		}
		super.onAttach(activity);
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub

		Context c = (Context) getActivity();

		mLightManager = LightManager.getInstance();
		mLightManager.addObserver(SafeSimpleOperation.this);

		mDh = new DataHelper(c);
		new getDataTask().execute(1);
	}

	public class getDataTask extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			SQLiteDatabase db = mDh.getSQLiteDatabase();
			mList = DataUtil.getOtherManagementDevices((Context) getActivity(),
					mDh, db, UiUtils.SECURITY_CONTROL);
			mDh.close(db);
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub

			initView();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.simple_operation, null);
		return mView;
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			Button btn = (Button) msg.obj;
			switch (what) {
			case 1:
				if (mHandler.hasMessages(2)) {
					mHandler.removeMessages(2);
				}
				on_off_img.clearAnimation();
				on_off_img.setVisibility(View.INVISIBLE);
				load_message.setVisibility(View.INVISIBLE);
				btn_on.setEnabled(true);
				btn_off.setEnabled(true);
				break;
			case 2:
				if (mHandler.hasMessages(1)) {
					mHandler.removeMessages(1);
				}
				on_off_img.clearAnimation();
				on_off_img.setVisibility(View.INVISIBLE);
				load_message.setVisibility(View.INVISIBLE);
				btn_on.setEnabled(true);
				btn_off.setEnabled(true);
				break;
			default:
				break;
			}
		};
	};

	private void initView() {
		// TODO Auto-generated method stub
		on_off_img = (ImageView) mView.findViewById(R.id.load_img);
		btn_on = (Button) mView.findViewById(R.id.btn_on);
		btn_off = (Button) mView.findViewById(R.id.btn_off);
		load_message = (TextView) mView.findViewById(R.id.load_message);

		loadAnim = AnimationUtils.loadAnimation((Context) getActivity(),
				R.anim.loading_animation);

		btn_on.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLightManager.LocalIASCIEOperation(null, 6);
				on_off_img.setVisibility(View.VISIBLE);
				on_off_img.startAnimation(loadAnim);
				load_message.setVisibility(View.VISIBLE);
				load_message.setText("开启中...");
				btn_on.setEnabled(false);
				btn_off.setEnabled(false);
				mHandler.sendEmptyMessageDelayed(1, 3000);
			}
		});
		
		btn_off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLightManager.LocalIASCIEOperation(null, 7);
				on_off_img.setVisibility(View.VISIBLE);
				on_off_img.startAnimation(loadAnim);
				load_message.setVisibility(View.VISIBLE);
				load_message.setText("关闭中...");
				btn_on.setEnabled(false);
				btn_off.setEnabled(false);
				mHandler.sendEmptyMessageDelayed(2, 3000);
			}
		});

	}

	public class UpdateSafeControlDevices extends
			AsyncTask<Boolean, Integer, Integer> {

		@Override
		protected Integer doInBackground(Boolean... params) {
			// TODO Auto-generated method stub
			boolean on = params[0];
			String on_off = on ? "1" : "0";
			for (SimpleDevicesModel sdm : mList) {
				sdm.setmOnOffStatus(on_off);
			}
			String where = " ieee=? and ep=? ";
			SQLiteDatabase db = mDh.getSQLiteDatabase();
			ContentValues cv;
			for (SimpleDevicesModel sdm : mList) {
				cv = new ContentValues();
				String[] args = { sdm.getmIeee(), sdm.getmEP() };
				cv.put(DevicesModel.ON_OFF_STATUS, on_off);
				mDh.update(db, DataHelper.DEVICES_TABLE, cv, where, args);
			}
			mDh.close(db);
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			on_off_img.clearAnimation();
			on_off_img.setVisibility(View.INVISIBLE);
			load_message.setVisibility(View.INVISIBLE);
			btn_on.setEnabled(true);
			btn_off.setEnabled(true);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLightManager.deleteObserver(SafeSimpleOperation.this);
	}



	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
	
		final Event event = (Event) object;
		if (EventType.LOCALIASCIEOPERATION == event.getType()) {
			if (event.isSuccess() == true) {
				int data = (Integer) event.getData();
				if (data == 6) {
					new UpdateSafeControlDevices().execute(true);
				} else if (data == 7) {
					new UpdateSafeControlDevices().execute(false);
				}
			}
		}
	}

	class operatortype {
		/***
		 * 获取设备类型
		 */
		public static final int TURNON = 0;
		/***
		 * 获取状态
		 */
		public static final int TURNOFF = 1;
		/***
		 * 当操作类型是2时，para1有以下意义 Param1: switchaction: 0x00: Off 0x01: On 0x02:
		 * Toggle
		 */
		public static final int TOGGLE = 2;
		public static final int GETSTATUS = 3;
	}

}
