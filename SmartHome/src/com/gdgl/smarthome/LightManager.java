package com.gdgl.smarthome;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.adapter.lights_adapter;
import com.gdgl.model.lights;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class LightManager extends Activity{
	
	ListView mListView;
	LinearLayout mBack;
	List<lights> mList=new ArrayList<lights>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lights_manager);
		lights mLights;
		for(int m=0;m<25;m++)
		{
			if(m%2==0)
			{
				mLights=new lights(m, "light_"+m, "厨房_"+m,false);
			}
			else
			{
				mLights=new lights(m, "light_"+m, "厨房_"+m,false,0.2);
			}
			
			mList.add(mLights);
		}
		Log.i("zgs", "zgs-> LightManager"+mList.get(9).getLevel());
		mListView=(ListView)findViewById(R.id.light_list);
		mListView.setAdapter(new lights_adapter(LightManager.this,mList));
		
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
