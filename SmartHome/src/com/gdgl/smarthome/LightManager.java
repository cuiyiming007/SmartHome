package com.gdgl.smarthome;

import com.gdgl.adapter.lights_adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class LightManager extends Activity {
	
	ListView mListView;
	LinearLayout mBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lights_manager);
		mListView=(ListView)findViewById(R.id.light_list);
		mListView.setAdapter(new lights_adapter(LightManager.this));
		
		mBack=(LinearLayout)findViewById(R.id.goback);
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}

	
}
