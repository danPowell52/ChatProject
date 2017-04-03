package com.example.daniel.projectchatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Daniel on 04/03/2017.
 */

public class ClientThread extends Thread {
    private int port = 8888;
    OutputStream outputStream;
    InputStream inputStream;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    EncryptionManager encryptionManager;
    SignatureManager signatureManager;
    KeyMessage newKeys;
    Boolean running = true;
    Socket socket;
    Intent broadcastIntent;
    Context context;

    public ClientThread(Context c){
        context=c;
    }

    @Override
    public void run(){
        Log.d("Payara","server write called "+currentThread().getId() + "  "+ currentThread().getName());
        //initialise connection and lsiten
        socket = new Socket();
        try {
            Log.d("Payara", "before connect");
            socket.connect((new InetSocketAddress("192.168.49.1", port)), 500);
            Log.d("Payara", "after connect");
            //listen for messages
            outputStream = socket.getOutputStream();

            objectOutputStream = new ObjectOutputStream(outputStream);

            inputStream = socket.getInputStream();
            //note object streams cant be reread form the same stream or the stream will become corrupted
            objectInputStream = new ObjectInputStream(inputStream);
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
            String line = "";
            StringBuilder message = new StringBuilder();
            Log.d("Payara","SETTING SECURITY NUMBER 1");
            Log.d("Payara","client running "+running);
            Boolean ready;

            Object object;

            Boolean securitySet=false;
            encryptionManager = new EncryptionManager();
            //encryptionManager.generateKey();
            signatureManager = new SignatureManager();
            signatureManager.generateKeys();
            Log.d("Payara","SETTING SECURITY");



            while(running.equals(true)){
                //Log.d("Payara","server running "+in.ready());
                //Log.d("Payara Server reader","hello");


                while((object = objectInputStream.readObject()) != null){

                    if (securitySet.equals(false)){
                        newKeys = (KeyMessage)object;
                        encryptionManager.generateKey(new BigInteger(newKeys.getP()), new BigInteger(newKeys.getG()));
                        encryptionManager.genSecret(newKeys.getDHkey());
                        KeyMessage keyMessage = new KeyMessage();
                        keyMessage.setDHkey(encryptionManager.getPub().getEncoded());
                        keyMessage.setRSAkey(signatureManager.getPublicKey().getEncoded());
                        signatureManager.setReceivedPublic(newKeys.getRSAkey());
                        write(keyMessage);
                        securitySet = true;
                        Log.d("Payara", "security set");
                    } else {
                        Message newMessage = (Message)object;
                        if(signatureManager.verifySig(newMessage.getEncryptedMessage(), newMessage.getSignature())){
                            newMessage.setMessage(encryptionManager.receiveMessage(newMessage.getEncryptedMessage()));
                            broadcastIntent = new Intent().putExtra("message",newMessage.getMessage());
                            broadcastIntent.setAction("chatapp.received.message");
                            context.sendBroadcast(broadcastIntent);
                        }
                        Log.d("Payara", newMessage.getMessage());
                    }
                }

                /**
                 *
                 *
                ready = in.ready();
                while(ready.equals(true)){
                    Log.d("Payara","client ready "+in.ready());
                    line = in.readLine();
                    broadcastIntent = new Intent().putExtra("message",line);
                    broadcastIntent.setAction("chatapp.received.message");
                    context.sendBroadcast(broadcastIntent);
                    ready=false;
                }
                 **/
                // }


                //update any UI with messages received
                //Log.d("Payara Server reader", "helloes");

            }
            /**
            while(running.equals(true)){

                while((line = in.readLine()) != null){
                    broadcastIntent = new Intent().putExtra("message",line);
                    broadcastIntent.setAction("chatapp.received.message");
                    Log.d("Payara Server reader", line);
                    context.sendBroadcast(broadcastIntent);
                }


            } **/


            Log.d("Payara","client ended");



        } catch (IOException e) {
            Log.d("Payara","client failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            Log.d("Payara","CLosing in the finally");
            try {
                socket.close();
                objectInputStream.close();
                objectOutputStream.close();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void write(KeyMessage keys){


        try {
            Log.d("Payara","sending keys");

            objectOutputStream.writeObject(keys);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void write(String message){
        currentThread().getId();
        Log.d("Payara","client write called "+currentThread().getId() + "  "+ currentThread().getName());
        if(outputStream ==null|| inputStream ==null){
            Log.d("Payara","NULLS FOUND");
            return;
        } else {
            Log.d("Payara", "wrting try");
            try {
                String str = "This is a String ~ GoGoGo";
                if (message ==null){
                    return;
                }
                InputStream inputStream = new ByteArrayInputStream(message.getBytes());
                byte byteVar[] = new byte[1024];
                Message sendMessage = new Message();
                sendMessage.setEncryptedMessage(encryptionManager.sendMessage(message.getBytes()));
                sendMessage.setSignature(signatureManager.generateSig(sendMessage.getEncryptedMessage()));
                objectOutputStream.writeObject(sendMessage);
                objectOutputStream.flush();
                /**
                int len;
                while ((len = inputStream.read(byteVar)) != -1) {
                    outputStream.write(byteVar, 0, len);
                }
                 **/
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
