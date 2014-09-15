package com.gdgl.activity;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class MessageListFragment extends BaseFragment implements UIListener {

	private View mView;
	List<CallbackWarnMessage> mList;

	ViewGroup no_dev;
	Button mBack;

	// LinearLayout deviceslist;

	ListView messageListView;
	DataHelper dh;

	MessageAdapter messageAdapter;
	CallbackWarnMessage currentMessage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		dh = new DataHelper((Context) getActivity());
		new getDataTask().execute(1);
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

		// deviceslist = (LinearLayout) mView.findViewById(R.id.message_list);

		messageListView = (ListView) mView.findViewById(R.id.message_list);

		messageAdapter = new MessageAdapter();
		messageListView.setAdapter(messageAdapter);
		if (null == mList || mList.size() == 0) {
			no_dev.setVisibility(View.VISIBLE);
			// deviceslist.setVisibility(View.GONE);
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
				mView = LayoutInflater.from((Context) getActivity()).inflate(
						R.layout.messageitem, null);
				mHolder.warn_img = (ImageView) mView
						.findViewById(R.id.warm_img);
				mHolder.warn_name = (TextView) mView
						.findViewById(R.id.describle_message);
				mHolder.warn_state = (TextView) mView
						.findViewById(R.id.detail_message);
				mHolder.warn_time = (TextView) mView.findViewById(R.id.time);
				mView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) mView.getTag();
			}
			mHolder.warn_name.setText(message.getW_description());
			
			// mHolder.warn_state.setText(message.getW_description()+"收到报警信息，请注意！");
			mHolder.warn_state.setText(message.getDetailmessage());
			mHolder.warn_time.setText(message.getTime());
			return mView;
		}

		public class ViewHolder {
			ImageView warn_img;
			TextView warn_name;
			TextView warn_state;
			TextView warn_time;
		}

	}

	public void dialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("确认清空所有消息吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				clearAllMessage();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

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
			mList.add(data);
			messageListView.post(new Runnable() {
				@Override
				public void run() {
					messageAdapter.notifyDataSetChanged();
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

	}
}
