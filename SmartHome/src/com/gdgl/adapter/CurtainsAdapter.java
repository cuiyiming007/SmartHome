package com.gdgl.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gdgl.model.CurtainModel;
import com.gdgl.model.LightsModel;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class CurtainsAdapter extends BaseAdapter {

    private static final String TAG = "lights_adapter";

    // 状态改变成功
    private static final int STATE_CHANGE_SUCESS = 1;

    // 状态改变失败
    private static final int STATE_CHANGE_FAILED = 2;

    Animation hyperspaceJumpAnimation;
    
    gotoCurtainsControlFragment mgotoCurtainsControlFragment;
    
    
    int mCurrentId = 0;
    

    private Context mContext;

    MyOkCancleDlg mDialog;
    ImageView mImag;
    CheckBox curtain_on_off;

    List<CurtainModel> mList = new ArrayList<CurtainModel>();

    public CurtainsAdapter(Context context, List<CurtainModel> list,gotoCurtainsControlFragment gc) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mList = list;
        mgotoCurtainsControlFragment=gc;
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(mContext,
                R.anim.loading_animation);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (null != mList) {
            Log.i(TAG, "zgs->getCount()" + mList.size());
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        Log.i(TAG, "zgs->getItemId()" + arg0);
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        Log.i("lights_adapter", "zgs->arg0=" + arg0);
        View mView = arg1;
        ViewHolder mHolder;
        final CurtainModel mCurtain = mList.get(arg0);
        if (null == mView) {
            mHolder = new ViewHolder();
            mView = LayoutInflater.from(mContext).inflate(
                    R.layout.curtain_list_item, null);
            mHolder.curtain_name = (TextView) mView
                    .findViewById(R.id.curtain_name);
            mHolder.delete = (Button) mView.findViewById(R.id.delete);
            mHolder.curtain_region = (TextView) mView
                    .findViewById(R.id.curtain_region);
            mHolder.curtain_state=(TextView) mView
                    .findViewById(R.id.curtain_state);

            mHolder.img = (ImageView) mView.findViewById(R.id.img);

            mView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) mView.getTag();
        }

        mHolder.delete.setTag(arg0);
        mHolder.delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                mCurrentId = Integer.parseInt(v.getTag().toString());
                mDialog = new MyOkCancleDlg(mContext);
                mDialog.setContent("确定要删除" + mList.get(mCurrentId).getName()
                        + "吗?");
                mDialog.setDialogCallback(dialogcallback);
                mDialog.show();

            }
        });
        mHolder.curtain_name.setText(mCurtain.getName());
        mHolder.curtain_region.setText(mCurtain.getRegion());
        if(mCurtain.getLevel()==0.0){
            mHolder.curtain_state.setText("完全关闭");
        }else if(mCurtain.getLevel()==1.0){
            mHolder.curtain_state.setText("完全打开");
        }else{
            mHolder.curtain_state.setText("打开 "+(int)(mCurtain.getLevel()*100)+"%");
        }
        
        mView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mgotoCurtainsControlFragment.gotoCurtainsControlFragment(mCurtain.getId(), mCurtain.getLevel());
            }
        });
        
        return mView;
    }

    Dialogcallback dialogcallback = new Dialogcallback() {
        @Override
        public void dialogdo() {
            // TODO Auto-generated method stub
            mList.remove(mCurrentId);
            CurtainsAdapter.this.notifyDataSetChanged();
        }
    };

    //
    class ViewHolder {
        Button delete;
        ImageView img;
        TextView curtain_name;
        TextView curtain_region;
        TextView curtain_state;

    }
    
    public interface gotoCurtainsControlFragment{
        public void gotoCurtainsControlFragment(int id,double mProgress);
    }
    
}
