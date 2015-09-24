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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.manager.VideoManager;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.video.VideoNode;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOKOnlyDlg;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;
import com.videogo.constant.IntentConsts;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EzvizAPI;
import com.videogo.openapi.bean.req.GetCameraInfoList;
import com.videogo.openapi.bean.resp.CameraInfo;

public class VideoFragment extends Fragment implements UIListener,
		Dialogcallback {
	GridView content_view;
	View mView;
	ViewGroup nodevices;
	CustomeAdapter adapter;
	ButtonFloat mButtonFloat;
	private EzvizAPI mEzvizAPI = null;
	public static final String PASS_OBJECT = "pass_object";
	//List<VideoNode> mList;
	List<CameraInfo> mList;
	CameraInfo currentVideoNode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		Log.i("aaa", "1111111111");
		mEzvizAPI = EzvizAPI.getInstance();
		new Thread(new Runnable() {
			public void run() {
				Log.i("aaa", "222222222");
				mEzvizAPI.setAccessToken("at.7o7px5k49ncb8fge0mytlosqa9tegu17-706oodewu2-0mf2ean-dfn00dkhq");
				Log.i("aaa", "33333333333");
				GetCameraInfoList getCameraInfoList = new GetCameraInfoList();
				Log.i("aaa", "4444444444");
				getCameraInfoList.setPageSize(10);
				try {
					Log.i("aaa", "55555555555");
					 mList = mEzvizAPI.getCameraInfoList(getCameraInfoList);
					 Log.i("aaa", "bbbbbbbbbbbb");
				} catch (BaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.devices_main_fragment, null);
		initview();
		CallbackManager.getInstance().addObserver(this);
		return mView;
	}

	// @Override
	// public void setUserVisibleHint(boolean isVisibleToUser) {
	// super.setUserVisibleHint(isVisibleToUser);
	// if (isVisibleToUser == true) {
	// if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
	// VideoManager.getInstance().getIPClist();
	// } else if (NetworkConnectivity.networkStatus ==
	// NetworkConnectivity.INTERNET) {
	// LibjingleSendManager.getInstance().getIPClist();
	// }
	// }
	// }

	private void initview() {
		// TODO Auto-generated method stub
		nodevices = (ViewGroup) mView.findViewById(R.id.nodevices);
		nodevices.setVisibility(View.GONE);
		content_view = (GridView) mView.findViewById(R.id.content_view);
		mButtonFloat = (ButtonFloat) mView.findViewById(R.id.buttonFloat);
		adapter = new CustomeAdapter();
		content_view.setAdapter(adapter);
		content_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i("aaa", "66666666666");
				CameraInfo cameraInfo = adapter.getItem(position);
				Log.i("aaa", "777777777");//简单视频播放YingShiVideoActivity,YingShiRealPlayActivity
					Intent intent = new Intent((Context) getActivity(),YingShiRealPlayActivity.class);
					Log.i("aaa", "888888888");
					intent.putExtra(IntentConsts.EXTRA_CAMERA_INFO, cameraInfo);
					Log.i("aaa", "999999999");
					startActivity(intent);
				} 
		});
		mButtonFloat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					VideoInfoDialog addDlg;
					addDlg = new VideoInfoDialog(getActivity(),
							VideoInfoDialog.Add, mList.size());
					addDlg.setContent("添加");
					addDlg.show();
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(getActivity());
					myOKOnlyDlg.setContent(getResources().getString(
							R.string.Unable_In_InternetState));
					myOKOnlyDlg.show();
				}*/
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
	public void onDestroy() {
		CallbackManager.getInstance().deleteObserver(this);
		unregisterForContextMenu(content_view);
		super.onDestroy();
	}

	private class CustomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mList) {
				return mList.size();
			}
			return 0;
		}

		@Override
		public CameraInfo getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mList) {
				return mList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			/*if (null != mList
					&& !TextUtils.isEmpty(mList.get(position).getId())) {
				return Integer.parseInt(mList.get(position).getId());
			}*/
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(mList==null) {
				return convertView;
			}
			final ViewHolder mViewHolder;
			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from((Context) getActivity())
						.inflate(R.layout.gridview_card_item, null);
				mViewHolder.funcImg = (ImageView) convertView
						.findViewById(R.id.func_img);
				mViewHolder.funcText = (TextView) convertView
						.findViewById(R.id.func_name);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			//if (mList.get(position).getIpc_status().equals("a")) {
				mViewHolder.funcImg.setImageResource(R.drawable.ui2_device_video_style);
				
			//} else {
			//	mViewHolder.funcImg.setImageResource(R.drawable.ui2_device_video_off);
			//}
			mViewHolder.funcText.setText(mList.get(position).getCameraName());
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
		// Log.i("", "编辑&删除");
		menu.setHeaderTitle("编辑&删除");
		menu.add(0, 1, 0, "编辑");
		menu.add(0, 2, 0, "删除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getGroupId() != 0) {
			return false;
		}
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		currentVideoNode = mList.get(position);
		int menuIndex = item.getItemId();
		if (1 == menuIndex) {
			if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
				VideoInfoDialog videoInfoDialog = new VideoInfoDialog(
						getActivity(), VideoInfoDialog.Edit, currentVideoNode);
				videoInfoDialog.setContent("编辑" + currentVideoNode.getCameraName());
				videoInfoDialog.show();
			} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
				MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(getActivity());
				myOKOnlyDlg.setContent(getResources().getString(
						R.string.Unable_In_InternetState));
				myOKOnlyDlg.show();
			}
		}
		if (2 == menuIndex) {
			if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
				MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
						(Context) getActivity());
				mMyOkCancleDlg.setDialogCallback((Dialogcallback) this);
				mMyOkCancleDlg.setContent("确定要删除" + currentVideoNode.getCameraName()
						+ "吗?");
				mMyOkCancleDlg.show();
			} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
				MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(getActivity());
				myOKOnlyDlg.setContent(getResources().getString(
						R.string.Unable_In_InternetState));
				myOKOnlyDlg.show();
			}
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void update(Manger observer, Object object) {
		final Event event = (Event) object;
		if (event.getType() == EventType.ADDIPC) {
			if (event.isSuccess()) {
				CameraInfo videoNode = (CameraInfo) event.getData();
				mList.add(videoNode);
				content_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.notifyDataSetChanged();
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
			if (event.getType() == EventType.EDITIPC) {
				if (event.isSuccess()) {
					CameraInfo videoNode = (CameraInfo) event.getData();
					for (int i = 0; i < mList.size(); i++) {
						if (mList.get(i).getCameraId().equals(videoNode.getCameraId())) {
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
		} else if (event.getType() == EventType.DELETEIPC) {
			if (event.isSuccess()) {
				int vid = (Integer) event.getData();
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getCameraId().equals(String.valueOf(vid))) {
						mList.remove(i);
						break;
					}
				}
				content_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.notifyDataSetChanged();
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
		} else if (event.getType() == EventType.IPCONLINESTATUS) {
			char[] videoStatusList = (char[]) event.getData();
			for (int i = 0; i < videoStatusList.length; i++) {
				if (videoStatusList[i] != 'c') {
					for (int j = 0; j < mList.size(); j++) {
						if (mList.get(j).getCameraId().equals(String.valueOf(i))) {
							//mList.get(j).setIpc_status(String.valueOf(videoStatusList[i]));
							break;
						}
					}
				}
			}
			content_view.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					adapter.notifyDataSetChanged();
				}
			});
		}
	}

	@Override
	public void dialogdo() {
		//VideoManager.getInstance().deleteIPC(currentVideoNode);
	}
}
