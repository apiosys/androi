package com.apiosystems.datacollector.SensorClasses;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by SatyaSri on 8/19/2014.
 */
public class OrientationSensor extends SensorBaseClass implements SensorEventListener {

    public Sensor mSensor;
    public Context mContext;
    public float values[];
    public SensorUpdateTaskHandler mTaskHandler;

    public OrientationSensor(Context context) {
        //TODO - Refactor the orientation sensor
        // Use: http://developer.android.com/reference/android/hardware/SensorManager.html#getOrientation%28float[],%20float[]%29
        // http://developer.android.com/reference/android/hardware/Sensor.html#TYPE_ORIENTATION
        super(context);
        this.mContext = context;
        if(isSensorAvailable(Sensor.TYPE_ORIENTATION, mContext)) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            if (mSensor != null) {
                mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
            }
        }
    }

    public void registerSensor(){
        if(mSensor!=null) {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    public void unregisterSensor(){
        if(mSensor!=null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(mTaskHandler != null) {
            if (mSensor.getType() == Sensor.TYPE_ORIENTATION) {
                values = new float[3];
                values[0] = (float) Math.toRadians(sensorEvent.values[0]);
                values[1] = (float) Math.toRadians(sensorEvent.values[1]);
                values[2] = (float) Math.toRadians(sensorEvent.values[2]);

                mTaskHandler.onUpdate(values);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public Sensor getSensor(){
        return this.mSensor;
    }

    public void setSensor(Sensor sensor){
        this.mSensor = sensor;
    }


    public SensorUpdateTaskHandler getTaskHandler() {
        return mTaskHandler;
    }

    public void setTaskHandler(SensorUpdateTaskHandler mTaskHandler) {
        this.mTaskHandler = mTaskHandler;
    }




}
