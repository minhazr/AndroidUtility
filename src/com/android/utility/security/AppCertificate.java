
package com.android.utility.security;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

import com.android.utility.log.LogModule;
import com.android.utility.log.Logger;

public class AppCertificate {

    private static final String TAG = AppCertificate.class.getSimpleName();
    private static final String HASH_CIPHER = "SHA1";

    public static int getHash(Context context) {
        Signature[] sigs;
        try
        {
            sigs = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
            for (final Signature sig : sigs)
            {
                return sig.hashCode();
            }
        }
        catch (final NameNotFoundException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }

        return -1;

    }

    public static String getFingerprint(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }
        X509Certificate certificate = getCertificate(context);
        if (certificate == null)
        {
            return null;
        }
        try
        {
            MessageDigest md = MessageDigest.getInstance(HASH_CIPHER);
            byte[] encoded = md.digest(certificate.getEncoded());
            return toHexString(encoded);

        }
        catch (CertificateEncodingException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        catch (NoSuchAlgorithmException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }

        return null;

    }

    public static String toHexString(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static X509Certificate getCertificate(Context context) {
        Signature[] signatures = null;
        try
        {
            signatures = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
        }
        catch (NameNotFoundException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
            return null;
        }

        byte[] certificate_byte = signatures[0].toByteArray();

        InputStream input = new ByteArrayInputStream(certificate_byte);

        CertificateFactory factory = null;
        try
        {
            factory = CertificateFactory.getInstance("X509");

        }
        catch (CertificateException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
            return null;

        }
        X509Certificate certificate = null;
        try
        {
            certificate = (X509Certificate) factory.generateCertificate(input);
            return certificate;
        }
        catch (CertificateException e)
        {
            Logger.d(LogModule.SECURITY, TAG, e.getMessage());
        }
        return null;
    }

}
