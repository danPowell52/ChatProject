package com.example.daniel.projectchatapp;


import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 * Created by Daniel on 28/03/2017.
 */

public class EncryptionManager {
    String message = "This is a string go go";
    PublicKey myPublicKey;
    PrivateKey myPrivateKey;
    byte[] secretKey;
    BigInteger p = new BigInteger("8869998188050584552566469753637748692311194181235512997243787255044945932122231229878152404196352011928024744217310781140445480230017304932545578901079989");
    BigInteger g = new BigInteger("10273555685686400395906274508496348613855725652394316041951327916278766242705880898162930067802504895237306256527529216660314218503674398731871648448369603");



    public void generateKey(BigInteger p, BigInteger g){
        KeyPairGenerator keyGenerator;
        //FinalpublicKey.getEncoded();
        //FinalpublicKey.getFormat();

        try {

            DHParameterSpec param = new DHParameterSpec(p, g);
            keyGenerator = KeyPairGenerator.getInstance("DH");
            keyGenerator.initialize(param); //1024 works with 16
            KeyPair pair = keyGenerator.generateKeyPair();

            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();
            myPrivateKey = privateKey;
            myPublicKey = publicKey;
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            //Logger.getLogger(Diffie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

    }



    public PrivateKey getPriv(){
        return myPrivateKey;
    }

    public PublicKey getPub(){
        return myPublicKey;
    }


    public void genSecret( byte[] publickey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publickey);
            PublicKey publicKey =
                    keyFactory.generatePublic(pubKeySpec);
            KeyAgreement agreement = KeyAgreement.getInstance("DH");
            Log.d("Payara",myPublicKey.toString());
            Log.d("Payara",publicKey.toString());
            DHPublicKey keys = (DHPublicKey)publicKey;
            keys.getParams().getG();
            DHPublicKey keys1 = (DHPublicKey)myPublicKey;
            keys1.getParams().getG();

            System.out.println(keys.getParams().getG());
            System.out.println(keys1.getParams().getG());
            System.out.println(keys.getParams().getP());
            System.out.println(keys1.getParams().getP());

            //System.out.println(publicK.getFormat());
            agreement.init(myPrivateKey);
            agreement.doPhase(publicKey, true);
            secretKey = agreement.generateSecret();
            final byte[] shortenedKey = new byte[16]; //16 works with 1024


            System.arraycopy(secretKey, 0, shortenedKey, 0, shortenedKey.length);
            secretKey = shortenedKey;
            for(byte i : shortenedKey){
                //System.out.println("BYTE: "+i);
            }
            System.out.println(secretKey.length);

            //sendMessage(secretKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
           // Log.d("Payara" , "cause is " + e.);
        } catch (InvalidKeySpecException ex) {
            ex.printStackTrace();
           // Logger.getLogger(Diffie.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    public byte[] sendMessage(byte[] message){


        try {
            final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "AES");
            final Cipher cipher  = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            final byte[] encryptedMessage = cipher.doFinal(message);
            return encryptedMessage;
            //receiveMessage(secretKey, encryptedMessage);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String receiveMessage( byte[] encryptedMessage){
        try {

            // You can use Blowfish or another symmetric algorithm but you must adjust the key size.
            final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "AES");
            final Cipher        cipher  = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            String secretMessage = new String(cipher.doFinal(encryptedMessage));
            System.out.println("HELLOE "+ secretMessage);
            Log.d("Payara","message is "+ secretMessage);
            return secretMessage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
