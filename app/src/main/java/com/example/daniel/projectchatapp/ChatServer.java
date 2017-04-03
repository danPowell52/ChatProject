package com.example.daniel.projectchatapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Daniel on 03/03/2017.
 */

public class ChatServer extends Service {
    private ServerThread socketControl;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setMessage(){
        Log.d("Payara","callback") ;
    }

    @Override
    public void onCreate(){
        Log.d("Payara","Server Create");
        socketControl = new ServerThread(this);
        socketControl.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int id) {

        Log.d("Payara","Server Write");
        if (socketControl.getConnected()){
            socketControl.write(intent.getStringExtra("message"));

        }



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("Payara", " CHat Service Destroyed");
        socketControl.end();

    }








    public void warble(){
        Log.d("Payara", "Intent Handled");
        int PORT = 8888;
        Socket socket = new Socket();

        try {
            Log.d("Payara","In the try");
            ServerSocket serverSocket = new ServerSocket();
            Log.d("Payara","before accept");
            Socket client = serverSocket.accept();
            //socket.connect(new InetSocketAddress("Dan Powell", PORT), 500);

            Log.d("Payara","after accept");

            // convert String into InputStream
            String str = "This is a String ~ GoGoGo";
            InputStream inputStream = new ByteArrayInputStream(str.getBytes());
            byte byteVar[] = new byte[1024];

            OutputStream outputStream = client.getOutputStream();
            int len;
            while ( (len = inputStream.read(byteVar)) != -1) {
                outputStream.write(byteVar, 0, len);
            }

            Log.d("Payara", "String sent");
            outputStream.close();
            inputStream.close();
            client.close();

        } catch (IOException e) {
            Log.d("Payara", "in the catch .... "+ e);
            e.printStackTrace();
        }
        finally {
            Log.d("Payara", "in the finally");
            if (socket != null) {
                if (socket.isConnected()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        //catch logic
                    }
                }
            }
        }
    }
}
