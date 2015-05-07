package com.apiosystems.datacollector.ui;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apiosystems.datacollector.Logger.SensorLogger;
import com.apiosystems.datacollector.SensorClasses.LocationSensor;
import com.apiosystems.datacollector.SensorClasses.SensorBaseClass;
import com.apiosystems.datacollector.util.Helper;

import java.util.List;
import java.util.Timer;
import java.util.logging.Logger;

import datacollector.apiosystems.com.datacollector.R;

public class SensorActivity extends Activity implements Runnable {

    private EditText mEditText;
    private Button mStartButton;
    private Button mEndButton;
    private Button mClearButton;
    private static TextView mGyrTextView;
    private static TextView mGtyTextView;
    private static TextView mOriTextView;
    private static TextView mAccTextView;
    private static TextView mMagTextView;

    public static Timer timer ;
    public static final String LOG_TAG = "SENSOR_ACTIVITY";

    public SensorLogger mSensorLogger;
    public String mExperimentName;
    public boolean mStartLog = false;
    public LocationSensor mLocationSensor ;
    Thread Logger;
    public static  boolean mThreadFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.i(LOG_TAG,"Before super.onCreate()");
        super.onCreate(savedInstanceState);

        //Log.i(LOG_TAG,"OnCreate");
        setContentView(R.layout.data_capture);
        if(mLocationSensor == null){
            Log.i(LOG_TAG, "LOCATION_SENSOR_NULL");
            mLocationSensor = new LocationSensor(this);
        }

        if (mLocationSensor.checkPlayServices()) {
            mLocationSensor.buildGoogleApiClient();
        }

        mEditText    = (EditText) findViewById(R.id.EditText);
        mStartButton = (Button) findViewById(R.id.StartButton);
        mEndButton   = (Button) findViewById(R.id.EndButton);
        mClearButton = (Button) findViewById(R.id.ClearButton);

        mGyrTextView = (TextView) findViewById(R.id.gyrtextview);
        mGyrTextView.setText("- - - ");
        mGtyTextView = (TextView) findViewById(R.id.gtytextview);
        mGtyTextView.setText("- - - ");
        mOriTextView = (TextView) findViewById(R.id.oritextview);
        mOriTextView.setText("- - - ");
        mAccTextView = (TextView) findViewById(R.id.acctextview);
        mAccTextView.setText("- - - ");
        mMagTextView = (TextView) findViewById(R.id.magtextview);
        mMagTextView.setText("- - - ");

        //Log.i("Current Date : " , Helper.getCurrentDate());
        //Log.i("Current Time : " , Helper.getCurrentTime());

        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mEditText.setText("");
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartLog = true;
                mExperimentName = mEditText.getText().toString();
                //Log.i("EXPERIMENT_NAME",mExperimentName);
                Toast.makeText(getApplicationContext(),mExperimentName + " STARTED :-) ", Toast.LENGTH_SHORT).show();
                mSensorLogger = new SensorLogger(getApplicationContext(), SensorActivity.this);
                mSensorLogger.startLogging(mExperimentName);
                timer = new Timer();
                timer.schedule(mSensorLogger, Helper.TIMER_DELAY, Helper.TIMER_PERIOD);
                //Logger = new Thread(new SensorActivity());
                //Logger.start();
            }
        });

        mEndButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(),mExperimentName + " STOPPED :-( ", Toast.LENGTH_SHORT).show();
                mSensorLogger.stopLogging();
                timer.cancel();
                timer.purge();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mStartLog){
            mStartLog = false;
            mSensorLogger.stopLogging();
            timer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public static void setGyrText(String text){
        mGyrTextView.setText(text);
    }
    public static void setGtyText(String text){
        mGtyTextView.setText(text);
    }
    public static void setOriText(String text){
        mOriTextView.setText(text);
    }
    public static void setAccText(String text){
        mAccTextView.setText(text);
    }
    public static void setMagText(String text){
        mMagTextView.setText(text);
    }
    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    @Override
    public void run() {
        timer = new Timer();
        timer.schedule(mSensorLogger, Helper.TIMER_DELAY, Helper.TIMER_PERIOD);
    }
}