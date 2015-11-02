package com.gdgl.activity;

import com.gdgl.smarthome.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class MessageIpcLinkageVideoPlayer extends Activity {
	private VideoView videoView;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ipc_linkage_video_player);
        
        int ori = getResources().getConfiguration().orientation ; //获取屏幕方向
        if(ori == Configuration.ORIENTATION_LANDSCAPE){
        	getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); 
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        }
        
        Intent intent = getIntent();
        Uri videoUri = intent.getData();
        videoView = (VideoView) findViewById(R.id.videoView);
        /**
         * VideoView控制视频播放的功能相对较少，具体而言，它只有start和pause方法。为了提供更多的控制，
         * 可以实例化一个MediaController，并通过setMediaController方法把它设置为VideoView的控制器。
         */
        videoView.setMediaController(new MediaController(this));
//        Uri videoUri = Uri.parse(Environment.getExternalStorageDirectory()
//                .getPath() + "/1.mp4");
        videoView.setVideoURI(videoUri);
        videoView.start();
        
        videoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
		});
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    	if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { 
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); 
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
     
//            questionVideo.setDimensions(displayHeight, displayWidth); 
//            questionVideo.getHolder().setFixedSize(displayHeight, displayWidth); 
     
        } else { 
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); 
     
//            questionVideo.setDimensions(displayWidth, smallHeight); 
//            questionVideo.getHolder().setFixedSize(displayWidth, smallHeight); 
     
        }
    }
}
