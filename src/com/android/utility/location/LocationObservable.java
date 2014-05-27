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

package com.android.utility.location;

import java.util.Date;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.android.utility.log.LogModule;
import com.android.utility.log.Logger;
import com.android.utility.util.AppInfo;
import com.android.utility.util.Device;

/**
 * This is main location class uses. It uses GMS location
 * service and standard location interface to
 * Determine best location.
 * 
 * @author Minhaz Rafi Chowdhury
 */
public class LocationObservable extends Observable implements LocationNotifier {

    private static final String TAG = LocationObservable.class.getSimpleName();

    /**
     * right now minimum time is set to zero as we are just interested to one
     * good location.
     * for continuous location update we must set it to a lower frequency level
     * other wise this will create
     * severe performance issue.
     **/
    public static final long LOCATION_UPDATE_MIN_TIME = 0;
    public static final float LOCATION_UPDATE_MIN_DISTANCE = 0;
    public static final float REQUESTED_ACCURACY_IN_METERS = 100.0f;
    public static final long LOCATION_UPDATE_MAX_DELTA_THRESHOLD = 1000 * 60 * 1;
    public static final long TIMEOUT_MILLIS_DEFAULT = 30 * 1000;

    private Location mLastLocation;
    private LocationManager manager;
    private StandardLocationHandler standardLocationHandler;
    private GMSLocationHandler gmsLocationHandler;

    /**
     * TODO : Timeout implementation has to be reviewed
     * mTimeoutMillis is max time application wants to wait for a getting FIRST
     * location update.
     * This is different from LOCATION_UPDATE_MIN_TIME
     */

    public LocationObservable(Context context) {

        if (AppInfo.isGMSServiceInManifest(context) && Device.isPlayServicesAvailable(context))
        {
            gmsLocationHandler = new GMSLocationHandler(context, this);
            Logger.d(LogModule.LOCATION, TAG, "Yes play service is available");
        }
        else
        {
            manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            standardLocationHandler = new StandardLocationHandler(this);
            Logger.d(LogModule.LOCATION, TAG, "Oopps no play service");
        }
    }

    private void updateLocation(Location current_location) {
        Logger.d(LogModule.LOCATION, TAG, "Location update receievd and Last Locaiton is empty");
        updateLocation(mLastLocation, current_location);
    }

    /**
     * Test current location update to see if it matches current location
     * requirements set by business
     * 
     * @param current_location
     */
    private void updateLocation(Location last_location, Location current_location) {
        if ((current_location == null) && (last_location == null))
        {
            Logger.d(LogModule.LOCATION, TAG, "current_location = " + current_location);
            Logger.d(LogModule.LOCATION, TAG, "last_location = " + last_location);
            Logger.d(
                    LogModule.LOCATION,
                    TAG,
                    "Both current_location and last_location are null, so we do not send location update to application");
            return;
        }
        if ((current_location != null) && (last_location == null))
        {
            onDesiredLocationChanged(current_location);
            return;

        }
        else if ((current_location == null) && (last_location != null))
        {
            onDesiredLocationChanged(last_location);
            return;
        }

        long now = new Date().getTime();
        long locationUpdateDelta = now - current_location.getTime();
        // Checking if location is in my desired time threshold
        boolean currentLocationIsInTimeThreshold = locationUpdateDelta <= LOCATION_UPDATE_MAX_DELTA_THRESHOLD;
        // is accuracy meet our criteria
        boolean currentLocationIsAcurate = current_location.getAccuracy() <= REQUESTED_ACCURACY_IN_METERS;

        // Checking if last location is in my desired time threshold
        boolean lastLocationIsInTimeThreshold = (now - last_location.getTime()) <= LOCATION_UPDATE_MAX_DELTA_THRESHOLD;
        // is accuracy meet our criteria
        boolean lastLocationIsAcurate = last_location.getAccuracy() <= REQUESTED_ACCURACY_IN_METERS;

        boolean currentLocationBetter = current_location.getAccuracy() <= last_location
                .getAccuracy()
                ? true
                : false;
        // if current location is better and it has all desired requirement
        if (currentLocationBetter && currentLocationIsInTimeThreshold && currentLocationIsAcurate)
        {
            onDesiredLocationChanged(current_location);
            // if last location is better and it has all desired requirement
        }
        else if (!currentLocationBetter && lastLocationIsAcurate && lastLocationIsInTimeThreshold)
        {
            onDesiredLocationChanged(last_location);
        }
        else
        {
            return;
        }
    }

    synchronized public void onDesiredLocationChanged(Location location) {
        Logger.d(LogModule.LOCATION, TAG, "found my desired location: notifying observer now"
                + location);
        mLastLocation = location;
        setChanged();
        notifyObservers(location);
    }

    public void setTimeOut(long time) {
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                Logger.d(LogModule.LOCATION, TAG, "Executing location time out");
                unregister();
                onDesiredLocationChanged(null);
            }
        }, time, TimeUnit.MILLISECONDS);
    }

    /**
     * Check if current location meet accuracy and time
     * threshold requirement
     * 
     * @param location
     * @return true if location has accuracy
     */
    public boolean isAccurate(Location location) {
        if ((location != null) && location.hasAccuracy()
                && (location.getAccuracy() <= REQUESTED_ACCURACY_IN_METERS))
        {
            long locationUpdateDelta = new Date().getTime() - location.getTime();
            if (locationUpdateDelta < LOCATION_UPDATE_MAX_DELTA_THRESHOLD)
            {
                Logger.d(LogModule.LOCATION, TAG, "Location is accurate: " + location.toString());
                return true;
            }
        }
        Logger.d(LogModule.LOCATION, TAG, "Location is not accurate: " + String.valueOf(location));
        return false;
    }

    /**
     * Return true if listener is already listen to location update
     * 
     * @return
     */
    public boolean isListenerRunning() {
        if (gmsLocationHandler != null)
        {
            return gmsLocationHandler.isListenerRunning();
        }
        else
        {
            return standardLocationHandler.isListenerRunning();
        }
    }

    public void register() {
        Logger.d(LogModule.LOCATION, TAG, "Registering this location listener: " + this.toString());
        if (manager == null)
        {
            gmsLocationHandler.start();
        }
        else
        {
            standardLocationHandler.register(manager);
        }

    }

    public void unregister() {

        if ((manager == null) && (gmsLocationHandler != null) && isListenerRunning())
        {
            Logger.d(LogModule.LOCATION, TAG, "Unregistering GMS location: ");
            gmsLocationHandler.stop();
        }
        else
        {
            Logger.d(LogModule.LOCATION, TAG, "Unregistering this Standard listener: ");
            standardLocationHandler.unregister(manager);
        }
    }

    @Override
    public void notify(Location location) {
        updateLocation(location);

    }

    @Override
    public void notify(Location location, Location location2) {
        updateLocation(location, location2);
    }

}
