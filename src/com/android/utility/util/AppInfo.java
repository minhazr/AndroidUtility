
package com.android.utility.util;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.utility.log.LogModule;
import com.android.utility.log.Logger;

public class AppInfo {
    private static final String TAG = AppInfo.class.getSimpleName();
    private final Context mContext;

    public AppInfo(Context applicationContext) {
        // TODO Auto-generated constructor stub
        if (applicationContext == null)
        {
            throw new NullPointerException("Context can not be null");
        }
        mContext = applicationContext;
    }

    public static int getVersionCode(Context context) {

        try
        {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        }
        catch (NameNotFoundException e)
        {
            // Huh? Really?
            return 0;
        }
    }

    public String getVersionName() {

        try
        {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        }
        catch (NameNotFoundException e)
        {
            // Huh? Really?
            return null;
        }
    }

    public String getPackageName() {
        return mContext.getPackageName();
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        if ((context == null) || TextUtils.isEmpty(permission))
        {
            throw new IllegalArgumentException("Empty parameter");
        }
        boolean permission_granted = (PackageManager.PERMISSION_GRANTED == context
                .checkCallingOrSelfPermission(permission));
        Logger.d(LogModule.UTILITY, TAG, permission + " Available: " + permission_granted);
        return permission_granted;
    }

    public static String getAppSignature(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }
        Signature[] sigs;
        try
        {
            sigs = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES).signatures;
            StringBuilder builder = new StringBuilder();
            for (Signature sig : sigs)
            {
                builder.append(sig.hashCode());
            }
            return builder.toString();
        }
        catch (NameNotFoundException e)
        {
            Logger.d(TAG, e.getMessage());
        }
        return null;
    }

    public static Locale getAppLocale(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }
        return context.getResources().getConfiguration().locale;
    }

    public static boolean isGMSServiceInManifest(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }
        try
        {
            ApplicationInfo app_info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app_info.metaData;
            String gms_version = bundle.getString("com.google.android.gms.version");
            if (!TextUtils.isEmpty(gms_version))
            {
                return true;
            }
        }
        catch (NameNotFoundException e)
        {
            Logger.e(LogModule.UTILITY, TAG,
                    "Failed to load meta-data, NameNotFound: " + e.getMessage());
        }
        catch (NullPointerException e)
        {
            Logger.e(LogModule.UTILITY, TAG,
                    "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        return false;
    }

    /**
     * Read manifest file and return meta data value
     * <meta-data android:name="key" android:value="value" />
     * 
     * @param context Application Context
     * @param key android:name part of the meta data
     * @return null or the android:value part of the meta data
     */
    public static String getMetaData(final Context context, final String key) {
        if ((context == null) || TextUtils.isEmpty(key))
        {
            throw new IllegalArgumentException();
        }
        String result = null;
        try
        {
            final ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            final Bundle bundle = ai.metaData;
            result = bundle.getString(key);
        }
        catch (final NameNotFoundException e)
        {
            Logger.d(LogModule.UTILITY, TAG, e.getMessage());
        }
        catch (final NullPointerException e)
        {
            Logger.d(LogModule.UTILITY, TAG, e.getMessage());
        }
        return result;
    }
    /**
     * Return true if any activity can resolve that intent
     * @param context
     * @param intent
     * @return
     */
    
    public static  boolean isCallable(Context context , Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 
            PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

}
