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

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.android.utility.log.Logger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * This is GMS location service.
 *
 * @author Minhaz Rafi Chowdhury
 *
 */
class GMSLocationHandler implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = GMSLocationHandler.class.getSimpleName();

    private LocationClient locationClient;
    private LocationRequest locationRequest;
    private boolean requestInProgress = false;
    private static final long LOCATION_UPDATE_MIN_TIME = LocationObservable.LOCATION_UPDATE_MIN_TIME;
    private LocationNotifier locationNotifier;

    GMSLocationHandler(Context context, LocationNotifier locationNotifier) {
        if ((context == null) || (locationNotifier == null)) {
            throw new NullPointerException();
        }
        locationClient = new LocationClient(context, this, this);
        locationRequest = LocationRequest.create();
        // Use high accuracy
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 0 seconds
        locationRequest.setInterval(LOCATION_UPDATE_MIN_TIME);
        // Set the fastest update interval to 0 second
        locationRequest.setFastestInterval(LOCATION_UPDATE_MIN_TIME);
        this.locationNotifier = locationNotifier;
    }

    @Override
    public void onLocationChanged(Location arg0) {
    	Logger.d(TAG, "Receive location update: " + this.toString());
        this.locationNotifier.notify(arg0);
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
    }

    @Override
    public void onConnected(Bundle arg0) {
    	Logger.d(TAG, "GMS service connected: " + this.toString());
        requestInProgress = true;
        locationClient.requestLocationUpdates(locationRequest, this);

    }

    public boolean isListenerRunning() {
        return this.requestInProgress;
    }

    void stop() {
    	Logger.d(TAG, "Service Stop: ");
        requestInProgress = false;
        if (locationClient.isConnected()) {
            locationClient.removeLocationUpdates(this);
        }
        locationClient.disconnect();
    }

    void start() {
    	Logger.d(TAG, "Service started: " + this.toString());
        requestInProgress = true;
        locationClient.connect();
    }

    @Override
    public void onDisconnected() {
    	Logger.d(TAG, "Service disconnected ");
        requestInProgress = false;
    }
}
