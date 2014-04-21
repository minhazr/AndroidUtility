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

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.android.utility.log.Logger;

/**
 * This is Standard location service.
 *
 *
 */
class StandardLocationHandler implements LocationListener {
    private static final String TAG = StandardLocationHandler.class.getSimpleName();

    private boolean requestInProgress = false;
    private static final long LOCATION_UPDATE_MIN_TIME = LocationObservable.LOCATION_UPDATE_MIN_TIME;
    private static final float LOCATION_UPDATE_MIN_DISTANCE = LocationObservable.LOCATION_UPDATE_MIN_DISTANCE;

    private LocationNotifier locationNotifier;

    public StandardLocationHandler(LocationNotifier locationNotifier) {
        this.locationNotifier = locationNotifier;
    }

    @Override
    public void onLocationChanged(Location location) {
    	Logger.d(TAG, "Receive location update: " + this.toString());
        this.locationNotifier.notify(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public boolean isListenerRunning() {
        return this.requestInProgress;
    }

    public void register(LocationManager locationManager) {
    	Logger.d(TAG, "Registering this location listener: " + this.toString());

        requestInProgress = true;
        Location gpsLocation = null, networkLocation = null;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                                   LOCATION_UPDATE_MIN_TIME,
                                                   LOCATION_UPDATE_MIN_DISTANCE,
                                                   this);
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                                   LOCATION_UPDATE_MIN_TIME,
                                                   LOCATION_UPDATE_MIN_DISTANCE,
                                                   this);
            networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        Logger.d(TAG,"locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) = " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        Logger.d(TAG,"locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) = " + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));            
        Logger.d(TAG,"gpsLocation = " + gpsLocation);
        Logger.d(TAG,"networkLocation = " + networkLocation);
        this.locationNotifier.notify(gpsLocation, networkLocation);
    }

    public void unregister(LocationManager locationManager) {
    	Logger.d(TAG, "Unregistering this location listener: ");

        requestInProgress = false;
        locationManager.removeUpdates(this);
    }
}
