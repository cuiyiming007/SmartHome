package com.gdgl.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.manager.WarnManager;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Callback.CallbackWarnMessage;
import com.gdgl.smarthome.R;

public class MessageListFragment extends BaseFragment implements UIListener,android.view.View.OnClickListener{

	private View mView;
	List<CallbackWarnMessage> mList;
	HashMap<String, Boolean> mCheckHashMap = new HashMap<String, Boolean>();

	ViewGroup no_dev, del_tools;
	Button mBack, check_all, cancel_all, cancel, delete;
	

	// LinearLayout deviceslist;

	ListView messageListView;
	DataHelper dh;

	MessageAdapter messageAdapter;
	CallbackWarnMessage currentMessage;

	/* 闵伟add start */
	// 是否删除模式
	private boolean isDeleteMode = false;
	private boolean isCheckAll = false;
	/* 闵伟add end  */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
		//initCheckHashMap(false);
	}

	private void initData() {
		// TODO Auto-generated method stub
		dh = new DataHelper((Context) getActivity());
		new getDataTask().execute(1);
	}
	
	private void initCheckHashMap(boolean b){
		//mCheckHashMap.clear();
		for(int i=0; i<mList.size(); i++){
			mCheckHashMap.put(mList.get(i).getId(), b);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("删除");
		menu.add(0, 1, 0, "删除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		final int position = info.position;
		currentMessage = mList.get(position);
		int menuIndex = item.getItemId();
		if (menuIndex == 1) {
			mList.remove(currentMessage);
			messageAdapter.notifyDataSetChanged();
			new AsyncTask<Object, Object, Object>() {
				@Override
				protected Object doInBackground(Object... params) {
					String where = " _id = ? ";
					String[] args = { currentMessage.getId() };

					SQLiteDatabase mSQLiteDatabase = dh.getSQLiteDatabase();
					dh.delete(mSQLiteDatabase, DataHelper.MESSAGE_TABLE,
							where, args);
					return null;
				}

			}.execute(currentMessage);

		}
		return super.onContextItemSelected(item);
	}

	public class getDataTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			mList = DataUtil.getWarmMessage((Context) getActivity(), dh);
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
		mView = inflater.inflate(R.layout.message_list, null);
		return mView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		no_dev = (ViewGroup) mView.findViewById(R.id.no_dev);
		del_tools = (ViewGroup) mView.findViewById(R.id.deletetools);
		// deviceslist = (LinearLayout) mView.findViewById(R.id.message_list);
		
		// delete tools
		check_all = (Button) mView.findViewById(R.id.check_all);
		cancel_all = (Button) mView.findViewById(R.id.cancel_all);
		cancel = (Button) mView.findViewById(R.id.cancel);
		delete = (Button) mView.findViewById(R.id.delete);
		check_all.setOnClickListener(this);
		cancel_all.setOnClickListener(this);
		cancel.setOnClickListener(this);
		delete.setOnClickListener(this);
		
		messageListView = (ListView) mView.findViewById(R.id.message_list);

		messageAdapter = new MessageAdapter();
		messageListView.setAdapter(messageAdapter);
		if (null == mList || mList.size() == 0) {
			no_dev.setVisibility(View.VISIBLE);
		} else {
			messageListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				}
			});
		}
		registerForContextMenu(messageListView);

	}

	public class MessageAdapter extends BaseAdapter {

		public MessageAdapter(){
			initCheckHashMap(false);
		}
		
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
				return position;
			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View mView = convertView;
			ViewHolder mHolder;
			final CallbackWarnMessage message = (CallbackWarnMessage) getItem(position);

			if (null == mView) {
				mHolder = new ViewHolder();
//				mView = LayoutInflater.from((Context) getActivity()).inflate(
//						R.layout.messageitem, null);
				mView = LayoutInflater.from((Context) getActivity()).inflate(
						R.layout.messageitemwithcheckbox, null);
				mHolder.warn_img = (ImageView) mView
						.findViewById(R.id.warm_img);
				mHolder.warn_name = (TextView) mView
						.findViewById(R.id.describle_message);
				mHolder.warn_state = (TextView) mView
						.findViewById(R.id.detail_message);
				mHolder.warn_time = (TextView) mView.findViewById(R.id.time);
				mHolder.warn_check = (CheckBox) mView.findViewById(R.id.checkBoxMessage);
				mHolder.warm_point = (ImageView) mView.findViewById(R.id.warm_point);
				mView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) mView.getTag();
				mHolder.warn_check.setChecked(mCheckHashMap.get(message.getId()));
			}
			mHolder.warn_img.setImageResource(Integer.parseInt(message.getHome_id()));
			
			if(message.getDetailmessage().indexOf("提示") != -1){
				mHolder.warm_point.setBackgroundResource(R.drawable.ui_toptitle_alarm_message_new_y);
			}else{
				mHolder.warm_point.setBackgroundResource(R.drawable.ui_toptitle_alarm_message_new);
			}
			mHolder.warn_name.setText(message.getZone_name());
			mHolder.warn_state.setText(message.getDetailmessage());
			mHolder.warn_time.setText(message.getTime());

			final CheckBox cb = mHolder.warn_check;
			cb.setChecked(mCheckHashMap.get(message.getId()));
			cb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mCheckHashMap.put(message.getId(), cb.isChecked());
					Log.i("check", message.getId() +"-"+ cb.isChecked() + "");
				}
				
				
			});
			if(isDeleteMode){
				mHolder.warn_check.setVisibility(View.VISIBLE);
			}else{
				mHolder.warn_check.setVisibility(View.GONE);
			}
			return mView;
		}

		public class ViewHolder {
			ImageView warn_img;
			TextView warn_name;
			TextView warn_state;
			TextView warn_time;
			CheckBox warn_check;
			ImageView warm_point;
		}

	}

