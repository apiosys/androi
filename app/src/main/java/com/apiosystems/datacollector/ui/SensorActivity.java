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
import com.apiosystems.datacollector.SensorClasses.SensorBaseClass;
import com.apiosystems.datacollector.util.Helper;

import java.util.List;
import java.util.Timer;

import datacollector.apiosystems.com.datacollector.R;

public class SensorActivity extends Activity {

    private EditText mEditText;
    private Button mStartButton;
    private Button mEndButton;
    private Button mClearButton;
    private SensorManager mSensorManager;
    private List<Sensor> mSensorList;
    private static TextView mGyrTextView;
    private static TextView mGtyTextView;
    private static TextView mOriTextView;
    private static TextView mAccTextView;
    private static TextView mMagTextView;
    private Button mOkBtn;

    public static Timer timer ;
    private static final String LOG_TAG = "SENSOR_ACTIVITY";

    public SensorLogger mSensorLogger;
    private String mExperimentName;
    private boolean mFlagExpName = false;
    private boolean mStartLog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG,"Before super.onCreate()");
        super.onCreate(savedInstanceState);

        Log.i(LOG_TAG,"OnCreate");
        setContentView(R.layout.data_capture);

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

        Log.i("Current Date : " , Helper.getCurrentDate());
        Log.i("Current Time : " , Helper.getCurrentTime());

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
                // TODO Auto-generated method stub
                mStartLog = true;
                mExperimentName = mEditText.getText().toString();
                Log.i("EXPERIMENT_NAME",mExperimentName);
                Toast.makeText(getApplicationContext(),mExperimentName + " STARTED :-) ", Toast.LENGTH_SHORT).show();
                mSensorLogger = new SensorLogger(getApplicationContext());
                mSensorLogger.startLogging(mExperimentName);
                timer = new Timer();
                timer.schedule(mSensorLogger, 0, 30);
            }
        });

        mEndButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO Auto-generated method stub
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


    @Override
    protected void onResume() {
        super.onResume();
    }

}