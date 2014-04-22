
package com.android.utility.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class PreferenceHelper {
    private static final String PREF_NAME = "com.mcription.cache";
    private final SharedPreferences sharedPrefs;
    private final Editor prefsEditor;

    PreferenceHelper(Context context) {
        this.sharedPrefs = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        this.prefsEditor = sharedPrefs.edit();
    }

    public synchronized void save(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value))
        {
            throw new IllegalArgumentException();
        }
        prefsEditor.putString(key, value);
        prefsEditor.commit();

    }

    public synchronized String getValue(String key) {
        if (TextUtils.isEmpty(key))
        {
            throw new IllegalArgumentException();
        }
        return sharedPrefs.getString(key, null);

    }

    public synchronized void reset() {
        if (prefsEditor != null)
        {
            prefsEditor.clear();
            prefsEditor.commit();
        }
    }

}
