package com.gdgl.activity;

import java.util.List;

import com.gc.materialdesign.views.ButtonFloat;
import com.gdgl.libjingle.LibjingleSendManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.SceneLinkageManager;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.scene.SceneInfo;
import com.gdgl.network.NetworkConnectivity;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ScenesFragment extends Fragment implements UIListener,
		Dialogcallback {

	GridView content_view;
	View mView;
	List<SceneInfo> mScenes;
	SceneInfo currentSceneInfo;
	ButtonFloat mButtonFloat;
	ViewGroup nodevices;
	LinearLayout defaultScene1;
	ImageView defaultScene1Img;
	TextView defaultScene1Text;
	LinearLayout defaultScene2;
	ImageView defaultScene2Img;
	TextView defaultScene2Text;
	CustomeAdapter mCustomeAdapter;
	DataHelper mDateHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		CallbackManager.getInstance().addObserver(this);
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		mDateHelper = new DataHelper((Context) getActivity());
		SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();

		mScenes = mDateHelper.queryForSceneInfoList(mSQLiteDatabase, null,
				null, null, null, null, SceneInfo.SCENE_INDEX, null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.scene_fragment, null);
		initview();
		return mView;
	}

	private void initview() {
		// TODO Auto-generated method stub
		nodevices = (ViewGroup) mView.findViewById(R.id.nodevices);
		defaultScene1 = (LinearLayout) mView.findViewById(R.id.default1);
		defaultScene2 = (LinearLayout) mView.findViewById(R.id.default2);
		content_view = (GridView) mView.findViewById(R.id.content_view);
		mButtonFloat = (ButtonFloat) mView.findViewById(R.id.buttonFloat);
		
		mCustomeAdapter = new CustomeAdapter();
		mCustomeAdapter.setList(mScenes);
		content_view.setAdapter(mCustomeAdapter);
		// content_view.setLayoutAnimation(UiUtils.getAnimationController((Context)getActivity()));
		mButtonFloat.attachToListView(content_view);

		defaultScene1Img = (ImageView) defaultScene1.getChildAt(0);
		defaultScene1Text = (TextView) defaultScene1.getChildAt(1);
		defaultScene2Img = (ImageView) defaultScene2.getChildAt(0);
		defaultScene2Text = (TextView) defaultScene2.getChildAt(1);
		defaultScene1Img.setImageResource(R.drawable.ui2_scene_style);
		defaultScene1Text.setText("全部布防");
		defaultScene2Img.setImageResource(R.drawable.ui2_scene_style);
		defaultScene2Text.setText("全部撤防");

		defaultScene1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					SceneLinkageManager.getInstance().DoScene(-1);
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					LibjingleSendManager.getInstance().DoScene(-1);
				}
			}
		});
		defaultScene1.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "默认情景，不可编辑", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});
		defaultScene2.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "默认情景，不可编辑", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});
		defaultScene2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					SceneLinkageManager.getInstance().DoScene(-2);
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					LibjingleSendManager.getInstance().DoScene(-2);
				}
			}
		});

		content_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
					SceneLinkageManager.getInstance().DoScene(
							mScenes.get(position).getSid());
				} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
					LibjingleSendManager.getInstance().DoScene(
							mScenes.get(position).getSid());
				}
			}
		});
		mButtonFloat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), SceneDevicesActivity.class);
				i.putExtra(SceneDevicesActivity.TYPE,
						SceneDevicesActivity.CREATE);
				i.putExtra(SceneInfo.SCENE_INDEX, mScenes.size());
				startActivity(i);
			}
		});
		if (null == mScenes || mScenes.size() == 0) {
			nodevices.setVisibility(View.VISIBLE);
		} else {
			nodevices.setVisibility(View.GONE);
		}
		registerForContextMenu(content_view);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.setHeaderTitle("编辑&删除");
		menu.add(0, 1, 0, "编辑");
		menu.add(0, 2, 0, "删除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		currentSceneInfo = mScenes.get(position);
		int menuIndex = item.getItemId();
		if (1 == menuIndex) {
			Intent i = new Intent(getActivity(), SceneDevicesActivity.class);
			i.putExtra(SceneDevicesActivity.TYPE, SceneDevicesActivity.EDIT);
			i.putExtra(Constants.PASS_OBJECT, currentSceneInfo);
			startActivity(i);
		}
		if (2 == menuIndex) {
			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
					(Context) getActivity());
			mMyOkCancleDlg.setDialogCallback(this);
			mMyOkCancleDlg.setContent("确定要删除  "
					+ mScenes.get(position).getScnname() + " 吗?");
			mMyOkCancleDlg.show();
		}
		return super.onContextItemSelected(item);
	}

	private class CustomeAdapter extends BaseAdapter {

		private List<SceneInfo> mList;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder mViewHolder;
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
			mViewHolder.funcImg.setImageResource(R.drawable.ui2_scene_style);
			mViewHolder.funcText.setText(mList.get(position).getScnname());
			return convertView;
		}

		class ViewHolder {
			ImageView funcImg;
			TextView funcText;
		}

		public void setList(List<SceneInfo> s) {
			mList = null;
			mList = s;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		content_view.setVisibility(View.VISIBLE);
		initData();
		if (null == mScenes || mScenes.size() == 0) {
			nodevices.setVisibility(View.VISIBLE);
		} else {
			nodevices.setVisibility(View.GONE);
		}
		mCustomeAdapter = new CustomeAdapter();
		mCustomeAdapter.setList(mScenes);
		content_view.setAdapter(mCustomeAdapter);
		// content_view.setLayoutAnimation(UiUtils.getAnimationController((Context)getActivity()));
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		CallbackManager.getInstance().deleteObserver(this);
		super.onDestroy();
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		Event event = (Event) object;
		if (event.getType() == EventType.ADDSCENE) {
			if (event.isSuccess()) {
				mScenes.add((SceneInfo) event.getData());
				content_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mCustomeAdapter.notifyDataSetChanged();
					}
				});
			}
		} else if (event.getType() == EventType.EDITSCENE) {
			if (event.isSuccess()) {
				SceneInfo sceneInfo = (SceneInfo) event.getData();
				for (int i = 0; i < mScenes.size(); i++) {
					if (mScenes.get(i).getSid() == sceneInfo.getSid()) {
						mScenes.remove(i);
						mScenes.add(i, sceneInfo);
						break;
					}
				}
				content_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mCustomeAdapter.notifyDataSetChanged();
					}
				});
			}
		} else if (event.getType() == EventType.DELSCENE) {
			if (event.isSuccess()) {
				int sid = (Integer) event.getData();
				for (int i = 0; i < mScenes.size(); i++) {
					if (mScenes.get(i).getSid() == sid) {
						mScenes.remove(i);
						break;
					}
				}
				content_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mCustomeAdapter.notifyDataSetChanged();
					}
				});
			}
		} else if (event.getType() == EventType.DOSCENE) {
			if (event.isSuccess()) {
				final int sid = (Integer) event.getData();

				content_view.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (sid < 0) {
							if (sid == -1)
								Toast.makeText(getActivity(), "全部布防 已应用",
										Toast.LENGTH_SHORT).show();
							if (sid == -2)
								Toast.makeText(getActivity(), "全部撤防 已应用",
										Toast.LENGTH_SHORT).show();
						} else {
							for (int i = 0; i < mScenes.size(); i++) {
								if (mScenes.get(i).getSid() == sid) {
									Toast.makeText(
											getActivity(),
											mScenes.get(i).getScnname()
													+ " 已应用",
											Toast.LENGTH_SHORT).show();
									break;
								}
							}
						}
					}
				});
			}
		}
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
			SceneLinkageManager.getInstance().DelScene(
					currentSceneInfo.getSid());
		} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
			LibjingleSendManager.getInstance().DelScene(
					currentSceneInfo.getSid());
		}
	}

}
