package com.gdgl.manager;

import java.util.HashMap;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.scene.SceneDevice;
import com.gdgl.mydata.scene.SceneInfo;
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
			mDateHelper.emptyTable(mSqLiteDatabase, DataHelper.SCENE_DEVICES_TABLE);
			
			for(SceneInfo sceneInfo:mSceneInfoList) {
				mSqLiteDatabase.insert(DataHelper.SCENE_TABLE, null,
						sceneInfo.convertContentValues());

				List<SceneDevice> sceneDevicesList = UiUtils
						.parseActionParamsToSceneDevices(sceneInfo.getSid(),
								sceneInfo.getScnaction());
				for (SceneDevice sceneDevice : sceneDevicesList) {
					mSqLiteDatabase.insert(DataHelper.SCENE_DEVICES_TABLE,
							null, sceneDevice.convertContentValues());
				}
			}
			mSqLiteDatabase.close();
			return null;
		}

	}
}
