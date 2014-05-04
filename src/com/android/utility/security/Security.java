
package com.android.utility.security;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.util.Base64;

import com.android.utility.log.LogModule;
import com.android.utility.log.Logger;

public class Security {
    private static final String HASH_CIPHER = "SHA-512";
    private static final String TAG = Security.class.getSimpleName();

    public static byte[] toHash(String text) throws CipherException {
        String hashAlgoName = HASH_CIPHER;
        MessageDigest hash = null;
        try
        {
            hash = MessageDigest.getInstance(hashAlgoName);
        }
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            throw new CipherException(e.getMessage());
        }

        try
        {
            hash.update(text.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new CipherException(e.getMessage());
        }
        byte[] digest = hash.digest();
        return digest;

    }

    public static byte[] toHash(BufferedInputStream in, int bufferSize) throws CipherException {
        MessageDigest digest = null;
        try
        {
            digest = MessageDigest.getInstance(HASH_CIPHER);
        }
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            // TODO Auto-generated catch block
            throw new CipherException(e.getMessage());
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
}
