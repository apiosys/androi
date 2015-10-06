package com.apiosystems.datacollector.Logger;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.apiosystems.datacollector.SensorClasses.AccelerometerSensor;
import com.apiosystems.datacollector.SensorClasses.DeviceOrientation;
import com.apiosystems.datacollector.SensorClasses.GravitySensor;
import com.apiosystems.datacollector.SensorClasses.GyroscopeSensor;
import com.apiosystems.datacollector.SensorClasses.LinearAccelerometerSensor;
import com.apiosystems.datacollector.SensorClasses.LocationSensor;
import com.apiosystems.datacollector.SensorClasses.LocationSensor2;
import com.apiosystems.datacollector.SensorClasses.MagnetometerSensor;
import com.apiosystems.datacollector.SensorClasses.OrientationSensorKitkat;
import com.apiosystems.datacollector.SensorClasses.PressureSensor;
import com.apiosystems.datacollector.SensorClasses.ProximitySensor;
import com.apiosystems.datacollector.SensorClasses.RawMagnetometerSensor;
import com.apiosystems.datacollector.SensorClasses.RotationSensor;
import com.apiosystems.datacollector.service.ApioActivityRecognitionService;
import com.apiosystems.datacollector.ui.SensorActivity;
import com.apiosystems.datacollector.util.Helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TimerTask;

public class SensorLogger extends TimerTask {

    public static final String LOG_TAG = "SENSOR_LOGGER";
    public static final String DIRECTORY_APIO_DATA_CAPTURE = "/ApioDataCapture/";
    public static boolean mFlagOn = false;

    public static AccelerometerSensor mAccSensor = null;
    public static GyroscopeSensor mGyrSensor = null;
    public static MagnetometerSensor mMagSensor = null;
    public static GravitySensor mGtySensor = null;
    public static LinearAccelerometerSensor mLinAccSensor = null;
    public static RawMagnetometerSensor mRawMagSensor = null;
    public static OrientationSensorKitkat mOriSensor = null;
    public static LocationSensor mLocSensor = null;
    public static LocationSensor2 mLocSensor2 = null;
    public static ProximitySensor mProxSensor = null;
    public static DeviceOrientation mDeviceOrientation = null;
    public static PressureSensor mPressureSensor = null;
    public static ApioActivityRecognitionService mActRecognition = null;
    public static RotationSensor mRotSensor = null;//Used for Attitude
    public static Context mContext;

    public FileWriter mFileWriter = null;
    public BufferedWriter mBufferedWriter = null;
    public File mExperimentFile = null;

    String gyrValuesStr = null;
    String gtyValuesStr = null;
    String oriValuesStr = null;
    String accValuesStr = null;
    String magValuesStr = null;
    String locValuesStr = null;
    String laccValuesStr= null;
    String rmagValuesStr= null;
    String rawaccValuesStr = null;
    String activityrecognition_confidence_activity = null;
    String proxValuesStr = null;
    String actrecogValuesStr = null;
    String deviceorientation = null;
    String rotValuesStr = null ;//attitude
    String locmetaValuesStr = null;
    String pressureValuesStr = null;

    public SensorLogger(Context context, Activity activity){
        mContext = context;
        this.mAccSensor = new AccelerometerSensor(mContext);
        this.mGyrSensor = new GyroscopeSensor(mContext);
        this.mMagSensor = new MagnetometerSensor(mContext);
        this.mOriSensor = new OrientationSensorKitkat(mContext);
        this.mGtySensor = new GravitySensor(mContext);
        this.mLocSensor = new LocationSensor(mContext, activity);
        this.mLocSensor2 = new LocationSensor2(mContext, activity);
        this.mRotSensor = new RotationSensor(mContext);
        this.mProxSensor = new ProximitySensor(mContext);
        this.mActRecognition = new ApioActivityRecognitionService(mContext);//Activity
        this.mDeviceOrientation = new DeviceOrientation();
        this.mLinAccSensor = new LinearAccelerometerSensor(mContext);
        this.mRawMagSensor = new RawMagnetometerSensor(mContext);
        this.mPressureSensor = new PressureSensor(mContext);
    }

    public void startLogging(String fileName) {
        mFlagOn = true;
        Helper.setTimeOnStart(System.currentTimeMillis());
        mExperimentFile = getFile(fileName);
        initializeBufferedWriter();
        if(!SensorActivity.isDriver) {
            writeDataToFile(Helper.PASSENGER + Helper.NEW_LINE);
        }else{
            writeDataToFile(Helper.DRIVER + Helper.NEW_LINE);
        }
        registerSensors();
    }

    public void writeDataToFile(String content){
        try{
            mBufferedWriter.append(content);
        }catch(IOException ioe){
            Log.i(LOG_TAG, "IOE in writeToDataFile()");
        }
    }

