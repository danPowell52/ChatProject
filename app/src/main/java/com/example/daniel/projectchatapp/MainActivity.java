package com.example.daniel.projectchatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        /**
        EncryptionManager Bob = new EncryptionManager();
        EncryptionManager Alice = new EncryptionManager();
        int bitLength = 512; // 512 bits
        SecureRandom rnd = new SecureRandom();
        BigInteger p;
        BigInteger g;
        p = BigInteger.probablePrime(bitLength, rnd);
        g = BigInteger.probablePrime(bitLength, rnd);
        Bob.generateKey(p,g);
        String newp = p.toString();
        String newg = g.toString();
        p = new BigInteger(newp);
        g = new BigInteger(newg);
        Alice.generateKey(p,g);
        System.out.println(Bob.getPub());
        System.out.println(Alice.getPub());

        //heres where transmission of keys occurrs
        Bob.genSecret(Alice.getPub().getEncoded());
        Alice.genSecret(Bob.getPub().getEncoded());

        //this will be called in the the read after the line is finished
        Alice.receiveMessage(Bob.sendMessage("hello".getBytes()));
         **/
    }

    public void testP2p(View view){
        Intent intent = new Intent(this, PeerActivity.class);
        startActivity(intent);
    }

    public void testChat(View view){
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }
}
