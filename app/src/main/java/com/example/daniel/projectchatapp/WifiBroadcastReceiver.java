package com.example.daniel.projectchatapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Daniel on 04/05/2017.
 */

public class WifiBroadcastReceiver extends BroadcastReceiver {
    private ChatActivity mActivity;

    public WifiBroadcastReceiver(ChatActivity activity){
        this.mActivity = activity;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == "chatapp.received.message"){
            Log.d("Payara","intent message received");
            //less coupled way to work
            ChatFragment fragment = (ChatFragment)mActivity.getFragmentManager().findFragmentById(R.id.frag_list);
            fragment.receiveMessage(intent.getStringExtra("message"));
        }
    }
}
