package com.example.daniel.projectchatapp;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Daniel on 29/03/2017.
 */

public class KeyMessage implements Serializable{
    byte[] RSAkey;
    byte[] DHkey;
    String p;
    String g;

    public byte[] getRSAkey() {
        return RSAkey;

    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getP() {
        return p;
    }

    public void setP(String p){
        this.p = p;
    }

    public void setRSAkey(byte[] RSAkey) {
        this.RSAkey = RSAkey;
    }

    public byte[] getDHkey() {
        return DHkey;
    }

    public void setDHkey(byte[] DHkey) {
        this.DHkey = DHkey;
    }
}
