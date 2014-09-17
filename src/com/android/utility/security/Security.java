/*
 * Copyright (C) 2014 Minhaz Rafi Chowdhury.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.android.utility.security;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.utility.log.LogModule;
import com.android.utility.log.Logger;
import com.android.utility.util.AppInfo;

public class Security {
    public static final String HASH_CIPHER = "SHA-512";
    public static final String HMAC_CIPHER = "HmacSHA1";
    private static final byte[] salt = { (byte) 0xA4, (byte) 0x0B, (byte) 0xC8,
        (byte) 0x34, (byte) 0xD6, (byte) 0x95, (byte) 0xF3, (byte) 0x13 };
    private static final String TAG = Security.class.getSimpleName();

    public static byte[] toHash(String text) {
        String hashAlgoName = HASH_CIPHER;
        MessageDigest hash = null;
        try
        {
            hash = MessageDigest.getInstance(hashAlgoName);
        }
        catch (NoSuchAlgorithmException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }

        try
        {
            hash.update(text.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        byte[] digest = hash.digest();
        return digest;

    }

    public static byte[] toHash(BufferedInputStream in, int bufferSize) {
        MessageDigest digest = null;
        try
        {
            digest = MessageDigest.getInstance(HASH_CIPHER);
        }
        catch (NoSuchAlgorithmException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        byte[] buffer = new byte[bufferSize];
        int sizeRead = -1;
        try
        {
            while ((sizeRead = in.read(buffer)) != -1)
            {
                digest.update(buffer, 0, sizeRead);
            }
        }
        catch (IOException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        finally
        {
            try
            {
                in.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        byte[] hash = null;
        hash = new byte[digest.getDigestLength()];
        hash = digest.digest();
        return hash;
    }

    public static String getHmac(Context context, String value, String key) {
        Mac mac = null;
        try
        {
            mac = Mac.getInstance("HmacSHA1");
        }
        catch (NoSuchAlgorithmException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")),
                mac.getAlgorithm());
        try
        {
            mac.init(secret);

        }
        catch (InvalidKeyException e)
        {
            // TODO Auto-generated catch block
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }

        byte[] digest = mac.doFinal(value.getBytes());
        return Base64.encodeToString(digest, Base64.DEFAULT);
    }

    public static SecretKey generateKey(char[] passphraseOrPin){
        // Number of PBKDF2 hardening rounds to use. Larger values increase
        // computation time. You should select a value that causes computation
        // to take >100ms.
        final int iterations = 1000;

        // Generate a 256-bit key
        int outputKeyLength;
        try
        {
            outputKeyLength = Cipher.getMaxAllowedKeyLength("AES");
            Log.d("minhaz", "key length available "+outputKeyLength);
        }
        catch (NoSuchAlgorithmException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
            outputKeyLength=128;
        }
        if (outputKeyLength>256)
            outputKeyLength=256;

        SecretKeyFactory secretKeyFactory = null;
        SecretKey secretKey = null;
        try
        {
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
            secretKey = secretKeyFactory.generateSecret(keySpec);
        }
        catch (NoSuchAlgorithmException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        catch (InvalidKeySpecException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
       
        

        return secretKey;
    }
  


    public static byte[] encrypt(byte[] key, byte[] clear) {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = null;
        byte[] encrypted = null;
        try
        {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encrypted = cipher.doFinal(clear);
        }
        catch (NoSuchAlgorithmException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        catch (NoSuchPaddingException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        catch (InvalidKeyException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        catch (IllegalBlockSizeException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        catch (BadPaddingException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        return encrypted;
    }

    public static byte[] decrypt(byte[] key, byte[] encrypted) {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher;
        byte[] decrypted = null;
        try
        {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            decrypted = cipher.doFinal(encrypted);
        }
        catch (NoSuchAlgorithmException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        catch (NoSuchPaddingException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        catch (InvalidKeyException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        catch (IllegalBlockSizeException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        catch (BadPaddingException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
       

        return decrypted;
    }
}
