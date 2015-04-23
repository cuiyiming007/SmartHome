package com.gdgl.util;

import java.util.LinkedList;
import java.util.List;

import com.gdgl.app.ApplicationController;
import com.gdgl.smarthome.R;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;


//单例模式fragment管理器 用于保存fragment状态
public class MyApplicationFragment extends Application{
	private List<Fragment> mFragmentList = new LinkedList<Fragment>();	//存储fragment的list
	private static MyApplicationFragment instance;	//静态单例
	private int mFragmentListSize = 0;
	private FragmentActivity mActivity;
	private FragmentManager fragmentManager;
	
	private MyApplicationFragment(){
		
	}
	
	public static MyApplicationFragment getInstance(){
		if(instance == null){
			instance = new MyApplicationFragment();
		}
		return instance;
	}
	
	public void setActivity(FragmentActivity activity){
		mActivity = activity;
	}
	
	public void removeLastFragment(){		
		if(mFragmentListSize > 1){
			fragmentManager = mActivity.getSupportFragmentManager();
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
		fragmentManager = mActivity.getSupportFragmentManager();
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
		fragmentManager = mActivity.getSupportFragmentManager();
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