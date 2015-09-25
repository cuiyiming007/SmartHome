package com.gdgl.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Callback.CallbackIpcLinkageMessage;
import com.gdgl.smarthome.R;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MessageIpcLinkageFragment extends Fragment implements UIListener,
		android.view.View.OnClickListener {
	private View mView;
	int width, height;
	List<CallbackIpcLinkageMessage> mList;
	HashMap<String, Boolean> mCheckHashMap = new HashMap<String, Boolean>();

	ViewGroup no_dev, del_tools;
	Button mBack, check_all, cancel_all, cancel, delete;

	ListView messageListView;
	DataHelper dh;

	MessageAdapter messageAdapter;
	CallbackIpcLinkageMessage currentMessage;

	// 是否删除模式
	private boolean isDeleteMode = false;
	private boolean isCheckAll = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		CallbackManager.getInstance().addObserver(this);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		dh = new DataHelper((Context) getActivity());
		new getDataTask().execute(1);
	}

	private void initCheckHashMap(boolean b) {
		// mCheckHashMap.clear();
		for (int i = 0; i < mList.size(); i++) {
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
					dh.delete(mSQLiteDatabase, DataHelper.IPC_LINKAGE_TABLE,
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
			mList = DataUtil.getIpcLinkageMessage((Context) getActivity(), dh);
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
//					MessageIpcLinkageMediaDlg ipcLinkageMediaDlg = new MessageIpcLinkageMediaDlg(
//							getActivity(), mList.get(position), width, height);
//					ipcLinkageMediaDlg.show();
					Intent intent = new Intent();
					intent.putExtra("CallbackIpcLinkageMessage", mList.get(position));
					intent.setClass(getActivity(), MessageIpcLinkageMediaActivity.class);
					startActivity(intent);
				}
			});
		}
		registerForContextMenu(messageListView);

	}

	public class MessageAdapter extends BaseAdapter {

		public MessageAdapter() {
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
			final CallbackIpcLinkageMessage message = (CallbackIpcLinkageMessage) getItem(position);

			if (null == mView) {
				mHolder = new ViewHolder();
				mView = LayoutInflater.from((Context) getActivity()).inflate(
						R.layout.messageitemwithcheckbox, null);
				mHolder.warn_img = (ImageView) mView
						.findViewById(R.id.warm_img);
				mHolder.warn_name = (TextView) mView
						.findViewById(R.id.describle_message);
				mHolder.warn_state = (TextView) mView
						.findViewById(R.id.detail_message);
				mHolder.warn_time = (TextView) mView.findViewById(R.id.time);
				mHolder.warn_check = (CheckBox) mView
						.findViewById(R.id.checkBoxMessage);
				mHolder.warm_point = (ImageView) mView
						.findViewById(R.id.warm_point);
				mView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) mView.getTag();
				mHolder.warn_check
						.setChecked(mCheckHashMap.get(message.getId()));
			}
			mHolder.warn_img.setImageResource(Integer.parseInt(message
					.getDevicePic()));
			mHolder.warm_point.setVisibility(View.GONE);
			mHolder.warn_name.setText(message.getDeviceName());
			mHolder.warn_state.setText(message.getDescription());
			mHolder.warn_time.setText(message.getTime());

			final CheckBox cb = mHolder.warn_check;
			cb.setChecked(mCheckHashMap.get(message.getId()));
			cb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mCheckHashMap.put(message.getId(), cb.isChecked());
					Log.i("check", message.getId() + "-" + cb.isChecked() + "");
				}

			});
			if (isDeleteMode) {
				mHolder.warn_check.setVisibility(View.VISIBLE);
			} else {
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

	@Override
	public void update(Manger observer, Object object) {
		final Event event = (Event) object;
		if (EventType.IPC_LINKAGE_MSG == event.getType()) {
			CallbackIpcLinkageMessage data = (CallbackIpcLinkageMessage) event
					.getData();
			// 让最新的message出现在最上面
			if (mList.size() == 0) {
				mList.add(data);
			} else {
				mList.add(0, data);
			}
			mCheckHashMap.put(data.getId(), false);
			messageListView.post(new Runnable() {
				@Override
				public void run() {
					messageAdapter.notifyDataSetChanged();
					no_dev.setVisibility(View.GONE);
				}
			});

		}
	}

	@Override
	public void onDestroy() {
		CallbackManager.getInstance().deleteObserver(this);
		super.onDestroy();
	}

	// show delete tools
	public void showDeltools() {
		del_tools.setVisibility(View.VISIBLE);
	}

	// hide delete tools
	public void hideDeltools() {
		del_tools.setVisibility(View.GONE);
	}

	// start delete mode
	public void startDeleteMode() {
		showDeltools();
		isDeleteMode = true;
		messageAdapter.notifyDataSetChanged();
	}

	// end delete mode
	public void endDeleteMode() {
		hideDeltools();
		isDeleteMode = false;
		isCheckAll = false; // 取消全选
		refreshCheckHashMap();
		messageAdapter.notifyDataSetChanged();
	}

	// delete button click
	public void deleteClick() {
		if (mList == null || mList.size() == 0) {
			endDeleteMode();
			return;
		}
		if (isDeleteMode) {
			endDeleteMode();
		} else {
			startDeleteMode();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
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
			Log.i("HashMap String", " = " + mCheckHashMap.toString());
			deleteMessage();
			messageAdapter.notifyDataSetChanged();
			endDeleteMode();

			break;
		}
	}

	// 全选 或者 清空
	public void refreshCheckHashMap() {
		if (isCheckAll) {
			initCheckHashMap(true);
		} else {
			initCheckHashMap(false);
		}
	}

	// 循环删除所有选中的资料
	public void deleteMessage() {
		SQLiteDatabase mSQLiteDatabase = dh.getWritableDatabase();
		Iterator<CallbackIpcLinkageMessage> itr = mList.iterator();
		while (itr.hasNext()) {
			CallbackIpcLinkageMessage nextObj = itr.next();
			String id = nextObj.getId();
			if (mCheckHashMap.get(id)) {
				deleteMessageSQL(mSQLiteDatabase, id);
				itr.remove();
				mCheckHashMap.remove(id);
				Log.i("deleteMessageSQL", "id = " + id);
			}
		}
	}

	// 按ID去SQLite删除资料
	public void deleteMessageSQL(SQLiteDatabase mSQLiteDatabase, String id) {
		String where = "_id=?";
		String[] args = { id };

		mSQLiteDatabase.delete(DataHelper.IPC_LINKAGE_TABLE, where, args);
	}
}
