
package com.android.utility.util;

import java.io.File;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public class Storage {

    @SuppressLint("NewApi")
    public static long getAvailableSpace() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize, availableBlocks;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();

        }
        else
        {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();

        }
        return availableBlocks * blockSize;

    }

    @SuppressLint("NewApi")
    public static long getTotalSpace() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize, blocksCount;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            blockSize = stat.getBlockSize();
            blocksCount = stat.getBlockCount();
        }
        else
        {
            blockSize = stat.getBlockSizeLong();
            blocksCount = stat.getBlockCountLong();
        }
        return blocksCount * blockSize;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            return true;
        }
        return false;
    }

}
