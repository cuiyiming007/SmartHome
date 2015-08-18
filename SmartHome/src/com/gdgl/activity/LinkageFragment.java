package com.gdgl.activity;

/***
 * 最外层设备菜单
 */
import java.util.List;

import com.gc.materialdesign.views.ButtonFloat;
import com.gdgl.adapter.LinkageAdapter;
import com.gdgl.libjingle.LibjingleSendManager;
import com.gdgl.manager.CallbackManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.SceneLinkageManager;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.Constants;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.Linkage;
import com.gdgl.mydata.video.VideoNode;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gdgl.activity.LinkageDetailActivity;

public class LinkageFragment extends Fragment implements UIListener,
		Dialogcallback {

	List<Linkage> linkageList;
	List<VideoNode> videoList;

	View mView;
	ViewGroup nodevices;
	ListView linkage_list;
	ButtonFloat buttonFloat;
	Linkage currentLinkage;

	LinkageAdapter linkageAdapter;

	DataHelper mDateHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		CallbackManager.getInstance().addObserver(this);
		mDateHelper = new DataHelper((Context) getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.linkage_fragment, null);
		initview();
		return mView;
	}

	private void initview() {
		// TODO Auto-generated method stub
		buttonFloat = (ButtonFloat) mView.findViewById(R.id.buttonFloat);
		nodevices = (ViewGroup) mView.findViewById(R.id.nodevices);
		nodevices.setVisibility(View.GONE);
		linkage_list = (ListView) mView.findViewById(R.id.linkage_list);
		linkageAdapter = new LinkageAdapter(getActivity());
		linkage_list.setAdapter(linkageAdapter);
		setListeners();
		registerForContextMenu(linkage_list);
	}

	public void update() {
		SQLiteDatabase db = mDateHelper.getSQLiteDatabase();
		videoList = DataHelper.getVideoList((Context) getActivity(),
				mDateHelper);
		linkageList = DataHelper.queryForLinkageList(db,
				DataHelper.LINKAGE_TABLE, null, null);
		if (linkageList.size() > 0) {
			nodevices.setVisibility(View.GONE);
		} else {
			nodevices.setVisibility(View.VISIBLE);
		}
		linkageAdapter.setList(linkageList);
		linkageAdapter.setVideoList(videoList);
		linkageAdapter.notifyDataSetChanged();
		db.close();
	}

	private void setListeners() {
		buttonFloat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(LinkageDetailActivity.TYPE, 1);
				if (linkageList.size() > 0) {
					intent.putExtra(LinkageDetailActivity.INDEX, linkageList
							.get(linkageList.size() - 1).getLid());
				} else {
					intent.putExtra(LinkageDetailActivity.INDEX, 1);
				}
				intent.setClass(getActivity(), LinkageDetailActivity.class);
				startActivity(intent);
			}
		});
		linkage_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constants.PASS_OBJECT,
						linkageList.get(position));
				intent.putExtras(bundle);
				intent.putExtra(LinkageDetailActivity.TYPE, 2);
				intent.setClass(getActivity(), LinkageDetailActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.setHeaderTitle("删除联动");  //===去掉 编辑&
		menu.add(0, 1, 0, "删除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		currentLinkage = linkageList.get(position);
		int menuIndex = item.getItemId();
		if (1 == menuIndex) {
			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
					(Context) getActivity());
			mMyOkCancleDlg.setDialogCallback(this);
			mMyOkCancleDlg.setContent("确定要删除  "
					+ linkageList.get(position).getLnkname() + " 吗?");
			mMyOkCancleDlg.show();
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		update();
		super.onResume();
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		Event event = (Event) object;
		if (event.getType() == EventType.ADDLINKAGE) {
			if (event.isSuccess()) {
				Linkage mLinkage = (Linkage) event.getData();
				linkageList.add(mLinkage);
				linkage_list.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						linkageAdapter.notifyDataSetChanged();
					}
				});
			}
		} else if (event.getType() == EventType.EDITLINKAGE) {
			if (event.isSuccess()) {
				Linkage mLinkage = (Linkage) event.getData();
				for (int i = 0; i < linkageList.size(); i++) {
					if (linkageList.get(i).getLid() == mLinkage.getLid()) {
						linkageList.set(i, mLinkage);
						break;
					}
				}
				linkage_list.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						linkageAdapter.notifyDataSetChanged();
					}
				});
			}
		} else if (event.getType() == EventType.DELETELINKAGE) {
			if (event.isSuccess()) {
				int lid = (Integer) event.getData();
				for (int i = 0; i < linkageList.size(); i++) {
					if (linkageList.get(i).getLid() == lid) {
						linkageList.remove(i);
						break;
					}
				}
				linkage_list.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						linkageAdapter.notifyDataSetChanged();
					}
				});
			}
		} else if (event.getType() == EventType.ENABLELINKAGE) {
			if (event.isSuccess()) {
				int[] ints = (int[]) event.getData();
				int lid = ints[0];
				int enable = ints[1];
				for (int i = 0; i < linkageList.size(); i++) {
					if (linkageList.get(i).getLid() == lid) {
						linkageList.get(i).setEnable(enable);
						break;
					}
				}
				linkage_list.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						linkageAdapter.notifyDataSetChanged();
					}
				});
			}
		}
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		if (NetworkConnectivity.networkStatus == NetworkConnectivity.LAN) {
			SceneLinkageManager.getInstance().DeleteLinkage(
					currentLinkage.getLid());
		} else if (NetworkConnectivity.networkStatus == NetworkConnectivity.INTERNET) {
			LibjingleSendManager.getInstance().DeleteLinkage(
					currentLinkage.getLid());
		}
	}

}
