package com.example.daniel.projectchatapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 02/03/2017.
 */

public class Wifip2pBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private PeerActivity mActivity;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();



    public Wifip2pBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                    PeerActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled

            } else {
                // Wi-Fi P2P is not enabled
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            Log.d("Payara", "peers detected");
            if (mManager != null) {
                mManager.requestPeers(mChannel, peerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            Log.d("Payara", "connection changed");
            if (mManager == null) {
                Log.d("Payara", "manager is null");
                return;
            }

            WifiP2pInfo wifiInfo =  intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
            Log.d("Payara","helloes "+ wifiInfo.toString());

            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            Log.d("Payara", "netowrk info "+networkInfo.isConnected());
            Log.d("Payara", "netowrk info "+networkInfo.toString());
            if (networkInfo.isConnected()) {
                Log.d("Payara", "connected in receiver");
                final Intent server = new Intent(mActivity, ChatServer.class);
                final Intent client = new Intent(mActivity, ChatClient.class);
                mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {

                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo info) {

                        InetAddress groupOwnerAddress = info.groupOwnerAddress;
                        if (info.isGroupOwner){
                            mActivity.setOwner(true);
                            mActivity.startService(server);

                            mActivity.startService(server);

                        } else {
                            mActivity.setOwner(false);
                            mActivity.startService(client);
                        }
                        String s=groupOwnerAddress.getHostAddress();
                        Log.d("Payara", "group "+ s);

                    }
                });
            } else {
                Log.d("Payara", "disconnected in receiver");
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            Log.d("Payara", "action changed");
        }

    }

    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {

        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            List<WifiP2pDevice> refreshedPeers = new ArrayList<>();
            refreshedPeers.addAll(peerList.getDeviceList());
            //Log.d("Payara", peerList.getDeviceList().toString());
            for (WifiP2pDevice ddevice : peers){
                //Log.d("payara", ddevice.deviceName);
                //Log.d("payara2", ddevice.toString());
            }
            if (!refreshedPeers.equals(peers)) {
                peers.clear();
                peers.addAll(refreshedPeers);

                // If an AdapterView is backed by this data, notify it
                // of the change.  For instance, if you have a ListView of
                // available peers, trigger an update.
                //((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
                mActivity.setPeerStatus(peers);
                // Perform any other updates needed based on the new list of
                // peers connected to the Wi-Fi P2P network.
            }

            if (peers.size() == 0) {
                //Log.d(WiFiDirectActivity.TAG, "No devices found");
                return;
            }
        }

    };
}
