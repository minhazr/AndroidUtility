
package com.android.utility.security;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {
    private static final String HASH_CIPHER = "SHA-512";

    public static String toHash(String text) throws CipherException {
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
        if ((digest != null) & (digest.length > 0))
        {
            return new String(digest);
        }
        return null;

    }

    public static String toHash(BufferedInputStream in, int bufferSize) throws CipherException {
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
            in.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            throw new CipherException(e.getMessage());
        }

        byte[] hash = null;
        hash = new byte[digest.getDigestLength()];
        hash = digest.digest();
        return new String(hash);
    }

}
