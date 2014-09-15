package com.gdgl.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdgl.activity.SmartHome.refreshAdapter;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.model.DevicesGroup;
import com.gdgl.model.DevicesModel;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.SimpleResponseData;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.DispatchOperator;
import com.gdgl.util.ExpandCollapseAnimation;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;
import com.gdgl.util.UiUtils;

public class CommonUseFragment extends Fragment implements refreshAdapter,
		Dialogcallback, UIListener {

	public static final int REGION = 0;
	public static final int SCENE = 1;
	public static final int DEV = 2;

	public static final int STOP = 3;

	private static final int DURATION = 500;

	GridView content_view;
	View mView;

	List<StringTag> mList;
	private int deletePostion = -1;

	CustomeAdapter regionAdap, sceneAdap, devAdap;

	ViewGroup nodevices;

	public class StringTag {
		public String name;
		public String tag;

		public StringTag(String s1, String s2) {
			name = s1;
			tag = s2;
		}
	}

	List<String> mRegion, mScene, mIeees;
	List<SimpleDevicesModel> mDev;

	RelativeLayout region_lay, scene_lay, dev_lay;
	ImageView up_down_region, up_down_scene, up_down_dev;
	LinearLayout region_content, scene_content, dev_content;

	TextView no_region, no_scene, no_dev;
	GridView region_list, scene_list, dev_list;

	boolean region_expn = false;
	boolean scene_expn = false;
	boolean dev_expn = false;

	DataHelper mDh;

	HashMap<String, View> mViewGroupMap;
	HashMap<View, String> mViewToIees;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		mDh = new DataHelper((Context) getActivity());
		mViewGroupMap = new HashMap<String, View>();
		mViewToIees = new HashMap<View, String>();
		initRegiondata();
		initScenedata();
		initDevdata();
	}

	private void initDevdata() {
		// TODO Auto-generated method stub
		getFromSharedPreferences.setsharedPreferences((Context) getActivity());
		if (null != mIeees) {
			mIeees = null;

		}
		if (null != mDev) {
			mDev = null;
		}
		mIeees = new ArrayList<String>();
		mDev = new ArrayList<SimpleDevicesModel>();
		if (null == devAdap) {
			devAdap = new CustomeAdapter(DEV, (Context) getActivity());
		}
		String comm = getFromSharedPreferences.getCommonUsed();
		if (null != comm && !comm.trim().equals("")) {
			String[] result = comm.split("@@");
			for (String string : result) {
				if (!string.trim().equals("")) {
					if (string.indexOf(UiUtils.DEVICES_FLAG) == 0) {
						mIeees.add(string.replace(UiUtils.DEVICES_FLAG, "")
								.trim());
					}
				}
			}
		}
		DataHelper dh = new DataHelper((Context) getActivity());

		List<SimpleDevicesModel> mtem;

		String where = " ieee=? ";

		for (String s : mIeees) {
			String[] args = { s };
			mtem = DataUtil
					.getDevices((Context) getActivity(), dh, args, where);
			if (null != mtem && mtem.size() > 0) {
				for (SimpleDevicesModel model : mtem) {
					mDev.add(model);
				}
			}
		}
	}

	private void initScenedata() {
		// TODO Auto-generated method stub
		getFromSharedPreferences.setsharedPreferences((Context) getActivity());
		if (null != mScene) {
			mScene = null;
		}
		if (null == sceneAdap) {
			sceneAdap = new CustomeAdapter(SCENE, (Context) getActivity());
		}
		mScene = new ArrayList<String>();
		String comm = getFromSharedPreferences.getCommonUsed();
		if (null != comm && !comm.trim().equals("")) {
			String[] result = comm.split("@@");
			for (String string : result) {
				if (!string.trim().equals("")) {
					if (string.indexOf(UiUtils.SCENE_FLAG) == 0) {
						mScene.add(string.replace(UiUtils.SCENE_FLAG, "")
								.trim());
					}
				}
			}
		}
	}

	private void initRegiondata() {
		// TODO Auto-generated method stub
		getFromSharedPreferences.setsharedPreferences((Context) getActivity());
		if (null != mRegion) {
			mRegion = null;
		}
		if (null == regionAdap) {
			regionAdap = new CustomeAdapter(REGION, (Context) getActivity());
		}
		mRegion = new ArrayList<String>();
		String comm = getFromSharedPreferences.getCommonUsed();
		if (null != comm && !comm.trim().equals("")) {
			String[] result = comm.split("@@");
			for (String string : result) {
				if (!string.trim().equals("")) {
					if (string.indexOf(UiUtils.REGION_FLAG) == 0) {
						mRegion.add(string.replace(UiUtils.REGION_FLAG, "")
								.trim());
					}
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.common_used_feagment, null);
		initview();
		return mView;
	}

	private void initview() {
		// TODO Auto-generated method stub

		region_lay = (RelativeLayout) mView.findViewById(R.id.region);
		scene_lay = (RelativeLayout) mView.findViewById(R.id.scene);
		dev_lay = (RelativeLayout) mView.findViewById(R.id.devices);

		up_down_region = (ImageView) mView.findViewById(R.id.up_down_region);
		up_down_scene = (ImageView) mView.findViewById(R.id.up_down_scene);
		up_down_dev = (ImageView) mView.findViewById(R.id.up_down_devices);

		region_content = (LinearLayout) mView.findViewById(R.id.region_content);
		scene_content = (LinearLayout) mView.findViewById(R.id.scene_content);
		dev_content = (LinearLayout) mView.findViewById(R.id.dev_content);

		no_region = (TextView) mView.findViewById(R.id.no_region);
		no_scene = (TextView) mView.findViewById(R.id.no_scene);
		no_dev = (TextView) mView.findViewById(R.id.no_devices);

		region_list = (GridView) mView.findViewById(R.id.region_list);
		scene_list = (GridView) mView.findViewById(R.id.scene_list);
		dev_list = (GridView) mView.findViewById(R.id.dev_list);

		expnRegion(true);
		hideScene(false);
		hideDev(false);

		registerForContextMenu(region_list);
		registerForContextMenu(scene_list);
		registerForContextMenu(dev_list);

		region_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (region_expn) {
					hideRegion(true);
				} else {
					if (scene_expn) {
						hideScene(true);
					}
					if (dev_expn) {
						hideDev(true);
					}
					expnRegion(true);
				}
			}
		});

		scene_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (scene_expn) {
					hideScene(true);
				} else {
					if (region_expn) {
						hideRegion(true);
					}
					if (dev_expn) {
						hideDev(true);
					}
					expnScene(true);
				}
			}
		});

		dev_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (dev_expn) {
					hideDev(true);
				} else {
					if (region_expn) {
						hideRegion(true);
					}
					if (scene_expn) {
						hideScene(true);
					}
					expnDev(true);
				}
			}
		});

	}

	private void hideRegion(boolean hasAnim) {
		// TODO Auto-generated method stub
		region_expn = false;
		up_down_region.setImageResource(R.drawable.xia);
		if (hasAnim) {
			Animation anim = new ExpandCollapseAnimation(region_content,
					ExpandCollapseAnimation.COLLAPSE);
			anim.setDuration(DURATION);
			region_content.startAnimation(anim);
		} else {
			region_content.setVisibility(View.GONE);
		}
	}

	private void hideScene(boolean hasAnim) {
		// TODO Auto-generated method stub
		scene_expn = false;
		up_down_scene.setImageResource(R.drawable.xia);

		if (hasAnim) {
			Animation anim = new ExpandCollapseAnimation(scene_content,
					ExpandCollapseAnimation.COLLAPSE);
			anim.setDuration(DURATION);
			scene_content.startAnimation(anim);
		} else {
			scene_content.setVisibility(View.GONE);
		}
	}

	private void hideDev(boolean hasAnim) {
		// TODO Auto-generated method stub
		dev_expn = false;
		up_down_dev.setImageResource(R.drawable.xia);
		if (hasAnim) {
			Animation anim = new ExpandCollapseAnimation(dev_content,
					ExpandCollapseAnimation.COLLAPSE);
			anim.setDuration(DURATION);
			dev_content.startAnimation(anim);
		} else {
			dev_content.setVisibility(View.GONE);
		}
	}

	private void expnRegion(boolean hasAnim) {
		// TODO Auto-generated method stub
		region_expn = true;
		up_down_region.setImageResource(R.drawable.shang);
		if (hasAnim) {
			Animation anim = new ExpandCollapseAnimation(region_content,
					ExpandCollapseAnimation.EXPAND);
			anim.setDuration(DURATION);
			region_content.startAnimation(anim);
		} else {
			region_content.setVisibility(View.VISIBLE);
		}
		if (null != mRegion && mRegion.size() > 0) {
			no_region.setVisibility(View.GONE);
			region_list.setVisibility(View.VISIBLE);
			region_list.setLayoutAnimation(UiUtils
					.getAnimationController((Context) getActivity()));
			regionAdap.setString(mRegion);
			region_list.setAdapter(regionAdap);
			region_list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent i = new Intent((Context) getActivity(),
							RegionDevicesActivity.class);
					i.putExtra(RegionDevicesActivity.REGION_NAME,
							mRegion.get(arg2).trim());
					startActivity(i);
				}

			});
			regionAdap.notifyDataSetChanged();
			return;
		}
		no_region.setVisibility(View.VISIBLE);
		region_list.setVisibility(View.GONE);
	}

	private void expnScene(boolean hasAnim) {
		// TODO Auto-generated method stub
		scene_expn = true;
		up_down_scene.setImageResource(R.drawable.shang);
		if (hasAnim) {
			Animation anim = new ExpandCollapseAnimation(scene_content,
					ExpandCollapseAnimation.EXPAND);
			anim.setDuration(DURATION);
			scene_content.startAnimation(anim);
		} else {
			scene_content.setVisibility(View.VISIBLE);
		}
		if (null != mScene && mScene.size() > 0) {
			no_scene.setVisibility(View.GONE);
			scene_list.setVisibility(View.VISIBLE);
			scene_list.setLayoutAnimation(UiUtils
					.getAnimationController((Context) getActivity()));
			sceneAdap.setString(mScene);
			scene_list.setAdapter(sceneAdap);
			scene_list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					ViewGroup vg = (ViewGroup) arg1;
					ImageView sceneimg, refeeshImg;
					sceneimg = (ImageView) vg.findViewById(R.id.dev_img);
					refeeshImg = (ImageView) vg.findViewById(R.id.refresh_img);
					refeeshImg.setVisibility(View.VISIBLE);
					sceneimg.setVisibility(View.GONE);
					Animation hyperspaceJumpAnimation = AnimationUtils
							.loadAnimation((Context) getActivity(),
									R.anim.loading_animation);

					refeeshImg.startAnimation(hyperspaceJumpAnimation);
					List<SimpleDevicesModel> mLs = null;
					String sce = mScene.get(arg2);
					if (!sce.trim().equals("")) {
						mLs = DataUtil.getScenesDevices(
								(Context) getActivity(), mDh, sce);
					}
					SceneDevicesActivity.OperatorDevices so;
					List<SceneDevicesActivity.OperatorDevices> ml = new ArrayList<SceneDevicesActivity.OperatorDevices>();
					for (SimpleDevicesModel sd : mLs) {
						DevicesGroup ds = getModelByID(sd.getmIeee());
						if (null != ds) {
							so = new SceneDevicesActivity.OperatorDevices(ds
									.getDevicesState(), ds.getDevicesValue(),
									sd);
							ml.add(so);
						}
					}
					DispatchOperator dp = new DispatchOperator(
							(Context) getActivity(), ml);
					dp.operator();
					ContentValues cv;
					String where = " group_name=? ";
					String[] args = { sce };
					cv = new ContentValues();
					cv.put(DevicesGroup.GROUP_STATE, 1);
					SQLiteDatabase db = mDh.getSQLiteDatabase();
					mDh.update(db, DataHelper.GROUP_TABLE, cv, where, args);
					mDh.close(db);
					Message msg = Message.obtain();
					msg.what = SCENE;
					msg.obj = arg1;
					mHandler.sendMessageDelayed(msg, 2000);
				}

			});
			sceneAdap.notifyDataSetChanged();
			return;
		}
		no_scene.setVisibility(View.VISIBLE);
		scene_list.setVisibility(View.GONE);
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			ViewGroup vg = (ViewGroup) msg.obj;
			switch (what) {
			case SCENE:
				ImageView sceneimg,
				refeeshImg;
				sceneimg = (ImageView) vg.findViewById(R.id.dev_img);
				refeeshImg = (ImageView) vg.findViewById(R.id.refresh_img);
				refeeshImg.clearAnimation();
				refeeshImg.setVisibility(View.GONE);
				sceneimg.setVisibility(View.VISIBLE);
				break;
			case DEV:
				ImageView devimg,
				devrefeeshImg;
				TextView devstate;
				devimg = (ImageView) vg.findViewById(R.id.dev_img);
				devrefeeshImg = (ImageView) vg.findViewById(R.id.refresh_img);

				devstate = (TextView) vg.findViewById(R.id.dev_state);

				String ieee = mViewToIees.get((View) msg.obj);

				SimpleDevicesModel sd = getDevicesByIeee(ieee);
				if (null != sd) {
					setTextViewContent(sd, devstate);
				}
				devrefeeshImg.clearAnimation();
				devrefeeshImg.setVisibility(View.GONE);
				devimg.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.setHeaderTitle("删除");
		menu.add(0, 1, 0, "删除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		deletePostion = position;
		int menuIndex = item.getItemId();
		if (region_expn) {
			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
					(Context) getActivity());
			mMyOkCancleDlg.setDialogCallback(CommonUseFragment.this);
			mMyOkCancleDlg.setContent("确定要删除区域" + mRegion.get(position) + "吗?");
			mMyOkCancleDlg.show();
		} else if (scene_expn) {
			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
					(Context) getActivity());
			mMyOkCancleDlg.setDialogCallback(CommonUseFragment.this);
			mMyOkCancleDlg.setContent("确定要删除场景" + mScene.get(position) + "吗?");
			mMyOkCancleDlg.show();
		} else if (dev_expn) {
			MyOkCancleDlg mMyOkCancleDlg = new MyOkCancleDlg(
					(Context) getActivity());
			mMyOkCancleDlg.setDialogCallback(CommonUseFragment.this);
			mMyOkCancleDlg.setContent("确定要删除设备"
					+ mDev.get(position).getmUserDefineName() + "吗?");
			mMyOkCancleDlg.show();
		}
		return super.onContextItemSelected(item);
	}

	private DevicesGroup getModelByID(String ieee) {
		return DataUtil.getOneScenesDevices((Context) getActivity(), mDh, ieee);
	}

	private void expnDev(boolean hasAnim) {
		// TODO Auto-generated method stub
		dev_expn = true;
		up_down_dev.setImageResource(R.drawable.shang);
		if (hasAnim) {
			Animation anim = new ExpandCollapseAnimation(dev_content,
					ExpandCollapseAnimation.EXPAND);
			anim.setDuration(DURATION);
			dev_content.startAnimation(anim);
		} else {
			dev_content.setVisibility(View.VISIBLE);
		}
		if (null != mIeees && mIeees.size() > 0) {
			no_dev.setVisibility(View.GONE);
			dev_list.setVisibility(View.VISIBLE);
			devAdap.setString(mIeees);
			dev_list.setAdapter(devAdap);
			dev_list.setLayoutAnimation(UiUtils
					.getAnimationController((Context) getActivity()));
			dev_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					ViewGroup vg = (ViewGroup) view;
					ImageView sceneimg, refeeshImg;
					sceneimg = (ImageView) vg.findViewById(R.id.dev_img);
					refeeshImg = (ImageView) vg.findViewById(R.id.refresh_img);
					refeeshImg.setVisibility(View.VISIBLE);
					sceneimg.setVisibility(View.GONE);
					Animation hyperspaceJumpAnimation = AnimationUtils
							.loadAnimation((Context) getActivity(),
									R.anim.loading_animation);

					refeeshImg.startAnimation(hyperspaceJumpAnimation);

					SimpleDevicesModel smd = getDevicesByIeee(mIeees
							.get(position));
					SceneDevicesActivity.OperatorDevices so = null;
					List<SceneDevicesActivity.OperatorDevices> ml = new ArrayList<SceneDevicesActivity.OperatorDevices>();
					DevicesGroup ds = getModelByID(smd.getmIeee());
					if (null != ds) {
						so = new SceneDevicesActivity.OperatorDevices(ds
								.getDevicesState(), ds.getDevicesValue(), smd);
						ml.add(so);
					}
					DispatchOperator dp = new DispatchOperator(
							(Context) getActivity(), ml);
					dp.operator();

					mViewGroupMap.put(mIeees.get(position), view);

					Message msg = Message.obtain();
					msg.what = SCENE;
					msg.obj = view;
					mHandler.sendMessageDelayed(msg, 3000);

				}
			});
			devAdap.notifyDataSetChanged();
			return;
		}
		no_dev.setVisibility(View.VISIBLE);
		dev_list.setVisibility(View.GONE);
	}

	protected SimpleDevicesModel getDevicesByIeee(String iee) {
		// TODO Auto-generated method stub
		String[] args = { iee };
		String where = " ieee=? ";
		Context c = (Context) getActivity();
		List<SimpleDevicesModel> ls = DataUtil.getDevices(c, new DataHelper(c),
				args, where);
		SimpleDevicesModel smd = null;
		if (null != ls && ls.size() > 0) {
			smd = ls.get(0);
		}
		return smd;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.i("onResume", "onResume");
		initRegiondata();
		initScenedata();
		initDevdata();
		if (region_expn) {
			expnRegion(false);
		} else if (scene_expn) {
			expnScene(false);
		} else if (dev_expn) {
			expnDev(false);
		}
		super.onResume();
	}

	private class CustomeAdapter extends BaseAdapter {

		private List<String> mString;
		private int Type;
		private Context mContext;
		private static final int ITEM_TYPE_COUNT = 3;

		public CustomeAdapter(int type, Context c) {
			Type = type;
			mContext = c;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mString.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mString.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return Type;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return ITEM_TYPE_COUNT;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder mViewHolder;
			String s = mString.get(position);

			int type = getItemViewType(position);
			if (null == convertView) {
				mViewHolder = new ViewHolder();
				switch (type) {
				case REGION:
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.gridview_item, null);
					convertView = LayoutInflater.from((Context) getActivity())
							.inflate(R.layout.gridview_item, null);
					mViewHolder.funcImg = (ImageView) convertView
							.findViewById(R.id.func_img);
					mViewHolder.funcText = (TextView) convertView
							.findViewById(R.id.func_name);
					break;
				case SCENE:
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.scene_grid_item, null);
					mViewHolder.funcImg = (ImageView) convertView
							.findViewById(R.id.dev_img);
					mViewHolder.funcText = (TextView) convertView
							.findViewById(R.id.dev_name);
					mViewHolder.refeeshImg = (ImageView) convertView
							.findViewById(R.id.refresh_img);
					mViewHolder.funcImg.setTag(mViewHolder.refeeshImg);
					break;
				case DEV:
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.devices_grid_item, null);
					mViewHolder.funcImg = (ImageView) convertView
							.findViewById(R.id.dev_img);
					mViewHolder.funcText = (TextView) convertView
							.findViewById(R.id.dev_name);
					mViewHolder.devState = (TextView) convertView
							.findViewById(R.id.dev_state);
					mViewHolder.refeeshImg = (ImageView) convertView
							.findViewById(R.id.refresh_img);
					break;
				default:
					break;
				}
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}

			if (Type == REGION) {
				mViewHolder.funcImg.setImageResource(R.drawable.region);
			} else if (Type == SCENE) {
				mViewHolder.funcImg.setImageResource(R.drawable.scene);
			} else if (Type == DEV) {
				String[] args = { s };
				String where = " ieee=? ";
				List<SimpleDevicesModel> ls = DataUtil.getDevices(mContext,
						new DataHelper(mContext), args, where);
				SimpleDevicesModel smd = null;
				if (null != ls && ls.size() > 0) {
					smd = ls.get(0);
				}
				if (null != smd) {
					if (DataHelper.IAS_ZONE_DEVICETYPE == smd.getmDeviceId()
							|| DataHelper.IAS_ACE_DEVICETYPE == smd
									.getmDeviceId()) {

						mViewHolder.funcImg.setImageResource(UiUtils
								.getDevicesSmallIconByModelId(smd.getmModelId()
										.trim()));
					} else if (smd.getmModelId().indexOf(
							DataHelper.Multi_key_remote_control) == 0) {
						mViewHolder.funcImg.setImageResource(UiUtils
								.getDevicesSmallIconForRemote(smd
										.getmDeviceId()));
					} else {
						mViewHolder.funcImg.setImageResource(UiUtils
								.getDevicesSmallIcon(smd.getmDeviceId()));
					}

					setTextViewContent(smd, mViewHolder.devState);
					mViewHolder.funcText.setText(smd.getmUserDefineName()
							.trim());
				}
			}
			if (Type != DEV) {
				mViewHolder.funcText.setText(s.trim());
			}

			return convertView;
		}

		class ViewHolder {
			ImageView funcImg;
			ImageView refeeshImg;
			TextView funcText;
			TextView devState;
		}

		public void setString(List<String> ls) {
			mString = null;
			mString = ls;
		}
	}

	@Override
	public void refreshFragment() {
		// TODO Auto-generated method stub
		Log.i("zgs", "refreshFragment->refreshFragment");
		if (region_expn) {
			initRegiondata();
			expnRegion(false);
		} else if (scene_expn) {
			initScenedata();
			expnScene(false);
		} else if (dev_expn) {
			initDevdata();
			expnDev(false);
		}
	}

	@Override
	public void dialogdo() {
		// TODO Auto-generated method stub
		getFromSharedPreferences.setsharedPreferences((Context) getActivity());
		StringBuilder sb = new StringBuilder();
		String sR = getFromSharedPreferences.getCommonUsed();
		String deleteString = "";
		List<String> rs = new ArrayList<String>();
		if (!(null == sR || sR.trim().equals(""))) {
			String[] com = sR.split("@@");
			for (String string : com) {
				if (!string.trim().equals("")) {
					rs.add(string);
				}
			}
		}

		if (region_expn) {
			deleteString = UiUtils.REGION_FLAG + mRegion.get(deletePostion);
			mRegion.remove(deletePostion);

		} else if (scene_expn) {
			deleteString = UiUtils.SCENE_FLAG + mScene.get(deletePostion);
			mScene.remove(deletePostion);
		} else if (dev_expn) {
			deleteString = UiUtils.DEVICES_FLAG + mIeees.get(deletePostion);
			mIeees.remove(deletePostion);
		}

		for (String string : rs) {
			if (!string.equals(deleteString)) {
				sb.append(string + "@@");
			}
		}
		getFromSharedPreferences.setCommonUsed(sb.toString());

		refreshFragment();
	}

	private void setTextViewContent(SimpleDevicesModel s, TextView view) {
		if (s.getmDeviceId() == DataHelper.ON_OFF_SWITCH_DEVICETYPE) {
			String state = "";
			String sss = s.getmOnOffStatus();
			String[] result = sss.split(",");
			for (String string : result) {
				if (string.trim().equals("1")) {
					state += "开 ";
				} else {
					state += "关 ";
				}
			}
			Log.i("tag", "tag->" + state);
			view.setText(state);
		} else if (s.getmDeviceId() == DataHelper.IAS_ZONE_DEVICETYPE) {
			if (s.getmOnOffStatus().trim().equals("0")) {
				view.setText("已布防");
			} else {
				view.setText("已撤防");
			}
		} else if (s.getmDeviceId() == DataHelper.LIGHT_SENSOR_DEVICETYPE) {
			view.setText("亮度: " + getFromSharedPreferences.getLight());
		} else if (s.getmDeviceId() == DataHelper.TEMPTURE_SENSOR_DEVICETYPE) {
			view.setText("温度: " + getFromSharedPreferences.getTemperature()
					+ "\n湿度: " + getFromSharedPreferences.getHumidity()+"%");
		} else {
			if (s.getmOnOffStatus().trim().equals("1")) {
				view.setText("开");
			} else {
				view.setText("关");
			}
		}
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		final Event event = (Event) object;
		EventType e = event.getType();

		if (EventType.ONOFFOUTPUTOPERATION == event.getType()
				|| EventType.IASWARNINGDEVICOPERATION == event.getType()
				|| EventType.MAINSOUTLETOPERATION == event.getType()) {

			if (event.isSuccess() == true) {
				// data maybe null
				SimpleResponseData data = (SimpleResponseData) event.getData();
				String ieee = data.getIeee();
				String ep = data.getEp();
				SimpleDevicesModel sd = getDevicesByIeee(ieee);
				if (sd != null) {
					boolean status = sd.getmOnOffStatus().trim().equals("1");
					ContentValues c = new ContentValues();
					c.put(DevicesModel.ON_OFF_STATUS, status ? "1" : "o");
					updateDevices(ieee, ep, c);
				}

				Message msg = Message.obtain();
				msg.what = DEV;
				msg.obj = mViewGroupMap.get(ieee);
				mViewGroupMap.remove(ieee);
				mViewToIees.put(mViewGroupMap.get(ieee), ieee);
				mHandler.sendMessage(msg);

			} else {

			}
		} else if (EventType.LIGHTSENSOROPERATION == event.getType()) {
			// data maybe null
			if (event.isSuccess()) {
				SimpleResponseData data = (SimpleResponseData) event.getData();
				String light = data.getParam1() + "Lux";
				getFromSharedPreferences.setLight(light);
				devAdap.notifyDataSetChanged();
			} else {
			}
		} else if (EventType.TEMPERATURESENSOROPERATION == event.getType()) {
			// data maybe null
			if (event.isSuccess()) {

				SimpleResponseData data = (SimpleResponseData) event.getData();
				String temperature = String.valueOf(Float.valueOf(data
						.getParam1().substring(0, data
						.getParam1().length()-2)) / 10 + "°C");
				getFromSharedPreferences.setTemperature(temperature);
				devAdap.notifyDataSetChanged();
			} else {
				Toast.makeText(getActivity(), "获取温度失败", 3000).show();
			}
		} else if (EventType.HUMIDITY == event.getType()) {
			if (event.isSuccess()) {
				SimpleResponseData data = (SimpleResponseData) event.getData();
				String humidity = String.valueOf(Float.valueOf(data
						.getParam1().substring(0, data
								.getParam1().length()-2)) / 10);
				getFromSharedPreferences.setHumidity(humidity);
				devAdap.notifyDataSetChanged();
			}
		}
	}

	public boolean updateDevices(String Ieee, String ep, ContentValues c) {
		// TODO Auto-generated method stub
		String where = " ieee = ? and ep = ?";
		String[] args = { Ieee, ep };
		SQLiteDatabase mSQLiteDatabase = mDh.getSQLiteDatabase();
		int result = mDh.update(mSQLiteDatabase, DataHelper.DEVICES_TABLE, c,
				where, args);
		// mDataHelper
		if (result >= 0) {
			return true;
		}
		return false;
	}

}
