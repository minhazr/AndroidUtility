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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import android.net.TrafficStats;

import com.minhaz.utility.BuildConfig;

public final class Logger {

    private static final HashSet<LogModule> loggTypes =
            new HashSet<LogModule>(Arrays.asList(LogModule.UTILITY));
    private final static AtomicInteger counter = new AtomicInteger(0);

    private static AtomicBoolean flag = new AtomicBoolean();
    private static long timer;
    private static final ReentrantLock lock = new ReentrantLock();
    private static long dataSent = 0;

    private static final Set<LogModule> getLoggingTypes() {
        synchronized (loggTypes)
        {
            return Collections.unmodifiableSet(new HashSet<LogModule>(loggTypes));
        }
    }

    public static void logTime(String tag) {
        lock.lock();
        try
        {
            if (flag.get())
            {
                timer = System.nanoTime();
                flag.set(false);
            }
            else
            {
                if ((timer - System.nanoTime()) > 1000)
                {
                    timer = timer / 1000;
                    Logger.d(tag, "Time Log completed :" + timer + " ms");

                }
                flag.set(true);
                timer = 0;
            }
        }
        finally
        {
            if (lock.isLocked())
            {
                lock.unlock();
            }
        }

    }

    public static void logTime(String tag, boolean reset) {
        lock.lock();
        try
        {
            if (reset)
            {
                flag.set(true);
                timer = System.nanoTime();
            }
        }
        finally
        {
            if (lock.isLocked())
            {
                lock.unlock();
            }
        }

    }

    public static final void addLoggingTypes(LogModule type) {
        synchronized (loggTypes)
        {
            loggTypes.add(type);
        }
    }

    public static final void logData(String tag) {
        int id = android.os.Process.myPid();
        if (dataSent == 0)
        {
            dataSent = TrafficStats.getUidTxBytes(id);
        }
        else
        {
            Logger.d(tag, "Total Data Sent :" + (TrafficStats.getUidTxBytes(id) - dataSent)
                    + " bytes");
            dataSent = 0;
        }

    }

    /**
     * Certain logging behaviors are available for debugging beyond those that
     * should be
     * enabled in production.
     * Disables a particular extended logging behavior in the sdk.
     * 
     * @param type
     *            The LoggingBehavior to disable
     */
    public static final void removeLoggingTypes(LogModule type) {
        synchronized (loggTypes)
        {
            loggTypes.remove(type);
        }
    }

    /**
     * Certain logging behaviors are available for debugging beyond those that
     * should be
     * enabled in production.
     * Disables all extended logging behaviors.
     */
    public static final void clearLoggingTypes() {
        synchronized (loggTypes)
        {
            loggTypes.clear();
        }
    }

    /**
     * Certain logging behaviors are available for debugging beyond those that
     * should be
     * enabled in production.
     * Checks if a particular extended logging behavior is enabled.
     * 
     * @param type
     *            The LoggingBehavior to check
     * @return whether behavior is enabled
     */
    public static final boolean isLoggingEnabled(LogModule type) {
        synchronized (loggTypes)
        {
            return BuildConfig.DEBUG && loggTypes.contains(type);
        }
    }

    public static void v(String tag, String message) {
        String message_tag = (tag == null) ? new String("Subscription") : String.format(Locale.US,
                tag);
        Set<LogModule> types = getLoggingTypes();
        for (LogModule type : types)
        {
            if (isLoggingEnabled(type))
            {
                android.util.Log.v(message_tag, buildMessage(message));
            }
        }
    }

    public synchronized static void d(String tag, String message) {
        String message_tag = (tag == null) ? new String("Subscription") : String.format(Locale.US,
                tag);
        Set<LogModule> types = getLoggingTypes();
        for (LogModule type : types)
        {
            if (isLoggingEnabled(type))
            {
                android.util.Log.d(message_tag, buildMessage(message));
            }
        }
    }

    public static void e(String tag, String message) {
        String message_tag = (tag == null) ? new String("Subscription") : String.format(Locale.US,
                tag);
        Set<LogModule> types = getLoggingTypes();
        for (LogModule type : types)
        {
            if (isLoggingEnabled(type))
            {
                android.util.Log.e(message_tag, buildMessage(message));
            }
        }
    }

    public static void i(String tag, String message) {
        String message_tag = (tag == null) ? new String("Subscription") : String.format(Locale.US,
                tag);
        Set<LogModule> types = getLoggingTypes();
        for (LogModule type : types)
        {
            if (isLoggingEnabled(type))
            {
                android.util.Log.i(message_tag, buildMessage(message));
            }
        }
    }

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String message) {
        String msg = (message == null) ? new String(Logger.class.getSimpleName()) : String.format(
                Locale.US, message);
        return String.format(Locale.US, "[%d] %d: %s",
                Thread.currentThread().getId(), counter.incrementAndGet(), msg);
    }

}
