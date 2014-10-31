package com.gdgl.activity;

import h264.com.VideoActivity;
import h264.com.VideoInfoDialog;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gdgl.activity.UIinterface.IFragmentCallbak;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.manager.VideoManager;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.video.VideoNode;
import com.gdgl.mydata.video.VideoResponse;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.UiUtils;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

public class VideoFragment extends Fragment implements UIListener,
		Dialogcallback, IFragmentCallbak {
	GridView content_view;
	View mView;
	ViewGroup nodevices;
	CustomeAdapter adapter;

	public static final String PASS_OBJECT = "pass_object";
	List<VideoNode> mList;
	VideoNode currentvVideoNode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		mList = DataHelper.getVideoList((Context) getActivity(),
				new DataHelper((Context) getActivity()));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.devices_main_fragment, null);
		initview();
		VideoManager.getInstance().addObserver(this);
		return mView;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser == true) {
			VideoManager.getInstance().getIPClist();
		}
	}

	private void initview() {
		// TODO Auto-generated method stub
		nodevices = (ViewGroup) mView.findViewById(R.id.nodevices);
		nodevices.setVisibility(View.GONE);
		content_view = (GridView) mView.findViewById(R.id.content_view);
		adapter = new CustomeAdapter();
		content_view.setAdapter(adapter);
		content_view.setLayoutAnimation(UiUtils
				.getAnimationController((Context) getActivity()));
		content_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Bundle extras = new Bundle();
				Intent intent = new Intent((Context) getActivity(),
						VideoActivity.class);
				extras.putParcelable(PASS_OBJECT, mList.get(arg2));
				intent.putExtras(extras);
				startActivity(intent);
			}

		});
		if (null == mList || mList.size() == 0) {
			nodevices.setVisibility(View.VISIBLE);
		} else {
			nodevices.setVisibility(View.GONE);
		}
		registerForContextMenu(content_view);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		content_view.setVisibility(View.GONE);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		content_view.setVisibility(View.VISIBLE);

		content_view.setAdapter(adapter);
		content_view.setLayoutAnimation(UiUtils
				.getAnimationController((Context) getActivity()));
		super.onResume();
	}

	@Override
	public void onDestroy() {
		VideoManager.getInstance().deleteObserver(this);
		super.onDestroy();
	}

	private class CustomeAdapter extends BaseAdapter {

		public CustomeAdapter() {
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mList) {
				return mList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mList) {
				return mList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mList
					&& !TextUtils.isEmpty(mList.get(position).getId())) {
				return Integer.parseInt(mList.get(position).getId());
			}
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder mViewHolder;
			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from((Context) getActivity())
						.inflate(R.layout.video_gridview_item, null);
				mViewHolder.funcImg = (ImageView) convertView
						.findViewById(R.id.func_img);
				mViewHolder.funcText = (TextView) convertView
						.findViewById(R.id.func_name);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			//mViewHolder.funcImg.setImageResource(R.drawable.video3);
			mViewHolder.funcText.setText(mList.get(position).getAliases());
			return convertView;
		}

		class ViewHolder {
			ImageView funcImg;
			TextView funcText;
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("编辑&删除");
		menu.add(0, 1, 0, "编辑");
		menu.add(0, 2, 0, "删除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		currentvVideoNode = mList.get(position);
		int menuIndex = item.getItemId();

		if (1 == menuIndex) {
			VideoInfoDialog videoInfoDialog = new VideoInfoDialog(
					getActivity(), VideoInfoDialog.Edit, this,
					currentvVideoNode);
			videoInfoDialog.setContent("编辑" + currentvVideoNode.getAliases());
			videoInfoDialog.show();
		}
		if (2 == menuIndex) {
			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
					(Context) getActivity());
			mMyOkCancleDlg.setDialogCallback((Dialogcallback) this);
			mMyOkCancleDlg.setContent("确定要删除" + currentvVideoNode.getAliases()
					+ "吗?");
			mMyOkCancleDlg.show();
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void update(Manger observer, Object object) {
		final Event event = (Event) object;
		if (event.getType() == EventType.GETVIDEOLIST) {
			if (event.isSuccess()) {
				mList.clear();
				VideoResponse videoResponse = (VideoResponse) event.getData();
				for (int i = 0; i < videoResponse.getList().size(); i++) {
					mList.add(videoResponse.getList().get(i));
				}
				adapter.notifyDataSetChanged();
			}
		} else if (event.getType() == EventType.DELETEIPC) {
			if (event.isSuccess()) {
				Toast.makeText(getActivity(), "删除成功!", Toast.LENGTH_SHORT)
						.show();
				updateDeleteVideoList(currentvVideoNode);
			} else {
				Toast.makeText(getActivity(), "删除失败!", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	public void updateAddVideoList(VideoNode videoNode) {
		// 如果在列表中已经存在该摄像头，就返回，不添加
		for (VideoNode node : mList) {
			if (node.getId().equals(videoNode.getId())) {
				return;
			}
		}

		mList.add(videoNode);
		adapter.notifyDataSetChanged();
	}

	public void updateDeleteVideoList(VideoNode videoNode) {
		if (mList.contains(videoNode)) {
			mList.remove(videoNode);
		}
		adapter.notifyDataSetChanged();
	}

	public void updateEditVideoList(VideoNode videoNode) {
		String id = videoNode.getId();
		for (VideoNode v : mList) {
			if (v.getId().equals(id)) {
				mList.remove(v);
				mList.add(videoNode);
				break;
			}
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onFragmentResult(int requsetId, boolean result, Object data) {
		if (requsetId == VideoInfoDialog.Add && result) {
			updateAddVideoList((VideoNode) data);
		}
		if (requsetId == VideoInfoDialog.Edit && result) {
			updateEditVideoList((VideoNode) data);
		}
	}

	@Override
	public void dialogdo() {
		VideoManager.getInstance().deleteIPC(currentvVideoNode);
	}
}
