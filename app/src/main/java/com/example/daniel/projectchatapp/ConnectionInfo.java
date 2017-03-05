package com.example.daniel.projectchatapp;

import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.net.InetAddress;

/**
 * Created by Daniel on 02/03/2017.
 */

public class ConnectionInfo implements WifiP2pManager.ConnectionInfoListener, WifiP2pManager.GroupInfoListener{
    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        // InetAddress from WifiP2pInfo struct.
        InetAddress groupOwnerAddress = info.groupOwnerAddress;
        Log.d("Payara" , "group owner form info" + groupOwnerAddress.toString());
        // After the group negotiation, we can determine the group owner
        // (server).
        if (info.groupFormed && info.isGroupOwner) {
            Log.d("Payara", "I AM OVERLORD");
            // Do whatever tasks are specific to the group owner.
            // One common case is creating a group owner thread and accepting
            // incoming connections.
        } else if (info.groupFormed) {
            // The other device acts as the peer (client). In this case,
            // you'll want to create a peer thread that connects
            // to the group owner.
        }

    }

    @Override
    public void onGroupInfoAvailable(WifiP2pGroup group) {
        Log.d("Payara","group stuff" + group.toString());
        //group.getOwner();
        //Log.d("payara"," info owner" + group.isGroupOwner());
    }
}
