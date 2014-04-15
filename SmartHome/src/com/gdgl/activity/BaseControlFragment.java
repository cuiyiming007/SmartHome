package com.gdgl.activity;

import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;

import com.gdgl.activity.ShowDevicesGroupFragmentActivity.EditDevicesName;
import com.gdgl.manager.UIListener;
import com.gdgl.util.MyDlg;


public abstract class BaseControlFragment extends Fragment implements
		EditDevicesName,UIListener{
	public UpdateDevice mUpdateDevice;
	public Dialog mDialog;
	public interface UpdateDevice {
		public void saveDevicesName(String name);
		public boolean updateDevices(String Ieee, String ep,ContentValues c);
	}
	
	
}
