package com.example.daniel.projectchatapp;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Daniel on 03/05/2017.
 */

public class ReadFile extends AsyncTask {
    @Override
    protected byte[] doInBackground(Object[] params) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            InputStream fileStream = (InputStream)params[0];
            int line;
            byte[] bytes = new byte[16384];
            //readFile(fileStream);
            while ((line = fileStream.read()) != -1) {
                buffer.write(line);
            }
            buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }
}
