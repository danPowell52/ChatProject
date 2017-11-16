package com.example.daniel.projectchatapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Daniel on 03/05/2017.
 */

public class WriteFile extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        try {
            InputStream inputstream = (InputStream)params[0] ;
            //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(Uri.fromFile(file).toString())));
            FileOutputStream outputStream = new FileOutputStream((File)params[1]);
            int line;
            while ((line =inputstream.read())!= -1){
                outputStream.write(line);
            }

            //outputStream.write(inputstream.read());
            outputStream.close();
            System.out.println("FIle created"+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
