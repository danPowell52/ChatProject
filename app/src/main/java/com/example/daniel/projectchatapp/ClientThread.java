package com.example.daniel.projectchatapp;

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
                line = in.readLine();
                Log.d("Payara Client reader", message.toString());
                //update any UI with messages received
                if (line != null){
                    message.append(line);
                    Log.d("Payara Client reader", message.toString());
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

    public void write(){
        currentThread().getId();
        Log.d("Payara","client write called "+currentThread().getId() + "  "+ currentThread().getName());
        if(outputStream ==null|| inputStream ==null){
            Log.d("Payara","NULLS FOUND");
            return;
        } else {
            Log.d("Payara", "wrting try");
            try {
                String str = "This is a String ~ GoGoGo";
                InputStream inputStream = new ByteArrayInputStream(str.getBytes());
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
