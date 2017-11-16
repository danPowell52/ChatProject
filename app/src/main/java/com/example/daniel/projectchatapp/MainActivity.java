package com.example.daniel.projectchatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.math.BigInteger;
import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SendMessage blah = new SendMessage();
        //blah.execute("hello");
        Log.d("Payara","NULLS FOUND");
    }

    public void testP2p(View view){
        Intent intent = new Intent(this, PeerActivity.class);
        startActivity(intent);
    }

    public void testChat(View view){
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    public void testWifi(View view){
        Intent intent = new Intent(this, WifiActivity.class);
        startActivity(intent);
    }
}
