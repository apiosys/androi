package com.apiosystems.datacollector.SensorClasses;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.apiosystems.datacollector.util.Helper;

public class LocationAndroid implements LocationListener {
    private static final String LOCATION_PROVIDER = android.location.LocationManager.NETWORK_PROVIDER;
    public static LocationManager mLocationManager = null;
    public static Location mLocation = null;
    public static final String LOG_TAG = "LOCATION_SENSOR2";
    public static double values[] = { -1, -1, -1 };
    public static long locTime = -1;
    public static float horaccu = 0.0f;
    public static float veraccu = 0.0f;
    public static float speed = 0.0f;
    public static double alt = 0.0f;

    public static final long TIME_INTERVAL_BETWEEN_LOCATION_UPDATES = 30;
    public static final float DISTANCE_BETWEEN_LOCATION_UPDATES = 0.01f;
    Context mContext;

    public LocationAndroid(Context context){
        this.mContext = context;
        mLocationManager = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocation = mLocationManager.getLastKnownLocation(LOCATION_PROVIDER);
        if(mLocation != null){
            onLocationChanged(mLocation);
        }
    }

    public void registerSensor(){
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, TIME_INTERVAL_BETWEEN_LOCATION_UPDATES,
                DISTANCE_BETWEEN_LOCATION_UPDATES, this);
    }

    public void unregisterSensor(){
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        values = new double[2];
        values[0] = location.getLatitude();
        values[1] = location.getLongitude();
        alt = location.getAltitude();
        locTime = location.getTime();
        horaccu = location.getAccuracy();
        veraccu = 0.0f;
        speed   = location.getSpeed();
    }

    public double[] getValues(){
        return values;
    }
    public void setValues(double[] values) {
        this.values = values;
    }

    public String getLatLongAlt(){
        String valuesstr;
        if(mLocationManager.isProviderEnabled(LOCATION_PROVIDER)){
            valuesstr = String.valueOf(values[0]) + Helper.SPACE
                      + String.valueOf(values[1]) + Helper.SPACE
                      + String.valueOf(alt) + Helper.SPACE ;
        }else{
            valuesstr = "0 0 0 ";
        }
        return valuesstr;
    }

    public String getLocMeta(){
        String locmeta;
        if(mLocationManager.isProviderEnabled(LOCATION_PROVIDER)){
            locmeta = String.valueOf(locTime) + Helper.SPACE
                    + String.valueOf(horaccu) + Helper.SPACE
                    + Helper.DASH + Helper.SPACE //Vertical Accuracy
                    + String.valueOf(speed) + Helper.SPACE
                    + String.valueOf(0.0) + Helper.SPACE;
        }else{
            locmeta = "0 0 0 0 0 ";
        }
        return locmeta;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        //TODO
    }

    @Override
    public void onProviderDisabled(String provider) {
        //TODO
    }
}
