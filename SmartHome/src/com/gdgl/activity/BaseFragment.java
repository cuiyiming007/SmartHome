package com.gdgl.activity;


import java.util.List;

import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Callback.CallbackResponseType2;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

public abstract class BaseFragment extends Fragment implements UIListener{
	
	public abstract void stopRefresh();
	
	public CallbackManager mCallbackManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mCallbackManager = CallbackManager.getInstance();
		mCallbackManager.addObserver(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mCallbackManager.deleteObserver(this);
	}

	public void ProcessUpdate(CallbackResponseType2 cr) {
		if(null==cr ){
			return ;
		}
		DevicesModel ds = null;
		List<DevicesModel> mList;
		DataHelper mDh = new DataHelper((Context) getActivity());
		String where = " ieee=? and ep=? ";
		String[] args = {
				cr.getDeviceIeee() == null ? "" : cr.getDeviceIeee().trim(),
				cr.getDeviceEp() == null ? "" : cr.getDeviceEp().trim() };
		mList = mDh.queryForList(mDh.getSQLiteDatabase(),
				DataHelper.DEVICES_TABLE, null, where, args, null, null, null,
				null);
		if(null!=mList && mList.size()>0){
			ds=mList.get(0);
		}
		if(null!=ds){
			new UpdateDatabaseTask().execute(ds);
		}
	}

	public boolean isInList(DevicesModel ds, List<DevicesModel> list) {
		boolean b = false;
		for (DevicesModel devicesModel : list) {
			if (ds.getmIeee().trim().equals(devicesModel.getmIeee().trim())
					&& ds.getmEP().trim().equals(devicesModel.getmEP())) {
				b = true;
			}
		}
		return b;
	}

	public class UpdateDatabaseTask extends
			AsyncTask<DevicesModel, Void, Integer> {
		private List<DevicesModel> mDevList;
		DataHelper mDh;

		@Override
		protected Integer doInBackground(DevicesModel... params) {
			// TODO Auto-generated method stub
			int result = 0;
			mDh = new DataHelper((Context) getActivity());
			mDevList = mDh.queryForList(mDh.getSQLiteDatabase(),
					DataHelper.DEVICES_TABLE, null, null, null, null, null,
					null, null);
			DevicesModel dm = params[0];

			if (isInList(dm, mDevList)) {
				ContentValues v = new ContentValues();
				String Where = " ieee=? and ep=? ";
				String[] args = {
						dm.getmIeee() == null ? "" : dm.getmIeee().trim(),
						dm.getmEP() == null ? "" : dm.getmEP().trim() };
				v.put(DevicesModel.ON_OFF_STATUS,
						dm.getmOnOffStatus() == null ? "0" : dm
								.getmOnOffStatus().trim());
				result = mDh.update(mDh.getSQLiteDatabase(),
						DataHelper.DEVICES_TABLE, v, Where, args);
			} else {
				long ll = mDh.insert(mDh.getSQLiteDatabase(),
						DataHelper.DEVICES_TABLE, null, dm);
				result = (int) ll;
			}
			return result;
		}

	}
}
