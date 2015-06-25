package com.gdgl.manager;

import java.util.HashMap;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Linkage;
import com.gdgl.mydata.scene.SceneDevice;
import com.gdgl.mydata.scene.SceneInfo;
import com.gdgl.mydata.timing.TimingAction;
import com.gdgl.network.StringRequestChina;
import com.gdgl.network.VolleyOperation;
import com.gdgl.util.NetUtil;
import com.gdgl.util.UiUtils;

public class SceneLinkageManager extends Manger {

	private final static String TAG = "SceneLinkageManager";

	private static SceneLinkageManager instance;

	public static SceneLinkageManager getInstance() {
		if (instance == null) {
			instance = new SceneLinkageManager();
		}
		return instance;
	}

	// Scene API
	public void AddScene(String scnname, String scnaction, int scnindex) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("scnname", scnname);
		paraMap.put("scnaction", scnaction);
		paraMap.put("scnindex", Integer.toString(scnindex));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "AddScene.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void EditScene(String scnname, String scnaction, int scnindex,
			int sid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("scnname", scnname);
		paraMap.put("scnaction", scnaction);
		paraMap.put("scnindex", Integer.toString(scnindex));
		paraMap.put("sid", Integer.toString(sid));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "EditScene.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void DelScene(int sid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("sid", Integer.toString(sid));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "DelScene.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void DoScene(int sid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("sid", Integer.toString(sid));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "DoScene.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void GetSceneList() {
		String url = NetUtil.getInstance().getVideoURL(
				NetUtil.getInstance().IP, "GetSceneList.cgi");

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						new GetSceneListTask().execute(response);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	// Linkage API
	public void AddLinkage(String lnkname, String trgieee, String trgep,
			String trgcnd, String lnkact, int enable) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("lnkname", lnkname);
		paraMap.put("trgieee", trgieee);
		paraMap.put("trgep", trgep);
		paraMap.put("trgcnd", trgcnd);
		paraMap.put("lnkact", lnkact);
		paraMap.put("enable", Integer.toString(enable));
		String param = hashMap2ParamString(paraMap);
		Log.i("AddLinkage", param);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "AddLinkage.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void EditLinkage(String lnkname, String trgieee, String trgep,
			String trgcnd, String lnkact, int enable, int lid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("lnkname", lnkname);
		paraMap.put("trgieee", trgieee);
		paraMap.put("trgep", trgep);
		paraMap.put("trgcnd", trgcnd);
		paraMap.put("lnkact", lnkact);
		paraMap.put("enable", Integer.toString(enable));
		paraMap.put("lid", Integer.toString(lid));
		String param = hashMap2ParamString(paraMap);
		Log.i("EditLinkage", param);
		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "EditLinkage.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void DeleteLinkage(int lid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("lid", Integer.toString(lid));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "DelLinkage.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void GetLinkageList() {
		String url = NetUtil.getInstance().getVideoURL(
				NetUtil.getInstance().IP, "GetLinkageList.cgi");
		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						new GetLinkageListTask().execute(response);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void EnableLinkage(int enable, int lid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("enable", Integer.toString(enable));
		paraMap.put("lid", Integer.toString(lid));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "EnableLinkage.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	// Timing API
	public void AddTimeAction(String actname, String actpara, int actmode,
			String para1, int para2, String para3, int enable) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("actname", actname);
		paraMap.put("actpara", actpara);
		paraMap.put("actmode", Integer.toString(actmode));
		paraMap.put("para1", para1);
		paraMap.put("para2", Integer.toString(para2));
		paraMap.put("para3", para3);
		paraMap.put("enable", Integer.toString(enable));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "AddTimeAction.cgi", param);
		Log.i("", url);
		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void EditTimeAction(String actname, String actpara, int actmode,
			String para1, int para2, String para3, int enable, int tid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("actname", actname);
		paraMap.put("actpara", actpara);
		paraMap.put("actmode", Integer.toString(actmode));
		paraMap.put("para1", para1);
		paraMap.put("para2", Integer.toString(para2));
		paraMap.put("para3", para3);
		paraMap.put("tid", Integer.toString(tid));
		paraMap.put("enable", Integer.toString(enable));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "EditTimeAction.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void DelTimeAction(int tid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("tid", Integer.toString(tid));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "DelTimeAction.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void GetTimeActionList() {
		String url = NetUtil.getInstance().getVideoURL(
				NetUtil.getInstance().IP, "GetTimeActionList.cgi");
		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub
						new GetTimingActionListTask().execute(arg0);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}

	public void EnableTimeAction(int enable, int tid) {
		HashMap<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("enable", Integer.toString(enable));
		paraMap.put("tid", Integer.toString(tid));
		String param = hashMap2ParamString(paraMap);

		String url = NetUtil.getInstance().getCumstomURL(
				NetUtil.getInstance().IP, "EnableTimeAction.cgi", param);

		StringRequestChina req = new StringRequestChina(url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		ApplicationController.getInstance().addToRequestQueue(req);
	}
	
	class GetLinkageListTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<Linkage> mLinkageList = VolleyOperation
					.handleLinkageListString(params[0]);
			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSqLiteDatabase = mDateHelper.getSQLiteDatabase();
			mDateHelper.emptyTable(mSqLiteDatabase, DataHelper.LINKAGE_TABLE);

			for (Linkage linkage : mLinkageList) {
				mSqLiteDatabase.insert(DataHelper.LINKAGE_TABLE, null,
						linkage.convertContentValues());
			}
			mSqLiteDatabase.close();
			return null;
		}
	}

	class GetSceneListTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<SceneInfo> mSceneInfoList = VolleyOperation
					.handleSceneInfoListString(params[0]);
			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSqLiteDatabase = mDateHelper.getSQLiteDatabase();
			mDateHelper.emptyTable(mSqLiteDatabase, DataHelper.SCENE_TABLE);
			mDateHelper.emptyTable(mSqLiteDatabase,
					DataHelper.SCENE_DEVICES_TABLE);

			for (SceneInfo sceneInfo : mSceneInfoList) {
				mSqLiteDatabase.insert(DataHelper.SCENE_TABLE, null,
						sceneInfo.convertContentValues());

				if (!sceneInfo.getScnaction().isEmpty()) {
					List<SceneDevice> sceneDevicesList = UiUtils
							.parseActionParamsToSceneDevices(
									sceneInfo.getSid(),
									sceneInfo.getScnaction());
					for (SceneDevice sceneDevice : sceneDevicesList) {
						mSqLiteDatabase.insert(DataHelper.SCENE_DEVICES_TABLE,
								null, sceneDevice.convertContentValues());
					}
				}
			}
			mSqLiteDatabase.close();
			return null;
		}
	}

	class GetTimingActionListTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<TimingAction> mTimingActionsList = VolleyOperation
					.handleTimingActionListString(params[0]);
			DataHelper mDateHelper = new DataHelper(
					ApplicationController.getInstance());
			SQLiteDatabase mSqLiteDatabase = mDateHelper.getSQLiteDatabase();
			mDateHelper.emptyTable(mSqLiteDatabase,
					DataHelper.TIMINGACTION_TABLE);

			for (TimingAction timingAction : mTimingActionsList) {
				mSqLiteDatabase.insert(DataHelper.TIMINGACTION_TABLE, null,
						timingAction.convertContentValues());
			}
			mSqLiteDatabase.close();
			return null;
		}
	}
}
