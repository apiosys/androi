package com.apiosystems.datacollector.SensorClasses;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.apiosystems.datacollector.util.Helper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Akshayraj on 5/5/15.
 */
public class LocationSensor implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    public static  double values[];
    private final static String LOG_TAG = "LOCATION_SENSOR";
    private Location mLastLocation;   // Google client to interact with Google API
    public GoogleApiClient mGoogleApiClient;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private Context mContext;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    public LocationSensor(Context context){
        this.mContext = context;
        buildGoogleApiClient();
    }

    //TODO - LOCATION SERVICES

    /**
     * Creating google api client object
     * */
    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private void updateGPS() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        values = new double[3];
        if (mLastLocation != null) {
            values[0] = mLastLocation.getLatitude();
            values[1] = mLastLocation.getLongitude();
            values[2] = mLastLocation.getSpeed();
        }
    }

    public double[] getValues(){
        return values;
    }

    public String getValuesStr(){
        String valuesstr;
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null) {
            valuesstr = String.valueOf(values[0]) + Helper.SPACE
                    + String.valueOf(values[1]) + Helper.SPACE
                    + String.valueOf(values[2]) + Helper.SPACE;
        }else{
            valuesstr = "- - - ";
        }
        return valuesstr;
    }

    public void registerSensor(){
        mRequestingLocationUpdates = true;
        startLocationUpdates();
    }

    public void unregisterSensor(){
        stopLocationUpdates();
    }
    @Override
    public void onConnected(Bundle bundle) {
        updateGPS();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

    }

    @Override
    public void onLocationChanged(Location location) {
        updateGPS();
    }

    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorPendingIntent(resultCode, mContext ,
                        PLAY_SERVICES_RESOLUTION_REQUEST);
            }
            return false;
        }
        return true;
    }

    public void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            startLocationUpdates();
            Log.i(LOG_TAG, "Periodic location updates started!");

        } else {
            mRequestingLocationUpdates = false;
            stopLocationUpdates();
            Log.i(LOG_TAG, "Periodic location updates stopped!");
        }
    }

    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    protected void startLocationUpdates() {
    /*    if (checkPlayServices()) {
            createLocationRequest();
        }TODO
    */
        createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

}
