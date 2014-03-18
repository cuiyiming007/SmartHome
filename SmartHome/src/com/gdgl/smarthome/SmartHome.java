package com.gdgl.smarthome;

import java.util.ArrayList;

import com.gdgl.mylistener.OnViewChangeListener;



import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SmartHome extends Activity implements OnViewChangeListener,
		OnClickListener {
	private MyScrollLayout mScrollLayout;
	private LinearLayout[] mImageViews;
	private int mViewCount;
	private int mCurSel;
	private ImageView set;
	private ImageView add;

	private TextView gongneng;
	private TextView quyu;
	private TextView changjin;

	private boolean isOpen = false;

	private ListView listview1;
	private ListView listview2;

	// �Զ���ĵ�������
	SelectPicPopupWindow menuWindow; // ������
	SelectAddPopupWindow menuWindow2; // ������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		gongneng = (TextView) findViewById(R.id.gongneng);
		quyu = (TextView) findViewById(R.id.quyu);
		changjin = (TextView) findViewById(R.id.changjin);

//		listview1 = (ListView) findViewById(R.id.listView1);
//		listview2 = (ListView) findViewById(R.id.listView2);
//
//		HuihuaAdapter ha = new HuihuaAdapter(this, getHuahui());
//		listview1.setAdapter(ha);
//		listview1.setCacheColorHint(0);
//
//		ContactAdapter hc = new ContactAdapter(this, getContact());
//		listview2.setAdapter(hc);
//		listview2.setCacheColorHint(0);

		mScrollLayout = (MyScrollLayout) findViewById(R.id.ScrollLayout);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lllayout);
		mViewCount = mScrollLayout.getChildCount();
		mImageViews = new LinearLayout[mViewCount];
		for (int i = 0; i < mViewCount; i++) {
			mImageViews[i] = (LinearLayout) linearLayout.getChildAt(i);
			mImageViews[i].setEnabled(true);
			mImageViews[i].setOnClickListener(this);
			mImageViews[i].setTag(i);
		}
		mCurSel = 0;
		mImageViews[mCurSel].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);

		set = (ImageView) findViewById(R.id.set);
		add = (ImageView) findViewById(R.id.add);

		set.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				uploadImage(SmartHome.this);
			}
		});
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				uploadImage2(SmartHome.this);
			}
		});
	}

	
	public void uploadImage(final Activity context) {
		menuWindow = new SelectPicPopupWindow(SmartHome.this, itemsOnClick);
		// ��ʾ����
		menuWindow.showAtLocation(SmartHome.this.findViewById(R.id.set),
				Gravity.TOP | Gravity.RIGHT, 10, 230); // ����layout��PopupWindow����ʾ��λ��
	}

	public void uploadImage2(final Activity context) {
		menuWindow2 = new SelectAddPopupWindow(SmartHome.this, itemsOnClick2);
		// ��ʾ����
		menuWindow2.showAtLocation(SmartHome.this.findViewById(R.id.add),
				Gravity.TOP | Gravity.RIGHT, 10, 230); // ����layout��PopupWindow����ʾ��λ��
	}

	// Ϊ��������ʵ�ּ�����
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
		}
	};

	// Ϊ��������ʵ�ּ�����
	private OnClickListener itemsOnClick2 = new OnClickListener() {

		public void onClick(View v) {
			menuWindow2.dismiss();
		}
	};

	private void setCurPoint(int index) {
		if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
			return;
		}
		mImageViews[mCurSel].setEnabled(true);
		mImageViews[index].setEnabled(false);
		mCurSel = index;

		if (index == 0) {
			gongneng.setTextColor(0xff228B22);
			quyu.setTextColor(Color.BLACK);
			changjin.setTextColor(Color.BLACK);
		} else if (index == 1) {
			gongneng.setTextColor(Color.BLACK);
			quyu.setTextColor(0xff228B22);
			changjin.setTextColor(Color.BLACK);
		} else {
			gongneng.setTextColor(Color.BLACK);
			quyu.setTextColor(Color.BLACK);
			changjin.setTextColor(0xff228B22);
		}
	}

	@Override
	public void OnViewChange(int view) {
		// TODO Auto-generated method stub
		setCurPoint(view);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int pos = (Integer) (v.getTag());
		setCurPoint(pos);
		mScrollLayout.snapToScreen(pos);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_MENU)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
