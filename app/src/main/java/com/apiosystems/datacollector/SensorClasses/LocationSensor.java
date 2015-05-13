package com.apiosystems.datacollector.SensorClasses;


import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.apiosystems.datacollector.service.ActivityRecognitionIntentService;
import com.apiosystems.datacollector.util.Helper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Akshayraj on 5/5/15.
 */
public class LocationSensor extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int ACTIVITY_RECOGNITION_REQUEST_INTERVAL = 10000;
    private LocationRequest mLocationRequest;
    public static double lat = 0.0;
    public static double lon = 0.0;
    public static long locTime = -1;
    public static float horaccu = 0.0f;
    public static float veraccu = 0.0f;
    public static float speed = 0.0f;
    public static double alt = 0.0f;

    public static GoogleApiClient mGoogleApiClient;
    public static final String LOG_TAG = LocationSensor.class.getSimpleName();
    public static Context mContext;
    public static Activity mActivity;

    public LocationSensor(Context context, Activity activity){
        this.mContext = context;
        this.mActivity = activity;
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)//this refers to the Class which implements GoogleApiClient.ConnectionCallbacks interface
                .addOnConnectionFailedListener(this)//this refers to the Class which implements GoogleApiClient.OnConnectionFailedListener interface
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();
    }

    public static void connectPlayServices(){
        mGoogleApiClient.connect();
    }

    public void startActivityRecognition(){
        Intent intent = new Intent(LocationSensor.this, ActivityRecognitionIntentService.class);
        PendingIntent activityRecognitionPendingIntent =
                PendingIntent.getService(LocationSensor.this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, ACTIVITY_RECOGNITION_REQUEST_INTERVAL, activityRecognitionPendingIntent);
    }

    public void registerSensor(){
        connectPlayServices();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)        // 10 seconds, in milliseconds
                .setFastestInterval(LocationSensor2.TIME_INTERVAL_BETWEEN_LOCATION_UPDATES); // 1 second, in milliseconds
    }

    public void unregisterSensor(){
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(Helper.LOG_TAG, LOG_TAG + "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            // Blank for a moment...
        }
        else {
            startActivityRecognition();
            handleNewLocation(location);
        };
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(Helper.LOG_TAG, LOG_TAG + "Location services suspended. Please reconnect.");
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(mActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(Helper.LOG_TAG, LOG_TAG + "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    private void handleNewLocation(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        alt = location.getAltitude();
        locTime = location.getTime();
        horaccu = location.getAccuracy();
        veraccu = 0.0f;
        speed   = location.getSpeed();
        //Log.d(Helper.LOG_TAG , LOG_TAG + location.toString());
    }

    public String getValuesStr(){
        String valuesstr;
        if(mGoogleApiClient.isConnected()){
            valuesstr = lat + Helper.SPACE
                    + lon + Helper.SPACE
                    + String.valueOf(alt) + Helper.SPACE ;
        }else{
            valuesstr = "- - - ";
        }
        //Log.i(Helper.LOG_TAG, "Lat, Long");
        return valuesstr;
    }

    public String getLocMeta(){
        String locmeta;
        if(mGoogleApiClient.isConnected()){
            locmeta = String.valueOf(locTime) + Helper.SPACE
                    + String.valueOf(horaccu) + Helper.SPACE
                    + Helper.DASH + Helper.SPACE //Vertical Accuracy
                    + String.valueOf(speed) + Helper.SPACE
                    + String.valueOf(0.0) + Helper.SPACE;
        }else{
            locmeta = "- - - - - ";
        }
        //Log.i(Helper.LOG_TAG, "Loc MetaData");
        return locmeta;
    }

    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p/>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG, "Service onBind");
        return null;
    }
}
