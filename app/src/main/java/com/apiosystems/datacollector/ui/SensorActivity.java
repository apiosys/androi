package com.apiosystems.datacollector.ui;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apiosystems.datacollector.Logger.SensorLogger;

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
    private TextView mSensorDataTextView;
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

        mEditText = (EditText) findViewById(R.id.EditText);
        mStartButton = (Button) findViewById(R.id.StartButton);
        mEndButton = (Button) findViewById(R.id.EndButton);
        mClearButton = (Button) findViewById(R.id.ClearButton);
        mSensorDataTextView = (TextView)findViewById(R.id.textView1);

        displaySensors();

        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mFlagExpName){
                    mFlagExpName = true ;
                    mEditText.setText("");
                }else{
                    mEditText.setText(mExperimentName);
                }
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
                Toast.makeText(getApplicationContext(),mExperimentName + " STARTED :-) ", Toast.LENGTH_SHORT);
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
                Toast.makeText(getApplicationContext(),mExperimentName + " STOPPED :-( ", Toast.LENGTH_SHORT);
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

    public void displaySensors(){
        mSensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder data = new StringBuilder();

        data.append("SENSORS ON DEVICE :\n");
        for(Sensor sensor: sensorList){
            data.append(sensor.getName() + "\n");
        }
        mSensorDataTextView.setText(data);
    }
}