package com.example.daniel.projectchatapp;

/**
 * Created by Daniel on 09/03/2017.
 */

public class Message {
    public String body ="hello world";
    public Boolean isMine = true;
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



}
