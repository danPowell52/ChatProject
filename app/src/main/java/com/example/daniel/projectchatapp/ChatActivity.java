package com.example.daniel.projectchatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.widget.ListView;

/**
 * Created by Daniel on 05/03/2017.
 */

public class ChatActivity extends AppCompatActivity {

    public String hello = "hello";

    public String getHello(){
        return hello;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
    }



}
