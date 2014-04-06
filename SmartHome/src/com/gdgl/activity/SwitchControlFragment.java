package com.gdgl.activity;

import com.gdgl.adapter.DevicesBaseAdapter;
import com.gdgl.model.DevicesModel;
import com.gdgl.smarthome.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class SwitchControlFragment extends BaseControlFragment {

    boolean[] mBoolean = { false, false, false };
    View mView;
    int mCount;
    DevicesModel mDevices;

    TextView txt_devices_name, txt_devices_region;

    CheckBox mSwitch1, mSwitch2, mSwitch3;
    TextView mSwichName1, mSwichName2, mSwichName3;
    RelativeLayout viewGroup1, viewGroup2;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        if (null != extras) {
            mDevices = (DevicesModel) extras
                    .getParcelable(DevicesBaseAdapter.PASS_OBJECT);
        }
        initstate();
    }

    private void initstate() {
        // TODO Auto-generated method stub
        if (null != mDevices) {
            for (int m = 0; m < mDevices.getDevicesState().length; m++) {
                mBoolean[m] = mDevices.getDevicesState()[m];
            }
        }
        mCount = mDevices.getDevicesState().length;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mView = inflater.inflate(R.layout.switch_control, null);
        initView();
        return mView;
    }

    private void initView() {
        // TODO Auto-generated method stub
        mSwitch1 = (CheckBox) mView.findViewById(R.id.switch_state1);
        mSwitch2 = (CheckBox) mView.findViewById(R.id.switch_state2);
        mSwitch3 = (CheckBox) mView.findViewById(R.id.switch_state3);

        mSwichName1 = (TextView) mView.findViewById(R.id.switch_name1);
        mSwichName2 = (TextView) mView.findViewById(R.id.switch_name2);
        mSwichName3 = (TextView) mView.findViewById(R.id.switch_name3);

        txt_devices_name = (TextView) mView.findViewById(R.id.txt_devices_name);
        txt_devices_region = (TextView) mView
                .findViewById(R.id.txt_devices_region);

        txt_devices_name.setText(mDevices.getDevicesName());
        txt_devices_region.setText(mDevices.getDevicesRegion());

        viewGroup1 = (RelativeLayout) mView.findViewById(R.id.switch_group1);
        viewGroup2 = (RelativeLayout) mView.findViewById(R.id.switch_group2);

        switch (mCount) {
        case 1:
            viewGroup1.setVisibility(View.VISIBLE);
            viewGroup2.setVisibility(View.GONE);
            mSwitch1.setChecked(mBoolean[0]);
            break;
        case 2:
            viewGroup1.setVisibility(View.GONE);
            viewGroup2.setVisibility(View.VISIBLE);
            mSwitch2.setChecked(mBoolean[0]);
            mSwitch3.setChecked(mBoolean[1]);

            mSwichName2.setText("开关1");
            mSwichName3.setText("开关2");
            break;
        case 3:
            mSwitch1.setChecked(mBoolean[0]);
            mSwitch2.setChecked(mBoolean[1]);
            mSwitch3.setChecked(mBoolean[2]);
            break;
        default:
            break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        if (!(activity instanceof SaveDevicesName)) {
            throw new IllegalStateException("Activity必须实现SaveDevicesName接口");
        }
        mSaveDevicesName = (SaveDevicesName) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void editDevicesName() {
        // TODO Auto-generated method stub

    }

    // public interface
}
