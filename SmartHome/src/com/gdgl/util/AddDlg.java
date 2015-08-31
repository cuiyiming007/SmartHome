package com.gdgl.util;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.manager.CGIManager;
import com.gdgl.model.RemoteControl;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.mydata.Region.GetRoomInfo_response;
import com.gdgl.mydata.Region.Room;
import com.gdgl.smarthome.R;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddDlg {
	private Context mContext;
	Dialog dialog;
	Button save;
	Button cancle;
	TextView textView;

	EditText mName;
	TextView devices_region;
	TextView text_name;
	
	AddDialogcallback mAddDialogcallback;
	
	public static final int REGION = 1;
	public static final int SCENE = 2;
	public static final int REMOTE_CONTROL = 3;
	
	public static final String ADD = "add";
	public static final String Edit = "edit";

	int mType;

	public AddDlg(Context c, int type) {
		mContext = c;
		mType = type;
		dialog = new Dialog(mContext, R.style.MyDialog);
		dialog.setContentView(R.layout.add_dlg);
		textView = (TextView) dialog.findViewById(R.id.txt_title);

		devices_region = (TextView) dialog
				.findViewById(R.id.devices_region);

		mName = (EditText) dialog.findViewById(R.id.edit_name);
		text_name = (TextView) dialog.findViewById(R.id.text_name);

		save = (Button) dialog.findViewById(R.id.btn_save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String mN = mName.getText().toString();
				if(mType==REGION){
					if (null != mN && !mN.trim().equals("")) {
						saveRegion(mN.trim());
					}
				}else if(mType==REMOTE_CONTROL){
					if (null != mN && !mN.trim().equals("")) {
						saveRemoteControl(mN.trim());
					}
				}
				mAddDialogcallback.refreshdata();
				dismiss();
			}
		});

		cancle = (Button) dialog.findViewById(R.id.btn_cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
	public AddDlg(Context c, Room room) {
		mContext = c;
		final Room mRoom = room;
		dialog = new Dialog(mContext, R.style.MyDialog);
		dialog.setContentView(R.layout.add_dlg);
		textView = (TextView) dialog.findViewById(R.id.txt_title);

		devices_region = (TextView) dialog
				.findViewById(R.id.devices_region);

		mName = (EditText) dialog.findViewById(R.id.edit_name);
		mName.setText(mRoom.getroom_name());
		text_name = (TextView) dialog.findViewById(R.id.text_name);

		save = (Button) dialog.findViewById(R.id.btn_save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String mN = Uri.encode(mName.getText().toString());
				updateRegion(mRoom, mN);	
				mAddDialogcallback.refreshdata();
				dismiss();
			}
		});

		cancle = (Button) dialog.findViewById(R.id.btn_cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
	protected void updateRegion(Room room, String name){
		CGIManager.getInstance().ZBAddRoomDataMain(""+room.getroom_id(), name, room.getroompic());
	}

	protected void saveRemoteControl(String trim) {
		// TODO Auto-generated method stub
		getFromSharedPreferences.setsharedPreferences(mContext);
		RemoteControl rc=new RemoteControl();
		rc.Name=trim;
		rc.Index=getFromSharedPreferences.getRemoteControlId()+"";
		rc.IsLearn="0";
		List<RemoteControl> mControlList=getFromSharedPreferences.getRemoteControl();
		if(null==mControlList){
			mControlList=new ArrayList<RemoteControl>();
		}
		mControlList.add(rc);
		getFromSharedPreferences.addRemoteControlList(mControlList);
	}

	protected void saveRegion(String mN) {
		// TODO Auto-generated method stub
		List<Room> mList = new ArrayList<Room>();
		
		DataHelper mDateHelper = new DataHelper(mContext);
		SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
		mList=mDateHelper.queryForRoomList(mSQLiteDatabase, DataHelper.ROOMINFO_TABLE, null, null, null, null, null, GetRoomInfo_response.ROOM_ID, null);
		
		//加入新的roomid
		int roomid=1;
		String name=Uri.encode(mN);
		if (mList.size()>0) {
			while(true) {
				int i=0;
				for (;i<mList.size();) {
					if(roomid==mList.get(i).getroom_id()) {
						roomid++;
						i++;
						continue;
					}
					i++;
				}
				if(i==mList.size()) {
					break;
				}
			}
		}
		SQLiteDatabase mSQLiteDatabase1 = mDateHelper.getSQLiteDatabase();
		CGIManager.getInstance().ZBAddRoomDataMain(Integer.toString(roomid), name, "");
		ArrayList<Room> addList=new ArrayList<Room>();
		Room addroomdata=new Room();
		addroomdata.setroom_id(roomid);
		addroomdata.setroom_name(mN);
		addroomdata.setroom_pic("");
		addList.add(addroomdata);
		mDateHelper.insertAddRoomInfo(mSQLiteDatabase1, DataHelper.ROOMINFO_TABLE, null, addList);	
		
	}

	public void setContent(String content) {
		textView.setText(content);
	}

	public void setType(String type) {
		text_name.setText(type);
	}

	public void show() {
		dialog.show();
	}

	public void hide() {
		dialog.hide();
	}

	public void dismiss() {
		dialog.dismiss();
	}
	
	public interface AddDialogcallback {
		public void refreshdata();
	}

	public void setDialogCallback(AddDialogcallback dialogcallback) {
		this.mAddDialogcallback = dialogcallback;
	}
	
}
