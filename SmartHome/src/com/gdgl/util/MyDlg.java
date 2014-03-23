package com.gdgl.util;

import com.gdgl.smarthome.R;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyDlg {

	/**
	 * �õ��Զ����progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {
		Log.i("MyProcessDlg", "zgs->createLoadingDialog");
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.process_dlg, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);

		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.txt_wait);

		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);

		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);
		v.setAnimation(AnimationUtils.loadAnimation(context,
				R.anim.slide_left_in));
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);

		// loadingDialog.setCancelable(false);
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		return loadingDialog;

	}

	public static Dialog createOkDialog(Context context, String msg,
			OnClickListener okListener, OnClickListener cancleListener) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.ok_cancle_dlg, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.okdialog_view);

		TextView tipTextView = (TextView) v.findViewById(R.id.txt_title);
		tipTextView.setText(msg);

		Button btn_ok = (Button) v.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(okListener);

		Button btn_cancle = (Button) v.findViewById(R.id.btn_cancle);
		btn_cancle.setOnClickListener(cancleListener);

		// v.setAnimation(AnimationUtils.loadAnimation(context,
		// R.anim.slide_left_in));
		Dialog loadingDialog = new Dialog(context);
		loadingDialog.setCancelable(false);
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));

		return loadingDialog;

	}

	// }
}
