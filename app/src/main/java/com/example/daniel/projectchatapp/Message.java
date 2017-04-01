package com.example.daniel.projectchatapp;

import java.io.Serializable;

/**
 * Created by Daniel on 09/03/2017.
 */

public class Message implements Serializable{
    public String body ="hello world";
    public byte[] encryptedMessage;
    public Boolean isMine = true;
    byte[] signature;

    public void setSignature(byte[] signature){
        this.signature = signature;
    }

    public byte[] getSignature(){
        return signature;
    }

    public Boolean isMine(){
        return true;
    }

    public Message(){

    }

    public Message(String message, Boolean mine){
        body=message;
        isMine=mine;
    }

    public void setMessage(String message){
        body = message;
    }

    public String getMessage(){
        return body;
    }

    public void setIsMine(Boolean mine){
        isMine = mine;
    }

    public Boolean getIsMine(){
        return isMine;
    }

    public byte[] getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setEncryptedMessage(byte[] encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }
}
