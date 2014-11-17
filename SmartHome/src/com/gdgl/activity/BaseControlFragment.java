package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.gdgl.activity.ShowDevicesGroupFragmentActivity.EditDevicesName;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Callback.CallbackResponseType2;
import com.gdgl.mydata.getlocalcielist.CIEresponse_params;

public abstract class BaseControlFragment extends Fragment implements
		EditDevicesName, UIListener {
	public UpdateDevice mUpdateDevice;
	public Dialog mDialog;
	public DataHelper mDh;

	public interface UpdateDevice {
		public void saveDevicesName(String name);

		/***
		 * 更新数据库
		 * @param sd
		 * @param c
		 * @return
		 */
//		public boolean updateDevices(SimpleDevicesModel sd, ContentValues c);
	}

	public CallbackManager mCallbackManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mCallbackManager = CallbackManager.getInstance();
		mCallbackManager.addObserver(this);
		mDh = new DataHelper((Context) getActivity());
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mCallbackManager.deleteObserver(this);
	}

	public void ProcessUpdate(CallbackResponseType2 cr,
			List<DevicesModel> OldList) {
		if(null==cr || null==OldList){
			return ;
		}
		DevicesModel ds = null;
		List<DevicesModel> mList;
		DataHelper mDh = new DataHelper((Context) getActivity());
		String where = " ieee=? and ep=? ";
		String[] args = {
				cr.getDeviceIeee() == null ? "" : cr.getDeviceIeee().trim(),
				cr.getDeviceEp() == null ? "" : cr.getDeviceEp().trim() };
		mList = mDh.queryForDevicesList(mDh.getSQLiteDatabase(),
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
		

		@Override
		protected Integer doInBackground(DevicesModel... params) {
			// TODO Auto-generated method stub
			int result = 0;
			mDevList = mDh.queryForDevicesList(mDh.getSQLiteDatabase(),
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
				long ll = mDh.insertDevice(mDh.getSQLiteDatabase(),
						DataHelper.DEVICES_TABLE, null, dm);
				result = (int) ll;
			}
			return result;
		}

	}
	
	public void updateDeviceStatus(ArrayList<CIEresponse_params> sd){
		new updateDeviceStatus().execute(sd);
	}
	
	
	public class updateDeviceStatus extends AsyncTask<ArrayList<CIEresponse_params>, Integer, Integer>{

		@Override
		protected Integer doInBackground(ArrayList<CIEresponse_params>... params) {
			// TODO Auto-generated method stub
//			List<SimpleDevicesModel> updateList=params[0];
//			if(null==updateList || updateList.size()==0){
//				return null;
//			}else{
//				String where=" ieee=? and ep=? ";
//				ContentValues v;
//				SQLiteDatabase db=mDh.getSQLiteDatabase();
//				for (SimpleDevicesModel simpleDevicesModel : updateList) {
//					String[] args={simpleDevicesModel.getmIeee().trim(),simpleDevicesModel.getmEP().trim()};
//					String status=simpleDevicesModel.getmOnOffStatus();
//					v=new ContentValues();
//					v.put(DevicesModel.ON_OFF_STATUS, status=="1"?"1":"0");
//					mDh.update(db, DataHelper.DEVICES_TABLE, v, where, args);
//				}
//				mDh.close(db);
//			}
			return null;
		}
		
	}
}
