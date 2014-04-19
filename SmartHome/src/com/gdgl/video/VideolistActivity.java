package com.gdgl.video;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.gdgl.smarthome.R;

public class VideolistActivity extends Activity {
	// ��ɶ�̬���飬����ת�����
	ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
	// String[] listItemName={"ͨ��1","ͨ��2","ͨ��3","ͨ��4"};
	String[] listItemName = { "�����1", "�����2", "�����3", "�����4", "�����5" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videolist);

		ListView lv = (ListView) findViewById(R.id.listView1);

		// ��ʼ���б�
		for (int i = 0; i < 5; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemText", R.drawable.videolist);
			map.put("ItemTitle", listItemName[i]);
			mylist.add(map);
		}

		// ���������������===��ListItem //?????
		SimpleAdapter mSchedule = new SimpleAdapter(this, // ûʲô����
				mylist,// �����Դ
				R.layout.videoitem,// ListItem��XMLʵ��

				// ��̬������ListItem��Ӧ������
				new String[] { "ItemText", "ItemTitle" },

				// ListItem��XML�ļ���������� ID
				new int[] { R.id.imageView1, R.id.textView1 });
		// ��Ӳ�����ʾ
		lv.setAdapter(mSchedule);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(VideolistActivity.this,
						DisplayH264.class);
				intent.putExtra("ipc_channel", arg2);
				startActivity(intent);
			}

		});
	}

}
