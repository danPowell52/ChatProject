package com.example.daniel.projectchatapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by Daniel on 02/03/2017.
 */

public class PeerActivity extends AppCompatActivity {
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    TextView textView;
    ViewGroup layout;
    WifiP2pDevice connector;
    Boolean owner;
    Intent intent;
    Intent client;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peer_activity);
        textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("wfif page");
        layout =(ViewGroup) findViewById(R.id.peer_activity);
        layout.addView(textView);

        intent = new Intent(this, ChatServer.class);
        client = new Intent(this, ChatClient.class);
        //functionality
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new Wifip2pBroadcastReceiver(mManager, mChannel, this);
        search();

    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public void search(){
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                textView.setText("Searching");
                //Searching is an asynchronous search and will not return in time to set status
                //setPeerStatus(((WiFiDirectBroadcastReceiver)mReceiver).getPeers());
            }

            @Override
            public void onFailure(int reasonCode) {
                //textView.setText("fail because ... " + reasonCode);
            }
        });
    }

    public void setPeerStatus(List<WifiP2pDevice> peers) {
        StringBuilder peerList = new StringBuilder();

        TextView peerStatus = new TextView(this);
        peerStatus.setTextSize(40);
        Log.d("Payara", "searching " + peers.size());
        for (WifiP2pDevice ddevice : peers){
            Log.d("Payara", ddevice.deviceName);
            Log.d("Payara", ddevice.toString());
        }
        if (peers.size() < 1){
            textView.setText("no devices found");
        } else {
            for(WifiP2pDevice device: peers){
                peerList.append(device);
            }
            textView.setText(peerList);
            connector = peers.get(0);
            //connect(peers.get(0));
            //group();

        }
        layout.addView(peerStatus);
    }

    public void connect(View view){
        connect(connector);
    }

    public void connect(WifiP2pDevice device){
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;


        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                //success logic
                Log.d("Payara", "Connection Started");
                textView.setText("WE HAVE LIFTOFF");


                //ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                //NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                //Log.d("Payara","networkInfo "+ networkInfo);
                //mManager.requestConnectionInfo(mChannel, new ConnectionService());
                /**
                mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo info) {
                        Log.d("Payara","conn stuff " + info.toString());
                        InetAddress groupOwnerAddress = info.groupOwnerAddress;
                        if (info.isGroupOwner){
                            owner = true;
                        } else {
                            owner = false;
                        }
                        Log.d("Payara" , "group owner form info" + groupOwnerAddress.toString());
                    }
                });
                 */


            }

            @Override
            public void onFailure(int reason) {
                //failure logic
                Log.d("Payara", "Connection Failed");
            }
        });
        Log.d("Payara", "end of connect method");
/*
        mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                Log.d("Payara","group stuff" + group.toString());
                //group.getOwner();
                //Log.d("payara"," info owner" + group.isGroupOwner());
            }
        });
        */
    }

    public void group(){

        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Device is ready to accept incoming connections from peers.
                startService(intent);
                startService(client);
            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }

    public void setOwner(Boolean newOwner){
        owner = newOwner;
    }

    public void sendMessage(View view){
        sendMessage();
    }

    public void sendMessage(){
        Log.d("Payara", "Send Message "+ owner);

        if(owner.equals(true)){
            Log.d("Payara","message server");
            startService(intent);
        } else if(owner.equals(false)) {
            Log.d("Payara","message client");
            startService(client);
        }

    }
}
