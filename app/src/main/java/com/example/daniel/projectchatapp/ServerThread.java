package com.example.daniel.projectchatapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Daniel on 04/03/2017.
 */

public class ServerThread extends Thread{
    Socket client;
    OutputStream outputStream;
    InputStream inputStream;
    Boolean running = true;
    Boolean connected = false;

    @Override
    public void run(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8888);
            Log.d("Payara","before accept");
            Socket client = serverSocket.accept();
            Log.d("Payara","after accept");

            //listen for messages
            outputStream = client.getOutputStream();

            inputStream = client.getInputStream();
            connected = true;
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(client.getInputStream()));
            String line = "";
            StringBuilder message = new StringBuilder();
            Log.d("Payara","server running "+running);
            while(running.equals(true)){

                while((line = in.readLine()) != null){

                    Log.d("Payara Server reader", line);
                }

                //update any UI with messages received
                //Log.d("Payara Server reader", "helloes");

            }

            Log.d("Payara","server ended");
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.d("Payara","server failed");
            e.printStackTrace();
        }

    }

    public void write(){
        // convert String into InputStream
        currentThread().getId();
        Log.d("Payara","server write called "+currentThread().getId() + "  "+ currentThread().getName());
        if(outputStream ==null|| inputStream ==null){
            Log.d("Payara","NULLS FOUND");
            return;
        } else {
            Log.d("Payara", "wrting try");
            try {
                String str = "This is a String ~ GoGoGo";
                InputStream inputStream = new ByteArrayInputStream(str.getBytes());
                byte byteVar[] = new byte[1024];

                OutputStream outputStream = client.getOutputStream();
                int len;
                while ((len = inputStream.read(byteVar)) != -1) {
                    outputStream.write(byteVar, 0, len);
                }
            } catch (IOException e) {

            }
        }
    }


    public void end(){
        running=false;
    }

    public boolean getConnected(){
        return connected;
    }
}
