package h264.com;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gdgl.manager.VideoManager;
import com.gdgl.mydata.video.VideoNode;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOKOnlyDlg;

public class VideoInfoDialog {
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
	EditText serialEditText;
	LinearLayout devices_region;
	TextView text_name;
	VideoNode videoNode;

	// AddDialogcallback mAddDialogcallback;

	public static final int Add = 1;
	public static final int Edit = 2;

	int mType;

	public VideoInfoDialog(Context c, int type, Object data) {
		this(c, type);

		videoNode = (VideoNode) data;
		userNameEdit.setText(videoNode.getName());
		ipEditText.setText(videoNode.getIpc_ipaddr());
		portEditText.setText(videoNode.getRtspport());
		httpportEditText.setText(videoNode.getHttpport());
		passworeEditText.setText(videoNode.getPassword());
		aliasEditText.setText(videoNode.getAliases());
		serialEditText.setText(videoNode.getSerialNum());
	}

	public VideoInfoDialog(Context c, int type, int index) {
		this(c, type);

		videoNode = new VideoNode();
		videoNode.setIndex(index);

		String port = "554";
		String httpport = "8000";
		String nameString = "admin";

		ipEditText.setText("");
		portEditText.setText(port);
		httpportEditText.setText(httpport);
		userNameEdit.setText(nameString);
		passworeEditText.setText("");
		aliasEditText.setText("");
		serialEditText.setText("");
	}

	public VideoInfoDialog(Context c, int type) {
		mContext = c;
		mType = type;
		dialog = new Dialog(mContext, R.style.MyDialog);
		dialog.setContentView(R.layout.video_info_dlg);
		dialog.setCanceledOnTouchOutside(false);
		textView = (TextView) dialog.findViewById(R.id.txt_title);

		userNameEdit = (EditText) dialog.findViewById(R.id.edit_user_name);
		ipEditText = (EditText) dialog.findViewById(R.id.edit_video_ip);
		portEditText = (EditText) dialog.findViewById(R.id.edit_port);
		httpportEditText = (EditText) dialog.findViewById(R.id.edit_http_port);
		passworeEditText = (EditText) dialog.findViewById(R.id.edit_password);
		aliasEditText = (EditText) dialog.findViewById(R.id.edit_alias);
		text_name = (TextView) dialog.findViewById(R.id.text_user_name);
		serialEditText = (EditText) dialog.findViewById(R.id.edit_serial);

		save = (Button) dialog.findViewById(R.id.btn_save);
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getVideoNode()) {
					if (mType == Add) {
						VideoManager.getInstance().addIPC(videoNode);
					} else if (mType == Edit) {
						VideoManager.getInstance().editIPC(videoNode);
					}
					dismiss();
				} else {
					MyOKOnlyDlg myOKOnlyDlg = new MyOKOnlyDlg(mContext);
					myOKOnlyDlg.setContent("请输入完整信息.");
					myOKOnlyDlg.show();
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
	}

	private boolean getVideoNode() {
		String ipString = ipEditText.getText().toString();
		String port = portEditText.getText().toString();
		String httpport = httpportEditText.getText().toString();
		String nameString = userNameEdit.getText().toString();
		String passwordString = passworeEditText.getText().toString();
		String aliase = aliasEditText.getText().toString();
		String serial = serialEditText.getText().toString();

		if (ipString == null || ipString.isEmpty() || port == null
				|| port.isEmpty() || httpport == null || httpport.isEmpty()
				|| nameString == null || nameString.isEmpty()
				|| passwordString == null || passwordString.isEmpty()
				|| aliase == null || aliase.isEmpty() || serial == null
				|| serial.isEmpty()) {
			return false;
		}
		videoNode.setAliases(aliase);
		videoNode.setHttpport(httpport);
		videoNode.setRtspport(port);
		videoNode.setIpc_ipaddr(ipString);
		videoNode.setName(nameString);
		videoNode.setPassword(passwordString);
		videoNode.setSerialNum(serial);
		videoNode.setDomainName("");
		return true;
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
}
