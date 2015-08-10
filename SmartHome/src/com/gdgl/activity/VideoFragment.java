package com.gdgl.activity;

import h264.com.VideoInfoDialog;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gdgl.activity.UIinterface.IFragmentCallbak;
import com.gdgl.libjingle.LibjingleResponseHandlerManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.manager.VideoManager;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.video.VideoNode;
import com.gdgl.mydata.video.VideoResponse;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.UiUtils;
import com.gdgl.util.VersionDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

public class VideoFragment extends Fragment implements UIListener,
		Dialogcallback, IFragmentCallbak {
	private static final String TAG = "VideoFragment";
	GridView content_view;
	View mView;
	ViewGroup nodevices;
	CustomeAdapter adapter;
	VideoInfoDialog videoInfoDialog;

	public static final String PASS_OBJECT = "pass_object";
	List<VideoNode> mList;
	VideoNode currentVideoNode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		mList = DataHelper.getVideoList((Context) getActivity(),
				new DataHelper((Context) getActivity()));
		Log.i(TAG, "mList.size == " + mList.size());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.devices_main_fragment, null);
		initview();
		VideoManager.getInstance().addObserver(this);
		CallbackManager.getInstance().addObserver(this);
		LibjingleResponseHandlerManager.getInstance().addObserver(this);
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
//		content_view.setLayoutAnimation(UiUtils
//				.getAnimationController((Context) getActivity()));
		content_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Bundle extras = new Bundle();
//				Intent intent = new Intent((Context) getActivity(),
//						VideoActivity.class);
				Intent intent = new Intent((Context) getActivity(),
						HikVideoActivity.class);
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
		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
			registerForContextMenu(content_view);
		} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
			content_view.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					// TODO Auto-generated method stub
					VersionDlg vd = new VersionDlg(getActivity());
					vd.setContent(getResources().getString(R.string.Unable_In_InternetState));
					vd.show();
					return true;
				}
			});
		}
		
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
//		content_view.setAdapter(adapter);
//		content_view.setLayoutAnimation(UiUtils
//				.getAnimationController((Context) getActivity()));
		super.onResume();
	}

	@Override
	public void onDestroy() {
		VideoManager.getInstance().deleteObserver(this);
		CallbackManager.getInstance().deleteObserver(this);
		LibjingleResponseHandlerManager.getInstance().deleteObserver(this);
		super.onDestroy();
	}

	private class CustomeAdapter extends BaseAdapter {

//		public CustomeAdapter() {
//		}

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
			Log.i(TAG+"GETVIEW", mList.get(position).getAliases());
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
		if(item.getGroupId() != 0){
			return false;
		}
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		currentVideoNode = mList.get(position);
		int menuIndex = item.getItemId();
		if (1 == menuIndex) {
			VideoInfoDialog videoInfoDialog = new VideoInfoDialog(
					getActivity(), VideoInfoDialog.Edit, currentVideoNode);
			videoInfoDialog.setContent("编辑" + currentVideoNode.getAliases());
			videoInfoDialog.show();
//			if(videoInfoDialog == null){
//				videoInfoDialog = new VideoInfoDialog(
//						getActivity(), VideoInfoDialog.Edit, this,
//						currentVideoNode);
//			}else{
//				videoInfoDialog.setVideoNode(currentVideoNode);
//			}
//			videoInfoDialog.setContent("编辑" + currentVideoNode.getAliases());
//			videoInfoDialog.show();
		}
		if (2 == menuIndex) {
			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
					(Context) getActivity());
			mMyOkCancleDlg.setDialogCallback((Dialogcallback) this);
			mMyOkCancleDlg.setContent("确定要删除" + currentVideoNode.getAliases()
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
				Log.i(TAG, "EventType.GETVIDEOLIST");
				
				mList.clear();
				VideoResponse videoResponse = (VideoResponse) event.getData();
				for (int i = 0; i < videoResponse.getList().size(); i++) {
					mList.add(videoResponse.getList().get(i));
					Log.i(TAG, mList.get(i).getAliases());
				}
				content_view.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						Log.i(TAG, "adapter.notifyDataSetChanged");
						adapter.notifyDataSetChanged();
					}
				});
			}
		} else if (event.getType() == EventType.DELETEIPC) {
			if (event.isSuccess()) {
				int vid = (Integer) event.getData();
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getId().equals(String.valueOf(vid))) {
						mList.remove(i);
						break;
					}
				}
				content_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.notifyDataSetChanged();
						Toast.makeText(getActivity(), "删除成功!",
								Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				content_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "删除失败!",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		} else if (event.getType() == EventType.ADDIPC) {
			if (event.isSuccess()) {
				VideoNode videoNode = (VideoNode) event.getData();
				mList.add(videoNode);
				content_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.notifyDataSetChanged();
						Toast.makeText(getActivity(), "添加成功",
								Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				content_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "添加失败",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

		} else if (event.getType() == EventType.EDITIPC) {
			Log.i("EventType", " = EDITIPC");
			if (event.isSuccess()) {
				VideoNode videoNode = (VideoNode) event.getData();
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getId().equals(videoNode.getId())) {
						mList.remove(i);
						mList.add(i, videoNode);
						break;
					}
				}
				content_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.notifyDataSetChanged();
						Toast.makeText(getActivity(), "编辑成功",
								Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				content_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "编辑失败",
								Toast.LENGTH_SHORT).show();
					}
				});
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
		for(int i=0; i<mList.size(); i++){
			if(mList.get(i).getId().equals(id)){
				mList.set(i, videoNode);
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
		VideoManager.getInstance().deleteIPC(currentVideoNode);
	}
}
