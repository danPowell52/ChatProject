package com.example.daniel.projectchatapp;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Daniel on 27/04/2017.
 */

public class DeviceListAdapter extends ArrayAdapter {
    List<WifiP2pDevice> peers;

    public DeviceListAdapter(Context context, int resource) {
        super(context, resource);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newView = inflater.inflate(R.layout.device_layout, null);
        TextView text = (TextView) newView.findViewById(R.id.message_text);
        text.setText(peers.get(position).deviceName);
        return convertView;
    }
}
