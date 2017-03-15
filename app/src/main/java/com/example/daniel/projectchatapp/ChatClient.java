package com.example.daniel.projectchatapp;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Daniel on 03/03/2017.
 */

public class ChatClient extends Service {
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
        socketControl.start();
    }


    @Override
    public int onStartCommand(Intent intent, int flag, int id) {
        Log.d("Payara","client write " +intent.toString());

        socketControl.write(intent.getStringExtra("message"));


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        socketControl.end();
    }









    public void warble(){
        //think about a wifilock for android
        Log.d("Payara", "MyClient started");

        Context context = this.getApplicationContext();
        //WifiP2pDevice wifiP2pDevice =  (WifiP2pDevice)intent.getParcelableExtra("device");

        //String host = wifiP2pDevice.deviceName;

        int port = 8888;
        int len;
        Socket socket = new Socket();
        byte buf[]  = new byte[1024];

        try {
            /**
             * Create a client socket with the host,
             * port, and timeout information.
             */

            // Log.d("Payara", " device name is " + wifiP2pDevice.deviceName);
            Log.d("Payara", " Host name is " + InetAddress.getLocalHost());
            socket.bind(null);
            InetSocketAddress hello = new InetSocketAddress("localhost", port);
            Log.d("Payara", "  " + hello.isUnresolved());
            //String addr = InetAddress.getByName(wifiP2pDevice.deviceAddress).getHostName();
            //Log.d("Payara", "address " +addr);192.168.49.1
            socket.connect((new InetSocketAddress("localhost", port)), 500);
            Log.d("Payara", "MyClient connected");
            /**
             * Create a byte stream from a JPEG file and pipe it to the output stream
             * of the socket. This data will be retrieved by the server device.
             */
            OutputStream outputStream = socket.getOutputStream();

            InputStream inputStream = socket.getInputStream();
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
            String line = "";
            StringBuilder message = new StringBuilder();
            while((line = in.readLine()) != null){
                message.append(line);
            }
            Log.d("Payara reader", message.toString());


            outputStream.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            //catch logic
        } catch (IOException e) {
            //catch logic
        }

/**
 * Clean up any open sockets when done
 * transferring or if an exception occurred.
 */
        finally {
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
