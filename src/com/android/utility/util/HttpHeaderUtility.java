
package com.android.utility.util;

import java.io.UnsupportedEncodingException;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

/**
 * algorithim:ALGORITHIM;credential:USERNAME/PASSWORD/PRODUCTID/HASH/TIME/LOCAL/
 * COUNTRY/CLIENT;signature:VALUE
 */
public class HttpHeaderUtility {
    private static final String TAG = HttpHeaderUtility.class.getSimpleName();

    private final static String ALGORITHIM = "algorithim";
    private final static String CREDENTIAL = "credential";
    private final static String USER_NAME = "user_name";
    private final static String PASSWORD = "password";
    private final static String PRODUCT_ID = "product_id";
    private final static String HASH = "hash";
    private final static String TIME = "time";
    private final static String LOCAL = "local";
    private final static String COUNTRY = "country";
    private final static String CLIENT = "client";
    private final static String SIGNATURE = "signature";

    private String algorithim;
    private String username;
    private String password;
    private String product_id;
    private String hash;
    private long time;
    private String local;
    private String country;
    private String client;
    private String signature;

    public HttpHeaderUtility(String base64_string) {
        if (TextUtils.isEmpty(base64_string))
        {
            throw new IllegalArgumentException();
        }
        byte[] encoded = Base64.decode(base64_string, Base64.DEFAULT);
        try
        {
            process(new String(encoded, "UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void process(final String header) {
        Log.d(TAG, header);
        String[] header_data = header.split(";");
        for (String data : header_data)
        {
            Log.d(TAG, "split by colon :" + header);
            if (data.startsWith(ALGORITHIM))
            {
                CharSequence algorithim = data.subSequence(data.indexOf(':') + 1, data.length());
                if (!TextUtils.isEmpty((algorithim)))
                {

                    this.setAlgorithim(algorithim.toString());

                }
            }
            else if (data.startsWith(CREDENTIAL))
            {
                String[] credentals = data.split("/");
                for (String credential : credentals)
                {
                    if (credential.startsWith(USER_NAME))
                    {
                        if (!TextUtils.isEmpty(data.subSequence(credential.indexOf(':') + 1,
                                credential.length())))
                        {
                            this.setUsername(data.subSequence(credential.indexOf(':') + 1,
                                    credential.length())
                                    .toString());
                        }
                    }
                    else if (credential.startsWith(PASSWORD))
                    {
                        if (!TextUtils.isEmpty(credential.subSequence(credential.indexOf(':') + 1,
                                credential.length())))
                        {
                            this.setPassword(credential.subSequence(credential.indexOf(':') + 1,
                                    credential.length())
                                    .toString());
                        }
                    }
                    else if (credential.startsWith(PRODUCT_ID))
                    {
                        if (!TextUtils.isEmpty(credential.subSequence(credential.indexOf(':') + 1,
                                credential.length())))
                        {
                            this.setProduct_id(credential.subSequence(credential.indexOf(':') + 1,
                                    credential.length())
                                    .toString());
                        }
                    }
                    else if (credential.startsWith(HASH))
                    {
                        if (!TextUtils.isEmpty(credential.subSequence(credential.indexOf(':') + 1,
                                credential.length())))
                        {
                            this.setHash(credential.subSequence(data.indexOf(':') + 1,
                                    credential.length())
                                    .toString());
                        }
                    }
                    else if (credential.startsWith(TIME))
                    {
                        if (!TextUtils.isEmpty(credential.subSequence(credential.indexOf(':') + 1,
                                credential.length())))
                        {
                            this.setTime(Long.parseLong(credential.subSequence(
                                    credential.indexOf(':') + 1,
                                    credential.length()).toString()));
                        }
                    }
                    else if (credential.startsWith(LOCAL))
                    {
                        if (!TextUtils.isEmpty(credential.subSequence(credential.indexOf(':') + 1,
                                credential.length())))
                        {
                            this.setLocal(credential.subSequence(credential.indexOf(':') + 1,
                                    credential.length())
                                    .toString());
                        }
                    }
                    else if (credential.startsWith(COUNTRY))
                    {
                        Log.d(TAG,
                                "setting up country :"
                                        + credential.subSequence(credential.indexOf(':') + 1,
                                                credential.length()));
                        if (!TextUtils.isEmpty(credential.subSequence(credential.indexOf(':') + 1,
                                credential.length())))
                        {
                            this.setCountry(credential.subSequence(credential.indexOf(':') + 1,
                                    credential.length())
                                    .toString());
                        }
                    }
                    else if (credential.startsWith(CLIENT))
                    {
                        if (!TextUtils.isEmpty(credential.subSequence(credential.indexOf(':') + 1,
                                credential.length())))
                        {
                            this.setClient(credential.subSequence(credential.indexOf(':') + 1,
                                    credential.length())
                                    .toString());
                        }
                    }

                }
            }
            else if (data.startsWith(SIGNATURE))
            {
                CharSequence signature = data.subSequence(data.indexOf(':') + 1, data.length());
                if (!TextUtils.isEmpty((signature)))
                {
                    this.setSignature(signature.toString());

                }
            }
        }

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getAlgorithim() {
        return algorithim;
    }

    public void setAlgorithim(String algorithim) {
        this.algorithim = algorithim;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public static String toBase64(String algorithim, String user,
            String password, String product_id, String hash, long time, String local,
            String country, String client, String signature) {
        StringBuilder builder = new StringBuilder();

        if (!TextUtils.isEmpty(algorithim))
        {
            builder.append(ALGORITHIM + ":" + algorithim + ";");
        }
        builder.append(CREDENTIAL + ":");
        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password))
        {
            builder.append(USER_NAME + ":" + user);
            builder.append("/" + PASSWORD + ":" + password);
        }
        if (!TextUtils.isEmpty(product_id) && !TextUtils.isEmpty(hash))
        {
            builder.append("/" + PRODUCT_ID + ":" + product_id);
            builder.append("/" + HASH + ":" + hash);
        }
        if (time > 0)
        {
            builder.append("/" + time);
        }
        if (!TextUtils.isEmpty(local))
        {
            builder.append("/" + LOCAL + ":" + local);
        }
        if (!TextUtils.isEmpty(country))
        {
            builder.append("/" + COUNTRY + ":" + country);
        }
        if (!TextUtils.isEmpty(client))
        {
            builder.append("/" + CLIENT + ":" + client);
        }
        if (!TextUtils.isEmpty(signature))
        {
            builder.append(";" + SIGNATURE + ":" + signature);
        }
        return Base64.encodeToString(builder.toString().getBytes(), Base64.DEFAULT);
    }

    public static String toBase64(String header) {
        if (TextUtils.isEmpty(header))
        {

        }
        byte[] data = null;
        try
        {
            data = header.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.DEFAULT);
    }
}
