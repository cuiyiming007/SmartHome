package com.gdgl.activity;
 
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
 
/**
 * ���ܣ�ʹ��ViewPagerʵ�ֳ��ν���Ӧ��ʱ������ҳ
 * 
 * (1)�ж��Ƿ����״μ���Ӧ��--��ȡ��ȡSharedPreferences�ķ���
 * (2)�ǣ����������activity���������MainActivity
 * (3)5s��ִ��(2)����
 * 
 * @author yayun
 *
 */
public class SplashActivity extends Activity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
         
       // boolean mFirst = isFirstEnter(SplashActivity.this,SplashActivity.this.getClass().getName());
        boolean mFirst = false;
        //Toast.makeText(this, mFirst+"", Toast.LENGTH_SHORT).show();
        if(mFirst)
            mHandler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY,100);
        else
            mHandler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY,100);
        SharedPreferences sharedPreferences= this.getSharedPreferences("my_pref", MODE_PRIVATE);
        sharedPreferences.edit().putString("guide_activity", "false").commit();
        
        
    }   
     
    //****************************************************************
    // �ж�Ӧ���Ƿ���μ��أ���ȡSharedPreferences�е�guide_activity�ֶ�
    //****************************************************************
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private boolean isFirstEnter(Context context,String className){
        if(context==null || className==null||"".equalsIgnoreCase(className))return false;
        String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE)
                 .getString(KEY_GUIDE_ACTIVITY, "");//ȡ���������� �� com.my.MainActivity
        if(mResultStr.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }
     
     
    //*************************************************
    // Handler:��ת����ͬҳ��
    //*************************************************
    private final static int SWITCH_MAINACTIVITY = 1000;
    private final static int SWITCH_GUIDACTIVITY = 1001;
    public Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
            case SWITCH_MAINACTIVITY:
                Intent mIntent = new Intent();
                mIntent.setClass(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.startActivity(mIntent);
                SplashActivity.this.finish();
                break;
            case SWITCH_GUIDACTIVITY:
                mIntent = new Intent();
                Log.i("guide", "1111111111111");
                mIntent.setClass(SplashActivity.this, GuideActivity.class);
                SplashActivity.this.startActivity(mIntent);
                SplashActivity.this.finish();
                break;
            }
            super.handleMessage(msg);
        }
    };
}