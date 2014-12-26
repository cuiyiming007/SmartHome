package com.gdgl.util;

import java.util.LinkedList;
import java.util.List;

import com.gdgl.app.ApplicationController;
import com.gdgl.smarthome.R;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;


//单例模式fragment管理器 用于保存fragment状态
public class MyApplicationFragment extends Application{
	private List<Fragment> mFragmentList = new LinkedList<Fragment>();	//存储fragment的list
	private static MyApplicationFragment instance;	//静态单例
	private int mFragmentListSize = 0;
	private Activity mActivity;
	private FragmentManager fragmentManager;
	
	private MyApplicationFragment(){
		
	}
	
	public static MyApplicationFragment getInstance(){
		if(instance == null){
			instance = new MyApplicationFragment();
		}
		return instance;
	}
	
	public void setActivity(Activity activity){
		mActivity = activity;
	}
	
	public void removeLastFragment(){		
		if(mFragmentListSize > 1){
			fragmentManager = mActivity.getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.remove(mFragmentList.get(mFragmentListSize - 1));
			fragmentTransaction.show(mFragmentList.get(mFragmentListSize - 2));
			fragmentTransaction.commit();
			mFragmentList.remove(mFragmentListSize - 1);
		}
		updateFragmentListSize();
	}

	//存储开启的fragment
	public void addFragment(Fragment fragment){
		fragmentManager = mActivity.getFragmentManager();
		if(mFragmentListSize > 0){
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.hide(mFragmentList.get(mFragmentListSize - 1));
			fragmentTransaction.show(fragment);
			fragmentTransaction.commit();
		}
		mFragmentList.add(fragment);
		updateFragmentListSize();
	}
	
	public void clearFragment(){
		fragmentManager = mActivity.getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		for(Fragment fragment : mFragmentList){
			fragmentTransaction.remove(fragment);
		}
		fragmentTransaction.commit();
		mFragmentList.clear();
		updateFragmentListSize();
	}

	
	public void addNewTask(Fragment fragment){
		clearFragment();
		addFragment(fragment);
	}
	
	public void updateFragmentListSize(){
		mFragmentListSize = mFragmentList.size();
	}
	
	public int getFragmentListSize(){
		return mFragmentListSize;
	}
}