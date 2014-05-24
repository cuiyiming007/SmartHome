package com.gdgl.activity;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.activity.BindControlFragment.backAction;
import com.gdgl.activity.BindControlFragment.updateList;
import com.gdgl.activity.JoinNetDevicesListFragment.JoinNetAdapter.ViewHolder;
import com.gdgl.activity.JoinNetFragment.ChangeFragment;
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
import com.gdgl.mydata.binding.BindingDataEntity;
import com.gdgl.mydata.binding.BindingDivice;
import com.gdgl.mydata.binding.Binding_response_params;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class BindListFragment extends Fragment implements updateList,UIListener{

	private View mView;
	List<DevicesModel> mList;

	ViewGroup no_dev;
	Button mBack;

	LinearLayout ch_pwd;
	LinearLayout deviceslist;

	ListView bindList;
	DataHelper dh;
	
	BindAdapter mBindAdapter;
	LightManager mLightManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		dh = new DataHelper((Context) getActivity());
		mLightManager=LightManager.getInstance();
		mLightManager.addObserver(this);
		new getDataTask().execute(1);
	}
	
	public class getDataTask extends AsyncTask<Integer, Integer, Integer>{

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			mList = DataUtil.getBindDevices((Context) getActivity(), dh);
			return 1;
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			initView();
			SimpleDevicesModel mSimpleDevicesModel;
			if(null!=mList && mList.size()>0){
				for (DevicesModel dm : mList) {
					mSimpleDevicesModel=new SimpleDevicesModel();
					mSimpleDevicesModel.setmIeee(dm.getmIeee());
					mSimpleDevicesModel.setmEP(dm.getmEP());
					mLightManager.getBindList(mSimpleDevicesModel);
				}
			}
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.bind_list, null);
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		ch_pwd = (LinearLayout) mView.findViewById(R.id.ch_pwd);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		ch_pwd.setLayoutParams(mLayoutParams);
		no_dev = (ViewGroup) mView.findViewById(R.id.no_dev);

		deviceslist = (LinearLayout) mView.findViewById(R.id.deviceslist);
		
		bindList=(ListView)mView.findViewById(R.id.devices_list);
		
		if (null == mList || mList.size() == 0) {
			no_dev.setVisibility(View.VISIBLE);
			deviceslist.setVisibility(View.GONE);
		} else {
			mBindAdapter=new BindAdapter();
			bindList.setAdapter(mBindAdapter);
			bindList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Bundle extra=new Bundle();
					extra.putInt(BindControlFragment.DevicesId, mList.get(position).getID());
//					int bindId=mList.get(position).getmBindTo()==null || mList.get(position).getmBindTo().trim().equals("")?-1:Integer.parseInt(mList.get(position).getmBindTo().trim());
//					extra.putInt(BindControlFragment.BindId, bindId);
					ChangeFragment c = (ChangeFragment) getActivity();
					BindControlFragment mBindControlFragment = new BindControlFragment();
					mBindControlFragment.setUplist(BindListFragment.this);
					mBindControlFragment.setBackAction((backAction)getActivity());
					mBindControlFragment.setArguments(extra);
					c.setFragment(mBindControlFragment);
				}
			});
		}
		
	}

	public class BindAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mList && mList.size() > 0) {
				return mList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mList && mList.size() > 0) {
				return mList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mList && mList.size() > 0) {
				return mList.get(position).getID();
			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View mView = convertView;
			ViewHolder mHolder;
			final DevicesModel mDevices = (DevicesModel) getItem(position);

			if (null == mView) {
				mHolder = new ViewHolder();
				mView = LayoutInflater.from((Context) getActivity()).inflate(
						R.layout.bind_item, null);
				mHolder.devices_img = (ImageView) mView
						.findViewById(R.id.devices_img);
				mHolder.devices_name = (TextView) mView
						.findViewById(R.id.devices_name);
				mHolder.devices_state = (TextView) mView
						.findViewById(R.id.devices_state);
				mView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) mView.getTag();
			}

			mHolder.devices_name.setText(mDevices.getmUserDefineName().replace(
					" ", ""));
			if (null != mDevices.getmBindTo()
					&& !mDevices.getmBindTo().trim().equals("")) {
				mHolder.devices_state.setText("已绑定");
			} else {
				mHolder.devices_state.setText("未绑定到任何设备");
			}

			int devModeleId = Integer.parseInt(mDevices.getmDeviceId());

			if (DataHelper.IAS_ZONE_DEVICETYPE == devModeleId
					|| DataHelper.IAS_ACE_DEVICETYPE == devModeleId) {

				mHolder.devices_img.setImageResource(UiUtils
						.getDevicesSmallIconByModelId(mDevices.getmModelId()
								.trim()));
			} else if (mDevices.getmModelId().indexOf(
					DataHelper.Multi_key_remote_control) == 0) {
				mHolder.devices_img.setImageResource(UiUtils
						.getDevicesSmallIconForRemote(devModeleId));
			} else {
				mHolder.devices_img.setImageResource(UiUtils
						.getDevicesSmallIcon(devModeleId));
			}
			return mView;
		}

		public class ViewHolder {
			ImageView devices_img;
			TextView devices_name;
			TextView devices_state;
		}

	}

	@Override
	public void upList(DevicesModel dm, String id) {
		// TODO Auto-generated method stub
		int postion = 0;
		if (null != mList && mList.size() > 0) {
			for (int i = 0; i < mList.size(); i++) {
				if (mList.get(i).getmIeee().trim()
						.equals(dm.getmIeee().trim())
						&& mList.get(i).getmEP().trim()
								.equals(dm.getmEP().trim())) {
					postion=i;

				}
			}
			mList.get(postion).setmBindTo(id);
			mBindAdapter.notifyDataSetChanged();
		}
		
	}
	
	public class updateDevTask extends AsyncTask<BindingDataEntity, Integer, Integer>{
		String ieee="";
		String ep="";
		String newId="";
		@Override
		protected Integer doInBackground(BindingDataEntity... params) {
			// TODO Auto-generated method stub
			BindingDataEntity data=params[0];
			if(null==data){
				return -1;
			}
			List<DevicesModel> mDevList=new ArrayList<DevicesModel>();
			List<DevicesModel> tempList;
			Binding_response_params brp=data.getResponse_params();
			ArrayList<BindingDivice> ablist=brp.getList();
			String where=" ieee=? and ep=? ";
			String[] args;
			
			if(null!=ablist && ablist.size()>0){
				SQLiteDatabase db=dh.getSQLiteDatabase();
				for (BindingDivice bindingDivice : ablist) {
					args=new String[2];
					args[0]=bindingDivice.getIeee();
					args[1]=bindingDivice.getEp();
					tempList=dh.queryForList(db, DataHelper.DEVICES_TABLE, null, where, args, null, null, null, null);
					if(null!=tempList && tempList.size()>0){
						mDevList.add(tempList.get(0));
					}
				}
				dh.close(db);
			}
			
			String newBindId="";
			
			if(null!=mDevList && mDevList.size()>0){
				for (DevicesModel dm : mDevList) {
					if(null!=dm){
						newBindId+=dm.getID()+"##";
					}
				}
			}
			
			String[] argss = { brp.getIeee(), brp.getEp() };
			ContentValues values = new ContentValues();
			values.put(DevicesModel.BIND_TO, newBindId);
			SQLiteDatabase db = dh.getSQLiteDatabase();
			dh.update(db, DataHelper.DEVICES_TABLE, values,
					where, argss);
			
			dh.close(db);
			
			newId=newBindId;
			ieee=brp.getIeee();
			ep=brp.getEp();
			
			return 1;
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			int m=getPostionInList(ieee, ep);
			if(-1!=m){
				mList.get(m).setmBindTo(newId);
				mBindAdapter.notifyDataSetChanged();
			}
		}
	}
	
	public int getPostionInList(String ieee,String ep){
		if(null==mList || null==ieee || null==ep){
			return -1;
		}
		if(ieee.trim().equals("") || ep.trim().equals("")){
			return -1;
		}
		for (int i=0;i<mList.size();i++) {
			DevicesModel dm=mList.get(i);
			if(ieee.trim().equals(dm.getmIeee().trim()) && ep.trim().equals(dm.getmEP().trim())){
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		//EventType.GETBINDLIST
		final Event event = (Event) object;
		if (EventType.GETBINDLIST == event.getType()) {
			if (event.isSuccess()==true) {
				// data maybe null
				BindingDataEntity data=(BindingDataEntity)event.getData();
				new updateDevTask().execute(data);
				
			}else {
				//if failed,prompt a Toast
				
			}
		}
	}
	@Override
	public void onDestroy() {
		mLightManager.deleteObserver(this);
		super.onDestroy();
	}
	
}
