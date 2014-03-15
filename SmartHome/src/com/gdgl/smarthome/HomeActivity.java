package com.gdgl.smarthome;

//import com.gdgl.adapter.ImageForGridViewAdapter;

import com.gdgl.adapter.ImageForGridViewAdapter;
import com.gdgl.util.MyVolley;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import android.widget.GridView;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		GridView gridview = (GridView) findViewById(R.id.homegridview);
		gridview.setAdapter(new ImageForGridViewAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(HomeActivity.this, "" + arg2, Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(HomeActivity.this,MyVolley.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
