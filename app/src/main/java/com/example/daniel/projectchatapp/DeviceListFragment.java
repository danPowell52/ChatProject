package com.example.daniel.projectchatapp;

import android.app.ListFragment;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Daniel on 27/04/2017.
 */

public class DeviceListFragment extends ListFragment {
    List<WifiP2pDevice> peers;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View contentView = inflater.inflate(R.layout.chat_layout, null);
        return contentView;
    }
}
