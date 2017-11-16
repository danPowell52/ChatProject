package com.example.daniel.projectchatapp;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Daniel on 04/04/2017.
 */

public class WifiThread extends Thread {
    @Override
    public void run() {
        /**
        URL url = null;
        try {
            String registrationUrl = String.format("http://192.168.0.15:8080/hello");
            url = new URL(registrationUrl);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d("MyApp", "Registration success");
            } else {
                Log.w("MyApp", "Registration failed for: " + registrationUrl);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         **/

        Socket client = null;
        try {
            client = new Socket();
            client.connect(new InetSocketAddress("10.72.97.218", 18999),500);
            Log.d("Payara","connected");
            client.close();
        } catch (IOException e) {
            try {
                client.close();
                Log.d("Payara","client closed");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();

        }

    }

    //get Files
    /**
     * Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
     intent.setType("image/*");
     startActivityForResult(intent, CHOOSE_FILE_RESULT_CODE); choose variable is an int of value 20


     @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {

     // User has picked an image. Transfer it to group owner i.e peer using
     // FileTransferService.
     Uri uri = data.getData();
     TextView statusText = (TextView) mContentView.findViewById(R.id.status_text);
     statusText.setText("Sending: " + uri);
     Log.d(WiFiDirectActivity.TAG, "Intent----------- " + uri);
     Intent serviceIntent = new Intent(getActivity(), FileTransferService.class);
     serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
     serviceIntent.putExtra(FileTransferService.EXTRAS_FILE_PATH, uri.toString());
     serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
     info.groupOwnerAddress.getHostAddress());
     serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, 8988);
     getActivity().startService(serviceIntent);
     }


        //process the intent from above
     if (intent.getAction().equals(ACTION_SEND_FILE)) {
     String fileUri = intent.getExtras().getString(EXTRAS_FILE_PATH);
     String host = intent.getExtras().getString(EXTRAS_GROUP_OWNER_ADDRESS);
     Socket socket = new Socket();
     int port = intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);
     */
}
