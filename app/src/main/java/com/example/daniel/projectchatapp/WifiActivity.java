package com.example.daniel.projectchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Daniel on 04/04/2017.
 */

public class WifiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_activity_layout);

        /**
        ClientThread thread = new ClientThread(this);
        thread.setPort(18999);
        thread.setAddress("10.72.97.218");
        thread.start();
         **/

    }

    public void wifiConnect(View view){
        Intent intent = new Intent(this, ChatActivity.class);
        EditText username = (EditText) findViewById(R.id.username);
        EditText recipient = (EditText) findViewById(R.id.recipient);

        startActivity(intent);

        Intent serviceIntent = new Intent(this, WifiChatClient.class);
        serviceIntent.setAction("setUsername");
        serviceIntent.putExtra("username",username.getText().toString());
        serviceIntent.putExtra("recipient",recipient.getText().toString());
        startService(serviceIntent);
    }
}
