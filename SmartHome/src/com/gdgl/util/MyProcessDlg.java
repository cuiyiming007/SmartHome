package com.gdgl.util;



import com.gdgl.smarthome.R;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyProcessDlg {
		
		/**
		 * �õ��Զ����progressDialog
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
			v.setAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_left_in));
			Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);

			//loadingDialog.setCancelable(false);
			loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));
			
			return loadingDialog;

		}
		
//	}
}
