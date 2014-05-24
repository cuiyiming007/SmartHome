package com.gdgl.activity;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Callback.CallbackWarmMessage;
import com.gdgl.smarthome.R;

public class MessageListFragment extends BaseFragment implements UIListener {

	private View mView;
	List<CallbackWarmMessage> mList;

	ViewGroup no_dev;
	Button mBack;

	// LinearLayout deviceslist;

	ListView messageListView;
	DataHelper dh;

	MessageAdapter messageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		dh = new DataHelper((Context) getActivity());
		CallbackManager.getInstance().addObserver(this);
		new getDataTask().execute(1);
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

		if (null == mList || mList.size() == 0) {
			no_dev.setVisibility(View.VISIBLE);
			// deviceslist.setVisibility(View.GONE);
		} else {
			messageAdapter = new MessageAdapter();
			messageListView.setAdapter(messageAdapter);
			messageListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				}
			});
		}

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
			final CallbackWarmMessage message = (CallbackWarmMessage) getItem(position);

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
			mHolder.warn_state.setText(message.getW_description()+"收到报警信息，请注意！");
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

	@Override
	public void update(Manger observer, Object object) {
		final Event event = (Event) object;
		if (EventType.WARM == event.getType()) {
			CallbackWarmMessage data = (CallbackWarmMessage) event.getData();
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
		CallbackManager.getInstance().deleteObserver(this);
		super.onDestroy();
	}

	@Override
	public void stopRefresh() {
		
	}
}
