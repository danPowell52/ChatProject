package com.example.daniel.projectchatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Daniel on 04/03/2017.
 */

public class ClientThread extends Thread {
    private int port = 8888;
    OutputStream outputStream;
    InputStream inputStream;
    Boolean running = true;
    Socket socket;
    Intent broadcastIntent;
    Context context;

    public ClientThread(Context c){
        context=c;
    }

    @Override
    public void run(){
        //initialise connection and lsiten
        socket = new Socket();
        try {
            Log.d("Payara", "before connect");
            socket.connect((new InetSocketAddress("192.168.49.1", port)), 500);
            Log.d("Payara", "after connect");
            //listen for messages
            outputStream = socket.getOutputStream();

            inputStream = socket.getInputStream();
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
            String line = "";
            StringBuilder message = new StringBuilder();
            Log.d("Payara","client running "+running);
            while(running.equals(true)){

                while((line = in.readLine()) != null){
                    broadcastIntent = new Intent().putExtra("message",line);
                    broadcastIntent.setAction("chatapp.received.message");
                    Log.d("Payara Server reader", line);
                    context.sendBroadcast(broadcastIntent);
                }


            }

            Log.d("Payara","client ended");
            outputStream.close();
            inputStream.close();


        } catch (IOException e) {
            Log.d("Payara","client failed");
            e.printStackTrace();
        }
    }

    public void write(String message){
        currentThread().getId();
        Log.d("Payara","client write called "+currentThread().getId() + "  "+ currentThread().getName());
        if(outputStream ==null|| inputStream ==null){
            Log.d("Payara","NULLS FOUND");
            return;
        } else {
            Log.d("Payara", "wrting try");
            try {
                String str = "This is a String ~ GoGoGo";
                InputStream inputStream = new ByteArrayInputStream(message.getBytes());
                byte byteVar[] = new byte[1024];

                OutputStream outputStream = socket.getOutputStream();
                int len;
                while ((len = inputStream.read(byteVar)) != -1) {
                    outputStream.write(byteVar, 0, len);
                }
                Log.d("Payara","Client Message Sent");
            } catch (IOException e) {

            }
            Log.d("Payara","Client Message Sent");
        }
    }

    public void end(){
        running = false;

    }
}
