package h264.com;



import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdgl.activity.VideoFragment;
import com.gdgl.manager.VideoManager;
import com.gdgl.mydata.video.VideoNode;
import com.gdgl.smarthome.R;
import com.gdgl.util.AddDlg.AddDialogcallback;

public class VideoInfoDialog {
	private Context mContext;
	Dialog dialog;
	Button save;
	Button cancle;
	TextView textView;

	EditText userNameEdit;
	EditText ipEditText;
	EditText portEditText;
	EditText passworeEditText;
	EditText aliasEditText;
	LinearLayout devices_region;
	TextView text_name;
	
	
	AddDialogcallback mAddDialogcallback;
	
	public static final int Add = 1;
	public static final int Edit = 2;

	int mType;

	public VideoInfoDialog(Context c, int type) {
		mContext = c;
		mType = type;
		dialog = new Dialog(mContext, R.style.MyDialog);
		dialog.setContentView(R.layout.video_info_dlg);
		textView = (TextView) dialog.findViewById(R.id.txt_title);

//		devices_region = (LinearLayout) dialog
//				.findViewById(R.id.devices_region);

		userNameEdit = (EditText) dialog.findViewById(R.id.edit_user_name);
		ipEditText = (EditText) dialog.findViewById(R.id.edit_video_ip);
		portEditText = (EditText) dialog.findViewById(R.id.edit_port);
		passworeEditText = (EditText) dialog.findViewById(R.id.edit_password);
		aliasEditText = (EditText) dialog.findViewById(R.id.edit_alias);
		text_name = (TextView) dialog.findViewById(R.id.text_user_name);

		save = (Button) dialog.findViewById(R.id.btn_save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				VideoNode videoNode=getVideoNode();
				VideoManager.getInstance().addIPC(videoNode);
				
//				String mN = mName.getText().toString();
//				if(text_name.getText().toString().trim().equals("区域名称")){
//					if (null != mN && !mN.trim().equals("")) {
//						saveRegion(mN.trim());
//					}
//				}else{
//					if (null != mN && !mN.trim().equals("")) {
//						saveScene(mN.trim());
//					}
//				}
//				
				((VideoFragment)mAddDialogcallback).updateVideoList(videoNode);
				dismiss();
			}

			private VideoNode getVideoNode() {
				String ipString=ipEditText.getText().toString();
				String port=portEditText.getText().toString();
				String nameString=userNameEdit.getText().toString();
				String passwordString=passworeEditText.getText().toString();
				String aliase=aliasEditText.getText().toString();
				
				VideoNode videoNode=new VideoNode();
				videoNode.setAliases(aliase);
				videoNode.setHttpport(port);
				videoNode.setIpc_ipaddr(ipString);
				videoNode.setName(nameString);
				videoNode.setPassword(passwordString);
				return videoNode;
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

//	protected void saveScene(String trim) {
//		// TODO Auto-generated method stub
//		getFromSharedPreferences.setharedPreferences(mContext);
//		DevicesGroup dg=new DevicesGroup(mContext);
//		dg.setDevicesState(false);
//		dg.setEp("");
//		dg.setIeee("-1");
//		dg.setGroupName(trim);
//		dg.setGroupId(getFromSharedPreferences.getSceneId());
//		dg.setDevicesValue(0);
//		DataHelper dh=new DataHelper(mContext);
//		ArrayList<DevicesGroup> al=new ArrayList<DevicesGroup>();
//		al.add(dg);
//		for (DevicesGroup devicesGroup : al) {
//			dh.insertGroup(dh.getReadableDatabase(), DataHelper.GROUP_TABLE, null,devicesGroup.convertContentValues());
//		}
//		
//	}

//	protected void saveRegion(String mN) {
//		// TODO Auto-generated method stub
//		List<String> mList = new ArrayList<String>();
//		String[] mregions = null;
//		getFromSharedPreferences.setharedPreferences(mContext);
//		String reg = getFromSharedPreferences.getRegion();
//		if (null != reg && !reg.trim().equals("")) {
//			mregions = reg.split("@@");
//		}
//		if (null != mregions) {
//			for (String string : mregions) {
//				if (!string.equals("")) {
//					mList.add(string);
//				}
//			}
//		}
//		if (!mList.contains(mN)) {
//			mList.add(mN);
//		}
//		StringBuilder ms = new StringBuilder();
//		for (String string : mList) {
//			ms.append(string + "@@");
//		}
//		getFromSharedPreferences.setRegion(ms.toString());
//	}

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
	

	public void setDialogCallback(AddDialogcallback dialogcallback) {
		this.mAddDialogcallback = dialogcallback;
	}
	
}
