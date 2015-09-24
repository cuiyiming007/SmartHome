package com.gdgl.adapter;

import java.util.List;

import com.gdgl.smarthome.R;
import com.videogo.constant.Constant;
import com.videogo.openapi.bean.resp.CameraInfo;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class SimpleRealPlayerAdapter extends BaseAdapter{
	private Context mContext = null;

    private List<CameraInfo> mCameraInfoList = null;

    /** 屏幕当前方向 */
    private int mOrientation = Configuration.ORIENTATION_PORTRAIT;
    
    /** 竖屏时的宽度 */
    private int mDisplayWidth = 0;
    
    /**
     * 自定义控件集合
     * 
     * @author dengsh
     * @data 2012-6-25
     */
    public static class RealPlayerHolder {
        /** 显示现象页图片 */
        public ImageView mFigureIv;

        public RelativeLayout mLoadingRL;
        
        public TextView mWaittingTv;
        
        public ImageView mPlayIv;
    }

    public SimpleRealPlayerAdapter(Context context) {
        mContext = context;
    }

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    public void setDisplayWidthHeight(int displayWidth, int displayHeight) {
        mDisplayWidth = displayWidth;
    }
    
    public void setCameraList(List<CameraInfo> cameraInfoList) {
        mCameraInfoList = cameraInfoList;
    }

    @Override
    public int getCount() {
        if (mCameraInfoList != null) {
            return mCameraInfoList.size();          
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        if (position >= 0 && getCount() > position) {
            if (mCameraInfoList != null) {
                item = mCameraInfoList.get(position);
            }
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // 自定义视图
        RealPlayerHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new RealPlayerHolder();

            // 获取list_item布局文件的视图
            convertView = LayoutInflater.from(mContext).inflate(R.layout.realplayer_item, null);
            // 获取控件对象
            viewHolder.mFigureIv = (ImageView) convertView.findViewById(R.id.default_figure_iv);
            viewHolder.mLoadingRL = (RelativeLayout) convertView.findViewById(R.id.realplay_loading_rl);
            viewHolder.mWaittingTv = (TextView) convertView.findViewById(R.id.realplay_watting_tv);
            viewHolder.mPlayIv = (ImageView) convertView.findViewById(R.id.realplay_play_iv);
            
            // 设置点击图标的监听响应函数
            viewHolder.mPlayIv.setOnClickListener((OnClickListener) mContext);

            // 设置控件集到convertView
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RealPlayerHolder) convertView.getTag();
        }

        viewHolder.mPlayIv.setTag(Integer.valueOf(position));

        updateFigureIvLayoutParams(viewHolder.mFigureIv, (int)(mDisplayWidth*Constant.LIVE_VIEW_RATIO));

        return convertView;
    }
    
    public void updateFigureIvLayoutParams(ImageView figureIv, int height) {
        if(figureIv == null) {
            return;
        }
        if(mOrientation == Configuration.ORIENTATION_PORTRAIT) {        
            RelativeLayout.LayoutParams figureLp = (LayoutParams) figureIv.getLayoutParams();
            figureLp.width = LayoutParams.MATCH_PARENT;
            figureLp.height = height;    
            figureIv.setLayoutParams(figureLp);   
        } else {        
            RelativeLayout.LayoutParams figureLp = (LayoutParams) figureIv.getLayoutParams();
            figureLp.width = LayoutParams.MATCH_PARENT;
            figureLp.height = LayoutParams.MATCH_PARENT;                   
            figureIv.setLayoutParams(figureLp);            
        }
    }
}
