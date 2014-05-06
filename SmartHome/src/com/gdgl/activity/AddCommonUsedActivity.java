package com.gdgl.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.gdgl.adapter.AllDevicesAdapter;
import com.gdgl.adapter.AllDevicesAdapter.ViewHolder;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.UiUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AddCommonUsedActivity extends Activity {

	List<String> mRegion;
	List<String> mExitRegion;

	List<String> mScene;
	List<String> mExitScene;

	List<String> mDevices;
	List<String> mExitDevices;

	HashMap<String, SimpleDevicesModel> devices_map;

	Button mAdd;
	TextView no_region, no_scene, no_devices;

	TextView region_text, scene_text, devices_text;

	RelativeLayout region_lay, scene_lay, devices_lay;

	ListView region_list, scene_list, dev_list;

	public static final int REGION = 1;
	public static final int SCENE = 2;
	public static final int DEVICES = 3;

	int currentIndex = 2;

	List<TextView> lt;
	List<RelativeLayout> lr;

	CommonUsedAdapter mRegionAdapter, mSceneAdapter, mDevAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_commonused);
		initdata();
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		LinearLayout mBack = (LinearLayout) findViewById(R.id.goback);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mAdd = (Button) findViewById(R.id.add_use);
		mAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StringBuilder sb = new StringBuilder();
				getFromSharedPreferences
						.setharedPreferences(AddCommonUsedActivity.this);
				String sR = getFromSharedPreferences.getCommonUsed();
				List<String> rs=new ArrayList<String>();
				if (!(null == sR || sR.trim().equals(""))) {
					String[] com=sR.split("@@");
					for (String string : com) {
						if(!string.trim().equals("")){
							rs.add(string);
						}
					}
				}
				switch (currentIndex) {
				case 1:
					if (null != mExitRegion && mExitRegion.size() > 0) {
						for (String s : mExitRegion) {
							sb.append(UiUtils.REGION_FLAG + s + "@@");
						}
					}
					break;
				case 2:
					if (null != mExitScene && mExitScene.size() > 0) {
						for (String s : mExitScene) {
							sb.append(UiUtils.SCENE_FLAG + s + "@@");
						}
					}
					break;
				case 3:
					if (null != mExitDevices && mExitDevices.size() > 0) {
						for (String s : mExitDevices) {
							sb.append(UiUtils.DEVICES_FLAG + s + "@@");
						}
					}
					break;
				default:
					break;
				}
				
				String newR=sb.toString();
				if(!newR.trim().equals("")){
					String[] newA=newR.split("@@");
					for (String string : newA) {
						if(!string.trim().equals("")){
							if(!rs.contains(string)){
								rs.add(string);
							}
						}
					}
				}
				StringBuilder nsb=new StringBuilder();
				if(null!=rs && rs.size()>0){
					for (String string : rs) {
						if(!string.equals("")){
							nsb.append(string+"@@");
						}
					}
				}
				getFromSharedPreferences.setCommonUsed(nsb.toString());
				switch (currentIndex) {
				case 1:
					initRegion();
					break;
				case 2:
					initScene();
					break;
				case 3:
					initDevices();
					break;
				default:
					break;
				}
			}
		});

		region_list = (ListView) findViewById(R.id.region_list);

		scene_list = (ListView) findViewById(R.id.scene_list);

		dev_list = (ListView) findViewById(R.id.dev_list);

		no_region = (TextView) findViewById(R.id.no_region);
		no_scene = (TextView) findViewById(R.id.no_scene);
		no_devices = (TextView) findViewById(R.id.no_devices);

		Log.i("tag", "zgs-> mRegion.size()=" + mRegion.size());
		Log.i("tag", "zgs-> mScene.size()=" + mScene.size());

		if (null == mRegion || mRegion.size() == 0) {
			no_region.setVisibility(View.VISIBLE);
			region_list.setVisibility(View.GONE);
		} else {
			region_list.setAdapter(mRegionAdapter);
		}

		if (null == mScene || mScene.size() == 0) {
			no_scene.setVisibility(View.VISIBLE);
			scene_list.setVisibility(View.GONE);
		} else {
			scene_list.setAdapter(mSceneAdapter);
		}

		if (null == mDevices || mDevices.size() == 0) {
			no_devices.setVisibility(View.VISIBLE);
			dev_list.setVisibility(View.GONE);
		} else {
			dev_list.setAdapter(mDevAdapter);
		}

		region_text = (TextView) findViewById(R.id.region_text);
		scene_text = (TextView) findViewById(R.id.scene_text);
		devices_text = (TextView) findViewById(R.id.devices_text);

		region_text.setTag(1);
		scene_text.setTag(2);
		devices_text.setTag(3);

		region_lay = (RelativeLayout) findViewById(R.id.region_lay);
		scene_lay = (RelativeLayout) findViewById(R.id.scene_lay);
		devices_lay = (RelativeLayout) findViewById(R.id.devices_lay);

		lt = new ArrayList<TextView>();
		lt.add(region_text);
		lt.add(scene_text);
		lt.add(devices_text);

		lr = new ArrayList<RelativeLayout>();
		lr.add(region_lay);
		lr.add(scene_lay);
		lr.add(devices_lay);

		region_text.setOnClickListener(new TextViewClickListeners());
		scene_text.setOnClickListener(new TextViewClickListeners());
		devices_text.setOnClickListener(new TextViewClickListeners());

	}

	private class TextViewClickListeners implements View.OnClickListener {

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TextView tv = (TextView) v;
			int tag = (Integer) tv.getTag();
			if (tag != currentIndex) {
				lt.get(currentIndex - 1).setBackground(null);
				lr.get(currentIndex - 1).setVisibility(View.GONE);
				tv.setBackground(getResources().getDrawable(
						R.drawable.dlg_button_press));
				lr.get(tag - 1).setVisibility(View.VISIBLE);
				switch (tag) {
				case 1:
					initRegion();
					break;
				case 2:
					initScene();
					break;
				case 3:
					initDevices();
					break;
				default:
					break;
				}
				currentIndex = tag;
			}
		}

	}

	private void initdata() {
		// TODO Auto-generated method stub

		initRegion();
		initScene();
		initDevices();

	}

	private void initDevices() {
		// TODO Auto-generated method stub
		devices_map = new HashMap<String, SimpleDevicesModel>();
		mDevices = new ArrayList<String>();
		mExitDevices = new ArrayList<String>();
		getFromSharedPreferences
				.setharedPreferences(AddCommonUsedActivity.this);
		String comm = getFromSharedPreferences.getCommonUsed();
		if (null != comm && !comm.trim().equals("")) {
			String[] result = comm.split("@@");
			for (String string : result) {
				if (!string.trim().equals("")) {
					if (string.indexOf(UiUtils.DEVICES_FLAG) == 0) {
						mExitDevices.add(string.replace(UiUtils.DEVICES_FLAG,
								"").trim());
					}
				}
			}
		}
		
		List<SimpleDevicesModel> mTempList = DataUtil.getDevices(
				AddCommonUsedActivity.this, new DataHelper(
						AddCommonUsedActivity.this), null, null);
		for (SimpleDevicesModel simpleDevicesModel : mTempList) {
			if (null == mExitDevices || mExitDevices.size() == 0) {
				mDevices.add(simpleDevicesModel.getmUserDefineName().trim());
				devices_map.put(simpleDevicesModel.getmUserDefineName().trim(),
						simpleDevicesModel);
			} else {
				for (String s : mExitDevices) {
					Log.i("zgs", "zgs->s="+s+"  mExitDevices.size()="+mExitDevices.size());
				}
				if (!mExitDevices.contains(simpleDevicesModel
						.getmIeee().trim())) {
					mDevices.add(simpleDevicesModel.getmUserDefineName().trim());
					devices_map.put(simpleDevicesModel.getmUserDefineName()
							.trim(), simpleDevicesModel);
				}
			}

		}
		
		if (null == mDevices || mDevices.size() == 0) {
			if (null != no_devices) {
				no_devices.setVisibility(View.VISIBLE);
				dev_list.setVisibility(View.GONE);
			}
		} else {
			if (null == mDevAdapter) {
				mDevAdapter = new CommonUsedAdapter(DEVICES,
						AddCommonUsedActivity.this);
				mDevAdapter.setList(mDevices);
			} else {
				mDevAdapter.setList(mDevices);
				mDevAdapter.notifyDataSetChanged();
			}
		}

	}

	private void initScene() {
		// TODO Auto-generated method stub
		mScene = new ArrayList<String>();
		mExitScene = new ArrayList<String>();

		getFromSharedPreferences
				.setharedPreferences(AddCommonUsedActivity.this);
		String comm = getFromSharedPreferences.getCommonUsed();
		if (null != comm && !comm.trim().equals("")) {
			String[] result = comm.split("@@");
			for (String string : result) {
				if (!string.trim().equals("")) {
					if (string.indexOf(UiUtils.SCENE_FLAG) == 0) {
						mExitScene.add(string.replace(UiUtils.SCENE_FLAG, "")
								.trim());
					}
				}
			}
		}

		List<String> temp = new ArrayList<String>();
		temp = DataUtil.getScenes(AddCommonUsedActivity.this, null);

		for (String string : temp) {
			if (!mExitScene.contains(string)) {
				mScene.add(string);
				Log.i("tag", "zgs-> add to scene " + string);
			}
		}

		if (null == mScene || mScene.size() == 0) {
			if (null != no_scene) {
				no_scene.setVisibility(View.VISIBLE);
				scene_list.setVisibility(View.GONE);
			}
		} else {
			if (null == mSceneAdapter) {
				mSceneAdapter = new CommonUsedAdapter(SCENE,
						AddCommonUsedActivity.this);
				mSceneAdapter.setList(mScene);
			} else {
				mSceneAdapter.setList(mScene);
				mSceneAdapter.notifyDataSetChanged();
			}
		}
	}

	private void initRegion() {
		// TODO Auto-generated method stub
		mRegion = new ArrayList<String>();
		mExitRegion = new ArrayList<String>();
		getFromSharedPreferences
				.setharedPreferences(AddCommonUsedActivity.this);
		String comm = getFromSharedPreferences.getCommonUsed();
		if (null != comm && !comm.trim().equals("")) {
			String[] result = comm.split("@@");
			for (String string : result) {
				if (!string.trim().equals("")) {
					if (string.indexOf(UiUtils.REGION_FLAG) == 0) {
						mExitRegion.add(string.replace(UiUtils.REGION_FLAG, "")
								.trim());
					}
				}
			}
		}

		List<String> temp = new ArrayList<String>();
		String[] mregion = null;
		String reg = getFromSharedPreferences.getRegion();
		if (null != reg && !reg.trim().equals("")) {
			mregion = reg.split("@@");
		}
		if (null != mregion) {
			for (String string : mregion) {
				if (!string.equals("")) {
					temp.add(string);
				}
			}
		}
		for (String string : temp) {
			if (!mExitRegion.contains(string)) {
				mRegion.add(string);
				Log.i("tag", "zgs-> add to region " + string);
			}
		}

		if (null == mRegion || mRegion.size() == 0) {
			if (null != no_region) {
				no_region.setVisibility(View.VISIBLE);
				region_list.setVisibility(View.GONE);
			}
		} else {
			if (null == mRegionAdapter) {
				mRegionAdapter = new CommonUsedAdapter(REGION,
						AddCommonUsedActivity.this);
				mRegionAdapter.setList(mRegion);
			} else {
				mRegionAdapter.setList(mRegion);
				mRegionAdapter.notifyDataSetChanged();
			}
		}
	}

	private class CommonUsedAdapter extends BaseAdapter {

		private List<String> mList;
		private int type;
		private Context mContext;
		private Map<Integer, Boolean> isSelected;

		public CommonUsedAdapter(int t, Context c) {
			// mList = m;
			type = t;
			mContext = c;
		}

		public void setList(List<String> ls) {
			mList = null;
			mList = ls;
			isSelected = new HashMap<Integer, Boolean>();
			for (int i = 0; i < mList.size(); i++) {
				isSelected.put(i, false);
			}
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
			if (null != mList && mList.size() > 0) {
				return mList.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			final String s = mList.get(position);
			final SimpleDevicesModel sModel = devices_map.get(s);
			ViewHolder mViewHolder;
			if (null == convertView) {
				mViewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.common_used_item, null);
				mViewHolder.use_img = (ImageView) convertView
						.findViewById(R.id.used_img);
				mViewHolder.use_text = (TextView) convertView
						.findViewById(R.id.used_name);
				mViewHolder.selected = (CheckBox) convertView
						.findViewById(R.id.selected);
				convertView.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			Log.i("zgs", "zgs->s=" + s);
			mViewHolder.use_text.setText(s);
			if (type == REGION) {
				mViewHolder.use_img.setImageResource(R.drawable.region);
			} else if (type == SCENE) {
				mViewHolder.use_img.setImageResource(R.drawable.scene);
			} else if (type == DEVICES) {
				mViewHolder.use_img.setImageResource(R.drawable.scene);
				if (DataHelper.IAS_ZONE_DEVICETYPE == sModel.getmDeviceId()
						|| DataHelper.IAS_ACE_DEVICETYPE == sModel
								.getmDeviceId()) {

					mViewHolder.use_img.setImageResource(UiUtils
							.getDevicesSmallIconByModelId(sModel.getmModelId()
									.trim()));
				} else if (sModel.getmModelId().indexOf(
						DataHelper.Multi_key_remote_control) == 0) {
					mViewHolder.use_img
							.setImageResource(UiUtils
									.getDevicesSmallIconForRemote(sModel
											.getmDeviceId()));
				} else {
					mViewHolder.use_img.setImageResource(UiUtils
							.getDevicesSmallIcon(sModel.getmDeviceId()));
				}
			}
			mViewHolder.selected
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							isSelected.put(position, isChecked);
							switch (type) {
							case REGION:
								if (isChecked) {
									if (!mExitRegion.contains(s)) {
										mExitRegion.add(s);
									}
								} else if (mExitRegion.contains(s)) {
									mExitRegion.remove(s);
								}
								break;
							case SCENE:
								if (isChecked) {
									if (!mExitScene.contains(s)) {
										mExitScene.add(s);
									}
								} else if (mExitScene.contains(s)) {
									mExitScene.remove(s);
								}
								break;
							case DEVICES:
								if (isChecked) {
									if (!mExitDevices.contains(sModel
											.getmIeee().trim())) {
										mExitDevices.add(sModel.getmIeee()
												.trim());
									}
								} else if (mExitDevices.contains(sModel
										.getmIeee().trim())) {
									mExitDevices.remove(sModel.getmIeee()
											.trim());
								}
								break;
							default:
								break;
							}
						}
					});
			mViewHolder.selected.setChecked(isSelected.get(position));
			return convertView;
		}

		class ViewHolder {
			ImageView use_img;
			TextView use_text;
			CheckBox selected;
		}

	}

}
