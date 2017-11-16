package com.example.daniel.projectchatapp;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Daniel on 01/04/2017.
 */

public class SendMessage extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {

        if (params[1] == null){
            return null;
        } else{
            if (params[0].toString()=="p2pserver"){

                ServerThread thread = (ServerThread)params[1];
                if (params[2] != null ){
                    Log.d("Payara","Send message to server "+ params[2].toString());
                    if (params[2].toString().equals("hasFile")){
                        Log.d("Payara","Send file to server");
                        thread.write(params[3].toString(), params[4].toString());
                    } else {
                        thread.write(params[2].toString());
                    }


                }
            } else if (params[0].toString()=="p2pclient"){

                ClientThread thread = (ClientThread)params[1];
                if (params[2] != null ){
                    Log.d("Payara","send message to client " + params[2].toString());
                    if (params[2].toString().equals("hasFile")){
                        Log.d("Payara","Send file to server");
                        thread.write(params[3].toString(),  params[4].toString());
                    } else {
                        thread.write(params[2].toString());
                    }


                }
            }
        }


        /**
        Log.d("Payara","async "+ params[0].toString());
        if (params[1] == null){
            return null;
        } else{
            ClientThread clientThread = (ClientThread)params[0];
            clientThread.write(params[1].toString());
        }
         **/


        return null;
    }



    public void setThread(){

    }

    public void setMessage(){}

}
