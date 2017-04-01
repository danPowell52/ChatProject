package com.example.daniel.projectchatapp;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by Daniel on 28/03/2017.
 */

public class SignatureManager {
    PrivateKey privateKey;
    PublicKey publicKey;
    PublicKey receivedPublic;

    public void getPrivateKey(){

    }

    public PublicKey getPublicKey(){
        return publicKey;
    }

    public PublicKey getReceivedPublic() {
        return receivedPublic;
    }

    public void setReceivedPublic(byte[] receivedPublic) {
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(receivedPublic);
            PublicKey pubKey =
                    keyFactory.generatePublic(pubKeySpec);
            this.receivedPublic = pubKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        //this.receivedPublic = receivedPublic;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void generateKeys(){
        Log.d("Payara","generating keys");
        KeyPairGenerator keyGenerator = null;
        try {
            keyGenerator = KeyPairGenerator.getInstance("RSA");
            keyGenerator.initialize(512);
            KeyPair pair = keyGenerator.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public byte[] generateSig(byte[] message){
        try {

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);



            signature.update(message);
            byte[] realSig = signature.sign();
            byte[] key = publicKey.getEncoded();
            return realSig;
            //verifySig(publicKey.getEncoded(), realSig);
        }catch(Exception e){
            System.out.println("fuck boy 1 "+ e);

        }
        return null;
    }

    public Boolean verifySig( byte[] message,byte[] sig){

        try {
            //System.out.println(key.getFormat());

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(receivedPublic);
            signature.update(message);
            boolean verifies = signature.verify(sig);
            System.out.println("signature verifies: " + verifies);
            return verifies;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("fuck boy 2 "+ e);
        //}  catch (InvalidKeySpecException e) {
        //    e.printStackTrace();
        //    System.out.println("fuck boy 3 "+ e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            System.out.println("fuck boy 4 "+ e);
        } catch (SignatureException e) {
            e.printStackTrace();
            System.out.println("fuck boy 5 "+ e);
        }
        return false;
    }
}
