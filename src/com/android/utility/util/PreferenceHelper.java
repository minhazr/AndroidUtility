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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.android.utility.log.Logger;

public class PreferenceHelper {
    private static final String PREF_NAME = PreferenceHelper.class.getSimpleName();
    private static final String TAG = PreferenceHelper.class.getSimpleName();
    private final SharedPreferences sharedPrefs;
    private final Editor prefsEditor;

    public PreferenceHelper(Context context) {
        this.sharedPrefs = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        this.prefsEditor = sharedPrefs.edit();
    }

    public void save(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value))
        {
            throw new IllegalArgumentException();
        }
        synchronized (prefsEditor)
        {
            prefsEditor.putString(key, value);
            prefsEditor.commit();
        }
    }

    public void save(String key, int value) {
        if (TextUtils.isEmpty(key))
        {
            throw new IllegalArgumentException();
        }
        synchronized (prefsEditor)
        {
            prefsEditor.putInt(key, value);
            prefsEditor.commit();
        }
    }

    public void save(String key, boolean value) {
        if (TextUtils.isEmpty(key))
        {
            throw new IllegalArgumentException();
        }
        synchronized (prefsEditor)
        {
            prefsEditor.putBoolean(key, value);
            prefsEditor.commit();
        }
    }

    public void save(String key, long value) {
        if (TextUtils.isEmpty(key))
        {
            throw new IllegalArgumentException();
        }
        synchronized (prefsEditor)
        {
            prefsEditor.putLong(key, value);
            prefsEditor.commit();
        }
    }

    public void save(String key, float value) {
        if (TextUtils.isEmpty(key))
        {
            throw new IllegalArgumentException();
        }
        synchronized (prefsEditor)
        {
            prefsEditor.putFloat(key, value);
            prefsEditor.commit();
        }
    }

    public String getString(String key) {
        if (TextUtils.isEmpty(key))
        {
            throw new IllegalArgumentException();
        }
        try
        {
            synchronized (sharedPrefs)
            {
                if (sharedPrefs.contains(key))
                {
                    return sharedPrefs.getString(key, "empty");
                }
            }
        }
        catch (ClassCastException cce)
        {
            Logger.d(TAG, cce.getMessage());
            return null;
        }
        return null;
    }

    public int getInt(String key) {
        if (TextUtils.isEmpty(key))
        {
            throw new IllegalArgumentException();
        }
        try
        {
            synchronized (sharedPrefs)
            {
                return sharedPrefs.getInt(key, 0);
            }
        }
        catch (ClassCastException cce)
        {
            Logger.d(TAG, cce.getMessage());
            return 0;
        }
    }

    public float getFloat(String key) {
        if (TextUtils.isEmpty(key))
        {
            throw new IllegalArgumentException();
        }
        try
        {
            synchronized (sharedPrefs)
            {
                return sharedPrefs.getFloat(key, 0);
            }
        }
        catch (ClassCastException cce)
        {
            Logger.d(TAG, cce.getMessage());
            return 0;
        }
    }

    public long getlong(String key) {
        if (TextUtils.isEmpty(key))
        {
            throw new IllegalArgumentException();
        }
        try
        {
            synchronized (sharedPrefs)
            {
                return sharedPrefs.getLong(key, 0);
            }
        }
        catch (ClassCastException cce)
        {
            Logger.d(TAG, cce.getMessage());
            return 0;
        }
    }

    public boolean getBoolean(String key) {
        if (TextUtils.isEmpty(key))
        {
            throw new IllegalArgumentException();
        }
        try
        {
            synchronized (sharedPrefs)
            {
                return sharedPrefs.getBoolean(key, false);
            }
        }
        catch (ClassCastException cce)
        {
            Logger.d(TAG, cce.getMessage());
            return false;
        }
    }

    public void reset() {
        synchronized (prefsEditor)
        {
            prefsEditor.clear();
            prefsEditor.commit();
        }
    }

}
