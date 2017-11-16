package com.example.daniel.projectchatapp;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.widget.ListView;

/**
 * Created by Daniel on 05/03/2017.
 */

public class ChatActivity extends AppCompatActivity {

    private String hello = "hello";
    private WifiBroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;

    public String getHello(){
        return hello;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("chatapp.received.message");
        mReceiver = new WifiBroadcastReceiver(this);
    }

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


}
