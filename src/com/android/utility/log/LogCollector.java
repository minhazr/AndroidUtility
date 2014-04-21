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

package com.android.utility.log;

import java.io.File;
import java.io.IOException;

import android.content.Context;

import com.android.utility.util.Storage;

public class LogCollector {
    private static Process process;
    private static boolean started;

    public static void stop() {
        if (process != null)
        {
            process.destroy();
        }
        started = false;
    }

    public static void collect(final Context context) {
        if (Storage.isExternalStorageWritable() && !started)
        {
            String fileName = "logcat_" + System.currentTimeMillis() + ".txt";
            File outputFile = new File(context.getExternalCacheDir(), fileName);
            try
            {
                process = Runtime.getRuntime().exec(
                        "logcat -f " + outputFile.getAbsolutePath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

}
