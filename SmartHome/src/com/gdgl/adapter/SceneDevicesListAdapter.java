package com.gdgl.adapter;

import java.util.List;

import com.gdgl.model.DevicesGroup;
import com.gdgl.model.SimpleDevicesModel;
import com.gdgl.mydata.DataHelper;
import com.gdgl.mydata.DataUtil;
import com.gdgl.mydata.getFromSharedPreferences;
import com.gdgl.smarthome.R;
import com.gdgl.util.MyOkCancleDlg.Dialogcallback;
import com.gdgl.util.UiUtils;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SceneDevicesListAdapter extends BaseAdapter implements Dialogcallback {

    protected Context mContext;
    protected List<SimpleDevicesModel> mDevicesList;
    protected DevicesObserver mDevicesObserver;

    public static final String DEVICES_ID = "devices_id";
    public static final String BOLLEAN_ARRARY = "devices_state";
    public static final String DEVIVES_VALUE = "devices_value";

    ViewHolder lay;

    public class SwitchModel {
        public String modelId;
        public String name;
        public String[] anotherName;
        public String[] ieee;
        public boolean[] state;

    }

    private String index;

    public SceneDevicesListAdapter(Context c, List<SimpleDevicesModel> list,
            DevicesObserver mObserver) {
        mContext = c;
        mDevicesList = list;
        mDevicesObserver = mObserver;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (null != mDevicesList) {
            return mDevicesList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (null != mDevicesList) {
            return mDevicesList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        if (null != mDevicesList) {
            return (long) mDevicesList.get(position).getID();
        }
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        
        if(null == mDevicesList){
            return convertView;
        }
        View mView = convertView;
        ViewHolder mHolder;
        final SimpleDevicesModel mDevices = mDevicesList.get(position);
        DataHelper dh=new DataHelper(mContext);
        final DevicesGroup mDevicesGp = DataUtil.getOneScenesDevices(mContext, dh, mDevices.getmIeee());
        
        if (null == mView) {
            mHolder = new ViewHolder();
            mView = LayoutInflater.from(mContext).inflate(
                    R.layout.scene_item_forlist, null);
            mHolder.devices_img = (ImageView) mView
                    .findViewById(R.id.devices_img);
            mHolder.devices_name = (TextView) mView
                    .findViewById(R.id.devices_name);
            mHolder.devices_region = (TextView) mView
                    .findViewById(R.id.devices_region);
            mHolder.devices_state = (TextView) mView
                    .findViewById(R.id.devices_state);
            mHolder.devices_scene_state = (TextView) mView
                    .findViewById(R.id.devices_scene_state);
            mHolder.scene_stste = (LinearLayout) mView
                    .findViewById(R.id.scene_stste);
            mView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) mView.getTag();
        }

        mHolder.devices_name.setText(mDevices.getmUserDefineName().replace(" ",
                ""));
        mHolder.devices_region.setText(mDevices.getmDeviceRegion().replace(" ",
                ""));

        mHolder.devices_img.setImageResource(UiUtils
				.getDevicesSmallIcon(mDevices.getmDeviceId(),mDevices.getmModelId().trim()));
//        if (DataHelper.IAS_ZONE_DEVICETYPE == mDevices.getmDeviceId()
//                || DataHelper.IAS_ACE_DEVICETYPE == mDevices.getmDeviceId()) {
//
//            mHolder.devices_img
//                    .setImageResource(UiUtils
//                            .getDevicesSmallIconByModelId(mDevices
//                                    .getmModelId().trim()));
//        } else if (mDevices.getmModelId().indexOf(
//                DataHelper.Multi_key_remote_control) == 0) {
//            mHolder.devices_img.setImageResource(UiUtils
//                    .getDevicesSmallIconForRemote(mDevices.getmDeviceId()));
//        }else {
//            mHolder.devices_img.setImageResource(UiUtils
//                    .getDevicesSmallIcon(mDevices.getmDeviceId()));
//        }

        if (mDevices.getmDeviceId() == DataHelper.ON_OFF_SWITCH_DEVICETYPE) {
            String state = "";
            String s = mDevices.getmOnOffStatus();
            Log.i("tag", "tag->" + s);
            String[] result = s.split(",");
            for (String string : result) {
                if (string.trim().equals("1")) {
                    state += "开 ";
                } else {
                    state += "关 ";
                }
            }
            Log.i("tag", "tag->" + state);
            mHolder.devices_state.setText(state);
            mHolder.devices_scene_state.setText(mDevicesGp.getDevicesState()?"开":"关");
        } else if (mDevices.getmDeviceId() == DataHelper.IAS_ZONE_DEVICETYPE) {
            if (mDevices.getmOnOffStatus().trim().equals("0")) {
                mHolder.devices_state.setText("已布防");
            } else {
                mHolder.devices_state.setText("已撤防");
            }
            mHolder.devices_scene_state.setText(mDevicesGp.getDevicesState()?"布防":"撤防");
        } else if (mDevices.getmDeviceId() == DataHelper.LIGHT_SENSOR_DEVICETYPE) {
        	mHolder.scene_stste.setVisibility(View.GONE);
            mHolder.devices_state.setText("亮度: "+getFromSharedPreferences.getLight());
            mHolder.devices_scene_state.setText(mDevicesGp.getDevicesState()?"布防":"撤防");
        } else if (mDevices.getmDeviceId() == DataHelper.TEMPTURE_SENSOR_DEVICETYPE) {
        	mHolder.scene_stste.setVisibility(View.GONE);
            mHolder.devices_state.setText("温度: "+getFromSharedPreferences.getTemperature()+"\n湿度: "+getFromSharedPreferences.getHumidity()+"%");
            mHolder.devices_scene_state.setText(mDevicesGp.getDevicesState()?"布防":"撤防");
        } else {
        	mHolder.scene_stste.setVisibility(View.VISIBLE);
        	mHolder.devices_scene_state.setText(mDevicesGp.getDevicesState()?"开":"关");
            if (mDevices.getmOnOffStatus().trim().equals("1")) {
                mHolder.devices_state.setText("开");
            } else {
                mHolder.devices_state.setText("关");
            }
        }
        
        if(mDevicesGp.getDevicesValue()>0){
        	mHolder.devices_scene_state.setText("开"+mDevicesGp.getDevicesValue()+"%");
        }
        
        return mView;
    }

    public void setList(List<SimpleDevicesModel> list) {
        mDevicesList = null;
        mDevicesList = list;
    }

    public class ViewHolder {
        ImageView devices_img;
        TextView devices_name;
        TextView devices_region;
        TextView devices_state;
        TextView devices_scene_state;
        LinearLayout scene_stste;
    }

    public interface DevicesObserver {

        public void setLayout();

        public void deleteDevices(String id);

    }

    @Override
    public void dialogdo() {
    }

}
