package com.gdgl.activity;

import android.app.Fragment;

import com.gdgl.activity.ShowDevicesGroupFragmentActivity.EditDevicesName;



public abstract class BaseControlFragment extends Fragment implements
		EditDevicesName {
	public SaveDevicesName mSaveDevicesName;
	public interface SaveDevicesName {
		public void saveDevicesName(String name);
	}
}
