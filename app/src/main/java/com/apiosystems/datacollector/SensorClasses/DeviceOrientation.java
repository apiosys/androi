package com.apiosystems.datacollector.SensorClasses;

import android.content.res.Configuration;
import android.util.Log;
import android.view.Display;

import com.apiosystems.datacollector.util.Helper;

/**
 * Created by Akshayraj on 5/15/15.
 */
public class DeviceOrientation {
    private static final String LANDSCAPE = "Orientation:Landscape";
    private static final String PORTRAIT = "Orientation:Portrait";
    private static final String UNKNOWN = "Orientation:Unknown";
    private static final String LOG_TAG = DeviceOrientation.class.getSimpleName();
    public  static String mDeviceOrientation = PORTRAIT;
    public static Configuration config = null;

    public DeviceOrientation(){
        mDeviceOrientation = getScreenOrientation();
    }

    public static void onConfigurationChanged(Configuration configuration) {
        config = configuration;
        // Checks the orientation of the screen
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mDeviceOrientation = LANDSCAPE;
        } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            mDeviceOrientation = PORTRAIT;
        }else{
            mDeviceOrientation = UNKNOWN;
        }
    }

    public String getScreenOrientation() {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT)                {
            return PORTRAIT; // Portrait Mode

        }else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return LANDSCAPE;   // Landscape mode
        }
        return UNKNOWN;
    }

    public static String getValuesStr(){
        Log.i(LOG_TAG, mDeviceOrientation);
        return mDeviceOrientation + Helper.SPACE;
    }

}
