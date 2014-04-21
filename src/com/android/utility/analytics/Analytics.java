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

package com.android.utility.analytics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

public class Analytics {
    private static final String TAG = Analytics.class.getSimpleName();
    private static Analytics INSTANCE;
    private static URI uri;
    private static BufferedWriter writer;

    public static Analytics getInstance() {
        synchronized (INSTANCE)
        {
            if (INSTANCE == null)
            {
                return INSTANCE = new Analytics();
            }
            return INSTANCE;
        }

    }

    public static void log(String tag, String message) {
        long time = System.currentTimeMillis();
        String data = tag + ": " + time + ":" + message;
        if (uri == null)
        {
            throw new IllegalArgumentException();
        }
        if (uri.getScheme().startsWith("http"))
        {

        }
        else if (uri.getScheme().startsWith("file"))
        {
            if (writer == null)
            {
                initFileWriter();

            }
            writeMessage(data);

        }
        else if (uri.getScheme().startsWith("ftp"))
        {

        }
    }

    private static void initFileWriter() {
        File file = new File(uri);
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
                writer = new BufferedWriter(new FileWriter(file));
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static void writeMessage(String message) {
        try
        {
            writer.write(message);
            writer.newLine();
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void messageWriter(Context context, String message) {
        String name = context.getClass().getSimpleName();
        long time = System.nanoTime();
        writeMessage(name + " : " + message + " : " + time);
    }

    public void activityCreate(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }
        messageWriter(context, "onCreate");
    }

    public void activityStart(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }
        messageWriter(context, "onStart");
    }

    public void activityResume(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }
        messageWriter(context, "onResume");
    }

    public void activityStop(Context context) {
        if (context == null)
        {
            throw new IllegalArgumentException();
        }
        messageWriter(context, "onStop");

    }

    public static void logEvent(Context context, View view, String event_name) {
        if ((context == null) || (view == null) || TextUtils.isEmpty(event_name))
        {
            throw new IllegalArgumentException();
        }
        String name = context.getResources().getResourceEntryName(view.getId());
        messageWriter(context, name + "onStop");
    }

    public void setURI(URI uri) {
        this.uri = uri;
    }

    public void clear() {

    }

}
