package com.apiosystems.datacollector.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.apiosystems.datacollector.util.Helper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
/**
 * Created by Akshayraj on 5/13/15.
 */
public class ApioActivityRecognitionService extends Service {

    public static final String LOG_TAG = ApioActivityRecognitionService.class.getSimpleName();
    public static String activityName = "2";//Unknown
    public static String confidencestr = "0";//LowConfidence

    private static final int ACTIVITY_RECOGNITION_REQUEST_INTERVAL = 1000;
    private GoogleApiClient mGoogleApiClient = null;
    Context mContext;

    public ApioActivityRecognitionService() {
        super();
    }

    public ApioActivityRecognitionService(Context context){
        super();
        //mContext = context;
        //Log.i(LOG_TAG, "ApioActivityRecognitionService(Context context)");
        this.mContext = context;
        initActivityRecognition();
        //Log.i(LOG_TAG, "ACTIVITY_APICLIENT");
        connectPlayServices();
    }

    private void connectPlayServices() {
        mGoogleApiClient.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i(LOG_TAG, "Service onStartCommand");
        if (intent != null) {
            if (intent.hasExtra(ActivityRecognitionIntentService.ACTIVITY_NAME_KEY)) {
                activityName = intent.getStringExtra(ActivityRecognitionIntentService.ACTIVITY_NAME_KEY);
                confidencestr = intent.getStringExtra(ActivityRecognitionIntentService.ACTIVITY_CONFIDENCE_KEY);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public static String getActivityName() {
        return activityName + Helper.SPACE;
    }

    public static String getConfidencestr() {
        return confidencestr + Helper.SPACE;
    }

    public String getValuesStr(){
        String valuesstr = getConfidencestr() + ":" + getActivityName() + Helper.SPACE;
        //Log.i(LOG_TAG,valuesstr);
        return valuesstr;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initActivityRecognition() {
        Log.d(LOG_TAG, "initActivityRecognition()");
        GoogleApiClient.ConnectionCallbacks connectionCallback = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                Log.d(LOG_TAG, "onConnected()");
                /*
                * Create the PendingIntent that Location Services uses
                * to send activity recognition updates back to this app.
                */
                Intent intent = new Intent(mContext, ActivityRecognitionIntentService.class);
                PendingIntent activityRecognitionPendingIntent =
                        PendingIntent.getService(mContext, 0, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, ACTIVITY_RECOGNITION_REQUEST_INTERVAL, activityRecognitionPendingIntent);
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d(LOG_TAG, "onDisconnected(): reason: " + i);
            }
        };

        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {

            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                Log.d(LOG_TAG, "onConnectionFailed(): connectionResult: " + ((connectionResult != null) ? connectionResult.getErrorCode(): "null result"));
            }
        };

        // Updated to the new GoogleApiClient (Play Services 6.5+)
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(connectionCallback)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .build();

        mGoogleApiClient.connect();
    }
}
