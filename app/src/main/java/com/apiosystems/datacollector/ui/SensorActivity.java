package com.apiosystems.datacollector.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import com.apiosystems.datacollector.Logger.SensorLogger;
import com.apiosystems.datacollector.SensorClasses.DeviceOrientation;
import com.apiosystems.datacollector.util.Helper;
import com.apiosystems.datacollector.util.TextViewBackEvent;

import java.util.Timer;
import datacollector.apiosystems.com.datacollector.R;

public class SensorActivity extends Activity {

    private static TextViewBackEvent mTextView;
    private static Button mPhoneCallBtn;
    private static Button mGeneralHandlingBtn;
    private static Button mStartButton;
    private static Button mEndButton;
    private static Switch mSwitchButton;
    public static Timer timer ;
    public static final String LOG_TAG = "SENSOR_ACTIVITY";
    private static int BLUE ;
    public static int LTBLUE;
    public static int GREY;
    public static int WHITE;

    public static String deviceName = android.os.Build.MODEL;
    public static String deviceMan = android.os.Build.MANUFACTURER;

    public static SensorLogger mSensorLogger;
    public static boolean isDriver = true;
    public static boolean mStartLog = false;
    public static boolean mPhoneCallStarted = false;
    public static boolean mGeneralHandlingStarted = false;
    public static boolean mUserTexting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.data_capture);
        mTextView = (TextViewBackEvent) findViewById(R.id.textView);//Custom layout component
        mPhoneCallBtn = (Button) findViewById(R.id.phonecall_btn);
        mGeneralHandlingBtn = (Button) findViewById(R.id.generalhandling_btn);
        mStartButton = (Button) findViewById(R.id.StartButton);
        mEndButton   = (Button) findViewById(R.id.EndButton);
        mSwitchButton = (Switch) findViewById(R.id.switch_button);
        LTBLUE = getResources().getColor(R.color.LTBLUE);
        BLUE = getResources().getColor(R.color.BLUE);
        GREY = Color.DKGRAY;
        WHITE = Color.WHITE;

        disableView();
        DeviceOrientation.config = getResources().getConfiguration();
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mUserTexting) {
                    mUserTexting = true;
                    mTextView.setText("");
                    mSensorLogger.writeDataToFile("SE:Texting" +
                            Helper.NEW_LINE);
                    Log.i(LOG_TAG, "TEXTVIEW_CLICKED" + mUserTexting);
                }
            }
        });

        mSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    isDriver = true;
                    mSwitchButton.setText("User is : DRIVER ");
                    //mSensorLogger.writeDataToFile(Helper.DRIVER
                    //      + Helper.NEW_LINE);
                    Log.i(LOG_TAG, "DRIVER");
                } else {
                    isDriver = false;
                    mSwitchButton.setText("User is : PASSENGER ");
                    //mSensorLogger.writeDataToFile(Helper.PASSENGER
                    //        + Helper.NEW_LINE);
                    Log.i(LOG_TAG, "PASSENGER");
                }
            }
        });

        mPhoneCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPhoneCallStarted) {
                    startPhoneCallSnippet();
                    mSensorLogger.writeDataToFile("SE:PhoneCall"
                            + Helper.NEW_LINE);
                } else {
                    stopPhoneCallSnippet();
                    mSensorLogger.writeDataToFile("EE:PhoneCall"
                            + Helper.NEW_LINE);
                }
            }
        });

        mGeneralHandlingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mGeneralHandlingStarted) {
                    startGeneralHandlingSnippet();
                    mSensorLogger.writeDataToFile("SE:GeneralHandling"
                            + Helper.NEW_LINE);
                } else {
                    stopGeneralHandlingSnippet();
                    mSensorLogger.writeDataToFile("EE:GeneralHandling"
                            + Helper.NEW_LINE);
                }
            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartLog = true;
                enableView();
                mSensorLogger = new SensorLogger(getApplicationContext(), SensorActivity.this);
                Thread loggerThread = new Thread(new Runnable() {
                    /**
                     * Starts executing the active part of the class' code. This method is
                     * called when a thread is started that has been created with a class which
                     * implements {@code Runnable}.
                     */
                    @Override
                    public void run() {
                        mSensorLogger.startLogging(Helper.getPhoneName());
                        timer = new Timer();
                        timer.scheduleAtFixedRate(mSensorLogger,Helper.TIMER_DELAY,Helper.TIMER_PERIOD);
                    }
                });
                loggerThread.start();
            }
        });

        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPhoneCallSnippet();
                stopGeneralHandlingSnippet();
                disableView();
                mStartLog = false;
                mSensorLogger.stopLogging();
                timer.cancel();
                timer.purge();
            }
        });
    }

    private void startPhoneCallSnippet(){
        mPhoneCallStarted = true;
        mPhoneCallBtn.setBackgroundColor(BLUE);
        mPhoneCallBtn.setText("STOP PHONE CALL");
    }

    private void stopPhoneCallSnippet(){
        mPhoneCallStarted = false;
        mPhoneCallBtn.setBackgroundColor(LTBLUE);
        mPhoneCallBtn.setText("START PHONE CALL");
    }

    private void startGeneralHandlingSnippet(){
        mGeneralHandlingStarted = true;
        mGeneralHandlingBtn.setBackgroundColor(BLUE);
        mGeneralHandlingBtn.setText("STOP GENERAL HANDLING");

    }

    private void stopGeneralHandlingSnippet(){
        mGeneralHandlingStarted = false;
        mGeneralHandlingBtn.setBackgroundColor(LTBLUE);
        mGeneralHandlingBtn.setText("START GENERAL HANDLING");

    }

    private void enableView() {
        mTextView.setBackgroundColor(WHITE);
        mPhoneCallBtn.setBackgroundColor(LTBLUE);
        mGeneralHandlingBtn.setBackgroundColor(LTBLUE);
        mStartButton.setBackgroundColor(LTBLUE);
        mEndButton.setBackgroundColor(LTBLUE);

        mTextView.setEnabled(true);
        mTextView.setText(Helper.START_TEXTING);
        mSwitchButton.setEnabled(false);//Dont change Role while logging
        mPhoneCallBtn.setEnabled(true);
        mGeneralHandlingBtn.setEnabled(true);
        mStartButton.setEnabled(false);
        mEndButton.setEnabled(true);
    }

    private void disableView(){
        mTextView.setBackgroundColor(Color.GRAY);
        mPhoneCallBtn.setBackgroundColor(GREY);
        mGeneralHandlingBtn.setBackgroundColor(GREY);
        mStartButton.setBackgroundColor(GREY);
        mEndButton.setBackgroundColor(GREY);

        mTextView.setText("");
        mTextView.setEnabled(false);
        //mSwitchButton.setChecked(false);
        mSwitchButton.setEnabled(true);//Change Role before Logging
        mPhoneCallBtn.setEnabled(false);
        mGeneralHandlingBtn.setEnabled(false);
        mStartButton.setEnabled(true);
        mEndButton.setEnabled(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(mStartLog) {
            enableView();
        }
        if(mGeneralHandlingStarted){
            mGeneralHandlingBtn.setBackgroundColor(BLUE);
        }
        if (mPhoneCallStarted){
            mPhoneCallBtn.setBackgroundColor(BLUE);
        }
        Log.i(LOG_TAG,"ON_CONFIGURATION_CHANGED");
        DeviceOrientation.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    @Override
    protected void onDestroy() {
        /*if(mStartLog){
            mStartLog = false;
            mSensorLogger.stopLogging();
            timer.cancel();
        }*/
        super.onDestroy();
    }
}