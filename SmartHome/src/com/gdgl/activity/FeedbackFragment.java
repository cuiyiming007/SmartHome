package com.gdgl.activity;

import com.gdgl.manager.CGIManager;
import com.gdgl.manager.Manger;
import com.gdgl.manager.UIListener;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyApplicationFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackFragment extends Fragment implements UIListener {
	private View mView;
	private EditText feedbackEditText;
	private TextView feedbackTextView;
	private Button feedbackButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.feedback_fragment, null);
		initView();
		return mView;
	}

	private void initView() {
		feedbackEditText = (EditText) mView.findViewById(R.id.feedback_edit);
		feedbackTextView = (TextView) mView.findViewById(R.id.feedback_num);
		feedbackButton = (Button) mView.findViewById(R.id.feedback_commit);

		feedbackEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				feedbackTextView.setText(s.length() + "/400");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		feedbackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (feedbackEditText.getText().length() > 0) {
					CGIManager.getInstance().feedbackToServer("dd",
							feedbackEditText.getText().toString());
				} else {
					Toast.makeText(getActivity(), "请输入反馈信息", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	@Override
	public void update(Manger observer, Object object) {
		// TODO Auto-generated method stub
		Event event = (Event) object;
		if (EventType.FEEDBACKTOSERVER == event.getType()) {

			if (event.isSuccess() == true) {
				Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT)
						.show();
				MyApplicationFragment.getInstance().removeLastFragment();
			} else {
				// if failed,prompt a Toast
				Toast.makeText(getActivity(), "提交失败，请稍后重试", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}
