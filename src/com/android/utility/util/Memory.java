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

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug.MemoryInfo;

public class Memory {
    private static final String TAG = Memory.class.getSimpleName();

    public static int getDeviceHeapSize(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getMemoryClass();
    }

    public static int getAvailabeHeapMemory(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }
        int id = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        int pids[] = new int[1];
        pids[0] = id;
        int memory = 0;
        android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(pids);
        for (MemoryInfo memoryInfo : memoryInfoArray)
        {
            memory = memoryInfo.getTotalPrivateDirty() + memoryInfo.getTotalPss()
                    + memoryInfo.getTotalSharedDirty();
        }
        return memory;
    }

    public static long getTotalAvailableMemory(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        android.app.ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static String getTotalRAM() {
        RandomAccessFile reader = null;
        String load = null;
        try
        {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            close(reader);

        }
        return load;
    }

    private static void close(Closeable c) {
        if (c == null)
        {
            return;
        }
        try
        {
            c.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