    private void registerSensors() {
        mAccSensor.registerSensor();
        mGyrSensor.registerSensor();
        mMagSensor.registerSensor();
        mOriSensor.registerSensor();
        mGtySensor.registerSensor();
        mLocSensor.registerSensor();
       // mLocSensor2.registerSensor();
        mProxSensor.registerSensor();
        mRotSensor.registerSensor();
        mLinAccSensor.registerSensor();
        mRawMagSensor.registerSensor();
        mPressureSensor.registerSensor();
    }

    public String getSensorValues(){
        gyrValuesStr = mGyrSensor.getValuesStr();
        gtyValuesStr = mGtySensor.getValuesStr();
        oriValuesStr = mOriSensor.getValuesStr();
        accValuesStr = mLinAccSensor.getValuesStr();
        magValuesStr = mMagSensor.getValuesStr();
        locValuesStr = mLocSensor.getValuesStr();
        proxValuesStr = mProxSensor.getDistanceStr();
        actrecogValuesStr = mActRecognition.getValuesStr();
        laccValuesStr = mLinAccSensor.getValuesStr();
        rmagValuesStr = mRawMagSensor.getValuesStr();
        rotValuesStr = mRotSensor.getValuesStr() ;
        locmetaValuesStr = mLocSensor.getLocMeta();
        deviceorientation = DeviceOrientation.getValuesStr();
        rawaccValuesStr = mAccSensor.getValuesStr();
        activityrecognition_confidence_activity = Helper.DASH + Helper.SPACE;
        pressureValuesStr = mPressureSensor.getValuesStr();

        String mSensorValues =    magValuesStr//1,2,3
                                + accValuesStr//4,5,6
                                + locValuesStr//7,8,9
                                + gyrValuesStr//10,11,12
                                + rotValuesStr//13,14,15
                                + rmagValuesStr//16,17,18
                                + locmetaValuesStr//19,20,21,22,23
                                + rawaccValuesStr//24,25,26
                                + actrecogValuesStr
                                + proxValuesStr//28
                                + deviceorientation//29
                                + pressureValuesStr
                                + Helper.NEW_LINE ;
        return mSensorValues;
    }

    public void stopLogging(){
        mFlagOn = false;
        Helper.setTimeOnStart(0);
        closeWriters();
        unregisterSensors();
        Log.i(LOG_TAG,"LOGGING STOPPED");
    }

    private void closeWriters() {
        try{
            mBufferedWriter.close();
            mFileWriter.close();
        }catch (IOException ioe){
            Log.i(LOG_TAG,"IOE while closing Writers");
        }
    }

    private void unregisterSensors() {
        mAccSensor.unregisterSensor();
        mGyrSensor.unregisterSensor();
        mMagSensor.unregisterSensor();
        mOriSensor.unregisterSensor();
        mGtySensor.unregisterSensor();
        mLocSensor.unregisterSensor();
        mLocSensor2.unregisterSensor();
        mRotSensor.unregisterSensor();
        mProxSensor.unregisterSensor();
        mLinAccSensor.unregisterSensor();
        mRawMagSensor.unregisterSensor();
        mPressureSensor.unregisterSensor();
    }

    public void initializeBufferedWriter(){
        try{
            mFileWriter = new FileWriter(mExperimentFile.getAbsoluteFile());
            mBufferedWriter = new BufferedWriter(mFileWriter);
        }catch (IOException ioexception){
            Log.i(LOG_TAG,"IOEXCEPTION in initializeBufferedWriter()",ioexception);
        }
    }

    public File getFile(String fileName){
        File sensorLogDir = null;
        File sensorLogFile = null;
        if (isExternalStorageWritable()) {
            sensorLogDir = new File(Environment.getExternalStorageDirectory()
                    + DIRECTORY_APIO_DATA_CAPTURE + Helper.getCurrentDate());
            Log.i(LOG_TAG, "sensorLogDir : " + sensorLogDir.getAbsolutePath());
            if (!sensorLogDir.exists()) {
                sensorLogDir.mkdirs();
            }
            sensorLogFile = new File(sensorLogDir, fileName + "_" +
                    Helper.getCurrentDateTimeForFile() + "_log" + ".txt");
            if (!sensorLogFile.exists()) {
                try{
                    sensorLogFile.createNewFile();
                }catch(IOException ioe){
                    Log.i(LOG_TAG, "sensorLogFile : " + ioe);
                }
                Log.i(LOG_TAG,"sensorLogFile Created");
            }
        } else {
            Log.i(LOG_TAG, "Cannot Write to External Storage");
        }
        return sensorLogFile;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * The task to run should be specified in the implementation of the {@code run()}
     * method.
     */
    @Override
    public void run() {
        if(mFlagOn){
            String log =  Helper.getAbsoluteTime() + Helper.SPACE
                        + Helper.getElapsedTime() + Helper.SPACE
                        + getSensorValues();
            writeDataToFile(log);
            //Log.i(LOG_TAG, " : " + Helper.getCurrentDateTimeinMillis());
        }
    }
}
