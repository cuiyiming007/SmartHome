package h264.com;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gdgl.activity.UIinterface.IFragmentCallbak;
import com.gdgl.app.ApplicationController;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.manager.VideoManager;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.video.VideoNode;
import com.gdgl.mydata.video.VideoResponse;
import com.gdgl.smarthome.R;

public class VideoInfoDialog implements UIListener {
	private Context mContext;
	Dialog dialog;
	Button save;
	Button cancle;
	TextView textView;

	EditText userNameEdit;
	EditText ipEditText;
	EditText portEditText;
	EditText httpportEditText;
	EditText passworeEditText;
	EditText aliasEditText;
	LinearLayout devices_region;
	TextView text_name;
	IFragmentCallbak listener;
	VideoNode videoNode;

	// AddDialogcallback mAddDialogcallback;

	public static final int Add = 1;
	public static final int Edit = 2;

	int mType;

	public VideoInfoDialog(Context c, int type,
			IFragmentCallbak fragmentCallbak, Object data) {
		this(c, type, fragmentCallbak);
		videoNode = (VideoNode) data;
		userNameEdit.setText(videoNode.getName());
		ipEditText.setText(videoNode.getIpc_ipaddr());
		portEditText.setText(videoNode.getRtspport());
		httpportEditText.setText(videoNode.getHttpport());
		passworeEditText.setText(videoNode.getPassword());
		aliasEditText.setText(videoNode.getAliases());
	}

	public VideoInfoDialog(Context c, int type, IFragmentCallbak fragmentCallbak) {
		mContext = c;
		mType = type;
		dialog = new Dialog(mContext, R.style.MyDialog);
		dialog.setContentView(R.layout.video_info_dlg);
		textView = (TextView) dialog.findViewById(R.id.txt_title);
		listener = fragmentCallbak;

		userNameEdit = (EditText) dialog.findViewById(R.id.edit_user_name);
		ipEditText = (EditText) dialog.findViewById(R.id.edit_video_ip);
		portEditText = (EditText) dialog.findViewById(R.id.edit_port);
		httpportEditText = (EditText) dialog.findViewById(R.id.edit_http_port);
		passworeEditText = (EditText) dialog.findViewById(R.id.edit_password);
		aliasEditText = (EditText) dialog.findViewById(R.id.edit_alias);
		text_name = (TextView) dialog.findViewById(R.id.text_user_name);

		save = (Button) dialog.findViewById(R.id.btn_save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				videoNode = getVideoNode();
				if (mType == Add) {
					VideoManager.getInstance().addIPC(videoNode);
				} else if (mType == Edit) {
					VideoManager.getInstance().editIPC(videoNode);
				}
			}

		});

		cancle = (Button) dialog.findViewById(R.id.btn_cancle);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		VideoManager.getInstance().addObserver(this);
	}

	private VideoNode getVideoNode() {
		String ipString = ipEditText.getText().toString();
		String port = portEditText.getText().toString();
		String httpport = httpportEditText.getText().toString();
		String nameString = userNameEdit.getText().toString();
		String passwordString = passworeEditText.getText().toString();
		String aliase = aliasEditText.getText().toString();

		VideoNode videoNode = new VideoNode();
		videoNode.setAliases(aliase);
		videoNode.setHttpport(httpport);
		videoNode.setRtspport(port);
		videoNode.setIpc_ipaddr(ipString);
		videoNode.setName(nameString);
		videoNode.setPassword(passwordString);
		if (mType == Edit) {
			videoNode.setId(this.videoNode.getId());
		} else {
			videoNode.setId("");

		}
		return videoNode;
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

	@Override
	public void update(Manger observer, Object object) {
		final Event event = (Event) object;
		if (event.getType() == EventType.ADDIPC) {
			if (event.isSuccess()) {
				// get the id
				VideoResponse response = (VideoResponse) event.getData();
				Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
				videoNode.setId(response.getResponse_params().getIpc_id());
				listener.onFragmentResult(Add, true, videoNode);
				dismiss();

				addToDB();
			} else {
				Toast.makeText(mContext, "添加失败", Toast.LENGTH_SHORT).show();
			}

		} else if (event.getType() == EventType.EDITIPC) {
			if (event.isSuccess()) {
				Toast.makeText(mContext, "编辑成功", Toast.LENGTH_SHORT).show();
				listener.onFragmentResult(Edit, true, videoNode);
				dismiss();
				editToDB();
			} else {
				Toast.makeText(mContext, "编辑失败", Toast.LENGTH_SHORT).show();
			}
		}

	}

	private void editToDB() {
		new AsyncTask<Object, Object, Object>(){

			@Override
			protected Object doInBackground(Object... params) {
				DataHelper mDateHelper = new DataHelper(
						ApplicationController.getInstance());
				SQLiteDatabase mSQLiteDatabase = mDateHelper
						.getSQLiteDatabase();
				
				String Where = " id=?";
				String[] args = { videoNode.getId() == null ? "" : videoNode
						.getId().trim(), };
				ContentValues contentValues = videoNode.convertContentValues();
				mDateHelper.update(mSQLiteDatabase, DataHelper.VIDEO_TABLE,
						contentValues, Where, args);
				return true;
			}}.execute();
	}

	private void addToDB() {
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				DataHelper mDateHelper = new DataHelper(
						ApplicationController.getInstance());
				SQLiteDatabase mSQLiteDatabase = mDateHelper.getSQLiteDatabase();
				ArrayList<VideoNode> videoNodes = new ArrayList<VideoNode>();
				videoNodes.add(videoNode);
				mDateHelper.insertVideoList(mSQLiteDatabase, DataHelper.VIDEO_TABLE,
						null, videoNodes);
				return true;
			}
		}.execute();
		
	}

}
