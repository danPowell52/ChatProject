package com.example.daniel.projectchatapp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;

/**
 * Created by Daniel on 04/03/2017.
 */

public class ServerThread extends Thread{
    Socket client;
    OutputStream outputStream;
    InputStream inputStream;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    SignatureManager signatureManager;
    EncryptionManager encryptionManager;
    Boolean running = true;
    Boolean connected = false;
    ChatServer context;
    Intent broadcastIntent;
    BigInteger p;
    BigInteger g;
    ServerSocket serverSocket;

    public ServerThread(ChatServer c){
        context=c;
    }

    @Override
    public void run(){
        serverSocket = null;
        Log.d("Payara","server write called "+currentThread().getId() + "  "+ currentThread().getName());
        try {
            serverSocket = new ServerSocket(8888);
            Log.d("Payara","before accept");
            client = serverSocket.accept();
            Log.d("Payara","after accept");
            int bitLength = 512; // 512 bits
            SecureRandom rnd = new SecureRandom();
            p = BigInteger.probablePrime(bitLength, rnd);
            g = BigInteger.probablePrime(bitLength, rnd);

            //listen for messages
            outputStream = client.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);

            inputStream = client.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            connected = true;
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(client.getInputStream()));
            String line = "";
            StringBuilder message = new StringBuilder();

            Log.d("Payara","server running "+running);
            int count = 0;
            Boolean ready;

            Object object;
            Boolean securitySet = false;
            encryptionManager = new EncryptionManager();
            encryptionManager.generateKey(p, g);
            Log.d("Payara","SETTING SECURITY");
            signatureManager = new SignatureManager();
            signatureManager.generateKeys();

            com.mycompany.chatappsecurity.KeyMessage keyMessage = new com.mycompany.chatappsecurity.KeyMessage();
            keyMessage.setP(p.toString());
            keyMessage.setG(g.toString());
            keyMessage.setDHkey(encryptionManager.getPub().getEncoded());
            keyMessage.setRSAkey(signatureManager.getPublicKey().getEncoded());
            if (securitySet.equals(false)){
                write(keyMessage);
            }

