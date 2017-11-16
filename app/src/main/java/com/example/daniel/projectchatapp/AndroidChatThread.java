package com.example.daniel.projectchatapp;

import android.content.Context;
import android.content.Intent;

import com.mycompany.chatappsecurity.*;

/**
 * Created by Daniel on 07/04/2017.
 */

public class AndroidChatThread extends ChatThread {
    Context context;
    public AndroidChatThread(String role, com.mycompany.chatappsecurity.SignatureManager signatureManager, int port, String address) {
        super(role, signatureManager, port, address);
    }

    public AndroidChatThread(String role, Context c, com.mycompany.chatappsecurity.SignatureManager signatureManager, int port, String address) {
        super(role, signatureManager, port, address);
        this.context = c;
    }

    @Override
    public String getMessage(String message, String recipient, String sender){
        Intent broadcastIntent = new Intent().putExtra("message",message);
        broadcastIntent.setAction("chatapp.received.message");
        context.sendBroadcast(broadcastIntent);
        return null;
    }
}
