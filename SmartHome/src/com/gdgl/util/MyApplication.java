package com.gdgl.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

//单例模式activity管理器 用于安全退出应用
public class MyApplication extends Application {
	private List<Activity> mActivityList = new LinkedList<Activity>(); // 存储activity的list
	private static MyApplication instance; // 静态单例

	private MyApplication() {

	}

	public static MyApplication getInstance() {
		if (instance == null) {
			instance = new MyApplication();
		}
		return instance;
	}

	// 存储开启的activity
	public void addActivity(Activity activity) {
		mActivityList.add(activity);
	}

	public void removeActivity(Activity activity) {
		mActivityList.remove(activity);
	}

	// 逐一销毁activity
	public void finishActivity() {
		for (Activity activity : mActivityList) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}

	// 退出应用
	public void finishSystem() {
		finishActivity();
		System.exit(0);
	}
}