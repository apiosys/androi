package com.apiosystems.datacollector.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by jaredsheehan on 12/15/14.
 */
public class ActivityRecognitionIntentService extends IntentService {

    enum SAL_ENUMS
    {
        DEVICE_DETECTION_UNKNOWN,//Android unknown - iOS unknown
        DEVICE_DETECTION_CYCLING,//Android onBicycle iOS cycling
        DEVICE_DETECTION_WALKING,//Andriod onfoot or walking - iOS walking
        DEVICE_DETECTION_RUNNING,//Andriod running - iOS running
        DEVICE_DETECTION_CARMODE,//Andriod inVehicle - iOS automotive
        DEVICE_DETECTION_NO_MOVEMENT,//Andriod STILL - iOS stationary
        DEVICE_DETECTION_CAR_AND_STATIONARY //for debug purpose
    };  //SALDeviceHWDetections
    private static String LOG_TAG = ActivityRecognitionIntentService.class.getSimpleName();
    public static String ACTIVITY_NAME_KEY = "ACTIVITY_NAME_KEY";
    public static String ACTIVITY_CONFIDENCE_KEY = "ACTIVITY_CONFIDENCE_KEY";

    public ActivityRecognitionIntentService(){
        super(ActivityRecognitionIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // If the incoming intent contains an update
        if (ActivityRecognitionResult.hasResult(intent)) {
            // Get the update
            ActivityRecognitionResult result =
                    ActivityRecognitionResult.extractResult(intent);
            // Get the most probable activity
            DetectedActivity mostProbableActivity =
                    result.getMostProbableActivity();
            /*
             * Get the probability that this activity is the
             * the user's actual activity
             */
            int confidence = getConfidenceRange(mostProbableActivity.getConfidence());

            String confidencestr = String.valueOf(confidence);//getConfidenceRange(confidence);
            /*
             * Get an integer describing the type of activity
             */
            int activityType = getSALEnumFromType(mostProbableActivity.getType());
            final String activityName = String.valueOf(activityType); //getNameFromType(activityType);

            /*
             * At this point, you have retrieved all the information
             * for the current update. You can display this
             * information to the user in a notification, or
             * send it to an Activity or Service in a broadcast
             * Intent.
             */
//            final String log = "ActivityRecognitionResult has result: activityName: " + activityName + " confidence: " + confidencestr;
            //Log.d(LOG_TAG, log);
            broadcastNewActivityRecognized(activityName, confidencestr);
        } else {
            /*
             * This implementation ignores intents that don't contain
             * an activity update. If you wish, you can report them as
             * errors.
             */
            Log.d(LOG_TAG, "ActivityRecognitionResult has no result");
        }
    }

    private int getSALEnumFromType(int activityType) {
        switch(activityType) {
            case DetectedActivity.IN_VEHICLE:
                int inVehicle = SAL_ENUMS.DEVICE_DETECTION_CARMODE.ordinal();
                return inVehicle;
            case DetectedActivity.ON_BICYCLE:
                int inBicycle = SAL_ENUMS.DEVICE_DETECTION_CYCLING.ordinal();
                return inBicycle;
            case DetectedActivity.ON_FOOT:
                int onFoot = SAL_ENUMS.DEVICE_DETECTION_WALKING.ordinal();
                return onFoot;
            case DetectedActivity.RUNNING:
                int running = SAL_ENUMS.DEVICE_DETECTION_RUNNING.ordinal();
                return running;
            case DetectedActivity.STILL:
                int still = SAL_ENUMS.DEVICE_DETECTION_NO_MOVEMENT.ordinal();
                return still;
            case DetectedActivity.UNKNOWN:
                int unknown = SAL_ENUMS.DEVICE_DETECTION_UNKNOWN.ordinal();
                return unknown;
            case DetectedActivity.WALKING:
                int walking = SAL_ENUMS.DEVICE_DETECTION_WALKING.ordinal();
                return walking;
        }
        return SAL_ENUMS.DEVICE_DETECTION_UNKNOWN.ordinal();
    }

    private int getConfidenceRange(int i){
        int range;
        i = Math.abs(i);
        if(i <= 50){
            range = 0;
        }else if(i <= 60){
            range = 1;
        }else if(i <= 100){
            range = 2;
        }else{
            range = 0;
        }
         return range;
    }

    private void broadcastNewActivityRecognized(String activityName, String confidence){
        Intent serviceLauncher = new Intent(this, ApioActivityRecognitionService.class);
        serviceLauncher.putExtra(ACTIVITY_NAME_KEY, activityName);
        serviceLauncher.putExtra(ACTIVITY_CONFIDENCE_KEY, confidence);
        startService(serviceLauncher);
    }
}