            while(running.equals(true)){
                //Log.d("Payara","server running "+in.ready());
                //Log.d("Payara Server reader","hello");
                count ++;
                ready = in.ready();




                while((object = objectInputStream.readObject()) != null){
                    if (securitySet.equals(false)){
                        com.mycompany.chatappsecurity.KeyMessage newKeys = (com.mycompany.chatappsecurity.KeyMessage)object;
                        encryptionManager.genSecret(newKeys.getDHkey());
                        signatureManager.setReceivedPublic(newKeys.getRSAkey());
                        securitySet = true;
                        Log.d("Payara", "security set");
                    } else {
                        com.mycompany.chatappsecurity.Message newMessage = (com.mycompany.chatappsecurity.Message)object;
                        if(signatureManager.verifySig(newMessage.getEncryptedMessage(), newMessage.getSignature())){
                            Log.d("Payara", "message is "+newMessage.getEncryptedMessage().toString());
                            newMessage.setMessage(encryptionManager.receiveMessage(newMessage.getEncryptedMessage()));
                            //if the message has a file
                            if (newMessage.getMessage().equals("hasFile")){
                                System.out.println("file found");
                                final File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) ,
                                        context.getPackageName() + "wifip2pshared-" + System.currentTimeMillis()
                                        + "."+newMessage.getFileType());
                                Intent intent =
                                        new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(f) );
                                //intent.setData(Uri.fromFile(file));
                                context.sendBroadcast(intent);
                                try {
                                    System.out.println("received file is " + newMessage.getFile());
                                    InputStream myInputStream = new ByteArrayInputStream(newMessage.getFile());
                                    copyFile(myInputStream, new FileOutputStream(f));
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                            broadcastIntent = new Intent().putExtra("message",newMessage.getMessage());
                            broadcastIntent.setAction("chatapp.received.message");
                            context.sendBroadcast(broadcastIntent);

                        }



                        Log.d("Payara", newMessage.getMessage());
                    }

                }
                /**
                while(ready.equals(true)){
                    Log.d("Payara","server ready "+in.ready());
                    //Log.d("Payara","server read "+in.readLine());
                    line = in.readLine();
                    /**
                    while((line = in.readLine()) !=null){
                        Log.d("Payara","server read the line"+line);
                    }
                     **/
                    //line = in.readLine();
                /**
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



            Log.d("Payara","server ended");
        } catch (IOException e) {
            Log.d("Payara", "server failed");
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            Log.d("Payara","CLosing in the finally");
            try {
                client.close();
                serverSocket.close();
                objectInputStream.close();
                objectOutputStream.close();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void write(String fileType, String file){
        Log.d("Payara","Send file called");
        if(outputStream ==null|| inputStream ==null){
            Log.d("Payara","NULLS FOUND");
            return;
        } else {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            Log.d("Payara","file uri "+ file);
            try {
                InputStream fileStream = context.getContentResolver().openInputStream(Uri.parse(file));
                int line;
                byte[] bytes = new byte[16384];
                while ((line = fileStream.read()) != -1) {
                    buffer.write(line);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            com.mycompany.chatappsecurity.Message sendMessage = new com.mycompany.chatappsecurity.Message();
            sendMessage.setFileType(fileType);
            sendMessage.setEncryptedMessage(encryptionManager.sendMessage("hasFile".getBytes()));
            sendMessage.setSignature(signatureManager.generateSig(sendMessage.getEncryptedMessage()));
            sendMessage.setfile(buffer.toByteArray());
            Log.d("Payara","get file is " +sendMessage.getFile());
            sendMessage.setHasFile(true);
            try {
                objectOutputStream.writeObject(sendMessage);
                objectOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void write(com.mycompany.chatappsecurity.KeyMessage keys){

        try {
            Log.d("Payara","sending keys");

            objectOutputStream.writeObject(keys);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void write(String message){
        // convert String into InputStream
        currentThread().getId();
        Log.d("Payara","server write called "+currentThread().getId() + "  "+ currentThread().getName());
        if(outputStream ==null|| inputStream ==null){
            Log.d("Payara","NULLS FOUND");
            return;
        } else {
            Log.d("Payara", "wrting try");
            try {
                //String str = "This is a String ~ GoGoGo";
                if (message ==null){
                    //everynow and then the connection refreshes and sends null message so stop it here
                    return;
                }

                InputStream inputStream = new ByteArrayInputStream(message.getBytes());
                byte byteVar[] = new byte[1024];

                //encryption manager needs to encyprt and sing string to send
                com.mycompany.chatappsecurity.Message sendMessage = new com.mycompany.chatappsecurity.Message();
                sendMessage.setEncryptedMessage(encryptionManager.sendMessage(message.getBytes()));
                Log.d("Payara", "message is "+sendMessage.getEncryptedMessage());
                sendMessage.setSignature(signatureManager.generateSig(sendMessage.getEncryptedMessage()));
                objectOutputStream.writeObject(sendMessage);
                objectOutputStream.flush();
                /**
                int len;
                while ((len = inputStream.read(byteVar)) != -1) {
                    outputStream.write(byteVar, 0, len);
                }
                 **/
            } catch (IOException e) {

            }
        }
    }

    public void write(InputStream inputStream, String fileUri){
        ContentResolver cr = context.getContentResolver();
        InputStream is = null;
        try {
            is = cr.openInputStream(Uri.parse(fileUri));
        } catch (FileNotFoundException e) {
            Log.d("Payara", e.toString());
        }
    }

    public static boolean copyFile(InputStream inputStream, OutputStream out) {
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read()) != -1) {
                out.write(len);

            }
            out.close();
            inputStream.close();
        } catch (IOException e) {
            Log.d("MainActivity", e.toString());
            return false;
        }
        return true;
    }

    public void end(){
        running=false;
    }

    public boolean getConnected(){
        return connected;
    }
}
