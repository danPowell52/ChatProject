package com.example.daniel.projectchatapp;

import android.app.IntentService;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.net.InetAddress;

/**
 * Created by Daniel on 03/03/2017.
 */

public class ConnectionService extends IntentService implements WifiP2pManager.ConnectionInfoListener {

    public ConnectionService(){
        super("ConnectionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        Log.d("Payara","conn stuff " + info.toString());
        InetAddress groupOwnerAddress = info.groupOwnerAddress;
        Log.d("Payara" , "group owner form info" + groupOwnerAddress.toString());
    }
}
