package com.apiosystems.datacollector.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.apiosystems.datacollector.SensorClasses.LocationSensor;
import com.apiosystems.datacollector.util.Helper;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognitionIntentService extends IntentService{
    private static final String LOG_TAG = ActivityRecognitionIntentService.class.getSimpleName();
    public static String ACTIVITY_NAME_KEY = "ACTIVITY_NAME_KEY";
    public static String ACTIVITY_CONFIDENCE_KEY = "ACTIVITY_CONFIDENCE_KEY";

    public ActivityRecognitionIntentService(){
        super("ActivityRecognitionIntentService");
    }

    protected void onHandleIntent(Intent intent){
        if(ActivityRecognitionResult.hasResult(intent)){
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            DetectedActivity mostProbAct = result.getMostProbableActivity();
            int confidence = mostProbAct.getConfidence();
            String mostProbActName = getActivityName(mostProbAct.getType());

            broadcastNewActivityRecognized(mostProbActName, confidence);
            Intent i = new Intent("SAVVY");
            i.putExtra("act", mostProbActName);
            i.putExtra("confidence", confidence);
        }else
            Log.i(Helper.LOG_TAG, LOG_TAG);
    }

    public static String getActivityName(int activityType){
        switch(activityType){

            case DetectedActivity.IN_VEHICLE:
                return "IN_VEHICLE";
            case DetectedActivity.ON_BICYCLE:
                return "ON_BICYCLE";
            case DetectedActivity.ON_FOOT:
                return "ON_FOOT";
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.UNKNOWN:
                return "UNKNOWN";
            case DetectedActivity.TILTING:
                return "TILTING";
        }
        return "UNKNOWN";
    }

    public void broadcastNewActivityRecognized(String activityName, int confidence){
        Intent serviceLauncher = new Intent(this, LocationSensor.class);
        serviceLauncher.putExtra(ACTIVITY_NAME_KEY, activityName);
        serviceLauncher.putExtra(ACTIVITY_CONFIDENCE_KEY, confidence);
        startService(serviceLauncher);
    }
}