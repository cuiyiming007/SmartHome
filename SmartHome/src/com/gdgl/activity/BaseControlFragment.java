package com.gdgl.activity;

import android.app.Fragment;
import android.content.ContentValues;

import com.gdgl.activity.ShowDevicesGroupFragmentActivity.EditDevicesName;
import com.gdgl.manager.UIListener;


public abstract class BaseControlFragment extends Fragment implements
		EditDevicesName,UIListener{
	public UpdateDevice mUpdateDevice;
	
	public interface UpdateDevice {
		public void saveDevicesName(String name);
		public boolean updateDevices(String Ieee, String ep,ContentValues c);
	}
	
	
}
