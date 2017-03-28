package com.example.daniel.projectchatapp;


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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
/**
 * Created by Daniel on 28/03/2017.
 */

public class EncryptionManager {
    PublicKey myPublicKey;
    PrivateKey myPrivateKey;
    byte[] secretKey;
    public void generateKey(){
        KeyPairGenerator keyGenerator;
        //FinalpublicKey.getEncoded();
        //FinalpublicKey.getFormat();

        try {

            keyGenerator = KeyPairGenerator.getInstance("DH");
            keyGenerator.initialize(512); //1024 works with 16
            KeyPair pair = keyGenerator.generateKeyPair();

            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();
            myPrivateKey = privateKey;
            myPublicKey = publicKey;
        } catch (NoSuchAlgorithmException ex) {
            //Logger.getLogger(Diffie.class.getName()).log(Level.SEVERE, null, ex);
        }

    }



    public PrivateKey getPriv(){
        return myPrivateKey;
    }

    public PublicKey getPub(){
        return myPublicKey;
    }


    public void genSecret(PrivateKey privateK, PublicKey publicK){
        try {
            KeyAgreement agreement = KeyAgreement.getInstance("DH");
            agreement.init(privateK);
            agreement.doPhase(publicK, true);
            secretKey = agreement.generateSecret();
            final byte[] shortenedKey = new byte[16]; //16 works with 1024

            System.arraycopy(secretKey, 0, shortenedKey, 0, shortenedKey.length);
            secretKey = shortenedKey;
            System.out.println(secretKey.length);

            //sendMessage(secretKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

    }

    public void diffie2(){

    }

    public byte[] sendMessage(String message){


        try {
            final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "AES");
            final Cipher cipher  = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            final byte[] encryptedMessage = cipher.doFinal(message.getBytes());
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

    public void receiveMessage( byte[] encryptedMessage){
        try {

            // You can use Blowfish or another symmetric algorithm but you must adjust the key size.
            final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "AES");
            final Cipher        cipher  = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            String secretMessage = new String(cipher.doFinal(encryptedMessage));
            System.out.println("HELLOE "+ secretMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
