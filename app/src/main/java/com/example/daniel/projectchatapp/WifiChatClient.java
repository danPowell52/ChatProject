package com.example.daniel.projectchatapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Daniel on 08/04/2017.
 */

public class WifiChatClient extends Service{

    ClientThread socketControl;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("Payara","client create");
        socketControl = new ClientThread(this);
        socketControl.setAddress("10.72.97.218");
        socketControl.setPort(18999);

        socketControl.start();
    }


    @Override
    public int onStartCommand(Intent intent, int flag, int id) {
        Log.d("Payara","client write " +intent.toString());
        if (intent.getAction() != null) {
            if (intent.getAction().equals("setUsername")) { //protect against nulls
                Log.d("Payara", "setting username");
                socketControl.setUsername(intent.getStringExtra("username"));
                socketControl.setRecipient(intent.getStringExtra("recipient"));
            }
        }
        SendMessage sendMessage = new SendMessage();

        sendMessage.execute("p2pclient",socketControl, intent.getStringExtra("message"),intent.getStringExtra("fileType"),intent.getStringExtra("fileUri"));
        //socketControl.write(intent.getStringExtra("message"));


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("Payara", " CHat Service Destroyed");
        socketControl.end();
    }

}
