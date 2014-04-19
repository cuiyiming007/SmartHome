package com.gdgl.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;

import com.gdgl.smarthome.R;

public class DisplayH264 extends Activity
{
	public static int ipc_channel = -1;;
	Display display;
	DecodeH264 decodeh264;
	public static int ret = 0;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras_ipc_channel = getIntent().getExtras();
		ipc_channel = extras_ipc_channel.getInt("ipc_channel");
        System.out.println("ipc-channel =" + ipc_channel);
		
		// ��ȡ��Ļ��Ⱥ͸߶�
		display = getWindowManager().getDefaultDisplay();
		decodeh264 = new DecodeH264(this, display.getWidth(), 
				display.getHeight());
		setContentView(decodeh264);//���ò���
		
		//decodeh264.PlayVideo(ipc_channel);
		ret = decodeh264.PlayVideo(ipc_channel);
		if(ret != 1)
		{
			showMessage(ret);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() 
	{
		// TODO Auto-generated method stub
		decodeh264.initalThread();
		super.finish();
	}

	//��ʾ�������ӷ�����
    public void showMessage(int type)
	{
    	Builder dl = new AlertDialog.Builder(DisplayH264.this);
    	dl.setTitle(R.string.sure);
    	if(type == 2)
    	{
    		dl.setMessage(R.string.ipc_offline);
    	}
    	else if(type == 3)
    	{
    		dl.setMessage(R.string.faile);
    	}
    	
    	dl.setPositiveButton(R.string.back,new DialogInterface.OnClickListener()
    	{
    		public void onClick(DialogInterface dialog, int whichButton)
    		{
    			dialog.cancel();//ȷ����ȥ��ǰ����
    		}
    	});
    	dl.setNegativeButton(R.string.close,new DialogInterface.OnClickListener() 
    	{
    		public void onClick(DialogInterface dialog, int whichButton) 
    		{
    			DisplayH264.this.finish();//ȡ�����˳�����
    		}
    	});
    	dl.show();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	/*
	@Override
	protected void onActivityResult(int requestCode, 
			int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) 
		{
			Uri uri = data.getData();
			String filPath=uri.getPath();
			try
			{
				FileInputStream fileIS = new FileInputStream(filPath);
				DataInputStream inputStream=new DataInputStream(fileIS);
				vv.playLocalVideo(inputStream);

			} 
			catch (IOException e)
			{
				Log.e(TAG, "onActivityResult IOException"+e.getMessage());
				return;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	*/
	
	
/*
	private FrameLayout.LayoutParams setPortrait() 
	{
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// ���ù����ֵ�λ��(���ڶ���)
		params.bottomMargin = 0;
		//params.rightMargin = display.getWidth() / 2;
		params.gravity = Gravity.TOP | Gravity.LEFT;
		return params;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		// TODO Auto-generated method stub
		
		if (this.getResources().getConfiguration().orientation == 
				Configuration.ORIENTATION_LANDSCAPE) 
		{
			FrameLayout.LayoutParams params = 
					new FrameLayout.LayoutParams(70,
					FrameLayout.LayoutParams.MATCH_PARENT);
			// ���ù����ֵ�λ��(���ڵײ�)
			params.bottomMargin = 0;
			
		} 
		else if (this.getResources().getConfiguration().orientation == 
				Configuration.ORIENTATION_PORTRAIT) 
		{
			FrameLayout.LayoutParams params = setPortrait();
		}
		
		super.onConfigurationChanged(newConfig);
	}
	*/
}
