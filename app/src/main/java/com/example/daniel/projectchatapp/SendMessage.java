package com.example.daniel.projectchatapp;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Daniel on 01/04/2017.
 */

public class SendMessage extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        Log.d("Payara","async "+ params[0].toString());
        if (params[1] == null){
            return null;
        } else{
            ClientThread clientThread = (ClientThread)params[0];
            clientThread.write(params[1].toString());
        }


        return null;
    }

    public void setThread(){

    }

    public void setMessage(){}

}
