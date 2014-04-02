package com.gdgl.activity;

import com.gdgl.adapter.DevicesAdapter;
import com.gdgl.smarthome.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class devicesFragment extends ListFragment {

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View mView = inflater.inflate(R.layout.devices_layout, container,
                false);
        this.setListAdapter(new DevicesAdapter((Context) getActivity()));
        
        return mView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        Intent intent=new Intent();
        if(position%2==0)
        {
            intent.setClass((Context) getActivity(), LightManager.class);
        }
        else
        {
            intent.setClass((Context) getActivity(), ElectricalControl.class);
        }
        startActivity(intent);
        //super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

}
