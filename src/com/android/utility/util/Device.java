
package com.android.utility.util;

import java.io.File;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * This class collect various device information
 * 
 * @author Minhaz Rafi Chowdhury
 * @status Incomplete
 */
public class Device {
    private static final String TAG = Device.class.getSimpleName();
    private final Context mContext;
    private final TelephonyManager teleManager;

    public Device(Context context) {
        mContext = context;
        teleManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * This method will return device IMEI no
     * 
     * @return device IMEI as string or null if not found.
     */
    public String getIMEI() {
        return teleManager.getDeviceId();
    }

    /**
     * This method will return MAC of NIC
     * 
     * @return device NIC MAC as string
     */
    public static String getWiFiMAC(Context context) {

        final WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiMan.isWifiEnabled())
        {
            return null;
        }
        final WifiInfo wifiInf = wifiMan.getConnectionInfo();
        final String macAddr = wifiInf.getMacAddress();
        if (macAddr != null)
        {
            return wifiInf.getMacAddress();
        }

        return null;
    }

    /**
     * Retrieve Mobile Operator name from
     * 
     * @return
     */
    public String getMobileOperator() {

        return teleManager.getSimOperatorName();
    }

    public String getAndroidId() {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Return SIM number from SIM. if unable to retrive
     * SIM number or failed return null.
     * 
     * @return SIM no or Null if failed
     */
    public String getSimNo() {
        // TODO Auto-generated method stub
        return teleManager.getSimSerialNumber();
    }

    public String getPhoneNo() {
        // TODO Auto-generated method stub
        return teleManager.getLine1Number();
    }

    @SuppressLint("NewApi")
    public boolean isLocationEnable() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            return Settings.Secure.getInt(mContext.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED, 0) != 0;
        }
        else
        {
            return Settings.Global.getInt(mContext.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    public static boolean isAmazonDevice() {
        return android.os.Build.MANUFACTURER.equals("Amazon")
                || android.os.Build.MODEL.startsWith("KF");
    }

    public static boolean isPlayServicesAvailable(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (status == ConnectionResult.SUCCESS)
        {
            return true;
        }
        return false;
    }

    public static long getDirectorySize(File file) {
        if (file == null)
        {
            throw new IllegalArgumentException();
        }

        if (file.exists())
        {
            long result = 0;
            File[] files = file.listFiles();
            if (files == null)
            {
                return result;
            }
            for (File current_file : files)
            {
                if (current_file.isDirectory())
                {
                    result += getDirectorySize(current_file);
                }
                else
                {
                    result += current_file.length();
                }
            }

            return result;
        }
        return 0;
    }

    public static String getBluetoothMAC(Context context) {
        BluetoothAdapter adapter;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            adapter = BluetoothAdapter.getDefaultAdapter();

        }
        else
        {
            adapter = (BluetoothAdapter) context.getSystemService(Context.BLUETOOTH_SERVICE);

        }
        if (adapter != null)
        {
            return adapter.getAddress();
        }
        return null;
    }

    public int getApiLevel() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

}
