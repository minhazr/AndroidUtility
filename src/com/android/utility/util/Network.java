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

package com.android.utility.util;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.Secure;

import com.android.utility.log.LogModule;
import com.android.utility.log.Logger;

/**
 * Helper to provide network information
 * 
 * @author Minhaz Rafi Chowdhury
 */
public class Network {
    private static final String TAG = Network.class.getSimpleName();

    public Network(Context context) {

    }

    /**
     * Return Aeroplane mode status
     * 
     * @param context Activity/Application Context
     * @return true if Aeroplane mode is on
     */
    @SuppressLint("NewApi")
    public static boolean isAeroplanModeOn(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        }
        else
        {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    @SuppressLint("NewApi")
    public boolean isWiFiEnable(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            return Secure.getInt(context.getContentResolver(), Secure.WIFI_ON, 0) != 0;
        }
        else
        {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.WIFI_ON, 0) != 0;
        }
    }

    /**
     * Check if data connection is available.
     * 
     * @param context
     * @return
     */
    public static boolean isDataConnectionAvailable(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return ((netInfo != null) && netInfo.isConnected());
    }

    public static boolean isReachable(String url_string) {
        boolean result = false;
        try
        {
            HttpGet request = new HttpGet(url_string);
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse response = httpClient.execute(request);

            int status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK)
            {
                result = true;
            }

        }
        catch (SocketTimeoutException e)
        {
            result = false; // this is somewhat expected
        }
        catch (ClientProtocolException e)
        {
            // TODO Auto-generated catch block
            Logger.e(LogModule.NETWORK, TAG, e.getMessage());
            result = false;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            Logger.e(LogModule.NETWORK, TAG, e.toString());
            result = false;
        }
        return result;
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return (mWifi.isConnected());
    }
}