//	public void dialog() {
//		AlertDialog.Builder builder = new Builder(getActivity());
//		builder.setMessage("确认清空所有消息吗？");
//		builder.setTitle("提示");
//		builder.setPositiveButton("确认", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				clearAllMessage();
//			}
//		});
//		builder.setNegativeButton("取消", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}
//	public void dialog() {
//		AlertDialog.Builder builder = new Builder(getActivity());
//		builder.setMessage("确认删除选中消息吗？");
//		builder.setTitle("提示");
//		builder.setPositiveButton("确认", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				clearAllMessage();
//			}
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		builder.setNegativeButton("取消", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}

	private void clearAllMessage() {
		mList.clear();
		WarnManager.getInstance().inialWarn();
		messageAdapter.notifyDataSetChanged();
		SQLiteDatabase mSQLiteDatabase = dh.getSQLiteDatabase();
		dh.emptyTable(mSQLiteDatabase, DataHelper.MESSAGE_TABLE);
	}

	@Override
	public void update(Manger observer, Object object) {
		final Event event = (Event) object;
		if (EventType.WARN == event.getType()) {
			CallbackWarnMessage data = (CallbackWarnMessage) event.getData();
			Log.i("CallbackWarnMessage New", " = "+data);
			/* 闵伟 add start  */	
//			int newId = Integer.parseInt(mList.get(0).getId()) + 1; //最新消息的SQLite ID
//			data.setId(""+newId);
			Log.i("now id", " = "+data.getId());
			//让最新的message出现在最上面
			if(mList.size() == 0){
				mList.add(data);
			}else{
				mList.add(0,data);
			}
			mCheckHashMap.put(data.getId(), false);
			/* 闵伟add end */
			messageListView.post(new Runnable() {
				@Override
				public void run() {
					messageAdapter.notifyDataSetChanged();
					/* 闵伟 add start 出现至少一笔message时,隐藏无消息view*/
					no_dev.setVisibility(View.GONE);
					/* 闵伟add end */
				}
			});

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void stopRefresh() {
		// TODO Auto-generated method stub
		
	}
	
	// show delete tools
	public void showDeltools(){
		del_tools.setVisibility(View.VISIBLE);
	}
	
	//hide delete tools
	public void hideDeltools(){
		del_tools.setVisibility(View.GONE);
	}
	
	// start delete mode
	public void startDeleteMode(){
		showDeltools();
		isDeleteMode = true;
		messageAdapter.notifyDataSetChanged();
	}
	
	//end delete mode
	public void endDeleteMode(){
		hideDeltools();
		isDeleteMode = false;
		isCheckAll = false; //取消全选
		refreshCheckHashMap();
		messageAdapter.notifyDataSetChanged();
	}
	
	//delete button click
	public void deleteClick(){
		if(mList == null || mList.size() == 0){
			endDeleteMode();
			return;
		}
		if(isDeleteMode){
			endDeleteMode();
		}else{
			startDeleteMode();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.check_all:
			isCheckAll = true;
			refreshCheckHashMap();
			messageAdapter.notifyDataSetChanged();
			break;
		case R.id.cancel_all:
			isCheckAll = false;
			refreshCheckHashMap();
			messageAdapter.notifyDataSetChanged();
			break;
		case R.id.cancel:
			endDeleteMode();
			break;
		case R.id.delete:
			Log.i("HashMap String", " = "+ mCheckHashMap.toString());
			deleteMessage();
			messageAdapter.notifyDataSetChanged();
			endDeleteMode();
						
			break;
		}
	}
	
	//全选 或者 清空
	public void refreshCheckHashMap(){
		if(isCheckAll){
			initCheckHashMap(true);
		}else{
			initCheckHashMap(false);
		}
	}
	
	//循环删除所有选中的资料
	public void deleteMessage(){
		SQLiteDatabase mSQLiteDatabase = dh.getWritableDatabase();
		Iterator<CallbackWarnMessage> itr = mList.iterator();
		while (itr.hasNext()) {    
			CallbackWarnMessage nextObj = itr.next();
			String id = nextObj.getId();
			if(mCheckHashMap.get(id)){
				deleteMessageSQL(mSQLiteDatabase, id);
				itr.remove();
				mCheckHashMap.remove(id);
				Log.i("deleteMessageSQL", "id = "+ id);
			}
		}
		if(mList.size() == 0){
			WarnManager.getInstance().inialWarn();
		}
		
	}
	
	//按ID去SQLite删除资料
	public void deleteMessageSQL(SQLiteDatabase mSQLiteDatabase, String id){
		String where = "_id=?";
		String[] args = {id};

		mSQLiteDatabase.delete(DataHelper.MESSAGE_TABLE,
				where, args);
	}
	/* 闵伟add end  */
}
